package hr.vestigo.modules.collateral.batch.bo79;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo79.BO791.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo79.BO791.PlacementIterator;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
 * Ažuriranje akumulirane vrijednosti polica osiguranja / garantnog iznosa garancija
 * @author hrakis
 */
public class BO790 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo79/BO790.java,v 1.4 2016/12/12 14:33:26 hrakis Exp $";
    
    private BatchContext bc;
    private BO791 bo791;
    private YRXX0 yrxx0;
    
    private boolean isInsurancePolicyBatch = false;
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal hundred = new BigDecimal("100.00");
    private final BigDecimal inspol_cat_id = new BigDecimal("616223");
    private final BigDecimal guar_cat_id = new BigDecimal("615223");

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo791 = new BO791(bc);
        this.yrxx0 = new YRXX0(bc);
        ArrayList<String> skippedGuarantees = new ArrayList<String>();
        
        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();
 
        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // inicijalizacija commona za knjiženje
        CollateralPosting collPosting = CollateralCommonFactory.getCollateralPosting(bc);
        
        // otvaranje izlazne datoteke
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String coreName = isInsurancePolicyBatch ? "AzuriranePolice" : "AzuriraneGarancije";
        String reportFileName = bc.getOutDir() + "/" + coreName + dateString + ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(reportFileName)), "Cp1250");
        streamWriter.write("\u0160ifra kolaterala;Iznos prije;Iznos poslije;Valuta\n");
        
        // dohvat kolaterala
        bc.beginTransaction();
        CollateralIterator iter = bo791.selectCollaterals();
        
        // izraèun akumulirane vrijednosti
        while (iter.next())
        {
            if ( (inspol_cat_id.equals(iter.col_cat_id()) && !isInsurancePolicyBatch) || (guar_cat_id.equals(iter.col_cat_id()) && isInsurancePolicyBatch) ) continue;
            
            bc.info("KOLATERAL " + iter.col_num().trim());

            // suma izloženosti svih aktivnih plasmana povezanih na kolateral
            BigDecimal placement_exposure_sum = zero;
            PlacementIterator iterPlacement = bo791.selectPlacements(iter.col_hea_id());
            while (iterPlacement.next())
            {
                BigDecimal exposure = yrxx0.exchange(iterPlacement.exposure_balance(), iterPlacement.exposure_cur_id(), iter.real_est_nm_cur_id(), iterPlacement.exposure_date());
                placement_exposure_sum = placement_exposure_sum.add(exposure);
                
                String exposure_cur_code_char = yrxx0.getExchangeRate(iterPlacement.exposure_date()).get(iterPlacement.exposure_cur_id()).code_char;
                bc.info(" -> PLASMAN " + iterPlacement.cus_acc_no() + ", izlozenost = " + iterPlacement.exposure_balance() + " " + exposure_cur_code_char + " = " + exposure + " " + iter.real_est_nm_cur_code_char() + " (datum izlozenosti = " + iterPlacement.exposure_date() + ")");
            }
            iterPlacement.close();
            bc.info(" -> suma izlozenosti = " + placement_exposure_sum + " " + iter.real_est_nm_cur_code_char());
            
            // izraèun nove vrijednosti
            BigDecimal new_value = null;
            boolean isSkipped = false;
            if (guar_cat_id.equals(iter.col_cat_id()))  // garancije
            {
                bc.info(" -> postotak definiran od strane garantora = " + iter.guarantor_perc() + "%, inicijalni iznos garancije = " + iter.init_guar_amount() + " " + iter.real_est_nm_cur_code_char());
                new_value = placement_exposure_sum.multiply(iter.guarantor_perc()).divide(hundred, 2, RoundingMode.HALF_UP);
                bc.info(" -> novi garantni iznos = " + new_value + " " + iter.real_est_nm_cur_code_char());
                
                if (new_value.compareTo(iter.init_guar_amount()) > 0)
                {
                    bc.info(" -> Garantni iznos se ne mijenja jer bi bio veci od inicijalnog iznosa garancije!");
                    skippedGuarantees.add(iter.col_num());
                    isSkipped = true;
                    new_value = iter.real_est_nomi_valu();
                }
            }
            else if (inspol_cat_id.equals(iter.col_cat_id()))  // police osiguranja
            {
                new_value = placement_exposure_sum;
                bc.info(" -> nova akumulirana vrijednost police = " + new_value + " " + iter.real_est_nm_cur_code_char());
            }
            
            // ažuriraj i proknjiži vrijednost kolaterala, te ga zapiši u izvještaj
            if (new_value != null)
            {
                if (!isSkipped) streamWriter.write(iter.col_num() + ";" + iter.real_est_nomi_valu() + ";" + new_value + ";" + iter.real_est_nm_cur_code_char() + "\n");
                bo791.updateCollHead(iter.col_hea_id(), iter.col_cat_id(), new_value, eve_id);
                collPosting.CollPosting(iter.col_hea_id(), false);
            }
        }
        iter.close();
        streamWriter.flush();
        streamWriter.close();
        bc.commitTransaction();
        
        // slanje izvještaja na mail
        String reportCode = isInsurancePolicyBatch ? "csvbo79i" : "csvbo79g";
        Vector attachments = new Vector();
        attachments.add(reportFileName);
        String message = "";
        if (isInsurancePolicyBatch)
        {
            message = "U prilogu je popis polica osiguranja kojima je a\u017Eurirana akumulirana vrijednost.";
        }
        else
        {
            message = "U prilogu je popis garancija kojima je a\u017Euriran garantni iznos.";
            if (skippedGuarantees.size() > 0)
            {
                String additionalMessage = "Garancije kojima nije a\u017Euriran garantni iznos jer bi vrijednost bila ve\u0107a od inicijalnog iznosa garancije: ";
                for (String guar_col_num : skippedGuarantees) additionalMessage += "," + guar_col_num;
                additionalMessage = additionalMessage.replaceFirst(",", "");
                message = additionalMessage + "\n\n\n" + message;
            }
        }
        YXY70.send(bc, reportCode, bc.getLogin(), attachments, message);
      
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    public BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6522088704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Azuriranje akumulirane vrijednosti polica osiguranja");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade u tablicu EVENT!", ex);
            throw ex;
        }
    }
        
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>coll_category</dt>Kategorija kolaterala koja je obuhvaæena obradom. Moguæe vrijednosti su POLICE i GARANCIJE. 
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Kategorija kolaterala" };

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
            if (!bank_sign.equals("RB") && !bank_sign.equals("RB#COL"))
            {
                error("Oznaka banke mora biti RB ili RB#COL!", null);
                return false;
            }
            bc.setBankSign("RB");
            
            // dohvati kategoriju kolaterala
            String coll_category = bc.getArg(1).trim().toUpperCase();
            if ("POLICE".equalsIgnoreCase(coll_category)) 
            {
                isInsurancePolicyBatch = true;
            }
            else if ("GARANCIJE".equalsIgnoreCase(coll_category))
            {
                isInsurancePolicyBatch = false;
            }
            else
            {
                error("Kategorija kolaterala mora biti POLICE ili GARANCIJE!", null);
                return false;
            }
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6522085704"));
        bp.setArgs(args);
        new BO790().run(bp);
    }
}