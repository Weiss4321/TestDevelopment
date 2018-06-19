//created 2011.10.19
package hr.vestigo.modules.collateral.batch.bo68;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;

import hr.vestigo.modules.collateral.batch.bo68.BO681.CustDataIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.excel.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author hramlo
 */
public class BO680 extends Batch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo68/BO680.java,v 1.2 2014/03/10 13:32:09 hrakis Exp $";
    
    
    private BatchContext bc = null;
    private BO681 bo681=null;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    
    private BigDecimal eve_id=null;
    private ConnCtx connCtx = null;
    private CustDataIterator iter=null;
    
    
    public BO680(){     
    }
    
    public String executeBatch(BatchContext bc) throws Exception{
    
        this.bc=bc;
        
        bo681=new BO681(bc);
        
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
                iter=bo681.getCustData();               
            }catch(Exception e){
                bc.warning("Exception kod poziva metode bo681.getCustData()!!!");
                e.printStackTrace();                
            }
            

            String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
            String dir = bc.getOutDir() + "/";
            bc.debug("dir:"+dir);
            String fileName = dir + "LP_".concat(dateString);
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
            Worksheet sheetLP = workbook.addWorksheet(new Worksheet("Lista evidencija partija", dir + " LP_"+dateString));

           
            sheetLP.setDefaultRowHeight(12);
            
            
            bc.debug("Kreiran sheet.");
            
            // definiranje kolona
            sheetLP.addColumns(getColumns(1));
            
            
            bc.debug("Definirane kolone.");
            
            // otvaranje sheetova
            workbook.openAllWorksheets();
            bc.debug("Otvoren sheet.");
            
            // formiranje zaglavlja na sheetovima
            sheetLP.addRow(getHeaderRow(1));
                    
            bc.debug("Dodano zaglavlje.");
            
            
            // zapisivanje podataka u prva dva sheeta
            
            BigDecimal coll_hea_id = null;
            String col_num=null;
            int row_no=0;
            
            while(iter.next()){
                
               
                    row_no++;
                    
                    if(iter.col_hea_id()==null){
                        coll_hea_id=bo681.getCollHeaId(iter.coll_hf_prior_id());
                        col_num=bo681.getCollNum(coll_hea_id);
                    }else {
                        col_num=bo681.getCollNum(iter.col_hea_id());
                    }
                    
                    
                    Row row_active = getDetailsRow(iter, col_num, new Integer(row_no));
                    sheetLP.addRow(row_active); 
                 
               
                
            }
            
            bc.debug("Zapisani svi dohvaceni redovi");
            
            workbook.closeAllWorksheets();
            workbook.close();
            bc.debug("Zatvoren workbook.");

            // slanje maila
            YXY70.send(bc, "csv249", bc.getLogin(), fileName);
            
            bc.debug("Obrada zavrsena.");         
            
            
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
        return RemoteConstants.RET_CODE_SUCCESSFUL;
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
            event.put("eve_typ_id", new BigDecimal("4819619704"));
            event.put("event_date", new java.sql.Date(System.currentTimeMillis()));
            event.put("cmnt", "bo68:Lista evidencija partija koje su u okviru i samostalne");
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
        
        Vector columns = new Vector();
        columns.add(new Column(30));  // Redni broj
        columns.add(new Column(75));  // Partija plasmana
        columns.add(new Column(55));  // Broj okvira
        columns.add(new Column(55));  // Interni MB komitenta
        columns.add(new Column(150)); // Komitent naziv
        columns.add(new Column(95));  // Sifra kolaterala        
        return columns;
    }
    
    
    /** 
     * Metoda koja formira red sa zaglavljem tablice.
     * @param sheetNumber redni broj sheeta
     * @return formirani red tablice
     */
    private Row getHeaderRow(int sheetNumber)
    {
        Row row = new Row(50, "s1");
        row.addCell(new Cell("Redni broj"));
        row.addCell(new Cell("Partija plasmana"));
        row.addCell(new Cell("Broj okvira"));
        row.addCell(new Cell("Interni MB komitenta"));
        row.addCell(new Cell("Komitent naziv"));
        row.addCell(new Cell("\u0160ifra kolaterala"));
        return row;
    }
    
    /**
     * Metoda koja formira red tablice s podacima o polici (za prva dva sheeta)
     * @param iter Iterator s podacima o polici
     * @return formirani red tablice
     */
    private Row getDetailsRow(CustDataIterator iter, String col_num, Integer row_no) throws Exception {
        

        Row row = new Row();
        row.addCell(new Cell(row_no));                                  // Redni broj
        row.addCell(new Cell(iter.cus_acc_no()));                     // Partija plasmana
        row.addCell(new Cell(iter.frame_cus_acc_no()));               // Broj okvira
        row.addCell(new Cell(iter.register_no()));                      // Interni MB komitenta
        row.addCell(new Cell(replaceXmlCharacters(iter.name())));       // Komitent naziv
        row.addCell(new Cell(col_num));                          //  Sifra kolaterala
       
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
        BatchParameters bp = new BatchParameters(new BigDecimal("4819609704"));
        bp.setArgs(args);
        new BO680().run(bp);       
    }
}

