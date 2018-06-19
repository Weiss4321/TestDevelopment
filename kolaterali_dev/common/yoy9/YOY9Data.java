/*
 * Created on 2007.02.05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy9;

import java.math.BigDecimal;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class YOY9Data {

		public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy9/YOY9Data.java,v 1.4 2008/04/08 11:58:54 hrazst Exp $";
	
		/**
		 * Collateral id
		 */	
		public BigDecimal colHeaId=null;
		
        /**
         * Collateral type id
         */ 
        public BigDecimal colTypeId=null;      
                
		/**
		 * Jedinstveni identifikacijski broj collaterala
		 */
		public String colNum=null;
		
		/** 
		 * Nominalna vrijednost collaterala kuju je unio collateral officer
		 */	
		public BigDecimal realEstNomiValu=null;
		
		/**
		 * Id valute vrijednosti collaterala
		 */	
		public BigDecimal realEstNmCurId=null;
		
		/**
		 * Id referenta koji je unio collateral
		 */	
		public BigDecimal useOpenId=null;
		
		/**
		 * Id organizacijske jedinice referenta
		 */	
		public BigDecimal originOrgUniId=null;
		
		/**
		 * Id tipa collaterala
		 */	
		public BigDecimal colCatId=null;
		
		public YOY9Data() {
			super();
		}
        
        public String toString(){
            StringBuffer buffy=new StringBuffer();
            
            buffy.append("colHeaId=").append(colHeaId).append(",\n");
            buffy.append("colTypeId=").append(colTypeId).append(",\n");
            buffy.append("colNum=").append(colNum).append(",\n");
            buffy.append("realEstNomiValu=").append(realEstNomiValu).append(",\n");
            buffy.append("realEstNmCurId=").append(realEstNmCurId).append(",\n");
            buffy.append("useOpenId=").append(useOpenId).append(",\n");
            buffy.append("originOrgUniId=").append(originOrgUniId).append(",\n");
            buffy.append("colCatId=").append(colCatId);
            
            return buffy.toString();
        }
        
        
        
        
}
