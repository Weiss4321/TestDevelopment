/*
 * Created on 2007.03.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo08;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.modules.collateral.batch.bo08.BO08Data;
import hr.vestigo.modules.collateral.batch.bo08.BO081;
//import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;
import java.util.HashMap;
import hr.vestigo.modules.collateral.common.yoy9.*;

import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;


/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO080 extends Batch {
	public static String cvsident =
		"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo08/BO080.java,v 1.13 2007/08/31 09:15:18 hratar Exp $";
	
	
	private BO081 bo081 = null;
	 
	private String [] ulazniParametri=null;
	
	private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
	
	private BatchContext bc = null;
	
	private ConnCtx ctx = null; 

	BO08Data dataVar;
	
	private int i = 0;
	
	private int size_collateral_id = 0; 
	
	private Date proc_date=null;
	
	private BigDecimal org_uni_id=null;
	
	private BigDecimal eve_typ_id=null;
	
	private hr.vestigo.modules.collateral.batch.bo08.BO082 bo082=null;
	
	private String bookingState;
	
	public BO080(){
		super();
		this.org_uni_id = new BigDecimal("53253");
		this.eve_typ_id = new BigDecimal("1702365003");
		this.proc_date = new Date(System.currentTimeMillis());
		size_collateral_id = 0;
		bookingState = new String("1");
	}
	
	
		
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("1702346003"));
		bp.setArgs(args);
		new BO080().run(bp);
	}			

	public String executeBatch(BatchContext bc) throws Exception {
		
		HashMap map = new HashMap();
		
		ResultSet resultSet=null;
		
		this.bo082 = new hr.vestigo.modules.collateral.batch.bo08.BO082();
		
		this.bc = bc;
		
		this.ctx = bc.getContext();
	
		bc.debug("******************** BO080.executeBatch() started *********************");
		
		this.bo081 =new BO081(bc);
		
		
		/**		da li je dobar broj parametra dobiven iz aplikacije, nije moguce dobiti 
		 * parametre s komandne linije
		 */ 
	    if(checkArgs()==false){
			return RemoteConstants.RET_CODE_ERROR;
	    }

		/**
		 * Stavlja ulazne parametre u pozadinski objekt (spremaju se kao konstante)
		 */
	    dataVar = new BO08Data(bc);
	   
	    
	    bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bo081.insertIntoEvent();
        }catch (Exception e) {
            e.printStackTrace();
            returnCode=RemoteConstants.RET_CODE_ERROR;
       }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
	    
     
        
        
        try {
        	
        	//Evidentirati izvoðenje obrade
        	insert_col_proc(bc);
        	
        	if (dataVar.getCategory().equals(new BigDecimal("618223")))//nekretnine
        	{
        		bc.debug("Pokretanje BO082 upita.");
            	resultSet = bo082.resultSetNekretnina(bc, dataVar);
            	bc.debug("Pokretanje BO082 upita uspjesno zavrseno.");
            	while (resultSet.next()) 
            	{
            		
//            		resetira varijable pozadinskog objekta koje cuvaju podatke o kolateralu
    				dataVar.reset_all_coll();
				
            		size_collateral_id++;
            		dataVar.setCol_hea_id(new BigDecimal(resultSet.getString(1)));
            		dataVar.setReal_est_nomi_valu(new BigDecimal(resultSet.getString(2)));
            		dataVar.setReal_est_nm_cur_id(new BigDecimal(resultSet.getString(3)));
            		dataVar.setReal_est_nomi_date(convertStringToDate(getDDMMYYYY(resultSet.getDate(4))));
            		dataVar.setReva_date_am(resultSet.getString(5));
            		dataVar.setReva_flag(resultSet.getString(6));
            		dataVar.setAccounting_indic(resultSet.getString(7));
            		System.out.println("->" + dataVar.getCol_hea_id());
            		bc.debug("Pokretanje revalorizacije.");
    				/**
    				 * metoda kojom se vrsi revalorizacija
    				 * */
    				reval(bc);
    			}
            	System.out.println("Velicina" + size_collateral_id);
    			// spremiti informaciju o izvedenoj obradi
    			bo081.updateCol_Proc(size_collateral_id);
            	
        	}
        	else 
        	{
        		Vector collateral_id = null;
			
        		//punjnje vektora collateral_id svim kolteralima koje ulaze u izraèun revalorizacije

        		collateral_id = bo081.selectColId(dataVar);
			
        		size_collateral_id = collateral_id.size();
        		System.out.println("Velicina" + size_collateral_id);
        		if (!collateral_id.isEmpty()) {
        			bc.info("Nije prazan vektor size:"+size_collateral_id);
        			dataVar.setCol_number(new BigDecimal(size_collateral_id));
        			for (i = 0; i < size_collateral_id; i++) {
        				System.out.println("->" + dataVar.getCol_hea_id());
        				//resetira varijable pozadniskog objekta koje cuvaju podatke o kolateralu
        				dataVar.reset_all_coll();
					
        				//uzima se jedan kolateral
        				Vector oneCollateral = (Vector) collateral_id.elementAt(i);
					
        				//puni se pozadinski objekt podacima o i-tom kolateralu
        				fill(oneCollateral);
					
					
        				bc.debug("Pokretanje revalorizacije.");
        				/**
        				 * metoda kojom se vrsi revalorizacija
        				 * */
        				reval(bc);
        			}
        			// spremiti informaciju o izvedenoj obradi
        			bo081.updateCol_Proc(size_collateral_id);
        		}
        		System.out.println("Velicina" + size_collateral_id);
        	}
        }catch (SQLException sqle){
            sqle.printStackTrace();
            returnCode=RemoteConstants.RET_CODE_ERROR;
        }catch (Exception e) {
            e.printStackTrace();
            returnCode=RemoteConstants.RET_CODE_ERROR;
        }
        
        
        return returnCode;
	}
	

	public void fill(Vector row) {	
		dataVar.setCol_hea_id((BigDecimal) row.elementAt(0));
		dataVar.setReal_est_nomi_valu((BigDecimal) row.elementAt(1));
		dataVar.setReal_est_nm_cur_id((BigDecimal) row.elementAt(2));
		dataVar.setReal_est_nomi_date((Date) row.elementAt(3));
		dataVar.setReva_date_am((String) row.elementAt(4));
		dataVar.setReva_flag((String) row.elementAt(5));
		dataVar.setAccounting_indic((String) row.elementAt(6));
	}
	
	
	public void	reval(BatchContext bc) throws Exception {
		bc.debug("reval Revalorization method");
		String pomString=null;
		
		int i=1;
		
	
		
		try {
			
			bo081.revaluation(dataVar);				
			
			// Proknjižiti izraèunatu revalorizaciju
			booking(bc);
			
			//izraèun za svaki kolateral spremiti u prometnu evidenciju  obraèuna
			bo081.insertIntoTrafficEvidence(dataVar,bookingState);

		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	}
	/**knjiženje, knjiži se izraèunati iznos revalorizacije, uveæanje tržišne vrijednosti kolaterla
	*poziva se common ZA KNJIŽENJE
	*import hr.vestigo.modules.collateral.common.yoy9.*;
	*/
	public void booking(BatchContext bc) throws Exception {
		if (dataVar.getAccounting_indic().equalsIgnoreCase("1")) {
			try {
				YOY90 YOY90=new YOY90(bc); 
				YOY90.CollPosting(dataVar.getCol_hea_id(),false);
			} catch (Exception e) {
				System.out.println("sql pogreska u BO080 booking");
			
				e.printStackTrace();
		 	}
			bookingState = new String("2");
		}
     }
	

	private boolean checkArgs(){
		
		String pomString=null;
		String delimiter=null;
		int brojac=0;
		int brojac1=0;
		int pozicija=0;
		int i=0;
		
		for(i=0;i<bc.getArgs().length;i++){
			bc.debug("bc_Arg[" + String.valueOf(i) + "]=" + bc.getArg(i));
		}
		
		if (bc.getArgs().length!=11){
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		//parametri se ne mogu dobiti sa komanden linije 
		
		
		return true;
	}
	
	
	
    private String getDDMMYYYY(Date date) {
        String dateString = "";
        if (date == null) {
            return dateString;
        }
        
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;
        String dayString;
        String monthString;

        if (day < 10) {
            dayString = "0" + day;
        }
        else {
            dayString = "" + day;
        }

        if (month < 10) {
            monthString = "0" + month;
        }
        else {
            monthString = "" + month;
        }

        dateString = dayString + "." + monthString + "." + year;

        return dateString;
    }
    
   
   //insert sloga u tabelu COL_PROC
    public void insert_col_proc(BatchContext bc) throws Exception {
    	HashMap map = new HashMap();
		map.put("proc_date",proc_date);
		map.put("value_date",dataVar.getDateOfReval()); 
		map.put("proc_type","R");    
		map.put("proc_way","A");                
		map.put("proc_status","0"); 
		map.put("col_number",new BigDecimal(0));            
		//map.put("proc_ts",proc_ts);     
		map.put("org_uni_id",new BigDecimal("53253"));
		map.put("use_id", bc.getUserID()); 
		map.put("user_lock", "");
	
			bo081.insertColProc(map);
		
	}
    private Date convertStringToDate(String dataString){
		
		String pomString=null;
		String dan=null;
		String mjesec=null;
		String godina=null;
		
		dan=dataString.substring(0,2);
		mjesec=dataString.substring(3,5);
		godina=dataString.substring(6,10);
		
		pomString=godina + "-" + mjesec + "-" + dan;
		return Date.valueOf(pomString);
		
	}
    
}
