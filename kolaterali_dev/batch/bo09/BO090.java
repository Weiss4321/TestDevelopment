/*
 * Created on 2007.04.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo09;



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
import hr.vestigo.modules.rba.util.StringUtils;


/**
 * Batch za preuzimanje podataka o vozilima iz PKR-a
 * 
 * @author hraamh
 *
 */
public class BO090 extends Batch {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo09/BO090.java,v 1.8 2007/07/02 09:55:09 hraamh Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	private boolean debug=false;
	private boolean erase=false;
	
	
	BO091 bo091= null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	//private BigDecimal batch_id=null;
	
	private Date proc_date=null;
	
	private Timestamp user_lock=null;
	
	
	public BO090(){
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal("1716834003");
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());	
	}
	
	
	private void hardcoding(){
		
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	public String executeBatch(BatchContext contex) throws Exception {
		this.bc=contex;
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

		bc.debug("******************** BO090.executeBatch() poceo *********************");
		
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
		if(bc.getArgs().length==3){
			if (bc.getArg(2).equals("E")==true){
				this.erase=true;
				bc.debug("Brisanje starih podataka ukljuceno!");
			}
		}	

		long startTime=System.currentTimeMillis();
		this.bo091= new BO091(bc,org_uni_id,use_id);
		//long recordCount=0;
		
		boolean newProces=false;
		
		if (debug) bc.debug("\nBO020->provjera da li je obrada vec pokrenuta");
		BigDecimal proc_id=this.bo091.selectColProc("V","1");
		if(proc_id!=null){
			bc.debug("Obrada za dani datum je vec uspjesno odradena!");
			return RemoteConstants.RET_CODE_ERROR;
		}else{
			//ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
			proc_id=this.bo091.selectColProc("V","0");
			//SAMO ZA DEBUGIRANJE I TESTIRANJE!!!
			/*if (erase){
				bc.beginTransaction();
				bo091.clearOldData(proc_id);
				bc.commitTransaction();
			}*/
			if(proc_id!=null){		
				bc.debug("Obrada za dani datum vec postoji (id= "+proc_id+") ali nije zavrsena. Nastavljam...!");
				this.bo091.setColProId(proc_id);
			}else{
				
				//nema nikakve obrade= potpuno nova obrada
				bc.debug("Potpuno nova obrada!");
				proc_id=this.bo091.getColProId();
				newProces=true;
			}
		}
		 
		
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	if(newProces){
        		this.putColProc("V","0",new BigDecimal(0),this.user_lock,true);
        	}
        	bc.debug("\nBO090->insertam dogadaj");
        	bo091.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        	bc.debug("\nBO090->dogadaj insertan");
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
       
        bc.commitTransaction();
        
        IteratorVehicleDWH iter=null;
        try{
        	iter= bo091.selectVehicleDWH(proc_id);
        	if(iter!=null){			
				while(iter.next()){
					String inputStatus="0";
					//InputData inputData= new InputData(iter);
					InputData inputData=bo091.getInputData(iter);
					int check=inputData.check();
					if(check==1) {
						inputStatus="7";
					}else if(check==2) {
						inputStatus="8";
					}
					
					Map cusaccExposureMap=bo091.selectFromCusaccExposure(inputData.acc_num); 
					//ako je smece bez broja sasije
					if((inputData.veh_vin_num==null)||(inputData.veh_vin_num.equals(""))||(inputData.veh_vin_num.length()>18)){
						if(debug) bc.debug("ignoriram slog: "+inputData);
						inputStatus="6";
						bc.beginTransaction();
						bo091.insertInDataDwhItem(bo091.getColProId(),null,inputData.acc_num,inputStatus,null);
						bc.debug("ignoriram slog: "+inputData.acc_num);
						bc.commitTransaction();
						continue;
					}else if(bo091.selectVehicleId(inputData.veh_vin_num)!=null){
						if(debug) bc.debug("ignoriram slog: "+inputData);
						inputStatus="A";
						bc.beginTransaction();
						bo091.insertInDataDwhItem(bo091.getColProId(),null,inputData.acc_num,inputStatus,null);
						bc.debug("ignoriram slog: "+inputData.acc_num);
						bc.commitTransaction();
						continue;
					}else if (cusaccExposureMap==null){
						if(debug) bc.debug("ignoriram slog: "+inputData);
						inputStatus="1";
						bc.beginTransaction();
						bo091.insertInDataDwhItem(bo091.getColProId(),null,inputData.acc_num,inputStatus,null);
						bc.debug("ignoriram slog: "+inputData.acc_num);
						bc.commitTransaction();
						continue;
					}
					Date approvalDate=(Date)cusaccExposureMap.get("date");
					BigDecimal accountCurrency_id=(BigDecimal)cusaccExposureMap.get("cur_id");
					BigDecimal amount=(BigDecimal)cusaccExposureMap.get("amount");
					String request_no=(String)cusaccExposureMap.get("request_no");
					BigDecimal cus_acc_id=(BigDecimal) cusaccExposureMap.get("cus_acc_id"); 
					
					if(debug) bc.debug("obradujem slog: "+inputData);
					
					
					
					//ako su neki potrebni ulazni atributi prazni zamjenjuje ih s zamjenskim
					if(inputData.lic_date==null){
						//ako nema datuma licence uzima se datum odobrenja partije
						inputStatus="5";
						inputData.lic_date=approvalDate;
					}
					if((inputData.veh_owner==null)||(inputData.veh_owner.equalsIgnoreCase(""))||(inputData.veh_owner.equalsIgnoreCase("0"))){
						//ako nema vlasnika vozila uzima se vlasnik plasmana
						inputStatus="3";
						inputData.veh_owner=inputData.loan_owner;
					}
					if((inputData.veh_amount==null)||(inputData.veh_amount.compareTo(new BigDecimal(0))==0)){
						//ako nema iznosa vozila uzima se iznos plasmana
						inputStatus="9";
						inputData.veh_amount=amount;
					}
					
					
					BigDecimal veh_cur_id=null;
					if(inputData.veh_cur==null){
						//ako nema valute vozila uzima se valuta partije
						inputStatus="2";
						veh_cur_id=accountCurrency_id;
					}else{
						if(StringUtils.isNumeric(inputData.veh_cur)){
							veh_cur_id=bo091.selectCurrencyIdWithCodeNum(inputData.veh_cur);
						}else{
							veh_cur_id=bo091.selectCurrencyIdWithCodeChar(inputData.veh_cur);
						}
					}
					BigDecimal loan_cus_id=bo091.selectCustomerId(inputData.loan_owner);
					BigDecimal coll_cus_id=loan_cus_id;
					if(!inputData.loan_owner.equals(inputData.veh_owner)){
						//ako nisu isti onda se ide po vlasnika
						coll_cus_id=bo091.selectCustomerId(inputData.veh_owner);
					}
					
					
					//POCETAK TRANSAKCIJE
					bc.beginTransaction();
					//BigDecimal hf_amount= bo091.selectContractAmount(inputData.acc_num);
					BigDecimal col_hea_id=bo091.insertCollHead(inputData,veh_cur_id,loan_cus_id,coll_cus_id,amount);
					if(col_hea_id==null){
						bc.debug("nije upisan slog u tablicu COLL_HEAD za acc_no="+inputData.acc_num);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n col_hea_id="+col_hea_id);
					
					BigDecimal col_veh_id=bo091.insertCollVechicle(inputData,col_hea_id);
					if(col_veh_id==null){
						bc.debug("nije upisan slog u tablicu COLL_VEHICLE za acc_no="+inputData.acc_num +" i col_hea_id="+col_hea_id);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n col_veh_id="+col_veh_id);
					String coll_cus_code= bo091.selectCustomerCode(inputData.veh_owner);
					BigDecimal col_own_id=bo091.insertCollOwner(inputData,col_hea_id,coll_cus_id,coll_cus_code);
					if(col_own_id==null){
						bc.debug("nije upisan slog u tablicu COLL_OWNER za acc_no="+inputData.acc_num +" i col_hea_id="+col_hea_id);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n col_own_id="+col_own_id);
					BigDecimal coll_hf_prior_id=bo091.insertCollHfPrior(inputData,col_hea_id,col_veh_id,veh_cur_id,approvalDate,amount);
					if(coll_hf_prior_id==null){
						bc.debug("nije upisan slog u tablicu COLL_HF_PRIOR za acc_no="+inputData.acc_num +" i col_hea_id="+col_hea_id);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n coll_hf_prior_id="+coll_hf_prior_id);
					BigDecimal loan_ben_id=bo091.insertLoanBeneficiary(inputData,coll_hf_prior_id,loan_cus_id,cus_acc_id,request_no);
					if(loan_ben_id==null){
						bc.debug("nije upisan slog u tablicu LOAN_BENEFICIARY za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id+" i coll_hf_prior_id="+coll_hf_prior_id);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n loan_ben_id="+loan_ben_id);
					BigDecimal col_lis_q_id=bo091.insertCollListQ(inputData,col_hea_id);
					if(loan_ben_id==null){
						bc.debug("nije upisan slog u tablicu COLL_LIST_Q za acc_no="+inputData.acc_num +" , col_hea_id="+col_hea_id);
						return RemoteConstants.RET_CODE_ERROR;
					}
					if(debug) bc.debug("\n col_lis_q_id="+col_lis_q_id);
					
					bo091.insertInDataDwhItem( bo091.getColProId(),null,inputData.acc_num,inputStatus,col_hea_id);
					
					// SAMO KOD DEBUGIRANJA I TESTIRANJA!!!
					//bo091.insertCollBatchTemp( bo091.getColProId(),col_hea_id,col_veh_id,col_own_id,coll_hf_prior_id,loan_ben_id,col_lis_q_id);
					bc.debug("commiting "+inputData.acc_num);
					bc.commitTransaction();
					//recordCount++;
					//KRAJ TRANSAKCIJE
				}
	        }
        	BigDecimal recordCount=bo091.getRecordCount();
        	this.putColProc("V","1",recordCount,this.user_lock,false);
        	iter.close();    	
        }catch (Exception e) {
        	bc.rollbackTransaction();
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
        
        bc.debug("\nBO090-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO090.executeBatch() zavrsio *********************");
		
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
			this.user_lock=this.bo091.insertColProc(map);
		}else{
			this.user_lock=this.bo091.updateColProc(map);
		}
		
	}
	
	
	public static void main(String[] args) {
		
        //BatchParameters bp = new BatchParameters(new BigDecimal(1663357003));// id s razvoja
        BatchParameters bp = new BatchParameters(new BigDecimal(1716889003));// id s razvoja
        bp.setArgs(args);
        new BO090().run(bp);        
    }
	
	

}
