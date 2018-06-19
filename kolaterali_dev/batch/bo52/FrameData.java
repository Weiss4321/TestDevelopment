package hr.vestigo.modules.collateral.batch.bo52;

import java.math.BigDecimal;
import java.util.*;


/**
 * Klasa koja predstavlja okvir.
 * @author hrakis
 */
public class FrameData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo52/FrameData.java,v 1.1 2010/09/22 08:30:45 hrakis Exp $";

    /** ID partije okvira. */
    public BigDecimal cus_acc_id;

    /** Partija okvira. */
    public String cus_acc_no;

    /** Skup svih grupa kolaterala. */
    public TreeMap<Integer, CollateralCategoryData> collateralCategories;

    /** Skup svih plasmana iz okvira. */
    public Vector<AccountData> accounts;
    
    /**
     * Konstruktor klase.
     * @param cus_acc_id ID partije okvira.
     * @param cus_acc_no Partija okvira.
     */
    public FrameData(BigDecimal cus_acc_id, String cus_acc_no)
    {
        this.cus_acc_id = cus_acc_id;
        this.cus_acc_no = cus_acc_no;
        this.collateralCategories = new TreeMap<Integer, CollateralCategoryData>();
        this.accounts = new Vector<AccountData>();
    }

    /** Vraæa sumu iznosa izloženosti (u kunama) svih plasmana iz okvira. */
    public BigDecimal getTotalExposureAmountHRK()
    {
        BigDecimal sum = BigDecimal.ZERO;
        for(int i = 0; i < accounts.size(); i++) sum = sum.add(accounts.get(i).exposureAmountHRK);
        return sum;
    }
    
    public String toString()
    {
        return "OKVIR: cus_acc_no=" + cus_acc_no + ", br.plasmana=" + accounts.size() + ", br.kategorija=" + collateralCategories.size();
    }
}


/**
 * Klasa koja predstavlja grupu kolaterala. 
 */
class CollateralCategoryData
{
    /** Prioritet tipa kolaterala. */
    public Integer priority;
    
    /** Skup kolaterala koji pripadaju grupi. */
    public Vector<CollateralData> collaterals;
    
    /**
     * Konstruktor klase.
     * @param priority Prioritet tipa kolaterala.
     */
    public CollateralCategoryData(Integer priority)
    {
        this.priority = priority;
        this.collaterals = new Vector<CollateralData>();
    }
    
    /** Vraæa sumu iznosa (u kunama) svih kolaterala iz grupe koji pokrivaju izloženost okvira. */
    public BigDecimal getTotalCoverageAmountHRK()
    {
        BigDecimal sum = BigDecimal.ZERO;
        for(int i = 0; i < collaterals.size(); i++) sum = sum.add(collaterals.get(i).coverageAmountHRK);
        return sum;
    }
    
    public String toString()
    {
        return "KATEGORIJA: prioritet=" + priority;
    }
}


/**
 * Klasa koja predstavlja kolateral koji pokriva okvir.
 */
class CollateralData
{
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** Prioritet tipa kolaterala. */
    public Integer priority;
    
    /** Iznos kolaterala (u valuti izloženosti okvira) koji pokriva iznos izloženosti okvira. */
    public BigDecimal coverageAmount;
    
    /** Valuta u kojoj je izražena izloženost okvira. */
    public BigDecimal coverageCurrency;
    
    /** Iznos kolaterala (u kunama) koji pokriva iznos izloženosti okvira. */
    public BigDecimal coverageAmountHRK;
    
    public String toString()
    {
        return "KOLATERAL: col_hea_id=" + col_hea_id + ", col_num=" + col_num + ", iznos=" + coverageAmountHRK + " kn";
    }
}


/**
 * Klasa koja predstavlja plasman iz okvira.
 */
class AccountData
{
    /** ID partije plasmana iz okvira. */
    public BigDecimal cus_acc_id;
    
    /** Partija plasmana iz okvira. */
    public String cus_acc_no;
    
    /** Iznos izloženosti plasmana (u valuti izloženosti). */
    public BigDecimal exposureAmount;
    
    /** Valuta u kojoj je izražena izloženost plasmana. */
    public BigDecimal exposureCurrency;
    
    /** Iznos izloženosti plasmana (u kunama). */
    public BigDecimal exposureAmountHRK;
    
    public String toString()
    {
        return "PLASMAN: cus_acc_id=" + cus_acc_id + ", cus_acc_no=" + cus_acc_no + ", izlozenost=" + exposureAmountHRK + " kn";
    }
}