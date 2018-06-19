package hr.vestigo.modules.collateral.batch.bo72;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author hrakis
 */
public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo72/CollateralData.java,v 1.1 2012/04/27 08:19:25 hrakis Exp $";
    
    
    public BigDecimal col_hea_id;
    
    public String col_num;
    
    public BigDecimal value;
    
    public BigDecimal value_cur_id;
    
    public BigDecimal value_hrk;
    
    public BigDecimal value_for_coverage_hrk;
    
    public TreeMap<Integer, MortgageData> mortgages;
    
    public ArrayList<PlacementData> placements;
    
    public ArrayList<CoverageData> coverages;
}


class MortgageData
{
    public BigDecimal coll_hf_prior_id;
    
    public int priority;
    
    public BigDecimal amount;
    
    public BigDecimal amount_cur_id;
    
    public BigDecimal amount_hrk;
    
    public boolean isRBA;
    
    public FrameAgreementData frame_agreement;
    
    public ArrayList<PlacementData> placements;
}


class FrameAgreementData
{
    public BigDecimal fra_agr_id;
    
    public String agreement_no;
    
    public BigDecimal amount;
    
    public BigDecimal amount_cur_id;
    
    public BigDecimal amount_hrk;
}


class PlacementData
{
    public BigDecimal cus_acc_id;
    
    public String cus_acc_no;
    
    public BigDecimal exposure;
    
    public BigDecimal exposure_cur_id;
    
    public BigDecimal exposure_hrk;
    
    public BigDecimal exposure_for_coverage_hrk;
    
    public BigDecimal cus_id;
    
    public BigDecimal exposure_bal_lcy;
    
    public BigDecimal exp_off_bal_lcy; 
    
    public boolean isCovered;
}


class CoverageData
{
    public PlacementData placement;
    
    public BigDecimal amount_hrk;
}


class ExchangeRateData
{
    public BigDecimal cur_id;
    
    public String code_num;
    
    public String code_char;
    
    public BigDecimal midd_rate;
}