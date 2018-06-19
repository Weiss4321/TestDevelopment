/*
 * Created on 2007.01.30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo06;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import hr.vestigo.modules.rba.util.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.common.yoy9.*;




/**
 * @author hrazvh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO060   extends Batch {
	public static String cvsident =
		"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo06/BO060.java,v 1.9 2007/06/06 08:30:37 hratar Exp $";
	
	private BatchContext bc = null;
	private java.text.SimpleDateFormat hr = new java.text.SimpleDateFormat("dd.MM.yyyy");
	private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
	private BigDecimal eve_id = null;
	private BigDecimal col_hea_id = null;
	private BigDecimal yearly_amort_rate = null;
	private BigDecimal yearly_amortisation = null;
	private BigDecimal yearly_amortisation_12 = null;
	private BigDecimal amortisation_for_period = null;
	private BigDecimal month_number = null;
	private java.sql.Date process_date = null;
	private java.sql.Date value_date = null;
	private java.sql.Date process_date_pom = null;
	private java.sql.Date value_date_pom = null;
	private String pocetni_datum = "";
	private String value_date_s = "";
	private BO061 bo061;
	private int length;
	private int month_number_int;
	private int i = 0;
	private int size_collateral_id = 0; 
	private  int ActualNoOfMonths;
	private Timestamp current_timestamp = null; 
	BO06Data p_data = new BO06Data();
	private YOY90 YOY90 = null;
	private String bookingState;
	
	public String executeBatch(BatchContext bc) throws Exception {
		System.out.println("Usao u execute bacth!!!!BO060");
		//inicijalizacija
		bo061 = new BO061(bc);
		YOY90 YOY90=new YOY90(bc); 
		value_date_s = bc.getArg(1);
		bookingState = new String("0");
		//postavljanje value_date i proces_date-a
		set_dates(bc);
		System.out.println("java.sql.Date process_date je " + process_date);
		
        try {
        	System.out.println("Radim bo061.insertIntoEvent();");
        	eve_id = bo061.insertIntoEvent();
        	System.out.println("eve id je " + eve_id);
        	
        	//provjera da li postoji vec obrada za taj datum
        	int status_counter = bo061.selectCol_Proc(process_date);

			System.out.println("int status_counter =" + status_counter);

			if (status_counter != 0) {
				
				System.out.println("Obrada vec postoji za zadani datum");
				
			}else{
				System.out.println("Ne postoji obrada  za zadani datum");
				
				//0. insert u col_proc da je obrada zapocela
				
				insert_col_proc(bc);
				
				// 1. obrada
				
				Vector collateral_id = null;
				
				collateral_id = bo061.selectCol_id(p_data);
				
				size_collateral_id = collateral_id.size();
				
				p_data.col_number = new BigDecimal(size_collateral_id);
				
				if (!collateral_id.isEmpty()) {
					bc.info("Nije prazan vektor size:"+size_collateral_id);
					
					for (i = 0; i < size_collateral_id; i++) {
						
						reset_object(bc);
						
						Vector row = (Vector) collateral_id.elementAt(i);
						
						p_data.col_hea_id =(BigDecimal) row.elementAt(0);
						p_data.real_est_nomi_valu =(BigDecimal) row.elementAt(1);
						p_data.real_est_nm_cur_id =(BigDecimal) row.elementAt(2);
						p_data.real_est_nomi_dat =(Date) row.elementAt(3);
						//p_data.val_per_min =(Date) row.elementAt(4);
						p_data.val_per_min =(String) row.elementAt(4);
						p_data.amort_flag =(String) row.elementAt(5);
						p_data.amort_age =(String) row.elementAt(6);
						p_data.amort_pst =(BigDecimal) row.elementAt(7);
						p_data.amort_per =(String) row.elementAt(8);
						p_data.accounting_indic =(String) row.elementAt(9);
						//2 . odluka da li se radi racun amortizacija
						
						/*
						int no_days = DateUtils.ActualNoDays(p_data.real_est_nomi_dat,p_data.val_per_min);
						Integer pom = new Integer(no_days); 
						Date pom_date = DateUtils.addOrDeductDaysFromDate(p_data.real_est_nomi_dat,pom,true );
						*/
						
						//pom_date je datum promjene+period u kojem treba napraviti novu procjenu 
						Date pom_date = DateUtils.addOrDeductDaysFromDate(p_data.real_est_nomi_dat,new Integer((p_data.val_per_min).trim()),true);
						
						if (pom_date.before(process_date)){
							System.out.println("Amortizacija se racuna col_hea_id:"+p_data.col_hea_id);
							
							amortisation();
							
							
						} else{
							System.out.println("Amortizacija se NE radi col_hea_id:"+p_data.col_hea_id);
							
							
							
						
						
						}
						
					}
					
					
					//update tablice COL_PROC
					bo061.updateCol_Proc(p_data);
					
				}else{
					
					bc.info("Prazan uvjet nema sto kreirati");
					
				}
				
			
			}
        }catch (Exception e) {
            e.printStackTrace();
            returnCode = RemoteConstants.RET_CODE_FATAL;
            return returnCode;
        
        }
        return returnCode;
	}
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("1688859003"));
		bp.setArgs(args);
		new BO060().run(bp);
	}
	public void amortisation(){
		
		BigDecimal pom_decimal = new java.math.BigDecimal("0.01");
		p_data.amort_age_decimal = new java.math.BigDecimal((p_data.amort_age).trim());
		yearly_amort_rate = pom_decimal.multiply(p_data.amort_age_decimal);
		System.out.println("yearly_amort_rate:"+yearly_amort_rate);
		
		yearly_amortisation = yearly_amort_rate.multiply(p_data.real_est_nomi_valu);
		
		yearly_amortisation_12 = yearly_amortisation.divide(new java.math.BigDecimal("12.00"), 2, BigDecimal.ROUND_HALF_UP);
		
		
		//broj mjeseci je od REAL_EST_NOMI_DAT do datuma obrade
		
		month_number_int = ActualNoMonths(p_data.real_est_nomi_dat,p_data.proces_date);
		
		month_number = new java.math.BigDecimal(month_number_int);
		
		amortisation_for_period = yearly_amortisation_12.multiply(month_number);
		
		p_data.proc_period = ""+month_number_int;
		p_data.proc_status = "1";
		
		p_data.col_turnover_amount = p_data.real_est_nomi_valu;
		p_data.col_turnover_amount_proc  =  amortisation_for_period;
		p_data.proc_perc = yearly_amort_rate;
		
		try {	
			bo061.get_file_data(p_data);
			
			System.out.println("Prosao dohvata podataka ide insert Col turnover");
			
			bo061.insertCol_Turnover(p_data);
			
			System.out.println("ide update Col head p_data.col_hea_id:"+p_data.col_hea_id);
			
						bo061.updateCol_Head(p_data);
			
			bookingState = new String("1");	//izvrsena amortizacija uspjesno
			//promjene MIlka poslala mail 22.02.2007
			// YOY90.CollPosting(bc,p_data.col_hea_id);
			//YOY90.CollPosting(p_data.col_hea_id);
			if (p_data.accounting_indic.equalsIgnoreCase("1")) {
						
				try {
				
					YOY90.CollPosting(p_data.col_hea_id,false);
				} catch (Exception e) {
					// TODO: handle exception
//					throw new VestigoTMException(1, "Transaction CO214: Greska kod knjizenja! ","colErr015", null);
					throw e;
				}
				bookingState = new String("2");
			}
			System.out.println("ide update Col turnover co_tur_id:"+p_data.col_tur_id);
			
			bo061.updateCol_Turnover(p_data,bookingState);
			
	 	} catch(Exception e) {	
	 		System.out.println("Greska u dohvatu podataka :"+e);
	 	}	
	 	
	}
	
	public java.sql.Date datum(String date){
		
		if((date != null) && (!date.trim().equalsIgnoreCase(""))) {
			String tmp = (date.substring(6,10))+"-"+(date.substring(3,5))+"-"+(date.substring(0,2));
			java.sql.Date dat = java.sql.Date.valueOf(tmp);
			return dat;
		}
		
		return null;
	}
	public void insert_col_proc(BatchContext bc){
		
		p_data.use_id = bc.getUserID();
		p_data.proces_date = process_date;
		p_data.org_uni_id =  new BigDecimal("53253");
		
		try{
			
			bo061.insertCol_Proc(p_data);
			
		}catch (SQLException e) {
			System.out.println("sql pogreska u updateCol_Proc");
			e.printStackTrace();
	 	}
		
	}
	public void reset_object(BatchContext bc){
		
		p_data.coll_id = null;
		p_data.amort_age = null;
		p_data.amort_pst = null;
		p_data.real_est_nomi_valu = null;
		
	}
	public void set_dates(BatchContext bc){
		value_date = datum(value_date_s);
		p_data.value_date = value_date;
		
		p_data.proces_date = new java.sql.Date(System.currentTimeMillis());
		process_date = p_data.proces_date;
		System.out.println("java.sql.Date value_date je " + p_data.value_date);
		System.out.println("java.sql.Date process_date je " + p_data.proces_date);
	}
	public int ActualNoMonths(java.sql.Date Date1, java.sql.Date Date2){

		GregorianCalendar calendar1 = new GregorianCalendar();
		GregorianCalendar calendar2 = new GregorianCalendar();

		calendar1.setTime(Date1);
		calendar2.setTime(Date2);

		int i = 1;
		while (true) {

			calendar1.add(Calendar.MONTH, 1);
			if (calendar1.getTime().getMonth() == calendar2.getTime().getMonth()) {

				break;
			}
			i++;
		}
		int godina1 = calendar1.getTime().getYear();
		int godina2 = calendar2.getTime().getYear();
		
		int broj_godina = godina2-godina1;
		//System.out.println("broj_godina!"+broj_godina);
		
		ActualNoOfMonths = i + 12*broj_godina;		


		return ActualNoOfMonths;
	}
}
