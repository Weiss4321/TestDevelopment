/*
 * hrazst Created on 2008.06.17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo40;


import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.coreapp.common.yxyE.YXYE0;
import hr.vestigo.modules.financial.common.yfw3.TurnoverSchemeData;
import hr.vestigo.modules.financial.common.yfw3.YFW30;
import hr.vestigo.modules.financial.common.yfwE.*;
import hr.vestigo.modules.financial.common.yfy6.YFY60;
import hr.vestigo.modules.financial.common.yfyE.YFYE0;
import hr.vestigo.modules.financial.share.TurnoverData;

/**
 * @author 
          _______  _______  _______  _______ _________
|\     /|(  ____ )(  ___  )/ ___   )(  ____ \\__   __/
| )   ( || (    )|| (   ) |\/   )  || (    \/   ) (
| (___) || (____)|| (___) |    /   )| (_____    | |
|  ___  ||     __)|  ___  |   /   / (_____  )   | |
| (   ) || (\ (   | (   ) |  /   /        ) |   | |
| )   ( || ) \ \__| )   ( | /   (_/\/\____) |   | |
|/     \||/   \__/|/     \|(_______/\_______)   )_(

 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
  
public class BO400 extends Batch{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo40/BO400.java,v 1.7 2009/09/07 11:43:38 hrazst Exp $";

    private BatchContext bc = null;
    private BO401 bo401=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private String defaultOrgUnitCode="022"; //default organizacijska jedinica na koju se uvijek knjizi
    private BigDecimal defaultOrgUnitId=null; //default organizacijska jedinica na koju se uvijek knjizi
    
    private YFWE1 yfwe1=null;  
    private YFW30 yfw30=null;    
    private YFWE0 yfwe0=null;
    private YFWE4 helper=null; 
    
       
    private ArrayList errorAccountDifference=new ArrayList(); //lista u koju ce se spremati racuni koji imaju konto razlicit od 99029 i 96029(ako ih se nade)
    
    /**
     * datum pustanja revalorizacije , ulazni parametar batcha
     */
    private Date processDate=null;
   
    public String executeBatch(BatchContext batchContext) throws Exception {
        
        this.bc=batchContext;        

        bo401 = new BO401(bc);
        yfwe1 = new YFWE1(bc);
        yfw30 = new YFW30(bc);    
        yfwe0 = new YFWE0(bc,yfwe1,yfw30);
        helper = new YFWE4(bc,yfwe1,new YFYE0(bc),new YFY60(bc),new YXYE0(bc)); 
                
        try {
            checkArguments();
            bc.debug("Argumenti provjereni.");           
        } catch (Exception e) {
            bc.error("Greska pri provjeri argumenata.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        try {
            bo401.insertIntoEvent();            
        }
        catch (Exception e) {
            bc.error("Greska pri insertiranju u event tablicu.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();         
        bc.debug("Event insertiran.");
   
        try {
            startBatch();
        } catch (Exception e) {
            bc.error("Greska u metodi startBatch.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        return returnCode;
    }
    /**MEtoda gdje je razradena logika batcha
     * 
     * @throws Exception
     */
    private void startBatch() throws Exception{
    
        BO401.IteratorAccounts iterAccounts=null;
        BO401.IteratorBalanceData iterBalance=null;
        
        CusAccBalanceData foreignCurrBalance=null;//balance data objekt za stranu valutu dohvacen iz balanca 
        CusAccBalanceData domesticCurrBalance=null;//balance data objekt za domacu valutu dohvacen iz balanca 
      
        boolean booking=true;
        boolean isEmpty=true;
                
        defaultOrgUnitId=bo401.getOrgId(defaultOrgUnitCode);              

        BigDecimal colProId=null;
        BigDecimal tra_typ_id=null;
        int colNumber=0; //broj dobro obradenih slogova        
        colProId=bo401.insertIntoColProc(processDate);    
        
        //dohvacanje accounta za koje se radi revalorizacija
        iterAccounts=bo401.fetchAccounts();        
        
        //svi accounti koji su dohvaceni se iteriraju
        while (iterAccounts.next()){ 
            
            foreignCurrBalance=null;
            domesticCurrBalance=null;
            CustomerAccountData cad=bo401.getCustomerAccountDetails(iterAccounts.cus_acc_id());

            //punjenje PostingDepositData objekta
            PostingDepositData pdd=fillPostingData(iterAccounts.cus_acc_id(), cad);
            
            bc.debug("iterAccounts.cus_acc_id()="+iterAccounts.cus_acc_id());
            iterBalance=bo401.fetchBalanceData(iterAccounts.cus_acc_id(), processDate);             
            
            //ako se nisu dohvatili balansi za partiju onda ce zastavica isEmpty biti true i nece se radit revalorizacija za taj kolateral
            isEmpty=true;
            while(iterBalance.next()){
                isEmpty=false;
                bc.debug("iterBalanceData.account()="+iterBalance.account());
                String account=iterBalance.account().trim();
                bc.debug("account="+account);  
                
                if(account.equals("96029") || account.equals("96019")){                    
                    foreignCurrBalance=fillBallanceDataFromIter(iterBalance);
                }else if(account.equals("99029") || account.equals("99019")){
                    domesticCurrBalance=fillBallanceDataFromIter(iterBalance);
                }else{
                    errorAccountDifference.add("Za cus_id="+iterBalance.cur_id()+" i acc_num_id="+iterBalance.acc_num_id()+" pronaden konto=" + iterBalance.account()+".");
                }
            }
            //zatvaram iterator jel mi vise ne treba
            try {
                iterBalance.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
            
            if(!isEmpty){
                bc.beginTransaction(); 
                
                bc.debug("foreignCurrBalance="+foreignCurrBalance.toString());
                bc.debug("domesticCurrBalance="+domesticCurrBalance.toString());
    
                yfwe1.getExchangeTypes(pdd,foreignCurrBalance.amo_type, foreignCurrBalance.ban_rel_type);
                bc.debug("used exchange_list_type="+pdd.exchange_list_type+"; exch_rate_type="+pdd.exch_rate_type);         
           
                BigDecimal rate = helper.getExchangeRateByType(pdd.exchange_list_type, pdd.exch_rate_type, foreignCurrBalance.cur_id, processDate);            
                BigDecimal valueOfForeignInDomesticCurr=foreignCurrBalance.balance.abs().multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
    
                bc.debug("valueOfForeignInDomesticCurr="+valueOfForeignInDomesticCurr);
                
                BigDecimal revalValue=valueOfForeignInDomesticCurr.subtract(domesticCurrBalance.balance);
                bc.debug("REVALUE: rate="+rate+"; old_amount="+domesticCurrBalance.balance.abs()+"; new_amount="+valueOfForeignInDomesticCurr+";posting_amount="+revalValue);
           
                if(revalValue.compareTo(new BigDecimal(0))==-1){
                    //ISKNJIZAVANJE 
                    bc.debug("ISKNJIZENJE");
                    pdd.posting_amount = revalValue.abs();// radim apsolutnu vrijednost            
                    tra_typ_id = bo401.selectTransactionTypeId(new BigDecimal("28934622.0"));
                    booking=false;
                } else if(revalValue.compareTo(new BigDecimal(0))== 1){
                    //KNJIZENJE
                    bc.debug("KNJIZENJE");
                    pdd.posting_amount = revalValue.abs();
                    tra_typ_id = bo401.selectTransactionTypeId(new BigDecimal("28911742.0"));
                    booking=true;
                } else {
                    bc.debug("No revalue jer je vrijednost za revalorizaciju 0 za cus_acc_bal_id="+foreignCurrBalance.cus_acc_bal_id);
                    continue;
                }            
                
                doRevalue(foreignCurrBalance, domesticCurrBalance, pdd, tra_typ_id, booking);
                bc.info("Uspjesno revalorizirano: cus_acc_no="+cad.cus_acc_no);
                
                colNumber++;
                bc.commitTransaction();
            }
        }
        
       
        bc.debug("errorAccountDifference="+errorAccountDifference);
        bo401.updateColProc(colProId,new BigDecimal(colNumber)); 
        bc.commitTransaction();           
    } 
    
    /**Metoda radi revalorizaciju tecajnih ralika kolaterala, dohvaca i priprema sve parametre potrebne za revalorizaciju
     * 
     * @param cusAccDataForeign
     * @param cusAccDataDomestic
     * @param data
     * @param tra_typ_id
     * @param booking
     * @throws Exception
     */
    public void doRevalue(CusAccBalanceData cusAccDataForeign,CusAccBalanceData cusAccDataDomestic, PostingDepositData data, BigDecimal tra_typ_id, boolean booking) throws Exception {
        bc.debug(" ******************** doRevalue ******************** ");
        
        Integer orderNo1 = null;
        Integer orderNo2 = null;
                
        //copy values
        data.cus_id = cusAccDataForeign.cus_id;
        data.cur_id = cusAccDataForeign.cur_id;
        data.acc_num_id = cusAccDataForeign.acc_num_id;
        
        data.external_flag = "0";
        data.ext_acc_type = null;
        data.ref_cus_acc_no = null;
        
        bc.debug("\n ---- \n ---- doRevalue za cusAccDataForeign:"+cusAccDataForeign.toString());
        bc.debug("\n ---- \n ---- doRevalue za cusAccDataDomestic:"+cusAccDataForeign.toString());
        bc.debug("\n ---- \n ---- doRevalue za PostingDepositData:"+data.toString());
        yfwe1.getCustomerDetails(data);
        BigDecimal dom_cur_id = yfwe1.getDomesticCur_id(data);
        bc.debug("doRevalue:"+data.toString());
        
        if(booking==true){
            orderNo1 = new Integer(1);
            orderNo2 = new Integer(2);        
        }else{
            orderNo1 = new Integer(2);
            orderNo2 = new Integer(1);            
        }
        
        MetaTurnoverData metaData1 = new MetaTurnoverData(bc);
        metaData1.ban_pro_typ_id = data.ban_pro_typ_id;     
        yfwe1.getBankProdTypeData(metaData1, data);
        metaData1.amo_type = cusAccDataForeign.amo_type;
        metaData1.ban_rel_type = cusAccDataForeign.ban_rel_type;
        metaData1.account = cusAccDataForeign.account;
        bc.debug("doRevalue(): metaData1.account = cusAccData.account = " + cusAccDataForeign.account);
        metaData1.pro_typ_id = cusAccDataForeign.pro_typ_id;
         
        TurnoverSchemeData turSchemeData1 = yfw30.getDescriptionFromTurnoverScheme(tra_typ_id, orderNo1, metaData1.ban_rel_type,
                metaData1.ban_pro_typ_id, null, data.eco_sec, null,null,data.pro_cat_id,null,null,
                    data.cus_acc_currency_type,data.term_code);
        
        if(turSchemeData1==null) {
            bc.raiseException(1," Preskace se revalorizacija za ovu partiju: Nije pronadjena parametrizacija TURNOVER_SCHEME itd za ovu kombinaciju:tra_typ_id="
                    +tra_typ_id+"; ord_no1="+new Integer(1)+"; ban_rel_type="+metaData1.ban_rel_type+"; ban_pro_typ_id="+metaData1.ban_pro_typ_id+
                    ";pro_typ_id="+metaData1.pro_typ_id+"eco_sec="+data.eco_sec+"; amo_type="+metaData1.amo_type+
                    "; currency_type="+data.cus_acc_currency_type+";");
        }
        bc.debug("dohvatio turSchemeData1");
      
        metaData1.tur_typ_id = turSchemeData1.tur_typ_id;
        yfwe1.getTuroverType(metaData1);
        bc.debug("metaData1(ORIGINAL)="+metaData1.toString());
        
        MetaTurnoverData metaData2 = new MetaTurnoverData(bc);
        metaData2.ban_pro_typ_id = data.ban_pro_typ_id;     
        yfwe1.getBankProdTypeData(metaData2, data);
        metaData2.amo_type = cusAccDataDomestic.amo_type;
        metaData2.ban_rel_type = cusAccDataDomestic.ban_rel_type;
        metaData2.account = cusAccDataDomestic.account;
        bc.debug("doRevalue(): metaData1.account = cusAccData.account = " + cusAccDataDomestic.account);
        metaData2.pro_typ_id = cusAccDataDomestic.pro_typ_id;
  
        TurnoverSchemeData turSchemeData2 = yfw30.getDescriptionFromTurnoverScheme(tra_typ_id, orderNo2, metaData2.ban_rel_type, 
               metaData2.ban_pro_typ_id, null ,data.eco_sec,null,null,data.pro_cat_id,null,null,data.cus_acc_currency_type, data.term_code);
        if(turSchemeData2==null) {
            bc.raiseException(1," Preskace se revalorizacija za ovu partiju: Nije pronadjena parametrizacija TURNOVER_SCHEME itd za ovu kombinaciju:tra_typ_id="+
                    tra_typ_id+"; ord_no="+new Integer(2)+"; ban_rel_type="+metaData2.ban_rel_type+"; ban_pro_typ_id="+
                    metaData2.ban_pro_typ_id+";pro_typ_id="+metaData2.pro_typ_id+"eco_sec="+data.eco_sec+"; amo_type="+metaData2.amo_type+
                    "; currency_type="+data.char_currency_type+";");
        }
        bc.debug("dohvatio turSchemeData2");
       
        metaData2.tur_typ_id = turSchemeData2.tur_typ_id;
        yfwe1.getTuroverType(metaData2);
        yfwe1.getBankProdTypeData(metaData2, data);

        bc.debug("metaData1="+metaData1.toString());
        bc.debug("metaData2="+metaData2.toString());
        bc.debug("------------INSERT TURNOVER 1 START ----------dom_cur_id=");
        //insert turnover 1
        helper.setCopyDomicil(null);
        turSchemeData1.filter = "CUSTOMER_ACCOUNT";
        turSchemeData1.pur_code = metaData1.pur_code;
        turSchemeData1.pur_id = metaData1.pur_id;
        helper.doRevalue(true);
        data.cur_id=cusAccDataForeign.cur_id;
        data.tra_id=yfwe0.insertEventTrx(tra_typ_id,bo401.getEveId(),defaultOrgUnitId);        
        TurnoverData turnover1 = helper.insertTurnover(data, metaData1, turSchemeData1, data.eve_id, data.tra_id, dom_cur_id, tra_typ_id);
        bc.debug("------------INSERTED TURNOVER 1 END ----------");
        //insert turnover 2
         helper.setCopyDomicil(turnover1.domicil);
        helper.copyDomesticAmount(turnover1.debit_amount.add(turnover1.credit_amount));
        data.cur_id=cusAccDataDomestic.cur_id;
        data.tra_id=yfwe0.insertEventTrx(tra_typ_id,bo401.getEveId(),defaultOrgUnitId);
        TurnoverData turnover2 = helper.insertTurnover(data, metaData2, turSchemeData2, data.eve_id, data.tra_id, dom_cur_id, tra_typ_id);        
        bc.debug("------------INSERTED TURNOVER 2 END ----------");
        helper.doRevalue(false);
        //yfwe1.checkBalance(data.tra_id);      
    }
    
    
    private PostingDepositData fillPostingData(BigDecimal cusAccId, CustomerAccountData cad) throws Exception{
        PostingDepositData pdd=new PostingDepositData(bc);

        pdd.eve_id=bo401.getEveId();
        pdd.event_org_uni_id=defaultOrgUnitId;
        pdd.wor_pla_id=null;
        pdd.login=bc.getLogin();
        pdd.use_id=new BigDecimal(1);
        pdd.process_timestamp=new Timestamp(System.currentTimeMillis());
        pdd.process_date=processDate;
        pdd.term_date_until=processDate;
        pdd.cus_acc_id=cusAccId;
        pdd.cus_acc_no=cad.cus_acc_no;
        pdd.cus_acc_org_uni_id=cad.org_uni_id;
        pdd.cus_acc_currency_type=cad.currency_type;
        pdd.cus_id=cad.cus_id;
        pdd.ban_pro_typ_id=cad.ban_pro_typ_id;
        pdd.pro_cat_id=cad.pro_cat_id; 
        pdd.cus_acc_status=cad.status;

        return pdd;
    }
    
    private CusAccBalanceData fillBallanceDataFromIter(BO401.IteratorBalanceData iterRow) throws Exception{
        CusAccBalanceData bd=new CusAccBalanceData(bc);
        bc.debug("fillBallanceDataFromIter poceo.");
        
        bd.cus_acc_bal_id=iterRow.cusacc_bal_id();
        bd.cus_acc_id=iterRow.cus_acc_id();
        bd.acc_num_id=iterRow.acc_num_id();
        bd.cus_id=iterRow.cus_id();
        bd.account=iterRow.account();      
        bd.ban_rel_type=iterRow.ban_rel_type();
        bd.amo_type=iterRow.amo_type();
        bd.pro_typ_id=iterRow.pro_typ_id();
        bd.balance_date=iterRow.balance_date();
        bd.cur_id=iterRow.cur_id();
        bd.balance=iterRow.balance();
        bd.clause_balance=iterRow.clause_balance();
       
        bc.debug("fillBallanceDataFromIter zavrsio.");
        return bd;
    }

    private void checkArguments() throws Exception{
        
        if (bc.getArgs().length!=2){
            bc.debug("Neispravan broj argumenata!");
            throw new Exception("Neispravan broj argumenata!");
        }
        
        if (!bc.getArg(0).startsWith("RB#")){
            bc.debug("Greska pri parametru bank_sign. Bank sign mora biti 'RB#[naziv modula naa koji se radi knjizenje]'!");
            throw new Exception("Greska pri parametru Bank sign. Bank sign mora biti 'RB#[naziv modula naa koji se radi knjizenje]'!");
        }
        
        try {
            processDate=DateUtils.createDateFromString(bc.getArg(1));
        } catch (Exception e) {
            bc.debug("Greska pri konverziji parametra datuma revalorizacije.");
            throw new Exception("Greska pri konverziji parametra datuma revalorizacije.");
        }       
    }
    
    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2795511003"));
        batchParameters.setArgs(args);
        new BO400().run(batchParameters);  
    }    
    
}
