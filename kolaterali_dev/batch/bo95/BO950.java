package hr.vestigo.modules.collateral.batch.bo95;
        
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;

import hr.vestigo.modules.collateral.common.yoy6.*;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
 * Ažuriranje GCTC podataka.
 */
public class BO950 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo95/BO950.java,v 1.4 2016/12/05 14:39:19 hrakis Exp $";

    private BO951 bo951;
    private String returnCode;
    private BatchContext bc;


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        bo951 = new BO951(bc);
        returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
        int totalCount = 0;
        ArrayList<String> updatedColls = new ArrayList<String>();
        
        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat kolaterala
        BO951.CollateralIterator iter = bo951.selectCollaterals();
        YOY64 yoy64 = new YOY64(bc);
        
        while (iter.next())
        {
            BO95Data collData = new BO95Data();
            collData.col_hea_id = iter.col_hea_id();
            collData.col_num = iter.col_num();
            collData.col_cat_id = iter.col_cat_id();
            collData.col_type_id = iter.col_type_id();
            collData.col_sub_id = iter.col_sub_id();
            collData.col_gro_id = iter.col_gro_id();
            collData.gctc_id = iter.gctc_id();
            collData.endorsement_type_id = iter.endorsement_type_id();
            collData.object_type_id = iter.object_type_id();
            collData.property_type_id = iter.property_type_id();
            bc.debug(collData.toString());
            totalCount++;
                         
            GCTCData gctcData = yoy64.getGCTCData(collData.col_cat_id, collData.col_type_id, collData.col_sub_id, collData.col_gro_id);
            if (gctcData == null) continue;
            bc.debug("gctc_id=" + gctcData.gctc_id + ", endorsement_type_id=" + gctcData.endorsement_type_id + ", object_type_id=" + gctcData.object_type_id + ", property_type_id=" + gctcData.property_type_id);
            
            if (!isEqual(gctcData.gctc_id, collData.gctc_id) || !isEqual(gctcData.endorsement_type_id, collData.endorsement_type_id) || !isEqual(gctcData.object_type_id, collData.object_type_id) || !isEqual(gctcData.property_type_id, collData.property_type_id))
            {
                try
                {
                    bc.debug("-> update");
                    bc.beginTransaction();
                    bo951.updateCollHead(gctcData, collData.col_hea_id, collData.col_num);
                    bc.commitTransaction();
                    updatedColls.add(collData.col_num);
                }
                catch (Exception ex)
                {
                    error("Neuspjesno azuriranje GCTC podataka za kolateral " + collData.col_num + "!", ex);
                    bc.rollbackTransaction();
                    returnCode = RemoteConstants.RET_CODE_WARNING;
                }
            }
        }
        
        info("Ukupno obradjeno " + totalCount + " kolaterala.");
        if (updatedColls.size() > 0)
        {
            info("GCTC podaci su azurirani za sljedece kolaterale:");
            for (String col_num : updatedColls) info(col_num);
            info("GCTC podaci su azurirani za ukupno " + updatedColls.size() + " kolaterala.");
        }
        else
        {
            info("GCTC podaci nisu azurirani za nijedan kolateral.");
        }
        
        return returnCode;
    }
    
    
    private boolean isEqual(BigDecimal b1, BigDecimal b2)
    {
        return (b1 == null && b2 == null) || (b1 != null && b1.equals(b2));
    }
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO950.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("7592507704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za azuriranje GCTC podataka kolaterala");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO950.insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju pocetka izvodjenja obrade!", ex);
            throw ex;
        }
    }

    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length != parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + parameterNames.length + "!", null);  
                return false;
            }
            
            // provjeri oznaku banke
            String bank_sign = bc.getArg(0);
            if (!bank_sign.equals("RB"))
            {
                error("Oznaka banke mora biti RB!", null);
                return false;
            }
            bc.setBankSign("RB");
            
            return true;
        }
        catch (Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
        }
    }
    
    private void error(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }

    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }
    

    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("7592506704"));
        bp.setArgs(args);
        new BO950().run(bp);
    }
}