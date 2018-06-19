package hr.vestigo.modules.collateral.common.yoyH;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


/**
 * Klasa koja sadr�i podatke o kolateralu koji se prate u historizaciji.
 * @author hrakis
 */
public class YOYHData implements Cloneable
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyH/YOYHData.java,v 1.3 2014/12/16 12:26:13 hrakis Exp $";
    
    private final BigDecimal real_estate_col_cat_id = new BigDecimal("618223");
    private final BigDecimal movable_col_cat_id = new BigDecimal("621223");
    private final BigDecimal vessel_col_cat_id = new BigDecimal("620223");
    private final BigDecimal vehicle_col_cat_id = new BigDecimal("624223");
    private final BigDecimal supply_col_cat_id = new BigDecimal("626223");
    
    
    public YOYHData(BigDecimal col_hea_id)
    {
        this.col_hea_id = col_hea_id;
    }


    /** Primarni klju� tablice. */
    public BigDecimal co_chg_his_id;

    /** ID kolaterala. */
    public BigDecimal col_hea_id;

    /** �ifra kolaterala. */
    public String col_num;

    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** ID podvrste kolaterala */
    public BigDecimal col_sub_id;
    
    /** Indikator osiguranosti kolaterala. */
    public String inspol_ind;


    /** Tr�i�na vrijednost kolaterala. */
    public BigDecimal real_est_nomi_valu;

    /** ID valute tr�i�ne vrijednosti kolaterala. */
    public BigDecimal real_est_nm_cur_id;

    /** Oznaka valute tr�i�ne vrijednosti kolaterala. */
    public String real_est_nm_cur_code_char;

    /** Datum tr�i�ne vrijednosti kolaterala. */
    public Date real_est_nomi_date;

    /** ID collateral officera koji je prekontrolirao vrijednost kolaterala. */
    public BigDecimal use_id_co;
    
    /** Ime collateral officera koji je prekontrolirao vrijednost kolaterala. */
    public String use_name_co;
    
    /** �ifra vrste tr�i�ne vrijednosti. */
    public String real_est_nom_type;
    
    /** Naziv vrste tr�i�ne vrijednosti. */
    public String real_est_nom_type_name;
    
    
    /** Procijenjena vrijednost kolaterala. */
    public BigDecimal real_est_estn_valu;
    
    /** Datum procjene kolaterala. */
    public Date real_est_estn_date;
    
    /** Opis procjene vrijednosti kolaterala. */
    public String real_est_nomi_desc;
    
    /** ID procjenitelja (tvrtka). */
    public BigDecimal real_est_euse_id;
    
    /** Naziv procjenitelja (tvrtka). */
    public String real_est_euse_name;
    
    /** ID procjenitelja (fizi�ka osoba). */
    public BigDecimal estimate_cus_id;
    
    /** Interni MB procjenitelja (fizi�ka osoba) */
    public String estimate_cus_register_no;
    
    /** Naziv procjenitelja (fizi�ka osoba). */
    public String estimate_cus_name;
    
    
    /** �ifra tipa procjenitelja. */
    public String est_type;
    
    /** Naziv tipa procjenitelja. */
    public String est_type_name;
    
    /** �ifra metode procjene 1. */
    public String met_est_1;
    
    /** Naziv metode procjene 1. */
    public String met_est_1_name;
    
    /** �ifra metode procjene 2. */
    public String met_est_2;
    
    /** Naziv metode procjene 2. */
    public String met_est_2_name;
    
    
    /** Cijena iz kupoprodajnog ugovora izra�ena u EUR. */
    public BigDecimal buy_sell_value;
    
    /** Valuta kupoprodajnog ugovora. (fiksno EUR) */
    public String buy_sell_value_cur_code_char = "EUR";
    
    
    /** Nova gra�evinska vrijednost. */
    public BigDecimal new_build_val;
    
    /** Napomena o nekretnini. */
    public String real_est_comment;
    
    
    /** Komentar (razlog vra�anja kolaterala u obradu). */
    public String cmnt;
    
    
    /** Indikator da li je kolateral potvr�en od strane collateral officera (D ili N). */
    public String co_ind;
    
    /** ID collateral officera koji je potvrdio kolateral (kva�ica). */
    public BigDecimal co_use_id;
    
    /** Ime collateral officera koji je potvrdio kolateral (kva�ica). */
    public String co_use_name;
    
    /** Timestamp potvrde (kva�ice) collateral officera. */
    public Timestamp co_ts;
    
    /** Timestamp poni�tenja potvrde CO kod vra�anja kolaterala ponovno u obradu. */
    public Timestamp co_chg_ts;
    
    /** ID collateral officera poni�tenja potvrde CO. */
    public BigDecimal co_chg_use_id;
    
    /** Ime collateral officera poni�tenja potvrde CO. */
    public String co_chg_use_name;
    
    
    /** ID referenta koji je promijenio zapis. */
    public BigDecimal use_id;
    
    /** Ime referenta koji je promijenio zapis. */
    public String use_name;
    
    /** Datum i vrijeme promjene zapisa. */
    public Timestamp user_lock;
    
    
    /** Datum i vrijeme upisa sloga u tablicu. */
    public Timestamp recording_time;
    
    /** Metoda dohva�a datum upisa sloga u tablicu */
    public Date getDate()
    {
        return new Date(recording_time.getTime());
    }
    
    
    /** Defaultni ponder. */
    public BigDecimal dfl_ponder;
    
    /** MVP ponder. */
    public BigDecimal mvp_ponder;
    
    /** CESP ponder. */
    public BigDecimal cesp_ponder;
    
    
    
    /**
     * Metoda dohva�a zadani ponder za kolateral.
     * @param ponder_type vrsta pondera (MVP, CESP)
     * @param isDefault da li se radi o defaultnom ponderu
     * @return vrijednost pondera
     */
    public BigDecimal getCollateralPonder(String ponder_type, boolean isDefault)
    {
        if (isDefault)
        {
            if ("MVP".equalsIgnoreCase(ponder_type)) return dfl_ponder;
        }
        else
        {
            if ("MVP".equalsIgnoreCase(ponder_type)) return mvp_ponder;
            else if ("CESP".equalsIgnoreCase(ponder_type)) return cesp_ponder;
        }
        return null;
    }
    
    /**
     * Metoda postavlja zadani ponder za kolateral.
     * @param ponder_type vrsta pondera (MVP, CESP)
     * @param isDefault da li se radi o defaultnom ponderu
     * @param value vrijednost pondera
     */
    public void setCollateralPonder(String ponder_type, boolean isDefault, BigDecimal value)
    {
        if (isDefault)
        {
            if ("MVP".equalsIgnoreCase(ponder_type)) dfl_ponder = value;
        }
        else
        {
            if ("MVP".equalsIgnoreCase(ponder_type)) mvp_ponder = value;
            else if ("CESP".equalsIgnoreCase(ponder_type)) cesp_ponder = value;
        }
    }


    
    /** Metoda vra�a je li kolateral nekretnina. */
    public boolean isRealEstate()
    {
        return real_estate_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda vra�a je li kolateral pokretnina. */
    public boolean isMovable()
    {
        return movable_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda vra�a je li kolateral plovilo. */
    public boolean isVessel()
    {
        return vessel_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda vra�a je li kolateral vozilo. */
    public boolean isVehicle()
    {
        return vehicle_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda vra�a je li kolateral zaliha. */
    public boolean isSupply()
    {
        return supply_col_cat_id.equals(col_cat_id);
    }
    
    
    /** Metoda vra�a da li kolateral pripada kategoriji kolaterala koja se historizira. */
    public boolean isHistorizationCategory()
    {
        return isRealEstate() || isMovable() || isVessel() || isVehicle() || isSupply();
    }
    
    
    /**
     * Metoda vra�a da li su podaci koji se prate za historizaciju u ovom objektu jednaki onima u zadanom objektu. 
     * @param data objekt s kojim se uspore�uje
     * @return true ako nema razlika, false ako razlike postoje
     */
    public boolean isEqualTo(YOYHData data)
    {
        return 
            objectsEqual(this.real_est_nomi_valu, data.real_est_nomi_valu) &&   // tr�i�na vrijednost kolaterala
            objectsEqual(this.real_est_nm_cur_id, data.real_est_nm_cur_id) &&   // valuta tr�i�ne vrijednosti kolaterala 
            objectsEqual(this.real_est_nomi_date, data.real_est_nomi_date) &&   // datum tr�i�ne vrijednosti kolaterala 
            objectsEqual(this.use_id_co, data.use_id_co) &&                     // collateral officer
            objectsEqual(this.real_est_nom_type, data.real_est_nom_type) &&     // vrsta tr�i�ne vrijednosti
            objectsEqual(this.real_est_estn_valu, data.real_est_estn_valu) &&   // procijenjena vrijednost kolaterala
            objectsEqual(this.real_est_estn_date, data.real_est_estn_date) &&   // datum procjene
            objectsEqual(this.real_est_nomi_desc, data.real_est_nomi_desc) &&   // opis procjene
            objectsEqual(this.real_est_euse_id, data.real_est_euse_id) &&       // procjenitelj (tvrtka)
            objectsEqual(this.estimate_cus_id, data.estimate_cus_id) &&         // procjenitelj (fizi�ka osoba)
            objectsEqual(this.est_type, data.est_type) &&                       // tip procjenitelja
            objectsEqual(this.met_est_1, data.met_est_1) &&                     // metoda procjene 1
            objectsEqual(this.met_est_2, data.met_est_2) &&                     // metoda procjene 2
            objectsEqual(this.buy_sell_value, data.buy_sell_value) &&           // cijena iz kupoprodajnog ugovora
            objectsEqual(this.new_build_val, data.new_build_val) &&             // nova gra�evinska vrijednost
            objectsEqual(this.real_est_comment, data.real_est_comment) &&       // napomena o nekretnini
            objectsEqual(this.cmnt, data.cmnt);                                 // komentar (razlog vra�anja u obradu)
    }
    
    /**
     * Metoda vra�a da li zadani objekti imaju istu vrijednost.
     * @param obj1 prvi objekt
     * @param obj2 drugi objekt
     * @return true ako su jednaki, false ako su razli�iti
     */
    private boolean objectsEqual(Object obj1, Object obj2)
    {
        return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
    }
    
    
    /**
     * Metoda koja iz zadanog objekta kopira podatke o potvrdi collateral officera (kva�ica).  
     * @param data objekt iz kojeg se kopiraju podaci
     */
    public void copyCoDataFrom(YOYHData data)
    {
        this.co_ind = data.co_ind;
        this.co_use_id = data.co_use_id;
        this.co_use_name = data.co_use_name;
        this.co_ts = data.co_ts;
        this.co_chg_ts = data.co_chg_ts;
        this.co_chg_use_id = data.co_chg_use_id;
    }
    
 
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("col_hea_id=").append(col_hea_id);
        buffer.append(", real_est_nomi_valu=").append(real_est_nomi_valu);
        buffer.append(", real_est_nm_cur_id=").append(real_est_nm_cur_id);
        buffer.append(", real_est_nomi_date=").append(real_est_nomi_date); 
        buffer.append(", use_id_co=").append(use_id_co);
        buffer.append(", real_est_nom_type=").append(real_est_nom_type);
        buffer.append(", real_est_estn_valu=").append(real_est_estn_valu);
        buffer.append(", real_est_estn_date=").append(real_est_estn_date);
        buffer.append(", real_est_nomi_desc=").append(real_est_nomi_desc);
        buffer.append(", real_est_euse_id=").append(real_est_euse_id);
        buffer.append(", estimate_cus_id=").append(estimate_cus_id);
        buffer.append(", est_type=").append(est_type);
        buffer.append(", met_est_1=").append(met_est_1);
        buffer.append(", met_est_2=").append(met_est_2);
        buffer.append(", buy_sell_value=").append(buy_sell_value);
        buffer.append(", new_build_val=").append(new_build_val);
        buffer.append(", real_est_comment=").append(real_est_comment);
        buffer.append(", cmnt=").append(cmnt);
        return buffer.toString();
    }

    @Override
    protected YOYHData clone() throws CloneNotSupportedException {
        return (YOYHData)super.clone();
    }
}



/**
 * Klasa koja sadr�i podatke o ponderu kolaterala.
 */
class CollPonderData
{
    /** Vrijednost pondera izra�ena u postotcima. */
    public BigDecimal ponder_value;
    
    /** Datum od kada vrijedi ponder. */
    public Date date_from;
    
    /** Datum do kada vrijedi ponder. */
    public Date date_until;

    /** Da li postoji dodatni uvjet koji odre�uje ponder. */
    public String add_request;
}


/**
 * Klasa koja sadr�i podatke o osiguranosti kolaterala.
 */
class CollInsPolIndData
{
    /** Indikator da li je kolateral osiguran. */
    public String inspol_ind;
    
    /** Datum od kada vrijedi zapis. */
    public Date date_from;
    
    /** Datum do kada vrijedi zapis. */
    public Date date_until;
}