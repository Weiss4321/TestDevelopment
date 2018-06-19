//created 2011.07.06
package hr.vestigo.modules.collateral.batch.bo62;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;

import hr.vestigo.modules.collateral.batch.bo62.BO621.IteratorNeknjizeni;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Batch za kreiranje izvješæa o neknjiženim kolateralima sa referentskih lista
 * @author hradnp
 */
public class BO620 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo62/BO620.java,v 1.4 2011/11/28 14:03:42 hradnp Exp $";

    private BO621 bo621 = null;

    private String returnCode =RemoteConstants.RET_CODE_SUCCESSFUL;
    private BatchContext bc = null;

    private ArrayList rows = new ArrayList();
    private String[] row; 
    private String key;
    private OutputStreamWriter streamWriter;

    public BO620() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String executeBatch(BatchContext bc) throws Exception {

        bc.debug("BO620 pokrenut.");
        this.bo621 = new BO621(bc);
        this.bc = bc;

        long pocetakIzvodenja=0;            
        pocetakIzvodenja=System.currentTimeMillis();

        if (bc.getArgs().length!=1){
            bc.debug("Neispravan broj argumenata!");
            return RemoteConstants.RET_CODE_ERROR;
        }
        if (bc.getArg(0).equals("RB")==false){
            bc.debug("Bank sign mora biti 'RB'!");
            return RemoteConstants.RET_CODE_ERROR;
        }

        //Spremanje poziva izvršavanja batcha u tablicu Event
        bc.beginTransaction();
        try{
            bo621.insertIntoEvent();
        }catch (Exception e) {
            bc.debug("InsertIntoEvent greska...");
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");

        try{
            // Dohvat podataka
            BO621.IteratorNeknjizeni iter= bo621.selectNeknjizeni();
            if(iter == null) return RemoteConstants.RET_CODE_ERROR;
            bc.debug("Dohvaceni podaci o kolateralima sa referentskih, odnosno verifikacijskih lista.");
            
            // Stvaranje izlazne datoteke
            String dir = bc.getOutDir() + "/";
            String fileName = dir + "UnPostedCollaterals_" + pocetakIzvodenja + ".csv";             
            streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
            bc.debug("Stvorena izlazna datoteka.");

            // Zapisivanje zaglavlja u izlaznu datoteku
            streamWriter.write(getHeaderRow());

            // Postavljanje iteratora u array, radi sortiranja zapisa
            boolean fetched=false;
            if(iter!=null){  
                int pom = 0;
                while(iter.next()){
                    if(!fetched){
                        fetched=true;
                    }
                    // TST0400000083 u sluèaju kad se kolateral nalazi na referentskoj listi, ali nema podatka o referentu
                    if(iter.user_name() == null && iter.collateral_status().equals("refer")){
                            // TST0400000083 u sluèaju kad se kolateral nalazi na referentskoj listi, ali nema podatka o referentu, referent se posebno dohvaca
                            String user_name = bo621.getUser(iter.col_hea_id());
                            row = new String[2];
                            row[0] = user_name.concat("_"+pom);
                            row[1] = getDetailsRow(iter, user_name);
                            rows.add(row);
                    }
                    // Ako se referent nalazi na refer (odnosno verif) listi, te postoje podaci o referentu (odnosno verifikatoru)
                    else{
                        row = new String[2];
                        String details = getDetailsRow(iter, pom);
                        row[0] = key;
                        row[1] = details;
                        rows.add(row);
                    }
                    pom++;
                }     
            }
            
            // Sortiranje zapisa prema Imenu i prezimenu referenta prije zapisivanja u datoteku
            Collections.sort(rows, new MyComparator());
            
            // Zapisivanje kolaterala u izlaznu datoteku 
            for(int i=0;i<rows.size();i++){
                streamWriter.write(((String[]) rows.get(i))[1]);
            }

            iter.close();

            if(!fetched){
                // ako nije dohvacen niti jedan kolateral ispisuje se greska u obradi
                bc.warning("Nije dohvacen niti jedan kolateral - nema neknjiženih kolaterala!");
            }

            // zatvaranje izlazne datoteke
            streamWriter.flush();
            streamWriter.close();
            bc.debug("Zatvorena izlazna datoteka.");

            // slanje maila
            bc.startStopWatch("sendMail");
            bc.debug("getLogin(): " + bc.getLogin());
            YXY70.send(bc, "csv801", bc.getLogin(), fileName);
            bc.stopStopWatch("sendMail");
            bc.debug("Mail poslan.");

        }catch(Exception e){
            bc.debug("Exc:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return returnCode;
    }

    /**
     * Metoda formira jedan red izvješæa i vraæa ga u obliku stringa.
     * @param iter Iterator s podacima
     * @return formirani red
     */
    // TST0400000083 - dodane provjere na polja, ukoliko je u bazi umjesto null vrijednosti "" 
    private String getDetailsRow(IteratorNeknjizeni iter, int pom) throws SQLException{

        StringBuffer buffer = new StringBuffer();
        buffer.append(iter.col_num().trim()).append(";");
        buffer.append(iter.first_entry_date().toString().trim()).append(";");
        buffer.append(iter.oj_code().trim()).append(";");
        buffer.append(iter.oj_name().trim()).append(";");
        
        // TST0400000083 - Ako se kolateral nalazi na verifikacijskoj listi, upisuje se verifikator, u sluèaju da se nalazi na referentskoj listi, referent
        if(iter.collateral_status().trim().equals("verifik")){
            if(iter.verification() != null && !iter.verification().equals("")){
                buffer.append(iter.verification().trim()).append(";");
                key = iter.verification().trim().concat("_"+pom);
            }
            else buffer.append(" ;");
        }else if(iter.collateral_status().trim().equals("refer")){
            if(iter.user_name() != null && !iter.user_name().equals("")){
                buffer.append(iter.user_name().trim()).append(";");
                key = iter.user_name().trim().concat("_"+pom);
            }
        }else buffer.append(" ;");
        
        if(iter.code_char() != null && !iter.code_char().equals(""))
            buffer.append(iter.code_char().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.real_est_nomi_value() != null && !iter.real_est_nomi_value().equals(""))
            buffer.append(iter.real_est_nomi_value().toString().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.real_est_nomi_date() != null && !iter.real_est_nomi_date().equals(""))
            buffer.append(iter.real_est_nomi_date().toString().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.rba_eligibility() != null && !iter.rba_eligibility().equals(""))
            buffer.append(iter.rba_eligibility().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.rba_eligibility_desc() != null && !iter.rba_eligibility_desc().equals(""))
            buffer.append(iter.rba_eligibility_desc().trim()).append(";");
        else buffer.append(" ;");
        
        buffer.append(iter.collateral_status().trim()).append(";");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda formira jedan red izvješæa i vraæa ga u obliku stringa.
     * @param iter Iterator s podacima
     * @param user_name Ime posljednjeg tko je mjenjao podatke
     * @return formirani red
     */
    // TST0400000083 metoda koja služi za formiranje retka u sluèaju kad se kolateral nalazi na referentskoj listi, ali nema podatka o referentu
    private String getDetailsRow(IteratorNeknjizeni iter, String user_name) throws SQLException{

        StringBuffer buffer = new StringBuffer();
        buffer.append(iter.col_num().trim()).append(";");
        buffer.append(iter.first_entry_date().toString().trim()).append(";");
        buffer.append(iter.oj_code().trim()).append(";");
        buffer.append(iter.oj_name().trim()).append(";");
        
        if(user_name != null && !user_name.equals(""))
            buffer.append(user_name).append(";");
        else buffer.append(" ;");
        
        if(iter.code_char() != null && !iter.code_char().equals(""))
            buffer.append(iter.code_char().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.real_est_nomi_value() != null && !iter.real_est_nomi_value().equals(""))
            buffer.append(iter.real_est_nomi_value().toString().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.real_est_nomi_date() != null && !iter.real_est_nomi_date().equals(""))
            buffer.append(iter.real_est_nomi_date().toString().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.rba_eligibility() != null && !iter.rba_eligibility().equals(""))
            buffer.append(iter.rba_eligibility().trim()).append(";");
        else buffer.append(" ;");
        
        if (iter.rba_eligibility_desc() != null && !iter.rba_eligibility_desc().equals(""))
            buffer.append(iter.rba_eligibility_desc().trim()).append(";");
        else buffer.append(" ;");
        
        buffer.append(iter.collateral_status().trim()).append(";");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda formira zaglavlje za izvješæe i vraæa ga u obliku stringa.
     * @return formirano zaglavlje
     */
    private String getHeaderRow(){

        StringBuffer buffer = new StringBuffer();
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Datum prvog unosa").append(";");
        buffer.append("\u0160ifra OJ").append(";");
        buffer.append("Naziv OJ").append(";");
        buffer.append("Referent").append(";");
        buffer.append("Valuta tr\u017Ei\u0161ne vrijednosti").append(";");
        buffer.append("Tr\u017Ei\u0161na vrijednost").append(";");
        buffer.append("Datum tr\u017Ei\u0161ne vrijednosti").append(";");
        buffer.append("RBA prihvatljivost").append(";");
        buffer.append("Obrazlo\u017Eenje RBA prihvatljivosti").append(";");
        buffer.append("Status kolaterala").append(";");
        return buffer.append("\n").toString();
    }

    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4663727704.0"));
        batchParameters.setArgs(args);
        new BO620().run(batchParameters);
    }
}

