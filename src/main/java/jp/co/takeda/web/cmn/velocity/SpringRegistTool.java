package jp.co.takeda.web.cmn.velocity;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.takeda.a.web.bean.SpringUtil;
import jp.co.takeda.service.DpmCodeMasterSearchService;
import jp.co.takeda.service.DpmPlannedCtgSearchService;
import jp.co.takeda.service.DpmProdSearchService;
import jp.co.takeda.service.DpmSosCtgSearchService;
import jp.co.takeda.service.DpsBusinessProgressSearchService;
import jp.co.takeda.service.DpsCExceptPlanSearchService;
import jp.co.takeda.service.DpsCodeMasterSearchService;
import jp.co.takeda.service.DpsContactOperationsSearchService;
import jp.co.takeda.service.DpsOfficePlanStatusSearchService;
import jp.co.takeda.service.DpsPlannedCtgSearchService;
import jp.co.takeda.service.DpsProdSearchService;
import jp.co.takeda.service.DpsSosCtgSearchService;
import jp.co.takeda.service.DpsSosJgiSearchService;
import jp.co.takeda.service.DpsYakkouSijouSearchService;

/**
 * SpringからBeanを取得するためのVelocityツール
 *
 * @author tkawabata
 */
public abstract class SpringRegistTool {

	/**
	 * LOGGER
	 */
	private static final Log LOG = LogFactory.getLog(SpringRegistTool.class);

	/**
	 * A reference to the ServletContext.
	 */
	protected ServletContext servletContext;

	/**
	 * Initializes this tool.
	 *
	 * @param obj the current ViewContext
	 * @throws IllegalArgumentException if the param is not a ViewContext
	 */
	public void init(Object obj) {
		if (!(obj instanceof ServletContext)) {
			throw new IllegalArgumentException("Tool can only be initialized with a ServletContext");
		}
		ServletContext context = (ServletContext) obj;
		this.servletContext = context;
	}

	/**
	 * フォーカス（一般MR計画）へのパスを取得する。
	 *
	 * @return HomeAndNaviのパス
	 */
	public String getFocusMrPlanPath() {
		return getBean("focusMrPlanUrl");
	}

	/**
	 * TPMターゲティング患者数入力画面へのパスを取得する。
	 *
	 * @return HomeAndNaviのパス
	 */
	public String getTargetPlanPatientUrl() {
		return getBean("targetPlanPatientUrl");
	}

	/**
	 * HomeAndNaviのパスを取得する。
	 *
	 * @return HomeAndNaviのパス
	 */
	public String getHomeAndNaviPath() {
		return getBean("homeAndNaviPath");
	}

	/**
	 * ReleaseNumberを取得する。
	 *
	 * @return ReleaseNumber
	 */
	public String getReleaseNumber() {
		return getBean("releaseNumber");
	}

	/**
	 * 業務進捗サービスを取得する。
	 *
	 * @return 業務進捗サービス
	 */
	protected DpsBusinessProgressSearchService getDpsBusinessProgressSearchService() {
		return getBean("dpsBusinessProgressSearchService");
	}

	/**
	 * 薬効市場サービスを取得する。
	 *
	 * @return 薬効市場サービス
	 */
	protected DpsYakkouSijouSearchService getDpsYakkouSijouSearchService() {
		return getBean("dpsYakkouSijouSearchService");
	}

	/**
	 * 品目検索サービスを取得する。
	 *
	 * @return 品目検索サービス
	 */
	protected DpsProdSearchService getDpsProdSearchService() {
		return getBean("dpsProdSearchService");
	}

	/**
	 * 営業所計画立案ステータス検索サービスを取得する。
	 *
	 * @return 営業所計画立案ステータス検索サービス
	 */
	protected DpsOfficePlanStatusSearchService getDpsOfficePlanStatusSearchService() {
		return getBean("dpsOfficePlanStatusSearchService");
	}

	/**
	 * 業務連絡情報を取得するサービス
	 *
	 * @return 業務連絡情報を取得するサービス
	 */
	protected DpsContactOperationsSearchService getDpsContactOperationsSearchService() {
		return getBean("dpsContactOperationsSearchService");
	}

	/**
	 * 組織・従業員に関するサービス
	 *
	 * @return 組織・従業員に関するサービス
	 */
	protected DpsSosJgiSearchService getDpsSosJgiSearchService() {
		return getBean("dpsSosJgiSearchService");
	}

	/**
	 * 管理の品目検索サービスを取得する。
	 *
	 * @return 品目検索サービス
	 */
	protected DpmProdSearchService getDpmProdSearchService() {
		return getBean("dpmProdSearchService");
	}

	/**
	 * 管理のコードマスタ検索サービスを取得する。
	 *
	 * @return コードマスタ検索サービス
	 */
	protected DpmCodeMasterSearchService getDpmCodeMasterSearchService() {
		return getBean("dpmCodeMasterSearchService");
	}

	/**
	 * 計画管理汎用マスタ検索サービスを取得する。
	 *
	 * @return 計画管理汎用マスタ検索サービス
	 */
	protected DpmCodeMasterSearchService dpmCodeMasterSearchService() {
		return getBean("dpmCodeMasterSearchService");
	}

	/**
	 * 組織カテゴリ検索サービスを取得する。
	 *
	 * @return 組織カテゴリ検索サービス
	 */
	protected DpmSosCtgSearchService getDpmSosCtgSearchService() {
		return getBean("dpmSosCtgSearchService");
	}

	/**
	 * 計画対象カテゴリ領域サービスを取得する。
	 *
	 * @return 計画対象カテゴリ領域サービス
	 */
	protected DpmPlannedCtgSearchService getDpmPlannedCtgSearchService() {
		return getBean("dpmPlannedCtgSearchService");
	}

	/**
	 * 支援のコードマスタ検索サービスを取得する。
	 *
	 * @return コードマスタ検索サービス
	 */
	protected DpsCodeMasterSearchService getDpsCodeMasterSearchService() {
		return getBean("dpsCodeMasterSearchService");
	}

	/**
	 * 支援の計画対象カテゴリ領域サービスを取得する。
	 *
	 * @return 計画対象カテゴリ領域サービス
	 */
	protected DpsPlannedCtgSearchService getDpsPlannedCtgSearchService() {
		return getBean("dpsPlannedCtgSearchService");
	}

	/**
	 * 計画支援の組織カテゴリコード検索サービスを取得する。
	 *
	 * @return 計画対象カテゴリ領域サービス
	 */
	protected DpsSosCtgSearchService getDpsSosCtgSearchService() {
		return getBean("dpsSosCtgSearchService");
	}

	/**
	 * SpringBeanを取得する。
	 *
	 * @param <T> 任意の型T
	 * @param beanId BeanId
	 * @return SpringBean
	 */
	@SuppressWarnings("unchecked")
	private <T> T getBean(final String beanId) {
		try {
			return (T) SpringUtil.getBean(beanId, this.servletContext);
		} catch (Exception e) {
			if (LOG.isFatalEnabled()) {
				final String errMsg = "VelocityからSpringBeanが取得出来ない";
				LOG.fatal(errMsg, e);
			}
			return null;
		}
	}

	protected DpsCExceptPlanSearchService getDpsCExceptPlanSearchService() {
		return getBean("dpsCExceptPlanSearchService");
	}
}
