//created 2017.02.13
package hr.vestigo.modules.collateral.common.yoyJ;

import hr.vestigo.modules.coreapp.share.DataObject;

/**
 *
 * @author hrazst
 */
public class OutputData extends DataObject<Object>  {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyJ/OutputData.java,v 1.1 2017/02/13 09:20:52 hrazst Exp $";
    
    /**
     * Šifra greške - 0 ako nema greške
     * 
     */
    public int errorCode=0;
    /**
     * Opis greške - prazno ako je šifra greške=0
     */
    public String errorDesc="";   
    /**
     * Šifra kolaterala
     */
    public String col_num=null;
    
    
}

