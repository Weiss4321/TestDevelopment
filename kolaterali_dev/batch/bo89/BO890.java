package hr.vestigo.modules.collateral.batch.bo89;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo89.BO891.CollIterator;
import hr.vestigo.modules.collateral.common.yoyH.YOYH0;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;


/**
 * Inicijalno punjenje podataka za historizaciju kolaterala
 * @author hrakis
 */
public class BO890 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo89/BO890.java,v 1.1 2014/12/16 12:36:14 hrakis Exp $";
    
    private BatchContext bc;
    private BO891 bo891;
    private YOYH0 yoyH0;

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo891 = new BO891(bc);
        this.yoyH0 = new YOYH0(bc);

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;

        bc.beginTransaction();

        // ažuriranje šifre kolaterala za postojeæe zapise
        bo891.updateColNum();

        // ažuriranje vremena promjene za postojeæe zapise
        bo891.updateRecordingTime();

        // dohvat svih kolaterala koji ulaze u historizaciju
        int count = 0;
        CollIterator iter = bo891.selectCollaterals();
        while (iter.next())
        {
            info("Punim povijest promjena za kolateral " + iter.col_num().trim());
            bc.startStopWatch("yoyH0.historize");
            yoyH0.historize(iter.col_hea_id());  // napuni povijest promjena za kolateral
            bc.stopStopWatch("yoyH0.historize");
            count++;
        }

        bc.commitTransaction();
        
        info("Inicijalno punjenje zavrseno. Obradjeno " + count + " kolaterala.");

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }


    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO890.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("7631155704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Inicijalno punjenje podataka za historizaciju kolaterala");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO890.insertIntoEvent");
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
            if (bc.getArgs().length < parameterNames.length - 1 || bc.getArgs().length > parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + (parameterNames.length - 1) + " ili " + parameterNames.length + "!", null);  
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
        BatchParameters bp = new BatchParameters(new BigDecimal("7631152704"));
        bp.setArgs(args);
        new BO890().run(bp);
    }
}