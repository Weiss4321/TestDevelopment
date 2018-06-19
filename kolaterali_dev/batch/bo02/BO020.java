/*
 * Created on 2007.02.02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo02;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.DealCollateralCoverage;

/**
 * Batch za izracun pokrivenosti, ali sva se logika nalazi u YOY8 commonu. Batch samo uzima plasmane u iterator
 * , provjerava da li su zapisani u col_proc_gar tablicu. ako nisu zapisani znaci da se nisu obradili matricnim izracunom 
 * u YOY8 commonu i stoga ulaze u izracun. ako vec postoje u col_proc_gar znaci da su se vec uzeli u izracun preko dohvata skupa podataka
 * za uparivanje plasmana i kolaterala 
 * 
 * 
 * @author hraamh
 *
 */
public class BO020 extends Batch {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo02/BO020.java,v 1.21 2012/02/29 09:30:07 hrakis Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private boolean debug=false;
	/**
     * flag da li se koristi ponderirani izracun
	 */
	private boolean ponding=true;
    /**
     * oznaka obrade u col_proc tablici. pogledati determintProcType() i klasu DealCollateralCoverage
     */
	private String proc_type="N";
	private int procState=0;
	

	private int eligibility=-1;
	
	/**
	 * Flag koji govori da li su u tabeli redovni podaci o izloženosti, podaci za kraj mjeseca ili nešto drugo.
	 */
	private String exp_type_ind = null;

	
	private long recordCount=0;
	private long doneRecordCount=0;
    /**
     * datum obrade
     */
	private Date proc_date=null;
    /**
     * datum valute koji se koristi u izracunu. datum valute plasmana
     */
	private Date value_date=null;
	private BigDecimal org_uni_id=null;
	private BigDecimal eve_typ_id=null;
	private BigDecimal use_id=null;
	private Timestamp user_lock=null;
	/**
	 * flag koji gleda da li je obrada iz pocetka ili se nastavlja prethodno prekinuta obrada
	 */
	private boolean newStart=true;
	
	
	private BO021 bo021=null;
	//private YOY80 yoy80=null;
	private DealCollateralCoverage dealCollateralCoverage=null;
	
	public BO020(){
		super();
		this.org_uni_id=new BigDecimal("53253");
		this.eve_typ_id= new BigDecimal("1689967003");
		this.proc_date= new Date(System.currentTimeMillis());
	}
	
	public String executeBatch(BatchContext contex) throws Exception {
		
		this.bc=contex;
		long startTime=0;
		long endTime=0;
		
		bc.debug("******************** BO020.executeBatch() poceo *********************");
		startTime=System.currentTimeMillis();
		this.bo021 =new BO021(bc);
		
		if (bc.getArgs().length!=3){
			bc.debug("Neispravan broj argumenata!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (bc.getArg(0).equals("RB")==false){
			bc.debug("Bank sign mora biti 'RB'!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (determintProcType(bc.getArg(1))!=0){
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (bc.getArg(2).equals("0")==true){
			this.debug=true;
		}
		
		bc.debug("Ponderirani_izracun: "+ponding);
		bc.debug("exp_type_ind: " + exp_type_ind);
		
		switch(eligibility){
			case DealCollateralCoverage.RBA:
				bc.debug("prihvatljivost RBA");
				break;
			case DealCollateralCoverage.B1:
				bc.debug("prihvatljivost B1");
				break;
			case DealCollateralCoverage.B2:
				bc.debug("prihvatljivost B2");
				break;
			case DealCollateralCoverage.B2IRB:
				bc.debug("prihvatljivost B2IRB");
				break;
            case DealCollateralCoverage.ND:
                bc.debug("prihvatljivost ND");
                break;
		}
		
		this.use_id=bc.getUserID();
		bc.debug("\nBO020->dohvat datuma valute");
		this.value_date=this.bo021.getValueDate(exp_type_ind);
		bc.debug("\nBO020->dohvat valute zavrsen: "+value_date);
		if(this.value_date==null){
			bc.debug("Nema datuma valute!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		//provjera da li je vec odraden proracun za dani datum
		if (debug) bc.debug("\nBO020->provjera da li je obrada vec pokrenuta");
		BigDecimal proc_id=this.bo021.selectColProc(value_date,proc_type,"1");
		if(proc_id!=null){
			procState=1;
			bc.debug("Obrada za dani datum je vec uspjesno odradena!");
			return RemoteConstants.RET_CODE_SUCCESSFUL;
		}else{
			//ako ne postoji uspjesna obrada gleda se da li postoji neuspjesna obrada
			proc_id=this.bo021.selectColProc(value_date,proc_type,"0");
			if(proc_id!=null){
				procState=0;
				bc.debug("Obrada za dani datum vec postoji ali nije zavrsena. Nastavljam...!");
				this.newStart=false;	
				this.bo021.setColProId(proc_id);
			}else{
				procState=-1;
				//nema nikakve obrade= potpuno nova obrada
				bc.debug("Potpuno nova obrada!");
				proc_id=this.bo021.getColProId();
				this.newStart=true;
			}
		}
		
		if(!bo021.isDataLoadedForDate(value_date)){
			bc.debug("Nisu uèitani svi potrebni podaci u pomocne *_d tabele!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		
		this.dealCollateralCoverage=CollateralCommonFactory.getDealCollateralCoverage(this.bc);
		this.dealCollateralCoverage.init(value_date,exp_type_ind,proc_id,this.use_id,this.debug);
		
		bc.debug("\nBO020->dohvat broja zapisa koje treba obraditi");
		this.recordCount=this.bo021.fetchCusAccExposureCount(proc_id,value_date,exp_type_ind);
		
		
		if (debug) bc.debug("\nBO020->insert dogadaja");
		bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bo021.insertIntoEvent(this.eve_typ_id,this.org_uni_id);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        if (debug) bc.debug("\nBO020->dogadaj insertan");
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
        
        if(newStart){
        	if (debug) bc.debug("\nBO020->zapisujem pocetak obrade plasmana");
            this.putColProc(this.proc_type,"0",new BigDecimal(0),this.user_lock,true);
        }     
        //oznacujem sve plasmane koji su vezani samo na neaktivne OS-ove i ne ulaze u obracun
        try{
        	this.bo021.markUnacceptableAgreementPlacements(value_date,exp_type_ind);
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        IteratorCusaccExposure iter=bo021.getPlacementsNew(value_date,exp_type_ind,eligibility);
        while(iter.next()){
        	String status=bo021.getColProcGarStatus(iter.cus_acc_id());
        	if(status!=null){
        		continue;
        	}else{
        		bc.debug("\nBO020-> obradujem plasman "+iter.cus_acc_id());
            	bc.beginTransaction();
            	int result=this.dealCollateralCoverage.execute(iter.cus_acc_id(),this.ponding,eligibility);
            	if(result!=0){
            		this.bo021.insertGarbage(iter.cus_acc_id(),""+result);
            	}
            	bc.commitTransaction();
            	bc.debug("\nBO020->plasman "+iter.cus_acc_id()+" obraden s rezultatom "+result);
        	}
        }
        iter.close();
        
        if (debug) bc.debug("\nBO020->zavrsena iterativna obrada");
        if (debug) bc.debug("\nBO020->dohvacam broj obradenih slogova");
        this.doneRecordCount=this.bo021.fetchCusAccExpCollCount(this.bo021.getColProId());
        bc.debug("\nBO020->zapisujem kraj obrade u COLL_PROC tablicu");
        bc.debug("\nBO020->fiksam COLL_PROC tablicu");
        //bo021.fixRounding(new BigDecimal("0.05"));
        this.putColProc(this.proc_type,"1",new BigDecimal(this.doneRecordCount),this.user_lock,false);
        bc.debug("\nBO020-> kraj obrade zapisan");
        endTime=System.currentTimeMillis();
        bc.debug("Broj zapisa za obradu=" + recordCount + " ,Broj obradenih zapisa=" + doneRecordCount);
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO020.executeBatch() zavrsio *********************");
		
		if (returnCode.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return returnCode;
	}
	
	public void putColProc(String proc_type,String proc_status,BigDecimal col_number, Timestamp user_lock, boolean insert) throws Exception{
		HashMap map= new HashMap();
		map.put("proc_date",proc_date);    
		map.put("value_date",value_date); 
		map.put("proc_type",proc_type);    
		map.put("proc_way","A");                
		map.put("proc_status",proc_status);          
		map.put("col_number",col_number);            
		//map.put("proc_ts",proc_ts);                  
		map.put("org_uni_id",org_uni_id);            
		map.put("use_id", use_id); 
		map.put("user_lock", user_lock);
		if(insert){
			this.user_lock=this.bo021.insertColProc(map);
		}else{
			this.user_lock=this.bo021.updateColProc(map);
		}
		
	}
	
	private int determintProcType(String procType){
		this.proc_type=procType;
		if(procType.equals("N")){
			this.ponding=false;			
			this.eligibility=DealCollateralCoverage.RBA;
			this.exp_type_ind=DealCollateralCoverage.DVA;
        }else if(proc_type.equals("NG")){
            this.ponding=false;
            this.eligibility=DealCollateralCoverage.RBA;
            this.exp_type_ind=DealCollateralCoverage.DGK;   
		}else if(procType.equals("C")){
			this.ponding=false;
			this.eligibility=DealCollateralCoverage.B1;
			this.exp_type_ind=DealCollateralCoverage.DVA;
		}else if(procType.equals("H")){
			this.ponding=false;
			this.eligibility=DealCollateralCoverage.B2;
			this.exp_type_ind=DealCollateralCoverage.DVA;
        }else if(proc_type.equals("HG")){
            this.ponding=false;
            this.eligibility=DealCollateralCoverage.B2;
            this.exp_type_ind=DealCollateralCoverage.DGK;			
		}else if(procType.equals("J")){
			this.ponding=false;
			this.eligibility=DealCollateralCoverage.B2IRB;
			this.exp_type_ind=DealCollateralCoverage.DVA;
		}else if(procType.equals("L")){
            this.ponding=false;
            this.eligibility=DealCollateralCoverage.ND;
            this.exp_type_ind=DealCollateralCoverage.DVA;
     
        
        }else if(procType.equals("P")){
			this.ponding=true;
			this.eligibility=DealCollateralCoverage.RBA;
			this.exp_type_ind=DealCollateralCoverage.DVA;
        }else if(proc_type.equals("PG")){
            this.ponding=true;
            this.eligibility=DealCollateralCoverage.RBA;
            this.exp_type_ind=DealCollateralCoverage.DGK;   
		}else if(procType.equals("E")){
			this.ponding=true;
			this.eligibility=DealCollateralCoverage.B1;
			this.exp_type_ind=DealCollateralCoverage.DVA;
		}else if(procType.equals("I")){
			this.ponding=true;
			this.eligibility=DealCollateralCoverage.B2;
			this.exp_type_ind=DealCollateralCoverage.DVA;
        }else if(procType.equals("IG")){
            this.ponding=true;
            this.eligibility=DealCollateralCoverage.B2;
            this.exp_type_ind=DealCollateralCoverage.DGK;			
		}else if(procType.equals("K")){
			this.ponding=true;
			this.eligibility=DealCollateralCoverage.B2IRB;
			this.exp_type_ind=DealCollateralCoverage.DVA;
		}else if(procType.equals("M")){
            this.ponding=true;
            this.eligibility=DealCollateralCoverage.ND;
            this.exp_type_ind=DealCollateralCoverage.DVA;
        }else if(procType.equals("T")){
            this.ponding=true;
            this.eligibility=DealCollateralCoverage.RBA_MICRO;
            this.exp_type_ind=DealCollateralCoverage.DVA;
		}else{
			return -1;
		}
		return 0;
	}
	


	public static void main(String[] args)
	{
        BatchParameters bp = new BatchParameters(new BigDecimal(1663357003));// id s razvoja
        bp.setArgs(args);
        new BO020().run(bp);        
    }    

}