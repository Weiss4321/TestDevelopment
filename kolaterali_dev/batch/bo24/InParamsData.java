package hr.vestigo.modules.collateral.batch.bo24;

import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;

public class InParamsData {
    
    public String client_type;
    public BigDecimal organization_unit;
    public String vehicle_flag;
    public Date date_from;
    public Date date_until;
    //public String customer_type;

    
    public void setDateFrom(String date_from) {
        if (date_from!=null && !date_from.trim().equals("")  && DateUtils.isOmegaDate(date_from)){
            this.date_from  = DateUtils.convertOmegaDate(date_from);
        }else{
            this.date_from = Date.valueOf("1970-01-01");
        }
    }
    
    public void setDateUntil(String date_until) {
        if (date_until!=null && !date_until.trim().equals("")  && DateUtils.isOmegaDate(date_until)){
            this.date_until  = DateUtils.convertOmegaDate(date_until);
        }else{
            this.date_until = Date.valueOf("9999-12-31");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("client_type :"+client_type);
        buffer.append("organization_unit :"+organization_unit);
        buffer.append("date_from :"+date_from);
        buffer.append("date_until :"+date_until);
        buffer.append("vehicle_flag :"+vehicle_flag);
        return buffer.toString();
        
    }
    
}
