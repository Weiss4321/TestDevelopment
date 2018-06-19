package hr.vestigo.modules.collateral.batch.bo12;



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



/**
 * Batch za uparivanje plasmana s podacima iz DWH modula
 * 
 * @author hraamh
 *
 */
public class BO120 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo12/BO120.java,v 1.4 2007/07/19 12:55:11 hraamh Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	private boolean debug=false;
	
	private String proc_type="F";
	
	BO121 bo121= null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	
	private Date proc_date=null;
	
	private Timestamp user_lock=null;
	
	
	public BO120(){
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal("1727286003");	
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());	
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	public String executeBatch(BatchContext contex) throws Exception {
		this.bc=contex;
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

		bc.debug("******************** Bo120.executeBatch() poceo *********************");
		
		if (bc.getArgs().length<2){
			bc.debug("Neispravan broj argumenata!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (bc.getArg(0).equals("RB")==false){
			bc.debug("Bank sign mora biti 'RB'!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (bc.getArg(1).equals("0")==true){
			this.debug=true;
		}
		

		long startTime=System.currentTimeMillis();
		this.bo121= new BO121(bc,org_uni_id,use_id,this.debug);
		long recordCount=0;
		BigDecimal proc_id=this.bo121.selectColProc(proc_type);
		bc.beginTransaction();
		if(proc_id==null){
			 this.putColProc(this.proc_type,"0",new BigDecimal(0),this.user_lock,true);
		}else{
			bo121.setColProId(proc_id);
			this.putColProc(this.proc_type,"0",new BigDecimal(0),this.user_lock,false);
		}
		bc.commitTransaction();

        try{
        	//bo121.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        IteratorLoanBen iter= null;
        IteratorLoanBenCusAccId iter2=null;
        
        try{
        	//obrada plasmana koji imaju broj partije prazan
        	iter= bo121.selectDataWithEmptyAccNo();
        	if(iter!=null){		
        		if (debug) bc.debug("\nPrazno fiks");
				while(iter.next()){
					Map data=null;
					BigDecimal la_acc_id=null;
					String acc_no=null;
					String customerType=bo121.getHardCode(iter.cus_typ_id());
					if (debug) bc.debug("\nFiksam "+iter.loan_ben_id()+" ->"+customerType);
					if((customerType!=null)&&(customerType.equalsIgnoreCase("FO"))){
						if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
						}else{
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
							bc.commitTransaction();
						}
					}else if((customerType!=null)&&(customerType.equalsIgnoreCase("PO"))){
						if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
						}else if(!(iter.request_no().startsWith("FR"))&&((data=bo121.selectFromCEWithContractNo(iter.request_no()))!=null)){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko contract: "+iter.request_no());
						}else{
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
							bc.commitTransaction();
						}
					}
					
				}
				iter.close();
        	}
        	
        	/*
        	iter= null;
        	//obrada plasmana koji se ne mogu upariti preko broja partije
        	iter= bo121.selectDataWithWrongAccNo();
        	if(iter!=null){	
        		bc.debug("\nKrivo fiks");
				while(iter.next()){
					Map data=null;
					BigDecimal la_acc_id=null;
					String acc_no=null;
					String customerType=bo121.getHardCode(iter.cus_typ_id());
					if (debug) bc.debug("\nFiksam "+iter.loan_ben_id()+" s acc_no:"+iter.acc_no()+" ->"+customerType);
					if((customerType!=null)&&(customerType.equalsIgnoreCase("FO"))){
						
						if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
						}else{
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
							bc.commitTransaction();
						}
					}else if((customerType!=null)&&(customerType.equalsIgnoreCase("PO"))){
						if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
						}else if(!(iter.request_no().startsWith("FR"))&&((data=bo121.selectFromCEWithContractNo(iter.request_no()))!=null)){
							la_acc_id=(BigDecimal) data.get("cus_acc_id");
							acc_no=(String) data.get("cus_acc_no");
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
							bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
							bc.commitTransaction();
							recordCount++;
							bc.debug("\nFiksan "+iter.loan_ben_id()+"preko contract_no: "+iter.request_no());
						}else{
							bc.beginTransaction();
							bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
							bc.commitTransaction();
						}
					}			
				}
				iter.close();
        	}*/
        	
        	

        	iter= null;
        	//obrada plasmana koji se ne mogu upariti preko broja partije
        	iter= bo121.selectDataWithAccNo();
        	if(iter!=null){	
        		bc.debug("\nKrivo fiks");
				while(iter.next()){
					if(bo121.selectCEIdWithAccNo(iter.acc_no())==null){
						Map data=null;
						BigDecimal la_acc_id=null;
						String acc_no=null;
						String customerType=bo121.getHardCode(iter.cus_typ_id());
						if (debug) bc.debug("\nFiksam "+iter.loan_ben_id()+" s acc_no:"+iter.acc_no()+" ->"+customerType);
						if((customerType!=null)&&(customerType.equalsIgnoreCase("FO"))){
							
							if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
								la_acc_id=(BigDecimal) data.get("cus_acc_id");
								acc_no=(String) data.get("cus_acc_no");
								bc.beginTransaction();
								bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
								bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
								bc.commitTransaction();
								recordCount++;
								bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
							}else{
								bc.beginTransaction();
								bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
								bc.commitTransaction();
							}
						}else if((customerType!=null)&&(customerType.equalsIgnoreCase("PO"))){
							if((data=bo121.selectFromCEWithRequestNo(iter.request_no()))!=null){
								la_acc_id=(BigDecimal) data.get("cus_acc_id");
								acc_no=(String) data.get("cus_acc_no");
								bc.beginTransaction();
								bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
								bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
								bc.commitTransaction();
								recordCount++;
								bc.debug("\nFiksan "+iter.loan_ben_id()+"preko request_no: "+iter.request_no());
							}else if(!(iter.request_no().startsWith("FR"))&&((data=bo121.selectFromCEWithContractNo(iter.request_no()))!=null)){
								la_acc_id=(BigDecimal) data.get("cus_acc_id");
								acc_no=(String) data.get("cus_acc_no");
								bc.beginTransaction();
								bo121.insertInDataDwhItem(bo121.getColProId(),la_acc_id,acc_no,"0",iter.loan_ben_id());
								bo121.updateLoanBeneficiary(iter.loan_ben_id(),la_acc_id,acc_no);
								bc.commitTransaction();
								recordCount++;
								bc.debug("\nFiksan "+iter.loan_ben_id()+"preko contract_no: "+iter.request_no());
							}else{
								bc.beginTransaction();
								bo121.insertInDataDwhItem(bo121.getColProId(),null,null,"1",iter.loan_ben_id());
								bc.commitTransaction();
							}
						}			
					}
				}
				iter.close();
        	}
        	
        	//obrada slogova iz LOAN_BENEFICIARY kojima je broj partije ispravan ali im je id partije null
        	iter2=bo121.selectDataWithoutCusAccId();
        	if(iter2!=null){	
	        	bc.debug("\n cus_acc_id fiks");
				while(iter2.next()){
					bc.beginTransaction();
					bo121.insertInDataDwhItem(bo121.getColProId(),iter2.cus_acc_id(),iter2.acc_no(),"0",iter2.loan_ben_id());
					bo121.updateLoanBeneficiary(iter2.loan_ben_id(),iter2.cus_acc_id(),iter2.acc_no());
					bc.commitTransaction();
					recordCount++;
					bc.debug("\nFiksan "+iter2.loan_ben_id());
				}
				iter2.close();
        	}
        	
        	this.putColProc(this.proc_type,"1",new BigDecimal(recordCount),this.user_lock,false);
        	bc.debug("\nBO120-> kraj obrade zapisan");
            long endTime=System.currentTimeMillis();
             
     		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
     		bc.debug("Broj osvjezavanja LOAN_BENEFICIARY slogova: "+recordCount);	
     		bc.debug("******************** Bo120.executeBatch() zavrsio *********************");
     		
        }catch (Exception e) {
        	bc.rollbackTransaction();
            e.printStackTrace();
            toReturn=RemoteConstants.RET_CODE_ERROR;
        }finally{
        	try{
        		if(iter!=null){
        			iter.close();
        		}
        		if(iter2!=null){
        			iter2.close();
        		}
        	}catch(SQLException sqle){
        		sqle.printStackTrace();
        		return RemoteConstants.RET_CODE_ERROR;
        	}
        }
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;	
		return toReturn;
	}
	
	public void putColProc(String proc_type,String proc_status,BigDecimal col_number, Timestamp user_lock, boolean insert) throws Exception{
		Map map= new HashMap();
		map.put("proc_date",proc_date);    
		map.put("value_date",proc_date); 
		map.put("proc_type",proc_type);    
		map.put("proc_way","A");                
		map.put("proc_status",proc_status);          
		map.put("col_number",col_number);            
		//map.put("proc_ts",proc_ts);                  
		map.put("org_uni_id",org_uni_id);            
		map.put("use_id", use_id); 
		map.put("user_lock", user_lock);
		if(insert){
			this.user_lock=this.bo121.insertColProc(map);
		}else{
			this.user_lock=this.bo121.updateColProc(map);
		}
		
	}
	
	
	
	public static void main(String[] args) {
	 	
        BatchParameters bp = new BatchParameters(new BigDecimal(1727284003));// id s razvoja
        bp.setArgs(args);
        new BO120().run(bp);        
    }
	
	

}
