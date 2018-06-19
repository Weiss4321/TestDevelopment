package hr.vestigo.modules.collateral.batch.bo44;

import java.math.BigDecimal;
import java.sql.Date;

public class CollData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo44/CollData.java,v 1.2 2016/09/02 10:40:44 hrakis Exp $";

    BigDecimal ip_id;
    String b2asset_class;
    String oj_code;
    String oj_name;
    String col_num;
    String collateral_status; 
    String ip_code;
    String ic_name; 
    String osig_reg_no;
    String id_osiguranika;
    String osiguranik;
    Date valid_until;
    Date paid_from;
    Date paid_until;
    String valuta;
    BigDecimal osigurana_svota;
    BigDecimal akum_amount;
    String status_police;
    String ip_spec_stat;
    String spec_stat;
    String id_ugovaratelja;
    String ugovaratelj;
    String id_korisnika;
    String korisnik;
    String plasman;
    String dwh_status; 
    String status_u_modulu;
    BigDecimal use_id;
    
    String referent_login;
    String referent_name;
    String referent_org_code;
    String referent_org_name;
        
    String col_group;
}