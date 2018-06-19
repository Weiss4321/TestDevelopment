/*
 * hrazst Created on 2008.06.17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo26;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.TimeCounter;



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

public class BO260 extends Batch{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo26/BO260.java,v 1.8 2009/01/08 14:05:14 hrazst Exp $";

    private BatchContext bc = null;
    private BO261 bo261=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    
    /**
     * datum revalorizacije , ulazni parametar batcha
     */
    private Date revaDate=null;
   
    public String executeBatch(BatchContext batchContext) throws Exception {
        
        this.bc=batchContext;        
        TimeCounter tc=null;tc=new TimeCounter(bc);
        
        tc.start("executeBatch.");
        
        bo261 = new BO261(bc);
        
        try {
            checkArguments();
            bc.debug("Argumenti provjereni.");           
        } catch (Exception e) {
            bc.error("Greska pri provjeri argumenata.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        try {
            bo261.insertIntoEvent();            
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
        tc.end();
        
        return returnCode;
    }
    
    private void startBatch() throws Exception{
        BO26CollHeadData collHeadData=new BO26CollHeadData();
        BO26CollHeadData.CollHeadDataToProcess dataToProcess=null;
        BO26CollHeadData.CollHeadDataProcessed dataProcessed=null;
        BO26CollHeadData.ColTurnoverData dataColTurnover=null;
        
        BO261.IteratorRevaPom iterRevaPom=null;
        String recordStatus="";
        BigDecimal colProId=null;
        int colNumber=0; //broj dobro obradenih slogova
        CollateralPosting collPosting=CollateralCommonFactory.getCollateralPosting(this.bc);
        
        colProId=bo261.getColProcId(revaDate, "R", "1");        
        if(colProId!=null){
            bc.info("Obrada za dani datum je vec uspjesno odradena!");
            throw new Exception("Obrada za dani datum je vec uspjesno odradena!");
        }else{
            //ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
            colProId=bo261.getColProcId(revaDate, "R", "0");
            
            if(colProId!=null){
                bc.info("Obrada za dani datum vec postoji (id= "+colProId+") ali nije zavrsena. Nastavljam...!");
            }else{                
                bc.info("\nNe postoji obrada revalorizacije za zadani datum. Pocinje potpuno nova obrada.");
                try {            
                    colProId=bo261.insertIntoColProc(revaDate);            
                }
                catch (Exception e) {
                    bc.error("Greska pri insertiranju u col_proc tablicu.",e);
                    throw new Exception("Greska pri insertiranju u col_proc tablicu.");
                }
                bc.commitTransaction();         
                bc.debug("Col_proc insert odraden.");  
            }          
        }          
        
        //dohvacanje zapisa za obradu
        iterRevaPom=bo261.fatchRevaPomData(revaDate);
        
        //svi dohvaceni collateral-i koji se trebaju revalorizirat se iteriraju
        while (iterRevaPom.next()){
            bc.debug("Pocinje obrada collaterala sa col_num="+iterRevaPom.col_num());
            dataToProcess=bo261.getOneCollateralData(iterRevaPom.col_num());
            
            dataColTurnover=null;
            
            //ako nema colaterala sa trazenim col_num-om, onda je status sloga u col_turnover tablici jednak 1
            if(collHeadData==null){               
                bc.debug("Nije se pronasao kolateral sa col_num="+iterRevaPom.col_num());
                dataColTurnover=fillColTurnoverObject(colProId, iterRevaPom.col_num(), null, "1", null, null);
                
            //ako je status kolaterala razlicit od 3, onda je status sloga u col_turnover tablici jednak 2
            }else if(!dataToProcess.collateral_status.equals("3")){
                bc.debug("Kolateral sa col_num="+iterRevaPom.col_num()+" nije na listi aktivnih.");
                dataColTurnover=fillColTurnoverObject(colProId, iterRevaPom.col_num(), dataToProcess, "2", null, null);

            //ako je valuta kolaterala razlicita od valute poslane u tablici reva_pom, onda je status sloga u col_turnover tablici jednak 3
            }else if(!dataToProcess.cur_code.equals(iterRevaPom.valuta())){
                bc.debug("Kolateral sa col_num="+iterRevaPom.col_num()+" ima razlicitu valutu nego sto je u poslana na revalorizaciju.");
                dataColTurnover=fillColTurnoverObject(colProId, iterRevaPom.col_num(), dataToProcess, "3", null, null);
            
            //ako se produ gore navedene provjere onda se ide u daljnji postupak revalorizacije i 
            //onda je status sloga u col_turnover tablici jednak 0
            }else{                
                try {
                    String status="0";
                    BigDecimal pomRevaCoef=null;
                    
                    bc.debug("Za kolateral sa col_num="+iterRevaPom.col_num()+" pocinje revalorizaciju.");
                    dataProcessed=collHeadData.new CollHeadDataProcessed();
                    
                    dataProcessed.real_est_nomi_value=iterRevaPom.new_value();
                    dataProcessed.real_est_nomi_date=revaDate;
                    if(iterRevaPom.proc_perc()==null){ 
                        if(dataToProcess.real_est_nomi_value.compareTo(new BigDecimal("0"))!=0)
                        {
                            pomRevaCoef=iterRevaPom.new_value().divide(dataToProcess.real_est_nomi_value,2, BigDecimal.ROUND_HALF_UP);                       
                        }else{                            
                            pomRevaCoef=new BigDecimal("0");
                        }                
                    }else{
                        pomRevaCoef=iterRevaPom.proc_perc();
                    }
                    
                    //ako je reva_coef veci od 10 onda se fiksira na vrijednost 9.99 jer u tablici COLL_HEAD.REVA_COEF polje 3.2 
                    //tak da ne stane veci broj od 9.99 
                    if(pomRevaCoef.compareTo(new BigDecimal("10"))>=0){
                        dataProcessed.reva_coef=new BigDecimal("9.99");
                    }else{
                        dataProcessed.reva_coef=pomRevaCoef;
                    }
                    
                    dataProcessed.reva_date=revaDate;
                    dataProcessed.reva_date_am="A";
                    dataProcessed.reva_bvalue=dataToProcess.real_est_nomi_value;
                    dataProcessed.reva_bdate=dataToProcess.real_est_nomi_date;
                    dataProcessed.reva_bdate_am=dataToProcess.reva_date_am;
                    
                    bo261.updateOneCollateralData(dataToProcess.col_hea_id, dataProcessed);
                    //poziva se knjizenje kolaterala
                    try {
                        collPosting.CollPosting(dataToProcess.col_hea_id, false);    
                    } catch(VestigoTMException e1){
                        bc.debug("Error e1:"+e1.getMessageID());
                        if(e1.getMessageID().equals("wrn419")){
                            bc.debug("neaktivan komitent.");
                            bc.rollbackTransaction();
                            status="4";
                        }else{
                            throw e1;
                        }                    
                    }                     
                    
                    //izracunava se vrijednost revalorizacije i puni se objekt za tablicu col_turnover 
                    BigDecimal revaValue=iterRevaPom.new_value().subtract(dataToProcess.real_est_nomi_value); 
                    //vracam prvotno izracunatu vrijednost jer sam ga mozda ranije za tablicu COLL_HEAD promjenio, a u 
                    //tablicu col_turnover mogu upisat reva_coef veci od 9.99
                    dataProcessed.reva_coef=pomRevaCoef;
                    
                    dataColTurnover=fillColTurnoverObject(colProId, iterRevaPom.col_num(), dataToProcess, status, revaValue , dataProcessed.reva_coef);
                } catch (Exception e) {
                    bc.info("Podaci koji se trebaju obradit="+ dataToProcess.toString());
                    bc.info("Obradeni podaci koji se upisuju u tablicu="+dataProcessed.toString());
                    throw e;
                }
            }
            
            bo261.insertIntoColTurnover(dataColTurnover);
            
            bc.commitTransaction();            
        }
        
        bo261.updateColProc(colProId, bo261.getNumberOfRowsFromColProc(colProId)); 
        bc.commitTransaction();
    }
 
    private BO26CollHeadData.ColTurnoverData fillColTurnoverObject(BigDecimal colProId,
                                                                   String colNum, 
                                                                   BO26CollHeadData.CollHeadDataToProcess dataToProcess, 
                                                                   String status, 
                                                                   BigDecimal amountProc, 
                                                                   BigDecimal procPerc){
        BO26CollHeadData collHeadData=new BO26CollHeadData();
        BO26CollHeadData.ColTurnoverData dataColTurnover=collHeadData.new ColTurnoverData();
        
        dataColTurnover.col_pro_id=colProId;
        dataColTurnover.col_num=colNum;
        if (dataToProcess!=null) dataColTurnover.col_hea_id=dataToProcess.col_hea_id;
        if (dataToProcess!=null) dataColTurnover.coll_type_id=dataToProcess.col_typ_id;
        if (dataToProcess!=null) dataColTurnover.amount=dataToProcess.real_est_nomi_value;
        if (dataToProcess!=null) dataColTurnover.amount_proc=amountProc;
        if (dataToProcess!=null) dataColTurnover.proc_perc=procPerc;
        if (dataToProcess!=null) dataColTurnover.date_from=dataToProcess.real_est_nomi_date;
        if (dataToProcess!=null) dataColTurnover.date_until=revaDate;
        if (dataToProcess!=null) dataColTurnover.proc_period=Integer.valueOf(DateUtils.ActualNumOfMonths(revaDate, dataToProcess.real_est_nomi_date)).toString();
        dataColTurnover.proc_status=status;
        
        return dataColTurnover;
    }
         
    private void checkArguments() throws Exception{
        
        if (bc.getArgs().length!=2){
            bc.debug("Neispravan broj argumenata!");
            throw new Exception("Neispravan broj argumenata!");
        }
        
        if (!bc.getArg(0).equals("RB") && !bc.getArg(0).equals("RB#COL")){
            bc.debug("Greska pri parametru bank_sign. Bank sign mora biti 'RB' ili RB#COL!");
            throw new Exception("Greska pri parametru Bank sign. Bank sign mora biti 'RB' ili RB#COL!");
        }else{
            bc.setBankSign("RB");
        }
        
        try {
            revaDate=DateUtils.createDateFromString(bc.getArg(1));
        } catch (Exception e) {
            bc.debug("Greska pri konverziji parametra datuma revalorizacije.");
            throw new Exception("Greska pri konverziji parametra datuma revalorizacije.");
        }       
    }
    
    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2514253003"));
        batchParameters.setArgs(args);
        new BO260().run(batchParameters);  
    }    

}
