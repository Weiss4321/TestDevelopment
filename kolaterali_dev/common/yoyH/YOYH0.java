package hr.vestigo.modules.collateral.common.yoyH;

import hr.vestigo.framework.remote.RemoteContext;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * <p><b>
 * Common za historizaciju kolaterala.
 * </b><p/>
 * <p>
 * Zapisivanje podataka u povijest promjena inicira se pozivom metode historize kojoj se predaje ID kolaterala.
 * Metoda usporeðuje trenutne podatke sa zadnjim podacima zapisanima u povijesti promjena.
 * Ako se barem jedan podatak razlikuje, novi podaci zapisuju se u povijest promjena.</p>
 * <p>
 * Metoda getHistory dohvaæa povijest promjena u obliku liste.</p>
 * 
 * @author hrakis
 */
public class YOYH0
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyH/YOYH0.java,v 1.5 2014/12/16 12:26:13 hrakis Exp $";

    private RemoteContext rc;
    public YOYH1 yoyH1;

   
    /**
     * Konstruktor commona.
     * @param rc Remote context
     */
    public YOYH0(RemoteContext rc) throws Exception
    {
        this.rc = rc;
        yoyH1 = new YOYH1(rc);
    }
    
    
    /**
     * Metoda koja pokreæe historizaciju zadanog kolaterala.
     * @param col_hea_id ID kolaterala
     */
    public void historize(BigDecimal col_hea_id) throws Exception
    {
        final String tag = "YOYH1.historize(" + col_hea_id + "): ";
        rc.debug(tag + "begin");
        
        // dohvati zadnje podatke iz povijesti promjena
        YOYHData oldData = yoyH1.getOldCollateralData(col_hea_id);
        rc.debug(tag + "old data: " + oldData);
        
        // dohvati nove podatke o kolateralu
        YOYHData newData = yoyH1.getNewCollateralData(col_hea_id);
        rc.debug(tag + "new data: " + newData);
        
        if (newData.isHistorizationCategory())  // provjeri da li je potrebno voditi historizaciju za ovu kategoriju kolaterala
        {
            if (!newData.isEqualTo(oldData))  // provjeri da li postoje razlike u starim i novim podacima
            {
                rc.debug(tag + "HAS CHANGES -> inserting into history...");
                newData.copyCoDataFrom(oldData);        // kopiraj zadnje podatke o kvaèici u novi objekt  
                yoyH1.deactivateCoChgHistory(oldData);  // deaktiviraj zadnju kvaèicu (postojeæa programska podrška radi pod pretpostavkom da može postojati samo jedna kvaèica za kolateral)  
                yoyH1.insertIntoCoChgHistory(newData);  // ubaci novi zapis
            }
            else
            {
                rc.debug(tag + "no changes");
            }
        }
        else
        {
            rc.debug(tag + "historization not needed for this category");
        }
        
        rc.debug(tag + "end");
    }
    
    
    /**
     * Metoda koja dohvaæa povijest promjena po zadanom kolateralu. U rezultat se ubacuju promjene po svim ponderima.
     * @param col_hea_id ID kolaterala
     * @return lista promjena po kolateralu
     */
    public ArrayList<YOYHData> getHistory(BigDecimal col_hea_id) throws Exception
    {
        final String tag = "YOYH1.getHistory(" + col_hea_id + "): ";
        rc.debug(tag + "begin");
        
        // dohvati sve zapise iz povijesti promjena
        ArrayList<YOYHData> history = new ArrayList<YOYHData>();
        CollHistoryIterator iter = yoyH1.getHistory(col_hea_id);
        while (iter.next()) history.add(createDataObjectFromIterator(iter));
        iter.close();
        
        // ako je povijest prazna, nema potrebe za provjerom pondera
        if (history.size() == 0) return history;
        
        // dohvati osiguranosti po datumu
        ArrayList<CollInsPolIndData> insPolIndHistory = new ArrayList<CollInsPolIndData>();
        CollInsPolIndIterator insPolIndIter = yoyH1.getCollateralInsPolInd(col_hea_id, false);  // povijesni podaci
        while (insPolIndIter.next()) insPolIndHistory.add(createDataObjectFromIterator(insPolIndIter));
        insPolIndIter.close();
        insPolIndIter = yoyH1.getCollateralInsPolInd(col_hea_id, true);  // trenutni podaci
        while (insPolIndIter.next()) insPolIndHistory.add(0, createDataObjectFromIterator(insPolIndIter));  // ubaci na poèetak tako da njih prve uhvati
        insPolIndIter.close();
        
        // ubaci promjene po ponderima
        setCollateralPonders(history, insPolIndHistory, "MVP", false);  // MVP kolateral ponder
        setCollateralPonders(history, insPolIndHistory, "CESP", false); // CESP kolateral ponder
        setCollateralPonders(history, insPolIndHistory, "MVP", true);   // defaultni MVP ponder
        
        rc.debug(tag + "end");
        
        return history;
    }
    
    
    /**
     * Metoda koja dohvaæa povijest promjena po zadanom kolateralu - samo ono što je zapisano u tablici CO_CHG_HISTORY, bez promjena po ponderima.
     * @param col_hea_id ID kolaterala
     * @return lista promjena po kolateralu
     */
    public ArrayList<YOYHData> getRawHistoryData(BigDecimal col_hea_id) throws Exception
    {
        final String tag = "YOYH1.getRawHistoryData(" + col_hea_id + "): ";
        rc.debug(tag + "begin");
        
        // dohvati sve zapise iz povijesti promjena
        ArrayList<YOYHData> history = new ArrayList<YOYHData>();
        CollHistoryIterator iter = yoyH1.getHistory(col_hea_id);
        while (iter.next()) history.add(createDataObjectFromIterator(iter));
        iter.close();
        
        rc.debug(tag + "end");
        
        return history;
    }
    
    
    /**
     * Metoda dohvaæa i ubacuje u povijest promjena sve važeæe kolateral pondere.
     * @param history povijest promjena
     * @param insPolIndHistory povijest osiguranosti kolaterala
     * @param ponder_type vrsta pondera
     * @param isDefault da li se radi o defaultnom ponderu
     */
    private void setCollateralPonders(ArrayList<YOYHData> history, ArrayList<CollInsPolIndData> insPolIndHistory, String ponder_type, boolean isDefault) throws Exception
    {
        if (history.size() == 0) return;
        YOYHData collateral = history.get(0);

        // dohvati pondere
        ArrayList<CollPonderData> ponders = new ArrayList<CollPonderData>();
        CollPonderIterator iterPonder = yoyH1.getCollateralPonders(ponder_type, isDefault, collateral.col_hea_id, collateral.col_cat_id, collateral.col_typ_id, collateral.col_sub_id);
        while (iterPonder.next()) ponders.add(createDataObjectFromIterator(iterPonder));
        iterPonder.close();

        // postavi važeæe pondere za svaki objekt u povijesti promjena
        for (YOYHData historyData : history)
        {
            BigDecimal ponderValue = getPonder(ponders, isDefault, insPolIndHistory, historyData.getDate());
            historyData.setCollateralPonder(ponder_type, isDefault, ponderValue);
        }

        // idi izmeðu svaka dva susjedna zapisa u povijesti promjena i provjeri je li u vremenu izmeðu bilo promjena po ponderu 
        for (int i = 0; i < history.size(); i++)
        {
            YOYHData data1 = history.get(i);

            // dohvati sljedeæi zapis
            YOYHData data2;
            if (i + 1 < history.size())
            {
                data2 = history.get(i + 1);
            }
            else // ako je kraj, za sljedeæi zapis stvori objekt s današnjim datumom i vrijednostima iz zadnjeg zapisa 
            {
                data2 = data1.clone();
                data2.recording_time = new Timestamp(System.currentTimeMillis());
            }

            // kalendar 1 - datum iz prvog zapisa - ovaj æe se kalendar koristiti za pomicanje po danima
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(data1.getDate());
            clearTimeFromCalendar(calendar1);
            calendar1.add(Calendar.DATE, 1);

            // kalendar 2 - datum iz drugog zapisa - ovaj kalendar ostaje fiksan
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(data2.getDate());
            clearTimeFromCalendar(calendar2);

            // za svaki dan izmeðu dva datuma
            for(; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.DATE, 1))
            {
                Date date = new Date(calendar1.getTimeInMillis());  // dohvati datum iz trenutne pozicije kalendara 
                BigDecimal newPonderValue = getPonder(ponders, isDefault, insPolIndHistory, date);  // dohvati važeæi ponder za datum
                BigDecimal oldPonderValue = data1.getCollateralPonder(ponder_type, isDefault);  // dohvati ponder prethodnog zapisa
                
                if (!bigDecimalsEqual(newPonderValue, oldPonderValue))  // ako se ponderi razlikuju
                {
                    YOYHData data = data1.clone();  // stvori novi zapis u povijesti promjena na temelju prethodnog
                    data.setCollateralPonder(ponder_type, isDefault, newPonderValue);  // u novom zapisu postavi novu vrijednost pondera
                    data.recording_time = new Timestamp(date.getTime());  // vrijeme novog zapisa postavi na datum promjene
                    history.add(history.indexOf(data1) + 1, data);  // dodaj novi zapis u povijest promjena
                    data1 = data;  // zapis postaje novi prethodni zapis
                }
            }
        }
    }
    
    
    /**
     * Metoda briše vrijeme (sate, minute, sekunde) iz zadanog kalendara.
     * @param calendar kalendar
     */
    private void clearTimeFromCalendar(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
    
    
    /**
     * Metoda koja stvara objekt s podacima o kolateralu na temelju zadanog iteratora.
     * @param iter iterator s podacima o kolateralu
     * @return objekt s podacima o kolateralu
     */
    public YOYHData createDataObjectFromIterator(CollHistoryIterator iter) throws Exception
    {
        YOYHData data = new YOYHData(iter.col_hea_id());
     
        data.co_chg_his_id = iter.co_chg_his_id();
        data.col_num = iter.col_num();
        data.col_cat_id = iter.col_cat_id();
        data.col_typ_id = iter.col_typ_id();
        data.col_sub_id = iter.col_sub_id();
        data.real_est_nomi_valu = iter.real_est_nomi_valu();
        data.real_est_nm_cur_id = iter.real_est_nm_cur_id();
        data.real_est_nm_cur_code_char = iter.real_est_nm_cur_code_char();
        data.real_est_nomi_date = iter.real_est_nomi_date();
        data.use_id_co = iter.use_id_co();
        data.use_name_co = iter.use_name_co();
        data.real_est_nom_type = iter.real_est_nom_type();
        data.real_est_nom_type_name = iter.real_est_nom_type_name();
        data.real_est_estn_valu = iter.real_est_estn_valu();
        data.real_est_estn_date = iter.real_est_estn_date();
        data.real_est_nomi_desc = iter.real_est_nomi_desc();
        data.real_est_euse_id = iter.real_est_euse_id();
        data.real_est_euse_name = iter.real_est_euse_name();
        data.estimate_cus_id = iter.estimate_cus_id();
        data.estimate_cus_register_no = iter.estimate_cus_register_no();
        data.estimate_cus_name = iter.estimate_cus_name();
        data.est_type = iter.est_type();
        data.est_type_name = iter.est_type_name();
        data.met_est_1 = iter.met_est_1();
        data.met_est_1_name = iter.met_est_1_name();
        data.met_est_2 = iter.met_est_2();
        data.met_est_2_name = iter.met_est_2_name();
        data.buy_sell_value = iter.buy_sell_value();
        data.new_build_val = iter.new_build_val();
        data.real_est_comment = iter.real_est_comment();
        data.cmnt = iter.cmnt();
        data.co_ind = iter.co_ind();
        data.co_use_id = iter.co_use_id();
        data.co_use_name = iter.co_use_name();
        data.co_ts = iter.co_ts();
        data.co_chg_ts = iter.co_chg_ts();
        data.co_chg_use_id = iter.co_chg_use_id();
        data.co_chg_use_name = iter.co_chg_use_name();
        data.use_id = iter.use_id();
        data.use_name = iter.use_name();
        data.user_lock = iter.user_lock();
        data.recording_time = iter.recording_time();
        
        if (data.col_num != null) data.col_num = data.col_num.trim();
        if (data.use_name_co != null) data.use_name_co = data.use_name_co.trim();
        if (data.estimate_cus_register_no != null) data.estimate_cus_register_no = data.estimate_cus_register_no.trim();
        if (data.estimate_cus_name != null) data.estimate_cus_name = data.estimate_cus_name.trim();
        if (data.co_use_name != null) data.co_use_name = data.co_use_name.trim();
        if (data.co_chg_use_name != null) data.co_chg_use_name = data.co_chg_use_name.trim();
        if (data.use_name != null) data.use_name = data.use_name.trim();
        
        return data;
    }
    
    /**
     * Metoda koja stvara objekt s podacima o ponderu na temelju zadanog iteratora.
     * @param iter iterator s podacima o ponderu
     * @return objekt s podacima o ponderu
     */
    public CollPonderData createDataObjectFromIterator(CollPonderIterator iter) throws Exception
    {
        CollPonderData data = new CollPonderData();
        data.ponder_value = iter.ponder_value();
        data.date_from = iter.date_from();
        data.date_until = iter.date_until();
        data.add_request = iter.add_request();
        return data;
    }
    
    /**
     * Metoda koja stvara objekt s podacima o osiguranosti na temelju zadanog iteratora.
     * @param iter iterator s podacima o osiguranosti
     * @return objekt s podacima o osiguranosti
     */
    public CollInsPolIndData createDataObjectFromIterator(CollInsPolIndIterator iter) throws Exception
    {
        CollInsPolIndData data = new CollInsPolIndData();
        data.inspol_ind = iter.inspol_ind();
        data.date_from = iter.date_from();
        data.date_until = iter.date_until();
        return data;
    }
    
    
    /**
     * Metoda na listi promjena pondera pronalazi ponder koji je važio na zadani datum.
     * @param ponders lista promjena pondera
     * @param isDefault da li se radi o defaultnom ponderu
     * @param insPolIndHistory lista promjena osiguranosti kolaterala
     * @param date datum važenja
     * @return vrijednost pondera
     */
    private BigDecimal getPonder(ArrayList<CollPonderData> ponders, boolean isDefault, ArrayList<CollInsPolIndData> insPolIndHistory, Date date)
    {
        String add_request = null;  // dodatni uvjet za ponder
        
        // ako se radi o defaultnom ponderu, za dodatni uvjet postavi osiguranost kolaterala na zadani datum
        if (isDefault) add_request = findInsPolInd(insPolIndHistory, date);   
        
        // dohvati važeæi ponder
        BigDecimal ponder = findPonder(ponders, add_request, date);
        
        // ako se radi o defaultnom ponderu i kolateral je osiguran a nije se uspjelo naæi ponder, probaj naæi ponder za neosiguran kolateral  
        if (isDefault && ponder == null && "D".equalsIgnoreCase(add_request)) ponder = findPonder(ponders, "N", date); 
        
        // ako se radi o defaultnom ponderu i nije se uspjelo naæi ponder, stavi ponder na 100% 
        if (isDefault && ponder == null) ponder = new BigDecimal("100.00");
        
        // ne postoji važeæi ponder
        return ponder;
    }

    
    /**
     * Metoda koja iz zadane liste promjena pondera dohvaæa važeæi ponder za zadani datum i dodatni uvjet.
     * @param ponders lista pondera
     * @param add_request dodatni uvjet
     * @param date datum važenja
     * @return vrijednost pondera, null ako nije pronaðen važeæi ponder
     */
    private BigDecimal findPonder(ArrayList<CollPonderData> ponders, String add_request, Date date)
    {
        for (CollPonderData ponder : ponders)
        {
            if ((add_request == null || ponder.add_request.equalsIgnoreCase(add_request)) &&
                    date.compareTo(ponder.date_from) >= 0 &&
                    date.compareTo(ponder.date_until) <= 0)
            {
                return ponder.ponder_value; 
            }
        }
        return null;
    }

    
    /**
     * Metoda koja za zadani datum dohvaæa osiguranost kolaterala iz zadane liste promjena osiguranosti kolaterala.
     * @param insPolIndHistory lista promjena osiguranosti kolaterala
     * @param date datum važenja
     */
    private String findInsPolInd(ArrayList<CollInsPolIndData> insPolIndHistory, Date date)
    {
        for (CollInsPolIndData insPolData : insPolIndHistory)
        {
            if (date.compareTo(insPolData.date_from) >= 0 && date.compareTo(insPolData.date_until) <= 0)
            {
                return insPolData.inspol_ind; 
            }
        }
        return null;
    }
    
    
    /**
     * Metoda koja usporeðuje dva broja uzimajuæi u obzir null vrijednosti.
     * @param bd1 Prvi broj
     * @param bd2 Drugi broj
     * @return da li su dva broja jednaka
     */
    private boolean bigDecimalsEqual(BigDecimal bd1, BigDecimal bd2)
    {
        return (bd1 == null && bd2 == null) || (bd1 != null && bd1.equals(bd2));
    }
}