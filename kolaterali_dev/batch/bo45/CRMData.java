package hr.vestigo.modules.collateral.batch.bo45;

import java.math.BigDecimal;

public class CRMData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo45/CRMData.java,v 1.9 2013/07/15 08:26:38 hrakis Exp $";
    
    public BigDecimal cus_id;               // ID komitenta
    public String register_no;              // interni MB komitenta 
    public String name;                     // naziv komitenta
    public String b2_asset_class;           // b2 asset class komitenta
    public BigDecimal cus_acc_id;           // ID plasmana
    public String cus_acc_no;               // broj partije plasmana
    public BigDecimal exp_balance_hrk;      // ukupna izloženost
    public String frame_cus_acc_no;         // broj partije okvira iz kojeg je plasman
    public String module_code;              // sustav iz kojeg su dobiveni podaci
    public String cus_acc_orig_st;          // status u aplikaciji iz koje se uzima plasman
    public BigDecimal exp_off_bal_lcy;      // ukupna izloženost - vanbilanèno

    // analitièki podaci
    public BigDecimal col_hea_id;           // ID kolaterala
    public BigDecimal exp_coll_amount;      // iznos kolaterala koji pokriva iznos izloženosti
    public BigDecimal col_cat_id;           // ID kategorije kolaterala
    public BigDecimal col_type_id;          // ID tipa kolaterala
    public BigDecimal ponder;               // MVP ponder izražen u postotku
    public String col_type_name;            // naziv tipa kolaterala
    
    public BigDecimal subtype_coll;         // podtip kolaterala izražen kao BigDecimal
    public String subtype_coll_str;         // podtip kolaterala izražen kao String
    
    // sintetièki iznosi pokrivenosti
    public BigDecimal gotovinskiDepozit = new BigDecimal(0);
    public BigDecimal policaOsiguranja = new BigDecimal(0);
    public BigDecimal garancijaDrzave = new BigDecimal(0);
    public BigDecimal garancijaDrugeBanke = new BigDecimal(0);
    public BigDecimal poljoprivrednoGospodarstvo = new BigDecimal(0);
    public BigDecimal poslovnoStambeniObjekt = new BigDecimal(0);
    public BigDecimal skladProdUredskiProstor = new BigDecimal(0);
    public BigDecimal skladisniProstor = new BigDecimal(0);
    public BigDecimal stambeniObjekt = new BigDecimal(0);
    public BigDecimal hotelPansion = new BigDecimal(0);
    public BigDecimal hotelskoNaselje = new BigDecimal(0);
    public BigDecimal privatanSmjestaj = new BigDecimal(0);
    public BigDecimal gradjevinskoZemljiste = new BigDecimal(0);
    public BigDecimal poljoprivrednoZemljiste = new BigDecimal(0);
    public BigDecimal osobniAutomobil = new BigDecimal(0);
    public BigDecimal motocikl = new BigDecimal(0);
    public BigDecimal kamion = new BigDecimal(0);
    public BigDecimal gradjevinskoVoziloStroj = new BigDecimal(0);
    public BigDecimal plovilo = new BigDecimal(0);
    public BigDecimal zrakoplov = new BigDecimal(0);
    public BigDecimal stroj = new BigDecimal(0);
    public BigDecimal ostalaGarancija = new BigDecimal(0);
    public BigDecimal dionica = new BigDecimal(0);
    public BigDecimal obveznica = new BigDecimal(0);
    public BigDecimal udioUFondu = new BigDecimal(0);
    public BigDecimal udioUPoduzecu = new BigDecimal(0);
    public BigDecimal pokretnineOstale = new BigDecimal(0);
    public BigDecimal pokretnineOprema = new BigDecimal(0);
    public BigDecimal pokretnineIToprema = new BigDecimal(0);
    public BigDecimal cesija = new BigDecimal(0);
    public BigDecimal zaliha = new BigDecimal(0);
    
    public BigDecimal ukupnaPokrivenost()
    {
        return gotovinskiDepozit
        .add(policaOsiguranja)
        .add(garancijaDrzave)
        .add(garancijaDrugeBanke)
        .add(poljoprivrednoGospodarstvo)
        .add(poslovnoStambeniObjekt)
        .add(skladProdUredskiProstor)
        .add(skladisniProstor)
        .add(stambeniObjekt)
        .add(hotelPansion)
        .add(hotelskoNaselje)
        .add(privatanSmjestaj)
        .add(gradjevinskoZemljiste)
        .add(poljoprivrednoZemljiste)
        .add(osobniAutomobil)
        .add(motocikl)
        .add(kamion)
        .add(gradjevinskoVoziloStroj)
        .add(plovilo)
        .add(zrakoplov)
        .add(stroj)
        .add(ostalaGarancija)
        .add(dionica)
        .add(obveznica)
        .add(udioUFondu)
        .add(udioUPoduzecu)
		.add(pokretnineOstale)
        .add(pokretnineOprema)
        .add(pokretnineIToprema)
        .add(cesija)
        .add(zaliha);
    }
    
     
    /**
     * Metoda koja vraæa da li je plasman okvir.
     */
    public boolean isFrame()
    {
        return ("OKV".equals(module_code) && frame_cus_acc_no == null);
    }
    
    /**
     * Metoda koja vraæa da li je plasman iz okvira.
     */
    public boolean isAccountFromFrame()
    {
        return (frame_cus_acc_no != null);
    }
    
    /**
     * Metoda koja vraæa da li je plasman na SSP.
     */
    public boolean isSSP()
    {
        if (module_code == null || cus_acc_orig_st == null) return false;
        String module = module_code.trim();
        String status = cus_acc_orig_st.trim();
        return (module.equals("TRC") && status.equals("E")) ||
               (module.equals("PKR") && status.equals("T")) ||
               (module.equals("PPZ") && status.equals("SS")) ||
               (module.equals("KRD") && status.equals("SS")) ||
               (module.equals("GAR") && status.equals("SS")) ||
               (module.equals("KKR") && (status.equals("94") || status.equals("95"))) ||
               (module.equals("LOC") && status.equals("SS"));
    }
}