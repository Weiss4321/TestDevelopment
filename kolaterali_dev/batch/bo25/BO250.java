/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo25;

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
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.rba.util.StringUtils;

/**
 * 
 * Deaktivacija dospjelih okvirnih sporazuma
 * 
 * 
 * @author hraamh
 *
 */
public class BO250 extends Batch {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo25/BO250.java,v 1.7 2015/03/17 08:04:34 hrakis Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	private Date proc_date=null;
	private Timestamp user_lock=null;
	private String proc_status="0";
	private BigDecimal eve_id=null;
	private String proc_type="S";
	private BigDecimal proc_id=null;
	
	private BO251 bo251=null;
	
	/**
	 * 
	 */
	public BO250() {
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal("2354126003");	
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());
	}
	

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	@Override
	public String executeBatch(BatchContext bc) throws Exception {
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;
		this.bc=bc;
		/*
		 * Stvaranje potrebnih objekata
		 */
		CommonCollateralMethods commonMethods= CollateralCommonFactory.getCommonCollateralMethods(bc);
		bo251= new BO251(bc,org_uni_id,use_id);

		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	proc_id=bo251.getColProId();
        	commonMethods.insertColProc(getColProcMap(proc_id,proc_type,"0",new BigDecimal(0),this.user_lock));
        	this.eve_id=commonMethods.insertIntoEvent(this.eve_typ_id,this.org_uni_id,this.use_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.debug("\ndogadaj insertan");
        bc.info("PROC_DATE = " + proc_date);
        bc.commitTransaction();
		
        long count=0;
        long startTime= System.currentTimeMillis();
        
        CollateralPosting collPosting=CollateralCommonFactory.getCollateralPosting(this.bc);
        
        IteratorFrameAgr iter= bo251.selectFrameAgreements();
        
        
        if(iter!=null){
        	while(iter.next()){
        	    bc.info("FRA_AGR_ID = " + iter.fra_agr_id());
        	    boolean isObsolete = bo251.isObsolete(iter.fra_agr_id(), proc_date);
        	    boolean isReadyToDeactivate = bo251.isReadyToDeactivate(iter.fra_agr_id());
        	    bc.info("isObsolete = " + isObsolete + ", isReadyToDeactivate = " + isReadyToDeactivate);
        	    
        		if(isObsolete && isReadyToDeactivate){
        			bc.beginTransaction();

        			bo251.updateAgrListQ(iter.fra_agr_id(), "1");
        			BigDecimal id= bo251.insertAgrListQ(iter.fra_agr_id(), "0");
        			bo251.updateFrameAgreementMortgagesStatus(eve_id, iter.fra_agr_id(), "I", "NA", "N");
                    bo251.updateFrameAgreement(iter.fra_agr_id(),"N");
        			IteratorColHeaId coll_head_iter= bo251.getFrameAgrCollaterals(iter.fra_agr_id());	
        			if(coll_head_iter!=null){
        				while(coll_head_iter.next()){
        					Map collateralData= bo251.selectCollateralById(coll_head_iter.col_hea_id());
        					BigDecimal col_hea_id=coll_head_iter.col_hea_id();
        					String collateral_status=(String) collateralData.get("collateral_status");
        					Timestamp coll_user_lock=(Timestamp) collateralData.get("user_lock");
        					Boolean posting=(Boolean) collateralData.get("posting");
        					
        					boolean isActiveMortgagesCollateral=rearrangeMortgages(col_hea_id);
        					//ako nema aktivnih hipoteka treba isknjiziti i staviti na listu slobodnih
        					if(!isActiveMortgagesCollateral){
        						bo251.updateCollHeadStatus(col_hea_id, "N", use_id);
        						if(posting){
        							collPosting.CollPosting(col_hea_id, true);
        						} 
        						bo251.updateCollListQ(col_hea_id, "DEAKTIVIRAJ");
        						bo251.insertCollListQ(col_hea_id);
        					}
        				}       				
        				coll_head_iter.close();
        			}
        			commonMethods.insertInDataDwhItem(proc_id, iter.fra_agr_id(), null, "0", id);	
        			bc.commitTransaction();
        			count++;
        		}
        		
        	}
        }
        
        
        
        bc.beginTransaction();
        commonMethods.updateColProc(getColProcMap(proc_id,proc_type,"1",new BigDecimal(count),this.user_lock));
        bc.commitTransaction();
		
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO220.executeBatch() zavrsio *********************");
		
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return toReturn;
	}
	
	private boolean rearrangeMortgages(BigDecimal col_hea_id) throws Exception{
		int lastPriority=0;
		boolean result=false;
		
		IteratorColHfPrior iter=null;
		
		try {
			iter=bo251.getMortgagesByCollateral(col_hea_id);
			if(iter!=null){
				while(iter.next()){
					result=true;
					int priority=Integer.parseInt(iter.hf_priority());
					if(priority>(lastPriority+1)){
						//ako treba presloziti prioriteta hipoteka
						bo251.updateHfPriority(iter.coll_hf_prior_id(), StringUtils.generateStringWithLeadingZeros(""+priority, 2), iter.user_lock(), eve_id);
					}
					lastPriority++;
				}
				iter.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
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
		BatchParameters bp = new BatchParameters(new BigDecimal("2354125003"));// id s razvoja
        bp.setArgs(args);
        new BO250().run(bp);
	}

}
