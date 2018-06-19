package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.CurrencyLookUpReturnValues;
import hr.vestigo.modules.coreapp.util.SpecialAuthorityManager;
                 
/** 
 * @author HRAZST
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollCommonFunctions extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCommonFunctions.java,v 1.11 2017/07/19 08:04:20 hrazst Exp $";

	CollateralUtil coll_util = null;
	LookUps coll_lookups = null;
	String MASTER_LDBNAME="CollCommonDataLDB";
	
    public CollCommonFunctions(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
        coll_lookups = new LookUps(ra);
    }
    
    public void CollCommonFunctions_SE() {               
//        if(ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")){
//            coll_util.setRequired(new String[]{"Coll_txtUsedInRecovery", "Coll_txtKindOfRecoveryCode", "Coll_txtKindOfRecoveryDesc", 
//                    "Coll_txtFullAmountRecovery", "Coll_txtFullAmountRecoveryCur", "Coll_txtRecoveryAmount", "Coll_txtRecoveryAmountCur", 
//                    "Coll_txtRecoveryDate", "Coll_txtRecoveryComment","Coll_txtRealizationAmount","Coll_txtRealizationAmountCur",
//                    "Coll_txtRealizationDate","Coll_txtKindOfRealization","Coll_txtCostRealization","Coll_txtPartOfComplex"}, true);
//        }
        if(ra.getScreenContext().equalsIgnoreCase("scr_update") && coll_util.CheckIfExistFieldOnScreen("Coll_txtPartOfComplex")){
            coll_util.enableField("Coll_txtPartOfComplex", 0);
            coll_util.enableField("Coll_txtUsedInRecovery", 0);
            ra.invokeValidation("Coll_txtPartOfComplex");
            ra.invokeValidation("Coll_txtUsedInRecovery");
        }
        this.SetDefaultValuesOnLDB();
        if(coll_util.CheckIfExistFieldOnScreen("Coll_txtKindOfRecoveryCode")) ra.invokeValidation("Coll_txtKindOfRecoveryCode");
      
        if(!(new SpecialAuthorityManager(ra)).checkSpecialAuthorityCodeForApplicationUser("aix", "COLRECOV"))
        {
            coll_util.enableFields(new String[]{"Coll_txtUsedInRecovery", "Coll_txtKindOfRecoveryCode", "Coll_txtKindOfRecoveryDesc", 
                  "Coll_txtFullAmountRecovery", "Coll_txtFullAmountRecoveryCur", "Coll_txtRecoveryAmount", "Coll_txtRecoveryAmountCur", 
                  "Coll_txtRecoveryDate", "Coll_txtRecoveryComment","Coll_txtRealizationAmount","Coll_txtRealizationAmountCur",
                  "Coll_txtRealizationDate","Coll_txtKindOfRealization","Coll_txtCostRealization","Coll_txtPartOfComplex"}, 2);
        } 
    }     
    
    public boolean Coll_txtFullAmountRecoveryCur_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtFullAmountRecoveryCur","CUR_ID_FULL_AMOUNT"});
            return true;
        }
        
        LookUps.CurrencyLookUpReturnValues value = coll_lookups.new CurrencyLookUpReturnValues();
        value=coll_lookups.CurrencyNewLookUp(MASTER_LDBNAME, null, "Coll_txtFullAmountRecoveryCur", null, "CUR_ID_FULL_AMOUNT");
        
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtFullAmountRecoveryCur", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "CUR_ID_FULL_AMOUNT", value.curId);        
        
        return true;
    }
    
    public boolean Coll_txtRecoveryAmountCur_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtRecoveryAmountCur","CUR_ID_RECOVERY_AMOUNT"});
            return true;
        }
        
        LookUps.CurrencyLookUpReturnValues value = coll_lookups.new CurrencyLookUpReturnValues();
        value=coll_lookups.CurrencyNewLookUp(MASTER_LDBNAME, null, "Coll_txtRecoveryAmountCur", null, "CUR_ID_RECOVERY_AMOUNT");

        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRecoveryAmountCur", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "CUR_ID_RECOVERY_AMOUNT", value.curId);        
        return true;
    }  
    
    public boolean Coll_txtRealizationAmountCur_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtRealizationAmountCur", "CUR_ID_REALIZATION_AMOUNT"});
            return true;
        }
        
        LookUps.CurrencyLookUpReturnValues value = coll_lookups.new CurrencyLookUpReturnValues();
        value=coll_lookups.CurrencyNewLookUp(MASTER_LDBNAME, null, "Coll_txtRealizationAmountCur", null, "CUR_ID_REALIZATION_AMOUNT");

        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRealizationAmountCur", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REALIZATION_AMOUNT", value.curId);        
        return true;
    } 
    
    public boolean Coll_txtRecoveryAmount_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        BigDecimal full_amount = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "Coll_txtFullAmountRecovery");
        BigDecimal recovery_amount = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "Coll_txtRecoveryAmount");
            
        if(!ra.getScreenContext().equalsIgnoreCase("scr_update") && full_amount!=null && recovery_amount!=null && full_amount.compareTo(recovery_amount)<0) {
            ra.showMessage("infzstColl02");
            return false;
        }        
        
        return true;
    }
    

    
    public boolean Coll_txtUsedInRecovery_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        LookUps.SystemCodeLookUpReturnValues value = null;
        if(!(ElValue==null || ElValue.equals(""))){
            value = coll_lookups.new SystemCodeLookUpReturnValues();
            value = coll_lookups.SystemCodeValue(MASTER_LDBNAME, "Coll_txtUsedInRecovery", null, "used_in_recovery");
            if(value!=null) ra.setAttribute(MASTER_LDBNAME, "Coll_txtUsedInRecovery", value.sysCodeValue);
        }        
        if(ra.getScreenContext().equalsIgnoreCase("scr_update")){
            if(value!=null && "D".equals(value.sysCodeValue.toUpperCase())){
                coll_util.enableFields(new String[]{"Coll_txtRecoveryAmount","Coll_txtRecoveryAmountCur","Coll_txtRealizationAmount","Coll_txtRealizationAmountCur"}, 0);
            }else{
                if(!"D".equals(ra.getAttribute(MASTER_LDBNAME, "Coll_txtPartOfComplex"))){
                    coll_util.enableFields(new String[]{"Coll_txtRecoveryAmount","Coll_txtRecoveryAmountCur","Coll_txtRealizationAmount","Coll_txtRealizationAmountCur"}, 1);
                }else{
                    coll_util.enableFields(new String[]{"Coll_txtRealizationAmount","Coll_txtRealizationAmountCur"}, 1);    
                } 
            }
        }
        if(value==null && !(ElValue==null || ElValue.equals(""))) return false;
        return true;
    }
    
    public boolean Coll_txtPartOfComplex_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        LookUps.SystemCodeLookUpReturnValues value = null;
        if(!(ElValue==null || ElValue.equals(""))){
            value = coll_lookups.new SystemCodeLookUpReturnValues();
            value = coll_lookups.SystemCodeValue(MASTER_LDBNAME, "Coll_txtPartOfComplex", null, "used_in_recovery");
            if(value!=null) ra.setAttribute(MASTER_LDBNAME, "Coll_txtPartOfComplex", value.sysCodeValue);
        }        
        if(ra.getScreenContext().equalsIgnoreCase("scr_update")){
            //ako je odabrana vrijednost D onda se polja iza array-a otprotektiraju za unos
            if(value!=null && "D".equals(value.sysCodeValue.toUpperCase())){
                coll_util.enableFields(new String[]{"Coll_txtRecoveryAmount","Coll_txtRecoveryAmountCur","Coll_txtRecoveryComment"}, 0);
            }else{
                if(!"D".equals(ra.getAttribute(MASTER_LDBNAME, "Coll_txtUsedInRecovery"))){
                    coll_util.enableFields(new String[]{"Coll_txtRecoveryAmount","Coll_txtRecoveryAmountCur","Coll_txtRecoveryComment"}, 1);
                }else{
                    coll_util.enableFields(new String[]{"Coll_txtRecoveryComment"}, 1);    
                }                
                coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtRecoveryComment"});
            }
        }
        if(value==null && !(ElValue==null || ElValue.equals(""))) return false;
        return true;
    }    
        
    public boolean Coll_txtKindOfRecovery_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtKindOfRecoveryCode","Coll_txtKindOfRecoveryDesc"});
            return true;
        }
        if(ElName.equals("Coll_txtKindOfRecoveryCode")) coll_util.clearField(MASTER_LDBNAME, "Coll_txtKindOfRecoveryDesc");
        else coll_util.clearField(MASTER_LDBNAME, "Coll_txtKindOfRecoveryCode");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();        
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "Coll_txtKindOfRecoveryCode", "Coll_txtKindOfRecoveryDesc", "kind_of_recovery");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtKindOfRecoveryCode", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtKindOfRecoveryDesc", value.sysCodeDesc);        
        return true;
    }  
    
    public boolean Coll_txtEconomicLife_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")) {
            return true;
        }
        if(((Integer) ElValue)<0 || ((Integer) ElValue)>300){
            ra.showMessage("infzstColl05");
            return false;
        }     
        return true;
    }
    
    public boolean Coll_RecoveryFilled_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat();     
        return true;
    }    
    
    public boolean Coll_txtForSale_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) return true;
        return coll_lookups.ConfirmDN(MASTER_LDBNAME, "Coll_txtForSale", ElValue);
    }
    
    public boolean Coll_txtTakeOverBank_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) return true;
        return coll_lookups.ConfirmDN(MASTER_LDBNAME, "Coll_txtTakeOverBank", ElValue);
    }
    
    public boolean Coll_txtCostRealizationCur_FV(String ElName, Object ElValue, Integer LookUp) {
        this.SetRecoveryProcStat(); 
        if(ElValue==null || ElValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"Coll_txtCostRealizationCur", "REALIZATION_COST_CUR_ID"});
            return true;
        }
        
        LookUps.CurrencyLookUpReturnValues value = coll_lookups.new CurrencyLookUpReturnValues();
        value=coll_lookups.CurrencyNewLookUp(MASTER_LDBNAME, null, "Coll_txtCostRealizationCur", null, "REALIZATION_COST_CUR_ID");
        
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtCostRealizationCur", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "REALIZATION_COST_CUR_ID", value.curId);        
        
        return true;
    }
        
    private void SetRecoveryProcStat(){
        String[] recoveryFields=new String[]{"Coll_txtUsedInRecovery", "Coll_txtKindOfRecoveryCode", "Coll_txtKindOfRecoveryDesc", 
                "Coll_txtFullAmountRecovery", "Coll_txtFullAmountRecoveryCur", "Coll_txtRecoveryAmount", "Coll_txtRecoveryAmountCur", 
                "Coll_txtRecoveryDate", "Coll_txtRecoveryComment","Coll_txtPartOfComplex"};        
        for (String field : recoveryFields) {
            Object value = ra.getAttribute(MASTER_LDBNAME, field);
            if(value!=null && !value.toString().trim().equals("")){
                ra.setAttribute(MASTER_LDBNAME, "Coll_txtRecoveryProcStat","D");
                return;
            }            
        }        
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRecoveryProcStat","N");
    } 
    
    private void SetDefaultValuesOnLDB(){       
        coll_util.SetDefaultValue(MASTER_LDBNAME, "Coll_txtSumHipToFirstRBACurChar", "EUR");
        //coll_util.SetDefaultValue(MASTER_LDBNAME, "Coll_txtPartOfComplex", "N");
    } 
    
    public void recovery_amount_history(){        
        ra.loadScreen("CollRecAmountHistory");
    }
}
