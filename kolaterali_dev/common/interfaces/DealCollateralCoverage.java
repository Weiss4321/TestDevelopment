/*
 * Created on 2007.11.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.interfaces;

import java.math.BigDecimal;
import java.sql.Date;

import hr.vestigo.modules.rba.interfaces.AbstractCommonInterface;

/**
 * 
 * Izracun pokrivenosti plasmana kolateralom
 * 
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface DealCollateralCoverage extends AbstractCommonInterface {
	
	/**
	 * oznaka RBA prihvatljivosti
	 */
	public final static int RBA=0;
	/**
	 * oznaka B1 prihvatljivosti
	 */
	public final static int B1=1;
	/**
	 * oznaka B2 prihvatljivosti
	 */
	public final static int B2=2;
	/**
	 * oznaka B2IRB prihvatljivosti
	 */
	public final static int B2IRB=3;
	
	/**
     * oznaka ND prihvatljivosti
     */
    public final static int ND=4;
    
    /**
     * oznaka RBA prihvatljivosti za Micro klijente
     */
    public final static int RBA_MICRO=5;

    
    
    /**
     * oznaka redovnih podataka po datumu valute analitike
     */
    public final static String DVA = "DVA";
    
    /**
     * oznaka podataka po datumu valute glavne knjige
     */
    public final static String DGK = "DGK";
    
    
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/interfaces/DealCollateralCoverage.java,v 1.6 2012/02/08 11:50:14 hrakis Exp $";
	
	/**
	 * inicijaliziranje commona startnim podacima
	 * 
	 * @param date datum valute
	 * @param exp_type_ind Flag koji govori da li su u tabeli redovni podaci o izloženosti, podaci za kraj mjeseca ili nešto drugo
	 * @param col_proc id obrade
	 * @param use_id user id
	 * @param debugOut ispis debug teksta
	 */
	public void init(Date date, String exp_type_ind, BigDecimal col_proc, BigDecimal use_id, boolean debugOut) throws Exception;
	
	/**
	 * izracun pokrivenosti za sve plasmane i kolaterale vezane za ulazni plasman
	 * 
	 * @param cus_acc_id partija plasmana
	 * @param ponded ponderiran ili neponderiran izracun
	 * @param eligibility prihvatljivost; vrijednosti su u statickim poljima ovog interface-a
	 * @return status izracuna
	 * @throws Exception
	 */
	public int execute(BigDecimal cus_acc_id, boolean ponded, int eligibility) throws Exception;
	/**
     * dodatak u izracun, u ovom modu se rezultati ne upisuju u bazu, samo se ispisu na SystemOut.
     * koristiti samo provjeru zasto neki plasmani nisu usli u izracun
     *
	 */
    public void useTestcaseCalculation();
	

}
