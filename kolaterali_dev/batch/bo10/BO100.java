package hr.vestigo.modules.collateral.batch.bo10;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.StringUtils;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Batch za preuzimanje podataka o garantnim depozitima iz OBC/DPF-a
 * @author hraamh
 */ 
public class BO100 extends Batch {

    public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo10/BO100.java,v 1.36 2014/01/16 09:33:01 hrajkl Exp $";

    private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;

    private BatchContext bc = null;
    private boolean debug=false;
    private BO101 bo101=null;
    private BigDecimal org_uni_id=null;
    private BigDecimal use_id=null;
    private BigDecimal eve_typ_id=null;
    private Date proc_date=null;
    private Timestamp user_lock=null;


    public BO100(){
        super();
        this.org_uni_id=new BigDecimal(53253);
        this.eve_typ_id= new BigDecimal("1716855003");	
        this.use_id=new BigDecimal(1);
    }

    /* (non-Javadoc)
     * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
     */
    public String executeBatch(BatchContext contex) throws Exception {
        this.bc=contex;
        String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

        bc.debug("******************** BO100.executeBatch() poceo *********************");

        // Provjera ulaznih parametara
        if ((bc.getArgs().length!=1)&&(bc.getArgs().length!=2)){
            bc.debug("Neispravan broj argumenata!");
            return RemoteConstants.RET_CODE_ERROR;
        }
        if (bc.getArg(0).equals("RB")==false){
            bc.debug("Bank sign mora biti 'RB'!");
            return RemoteConstants.RET_CODE_ERROR;
        }
        if(bc.getArgs().length==2){
            if (bc.getArg(1).equals("0")==true){
                this.debug=true;
            }
        } 

        long startTime=System.currentTimeMillis();
        this.bo101 = new BO101(bc,org_uni_id,use_id);

        // Dohvat datuma za koji su podaci o depozitima
        this.proc_date=bo101.getDepositDate();
        if(proc_date==null){
            return RemoteConstants.RET_CODE_ERROR;
        }

        // Provjera da li je obrada za datum veæ izvršena ili je u tijeku
        boolean newProces=false;
        if (debug) bc.debug("\nBO100->provjera da li je obrada vec pokrenuta");
        BigDecimal proc_id=this.bo101.selectColProc(this.proc_date,"D","1");
        if(proc_id!=null){
            bc.debug("Obrada za dani datum je vec uspjesno odradena! " + proc_id);
            return RemoteConstants.RET_CODE_SUCCESSFUL;
        }else{
            //ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
            proc_id=this.bo101.selectColProc(this.proc_date,"D","0");
            if(proc_id!=null){

                bc.debug("Obrada za dani datum vec postoji ali nije zavrsena. Nastavljam...!" + proc_id);
                this.bo101.setColProId(proc_id);
            }else{

                //nema nikakve obrade - potpuno nova obrada
                bc.debug("Potpuno nova obrada!");
                proc_id=this.bo101.getColProId();
                newProces=true;
            }
        }
        bc.debug("\nBO100->insertam dogadaj");
        bc.beginTransaction();

        // Logiranje poziva izvrsavanja batcha u tablicu Event
        try{
            if(newProces){
                this.putColProc("D","0",new BigDecimal(0),this.user_lock,true);
            }
            //ODKOMENTIRAJ ZA PRODUKCIJU
            bo101.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.debug("\nBO100->dogadaj insertan " + proc_id);
        bc.commitTransaction();

        Date today = new Date(System.currentTimeMillis());
        Date maxDate = Date.valueOf("9999-12-31");
        String razlog1 = "Depozit u orig. modulu nema datum dospije\u0107a pa isti nije a\u017Euriran. Ostali podaci o depozitu su a\u017Eurirani.";
        String razlog2 = "Depozit je na\u0111en u modulu kolaterala, ali ga nema u podacima iz DWH - u modulu nije a\u017Eurirano stanje.";
        String razlog3 = "Depozit u Siriusu nema upisanu partiju plasmana pa nije automatski kreiran u modulu kolaterala";
        String razlog4 = "";

        // Stvaranje izlazne datoteke
        String dateString = new SimpleDateFormat("yyyyMMdd").format(today);      
        String dir = bc.getOutDir() + "/";
        String fileName = dir + "DepositWithNoMaturityDate_" + dateString + ".csv";             
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka.");

        // Zapisivanje zaglavlja u izlaznu datoteku
        streamWriter.write(getHeaderRow());        

        IteratorDepositDWH_new iter=null;
        try{
            // Dohvat podataka o depozitima
            iter = bo101.selectDepositDWH(proc_id);
            if(iter!=null){			
                while(iter.next()){
                    String inputStatus = "0";
                    boolean insert = false;
                    boolean deactivate = false;
                    boolean RBA_eligibility = true;
                    // Stvaranje objekta sa podacima
                    InputData inputData = bo101.getInputData(iter);
                    bc.debug("inputData:"+inputData.toString());
                    // Provjera podataka
                    int check = inputData.check();

                    if(check!=0){
                        if(debug) bc.debug("ignoriram slog: "+inputData);
                        bc.debug("ignoriram slog: "+inputData);
                        inputStatus=""+check;
                        bc.beginTransaction();
                        bo101.insertInDataDwhItem(bo101.getColProId(),null,inputData.acc_num,inputStatus,null);
                        bc.commitTransaction();
                        continue;
                    }

                    bc.debug("obradjujem partiju depozita: "+inputData.cde_account+"-"+inputData.cde_dep_unti+"-"+inputData.status);
                    // Ako su neki potrebni ulazni atributi prazni zamjenjuje ih s zamjenskim
                    if((inputData.cde_owner==null)||(inputData.cde_owner.equalsIgnoreCase(""))||(inputData.cde_owner.equalsIgnoreCase("0"))){
                        inputStatus="5";
                        inputData.cde_owner=inputData.loan_owner;
                    }

                    // Ako je istekao depozit
                    boolean outOfDateDeposit=false;
                    // FBPr200020517 - izbaèena provjera da li je depozit istekao - prema datumu dospjeæa iz dwh podataka
//                    if(inputData.cde_dep_unti != null && DateUtils.whoIsOlder(inputData.cde_dep_unti,today)<=0){
//                        outOfDateDeposit=true;
//                        RBA_eligibility = false;
//                    }
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
                        cde_cur_id=bo101.selectCurrencyIdWithCodeNum(inputData.cde_cur);
                    }else{
                        cde_cur_id=bo101.selectCurrencyIdWithCodeChar(inputData.cde_cur);
                    }

                    // Dohvat col_cas_id i col_hea_id iz coll_cahsdep tablice
                    BigDecimal col_cas_id = null;
                    BigDecimal col_hea_id = null;
                    String collateral_status = null;
                    Map collCashMap = null;
                    try{
                        collCashMap = bo101.SelectFromCollCashDeposit(inputData.cde_account,inputData.cde_reg_no);
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
                            bc.debug(".....BO101.sqlj SelectFromCollCashDeposit        Message  : " + e.getMessage());
                            bc.debug(".....BO101.sqlj SelectFromCollCashDeposit      Error code : " + e.getErrorCode());
                            bc.debug(".....BO101.sqlj SelectFromCollCashDeposit        SQLState : " + e.getSQLState());
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

                    bc.debug("radim insert ili update, col_hea_id: "+col_hea_id+" col_cas_id: "+col_cas_id);				
                    // Slog ignoriram samo ako je novi depozit i nema iznosa                    
                    if(noAmount && insert){   
                        if(debug) bc.debug("ignoriram slog: "+inputData);
                        bc.debug("ignoriram slog: "+inputData);
                        inputStatus="4";
                        bc.beginTransaction();
                        bo101.insertInDataDwhItem(bo101.getColProId(),null,inputData.acc_num,inputStatus,null);
                        bc.commitTransaction();
                        continue;
                    }

                    // 10.08.2010, radi garantnih depozita iz siriusa koji nemaju partiju plasmana i vlasnika plasmana 
                    BigDecimal loan_cus_id=null;
                    String loan_cus_code=null;
                    String loan_cus_name=null;
                    Map customerMap=bo101.selectFromCustomer(inputData.loan_owner);	
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
                        customerMap=bo101.selectFromCustomer(inputData.cde_owner);	
                        if(customerMap!=null){                         
                            cde_cus_id=(BigDecimal)customerMap.get("cus_id");
                            cde_cus_code=(String)customerMap.get("code");
                            cde_cus_name=(String)customerMap.get("name");
                        }
                    }
                    bc.debug("C ....dohvatio vlasnika...loan_owner, cde_owner, cde_cus_id!: "+inputData.loan_owner+"-"+inputData.cde_owner+"-"+cde_cus_id);   

                    // Ako nema iznosa partije plasmana 
                    boolean yesAccount=true;
                    String account ="D";
                    Map cusaccExposureMap=null;                                  
                    if(inputData.acc_num==null || inputData.acc_num.equalsIgnoreCase("")){
                        // ako nema broj partije plasmana na ulazu
                        yesAccount=false;
                        account ="N";
                        
                        // FBPr200018975 - dodan ispis u datoteku; premješten ovaj dio koda buduæi da je za za upis u datoteku potreban cde_cus_name
                        // FBPr200019821/TST0700000540 - dodan uvjet - upisuje se u datoteku samo ako je novi depozit, a nema upisanu partiju plasmana; te ako mu namjena nije matematièka prièuva (038) ili tehnièka prièuva (039)
                        // 10930 - daodan uvjet A/B/C za izbacivanje kandidata predviðenih za zapisivanja u datoteku (rijeè je o novim depozitima koji je postoje u modulu kolaterala tj postoje u PAY i RET)
                        
                        if (insert && bo101.acceptedPurpose(inputData.cde_account.trim()) && !izbaciUljeza(inputData.cde_account, inputData.acc_num, inputData.status))                                                             
                            streamWriter.write(getDetailsRow(inputData.cde_account, inputData.cde_cur, inputData.cde_amount, inputData.cde_owner, cde_cus_name, razlog3));
                    }else{    
                        // ako ima broj partije plasmana na ulazu
                        account ="D";
                        cusaccExposureMap = bo101.selectFromCusaccExposure(inputData.acc_num); 
                        
                        // RTC 6336 
                        // RTC 10567 - dodan uvjet - upisuje se u datoteku samo ako je novi depozit
                        // 10930 - daodan uvjet A/B/C za izbacivanje kandidata predviðenih za zapisivanja u datoteku (rijeè je o novim depozitima koji je postoje u modulu kolaterala tj postoje u PAY i RET)
                        
                        if(insert && cusaccExposureMap==null && !izbaciUljeza(inputData.cde_account, inputData.acc_num, inputData.status)){
                            bc.debug("Postoji partija plasmana " +inputData.acc_num + " na ulazu, ali ona ne postoji u cusacc_exposure.");
                            // provjera da li je partija plasmana ispravnog formata
                            if(!validateAccountFormat(inputData.acc_num)){
                                razlog4 = "Depozit u Siriusu u polju partija plasmana ima popunjen podatak <"+inputData.acc_num+"> koji nije DWH prepoznatljiva partija plasmana pa nije automatski kreiran u modulu kolaterala";
                                streamWriter.write(getDetailsRow(inputData.cde_account, inputData.cde_cur, inputData.cde_amount, inputData.cde_owner, cde_cus_name, razlog4));
                            }
                        }
                    }   
                    if(cusaccExposureMap==null){
                        yesAccount = false;
                        account = "N";
                        cusaccExposureMap = new HashMap();
                    }
                    if (!yesAccount)
                        inputStatus="P";

                    Date approvalDate=(Date)cusaccExposureMap.get("date");
                    String request_no=(String)cusaccExposureMap.get("request_no");
                    BigDecimal cus_acc_id=(BigDecimal) cusaccExposureMap.get("cus_acc_id"); 

                    if(debug){
                        bc.debug("obradujem slog: "+inputData);
                    }else{
                        bc.debug("obradujem partiju: "+inputData.acc_num+" - "+account+" - " 
                                + cus_acc_id + " partija depozita: "+inputData.cde_account+ " cas_exc_id "+inputData.cas_exc_id);
                    }

                    if((outOfDateDeposit || noActiveStatusDeposit) && (("3".equals(collateral_status)) || ("F".equals(collateral_status)))){
                        deactivate=true;
                    }
                    bc.debug("D ....da li deaktiviram: "+outOfDateDeposit+"-"+noActiveStatusDeposit+"-"+collateral_status);       
 					    
                    //POCETAK TRANSAKCIJE
                    bc.beginTransaction();
                    col_hea_id=bo101.putCollHead(col_hea_id,inputData,cde_cur_id,loan_cus_id,cde_cus_id,deactivate,RBA_eligibility,yesAccount);
                    if(col_hea_id==null && yesAccount){
                        bc.debug("nije upisan/promijenjen slog u tablici COLL_HEAD za acc_no="+inputData.acc_num);
                        return RemoteConstants.RET_CODE_ERROR;
                    } 
                    bc.debug("\n col_hea_id="+col_hea_id);

                    // col_cas_id
                    col_cas_id=bo101.putCollCashdep(col_cas_id,inputData,col_hea_id,cde_cur_id,!(noAmount && outOfDateDeposit),yesAccount);
                    if(col_cas_id==null && yesAccount){
                        bc.debug("nije upisan/promijenjen slog u tablici COLL_CASHDEP za acc_no="+inputData.acc_num +" i col_hea_id="+col_hea_id);
                        return RemoteConstants.RET_CODE_ERROR;
                    } 
                    bc.debug("\n col_cas_id="+col_cas_id);

                    // col_own_id
                    BigDecimal col_own_id=null;
                    if(!insert){
                        col_own_id= bo101.selectCollOwnerId(cde_cus_id,col_hea_id);
                    }		
                    col_own_id=bo101.putCollOwner(col_own_id,inputData.cde_owner,col_hea_id,cde_cus_id,cde_cus_code,yesAccount);
                    if(col_own_id==null && yesAccount){
                        bc.debug("nije upisan slog u tablicu COLL_OWNER za ime="+cde_cus_name +" i col_hea_id="+col_hea_id);
                        return RemoteConstants.RET_CODE_ERROR;
                    }
                    if(debug) bc.debug("\n col_own_id="+col_own_id);

                    // coll_hf_prior_id
                    BigDecimal coll_hf_prior_id=null;
                    if(!insert){
                        coll_hf_prior_id=bo101.selectCollHfPriorId(col_cas_id,col_hea_id);
                    }
                    bc.debug("dohvacen coll_hf_prior_id iz COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id);  
                    coll_hf_prior_id=bo101.putCollHfPrior(coll_hf_prior_id,inputData,col_hea_id,col_cas_id,cde_cur_id,approvalDate,deactivate,yesAccount,insert);
                    if(coll_hf_prior_id==null && yesAccount && insert){   
                        bc.debug("nije upisan slog u tablicu COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id); 
                        return RemoteConstants.RET_CODE_ERROR;
                    } else if (coll_hf_prior_id==null) {
                        bc.debug("nije dohvacen slog iz tablice COLL_HF_PRIOR za col_hea_id="+col_hea_id + " - col_cas_id:="+col_cas_id + " - coll_hf_prior_id:" + coll_hf_prior_id); 
                    }
                    if(debug) bc.debug("\n coll_hf_prior_id="+coll_hf_prior_id);

                    // loan_ben_id
                    BigDecimal loan_ben_id=null;
                    if(!insert){
                        loan_ben_id=bo101.selectLoanBeneficiaryId(coll_hf_prior_id,inputData.acc_num);
                    } 
                    bc.debug("dohvacen loan_ben_id iz LOAN_BENEFICIARY za coll_hf_prior_id="+coll_hf_prior_id + " - loan_ben_id="+loan_ben_id);  
                    loan_ben_id=bo101.putLoanBeneficiary(loan_ben_id, inputData.loan_owner, inputData.acc_num ,coll_hf_prior_id,loan_cus_id,cus_acc_id,request_no,yesAccount,insert);
                    if(loan_ben_id==null && yesAccount && insert && loan_cus_id != null){
                        bc.debug("nije upisan slog u tablicu LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id);
                        return RemoteConstants.RET_CODE_ERROR;
                    } else if(loan_ben_id==null) {
                        bc.debug("nije dohvacen slog u tablici LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id);						    
                    }
                    if(debug) bc.debug("\n loan_ben_id="+loan_ben_id);

                    // Odredjivanje datuma krajnjeg roka dospijeca depozita, samo ako postoji datum dospijeca i ako je razlicit od 31.12.9999
                    bc.debug("11 .... odredi datum krajnjeg dospijeca depozita za col_hea_id = "+col_hea_id + " i za datum dospijeca = " + inputData.cde_dep_unti);
                    if (inputData.cde_dep_unti != null && !(inputData.cde_dep_unti.equals(""))) {
                        if(DateUtils.whoIsOlder(inputData.cde_dep_unti,maxDate) <= 0){
                            bo101.setFinalMaturityDate(col_hea_id);
                        }
                    } else {
                        // Depozit nema datum dospijeca, treba ga zapisati u csv i to samo ako postoji u modulu kolaterala 
                        if (col_hea_id != null) 
                            streamWriter.write(getDetailsRow(inputData.cde_account, inputData.cde_cur, inputData.cde_amount, inputData.cde_owner, cde_cus_name, razlog1));
                    }

                    // Postavljanje svih prihvatljivosti 
                    bc.debug("2 ....Pozivam common za postavljanje prihvatljivosti...!: "+col_hea_id);	
                    if (col_hea_id != null) {
                        if (inputData.cde_dep_unti != null) {
                            YOYG0 yoyg0 = new YOYG0(bc, col_hea_id);
                            yoyg0.azurirajPrihvatljivosti();
                        }  
                        inputStatus = "U";                       
                        if(insert){
                            bc.debug("3 ....Pozivam INSERT u COLL_LIST_Q: "+col_hea_id);                                
                            bo101.insertCollListQ(col_hea_id,"3",deactivate);
                            inputStatus = "I";
                        }else if(deactivate){
                            bc.debug("4 ....Pozivam UPDATE u COLL_LIST_Q: "+col_hea_id+" - " +collateral_status);                                  
                            bo101.deactivateCollListQ(col_hea_id,collateral_status);
                            bc.debug("5 ....Pozivam INSERT u COLL_LIST_Q: "+col_hea_id+" - " +collateral_status);                                     
                            bo101.insertCollListQ(col_hea_id,collateral_status,deactivate);
                            inputStatus = "D";
                        } 
                    }
                    bo101.insertInDataDwhItem(bo101.getColProId(),null,inputData.acc_num,inputStatus,col_hea_id);
                    bc.commitTransaction();
                    bc.debug("commiting: "+inputData.acc_num);
                    //KRAJ TRANSAKCIJE
                }   
            }    
            BigDecimal recordCount=bo101.getRecordCount();
            this.putColProc("D","1",recordCount,this.user_lock,false);
            iter.close();    	

            // Ispisati depozite kojih postoje u modulu a za njih nema ulaznih podataka za azuriranje           
            IteratorCashDepositInModul iter1=null;
            iter1= bo101.selectModulInDeposit();
            if(iter1!=null){         
                while(iter1.next()){
                    streamWriter.write(getDetailsRow(iter1.cde_account(), iter1.cde_cur(), iter1.cde_amount(), iter1.cde_owner(), iter1.cde_owner_name(), razlog2));
                }
            }
            iter1.close();

            // Zatvaranje izlazne datoteke
            if(streamWriter != null) streamWriter.flush();
            if(streamWriter != null) streamWriter.close();
            bc.debug("Zatvorena izlazna datoteka.");

            // Slanje maila
            SendMail mail= new SendMail();
            String recipients=null;
            try { 
                recipients=bo101.fetchRecipients("csv184");    
            } catch (Exception e) {
                bc.info("Greska pri dohvatu mail adresa.");
                return RemoteConstants.RET_CODE_ERROR;
            }   
            String subject="bo10: Kontrola batcha za a\u017Euriranje depozita.";
            String msg="Lista depozita koji nemaju definiran datum dospijeæa ili postoje u modulu ali ih nema u ulaznim podacima: " +"\n";
            int mailFlag=mail.send(bc, "", "", recipients, "", "", subject, msg, null, fileName);            
            bc.info("mailFlag(0-poslano,1-error)="+mailFlag);       

        }catch (Exception e) {
            e.printStackTrace();
            toReturn=RemoteConstants.RET_CODE_ERROR;
        }finally{
            try{
                iter.close();
            }catch(SQLException sqle){
                sqle.printStackTrace();
                return RemoteConstants.RET_CODE_ERROR;
            }
        }

        bc.debug("\nBO1000-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();

        bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");	
        bc.debug("******************** BO100.executeBatch() zavrsio *********************");

        if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;		
        return toReturn;
    }

    /**
     * Metoda proslijeðene podatke sprema u mapu te poziva metode za insert, odnosno update podataka
     * @param proc_type
     * @param proc_status
     * @param col_number
     * @param user_lock
     * @param insert
     * @throws Exception
     */
    public void putColProc(String proc_type,String proc_status,BigDecimal col_number, Timestamp user_lock, boolean insert) throws Exception{
        Map map = new HashMap();
        map.put("proc_date",proc_date);    
        map.put("value_date",proc_date); 
        map.put("proc_type",proc_type);    
        map.put("proc_way","A");                
        map.put("proc_status",proc_status);          
        map.put("col_number",col_number);                 
        map.put("org_uni_id",org_uni_id);            
        map.put("use_id", use_id); 
        map.put("user_lock", user_lock);
        if(insert){
            this.user_lock=this.bo101.insertColProc(map);
        }else{
            this.user_lock=this.bo101.updateColProc(map);
        }
    }

    /**
     * Metoda formira zaglavlje za izvješæe i vraæa ga u obliku stringa.
     * @return formirano zaglavlje
     */
    private String getHeaderRow(){

        StringBuffer buffer = new StringBuffer();
        buffer.append("Partija depozita").append(";");
        buffer.append("Valuta depozita").append(";");
        buffer.append("Iznos depozita").append(";");
        buffer.append("\u0160ifra vlasnika").append(";");
        buffer.append("Naziv vlasnika").append(";");
        buffer.append("Razlog").append(";");
        return buffer.append("\n").toString();
    }	

    /**
     * Metoda formira jedan red izvješæa i vraæa ga u obliku stringa.
     * @param iter Iterator s podacima
     * @return formirani red
     */
    private String getDetailsRow(String cde_account, String cde_currency, BigDecimal cde_amount, String cde_owner, String cde_cus_name, String razlog) throws SQLException{

        StringBuffer buffer = new StringBuffer();       
        buffer.append(cde_account.trim()).append(";");  
        buffer.append(cde_currency.trim()).append(";");
        buffer.append(cde_amount.toString().trim()).append(";");        
        buffer.append(cde_owner.trim()).append(";");
        buffer.append(cde_cus_name.trim()).append(";");
        buffer.append(razlog.trim()).append(";");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda provjerava da li je string ispravnog formata da bi bio partija.
     * <p>Moguæi formati su: 
     * <p>XXX-XX-XXXXXX - moduli GAR/PKR/KRD/MEN/TRC (na kraju može biti 6-8 znamenki)
     * <p>019-63-XXXXXXXXX - modul KKR 
     * <p>32XXXXXXXX - modul TRC (ukupno 10 znamenki)
     * <p>11XXXXXXXX ili 15XXXXXXXX - modul PPZ
     * <p>60XXXXXXXX - modul OKV
     * <p>80XXXXXXXX ili 81XXXXXXXX - modul LOC
     * @param account broj raèuna koji je potrebno provjeriti
     * @return true ako 
     */
    private boolean validateAccountFormat(String account){

        Pattern pattern1 = Pattern.compile("\\d{3}-\\d{2}-\\d{6,8}");                   // XXX-XX-XXXXXX..
        Pattern pattern2 = Pattern.compile("019-63-\\d{9}");                            // 019-63-XXXXXXXXX
        Pattern pattern3 = Pattern.compile("^((1[15])|(32)|(60)|(8[01]))\\d{8}");       // poèinju sa 11,15,32,60,80 ili 81 i imaju 10 znamenki
        
        if(pattern1.matcher(account).matches()) return true;
        else if(pattern2.matcher(account).matches()) return true;
        else if(pattern3.matcher(account).matches()) return true;
        return false;
    }
    
    /**
     * Metoda prema zahtjevu 10930 iskljuèuje kandidate predviðene za zapisivanja u datoteku prema odreðenom uvjetu(A/B/C)<br>
     * @param partijaDepozita
     * @param partijaPlasmana
     * @param status
     * @return <b>true</b> ako kandidata treba izostaviti od zapisivanja u datoteku inaèe false
     */
    private boolean izbaciUljeza(String partijaDepozita, String partijaPlasmana, String status){
        bc.debug("izbaciUljeza("+partijaDepozita+","+partijaPlasmana+","+status+")");
        //sluèaj A kada je cashdep_dwh.status = 'I'
        if(status!=null && status.trim().equals("I")){
            bc.debug("Zadovoljen uvjet A za izbacivanje uljeza");
            return true;
        }
        
        //sluèaj B kada je cashdep_dwh.cde_account = yyyyMMDD*****
        if(partijaDepozita!=null && partijaDepozita.length()==13){
            String yymmdd = partijaDepozita.substring(0, 8);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                sdf.setLenient(false);
                sdf.parse(yymmdd);
                bc.debug("Zadovoljen uvjet B za izbacivanje uljeza");
                return true;
            } catch (ParseException e) {
                bc.debug("partija depozita ne sadrži format yyyyMMdd!");
            } catch (IllegalArgumentException e) {
                bc.debug("partija depozita ne zadovoljava uvjeta za izbacivanje kandidata!");
            }
        }
        
        //sluèaj C kada je cashdep_dwh.acc_num = 013
        if(partijaPlasmana!=null && partijaPlasmana.equals("013")){
            bc.debug("Zadovoljen uvjet C za izbacivanje uljeza");
            return true;
        }
        
        return false;
    }
    
    
    public static void main(String[] args) {

        BatchParameters bp = new BatchParameters(new BigDecimal(1716890003));// id s razvoja
        bp.setArgs(args);
        new BO100().run(bp);        
    }
}
