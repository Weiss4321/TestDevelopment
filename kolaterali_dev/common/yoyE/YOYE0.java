//created 2009.09.18
package hr.vestigo.modules.collateral.common.yoyE;
       
import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.framework.remote.transaction.ConnCtx;
    
import java.math.BigDecimal;
import java.sql.Date;
/**
 *
 * @author hramkr
 */
public class YOYE0 {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyE/YOYE0.java,v 1.13 2017/12/21 14:04:06 hrazst Exp $";
    /**
     * Common za izracun ponderirane i raspolozive vrijednosti kolaterala
     */
    private String bank_sign=null;
    private ConnCtx ctx=null;
    private RemoteContext rc=null;

    public YOYE0(RemoteContext rc) throws Exception {
        this.ctx = rc.getContext();
        this.bank_sign = rc.getBankSign();
        this.rc=rc;
    }
    /**  
     * Method fills the data object (racuna ponderiranu i raspolozivu vrijednost kolaterala)
     * @param kolData
     * @throws Exception
     */
    
    public void getNoPonderAndPonderRestAmount(YOYE0Data kolData) throws Exception{
    
        YOYE1 YOEY1_get = new YOYE1(rc);
        Iter_ponder iter_ponder = null;
        Iter iter = null;
        Iter_rba_neponder iter_rba_neponder = null;
        Iter_rba_ponder iter_rba_ponder = null;
        BigDecimal co_ponder = null;
        BigDecimal dfl_ponder = null;
        BigDecimal NorbaMortgage = null;
        BigDecimal market_value = new BigDecimal(0); 
        
// 1. dohvatiti pondere za kolateral
        try{
            rc.debug("YOEY1_get.getCollMvpPonder() --> col_hea_id = "+kolData.colHeaId + kolData.colCatId + kolData.colTypId + kolData.colSubTypId);            
            iter_ponder = YOEY1_get.getCollMvpPonder(kolData);
            
            while (iter_ponder.next()){
                rc.debug("YOEY1_get.getCollMvpPonder() --> mvp_dfl_ponder = "+iter_ponder.dfl_ponder());
                kolData.minPonder = iter_ponder.min_ponder();
                kolData.dflPonder = iter_ponder.dfl_ponder();
                kolData.maxPonder = iter_ponder.max_ponder();               
            }
        }finally{
            if(iter_ponder!=null) {
                try {
                    iter_ponder.close();
                } catch (Exception ignored) {}
            }
        }        
       
        dfl_ponder = kolData.dflPonder;
// 1a. dohvatiti CO dfl mvp ponder ako postoji
        rc.debug("YOEY1_get.getCoMvpPonder() --> col_hea_id = "+kolData.colHeaId);        
        co_ponder = YOEY1_get.getCoMvpPonder(kolData);
        rc.debug("YOEY1_get.getCoMvpPonder() --> col_hea_id = "+kolData.colHeaId + co_ponder);
        if (co_ponder != null)
            dfl_ponder = co_ponder;
        
//        kolData.ponderAmount = getPonderAmountOfCollateral(dfl_ponder, kolData.nominalAmount);
        
// neponderirana trzisna vrijednost: kolData.nominalAmount
// ponderirana trzisna vrijednost: kolData.ponderAmount
         

        kolData.exposureDate = getMaxExposureDate();
        kolData.coverDate = getMaxCoverDate();
        kolData.NDCoverDate = getMaxNDCoverDate();

        if (kolData.nominalAmount == null)
            kolData.nominalAmount = new BigDecimal("0.00");
        
        market_value = YOEY1_get.getMarketValueOnExposureDate(kolData.colHeaId);
        if (market_value.compareTo(new BigDecimal("0.00")) != 0)
            kolData.nominalAmount = market_value;
          
        kolData.ponderAmount = getPonderAmountOfCollateral(dfl_ponder, kolData.nominalAmount);
        
        kolData.restAmount = kolData.nominalAmount;     // raspolozivi iznos - neponderirani
        kolData.restPonAmount = kolData.ponderAmount;   // raspolozivi iznos - ponderirani
        kolData.covAmount = new BigDecimal("0.00");
        kolData.covPonAmount = new BigDecimal("0.00");
        
        kolData.exposureAmount = new BigDecimal("0.00");        // izlozenost svih partija vezanih na kolateral iz tbl cusacc_exposure
        kolData.exposureNoPonAmount = new BigDecimal("0.00");   // iskoristeni dio kolaterala iz tbl cusac_exp_coll-neponderirani
        kolData.exposurePonAmount = new BigDecimal("0.00");     // iskoristeni dio kolaterala iz tbl cusac_exp_coll-ponderirani
          
        try{
            rc.debug("YOEY1_getCollNeponderAmount() --> col_hea_id = "+kolData.colHeaId);            
            iter_rba_neponder = YOEY1_get.getCollNeponderAmount(kolData);
            
            while (iter_rba_neponder.next()){
                rc.debug("YOEY1_get.getCollNeponderAmount() --> col_hea_id = "+kolData.colHeaId);
                if(iter_rba_neponder.np_exp_col_amount() != null){

                    kolData.restAmount = kolData.restAmount.subtract(iter_rba_neponder.np_exp_col_amount());
                    kolData.exposureNoPonAmount = kolData.exposureNoPonAmount.add(iter_rba_neponder.np_exp_col_amount());
                      
                 }
            }  
        }finally{
            if(iter_rba_neponder!=null) {
                try {
                    iter_rba_neponder.close();
                } catch (Exception ignored) {}
            }
        }
//        kolData.exposureAmount = kolData.exposureNoPonAmount;
// dohvatiti izlozenost svih partija vezanih na kolateral
        try{
            rc.debug("YOEY1_get.getCollExposure() --> col_hea_id = "+kolData.colHeaId);            
            iter = YOEY1_get.getCollExposure(kolData);
            
            while (iter.next()){
                rc.debug("YOEY1_get.getCollExposure() --> exposure_cur_id = "+iter.exposure_cur_id());
                if(iter.exposure_amount() != null && iter.exposure_cur_id() != null && kolData.colCurId != null){
                    kolData.exposureAmount = kolData.exposureAmount.add(getExpAmountInCollateralCurrency(kolData.colCurId,iter.exposure_cur_id(),iter.exposure_amount()));
                 }
            }
        }finally{
            if(iter!=null) {
                try {
                    iter.close();
                } catch (Exception ignored) {}
            }
        }            
         
              
// ako nema iznosa u izracunu pokrivenosti raspolozivo = trzisna - izlozenost svih partija 
        if (kolData.exposureNoPonAmount.compareTo(new BigDecimal("0.00")) == 0){
            kolData.exposureNoPonAmount = kolData.exposureAmount;
            kolData.restAmount = kolData.restAmount.subtract(kolData.exposureAmount);
        }
          
// dohvatiti hipoteke u tudju korist
        
        rc.debug("getMortgagesSum() --> col_hea_id = "+kolData.colHeaId+kolData.colCurId);          
        kolData.otherMortgAmount = YOEY1_get.getMortgagesSum(kolData.colHeaId, kolData.colCurId);   
        rc.debug("getMortgagesSum() --> NorbaMortgage = "+kolData.otherMortgAmount);
        
        if (kolData.otherMortgAmount !=null) {
            kolData.restAmount = kolData.restAmount.subtract(kolData.otherMortgAmount);        
        } else {
            kolData.otherMortgAmount = new BigDecimal("0.00");
        }
        if (kolData.restAmount.signum() == -1 ) {
            kolData.restAmount = new BigDecimal("0.00");
        }
     
// ponderirana raspoloziva
        
        try{
            rc.debug("YOEY1_getCollPonderAmount() --> col_hea_id = "+kolData.colHeaId);            
            iter_rba_ponder = YOEY1_get.getCollPonderAmount(kolData);
            
            while (iter_rba_ponder.next()){
                rc.debug("YOEY1_getCollPonderAmount() --> col_hea_id = "+kolData.colHeaId);
                if(iter_rba_ponder.p_exp_col_amount() != null){

                    kolData.restPonAmount = kolData.restPonAmount.subtract(iter_rba_ponder.p_exp_col_amount());
                    kolData.exposurePonAmount =  kolData.exposurePonAmount.add(iter_rba_ponder.p_exp_col_amount());
                 }
            }
        }finally{
            if(iter_rba_ponder!=null) {
                try {
                    iter_rba_ponder.close();
                } catch (Exception ignored) {}
            }
        }
 
     // ako nema iznosa u izracunu pokrivenosti raspolozivo = ponderiran trzisna - izlozenost svih partija         
        if (kolData.exposurePonAmount.compareTo(new BigDecimal("0.00")) == 0){
            kolData.exposurePonAmount = kolData.exposureAmount;
            kolData.restPonAmount = kolData.restPonAmount.subtract(kolData.exposureAmount);            
        }
               
        if (kolData.otherMortgAmount !=null) {
            kolData.restPonAmount = kolData.restPonAmount.subtract(kolData.otherMortgAmount);        
        }        
        if (kolData.restPonAmount.signum() == -1 ) {
            kolData.restPonAmount = new BigDecimal("0.00");
        }        
          
    }    
    /**  
     * Method fills the data object (racuna neponderiranu raspolozivu vrijednost kolaterala)
     * @param kolData
     * @throws Exception
     */   
    public void getPonderRestAmount(YOYE0Data kolData) throws Exception{
        YOYE1 YOEY1_get = new YOYE1(rc);
        Iter_rba_neponder iter_rba_neponder = null;   
        
        kolData.coverDate = getMaxCoverDate();
        
        
        if (kolData.nominalAmount == null)
            kolData.nominalAmount = new BigDecimal("0.00");
        
        kolData.restAmount = kolData.nominalAmount;
        
        
        try{
            rc.debug("YOEY1_getCollNeponderAmount() --> col_hea_id = "+kolData.colHeaId);            
            iter_rba_neponder = YOEY1_get.getCollNeponderAmount(kolData);
            
            while (iter_rba_neponder.next()){
                rc.debug("YOEY1_get.getCollNeponderAmount() --> col_hea_id = "+kolData.colHeaId);
                if(iter_rba_neponder.np_exp_col_amount() != null){

                    kolData.restAmount = kolData.restAmount.subtract(iter_rba_neponder.np_exp_col_amount());
                      
                 }
            }  
        }finally{
            if(iter_rba_neponder!=null) {
                try {
                    iter_rba_neponder.close();
                } catch (Exception ignored) {}
            }
        }
        
        if (kolData.restAmount.signum() == -1 ) {
            kolData.restAmount = new BigDecimal("0.00");
        }
    }
    public void getPonderAndRestAmount(YOYE0Data kolData) throws Exception{
        
        YOYE1 YOEY1_get = new YOYE1(rc);
        Iter_ponder iter_ponder = null;
        Iter iter = null;
        BigDecimal co_ponder = null;
        BigDecimal dfl_ponder = null;
         
// 1. dohvatiti pondere za kolateral
        try{
            rc.debug("YOEY1_get.getCollMvpPonder() --> col_hea_id = "+kolData.colHeaId + kolData.colCatId + kolData.colTypId + kolData.colSubTypId);            
            iter_ponder = YOEY1_get.getCollMvpPonder(kolData);
            
            while (iter_ponder.next()){
                rc.debug("YOEY1_get.getCollMvpPonder() --> mvp_dfl_ponder = "+iter_ponder.dfl_ponder());
                kolData.minPonder = iter_ponder.min_ponder();
                kolData.dflPonder = iter_ponder.dfl_ponder();
                kolData.maxPonder = iter_ponder.max_ponder();               
            }
        }finally{
            if(iter_ponder!=null) {
                try {
                    iter_ponder.close();
                } catch (Exception ignored) {}
            }
        }        
           
        dfl_ponder = kolData.dflPonder;
// 1a. dohvatiti CO dfl mvp ponder ako postoji
        rc.debug("YOEY1_get.getCoMvpPonder() --> col_hea_id = "+kolData.colHeaId);        
        co_ponder = YOEY1_get.getCoMvpPonder(kolData);
        rc.debug("YOEY1_get.getCoMvpPonder() --> col_hea_id = "+kolData.colHeaId + co_ponder);
        if (co_ponder != null)
            dfl_ponder = co_ponder;
        
        kolData.ponderAmount = getPonderAmountOfCollateral(dfl_ponder, kolData.nominalAmount);
//        kolData.ponderAmount = getPonderAmountOfCollateral(kolData.dflPonder, kolData.nominalAmount);
        kolData.exposureDate = getMaxExposureDate();
        
        kolData.restAmount = kolData.ponderAmount;
        kolData.exposureAmount = new BigDecimal("0.00"); 
          
        try{
            rc.debug("YOEY1_get.getCollExposure() --> col_hea_id = "+kolData.colHeaId);            
            iter = YOEY1_get.getCollExposure(kolData);
            
            while (iter.next()){
                rc.debug("YOEY1_get.getCollExposure() --> exposure_cur_id = "+iter.exposure_cur_id());
                if(iter.exposure_amount() != null && iter.exposure_cur_id() != null && kolData.colCurId != null){
                    
                    kolData.exposureAmount = kolData.exposureAmount.add(getExpAmountInCollateralCurrency(kolData.colCurId,iter.exposure_cur_id(),iter.exposure_amount()));
                    kolData.restAmount = kolData.restAmount.subtract(getExpAmountInCollateralCurrency(kolData.colCurId,iter.exposure_cur_id(),iter.exposure_amount()));
                      
                 }
            }
        }finally{
            if(iter!=null) {
                try {
                    iter.close();
                } catch (Exception ignored) {}
            }
        }
        
        if (kolData.restAmount.signum() == -1 ) {
            kolData.restAmount = new BigDecimal("0.00");
        }

    }        
    
     
    public void getRestAmount(YOYE0Data kolData) throws Exception{
  
        YOYE1 YOEY1_get = new YOYE1(rc);
        Iter iter = null;

        kolData.ponderAmount = getPonderAmountOfCollateral(kolData.ponder, kolData.nominalAmount);
        kolData.exposureDate = getMaxExposureDate();
        
        kolData.restAmount = kolData.ponderAmount;
        kolData.exposureAmount = new BigDecimal("0.00"); 
        
        try{
            rc.debug("YOEY1_get.getCollExposure() --> col_hea_id = "+kolData.colHeaId);            
            iter = YOEY1_get.getCollExposure(kolData);
            
            while (iter.next()){
                rc.debug("YOEY1_get.getCollExposure() --> exposure_cur_id = "+iter.exposure_cur_id());
                if(iter.exposure_amount() != null && iter.exposure_cur_id() != null && kolData.colCurId != null){
                    
                    kolData.exposureAmount = kolData.exposureAmount.add(getExpAmountInCollateralCurrency(kolData.colCurId,iter.exposure_cur_id(),iter.exposure_amount()));
                    kolData.restAmount = kolData.restAmount.subtract(getExpAmountInCollateralCurrency(kolData.colCurId,iter.exposure_cur_id(),iter.exposure_amount()));
                      
                 }
            }
        }finally{
            if(iter!=null) {
                try {
                    iter.close();
                } catch (Exception ignored) {}
            }
        }
        
        if (kolData.restAmount.signum() == -1 ) {
            kolData.restAmount = new BigDecimal("0.00");
        }

    } 
     
    /**
     * Method calculates the ponder amount of collateral 
     * @param ponder
     * @param nominalAmount 
     * @return ponderAmount
     * @throws Exception
     */
    private BigDecimal getPonderAmountOfCollateral(BigDecimal ponder, BigDecimal nominalAmount) throws Exception{
        
        BigDecimal ponderAmount = new BigDecimal("0.00"); 
        BigDecimal numberOneHundred = new java.math.BigDecimal("100.00"); 

        if (nominalAmount != null && ponder != null) { 
            ponderAmount = nominalAmount.multiply(ponder);
            ponderAmount= ponderAmount.divide(numberOneHundred, 2, java.math.BigDecimal.ROUND_HALF_UP);
         }
        
         return ponderAmount;
        
    }    
    
    /**
     * Method get max exposure date 
     * @param 
     * @return maxExposureDate
     * @throws Exception
     */
    private Date getMaxExposureDate() throws Exception{
        YOYE1 YOYE1_find = new YOYE1(rc);
        
        Date maxExposureDate = null;
        
        maxExposureDate = YOYE1_find.getExposureDate();
        
        return maxExposureDate;
    }
        
    /**
     * Method calculates the amount in collateral currency
     * @param col_cur_id (valuta kolaterala)
     * @param exp_cur_id (valuta izlozenosti)
     * @param exp_amount (iznos izlozenosti u valuti izlozenosti)
     * @return exp_amount_kol (iznos izlozenosti u valuti kolaterala)
     * @throws Exception
     */
    private BigDecimal getExpAmountInCollateralCurrency(BigDecimal col_cur_id, BigDecimal exp_cur_id, BigDecimal exp_amount) throws Exception{
        YOYE1 YOYE1_find = new YOYE1(rc);
        
        BigDecimal exch_rate_kol = null;
        BigDecimal exch_rate_exp = null;
        BigDecimal exp_amount_kol = null;

        exch_rate_kol = YOYE1_find.getMiddRate(col_cur_id);
        exch_rate_exp = YOYE1_find.getMiddRate(exp_cur_id);
        exp_amount_kol = exp_amount.multiply(exch_rate_exp);
        exp_amount_kol = exp_amount_kol.divide(exch_rate_kol, 2, BigDecimal.ROUND_HALF_UP);
        
        return exp_amount_kol;
    }
    /**
     * Method get max exposure date 
     * @param 
     * @return maxExposureDate
     * @throws Exception
     */
    private Date getMaxCoverDate() throws Exception{
        YOYE1 YOYE1_find = new YOYE1(rc);
        
        Date maxCoverDate = null;
        
        maxCoverDate = YOYE1_find.getCoverDate();
        
        return maxCoverDate;
    }
    
    /**
     * Method get max exposure date 
     * @param 
     * @return maxExposureDate
     * @throws Exception
     */
    private Date getMaxNDCoverDate() throws Exception{
        YOYE1 YOYE1_find = new YOYE1(rc);
        
        Date maxCoverDate = null;
        
        maxCoverDate = YOYE1_find.getNDCoverDate();
        
        return maxCoverDate;
    }
    
}