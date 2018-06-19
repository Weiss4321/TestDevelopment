package hr.vestigo.modules.collateral.batch.bo31;

import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class ReportData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo31/ReportData.java,v 1.1 2015/05/08 11:45:45 hrakis Exp $";
    
    protected BatchContext bc;
    protected ConnCtx connCtx;
    
    public ArrayList<ArrayList<String>> columns = new ArrayList<ArrayList<String>>();
   
    public abstract boolean hasMortgage();

    public abstract String getFileName();
    
    public abstract void setColumns();
    

    public ReportData()
    {
        for (int i = 0; i < getNumberOfSheets(); i++)
        {
            this.columns.add(new ArrayList<String>());
        }
        this.setColumns();
    }
    
    public void setBatchContext(BatchContext batchContext) throws SQLException
    {
        this.bc = batchContext;
        this.connCtx = bc.getContext();
    }
    
    public int getNumberOfSheets()
    {
        return 2;
    }
    
    public void selectAdditionalData(HashMap data, Date value_date, YRXX0 yrxx0) throws Exception
    {
        
    }
    
    public void fillDataFromIterator(Object iter, HashMap<String, Object> data, Class reference) throws Exception
    {
        Class cls = iter.getClass();
        Method[] methods = cls.getDeclaredMethods();
        
        for (Method method : methods)
        {
            String methodName = method.getName();
            Object value = method.invoke(iter);
            
            String column = null;
            try
            {
                Field field = reference.getField(methodName);
                column = (String)field.get(null);
            }
            catch (NoSuchFieldException ex)
            {
                column = methodName;
            }
           
            data.put(column, value);
        }
    }
    
    
    /*public void fillDataFromObject(Object obj, HashMap<String, Object> data, Class reference) throws Exception
    {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        
        for (Field field : fields)
        {
            String fieldName = field.getName();
            Object value = field.get(obj);
            
            String column = null;
            try
            {
                Field refField = reference.getField(fieldName);
                column = (String)refField.get(null);
            }
            catch (NoSuchFieldException ex)
            {
                column = fieldName;
            }
           
            data.put(column, value);
        }
    }*/
    
    
    public String convertToUnicode(String text)
    {
        text = text.replaceAll("š", "\u0161"); 
        text = text.replaceAll("Š", "\u0160");
        text = text.replaceAll("æ", "\u0107");
        text = text.replaceAll("Æ", "\u0106");
        text = text.replaceAll("è", "\u010D");
        text = text.replaceAll("È", "\u010C");
        text = text.replaceAll("ž", "\u017E");
        text = text.replaceAll("Ž", "\u017D");
        text = text.replaceAll("ð", "\u0111");
        text = text.replaceAll("Ð", "\u0110");
        return text;
    }
}