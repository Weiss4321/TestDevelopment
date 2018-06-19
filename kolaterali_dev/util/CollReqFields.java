//created 2014.09.25
package hr.vestigo.modules.collateral.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;

/**
 *
 * @author hrazst
 */
public class CollReqFields {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/CollReqFields.java,v 1.17 2016/11/23 13:04:11 hrazst Exp $";
    
    public Vector<CollReqFieldData> GetRealEstateReqData(){
      Vector<CollReqFieldData> v= new Vector<CollReqFieldData>();
      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_COL_TYPE_ID", "Niste unijeli tip kolaterala."));  
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_REAL_EST_TYPE", "Niste unijeli vrstu nekretnine."));  
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_REAL_EST_COURT_ID", "Niste unijeli sud nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtRealEstSpecStat", "Niste unijeli ekonomsku neovisnost."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "Kol_txtCRMHnb_REstate", "Niste unijeli HNB ekonomsku neovisnost."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_REAL_EST_CADA_MUNC", "Niste unijeli katastarsku opæinu nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtRealEstLandPart", "Niste unijeli katastarsku èesticu nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtSqrm2", "Niste unijeli površinu nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtBuildPermInd","Niste unijeli da li kolateral ima indikator o postojanju uporabne dozvole."));

      //uvjet da se provjerava da li je unesena grad. dozvola je da je tip kolaterala 7777 i tip nekretnine 1222
      Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_COL_TYPE_ID", new BigDecimal("7777"), true)); //ako je zemljiste 
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("2222"), false)); // i nije poljoprivredno
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "Kol_txtBuildPerm", "Niste unijeli da li kolateral ima graðevinsku dozvola.", con));
      
      //uvjet da se provjerava da li je unesena legalnost je da je tip kolaterala razlicit od 7777 - zemljista
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_COL_TYPE_ID", new BigDecimal("7777"), false));
      con.add(new CollReqFieldDataCondition("Kol_txtBuildPerm", new String(""), true));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "Kol_txtLegality", "Niste unijeli legalnost nekretnine.",con ));
      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "Coll_txtRePurpose", "Niste unijeli namjenu nekretnine."));     
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtConstructionRight", "Niste unijeli pravo graðenja."));  
      
    //-------------------------------------------------------------------------------------------------------------------------------------------------
      //UVJETI ZA KAT/KATOVA/LIFT KADA JE INSERT CONTEXT... TJ. UNOS NOVOGA 
      //uvjet da se provjerava da li je kat obavezan
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("5222"), true)); // ako je stan
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtFloor", "Niste unijeli kat.", con, null,"scr_change")); 
      
      //uvjet da se provjerava da li je broj katova
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("5222"), true)); // ako je stan
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtTotalFloors", "Niste unijeli broj katova.", con,null, "scr_change")); 
      
      //uvjet da se provjerava da li je broj katova
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_txtTotalFloors", new BigDecimal("1156953223"), true)); // ako je stambena zgrada
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtFloors", "Niste unijeli broj katova.", con, null,"scr_change"));   
      
      //uvjet da se provjerava da li je unešen lift
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_COL_TYPE_ID", new BigDecimal("7777"), false)); //ako nije zemljiste 
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("4222"), false)); // i nije kuca
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtLift", "Niste unijeli da li postoji lift.", con, null,"scr_change"));    
      
      //PONOVLJENI UVJETI ZA KAT/KATOVA/LIFT KADA JE UPDATE CONTEXT... TJ. AZURIRANJE STAROG. AKO JE VEÆ POPUNJEN KAT/KATOVA/LIFT ONDA MORA I DALJE BITI OBAVEZAN.
      //AKO NIJE POPUNJEN NA STAROM NEKOM ONDA NIJE OBAVEZAN DOK SE NE POPUNI PRVI PUT
      //uvjet da se provjerava da li je kat obavezan
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("5222"), true)); // ako je stan
      con.add(new CollReqFieldDataCondition("RealEstate_txtFloorOLD", "", false)); // ako stara vrijednost je popunjena
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtFloor", "Niste unijeli kat 1.", con, null, "scr_update")); 
      
      //uvjet da se provjerava da li je broj katova
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("5222"), true)); // ako je stan
      con.add(new CollReqFieldDataCondition("RealEstate_txtFloorOLD", "", false)); // ako stara vrijednost je popunjena
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtTotalFloors", "Niste unijeli broj katova.", con, null, "scr_update")); 
      
      //uvjet da se provjerava da li je broj katova
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_txtTotalFloors", new BigDecimal("1156953223"), true)); // ako je stambena zgrada
      con.add(new CollReqFieldDataCondition("RealEstate_txtTotalFloorsOLD", "", false)); // ako stara vrijednost je popunjena
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtFloors", "Niste unijeli broj katova.", con, null, "scr_update"));   
      
      //uvjet da se provjerava da li je unešen lift
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_COL_TYPE_ID", new BigDecimal("7777"), false)); //ako nije zemljiste 
      con.add(new CollReqFieldDataCondition("RealEstate_REAL_EST_TYPE", new BigDecimal("4222"), false)); // i nije kuca
      con.add(new CollReqFieldDataCondition("RealEstate_txtLiftOLD", "", false)); // ako stara vrijednost je popunjena
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtLift", "Niste unijeli da li postoji lift.", con, null, "scr_update"));  
      //-------------------------------------------------------------------------------------------------------------------------------------------------
      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtRealEstPdesc", "Niste unijeli opis nekretnine u zemljišno-knjižnom ulošku."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtStreet", "Niste unijeli ulicu nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtHousenr", "Niste unijeli kuæni broj nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_POL_MAP_ID_AD", "Niste unijeli mjesto nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_POS_OFF_ID_AD", "Niste unijeli poštu nekretnine."));        
      
      //uvjet da se provjerava da li je unesen procjenitelj da je opis procjene 1 ili 2 
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstate_txtEstnMark", "3", false));
      con.add(new CollReqFieldDataCondition("RealEstate_txtEstnMark", "4", false));
      con.add(new CollReqFieldDataCondition("RealEstate_txtEstnMark", "5", false));
      con.add(new CollReqFieldDataCondition("RealEstate_txtEstnMark", "6", false));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtEUseIdLogin", "Niste unijeli procjenitelja nekretnine.", con));      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtEUseIdLoginRC", "Niste unjeli tvrtku procjenitelja.", con));
      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtEstnValu", "Niste unijeli tržišnu vrijednost nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_REAL_EST_NM_CUR_ID", "Niste unijeli valutu tržišne vrijednosti nekretnine."));      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtNewBuildVal", "Niste unijeli novu graðevinsku vrijednost nekretnine."));      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtEstnMark", "Niste odabrali opis procjene nekretnine."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtEstnDate", "Niste unijeli datum procjene nekretnine."));      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtAssessmentMethod1Code", "Niste odabrali metodu procjene nekretnine 1.")); 
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtAssessmentMethod2Code", "Niste odabrali metodu procjene nekretnine 2.")); 
      //v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtAccuracyEstimatesCode", "Niste unijeli toènost provedbe procjene.")); 
      //v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtDeadLinesCode", "Niste unijeli podatak o poštivanju rokova."));       
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtComDoc", "Niste unijeli da li je predana sva dokumentacija."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "ColLow_txtEligibility", "Niste unijeli prihvatljivost kolaterala."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtRecLop", "Niste unijeli upisano pravo banke."));
      
      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstateDialogLDB_B","Reb_RealEstate_txtB2HNB","",false)); //Ako stara vrijednost je popunjena onda je polje B2 HNB stand obaverno
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtB2HNB", "Niste unijeli B2 HNB stand.", con)); 

      con = new Vector<CollReqFieldDataCondition>();
      con.add(new CollReqFieldDataCondition("RealEstateDialogLDB_B","Reb_RealEstate_txtB2IRB","",false)); //Ako stara vrijednost je popunjena onda je polje B2 IRB stand obaverno      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtB2IRB", "Niste unijeli B2 IRB stand.", con)); 
      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_COL_COUNTY", "Niste unijeli regionalno-lokacijska oznaka nekretnine - županiju."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_COL_PLACE", "Niste unijeli regionalno-lokacijska oznaka nekretnine - mjesto."));
      //v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtRegionCode", "Niste unijeli regionalno-lokacijska oznaka nekretnine - regiju."));
      
      //v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtTypeTV", "Niste odabrali vrstu tržišne vrijednosti nekretnine.")); 
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtPricem2", "Niste unijeli cijenu po kvadratu nekretnine."));      
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtAmortAge", "Niste unijeli amoritzacijski period."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_AMORT_PER_CAL_ID", "Niste unijeli period izraèuna iznosa amortizacije."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtAmortPerCal", "Niste unijeli period izraèuna amortizacije."));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "ColRba_txtEligibility1", "Niste unijeli RBA eligibility.", "infclt103"));
      v.add(new CollReqFieldData("","RealEstateDialogLDB", "RealEstate_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
      
      v.add(new CollReqFieldData("","CollCommonDataLDB", "Coll_txtEconomicLife", "Niste unijeli ekonomski vijek kolaterala.")); 
      
      return v;
    }  
    
    public Vector<CollReqFieldData> GetVehiReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB,"Coll_txtCollTypeCode","Niste unijeli tip vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Coll_txtSecTypeCode","Niste unijeli vrstu vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVIN","Niste unijeli broj šasije."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehColour","Niste unijeli boju vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehMadeYear","Niste unijeli godinu proizvodnje."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehMade","Niste unijeli marku vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehType","Niste unijeli tip vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehModel","Niste unijeli model vozila."));        
        v.add(new CollReqFieldData("",CollHeadLDB,"KolLow_txtEligibility","Niste unijeli mišljenje pravne službe."));
        
        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2HNB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 HNB stand obaverno
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2HNB", "Niste unijeli B2 HNB stand.", con)); 

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2IRB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 IRB stand obaverno      
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2IRB", "Niste unijeli B2 IRB stand.", con));         
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS","Niste unijeli CRM mišljenje."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCRMHnb", "Niste unijeli CRM HNB mišljenje."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehState","Niste unijeli stanje vozila."));
        //v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtEUsePersonId", "Niste unijeli procjenitelja."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiAmount", "Niste unijeli vrijednost vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiCur", "Niste unijeli valutu nominalne/procijenjene vrijednosti."));   
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtEstnDate", "Niste unijeli datum procjene."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod1Code", "Niste unijeli metodu procjene 1."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod2Code", "Niste unijeli metodu procjene 2."));
        v.add(new CollReqFieldData("",CollHeadLDB,"Coll_txtComDoc", "Niste unijeli da li je knjižica vozila dostavljena."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehEngineType", "Niste unijeli vrstu motora."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehPowerKw", "Niste unijeli snagu vozila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehCcm", "Niste unijeli radni obujam motora."));        
        // ako je dostavljena knjizica vozila 
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition("CollHeadLDB","Coll_txtComDoc", "D", true)); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehVehLicence", "Niste unijeli serijski broj knjižice vozila.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehFPlate", "Niste unijeli registraciju vozila.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Vehi_txtVehFPlateDate", "Niste unijeli datum registracije.",con));        
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehLicRetOwn","Niste unijeli da li je knjižica vraæena."));
        // ako je knjizica vozila vracena 
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition("Vehi_txtVehLicRetOwn", "D", true)); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Vehi_txtVehLicRetDat","Niste unijeli datum vraæanja knjižice vozila.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Vehi_txtVehLicRetWho","Niste unijeli tko je preuzeo knjižicu.",con));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehInsurance","Niste unijeli da li je polica osiguranja dostavljena.")); 
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiValu", "Niste unijeli nominalnu vrijednost c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNmValCurr", "Niste unijeli valutu nominalne vrijednosti c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti c.o.")); 
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollOfficer","Niste unijeli kolateral officera."));
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtTypeTV","Niste unijeli vrstu tržišne vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility","Niste unijeli RBA prihvatljivost."));        
        
        v.add(new CollReqFieldData("","CollCommonDataLDB", "Coll_txtEconomicLife", "Niste unijeli ekonomski vijek kolaterala."));        
        
        return v;
    } 
      
    public Vector<CollReqFieldData> GetVesselReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "Niste unijeli vrstu plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtName", "Niste unijeli naziv plovila."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Vessel_txtHarbour", "Niste unijeli luèku kapetaniju plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txMadeYear", "Niste unijeli godinu proizvodnje plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Vessel_txtNumEngine", "Niste unijeli broj motora plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtSign", "Niste unijeli znak raspoznavanja plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtBrutto", "Niste unijeli brutto tonažu plovila.")); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtNetto", "Niste unijeli netto tonažu plovila."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtNumber", "Niste unijeli broj uloška plovila."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtComDoc","Niste unijeli da li je knjižica dostavljena."));
        v.add(new CollReqFieldData("",CollHeadLDB,"KolLow_txtEligibility","Niste unijeli mišljenje pravne službe."));        

        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2HNB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 HNB stand obaverno
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2HNB", "Niste unijeli B2 HNB stand.", con)); 

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2IRB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 IRB stand obaverno      
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2IRB", "Niste unijeli B2 IRB stand.", con));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS","Niste unijeli CRM mišljenje.")); 
        //v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtEUsePersonId", "Niste unijeli procjenitelja."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiAmount","Niste unijeli vrijednost plovila."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiCur","Niste unijeli valutu nominalne/procijenjene vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtEstnDate","Niste unijeli datum procjene."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod1Code","Niste unijeli metodu procjene 1."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod2Code","Niste unijeli metodu procjene 2."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiValu", "Niste unijeli nominalnu vrijednost c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNmValCurr", "Niste unijeli valutu nominalne vrijednosti c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti c.o.")); 
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollOfficer","Niste unijeli kolateral officera."));
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtTypeTV","Niste unijeli vrstu tržišne vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility","Niste unijeli RBA prihvatljivost."));        
        
        v.add(new CollReqFieldData("","CollCommonDataLDB", "Coll_txtEconomicLife", "Niste unijeli ekonomski vijek kolaterala."));
        
        return v;
    } 
    
    public Vector<CollReqFieldData> GetMovableReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip."));

        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtCollTypeCode", "2AVI", true));       
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "Niste unijeli vrstu.", con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehModel","Niste unijeli marku/model.", con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Mov_txtSerNum","Niste unijeli serijski broj.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehMadeYear","Niste unijeli godinu proizvodnje.", con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Mov_txtRegSign","Niste unijeli nacionalna i reg. oznaka.", con));      
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Mov_txtHrReg","Niste unijeli R.br. u hrv. registru zrakoplova.", con));       

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtCollTypeCode", "2RAC", true));       
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehModel","Niste unijeli marku/model.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Mov_txtSerNum","Niste unijeli serijski broj.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehMadeYear","Niste unijeli godinu proizvodnje.", con));
        
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtCollTypeCode", "2STR", true));       
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "Niste unijeli vrstu.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehModel","Niste unijeli marku/model.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Mov_txtSerNum","Niste unijeli serijski broj.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Vehi_txtVehMadeYear","Niste unijeli godinu proizvodnje.", con));
                
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
        v.add(new CollReqFieldData("",CollHeadLDB,"KolLow_txtEligibility","Niste unijeli mišljenje pravne službe."));        

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2HNB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 HNB stand obaverno
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2HNB", "Niste unijeli B2 HNB stand.", con)); 

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2IRB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 IRB stand obaverno      
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2IRB", "Niste unijeli B2 IRB stand.", con));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS","Niste unijeli CRM mišljenje."));   
        //v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtEUsePersonId", "Niste unijeli procjenitelja."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiAmount","Niste unijeli vrijednost."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiCur","Niste unijeli valutu nominalne/procijenjene vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtEstnDate","Niste unijeli datum procjene."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod1Code","Niste unijeli metodu procjene 1."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod2Code","Niste unijeli metodu procjene 2."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiValu", "Niste unijeli nominalnu vrijednost c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNmValCurr", "Niste unijeli valutu nominalne vrijednosti c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti c.o.")); 
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollOfficer","Niste unijeli kolateral officera."));
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtTypeTV","Niste unijeli vrstu tržišne vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility","Niste unijeli RBA prihvatljivost."));        
        
        v.add(new CollReqFieldData("","CollCommonDataLDB", "Coll_txtEconomicLife", "Niste unijeli ekonomski vijek kolaterala."));
        
        return v;
    } 
    
    public Vector<CollReqFieldData> GetSuppliesReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));    
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Ves_txtDsc", "Niste unijeli opis."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtKeeper", "Niste unijeli kod koga se èuvaju zalihe."));
        
        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB, "Coll_txtCollTypeCode", "2ZAL", true));       
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtLocation", "Niste unijeli lokaciju.", con)); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtAdresa", "Niste unijeli adresu.", con)); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtPlace", "Niste unijeli mjesto.", con));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtMinValue", "Niste unijeli da li se dužnik obavezuje na minimalnu vrijednost."));
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollSecPaperDialogLDB,"Supp_txtMinValue", "D", true));       
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Supp_txtMinAmount", "Niste unijeli minimalna vrijednost zaliha.", con));
        
        //v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtEUsePersonId", "Niste unijeli procjenitelja."));
        v.add(new CollReqFieldData("",CollHeadLDB,"KolLow_txtEligibility","Niste unijeli mišljenje pravne službe."));        

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2HNB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 HNB stand obaverno
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2HNB", "Niste unijeli B2 HNB stand.", con)); 

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB,"Coll_txtB2IRB_OLD","",false)); //Ako stara vrijednost je popunjena onda je polje B2 IRB stand obaverno      
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtB2IRB", "Niste unijeli B2 IRB stand.", con));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS","Niste unijeli CRM mišljenje.")); 
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiAmount","Niste unijeli vrijednost."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB,"Kol_txtVehiCur","Niste unijeli valutu nominalne/procijenjene vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtEstnDate","Niste unijeli datum procjene."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod1Code","Niste unijeli metodu procjene 1."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAssessmentMethod2Code","Niste unijeli metodu procjene 2."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiValu", "Niste unijeli nominalnu vrijednost c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNmValCurr", "Niste unijeli valutu nominalne vrijednosti c.o."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti c.o.")); 
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollOfficer","Niste unijeli kolateral officera."));
        //v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtTypeTV","Niste unijeli vrstu tržišne vrijednosti."));        
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility","Niste unijeli RBA prihvatljivost."));        
        
        v.add(new CollReqFieldData("","CollCommonDataLDB", "Coll_txtEconomicLife", "Niste unijeli ekonomski vijek kolaterala."));
        
        return v;
    } 
    
    public Vector<CollReqFieldData> GetGaraReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS", "Niste unijeli CRM mišljenje."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCRMHnb", "Niste unijeli CRM HNB mišljenje."));
          
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtGuarIssRegNo", "Niste unijeli izdavatelja."));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", "Niste unijeli državu."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtGuarAmount", "Niste unijeli iznos garancije."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtGuarCur", "Niste unijeli valutu garancije."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtIntFeeInd", "Niste unijeli da li se izos uveæava za kamate i naknade.")); 
        
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB, "Coll_txtCollTypeCode", "4SLETOCO", true));
        con.add(new CollReqFieldDataCondition(CollHeadLDB, "Coll_txtCollTypeCode", "4BLETOCO", true));      
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtAmortInd", "Niste unijeli da li je garancija amortizirajuæa.", con));         
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtRespiro", "Niste unijeli da li ima respiro period.",con));
        
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollSecPaperDialogLDB, "Kol_txtRespiro", "D", true));          
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtRespiroDate", "Niste unijeli respiro datum.", con));    
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtGuarDatnFrom", "Niste unijeli datum od kada vrijedi garancija."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtGuarDatnUnti", "Niste unijeli datum do kada vrijedi garancija."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtComDoc", "Niste unijeli da li je predana sva dokumentacija."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtFirstCall", "Niste unijeli da li je garancija prihvatljiva na prvi poziv."));

        v.add(new CollReqFieldData("",CollHeadLDB, "KolLow_txtEligibility", "Niste unijeli mišljenje pravne službe."));
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility", "Niste unijeli RBA prihvatljivost."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti collateral officer-a.")); 
        
        return v;
    } 
    
    public Vector<CollReqFieldData> GetInsuPolReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS", "Niste unijeli CRM mišljenje."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCRMHnb", "Niste unijeli CRM HNB mišljenje."));
          
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpConData", "Niste unijeli ugovaratelja."));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpInsData", "Niste unijeli osiguranika."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtInsPolName", "Niste unijeli osiguravatelja."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "Niste unijeli vrstu police."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtInsPolNumber", "Niste unijeli broj policu."));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpIssueDate", "Niste unijeli datum izdavanja."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpValiFrom", "Niste unijeli datum od kada vrijedi polica."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpValiUntil", "Niste unijeli datum do kada vrijedi polica."));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpNomValue", "Niste unijeli nominalnu vrijednost police osiguranja."));         
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpNomCurr", "Niste unijeli valtu nominalne vrijednost."));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpAmount", "Niste unijeli iznos premije osiguranja."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpAmoCur", "Niste unijeli valutu premije osiguranja."));                
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpPaidFrom", "Niste unijeli datum od kada je plaæena premija."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpPaidUntil", "Niste unijeli datum do kada je plaæena premija."));
                
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpStatus", "Niste unijeli status police."));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtIpSpecStatus", "Niste unijeli napomenu o polici."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtWrnStatusCode", "Niste unijeli status slanja obavijesti i opomena."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtKmtStatusCode", "Niste unijeli status poveæanja kamatne stope."));     
                
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB, "Coll_txtCollTypeCode", "7PLK", true));          
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtAcumBuyValue", "Niste unijeli akumuliranu ili otkupnu vrijednost.", con));

        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollHeadLDB, "Coll_txtCollTypeCode", "7PLK", true));          
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtRecLop", "Niste unijeli da li je potpisana izjava.", con));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti collateral officer-a.")); 
        
        v.add(new CollReqFieldData("",CollHeadLDB, "KolLow_txtEligibility", "Niste unijeli mišljenje pravne službe."));
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility", "Niste unijeli RBA prihvatljivost."));
        
        return v;
    } 

    public Vector<CollReqFieldData> GetCesijeReqData(){
        Vector<CollReqFieldData> v = new Vector<CollReqFieldData>();
        Vector<CollReqFieldDataCondition> con = new Vector<CollReqFieldDataCondition>();
        String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
        String CollHeadLDB = "CollHeadLDB";

        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtCollTypeCode", "Niste unijeli tip."));
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtContractTypeCode", "Niste unijeli vrstu ugovora."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "SPEC_STATUS", "Niste unijeli CRM mišljenje."));  
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCRMHnb", "Niste unijeli CRM HNB mišljenje."));
          
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanja", "Niste unijeli vrstu potraživanja.")); 
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaDate", "Niste unijeli datum sklapanja ugovora."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaNaplata", "Niste unijeli da li je cesija radi naplate plasmana."));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaLimit", "Niste unijeli da li teretimo limit."));        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaRegres", "Niste unijeli da li je regres."));
        
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanja", "3", false));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaDateExp", "Niste unijeli datum stanja potraživanja.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Coll_txtCdeCurr", "Niste unijeli valutu garancije/cesije.")); 
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaMatDate", "Niste unijeli datum dospijeæa potraživanja.",con));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtGuarAmount", "Niste unijeli iznos.",con));

        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaAllAmount", "Niste unijeli ukupan iznos potraživanja."));
            
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaLista", "Niste unijeli da li je dostavljena lista."));
        con = new Vector<CollReqFieldDataCondition>();
        con.add(new CollReqFieldDataCondition(CollSecPaperDialogLDB, "Kol_txtCesijaLista", "D", true));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaListaDatum", "Niste unijeli datum dostave liste.",con));
        
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika", "Niste unijeli dinamiku dostave liste."));
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCedentMb", "Niste unijeli cedent-a."));                
        v.add(new CollReqFieldData("",CollSecPaperDialogLDB, "Kol_txtCesusMb", "Niste unijeli cesus-a."));
   
        v.add(new CollReqFieldData("",CollHeadLDB, "Coll_txtNomiDate", "Niste unijeli datum nominalne vrijednosti collateral officer-a."));
        
        v.add(new CollReqFieldData("",CollHeadLDB, "KolLow_txtEligibility", "Niste unijeli mišljenje pravne službe."));
        v.add(new CollReqFieldData("",CollHeadLDB, "ColRba_txtEligibility", "Niste unijeli RBA prihvatljivost."));
        
        return v;
    } 
    
}

