/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo23;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.rba.util.DateUtils;

/**
 * Batch za B2 izvjestaje o kolateralima i plasmanima
 * 
 * @author hraamh
 *
 */
public class BO230 extends Batch {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo23/BO230.java,v 1.14 2015/01/09 14:56:52 hrakis Exp $";
	
	private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private BigDecimal org_uni_id=null;
	private BigDecimal use_id=null;
	private BigDecimal eve_typ_id=null;
	private Date proc_date=null;	
	private String bank_sign=null;
	private String proc_type=null;
	private BigDecimal eve_id=null;
	private BigDecimal eur_id=null;
	//private BigDecimal col_pro_fetch_id=null;
	private String cus_type=null;
	private BigDecimal zero=null;
	
	private HashSet<Long> collateralSet=null;
	
	private LinkedList<CollateralDataRow> coll_cache=null;
	private LinkedList<ExposureDataRow> exp_cache=null;
	private int cache_size=15;
	
	private static final String encoding = "Cp1250";
	
	private String collateralOutputFile=null;
	private String exposureOutputFile=null;
	
	private BO231 bo231=null;

	
	public BO230(){
		super();
		org_uni_id=new BigDecimal(53253);
		eve_typ_id= new BigDecimal("2257081003");
		use_id=new BigDecimal(1);
		zero= new BigDecimal(0);
		proc_date= new Date(System.currentTimeMillis());			
		eur_id= new BigDecimal(64999);
		coll_cache= new LinkedList<CollateralDataRow>();
		exp_cache= new LinkedList<ExposureDataRow>();
		collateralSet= new HashSet<Long>();
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.batch.Batch#executeBatch(hr.vestigo.framework.remote.batch.BatchContext)
	 */
	@Override
	public String executeBatch(BatchContext bc) throws Exception {
		this.bc=bc;
		bank_sign=bc.getBankSign();
		String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;

		bc.debug("******************** BO230.executeBatch() poceo *********************");
		
		if (bc.getArgs().length!=4) {
			bc.debug("Broj ulaznih argumenata nije 4!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		
		String v_date=bc.getArg(1);
		Date value_date= DateUtils.parseDate(v_date);
		proc_type=bc.getArg(2);
		cus_type=bc.getArg(3);
		collateralOutputFile=bc.getOutDir()+"/CollateralCSV"+value_date+".csv";
		exposureOutputFile=bc.getOutDir()+"/ExposureCSV"+value_date+".csv";
		bc.debug("value_date: "+value_date);
		bc.debug("collateralOutputFile: "+collateralOutputFile);
		long startTime=System.currentTimeMillis();
		
		/*
		 * Stvaranje potrebnih objekata
		 */
		CommonCollateralMethods commonMethods= CollateralCommonFactory.getCommonCollateralMethods(bc);
		this.bo231= new BO231(bc);
		
		bc.beginTransaction();
		commonMethods.insertIntoEvent(eve_typ_id, org_uni_id, use_id);
		bc.commitTransaction();
		/*
		col_pro_fetch_id=commonMethods.selectColProc(value_date, proc_type, "1");
		if(col_pro_fetch_id==null){
			bc.debug("Ne postoji obrada za dani datum");
			return RemoteConstants.RET_CODE_ERROR;
		}
		*/
        //commonMethods.updateColProc(getColProcMap(proc_id,proc_type,"1",new BigDecimal(count),this.user_lock));
    	
		//makeCollateralReport(value_date);
		
		makeExposureReport(value_date);
		
        bc.debug("\nBO230-> kraj obrade zapisan");
        long endTime=System.currentTimeMillis();
        
		bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");
	
		bc.debug("******************** BO230.executeBatch() zavrsio *********************");
		
		if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) returnCode=RemoteConstants.RET_CODE_SUCCESSFUL;
		
		return toReturn;
	}
	
	private boolean isCollateralWritten(long col_hea_id){
		return collateralSet.contains(col_hea_id);
	}
	
	private void markCollateral(long col_hea_id){
		collateralSet.add(col_hea_id);
	}
	
	private CollateralDataRow makeCollDataRow(BigDecimal col_cat_id, BigDecimal col_hea_id, Date value_date, BigDecimal cur_id, 
			BigDecimal value, String coll_num, String coll_type_name, String eligibility, String b2_irb_elig, Date due_date) throws Exception{
		CollateralDataRow element=new CollateralDataRow();
		element.coll_num=coll_num;
		element.coll_type_name=coll_type_name;
		element.eligibility=eligibility;
		element.b2_irb_elig=b2_irb_elig;
		element.id=col_hea_id.longValue();
		//trazi se vlasnik
		Map owner= bo231.getCollOwner(col_hea_id, col_cat_id);
		element.owner_code=(String)owner.get("owner_code");
		element.owner_name=(String)owner.get("owner_name");
		
		Map hf= bo231.getHfPriorData(col_hea_id, value_date, cur_id, col_cat_id);
		element.rba_first_date_from= (Date) hf.get("date_from");
		element.rba_last_date_until= (Date) hf.get("date_until");
		
				
		if(value!=null){
			BigDecimal other_amount=(BigDecimal) hf.get("other_amount");
			element.coll_value=value;
			BigDecimal minored_value=value.subtract(other_amount);
			
			if(zero.compareTo(minored_value)>=0){
				element.value=zero;
				element.value_eur=zero;
			}else{
				element.value=minored_value;
				element.value_eur=bo231.exchange(cur_id, eur_id, minored_value, value_date);
			}			
		}
		if(cur_id!=null){
			element.cur_char=bo231.getCurrencyChar(cur_id);
		}
		element.due_date=due_date;
		element.trim();
		
		return element;
	}
	
	
	private ExposureDataRow makeExpDataRow(BigDecimal cus_acc_id,BigDecimal cur_id, BigDecimal value, 
			String acc_no, String owner_code, String owner_name, String request_no, Date value_date,
			Date hf_date_from, Date hf_date_until, String b2asset_class, BigDecimal col_cat_id, BigDecimal col_hea_id) throws Exception{
		ExposureDataRow element=new ExposureDataRow();
		element.id=cus_acc_id.longValue();
		element.cus_acc_no=acc_no;
		element.owner_code=owner_code;
		element.owner_name=owner_name;
		element.request_no=request_no;
		element.amount=value;
        element.b2_asset_class=b2asset_class;
        
        
		
		if((hf_date_from!=null)&&(hf_date_from.toString().equalsIgnoreCase("0001-01-01"))){
			hf_date_from=null;
		}
		
		if((hf_date_until!=null)&&(hf_date_until.toString().equalsIgnoreCase("0001-01-01"))){
			hf_date_until=null;
		}
		
		if (col_cat_id.longValue()==612223) 
		    hf_date_until=bo231.getCashDueDate(col_hea_id);
		
		element.hf_date_from=hf_date_from;
		element.hf_date_until=hf_date_until;
		
		if(value!=null){
			element.amount_eur=bo231.exchange(cur_id, eur_id, value, value_date);
		}
		element.trim();
		
		return element;
	}
	
	/*
	private void makeCollateralReport(Date value_date) throws Exception{
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(collateralOutputFile)), encoding);
		streamWriter.write(getCollateralHeader());
		streamWriter.flush();
		
		IteratorCollateral collaterals= bo231.getCollateralIterator(value_date);
		if (collaterals!=null) {
			int step=1;
			int count=0;
			while(collaterals.next()){
				count++;
				bc.debug("Obradujem coll_num->"+collaterals.col_num());				
				CollateralDataRow row=makeCollDataRow(collaterals.col_hea_id(), value_date, collaterals.cur_id(), collaterals.value(),
						collaterals.col_num(), collaterals.col_typ_name(), collaterals.eligibility());
				streamWriter.write(row.getCVSRow()+"\n");
				if(step<10){
					step++;
				}else{
					step=0;
					streamWriter.flush();
				}
				//if (count>100) break;
			}
			streamWriter.flush();
			streamWriter.close();
		}
	}
	*/

	private void makeExposureReport(Date value_date) throws Exception{
		OutputStreamWriter expStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(exposureOutputFile)), encoding);
		expStreamWriter.write(getExposureHeader());
		expStreamWriter.flush();
		OutputStreamWriter collStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(collateralOutputFile)), encoding);
		collStreamWriter.write(getCollateralHeader());
		collStreamWriter.flush();
		Date cde_date_until_final = null;
		
		IteratorExpColl exposures= bo231.getNewExpCollIterator(value_date,cus_type);
		if (exposures!=null) {
			int step=1;
			int count=0;
			while(exposures.next()){
				count++;
				bc.debug("***********************testiram na sistemskom************************************");
				
				Date due_date=bo231.getDueDate(exposures.col_cat_id(), exposures.col_hea_id(), exposures.due_date());

				CollateralDataRow coll=null;
				ExposureDataRow exp=null;
				coll=getCachedCollateral(exposures.col_hea_id().longValue());
				if(coll==null){
					coll=makeCollDataRow(exposures.col_cat_id(), exposures.col_hea_id(), value_date, exposures.col_cur_id(), exposures.col_value(),
							exposures.col_num(), exposures.col_typ_name(), exposures.eligibility(), exposures.b2_irb_elig(), due_date);
					
					cacheCollateral(coll);
				}
				  
				 
//				exp=getCachedExposure(exposures.cus_acc_id().longValue());

//				if(exp==null){
					exp=makeExpDataRow(exposures.cus_acc_id(), exposures.exp_cur_id(), exposures.exp_balance(), exposures.cus_acc_no(), 
							exposures.exp_owner_code(), exposures.exp_owner_name(), exposures.request_no(), 
                            value_date, exposures.hf_date_from(), exposures.hf_date_until(), exposures.b2asset_class(),
                            exposures.col_cat_id(), exposures.col_hea_id());
//					cacheExposure(exp);
//				}
				
				bc.debug("Exposure data:");
				bc.debug(exp.toString());
				bc.debug("Collateral data:");
				bc.debug(coll.toString());
				
				bc.debug("**********************************************************************");
				
				expStreamWriter.write(exp.getCVSRow()+";"+coll.getShortCVSRow()+";"+exp.getHfDateAppendix()+"\n");
				if(!isCollateralWritten(coll.id)){
					collStreamWriter.write(coll.getCVSRow()+"\n");
					markCollateral(coll.id);
					bc.debug("Pisem coll id:"+coll.id);
				}
				  
				if(step<10){
					step++;
				}else{
					step=0;
					expStreamWriter.flush();
					collStreamWriter.flush();
				}
				//if (count>100) break;
			}
			expStreamWriter.flush();
			expStreamWriter.close();
			collStreamWriter.flush();
			collStreamWriter.close();
		}
	}
	
	private void cacheCollateral(CollateralDataRow data){
		coll_cache.add(data);
		if(coll_cache.size()>cache_size) coll_cache.removeFirst();
	}
	
	
	
	private void cacheExposure(ExposureDataRow data){
		exp_cache.add(data);
		if(exp_cache.size()>cache_size) exp_cache.removeFirst();
	}
	
	/**
	 * Usporeduje podatke iz cache-a i vraca identificirani objekt. Ako nema trazenog objekta vraca null
	 * 
	 * @param ident identifikator (col_hea_id)
	 * @return identificirani objekt, null ako ga ne nade
	 */
	private CollateralDataRow getCachedCollateral(long ident){
		for (int i=(coll_cache.size()-1); i>=0;i--){
			CollateralDataRow tmp= (CollateralDataRow) coll_cache.get(i);
			if(tmp.identified(ident)){
				if(i!=(coll_cache.size()-1)){
					coll_cache.remove(i);
					coll_cache.add(tmp);
				}
				return tmp;
			}
		}
		return null;
	}
	
	/**
	 * Usporeduje podatke iz cache-a i vraca identificirani objekt. Ako nema trazenog objekta vraca null
	 * 
	 * @param ident identifikator (col_hea_id)
	 * @return identificirani objekt, null ako ga ne nade
	 */
	private ExposureDataRow getCachedExposure(long ident){		
		for (int i=(exp_cache.size()-1); i>=0;i--){
			ExposureDataRow tmp= (ExposureDataRow) exp_cache.get(i);
			if(tmp.identified(ident)){
				if(i!=(exp_cache.size()-1)){
					exp_cache.remove(i);
					exp_cache.add(tmp);
				}
				return tmp;
			}
		}
		return null;
	}
	
	private String getExposureHeader(){
		StringBuffer buffer= new StringBuffer();
        buffer.append("B2 ASSET CLASS VL. PLASMANA").append(";");
		buffer.append("ID OMEGA KORISNIKA PLASMANA").append(";");
		buffer.append("NAZIV KORISNIKA PLASMANA").append(";");
		buffer.append("PARTIJA PLASMANA").append(";");
		buffer.append("IZNOS PARTIJE EUR").append(";");
		buffer.append("BROJ ZAHTJEVA").append(";");
		buffer.append("ID OMEGA VLASNIKA KOLATERALA").append(";");
		buffer.append("NAZIV KLIJENTA - VLASNIKA KOLATERALA ").append(";");
		buffer.append("VRSTA KOLATERALA").append(";");
		buffer.append("PARTIJA KOLATERALA").append(";");
		buffer.append("DATUM DOSPIJEÆA KOLATERALA").append(";");
		buffer.append("VALUTA").append(";");		
		buffer.append("EXECUTION VALUE").append(";");
		buffer.append("EXECUTION VALUE EUR").append(";");
		buffer.append("B2 ELIGIBILITY INDICATOR").append(";");
	    buffer.append("B2 IRB ELIGIBILITY INDICATOR").append(";");
		buffer.append("DATUM UPISA HIPOTEKE").append(";");
		buffer.append("DATUM DOSPIJEÆA HIPOTEKE").append("\n");	
		return buffer.toString();
	}
	
	private String getCollateralHeader(){
		StringBuffer buffer= new StringBuffer();
		buffer.append("ID OMEGA VLASNIKA KOLATERALA").append(";");
		buffer.append("NAZIV KLIJENTA - VLASNIKA KOLATERALA").append(";");
		buffer.append("VRSTA KOLATERALA").append(";");
		buffer.append("PARTIJA KOLATERALA").append(";");
		buffer.append("DATUM DOSPIJEÆA KOLATERALA").append(";");
		buffer.append("VALUTA").append(";");	
		buffer.append("EXECUTION VALUE").append(";");
		buffer.append("EXECUTION VALUE EUR").append(";");
		buffer.append("B2 ELIGIBILITY INDICATOR").append(";");
	    buffer.append("B2 IRB ELIGIBILITY INDICATOR").append(";");
		buffer.append("DATUM UPISA HIPOTEKE").append(";");
		buffer.append("DATUM DOSPIJEÆA HIPOTEKE").append("\n");	

		return buffer.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("2257097003"));// id s razvoja
        bp.setArgs(args);
        new BO230().run(bp);
	}

}
