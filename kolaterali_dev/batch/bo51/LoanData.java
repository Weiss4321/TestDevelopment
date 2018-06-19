package hr.vestigo.modules.collateral.batch.bo51;
import java.sql.*;

public class LoanData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo51/LoanData.java,v 1.1 2010/03/24 13:16:22 hrakis Exp $";
    
    public String cus_acc_no;
    public Date exposure_date;
    public String module_code;
    public String flag;
    
    public LoanData()
    {
    }
    
    public LoanData(String cus_acc_no, Date exposure_date, String module_code, String flag)
    {
        this.cus_acc_no = cus_acc_no;
        this.exposure_date = exposure_date;
        this.module_code = module_code;
        this.flag = flag;
    }
}