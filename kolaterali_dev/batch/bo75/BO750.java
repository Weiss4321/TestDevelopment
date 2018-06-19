package hr.vestigo.modules.collateral.batch.bo75;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo75.BO751.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo75.BO751.MortgagePlacementIterator;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.financial.common.yfzD.YFZD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Obrada za izraèun polja RVRD i RVOD
 * @author hrakis
 */
public class BO750 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo75/BO750.java,v 1.6 2017/06/29 08:41:03 hrakis Exp $";
    
    private BatchContext bc;
    private BO751 bo751;
    
    private Date value_date;
    private DecimalFormat decimalFormat;
    private YRXX0 yrxx0;
    private BigDecimal domestic_cur_id;
    private String domestic_code_char;
    private Date min_history_date; 
    //private BigDecimal col_pro_id;
    private final BigDecimal zero = new BigDecimal("0.00");
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo751 = new BO751(bc);
        this.yrxx0 = new YRXX0(bc);
        decimalFormat = new DecimalFormat("###,##0.00");
        
        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // odreðivanje datuma za koji se vrši izraèun - ako nije zadnji dan u mjesecu, uzima se zadnji dan u prethodnom mjesecu
        Date currentDate = new Date(bc.getExecStartTime().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        boolean isEndOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
        if (!isEndOfMonth)
        {
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        value_date = new Date(calendar.getTimeInMillis());
        bc.info("DATUM ZA KOJI SE VRSI IZRACUN = " + value_date);
        
        // dohvat ID-a obrade izraèuna pokrivenosti
        /*col_pro_id = bo751.selectColProId(value_date);
        bc.debug("COL_PRO_ID = " + col_pro_id);
        if (col_pro_id == null)
        {
            bc.error("Nije izvrsen izracun pokrivenosti za datum " + value_date + "!", new String[]{});
            return RemoteConstants.RET_CODE_ERROR;
        }*/
        
        // dohvat domaæe valute
        YFZD0 yfzd0 = new YFZD0(bc);
        domestic_cur_id = yfzd0.getDomCurId();
        domestic_code_char = yfzd0.getDomCurCodeChar();
        
        // dohvat minimalnog datuma za koji postoje podaci u arhivi
        min_history_date = bo751.selectMinHistoryDate();
        
        // dohvati kolaterale koji ulaze u izraèun i uèitaj sve potrebne podatke
        ArrayList<CollateralData> collaterals = new ArrayList<CollateralData>();
        CollateralIterator iter = bo751.selectCollaterals(value_date);
        while (iter.next())
        {
            CollateralData collateral = loadCollateral(iter.col_hea_id(), value_date, true);
            if (collateral != null && collateral.mortgages.size() > 0) collaterals.add(collateral);
        }
        iter.close();
        
        // ispis teèajnih lista
        bc.debug("TECAJNE LISTE KORISTENE U IZRACUNU:");
        bc.debug(yrxx0.cacheToString());
        
        // izraèun polja RVRD i RVOD
        bc.info("IZRACUN:");
        for (CollateralData collateral : collaterals)
        {
            bc.info("krece izracun za " + collateral.col_num.trim());
            calculateFields(collateral);
            bc.info(collateralToString(collateral));
        }
        
        // zapisivanje izraèunatih vrijednosti u bazu podataka
        bc.beginTransaction();
        for (CollateralData collateral : collaterals)
        {
            updateFields(collateral);
        }
        bc.commitTransaction();
       
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
   /**
    * Metoda za zadani kolateral uèitava sve potrebne podatke za izraèun u memorijsku strukturu.
    * Memorijska struktura bi trebala slièiti na ovako nešto: 
    * <pre>
    *             / oH1  
    *     H1 - oK1
    *    /        \ oH2
    *  K1      
    *    \        / oH1
    *     H2 - oK1  
    *             \ oH2      
    * </pre>
    * oK i oH predstavljaju kolateral i hipoteke iz povijesti
    * @param col_hea_id ID kolaterala
    * @param value_date Datum valute
    * @boolean load_old_data oznaka da li da se uèitavaju povijesni podaci
    * @return objekt sa svim podacima potrebnim za izraèun
    */
    private CollateralData loadCollateral(BigDecimal col_hea_id, Date value_date, boolean load_old_data) throws Exception
    {
        // dohvati osnovne podatke o kolateralu
        CollateralData collateral = bo751.selectCollateralData(col_hea_id, value_date);
        if (collateral == null) return null;
        collateral.value = yrxx0.exchange(collateral.real_est_nomi_valu, collateral.real_est_nm_cur_id, domestic_cur_id, value_date);
        
        // dohvati sve hipoteke kolaterala zajedno s podacima o vezanom plasmanu (za jednu hipoteku može biti više plasmana - uzima se samo jedan plasman - prednost ima plasman s partijom xxx-50-xxxxxx i manjim datumom korištenja plasmana) 
        MortgagePlacementIterator iterMortgage = bo751.selectMortgagesAndPlacements(collateral.col_hea_id, value_date);
        while (iterMortgage.next())
        {
            MortgageData mortgage = getMortgageFromIterator(iterMortgage, value_date);
            MortgageData existingMortgage = collateral.getMortgageById(mortgage.coll_hf_prior_id);
            if (existingMortgage == null || (mortgage.isRBA() && isAcceptableCusAccNo(mortgage.cus_acc_no) && !isAcceptableCusAccNo(existingMortgage.cus_acc_no)))
            {
                collateral.mortgages.put(mortgage.hf_priority, mortgage);
            }
        }
        iterMortgage.close();
        
        // dohvati izloženosti plasmana vezanih na hipoteke kolaterala
        /*for (MortgageData mortgage : collateral.mortgages.values())
        {
            if (mortgage.isRBA()) mortgage.exp_balance_hrk = bo751.selectPlacementExposure(mortgage.cus_acc_id, col_pro_id, yrxx0, domestic_cur_id, value_date);
        }*/
        
        // naði zadnju RBA hipoteku
        for (MortgageData mortgage : collateral.mortgages.values())
        {
            if (mortgage.isRBA()) collateral.lastRbaMortgage = mortgage;
        }
        
        // sumiraj hipoteke
        for (MortgageData mortgage : collateral.mortgages.values())
        {
            if (mortgage.isRBA())
            {
                collateral.totalRbaMortgagesAmount = collateral.totalRbaMortgagesAmount.add(mortgage.amount);
                if (mortgage == collateral.lastRbaMortgage) break;
            }
            else
            {
                collateral.totalOtherMortgagesAmount = collateral.totalOtherMortgagesAmount.add(mortgage.amount);
            }
        }
        
        // uèitaj povijesne podatke za svaku hipoteku ako je tako naznaèeno
        if (load_old_data)
        {
            for (MortgageData mortgage : collateral.mortgages.values())
            {
                // ako je datum korištenja plasmana manji od minimalnog datuma iz arhive, nema smisla pokušavati uèitati povijesne podatke
                if (mortgage.isRBA() && min_history_date != null && mortgage.usage_date != null && mortgage.usage_date.compareTo(min_history_date) >= 0)
                {
                    mortgage.oldCollateral = loadCollateral(collateral.col_hea_id, mortgage.usage_date, false);
                }
            }
        }
        
        return collateral;
    }

    
    /**
     * Metoda koja kreira objekt s hipotekom iz iteratora.
     * @param iter Iterator s podacima o hipoteci
     * @param date Datum za koji vrijede podaci
     * @return objekt s hipotekom
     */
    private MortgageData getMortgageFromIterator(MortgagePlacementIterator iterMortgage, Date date) throws Exception
    {
        MortgageData mortgage = new MortgageData();
        mortgage.coll_hf_prior_id = iterMortgage.coll_hf_prior_id();
        mortgage.loan_ben_id = iterMortgage.loan_ben_id();
        mortgage.hf_priority = Integer.valueOf(iterMortgage.hf_priority());
        mortgage.hf_own_cus_id = iterMortgage.hf_own_cus_id();
        mortgage.amount_ref = iterMortgage.amount_ref();
        mortgage.cur_id_ref = iterMortgage.cur_id_ref();
        mortgage.cur_code_char = iterMortgage.cur_code_char();
        mortgage.amount = yrxx0.exchange(mortgage.amount_ref, mortgage.cur_id_ref, domestic_cur_id, date);
        mortgage.rvrd_current = iterMortgage.rvrd();
        mortgage.rvod_current = iterMortgage.rvod();
        mortgage.cus_acc_id = iterMortgage.cus_acc_id();
        mortgage.cus_acc_no = iterMortgage.cus_acc_no();
        mortgage.usage_date = iterMortgage.usage_date();
        return mortgage;
    }
    
    
    /**
     * Metoda koja vraæa da li je zadana partija plasmana u formatu xxx-50-xxxxxx.
     * @param cus_acc_no Partija plasmana
     */
    private boolean isAcceptableCusAccNo(String cus_acc_no)
    {
        return cus_acc_no != null && cus_acc_no.length() > 8 && cus_acc_no.substring(3, 7).equals("-50-");
    }
    
    
    /**
    * Metoda koja vrši izraèun polja RVRD i RVOD za hipoteke zadanog kolaterala. 
    * @param collateral objekt s podacima o kolateralu
    */
    private void calculateFields(CollateralData collateral)
    {
        bc.startStopWatch("calculateFields");
     
        for (MortgageData mortgage : collateral.mortgages.values())
        {
            // izraèun se vrši samo za RBA hipoteke koje imaju plasman
            if (!mortgage.isRBA() || mortgage.loan_ben_id == null || collateral.totalRbaMortgagesAmount.compareTo(zero) <= 0) continue;
            
            // izraèunaj RVRD
            mortgage.rvrd_new = collateral.value.subtract(collateral.totalOtherMortgagesAmount).multiply(mortgage.amount).divide(collateral.totalRbaMortgagesAmount, 2, RoundingMode.HALF_UP).max(zero);
            
            // izraèunaj RVOD - samo ako postoje povijesni podaci i ako vrijednost veæ nije izraèunata
            if (mortgage.oldCollateral != null && mortgage.rvod_current == null)
            {
                CollateralData oldCollateral = mortgage.oldCollateral;
                MortgageData oldMortgage = oldCollateral.getMortgageById(mortgage.coll_hf_prior_id);
                
                if (oldMortgage == null || oldCollateral.totalRbaMortgagesAmount.compareTo(zero) <= 0)
                {
                    mortgage.oldCollateral = null;
                    continue;
                }
                mortgage.rvod_new = oldCollateral.value.subtract(oldCollateral.totalOtherMortgagesAmount).multiply(oldMortgage.amount).divide(oldCollateral.totalRbaMortgagesAmount, 2, RoundingMode.HALF_UP).max(zero);
            }
        }
     
        bc.stopStopWatch("calculateFields");
    }
    
    
    /**
    * Metoda koja zapisuje u bazu podataka izraèunate vrijednosti polja RVOD i RVRD za hipoteke zadanog kolaterala.
    * @param collateral objekt s podacima o kolateralu
    */
    private void updateFields(CollateralData collateral) throws Exception
    {
        for (MortgageData mortgage : collateral.mortgages.values())
        {
            bo751.updateLoanBeneficiary(mortgage, domestic_cur_id);
        }
    }
    

    
    /**
     * Metoda formira tekst koji sadrži sve potrebne podatke izraèuna polja RVRD i RVOD za hipoteke zadanog kolaterala. 
     * @param collateral kolateral
     * @return formirani tekst
     */
    private String collateralToString(CollateralData collateral)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(collateralToString(collateral, 0, true));
        
        for (MortgageData mortgage : collateral.mortgages.values())
        {
            buffer.append(mortgageToString(mortgage, 1, true));
            
            if (!mortgage.isRBA() || mortgage.loan_ben_id == null) continue;
            
            buffer.append("\n").append(indentToString(2)).append("RVRD = ");
            buffer.append(bigDecimalToString(mortgage.rvrd_new)).append(" ").append(domestic_code_char);
            
            buffer.append("\n").append(indentToString(2)).append("RVOD ");
            if (mortgage.rvod_current != null)
            {
                buffer.append("= ").append(bigDecimalToString(mortgage.rvod_current)).append(" ").append(domestic_code_char);
                buffer.append(" (vec izracunato)");
            }
            else if (mortgage.oldCollateral == null)
            {
                buffer.append("- nema podataka za ").append(mortgage.usage_date);
            }
            else
            {
                buffer.append("= ").append(bigDecimalToString(mortgage.rvod_new)).append(" ").append(domestic_code_char);
                buffer.append(" - izracun za dan ").append(mortgage.usage_date).append(":");
                
                buffer.append(collateralToString(mortgage.oldCollateral, 3, false));
                
                for (MortgageData oldMortgage : mortgage.oldCollateral.mortgages.values())
                {
                    buffer.append(mortgageToString(oldMortgage, 4, false));
                }
            }
        }
        
        buffer.append("\n\n");
        return buffer.toString();
    }
    
    /**
     * Metoda formira tekst koji opisuje zadani kolateral.
     * @param collateral kolateral
     * @param indent broj kolona za koji je uvuèen tekst
     * @param capitalize da li je tekst formiran velikim slovima
     * @return formirani tekst
     */
    private String collateralToString(CollateralData collateral, int indent, boolean capitalize)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n").append(indentToString(indent));
        buffer.append(capitalize ? "KOLATERAL" : "Kolateral").append(" ");
        buffer.append(collateral.col_num.trim()).append(" - ");                                 // šifra kolaterala
        buffer.append(bigDecimalToString(collateral.real_est_nomi_valu)).append(" ");           // vrijednost kolaterala
        buffer.append(collateral.real_est_nm_cur_code_char).append(" = ");                      // valuta vrijednosti
        buffer.append(bigDecimalToString(collateral.value)).append(" ");                        // vrijednost izražena u domaæoj valuti
        buffer.append(domestic_code_char).append(" - ");                                        // domaæa valuta
        buffer.append("RBA hipoteke = ");
        buffer.append(bigDecimalToString(collateral.totalRbaMortgagesAmount)).append(" ");      // RBA hipoteke
        buffer.append(domestic_code_char).append(", ");                                         // domaæa valuta
        buffer.append("ostale hipoteke = ");
        buffer.append(bigDecimalToString(collateral.totalOtherMortgagesAmount)).append(" ");    // ostale hipoteke
        buffer.append(domestic_code_char);                                                      // domaæa valuta
        return buffer.toString();
    }
    
    /**
     * Metoda formira tekst koji opisuje zadanu hipoteku.
     * @param mortgage hipoteka
     * @param indent broj kolona za koji je uvuèen tekst
     * @param capitalize da li je tekst formiran velikim slovima
     * @return formirani tekst
     */
    private String mortgageToString(MortgageData mortgage, int indent, boolean capitalize)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n").append(indentToString(indent));
        buffer.append(capitalize ? "HIPOTEKA" : "Hipoteka").append(" ");
        buffer.append(mortgage.hf_priority).append(" - ");                      // prioritet hipoteke
        buffer.append(mortgage.isRBA() ? "RBA" : "xxx").append(" - ");          // je li hipoteka RBA
        buffer.append(bigDecimalToString(mortgage.amount_ref)).append(" ");     // iznos hipoteke
        buffer.append(mortgage.cur_code_char).append(" = ");                    // valuta iznosa hipoteke
        buffer.append(bigDecimalToString(mortgage.amount)).append(" ");         // iznos hipoteke u domaæoj valuti
        buffer.append(domestic_code_char);                                      // domaæa valuta
        if (mortgage.isRBA()) buffer.append(" -> plasman ").append(mortgage.cus_acc_no).append(" (dat.koristenja=").append(mortgage.usage_date).append(")");  // plasman na koji je vezana hipoteka
        if (mortgage.exp_balance_hrk != null) buffer.append(" izlozenost = ").append(bigDecimalToString(mortgage.exp_balance_hrk)).append(" ").append(domestic_code_char);  // izloženost plasmana
        return buffer.toString();
    }
    
    
    /**
     * Metoda formira tekst koji se koristi za uvlaèenje drugog teksta.
     * @param count broj kolona uvlaèenja
     * @return formirani tekst
     */
    private String indentToString(int count)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count; i++) buffer.append("|  ");
        return buffer.toString();
    }
    
    /**
     * Metoda koja vraæa formatirani zapis zadanog broja.
     * @param number broj
     * @return formatirani zapis broja
     */
    private String bigDecimalToString(BigDecimal number)
    {
        if (number == null) return "";
        else return decimalFormat.format(number);
    }

    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6383616704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za izracun polja RVRD i RVOD");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade u tablicu EVENT!", ex);
            throw ex;
        }
    }

        
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters()
    {
        try
        {
            if (bc.getArgs().length != 1) throw new Exception("Neispravan broj parametara!");

            bc.info("Parametri predani obradi:");
            bc.info("Oznaka banke = " + bc.getArg(0));
            
            bc.userLog("Parametri predani obradi:");
            bc.userLog("Oznaka banke = " + bc.getArg(0));

            if (!bc.getArg(0).equals("RB")) throw new Exception("Bank sign mora biti 'RB'!");
            bc.setBankSign(bc.getArg(0));
        }
        catch(Exception ex)
        {
            bc.error("Neispravno zadani parametri! Parametri se predaju u formatu 'bank_sign'!", ex);
            return false;
        }
        return true;
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6383607704"));
        bp.setArgs(args);
        new BO750().run(bp);
    }
}