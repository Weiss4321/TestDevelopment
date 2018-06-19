//created 2009.09.25
package hr.vestigo.modules.collateral.batch.bo46;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
 
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.batch.io.FileConstants;
import hr.vestigo.framework.remote.batch.io.FileManager;
import hr.vestigo.framework.remote.batch.io.VFile;
import hr.vestigo.framework.remote.batch.io.VOutputStream;
import hr.vestigo.framework.remote.batch.io.VWriter;

import hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.DecimalUtils;
    
/**
 * 
 * Batch za amortizaciju i revalorizaciju nekretnina i vozila
 *
 * @author hraamh
 */
public class BO460 extends AbstractCollateralFileTransferBatch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo46/BO460.java,v 1.16 2012/02/28 10:24:48 hramkr Exp $";

    BO461 bo461=null;
     
    sqlj.runtime.NamedIterator processIterator=null;
    
    //protected String cities="3239997,9024997,15709997,24490997,24791997,27839997,33812997,45691997,47163997,51225997,52825997,55638997,56421997,57690997,58211997,63118997,66761997,67199997,67750997,69248997,69361997,71684997,71951997";
    protected String[] cities = { "3239997" , "9024997" , "15709997" , "24490997" , "24791997" , "27839997" , "33812997" , "45691997" , "47163997" , "51225997" , "52825997" , "55638997" , "56421997" , "57690997" , "58211997" , "63118997" , "66761997" , "67199997" , "67750997" , "69248997" , "69361997" , "71684997" , "71951997" };
    protected String Zagreb="72150997";
    
    private BigDecimal app_use_id= null;
    private BigDecimal col_cat_id= null;
    private String sys_cod_id=null;
    private Date last_amort_date=null;
    private int current_year=-1;
    private BigDecimal koef_amort=null;
    private CollateralPosting postingEngine=null;
    
    private BigDecimal zero=null;
    private BigDecimal EUR=null;
    private BigDecimal HRK=null;
    private BigDecimal criticalExposurePercentage=null;
    private BigDecimal exposureColProId=null;
    
    private enum ProcessType{RealEstate, Vehicle, Nothing }    
    private ProcessType batchProcess= ProcessType.Nothing;
    
    private final String mailcode="csv216"; 
    private String cvsHeader="\u0160ifra kolaterala;Vrsta;Podvrsta;Valuta tr\u017Ei\u0161ne vrijednosti;Tr\u017Ei\u0161na vrijednost;Datum tr\u017Ei\u0161ne vrijednosti;Datum procjene";
    private String cvsHeaderVehicle="\u0160ifra kolaterala;Vrsta;Valuta tr\u017Ei\u0161ne vrijednosti;Procijenjena vrijednost;Datum tr\u017Ei\u0161ne vrijednosti;Stara tr\u017Ei\u0161na vrijednost;Godina proizvodnje;(1-KOEF*N);Nova tr\u017Ei\u0161na vrijednost;Iznos amortizacije";
    private VFile fileOut=null;
    private FileManager fileManager= null;
    private VWriter  fWriter=null;
    private VOutputStream fOutputStream=null;
    private boolean manipulateOutFile=false;
    private String encoding = "Cp1250";
    
    public BO460(){
        super();
    }
    
    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#closeExtraConnections()
     */
    @Override
    protected void closeExtraConnections() throws Exception {
        if((processIterator!=null)&&(!processIterator.isClosed())){
            processIterator.close();
        }
        bo461.closeExtraConnection();
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#getArgs(hr.vestigo.framework.remote.batch.BatchContext)
     */
    @Override
    protected int getArgs(BatchContext bc) {
        String type=bc.getArg(1);
        outputDir=bc.getOutDir();
        if(!outputDir.endsWith("/")) outputDir+="/";
        if("NEKR".equalsIgnoreCase(type)){
            batchProcess= ProcessType.RealEstate;
        }else if("VOZI".equalsIgnoreCase(type)){
            batchProcess= ProcessType.Vehicle;
        }else{
            return 1;
        }
        String input_date=(String)bc.getArg(2);  
        last_amort_date=Date.valueOf(input_date);
        return 0;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#getBatchName()
     */
    @Override
    protected String getBatchName() {
        return "bo46";
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#getColProId()
     */
    @Override
    protected BigDecimal getColProId() throws Exception {
        return bo461.getColProId();
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#getTargetModule()
     */
    @Override
    protected String getTargetModule() {
        return null;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#init()
     */
    @Override
    protected void init() throws Exception {
        app_use_id=getContext().getUserID();       
        if(batchProcess== ProcessType.RealEstate){
            proc_type="ARN";
            col_cat_id=new BigDecimal(618223);
            sys_cod_id="koef_amort_restate";
        }else if(batchProcess== ProcessType.Vehicle){
            proc_type="ARV";
            col_cat_id=new BigDecimal(624223);
            sys_cod_id="koef_amort_vehicle";
        }else{
            throw new Exception("Nepostojan tip obrade");
        }
        bo461= new BO461(getContext(),org_uni_id,use_id);
        proc_date=new Date(System.currentTimeMillis());
        value_date=proc_date;
        koef_amort= new BigDecimal(bo461.getSystemCodeValue(sys_cod_id));
        postingEngine= CollateralCommonFactory.getCollateralPosting(getContext());
        zero= new BigDecimal(0);
        EUR=new BigDecimal(64999);
        HRK=new BigDecimal(63999);
        exposureColProId=bo461.getLastExposureCalculationID("N");
        criticalExposurePercentage= new BigDecimal(1.49);
        current_year = Calendar.getInstance().get(Calendar.YEAR);
        outputFile= outputDir + proc_date + "_" + batchProcess+ "_Revalorize.csv";
        outputFileFilter=outputFile;
        dataCounter=0;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#isAlwaysFreshStart()
     */
    @Override
    protected boolean isAlwaysFreshStart() {
        return false;
    } 

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#isFileTransfer()
     */
    @Override
    protected boolean isFileTransfer() {
        return false;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#isInFileManipulation()
     */
    @Override
    protected boolean isInFileManipulation() {
        return false;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#isMQNotify()
     */
    @Override
    protected boolean isMQNotify() {
        return false;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#isOutFileManipulation()
     */
    @Override
    protected boolean isOutFileManipulation() {
        return manipulateOutFile;
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#runBatch()
     */
    @Override
    protected String runBatch() throws Exception {
        getContext().debug("runBatch tyip:"+batchProcess);
        getContext().getBatchTransactionHeader().setModuleName("COL");
        if(batchProcess== ProcessType.Vehicle){
            return processVehicles();
        }else if(batchProcess== ProcessType.RealEstate){
            return processRealEstate();
        }else{
            return RemoteConstants.RET_CODE_ERROR;
        }
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#setColProId(java.math.BigDecimal)
     */
    @Override
    protected void setColProId(BigDecimal col_pro_id) {
        bo461.setColProId(col_pro_id);
    }

    /* (non-Javadoc)
     * @see hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch#setEve_typ_id()
     */
    @Override
    protected void setEve_typ_id() {
        this.eve_typ_id=new BigDecimal("713835984");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("713861724"));
        bp.setArgs(args);
        new BO460().run(bp);

    }
     
    public String processVehicles() throws Exception
    {
        String result = RemoteConstants.RET_CODE_SUCCESSFUL;
        VehicleIterator iter = bo461.selectVehicleIterator(col_cat_id, last_amort_date);
        processIterator = iter;  //postavlja se iterator koji ce se na kraju eksplicitno zatvoriti u slucaju greske ili zavrsetka obrade
        boolean fetched = false;
      
        if(iter!=null)
        {  
            while(iter.next())
            {
                if(!fetched) fetched = true;
                getContext().info("obradujem kolateral vozilo:" + iter.col_num() + " - col_hea_id: " + iter.col_hea_id());
                String proc_status = "0";
                BigDecimal col_tur_id = null;
  
                try
                {
                    int made_year = Integer.parseInt(iter.veh_made_year());
                    BigDecimal n = new BigDecimal(current_year - made_year + 1);
                    BigDecimal k = new BigDecimal(1).subtract(koef_amort.multiply(n));  // k = 1 - (koef_amort * n)
                    
//                    getContext().info("god.proizvodnje: " + made_year + "trenutna god.-god.proizvodnje+1: "+n+ "koeficijent amortizacije: " + k);
                    
//                    if(k.compareTo(BigDecimal.ZERO) <= 0) continue;
                    BigDecimal market_value_new = iter.estimated_value().multiply(k).setScale(2, RoundingMode.HALF_EVEN);
                    if(DecimalUtils.compareValues(market_value_new, zero, DecimalUtils.LOW_PRECISION) <= 0) market_value_new = zero;
                    BigDecimal amort_value = iter.market_value_old().subtract(market_value_new);
                    
                    getContext().info("god.proizvodnje: " + made_year + " - trenutna god.-god.proizvodnje+1: "+n+ " - koeficijent amortizacije: " + k +" - amort_value: " + amort_value);
//                    getContext().info("amort_value: " + amort_value);
                    
                    if(amort_value.compareTo(zero) <= 0) {
                        getContext().info("daljnja amortizacija nije moguca, preskacem kolateral: " + iter.col_num());                        
                        continue;  // ako daljnja amortizacija kolaterala nije moguæa, preskoèi zapis
                    }
                    BigDecimal percentage = koef_amort.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
 
                    String addRequest = "N";
                    if("D".equalsIgnoreCase(iter.veh_kasko())) addRequest = "D";
                    BigDecimal ponder = bo461.getPonder(iter.col_hea_id(), col_cat_id, iter.col_typ_id(), iter.col_sub_id(), addRequest);
                    BigDecimal weigh_value_new = market_value_new.multiply(ponder).setScale(2, RoundingMode.HALF_EVEN);
                     
                    getContext().beginTransaction();
                    bo461.updateCollHead(iter.col_hea_id(), market_value_new, koef_amort, iter.market_value_old(), weigh_value_new, iter.weight_value_old(), iter.old_date(), iter.reva_date_am());
                    getContext().info("gotov update coll_head: " + amort_value);
                    postingEngine.CollPosting(iter.col_hea_id(), false);
                    getContext().info("gotovo knjizenje: " + amort_value);
                    col_tur_id = bo461.insertColTurnover(iter.col_hea_id(), iter.col_typ_id(), null, iter.market_value_old(), amort_value, percentage, iter.old_date(), proc_date,
                            proc_status, iter.col_num(), n.toString(), ""+getMonthDifference(iter.old_date(), proc_date));
                    getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), proc_status, col_tur_id);
                    writeToFileVehicle(iter.col_num(),bo461.getVehSubTypeName(iter.col_sub_id()), bo461.getCurrencyName(iter.market_currency()),iter.estimated_value(), iter.old_date(), iter.market_value_old(), iter.veh_made_year(), k, amort_value, market_value_new);
                    getContext().commitTransaction();
                    incrementCounter(1);
                    getContext().info("zavrsio col_hea_id:"+iter.col_hea_id()+" uz col_tur_id:"+col_tur_id);

                }
                catch(Exception ex)
                { 
                    getContext().warning("colateral col_hea_id:"+iter.col_hea_id()+" nije obraden zbog greske:\n"+ex.getMessage());
                    ex.printStackTrace();
                    getContext().rollbackTransaction();
                    proc_status = "1";
                    getContext().beginTransaction();
                    getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), proc_status, col_tur_id);
                    getContext().commitTransaction();
                }               
            }
// posalji na mail     
            
            closeFileStreaming();
            if(fileOut!=null){
                getContext().info("slanje datoteke vozila");
                //salje se datoteka mailom
                YXY70.send(getContext(), mailcode, getContext().getLogin(), this.outputFile);
            }
            
        }
        
        if(!fetched) getContext().warning("Nije dohvacen niti jedan kolateral!");  // ako nije dohvacen niti jedan kolateral ispisuje se greska u obradi
        
        return result;
    }
    
    public String processRealEstate() throws Exception{
        String result=RemoteConstants.RET_CODE_SUCCESSFUL;
        try{
            RealEstateIterator iter= bo461.selectRealEstateIterator(col_cat_id, last_amort_date);

            //postavlja se iterator koji ce se na kraju eksplicitno zatvoriti u slucaju greske ili zavrsetka obrade
            processIterator=iter;
            boolean fetched=false;
            if(iter!=null){  
                while(iter.next()){ 
                    getContext().debug("Procesiram nekretninu = " + iter.col_num() + " ID="+iter.col_hea_id() + " reva flag: "  + iter.reva_flag());
                    if(!fetched){
                        fetched=true;
                    }
                    try{
                        if(bo461.getNumberOfNonRetailCustomers(iter.col_hea_id()) > 0) {
                            getContext().debug("Nekretnina nije retail nekretnina: "+iter.col_num() + " - " + iter.col_hea_id() + " broj nonretail: ");
                            continue;
                        }
                        
                        if("0".equalsIgnoreCase(iter.reva_flag())){
                            //grupna revalorizacija
                            getContext().beginTransaction();
                            revalorization(iter);
                            getContext().commitTransaction();
                        }else{
                            //amortizacija ili lista revalorizacije
                            getContext().beginTransaction();
                            amortization(iter);
                            getContext().commitTransaction();
                        }
                        
                    }catch(SQLException sqle){
                        getContext().warning("greska u zapisu:"+iter.col_hea_id());
                        getContext().rollbackTransaction();
                        getContext().warning("colateral col_hea_id:"+iter.col_hea_id()+" nije obraden zbog greske:\n"+sqle.getMessage());
                        getContext().warning("SQLCODE: "+sqle.getErrorCode());
                        sqle.printStackTrace();
                        getContext().beginTransaction();
                        getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), "1", null);
                        getContext().commitTransaction();
                    }
                }
            }
            if(!fetched){
                //ako nije dohvacen niti jedan kolateral ispisuje se greska u obradi
                getContext().warning("Nije dohvacen niti jedan kolateral!");
            }
            closeFileStreaming();
//            if(fileOut!=null){
                getContext().debug("slanje datoteke");
                //salje se datoteka mailom
                YXY70.send(getContext(), mailcode, getContext().getLogin(), this.outputFile);
//            }
            
        }catch(Exception e){
            getContext().debug("Exc:"+e.getMessage());
            e.printStackTrace();
            throw e;
        }finally{
            closeFileStreaming();
        }
        return result;
    }
    
    private void closeFileStreaming() throws IOException{
        if(fWriter!=null){
            fWriter.close();
            fWriter=null;
        }
        if(fOutputStream!=null){
            fOutputStream.close();
            fOutputStream=null;
        }
        if(fileOut!=null){
            manipulateOutFile=true;
        } 
    }
    
    public void amortization(RealEstateIterator iter) throws Exception
    {
        getContext().debug("obradujem amortizacija col_hea_id:"+iter.col_hea_id());
        BigDecimal EURValue = toEuros(iter.market_currency(), iter.market_value_old());
        int years_diference = getMonthDifference(iter.datum_procjene(), proc_date) / 12;
        BigDecimal exposureBurden = bo461.getExposuredCollateralAmount(iter.col_hea_id(), exposureColProId);
        if (exposureBurden == null)
            exposureBurden = new BigDecimal("0.00");
        BigDecimal exposurePercentage = new BigDecimal("0.00");
        if(zero.compareTo(exposureBurden) != 0) exposurePercentage = iter.market_value_old().divide(exposureBurden, RoundingMode.HALF_EVEN);
        getContext().debug("uvjeti amortizacije col_hea_id:"+iter.col_hea_id()+" EURVAlue:" + EURValue + " years_diference:" + years_diference + " exposurePercentage" + exposurePercentage);
        // uvjeti amortizacije
        if((DecimalUtils.compareValues(EURValue, new BigDecimal(100000), DecimalUtils.LOW_PRECISION) >= 0) &&
            (years_diference >= 9) &&
            (DecimalUtils.compareValues(exposurePercentage, criticalExposurePercentage, DecimalUtils.LOW_PRECISION) <= 0))
        {
            getContext().debug("Ispunjeni uvjeti za amortizaciju: ");
            BigDecimal n = new BigDecimal(years_diference);
            BigDecimal k = new BigDecimal(1).subtract(koef_amort.multiply(n));  // k = 1 - (koef_amort * n)
            BigDecimal market_value_new = iter.estimated_value().multiply(k).setScale(2, RoundingMode.HALF_EVEN);
            if(DecimalUtils.compareValues(market_value_new, zero, DecimalUtils.LOW_PRECISION) <= 0) market_value_new = zero;
            BigDecimal amort_value = iter.market_value_old().subtract(market_value_new);
            
            getContext().debug("n:"+n + " k:"+k + " market_value_new:"+market_value_new + " amort_value:"+amort_value);
            if(amort_value.compareTo(zero) <= 0) return;  // ako daljnja amortizacija kolaterala nije moguæa, preskoèi zapis
            BigDecimal percentage = koef_amort.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_EVEN);
            
            BigDecimal ponder = bo461.getPonder(iter.col_hea_id(), col_cat_id, iter.col_typ_id(), iter.col_sub_id(), "N");
            getContext().debug("dohvacen ponder za col_hea_id:"+iter.col_hea_id()+" ponder="+ponder);
            BigDecimal weigh_value_new = market_value_new.multiply(ponder).setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal col_tur_id = null;
            bo461.updateCollHead(iter.col_hea_id(), market_value_new, koef_amort, iter.market_value_old(), weigh_value_new, iter.weight_value_old(), iter.old_date(), iter.reva_date_am());
            postingEngine.CollPosting(iter.col_hea_id(), false);
            col_tur_id=bo461.insertColTurnover(iter.col_hea_id(), iter.col_typ_id(), null, iter.market_value_old(), amort_value, percentage, iter.old_date(), proc_date,
                    "0", iter.col_num(), n.toString(), ""+getMonthDifference(iter.old_date(), proc_date));
            getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), "0", col_tur_id);
            incrementCounter(1);
            getContext().debug("zavrsio amortizacija col_hea_id:"+iter.col_hea_id()+" uz col_tur_id:"+col_tur_id);
        }
        else  // salje se na listu za rucnu revalorizaciju
        {
            getContext().debug("Nisu ispunjeni uvjeti za amortizaciju, saljem na listu za rucnu revalorizaciju: " + iter.col_num());
            writeToFile(iter.col_num(), bo461.getColTypeName(iter.col_typ_id()), bo461.getColSubTypeName(iter.col_sub_id()), bo461.getCurrencyName(iter.market_currency()), iter.market_value_old(), iter.old_date(), iter.datum_procjene());
            getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), "0", null);
        }
    }
      
    public void revalorization(RealEstateIterator iter) throws Exception
    {
        getContext().debug("obradujem revalorizacija col_hea_id:"+iter.col_hea_id());
        int years_diference = getMonthDifference(iter.old_date(), proc_date)/12;
        BigDecimal k = null;  // koeficijent revalorizacije
        
        /*  08.12.2009.   PROMJENA DOHVATA PO OPCINAMA */
        HashMap placeData = bo461.getRealEstatePlaceData(iter.col_hea_id());
        BigDecimal col_place = (BigDecimal)placeData.get("col_place");
        BigDecimal col_county = (BigDecimal)placeData.get("col_county");
        BigDecimal cada_id = (BigDecimal)placeData.get("cada_id");
        
        String datum_string = iter.datum_procjene().toString();
        String godina = iter.datum_procjene().toString().substring(0,4);
        int estimate_year = Integer.parseInt(godina);
        
        if(isZagreb(col_place)) {
            getContext().debug("Nekretnina iz Zagreba, dohvat koef za col_cat_id,col_typ_id,col_place,cada_id,godina:"+iter.col_hea_id()+";"+iter.col_cat_id()+";"+iter.col_typ_id()+";"+col_place+";"+cada_id+";"+iter.datum_procjene()+";"+estimate_year);            
            k = bo461.getZagrebCoef(iter.col_cat_id(), iter.col_typ_id(), col_place, cada_id, estimate_year);

        } 
        else if(inCities(col_place)) {
            getContext().debug("Nekretnina iz grada, dohvat koef za col_cat_id,col_typ_id,col_place,godina:"+iter.col_hea_id()+";" + iter.col_cat_id()+";"+iter.col_typ_id()+";"+col_place+";"+iter.datum_procjene()+";"+estimate_year);   
            k = bo461.getCityCoef(iter.col_cat_id(), iter.col_typ_id(), col_place, estimate_year);
        }
            else {
                getContext().debug("Nekretnina iz zupanije, dohvat koef za col_cat_id,col_typ_id,col_county,col_place,godina:"+iter.col_hea_id()+";" + iter.col_cat_id()+";"+iter.col_typ_id()+";"+col_county+";"+iter.datum_procjene()+";"+estimate_year);   
                k = bo461.getRevalorizationCoef(iter.col_cat_id(), iter.col_typ_id(), col_county, estimate_year);
        }
        getContext().debug("dohvacen koeficijent revalorizacije: "+k);
        /*  08.12.2009. END */   
              
        //ponder
        BigDecimal ponder = bo461.getPonder(iter.col_hea_id(), iter.col_cat_id(), iter.col_typ_id(), iter.col_sub_id(), "N");
        getContext().debug("dohvacen ponder za col_hea_id:"+iter.col_hea_id()+" ponder="+ponder);
        BigDecimal market_value_new = k.multiply(iter.market_value_old()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal reva_value = market_value_new.subtract(iter.market_value_old());
        BigDecimal weigh_value_new = iter.market_value_old().multiply(ponder).setScale(2, RoundingMode.HALF_EVEN);
        getContext().debug(" market_value_new:"+market_value_new + " reva_value:"+reva_value + " weigh_value_new:"+weigh_value_new);
        //update kolaterala
//        getContext().beginTransaction();
        bo461.updateCollHead(iter.col_hea_id(), market_value_new, k, iter.market_value_old(), weigh_value_new, iter.weight_value_old(), iter.old_date(), iter.reva_date_am());
        postingEngine.CollPosting(iter.col_hea_id(), false);
        BigDecimal col_tur_id=bo461.insertColTurnover(iter.col_hea_id(), iter.col_typ_id(), null, iter.market_value_old(), reva_value, k, iter.old_date(), proc_date,
                "0", iter.col_num(), ""+years_diference,""+getMonthDifference(iter.old_date(), proc_date));
        getCommonCollateralMethods().insertInDataDwhItem(getColProId(), iter.col_hea_id(), iter.col_num(), "0", col_tur_id);
        incrementCounter(1);
//        getContext().commitTransaction();
        getContext().debug("zavrsio revaloriziranje col_hea_id:"+iter.col_hea_id()+" uz col_tur_id:");
    }
     
    /**
     * racuna se razlika u mjesecima
     * 
     * @param before stari datum
     * @param after novi datum
     * @return after- before u mjesecima
     */
    private int getMonthDifference(Date before, Date after){
        int result=0;
        String s_before=before.toString();
        String s_after=after.toString();
        
        int y_before=Integer.parseInt(s_before.substring(0, 4));
        int y_after=Integer.parseInt(s_after.substring(0, 4));
        
        int m_before=Integer.parseInt(s_before.substring(5, 7));
        int m_after=Integer.parseInt(s_after.substring(5, 7));
        
        int d_before=Integer.parseInt(s_before.substring(8));
        int d_after=Integer.parseInt(s_after.substring(8));

        result=(y_after-y_before)*12 +(m_after-m_before);
        if(d_before>d_after){
            result--;
        }
        return result;
    }
     
    /**
     * preracun u EUR
     * 
     * @param cur_id id valute
     * @param value iznos u valuti
     * @return vrijednost u EUR valuti
     * @throws Exception
     */
    private BigDecimal toEuros(BigDecimal cur_id, BigDecimal value)throws Exception{
        if((cur_id==null) ||(value==null)) return null;
        if(cur_id.compareTo(EUR)==0){
            return value;
        }else if(cur_id.compareTo(HRK)==0){
            return exchange(cur_id, value, value_date, false);
        }else{
            BigDecimal tmpHRK=exchange(cur_id, value, value_date, true);
            return exchange(EUR, tmpHRK, value_date, true);
        }
    }
    
    private void writeToFile(String col_num, String col_type, String col_sub_type, String col_cur, BigDecimal market_value_old, Date date_market, Date date_reval) throws Exception{
        if(fileManager==null){
            fileManager= bc.getFileManager();
            fileOut= new VFile(outputFile);
            fOutputStream= fileManager.getOutputStream(FileManager.OUTPUT_STREAM_TYPE_FILE, fileOut);
            fWriter= fileManager.getWriter(FileManager.WRITER_TYPE_BUFFERED, fOutputStream, encoding);
            fWriter.write(cvsHeader+"\n");
            fWriter.flush();
            fOutputStream.flush();
        }

        StringBuffer buffer=new StringBuffer();
        append(buffer,col_num);
        append(buffer,col_type);
        append(buffer,col_sub_type);
        append(buffer,col_cur);
        append(buffer,market_value_old);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        append(buffer,formatter.format(date_market));
        append(buffer,formatter.format(date_reval));
        buffer.append("\n");
        fWriter.write(buffer.toString());
        fWriter.flush();
        fOutputStream.flush();
    }
     
    private void writeToFileVehicle(String col_num, String col_sub_type, String col_cur, BigDecimal estimate_value, Date date_market, BigDecimal market_value_old, String made_year, BigDecimal koeficijent, BigDecimal amort_value, BigDecimal market_value_new) throws Exception{
        if(fileManager==null){
            fileManager= bc.getFileManager();
            fileOut= new VFile(outputFile);
            fOutputStream= fileManager.getOutputStream(FileManager.OUTPUT_STREAM_TYPE_FILE, fileOut);
            fWriter= fileManager.getWriter(FileManager.WRITER_TYPE_BUFFERED, fOutputStream, encoding);
            fWriter.write(cvsHeaderVehicle+"\n");
            fWriter.flush();
            fOutputStream.flush();
        }
        
        StringBuffer buffer=new StringBuffer();
        append(buffer,col_num);
        append(buffer,col_sub_type);
        append(buffer,col_cur);
        append(buffer,estimate_value);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        append(buffer,formatter.format(date_market));
        append(buffer,market_value_old);
        append(buffer,made_year);
        append(buffer,koeficijent);
        append(buffer,market_value_new);
        append(buffer,amort_value);

        
        buffer.append("\n");
        fWriter.write(buffer.toString());
        fWriter.flush();
        fOutputStream.flush();
    }

    /** 
     * Metoda koja zadanom StringBufferu dodaje vrijednost predanog objekta (ako je razlièit od null) i delimiter.
     * @param buffer StringBuffer
     * @param value objekt koji sadrži vrijednost
     */
    private void append(StringBuffer buffer, Object value)
    {
        if(value != null) buffer.append(value.toString().trim());
        buffer.append(";");  // delimiter
    }
    
    protected boolean inCities(BigDecimal placeID){
        if(placeID==null) return false;
        String city=placeID.toString();
        for(int i=0; i< cities.length;i++){
            if(city.equals(cities[i])) return true;
        }
        return false;
    }
    
    protected boolean isZagreb(BigDecimal placeID){
        if(placeID==null) return false;
        if(placeID.toString().equals(Zagreb)){
            return true;
        }else{
            return false;
        }
    }
    
    
}

