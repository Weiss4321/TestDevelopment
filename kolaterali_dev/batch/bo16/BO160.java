/*
 * Created on 2007.10.04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo16;

/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
  

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
import hr.vestigo.modules.collateral.common.yoy9.*;

/**
 * Batch za ispravak stanja za 99 .....
 * 
 * @author hramkr
 *
 */
public class BO160 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo16/BO160.java,v 1.2 2007/10/29 10:01:27 hramkr Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	private boolean debug=false;
	private String procType="Z";
	
	
	BO161 bo161= null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
//	private BigDecimal ratio=null;
	
	private Date proc_date=null;
	
	private Timestamp user_lock=null;
	
 

	
	public BO160(){
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal("1849809003");
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());	
//		this.ratio= new BigDecimal("1.00");

	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	public String executeBatch(BatchContext contex) throws Exception {
		this.bc=contex;
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

		bc.debug("******************** BO160.executeBatch() poceo *********************");
		
		if (bc.getArgs().length<2){
			bc.debug("Neispravan broj argumenata!");
			return RemoteConstants.RET_CODE_ERROR;
		} 
		if (bc.getArg(0).equals("RB")==false){
/*			bc.debug("Bank sign mora biti 'RB'!");
			return RemoteConstants.RET_CODE_ERROR;*/
		}
		if (bc.getArg(1).equals("0")==true){
			this.debug=true;
		}
 
		long startTime=System.currentTimeMillis();
		this.bo161= new BO161(bc,org_uni_id,use_id);
		//long recordCount=0;
		
		boolean newProces=false;
		
		BigDecimal cur_id = new BigDecimal("63999");
		
		if (debug) bc.debug("\nBO160->provjera da li je obrada vec pokrenuta");
		BigDecimal proc_id=this.bo161.selectColProc(this.procType,"1");
		if(proc_id!=null){
			bc.debug("Obrada je vec uspjesno odradena!");
			return RemoteConstants.RET_CODE_ERROR;
		}else{
			//ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
			proc_id=this.bo161.selectColProc(this.procType,"0");
			if(proc_id!=null){		
				bc.debug("Obrada vec postoji (id= "+proc_id+") ali nije zavrsena. Nastavljam...!");
				this.bo161.setColProId(proc_id);
			}else{
				
				//nema nikakve obrade= potpuno nova obrada
				bc.debug("Potpuno nova obrada!");
				proc_id=this.bo161.getColProId();
				newProces=true;
			}
		}
		   
		
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	if(newProces){
        		this.putColProc(this.procType,"0",new BigDecimal(0),true);
        	}
        	bc.debug("\nBO160->insertam dogadaj");
        	bo161.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        	bc.debug("\nBO160->dogadaj insertan");
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        bc.commitTransaction();
         
        IteratorSelectCollateral iter=null;
		YOY90 yoy90=null;
		
        try{ 
//        	iter= bo161.selectCusaccBalance();
        	iter= bo161.selectCollateral();
        	if(iter!=null){			 
				while(iter.next()){
					if(debug) bc.debug("\nfiksam zapis "+iter.col_hea_id() + " cus_acc: " + iter.acc_no());
					bc.beginTransaction();
					BigDecimal rba_hf_amount = bo161.selectRbaHfPrior(iter.col_hea_id());
					BigDecimal others_hf_amount = bo161.selectOthersHfPrior(iter.col_hea_id());
					bo161.updateCollateral(iter.col_hea_id(),iter.nominal_amount(),iter.mvp_ponder(),others_hf_amount);
// poziv knjizenja	
					
				    yoy90=new YOY90(bc);
				    try {
				        yoy90.CollPosting(iter.col_hea_id(),false);    
	                }catch (Exception e) {
	                	if(debug) bc.debug("Greska pri obradi kolaterala sa col_hea_id=" + iter.col_hea_id() + "\n Greska: " + e);
	                	if(debug) bc.debug("\n");
	                    bc.rollbackTransaction();
	                }					
					
					bo161.insertInDataDwhItem(bo161.getColProId(),iter.col_hea_id(),null,"0",null);
			
					bc.commitTransaction();
					if(debug) bc.debug("\nfiksano id: "+iter.col_hea_id()+" cus_acc: " + iter.acc_no()+" nominal_amount:"+iter.nominal_amount()+" others_amount:"+others_hf_amount);
				}
	        }      
        	BigDecimal recordCount=bo161.getRecordCount();
        	this.putColProc(this.procType,"1",recordCount,false);
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
        
        bc.debug("\nBO160-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO160.executeBatch() zavrsio *********************");
		
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return toReturn;
	}
	
	public void putColProc(String proc_type,String proc_status,BigDecimal col_number, boolean insert) throws Exception{
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
			this.user_lock=this.bo161.insertColProc(map);
		}else{
			this.user_lock=this.bo161.updateColProc(map);
		}
		
	}
	
	/**
	 * Fiksanje iznosa hipoteke na nacin da hip_vrijednost=round(hf_amount/tecaj) na 2 decimale
	 * 
	 * @param hf_amount prijasnji iznos hipoteke
	 * @param rate tecaj 
	 * @return novi iznos hipoteke
	 */
/*	public BigDecimal fix_hf_amount(BigDecimal hf_amount, BigDecimal rate){
		BigDecimal result=null;
		if((hf_amount!=null)&&(rate!=null)&&(rate.compareTo(new BigDecimal(0))!=0)){
			result=hf_amount.divide(rate,2,BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}*/
	
	public static void main(String[] args) {
		
        BatchParameters bp = new BatchParameters(new BigDecimal(1849806003));// bat_def_id s razvoja
        bp.setArgs(args);
        new BO160().run(bp);        
    }
	
	

}

