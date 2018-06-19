/*
 * Created on 2007.10.04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo15;

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


/**
 * Batch za ispravak stanja za 99 .....
 * 
 * @author hramkr
 *
 */
public class BO150 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo15/BO150.java,v 1.4 2011/01/21 09:32:22 hramkr Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	private boolean debug=false;
	private String procType="B";
	
	
	BO151 bo151= null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
//	private BigDecimal ratio=null;
	
	private Date proc_date=null;
	
	private Timestamp user_lock=null;
	
 

	
	public BO150(){
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

		bc.debug("******************** BO150.executeBatch() poceo *********************");
		
		if (bc.getArgs().length<3){
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

		String account = bc.getArg(2);
		boolean do_all = false;
		if (account.equals("ALL"))
		    do_all = true;
		
		long startTime=System.currentTimeMillis();
		this.bo151= new BO151(bc,org_uni_id,use_id);
		//long recordCount=0;
		
		boolean newProces=false;
		
		BigDecimal cur_id = new BigDecimal("63999");
		
		if (debug) bc.debug("\nBO150->provjera da li je obrada vec pokrenuta");
		BigDecimal proc_id=this.bo151.selectColProc(this.procType,"1");
		if(proc_id!=null){
			bc.debug("Obrada je vec uspjesno odradena!");
			return RemoteConstants.RET_CODE_ERROR;
		}else{
			//ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
			proc_id=this.bo151.selectColProc(this.procType,"0");
			if(proc_id!=null){		
				bc.debug("Obrada vec postoji (id= "+proc_id+") ali nije zavrsena. Nastavljam...!");
				this.bo151.setColProId(proc_id);
			}else{
				
				//nema nikakve obrade= potpuno nova obrada
				bc.debug("Potpuno nova obrada!");
				proc_id=this.bo151.getColProId();
				newProces=true;
			}
		}
		   
		 
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	if(newProces){
        		this.putColProc(this.procType,"0",new BigDecimal(0),true);
        	}
        	bc.debug("\nBO150->insertam dogadaj");
        	bo151.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        	bc.debug("\nBO150->dogadaj insertan");
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
         
        bc.commitTransaction();
        
        IteratorCusaccBalance iter=null;
        try{
        	iter= bo151.selectCusaccBalance(account, do_all);
        	if(iter!=null){			 
				while(iter.next()){
					if(debug) bc.debug("\nfiksam zapis "+iter.cusacc_bal_id());
					bc.beginTransaction();
					BigDecimal debit_amount = bo151.selectAccountTurnoverDebit(iter.acc_num_id(),iter.account(),iter.balance_date());
					BigDecimal credit_amount = bo151.selectAccountTurnoverCredit(iter.acc_num_id(),iter.account(),iter.balance_date());
					bo151.updateCusaccBalance(iter.cusacc_bal_id(),cur_id,debit_amount,credit_amount);
					bo151.insertInDataDwhItem(bo151.getColProId(),iter.cusacc_bal_id(),null,"0",null);
					bc.commitTransaction();
					if(debug) bc.debug("\nfiksano id:"+iter.cusacc_bal_id()+" cus_acc_id:"+iter.cus_acc_id()+" credit_amount:"+credit_amount+" debit_amount:"+debit_amount+" balance:" + credit_amount.subtract(debit_amount));
				}
	        }    
        	BigDecimal recordCount=bo151.getRecordCount();
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
        
        bc.debug("\nBO150-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO150.executeBatch() zavrsio *********************");
		
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
			this.user_lock=this.bo151.insertColProc(map);
		}else{
			this.user_lock=this.bo151.updateColProc(map);
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
        new BO150().run(bp);        
    }
	
	

}

