//created 2011.11.11
package hr.vestigo.modules.collateral.batch.bo69;


import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo69.BO691.CessionIterator;
import hr.vestigo.modules.collateral.common.yoy6.CollListQData;
import hr.vestigo.modules.collateral.common.yoy6.YOY60;
import hr.vestigo.modules.collateral.common.yoy9.YOY90;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.DateUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *  Istek trzisne vrijednosti cesije
 * @author hramlo
 */
public class BO690 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo69/BO690.java,v 1.5 2014/03/10 13:32:32 hrakis Exp $";

    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BatchContext bc = null;
    private BigDecimal eve_id=null;
    java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
    long timeT = calendar.getTime().getTime();
    public Timestamp CREATE_DATE_TIME  = new java.sql.Timestamp(timeT);
    private BO691 bo691=null;
    private CessionIterator iter=null;
    
    StringBuilder cesijeKandidati = new StringBuilder();
    StringBuilder cesijeIstekle = new StringBuilder();


    public BO690(){     
    }

    public String executeBatch(BatchContext bc) throws Exception{
        
        this.bc=bc;
        bo691=new BO691(bc); 

        bc.getBatchTransactionHeader().setModuleName("COL");
        
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
            
            //iterator s dohvacenim podacima - okviri           
            try{
                iter=bo691.getCessionData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo691.getFrameData()!!!");
                e.printStackTrace();                
            }

            while(iter.next())
            {
                
                bc.info("*** COL_CES_ID:"+iter.COL_CES_ID()+",COL_HEA_ID:"+iter.COL_HEA_ID()+",COL_NUM:"+iter.COL_NUM()+",CES_EXP_DATE:"+iter.CES_EXP_DATE()+",DINAMIKA:"+iter.DINAMIKA()+",REAL_EST_NOMI_DATE:"+iter.REAL_EST_NOMI_DATE()+" ***");
                
                int dinamika = Integer.parseInt(iter.DINAMIKA());
                
                Date datumKandidata = DateUtils.addOrDeductMonthsFromDate(iter.CES_EXP_DATE(), dinamika);
                bc.info("datum-dinamika:"+datumKandidata);
                datumKandidata = DateUtils.addOrDeductDaysFromDate(datumKandidata, 23, true);
                bc.info("datumKandidata:"+datumKandidata);
                
                //datum isteka trzisne vrijednosti je datum_stanja_potrazivanja + dinamika(u mjesecima) + 30 dana a kako su kandidati 7 dana prije isteka onda je potrebno dodati jos 7 dana
                Date datumIsteklihPolica = DateUtils.addOrDeductDaysFromDate(datumKandidata, 7, true);
                bc.info("datumIsteklihPolica:"+datumIsteklihPolica);
                
                // ako nema niti jedan aktivni ništa se ne radi sa time, aktivanPlasman je null
                String aktivanPlasman = bo691.getExposure(iter.COL_HEA_ID());
                if(aktivanPlasman==null)
                {
                    bc.info("Kolateral "+iter.COL_HEA_ID()+" nema aktivan plasman!");
                    continue;
                }

                
                if(DateUtils.whoIsOlder(DateUtils.getCurrentDate(), datumKandidata)==0)
                {
                    //ako su datumi jednaki to znaci da 7 dana prije akcije cesija zadovoljava uvjete pa se salje kao kandidat                    
                    cesijeKandidati.append(slogIzvjestaja("0", iter.COL_NUM(), aktivanPlasman));
                }
                else if( DateUtils.whoIsOlder(DateUtils.getCurrentDate(), datumIsteklihPolica)>=0 )
                {
                    //istekle cesije
                    try{
                        bo691.setMarketVal(iter.COL_HEA_ID());
                        
                        cesijeIstekle.append(slogIzvjestaja("1", iter.COL_NUM(), aktivanPlasman));
                        
                    }catch(Exception e){
                        bc.warning("puklo na setMarketVal");
                        throw e;
                    }
                    //step 20
                    //isknjizenje cesije
                  
                    String account_indic=bo691.checkColAccount(iter.COL_HEA_ID());

                    if(account_indic.equalsIgnoreCase("1")){
                        //ako se kol knjizi poziva se common za isknjizenje
                        YOY90 yoy90=new YOY90(bc);
                        yoy90.CollPosting(iter.COL_HEA_ID(), true);
                    }
                    //step 30 slanje deakt kolaterala na listu neaktivnih


                    BigDecimal source_list=bo691.getSourceList(iter.COL_HEA_ID());
                    
                    
                    YOY60 yoy60=new YOY60(bc);
                    CollListQData plq =new CollListQData();

                    plq.col_hea_id=iter.COL_HEA_ID();
                    plq.status="1";
                    plq.income_time=CREATE_DATE_TIME;
                    plq.use_id=bc.getUserID();
                    plq.release_time=CREATE_DATE_TIME;
                    plq.cmnt="ISTEK TRZISNE VRIJEDNOSTI";
                    plq.action_type="ISKNJIZI";
                    plq.org_uni_id=new BigDecimal(53253);
                    plq.source_list=source_list.toString();
                    plq.status_source_list="1";
                    plq.target_list=source_list.toString();
                    plq.status_target_list="0";
                    plq.target_list_use_id=null;
                    plq.target_list_org_uni_id=null;
                    yoy60.update_insert_CollListQ(plq);

                }
                
                bc.commitTransaction();

            }//end iter 
            
            bc.debug("zapis podataka u datoteke");
            
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            
            bc.debug("cesijeIstekle:\n"+(cesijeIstekle.toString()).replaceAll(";", "\n"));
            if(cesijeIstekle.length() > 0)
            {
                //istekle
                String shortFileNameIstekle = "Obavijest o isteku vrijednosti cesija " + dateString + ".csv";
                String fileNameIstekle = bc.getOutDir() + "/" + shortFileNameIstekle;
                OutputStreamWriter streamWriterIstekle = new OutputStreamWriter(new FileOutputStream(new File(fileNameIstekle)), "Cp1250");
                
                streamWriterIstekle.write(cesijeIstekle.toString());
                streamWriterIstekle.flush();
                streamWriterIstekle.close();
                bc.debug("Podaci zapisani u datoteku isteklih cesija");
                
                YXY70.send(bc, "csvbo69", bc.getLogin(), fileNameIstekle);
            }
            
            bc.debug("cesijeKandidati:\n"+(cesijeKandidati.toString()).replaceAll(";", "\n"));
            if(cesijeKandidati.length() > 0)
            {
                //kandidati
                String shortFileNameKandidati = "Najava isteka vrijednosti cesija " + dateString + ".csv";
                String fileNameKandidati = bc.getOutDir() + "/" + shortFileNameKandidati;
                OutputStreamWriter streamWriterKandidati = new OutputStreamWriter(new FileOutputStream(new File(fileNameKandidati)), "Cp1250");
                
                streamWriterKandidati.write(cesijeKandidati.toString());
                streamWriterKandidati.flush();
                streamWriterKandidati.close();
                bc.debug("Podaci zapisani u datoteku kandidata");

                YXY70.send(bc, "csvbo692", bc.getLogin(), fileNameKandidati);
            }


        }catch (Exception e) {
            bc.info("greska u obradi podataka bo69!");
            bc.info(bo691.stackTraceToString(e));
            return RemoteConstants.RET_CODE_ERROR;

        }finally{// (bez obzira obrada uspjesno ili neuspjesno zavrsila-zatvaram iterator)            

            if(iter!=null){
                try{
                    iter.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
        } 
        return returnCode;
    }
    
    
    private String writeErrorFile(String errors,String dirName, String nameOfProcessedFile) throws Exception{
        BufferedWriter outFile=null;
        String pomFileName=null;
        
        if(!dirName.endsWith("/")){
            dirName+="/";
        }
        pomFileName=dirName+nameOfProcessedFile.replaceAll(".csv", ".ReportInfo.csv");
        outFile=new BufferedWriter(new FileWriter(pomFileName));
        outFile.write(errors);
        outFile.close();
        bc.debug("pomFileName"+pomFileName);
        return pomFileName;        
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
            event.put("eve_typ_id", new BigDecimal("4835696704"));
            event.put("event_date", new java.sql.Date(System.currentTimeMillis()));
            event.put("cmnt", "bo69:Istek tr.vrijednosti cesija");
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

    //trebalo bi u system_code_value....nutmeg
    private String slogIzvjestaja(String tip, String kolateral, String vlasnik){
        if(tip.equals("0"))
            return "Cesiji  "+kolateral.trim()+" isti\u010De rok za dostavu novog stanja potra\u017Eivanja. Za 7 dana \u0107e vrijednost cesije vlasnika plasmana "+vlasnik.trim()+" biti nula ;\n";
        else  
            return "Cesiji  "+kolateral.trim()+" vlasnika plasmana "+vlasnik.trim()+" je postavljena nova vrijednost nula ;\n";
    }
    
    
    

    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("4835680704"));
        bp.setArgs(args);
        new BO690().run(bp);       
    } 
}

