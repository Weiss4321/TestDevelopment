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
     * �ifra gre�ke - 0 ako nema gre�ke
     * 
     */
    public int errorCode=0;
    /**
     * Opis gre�ke - prazno ako je �ifra gre�ke=0
     */
    public String errorDesc="";   
    /**
     * �ifra kolaterala
     */
    public String col_num=null;
    
    
}

