/*
 * Created on 2006.08.17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy4;

import java.math.BigDecimal;
import java.sql.Date;



/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HfPriorCheckData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy4/HfPriorCheckData.java,v 1.2 2014/11/12 14:13:48 hrazst Exp $";
	
	 public BigDecimal col_hea_id = null;

	 public BigDecimal hfs_value = null; 
	 public Date hfs_value_date = null;
	 
	 public BigDecimal 	hfs_value_last_one = null;
	 public Date hfs_date_last_one = null;
		
	 public BigDecimal third_right_nom = null;
	 public Date third_right_date = null;
	 public Date thi_date_last_unt = null; 
}
