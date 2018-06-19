//created 2014.04.02
package hr.vestigo.modules.collateral.batch.bo87;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Klasa koja sadr�i defaultne pondere po kategorijama, tipovima i vrstama
 * @author hraziv
 */
public class DefPonderData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo87/DefPonderData.java,v 1.1 2014/04/07 09:27:33 hraziv Exp $";
    
    /*�ifra kategorije*/
    public String category_code;
    /*Naziv kategorije*/
    public String category_name;
    /*�ifra tipa kolaterala*/
    public String type_code;
    /*Naziv tipa kolaterala*/
    public String type_name;
    /*�ifra vrste*/
    public String sub_type_code;
    /*Naziv vrste*/
    public String sub_type_name;
    /*Uvjet*/
    public String uvjet;
    /*Tip pondera*/
    public String ponder_type;
    /*Min ponder izra�en u %*/
    public BigDecimal min_value;
    /*Defaultni ponder izra�en u %*/
    public BigDecimal dfl_value;
    /*Max ponder izra�en u %*/
    public BigDecimal max_value;
    /*Status zapisa:A-aktivan, trenutno va�e�i,N-neaktivan*/
    public String status;
    /*Od kada vrijedi ponder*/
    public Date date_from;
    /*Do kada vrijedi ponder*/
    public Date date_until;
    /*OJ unosa sloga*/
    public String oj_unosa;
    /*Referent koji je unio zapis*/
    public String referent_unosa;
    /*Vrijeme unosa*/
    public Timestamp vrijeme_unosa;
    
    /** 
     * Konstruktor klase.
     */
    public DefPonderData()
    {
        
    }
}

