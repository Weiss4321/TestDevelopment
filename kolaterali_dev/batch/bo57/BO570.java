//created 2010.09.27
package hr.vestigo.modules.collateral.batch.bo57;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;

import hr.vestigo.modules.collateral.batch.bo57.BO571.FrameIterator;
import hr.vestigo.modules.collateral.batch.bo57.BO571.FrameIteratorContract;
import hr.vestigo.modules.collateral.batch.bo57.BO571.GarIterator;
import hr.vestigo.modules.collateral.batch.bo57.BO571.KRDIterator;
import hr.vestigo.modules.collateral.batch.bo57.BO571.LetterOfCreditIterator;
import hr.vestigo.modules.collateral.batch.bo57.BO571.OverdraftIterator;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import java.math.BigDecimal;
import java.util.HashMap;


/**
 *
 * @author hramlo
 */
public class BO570 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo57/BO570.java,v 1.6 2016/07/06 11:24:02 hrakis Exp $";

    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BatchContext bc = null;
    private BigDecimal eve_id=null;
    private ConnCtx connCtx = null;

    private BO571 bo571=null;
    private FrameIterator iter=null;
    private FrameIteratorContract iter1=null;
    private KRDIterator iter2=null;
    private GarIterator iter3=null;
    private OverdraftIterator iter4=null;
    private LetterOfCreditIterator iter5=null;


 


    public BO570(){     
    }

    public String executeBatch(BatchContext bc) throws Exception{
        this.bc=bc;
        bo571=new BO571(bc); 

        try{
            this.connCtx=bc.getContext();
        }catch(Exception e){
            bc.warning("Nema connCtx u podtransakciji!!");
        }

        //provjera ulaznih parametara
        try{
            if(!checkArgs()){
                for(int i=0;i<bc.getArgs().length;i++){
                    bc.debug("" + i +". " + bc.getArg(i));
                }
            }

        }catch(Exception e){
            bc.error("Broj parametara:" + bc.getArgs(),  new Exception("Neispravan broj parametara."));
            return RemoteConstants.RET_CODE_ERROR;

        }

        bc.debug("provjera ulaznih parametara gotova!");

        //insert u EVENT
        try{
            insertIntoEvent();
        }catch (Exception e) {
            bc.error("Insert EVENT - GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Insert EVENT - END");  


        try
        {
            
            //dohvat podataka 
            bc.debug("dohvat podataka");    

            //iterator s dohvacenim podacima - okviri - preko ugovora           
            try{
                iter1=bo571.getFrameDataContract();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getFrameDataContract()!!!");
                e.printStackTrace();                
            }

            while(iter1.next()){
                //za svaki okvir koji nema popunjen br.partije upisujem ga u loan_beneficiary

                bc.debug("uparujem okvir preko ugovora broj: " + iter1.contract_no() + " - partija broj: " + iter1.cus_acc_no());
                try{
                    bo571.updateLoanBenOkvContract(iter1.cus_acc_id(),iter1.cus_acc_no(),iter1.request_no(),iter1.contract_no());
                }catch(Exception e){
                    bc.warning("bo571.updateLoanBenOkvContract-puklo na updateLoanBen");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter
            
            
            
            
            //iterator s dohvacenim podacima - okviri - preko broja zahtjeva           
            try{
                iter=bo571.getFrameData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getFrameData()!!!");
                e.printStackTrace();                
            }

            while(iter.next()){
                //za svaki okvir koji nema popunjen br.partije upisujem ga u loan_beneficiary

                bc.debug("uparujem okvir preko zahtjeva broj: " + iter.request_no() + " - partija broj: " + iter.cus_acc_no());
                try{
                    bo571.updateLoanBen(iter.cus_acc_id(),iter.cus_acc_no(),iter.request_no());
                }catch(Exception e){
                    bc.warning("puklo na updateLoanBen");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter
            
            
            
            
            //iterator s dohvacenim podacima - krd          
            try{
                iter2=bo571.getKRDData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getKRDData()!!!");
                e.printStackTrace();                
            }

            while(iter2.next()){
                //za svaki KRD koji nema popunjen br.partije upisujem ga u loan_beneficiary
                bc.debug("uparujem KRD preko ugovora broj: " + iter2.contract_no() + " - partija broj: " + iter2.cus_acc_no());

                try{
                    bo571.updateLoanBenGK(iter2.cus_acc_id(),iter2.cus_acc_no(),iter2.contract_no());
                }catch(Exception e){
                    bc.warning("puklo na updateLoanBenGK KRD");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter2
            
            
            
            
            //iterator s dohvacenim podacima - garancije           
            try{
                iter3=bo571.getGarData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getGarData()!!!");
                e.printStackTrace();                
            }

            while(iter3.next()){
                //za svaku garanciju koja nema popunjen br.partije upisujem ga u loan_beneficiary
                bc.debug("uparujem GAR preko ugovora broj: " + iter3.contract_no() + " - partija broj: " + iter3.cus_acc_no());                
                try{
                    bo571.updateLoanBenGK(iter3.cus_acc_id(),iter3.cus_acc_no(),iter3.contract_no());
                }catch(Exception e){
                    bc.warning("puklo na updateLoanBenGK GAR");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter
            
            
            
            
            // iterator s dohvaæenim podacima - overdrafti
            try{
                iter4=bo571.getOverdraftData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getOverdraftData()!!!");
                e.printStackTrace();                
            }  

            while(iter4.next()){
                //za svaki overdraft koji nema popunjen br.partije upisujem ga u loan_beneficiary
                bc.debug("uparujem PPZ/SDR preko ugovora broj: " + iter4.contract_no() + " - partija broj: " + iter4.cus_acc_no());                
                try{
                    bo571.updateLoanBenGK(iter4.cus_acc_id(),iter4.cus_acc_no(),iter4.contract_no());
                }catch(Exception e){
                    bc.warning("puklo na updateLoanBenGK PPZ/SDR");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter            

            
            

            // iterator s dohvaæenim podacima - akreditivi (RTC 17232)
            try{
                iter5=bo571.getLetterOfCreditData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo571.getLetterOfCreditData()!!!");
                e.printStackTrace();                
            }  

            while(iter5.next()){
                // forma partija koja se treba dohvaæati je 8000xxxxxx
                if (iter5.cus_acc_no() == null || !iter5.cus_acc_no().startsWith("8000")) continue;
                
                //za svaki akreditiv koji nema popunjen br.partije upisujem ga u loan_beneficiary
                bc.debug("uparujem LOC preko ugovora broj: " + iter5.contract_no() + " - partija broj: " + iter5.cus_acc_no());                
                try{
                    bo571.updateLoanBenGK(iter5.cus_acc_id(),iter5.cus_acc_no(),iter5.contract_no());
                }catch(Exception e){
                    bc.warning("puklo na updateLoanBenGK LOC");
                    throw e;
                }
                bc.commitTransaction();
            }//end iter
            
        }
        catch (Exception e)
        {
            bc.warning("Puklo na obradi podataka-glavni blok - GRESKA!!!");
            bc.error("GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;

        }
        finally  // (bez obzira obrada uspjesno ili neuspjesno zavrsila-zatvaram iterator)
        {      
            if(iter!=null){
                try{
                    iter.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }        
            if(iter1!=null){
                try{
                    iter1.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
            if(iter2!=null){
                try{
                    iter2.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
            if(iter3!=null){
                try{
                    iter3.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
            if(iter4!=null){
                try{
                    iter4.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
        } 
        return returnCode;
    }


    private void insertIntoEvent() throws Exception{
        bc.warning("insertIntoEvent");   
        HashMap event = new HashMap();
        this.bc=bc;

        try{
            YXYB0 eve1 = new YXYB0(bc);
            YXYD0 genId = new YXYD0(bc.getContext());           

            eve_id = genId.getNewId();

            event.put("eve_id",eve_id);
            event.put("eve_typ_id", new BigDecimal("3863042044"));
            event.put("event_date", new java.sql.Date(System.currentTimeMillis()));
            event.put("cmnt", "bo57:Okviri iz Loxona");
            event.put("use_id", bc.getUserID());
            event.put("ext_event_code", null);
            event.put("ext_event_num", null); 
            event.put("bank_sign", bc.getBankSign());
            bc.warning("eve_id" + event.get("eve_id"));eve1.insertEvent(event);
            bc.updateEveID(eve_id);        
        } catch(Exception e){
            bc.warning("Event parametri=" + event.toString());
            throw e;
        }      
    } 


    private boolean checkArgs() throws Exception{
        bc.debug("bc.getArgs().length:"+bc.getArgs().length);

        if (bc.getArgs().length==1){
            return true;            
        }else{
            return false;
        }

    }

    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("3863030604"));
        bp.setArgs(args);
        new BO570().run(bp);       
    } 
}

