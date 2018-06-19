package hr.vestigo.modules.collateral.common.yoyF;

import java.sql.Date;
import java.sql.SQLException;

import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.modules.collateral.common.yoy0.YOY00;

/**
 * Common za ažuriranje povijesti promjena po polici osiguranja.<br/><br/>
 * Potrebno je popuniti instancu klase YOYFData (vidjeti javadoc klase) i predati ju konstruktoru.
 * Metodu selectOldState potrebno je pozvati prije ažuriranja police osiguranja kako bi se dohvatilo staro stanje.
 * Nakon ažuriranja police osiguranja potrebno je pozvati metodu selectNewState kako bi se dohvatilo novo stanje.
 * Nakon dohvata stanja poziva se metoda insertIntoIpChgHistory koja provjerava da li je bilo promjena po polici osiguranja i ažurira povijest promjena.
 * @author hrakis
 */
public class YOYF0
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyF/YOYF0.java,v 1.4 2011/05/18 13:33:55 hrakis Exp $";

    private RemoteContext rc;
    private YOYF1 yoyF1;
    private YOYFData data;
    
    public YOYF0(RemoteContext rc, YOYFData data) throws Exception
    {
        this.rc = rc;
        this.yoyF1 = new YOYF1(rc);
        this.data = data;
    }

    /**
     * Metoda koja dohvaæa podatke o polici osiguranja. Ti podaci predstavljaju staro stanje police, pa je metodu potrebno pozvati PRIJE ažuriranja podataka police osiguranja.
     */
    public void selectOldState() throws SQLException
    {
        yoyF1.selectOldState(data);
    }

    /**
     * Metoda koja dohvaæa podatke o polici osiguranja. Ti podaci predstavljaju novo stanje police, pa je metodu potrebno pozvati POSLIJE ažuriranja podataka police osiguranja.
     */
    public void selectNewState() throws SQLException
    {
        yoyF1.selectNewState(data);
    }

    /**
     * Metoda provjerava da li se novo stanje police osiguranja razlikuje od starog stanja i ažurira povijest promjena ukoliko razlike postoje. 
     */
    public void insertIntoIpChgHistory() throws Exception
    {
        // provjeri valjanost danih podataka
        if(data == null) throw new Exception("Data objekt je null!");
        if(data.ip_id == null && data.col_ins_id == null) throw new Exception("Nije popunjen ni ip_id ni col_ins_id!");
        if(data.ip_id != null && data.col_ins_id != null) throw new Exception("Popunjeni su i ip_id i col_ins_id!");
        if(data.col_hea_id == null) throw new Exception("Mora biti popunjen col_hea_id!");
        if(data.use_id == null) throw new Exception("Mora biti popunjen use_id!");
        if(data.org_uni_id == null) throw new Exception("Mora biti popunjen org_uni_id!");
        
        // provjeri jesu li se promjenili podaci o polici i ako je potrebno ažuriraj povijest promjena 
        if(!objectEquals(data.old_clt_inspolst, data.new_clt_inspolst)) yoyF1.insertIntoIpChgHistory(data, "clt_inspolst", data.old_clt_inspolst, data.new_clt_inspolst);
        if(!objectEquals(data.old_clt_pol_spec_st, data.new_clt_pol_spec_st)) yoyF1.insertIntoIpChgHistory(data, "clt_pol_spec_st", data.old_clt_pol_spec_st, data.new_clt_pol_spec_st);
        if(!objectEquals(data.old_ip_wrn_status, data.new_ip_wrn_status)) yoyF1.insertIntoIpChgHistory(data, "ip_wrn_status", data.old_ip_wrn_status, data.new_ip_wrn_status);
        if(!objectEquals(data.old_ip_kmt_status, data.new_ip_kmt_status)) yoyF1.insertIntoIpChgHistory(data, "ip_kmt_status", data.old_ip_kmt_status, data.new_ip_kmt_status);
    }

    private boolean objectEquals(Object obj1, Object obj2)
    {
        return ( (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2)) );
    }
}