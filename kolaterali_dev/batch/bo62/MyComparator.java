//created 2011.11.28
package hr.vestigo.modules.collateral.batch.bo62;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author hradnp
 */
public class MyComparator implements Comparator {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo62/MyComparator.java,v 1.1 2011/11/28 14:03:42 hradnp Exp $";

    @Override
    public int compare(Object object1, Object object2) {
        
        String[] s1 = (String[])object1;
        String[] s2 = (String[])object2;
        
        Collator c = Collator.getInstance(new Locale("hr"));
        
        return c.compare(s1[0], s2[0]);
    }
}

