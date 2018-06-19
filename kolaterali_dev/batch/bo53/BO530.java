//created 2010.03.16
package hr.vestigo.modules.collateral.batch.bo53;

/**
 *
 * @author hramlo
 */

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;

import hr.vestigo.modules.collateral.batch.bo53.BO531;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import hr.vestigo.modules.collateral.batch.bo53.BO531.CustDataIterator;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.excel.Alignment;
import hr.vestigo.modules.rba.util.excel.Borders;
import hr.vestigo.modules.rba.util.excel.Cell;
import hr.vestigo.modules.rba.util.excel.Column;
import hr.vestigo.modules.rba.util.excel.Font;
import hr.vestigo.modules.rba.util.excel.Interior;
import hr.vestigo.modules.rba.util.excel.Row;
import hr.vestigo.modules.rba.util.excel.Style;
import hr.vestigo.modules.rba.util.excel.Workbook;
import hr.vestigo.modules.rba.util.excel.Worksheet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

public class BO530 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo53/BO530.java,v 1.4 2011/01/24 13:38:08 hramlo Exp $";
    
    private BatchContext bc = null;
    private BO531 bo531=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    
    private BigDecimal eve_id=null;
    private ConnCtx connCtx = null;
    private CustDataIterator iter=null;
    
    
    public BO530(){     
    }
    
    public String executeBatch(BatchContext bc) throws Exception{
    
        this.bc=bc;
        
        bo531=new BO531(bc);
        
        try{
            this.connCtx=bc.getContext();
        }catch(Exception e){
            bc.warning("Nema connCtx u podtransakciji!!");
        }
        
        bc.warning("provjera ulaznih parametara");

        try{
            if(!checkArgs()){
                int i=0;
                for(i=0;i<bc.getArgs().length;i++) {
                    bc.debug("" + i +". " + bc.getArg(i));
                } 
                throw new Exception();
            }             

        }catch (Exception e) {
            bc.error("Broj parametara:" + bc.getArgs().length, bc.getArgs(), new Exception("Neispravan broj parametara."));
            return RemoteConstants.RET_CODE_ERROR;
        }  
        
        
        //insert u EVENT
        try{
            insertIntoEvent();
        }catch (Exception e) {
            bc.error("Insert EVENT - GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        bc.commitTransaction();
        bc.debug("Insert EVENT - END");  
        
        try{
            
            //dohvat podataka                  
            try{
                iter=bo531.getCustData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo531.getCustData()!!!");
                e.printStackTrace();                
            }
            
            
            String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
            String dir = bc.getOutDir() + "/";
            bc.debug("dir:"+dir);
            String fileName = dir + "Depoziti_CM_DWH_".concat(dateString);
            // stvaranje workbooka
            Workbook workbook = new Workbook(fileName, Workbook.FileType.ExcelML, true, 100);
            workbook.addStyle(new Style("Default", Alignment.Bottom, new Font("Arial", 9)));
            workbook.addStyle(new Style("s1", Alignment.BottomLeft, Borders.All, new Font("Arial", 9, true), new Interior("#FFFF00"), null));
            workbook.addStyle(new Style("s2", null, null, null, null, "Standard"));
            workbook.addStyle(new Style("s3", null, null, null, null, "0"));
            workbook.addStyle(new Style("s4", null, null, null, null, "Short Date"));
            workbook.open();
            bc.debug("Otvoren workbook.");
            
            // stvaranje sheetova
            Worksheet sheetCorporateDep = workbook.addWorksheet(new Worksheet("Corporate depoziti", dir + "Corporate depoziti_"+dateString));
            Worksheet sheetRetailDep = workbook.addWorksheet(new Worksheet("Retail depoziiti", dir + "Retail depoziti_"+dateString));
           
            sheetCorporateDep.setDefaultRowHeight(12);
            sheetRetailDep.setDefaultRowHeight(12);
            
            bc.debug("Kreirani sheetovi.");
             
            
            // definiranje kolona
            sheetCorporateDep.addColumns(getColumns(1));
            sheetRetailDep.addColumns(getColumns(2));
            
            bc.debug("Definirane kolone.");
            
            // otvaranje sheetova
            workbook.openAllWorksheets();
            bc.debug("Otvoreni sheetovi.");
            
            // formiranje zaglavlja na sheetovima
            sheetCorporateDep.addRow(getHeaderRow(1));
            sheetRetailDep.addRow(getHeaderRow(2));            
            bc.debug("Dodana zaglavlja.");
            
            
            // zapisivanje podataka u prva dva sheeta
            String col_num = null;
            BigDecimal ip_id = null;
            int row_no=0;
            int row_no2=0;
            while(iter.next()){
                bc.debug("Komitent:"+iter.name());
                bc.debug("iter.b2_asset():"+iter.b2_asset());
                if(!(iter.b2_asset().trim().equals("1")) && !(iter.b2_asset().trim().equals("36")) ) { // u prvi sheet uzimaju se korisnici plasmana pravne osobe
                    row_no++;
                    Row row_corporate = getDetailsRow(iter, new Integer(row_no));
                    sheetCorporateDep.addRow(row_corporate); 
                    bc.debug("U pravnim osobama");
                    
                }else{  // u drugi sheet stavljaju se korisnici plasmana fizicke osobe
                    bc.debug("fizicke osobe");
                    row_no2++;
                    Row row_retail=getDetailsRow(iter,new Integer(row_no2));
                    sheetRetailDep.addRow(row_retail);
                    }
                
            }
            
            bc.debug("Zapisani svi dohvaceni redovi");
            
            workbook.closeAllWorksheets();
            workbook.close();
            bc.debug("Zatvoren workbook.");

            bc.debug("Kreiranje file-a gotovo!");
            bc.debug("Ime file:"+fileName);
            fileName=fileName+".xml";
            
            YXY70.send(bc, "csv227", bc.getLogin(), fileName);
            bc.debug("Obrada zavrsena."); 
            
            return returnCode;
            
            
            /*
             * 24.01. izmjena prema CQ FBPr200010672- mail se salje samo narucitelju 
             * // dohvat mail adresa primatelja izvješæa
            String mailAddresses = bo531.getRecipients();
            if(mailAddresses == null) return RemoteConstants.RET_CODE_ERROR;

            // slanje maila
            int mailFlag = new SendMail().send(bc, "", "", mailAddresses, "", "", "Lista partija depozita i DWH povezanosti", "", null, fileName + ".xml");
            if(mailFlag != 0)
            {
                bc.error("Mail nije poslan!", new String[]{Integer.toString(mailFlag)});
                return RemoteConstants.RET_CODE_ERROR;
            }
            bc.debug("Mail poslan na " + mailAddresses + " .");
            
            bc.debug("Obrada zavrsena.");         */
            
            
        }catch(Exception e){
            bc.warning("Puklo na obradi podataka-glavni blok - GRESKA!!!");
            bc.error("GRESKA!!!", e);
            return RemoteConstants.RET_CODE_ERROR;
        
        }finally{// (bez obzira obrada uspjesno ili neuspjesno zavrsila-zatvaram iterator)            
        
            if(iter!=null){
                try{
                   iter.close();
                }catch(Exception itr){ 
                    throw itr;
                } 
            }           
        }
        
     }
   
    
    
    private boolean checkArgs() throws Exception{
        bc.debug("bc.getArgs().length:"+bc.getArgs().length);
        if (bc.getArgs().length==1){
            bc.debug("bc.getArg(0)"+bc.getArg(0));                      
            return true;

        }else{
            return false;
        }      
    }
    
    
    private void insertIntoEvent() throws Exception{
        bc.warning("insertIntoEvent");   
        HashMap event = new HashMap();

        try{
            YXYB0 eve1 = new YXYB0(bc);
            YXYD0 genId = new YXYD0(bc.getContext());           

            eve_id = genId.getNewId();

            event.put("eve_id",eve_id);
            event.put("eve_typ_id", new BigDecimal("3019570844"));
            event.put("event_date", new java.sql.Date(System.currentTimeMillis()));
            event.put("cmnt", "bo53:Lista partija depozita i DWH povezanosti");
            event.put("use_id", bc.getUserID());
            event.put("ext_event_code", null);
            event.put("ext_event_num", null); 
            event.put("bank_sign", bc.getBankSign());
            bc.warning("eve_id" + event.get("eve_id"));

            eve1.insertEvent(event);
            bc.updateEveID(eve_id);        
        } catch(Exception e){
            bc.warning("Event parametri=" + event.toString());
            throw e;
        }      
    }
    
    
    /** 
     * Metoda koja definira izgled kolona na tablici.
     * @param sheetNumber redni broj sheeta
     * @return vektor s definiranim kolonama tablice
     */
    private Vector getColumns(int sheetNumber){  
        //TODO provjeri sirinu polja
        Vector columns = new Vector();
        columns.add(new Column(30));  // oznaka partije kolaterala
        columns.add(new Column(95));  // status kolaterala
        columns.add(new Column(55));  // partija depozita u CM
        columns.add(new Column(150)); // dospijece depozita u CM
        columns.add(new Column(35));  // valuta depozita
        columns.add(new Column(95));  // iznos depozita
        columns.add(new Column(135)); // partija depozita u DWH
        columns.add(new Column(35));  // datum isporuke
        columns.add(new Column(95));  // interni MB korisnika
        columns.add(new Column(35));  // korisnik plasmana
        columns.add(new Column(75));  // B2asset
                
        return columns;
    }
    
    
    /** 
     * Metoda koja formira red sa zaglavljem tablice.
     * @param sheetNumber redni broj sheeta
     * @return formirani red tablice
     */
    private Row getHeaderRow(int sheetNumber){
        
        Row row = new Row(50, "s1");
        row.addCell(new Cell("Redni broj"));
        row.addCell(new Cell("\u0160ifra kolaterala"));
        row.addCell(new Cell("Status kolaterala"));
        row.addCell(new Cell("Partija depozita u CM"));
        row.addCell(new Cell("Dospije\u0107e depozita u CM"));
        row.addCell(new Cell("Valuta"));
        row.addCell(new Cell("Iznos"));
        row.addCell(new Cell("Partija depozita iz DWH"));
        row.addCell(new Cell("Datum isporuke podataka iz DWH"));
        row.addCell(new Cell("Interni MB korisnika"));
        row.addCell(new Cell("Korisnik plasmana"));
        row.addCell(new Cell("B2 asset"));
      
        return row;
    }
    
    /**
     * Metoda koja formira red tablice s podacima o polici (za prva dva sheeta)
     * @param iter Iterator s podacima o polici
     * @return formirani red tablice
     */
    private Row getDetailsRow(CustDataIterator iter, Integer row_no) throws Exception {
        

        Row row = new Row();
        row.addCell(new Cell(row_no));                    // Redni broj
        row.addCell(new Cell(iter.col_num()));            //sifra kolaterala         
        row.addCell(new Cell(iter.collateral_status()));  //status kolaterala                  
        row.addCell(new Cell(iter.cm_cde_account()));      //partija depozita u CM           
        row.addCell(new Cell(iter.cm_cde_dep_until()));    //dospijece depozita                
        row.addCell(new Cell(iter.code_char()));           //valuta          
        row.addCell(new Cell(iter.cm_cde_amount()));        //iznos             
        row.addCell(new Cell(iter.dwh_cde_account()));       //partija depozita iz DWH                  
        row.addCell(new Cell(iter.create_ts()));             //datum isporuke podataka 
        row.addCell(new Cell(iter.register_no()));          //interni mB
        row.addCell(new Cell(replaceXmlCharacters(iter.name())));  //korisnik plasmana                           
        row.addCell(new Cell(iter.b2_asset()));               //b2 asset     

        return row;
    }
    
    
    private String replaceXmlCharacters(String s){
        if(s == null) return s;
        s = s.replace("\"", "&quot;");
        s = s.replace("&", "&amp;");
        s = s.replace("'", "&apos;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;
    }
    
    
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("3019525084"));
        bp.setArgs(args);
        new BO530().run(bp);       
    }
}


