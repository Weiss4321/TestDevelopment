//created 2014.09.25
package hr.vestigo.modules.collateral.util;

/**
 *
 * @author hrazst
 */
public class CollReqFieldDataCondition {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/CollReqFieldDataCondition.java,v 1.3 2016/08/23 10:37:21 hrazst Exp $";
    
    public String preConditionLDB = null;
    public String preConditionField = "";
    public Object preConditionValue = null;
    public boolean equals = true;
    
    public CollReqFieldDataCondition(){}

    public CollReqFieldDataCondition(String preConditionField, Object preConditionValue, boolean equals){
        this.preConditionField = preConditionField;
        this.preConditionValue = preConditionValue;
        this.equals=equals;
    }  
    
    public CollReqFieldDataCondition(String preConditionLDB,String preConditionField, Object preConditionValue, boolean equals){
        this.preConditionLDB = preConditionLDB;
        this.preConditionField = preConditionField;
        this.preConditionValue = preConditionValue;
        this.equals=equals;
    } 
}

