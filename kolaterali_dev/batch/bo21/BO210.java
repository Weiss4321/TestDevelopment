/*
 * Created on 2007.11.23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo21;

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
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.rba.util.DateUtils;

/**
 * 
 * Arhiviranje kolateral obrada
 * 
 * @author hraamh
 *
 */
public class BO210 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo21/BO210.java,v 1.3 2008/04/15 13:18:44 hraamh Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	private Date proc_date=null;
	private Timestamp user_lock=null;
	
	private BO211 bo211=null;

	/**
	 * 
	 */
	public BO210() {
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal("2168257003");	
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	public String executeBatch(BatchContext contex) throws Exception {
		this.bc=contex;
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;
		boolean withDelete=false;

		bc.debug("******************** BO210.executeBatch() poceo *********************");
		//1. argument = bank_sign
		String bankSign=(bc.getArg(0));
		//2. argument = tip obrade proc_type
		String archive_type=bc.getArg(1);
		//3. argument granicni datum za arhiviranje
		Date archive_date=DateUtils.parseDate(bc.getArg(2));
		//4. argument = 0-delete col_proc_gar; 1- bez deleta
		if(bc.getArg(3).equalsIgnoreCase("0")){
			withDelete=true;
		}
		
		long startTime=System.currentTimeMillis();
		this.bo211= new BO211(bc,org_uni_id,use_id,bankSign);
		
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	this.putColProc("X",archive_type,archive_date,new BigDecimal(0),this.user_lock,true);
        	bo211.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        
        long counter=0;
        long proc_counter=0;
        int commitStep=1000;
        int step=0;
        boolean uncommited=false; 
        try{
        	if((archive_type.equalsIgnoreCase("O"))||(archive_type.equalsIgnoreCase("D"))){
        		//oslobadanje kolaterala i garantni depoziti
        		ColProcIterator colProcIter=bo211.selectColProc(archive_date, archive_type);
        		if(colProcIter!=null){
        			while(colProcIter.next()){
        				bc.debug("\n\n ************* \n\n " +
        						"Obradujem obradu id: "+colProcIter.col_pro_id()+
        						"\n type: "+colProcIter.proc_type()+
        						"\n value_date: "+colProcIter.value_date()+
        						"\n\n **********");
        				bc.beginTransaction();
        				if(withDelete){
        					//brisanje podataka iz in_data_dwh_item
        					bo211.deleteFromInDataDwhItem(colProcIter.col_pro_id());
        				}
        				bo211.insertColProcA(colProcIter);
        				bo211.deleteFromColProc(colProcIter.col_pro_id());
        				bc.commitTransaction();
        				counter++;
        				bc.debug("\n\n ************* \n\n " +
        						"Zavrsio obradu id: "+colProcIter.col_pro_id()+
        						"\n type: "+colProcIter.proc_type()+
        						"\n value_date: "+colProcIter.value_date()+
        						"\n\n **********");
        			}
        		}       		
        	}else if((archive_type.equalsIgnoreCase("C"))||(archive_type.equalsIgnoreCase("E"))||
        			(archive_type.equalsIgnoreCase("H"))||(archive_type.equalsIgnoreCase("I"))||
        			(archive_type.equalsIgnoreCase("J"))||(archive_type.equalsIgnoreCase("K"))||
        			(archive_type.equalsIgnoreCase("P"))||(archive_type.equalsIgnoreCase("N"))){
        		//izracuni pokrivenosti
        		ColProcIterator colProcIter=bo211.selectColProc(archive_date, archive_type);
        		if(colProcIter!=null){
        			while(colProcIter.next()){
        				bc.debug("\n\n ************* \n\n " +
        						"Obradujem obradu id: "+colProcIter.col_pro_id()+
        						"\n type: "+colProcIter.proc_type()+
        						"\n value_date: "+colProcIter.value_date()+
        						"\n\n **********");
        				step=0;
    					uncommited=false;
    					proc_counter=0;
        				CusaccExpCollIterator cusaccIter=bo211.selectCusaccExpColl(colProcIter.col_pro_id());
        				while(cusaccIter.next()){
        					if(step==0){
        						bc.beginTransaction();
        						uncommited=true;
        					}
        					bo211.insertCusaccExpCollA(cusaccIter);
        					bo211.deleteFromCusaccExpColl(cusaccIter.cus_acc_exp_col_id());
        					step++;
        					counter++;
        					proc_counter++;
        					if(step>=commitStep){
        						step=0;
        						bc.commitTransaction();
        						uncommited=false;
        					}
        				}
        				if(uncommited){
        					bc.commitTransaction();				
        				}	
        				
        				bc.beginTransaction();
        				if(withDelete){
        					//brisanje podataka iz col_proc_gar tablice	
        					bo211.deleteFromColProcGar(colProcIter.col_pro_id());
        				}
        				bo211.insertColProcA(colProcIter);
        				bo211.deleteFromColProc(colProcIter.col_pro_id());
        				bc.commitTransaction();
        				
        				//brisanje loan_beneficiary_d
        				bc.beginTransaction();
        				bo211.deleteFromLoanBeneficiaryD(colProcIter.value_date());
        				bc.commitTransaction();
        				//brisanje frame_agreement_d
        				bc.beginTransaction();
        				bo211.deleteFromFrameAgreementD(colProcIter.value_date());
        				bc.commitTransaction();
        				//brisanje col_hf_prior_d
        				bc.beginTransaction();
        				bo211.deleteFromCollHfPriorD(colProcIter.value_date());
        				bc.commitTransaction();
        				//brisanje coll_head_d
        				bc.beginTransaction();
        				bo211.deleteFromCollHeadD(colProcIter.value_date());
        				bc.commitTransaction();
        				
        				bc.debug("\n\n ************* \n\n " +
        						"Zavrsio obradu id: "+colProcIter.col_pro_id()+
        						"\n type: "+colProcIter.proc_type()+
        						"\n value_date: "+colProcIter.value_date()+
        						"\n broj prenesenih slogova iz cusacc_exp_coll: "+proc_counter+
        						"\n\n **********");
        			}
        		}	
        	}
        	bc.beginTransaction();
        	this.putColProc("X",archive_type, archive_date,new BigDecimal(counter),this.user_lock,false);
        	bc.commitTransaction();
        	bo211.closeExtraConnection();
		}catch (Exception e) {
	        e.printStackTrace();
	        toReturn=RemoteConstants.RET_CODE_ERROR;
	    }finally{
	    	bo211.closeExtraConnection();
	    }
        bc.debug("\nBO210-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO210.executeBatch() zavrsio s RC="+toReturn+" *********************");
		
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return toReturn;
	}
	
	public void putColProc(String proc_type,String proc_status, Date archive_date,BigDecimal col_number, Timestamp user_lock, boolean insert) throws Exception{
		Map map= new HashMap();
		map.put("proc_date",proc_date);    
		map.put("value_date",archive_date); 
		map.put("proc_type",proc_type);    
		map.put("proc_way","A");                
		map.put("proc_status",proc_status);          
		map.put("col_number",col_number);                             
		map.put("org_uni_id",org_uni_id);            
		map.put("use_id", use_id); 
		map.put("user_lock", user_lock);
		if(insert){
			this.user_lock=bo211.insertColProc(map);
		}else{
			this.user_lock=bo211.updateColProc(map);
		}
		
	}

	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("2168239003"));// id s razvoja
        bp.setArgs(args);
        new BO210().run(bp);
	}
}
