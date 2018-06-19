/*
 * Created on 2007.03.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollRevaluation  extends Handler{//komentare dodat
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollRevaluation.java,v 1.9 2009/11/30 08:10:16 hrakis Exp $";
    
	public CollRevaluation(ResourceAccessor ra) {
		super(ra);
		// TODO Auto-generated constructor stub
	}
    
	public void CollRevaluation_SE(){    	
    	String scrContext=null;
    	Date datumObrade;
    	if(!ra.isLDBExists("CollRevaluationLDB")){
			ra.createLDB("CollRevaluationLDB");
    	}

    	//    	dohvat trenutnog datuma
        
    	GregorianCalendar gc=new GregorianCalendar();

        datumObrade = new Date(gc.getTime().getTime());
    	
        ra.setAttribute("CollRevaluationLDB","CollRevaluation_txtDateOfReval",datumObrade);
        
        ra.setCursorPosition("CollRevaluation_txtCategoryCode"); 

    	//ra.createActionListSession("tblColControlListing",false);
        return;
    }	
	
	
	public boolean CollRevaluation_txtCategoryCode_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollRevaluationLDB";
		String coll_category;
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollRevaluation_txtCategoryCode", "");                        
			ra.setAttribute(ldbName, "CollRevaluation_txtCategoryName", "");                        
			ra.setAttribute(ldbName, "COL_CAT_ID", null);                               
			return true;                                                                                 
	 	}   
	
        ra.setAttribute(ldbName, "CollRevaluation_txtCategoryName", "");
        ra.setAttribute(ldbName, "COL_CAT_ID", null);   	
	     
		//LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
		//lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCategoryName", "name");
		//lookUpRequest.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
		//lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCategoryCode", "code");
        
        LookUpRequest lookUpRequest = new LookUpRequest("RevAmoCategoryLookUp");
        lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCategoryCode", "Šifra");
        lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCategoryName", "Naziv");
        
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012"); 
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
        /*
		coll_category =(String) ra.getAttribute(ldbName,"CollRevaluation_txtCategoryCode");
		if (coll_category!=null && !coll_category.equals("")) 
			if (!coll_category.equals("NEKR") && !coll_category.equals("PLOV") && !coll_category.equals("POKR") && !coll_category.equals("VOZI")) 
				ra.setContext("CollRevaluation_txtCollSubtypeCode","fld_protected");
			//	ra.setContext("CollRevaluation_txtSubtypeOfCollSubtypeCode","fld_protected");
		if (coll_category!=null && !coll_category.equals(""))
		
            if (!coll_category.equals("NEKR"))
		{
			ra.setContext("CollRevaluation_txtCountyCode","fld_protected");
			ra.setContext("CollRevaluation_txtLocationCode","fld_protected");
			ra.setContext("CollRevaluation_txtDistrictCode","fld_protected");
			
		}
        */
		return true;
	}
	
    /*
	public boolean CollRevaluation_txtCollTypeCode_FV(String elementName, Object elementValue, Integer lookUpType){
	 	String coll_category;
	 	BigDecimal coll_type;
	 	String col_cat_id;
		String ldbName = "CollRevaluationLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollRevaluation_txtCollTypeCode", "");                        
			ra.setAttribute(ldbName, "CollRevaluation_txtCollTypeName", "");                        
			ra.setAttribute(ldbName, "COL_TYPE_ID", null);                               
			return true;                                                                                 
	 	}   
	
	     ra.setAttribute(ldbName, "CollRevaluation_txtCollTypeName", "");
	     ra.setAttribute(ldbName, "COL_TYPE_ID", null);   	
	     
	     //col_cat_id = (String)ra.getAttribute(ldbName, "COL_CAT_ID");
	     coll_category =(String) ra.getAttribute(ldbName,"CollRevaluation_txtCategoryCode");
	    
	     //u LDB stavlja kategoriju po kojoj æe se podici lookUp
	     if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
	 		ra.createLDB("CollateralTypeLookUpLDB");
	 	} 
	     ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory",coll_category);
	     if (coll_category==null || coll_category.equals("")) 
	     {
	     	ra.showMessage("wrnclt96");
			return false;
	     }
			
	     ra.setAttribute(ldbName, "dummyStr", "");
	     ra.setAttribute(ldbName, "dummyBd", null);
	     ra.setAttribute(ldbName, "dummyDt", null);
	     
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp");
			
			lookUpRequest.addMapping(ldbName, "COL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollTypeName", "coll_type_name");
			lookUpRequest.addMapping(ldbName, "dummyStr", "coll_period_rev");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mn");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mn");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummyStr","coll_category");
			lookUpRequest.addMapping(ldbName, "dummyStr", "coll_hypo_fidu");
			lookUpRequest.addMapping(ldbName, "dummyStr", "hypo_fidu_name");
		    lookUpRequest.addMapping(ldbName, "dummyStr", "coll_anlitika");
		    lookUpRequest.addMapping(ldbName, "dummyStr", "coll_accounting");
		    lookUpRequest.addMapping(ldbName, "dummyStr", "accounting_name");
		    lookUpRequest.addMapping(ldbName, "dummyDt", "coll_date_from");
		    lookUpRequest.addMapping(ldbName, "dummyDt", "coll_date_until");
			
		   	try {
				ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
			} catch (NothingSelected ns) {
				return false;
			}
			coll_category =(String) ra.getAttribute(ldbName,"CollRevaluation_txtCategoryCode");
		    coll_type = (BigDecimal)ra.getAttribute(ldbName, "COL_TYPE_ID");
			if (coll_category!=null && !coll_category.equals("")) 
			{	if (coll_category.equals("NEKR")) 
					if (coll_type!=null) 
						if (!coll_type.equals(new BigDecimal("9777")) &&  !coll_type.equals(new BigDecimal("12777")) && !coll_type.equals(new BigDecimal("8777"))&& !coll_type.equals(new BigDecimal("10777")) && !coll_type.equals(new BigDecimal("7777")) )
							ra.setContext("CollRevaluation_txtCollSubtypeCode","fld_protected");
				//			ra.setContext("CollRevaluation_txtSubtypeOfCollSubtypeCode","fld_protected");
		
						
				if (coll_category.equals("POKR"))
					if (coll_type!=null) 
						if (!coll_type.equals(new BigDecimal("60777")) && !coll_type.equals(new BigDecimal("61777")))
							ra.setContext("CollRevaluation_txtCollSubtypeCode","fld_protected");	
			
			}	
			return true; 
	}	
    */
	/*
	public boolean CollRevaluation_txtCollSubtypeCode_FV(String elementName, Object elementValue, Integer lookUpType) {
		String coll_category;
		//BigDecimal coll_type;
		String ldbName = "CollRevaluationLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollRevaluation_txtCollSubtypeCode", "");                        
			ra.setAttribute(ldbName, "CollRevaluation_txtCollSubtypeName", "");                        
			ra.setAttribute(ldbName, "SEC_TYP_ID", null);   
			ra.setAttribute(ldbName,"dummyDt",null);
			return true;                                                                                 
	 	}   
	
		
		
	     ra.setAttribute(ldbName, "CollRevaluation_txtCollSubtypeName", "");
	     ra.setAttribute(ldbName, "SEC_TYP_ID", null);   	
	     ra.setAttribute(ldbName,"dummyDt",null);
	     coll_category = (String)ra.getAttribute(ldbName, "CollRevaluation_txtCategoryCode");
	     //coll_type = (BigDecimal)ra.getAttribute(ldbName, "COL_TYPE_ID");
	    
	     if(ra.getAttribute("CollRevaluationLDB", "COL_TYPE_ID") == null) {
			//Nije moguce odabrati podvrstu  dok se ne odabere vrsta kolaterala.
			ra.showMessage("wrnclt131");
			return false;
		}
	     
	     //trazi se da postoji za taj lookUp??
	    // if (!ra.isLDBExists("CollSecPaperLDB")) {
	 	//	ra.createLDB("CollSecPaperLDB");
	 	//}
	     
	     String lookup_name = "";
			BigDecimal ipIcId = null;
			if (coll_category.equalsIgnoreCase("POKR")) {
				if (!ra.isLDBExists("MovTypLDB")) {
					ra.createLDB("MovTypLDB");
				} 
				  
			    if (ra.getAttribute(ldbName, "COL_TYPE_ID") != null) {
		       		ipIcId = (BigDecimal) ra.getAttribute(ldbName, "COL_TYPE_ID");
		       }else{
		       		ra.showMessage("wrnclt129");
		       		return false;
		       }     
			   ra.setAttribute("MovTypLDB", "mov_col_typ_id", ipIcId);  	
			   lookup_name = "MovableTypeLookUp";
				 
			}
			else if (coll_category.equalsIgnoreCase("PLOV")) {
				lookup_name = "VesselTypeLookUp";						
			} else if (coll_category.equalsIgnoreCase("VOZI")) {
					lookup_name = "VehTypeLookUp";						
			} else 
			{
				lookup_name = "RealEstateTypeLookUp";
				if (!ra.isLDBExists("RealEstateTypeLookUpLDB")) {                                                 
			           ra.createLDB("RealEstateTypeLookUpLDB");                                                      
			    }  
				if(ra.getAttribute("CollRevaluationLDB", "COL_TYPE_ID") != null) {
					ra.setAttribute("RealEstateTypeLookUpLDB", "collateralTypeId", (java.math.BigDecimal)ra.getAttribute("CollRevaluationLDB", "COL_TYPE_ID"));  
				}
			}	 
			  
			ra.setAttribute(ldbName, "dummyStr", null);
			ra.setAttribute(ldbName, "CollRevaluation_txtCollSubtypeName", "");
	 
			LookUpRequest lookUpRequest = new LookUpRequest(lookup_name); 
			  
			if (coll_category.equalsIgnoreCase("POKR")) {
				lookUpRequest.addMapping(ldbName, "SEC_TYP_ID", "mov_typ_id");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeCode", "mov_typ_code");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeName", "mov_typ_dsc");		
			}  else if (coll_category.equalsIgnoreCase("PLOV")) {
				lookUpRequest.addMapping(ldbName, "SEC_TYP_ID", "ves_typ_id");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeCode", "ves_code");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeName", "ves_dsc");	
			}  else if (coll_category.equalsIgnoreCase("VOZI")) {
				lookUpRequest.addMapping(ldbName, "SEC_TYP_ID", "veh_gro_id");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeCode", "veh_gro_code");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeName", "veh_gro_desc");						
			} else { 
				lookUpRequest.addMapping(ldbName,"SEC_TYP_ID","real_es_type_id");
				lookUpRequest.addMapping(ldbName,"CollRevaluation_txtCollSubtypeCode", "real_es_type_code");
				lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCollSubtypeName", "real_es_type_desc");
				lookUpRequest.addMapping(ldbName, "dummyDt", "real_es_date_from");
				lookUpRequest.addMapping(ldbName, "dummyDt", "real_es_date_unti");
			}
				
			//coll_category =(String) ra.getAttribute(ldbName,"CollRevaluation_txtCategoryCode");
		    
			
						
			try {
					ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
					ra.showMessage("err012");
					return false;
			} catch (NothingSelected ns) {
					return false; 
			}
			

			return true;		
	
	}
*/
	
	/*
	 * public boolean CollRevaluation_txtSubtypeOfCollSubtypeCode_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		
					
		return true;     		

	}
	*/
	/*		
	public boolean CollRevaluation_txtCountyCode_FV(String elementName, Object elementValue, Integer lookUpType) {
//		RealEstate_txtRevReCounty
	 	//RealEstate_COL_COUNTY
		String ldbName = "CollRevaluationLDB";
	 	java.math.BigDecimal countyTypeId = new java.math.BigDecimal("3999.0");//????????
	 	
	 	ra.setAttribute(ldbName, "dummyStr", "");
	 	
	 	if (elementValue == null || ((String) elementValue).equals("")) {
		    
		     ra.setAttribute(ldbName, "CollRevaluation_txtCountyCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtCountyName", null);
		     ra.setAttribute(ldbName, "COUNTY_ID", null);
		    
		     ra.setAttribute(ldbName, "CollRevaluation_txtLocationCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtLocationName", null);
		     ra.setAttribute(ldbName, "LOCAT_ID", null);
		     
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictName", null);
		     ra.setAttribute(ldbName, "DISTR_ID", null);
		     
		     return true;
		 }
	 	
	 	if (ra.getCursorPosition().equals("CollRevaluation_txtCountyCode")) {
		     ra.setAttribute(ldbName, "COUNTY_ID", null);
		} 
	 	
	 	 if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping(ldbName, "COUNTY_ID", "pol_map_id");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCountyCode", "code");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtCountyName", "name");
		
		try {
		     ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		} catch (NothingSelected ns) {
		    return false;
		}
	 	
	 	return true;  
	}
	
	
	public boolean CollRevaluation_txtLocationCode_FV(String elementName, Object elementValue, Integer lookUpType) {
//		RealEstate_txtRevRePlace
	 	//RealEstate_COL_PLACE
		String ldbName = "CollRevaluationLDB";
	 	java.math.BigDecimal zupanija = (java.math.BigDecimal) ra.getAttribute(ldbName, "COUNTY_ID");
	 	if(zupanija == null){
	 		ra.showMessage("wrnclt80");
	 		//Nije moguce odabrati mjesto ako nije odabrana zupanija
	 		return false;
	 	}
	 	
	 	
	 	
	 	ra.setAttribute(ldbName, "dummyStr", "");
	 	
		BigDecimal countyId = null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute(ldbName, "dummyStr", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtLocationCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtLocationName", "");
		     ra.setAttribute(ldbName, "LOCAT_ID", null);
		    
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictName", "");
		     ra.setAttribute(ldbName, "DISTR_ID", null);
		     
		     
		     return true;
		 }
		 
		 if (ra.getCursorPosition().equals("CollRevaluation_txtLocationCode")) {
		     ra.setAttribute(ldbName, "LOCAT_ID", null);
		 } 
		 
		
		 if (!ra.isLDBExists("PolMapPlacesInCountyLookUpLDB")) {
		     ra.createLDB("PolMapPlacesInCountyLookUpLDB");
		 }
		 if (ra.getAttribute(ldbName, "COUNTY_ID") != null) {
		 	countyId = (BigDecimal) ra.getAttribute(ldbName, "COUNTY_ID");
//		 	System.out.println("ZUPANIJA je za mjesta   " + countyId );
		 }
			
		 ra.setAttribute("PolMapPlacesInCountyLookUpLDB", "bDCountyId", countyId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PolMapPlacesInCountyLookUp");
		
		 lookUpRequest.addMapping(ldbName, "LOCAT_ID", "pol_map_id");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtLocationCode", "code");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtLocationName", "name"); 
		
		 try {
		     ra.callLookUp(lookUpRequest);
		 } catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		 } catch (NothingSelected ns) {
		     return false;
		 }
		
	 	zupanija = null;
	 	return true;    		
		
	}
	
	
	
	public boolean CollRevaluation_txtDistrictCode_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollRevaluationLDB";
		java.math.BigDecimal mjesto = (java.math.BigDecimal) ra.getAttribute(ldbName, "LOCAT_ID");
		if(mjesto == null){
	 		ra.showMessage("wrnclt81");
	 		//Nije moguce odabrati gradsku cetvrt ako nije odabrano mjesto
	 		return false;
	 	}
		
		java.math.BigDecimal districtTypeId = new java.math.BigDecimal("5854877003.0");
		BigDecimal placeId=null;
		ra.setAttribute(ldbName, "dummyStr", "");	
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictCode", "");
		     ra.setAttribute(ldbName, "CollRevaluation_txtDistrictName", "");
		     ra.setAttribute(ldbName, "DISTR_ID", null);
		     
		     return true;
		 }
		
		
		
		 if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
		 }
		 if (ra.getAttribute(ldbName, "LOCAT_ID") != null) {
		 	placeId = (BigDecimal) ra.getAttribute(ldbName, "LOCAT_ID");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", placeId);
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", districtTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
		 
		 lookUpRequest.addMapping(ldbName, "DISTR_ID", "pol_map_id");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtDistrictCode", "code");
		 lookUpRequest.addMapping(ldbName, "CollRevaluation_txtDistrictName", "name"); 
		
		 try {
		     ra.callLookUp(lookUpRequest);
		 } catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		 } catch (NothingSelected ns) {
		     return false;
		 }
		
		
		
		mjesto = null;
	 	return true;
	 }
	
	
	public boolean CollRevaluation_txtDateOfReval_FV() {
		Date dateOfReval = (Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateOfReval");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		
		if(dateOfReval != null && current_date != null)
			if (current_date.before(dateOfReval)) {
				ra.showMessage("wrnclt121");
				return false;
			}
		 
		
		return true;		 
	}
	
	public boolean CollRevaluation_txtRevalPercentage_FV() {
		String ldbName = "CollRevaluationLDB";
		BigDecimal percentage_bd = ((BigDecimal)ra.getAttribute(ldbName,"CollRevaluation_txtRevalPercentage"));
		double percentage;
		if (percentage_bd != null) 
		{
				percentage = percentage_bd.doubleValue();
				if (percentage<0 || percentage>100)
				{
					ra.showMessage("wrnclt40");
					return false;
				}
				return true;
		}
		return true;
	}*/
	
	
	public boolean CollRevaluation_txtDateFromEval_FV() {
		Date dateFromEval = (Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateFromEval");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		
		if(dateFromEval != null && current_date != null)
			if (current_date.before(dateFromEval)) {
				ra.showMessage("wrnclt121");
				return false;
			}
		 
		java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateFromEval");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateUntilEval");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
		return true;	
	
	}
	/*
	public boolean CollRevaluation_txtDateUntilEval_FV() {
		java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateFromEval");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateUntilEval");
	 	if(datumOd == null && datumDo!=null){
	 	
	 		//Najprije upisite datum OD
	 		ra.showMessage("wrnclt30d");
	 		return false;
	 	}else{
	 		if(datumDo != null){
	 			if(datumDo.before(datumOd)){
	 				//Datum DO ne moze biti manji od datuma OD.
	 		 		ra.showMessage("wrnclt30c");
	 				return false;
	 			}
	 		}
	 	
	 	}
	 	
	 	return true; 
	}
	public boolean CollRevaluation_txtNegativeOrPositiveReval_FV(String ElName, Object ElValue, Integer LookUp) {
		String ldbName = "CollRevaluationLDB";
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute(ldbName, "CollRevaluation_txtNegativeOrPositiveReval", "");
			ra.setAttribute(ldbName, "CollRevaluation_txtNegativeOrPositiveRevalDesc", "");
				return true;
		}



		LookUpRequest request = new LookUpRequest("RevaFlagInd");		

		request.addMapping(ldbName, "CollRevaluation_txtNegativeOrPositiveReval", "Ind");
		request.addMapping(ldbName, "CollRevaluation_txtNegativeOrPositiveRevalDesc", "Opis");

	
		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		return true;
}
*/
		
	
    public void batch_sir() throws VestigoTMException{
     	String bankSign=null;
     	String param=null;
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(713861724));
		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
		
		String col_typ_code =(String) ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtCategoryCode");
		Date last_date=(Date) ra.getAttribute("CollRevaluationLDB","CollRevaluation_txtDateFromEval");
        
        if((col_typ_code==null) || (last_date==null)){
            throw new VestigoTMException("Not all data available for batch process!");
        }
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		param = bankSign + ";" + col_typ_code + ";" + last_date;	
		System.out.println(param);
		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
}
