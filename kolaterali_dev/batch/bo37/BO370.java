package hr.vestigo.modules.collateral.batch.bo37;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo37.BO371.DeactivatedCollateralsIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;


/**
 *
 * Izvješæe o slobodnim/deaktiviranim kolateralima.
 * @author hrakis
 * 
 */
public class BO370 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo37/BO370.java,v 1.3 2014/03/10 13:28:43 hrakis Exp $";
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO370 pokrenut.");
		BO371 bo371 = new BO371(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo371.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// provjera parametara
        if(!checkParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		// dohvatiti ID i datum zadnje izvedene obrade za deaktivaciju/osloboðenje kolaterala
		BigDecimal col_pro_id = null;
		Date value_date = null;
		Vector vect = bo371.selectColProId();
		if(vect == null) return RemoteConstants.RET_CODE_ERROR;
		if(vect.get(0) != null) col_pro_id = (BigDecimal)vect.get(0);
		if(vect.get(1) != null) value_date = (Date)vect.get(1);
		bc.debug("col_pro_id = " + col_pro_id);
		bc.debug("value_date = " + value_date);
		
		// dohvat deaktiviranih/osloboðenih kolaterala
		DeactivatedCollateralsIterator iter = bo371.selectDeactivatedCollaterals(col_pro_id);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
		// stvaranje streamwritera za pravne i fizièke osobe
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
		String fileNamePravne = bc.getOutDir() + "/" + "pravne_osobe" + dateString + ".csv";
		String fileNameFizicke = bc.getOutDir() + "/" + "fizicke_osobe" + dateString + ".csv";
		OutputStreamWriter streamWriterPravne = new OutputStreamWriter(new FileOutputStream(new File(fileNamePravne)), "Cp1250");
    	OutputStreamWriter streamWriterFizicke = new OutputStreamWriter(new FileOutputStream(new File(fileNameFizicke)), "Cp1250");
    	bc.debug("Stvoreni streamwriteri.");
    	
    	// zapisivanje zaglavlja u izlazne datoteke
    	streamWriterPravne.write(getHeader(value_date));
    	streamWriterFizicke.write(getHeader(value_date));
    	bc.debug("Zapisana zaglavlja.");
    	
    	// zapisivanje podataka u izlazne datoteke
    	int indexPravne = 1;
    	int indexFizicke = 1;
    	
    	// id za depozite, garancije, obveznice, zapisi
    	BigDecimal dep_col_cat_id = new BigDecimal("612223");
        BigDecimal gar_col_cat_id = new BigDecimal("615223");
        BigDecimal obv_col_cat_id = new BigDecimal("619223");
        BigDecimal zap_col_cat_id = new BigDecimal("627223");
    	
    	while(iter.next())
		{
			bc.debug("Kolateral col_num=" + iter.col_num());
			
			// dohvati interni MB i naziv vlasnika kolaterala 
			String owner_register_no = null;
			String owner_name = null;
			String dwh_status = null;
			String status_u_modulu = null;
	        Date cde_dep_unti = null;
	        String cde_account = null;
	        String prolongat = null;
	        String status = null;
	        Date respiro_date = null;
	        Date guar_datn_unti = null;
	        Date maturity_date = null;
			
			Vector vectOwner = bo371.selectCollateralOwner(iter.col_hea_id());
			Vector vectPlasman = bo371.selectPlasman(iter.cus_acc_no());
			Vector vectDepozit = null;
	        Vector vectGarancija = null;
	        Vector vectObveznica = null;

			
			if(iter.col_cat_id().compareTo(dep_col_cat_id) == 0) {
	            vectDepozit = bo371.selectCashDep(iter.col_hea_id());			    
			}
			if(iter.col_cat_id().compareTo(gar_col_cat_id) == 0) {
			    vectGarancija = bo371.selectGarancija(iter.col_hea_id());               
	        }
	        if(iter.col_cat_id().compareTo(obv_col_cat_id) == 0 || iter.col_cat_id().compareTo(zap_col_cat_id) == 0) {
	            vectObveznica = bo371.selectObveznica(iter.col_hea_id());               
	        }

			 
			if(vectOwner != null)
			{
				if(vectOwner.get(0) != null) owner_register_no = (String)vectOwner.get(0);
				if(vectOwner.get(1) != null) owner_name = (String)vectOwner.get(1);
			}
			
			if(vectPlasman != null)
	        {
	             if(vectPlasman.get(0) != null) dwh_status = (String)vectPlasman.get(0);
	             if(vectPlasman.get(1) != null) status_u_modulu = (String)vectPlasman.get(1);
	        }
	         
	        if(vectDepozit != null)
	        {
                if(vectDepozit.get(0) != null) cde_dep_unti = (Date)vectDepozit.get(0);
                if(vectDepozit.get(1) != null) cde_account = (String)vectDepozit.get(1);
                if(vectDepozit.get(2) != null) prolongat = (String)vectDepozit.get(2);
                if(vectDepozit.get(3) != null) status = (String)vectDepozit.get(3);
	        }
	         
	        if(vectGarancija != null)
	        {
                if(vectGarancija.get(0) != null) respiro_date = (Date)vectGarancija.get(0);
                if(vectGarancija.get(1) != null) guar_datn_unti = (Date)vectGarancija.get(1);
	        }
	        
	        if(vectObveznica != null)
	        {
                if(vectObveznica.get(0) != null) maturity_date = (Date)vectObveznica.get(0);
            }
	        
        	// zapisivanje jednog retka u odgovarajuæu datoteku
			if(iter.b2_type().trim().equals("1") || iter.b2_type().trim().equals("20"))  // komitent je fizièka osoba
        	{
        		streamWriterFizicke.write(getRow(iter, owner_register_no, owner_name, dwh_status, status_u_modulu, value_date, cde_dep_unti, prolongat, 
        		        status, cde_account, guar_datn_unti, respiro_date, maturity_date, indexFizicke++));
        	}
        	else  // komitent je pravna osoba
        	{
        		streamWriterPravne.write(getRow(iter, owner_register_no, owner_name, dwh_status, status_u_modulu, value_date, cde_dep_unti, prolongat, 
        		        status, cde_account, guar_datn_unti, respiro_date, maturity_date, indexPravne++));
        	}
		} 
		streamWriterFizicke.flush();
		streamWriterFizicke.close();
		streamWriterPravne.flush();
		streamWriterPravne.close();
        bc.debug("Podaci zapisani u datoteke.");
    	
        // slanje maila
        Vector attachments = new Vector();
        attachments.add(fileNameFizicke);
        attachments.add(fileNamePravne);
        YXY70.send(bc, "csv176", bc.getLogin(), attachments);
        
		// stvaranje marker datoteka
		new File(fileNameFizicke + ".marker").createNewFile();
		new File(fileNamePravne + ".marker").createNewFile();
		bc.debug("Stvoreni markeri.");
		
		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
	
	/**
	 * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
	 * @param iter Iterator s kolateralima
	 * @param owner_register_no Interni MB vlasnika kolaterala
	 * @param owner_name Naziv vlasnika kolaterala
	 * @param value_date Datum deaktiviranja
	 * @param index Redni broj retka na listi
	 * @return red tablice u obliku stringa
	 */
	private String getRow(DeactivatedCollateralsIterator iter, String owner_register_no, String owner_name, String dwh_status, String status_u_modulu, Date value_date,
	        Date cde_dep_unti, String prolongat, String status, String cde_account, Date guar_datn_unti, Date respiro_date, Date maturity_date, int index) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(index).append(";");
		buffer.append(isEmpty(iter.col_num())).append(";");
		buffer.append(isEmpty(owner_register_no)).append(";");
		buffer.append(isEmpty(owner_name)).append(";");
		buffer.append(isEmpty(iter.cus_acc_no())).append(";");
		buffer.append(isEmpty(iter.request_no())).append(";");
	    buffer.append(isEmpty(dwh_status)).append(";");
	    buffer.append(isEmpty(status_u_modulu)).append(";");
	    buffer.append(isEmpty(iter.loan_register_no())).append(";");
		buffer.append(isEmpty(iter.loan_name())).append(";");
		buffer.append(isEmpty(value_date)).append(";");
        buffer.append(isEmpty(cde_dep_unti)).append(";");
        buffer.append(isEmpty(prolongat)).append(";");
        buffer.append(isEmpty(status)).append(";");
        buffer.append(isEmpty(cde_account)).append(";");
        buffer.append(isEmpty(guar_datn_unti)).append(";");
        buffer.append(isEmpty(respiro_date)).append(";");
        buffer.append(isEmpty(maturity_date)).append(";");
		buffer.append(isEmpty(iter.collateral_status())).append("\n");
		return buffer.toString();
	}
	 
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @return zaglavlje u obliku stringa
	 */
	private String getHeader(Date value_date) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Deaktivirani/oslobo\u0111eni kolaterali na dan: " + DateUtils.getDDMMYYYY(value_date)).append("\n");
		buffer.append("Redni broj").append(";");
		buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Interni MB vlasnika kolaterala").append(";");
        buffer.append("Naziv vlasnika kolaterala").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Broj zahtjeva").append(";");
        buffer.append("DWH status plasmana").append(";");
        buffer.append("Status plasmana u modulu").append(";");
        buffer.append("Interni MB korisnika plasmana").append(";");
        buffer.append("Naziv korisnika plasmana").append(";");
        buffer.append("Datum deaktivacije/osloba\u0111anja").append(";");
        buffer.append("Datum dospijeæa/depozit").append(";");
        buffer.append("Prolongat/depozit").append(";");
        buffer.append("Status/depozit").append(";");
        buffer.append("Partija/depozit").append(";");
        buffer.append("Datum dospijeæa/garancija").append(";");
        buffer.append("Respiro datum").append(";");
        buffer.append("Datum dospijeæa/VRP").append(";");
        buffer.append("Status kolaterala").append("\n");

		return buffer.toString();
	}
	
	private String isEmpty(String s)
	{
		if (s == null) return ""; else return s.trim();
	}
	private String isEmpty(Date d)
	{
		if (d == null) return ""; else return DateUtils.getDDMMYYYY(d);
	}

	/**
	 * Metoda provjerava broj i ispravnost ulaznih parametara.
	 * @return da li je provjera prošla
	 */
	private boolean checkParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 1)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	    	}
	    	else throw new Exception("Neispravan broj parametara!");
		}
		catch(Exception ex)
		{
            bc.error("Neispravno zadani parametri!", ex);
            return false;
		}
		return true;
	}

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2775551003"));
        batchParameters.setArgs(args);
        new BO370().run(batchParameters);
    }
}