package jp.co.takeda.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import jp.co.takeda.bean.DpDto;
import jp.co.takeda.model.ExceptPlan;
import jp.co.takeda.model.SosMst;

/**
 * 補足情報画面に表示する内容のDTOクラス
 *
 * @author nakashima
 */
public class SupInfoDto extends DpDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	組織情報
	 */
	private final SosMst sosMst;

	/**
	 * 立案対象外情報
	 */
	private final List<ExceptPlan> exceptPlans;



	public SupInfoDto(SosMst sosMst, List<ExceptPlan> exceptPlans) {
		super();
		this.sosMst = sosMst;
		this.exceptPlans = exceptPlans;
	}

	/**
	 * @return sosMst
	 */
	public SosMst getSosMst() {
		return sosMst;
	}

	/**
	 * @return exceptPlans
	 */
	public List<ExceptPlan> getExceptPlans() {
		return exceptPlans;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}
