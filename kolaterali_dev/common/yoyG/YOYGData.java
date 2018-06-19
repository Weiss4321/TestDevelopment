package hr.vestigo.modules.collateral.common.yoyG;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

/**
 * Data objekt koji sadrži podatke potrebne za odreðivanje prihvatljivosti kolaterala.
 * @author hrakis
 */
public class YOYGData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyG/YOYGData.java,v 1.14 2014/11/17 12:05:42 hrakis Exp $";
    
    public YOYGData(BigDecimal col_hea_id)
    {
        this.col_hea_id = col_hea_id;
    }


    // podaci o kolateralu
    
    /** ID kolaterala. */
    protected BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    protected String col_num;
    
    /** Kategorija kolaterala. */
    protected BigDecimal col_cat_id;
    
    /** Vrsta kolaterala. */
    protected BigDecimal col_type_id;
    
    /** Podvrsta kolaterala. */
    protected BigDecimal col_subtype_id;
    
    /** RBA prihvatljivost. */
    protected String rba_eligibility;
    
    /** Predana dokumentacija. */
    protected String com_doc;
    
    /** Mišljenje pravne službe. */
    protected String law_eligibility;
    
    /** Upisana hipoteka. */
    protected String rec_lop;

    /** CRM mišljenje. */
    protected String real_est_spec_stat;
    
    /** CRM HNB mišljenje. */
    protected String crm_hnb;
    
    /** Da li je kolateral osiguran i polica aktivna. */
    protected String inspol_ind_aktivna;
    
    /** Da li je garancija prihvatljiva na prvi poziv. */
    protected String first_call;

    /** ID komitenta izdavatelja garancije. */
    protected BigDecimal guar_issuer_id;
    
    /** B2 asset class izdavatelja garancije. */
    protected String guar_issuer_b2ac;
    
    /** Država sjedišta izdavatelja garancije. */
    protected BigDecimal guar_cou_id;
    
    /** Da li garancija ima pozitivno mišljenje analitièara. */
    protected String pos_analyst_opinion;
    
    /** Datum do kada vrijedi garancija/depozit. */
    protected Date date_until;
    
    /** Datum do kada vrijedi garancija/depozit (uzima se ako je date_until prazan). */
    protected Date date_until_fallback;
    
    /** Datum dospijeæa plasmana. */
    protected Date due_date;

    /** Uporabna dozvola. */
    protected String build_perm_ind;
     
    /** Graðevinska dozvola. */
    protected String build_perm;
    
    /** Namjena nekretnine. */
    protected BigDecimal purpose;
    
    /** Da li je vozilo kasko osigurano. */
    protected String veh_kasko;
    
    /** Tip komitenta (glavnog vlasnika kolaterala). */
    protected BigDecimal cus_typ_id;
    
    /** Broj nekretnina u vlasništvu vlasnika kolaterala. */
    protected String num_of_estate;
    
    /** Vrsta potraživanja cesije. */
    protected BigDecimal ces_typ_id;
    
    /** Da li je cesija radi naplate plasmana. */
    protected String ces_nap_pls;
    
    /** Opis vrste procjene. */
    protected String real_est_nomi_desc;
    
    /** Šifra opisa vrste procjene (iz tablice SYSTEM_CODE_VALUE). */
    protected BigDecimal real_est_nomi_desc_id;
    
    /** Rating garancije. */ 
    protected String rating;
    
    /** Moody's LT rating izdavatelja garancije. */
    protected String mlt_rating;

    /** Nominalna vrijednost kolaterala. */
    protected BigDecimal ncv_amount;

    /** Valuta nominalne vrijednosti kolaterala. */
    protected BigDecimal ncv_cur_id;
    
    /** Datum nominalne vrijednosti kolaterala. */
    protected Date ncv_date;

    /** Omjer izloženosti svih aktivnih plasmana i NCV kolaterala. */
    protected BigDecimal exp_coll_ratio;
    
    /** Datum zadnje promjene cijene dionice. */
    protected Date price_date;
    
    /** Oznaka referentnog tržišta na kojem se trguje. */
    protected String market_code;
    
    /** Datum do kada je plaæena premija police osiguranja. */
    protected Date ip_paid_until;
    
    
    // stare prihvatljivosti
    
    /** Stara vrijednost CRM HNB mišljenja. */
    protected String crm_hnb_old;
    
    /** Stara vrijednost HNB prihvatljivosti. */
    protected String hnb_eligibility_old;
    
    /** Stara vrijednost B2 Stand prihvatljivosti. */
    protected String b2stand_eligibility_old;
    
    /** Stara vrijednost B2 IRB prihvatljivosti. */
    protected String b2irb_eligibility_old;
    
    /** Stara vrijednost ND prihvatljivosti. */
    protected String nd_eligibility_old;


    // metode koje provjeravaju ispunjenost uvjeta za prihvatljivosti
    
    /** Provjerava da li je kolateral RBA prihvatljiv. */
    public boolean rbaPrihvatljivost()
    {
        return "D".equals(rba_eligibility);
    }
    /** Provjerava da li je predana dokumentacija. */
    public boolean predanaDokumentacija()
    {
        return "D".equals(com_doc);
    }
    /** Provjerava da li je mišljenje pravne službe pozitivno. */
    public boolean misljenjePravneSluzbe()
    {
        return "D".equals(law_eligibility);
    }
    /** Provjerava da li je na kolateral upisana barem jedna važeæa RBA hipoteka. */
    public boolean upisanaHipoteka()
    {
        return "D".equals(rec_lop);
    }
    /** Provjerava da li je CRM mišljenje pozitivno. */
    public boolean crmMisljenje()
    {
        return "D".equals(real_est_spec_stat.trim());
    }
    /** Provjerava da li je CRM HNB mišljenje pozitivno. */ 
    public boolean crmHnbMisljenje()
    {
        return "D".equals(crm_hnb);
    }
    /** Provjerava da li je kolateral osiguran (polica aktivna i plaæena) */
    public boolean osigurano()
    {
        return "D".equals(inspol_ind_aktivna);
    }
    /** Provjerava da li je garancija na prvi poziv. */
    public boolean garancijaNaPrviPoziv()
    {
        return "D".equals(first_call);
    }
    /** Provjerava da li je garancija domaæa. */
    public boolean garancijaDomaca()
    {
        return new BigDecimal("999").equals(guar_cou_id);
    }
    /** Provjerava da li izdavatelj garancije ima popunjeno polje Rating. */
    public boolean garancijaRating()
    {  
        return (rating != null && rating.trim().length() > 0);
    }
    /** Provjerava da li izdavatelj garancije ima Moody's LT rating. */
    public boolean garancijaVanjskiRating()
    {  
        return (mlt_rating != null && mlt_rating.trim().length() > 0);
    }
    /** Provjerava da li nekretnina ima uporabnu dozvolu. */
    public boolean uporabnaDozvola()
    {
        return "D".equals(build_perm_ind);
    }
    /** Provjerava da li nekretnina ima graðevinsku dozvolu. */
    public boolean gradjevinskaDozvola()
    {
        return "D".equals(build_perm);
    }
    /** Provjerava da li je namjena nekretnine prihvatljiva. */ 
    public boolean namjenaNekretnine()
    {
        return new BigDecimal("20490472").equals(purpose) ||   // boravak
               new BigDecimal("20491902").equals(purpose);     // iznajmljivanje
    }
    /** Provjerava da li je vozilo kasko osigurano. */
    public boolean kaskoOsigurano()
    {
        return "D".equals(veh_kasko);
    }
    /** Provjerava da li je datum dospijeæa veæi od današnjeg datuma. */
    public boolean buduciDatumDospijeca()
    {
        Date dateUntil = date_until != null ? date_until : date_until_fallback;
        if(dateUntil == null) return false;
        return dateUntil.after(new Date(System.currentTimeMillis()));
    }
    /** Provjerava usklaðenost dospijeæa. */
    public boolean uskladjenostDospijeca()
    {
        if(due_date == null) return true;
        Date dateUntil = date_until != null ? date_until : date_until_fallback;
        if(dateUntil == null) return false;
        return (dateUntil.compareTo(due_date) >= 0);
    }
    /** Provjerava usklaðenost dospijeæa.
     * @param poljeKalendara Specificira što se na datumu poveæava. Za dane se predaje Calendar.DATE.
     * @param vrijednostUvecanja Za koliko se uveæava datum. 
     */
    public boolean uskladjenostDospijeca(int poljeKalendara, int vrijednostUvecanja)
    {
        if(due_date == null) return true;
        Calendar cal = Calendar.getInstance();
        cal.setTime(due_date);
        cal.add(poljeKalendara, vrijednostUvecanja);
        Date dueDate = new Date(cal.getTimeInMillis());
        Date dateUntil = date_until != null ? date_until : date_until_fallback;
        if(dateUntil == null) return false;
        return (dateUntil.compareTo(dueDate) >= 0);
    }
    /** Provjerava da li je glavni vlasnik kolaterala fizièka osoba. */
    public boolean vlasnikFizickaOsoba()
    {
        return new BigDecimal("1998").equals(cus_typ_id) ||     // strana fizièka osoba
               new BigDecimal("1999").equals(cus_typ_id);       // domaæa fizièka osoba
    }
    /** Provjerava da li je broj stambenih nekretnina u vlasništvu glavnog vlasnika kolaterala 1 ili 2. */
    public boolean brojNekretninaUVlasnistvu()
    {
        return "1".equals(num_of_estate) || "2".equals(num_of_estate) || "<1".equals(num_of_estate) || ">1".equals(num_of_estate);
    }
    /** Provjerava da li je cesija "sadašnja" ili "sadašnja ili buduæa". */
    public boolean vrstaPotrazivanjaCesije()
    {
        return new BigDecimal("3763282384").equals(ces_typ_id) || new BigDecimal("3763283814").equals(ces_typ_id);
    }
    /** Provjerava da li cesija nije radi naplate plasmana. */
    public boolean cesijaNijeRadiNaplatePlasmana()
    {
        return "N".equals(ces_nap_pls);
    }
    /** Provjerava da li je vrsta procjene prihvatljiva. */
    public boolean vrstaProcjene()
    {
        return new BigDecimal("808104444").equals(real_est_nomi_desc_id) ||     // elaborat o procjeni
               new BigDecimal("808107304").equals(real_est_nomi_desc_id);       // revizija elaborata o procjeni
    }
    /** Provjerava da li je omjer plasmana i NCV kolaterala unutar dopuštene razine. */
    public boolean jeLiPrihvatljivOmjerPlasmaniNcvKolaterala()
    {
        return (exp_coll_ratio.compareTo(new BigDecimal("0.75")) < 0);
    }
    /** Provjerava da li datum revalorizacije kolaterala nije stariji od zadanog vremenskog intervala. */
    public boolean datumRevalorizacijeNijeStarijiOd(int poljeKalendara, int vrijednost)
    {
        if(ncv_date == null) return true;
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        cal.setTime(now);
        cal.add(poljeKalendara, -vrijednost);
        Date targetDate = new Date(cal.getTimeInMillis());
        return (ncv_date.compareTo(targetDate) >= 0);
    }
    /** Provjerava da li dionica kotira na ZG burzi. */
    public boolean kotiraNaZgBurzi()
    {
        return "ZSE".equalsIgnoreCase(market_code);
    }
    /** Provjerava da li dionica ima objavljenu cijenu u zadanom periodu. */
    public boolean imaObjavljenuCijenuUZadnjih(int poljeKalendara, int vrijednost)
    {
        if(price_date == null) return false;
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        cal.setTime(now);
        cal.add(poljeKalendara, -vrijednost);
        Date targetDate = new Date(cal.getTimeInMillis());
        return (price_date.compareTo(targetDate) >= 0);
    }
    /** Provjerava da li garancija ima pozitivno mišljenje analitièara. */
    public boolean pozitivnoMisljenjeAnaliticara()
    {
        return "D".equalsIgnoreCase(pos_analyst_opinion);
    }
    /** Provjerava da li je datum do kada je plaæena premija police osiguranja veæi ili jednak datumu dospijeæa povezanog plasmana. */
    public boolean datumPremijaPlacenaDo()
    {
        if(due_date == null) return true;
        if(ip_paid_until == null) return false;
        return (ip_paid_until.compareTo(due_date) >= 0);
    }
    

    // metode koje provjeravaju pripadnost kolaterala nekoj kategoriji
    
    /** Provjerava da li je kolateral gotovinski depozit. */
    public boolean jeLiDepozit()
    {
        return new BigDecimal("612223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral dionica. */
    public boolean jeLiDionica()
    {
        return new BigDecimal("613223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral cesija. */
    public boolean jeLiCesija()
    {
        return new BigDecimal("614223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral garancija. */
    public boolean jeLiGarancija()
    {
        return new BigDecimal("615223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral polica osiguranja. */
    public boolean jeLiPolicaOsiguranja()
    {
        return new BigDecimal("616223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral mjenica. */
    public boolean jeLiMjenica()
    {
        return new BigDecimal("617223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral nekretnina. */
    public boolean jeLiNekretnina()
    {
        return new BigDecimal("618223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral obveznica. */
    public boolean jeLiObveznica()
    {
        return new BigDecimal("619223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral plovilo. */
    public boolean jeLiPlovilo()
    {
        return new BigDecimal("620223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral pokretnina. */
    public boolean jeLiPokretnina()
    {
        return new BigDecimal("621223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral udjel u fondu. */
    public boolean jeLiUdjelUFondu()
    {
        return new BigDecimal("622223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral vozilo. */
    public boolean jeLiVozilo()
    {
        return new BigDecimal("624223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral zadužnica. */
    public boolean jeLiZaduznica()
    {
        return new BigDecimal("625223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral gotovinski zaliha. */
    public boolean jeLiZaliha()
    {
        return new BigDecimal("626223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral zapis. */
    public boolean jeLiZapis()
    {
        return new BigDecimal("627223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral zlato, dijamant ili plemenita kovina. */
    public boolean jeLiZlato()
    {
        return new BigDecimal("628223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral udjel u poduzeæu. */
    public boolean jeLiUdjelUPoduzecu()
    {
        return new BigDecimal("629223").equals(col_cat_id);
    }
    /** Provjerava da li je kolateral vrijednosni papir. */
    public boolean jeLiVRP()
    {
        return (jeLiDionica() || jeLiObveznica() || jeLiUdjelUFondu() || jeLiUdjelUPoduzecu() || jeLiZapis());
    }
    

    // metode koje provjeravaju pripadnost kolaterala nekom tipu
    
    /** Provjerava da li izdavatelj garancije pripada B2 asset klasi 12, 30, 32, 53, 42, 31, 59, 35 ili 11. */
    public boolean jeLiGarancijaB2AC()
    {
        return "12".equalsIgnoreCase(guar_issuer_b2ac) ||
               "30".equalsIgnoreCase(guar_issuer_b2ac) ||
               "32".equalsIgnoreCase(guar_issuer_b2ac) ||
               "53".equalsIgnoreCase(guar_issuer_b2ac) ||
               "42".equalsIgnoreCase(guar_issuer_b2ac) ||
               "31".equalsIgnoreCase(guar_issuer_b2ac) ||
               "59".equalsIgnoreCase(guar_issuer_b2ac) ||
               "35".equalsIgnoreCase(guar_issuer_b2ac) ||
               "11".equalsIgnoreCase(guar_issuer_b2ac);
    }
    
    /** Provjerava da li je kolateral garancija banke. */
    public boolean jeLiGarancijaBanke()
    {
        return new BigDecimal("31777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral ostala garancija. */
    public boolean jeLiGarancijaOstala()
    {
        return new BigDecimal("30777").equals(col_type_id) ||  // garancije države
               new BigDecimal("57777").equals(col_type_id) ||  // garancije tijela lokalne uprave i samouprave
               new BigDecimal("36777").equals(col_type_id) ||  // jamstvo pravne osobe
               new BigDecimal("37777").equals(col_type_id) ||  // jamstvo fizièke osobe
               new BigDecimal("32777").equals(col_type_id) ||  // garancije nepovezanog poduzeæa
               new BigDecimal("34777").equals(col_type_id);    // garancije povezanog poduzeæa
    }
    
    
    /** Provjerava da li je kolateral depozit za netiranje. */
    public boolean jeLiDepozitZaNetiranje()
    {
        return new BigDecimal("76777").equals(col_type_id) ||   // depozit za netiranje u kunama
               new BigDecimal("77777").equals(col_type_id);     // depozit za netiranje u valuti
    }
    
    /** Provjerava da li je kolateral zemljište. */
    public boolean jeLiZemljiste()
    {
        return new BigDecimal("7777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral stambena nekretnina. */
    public boolean jeLiStambenaNekretnina()
    {
        return new BigDecimal("8777").equals(col_type_id);     // stambeni objekt
    }
    /** Provjerava da li je kolateral komercijalna nekretnina.
     *  Komercijalnom nekretninom se smatraju poslovni objekti, turistièki objekti i skladišni prostori. */
    public boolean jeLiKomercijalnaNekretnina()
    {
        return new BigDecimal("9777").equals(col_type_id)  ||  // poslovni objekt
               new BigDecimal("10777").equals(col_type_id) ||  // turistièki objekt
               new BigDecimal("12777").equals(col_type_id);    // skladišni prostor
    }
    /** Provjerava da li je kolateral Weak letter of comfort. */
    public boolean jeLiWeakLoC()
    {
        return new BigDecimal("35777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral Strong letter of comfort. */
    public boolean jeLiStrongLoC()
    {
        return new BigDecimal("73777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral Unfunded risk participation. */
    public boolean jeLiURPA()
    {
        return new BigDecimal("56777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral dvostrana cesija. */
    public boolean jeLiDvostranaCesija()
    {
        return new BigDecimal("45777").equals(col_type_id) || new BigDecimal("46777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral posebna polica osiguranja za mikro klijente. */
    public boolean jeLiPosebnaPolicaZaMikroKlijente()
    {
        return new BigDecimal("91777").equals(col_type_id);
    }
    /** Provjerava da li je kolateral posebna polica osiguranja za SE klijente. */
    public boolean jeLiPosebnaPolicaZaSEKlijente()
    {
        return new BigDecimal("93777").equals(col_type_id);
    }


    // metode koje provjeravaju pripadnost kolaterala nekom podtipu
    
    /** Provjerava da li je kolateral stambena zgrada. */
    public boolean jeLiStambenaZgrada()
    {
        return new BigDecimal("1156953223").equals(col_subtype_id);
    }
    /** Provjerava da li je kolateral komercijalna nekretnina koja je neprihvatljiva za neke prihvatljivosti.
     *  Komercijalne nekretnine koje su neprihvatljive su proizvodni objekti, hoteli, pansioni, hotelska naselja i bazeni. */
    public boolean jeLiKomercijalnaNekretninaNeprihvatljivePodvrste()
    {
        return new BigDecimal("7222").equals(col_subtype_id) ||         // proizvodni objekt
               new BigDecimal("10222").equals(col_subtype_id) ||        // hotel
               new BigDecimal("11222").equals(col_subtype_id) ||        // pansion
               new BigDecimal("31931223").equals(col_subtype_id) ||     // hotelsko naselje
               new BigDecimal("154714356223").equals(col_subtype_id) || // bazen
               new BigDecimal("12222").equals(col_subtype_id);          // privatan smještaj
    }
    
    /** Vraæa šifru kolaterala. */
    public String getColNum()
    {
        return col_num;
    }
}