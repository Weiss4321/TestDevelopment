package hr.vestigo.modules.collateral.common.yoyG;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
  
import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.yoyG.YOYG1.IteratorOpisi;

    
/**
 * Common za ažuriranje prihvatljivosti zadanom kolateralu.
 * 
 * U konstruktor se predaje remote context i ID kolaterala.
 * Odreðivanje i ažuriranje prihvatljivosti se inicira pozivom metode azurirajPrihvatljivosti.
 * Prije samog ažuriranja prihvatljivosti moguæe je podesiti koje prihvatljivosti æe se raèunati, pozivom metoda setOdrediXXXPrihvatljivost. Po defaultu, sve se prihvatljivosti ažuriraju. 
 * Takoðer, moguæe je pozivom metode setSpremiPromjene odrediti da li æe se izraèunate vrijednosti zapisati u bazu podataka. Po defaultu, vrijednosti se spremaju u bazu podataka.
 * Ako se izraèunate vrijednosti prihvatljivosti ne razlikuju od trenutaènih vrijednosti u bazi podataka, neæe se izvršiti zapisivanje prihvatljivosti u bazu podataka.
 * Nakon odreðivanja prihvatljivosti, moguæe je pregledati kriterije i ispunjenost kriterija za prihvatljivosti (metode getUvjetiZaXXX), kao i izraèunate vrijednosti prihvatljivosti (getXXXPrihvatljivost).  
 * 
 * @author hrakis
 */
public class YOYG0
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyG/YOYG0.java,v 1.21 2017/10/23 13:18:18 hrakis Exp $";

    private RemoteContext rc;
    private YOYG1 yoyG1;
    private YOYGData parametri;

    private LinkedHashMap<Uvjet,Boolean> uvjetiZaCRMHNB;
    private LinkedHashMap<Uvjet,Boolean> uvjetiZaHNB;
    private LinkedHashMap<Uvjet,Boolean> uvjetiZaB2Stand;
    private LinkedHashMap<Uvjet,Boolean> uvjetiZaB2IRB;
    private LinkedHashMap<Uvjet,Boolean> uvjetiZaND;
    
    private String CRMHNBMisljenje;
    private String HNBPrihvatljivost;
    private String B2StandPrihvatljivost;
    private String B2IRBPrihvatljivost;
    private String NDPrihvatljivost;
    
    private boolean odrediCRMHNBMisljenje = true;
    private boolean odrediHNBPrihvatljivost = true;
    private boolean odrediB2StandPrihvatljivost = true;
    private boolean odrediB2IRBPrihvatljivost = true;
    private boolean odrediNDPrihvatljivost = true;
    
    private boolean spremiPromjene = true;
    
    private HashMap<String,String> opisi = new HashMap<String,String>();
   
    public enum Uvjet
    {
        vrstaKolaterala,
        rbaPrihvatljivost,
        predanaDokumentacija,
        misljenjePravneSluzbe,
        upisanaHipoteka,
        crmMisljenje,
        crmHnbMisljenje,
        osigurano,
        garancijaNaPrviPoziv,
        uporabnaDozvola,
        gradjevinskaDozvola,
        namjenaNekretnine,
        kaskoOsigurano,
        buduciDatumDospijeca,
        uskladjenostDospijeca,
        vlasnikFizickaOsoba,
        brojNekretninaUVlasnistvu,
        cesijaNijeRadiNaplatePlasmana,
        vrstaProcjene,
        garancijaVanjskiRating,
        omjerPlasmaniNcvKolaterala,
        garancijaRating,
        datumRevalorizacije,
        kotiraNaZgBurzi,
        imaObjavljenuCijenu,
        pozitivnoMisljenjeAnaliticara,
        datumPremijaPlacenaDo,
        vrstaPotrazivanjaCesije;
        
        public String toString()
        {
            switch(this)
            {
                case vrstaKolaterala: return "Vrsta kolaterala je prihvatljiva";
                case rbaPrihvatljivost: return "RBA prihvatljivo";
                case predanaDokumentacija: return "Predana sva dokumentacija";
                case misljenjePravneSluzbe: return "Mi\u0161ljenje pravne slu\u017Ebe";
                case upisanaHipoteka: return "Upisana barem jedna va\u017Ee\u0107a RBA hipoteka";
                case crmMisljenje: return "CRM mi\u0161ljenje";
                case crmHnbMisljenje: return "CRM HNB mi\u0161ljenje";
                case osigurano: return "Osigurano (akt i pla)";
                case garancijaNaPrviPoziv: return "Garancija je na prvi poziv";
                case uporabnaDozvola: return "Uporabna dozvola";
                case gradjevinskaDozvola: return "Gra\u0111evinska dozvola";
                case namjenaNekretnine: return "Namjena nekretnine";
                case kaskoOsigurano: return "Kasko osiguranje";
                case buduciDatumDospijeca: return "Budu\u0107i datum dospije\u0107a";
                case uskladjenostDospijeca: return "Uskla\u0111enost datuma dospije\u0107a";
                case vlasnikFizickaOsoba: return "Vlasnik je fizi\u010Dka osoba";
                case brojNekretninaUVlasnistvu: return "Broj nekretnina u vlasni\u0161tvu";
                case cesijaNijeRadiNaplatePlasmana: return "Cesija nije radi naplate plasmana";
                case vrstaProcjene: return "Vrsta procjene";
                case garancijaVanjskiRating: return "Ima vanjski rating Moody's LT";
                case omjerPlasmaniNcvKolaterala: return "Omjer plasmani:NCV kolaterala";
                case garancijaRating: return "Ima popunjeno polje Rating";
                case datumRevalorizacije: return "Datum revalorizacije...";
                case kotiraNaZgBurzi: return "Kotira na ZG burzi";
                case imaObjavljenuCijenu: return "Ima objavljenu cijenu...";
                case pozitivnoMisljenjeAnaliticara: return "Pozitivno mi\u0161ljenje analiti\u010Dara";
                case datumPremijaPlacenaDo: return "Datum premija pla\u0107ena do >= datum dospije\u0107a plasmana";
                case vrstaPotrazivanjaCesije: return "Vrsta potra\u017Eivanja cesije";
                default: return super.toString();
            }
        }
    }

    /**
     * Konstruktor commona.
     * @param rc Remote context
     * @param col_hea_id ID kolaterala
     */
    public YOYG0(RemoteContext rc, BigDecimal col_hea_id) throws SQLException
    {
        this.rc = rc;
        setColHeaId(col_hea_id);
        yoyG1 = new YOYG1(rc);
        
        // dohvati opise prihvatljivosti u hashmapu
        IteratorOpisi iter = yoyG1.dohvatiOpise();
        while(iter.next()) opisi.put(iter.sys_code_value(), iter.sys_code_desc());
        iter.close();
    }
    
    /**
     * Metoda koja pokreæe odreðivanje i ažuriranje prihvatljivosti.
     * @return da li se odreðene prihvatljivosti razlikuju od starih prihvatljivosti 
     */
    public boolean azurirajPrihvatljivosti() throws Exception
    {
        // dohvati potrebne parametre
        yoyG1.dohvatiOsnovneParametre(parametri);
        if(parametri.jeLiDepozit())
        {
            yoyG1.dohvatiParametreZaDepozit(parametri);
            yoyG1.dohvatiDatumDospijecaPlasmana(parametri, true);
        }
        else if(parametri.jeLiGarancija())
        {
            yoyG1.dohvatiParametreZaGaranciju(parametri);
            yoyG1.dohvatiDatumDospijecaPlasmana(parametri, false);
            parametri.rating = yoyG1.dohvatiRating(parametri.guar_issuer_id, new BigDecimal("1354776003"));
            parametri.mlt_rating = yoyG1.dohvatiRating(parametri.guar_issuer_id, new BigDecimal("660757251"));
        }
        else if(parametri.jeLiNekretnina())
        {
            yoyG1.dohvatiParametreZaNekretninu(parametri);
            yoyG1.dohvatiOsiguranost(parametri);
            yoyG1.dohvatiParametreVlasnika(parametri);
            yoyG1.dohvatiVrstuProcjene(parametri);
            // yoyG1.dohvatiOmjerPlasmaniNcvKolaterala(parametri);
        }
        else if(parametri.jeLiVozilo())
        {
            yoyG1.dohvatiParametreZaVozilo(parametri);
            yoyG1.dohvatiOsiguranost(parametri);
        }
        else if(parametri.jeLiPokretnina())
        {
            yoyG1.dohvatiOsiguranost(parametri);
        }
        else if(parametri.jeLiPlovilo())
        {
            yoyG1.dohvatiOsiguranost(parametri);
        }
        else if(parametri.jeLiZaliha())
        {
            yoyG1.dohvatiOsiguranost(parametri);
        }
        else if(parametri.jeLiCesija())
        {
            yoyG1.dohvatiParametreZaCesiju(parametri);
        }
        else if(parametri.jeLiVRP())
        {
            yoyG1.dohvatiDatumDospijecaPlasmana(parametri, true);
            yoyG1.dohvatiParametreZaVRP(parametri);
        }
        else if(parametri.jeLiPolicaOsiguranja())
        {
            yoyG1.dohvatiParametreZaPolicuOsiguranja(parametri);
            yoyG1.dohvatiDatumDospijecaPlasmana(parametri, false);
        }
        
        // poništi eventualno popunjene varijable
        CRMHNBMisljenje = null;
        HNBPrihvatljivost = null;
        B2StandPrihvatljivost = null;
        B2IRBPrihvatljivost = null;
        NDPrihvatljivost = null;
        uvjetiZaCRMHNB = null;
        uvjetiZaHNB = null;
        uvjetiZaB2Stand = null;
        uvjetiZaB2IRB = null;
        uvjetiZaND = null;
        
        // ispitaj uvjete za sve prihvatljivosti i odredi prihvatljivost
        if(odrediCRMHNBMisljenje)
        {
            uvjetiZaCRMHNB = ispitajUvjeteZaCRMHNB();
            CRMHNBMisljenje = odrediPrihvatljivost(uvjetiZaCRMHNB);
            if(CRMHNBMisljenje != null) parametri.crm_hnb = CRMHNBMisljenje;  // nakon izraèuna CRM HNB potrebno je novo stanje ažurirati u parametrima, jer je uvjet za ostale prihvatljivosti
        }
        if(odrediHNBPrihvatljivost)
        {
            uvjetiZaHNB = ispitajUvjeteZaHNB();
            HNBPrihvatljivost = odrediPrihvatljivost(uvjetiZaHNB);
        }
        if(odrediB2StandPrihvatljivost)
        {
            uvjetiZaB2Stand = ispitajUvjeteZaB2Stand();
            B2StandPrihvatljivost = odrediPrihvatljivost(uvjetiZaB2Stand);
        }
        if(odrediB2IRBPrihvatljivost)
        {
            uvjetiZaB2IRB = ispitajUvjeteZaB2IRB();
            B2IRBPrihvatljivost = odrediPrihvatljivost(uvjetiZaB2IRB);
        }
        if(odrediNDPrihvatljivost)
        {
            uvjetiZaND = ispitajUvjeteZaND();
            NDPrihvatljivost = odrediPrihvatljivost(uvjetiZaND);
        }
        
        // uzmi stare vrijednosti za prihvatljivosti koje se nisu odreðivale
        if(CRMHNBMisljenje == null) CRMHNBMisljenje = parametri.crm_hnb_old;
        if(HNBPrihvatljivost == null) HNBPrihvatljivost = parametri.hnb_eligibility_old;
        if(B2StandPrihvatljivost == null) B2StandPrihvatljivost = parametri.b2stand_eligibility_old;
        if(B2IRBPrihvatljivost == null) B2IRBPrihvatljivost = parametri.b2irb_eligibility_old;
        if(NDPrihvatljivost == null) NDPrihvatljivost = parametri.nd_eligibility_old;

        // ako se bilo koja vrijednost promijenila, zapiši promjene u bazu podataka
        boolean jeLiBiloPromjena = false;
        if(!objectsEqual(CRMHNBMisljenje, parametri.crm_hnb_old) ||
           !objectsEqual(HNBPrihvatljivost, parametri.hnb_eligibility_old) ||
           !objectsEqual(B2StandPrihvatljivost, parametri.b2stand_eligibility_old) ||
           !objectsEqual(B2IRBPrihvatljivost, parametri.b2irb_eligibility_old) ||
           !objectsEqual(NDPrihvatljivost, parametri.nd_eligibility_old))
        {
            jeLiBiloPromjena = true;
            if(spremiPromjene) yoyG1.zapisiPrihvatljivosti(parametri, CRMHNBMisljenje, HNBPrihvatljivost, B2StandPrihvatljivost, B2IRBPrihvatljivost, NDPrihvatljivost);
        }
        
        return jeLiBiloPromjena;
    }
    
    /**
     * Metoda koja na temelju predanih uvjeta i njihovih ispunjenosti odreðuje prihvatljivost kolaterala. 
     * @param uvjeti Kolekcija parova uvjet/ispunjenost
     * @return "D" ako je prihvatljiv, "N" ako nije prihvatljiv, null ako se prihvatljivost ne odreðuje.
     */
    protected String odrediPrihvatljivost(LinkedHashMap<Uvjet,Boolean> uvjeti)
    {
        if(uvjeti == null) return null;
        if(uvjeti.size() <= 0) return null;
        
        for (Iterator<Boolean> it = uvjeti.values().iterator(); it.hasNext();)
        {
            if(!it.next().booleanValue()) return "N";
        }
        return "D";
    }
    
    /**
     * Metoda koja usporeðuje dva objekta uzimajuæi u obzir null vrijednosti.
     * @param s1 Prvi objekt
     * @param s2 Drugi objekt
     * @return da li su dva objekta jednaka
     */
    protected boolean objectsEqual(Object o1, Object o2)
    {
        return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
    }

    /**
     * Metoda koja ispituje ispunjenost svih uvjeta za HNB prihvatljivost. 
     * @return Vraæa kolekciju parova uvjet/ispunjenost.
     */
    protected LinkedHashMap<Uvjet,Boolean> ispitajUvjeteZaHNB()
    {
        LinkedHashMap<Uvjet,Boolean> uvjeti = new LinkedHashMap<Uvjet,Boolean>();
          
        if(parametri.jeLiDepozitZaNetiranje())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiDepozit())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.buduciDatumDospijeca, parametri.buduciDatumDospijeca());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiGarancija())
        {
            if(parametri.jeLiGarancijaBanke())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
            }
            else if(parametri.jeLiGarancijaOstala())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
            }
            else if(parametri.jeLiWeakLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiStrongLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiURPA())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            }
        }
        else if(parametri.jeLiMjenica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZaduznica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiNekretnina())
        {
            if(parametri.jeLiStambenaNekretnina())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
            else if(parametri.jeLiKomercijalnaNekretnina())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.uporabnaDozvola, parametri.uporabnaDozvola());
                uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
            else if(parametri.jeLiZemljiste())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                uvjeti.put(Uvjet.gradjevinskaDozvola, parametri.gradjevinskaDozvola());
            }
        }
        else if(parametri.jeLiVozilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiPokretnina())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiPlovilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiZaliha())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiPolicaOsiguranja())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            // zanemareno: vinkulirana u korist RBA
        }
        else if(parametri.jeLiCesija())
        {
            if(parametri.jeLiDvostranaCesija())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.cesijaNijeRadiNaplatePlasmana, parametri.cesijaNijeRadiNaplatePlasmana());
            }
        }
        else if(parametri.jeLiZlato())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            // zanemareno: banka ga skladišti
        }
        else if(parametri.jeLiUdjelUPoduzecu())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        
        return uvjeti;
    }

    
    
    /**
     * Metoda koja ispituje ispunjenost svih uvjeta za B2 Stand prihvatljivost. 
     * @return Vraæa kolekciju parova uvjet/ispunjenost.
     */
    protected LinkedHashMap<Uvjet,Boolean> ispitajUvjeteZaB2Stand()
    {
        LinkedHashMap<Uvjet,Boolean> uvjeti = new LinkedHashMap<Uvjet,Boolean>();
        
        if(parametri.jeLiNekretnina())
        {
            if(parametri.jeLiStambenaNekretnina())
            {
                if(parametri.jeLiStambenaZgrada())
                {
                    uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
                }
                else
                {
                    uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                    uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                    uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                    uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                    uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                    uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                    uvjeti.put(Uvjet.vlasnikFizickaOsoba, parametri.vlasnikFizickaOsoba());
                    uvjeti.put(Uvjet.brojNekretninaUVlasnistvu, parametri.brojNekretninaUVlasnistvu());
                    uvjeti.put(Uvjet.namjenaNekretnine, parametri.namjenaNekretnine());
                    uvjeti.put(Uvjet.crmHnbMisljenje, parametri.crmHnbMisljenje());
                    uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                }
            }
            else if(parametri.jeLiKomercijalnaNekretnina())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.uporabnaDozvola, parametri.uporabnaDozvola());
                uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.crmHnbMisljenje, parametri.crmHnbMisljenje());
            }
            else if(parametri.jeLiZemljiste())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                uvjeti.put(Uvjet.gradjevinskaDozvola, parametri.gradjevinskaDozvola());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.crmHnbMisljenje, parametri.crmHnbMisljenje());
            }
        }
        else if(parametri.jeLiVozilo())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiPokretnina())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiPlovilo())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZaliha())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiGarancija())
        {
            if(parametri.jeLiWeakLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiStrongLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiURPA())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaVanjskiRating, parametri.garancijaVanjskiRating());
            }
            else if(parametri.jeLiGarancijaB2AC())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaVanjskiRating, parametri.garancijaVanjskiRating());
            }
        }
        else if(parametri.jeLiDepozit())
        {
            if (parametri.jeLiDepozitZaNetiranje())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.buduciDatumDospijeca, parametri.buduciDatumDospijeca());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.buduciDatumDospijeca, parametri.buduciDatumDospijeca());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
        }
        else if(parametri.jeLiCesija())
        {
            if(parametri.jeLiDvostranaCesija())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.crmHnbMisljenje, parametri.crmHnbMisljenje());
                uvjeti.put(Uvjet.vrstaPotrazivanjaCesije, parametri.vrstaPotrazivanjaCesije());
            }
        }
        else if(parametri.jeLiMjenica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZaduznica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiPolicaOsiguranja())
        {
            if(parametri.jeLiPosebnaPolicaZaMikroKlijente() || parametri.jeLiPosebnaPolicaZaSEKlijente())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.datumPremijaPlacenaDo, parametri.datumPremijaPlacenaDo());
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());                
            }
            // zanemareno: vinkulirana u korist RBA
        }
        else if(parametri.jeLiDionica() || parametri.jeLiObveznica() || parametri.jeLiZapis())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.kotiraNaZgBurzi, parametri.kotiraNaZgBurzi());
            uvjeti.put(Uvjet.imaObjavljenuCijenu, parametri.imaObjavljenuCijenuUZadnjih(Calendar.MONTH, 1));
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiUdjelUPoduzecu() || parametri.jeLiUdjelUFondu())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZlato())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            // zanemareno: banka ga skladišti
        }
        
        return uvjeti;
    }

    
    
    /**
     * Metoda koja ispituje ispunjenost svih uvjeta za B2 IRB prihvatljivost. 
     * @return Vraæa kolekciju parova uvjet/ispunjenost.
     */
    protected LinkedHashMap<Uvjet,Boolean> ispitajUvjeteZaB2IRB()
    {
        LinkedHashMap<Uvjet,Boolean> uvjeti = new LinkedHashMap<Uvjet,Boolean>();
        
        if(parametri.jeLiNekretnina())
        {
            if(parametri.jeLiStambenaNekretnina())
            {
                if(parametri.jeLiStambenaZgrada())
                {
                    uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
                }
                else
                {
                    uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                    uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                    uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                    uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                    uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                    uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                    uvjeti.put(Uvjet.vlasnikFizickaOsoba, parametri.vlasnikFizickaOsoba());
                    uvjeti.put(Uvjet.brojNekretninaUVlasnistvu, parametri.brojNekretninaUVlasnistvu());
                    uvjeti.put(Uvjet.namjenaNekretnine, parametri.namjenaNekretnine());
                    uvjeti.put(Uvjet.crmHnbMisljenje, parametri.crmHnbMisljenje());
                    uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                }
            }
            else if(parametri.jeLiKomercijalnaNekretnina())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.uporabnaDozvola, parametri.uporabnaDozvola());
                uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
            }
            else if(parametri.jeLiZemljiste())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
                uvjeti.put(Uvjet.gradjevinskaDozvola, parametri.gradjevinskaDozvola());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
            }
        }
        else if(parametri.jeLiVozilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.kaskoOsigurano, parametri.kaskoOsigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiPokretnina())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiPlovilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiZaliha())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiGarancija())
        {
            if(parametri.jeLiWeakLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiStrongLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiURPA())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaRating, parametri.garancijaRating());
            }
            else if(parametri.jeLiGarancijaB2AC())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaRating, parametri.garancijaRating());
            }
        }
        else if(parametri.jeLiDepozit())
        {
            if(parametri.jeLiDepozitZaNetiranje())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.buduciDatumDospijeca, parametri.buduciDatumDospijeca());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.buduciDatumDospijeca, parametri.buduciDatumDospijeca());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());                
            }
        }
        else if(parametri.jeLiCesija())
        {
            if(parametri.jeLiDvostranaCesija())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.vrstaPotrazivanjaCesije, parametri.vrstaPotrazivanjaCesije());
            }
        }
        else if(parametri.jeLiMjenica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZaduznica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiPolicaOsiguranja())
        {
            if(parametri.jeLiPosebnaPolicaZaMikroKlijente() || parametri.jeLiPosebnaPolicaZaSEKlijente())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.datumPremijaPlacenaDo, parametri.datumPremijaPlacenaDo());
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());                
            }
            // zanemareno: vinkulirana u korist RBA
        }
        else if(parametri.jeLiDionica() || parametri.jeLiObveznica() || parametri.jeLiZapis())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.kotiraNaZgBurzi, parametri.kotiraNaZgBurzi());
            uvjeti.put(Uvjet.imaObjavljenuCijenu, parametri.imaObjavljenuCijenuUZadnjih(Calendar.MONTH, 1));
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
        }
        else if(parametri.jeLiUdjelUPoduzecu() || parametri.jeLiUdjelUFondu())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZlato())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            // zanemareno: banka ga skladišti
        }

        return uvjeti;
    }

    
    
    /**
     * Metoda koja ispituje ispunjenost svih uvjeta za ND prihvatljivost. 
     * @return Vraæa kolekciju parova uvjet/ispunjenost.
     */
    protected LinkedHashMap<Uvjet,Boolean> ispitajUvjeteZaND()
    {
        LinkedHashMap<Uvjet,Boolean> uvjeti = new LinkedHashMap<Uvjet,Boolean>();
        
        if(parametri.jeLiNekretnina())
        {
            if(parametri.jeLiStambenaNekretnina() || parametri.jeLiKomercijalnaNekretnina())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.osigurano, parametri.osigurano());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
            else if(parametri.jeLiZemljiste())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.predanaDokumentacija, parametri.predanaDokumentacija());
                uvjeti.put(Uvjet.vrstaProcjene, parametri.vrstaProcjene());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
        }
        else if(parametri.jeLiVozilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.kaskoOsigurano, parametri.kaskoOsigurano());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiPokretnina())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiPlovilo())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiZaliha())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.osigurano, parametri.osigurano());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        else if(parametri.jeLiGarancija())
        {
            if(parametri.jeLiWeakLoC())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else if(parametri.jeLiStrongLoC())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.garancijaRating, parametri.garancijaRating());
            }
            else if(parametri.jeLiURPA())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                if(parametri.garancijaDomaca()) uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 14));
                else uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 30));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaRating, parametri.garancijaRating());
            }
            else if(parametri.jeLiGarancijaB2AC())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                if(parametri.garancijaDomaca()) uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 14));
                else uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 30));
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.garancijaNaPrviPoziv, parametri.garancijaNaPrviPoziv());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.datumRevalorizacije, parametri.datumRevalorizacijeNijeStarijiOd(Calendar.MONTH, 18));
                if(parametri.garancijaDomaca()) uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 14));
                else uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca(Calendar.DATE, 30));
                uvjeti.put(Uvjet.pozitivnoMisljenjeAnaliticara, parametri.pozitivnoMisljenjeAnaliticara());
                uvjeti.put(Uvjet.garancijaRating, parametri.garancijaRating());
            }
        }
        else if(parametri.jeLiDepozit())
        {
            if(parametri.jeLiDepozitZaNetiranje())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca());
                uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            }
        }
        else if(parametri.jeLiCesija())
        {
            if(parametri.jeLiDvostranaCesija())
            {
                uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
                uvjeti.put(Uvjet.vrstaPotrazivanjaCesije, parametri.vrstaPotrazivanjaCesije());
            }
        }
        else if(parametri.jeLiMjenica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZaduznica())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiPolicaOsiguranja())
        {
            if(parametri.jeLiPosebnaPolicaZaMikroKlijente() || parametri.jeLiPosebnaPolicaZaSEKlijente())
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
                uvjeti.put(Uvjet.datumPremijaPlacenaDo, parametri.datumPremijaPlacenaDo());
            }
            else
            {
                uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
                uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());                
            }
            // zanemareno: vinkulirana u korist RBA
        }
        else if(parametri.jeLiDionica() || parametri.jeLiObveznica())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.kotiraNaZgBurzi, parametri.kotiraNaZgBurzi());
            uvjeti.put(Uvjet.imaObjavljenuCijenu, parametri.imaObjavljenuCijenuUZadnjih(Calendar.MONTH, 1));
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
            uvjeti.put(Uvjet.crmMisljenje, parametri.crmMisljenje());
            if (parametri.jeLiObveznica()) uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca());
        }
        else if(parametri.jeLiZapis())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.imaObjavljenuCijenu, parametri.imaObjavljenuCijenuUZadnjih(Calendar.MONTH, 1));
            uvjeti.put(Uvjet.uskladjenostDospijeca, parametri.uskladjenostDospijeca());
        }
        else if(parametri.jeLiUdjelUFondu())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
        }
        else if(parametri.jeLiUdjelUPoduzecu())
        {
            uvjeti.put(Uvjet.vrstaKolaterala, Boolean.FALSE);
        }
        else if(parametri.jeLiZlato())
        {
            uvjeti.put(Uvjet.rbaPrihvatljivost, parametri.rbaPrihvatljivost());
            uvjeti.put(Uvjet.misljenjePravneSluzbe, parametri.misljenjePravneSluzbe());
            uvjeti.put(Uvjet.upisanaHipoteka, parametri.upisanaHipoteka());
        }
        
        return uvjeti;
    }


    /**
     * Metoda koja ispituje ispunjenost svih uvjeta za CRM HNB mišljenje. 
     * @return Vraæa kolekciju parova uvjet/ispunjenost.
     */
    protected LinkedHashMap<Uvjet,Boolean> ispitajUvjeteZaCRMHNB()
    {
        return null;
        
        /*LinkedHashMap<Uvjet,Boolean> uvjeti = new LinkedHashMap<Uvjet,Boolean>();
        
        if(parametri.jeLiNekretnina())
        {
            if(parametri.jeLiStambenaNekretnina())
            {
                if(!parametri.jeLiStambenaZgrada())  // stanovi i kuæe
                {
                    uvjeti.put(Uvjet.vlasnikFizickaOsoba, parametri.vlasnikFizickaOsoba());
                    uvjeti.put(Uvjet.brojNekretninaUVlasnistvu, parametri.brojNekretninaUVlasnistvu());
                    uvjeti.put(Uvjet.namjenaNekretnine, parametri.namjenaNekretnine());
                }
            }
        }
        
        return uvjeti;*/
    }



    /** Postavlja ID kolaterala kojem æe se ažurirati prihvatljivosti. */
    public void setColHeaId(BigDecimal col_hea_id)
    {
        this.parametri = new YOYGData(col_hea_id);
    }
    
    /** Dohvaæa parametre na temelju kojih se odluèivalo o zadovoljenosti uvjeta za prihvatljivost. */
    public YOYGData getParametri() {
        return parametri;
    }


    /** Dohvaæa da li se raèuna CRM HNB mišljenje. */
    public boolean getOdrediCRMHNBMisljenje() {
        return odrediCRMHNBMisljenje;
    }
    
    /** Dohvaæa da li se raèuna HNB prihvatljivost. */
    public boolean getOdrediHNBPrihvatljivost() {
        return odrediHNBPrihvatljivost;
    }

    /** Dohvaæa da li se raèuna B2 Stand prihvatljivost. */
    public boolean getOdrediB2StandPrihvatljivost() {
        return odrediB2StandPrihvatljivost;
    }

    /** Dohvaæa da li se raèuna B2 IRB prihvatljivost. */
    public boolean getOdrediB2IRBPrihvatljivost() {
        return odrediB2IRBPrihvatljivost;
    }

    /** Dohvaæa da li se raèuna ND prihvatljivost. */
    public boolean getOdrediNDPrihvatljivost() {
        return odrediNDPrihvatljivost;
    }

    
    /** Dohvaæa sve uvjete i njihove ispunjenosti za CRM HNB mišljenje. */
    public LinkedHashMap<Uvjet, Boolean> getUvjetiZaCRMHNB() {
        return uvjetiZaCRMHNB;
    }
    
    /** Dohvaæa sve uvjete i njihove ispunjenosti za HNB prihvatljivost. */
    public LinkedHashMap<Uvjet, Boolean> getUvjetiZaHNB() {
        return uvjetiZaHNB;
    }

    /** Dohvaæa sve uvjete i njihove ispunjenosti za B2 Stand prihvatljivost. */
    public LinkedHashMap<Uvjet, Boolean> getUvjetiZaB2Stand() {
        return uvjetiZaB2Stand;
    }

    /** Dohvaæa sve uvjete i njihove ispunjenosti za B2 IRB prihvatljivost. */
    public LinkedHashMap<Uvjet, Boolean> getUvjetiZaB2IRB() {
        return uvjetiZaB2IRB;
    }

    /** Dohvaæa sve uvjete i njihove ispunjenosti za ND prihvatljivost. */
    public LinkedHashMap<Uvjet, Boolean> getUvjetiZaND() {
        return uvjetiZaND;
    }


    /** Dohvaæa izraèunato CRM HNB mišljenje. */
    public String getCRMHNBMisljenje() {
        return CRMHNBMisljenje;
    }
    
    /** Dohvaæa izraèunatu HNB prihvatljivost. */
    public String getHNBPrihvatljivost() {
        return HNBPrihvatljivost;
    }

    /** Dohvaæa izraèunatu B2 Stand prihvatljivost. */
    public String getB2StandPrihvatljivost() {
        return B2StandPrihvatljivost;
    }

    /** Dohvaæa izraèunatu B2 IRB prihvatljivost. */
    public String getB2IRBPrihvatljivost() {
        return B2IRBPrihvatljivost;
    }

    /** Dohvaæa izraèunatu ND prihvatljivost. */
    public String getNDPrihvatljivost() {
        return NDPrihvatljivost;
    }


    /** Dohvaæa opis izraèunate HNB prihvatljivosti. */
    public String getOpisHNBPrihvatljivosti() {
        return opisi.get(HNBPrihvatljivost);
    }

    /** Dohvaæa opis izraèunate B2 Stand prihvatljivosti. */
    public String getOpisB2StandPrihvatljivosti() {
        return opisi.get(B2StandPrihvatljivost);
    }

    /** Dohvaæa opis izraèunate B2 IRB prihvatljivosti. */
    public String getOpisB2IRBPrihvatljivosti() {
        return opisi.get(B2IRBPrihvatljivost);
    }

    /** Dohvaæa opis izraèunate ND prihvatljivost. */
    public String getOpisNDPrihvatljivosti() {
        return opisi.get(NDPrihvatljivost);
    }
    
    
    /** Dohvaæa staro CRM HNB mišljenje. */
    public String getStaroCRMHNBMisljenje() {
        return parametri.crm_hnb_old;
    }
    
    /** Dohvaæa staru HNB prihvatljivost. */
    public String getStaraHNBPrihvatljivost() {
        return parametri.hnb_eligibility_old;
    }

    /** Dohvaæa staru B2 Stand prihvatljivost. */
    public String getStaraB2StandPrihvatljivost() {
        return parametri.b2stand_eligibility_old;
    }

    /** Dohvaæa staru B2 IRB prihvatljivost. */
    public String getStaraB2IRBPrihvatljivost() {
        return parametri.b2irb_eligibility_old;
    }

    /** Dohvaæa staru ND prihvatljivost. */
    public String getStaraNDPrihvatljivost() {
        return parametri.nd_eligibility_old;
    }

    
    /** Vraæa šifru kolaterala. */
    public String getColNum()
    {
        return parametri.col_num;
    }


    /** Odreðuje da li da se raèuna CRM HNB mišljenje. */
    public void setOdrediCRMHNBMisljenje(boolean odrediCRMHNBMisljenje) {
        this.odrediCRMHNBMisljenje = odrediCRMHNBMisljenje;
    }
    
    /** Odreðuje da li da se raèuna HNB prihvatljivost. */
    public void setOdrediHNBPrihvatljivost(boolean odrediHNBPrihvatljivost) {
        this.odrediHNBPrihvatljivost = odrediHNBPrihvatljivost;
    }

    /** Odreðuje da li da se raèuna B2 Stand prihvatljivost. */
    public void setOdrediB2StandPrihvatljivost(boolean odrediB2StandPrihvatljivost) {
        this.odrediB2StandPrihvatljivost = odrediB2StandPrihvatljivost;
    }

    /** Odreðuje da li da se raèuna B2 IRB prihvatljivost. */
    public void setOdrediB2IRBPrihvatljivost(boolean odrediB2IRBPrihvatljivost) {
        this.odrediB2IRBPrihvatljivost = odrediB2IRBPrihvatljivost;
    }

    /** Odreðuje da li da se raèuna ND prihvatljivost. */
    public void setOdrediNDPrihvatljivost(boolean odrediNDPrihvatljivost) {
        this.odrediNDPrihvatljivost = odrediNDPrihvatljivost;
    }


    /** Odreðuje da li da se izraèunate prihvatljivosti spremaju u bazu podataka. */
    public void setSpremiPromjene(boolean spremiPromjene) {
        this.spremiPromjene = spremiPromjene;
    }
}