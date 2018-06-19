package hr.vestigo.modules.collateral.jcics.co18;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO18 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co18/DeclCO18.java,v 1.12 2013/12/23 09:11:38 hrajkl Exp $";
	private Map response = new HashMap();

	public DeclCO18() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal InsuPolicy_COL_HEA_ID = null;

	// out args
	public TableData tblInsuPolicy = new TableData();

	// inner classes
	public INSUPOLICYDIALOGINSERTM insupolicydialoginsertm = null;
	public INSUPOLICYDIALOGUPDATEM insupolicydialogupdatem = null;
	public INSUPOLICYDIALOGDELETEM insupolicydialogdeletem = null;
	public INSUPOLICYSTATUSUPDATEM insupolicystatusupdatem = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		InsuPolicy_COL_HEA_ID = (BigDecimal) request.get("InsuPolicy_COL_HEA_ID");
	}

	public Map getResponse() {
		response.put("tblInsuPolicy", tblInsuPolicy);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("InsuPolicyDialogInsertM")) {
			insupolicydialoginsertm = new INSUPOLICYDIALOGINSERTM(request);
			return insupolicydialoginsertm;
		} else if(mapping.equals("InsuPolicyDialogUpdateM")) {
			insupolicydialogupdatem = new INSUPOLICYDIALOGUPDATEM(request);
			return insupolicydialogupdatem;
		} else if(mapping.equals("InsuPolicyDialogDeleteM")) {
			insupolicydialogdeletem = new INSUPOLICYDIALOGDELETEM(request);
			return insupolicydialogdeletem;
		} else if(mapping.equals("InsuPolicyStatusUpdateM")) {
			insupolicystatusupdatem = new INSUPOLICYSTATUSUPDATEM(request);
			return insupolicystatusupdatem;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class INSUPOLICYDIALOGINSERTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COL_HEA_ID = null;
		public String InsuPolicyDialog_txtAct = null;
		public String InsuPolicyDialog_txtCode = null;
		public Date InsuPolicyDialog_txtDateFrom = null;
		public Date InsuPolicyDialog_txtDateSecVal = null;
		public Date InsuPolicyDialog_txtDateUntil = null;
		public String InsuPolicyDialog_txtPlace = null;
		public String InsuPolicyDialog_txtReplace = null;
		public BigDecimal InsuPolicyDialog_txtSecuVal = null;
		public BigDecimal IP_CONTRACTOR = null;
		public BigDecimal IP_CUR_ID = null;
		public BigDecimal IP_IC_ID = null;
		public BigDecimal IP_POL_HOLDER = null;
		public String IP_SPEC_STAT = null;
		public BigDecimal IP_TYPE_ID = null;
		public BigDecimal use_id = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_COL_CAT_ID = null;
		public BigDecimal RealEstate_COL_TYPE_ID = null;
		public BigDecimal RealEstate_REAL_EST_TYPE = null;
		public String InsuPolicyDialog_txtWrnStatusCode = null;
		public String InsuPolicyDialog_txtKmtStatusCode = null;
		public BigDecimal org_uni_id = null;
		public String InsuPolicyDialog_txtVinkulacija1RedSifra = null;
		public BigDecimal InsuPolicyDialog_txtIznosOsiguraneSvote = null;
		public BigDecimal InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID = null;
		public BigDecimal InsuPolicyDialog_txtIznosIznosVinkuliranUKorist = null;
		public BigDecimal InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID = null;
		public String InsuPolicyDialog_txtRBAVinkulacijaSifra = null;
		public String InsuPolicyDialog_txtDodatniPodaci = null;

		// out args
		public String RealEstate_txtInspolInd = null;
		public String ip_activ_and_pay = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public BigDecimal RealEstate_txtCollMvpPonderMin = null;
		public BigDecimal RealEstate_txtCollMvpPonderMax = null;
		public BigDecimal RealEstate_txtPonAvailValue = null;
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;

		public INSUPOLICYDIALOGINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			InsuPolicyDialog_txtAct = (String) request.get("InsuPolicyDialog_txtAct");
			InsuPolicyDialog_txtCode = (String) request.get("InsuPolicyDialog_txtCode");
			InsuPolicyDialog_txtDateFrom = (Date) request.get("InsuPolicyDialog_txtDateFrom");
			InsuPolicyDialog_txtDateSecVal = (Date) request.get("InsuPolicyDialog_txtDateSecVal");
			InsuPolicyDialog_txtDateUntil = (Date) request.get("InsuPolicyDialog_txtDateUntil");
			InsuPolicyDialog_txtPlace = (String) request.get("InsuPolicyDialog_txtPlace");
			InsuPolicyDialog_txtReplace = (String) request.get("InsuPolicyDialog_txtReplace");
			InsuPolicyDialog_txtSecuVal = (BigDecimal) request.get("InsuPolicyDialog_txtSecuVal");
			IP_CONTRACTOR = (BigDecimal) request.get("IP_CONTRACTOR");
			IP_CUR_ID = (BigDecimal) request.get("IP_CUR_ID");
			IP_IC_ID = (BigDecimal) request.get("IP_IC_ID");
			IP_POL_HOLDER = (BigDecimal) request.get("IP_POL_HOLDER");
			IP_SPEC_STAT = (String) request.get("IP_SPEC_STAT");
			IP_TYPE_ID = (BigDecimal) request.get("IP_TYPE_ID");
			use_id = (BigDecimal) request.get("use_id");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_COL_CAT_ID = (BigDecimal) request.get("RealEstate_COL_CAT_ID");
			RealEstate_COL_TYPE_ID = (BigDecimal) request.get("RealEstate_COL_TYPE_ID");
			RealEstate_REAL_EST_TYPE = (BigDecimal) request.get("RealEstate_REAL_EST_TYPE");
			InsuPolicyDialog_txtWrnStatusCode = (String) request.get("InsuPolicyDialog_txtWrnStatusCode");
			InsuPolicyDialog_txtKmtStatusCode = (String) request.get("InsuPolicyDialog_txtKmtStatusCode");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			InsuPolicyDialog_txtVinkulacija1RedSifra = (String) request.get("InsuPolicyDialog_txtVinkulacija1RedSifra");
			InsuPolicyDialog_txtIznosOsiguraneSvote = (BigDecimal) request.get("InsuPolicyDialog_txtIznosOsiguraneSvote");
			InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID = (BigDecimal) request.get("InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID");
			InsuPolicyDialog_txtIznosIznosVinkuliranUKorist = (BigDecimal) request.get("InsuPolicyDialog_txtIznosIznosVinkuliranUKorist");
			InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID = (BigDecimal) request.get("InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID");
			InsuPolicyDialog_txtRBAVinkulacijaSifra = (String) request.get("InsuPolicyDialog_txtRBAVinkulacijaSifra");
			InsuPolicyDialog_txtDodatniPodaci = (String) request.get("InsuPolicyDialog_txtDodatniPodaci");
		}

		public Map getResponse() {
			response.put("RealEstate_txtInspolInd", RealEstate_txtInspolInd);
			response.put("ip_activ_and_pay", ip_activ_and_pay);
			response.put("RealEstate_txtCollMvpPonder", RealEstate_txtCollMvpPonder);
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("RealEstate_txtCollMvpPonderMin", RealEstate_txtCollMvpPonderMin);
			response.put("RealEstate_txtCollMvpPonderMax", RealEstate_txtCollMvpPonderMax);
			response.put("RealEstate_txtPonAvailValue", RealEstate_txtPonAvailValue);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYDIALOGUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COL_HEA_ID = null;
		public String InsuPolicyDialog_txtAct = null;
		public String InsuPolicyDialog_txtCode = null;
		public Date InsuPolicyDialog_txtDateFrom = null;
		public Date InsuPolicyDialog_txtDateSecVal = null;
		public Date InsuPolicyDialog_txtDateUntil = null;
		public String InsuPolicyDialog_txtPlace = null;
		public String InsuPolicyDialog_txtReplace = null;
		public BigDecimal InsuPolicyDialog_txtSecuVal = null;
		public BigDecimal IP_CONTRACTOR = null;
		public BigDecimal IP_CUR_ID = null;
		public BigDecimal IP_IC_ID = null;
		public BigDecimal IP_POL_HOLDER = null;
		public String IP_SPEC_STAT = null;
		public BigDecimal IP_TYPE_ID = null;
		public BigDecimal use_id = null;
		public String InsuPolicyDialog_txtActB = null;
		public String InsuPolicyDialog_txtCodeB = null;
		public Date InsuPolicyDialog_txtDateFromB = null;
		public Date InsuPolicyDialog_txtDateSecValB = null;
		public Date InsuPolicyDialog_txtDateUntilB = null;
		public String InsuPolicyDialog_txtPlaceB = null;
		public String InsuPolicyDialog_txtReplaceB = null;
		public BigDecimal InsuPolicyDialog_txtSecuValB = null;
		public BigDecimal IP_CONTRACTOR_B = null;
		public BigDecimal IP_CUR_ID_B = null;
		public BigDecimal IP_IC_ID_B = null;
		public BigDecimal IP_POL_HOLDER_B = null;
		public BigDecimal IP_TYPE_ID_B = null;
		public BigDecimal USE_ID_B = null;
		public Timestamp InsuPolicyDialog_txtOpeningTsNF = null;
		public Timestamp InsuPolicyDialog_txtUserLockNF = null;
		public BigDecimal USE_OPEN_ID = null;
		public BigDecimal IP_ID = null;
		public String IP_SPEC_STAT_B = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_COL_CAT_ID = null;
		public BigDecimal RealEstate_COL_TYPE_ID = null;
		public BigDecimal RealEstate_REAL_EST_TYPE = null;
		public String InsuPolicyDialog_txtWrnStatusCode = null;
		public String InsuPolicyDialog_txtKmtStatusCode = null;
		public String InsuPolicyDialog_txtVinkulacija1RedSifra = null;
		public BigDecimal InsuPolicyDialog_txtIznosOsiguraneSvote = null;
		public BigDecimal InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID = null;
		public BigDecimal InsuPolicyDialog_txtIznosIznosVinkuliranUKorist = null;
		public BigDecimal InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID = null;
		public String InsuPolicyDialog_txtRBAVinkulacijaSifra = null;
		public String InsuPolicyDialog_txtDodatniPodaci = null;
		public BigDecimal org_uni_id = null;

		// out args
		public String RealEstate_txtInspolInd = null;
		public String ip_activ_and_pay = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public BigDecimal RealEstate_txtCollMvpPonderMin = null;
		public BigDecimal RealEstate_txtCollMvpPonderMax = null;
		public BigDecimal RealEstate_txtPonAvailValue = null;
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;

		public INSUPOLICYDIALOGUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			InsuPolicyDialog_txtAct = (String) request.get("InsuPolicyDialog_txtAct");
			InsuPolicyDialog_txtCode = (String) request.get("InsuPolicyDialog_txtCode");
			InsuPolicyDialog_txtDateFrom = (Date) request.get("InsuPolicyDialog_txtDateFrom");
			InsuPolicyDialog_txtDateSecVal = (Date) request.get("InsuPolicyDialog_txtDateSecVal");
			InsuPolicyDialog_txtDateUntil = (Date) request.get("InsuPolicyDialog_txtDateUntil");
			InsuPolicyDialog_txtPlace = (String) request.get("InsuPolicyDialog_txtPlace");
			InsuPolicyDialog_txtReplace = (String) request.get("InsuPolicyDialog_txtReplace");
			InsuPolicyDialog_txtSecuVal = (BigDecimal) request.get("InsuPolicyDialog_txtSecuVal");
			IP_CONTRACTOR = (BigDecimal) request.get("IP_CONTRACTOR");
			IP_CUR_ID = (BigDecimal) request.get("IP_CUR_ID");
			IP_IC_ID = (BigDecimal) request.get("IP_IC_ID");
			IP_POL_HOLDER = (BigDecimal) request.get("IP_POL_HOLDER");
			IP_SPEC_STAT = (String) request.get("IP_SPEC_STAT");
			IP_TYPE_ID = (BigDecimal) request.get("IP_TYPE_ID");
			use_id = (BigDecimal) request.get("use_id");
			InsuPolicyDialog_txtActB = (String) request.get("InsuPolicyDialog_txtActB");
			InsuPolicyDialog_txtCodeB = (String) request.get("InsuPolicyDialog_txtCodeB");
			InsuPolicyDialog_txtDateFromB = (Date) request.get("InsuPolicyDialog_txtDateFromB");
			InsuPolicyDialog_txtDateSecValB = (Date) request.get("InsuPolicyDialog_txtDateSecValB");
			InsuPolicyDialog_txtDateUntilB = (Date) request.get("InsuPolicyDialog_txtDateUntilB");
			InsuPolicyDialog_txtPlaceB = (String) request.get("InsuPolicyDialog_txtPlaceB");
			InsuPolicyDialog_txtReplaceB = (String) request.get("InsuPolicyDialog_txtReplaceB");
			InsuPolicyDialog_txtSecuValB = (BigDecimal) request.get("InsuPolicyDialog_txtSecuValB");
			IP_CONTRACTOR_B = (BigDecimal) request.get("IP_CONTRACTOR_B");
			IP_CUR_ID_B = (BigDecimal) request.get("IP_CUR_ID_B");
			IP_IC_ID_B = (BigDecimal) request.get("IP_IC_ID_B");
			IP_POL_HOLDER_B = (BigDecimal) request.get("IP_POL_HOLDER_B");
			IP_TYPE_ID_B = (BigDecimal) request.get("IP_TYPE_ID_B");
			USE_ID_B = (BigDecimal) request.get("USE_ID_B");
			InsuPolicyDialog_txtOpeningTsNF = (Timestamp) request.get("InsuPolicyDialog_txtOpeningTsNF");
			InsuPolicyDialog_txtUserLockNF = (Timestamp) request.get("InsuPolicyDialog_txtUserLockNF");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			IP_ID = (BigDecimal) request.get("IP_ID");
			IP_SPEC_STAT_B = (String) request.get("IP_SPEC_STAT_B");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_COL_CAT_ID = (BigDecimal) request.get("RealEstate_COL_CAT_ID");
			RealEstate_COL_TYPE_ID = (BigDecimal) request.get("RealEstate_COL_TYPE_ID");
			RealEstate_REAL_EST_TYPE = (BigDecimal) request.get("RealEstate_REAL_EST_TYPE");
			InsuPolicyDialog_txtWrnStatusCode = (String) request.get("InsuPolicyDialog_txtWrnStatusCode");
			InsuPolicyDialog_txtKmtStatusCode = (String) request.get("InsuPolicyDialog_txtKmtStatusCode");
			InsuPolicyDialog_txtVinkulacija1RedSifra = (String) request.get("InsuPolicyDialog_txtVinkulacija1RedSifra");
			InsuPolicyDialog_txtIznosOsiguraneSvote = (BigDecimal) request.get("InsuPolicyDialog_txtIznosOsiguraneSvote");
			InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID = (BigDecimal) request.get("InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID");
			InsuPolicyDialog_txtIznosIznosVinkuliranUKorist = (BigDecimal) request.get("InsuPolicyDialog_txtIznosIznosVinkuliranUKorist");
			InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID = (BigDecimal) request.get("InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID");
			InsuPolicyDialog_txtRBAVinkulacijaSifra = (String) request.get("InsuPolicyDialog_txtRBAVinkulacijaSifra");
			InsuPolicyDialog_txtDodatniPodaci = (String) request.get("InsuPolicyDialog_txtDodatniPodaci");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {
			response.put("RealEstate_txtInspolInd", RealEstate_txtInspolInd);
			response.put("ip_activ_and_pay", ip_activ_and_pay);
			response.put("RealEstate_txtCollMvpPonder", RealEstate_txtCollMvpPonder);
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("RealEstate_txtCollMvpPonderMin", RealEstate_txtCollMvpPonderMin);
			response.put("RealEstate_txtCollMvpPonderMax", RealEstate_txtCollMvpPonderMax);
			response.put("RealEstate_txtPonAvailValue", RealEstate_txtPonAvailValue);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYDIALOGDELETEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String InsuPolicyDialog_txtActB = null;
		public String InsuPolicyDialog_txtCodeB = null;
		public Date InsuPolicyDialog_txtDateFromB = null;
		public Date InsuPolicyDialog_txtDateSecValB = null;
		public Date InsuPolicyDialog_txtDateUntilB = null;
		public String InsuPolicyDialog_txtPlaceB = null;
		public String InsuPolicyDialog_txtReplaceB = null;
		public BigDecimal InsuPolicyDialog_txtSecuValB = null;
		public BigDecimal IP_CONTRACTOR_B = null;
		public BigDecimal IP_CUR_ID_B = null;
		public BigDecimal IP_IC_ID_B = null;
		public BigDecimal IP_POL_HOLDER_B = null;
		public BigDecimal IP_TYPE_ID_B = null;
		public BigDecimal USE_ID_B = null;
		public Timestamp InsuPolicyDialog_txtOpeningTsNF = null;
		public Timestamp InsuPolicyDialog_txtUserLockNF = null;
		public BigDecimal USE_OPEN_ID = null;
		public BigDecimal IP_ID = null;
		public BigDecimal COL_HEA_ID = null;
		public String IP_SPEC_STAT = null;
		public BigDecimal use_id = null;
		public String IP_SPEC_STAT_B = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_COL_CAT_ID = null;
		public BigDecimal RealEstate_COL_TYPE_ID = null;
		public BigDecimal RealEstate_REAL_EST_TYPE = null;
		public String InsuPolicyDialog_txtWrnStatusCode = null;
		public String InsuPolicyDialog_txtKmtStatusCode = null;
		public BigDecimal org_uni_id = null;

		// out args
		public String RealEstate_txtInspolInd = null;
		public String ip_activ_and_pay = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public BigDecimal RealEstate_txtCollMvpPonderMin = null;
		public BigDecimal RealEstate_txtCollMvpPonderMax = null;
		public BigDecimal RealEstate_txtPonAvailValue = null;
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;

		public INSUPOLICYDIALOGDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			InsuPolicyDialog_txtActB = (String) request.get("InsuPolicyDialog_txtActB");
			InsuPolicyDialog_txtCodeB = (String) request.get("InsuPolicyDialog_txtCodeB");
			InsuPolicyDialog_txtDateFromB = (Date) request.get("InsuPolicyDialog_txtDateFromB");
			InsuPolicyDialog_txtDateSecValB = (Date) request.get("InsuPolicyDialog_txtDateSecValB");
			InsuPolicyDialog_txtDateUntilB = (Date) request.get("InsuPolicyDialog_txtDateUntilB");
			InsuPolicyDialog_txtPlaceB = (String) request.get("InsuPolicyDialog_txtPlaceB");
			InsuPolicyDialog_txtReplaceB = (String) request.get("InsuPolicyDialog_txtReplaceB");
			InsuPolicyDialog_txtSecuValB = (BigDecimal) request.get("InsuPolicyDialog_txtSecuValB");
			IP_CONTRACTOR_B = (BigDecimal) request.get("IP_CONTRACTOR_B");
			IP_CUR_ID_B = (BigDecimal) request.get("IP_CUR_ID_B");
			IP_IC_ID_B = (BigDecimal) request.get("IP_IC_ID_B");
			IP_POL_HOLDER_B = (BigDecimal) request.get("IP_POL_HOLDER_B");
			IP_TYPE_ID_B = (BigDecimal) request.get("IP_TYPE_ID_B");
			USE_ID_B = (BigDecimal) request.get("USE_ID_B");
			InsuPolicyDialog_txtOpeningTsNF = (Timestamp) request.get("InsuPolicyDialog_txtOpeningTsNF");
			InsuPolicyDialog_txtUserLockNF = (Timestamp) request.get("InsuPolicyDialog_txtUserLockNF");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			IP_ID = (BigDecimal) request.get("IP_ID");
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			IP_SPEC_STAT = (String) request.get("IP_SPEC_STAT");
			use_id = (BigDecimal) request.get("use_id");
			IP_SPEC_STAT_B = (String) request.get("IP_SPEC_STAT_B");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_COL_CAT_ID = (BigDecimal) request.get("RealEstate_COL_CAT_ID");
			RealEstate_COL_TYPE_ID = (BigDecimal) request.get("RealEstate_COL_TYPE_ID");
			RealEstate_REAL_EST_TYPE = (BigDecimal) request.get("RealEstate_REAL_EST_TYPE");
			InsuPolicyDialog_txtWrnStatusCode = (String) request.get("InsuPolicyDialog_txtWrnStatusCode");
			InsuPolicyDialog_txtKmtStatusCode = (String) request.get("InsuPolicyDialog_txtKmtStatusCode");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {
			response.put("RealEstate_txtInspolInd", RealEstate_txtInspolInd);
			response.put("ip_activ_and_pay", ip_activ_and_pay);
			response.put("RealEstate_txtCollMvpPonder", RealEstate_txtCollMvpPonder);
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("RealEstate_txtCollMvpPonderMin", RealEstate_txtCollMvpPonderMin);
			response.put("RealEstate_txtCollMvpPonderMax", RealEstate_txtCollMvpPonderMax);
			response.put("RealEstate_txtPonAvailValue", RealEstate_txtPonAvailValue);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYSTATUSUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal IP_ID = null;
		public Date InsuPolicyDialog_txtDateUntil = null;
		public String IP_SPEC_STAT = null;
		public Date InsuPolicyDialog_txtDateFrom = null;
		public Date InsuPolicyDialog_txtDateSecVal = null;
		public String proc_type_flag = null;
		public BigDecimal use_id = null;
		public BigDecimal COL_HEA_ID = null;
		public BigDecimal org_uni_id = null;

		// out args
		public String InsuPolicyDialog_txtSpecActDes = null;

		public INSUPOLICYSTATUSUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			IP_ID = (BigDecimal) request.get("IP_ID");
			InsuPolicyDialog_txtDateUntil = (Date) request.get("InsuPolicyDialog_txtDateUntil");
			IP_SPEC_STAT = (String) request.get("IP_SPEC_STAT");
			InsuPolicyDialog_txtDateFrom = (Date) request.get("InsuPolicyDialog_txtDateFrom");
			InsuPolicyDialog_txtDateSecVal = (Date) request.get("InsuPolicyDialog_txtDateSecVal");
			proc_type_flag = (String) request.get("proc_type_flag");
			use_id = (BigDecimal) request.get("use_id");
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {
			response.put("InsuPolicyDialog_txtSpecActDes", InsuPolicyDialog_txtSpecActDes);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}