/*
 * Created on 2006.12.08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo04;

import java.math.BigDecimal;


import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.common.yoy9.*;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO040 extends Batch{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo04/BO040.java,v 1.10 2007/08/27 09:28:05 hrazst Exp $";
	
	/**
	 * Batch za knjiženje do sada unešenih kolaterala.
	 */
		
	private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BO041 bo041 = null;
	private BatchContext bc = null;
	
	public BO040() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String executeBatch(BatchContext batchContext) throws Exception {
		
		this.bc = batchContext;
		writeUserLog("******************** BO040.executeBatch() started *********************");
		 
		long pocetakIzvodenja=0;
		long krajIzvodenja=0;		

		this.bo041 =new BO041(bc);
		
		pocetakIzvodenja=System.currentTimeMillis();
		
		if (bc.getArgs().length!=1){
			writeUserLog("Neispravan broj argumenata!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		if (bc.getArg(0).equals("RB")==false){
			writeUserLog("Bank sign mora biti 'RB'!");
			return RemoteConstants.RET_CODE_ERROR;
		}
		
		try{
			startBatch();
		}catch(Exception e){
    		writeUserLog ( "METODA -> startBatch : Error = " + e.toString());
    		returnCode=RemoteConstants.RET_CODE_ERROR;
		}
		return returnCode;
	}
	
	
	public void startBatch() throws Exception{
		
		BO041.iteratorCollHead iteratorDataCollHead=null;		
		YOY90 yoy90=null;
		
		try{
			iteratorDataCollHead=bo041.fatchDataCollHead();	
			while(iteratorDataCollHead.next()){
				
			    yoy90=new YOY90(bc);
			    try {
			        yoy90.CollPosting(iteratorDataCollHead.col_hea_id(),false);    
                }catch (Exception e) {
                    writeUserLog("Greska pri obradi kolaterala sa col_hea_id=" + iteratorDataCollHead.col_hea_id() + "\n Greska: " + e);
                    writeUserLog("\n");
                    bc.rollbackTransaction();
                }
                bc.commitTransaction();		    
			}
			try {
			    iteratorDataCollHead.close();
			} catch (Exception ignored) { }
		}catch(Exception e){
			throw e;
		}		
	}
	
	private void writeUserLog(String tekst) throws Exception{
		try {
			bc.userLog(tekst);
		} catch (Exception e){
			throw e;
		}
	}
	
	public static void main(String[] args) {
		BatchParameters batchParameters = new BatchParameters(new BigDecimal("1679521003.0"));
        batchParameters.setArgs(args);
        new BO040().run(batchParameters);
	}
}
