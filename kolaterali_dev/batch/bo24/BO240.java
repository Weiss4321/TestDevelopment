package hr.vestigo.modules.collateral.batch.bo24;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;

import hr.vestigo.modules.collateral.batch.bo24.BO241.Iter;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

public class BO240 extends Batch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo24/BO240.java,v 1.10 2008/08/19 10:44:34 hraaks Exp $";
    private Vector dataCSV=null;
    private String configFile = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.', '/')+"/vehicle_report_booklets.xml";
   
    private String outputFileName;
    private String outputFile;
    private BatchContext bc;
    private HashMap parameters= new HashMap();
    private String reportName= "csv142";
    private String retcode = RemoteConstants.RET_CODE_SUCCESSFUL;
    InParamsData inParamsData = new InParamsData();
    private BO241 bo241;
  

    private static final String encoding = "Cp1250";
    
    private void params_to_data(String [] ulazniParametri) {
        inParamsData.client_type=ulazniParametri[0];// obavezno na unos P ili F
		if (ulazniParametri[1]!=null && !ulazniParametri[1].trim().equals("")){
	        inParamsData.organization_unit=new BigDecimal(ulazniParametri[1]);// nije obavezno za unos
		}
//        inParamsData.organization_unit=new BigDecimal(ulazniParametri[1]);// nije obavezno za unos
        inParamsData.setDateFrom(ulazniParametri[2]);// nije obavezno za  unos
        inParamsData.setDateUntil(ulazniParametri[3]);// nije obavezno za  unos
        inParamsData.vehicle_flag=ulazniParametri[4];//  obavezno za unos
        // ispis argumenata
        inParamsData.toString();
           
    }
    public void createCSV(){
        
    }
   
    public String executeBatch(BatchContext arg0) throws Exception {
        this.bc=arg0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));
        this.outputFileName = "Knjizice_vozila".concat(dateString).concat(".csv");
        this.outputFile = bc.getOutDir() + "/" + outputFileName;
        bc.debug("BO240, metoda setConfig(). Variable outputFileName=" + outputFileName);
        bc.debug("BO240, metoda setConfig(). Variable outputDir=" + outputFile);
        
        bo241 = new BO241(bc);
        dataCSV = new Vector();
        params_to_data(bc.getArgs());//setiranje argumentaa
        bc.beginTransaction();
        
        
        bo241.insertIntoEvent();
        Iter iter = bo241.fetchData(inParamsData);
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(this.outputFile)));
        //zaglavlje
        streamWriter.write("Redni broj;OJ;Naziv OJ;Sifra kolaterala;Omega ID vlasnika kolaterala;Ime i prezime vlasnika;Broj partije plasmana;Broj zahtijeva za plasman;Omega ID korisnika plasmana;Ime i prezime korisnika plasmana;Adresa korisnika plasmana;Posta korisnika plasmana;Mjesto korisnika plasmana;Broj sasije;Broj knjizice vozila;Datum kada je knjizica vracena vlasniku\n");
        int i =0;
        while(iter.next()){
            Bo24Data data = new Bo24Data();
            data.nr=++i;
            data.organization_unit= iter.org_uni_code();
            data.organization_unit_name = iter.org_uni_name();
            data.col_num=iter.col_num();
            
            data.acc_no=iter.acc_no();
            data.request_no=iter.request_no();
            
            data.user_register_no=iter.register_no();
            data.user_name = iter.name();

            data.user_address = iter.address();
            
            if(data.user_address!=null || !data.user_address.equals("")){
                data.user_address=iter.address();
                data.user_post_office = iter.postoffice();
                data.user_city = iter.city();
            } else {
                bo241.fetchCollUserAddress(iter.cus_id(), data);
            }
 
// dohvatiti vlasnika
            bo241.fetchCollOwner(iter.col_hea_id(), data);
            
           
           
           data.user_weh_vin_num=iter.veh_vin_num();
           data.user_veh_veh_licence=iter.veh_veh_licence();
           data.lic_ret_own_dat=iter.lic_ret_own_dat();
           
           writeLineToCSV(data, streamWriter);
           //streamWriter.flush();
           //streamWriter.close();
           //CSVGenerator.printCSV(reportName,bc.getLogin(), dataCSV, parameters,
         //           configFile, outputFile, encoding);
//           YXY70.send(bc,reportName, bc.getLogin() , outputFile);
            
        }
        streamWriter.flush();
        streamWriter.close();
        bc.commitTransaction();
        YXY70.send(bc,reportName, bc.getLogin() , outputFile);
        return retcode;
    }
    
    public void writeLineToCSV(Bo24Data data,OutputStreamWriter writer) throws Exception{
//        HashMap row = new HashMap();
//        row.put("nr", data.nr);
//        row.put("org_uni", data.organization_unit);
//        row.put("org_uni_name", data.organization_unit_name);
//        row.put("col_num", data.col_num);
//        row.put("register_no_owner", data.owner_register_no);
//        row.put("name_owner", data.owner_name);
//        row.put("acc_no", data.acc_no);
//        row.put("request_no", data.request_no);        
//        row.put("register_no_user", data.user_register_no);
//        row.put("name_user", data.user_name);//todo vidit kaj je s ovim
//        row.put("address", data.user_address);
//        row.put("postoffice", data.user_post_office);
//        row.put("city", data.user_city);
//        row.put("veh_vin_num", data.user_weh_vin_num);
//        row.put("veh_veh_licence", data.user_veh_veh_licence);
//        row.put("lic_ret_own_dat", data.lic_ret_own_dat);
//        dataCSV.add(row);
        
        writer.write(data.nr+";");
        writer.write(data.organization_unit+";");
        writer.write(data.organization_unit_name+";");
        writer.write(data.col_num+";");
        writer.write(data.owner_register_no+";");
        writer.write(data.owner_name+";");
        writer.write(data.acc_no+";");
        writer.write(data.request_no+";");
        writer.write(data.user_register_no+";");
        writer.write(data.user_name+";");
        writer.write(data.user_address+";");
        writer.write(data.user_post_office+";");
        writer.write(data.user_city+";");
        writer.write(data.user_weh_vin_num+";");
        writer.write(data.user_veh_veh_licence+";");
        writer.write(data.lic_ret_own_dat+";\n");
        
    }
    
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("2329865003"));// 
        bp.setArgs(args);
        new BO240().run(bp);
    }

}
