/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo30;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.yoy0.YOY00;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.DecimalUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.StringUtils;

/**
 * @author hraamh IN2 VRP podaci za kolateral modul
 *
 */
public class BO300 extends AbstractCollateralFileTransferBatch {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/BO300.java,v 1.25 2015/04/20 11:43:03 hraziv Exp $";
    
	private BO301 bo301=null;
    protected BigDecimal col_pro_id=null;
	private int commitStep=0;
	private final int commitPackage=10;
    private int in2_proc_type=-1;
	private final String mailcode="csv211";	
    
    private BigDecimal rat_typ_id_long;
    private BigDecimal rat_typ_id_short;
    private BigDecimal rat_typ_id_SP;
    private BigDecimal rat_typ_id_MST;
    private BigDecimal rat_typ_id_MLT;
    private BigDecimal rat_typ_id_MFS;
    
    private Date yesterday=null;
    
    private Date price_date=null;
		
	public BO300(){
		super();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("2540677003"));
        bp.setArgs(args);
        new BO300().run(bp);
	}
	
	private long stockExchangeJob(BufferedReader in) throws Exception{
		String line;
		long counter=0;
		while ((line = in.readLine()) != null) {
			StockMarketDataRow row= new StockMarketDataRow(line);
			bc.debug("*********************************************");
			bc.debug("Obradujem: "+row);
			BigDecimal stock=bo301.getStockByCode(row.code);
			if(stock!=null){
				continue;
			}
			
			BigDecimal country=bo301.getCountry(row.country);
			if(country==null){
				System.out.println("No country for old men: "+row);
				continue;
			}
			bc.beginTransaction();
			stock=bo301.insertStockExchange(row.code, row.name, country);
			bc.commitTransaction();
			counter++;
			bc.debug("Dodana burza! ");
		}
		return counter;
	}

	private long stockCMVRPJob(BufferedReader in) throws Exception{
		String line;
		long counter=0;
		while ((line = in.readLine()) != null) {
			VRPCodeRow row= new VRPCodeRow(line);
			bc.debug("*********************************************");
			bc.debug("Obradujem: "+row);
			
			String cmvrp=bo301.getCMVRPType(row.code);
			if(cmvrp==null){
				bc.beginTransaction();
				bo301.insertCMVRPType(row.code,row.description);
				bc.commitTransaction();
                bc.debug("Dodana sifra! ");
			}else{
                bc.debug("Vec postojan podatak");   
            }
			counter++;			
		}
		return counter;
	}
	
	private long stockPriceJob(BufferedReader in) throws Exception{
		String line;
		long counter=0;
		while ((line = in.readLine()) != null) {
            boolean isTransacted=false;
			StockDataRow row= new StockDataRow(line);
			bc.debug("*********************************************");
			bc.debug("Obradujem: "+row);
            /* DODANO 04.09.2009. prema FBPr200006192 */
            BigDecimal rat_sco_id_long=null;
            String score_long=null;
            BigDecimal rat_sco_id_short=null;
            BigDecimal rat_sco_id_SP=null;
            String score_SP=null;
            BigDecimal rat_sco_id_MST=null;
            String score_MST=null;
            BigDecimal rat_sco_id_MLT=null;
            String score_MLT=null;
            BigDecimal rat_sco_id_MFS=null;
            String score_MFS=null;
            String score_short=null;
            Map ratingScore= bo301.getRatingScore(rat_typ_id_long, row.long_rating);
            if(ratingScore!=null){
                rat_sco_id_long=(BigDecimal)ratingScore.get("rat_sco_id");
                score_long=(String)ratingScore.get("score");
            }
            ratingScore= bo301.getRatingScore(rat_typ_id_short, row.short_rating);
            if(ratingScore!=null){
                rat_sco_id_short=(BigDecimal)ratingScore.get("rat_sco_id");
                score_short=(String)ratingScore.get("score");
            }
            
            //Dodano prema CQ 16964 24.02.2015
            //SP
            ratingScore= bo301.getRatingScore(rat_typ_id_SP, row.sp_rating);
            if(ratingScore!=null){
                rat_sco_id_SP=(BigDecimal)ratingScore.get("rat_sco_id");
                score_SP=(String)ratingScore.get("score");
            }
            //MST
            ratingScore= bo301.getRatingScore(rat_typ_id_MST, row.mst_rating);
            if(ratingScore!=null){
                rat_sco_id_MST=(BigDecimal)ratingScore.get("rat_sco_id");
                score_MST=(String)ratingScore.get("score");
            }
            
            //MLT
            ratingScore= bo301.getRatingScore(rat_typ_id_MLT, row.mlt_rating);
            if(ratingScore!=null){
                rat_sco_id_MLT=(BigDecimal)ratingScore.get("rat_sco_id");
                score_MLT=(String)ratingScore.get("score");
            }
            
            //MFS
            ratingScore= bo301.getRatingScore(rat_typ_id_MFS, row.mfs_rating);
            if(ratingScore!=null){
                rat_sco_id_MFS=(BigDecimal)ratingScore.get("rat_sco_id");
                score_MFS=(String)ratingScore.get("score");
            }
            /*end CQ 16964 */
            
            /* end FBPr200006192 */
            CollIn2Data stockData=null;
            
			BigDecimal stockId=null;
            if((row.isin!=null) && (row.isin.compareTo("")!=0)){
                stockId=bo301.getCMVRPWithIsIn(row.isin);
            }else{
                stockId=bo301.getCMVRPWithTicker(row.ticker);
			}
            //stockId=bo301.getCMVRP(row.isin,row.ticker);
			if(stockId==null){
				bc.debug("Dodajem novu vrijednosnicu");
				try{
                    stockData=makeStock(row);
                    bc.beginTransaction();
                    isTransacted=true;
                    stockId=bo301.insertCollIn2(stockData);
                    bc.commitTransaction();
				}catch (IgnoreException ie) {
					System.out.println(ie.getMessage());
                    if(isTransacted){
                        isTransacted=false;
                        bc.rollbackTransaction();
                    }
					continue;
				}
			}else{
                bc.debug("Update vrijednosnice");
                try{
                    stockData=makeStock(row);
                    stockData.col_in2_id=stockId;
                    bc.beginTransaction();
                    isTransacted=true;
                    bo301.updateCollIn2(stockData);
                    bc.commitTransaction();
                }catch (IgnoreException ie) {
                    System.out.println(ie.getMessage());
                    if(isTransacted){
                        isTransacted=false;
                        bc.rollbackTransaction();
                    }
                    continue;
                }
            }
			Date closingDate=DateUtils.addOrDeductDaysFromDate(row.price_date, 1, false);
            
			bc.beginTransaction();
			bo301.updateCollIn2Price(stockId, closingDate);
            bo301.insertCollIn2Price(stockId, row.price_date, row.price);
            //Defect 17355 START - > Treba zatvoriti rating ako nema score-a
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_long, score_long, proc_date))){
                bo301.updateRatings(rat_typ_id_long, yesterday, stockId);
                if((rat_sco_id_long!=null)&&(score_long!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_long, rat_typ_id_long, score_long);
                }
            }
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_short, score_short, proc_date))){
                bo301.updateRatings(rat_typ_id_short, yesterday, stockId);
                if((rat_sco_id_short!=null)&&(score_short!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_short, rat_typ_id_short, score_short);
                }
            }
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_SP, score_SP, proc_date))){
                bo301.updateRatings(rat_typ_id_SP, yesterday, stockId);
                if((rat_sco_id_SP!=null)&&(score_SP!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_SP, rat_typ_id_SP, score_SP);
                }
            }
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_MST, score_MST, proc_date))){                
                bo301.updateRatings(rat_typ_id_MST, yesterday, stockId);
                if((rat_sco_id_MST!=null)&&(score_MST!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_MST, rat_typ_id_MST, score_MST);
                }
            }
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_MLT, score_MLT, proc_date))){
                bo301.updateRatings(rat_typ_id_MLT, yesterday, stockId);
                if((rat_sco_id_MLT!=null)&&(score_MLT!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_MLT, rat_typ_id_MLT, score_MLT);
                }
            }
            if((bo301.isCollIn2RatingChanged(stockId,rat_typ_id_MFS, score_MFS, proc_date))){
                bo301.updateRatings(rat_typ_id_MFS, yesterday, stockId);
                if((rat_sco_id_MFS!=null)&&(score_MFS!=null))
                {
                    bo301.insertCollIn2Rating(stockId, proc_date, rat_sco_id_MFS, rat_typ_id_MFS, score_MFS);
                }
            }
            //Defect 17355 END - > Treba zatvoriti rating ako nema score-a
			bc.commitTransaction();
			counter++;		
			bc.debug("Dodana cijena! ");
		}
		return counter;
	}
	
	private CollIn2Data makeStock(StockDataRow row) throws Exception{
		CollIn2Data ns= new CollIn2Data();
		Map vrpTypeData=bo301.getFromCMVRPType(row.vp_typ, row.issuer);
		if(vrpTypeData==null){
			System.out.println("Ignoriram vrijednosnicu: "+row);
			return null;
		}
		BigDecimal col_cat_id=(BigDecimal)vrpTypeData.get("col_cat_id");
		BigDecimal col_typ_id=(BigDecimal)vrpTypeData.get("col_typ_id");
		String currency_clause=(String)vrpTypeData.get("currency_clause");
		
		ns.col_cat_id=col_cat_id;
		ns.col_typ_id=col_typ_id;
		ns.iss_cus_id=bo301.getCustomer(row.issuer);
		if(ns.iss_cus_id==null){			
			throw new IgnoreException("Nepostojeci korisnik: "+row.issuer);
		}
		ns.ticker=row.ticker;
		ns.isin=StringUtils.replaceBlankWithNull(row.isin);
		ns.nom_cur_id=bo301.getCurrency(row.nom_cur);
		if(ns.nom_cur_id==null){
			throw new IgnoreException("Nepostojeca valuta: "+row.nom_cur);
		}
		ns.nom_amount=row.nom_amount;
        
        ns.sto_mar_id=bo301.getStockByCode(row.stock_exchange);
        /*
        if("XXXX".equalsIgnoreCase(row.stock_exchange)){
            ns.stock_ind="N";
        }else{
            ns.sto_mar_id=bo301.getStockByCode(row.stock_exchange);
            if(ns.sto_mar_id==null){
                ns.stock_ind="N";
            }else{
                ns.stock_ind="D";
            }
        }
        */
        ns.stock_ind=row.main_index_indicator;
		ns.issue_date=row.issue_date;
		ns.maturity_date=row.maturity_date;
		ns.int_rate=row.int_rate;
		ns.currency_clause=currency_clause;
		
		ns.daily_price=row.daily_price;
		ns.seniority_indic=row.seniority_indic;
		
		
		return ns;
	}
	
    
    private String getFileToProcess(String inputDirName, String extention) throws Exception{
        File directoryResFiles = new File(inputDirName);
        String[] listOfFiles = directoryResFiles.list();
        
        for(int i=0;i<listOfFiles.length;i++){
                if((listOfFiles[i].toUpperCase().indexOf(extention.toUpperCase())!=-1) && listOfFiles[i].endsWith("marker")){
            	return listOfFiles[i].replace(".marker", "");
            }
        }
        return null; 
    }
    
    @Override
    protected void closeExtraConnections() throws Exception {
        bo301.closeExtraConnection();
    }

    @Override
    protected int getArgs(BatchContext bc) {
        inputDir=bc.getArg(1);
        if(!inputDir.endsWith("/")) inputDir+="/";
        in2_proc_type=Integer.parseInt(bc.getArg(2));
        outputDir=bc.getOutDir();
        if(!outputDir.endsWith("/")) outputDir+="/";
        return 0;
    }

    @Override
    protected String getBatchName() {
        return "bo30";
    }

    @Override
    protected BigDecimal getColProId() throws Exception {
        if(col_pro_id==null){
            col_pro_id=(new YOY00(getContext())).getNewId();
        }
        return col_pro_id;
    }

    @Override
    protected String getTargetModule() {
        return null;
    }

    @Override
    protected void init() throws Exception {        
        bo301=new BO301(getContext());
        proc_date= new Date(System.currentTimeMillis());
        rat_typ_id_short = new BigDecimal(660836251);
        rat_typ_id_long = new BigDecimal(660835251);
        //SSB ---> sp_rating se mapira u    SPIL                            S&P Long Term Rating - Issue Level
        rat_typ_id_SP = new BigDecimal("91982780351");
        //MST
        rat_typ_id_MST = new BigDecimal(660833251);
        //MLT
        rat_typ_id_MLT = new BigDecimal(660757251);
        //MFS
        //Moody's VP rating - mapira se u rat_typ_id = 91982780351       MNW                           MOODY'S NO WATCH
        rat_typ_id_MFS = new BigDecimal("91982745351");
        value_date= proc_date;
        price_date=proc_date;
        yesterday= DateUtils.addOrDeductDaysFromDate(proc_date, 1, false);
        switch(in2_proc_type){
        case 0:
            inputFile=getFileToProcess(inputDir, "_in2tv");
            proc_type="3";
            break;
        case 1:
            inputFile=getFileToProcess(inputDir, "_in2td");
            proc_type="4";
            break;
        case 2:
            inputFile=getFileToProcess(inputDir, "_in2vp");
            proc_type="5";
            break;
        default:
            throw new Exception("Unknown in2_proc_type");
        }
    }

    @Override
    protected boolean isAlwaysFreshStart() {
        return true;
    }

    @Override
    protected boolean isInFileManipulation() {
        return true;
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
        
        if(inputFile==null){
            System.out.println("Nema datoteke!");
            return RemoteConstants.RET_CODE_ERROR;
        }       
        BufferedReader in = new BufferedReader(new FileReader(inputDir +inputFile));       
        long result=0;       
        switch(in2_proc_type){
            case 0:
                result=stockExchangeJob(in);
                break;
            case 1:
                result=stockCMVRPJob(in);
                break;
            case 2:
                if(checkFalseFile(new BufferedReader(new FileReader(inputDir +inputFile)))){
                    toReturn=sendNotificationMail();
                }else{
                    result=stockPriceJob(in);
                    break; 
                }
            default:
                toReturn= RemoteConstants.RET_CODE_WARNING;
        }      
        incrementCounter(result);
        return toReturn;
    }

    @Override
    protected void setColProId(BigDecimal col_pro_id) {
        this.col_pro_id=col_pro_id;
    }

    @Override
    protected void setEve_typ_id() {
        this.eve_typ_id=new BigDecimal("2540684003");
        
    }
    
    protected String sendNotificationMail() throws Exception{
        SendMail mail= new SendMail();
        
        String recipients=null;
        
        try {
            recipients=bo301.fetchRecipients(mailcode);    
        } catch (Exception e) {
            bc.info("Greska pri dohvatu mail adresa.");
            return RemoteConstants.RET_CODE_ERROR;
        }   
        String subject="bo30: VRP - preuzimanje cijena";
        String msg="Automatska obavijest o neizvrsenoj obradi u modulu kolaterala - podaci i cijene za vrijednosne papire nisu preuzeti iz IN2 sa datumom "+ DateUtils.getDDMMYYYY(price_date);
        int mailFlag=mail.send(bc, "", "", recipients, "", "", subject, msg, null, null);            
        bc.info("mailFlag(0-poslano,1-error)="+mailFlag);
        bc.warning(("Nema novih podataka za obraditi.\n"));                
        return RemoteConstants.RET_CODE_WARNING;
        
    }
    
    private boolean checkFalseFile(BufferedReader in) throws IOException{
        String line=null;
        BigDecimal zero= new BigDecimal(0);
        boolean result=true;
        while ((line = in.readLine()) != null) {
            StockDataRow row= new StockDataRow(line);
            if((row.price!=null) &&(DecimalUtils.compareValues(row.price, zero, 2)!=0)){
                result=false;
                break;
            }
            price_date=row.price_date;
        }
        in.close();
        return result;
    }
	
	
	
}
