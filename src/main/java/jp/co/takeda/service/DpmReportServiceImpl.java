package jp.co.takeda.service;

import static jp.co.takeda.a.exp.ErrMessageKey.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.takeda.a.bean.Conveyance;
import jp.co.takeda.a.exp.LogicalException;
import jp.co.takeda.a.exp.SystemException;
import jp.co.takeda.bean.Constants.TemplateInfo;
import jp.co.takeda.dao.CodeMasterDao;
import jp.co.takeda.dao.CommonDao;
import jp.co.takeda.dao.InsMstRealDao;
import jp.co.takeda.dao.JgiMstDAO;
import jp.co.takeda.dao.JgiMstRealDao;
import jp.co.takeda.dao.JisCodeMstDao;
import jp.co.takeda.dao.ManageChangeParamYTDao;
import jp.co.takeda.dao.ManageInsWsPlanDao;
import jp.co.takeda.dao.ManagePlannedProdDao;
import jp.co.takeda.dao.MasterManagePlannedProdDao;
import jp.co.takeda.dao.PlannedCtgDao;
import jp.co.takeda.dao.SosMstDAO;
import jp.co.takeda.dao.SosMstRealDao;
import jp.co.takeda.dao.TmsTytenMstUnRealDao;
import jp.co.takeda.dto.InsPlanForVacHeaderDto;
import jp.co.takeda.dto.InsPlanForVacResultDetailTotalDto;
import jp.co.takeda.dto.InsPlanForVacResultDto;
import jp.co.takeda.dto.InsPlanForVacScDto;
import jp.co.takeda.dto.InsPlanResultDto;
import jp.co.takeda.dto.InsPlanScDto;
import jp.co.takeda.dto.InsProdPlanResultDto;
import jp.co.takeda.dto.InsProdPlanResultHeaderDto;
import jp.co.takeda.dto.InsProdPlanScDto;
import jp.co.takeda.dto.ManageInsWsPlanDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanForVacScDto;
import jp.co.takeda.dto.ManageInsWsPlanHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdDto;
import jp.co.takeda.dto.ManageInsWsPlanProdHeaderDto;
import jp.co.takeda.dto.ManageInsWsPlanProdScDto;
import jp.co.takeda.dto.ManageInsWsPlanScDto;
import jp.co.takeda.dto.ManageWsPlanDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForIyakuProdHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacDto;
import jp.co.takeda.dto.ManageWsPlanForVacHeaderDto;
import jp.co.takeda.dto.ManageWsPlanForVacScDto;
import jp.co.takeda.dto.ManageWsPlanProdDto;
import jp.co.takeda.dto.ManageWsPlanProdScDto;
import jp.co.takeda.dto.ManageWsPlanScDto;
import jp.co.takeda.dto.ProdMonthPlanResultDto;
import jp.co.takeda.dto.ProdPlanResultDto;
import jp.co.takeda.dto.ProdPlanScDto;
import jp.co.takeda.dto.SosMonthPlanResultDto;
import jp.co.takeda.dto.SosPlanResultDto;
import jp.co.takeda.dto.SosPlanScDto;
import jp.co.takeda.logic.InsPlanExportLogic;
import jp.co.takeda.logic.InsPlanForVacExportLogic;
import jp.co.takeda.logic.InsProdPlanExportLogic;
import jp.co.takeda.logic.InsWsPlanExportLogic;
import jp.co.takeda.logic.InsWsPlanForVacExportLogic;
import jp.co.takeda.logic.InsWsPlanProdExportLogic;
import jp.co.takeda.logic.ManageWsPlanExportLogic;
import jp.co.takeda.logic.ManageWsPlanForVacExportLogic;
import jp.co.takeda.logic.ManageWsPlanProdExportLogic;
import jp.co.takeda.logic.ProdMonthPlanExportLogic;
import jp.co.takeda.logic.ProdPlanExportLogic;
import jp.co.takeda.logic.SosMonthPlanExportLogic;
import jp.co.takeda.logic.SosPlanExportLogic;
import jp.co.takeda.model.div.InsType;
import jp.co.takeda.web.cmn.bean.ExportResult;

/**
 * 計画管理の帳票サービス実装クラス
 *
 * @author tkawabata
 */
@Transactional
@Service("dpmReportService")
public class DpmReportServiceImpl implements DpmReportService {

	/**
	 * メッセージソース
	 */
	@Autowired(required = true)
	@Qualifier("messageSource")
	protected MessageSource messageSource;

	/**
	 * 共通DAO
	 */
	@Autowired(required = true)
	@Qualifier("commonDao")
	protected CommonDao commonDao;

	/**
	 * 組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstDAO")
	protected SosMstDAO sosMstDAO;

	/**
	 * 管理用組織情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("sosMstRealDao")
	protected SosMstRealDao sosMstRealDao;

	/**
	 * 計画対象品目DAO
	 */
	@Autowired(required = true)
	@Qualifier("managePlannedProdDao")
	protected ManagePlannedProdDao managePlannedProdDao;

	/**
	 * 計画対象品目DAO(PlannedProdを使わない)
	 */
	@Autowired(required = true)
	@Qualifier("masterManagePlannedProdDao")
	protected MasterManagePlannedProdDao masterManagePlannedProdDao;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstDAO")
	protected JgiMstDAO jgiMstDAO;

	/**
	 * 従業員情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("jgiMstRealDao")
	protected JgiMstRealDao jgiMstRealDao;

	/**
	 * 施設情報DAO
	 */
	@Autowired(required = true)
	@Qualifier("insMstRealDao")
	protected InsMstRealDao insMstRealDao;

	/**
	 * 変換パラメータ(Y→T価)DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageChangeParamYTDao")
	protected ManageChangeParamYTDao manageChangeParamYTDao;

	/**
	 * TMS特約店基本統合DAO
	 */
	@Autowired(required = true)
	@Qualifier("tmsTytenMstUnRealDao")
	protected TmsTytenMstUnRealDao tmsTytenMstUnRealDao;

	/**
	 * 管理の施設特約店別計画DAO
	 */
	@Autowired(required = true)
	@Qualifier("manageInsWsPlanDao")
	protected ManageInsWsPlanDao manageInsWsPlanDao;

	/**
	 * 組織別計画(担当者) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrPlanSearchService")
	protected DpmMrPlanSearchService dpmMrPlanSearchService;

	/**
	 * 組織別計画(チーム) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTeamPlanSearchService")
	protected DpmTeamPlanSearchService dpmTeamPlanSearchService;

	/**
	 * 組織別計画(支店) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchPlanSearchService")
	protected DpmBranchPlanSearchService dpmBranchPlanSearchService;

	/**
	 * 組織別計画(営業所) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficePlanSearchService")
	protected DpmOfficePlanSearchService dpmOfficePlanSearchService;

	/**
	 * 組織別計画(全社) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmIyakuPlanSearchService")
	protected DpmIyakuPlanSearchService dpmIyakuPlanSearchService;

	/**
	 * 組織別月別計画(全社) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmIyakuMonthPlanSearchService")
	protected DpmIyakuMonthPlanSearchService dpmIyakuMonthPlanSearchService;

	/**
	 * 組織別月別計画(支店) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmBranchMonthPlanSearchService")
	protected DpmBranchMonthPlanSearchService dpmBranchMonthPlanSearchService;

	/**
	 * 組織別月別計画(営業所) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmOfficeMonthPlanSearchService")
	protected DpmOfficeMonthPlanSearchService dpmOfficeMonthPlanSearchService;

	/**
	 * 組織別月別計画(担当者) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmMrMonthPlanSearchService")
	protected DpmMrMonthPlanSearchService dpmMrMonthPlanSearchService;

    /**
     * 計画管理汎用マスタDAO
     */
    @Autowired(required = true)
    @Qualifier("codeMasterDao")
    protected CodeMasterDao codeMasterDao;

    /**
     * 施設別計画(医)/施設品目別　検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmInsPlanSearchService")
    protected DpmInsPlanSearchService dpmInsPlanSearchService;

    /**
     * 施設別計画(ワ) 検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmInsPlanForVacSearchService")
    protected DpmInsPlanForVacSearchService dpmInsPlanForVacSearchService;

	/**
	 * 施設特約点別計画(医) 検索サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmInsWsPlanSearchService")
	protected DpmInsWsPlanSearchService dpmInsWsPlanSearchService;

    /**
     * 施設特約点別計画(ワ) 検索サービス
     */
    @Autowired(required = true)
    @Qualifier("dpmInsWsPlanForVacSearchService")
    protected DpmInsWsPlanForVacSearchService dpmInsWsPlanForVacSearchService;


    /**
     * 特約点別計画 検索サービス
     */
    @Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanSearchService")
	protected DpmTmsTytenPlanSearchService dpmTmsTytenPlanSearchService;

	/**
	 * 特約店別計画（ワ）検索系サービス
	 */
	@Autowired(required = true)
	@Qualifier("dpmTmsTytenPlanForVacSearchService")
	protected DpmTmsTytenPlanForVacSearchService dpmTmsTytenPlanForVacSearchService;

	/**
	 * 計画対象カテゴリ領域Dao
	 */
	@Autowired(required = true)
	@Qualifier("plannedCtgDao")
	protected PlannedCtgDao plannedCtgDao;

	/**
	 * JIS府県・市区町村取得Dao
	 */
	@Autowired(required = true)
	@Qualifier("jisCodeMstDao")
	protected JisCodeMstDao jisCodeMstDao;


	// (医)組織別計画
	public ExportResult outputSosPlanList(String templateRootPath, SosPlanScDto scDto, String jgiNo) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "組織別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		SosPlanResultDto resultDto;
		if (scDto.getSosCd4() != null) {

			// 組織コード(チーム)が設定されている場合、担当者別計画一覧を作成
			resultDto = dpmMrPlanSearchService.searchSosPlan(scDto, jgiNo);

		} else if (scDto.getSosCd3() != null && scDto.getOncSosFlg()) {

			// 組織コード(営業所:ONC)が設定されている場合、担当者別計画一覧を作成
			resultDto = dpmMrPlanSearchService.searchSosPlan(scDto, jgiNo);

		} else if (scDto.getSosCd3() != null && !scDto.getOncSosFlg()) {

			// 組織コード(営業所:ONC以外)が設定されている場合、チーム別計画一覧を作成
			resultDto = dpmTeamPlanSearchService.searchSosPlan(scDto);

		} else if (scDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合、営業所別計画一覧を作成
			resultDto = dpmOfficePlanSearchService.searchSosPlan(scDto);

		} else {

			// 組織コードが設定されていない場合、支店別計画一覧を作成
			resultDto = dpmBranchPlanSearchService.searchSosPlan(scDto);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM100P01.getTemplatePath(templateRootPath);
		SosPlanExportLogic logic = new SosPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, codeMasterDao, masterManagePlannedProdDao);
		return logic.export();
	}

	// (医)組織品目別計画
	public ExportResult outputProdPlanList(String templateRootPath, ProdPlanScDto prodPlanScDto) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (prodPlanScDto == null) {
			final String errMsg = "組織別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ProdPlanResultDto resultDto;
		if (prodPlanScDto.getJgiNo() != null) {

			// 従業員番号が設定されている場合、担当者別計画一覧を作成
			resultDto = dpmMrPlanSearchService.searchSosProdPlan(prodPlanScDto);

		} else if (prodPlanScDto.getSosCd4() != null) {

			// 組織コード(チーム)が設定されている場合、チーム別計画一覧を作成
			resultDto = dpmTeamPlanSearchService.searchSosProdPlan(prodPlanScDto);

		} else if (prodPlanScDto.getSosCd3() != null) {

			// 組織コード(営業所)が設定されている場合、営業所別計画一覧を作成
			resultDto = dpmOfficePlanSearchService.searchSosProdPlan(prodPlanScDto);

		} else if (prodPlanScDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合、支店別計画一覧を作成
			resultDto = dpmBranchPlanSearchService.searchSosProdPlan(prodPlanScDto);

		} else {

			// 組織コードが設定されていない場合、全社計画一覧を作成
			resultDto = dpmIyakuPlanSearchService.searchSosProdPlan(prodPlanScDto);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM101P01.getTemplatePath(templateRootPath);
		ProdPlanExportLogic logic = new ProdPlanExportLogic(templatePath, commonDao.getSystemTime(), prodPlanScDto, resultDto, sosMstRealDao, jgiMstRealDao, codeMasterDao);
		return logic.export();
	}


	// (医)施設別計画
	//@Override
    public ExportResult outputInsPlanList(String templateRootPath, InsPlanScDto insPlanScDto) throws LogicalException {

        //-------------------------
        // 引数チェック
        // -------------------------
        if (templateRootPath == null) {
            final String errMsg = "テンプレート配置パスがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }
        if (insPlanScDto == null) {
            final String errMsg = "施設別計画編集画面の検索条件DTOがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }

        InsPlanResultDto resultDto = null;

        resultDto = dpmInsPlanSearchService.searchInsPlan(insPlanScDto, true);

		// -------------------------
		// ヘッダ情報取得
		// -------------------------
        InsProdPlanResultHeaderDto headerDto = null;

		// 施設コードが入力された場合は、ヘッダー情報取得
		if (StringUtils.isNotEmpty(insPlanScDto.getInsNo())) {
			headerDto = dpmInsPlanSearchService.searchInsPlanHeaderOyako(insPlanScDto.getInsNo(),insPlanScDto.getProdCode(), insPlanScDto.getProdCategory());
		}

        // -------------------------
        // エクセルファイル生成
        // -------------------------
        final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM200P01.getTemplatePath(templateRootPath);
        InsPlanExportLogic logic = new InsPlanExportLogic(templatePath, commonDao.getSystemTime(), insPlanScDto, resultDto, insMstRealDao, sosMstRealDao,
        		jgiMstRealDao, codeMasterDao, masterManagePlannedProdDao, headerDto);
        return logic.export();
    }

     //(ワ)施設別計画
    public ExportResult outputInsPlanForVacList(String templateRootPath, InsPlanForVacScDto insPlanForVacScDto) throws LogicalException{

        //-------------------------
        // 引数チェック
        // -------------------------
        if (templateRootPath == null) {
            final String errMsg = "テンプレート配置パスがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }
        if (insPlanForVacScDto == null) {
            final String errMsg = "施設別計画編集画面の検索条件DTOがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }

        InsPlanForVacResultDto resultDto;

        resultDto = dpmInsPlanForVacSearchService.searchInsPlan(insPlanForVacScDto);

		// 集計行取得
		InsPlanForVacResultDetailTotalDto totalDto = dpmInsPlanForVacSearchService.searchInsPlanTotal(insPlanForVacScDto, resultDto);

		// -------------------------
		// ヘッダ情報取得
		// -------------------------
		InsPlanForVacHeaderDto headerDto = null;

		// 施設コードが入力された場合は、ヘッダー情報取得
		if (StringUtils.isNotEmpty(insPlanForVacScDto.getInsNo())) {
			// ヘッダー情報取得
			headerDto = dpmInsPlanForVacSearchService.searchInsPlanHeader(insPlanForVacScDto.getInsNo());
		}

        // -------------------------
        // エクセルファイル生成
        // -------------------------
        final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM200P02.getTemplatePath(templateRootPath);
        InsPlanForVacExportLogic logic = new InsPlanForVacExportLogic(templatePath, commonDao.getSystemTime(), insPlanForVacScDto, resultDto,
        		totalDto, insMstRealDao, sosMstRealDao, jgiMstRealDao, codeMasterDao, masterManagePlannedProdDao, jisCodeMstDao, headerDto);
        return logic.export();
    }

    // (医)施設品目別計画
    public ExportResult outputInsProdPlanList(String templateRootPath, InsProdPlanScDto insProdPlanScDto) throws LogicalException {
        //-------------------------
        // 引数チェック
        // -------------------------
        if (templateRootPath == null) {
            final String errMsg = "テンプレート配置パスがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }
        if (insProdPlanScDto == null) {
            final String errMsg = "施設別計画編集画面の検索条件DTOがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }

        InsProdPlanResultDto resultDto = null;
        resultDto = dpmInsPlanSearchService.searchInsProdPlan(insProdPlanScDto);

		// ヘッダー情報取得
		InsProdPlanResultHeaderDto headerDto = dpmInsPlanSearchService.searchInsPlanHeader(insProdPlanScDto.getInsNo(), null);

        // -------------------------
        // エクセルファイル生成
        // -------------------------
        final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM201P01.getTemplatePath(templateRootPath);
        InsProdPlanExportLogic logic = new InsProdPlanExportLogic(templatePath, commonDao.getSystemTime(), insProdPlanScDto, resultDto, insMstRealDao, codeMasterDao, headerDto.getInsName());
        return logic.export();
    }

	// (医)施設特約店別計画
	public ExportResult outputInsWsPlanList(String templateRootPath, ManageInsWsPlanScDto scDto, boolean ryutsu) throws LogicalException {

		// ----------------------
		// 引数チェック
		// ----------------------
		if (scDto == null) {
			final String errMsg = "特約店別計画検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		final String prodCode = scDto.getProdCode();
		if (prodCode == null) {
			final String errMsg = "検索対象の品目固定コードがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		InsType insType = scDto.getInsType();
		if (insType == null) {
			final String errMsg = "検索対象の施設出力対象区分がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto.getPlanData() == null) {
			final String errMsg = "検索対象の計画検索範囲がnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ManageInsWsPlanDto resultDto = null;
		resultDto = dpmInsWsPlanSearchService.searchList(scDto);

		// -------------------------
		// ヘッダ情報取得
		// -------------------------
		ManageInsWsPlanHeaderDto headerDto = dpmInsWsPlanSearchService.searchHeader(scDto);

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM400P01.getTemplatePath(templateRootPath);
		InsWsPlanExportLogic logic = new InsWsPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, jgiMstRealDao,
				insMstRealDao, tmsTytenMstUnRealDao, codeMasterDao, masterManagePlannedProdDao, ryutsu, headerDto);
		return logic.export();
	}


	// (ワ)施設特約店別計画
	public ExportResult outputInsWsPlanForVacList(String templateRootPath, ManageInsWsPlanForVacScDto insWsPlanForVacScDto, boolean ryutsu) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (insWsPlanForVacScDto == null) {
			final String errMsg = "施設特約別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ManageInsWsPlanForVacDto resultDto = null;
		resultDto = dpmInsWsPlanForVacSearchService.searchInsWsPlan(insWsPlanForVacScDto);

		// -------------------------
		// ヘッダ情報取得
		// -------------------------
		ManageInsWsPlanForVacHeaderDto headerDto = dpmInsWsPlanForVacSearchService.searchInsWsPlanHeader(insWsPlanForVacScDto.getInsNo(), insWsPlanForVacScDto.getTmsTytenCd());


		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM400P02.getTemplatePath(templateRootPath);
		InsWsPlanForVacExportLogic logic = new InsWsPlanForVacExportLogic(templatePath, commonDao.getSystemTime(), insWsPlanForVacScDto, resultDto, sosMstRealDao, jgiMstRealDao,
				insMstRealDao, tmsTytenMstUnRealDao, codeMasterDao, masterManagePlannedProdDao, ryutsu, headerDto);
		return logic.export();
	}

	// (医)施設特約店品目別計画
	public ExportResult outputInsWsPlanProdList(String templateRootPath, ManageInsWsPlanProdScDto manageInsWsPlanProdScDto, boolean ryutsu) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (manageInsWsPlanProdScDto == null) {
			final String errMsg = "施設特約店品目別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		 ManageInsWsPlanProdDto resultDto = null;
		 resultDto = dpmInsWsPlanSearchService.searchList(manageInsWsPlanProdScDto);

		// -------------------------
		// ヘッダー情報取得
		// -------------------------
		String insName = null;
		String tmsTytenName = null;

		ManageInsWsPlanProdHeaderDto headerDto = dpmInsWsPlanSearchService.searchHeader(manageInsWsPlanProdScDto);;

		if (headerDto.getInsMstResultDto() != null) {
			insName = headerDto.getInsMstResultDto().getInsName();
		}

		if (headerDto != null) {
			tmsTytenName = headerDto.getTmsTytenName();
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM401P01.getTemplatePath(templateRootPath);
		InsWsPlanProdExportLogic logic = new InsWsPlanProdExportLogic(templatePath, commonDao.getSystemTime(), manageInsWsPlanProdScDto, resultDto, sosMstDAO, insMstRealDao, tmsTytenMstUnRealDao, codeMasterDao,
																		ryutsu, insName, tmsTytenName);
		return logic.export();
	}

	// (医)特約店別計画
	public ExportResult outputManageWsPlanList(String templateRootPath, ManageWsPlanScDto scDto, boolean ryutsu, String titleUH, String titleP, String titleZ) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "特約店別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ManageWsPlanDto resultDto = null;
		resultDto = dpmTmsTytenPlanSearchService.searchList(scDto);

		ManageWsPlanForIyakuHeaderDto headerDto = dpmTmsTytenPlanSearchService.searchHeader(scDto);

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM300P01.getTemplatePath(templateRootPath);
		ManageWsPlanExportLogic logic = new ManageWsPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, headerDto, tmsTytenMstUnRealDao,
				codeMasterDao, masterManagePlannedProdDao, plannedCtgDao, ryutsu, titleUH, titleP, titleZ);
		return logic.export();
	}

	// (ワ)特約店別計画
	public ExportResult outputManageWsVacPlanList(String templateRootPath, ManageWsPlanForVacScDto scDto, boolean ryutsu) throws LogicalException {
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "特約店別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ManageWsPlanForVacDto resultDto = null;
		resultDto = dpmTmsTytenPlanForVacSearchService.searchList(scDto);

		ManageWsPlanForVacHeaderDto headerDto = dpmTmsTytenPlanForVacSearchService.searchHeader(scDto);

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM300P01.getTemplatePath(templateRootPath);
		ManageWsPlanForVacExportLogic logic = new ManageWsPlanForVacExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, headerDto, tmsTytenMstUnRealDao, codeMasterDao, masterManagePlannedProdDao, ryutsu);
		return logic.export();
	}

    // 特約店品目別計画
	@Override
    public ExportResult outputManageWsPlanProdList(String templateRootPath, ManageWsPlanProdScDto scDto, boolean ryutsu) throws LogicalException {
        // -------------------------
        // 引数チェック
        // -------------------------
        if (templateRootPath == null) {
            final String errMsg = "テンプレート配置パスがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }
        if (scDto == null) {
            final String errMsg = "特約店別計画編集画面の検索条件DTOがnull";
            throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
        }

        ManageWsPlanProdDto resultDto = dpmTmsTytenPlanSearchService.searchList(scDto);

        ManageWsPlanForIyakuProdHeaderDto headerDto = dpmTmsTytenPlanSearchService.searchHeader(scDto);

        // -------------------------
        // エクセルファイル生成
        // -------------------------
        final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM301P01.getTemplatePath(templateRootPath);
        ManageWsPlanProdExportLogic logic = new ManageWsPlanProdExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, headerDto, tmsTytenMstUnRealDao, codeMasterDao, plannedCtgDao, ryutsu);
        return logic.export();
    }

	// 組織別月別計画
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//public ExportResult outputSosMonthPlanList(String templateRootPath, SosPlanScDto scDto, String jgiNo) throws LogicalException {
	public ExportResult outputSosMonthPlanList(String templateRootPath, SosPlanScDto scDto, String jgiNo, int tougetuCount) throws LogicalException {
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "組織別月別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		SosMonthPlanResultDto resultDto = null;
		if (scDto.getSosCd3() != null) {

			// 組織コード(営業所)が設定されている場合、担当者別計画一覧を作成
			resultDto = dpmMrMonthPlanSearchService.searchSosPlan(scDto, jgiNo);

		} else if (scDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合、営業所別計画一覧を作成
			resultDto = dpmOfficeMonthPlanSearchService.searchSosPlan(scDto);

		} else {

			// 組織コードが設定されていない場合、支店別計画一覧を作成
			resultDto = dpmBranchMonthPlanSearchService.searchSosPlan(scDto);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM500P01.getTemplatePath(templateRootPath);
		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		//SosMonthPlanExportLogic logic = new SosMonthPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, codeMasterDao, masterManagePlannedProdDao);
		SosMonthPlanExportLogic logic = new SosMonthPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, codeMasterDao, masterManagePlannedProdDao, tougetuCount);
		//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		return logic.export();
	}

	// 組織品目別月別計画
	//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
	//public ExportResult outputProdMonthPlanList(String templateRootPath, ProdPlanScDto scDto, List<String> jrnsCtgList) throws LogicalException {
	public ExportResult outputProdMonthPlanList(String templateRootPath, ProdPlanScDto scDto, List<String> jrnsCtgList, int tougetuCount) throws LogicalException {
	//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		// -------------------------
		// 引数チェック
		// -------------------------
		if (templateRootPath == null) {
			final String errMsg = "テンプレート配置パスがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}
		if (scDto == null) {
			final String errMsg = "組織品目別月別計画編集画面の検索条件DTOがnull";
			throw new SystemException(new Conveyance(PARAMETER_ERROR, errMsg));
		}

		ProdMonthPlanResultDto resultDto;
		if (scDto.getJgiNo() != null) {

			// 従業員番号が設定されている場合、担当者別計画一覧を作成
			resultDto = dpmMrMonthPlanSearchService.searchSosProdPlan(scDto, jrnsCtgList);

		} else if (scDto.getSosCd3() != null) {

			// 組織コード(営業所)が設定されている場合、営業所別計画一覧を作成
			resultDto = dpmOfficeMonthPlanSearchService.searchSosProdPlan(scDto, jrnsCtgList);

		} else if (scDto.getSosCd2() != null) {

			// 組織コード(支店)が設定されている場合、支店別計画一覧を作成
			resultDto = dpmBranchMonthPlanSearchService.searchSosProdPlan(scDto, jrnsCtgList);

		} else {

			// 組織コードが設定されていない場合、全社計画一覧を作成
			resultDto = dpmIyakuMonthPlanSearchService.searchSosProdPlan(scDto, jrnsCtgList);
		}

		// -------------------------
		// エクセルファイル生成
		// -------------------------
		final String templatePath = TemplateInfo.EXCEL_TEMPLATE_DPM501P01.getTemplatePath(templateRootPath);
		//mod Start 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		//ProdMonthPlanExportLogic logic = new ProdMonthPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, codeMasterDao, masterManagePlannedProdDao,jgiMstRealDao);
		ProdMonthPlanExportLogic logic = new ProdMonthPlanExportLogic(templatePath, commonDao.getSystemTime(), scDto, resultDto, sosMstRealDao, codeMasterDao, masterManagePlannedProdDao,jgiMstRealDao, tougetuCount);
		//mod End 2022/11/29 H.futagami No.19　「合計」欄は「前月までの実績＋当月以降の入力した計画」の合計を表示
		return logic.export();
	}
}
