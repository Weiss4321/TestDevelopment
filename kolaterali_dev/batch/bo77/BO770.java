package hr.vestigo.modules.collateral.batch.bo77;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo77.BO771.ColProcIterator;
import hr.vestigo.modules.collateral.batch.bo77.BO771.CollateralIterator;
import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * Kreiranje izvješæa za Group Collateral Management Project
 * @author hrakis
 */
public class BO770 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo77/BO770.java,v 1.9 2014/06/11 08:51:29 hrakis Exp $";
    
    private BatchContext bc;
    private BO771 bo771;
    
    private Date value_date;
    private boolean make_control_report = false;
    
    private YOYM0 mapping_revalor_limit;
 
    private final BigDecimal hundred = new BigDecimal("100.00");


    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo771 = new BO771(bc);

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // mapiranja
        this.mapping_revalor_limit = new YOYM0(bc, "revalor_limit", value_date);     // revalorizacijski rokovi
        
        // provjera da li je datum valute zadnji dan u mjesecu
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value_date);
        boolean isEndOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
        
        // provjera da li je datum valute zadnji dan u tjednu
        boolean isEndOfWeek = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        
        // provjera da li je datum valute kvartalni datum (31.3, 30.6, 30.9, 31.12)
        boolean isQuartal = false;
        if(calendar.get(Calendar.MONTH) == Calendar.MARCH && isEndOfMonth) isQuartal = true;
        else if(calendar.get(Calendar.MONTH) == Calendar.JUNE && isEndOfMonth) isQuartal = true;
        else if(calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER && isEndOfMonth) isQuartal = true;
        else if(calendar.get(Calendar.MONTH) == Calendar.DECEMBER && isEndOfMonth) isQuartal = true;
        
        boolean makeMonthlyReports = isEndOfMonth;                  // mjeseèni izvještaji se rade za zadnji dan u mjesecu
        boolean makeWeeklyReports = isEndOfWeek || isEndOfMonth;    // tjedni izvještaji se rade za zadnji dan u tjednu i za zadnji dan u mjesecu
         
        if (makeMonthlyReports)
        {
            // odredi prvi datum koji se uzima u obzir - 31.12. prethodne godine u odnosu na datum valute
            calendar.set(calendar.get(Calendar.YEAR) - 1, 12, 31);
            Date min_value_date = new Date(calendar.getTimeInMillis());
            
            // dohvati ID-eve mjeseènih obrada koje su napunile podatke
            ColProcIterator iterColProc = bo771.selectColProcIds(min_value_date, value_date, "GCM");
            TreeMap<Date, BigDecimal> colProcs = new TreeMap<Date, BigDecimal>();
            while (iterColProc.next())
            {
                colProcs.put(iterColProc.value_date(), iterColProc.col_pro_id());
                bc.debug("VALUE_DATE = " + iterColProc.value_date() + ", COL_PRO_ID = " + iterColProc.col_pro_id());
            }
            
            // provjeri je li izvršena mjeseèna obrada za datum valute
            if (colProcs.containsKey(value_date))
            {
                // kreiranje mjeseènih izvještaja
                BO772 bo772 = null;     // 1.1 Portfolio Overview
                BO773 bo773 = null;     // 1.2 Portfolio Development
                BO774 bo774 = null;     // 1.3 RE, OPC Overview
                BO775 bo775 = null;     // 1.4 RE, OPC Development
                BO776 bo776 = null;     // 1.5 RE Location and Concentration
                BO777 bo777 = null;     // 2.1 Timely Revaluation
                BO778 bo778 = null;     // 2.2 Overdue Revaluation
                BO779 bo779 = null;     // 2.3 Overdue Revaluation Receivables and Goods on Stock
                BO77D bo77D = null;     // 3.3 Coll Expiry Date
                BO77E bo77E = null;     // 3.5 Insurance Coverage
                BO77F bo77F = null;     // 4.1 Concentration Monitoring
                BO77G bo77G = null;     // 6.1 Discounts_Applied
                BO77H bo77H = null;     // Ukupno Co i Ret
                bc.debug("***************************** MJESECNI IZVJESTAJI *****************************");
                
                try
                {
                    bo772 = new BO772(bc, value_date);
                    bo773 = new BO773(bc, value_date, colProcs.keySet());
                    bo774 = new BO774(bc, value_date);
                    bo775 = new BO775(bc, value_date, colProcs.keySet());
                    bo776 = new BO776(bc, value_date);
                    bo777 = new BO777(bc, value_date);
                    bo778 = new BO778(bc, value_date);
                    bo779 = new BO779(bc, value_date);
                    bo77D = new BO77D(bc, value_date);
                    bo77E = new BO77E(bc, value_date);
                    bo77F = new BO77F(bc, value_date);
                    bo77G = new BO77G(bc, value_date);
                    if (make_control_report) bo77H = new BO77H(bc, value_date, true);
    
                    // proði kroz sve mjeseène obrade
                    for (Date date : colProcs.keySet())
                    {
                        BigDecimal col_pro_id = colProcs.get(date);
                        CollateralIterator iter = bo771.selectCollaterals(date, col_pro_id);  // dohvati podatke koje je pripremila mjeseèna obrada
                        while (iter.next())
                        {
                            CollateralData collateral = getCollateralFromIterator(iter);
                            bo771.selectCollSubtype(collateral);  // dohvati podvrstu kolaterala
                            collateral.placement_owner_rating = bo771.selectCustomerRating(collateral.placement_cus_id, date);  // dohvati rating vlasnika plasmana
                            setDueRevaluationDate(collateral);  // dohvati datum isteka revalorizacije kolaterala
                            
                            if (date.compareTo(value_date) == 0) bc.debug(collateral.toString());
                            
                            // predaj kolateral svim mjeseènim izvještajima na procesiranje
                            bo772.processCollateral(collateral, date);
                            bo773.processCollateral(collateral, date);
                            bo774.processCollateral(collateral, date);
                            bo775.processCollateral(collateral, date);
                            bo776.processCollateral(collateral, date);
                            bo777.processCollateral(collateral, date);
                            bo778.processCollateral(collateral, date);
                            bo779.processCollateral(collateral, date);
                            bo77D.processCollateral(collateral, date);
                            bo77E.processCollateral(collateral, date);
                            
                            if (isQuartal)
                            {
                                bo77F.processCollateral(collateral, date);
                                bo77G.processCollateral(collateral, date);
                            }
                            
                            if (bo77H != null) bo77H.processCollateral(collateral, date);
                        }
                    }
                    
                    // spremi generirane mjeseène izvještaje
                    bo772.createReport();
                    bo773.createReport();
                    bo774.createReport();
                    bo775.createReport();
                    bo776.createReport();
                    bo777.createReport();
                    bo778.createReport();
                    bo779.createReport();
                    bo77D.createReport();
                    bo77E.createReport();
                    
                    if (isQuartal)
                    {
                        bo77F.createReport();
                        bo77G.createReport();
                    }
                    
                    if (bo77H != null) bo77H.createReport();
                }
                catch(Exception ex)
                {
                    // ako se dogodila bilo kakva greška, poèisti privremene datoteke 
                    if (bo772 != null) bo772.dispose();
                    if (bo773 != null) bo773.dispose();
                    if (bo774 != null) bo774.dispose();
                    if (bo775 != null) bo775.dispose();
                    if (bo776 != null) bo776.dispose();
                    if (bo777 != null) bo777.dispose();
                    if (bo778 != null) bo778.dispose();
                    if (bo779 != null) bo779.dispose();
                    if (bo77D != null) bo77D.dispose();
                    if (bo77E != null) bo77E.dispose();
                    if (bo77F != null) bo77F.dispose();
                    if (bo77G != null) bo77G.dispose();
                    if (bo77H != null) bo77H.dispose();
                    throw ex;   // baci exception dalje
                }
            }
            else
            {
                bc.warning("Za datum " + value_date + " nije se izvrsila mjesecna obrada za punjenje podataka za GCM!", new String[]{});
            }
        }
        else
        {
            bc.info("Datum valute nije zadnji dan u mjesecu - ne rade se mjesecni izvjestaji.");
        }

        
        
        // kreiranje dnevnih i tjednih izvještaja
        bc.debug("***************************** DNEVNI I TJEDNI IZVJESTAJI *****************************");
        BigDecimal daily_col_pro_id = bo771.selectColProId(value_date, "GCD");   // ID dnevne obrade koja je napunila podatke za datum valute
        if (daily_col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("DAILY_COL_PRO_ID = " + daily_col_pro_id);
        
        BigDecimal week_before_col_pro_id = null;  // ID dnevne obrade koja je napunila podatke za datum prije 7 dana
        Date week_before_date = null;
        if (makeWeeklyReports)
        {
            calendar.setTime(value_date);
            calendar.add(Calendar.DATE, -7);
            week_before_date = new Date(calendar.getTimeInMillis());
            bc.debug("WEEK_BEFORE_DATE = " + week_before_date);
            week_before_col_pro_id = bo771.selectColProId(week_before_date, "GCD");
            bc.debug("WEEK_BEFORE_COL_PRO_ID = " + week_before_col_pro_id);
        }

        BO77A bo77A = null;     // 2.4 Overdue Revaluation Financial Collateral
        BO77B bo77B = null;     // 3.1 Main Value Changes
        BO77C bo77C = null;     // 3.2 Rating Down-Up
        BO77H bo77H = null;     // Ukupno Co i Ret

        try
        {
            bo77A = new BO77A(bc, value_date);
            
            if (makeWeeklyReports && week_before_col_pro_id != null)
            {
                bo77B = new BO77B(bc, value_date);
                bo77C = new BO77C(bc, value_date, week_before_date);
            }
            if (make_control_report)
            {
                bo77H = new BO77H(bc, value_date, false);
            }
    
            CollateralIterator iter = bo771.selectCollaterals(value_date, daily_col_pro_id);  // dohvati podatke koje je pripremila dnevna obrada
            while (iter.next()) 
            {
                CollateralData collateral = getCollateralFromIterator(iter);
                bo771.selectCollSubtype(collateral);  // dohvati podvrstu kolaterala
                collateral.placement_owner_rating = bo771.selectCustomerRating(collateral.placement_cus_id, value_date);   // dohvati rating vlasnika plasmana
                setDueRevaluationDate(collateral);  // dohvati datum isteka revalorizacije kolaterala
                
                if (makeWeeklyReports && week_before_col_pro_id != null)  // ako se rade tjedni izvještaji, napuni podatke o kolateralu koji su vrijedili prije 7 dana
                {
                    collateral.week_before_collateral = bo771.selectCollateralData(week_before_date, week_before_col_pro_id, collateral.col_hea_id);
                }
                
                bc.debug(collateral.toString());
                
                // predaj kolateral svim dnevnim i tjednim izvještajima na procesiranje
                bo77A.processCollateral(collateral, value_date);
                if (bo77B != null) bo77B.processCollateral(collateral, value_date);
                if (bo77C != null) bo77C.processCollateral(collateral, value_date);
                if (bo77H != null) bo77H.processCollateral(collateral, value_date);
            }
            // spremi generirane dnevne i tjedne izvještaje
            bo77A.createReport();
            if (bo77B != null) bo77B.createReport();
            if (bo77C != null) bo77C.createReport();
            if (bo77H != null) bo77H.createReport();
        }
        catch(Exception ex)
        {
            // ako se dogodila bilo kakva greška, poèisti privremene datoteke 
            if (bo77A != null) bo77A.dispose();
            if (bo77B != null) bo77B.dispose();
            if (bo77C != null) bo77C.dispose();
            if (bo77H != null) bo77H.dispose();
            throw ex;   // baci exception dalje
        }
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda zadanom kolateralu raèuna i postavlja datum isteka revalorizacije.
     * @param collateral objekt s podacima o kolateralu
     */
    public void setDueRevaluationDate(CollateralData collateral)
    {
        // dohvat revalorizacijskog roka preko mapiranja
        GcmTypeData type = mapping_revalor_limit.resolve(collateral.col_cat_id, collateral.col_typ_id, collateral.col_sub_id);
        if (type == null)
        {
            bc.debug("...Kolateral nema definiran revalorizacijski rok.");
            return;
        }

        // datum isteka revalorizacije = datum zadnje revalorizacije + revalorizacijski rok (iz mapiranja)
        collateral.due_revaluation_date = addDays(collateral.last_evaluation, Integer.parseInt(type.code));                   
    }

    
    /**
     * Metoda na temelju zadanog iteratora stvara objekt s podacima o kolateralu.
     * @param iter iterator s podacima o kolateralu
     * @return kreirani objekt
     */
    private CollateralData getCollateralFromIterator(CollateralIterator iter) throws Exception
    {
        CollateralData collateral = new CollateralData();
        
        collateral.col_hea_id = iter.col_hea_id();
        if (iter.col_num() != null) collateral.col_num = iter.col_num().trim();
        collateral.col_cat_id = iter.col_cat_id();
        collateral.col_cat_name = iter.col_cat_name();
        collateral.col_typ_id = iter.col_typ_id();
        collateral.col_typ_name = iter.col_typ_name();
        
        collateral.ponder = iter.ponder();
        collateral.real_ponder = iter.real_ponder();
        if (collateral.real_ponder != null) collateral.actual_discont = hundred.subtract(collateral.real_ponder);
        else if (collateral.ponder != null) collateral.actual_discont = hundred.subtract(collateral.ponder);
        
        collateral.ncv = iter.ncv();
        collateral.wcov = iter.wcov();
        collateral.wcv = iter.wcv();
        if (iter.cus_acc_no() != null) collateral.contract_acc_no = iter.cus_acc_no().trim();
        collateral.ltv = iter.ltv();
        collateral.insurance_sum = iter.insurance_sum();
        collateral.insurance_exp_date = iter.insurance_exp_date();
        collateral.coll_exp_date = iter.coll_exp_date();
        
        collateral.earliest_exp_date = iter.earliest_exp_date();
        collateral.latest_exp_date = iter.latest_exp_date();
        if (collateral.earliest_exp_date == null) collateral.earliest_exp_date = collateral.coll_exp_date;  // prema RTC 10954 - ako polje Earliest Expiry Date nije popunjeno, uzeti Coll Expiry Date
        if (collateral.latest_exp_date == null) collateral.latest_exp_date = collateral.coll_exp_date;      // prema RTC 10954 - ako polje Latest Expiry Date nije popunjeno, uzeti Coll Expiry Date
        
        collateral.b2_irb_eligibility = iter.b2_irb_elig();
        if (collateral.b2_irb_eligibility != null && "D".equals(collateral.b2_irb_eligibility)) collateral.b2_irb_eligibility = "Y";   // umjesto D, potrebno je spremiti Y
        
        collateral.premium_paid = iter.premium_paid();
        if (collateral.premium_paid != null && "D".equals(collateral.premium_paid)) collateral.premium_paid = "Y";   // umjesto D, potrebno je spremiti Y
        if (collateral.insurance_exp_date == null) collateral.premium_paid = null;  // prema RTC 6116, 18.12.2013. - polje Premium Paid mora biti prazno za kolaterale koji nemaju nikakvu policu osiguranja

        collateral.placement_cus_id = iter.pl_cus_id();
        if (iter.pl_owner_register_no() != null) collateral.placement_owner_register_no = iter.pl_owner_register_no().trim();
        if (iter.pl_owner_name() != null) collateral.placement_owner_name = iter.pl_owner_name().trim();
        collateral.placement_due_date = iter.pl_due_date();
        
        if (iter.relation_manager() != null) collateral.relationship_manag_name = iter.relation_manager().trim();
        collateral.last_evaluation = iter.last_evaluation();
        if (iter.latest_evaluator() != null) collateral.latest_evaluator = iter.latest_evaluator().trim();
        
        return collateral;
    }
    
    
    /**
     * Metoda koja uveæava zadani datum za odreðeni broj dana.
     * @param date Datum
     * @param days Broj dana
     * @return uveæani datum
     */
    private Date addDays(Date date, int days){

        if (date == null) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date retDate = new Date(calendar.getTimeInMillis());
        Date maxDate = Date.valueOf("9999-12-31");
        if (retDate.compareTo(maxDate) >= 0) return maxDate;
        else return retDate;
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
            event.put("eve_typ_id", new BigDecimal("6406082704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "GCM Collateral Overview");
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
     * Parametri se predaju u formatu: <code>bank_sign value_date make_control_report</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>value_date</dt><dd>Datum valute.</dd>
     *    <dt>make_control_report</dt><dd>Da li se radi kontrolni izvještaj (D ili N). Ovo je opcionalni parametar - ako se ne navede podrazumijeva se N.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters()
    {
        try
        {
            int numberOfParameters = bc.getArgs().length;
            if (numberOfParameters < 2 || numberOfParameters > 3) throw new Exception("Neispravan broj parametara!");

            bc.info("Parametri predani obradi:");
            bc.info("Oznaka banke = " + bc.getArg(0));
            bc.info("Datum valute = " + bc.getArg(1));
            if (numberOfParameters == 3) bc.info("Da li se radi kontrolni izvjestaj = " + bc.getArg(2));
            
            bc.userLog("Parametri predani obradi:");
            bc.userLog("Oznaka banke = " + bc.getArg(0));
            bc.userLog("Datum valute = " + bc.getArg(1));
            if (numberOfParameters == 3) bc.userLog("Da li se radi kontrolni izvjestaj = " + bc.getArg(2));

            // oznaka banke
            if (!bc.getArg(0).equals("RB")) throw new Exception("Bank sign mora biti 'RB'!");
            bc.setBankSign(bc.getArg(0));
            
            // datum valute
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            value_date = new Date(dateFormat.parse(bc.getArg(1).trim()).getTime());
            
            // da li se radi kontrolni izvještaj
            if (numberOfParameters == 3 && "D".equalsIgnoreCase(bc.getArg(2))) make_control_report = true;

            return true;
        }
        catch (Exception ex)
        {
            bc.error("Neispravno zadani parametri! Parametri se predaju u formatu 'bank_sign value_date make_control_report'!", ex);
            return false;
        }
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6406079704"));
        bp.setArgs(args);
        new BO770().run(bp);
    }
}