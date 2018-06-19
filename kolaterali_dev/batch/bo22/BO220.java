/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo22;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo22.InputData;
import hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0;

/**
 * Batch za ucitavanje mjenica i zaduznica
 * 
 * @author hraamh
 *
 */
public class BO220 extends AbstractCollateralFileTransferBatch {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo22/BO220.java,v 1.15 2013/11/06 15:02:29 hraaks Exp $";
	  
    /*
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	private Date proc_date=null;	
	private Timestamp user_lock=null;
	private BigDecimal col_cat_id=null;
	//private BigDecimal col_type_id=null;
	private String bank_sign=null;
	private String proc_type=null;
	private BigDecimal eve_id=null;
	*/
	
    private IteratorB022DWH iter= null;
	private String activeStatus=null;
	private BO221 bo221=null;
    private BigDecimal col_cat_id=null;
    private String bank_sign=null;
    private boolean billProcess=true;
    private boolean corporateLoanstockProcess=false;
	
	public BO220(){
		super();
		org_uni_id=new BigDecimal(53253);
		eve_typ_id= new BigDecimal("2206122003");
		use_id=new BigDecimal(1);
		proc_date= new Date(System.currentTimeMillis());	
		activeStatus="A";		
	}
    
    @Override
    protected void init() throws Exception {
        bank_sign=bc.getBankSign();
        proc_date=new Date(System.currentTimeMillis());
        value_date=proc_date;
        activeStatus="A";
        bo221= new BO221(getContext(),use_id,bank_sign,org_uni_id,col_cat_id);
    }
    
    @Override
    protected int getArgs(BatchContext bc) {
        if (bc.getArg(1).equals("M")==true){
            //mjenice
            this.proc_type="0";
            col_cat_id = new BigDecimal(617223);
            //col_type_id = new BigDecimal(41777);
            billProcess=true;
        }else if (bc.getArg(1).equals("Z")==true){
            //zaduznice retail
            this.proc_type="1";
            col_cat_id = new BigDecimal(625223);
            //col_type_id = new BigDecimal(44777);
            billProcess=false;
        } else {
            // zaduznice corporate
            this.proc_type="ZCO";
            col_cat_id = new BigDecimal(625223);
            billProcess=false;
            corporateLoanstockProcess=true;
        }
        return 0;
    }
    
    @Override
    protected String runBatch() throws Exception {
        Date today= new Date(System.currentTimeMillis());
        /*
         * Zadnje dvije znamenke godine
         */
        String shortYear = (new java.sql.Date(System.currentTimeMillis())).toString().substring(2,4);       
        /*
         * *****************************************************************************************
         * DOHVAT ITERATOR I OBRADA DWH PODATAKA
         */
        

        if (billProcess){
            iter=bo221.selectBillDWH();
        }else{
            iter=bo221.selectLoanstockDWH();
        }
        
        if(iter!=null){         
            while(iter.next()){
                BigDecimal cus_acc_id=null;
                BigDecimal cus_id=null;
                String inputStatus="0";
                InputData dwhData=getDwhData(iter);
                bc.debug(dwhData.toString());
                int check=dwhData.check();
                if(check==1) {
                    inputStatus="1";
                }else if(check==2) {
                    inputStatus="2";
                }
                
                //26.11.2008.
                if(bo221.checkLoanStockExistance(dwhData.acc_no, dwhData.type,col_cat_id)){
                    getContext().debug("Vec postojeca mjenica/zaduznica. Preskacem...");
                    inputStatus="4";
                    bc.beginTransaction();
                    commonMethods.insertInDataDwhItem(bo221.getColProId(), null, dwhData.acc_no, inputStatus, null);
                    bc.commitTransaction();
                    continue;
                }
                
                
                //provjera da li postoji id za danog vlasnika
                cus_id=commonMethods.selectCustomerId(dwhData.owner, bank_sign);                
                if(cus_id==null){
                    check=1;
                    inputStatus="1";
                }
                //provjera da li postoji id racuna za dani broj racuna ili broj zahtjeva
                if(dwhData.acc_no!=null){
                    if(dwhData.acc_no.startsWith("FR")){
                        cus_acc_id=bo221.getCusAccIdByRequestNo(dwhData.acc_no);
                    }else{
                        cus_acc_id=commonMethods.selectCusAccId(dwhData.acc_no);
                    }
                }
                if(cus_acc_id==null){
                    check=2;
                    inputStatus="2";
                }
                
                if((billProcess)&&(dwhData.type.equalsIgnoreCase(""))){
                    check=3;
                    inputStatus="3";
                }
                                
                if(check!=0){ 
                    //ako provjere nisu prosle samo zapisuje u in_data_dwh_item
                    bc.beginTransaction();
                    commonMethods.insertInDataDwhItem(bo221.getColProId(), null, dwhData.acc_no, inputStatus, null);
                    bc.commitTransaction();
                }else{          
                    BigDecimal col_type_id=null;
                    if(billProcess){
                        //mjenice
                        if((dwhData.type.equals("2"))||(dwhData.type.equals("15"))){
                            col_type_id = new BigDecimal(41777);
                        }else if(dwhData.type.equals("69")){
                            col_type_id = new BigDecimal(42777);
                        }else if(dwhData.type.equals("99")){
                            col_type_id = new BigDecimal(43777);
                        }
                    }else{
                        //zaduznice - promjena vrste zaduznice - 05.01.2011
//                        col_type_id = new BigDecimal(44777);
                        if (corporateLoanstockProcess) 
                            col_type_id = new BigDecimal(44777); // corporate zaduznice
                        else 
                            col_type_id = new BigDecimal(69777); // retail zaduznice
                    }
                    
                    String prefix=commonMethods.selectCollTypeCodeById(col_type_id);
                    if(prefix==null){
                        throw new Exception("col_typ_code is null for "+col_type_id);
                    }else{
                        prefix=prefix.trim()+shortYear;
                    }
                    bc.debug("COL_NUM prefiks: "+prefix);
                    String col_num=null;
                    if (billProcess) {
                        col_num=commonMethods.getCollNum("CLT_BE", today, prefix, null);
                    }else{
                        col_num=commonMethods.getCollNum("CLT_LG", today, prefix, null);
                    }
                    String request_no=commonMethods.selectRequestNoFromCusaccExposure(cus_acc_id);
                    String collateral_status=null;
                    BigDecimal col_lis_typ_id=null;
                    
                    String cus_acc_status=bo221.getCusAccStatus(cus_acc_id);
                    
                    if(cus_acc_status.equalsIgnoreCase("A")){
                        collateral_status="3";
                        col_lis_typ_id=new BigDecimal(700223);
                    }else{
                        collateral_status="N";
                        col_lis_typ_id=new BigDecimal(709223);
                    }
                    
                    bc.beginTransaction();
                    Map data=new HashMap();
                    data.put("col_type_id", col_type_id);
                    data.put("eve_id", eve_id);
                    data.put("cus_id", cus_id);
                    data.put("col_num", col_num);
                    data.put("collateral_status", collateral_status);
                    data.put("number", dwhData.num);
                    /*
                     * Insert u COLL_HEAD
                     */
                    BigDecimal col_hea_id=bo221.insertCollHead(data);
                    BigDecimal temp_id=null;
                    if (billProcess){
                        temp_id=bo221.insertCollBillExch(col_hea_id);
                    }else{
                        temp_id=bo221.insertCollLoanstock(col_hea_id);
                    }
                    
                    
                    data.clear();
                    data.put("col_hea_id", col_hea_id);
                    data.put("cus_acc_id", cus_acc_id);
                    data.put("cus_id", cus_id);
                    if(!dwhData.acc_no.startsWith("FR")){
                        data.put("acc_no", dwhData.acc_no);
                    }
                    data.put("request_no", request_no);
                    data.put("bill_owner", dwhData.owner);
                    data.put("inspol_ind", dwhData.type);
                    data.put("ip_cus_id", dwhData.cus_id_insur);
                    
                    BigDecimal loan_ben_id=bo221.insertLoanBeneficiary(data, col_cat_id);
                    
                    data.clear();
                    data.put("col_hea_id", col_hea_id);
                    data.put("col_lis_typ_id", col_lis_typ_id);
                    data.put("action_type", "PREUZETO IZ OMEGE");
                    BigDecimal col_lis_q_id=bo221.insertCollListQ(data);

                 // dodati postavljanje svih prihvatljivosti                             
                    YOYG0 yoyg0 = new YOYG0(bc, col_hea_id);
                    yoyg0.azurirajPrihvatljivosti();                   
                    
                    commonMethods.insertInDataDwhItem(bo221.getColProId(), null, dwhData.acc_no, inputStatus, col_hea_id);                  
                    bc.commitTransaction();
                    incrementCounter(1);
                }
                
                 
            }
        }
        bc.beginTransaction();
        bo221.emptyDwhTable(billProcess);
        bc.commitTransaction();
        
        return toReturn;
    }
	
	public InputData getDwhData(IteratorB022DWH inputRow) throws SQLException{
		InputData result=new InputData();		
		result.owner=inputRow.owner();
		result.acc_no=inputRow.acc_no();
		result.num=inputRow.num();
		result.status=inputRow.status();
		// samo ako je sifra 90 ili 92 onda postoji id osiguravatelja
		result.type=inputRow.type()!=null?inputRow.type().trim():"";;
		result.cus_id_insur=(result.type.equalsIgnoreCase("90") || result.type.equalsIgnoreCase("92"))?
		        bo221.getCusId(inputRow.register_no()):null;
		return result;
	}	
	
	public Map getColProcMap(BigDecimal col_pro_id, String proc_type,String proc_status,BigDecimal col_number, Timestamp user_lock) throws Exception{
		Map map= new HashMap();
		map.put("col_pro_id",col_pro_id); 
		map.put("proc_date",proc_date);    
		map.put("value_date",proc_date); 
		map.put("proc_type",proc_type);    
		map.put("proc_way","A");                
		map.put("proc_status",proc_status);          
		map.put("col_number",col_number);                             
		map.put("org_uni_id",org_uni_id);            
		map.put("use_id", use_id); 
		map.put("user_lock", user_lock);
		return map;		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("2206140003"));// id s razvoja
        bp.setArgs(args);
        new BO220().run(bp);
	}

    @Override
    protected void closeExtraConnections() throws Exception {
        bo221.closeExtraConnection();
        if(iter!=null){
            iter.close();
        }
    }



    @Override
    protected String getBatchName() {
        return "bo22";
    }

    @Override
    protected BigDecimal getColProId() throws Exception {
        return bo221.getColProId();
    }

    @Override
    protected String getTargetModule() {
        return null;
    }



    @Override
    protected boolean isAlwaysFreshStart() {
        return false;
    }

    @Override
    protected boolean isFileTransfer() {
        return false;
    }

    @Override
    protected boolean isInFileManipulation() {
        return false;
    }

    @Override
    protected boolean isMQNotify() {
        return false;
    }

    @Override
    protected boolean isOutFileManipulation() {
        return false;
    }

    @Override
    protected void setColProId(BigDecimal col_pro_id) {
        this.bo221.setColProId(col_pro_id);
    }

    @Override
    protected void setEve_typ_id() {
        this.eve_typ_id= new BigDecimal("2206122003");
    }
    
    /**
     * omogucava vise pozivanje u jednom danu
     * 
     * @return false
     * 
     */
    protected boolean isOnlyOnceADay(){
        return false;
    }

}
