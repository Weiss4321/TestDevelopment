/*
 * hrazst Created on 2008.06.17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo29;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.TimeCounter;
import hr.vestigo.modules.rba.util.TimeUtils;



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
  
public class BO290 extends Batch{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo29/BO290.java,v 1.6 2008/10/14 09:23:30 hrazst Exp $";

    private BatchContext bc = null;
    private BO291 bo291=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BigDecimal masterRegInsId=null; //id institucije upisa 
    private BigDecimal masterHfHfcId=null; //id vrste tereta koja se zapisuje
    
    
    /**
     * datum pustanja obrade , ulazni parametar batcha
     */
    private Date processDate=null;
   
    public String executeBatch(BatchContext batchContext) throws Exception {
        
        this.bc=batchContext;        
        TimeCounter tc=null;tc=new TimeCounter(bc);
        
        tc.start("executeBatch.");
        
        bo291 = new BO291(bc);
        
        try {
            checkArguments();
            bc.debug("Argumenti provjereni.");           
        } catch (Exception e) {
            bc.error("Greska pri provjeri argumenata.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        try {
            bo291.insertIntoEvent();            
        }
        catch (Exception e) {
            bc.error("Greska pri insertiranju u event tablicu.",e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();         
        bc.debug("Event insertiran.");
        
        try {
            masterRegInsId=bo291.getUserCodeId("kol_reg_ins", "FINA");
        } catch (Exception e) {
            bc.info("Greska pri dohvatu iz user_code_value.");
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        try {
            masterHfHfcId=bo291.getSystemCodeId("clt_hf_vehicle", "3");
        } catch (Exception e) {
            bc.info("Greska pri dohvatu iz system_code_value.");
            return RemoteConstants.RET_CODE_ERROR;
        }        
        
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
        BO29Data collHeadData=new BO29Data();
        BO29Data.CollHfPriorData hfPriorData=null;
        BO29Data.InDataDwnItem inDataDwhItem=null;
        
        BO291.IteratorFinaVehicle iterFinaVehicle=null;
        BO291.IteratorReportData iterReportData=null;
        
        BigDecimal colProId=null;
        int colNumber=0; //broj dobro obradenih slogova
        
        colProId=bo291.getColProcId(processDate, "2", "1");        
        if(colProId!=null){
            bc.info("Obrada za dani datum je vec uspjesno odradena!");
            throw new Exception("Obrada za dani datum je vec uspjesno odradena!");
        }else{
            //ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
            colProId=bo291.getColProcId(processDate, "2", "0");
            
            if(colProId!=null){
                bc.info("Obrada za dani datum vec postoji (id= "+colProId+") ali nije zavrsena. Nastavljam...!");
            }else{                
                bc.info("\nNe postoji obrada za zadani datum. Pocinje potpuno nova obrada.");
                try {            
                    colProId=bo291.insertIntoColProc(processDate);            
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
        iterFinaVehicle=bo291.fetchFinaVehicle(processDate,"2");        
        Vector pomVector=null;
        String status="";
        BigDecimal colHeaId=null;
        Date approvalDate=null;
        
        //sva dohvacena vozila koja se trebaju obraditi se iteriraju
        while (iterFinaVehicle.next()){
            bc.debug("Pocinje obrada collaterala sa vozilo sa sasijom="+iterFinaVehicle.br_sasije());
            
            inDataDwhItem=null;
            hfPriorData=null;
            
            if(!iterFinaVehicle.upisano_pravo().equals("DA")){
                inDataDwhItem=fillInDataDwnItem(colProId, iterFinaVehicle.br_sasije(), "7", null);                
            }else{
                pomVector=checkForErrors(iterFinaVehicle.br_sasije().trim());
                status=(String) pomVector.get(0);
                colHeaId=(BigDecimal) pomVector.get(1);
                approvalDate=(Date) pomVector.get(2);
                
                //nakon sto se izvrse provjere, i vraceni status je razlicit od 0, ne nastavlja se sa obradom sloga jer je doslo do nekog error-a
                //nego se generira slog stim statusom za tablicu in_data_dwh_item, a ako je status 0 nastavlja se dalje sa obradom sloga                
                if(!status.equals("0")){                   
                    inDataDwhItem = fillInDataDwnItem(colProId, iterFinaVehicle.br_sasije(), status, colHeaId); 
                }else {
                    //ako je approvalDate==null status se postavlja na 6 ali slog se i dalje obraduje i update-a
                    if(approvalDate==null){
                        status="6";
                    }
                    hfPriorData=collHeadData.new CollHfPriorData();
                    hfPriorData.reg_ins=masterRegInsId;
                    hfPriorData.veh_con_num=iterFinaVehicle.br_zakljucka();
                    hfPriorData.con_num=iterFinaVehicle.br_uloska();
                    hfPriorData.reg_place=iterFinaVehicle.upisnicko_mjesto();
                    hfPriorData.rec_lop="D";
                    hfPriorData.con_date=iterFinaVehicle.datum_upisa();
                    hfPriorData.date_rec_lop=iterFinaVehicle.datum_upisa();
                    hfPriorData.hf_date_reciv=iterFinaVehicle.datum_upisa();
                    hfPriorData.hf_hfc_id=masterHfHfcId;
                    hfPriorData.hf_date_hfc_from=approvalDate;
                    
                    //update-aju se podaci o upisanoj fiduciji u tablici coll_hf_prior, i generira se slog za tablicu in_data_dwh_item
                    bo291.updateOneCollHfPrior(colHeaId, hfPriorData);
                    inDataDwhItem = fillInDataDwnItem(colProId, iterFinaVehicle.br_sasije(), status, colHeaId);
                }
            }          
            bo291.insertIntoInDataDwhItem(inDataDwhItem);            
            bc.commitTransaction();            
        }
        
        bo291.updateColProc(colProId, bo291.getNumberOfRowsFromColProc(colProId)); 
        bc.commitTransaction();        
               
        String fileDateTime="";
        bc.debug("Dohvat podataka za izvjestaj za colProId="+colProId);
        
        iterReportData=bo291.fetchDataForReport(colProId, true); 
        fileDateTime=DateUtils.createYYYYMMDDString(DateUtils.getCurrentDate())+ "-" +TimeUtils.getCurrentTime().toString().replace(":", ".");                
        String fileUpdatedData=writeFile(iterReportData, bc.getOutDir(), "preuzetaVozila_"+fileDateTime+".csv");
        
        iterReportData=bo291.fetchDataForReport(colProId, false); 
        fileDateTime=DateUtils.createYYYYMMDDString(DateUtils.getCurrentDate())+ "-" +TimeUtils.getCurrentTime().toString().replace(":", ".");        
        String fileNotUpdatedData=writeFile(iterReportData, bc.getOutDir(), "nepreuzetaVozila_"+fileDateTime+".csv");
        
        SendMail mail = new SendMail();       
        String recipients=null;
        
        try {
            recipients=bo291.fetchRecipients();    
        } catch (Exception e) {
            bc.info("Greska pri dohvatu mail adresa.");
            returnCode=RemoteConstants.RET_CODE_WARNING;
        }   
        
        if(recipients==null || recipients.equals("")){
            bc.info("Datoteke nisu poslane jel nije bilo mail adresa na koje bi se trebale poslat.");
        }else{
            String subject="bo29:Ucitavanje file-a FINA s podacima o upisu prava u Upisnik.";
            String msg="Ucitavanje FINA datoteke zavrsilo. U prilogu su datoteke sa preuzetim i nepreuzetim podacima.\n\n";
            String files=fileUpdatedData+"///"+fileNotUpdatedData;
            int mailFlag=mail.send(bc, "", "", recipients, "", "", subject, msg, null, files);            
            bc.info("mailFlag(0-poslano,1-error)="+mailFlag);
        }        
    }
    
    /**
     * 
     * @param brSasije
     * @return Vector koji u sebi ima 3 elementa<br>
     * 0 element je status, koji moze biti (0,1,2,3,4,5,8 - pogledat u specifikaciju sta koji znaci)<br>
     * 1 element je colHeaId ako se uspio dohvatit, inace je null<br>
     * 2 elemenat je approvalDate kredita ako se uspio dohvatit, inace je null
     * @throws Exception
     */
    private Vector checkForErrors(String brSasije) throws Exception{
        BigDecimal colHeaId=null;
        BigDecimal collHfPriorId=null;
        int isExistCredit=0;
        Date appDate=null;
        Vector returnVector=new Vector(3);
        
        for(int i=0;i<3;i++) returnVector.add(null);
        
        returnVector.set(0,"0"); //postavljam pocetno status stanje u vector
        
        colHeaId=bo291.getColHeaId(brSasije);
        
        if(colHeaId==null){
            returnVector.set(0,"8");
            return returnVector;
        }else if(colHeaId.compareTo(new BigDecimal("-1"))==0){
            returnVector.set(0,"1");
            return returnVector;
        }
        returnVector.set(1,colHeaId); //postavljam colHeaId u vector
        
        collHfPriorId=bo291.getCollHfPriorId(colHeaId);
        if(collHfPriorId==null){
            returnVector.set(0,"3");
            return returnVector;
        }else if(collHfPriorId.compareTo(new BigDecimal("-1"))==0){
            returnVector.set(0,"2");
            return returnVector;
        }        
        
        isExistCredit=bo291.checkCredit(collHfPriorId);
        if(isExistCredit==0){
            returnVector.set(0,"5");
            return returnVector;
        }else if(isExistCredit==-1){
            returnVector.set(0,"4");
            return returnVector;
        }  
        
        appDate=bo291.getApprovalDate(collHfPriorId);
        
        returnVector.set(2, appDate);; //postavljam approval date u vector    
        
        return returnVector;
    }
    
    /**Metoda kreira file sa podacima.<br>
     * @param errors
     * @param dirName - ime direktorija gdje ce se kreirat file
     * @param fileName - ime filea u koji ce se zapisat podaci
     * @return ime file zajedno sa putanjom gdje se nalazi.
     */
    private String writeFile(BO291.IteratorReportData iter, String dirName, String fileName) throws Exception{
        StringBuffer buffy = new StringBuffer();
        BufferedWriter outFile=null;
        String pomFileName=null;
        int i=1;

        if(!dirName.endsWith("/")){
            dirName+="/";
        }
        
        pomFileName=dirName+fileName;
        outFile=new BufferedWriter(new FileWriter(pomFileName));
                
        buffy.append("R.B.").append(";");
        buffy.append("Broj šasije").append(";");
        buffy.append("Šifra kolaterala").append(";");       
        buffy.append("Status preuzimanja").append(";");
        buffy.append("Opis statusa preuzimanja").append(";"); //ovdje treba ici opis statusa
        buffy.append("Datum i vrijeme preuzimanja").append("\n");
        outFile.write(buffy.toString());
        
        while(iter.next()){
            buffy=new StringBuffer();
            buffy.append(i).append(";");
            buffy.append(iter.input_code()).append(";");
            if(iter.col_num()==null){
                buffy.append("").append(";");
            }else{
                buffy.append(iter.col_num()).append(";");
            }            
            buffy.append(iter.status()).append(";");
            buffy.append(iter.status_desc()).append(";"); //ovdje treba ici opis statusa
            buffy.append(iter.proc_ts()).append("\n");
            outFile.write(buffy.toString());
            i++;
        }
       
        outFile.close();
        return pomFileName;        
    }
    
 
    private BO29Data.InDataDwnItem fillInDataDwnItem(BigDecimal colProId,
                                                       String brSasije, 
                                                       String status, 
                                                       BigDecimal colHeaId){
        BO29Data bo29Data=new BO29Data();
        BO29Data.InDataDwnItem inDataDwhItem=bo29Data.new InDataDwnItem();
        
        inDataDwhItem.col_pro_id=colProId;
        inDataDwhItem.input_code=brSasije;
        inDataDwhItem.status=status;
        inDataDwhItem.output_id=colHeaId;
        
        return inDataDwhItem;
    }
         
    private void checkArguments() throws Exception{
        
        if (bc.getArgs().length!=2){
            bc.debug("Neispravan broj argumenata!");
            throw new Exception("Neispravan broj argumenata!");
        }
        
        if (!bc.getArg(0).equals("RB")){
            bc.debug("Greska pri parametru bank_sign. Bank sign mora biti 'RB'!");
            throw new Exception("Greska pri parametru Bank sign. Bank sign mora biti 'RB'!");
        }else{
            bc.setBankSign(bc.getArg(0));
        }
        
        try {
            processDate=DateUtils.createDateFromString(bc.getArg(1));
        } catch (Exception e) {
            bc.debug("Greska pri konverziji parametra datuma revalorizacije.");
            throw new Exception("Greska pri konverziji parametra datuma revalorizacije.");
        }       
    }
    
    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2533241003"));
        batchParameters.setArgs(args);
        new BO290().run(batchParameters);  
    }    
    
}
