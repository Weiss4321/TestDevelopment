//created 2012.09.05
package hr.vestigo.modules.collateral.batch.bo74;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo74.BO741.IteratorData;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Obrada za ažuriranje liste slobodnih partija iz okvira
 * @author hradnp
 */
public class BO740 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo74/BO740.java,v 1.2 2012/09/11 10:46:54 hradnp Exp $";
    private BO741 bo741 = null;
      
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BatchContext bc = null;
   
    public BO740() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String executeBatch(BatchContext bc) throws Exception {
        
        bc.debug("BO740 pokrenut.");
        this.bo741 = new BO741(bc);
        this.bc = bc;
        
        // Ubacivanje eventa
        BigDecimal eve_id = bo741.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // Dohvat partija plasmana koje se ne nalaze u FRAME_ACC_EXCEPTION tablici
        IteratorData iter = bo741.getData();
        if(iter == null)return RemoteConstants.RET_CODE_ERROR;
        
        // Ubacivanje dohvaæenih partija plasmana u FRAME_ACC_EXCEPTION tablicu
        while(iter.next()){

            bc.beginTransaction();
            try{
                bo741.insertData(iter.cus_acc_id());
            }catch(Exception ex){
                bc.info("Message: " + ex.getMessage());
                bc.info("StackTrace: " + ex.getStackTrace());
                throw new Exception("Greška kod inserta u FRAME_ACC_EXCEPTION tablicu! ");
            }
            bc.commitTransaction();
            bc.debug("Ubaèeni podaci za broj partije: " + iter.cus_acc_no() + " i cus_acc_id = " + iter.cus_acc_id().toString());
        }
        
        bc.debug("Obrada završena.");
        return returnCode;
    }
    
    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("6272272704.0"));
        batchParameters.setArgs(args);
        new BO740().run(batchParameters);
    }
}

