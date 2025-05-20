package jp.co.takeda.a.web.action;

import jp.co.takeda.a.web.bean.SpringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.chain.commands.servlet.CreateAction;
import org.apache.struts.chain.contexts.ActionContext;
import org.apache.struts.chain.contexts.ServletActionContext;
import org.springframework.util.Assert;

/**
 * アクションをSpring管理で生成するクラス
 * 
 * @author tkawabata
 */
public class CreateSpringAction extends CreateAction {

	/**
	 * Spring管理されたアクションクラスを取得する。
	 * 
	 * @param context コンテキスト情報
	 * @param type アクションのタイプ
	 * @return Spring管理されたアクションクラス
	 * @throws Exception
	 */
	@Override
	protected Action createAction(ActionContext context, String type) throws Exception {
		String actionId = createActionId(type);
		ServletActionContext sac = (ServletActionContext) context;
		return SpringUtil.getBean(actionId, sac.getContext());
	}

	/**
	 * Actionの完全名(パッケージ名 + クラス名)からクラス名を抽出し、ActionのIdとする。
	 * 
	 * @param type Actionの完全名(パッケージ名 + クラス名)
	 * @return ActionのId(クラス名)
	 */
	protected String createActionId(String type) {
		Assert.notNull(type, "action type is null.");
		final String DOT = ".";
		return type.substring((type.lastIndexOf(DOT) + 1));
	}
}
