package hr.vestigo.modules.collateral.jcics.co39;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.math.*;

import hr.vestigo.framework.common.*;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO39 implements MappingDecl, Decl, DeclLoxonMappingInterface {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/DeclCO39.java,v 1.4 2008/05/21 11:39:09 hraamh Exp $";
	private Map response = new HashMap();

	public DeclCO39() {
	}

	//	in args
	public TableData collateralCMIDList = new TableData();

	//	out args
	public TableData CollateralMainDetails = new TableData();
	public TableData CollateralBillOfExchange = new TableData();
	public TableData CollateralCashDeposit = new TableData();
	public TableData LongtermCollateralContract = new TableData();
	public TableData CollateralDebenture = new TableData();
	public TableData CollateralEvaluation = new TableData();
	public TableData CollateralGuarantee = new TableData();
	public TableData CollateralLifeInsurance = new TableData();
	public TableData CollateralOwner = new TableData();
	public TableData CollateralPlane = new TableData();
	public TableData CollateralRealEstate = new TableData();
	public TableData CollateralSecurity = new TableData();
	public TableData CollateralShareInCompany = new TableData();
	public TableData CollateralVehicles = new TableData();
	public TableData CollateralVessels = new TableData();
	public TableData CollPledgeList = new TableData();
	public TableData InsurancePolicy = new TableData();
	public TableData CollateralOtherColl = new TableData();

	// inner classes
	public GETACCOUNTLINKEDCOLLATERALSBYCUSTOMERIDLIST getaccountlinkedcollateralsbycustomeridlist = null;
	public GETCOLLATERALCASHDEPOSITBYNATURALKEY getcollateralcashdepositbynaturalkey = null;
	public GETCOLLATERALLIFEINSURANCEBYNATURALKEY getcollaterallifeinsurancebynaturalkey = null;
	public GETCOLLATERALREALESTATEBYNATURALKEY getcollateralrealestatebynaturalkey = null;
	public GETCOLLATERALVEHICLEBYNATURALKEY getcollateralvehiclebynaturalkey = null;
	public GETCOLLATERALVESSELBYNATURALKEY getcollateralvesselbynaturalkey = null;

	public void setRequest(Map request) {
		collateralCMIDList = (TableData) request.get("collateralCMIDList");
	}

	public Map getResponse() {
		/*
		response.put("CollateralMainDetails", CollateralMainDetails);
		response.put("CollateralBillOfExchange", CollateralBillOfExchange);
		response.put("CollateralCashDeposit", CollateralCashDeposit);
		response.put("LongtermCollateralContract", LongtermCollateralContract);
		response.put("CollateralDebenture", CollateralDebenture);
		response.put("CollateralEvaluation", CollateralEvaluation);
		response.put("CollateralGuarantee", CollateralGuarantee);
		response.put("CollateralLifeInsurance", CollateralLifeInsurance);
		response.put("CollateralOwner", CollateralOwner);
		response.put("CollateralPlane", CollateralPlane);
		response.put("CollateralRealEstate", CollateralRealEstate);
		response.put("CollateralSecurity", CollateralSecurity);
		response.put("CollateralShareInCompany", CollateralShareInCompany);
		response.put("CollateralVehicles", CollateralVehicles);
		response.put("CollateralVessels", CollateralVessels);
		response.put("CollPledgeList", CollPledgeList);
		response.put("InsurancePolicy", InsurancePolicy);
		response.put("CollateralOtherColl", CollateralOtherColl);

		*/
		return response;
	}
	
	public void setResponseMap(Map responseData) {
		this.response=responseData;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("getAccountLinkedCollateralsByCustomerIDList")) {
			getaccountlinkedcollateralsbycustomeridlist = new GETACCOUNTLINKEDCOLLATERALSBYCUSTOMERIDLIST(request);
			return getaccountlinkedcollateralsbycustomeridlist;
		} else if(mapping.equals("getCollateralCashDepositByNaturalKey")) {
			getcollateralcashdepositbynaturalkey = new GETCOLLATERALCASHDEPOSITBYNATURALKEY(request);
			return getcollateralcashdepositbynaturalkey;
		} else if(mapping.equals("getCollateralLifeInsuranceByNaturalKey")) {
			getcollaterallifeinsurancebynaturalkey = new GETCOLLATERALLIFEINSURANCEBYNATURALKEY(request);
			return getcollaterallifeinsurancebynaturalkey;
		} else if(mapping.equals("getCollateralRealEstateByNaturalKey")) {
			getcollateralrealestatebynaturalkey = new GETCOLLATERALREALESTATEBYNATURALKEY(request);
			return getcollateralrealestatebynaturalkey;
		} else if(mapping.equals("getCollateralVehicleByNaturalKey")) {
			getcollateralvehiclebynaturalkey = new GETCOLLATERALVEHICLEBYNATURALKEY(request);
			return getcollateralvehiclebynaturalkey;
		} else if(mapping.equals("getCollateralVesselByNaturalKey")) {
			getcollateralvesselbynaturalkey = new GETCOLLATERALVESSELBYNATURALKEY(request);
			return getcollateralvesselbynaturalkey;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class GETACCOUNTLINKEDCOLLATERALSBYCUSTOMERIDLIST implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public TableData customerIDList = new TableData();

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETACCOUNTLINKEDCOLLATERALSBYCUSTOMERIDLIST(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			customerIDList = (TableData) request.get("customerIDList");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GETCOLLATERALCASHDEPOSITBYNATURALKEY implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public String internaId = null;
		public String accountId = null;

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETCOLLATERALCASHDEPOSITBYNATURALKEY(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			internaId = (String) request.get("internaId");
			accountId = (String) request.get("accountId");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GETCOLLATERALLIFEINSURANCEBYNATURALKEY implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public String insuranceCompanyID = null;
		public String insuranceContrNumb = null;

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETCOLLATERALLIFEINSURANCEBYNATURALKEY(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			insuranceCompanyID = (String) request.get("insuranceCompanyID");
			insuranceContrNumb = (String) request.get("insuranceContrNumb");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GETCOLLATERALREALESTATEBYNATURALKEY implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public String court = null;
		public String cadastralUnit = null;
		public String cadastralPlotNumber = null;
		public String ZKUNubmer = null;
		public String ZKUSubNumber = null;
		public String partOwnerShare = null;

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETCOLLATERALREALESTATEBYNATURALKEY(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			court = (String) request.get("court");
			cadastralUnit = (String) request.get("cadastralUnit");
			cadastralPlotNumber = (String) request.get("cadastralPlotNumber");
			ZKUNubmer = (String) request.get("ZKUNubmer");
			ZKUSubNumber = (String) request.get("ZKUSubNumber");
			partOwnerShare = (String) request.get("partOwnerShare");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GETCOLLATERALVEHICLEBYNATURALKEY implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public String chasisNumber = null;

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETCOLLATERALVEHICLEBYNATURALKEY(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			chasisNumber = (String) request.get("chasisNumber");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GETCOLLATERALVESSELBYNATURALKEY implements MappingDecl, DeclLoxonMappingInterface {
		public Map response = new HashMap();

		//	in args
		public String vesselMark = null;

		//	out args
		public TableData CollateralMainDetails = new TableData();
		public TableData CollateralBillOfExchange = new TableData();
		public TableData CollateralCashDeposit = new TableData();
		public TableData LongtermCollateralContract = new TableData();
		public TableData CollateralDebenture = new TableData();
		public TableData CollateralEvaluation = new TableData();
		public TableData CollateralGuarantee = new TableData();
		public TableData CollateralLifeInsurance = new TableData();
		public TableData CollateralOwner = new TableData();
		public TableData CollateralPlane = new TableData();
		public TableData CollateralRealEstate = new TableData();
		public TableData CollateralSecurity = new TableData();
		public TableData CollateralShareInCompany = new TableData();
		public TableData CollateralVehicles = new TableData();
		public TableData CollateralVessels = new TableData();
		public TableData CollPledgeList = new TableData();
		public TableData InsurancePolicy = new TableData();
		public TableData CollateralOtherColl = new TableData();

		public GETCOLLATERALVESSELBYNATURALKEY(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			vesselMark = (String) request.get("vesselMark");
		}

		public Map getResponse() {
			/*
			response.put("CollateralMainDetails", CollateralMainDetails);
			response.put("CollateralBillOfExchange", CollateralBillOfExchange);
			response.put("CollateralCashDeposit", CollateralCashDeposit);
			response.put("LongtermCollateralContract", LongtermCollateralContract);
			response.put("CollateralDebenture", CollateralDebenture);
			response.put("CollateralEvaluation", CollateralEvaluation);
			response.put("CollateralGuarantee", CollateralGuarantee);
			response.put("CollateralLifeInsurance", CollateralLifeInsurance);
			response.put("CollateralOwner", CollateralOwner);
			response.put("CollateralPlane", CollateralPlane);
			response.put("CollateralRealEstate", CollateralRealEstate);
			response.put("CollateralSecurity", CollateralSecurity);
			response.put("CollateralShareInCompany", CollateralShareInCompany);
			response.put("CollateralVehicles", CollateralVehicles);
			response.put("CollateralVessels", CollateralVessels);
			response.put("CollPledgeList", CollPledgeList);
			response.put("InsurancePolicy", InsurancePolicy);
			response.put("CollateralOtherColl", CollateralOtherColl);
			*/
			return response;
		}
		
		public void setResponseMap(Map responseData) {
			this.response=responseData;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}