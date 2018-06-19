package hr.vestigo.modules.collateral.common.yoyF;

import java.math.BigDecimal;

/**
 * Data objekt koji se koristi kod bilježenja promjene statusa polica osiguranja.<br/><br/>
 * Potrebno je popuniti sljedeæa polja:
 * <ul>
 *   <li>iskljuèivo jedno od polja: ip_id, col_ins_id</li>
 *   <li>use_id i org_uni_id</li>
 * </ul>
 * @author hrakis
 */
public class YOYFData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyF/YOYFData.java,v 1.2 2011/05/18 13:33:55 hrakis Exp $";

    //
    // polja koja popunjava korisnik commona
    //
    
    /** ID police osiguranja koja osigurava kolateral. */
    public BigDecimal ip_id;
    
    /** ID police osiguranja koja je kolateral. */
    public BigDecimal col_ins_id;
    
    /** ID referenta koji je mijenjao status. */
    public BigDecimal use_id;

    /** ID organizacijske jedinice referenta koji je mijenjao status. */
    public BigDecimal org_uni_id;

    /** ID dogaðaja koji je mijenjao status. */
    public BigDecimal eve_id;

    //
    // stare vrijednosti polja police osiguranja
    // popunjavaju se metodom selectOldState(), a može ih popuniti i korisnik commona
    //
    
    /** Stara vrijednost statusa police. */    
    public String old_clt_inspolst;
    
    /** Stara vrijednost napomene o polici osiguranja. */
    public String old_clt_pol_spec_st;
    
    /** Stara vrijednost statusa slanja opomena. */
    public String old_ip_wrn_status;
    
    /** Stara vrijednost statusa promjene kamate. */
    public String old_ip_kmt_status;
    
    //
    // nove vrijednosti polja police osiguranja
    // popunjavaju se metodom selectNewState(), a može ih popuniti i korisnik commona
    //

    /** Nova vrijednost statusa police. */
    public String new_clt_inspolst;
    
    /** Nova vrijednost napomene o polici osiguranja. */
    public String new_clt_pol_spec_st;

    /** Nova vrijednost statusa slanja opomena. */
    public String new_ip_wrn_status;

    /** Nova vrijednost statusa promjene kamate. */
    public String new_ip_kmt_status;
    
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
}