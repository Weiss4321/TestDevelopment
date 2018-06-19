//created 2010.09.09
package hr.vestigo.modules.collateral.batch.bo55;

/**
 *
 * @author hramlo
 */
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.modules.collateral.batch.bo55.BO551;
import hr.vestigo.modules.collateral.batch.bo55.BO551.CollHeadDataIterator;
import hr.vestigo.modules.collateral.batch.bo55.BO551.CollHfPriorIterator;
import hr.vestigo.modules.collateral.batch.bo55.BO551.LoanBeneficiaryIterator;
import hr.vestigo.modules.collateral.batch.bo55.BO551.FrameAgreementIterator;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.DateUtils;


public class BO550  extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo55/BO550.java,v 1.7 2017/06/01 09:07:28 hrakis Exp $";

    private BatchContext bc = null;
    private BO551 bo551=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;

    java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
    long timeT = calendar.getTime().getTime();
    private Date CREATE_DATE_TIME  = new java.sql.Date(timeT);

    final Timestamp current_timestamp = new java.sql.Timestamp(System.currentTimeMillis());

    private BigDecimal eve_id=null;
    private ConnCtx connCtx = null;
    private CollHeadDataIterator iter=null;
    private CollHfPriorIterator iter2=null;
    private LoanBeneficiaryIterator iter3=null;
    private FrameAgreementIterator iter4=null;
    
    private int chd_insert=0;
    private int chd_update=0;
    
    private int chpd_insert=0;
    private int chpd_update=0;
    
    private int lbd_insert=0;
    private int lbd_update=0;
    
    private int fad_insert=0;
    private int fad_update=0;
    


    public BO550(){     
    }

    public String executeBatch(BatchContext bc) throws Exception{

        this.bc=bc;

      
        bo551=new BO551(bc);

        try{
            this.connCtx=bc.getContext();
        }catch(Exception e){
            bc.warning("Nema connCtx u podtransakciji!!");
        }

        bc.info("provjera ulaznih parametara");

        try{
            if(!checkArgs()){
                int i=0;
                for(i=0;i<bc.getArgs().length;i++) {
                    bc.debug("" + i +". " + bc.getArg(i));
                } 
                throw new Exception();
            }             

        }catch (Exception e) {
            bc.error("Broj parametara:" + bc.getArgs().length, bc.getArgs(), new Exception("Neispravan broj parametara."));
            return RemoteConstants.RET_CODE_ERROR;
        }  

        bc.setBankSign(bc.getArg(0));

        //insert u EVENT
        try{
            insertIntoEvent();
            bc.commitTransaction();
        }catch (Exception e) {
            bc.error("Insert EVENT - GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.debug("Insert EVENT - END"); 

        //dohvat podataka         
        try{          
            try{
                iter=bo551.getCollHeadData();     
                iter2=bo551.getCollHfPriorData();
                iter3=bo551.getLoanBeneficiaryData();
                iter4=bo551.getFrameAgreementData();
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo551.getCustData()!!!");
                e.printStackTrace();                
            }
            
            processCollHeadData(iter);
            
            processCollHfPriorData(iter2);
            
            processLoanBeneficiaryData(iter3);
            
            processFrameAgreementData(iter4);
            
            bc.info("COLL_HEAD_D insertano po prvi puta:"+ chd_insert +" slog(ova), insert novog vazeceg sloga:"+chd_update);
            bc.info("COLL_HF_PRIOR_D insertano po prvi puta:"+ chpd_insert+" slog(ova), insert novog vazeceg sloga:"+chpd_update);
            bc.info("LOAN_BENEFICIARY_D insertano po prvi puta:"+lbd_insert +" slog(ova), insert novog vazeceg sloga:"+lbd_update);
            bc.info("FRAME_AGREEMENT_D insertano po prvi puta:"+ fad_insert+" slog(ova), insert novog vazeceg sloga:"+fad_update);
            bc.setCounter(chd_insert + chd_update + chpd_insert + chpd_update + lbd_insert + lbd_update + fad_insert + fad_update);

        }catch(Exception e){
            bc.warning("Puklo na obradi podataka-glavni blok - GRESKA!!!");
            bc.error("GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;

        }finally{// (bez obzira obrada uspjesno ili neuspjesno zavrsila-zatvaram iterator)            

            if(iter!=null){
                try{
                    iter.close();
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
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

    private boolean checkArgs() throws Exception{
        bc.debug("bc.getArgs().length:"+bc.getArgs().length);
        if (bc.getArgs().length==1){
            bc.debug("bc.getArg(0)"+bc.getArg(0));                      
            return true;
        }else{
            return false;
        }      
    }


    private void insertIntoEvent() throws Exception{
        bc.warning("insertIntoEvent");   
        HashMap event = new HashMap();

        try{
            YXYB0 eve1 = new YXYB0(bc);
            YXYD0 genId = new YXYD0(bc.getContext());           

            eve_id = genId.getNewId();

            event.put("eve_id",eve_id);
            event.put("eve_typ_id", new BigDecimal("3433767484"));
            event.put("event_date", new java.sql.Date(System.currentTimeMillis()));
            event.put("cmnt", "bo55:Promjena koristenja *_D tablica");
            event.put("use_id", bc.getUserID());
            event.put("ext_event_code", null);
            event.put("ext_event_num", null); 
            event.put("bank_sign", bc.getBankSign());
            bc.warning("eve_id" + event.get("eve_id"));

            eve1.insertEvent(event);
            bc.updateEveID(eve_id);        
        } catch(Exception e){
            bc.warning("Event parametri=" + event.toString());
            throw e;
        }      
    }

    private void processCollHeadData(CollHeadDataIterator iterData)throws Exception{
        bc.debug("Metoda processCollHeadData");
        DTableData data=null;

        
        //COLL_HEAD_D 
        while(iterData.next()){
            bc.debug("Pocinje obrada:"+iterData.col_hea_id());
            Vector chddata=bo551.checkColHeadD(iterData.col_hea_id());
            //dohvat svih atributa iz coll_head za taj id
            data=bo551.setCollHeadData(iterData.col_hea_id());
            bc.debug("Dohvat podataka iz Coll_head D u data objekt za id:"+iterData.col_hea_id());
            bc.debug("COL_HEAD data.user_lock ="+data.user_lock + ", chg_nom_val_proc_ts=" + data.chg_nom_val_proc_ts );
            
            //ako ne postoji vazeci -> insert (bez obzira na user_lock)
            bc.debug("chddata.get(0):"+chddata.get(0));
            if(chddata.get(0) == null){//nema vazeceg sloga s tim user_lockom                 
                data.LOAD_DATE=CREATE_DATE_TIME;
                data.LOAD_DATE_FROM=CREATE_DATE_TIME;
                bc.debug("object.LOAD_DATE_FROM"+data.LOAD_DATE_FROM);
                data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");

                bc.debug("prije insert coll_head_D");

                try{
                    bo551.insertCollHeadD(data);  
                    chd_insert++;
                    bc.debug("insertCollHeadD prosao");
                }catch(Exception e){
                    bc.warning("puklo na insertCollHeadD");
                    throw e;                    
                }    
                bc.debug("poslije insert coll_head_d");
            }else{//postoji vazeci->zatvaram stari i insertam novi..
                //provjeravam user_lock-> ako je razlièit- manji onda zatvaram stari i insertam novi, inace nista
                Timestamp old_user_lock = (Timestamp)chddata.get(1);
                Timestamp old_chg_nom_val_proc_ts = (Timestamp)chddata.get(2);
                if((data.user_lock.compareTo(old_user_lock))!=0 || (data.chg_nom_val_proc_ts!=null && (old_chg_nom_val_proc_ts == null || data.chg_nom_val_proc_ts.compareTo(old_chg_nom_val_proc_ts)!=0))){//nisu jednaki

                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=DateUtils.addOrDeductDaysFromDate(CREATE_DATE_TIME,new Integer(1),false);

                    try{
                        bo551.closeOldCollHeadD(data);
                    }catch(Exception e){
                        bc.warning("puklo na closeOldCollHeadD");
                        throw e;
                    }

                    //insert novog(vazeceg) sloga u coll_head
                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_FROM= CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                    try{
                        bo551.insertCollHeadD(data);   
                        chd_update++;
                        bc.debug("insertCollHeadD prosao");
                    }catch(Exception e){
                        bc.warning("puklo na insertCollHeadD");
                        throw e;                    
                    } 
                }else {//jednaki su - ne radi se ništa
                    continue;
                }
            }
            bc.commitTransaction();
        }//end iterData

    }


    private void processCollHfPriorData(CollHfPriorIterator iter2)throws Exception{
        //COLL_HF_PRIOR_D
        
        DTableData dataCHF=null;    
        
        bc.debug("Metoda processCollHfPriorData");

        while(iter2.next()){
            Vector chfdata=bo551.checkCollHfPriorD(iter2.coll_hf_prior_id());
            //dohvat svih atributa iz coll_hf_prior za taj id
            dataCHF=bo551.setCollHfPriorData(iter2.coll_hf_prior_id());
            bc.debug("Dohvat podataka u data objekt za id:"+iter2.coll_hf_prior_id());
            bc.debug("dataCHF.user_lock POSLIJE setCollHfPriorData"+dataCHF.user_lock);
            //ako ne postoji vazeci -> insert (bez obzira na user_lock)               
            if((BigDecimal)chfdata.get(0)== null){//nema vazeceg sloga

                dataCHF.LOAD_DATE=CREATE_DATE_TIME;
                dataCHF.LOAD_DATE_FROM=CREATE_DATE_TIME;
                bc.debug("object.LOAD_DATE_from"+dataCHF.LOAD_DATE_FROM);
                dataCHF.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                bc.debug("prije insert coll_hf_prior_D");

                try{
                    bo551.insertCollHfPriorD(dataCHF);
                    chpd_insert++;
                    bc.debug("insertCollHfPriorD prosao");
                }catch(Exception e){
                    bc.warning("puklo na insertCollHfPriorD");
                    throw e;                    
                }    
                bc.info("poslije insert coll_hf_prior");
            }else{//postoji vazeci->zatvaram stari i insertam novi..
                //provjeravam user_lock-> ako je razlièit- manji onda zatvaram stari i insertam novi, inace nista
                bc.debug("dataCHF.user_lock"+dataCHF.user_lock);
                bc.debug("chfdata.get(1)"+chfdata.get(1));
                if((dataCHF.user_lock.compareTo((Timestamp)chfdata.get(1)))!=0){//nisu jednaki

                    dataCHF.LOAD_DATE=CREATE_DATE_TIME;
                    dataCHF.LOAD_DATE_UNTIL=DateUtils.addOrDeductDaysFromDate(CREATE_DATE_TIME,new Integer(1),false);

                    try{
                        bo551.closeOldCollHfPriorD(dataCHF);
                    }catch(Exception e){
                        bc.warning("puklo na closeOldCollHeadD");
                        throw e;
                    }

                    //insert novog(vazeceg) sloga u coll_head
                    dataCHF.LOAD_DATE=CREATE_DATE_TIME;
                    dataCHF.LOAD_DATE_FROM= CREATE_DATE_TIME;
                    dataCHF.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                    try{
                        bo551.insertCollHfPriorD(dataCHF);   
                        chpd_update++;
                        bc.debug("insertCollHeadD prosao");
                    }catch(Exception e){
                        bc.warning("puklo na insertCollHeadD");
                        throw e;                    
                    } 
                }else {//jednaki su - ne radi se ništa
                    continue;
                }
            }
            bc.commitTransaction();
        }//end iter2
        
    }
    
    //LOAN_BENEFICIARY_D
    
    private void processLoanBeneficiaryData(LoanBeneficiaryIterator iter3)throws Exception{
    
        
        DTableData data=null;    
        
        bc.debug("Metoda processLoanBeneficiaryData");

        while(iter3.next()){
            Vector lbdata=bo551.checkLoanBenD(iter3.loan_ben_id());
            //dohvat svih atributa iz coll_hf_prior za taj id
            data=bo551.setLoanBenData(iter3.loan_ben_id());
            //ako ne postoji vazeci -> insert (bez obzira na user_lock)               
            if((BigDecimal)lbdata.get(0)== null){//nema vazeceg sloga

                data.LOAD_DATE=CREATE_DATE_TIME;
                data.LOAD_DATE_FROM=CREATE_DATE_TIME;
                bc.debug("object.LOAD_DATE_from"+data.LOAD_DATE_FROM);
                data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                bc.debug("prije insert loan_beneficiary_D");

                try{
                    bo551.insertLoanBeneficiaryD(data);    
                    lbd_insert++;
                    bc.debug("insertLoanBenD prosao");
                }catch(Exception e){
                    bc.warning("puklo na insertLoanBenD");
                    throw e;                    
                }    
                bc.info("poslije insert loan_beneficiary_d");
            }else{//postoji vazeci->zatvaram stari i insertam novi..
                //provjeravam user_lock-> ako je razlièit- manji onda zatvaram stari i insertam novi, inace nista
                if((data.user_lock.compareTo((Timestamp)lbdata.get(1)))!=0){//nisu jednaki

                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=DateUtils.addOrDeductDaysFromDate(CREATE_DATE_TIME,new Integer(1),false);

                    try{
                        bo551.closeOldLoanBenD(data);
                    }catch(Exception e){
                        bc.warning("puklo na closeOldLoanBenD");
                        throw e;
                    }

                    //insert novog(vazeceg) sloga u coll_head
                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_FROM= CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                    try{
                        bo551.insertLoanBeneficiaryD(data);   
                        lbd_update++;
                        bc.debug("insertLoanBenD prosao");
                    }catch(Exception e){
                        bc.warning("puklo na insertLoanBenD ");
                        throw e;                    
                    } 
                }else {//jednaki su - ne radi se ništa
                    continue;
                }
            }
            bc.commitTransaction();
        }//end iter3
        }



    //FRAME_AGREMEENT_D
    private void processFrameAgreementData(FrameAgreementIterator iter4)throws Exception{
        
        
        DTableData data=null;    
        
        bc.debug("Metoda processFrameAgreementData");

        while(iter4.next()){
            Vector fadata=bo551.checkFrameAgreementD(iter4.fra_agr_id());
            //dohvat svih atributa iz coll_hf_prior za taj id
            data=bo551.setFrameAgreementData(iter4.fra_agr_id());
            //ako ne postoji vazeci -> insert (bez obzira na user_lock)               
            if((BigDecimal)fadata.get(0)== null){//nema vazeceg sloga

                data.LOAD_DATE=CREATE_DATE_TIME;
                data.LOAD_DATE_FROM=CREATE_DATE_TIME;
                bc.debug("object.LOAD_DATE_FROM"+data.LOAD_DATE_FROM);
                data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                bc.debug("prije insert loan_beneficiary_D");

                try{
                    bo551.insertFrameAgreementD(data); 
                    fad_insert++;
                    bc.debug("insertFrameAgreementD prosao");
                }catch(Exception e){
                    bc.warning("puklo na insertFrameAgreementD");
                    throw e;                    
                }    
                bc.debug("poslije insert insertFrameAgreementD");
            }else{//postoji vazeci->zatvaram stari i insertam novi..
                //provjeravam user_lock-> ako je razlièit- manji onda zatvaram stari i insertam novi, inace nista
                if((data.user_lock.compareTo((Timestamp)fadata.get(1)))!=0){//nisu jednaki

                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=DateUtils.addOrDeductDaysFromDate(CREATE_DATE_TIME,new Integer(1),false);

                    try{
                        bo551.closeOldFrameAgreeD(data);
                    }catch(Exception e){
                        bc.warning("puklo na closeOldFrameAgreeD");
                        throw e;
                    }

                    //insert novog(vazeceg) sloga u coll_head
                    data.LOAD_DATE=CREATE_DATE_TIME;
                    data.LOAD_DATE_FROM= CREATE_DATE_TIME;
                    data.LOAD_DATE_UNTIL=Date.valueOf("9999-12-31");
                    try{
                        bo551.insertFrameAgreementD(data);  
                        fad_update++;
                        bc.debug("insertFrameAgreementD prosao");
                    }catch(Exception e){
                        bc.warning("puklo na insertFrameAgreementD ");
                        throw e;                    
                    } 
                }else {//jednaki su - ne radi se ništa
                    continue;
                }
            }
            bc.commitTransaction();
        }//end iter4
        
    }
    
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("3433766054"));
        bp.setArgs(args);
        new BO550().run(bp);       
    }

}







