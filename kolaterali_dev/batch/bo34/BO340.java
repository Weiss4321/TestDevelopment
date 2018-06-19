//created 2008.11.07
package hr.vestigo.modules.collateral.batch.bo34;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo68.BO681.CustDataIterator;
import hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.yoyE.YOYE1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
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

/**
 *
 * @author hraamh
 */
public class BO340 extends AbstractCollateralFileTransferBatch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo34/BO340.java,v 1.18 2014/03/10 13:27:10 hrakis Exp $";

    BO341 bo341 = null;
    VRPIterator iter = null;
    EXPIterator iterExp = null;
    String fileName = null;
    Workbook workbook = null;
    Worksheet sheetLP = null;

    public BO340(){
        super();
    }

    @Override
    protected void init() throws Exception {
        bo341 = new BO341(getContext(),org_uni_id,use_id);
        proc_type = "8";
        proc_date = new Date(System.currentTimeMillis());
        value_date = proc_date;
    }

    private CollHeadNewData calculateCollHead(VRPIterator iter) throws Exception{
        CollHeadNewData result = new CollHeadNewData();

        if(iter.col_cat_id().longValue()==619223){
            result.one_mar_amo = iter.price().movePointLeft(2);
            result.market_amount = result.one_mar_amo.multiply(iter.num_of_sec()).multiply(iter.one_nom_amo());
        }else{
            result.one_mar_amo = iter.price();
            result.market_amount = result.one_mar_amo.multiply(iter.num_of_sec());
        }        

        result.price_date=iter.date_from();
        result.weigh_value = result.market_amount.multiply(iter.mvp_ponder());

        if(iter.hfs_value()==null){
            result.avail_value = result.weigh_value;
        }else{
            result.avail_value = result.weigh_value.subtract(iter.hfs_value());
        }

        if(iter.third_right_nom()==null){
            result.avail_value=result.avail_value;   
        }else{
            result.avail_value = result.avail_value.subtract(iter.third_right_nom());
        }
        result.round();
        return result;
    }


    private VRPNewData calculateVRP(VRPIterator iter) throws Exception{
        VRPNewData result = new VRPNewData();

        result.one_nom_amo = iter.one_nom_amo();
        result.nominal_amount = result.one_nom_amo.multiply(iter.num_of_sec());
        result.one_nom_amo_kn = iter.one_nom_amo().multiply(iter.mid_rate()); 
        
        if(iter.col_cat_id().longValue()==619223){
            result.one_mar_amo=iter.price().movePointLeft(2);
            //11.01.2010 promjena kod izracuna obveznica  CQ : TST0100000642
            result.one_mar_amo_kn=result.one_mar_amo.multiply(iter.mid_rate());
            result.market_amount=result.one_mar_amo.multiply(iter.num_of_sec()).multiply(iter.one_nom_amo());
            result.market_amount_kn=result.one_mar_amo_kn.multiply(iter.num_of_sec()).multiply(iter.one_nom_amo());
        }else{
            result.one_mar_amo=iter.price();
            result.one_mar_amo_kn=result.one_mar_amo.multiply(iter.mid_rate());
            result.market_amount=result.one_mar_amo.multiply(iter.num_of_sec());
            result.market_amount_kn=result.one_mar_amo_kn.multiply(iter.num_of_sec());
        }     
        result.nominal_amount_kn=result.one_nom_amo_kn.multiply(iter.num_of_sec());  

        if((iter.col_cat_id().longValue()!=613223)&&(iter.col_cat_id().longValue()!=622223))
            result.one_mar_amo_per=(result.one_mar_amo.divide(iter.one_nom_amo(), BigDecimal.ROUND_HALF_EVEN)).movePointRight(2);
        result.price_date=iter.date_from();
        result.round();
        return result;
    }


    private BigDecimal calculateEXP(BigDecimal col_hea_id) throws Exception{   
        
        BigDecimal exch_rate_exp = null;
        BigDecimal amountSum = new BigDecimal(0);
        BigDecimal amountCur = null;

        YOYE1 YOYE1_find = new YOYE1(bc);
        iterExp = bo341.selectEXPIterator(col_hea_id);        
        while(iterExp.next()){
            exch_rate_exp = YOYE1_find.getMiddRate(iterExp.exposure_cur_id());
            bc.debug("VALUTA izlozenosti:"+iterExp.exposure_cur_id());
            bc.debug("SREDENJI TECAJ:"+exch_rate_exp);
            bc.debug("IZNOS izlozenosti:"+iterExp.exposure_amount());
            amountCur = iterExp.exposure_amount().multiply(exch_rate_exp);
            bc.debug("IZNOS u kunama :"+amountCur);
            amountSum = amountSum.add(amountCur);
            bc.debug("TRENUTNA IZLOZENOST(amountSum kroz iteracije):"+amountSum);
        }
        return amountSum;
    }


    private boolean checkRate(BigDecimal omjer, BigDecimal granica) throws Exception{
        if(omjer.compareTo(granica) == 1)//omjer je veci pa se ne mora upisati na listu
            return false;
        else
            return true;
    }

    private void createCsv() throws Exception{
        
        String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
        String dir = bc.getOutDir() + "/";
        bc.debug("dir:"+dir);
        fileName = dir + "Lista_za_margin_call_".concat(dateString);
        // stvaranje workbooka
        workbook = new Workbook(fileName, Workbook.FileType.ExcelML, true, 100);
        workbook.addStyle(new Style("Default", Alignment.Bottom, new Font("Arial", 9)));
        workbook.addStyle(new Style("s1", Alignment.BottomLeft, Borders.All, new Font("Arial", 9, true), new Interior("#FFFF00"), null));
        workbook.addStyle(new Style("s2", null, null, null, null, "Standard"));
        workbook.addStyle(new Style("s3", null, null, null, null, "0"));
        workbook.addStyle(new Style("s4", null, null, null, null, "Short Date"));
        workbook.open();
        bc.debug("Otvoren workbook.");

        // stvaranje sheetova
        sheetLP = workbook.addWorksheet(new Worksheet("Lista za margin call", dir + " LP_"+dateString));
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
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("2656542003"));
        bp.setArgs(args);
        new BO340().run(bp);
    }

    @Override
    protected void closeExtraConnections() throws Exception {
        if(iter!=null){
            iter.close();
        }
        bo341.closeExtraConnection();
    }

    @Override
    protected int getArgs(BatchContext bc) {
        return 0;
    }

    @Override
    protected BigDecimal getColProId() throws Exception{
        return bo341.getColProId();
    }

    @Override
    protected String getTargetModule() {
        return null;
    }

    @Override
    protected boolean isAlwaysFreshStart() {
        return false;
    }

    @Override
    protected boolean isInFileManipulation() {
        return false;
    }
    @Override
    protected boolean isOutFileManipulation() {
        return false;
    }

    @Override
    protected boolean isFileTransfer() {
        return false;
    }

    @Override
    protected boolean isMQNotify() {
        return false;
    }

    @Override
    protected String runBatch() throws Exception { 
        CollateralPosting collPosting = CollateralCommonFactory.getCollateralPosting(bc);
        iter = bo341.selectVRPIterator();

        if(iter != null){  
            createCsv(); 
            while(iter.next()){
                printIterValue(iter);
                CollHeadNewData chnd = calculateCollHead(iter);
                VRPNewData vnd = calculateVRP(iter);
                System.out.println("\n CollHeadNewData...\n"+chnd);
                System.out.println("\n VRPNewData...\n"+vnd);
                bc.beginTransaction();

                //TODO dorada prema FBPr200011491- samo za dionice col_cat_id=613223

                //bc.debug("iter.margin_granica() != NULL- U PETLJI SAM!");
                BigDecimal sumExp = calculateEXP(iter.col_hea_id());
                bc.debug("sumExp- UKUPNA IZLOZENOST:"+sumExp);

                if(sumExp.compareTo(new BigDecimal(0))!=0){
                    vnd.margin_omjer = vnd.market_amount_kn.divide(sumExp, 4,4); 
                } else {
                    vnd.margin_omjer = null; 
                }
                
                //bc.debug("SKALA vnd.margin_omjer:"+vnd.margin_omjer.scale());

                bc.info("XXXXXXXX vnd.margin_omjer: "+vnd.margin_omjer);
                if(iter.margin_granica()!=null && vnd.margin_omjer != null){
                    bc.debug("iter.margin_granica() != NULL && vnd.margin_omjer != null - U PETLJI SAM!");
                    if(checkRate(vnd.margin_omjer,iter.margin_granica())){
                        //kolateral se zapisuje u listu za  margin call
                        bc.debug("omjer je veci pa se  mora upisati na listu!");

                        Row row_active = getDetailsRow(iter,vnd, sumExp);
                        sheetLP.addRow(row_active); 
                    }
                }

                bo341.updateCollHead(chnd, iter.col_hea_id());
                bo341.updateCollVrp(vnd, iter.col_vrp_id());

                collPosting.CollPosting(iter.col_hea_id(), false);
                commonMethods.insertInDataDwhItem(getColProId(), iter.col_hea_id(), null, "0", iter.col_in2_pri_id());
                bc.commitTransaction();
                incrementCounter(1);
            }
        }

        bc.debug("Zapisani svi dohvaceni redovi");

        workbook.closeAllWorksheets();
        workbook.close();
        bc.debug("Zatvoren workbook.");
        
        // slanje maila
        YXY70.send(bc, "csvbo34", bc.getLogin(), fileName + ".xml");

        return toReturn;
    }

    protected void printIterValue(VRPIterator iter) throws Exception{
        String result="\n ";
        result+="\n col_num: "+iter.col_num();
        result+="\n col_cat_id: "+iter.col_cat_id();
        result+="\n col_hea_id: "+iter.col_hea_id();
        result+="\n col_in2_pri_id: "+iter.col_in2_pri_id();
        result+="\n col_vrp_id: "+iter.col_vrp_id();
        result+="\n date_from: "+iter.date_from();
        result+="\n hfs_value: "+iter.hfs_value();
        result+="\n mid_rate: "+iter.mid_rate();
        result+="\n mvp_ponder: "+iter.mvp_ponder();
        result+="\n nom_cur_id: "+iter.nom_cur_id();
        result+="\n num_of_sec: "+iter.num_of_sec();
        result+="\n one_nom_amo: "+iter.one_nom_amo();
        result+="\n price: "+iter.price();
        result+="\n third_right_nom: "+iter.third_right_nom();
        result+="\n margin_granica: "+iter.margin_granica();
        System.out.println("\n Iterator:"+result);
    }

    @Override
    protected void setColProId(BigDecimal col_pro_id) {
        bo341.setColProId(col_pro_id);
    }

    @Override
    protected void setEve_typ_id() {
        this.eve_typ_id = new BigDecimal("2656526003");
    }

    @Override
    protected String getBatchName() {
        return "bo34";
    }

    /** 
     * Metoda koja definira izgled kolona na tablici.
     * @param sheetNumber redni broj sheeta
     * @return vektor s definiranim kolonama tablice
     */
    private Vector getColumns(int sheetNumber){  

        // FBPr200011491 - dodane dvije nove kolone, te izmjenjen redoslijed i nazivi postojeæih
        Vector columns = new Vector();
        columns.add(new Column(95));  // Šifra kolaterala
        columns.add(new Column(70));  // Trž.vrij.kol(HRK)
        columns.add(new Column(75));  // Izloženost
        columns.add(new Column(20));  // Margin omjer
        columns.add(new Column(20));  // Margin granica 
        columns.add(new Column(30));  // Datum VRP cijene
        columns.add(new Column(20));  // Valuta cijene
        columns.add(new Column(75));  // Iznos cijene u valuti
        columns.add(new Column(75));  // Cijena u HRK, sr.teèaj na dan izraè.
        columns.add(new Column(20));  // IM broj komitenta
        columns.add(new Column(95));  // Naziv komitenta vlasnika aktivnog plasmana
        
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
        row.addCell(new Cell("\u0160ifra kolaterala"));
        row.addCell(new Cell("Tr\u017E.vrij.kol.(HRK)"));
        row.addCell(new Cell("Izlo\u017Eenost(HRK)"));
        row.addCell(new Cell("Margin omjer"));
        row.addCell(new Cell("Margin granica"));
        row.addCell(new Cell("Datum VRP cijene"));
        row.addCell(new Cell("Valuta cijene"));
        row.addCell(new Cell("Iznos cijene u valuti"));
        row.addCell(new Cell("Cijena u HRK, sr.te\u010Daj na dan izra\u010Duna"));
        row.addCell(new Cell("IM vlasnika plasmana"));
        row.addCell(new Cell("Vlasnik plasmana"));
        
        return row;
    }


    /**
     * Metoda koja formira red tablice s podacima o polici (za prva dva sheeta)
     * @param iter Iterator s podacima o polici
     * @return formirani red tablice
     */
    private Row getDetailsRow(VRPIterator iter, VRPNewData vnd,BigDecimal sumExp) throws Exception {

        Row row = new Row();                                       // Redni broj
        row.addCell(new Cell(iter.col_num()));                     // Sifra kolaterala
        row.addCell(new Cell(vnd.market_amount_kn));               // Trzisna vrijednost
        row.addCell(new Cell(sumExp));                             // Ukupna izlozenost
        row.addCell(new Cell(vnd.margin_omjer));                   // Izracunati omjer
        row.addCell(new Cell(iter.margin_granica()));              // Definirana margin granica
        row.addCell(new Cell(vnd.price_date));                     // datum cijene dionice
        row.addCell(new Cell(iter.code_char()));                   // Valuta cijene- char
        row.addCell(new Cell(vnd.one_mar_amo));                    // Iznos cijene u valuti
        row.addCell(new Cell(vnd.one_mar_amo_kn));                 // Iznos cijene u HRK
        
        String register_no = iter.register_no();
        if(register_no != null && !register_no.equals("")){
            row.addCell(new Cell(iter.register_no()));                 // IM
            row.addCell(new Cell(iter.name()));                        // Naziv komitenta
        }else{
            row.addCell(new Cell(""));
            row.addCell(new Cell(""));
        } 

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
}

