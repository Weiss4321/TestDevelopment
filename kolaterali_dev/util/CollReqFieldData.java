//created 2014.09.25
package hr.vestigo.modules.collateral.util;

import java.util.Vector;

/**
 *
 * @author hrazst
 */
public class CollReqFieldData {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/CollReqFieldData.java,v 1.2 2016/08/23 10:37:21 hrazst Exp $";
    
    public String screenName = "";
    public String ldb = "";
    public String field = "";
    public String msg = "";
    public String messageID = null;
    public String context = null;
    public Vector<CollReqFieldDataCondition> preConditions=null;
    
    public CollReqFieldData(){}

    public CollReqFieldData(String screenName, String ldb, String field, String msg){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
    } 

    public CollReqFieldData(String screenName, String ldb, String field, String msg, String messageID){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
        this.messageID = messageID;
    } 
    
    public CollReqFieldData(String screenName, String ldb, String field, String msg, String messageID, String context){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
        this.messageID = messageID;
        this.context = context;
    } 
    
    public CollReqFieldData(String screenName, String ldb, String field, String msg, Vector<CollReqFieldDataCondition> preConditions){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
        this.preConditions=preConditions;
    }
    
    public CollReqFieldData(String screenName, String ldb, String field, String msg, Vector<CollReqFieldDataCondition> preConditions, String messageID){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
        this.preConditions=preConditions;
        this.messageID = messageID;
    }  
    
    public CollReqFieldData(String screenName, String ldb, String field, String msg, Vector<CollReqFieldDataCondition> preConditions, String messageID, String context){
        this.screenName = screenName;
        this.ldb = ldb;
        this.field = field;
        this.msg = msg;
        this.preConditions=preConditions;
        this.messageID = messageID;
        this.context = context;
    }  
}

