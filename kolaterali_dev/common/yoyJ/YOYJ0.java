//created 2017.02.13
package hr.vestigo.modules.collateral.common.yoyJ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.yoy6.GCTCData;
import hr.vestigo.modules.collateral.common.yoy6.YOY64;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.StringUtils;

/**
 *
 * @author hrazst
 */
public class YOYJ0 {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyJ/YOYJ0.java,v 1.3 2017/03/02 12:45:58 hrazst Exp $";
    
    private RemoteContext rc;
    private YOYJ1 yoyj1 = null;
    private YOYJ2 yoyj2 = null;
    private YOY64 yoy64 = null;
    private BigDecimal defaultUseId=new BigDecimal("1");
    private BigDecimal defaultOrgId=new BigDecimal("53253");
    private BigDecimal col_lis_typ_id=new BigDecimal(700223);
    
    private CommonCollateralMethods commonMethods=null;
    
    /**
     * Konstruktor commona.
     * @param rc Remote context
     */
    public YOYJ0(RemoteContext rc) throws Exception
    {
        this.rc = rc;
        this.yoyj1= new YOYJ1(rc,defaultUseId,defaultOrgId);

        this.yoy64=new YOY64(rc);
        commonMethods= CollateralCommonFactory.getCommonCollateralMethods(rc);
    }
    
    
    public OutputData openCollateral(InputData colData) throws Exception{
        info("openCollateral - START");
        info(colData.toString());
        OutputData oData = new OutputData();
        String col_num = null;
        
        try {
            if(colData.coll_category.trim().equals("ZADU"))
            {
                oData.col_num=this.otvoriZaduznicu(colData);
            }
            else if(colData.coll_category.trim().equals("MJEN"))
            {
                oData.col_num=this.otvoriMjenicu(colData);
            }
            else
            {
                oData.col_num=this.otvoriDepozit(colData);
            }
        } catch (VestigoTMException e) {
            oData.errorCode=e.getTransactionStatus();
            oData.errorDesc=e.getLogMessage();            
        } catch (Exception e) {
            oData.errorCode=1;
            oData.errorDesc="Greška pri otvaranju kolaterala:\n"+StringUtils.getStackTraceString(e);
        }
        debug("openCollateral - END");
        return oData;
    }
    
    private String otvoriZaduznicu(InputData colData) throws Exception{
        debug("otvoriZaduznicu - START");
        BigDecimal eve_typ_id =  new java.math.BigDecimal("1609373003.0"); 
        BigDecimal col_cat_id = yoyj1.getColCatId(colData.coll_category.trim());
        BigDecimal col_typ_id = yoyj1.getColTypeId(colData.coll_type.trim());
        BigDecimal cus_id = yoyj1.getCusId(colData.register_no);
        Short number = colData.amount.shortValue();
        
        BigDecimal eve_id=yoyj1.insertIntoEvent(eve_typ_id, defaultOrgId, "Otvaranje zaduznice iz APS-a");
        String col_num=this.generateColNum(col_typ_id, "Z");
        
        BigDecimal col_hea_id= yoyj1.insertCollHead(col_typ_id, col_cat_id, eve_id, cus_id, col_num, "3", number);
        GCTCData data=yoy64.getGCTCData(col_cat_id, col_typ_id, null,null);
        yoy64.updateGCTCDataIDs(col_hea_id, data.gctc_id, data.object_type_id, data.property_type_id, data.endorsement_type_id); 
        
        yoyj1.insertCollLoanstock(col_hea_id);
        yoyj1.insertLoanBeneficiary(colData.register_no, cus_id, colData.cusacc_no, null, this.defaultUseId, colData.aps_number);
        
        YOYG0 yoyg0 = new YOYG0(rc, col_hea_id);
        yoyg0.azurirajPrihvatljivosti();   
        debug("otvoriZaduznicu - END");
        return col_num;
    }
    
    private String otvoriMjenicu(InputData colData) throws Exception{
        debug("otvoriMjenicu - START");
        BigDecimal eve_typ_id =  new java.math.BigDecimal("1619348003.0"); 
        BigDecimal col_cat_id = yoyj1.getColCatId(colData.coll_category.trim());
        BigDecimal col_typ_id = yoyj1.getColTypeId(colData.coll_type.trim());
        BigDecimal cus_id = yoyj1.getCusId(colData.register_no);
        Short number = colData.amount.shortValue();
        
        BigDecimal eve_id=yoyj1.insertIntoEvent(eve_typ_id, defaultOrgId, "Otvaranje mjenice iz APS-a");
        String col_num=this.generateColNum(col_typ_id, "Z");
        
        BigDecimal col_hea_id= yoyj1.insertCollHead(col_typ_id, col_cat_id, eve_id, cus_id, col_num, "3", number);
        GCTCData data=yoy64.getGCTCData(col_cat_id, col_typ_id, null,null);
        yoy64.updateGCTCDataIDs(col_hea_id, data.gctc_id, data.object_type_id, data.property_type_id, data.endorsement_type_id); 
        
        yoyj1.insertCollBillExch(col_hea_id, number);
        yoyj1.insertLoanBeneficiary(colData.register_no, cus_id, colData.cusacc_no, col_hea_id, this.defaultUseId, colData.aps_number);

        BigDecimal col_lis_q_id=yoyj1.insertCollListQ(col_lis_typ_id,col_hea_id,"Preuzeto iz APS-a.");
        
        YOYG0 yoyg0 = new YOYG0(rc, col_hea_id);
        yoyg0.azurirajPrihvatljivosti(); 
        debug("otvoriMjenicu - END");
        return col_num;      
    }
    
    private String otvoriDepozit(InputData colData) throws Exception{
        debug("otvoriDepozit - START");
        String col_num=null;
        BigDecimal eve_typ_id =  new java.math.BigDecimal("1619348003.0"); 
        BigDecimal col_cat_id = yoyj1.getColCatId(colData.coll_category.trim());
        BigDecimal col_typ_id = yoyj1.getColTypeId(colData.coll_type.trim());
        BigDecimal owner_cus_id = yoyj1.getCusId(colData.coll_owner_register_no);
        BigDecimal amount = colData.amount;
                
        BigDecimal eve_id=yoyj1.insertIntoEvent(eve_typ_id, defaultOrgId, "Otvaranje depozita iz APS-a");
        this.yoyj2 = new YOYJ2(this.rc, defaultOrgId, defaultUseId, col_cat_id, eve_id);       
        
        Date today = new Date(System.currentTimeMillis());
        Date maxDate = Date.valueOf("9999-12-31");
        String razlog1 = "Depozit u orig. modulu nema datum dospije\u0107a pa isti nije a\u017Euriran. Ostali podaci o depozitu su a\u017Eurirani.";
        String razlog2 = "Depozit je na\u0111en u modulu kolaterala, ali ga nema u podacima iz DWH - u modulu nije a\u017Eurirano stanje.";
        String razlog3 = "Depozit u Siriusu nema upisanu partiju plasmana pa nije automatski kreiran u modulu kolaterala";
        String razlog4 = "";

        CashDepData data = yoyj2.selectDepositData(colData.deposit_account_no);
        //stanje glavnice se dohvaæa iz cusacc_balance
        data.cde_amount = yoyj2.selectDepositBalance(data.cus_acc_id, data.cur_id);        
        BigDecimal exception=yoyj2.selectDepositException(data.cde_account);
        
        String inputStatus = "0";
        boolean insert = false;
        boolean deactivate = false;
        boolean RBA_eligibility = true;
        // Stvaranje objekta sa podacima
        CollCashDepData inputData = this.getInputData(data,colData.coll_type.trim(),exception);
        debug("inputData:"+inputData.toString());
        // Provjera podataka
        int check = inputData.check();
        
        if(check!=0){
            info("Greska pri provjeri parametara za otvaranje depozita = "+ check);
            throw new VestigoTMException(check, "Greska pri provjeri parametara za otvaranje depozita.", "");

        }
        debug("obradjujem partiju depozita: "+inputData.cde_account+"-"+inputData.cde_dep_unti+"-"+inputData.status);
        // Ako su neki potrebni ulazni atributi prazni zamjenjuje ih s zamjenskim
        if((inputData.cde_owner==null)||(inputData.cde_owner.equalsIgnoreCase(""))||(inputData.cde_owner.equalsIgnoreCase("0"))){
            inputStatus="5";
            inputData.cde_owner=inputData.loan_owner;
        }

        // Ako je istekao depozit
        //boolean outOfDateDeposit=false;
        // Ako je status partije takav da vise nije aktivna
        boolean noActiveStatusDeposit = false;
        if (inputData.status != null && (inputData.status.equalsIgnoreCase("C") || inputData.status.equalsIgnoreCase("I") || inputData.status.equalsIgnoreCase("X"))) {
            noActiveStatusDeposit = true;
            RBA_eligibility = false;
        }
        // Ako nema iznosa depozita
        boolean noAmount = false;
        if((inputData.cde_amount==null)||(inputData.cde_amount.compareTo(new BigDecimal(0))==0)){
            noAmount = true;
        }

        // Dohvat id-a valute
        BigDecimal cde_cur_id=null;
        if(StringUtils.isNumeric(inputData.cde_cur)){
            cde_cur_id=yoyj2.selectCurrencyIdWithCodeNum(inputData.cde_cur);
        }else{
            cde_cur_id=yoyj2.selectCurrencyIdWithCodeChar(inputData.cde_cur);
        }

        // Dohvat col_cas_id i col_hea_id iz coll_cahsdep tablice
        BigDecimal col_cas_id = null;
        BigDecimal col_hea_id = null;
        String collateral_status = null;
        Map collCashMap = null;
        try{
            collCashMap = yoyj2.SelectFromCollCashDeposit(inputData.cde_account,inputData.cde_reg_no);
            if(collCashMap!=null){
                insert = false;
                col_cas_id=(BigDecimal)collCashMap.get("col_cas_id");
                col_hea_id=(BigDecimal)collCashMap.get("col_hea_id");
                collateral_status=(String)collCashMap.get("collateral_status");
                if((col_cas_id==null)||(col_hea_id==null)){
                    insert = true;
                }
            }
        }catch(SQLException e){
            if(e.getErrorCode() == 100){
                col_cas_id = null;
                col_hea_id = null;
                collateral_status=null;
                insert = true;
            }else{
                debug(".....SelectFromCollCashDeposit        Message  : " + e.getMessage());
                debug(".....SelectFromCollCashDeposit      Error code : " + e.getErrorCode());
                debug(".....SelectFromCollCashDeposit        SQLState : " + e.getSQLState());
                throw(e);
            }   
        }
        if(collCashMap==null){
            insert = true;
        }else{
            insert = false;
            col_cas_id=(BigDecimal)collCashMap.get("col_cas_id");
            col_hea_id=(BigDecimal)collCashMap.get("col_hea_id");
            collateral_status=(String)collCashMap.get("collateral_status");
            if((col_cas_id==null)||(col_hea_id==null)){
                insert = true;
            }
        }
        collCashMap=null;

        //radi garantnih depozita iz siriusa koji nemaju partiju plasmana i vlasnika plasmana 
        BigDecimal loan_cus_id=null;
        String loan_cus_code=null;
        String loan_cus_name=null;
        Map customerMap=yoyj2.selectFromCustomer(inputData.loan_owner); 
        if(customerMap!=null){
            loan_cus_id=(BigDecimal)customerMap.get("cus_id");
            loan_cus_code=(String)customerMap.get("code");
            loan_cus_name=(String)customerMap.get("name");
        }

        BigDecimal cde_cus_id=null;
        String cde_cus_code=null;
        String cde_cus_name=null;
        if(inputData.loan_owner != null && inputData.loan_owner.equals(inputData.cde_owner)){
            cde_cus_id=loan_cus_id;
            cde_cus_code=loan_cus_code;
            cde_cus_name=loan_cus_name;
        }else{                                                                                 
            customerMap=yoyj2.selectFromCustomer(inputData.cde_owner);  
            if(customerMap!=null){                         
                cde_cus_id=(BigDecimal)customerMap.get("cus_id");
                cde_cus_code=(String)customerMap.get("code");
                cde_cus_name=(String)customerMap.get("name");
            }
        }
        debug("C ....dohvatio vlasnika...loan_owner, cde_owner, cde_cus_id!: "+inputData.loan_owner+"-"+inputData.cde_owner+"-"+cde_cus_id);   

        // Ako nema iznosa partije plasmana 
        boolean yesAccount=true;
        String account ="D";
        Map cusaccExposureMap=null;                                  
        if(inputData.acc_num==null || inputData.acc_num.equalsIgnoreCase("")){
            debug("nema broj partije plasmana na ulazu");
            // ako nema broj partije plasmana na ulazu
            yesAccount=false;
            account ="N";            
        }else{ 
            debug("ima broj partije plasmana na ulazu");
            account ="D";
            cusaccExposureMap = yoyj2.selectFromCusaccExposure(inputData.acc_num);
        }   
        if(cusaccExposureMap==null){
            yesAccount = false;
            account = "N";
            cusaccExposureMap = new HashMap();
        }
        if (!yesAccount) inputStatus="P";

        Date approvalDate=(Date)cusaccExposureMap.get("date");
        String request_no=(String)cusaccExposureMap.get("request_no");
        BigDecimal cus_acc_id=(BigDecimal) cusaccExposureMap.get("cus_acc_id"); 

        debug("obradujem partiju: "+inputData.acc_num+" - "+account+" - " + cus_acc_id + " partija depozita: "+inputData.cde_account+ " cas_exc_id "+inputData.cas_exc_id);

        if(noActiveStatusDeposit && (("3".equals(collateral_status)) || ("F".equals(collateral_status)))){
            deactivate=true;
        }
        debug("D ....da li deaktiviram: "+noActiveStatusDeposit+"-"+collateral_status);       
            
        col_hea_id=yoyj2.putCollHead(col_hea_id,inputData,cde_cur_id,loan_cus_id,cde_cus_id,deactivate,RBA_eligibility,yesAccount);
        if(col_hea_id==null && yesAccount){
            debug("nije upisan/promijenjen slog u tablici COLL_HEAD za acc_no="+inputData.acc_num);
            throw new VestigoTMException(10, "Greska pri upisu u coll_head.", "");
        } 
        debug(" col_hea_id="+col_hea_id);

        // col_cas_id
        col_cas_id=yoyj2.putCollCashdep(col_cas_id,inputData,col_hea_id,cde_cur_id,!noAmount,yesAccount);
        if(col_cas_id==null && yesAccount){
            debug("nije upisan/promijenjen slog u tablici COLL_CASHDEP za acc_no="+inputData.acc_num +" i col_hea_id="+col_hea_id);
            throw new VestigoTMException(11, "Greška pri upisu sloga u tablici COLL_CASHDEP za acc_no="+inputData.acc_num, "");
        } 
        debug(" col_cas_id="+col_cas_id);

        // col_own_id
        BigDecimal col_own_id=null;
        if(!insert){
            col_own_id= yoyj2.selectCollOwnerId(cde_cus_id,col_hea_id);
        }       
        col_own_id=yoyj2.putCollOwner(col_own_id,inputData.cde_owner,col_hea_id,cde_cus_id,cde_cus_code,yesAccount);
        if(col_own_id==null && yesAccount){
            debug("nije upisan slog u tablicu COLL_OWNER za ime="+cde_cus_name +" i col_hea_id="+col_hea_id);
            throw new VestigoTMException(12, "Greška pri upisu sloga u tablici COLL_OWNER za ime="+cde_cus_name , "");
        }
        debug(" col_own_id="+col_own_id);

        // coll_hf_prior_id
        BigDecimal coll_hf_prior_id=null;
        if(!insert){
            coll_hf_prior_id=yoyj2.selectCollHfPriorId(col_cas_id,col_hea_id);
        }
        debug("dohvacen coll_hf_prior_id iz COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id);  
        coll_hf_prior_id=yoyj2.putCollHfPrior(coll_hf_prior_id,inputData,col_hea_id,col_cas_id,cde_cur_id,approvalDate,deactivate,yesAccount,insert);
        if(coll_hf_prior_id==null && yesAccount && insert){   
            debug("nije upisan slog u tablicu COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id); 
            throw new VestigoTMException(13, "Greška pri upisu sloga u COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id , "");
        } else if (coll_hf_prior_id==null) {
            debug("nije dohvacen slog iz tablice COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id); 
        }
        debug(" coll_hf_prior_id="+coll_hf_prior_id);

        // loan_ben_id
        BigDecimal loan_ben_id=null;
        if(!insert){
            loan_ben_id=yoyj2.selectLoanBeneficiaryId(coll_hf_prior_id,inputData.acc_num);
        } 
        debug("dohvacen loan_ben_id iz LOAN_BENEFICIARY za coll_hf_prior_id="+coll_hf_prior_id + " - loan_ben_id="+loan_ben_id);  
        loan_ben_id=yoyj2.putLoanBeneficiary(loan_ben_id, inputData.loan_owner, inputData.acc_num ,coll_hf_prior_id,loan_cus_id,cus_acc_id,request_no,yesAccount,insert);
        if(loan_ben_id==null && yesAccount && insert && loan_cus_id != null){
            debug("nije upisan slog u tablicu LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id);
            throw new VestigoTMException(14, "Greška pri upisu sloga u LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id , "");
        } else if(loan_ben_id==null) {
            debug("nije dohvacen slog u tablici LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id);                            
        }
        debug(" loan_ben_id="+loan_ben_id);

        // Odredjivanje datuma krajnjeg roka dospijeca depozita, samo ako postoji datum dospijeca i ako je razlicit od 31.12.9999
        debug("11 .... odredi datum krajnjeg dospijeca depozita za col_hea_id = "+col_hea_id + " i za datum dospijeca = " + inputData.cde_dep_unti);
        if (inputData.cde_dep_unti != null && !(inputData.cde_dep_unti.equals(""))) {
            if(DateUtils.whoIsOlder(inputData.cde_dep_unti,maxDate) <= 0){
                yoyj2.setFinalMaturityDate(col_hea_id);
            }
        }

        // Postavljanje svih prihvatljivosti 
        debug("2 ....Pozivam common za postavljanje prihvatljivosti...!: "+col_hea_id);  
        if (col_hea_id != null) {
            if (inputData.cde_dep_unti != null) {
                YOYG0 yoyg0 = new YOYG0(rc, col_hea_id);
                yoyg0.azurirajPrihvatljivosti();
            }  
            inputStatus = "U";                       
            if(insert){
                debug("3 ....Pozivam INSERT u COLL_LIST_Q: "+col_hea_id);                                
                yoyj2.insertCollListQ(col_hea_id,"3",deactivate);
                inputStatus = "I";
            }else if(deactivate){
                debug("4 ....Pozivam UPDATE u COLL_LIST_Q: "+col_hea_id+" - " +collateral_status);                                  
                yoyj2.deactivateCollListQ(col_hea_id,collateral_status);
                debug("5 ....Pozivam INSERT u COLL_LIST_Q: "+col_hea_id+" - " +collateral_status);                                     
                yoyj2.insertCollListQ(col_hea_id,collateral_status,deactivate);
                inputStatus = "D";
            } 
            col_num=yoyj2.selectColNum(col_hea_id);
        }     
        
        return col_num;
    }
    
    /**
     * Stvaranje objekta sa podacima koji su proslijeðeni metodi
     * @param iter Iterator sa podacima za objekt
     * @return InputData objekt sa podacima
     * @throws Exception
     */
    private CollCashDepData getInputData(CashDepData data, String col_typ, BigDecimal cas_exc_id) throws Exception{
        CollCashDepData inputData=new CollCashDepData();
        
        inputData.cde_typ=col_typ;
        inputData.cde_cur=data.cde_cur;
        inputData.cde_amount=data.cde_amount;
        
        // mapiranje prolong_flag u cde_prolong
        String cde_prolong = "";
        if ("0".equals(data.prolong_flag)) cde_prolong = "N";
        else if ("1".equals(data.prolong_flag)) cde_prolong = "D";
        
        inputData.cde_prolong=cde_prolong;
        inputData.cde_reg_no="910000";
        inputData.cde_account=data.cde_account;
        inputData.cde_dep_from=data.cde_dep_from;
        inputData.cde_dep_unti=data.cde_dep_unti;
        inputData.cde_owner=data.cde_owner; 
        inputData.acc_num=data.acc_num;
        inputData.loan_owner=data.loan_owner;
        inputData.cas_exc_id=cas_exc_id;
        inputData.status = data.cus_acc_status;
        inputData.trim();
        return inputData;
    }
    
    
    private String generateColNum(BigDecimal col_type_id, String type) throws Exception{
        String shortYear = (new java.sql.Date(System.currentTimeMillis())).toString().substring(2,4);
        String prefix=commonMethods.selectCollTypeCodeById(col_type_id);
        if(prefix==null){
            throw new Exception("col_typ_code is null for "+col_type_id);
        }else{
            prefix=prefix.trim()+shortYear;
        }
        rc.debug("COL_NUM prefiks: "+prefix);
        String col_num=null;
        if (type.equals("M")) {
            col_num=commonMethods.getCollNum("CLT_BE", DateUtils.getCurrentDate(), prefix, null);
        }else{
            col_num=commonMethods.getCollNum("CLT_LG", DateUtils.getCurrentDate(), prefix, null);
        }
        return col_num;    
    }
    
    public void debug(String msg) {
        this.rc.debug("YOYJ0 -> "+msg);
    }

    private void info(String msg) {
        this.rc.info("INFO -> YOYJ0 -> "+msg);
    }    
}


