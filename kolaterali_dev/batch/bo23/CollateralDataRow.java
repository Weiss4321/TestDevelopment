package hr.vestigo.modules.collateral.batch.bo23;

import java.math.BigDecimal;
import java.sql.Date;

public class CollateralDataRow {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo23/CollateralDataRow.java,v 1.7 2012/04/26 13:30:30 hramkr Exp $";
	
	public long id=-1;
	
	public String owner_code=null;       
	public String owner_name=null;       
	public String coll_type_name=null;   
	public String coll_num=null;         
	public String cur_char=null; 
	public BigDecimal coll_value=null;
	public BigDecimal value=null;        
	public BigDecimal value_eur=null;    
	public String eligibility=null;
	public String b2_irb_elig=null;
	public Date rba_first_date_from=null;
	public Date rba_last_date_until=null;
	public Date due_date=null;
 
	public String toString(){
		String result="";
		result+= "\n owner_code= "+owner_code;                  
		result+= "\n owner_name= "+owner_name;                  
		result+= "\n coll_type_name= "+coll_type_name;          
		result+= "\n coll_num= "+coll_num;  
		result+= "\n due_date= "+due_date;
		result+= "\n cur_char= "+cur_char;  
		result+= "\n coll_value= "+coll_value;
		result+= "\n value= "+value;                            
		result+= "\n value_eur= "+value_eur;                    
		result+= "\n eligibility= "+eligibility; 
		result+= "\n b2_irb_elig= "+b2_irb_elig;
		result+= "\n rba_first_date_from= "+rba_first_date_from;
		result+= "\n rba_last_date_until= "+rba_last_date_until;
		
		return result;
	}
	
	public void trim(){
		if (owner_code!=null) {
			owner_code=owner_code.trim();
		}
		if (owner_name!=null) {
			owner_name=owner_name.trim();
		}
		if (coll_type_name!=null) {
			coll_type_name=coll_type_name.trim();
		}
		if (coll_num!=null) {
			coll_num=coll_num.trim();
		}
		if (cur_char!=null) {
			cur_char=cur_char.trim();
		}
		if (eligibility!=null) {
			eligibility=eligibility.trim();
		}
	      if (b2_irb_elig!=null) {
	          b2_irb_elig=b2_irb_elig.trim();
	        }
		
		
	}
	
	/**
	 * Redak koji se vraca za kolateral izvjesce
	 * 
	 * @return
	 */
	public String getCVSRow(){
		StringBuffer buffer=new StringBuffer("");
		buffer.append(owner_code).append(";");
		buffer.append(owner_name).append(";");
		buffer.append(coll_type_name).append(";");
		buffer.append(coll_num).append(";");
		buffer.append(due_date).append(";");
		buffer.append(cur_char).append(";");
		buffer.append(value).append(";");
		buffer.append(value_eur).append(";");
		buffer.append(eligibility).append(";");
		buffer.append(b2_irb_elig).append(";");
		buffer.append(rba_first_date_from).append(";");
		buffer.append(rba_last_date_until);
		
		return buffer.toString();
	}
	
	/**
	 * dio retka za plasman izvjesce; nema podatke o datum od do hipoteka
	 * 
	 * @return
	 */
	public String getShortCVSRow(){
		StringBuffer buffer=new StringBuffer("");
		buffer.append(owner_code).append(";");
		buffer.append(owner_name).append(";");
		buffer.append(coll_type_name).append(";");
		buffer.append(coll_num).append(";");
		buffer.append(due_date).append(";");
		buffer.append(cur_char).append(";");
		buffer.append(value).append(";");
		buffer.append(value_eur).append(";");
		buffer.append(eligibility).append(";");
	    buffer.append(b2_irb_elig);
		
		
		return buffer.toString();
	}
	
	public boolean identified(long ident){
		return (id==ident);
	}
	
}
