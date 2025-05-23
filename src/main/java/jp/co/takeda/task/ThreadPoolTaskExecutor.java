package jp.co.takeda.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

/**
 * JavaBean that allows for configuring a JDK 1.5 {@link java.util.concurrent.ThreadPoolExecutor} in bean style (through its
 * "corePoolSize", "maxPoolSize", "keepAliveSeconds", "queueCapacity" properties), exposing it as a Spring
 * {@link org.springframework.core.task.TaskExecutor}. This is an alternative to configuring a ThreadPoolExecutor instance
 * directly using constructor injection, with a separate {@link ConcurrentTaskExecutor} adapter wrapping it.
 * 
 * <p>
 * For any custom needs, in particular for defining a {@link java.util.concurrent.ScheduledThreadPoolExecutor}, it is
 * recommended to use a straight definition of the Executor instance or a factory method definition that points to the JDK 1.5
 * {@link java.util.concurrent.Executors} class. To expose such a raw Executor as a Spring
 * {@link org.springframework.core.task.TaskExecutor}, simply wrap it with a {@link ConcurrentTaskExecutor} adapter.
 * 
 * <p>
 * <b>NOTE:</b> This class implements Spring's {@link org.springframework.core.task.TaskExecutor} interface as well as the JDK
 * 1.5 {@link java.util.concurrent.Executor} interface, with the former being the primary interface, the other just serving as
 * secondary convenience. For this reason, the exception handling follows the TaskExecutor contract rather than the Executor
 * contract, in particular regarding the {@link org.springframework.core.task.TaskRejectedException}.
 * 
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.core.task.TaskExecutor
 * @see java.util.concurrent.Executor
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see java.util.concurrent.ScheduledThreadPoolExecutor
 * @see java.util.concurrent.Executors
 * @see ConcurrentTaskExecutor
 */
public class ThreadPoolTaskExecutor extends CustomizableThreadFactory implements SchedulingTaskExecutor, Executor, BeanNameAware, InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private final Object poolSizeMonitor = new Object();

	private int corePoolSize = 1;

	private int maxPoolSize = Integer.MAX_VALUE;

	private int keepAliveSeconds = 60;

	@SuppressWarnings("unused")
	private boolean allowCoreThreadTimeOut = false;

	private int queueCapacity = Integer.MAX_VALUE;

	private ThreadFactory threadFactory = this;

	private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

	private boolean waitForTasksToCompleteOnShutdown = false;

	private boolean threadNamePrefixSet = false;

	private String beanName;

	private ThreadPoolExecutor threadPoolExecutor;

	private long awaitTermination;

	/**
	 * タスクの終了を待つ秒数を設定する
	 * 
	 * @param awaitTermination タスクの終了を待つ秒数
	 */
	public void setAwaitTermination(long awaitTermination) {
		this.awaitTermination = awaitTermination;
	}

	/**
	 * Set the ThreadPoolExecutor's core pool size. Default is 1.
	 * <p>
	 * <b>This setting can be modified at runtime, for example through JMX.</b>
	 */
	public void setCorePoolSize(int corePoolSize) {
		synchronized (this.poolSizeMonitor) {
			this.corePoolSize = corePoolSize;
			if (this.threadPoolExecutor != null) {
				this.threadPoolExecutor.setCorePoolSize(corePoolSize);
			}
		}
	}

	/**
	 * Return the ThreadPoolExecutor's core pool size.
	 */
	public int getCorePoolSize() {
		synchronized (this.poolSizeMonitor) {
			return this.corePoolSize;
		}
	}

	/**
	 * Set the ThreadPoolExecutor's maximum pool size. Default is <code>Integer.MAX_VALUE</code>.
	 * <p>
	 * <b>This setting can be modified at runtime, for example through JMX.</b>
	 */
	public void setMaxPoolSize(int maxPoolSize) {
		synchronized (this.poolSizeMonitor) {
			this.maxPoolSize = maxPoolSize;
			if (this.threadPoolExecutor != null) {
				this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
			}
		}
	}

	/**
	 * Return the ThreadPoolExecutor's maximum pool size.
	 */
	public int getMaxPoolSize() {
		synchronized (this.poolSizeMonitor) {
			return this.maxPoolSize;
		}
	}

	/**
	 * Set the ThreadPoolExecutor's keep-alive seconds. Default is 60.
	 * <p>
	 * <b>This setting can be modified at runtime, for example through JMX.</b>
	 */
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		synchronized (this.poolSizeMonitor) {
			this.keepAliveSeconds = keepAliveSeconds;
			if (this.threadPoolExecutor != null) {
				this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
			}
		}
	}

	/**
	 * Return the ThreadPoolExecutor's keep-alive seconds.
	 */
	public int getKeepAliveSeconds() {
		synchronized (this.poolSizeMonitor) {
			return this.keepAliveSeconds;
		}
	}

	/**
	 * Specify whether to allow core threads to time out. This enables dynamic growing and shrinking even in combination with a
	 * non-zero queue (since the max pool size will only grow once the queue is full).
	 * <p>
	 * Default is "false". Note that this feature is only available on Java 6 or above. On Java 5, consider switching to the
	 * backport-concurrent version of ThreadPoolTaskExecutor which also supports this feature.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)
	 */
	public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}

	/**
	 * Set the capacity for the ThreadPoolExecutor's BlockingQueue. Default is <code>Integer.MAX_VALUE</code>.
	 * <p>
	 * Any positive value will lead to a LinkedBlockingQueue instance; any other value will lead to a SynchronousQueue instance.
	 * 
	 * @see java.util.concurrent.LinkedBlockingQueue
	 * @see java.util.concurrent.SynchronousQueue
	 */
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	/**
	 * Set the ThreadFactory to use for the ThreadPoolExecutor's thread pool.
	 * <p>
	 * Default is this executor itself (i.e. the factory that this executor inherits from). See
	 * {@link org.springframework.util.CustomizableThreadCreator}'s javadoc for available bean properties.
	 * 
	 * @see #setThreadPriority
	 * @see #setDaemon
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = (threadFactory != null ? threadFactory : this);
	}

	/**
	 * Set the RejectedExecutionHandler to use for the ThreadPoolExecutor. Default is the ThreadPoolExecutor's default abort
	 * policy.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor.AbortPolicy
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = (rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
	}

	/**
	 * Set whether to wait for scheduled tasks to complete on shutdown.
	 * <p>
	 * Default is "false". Switch this to "true" if you prefer fully completed tasks at the expense of a longer shutdown phase.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
	}

	public void setThreadNamePrefix(String threadNamePrefix) {
		super.setThreadNamePrefix(threadNamePrefix);
		this.threadNamePrefixSet = true;
	}

	public void setBeanName(String name) {
		this.beanName = name;
	}

	/**
	 * Calls <code>initialize()</code> after the container applied all property values.
	 * 
	 * @see #initialize()
	 */
	public void afterPropertiesSet() {
		initialize();
	}

	/**
	 * Creates the BlockingQueue and the ThreadPoolExecutor.
	 * 
	 * @see #createQueue
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		if (logger.isInfoEnabled()) {
			logger.info("Initializing ThreadPoolExecutor" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (!this.threadNamePrefixSet && this.beanName != null) {
			setThreadNamePrefix(this.beanName + "-");
		}
		BlockingQueue queue = createQueue(this.queueCapacity);
		this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, this.threadFactory,
			this.rejectedExecutionHandler);
		//		if (this.allowCoreThreadTimeOut) {
		//			// java1.6から追加されたメソッド
		//			// 設定上使用していないのでコメントアウト
		//			this.threadPoolExecutor.allowCoreThreadTimeOut(true);
		//		}
	}

	/**
	 * Create the BlockingQueue to use for the ThreadPoolExecutor.
	 * <p>
	 * A LinkedBlockingQueue instance will be created for a positive capacity value; a SynchronousQueue else.
	 * 
	 * @param queueCapacity the specified queue capacity
	 * @return the BlockingQueue instance
	 * @see java.util.concurrent.LinkedBlockingQueue
	 * @see java.util.concurrent.SynchronousQueue
	 */
	@SuppressWarnings("unchecked")
	protected BlockingQueue createQueue(int queueCapacity) {
		if (queueCapacity > 0) {
			return new LinkedBlockingQueue(queueCapacity);
		} else {
			return new SynchronousQueue();
		}
	}

	/**
	 * Return the underlying ThreadPoolExecutor for native access.
	 * 
	 * @return the underlying ThreadPoolExecutor (never <code>null</code>)
	 * @throws IllegalStateException if the ThreadPoolTaskExecutor hasn't been initialized yet
	 */
	public ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
		Assert.state(this.threadPoolExecutor != null, "ThreadPoolTaskExecutor not initialized");
		return this.threadPoolExecutor;
	}

	/**
	 * Implementation of both the JDK 1.5 Executor interface and the Spring TaskExecutor interface, delegating to the
	 * ThreadPoolExecutor instance.
	 * 
	 * @see java.util.concurrent.Executor#execute(Runnable)
	 * @see org.springframework.core.task.TaskExecutor#execute(Runnable)
	 */
	public void execute(Runnable task) {
		Executor executor = getThreadPoolExecutor();
		try {
			executor.execute(task);
		} catch (RejectedExecutionException ex) {
			throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
		}
	}

	/**
	 * This task executor prefers short-lived work units.
	 */
	public boolean prefersShortLivedTasks() {
		return true;
	}

	/**
	 * Return the current pool size.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#getPoolSize()
	 */
	public int getPoolSize() {
		return getThreadPoolExecutor().getPoolSize();
	}

	/**
	 * Return the number of currently active threads.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#getActiveCount()
	 */
	public int getActiveCount() {
		return getThreadPoolExecutor().getActiveCount();
	}

	/**
	 * Calls <code>shutdown</code> when the BeanFactory destroys the task executor instance.
	 * 
	 * @see #shutdown()
	 */
	public void destroy() {
		shutdown();
	}

	/**
	 * Perform a shutdown on the ThreadPoolExecutor.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("Shutting down ThreadPoolExecutor" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (this.waitForTasksToCompleteOnShutdown) {
			this.threadPoolExecutor.shutdown();

			try {
				this.threadPoolExecutor.awaitTermination(awaitTermination, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		} else {
			this.threadPoolExecutor.shutdownNow();
		}
	}
}
