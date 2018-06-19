/*
 * Created on 2007.11.23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo19;

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
import hr.vestigo.modules.rba.util.DateUtils;

/**
 * 
 * Oslobadanje kolaterala
 * 
 * @author hraamh
 *
 */
public class BO190 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo19/BO190.java,v 1.22 2017/07/20 11:45:24 hrakis Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	private boolean debug=false;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	private BigDecimal batch_id=null;
	private Date proc_date=null;
	private Timestamp user_lock=null;
	private String proc_status="0";
	
	private BO191 bo191=null;
	private Map<String, BigDecimal> collList=null;
    
    private boolean isObsolete=false;
    private boolean isDWHStatus=false;
    private boolean isSSPFree=false;
    private boolean isIpSpecStatus=false;

	/**
	 * 
	 */
	public BO190() {
		super();
		this.org_uni_id=new BigDecimal(53253);
		this.eve_typ_id= new BigDecimal(1929906003);	
		this.use_id=new BigDecimal(1);
		this.proc_date= new Date(System.currentTimeMillis());
		
		this.collList= new HashMap<String, BigDecimal>();
		this.collList.put("AKTIVNI",new BigDecimal(700223));
		this.collList.put("SLOBODNI",new BigDecimal(710223));
		this.collList.put("NEAKTIVNI",new BigDecimal(709223));
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	public String executeBatch(BatchContext contex) throws Exception {
		this.bc=contex;
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

		bc.debug("******************** BO190.executeBatch() poceo *********************");
		
		if ((bc.getArgs().length!=1)&&(bc.getArgs().length!=2)){
			bc.debug("Neispravan broj argumenata!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if(bc.getArgs().length==2){
			if (bc.getArg(1).equals("0")==true){
				this.debug=true;
			}
		}
		long startTime=System.currentTimeMillis();
		this.bo191= new BO191(bc,org_uni_id,use_id);
		
	
		
		boolean newProces=false;
		
		
		BigDecimal proc_id=this.bo191.getColProId();	
		bc.debug("\nBO090->insertam dogadaj");
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	this.putColProc("O",proc_status,new BigDecimal(0),this.user_lock,true);
        	bo191.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.debug("\nBO090->dogadaj insertan");
        bc.commitTransaction();
        int counter=0;
        IteratorCollHead collateralIter=null;
        try{
        //dohvat kolaterala
	        collateralIter=bo191.selectCollaterals();
	        if(collateralIter !=null){
	        	
	        	CollateralPosting collPosting=CollateralCommonFactory.getCollateralPosting(this.bc);
	        	while(collateralIter.next()){
	        		bc.debug("\n********************************************\nDohvacen _col_hea_id="+collateralIter.col_hea_id()+"\n***************************");
		        	
	        		//ako je dio okvirnog sporazuma ne oslobada se; dakle samo nastavlja
	        		//18.03.2008. izbaceno na zahtjev koristnika (Nevena)
	        		/*if(bo191.isFrameAgreementCollateral(collateralIter.col_hea_id())){
	        			continue;
	        		}*/
	        		
	        		//ako je tip za hipoteke i nema aktivne hipoteke -> oslobada se kolateral
	        		try{
		        		if(bo191.isMortgageType(collateralIter.col_cat_id())){
		        		    bc.debug("***kolateral ima hipoteku");
		        			if(isMortgageFree(collateralIter.col_hea_id(),collateralIter.col_cat_id())){
		        			    bc.debug("***hipoteke su neaktivne - oslobadjam kolateral");
		        				bc.beginTransaction();
		        				freeCollateral(collateralIter.col_hea_id(),collateralIter.user_lock(),collateralIter.col_cat_id());
		        				if((collateralIter.accounting_indic()!=null)&&(collateralIter.accounting_indic().trim().compareTo("1")==0)){
		        					collPosting.CollPosting(collateralIter.col_hea_id(), true);
		        				}
		        				bo191.insertInDataDwhItem(bo191.getColProId(), collateralIter.col_hea_id(), null, "0", null);
		        				counter++;
		        				bc.commitTransaction();
		        			}
		        			//ako je direktno vezanje i nema plasmana vezanih -> oslobada se kolateral
		        		}else{
                            bc.debug("***kolateral nema hipoteku");
		        			if(isDirectFree(collateralIter.col_hea_id(),collateralIter.col_cat_id())){
	                               bc.debug("*** oslobadjam kolateral");
		        				bc.beginTransaction();
		        				freeCollateral(collateralIter.col_hea_id(),collateralIter.user_lock(),collateralIter.col_cat_id());
		        				if((collateralIter.accounting_indic()!=null)&&(collateralIter.accounting_indic().trim().compareTo("1")==0)){
		        					collPosting.CollPosting(collateralIter.col_hea_id(), true);
		        				}
		        				bo191.insertInDataDwhItem(bo191.getColProId(), collateralIter.col_hea_id(), null, "0", null);
		        				counter++;
		        				bc.commitTransaction();
		        			}
		        		}
	        		}catch (hr.vestigo.framework.controller.tm.VestigoTMException e) {
						if(bc.isTransactionActive()){
							bc.rollbackTransaction();
						}
						System.out.println(e.getMessage());
						e.printStackTrace();
					}catch (hr.vestigo.framework.remote.transaction.VestigoTMException e) {
						if(bc.isTransactionActive()){
							bc.rollbackTransaction();
						}
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					
					//if(counter>100) break;
	        	}
	        }
	          	
        proc_status="1";
		}catch (Exception e) {
			proc_status="E";
	        e.printStackTrace();
	        toReturn=RemoteConstants.RET_CODE_ERROR;
	    }finally{
	    	try{
	    		BigDecimal recordCount=bo191.getRecordCount();
	    		bc.debug("Record count: "+recordCount);
	    		bc.debug("Col_pro_status: "+proc_status);
	    		this.putColProc("O",proc_status,recordCount,this.user_lock,false);
	    		collateralIter.close();
	    		bo191.closeExtraConnection();
	    		if (recordCount != null) bc.setCounter(recordCount.intValue());
	    	}catch(SQLException sqle){
	    		sqle.printStackTrace();
	    		return RemoteConstants.RET_CODE_ERROR;
	    	}
	    }
        
        
        bc.debug("\nBO1900-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO190.executeBatch() zavrsio *********************");
		
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return toReturn;
	}
	
	public boolean isDirectFree(BigDecimal col_hea_id, BigDecimal col_cat_id) throws Exception{
        isObsolete=false;
        isDWHStatus=false;
        isSSPFree=false;
        isIpSpecStatus=false;
        
		if(bo191.isObsolete(col_hea_id, col_cat_id)){
			bc.debug("isDirectFree->true isObsolete "+col_hea_id);
            isObsolete=true;
			return true;
		}
		
        bc.debug("isDirectFree->false isObsolete "+col_hea_id);
		//FBPr200011986 dodatni uvjet za police  
		
		if(col_cat_id.longValue()==616223){ 
	      if(bo191.checkIpSpecStatus(col_hea_id)){
	            bc.debug("isDirectFree->true checkIpSpecStatus "+col_hea_id);
	            isIpSpecStatus=true;
	            return true;
	        }
		}

        bc.debug("isDirectFree->false checkIpSpecStatus "+col_hea_id);
		
		//svi iz loan_ben a
		int all=bo191.countLoanBenDirectly(col_hea_id);
		if(all==0){
			bc.debug("isDirectFree->false all==0 "+col_hea_id);
			return false;
		}
		//svi iz loan vezani na cusacc_exposure b
		int allCusaccExp=bo191.countCussacExpDirectly(col_hea_id, false);
		//ako a!=b false
		if(all!=allCusaccExp){
			bc.debug("isDirectFree->false all!=allCusaccExp "+col_hea_id);
			return false;
		}
		//svi iz loan vezani na cusacc_exposure u status C ili R c
		int allCusaccExpInactive=bo191.countCussacExpDirectly(col_hea_id, true);
		//ako je b!=c false
		if(allCusaccExpInactive!=allCusaccExp){
			bc.debug("isDirectFree->false allCusaccExpInactive!=allCusaccExp "+col_hea_id);
			return false;
		}else{
            isDWHStatus=true;
        }
		/**
		 * 
		 * provjera da nije SSP kolateral
		 * 
		 */
		
		
		
		if(col_cat_id.longValue()!=612223){
			//provjerava se ako nisu garancije			
			boolean ssp=bo191.isDirectlySSP(col_hea_id);			
			bc.debug("checking SSP directly for "+col_hea_id+"="+ssp);
			if(ssp){
				return false;
			}else{
                isSSPFree=true;
            }
		}	
		
		bc.debug("isDirectFree->true "+col_hea_id);
		return true;
		//true
	}
	
	public boolean isMortgageFree(BigDecimal col_hea_id, BigDecimal col_cat_id) throws Exception{
        isObsolete=false;
        isDWHStatus=false;
        isSSPFree=false;
        
        //dodano na zahtjev FBPr200007652 31.12.2009.
        //ako je dio okvirnog sporazuma i sporazum je istekao prije 3 godine
        
        //za obveznice nema provjere FBPr200011841
        if(col_cat_id.longValue()!=619223){
            if(!bo191.checkFrameAgreement(col_hea_id)){
                return false;
            }          
        }
        
        
		if(bo191.isObsolete(col_hea_id, col_cat_id)){
			bc.debug("isMortgageFree->true isObsolete "+col_hea_id);
            isObsolete=true;
			return true;
		}
		
		//provjere se ne rade za obveznice
		if(col_cat_id.longValue()!=619223){ 
		    
		      //svi iz loan_ben a
	        int all=bo191.countLoanBenMortgage(col_hea_id);
	        if(all==0){
	            bc.debug("isMortgageFree->false all==0 "+col_hea_id);
	            return false;
	        }
	        //svi iz loan vezani na cusacc_exposure b
	        int allCusaccExp=bo191.countCussacExpMortgage(col_hea_id, false);
	        //ako a!=b false
	        if(all!=allCusaccExp){
	            bc.debug("isMortgageFree->false all!=allCusaccExp "+col_hea_id);
	            return false;       
	        }
	        //svi iz loan vezani na cusacc_exposure u status C ili R c
	        int allCusaccExpInactive=bo191.countCussacExpMortgage(col_hea_id, true);
	        //ako je b!=c false
	        if(allCusaccExpInactive!=allCusaccExp){
	            bc.debug("isMortgageFree->false allCusaccExpInactive!=allCusaccExp "+col_hea_id);
	            return false;
	        }else{
	            isDWHStatus=true;
	        } 
	        /*
	         * 
	         * provjera da nije SSP kolateral
	         * 
	         */
	        boolean ssp=bo191.isMortgageSSP(col_hea_id);
	        bc.debug("checking SSP through mortgage for "+col_hea_id+"="+ssp);
	        if(ssp){
	            return false;
	        }else{
	            isSSPFree=true;
	        }
	        bc.debug("isMortgageFree->true "+col_hea_id);
	        return true;
	        //true  	        
		}else {
		    return isObsolete;
		}
	}
	 
	public void freeCollateral(BigDecimal col_hea_id, Timestamp user_lock, BigDecimal col_cat_id) throws Exception{
		String coll_status=null;
		long category=col_cat_id.longValue();
		BigDecimal col_lis_typ_id_insert=null;
		BigDecimal col_lis_typ_id_update=this.collList.get("AKTIVNI");
		String action_type=null;
		bc.debug("*** u metodi freeCollateral:" );
		//625223 - zaduznice
		//617223 - mjenice
		//615223 - garancije (direktno)
		//612223 - cash dep
		//616223 - police osiguranja
        //614223 - cesije
		if((category==625223)||(category==617223)||(category==615223)||(category==612223) || (category==616223) || (category==614223)){
			coll_status="N";
			action_type="DEAKTIVIRAJ";
			col_lis_typ_id_insert=this.collList.get("NEAKTIVNI");
		}else if ((category==624223) && (bo191.isMigratedVehicle(col_hea_id))){
		    //624223 - vozila
		    //vozila migrirana 21.6.2007. se ne oslobadaju, samo deaktiviraju
            coll_status="N";
            action_type="DEAKTIVIRAJ";
            col_lis_typ_id_insert=this.collList.get("NEAKTIVNI");
            bo191.deactivateVehicleMortgage(col_hea_id);
        }else if (category==627223){
            //627223 - zapisi
            if(isObsolete){
                coll_status="N";
                action_type="DEAKTIVIRAJ";
                col_lis_typ_id_insert=this.collList.get("NEAKTIVNI");
            }else if(isSSPFree){
                coll_status="F";
                action_type="OSLOBODI";
                col_lis_typ_id_insert=this.collList.get("SLOBODNI");
            }            
        }else if (category==619223){
            //619223 - obveznice
            if(isObsolete){
                coll_status="F";
                action_type="OSLOBODI";
                col_lis_typ_id_insert=this.collList.get("SLOBODNI");
            }
        } else{
            //629223 - udjeli u poduzecima
            //626223 - zalihe
            //624223 - vozila, ostala
            //622223 - udjeli u fondovima
            //621223 - pokretnine
            //620223 - plovila
            //618223 - nekretnine
            //613223 - dionice
			coll_status="F";
			action_type="OSLOBODI";
			col_lis_typ_id_insert=this.collList.get("SLOBODNI"); 
		    bc.debug("*** u metodi freeCollateral 2:" );
		}	
		bo191.updateCollHead(col_hea_id, user_lock,coll_status);
		bo191.insertCollListQ(col_hea_id,col_lis_typ_id_insert);
		bo191.updateCollListQ(col_hea_id,col_lis_typ_id_update,action_type);
	}

 


	
	public void putColProc(String proc_type,String proc_status,BigDecimal col_number, Timestamp user_lock, boolean insert) throws Exception{
		Map map= new HashMap();
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
			this.user_lock=this.bo191.insertColProc(map);
		}else{
			this.user_lock=this.bo191.updateColProc(map);
		}
		
	}

	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal(1929922003));// id s razvoja
        bp.setArgs(args);
        new BO190().run(bp);
	}
}
