/*
 * Created on 2005.12.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
package hr.vestigo.modules.collateral.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.util.db.VestigoResultSet;

import hr.vestigo.modules.f_payment.util.FPaymUtil;


/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollateralUtil extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/CollateralUtil.java,v 1.111 2016/11/08 12:02:10 hrazst Exp $";

    private static final String dbname = "aix";
    private ResourceAccessor ra = null;

    private java.math.BigDecimal buy_fc = null;
    private java.math.BigDecimal midd_fc = null;
    private java.math.BigDecimal sell_fc = null;

    public CollateralUtil(ResourceAccessor ra) {
        super(ra);
        this.ra = ra;
    }

    public String leadingZerosToN(String inputString, int totLengthOfString){
        String retString = inputString;

        for(int k=0; k < (totLengthOfString - inputString.length());k++){

            retString = "0" + retString;
        }
        return retString;
    }//leadingZerosToN

    public boolean getCurrentExchRate(java.sql.Date argValueDate, java.math.BigDecimal argCurrency) throws hr.vestigo.framework.util.db.EmptyRowSet, java.sql.SQLException {
        javax.sql.RowSet rsExchRate = null;
        buy_fc = null;
        midd_fc = null;
        sell_fc = null;

        if(argValueDate==null){
            return false;
        }
        if(argCurrency==null){
            return false;
        }


        hr.vestigo.framework.util.db.DBParam params = new hr.vestigo.framework.util.db.DBParam(argCurrency);
        params.add(1,argValueDate);

        try {
            rsExchRate =
                ra.getMetaModelDataSource().selectPrepareRowSet(
                        "SELECT BUY_RATE_FC, MIDD_RATE, SELL_RATE_FC FROM EXCHANGE_RATE WHERE CUR_ID= ? AND ? BETWEEN DATE_FROM AND DATE_UNTIL ",params,dbname);

            if (rsExchRate.next()) {
                buy_fc = rsExchRate.getBigDecimal("BUY_RATE_FC");
                midd_fc = rsExchRate.getBigDecimal("MIDD_RATE");
                sell_fc = rsExchRate.getBigDecimal("SELL_RATE_FC");         
            }

        } catch (hr.vestigo.framework.util.db.EmptyRowSet e) {
            throw e;
            //"There is no data for requested value date;
        } catch (java.sql.SQLException se) {
            throw se;
        } finally {
            try {
                if (rsExchRate != null)
                    rsExchRate.close();
            } catch (java.sql.SQLException ignored) {
            }
        }
        if (buy_fc == null) {
            return false;
        } else {
            return true;
        }
    }//getCurrentExchRate

    public String getCodeCharOfCurrency(java.math.BigDecimal argCurId) throws hr.vestigo.framework.util.db.EmptyRowSet, java.sql.SQLException{
        javax.sql.RowSet rsCurrencyCharCode = null;
        String codeCharCurr = null;
        hr.vestigo.framework.util.db.DBParam params = new hr.vestigo.framework.util.db.DBParam(argCurId);

        try {
            rsCurrencyCharCode =
                ra.getMetaModelDataSource().selectPrepareRowSet(
                        "SELECT CODE_CHAR FROM CURRENCY WHERE CUR_ID= ? ",params,dbname);

            if (rsCurrencyCharCode.next()) {
                codeCharCurr = rsCurrencyCharCode.getString("CODE_CHAR");
            }

        } catch (hr.vestigo.framework.util.db.EmptyRowSet e) {
            throw e;
            //"There is no data for requested value date;
        } catch (java.sql.SQLException se) {
            throw se;
        } finally {
            try {
                if (rsCurrencyCharCode != null)
                    rsCurrencyCharCode.close();
            } catch (java.sql.SQLException ignored) {
            }
        }
        return codeCharCurr;

    }//getCodeCharOfCurrency


    public String getCodeNumOfCurrency(java.math.BigDecimal argCurId) throws hr.vestigo.framework.util.db.EmptyRowSet, java.sql.SQLException{
        javax.sql.RowSet rsCurrencyCodeNum = null;
        String codeNumCurr = null;
        hr.vestigo.framework.util.db.DBParam params = new hr.vestigo.framework.util.db.DBParam(argCurId);

        try {
            rsCurrencyCodeNum =
                ra.getMetaModelDataSource().selectPrepareRowSet(
                        "SELECT CODE_NUM FROM CURRENCY WHERE CUR_ID= ? ",params,dbname);

            if (rsCurrencyCodeNum.next()) {
                codeNumCurr = rsCurrencyCodeNum.getString("CODE_NUM");
            }

        } catch (hr.vestigo.framework.util.db.EmptyRowSet e) {
            throw e;
            //"There is no data for requested value date;
        } catch (java.sql.SQLException se) {
            throw se;
        } finally {
            try {
                if (rsCurrencyCodeNum != null)
                    rsCurrencyCodeNum.close();
            } catch (java.sql.SQLException ignored) {
            }
        }
        return codeNumCurr;

    }//getCodeNumOfCurrency

    public java.math.BigDecimal getBuyFCRate() {
        return buy_fc;
    }

    public java.math.BigDecimal getMiddRate() {
        return midd_fc;
    }

    public java.math.BigDecimal getSellFCRate() {
        return sell_fc;
    }


    //  da li je zadan iznos
    public boolean isAmount (java.math.BigDecimal amount) {
        if (amount == null || amount.compareTo(new java.math.BigDecimal("0.00")) == 0 || amount.toString().equalsIgnoreCase("")) 
            return false;
        else  
            return true;
    }   

    //  da li je zadan iznos
    public boolean isAmountNull (java.math.BigDecimal amount) {
        if (amount == null) 
            return false; 
        else  
            return true;
    }       

    //  da li je poznat integer
    public boolean isNumber (Integer number) {
        if (number == null || number.compareTo(new Integer("0")) == 0 || number.toString().equalsIgnoreCase("")) 
            return false;
        else  
            return true;
    }   

    public BigDecimal setDivideTwoBigDecPercent(BigDecimal number1, BigDecimal number2) {       
        //  dijeli dva bigdecimala   i racuna postotak  

        BigDecimal numberOneHundred = new java.math.BigDecimal("100.00");   
        BigDecimal number11;
        BigDecimal number21;
        BigDecimal product;

        number11 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number11.setScale(29);
        number21 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number21.setScale(29);
        product = new java.math.BigDecimal("0.000000000000000000000000000000");
        product.setScale(29);       

        number11 = number1;
        number21 = number2;

        product = number11.multiply(numberOneHundred);

        product=product.divide(number21, 2, java.math.BigDecimal.ROUND_HALF_UP);

        return product;
    }        

    public BigDecimal setDivideTwoBigDec(BigDecimal number1, BigDecimal number2) {      
        //  dijeli dva bigdecimala      

        BigDecimal number11;
        BigDecimal number21;
        BigDecimal product;

        number11 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number11.setScale(29);
        number21 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number21.setScale(29);
        product = new java.math.BigDecimal("0.000000000000000000000000000000");
        product.setScale(29);       

        number11 = number1;
        number21 = number2;

        product=number11.divide(number21, 2, java.math.BigDecimal.ROUND_HALF_UP);

        return product;
    }        

    public BigDecimal setDivideTwoBigDec_obveznice(BigDecimal number1, int number2) {       
        //  dijeli sa 100 odn. pomice decimalnu tocku u lijevo za 2 mjesta  

        BigDecimal product;  
        product=number1.movePointLeft(number2);

        return product;
    }        

    public java.math.BigDecimal setProductTwoBigDec(java.math.BigDecimal number1, java.math.BigDecimal number2) {       
        // mnozi dva bigdecimala, na 2 decimale     

        java.math.BigDecimal oneAmount;
        java.math.BigDecimal number11;
        java.math.BigDecimal number21;
        java.math.BigDecimal product;

        oneAmount = new java.math.BigDecimal("1.000000000000000000000000000000");
        oneAmount.setScale(29);
        number11 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number11.setScale(29);
        number21 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number21.setScale(29);
        product = new java.math.BigDecimal("0.000000000000000000000000000000");
        product.setScale(29);       

        number11 = number1;
        number21 = number2;

        product = number11.multiply(number21);

        product=product.divide(oneAmount, 2, java.math.BigDecimal.ROUND_HALF_UP);

        return product;
    }        

    public java.math.BigDecimal setProductTwoBigDec_ponder(java.math.BigDecimal number1, java.math.BigDecimal number2) {        
        //    mnozi dva bigdecimala, na 2 decimale      
        BigDecimal numberOneHundred = new java.math.BigDecimal("100.00");   
        java.math.BigDecimal oneAmount;
        java.math.BigDecimal number11;
        java.math.BigDecimal number21;
        java.math.BigDecimal product;

        oneAmount = new java.math.BigDecimal("1.000000000000000000000000000000");
        oneAmount.setScale(29);
        number11 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number11.setScale(29);
        number21 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number21.setScale(29);
        product = new java.math.BigDecimal("0.000000000000000000000000000000");
        product.setScale(29);       

        number11 = number1;
        number21 = number2;

        product = number11.multiply(number21);
        product = product.divide(numberOneHundred, 2, java.math.BigDecimal.ROUND_HALF_UP);

        return product;
    }            


    public java.math.BigDecimal setSumaTwoBigDec (java.math.BigDecimal number1, java.math.BigDecimal number2) {
        // zbraja 2 bigdecimala     
        java.math.BigDecimal totalSuma = null;

        totalSuma = number1.add(number2);

        return totalSuma;
    }    

    public java.math.BigDecimal setProductIntDec(Integer number1, java.math.BigDecimal number2) {       
        // mnozi integer i bigdecimal, na 2 decimale        

        String number1_str = null;
        java.math.BigDecimal number = null;
        if (!(number1 == null)) {
            number1_str = number1.toString();
            number = new java.math.BigDecimal(number1_str);
        }

        java.math.BigDecimal oneAmount;
        java.math.BigDecimal number11;
        java.math.BigDecimal number21;
        java.math.BigDecimal product;

        oneAmount = new java.math.BigDecimal("1.000000000000000000000000000000");
        oneAmount.setScale(29);
        number11 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number11.setScale(29);
        number21 = new java.math.BigDecimal("0.000000000000000000000000000000");
        number21.setScale(29);
        product = new java.math.BigDecimal("0.000000000000000000000000000000");
        product.setScale(29);       

        number11 = number;
        number21 = number2;

        product = number11.multiply(number21);
        product = product.divide(oneAmount, 2, java.math.BigDecimal.ROUND_HALF_UP);

        return product;
    }                

    public void countCdeAmount(ResourceAccessor ra) {
        // System.out.println("TU SAM u metodi , racunam  "+ra.getScreenContext());             
        // izracun raspolozive vrijednosti depozita nakon unosa, promjene hipoteke          
        BigDecimal numberZero = new java.math.BigDecimal("0.00");
        BigDecimal numberOneHundred = new java.math.BigDecimal("100.00");   

        // raspoloziva = ponderirana - sve hipoteke
        BigDecimal availValue = numberZero;   // raspoloziva
        BigDecimal thirdRightNom = numberZero;  // u korist trecih
        BigDecimal hfsValue = numberZero;   // u korist RBA

        availValue = (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtAcouValue");  // raspoloziva = ponedrirana

        thirdRightNom = (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtThirdRightInNom");
        hfsValue = (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtHfsValue");   

        if (isAmountNull(availValue) && isAmountNull(thirdRightNom))
            availValue = availValue.subtract(thirdRightNom);   

        if (isAmountNull(availValue) && isAmountNull(hfsValue)) 
            availValue = availValue.subtract(hfsValue);

        ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",availValue);     
        //  System.out.println("TU SAM u metodi , izracunao" +availValue);  
    }

    public void setVehLicenceCtx (ResourceAccessor ra) {
        String doc = ((String) ra.getAttribute("CollHeadLDB", "Coll_txtComDoc")).trim(); 

        if ((doc != null) && (!(doc.equals("")))) {
            if (doc.equals("D")) {  // 
                // ako je predana knjizica              
                ra.setContext("Vehi_txtVehVehLicence", "fld_plain");
                ra.setContext("Vehi_txtVehFPlate", "fld_plain");
                ra.setContext("Vehi_txtVehFPlateDate", "fld_plain");
                ra.setContext("Coll_txtDateRecDoc", "fld_plain");
                ra.setRequired("Vehi_txtVehVehLicence", true);  
                ra.setRequired("Vehi_txtVehFPlate", true);  
                ra.setRequired("Vehi_txtVehFPlateDate", true);  

                ra.setContext("Coll_txtDateToDoc", "fld_protected");                    
                ra.setRequired("Coll_txtDateToDoc", false); 

                // ostali podaci o vozilu                   

                ra.setContext("Vehi_txtVehChassis", "fld_plain");
                ra.setContext("Vehi_txtVehBasePurp", "fld_plain");
                ra.setContext("Vehi_txtVehProducer", "fld_plain");
                ra.setContext("Vehi_txtVehCouNumCode", "fld_plain");
                ra.setContext("Vehi_txtVehCouCharCode", "fld_plain");
                ra.setContext("Vehi_txtVehSitNo", "fld_plain");
                ra.setContext("Vehi_txtVehStaNo", "fld_plain");
                ra.setContext("Vehi_txtVehLyiNo", "fld_plain");
                ra.setContext("Vehi_txtVehAllowLoad", "fld_plain");
                ra.setContext("Vehi_txtVehEmptyMass", "fld_plain");
                ra.setContext("Vehi_txtVehAlloMass", "fld_plain");
                ra.setContext("Vehi_txtVehMaxVelocity", "fld_plain");
                ra.setContext("Vehi_txtVehAxisNo", "fld_plain");
                ra.setContext("Vehi_txtVehEngineType", "fld_plain");
                ra.setContext("Vehi_txtVehPowerKw", "fld_plain");
                ra.setContext("Vehi_txtVehCcm", "fld_plain");
                ra.setContext("Vehi_txtVehNoWheel", "fld_plain");
                ra.setContext("Vehi_txtVehTracks", "fld_plain");
                ra.setContext("Vehi_txtVehBrake", "fld_plain");
                ra.setContext("Vehi_txtVehKmTravel", "fld_plain");
                ra.setContext("Vehi_txtVehEquipment", "fld_plain");

                ra.setRequired("Vehi_txtVehEngineType", true);
                ra.setRequired("Vehi_txtVehPowerKw", true);
                ra.setRequired("Vehi_txtVehCcm", true);

            } else if (doc.equals("N")) {
                // ako nije predana knjizica                        
                ra.setContext("Coll_txtDateToDoc", "fld_plain");            // do kada treba dostaviti knjizicu     
                ra.setRequired("Coll_txtDateToDoc", false); 
                // ra.setRequired("Coll_txtDateToDoc", true);   

                ra.setContext("Vehi_txtVehVehLicence", "fld_plain");
                ra.setContext("Vehi_txtVehFPlate", "fld_plain");
                ra.setContext("Vehi_txtVehFPlateDate", "fld_plain");
                ra.setContext("Coll_txtDateRecDoc", "fld_plain");
                ra.setRequired("Vehi_txtVehVehLicence", false); 
                ra.setRequired("Vehi_txtVehFPlate", false); 
                ra.setRequired("Vehi_txtVehFPlateDate", false);                     

                // ostali podaci o vozilu                   
                ra.setContext("Vehi_txtVehChassis", "fld_plain");
                ra.setContext("Vehi_txtVehBasePurp", "fld_plain");
                ra.setContext("Vehi_txtVehProducer", "fld_plain");
                ra.setContext("Vehi_txtVehCouNumCode", "fld_plain");
                ra.setContext("Vehi_txtVehCouCharCode", "fld_plain");
                ra.setContext("Vehi_txtVehSitNo", "fld_plain");
                ra.setContext("Vehi_txtVehStaNo", "fld_plain");
                ra.setContext("Vehi_txtVehLyiNo", "fld_plain");
                ra.setContext("Vehi_txtVehAllowLoad", "fld_plain");
                ra.setContext("Vehi_txtVehEmptyMass", "fld_plain");
                ra.setContext("Vehi_txtVehAlloMass", "fld_plain");
                ra.setContext("Vehi_txtVehMaxVelocity", "fld_plain");
                ra.setContext("Vehi_txtVehAxisNo", "fld_plain");
                ra.setContext("Vehi_txtVehEngineType", "fld_plain");
                ra.setContext("Vehi_txtVehPowerKw", "fld_plain");
                ra.setContext("Vehi_txtVehCcm", "fld_plain");
                ra.setContext("Vehi_txtVehNoWheel", "fld_plain");
                ra.setContext("Vehi_txtVehTracks", "fld_plain");
                ra.setContext("Vehi_txtVehBrake", "fld_plain");
                ra.setContext("Vehi_txtVehKmTravel", "fld_plain");
                ra.setContext("Vehi_txtVehEquipment", "fld_plain");

                ra.setRequired("Vehi_txtVehEngineType", true);
                ra.setRequired("Vehi_txtVehPowerKw", true);
                ra.setRequired("Vehi_txtVehCcm", true);
            }
        }       
        return;
    }


    public void setVehLicenceReversCtx (ResourceAccessor ra) {
        Date rev_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehDateLic"); 
        String rev = (String) ra.getAttribute("CollSecPaperDialogLDB","Vehi_txtVehLicReturn");

        if (rev_date != null) {
            // ako je knjizica izdana na revers i ako nije vracena              
            if (rev != null) {
                if (rev.equalsIgnoreCase("D")) {
                    ra.setContext("Vehi_txtVehDateLicTo", "fld_plain");
                    ra.setRequired("Vehi_txtVehDateLicTo", false);                          
                } else {
                    ra.setContext("Vehi_txtVehDateLicTo", "fld_plain");
                    ra.setRequired("Vehi_txtVehDateLicTo", true);                       
                }
            } else { 
                ra.setContext("Vehi_txtVehDateLicTo", "fld_plain");
                ra.setRequired("Vehi_txtVehDateLicTo", true);
            }
        } else {
            // ako nije izdana  knjizica na revers                      
            ra.setContext("Vehi_txtVehDateLicTo", "fld_change_protected");
            ra.setRequired("Vehi_txtVehDateLicTo", false);  
        }       
        return;
    } 

    public void setVehLicenceReturnCtx (ResourceAccessor ra) {
        String rev = (String) ra.getAttribute("CollSecPaperDialogLDB","Vehi_txtVehLicRetOwn");

        //ako je knjizica vozila vracena vlasniku nakon sto je kredit zatvoren          
        if (rev != null) {
            if (rev.equalsIgnoreCase("D")) {
                ra.setContext("Vehi_txtVehLicRetDat", "fld_plain");
                ra.setRequired("Vehi_txtVehLicRetDat", true);       
                ra.setContext("Vehi_txtVehLicRetWho", "fld_plain");
                ra.setRequired("Vehi_txtVehLicRetWho", true);       
            } else {
                ra.setContext("Vehi_txtVehLicRetDat", "fld_change_protected");
                ra.setRequired("Vehi_txtVehLicRetDat", false);      
                ra.setContext("Vehi_txtVehLicRetWho", "fld_protected");
                ra.setRequired("Vehi_txtVehLicRetWho", false);                      
            }
        } else { 
            ra.setContext("Vehi_txtVehLicRetDat", "fld_change_protected");
            ra.setRequired("Vehi_txtVehLicRetDat", false);      
            ra.setContext("Vehi_txtVehLicRetWho", "fld_protected");
            ra.setRequired("Vehi_txtVehLicRetWho", false);          
        }

        return;
    }   


    public void setVehInsuranceCtx (ResourceAccessor ra) {
        String ins_flag = (String) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehInsurance"); 

        if ((ins_flag != null) && (!(ins_flag.equals("")))) {
            if (ins_flag.equals("D")) {  // 
                // ako je predana polica obaveznog osiguranja           
                // Milka, 12.12.2006 - promjena na zahtjev korisnika    
                this.enableField("Vehi_txtInsId", 0);
                this.enableField("Vehi_txtVehInsDate", 2);
                ra.setRequired("Vehi_txtInsId", false); 
                ra.setRequired("Vehi_txtVehInsDate", false);                    
            } else if (ins_flag.equals("N")) {
                // ako nije predana polica obaveznog osiguranja                     
                // Milka, 12.12.2006 - promjena na zahtjev korisnika
                this.enableField("Vehi_txtInsId", 2);
                this.enableField("Vehi_txtVehInsDate", 0);
                ra.setRequired("Vehi_txtInsId", false); 
                ra.setRequired("Vehi_txtVehInsDate", false);                    
            }
        }       
        return;
    }        


    public void setGuarValKlaCtx (ResourceAccessor ra) {
        // ako je valuta garancije kuna, obavezno je polje "valutna klauzula"
        BigDecimal valuta = ((BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "guar_cur_id"));             

        if (valuta == null)
            return;  // nije upisana valuta

        if (valuta.equals(new BigDecimal("63999"))) {
            ra.setContext("Kol_txtCurInd", "fld_plain");        
            ra.setRequired("Kol_txtCurInd", true);      

        } else {
            ra.setContext("Kol_txtCurInd", "fld_protected");
            ra.setRequired("Kol_txtCurInd", false);                             
        }
        return;
    }        

    public void setGuarRespiroCtx (ResourceAccessor ra) {
        String respiro = (String) ra.getAttribute("CollSecPaperDialogLDB","Kol_txtRespiro");

        if ((respiro != null) && (!(respiro.equals("")))) {
            if (respiro.equals("D")) {  
                // ako je respiro D - obavezan upis respiro datuma      
                ra.setContext("Kol_txtRespiroDate", "fld_plain");
                ra.setRequired("Kol_txtRespiroDate", true); 

            } else if (respiro.equals("N")) {
                // ako je respiro = N       
                // obrisati respiro datum                   
                ra.setAttribute("CollSecPaperDialogLDB","Kol_txtRespiroDate", "");
                ra.setContext("Kol_txtRespiroDate", "fld_protected");
                ra.setRequired("Kol_txtRespiroDate", false);    
            }
        }       
        return;
    }


    public void setGuarNumCtx (ResourceAccessor ra) {
        String vrsta = (String) ra.getAttribute("CollHeadLDB","Coll_txtCollTypeCode");

        if ((vrsta != null) && (!(vrsta.equals("")))) {
            
            if (vrsta.equals("4SLETOCO") || vrsta.equals("4BLETOCO")) {
                ra.setRequired("Kol_txtAmortInd", false); 
                ra.setRequired("Kol_txtRespiro", false); 
                ra.setRequired("Kol_txtRespiroDate", false); 
                ra.setRequired("Kol_txtFirstCall", false); 

                ra.setContext("Kol_txtAmortInd", "fld_hidden");
                ra.setContext("Kol_txtRespiro", "fld_hidden");
                ra.setContext("Kol_txtRespiroDate", "fld_hidden");
                ra.setContext("Kol_txtFirstCall", "fld_hidden");

                ra.setContext("Kol_lblAmortInd", "fld_hidden");
                ra.setContext("Kol_lblRespiro", "fld_hidden");
                ra.setContext("Kol_lblRespiroDate", "fld_hidden");
                ra.setContext("Kol_lblFirstCall", "fld_hidden");
            } else {
                ra.setRequired("Kol_txtAmortInd", true); 
                ra.setRequired("Kol_txtRespiro", true); 
                ra.setRequired("Kol_txtRespiroDate", false); 
                ra.setRequired("Kol_txtFirstCall", true); 

                ra.setContext("Kol_txtAmortInd", "fld_plain");
                ra.setContext("Kol_txtRespiro", "fld_plain");
                ra.setContext("Kol_txtRespiroDate", "fld_plain");
                ra.setContext("Kol_txtFirstCall", "fld_plain");                    

                ra.setContext("Kol_lblAmortInd", "fld_plain");
                ra.setContext("Kol_lblRespiro", "fld_plain");
                ra.setContext("Kol_lblRespiroDate", "fld_plain");
                ra.setContext("Kol_lblFirstCall", "fld_plain");
            }

            if (vrsta.equals("4AGARDRZ")) {   
                // ako je garancija drzave - obavezan upis broja garancije       
                ra.setRequired("Kol_txtGuarNo", true);  
            } else  {
                ra.setRequired("Kol_txtGuarNo", false); 
            }                
        }       

        return;
    }        

    public String countGuarPeriod (Date issue_date, Date maturity_date) {

        String period = "";

        if ((maturity_date).before(issue_date)) {
            return period;
        }

        GregorianCalendar calendar1 = new GregorianCalendar();
        GregorianCalendar calendar2 = new GregorianCalendar();       

        Date Date1 = issue_date;
        Date Date2 = maturity_date;

        calendar1.setTime(Date1);
        calendar2.setTime(Date2);

        long broj1 =  calendar1.getTime().getTime();
        long broj2 =  calendar2.getTime().getTime();

        long broj = broj2 - broj1;

        java.sql.Date Datenew = new java.sql.Date(broj);

        String datum_string = Datenew.toString();
        String godina = datum_string.substring(0,4);
        String mjesec = datum_string.substring(5,7);
        String dan = datum_string.substring(8,10);

        int godina_i = Integer.parseInt(godina);
        int mjesec_i = Integer.parseInt(mjesec);
        int dan_i = Integer.parseInt(dan);

        int one_amount = 1;
        int year_1970 = 1970;

        godina_i = godina_i - year_1970;
        mjesec_i = mjesec_i - one_amount;
        dan_i    = dan_i - one_amount;

        String godina_s = godina_i+"";
        String mjesec_s = mjesec_i + "";
        String dan_s = dan_i + "";

        if (godina_s.length() == 1) {
            godina_s="0"+godina_s;
        }

        if (mjesec_s.length() == 1) {
            mjesec_s = "0" + mjesec_s;
        }  

        if (dan_s.length() == 1) {
            dan_s = "0" + dan_s;
        }

        period = godina_s + mjesec_s + dan_s;

        return period;
    }

    public boolean ctrlCusaccExposureAccount (String account_number) {

        account_number = account_number.trim();

        if (account_number == null || account_number.equalsIgnoreCase(""))
            return true;

        if (account_number.length() < 8)
            return false;

        String prvi_dio = "";
        String prva_crta = "";
        String drugi_dio = "";
        String druga_crta = "";
        String treci_dio = "";

        prvi_dio = account_number.substring(0,3);
        prva_crta = account_number.substring(3,4);
        drugi_dio = account_number.substring(4,6);
        druga_crta = account_number.substring(6,7);
        treci_dio = account_number.substring(7);

        String accno_cn = "";
        String accno = ""; 
        String cd = "";

        if (prva_crta.equals("-") && druga_crta.equals("-")) {
            if ( (prvi_dio.length() == 3) && prvi_dio.equals("019")) {
                //               partija je oblika 019-63-25XXXXXXX
                if ((drugi_dio.length() == 2) && drugi_dio.equals("63")) {
                    if (treci_dio.length() == 9) {
                        return true;
                    }
                }
            } else if ( (prvi_dio.length() == 3) && (!(prvi_dio.equals("019")))){
                //  partija je oblika XXX-XX-XXXXXX
                //  XX in (40,41,42,55,56,96)                   
                if ((drugi_dio.length() == 2) && (drugi_dio.equals("40") || drugi_dio.equals("41") || drugi_dio.equals("42")
                        || drugi_dio.equals("55") || drugi_dio.equals("56") || drugi_dio.equals("96") || drugi_dio.equals("50"))) {
                    if (treci_dio.length() >= 6) {
                        // kontrola na kontrolni broj                           
                        accno_cn = treci_dio.substring(treci_dio.length()-1);
                        accno = account_number.substring(0,account_number.length()-1);

                        cd = algoritam11_CD(accno);

                        if (!accno_cn.equalsIgnoreCase(cd)) {
                            // ra.showMessage("wrn330");
                            return false;
                        }                           
                        return true;
                    }   
                }
            }
        } else {    
            if (account_number.length() == 10 ) {

                accno_cn = account_number.substring(9);
                accno = account_number.substring(0, 9); 
                cd = FPaymUtil.generateCheckDigit(accno, "11,10");
                if (!accno_cn.equalsIgnoreCase(cd)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }


    public boolean ctrlCashDepozitAccount (String account_number) {

        account_number = account_number.trim();

        if (account_number == null || account_number.equalsIgnoreCase(""))
            return true;

        if (account_number.length() < 8)
            return false;

        String prvi_dio = "";
        String prva_crta = "";
        String drugi_dio = "";
        String druga_crta = "";
        String treci_dio = "";

        prvi_dio = account_number.substring(0,3);
        prva_crta = account_number.substring(3,4);
        drugi_dio = account_number.substring(4,6);
        druga_crta = account_number.substring(6,7);
        treci_dio = account_number.substring(7);

        String accno_cn = "";
        String accno = ""; 
        String cd = "";
        //System.out.println(account_number+"+");
        if (account_number.length() == 10){
            accno_cn = account_number.substring(9);
            accno = account_number.substring(0, 9); 
            cd = FPaymUtil.generateCheckDigit(accno, "11,10");
            if (!accno_cn.equalsIgnoreCase(cd)) { 
                //System.out.println("kontrola nije prosla");
                return false;
            }
        }
        else if(!account_number.matches("\\d{3}-\\d{2}-\\d{6,}") ){
            //Change Request 10370
            if(!account_number.matches("[0-9]{13}")){
                return false; 
            }
        } 
        return true;

//        if (prva_crta.equals("-") &&  druga_crta.equals("-")) {
//            if ( prvi_dio.length() == 3){
//                //partija je oblika XXX-XX-XXXXXX
//                //XX in (40,41,42,55,56,96)                   
//                if (drugi_dio.length() == 2) {
//                    if (treci_dio.length() >= 6) {
//                        // kontrola na kontrolni broj-izbaceno 16.06.2008 jer se ne racuna u modulu depozita                                                                      
//                        return true;
//                    } 
//                }  
//            }
//        } else if (prva_crta.equals("-") && (!druga_crta.equals("-"))){
//            return false;
//        } else {    
//            if (account_number.length() == 10 ) {
//                // partija ima 10 znamenki
//                accno_cn = account_number.substring(9);
//                accno = account_number.substring(0, 9); 
//                cd = FPaymUtil.generateCheckDigit(accno, "11,10");
//                if (!accno_cn.equalsIgnoreCase(cd)) {
//                    return false;
//                }
//                return true;
//            }
//        }
//        return false;
    }        

    public void setSupplyMinValueCtx (ResourceAccessor ra) {
        String min_value = (String) ra.getAttribute("CollSecPaperDialogLDB", "Supp_txtMinValue"); 
        if ((min_value != null) && (!(min_value.equals("")))) {
            if (min_value.equals("D")) {  
                // ako je min_value D - obavezan upis minimalnog iznosa     
                ra.setContext("Supp_txtMinAmount", "fld_plain");
                ra.setRequired("Supp_txtMinAmount", true);  

            } else if (min_value.equals("N")) {
                // ako je min_value = N     
                // nije obavezan minimalni iznos, obrisati ga                   
                ra.setAttribute("CollSecPaperDialogLDB","Supp_txtMinAmount", "");
                ra.setContext("Supp_txtMinAmount", "fld_protected");
                ra.setRequired("Supp_txtMinAmount", false); 
            }
        }       
        return;
    }        

    public void setMovTypCtx (ResourceAccessor ra) {
        String vrsta = (String) ra.getAttribute("CollHeadLDB","Coll_txtCollTypeCode");

        if ((vrsta != null) && (!(vrsta.equals("")))) {
            if (vrsta.equals("2AVI")) {   
                // zrakoplov    
                ra.setContext("Coll_lblSecType","fld_plain");
                ra.setContext("Coll_txtSecTypeCode","fld_plain");
                ra.setContext("Coll_txtSecTypeName","fld_protected");
                ra.setContext("Mov_lblMobel","fld_plain");
                ra.setContext("Vehi_txtVehModel","fld_plain");
                ra.setContext("Mov_lblSerNum","fld_plain");
                ra.setContext("Mov_txtSerNum","fld_plain");
                ra.setContext("Vehi_lblVehMadeYear","fld_plain");
                ra.setContext("Vehi_txtVehMadeYear","fld_plain");
                ra.setContext("Mov_lblRegSign","fld_plain");
                ra.setContext("Mov_txtRegSign","fld_plain");
                ra.setContext("Mov_lblHrReg","fld_plain");
                ra.setContext("Mov_txtHrReg","fld_plain");
                ra.setContext("Coll_lblMovCondition","fld_plain");
                ra.setContext("Ves_txtDsc","fld_plain");
                ra.setContext("Supp_lblAdresa","fld_plain");
                ra.setContext("Supp_txtAdresa","fld_plain");
                ra.setContext("Supp_lblPlace","fld_plain");
                ra.setContext("Supp_txtPlace","fld_plain");

                ra.setRequired("Coll_txtSecTypeCode", true);    
                ra.setRequired("Coll_txtSecTypeName", true);
                ra.setRequired("Vehi_txtVehModel", true);   
                ra.setRequired("Mov_txtSerNum", true);  
                ra.setRequired("Vehi_txtVehMadeYear", true);    
                ra.setRequired("Mov_txtRegSign", true); 
                ra.setRequired("Mov_txtHrReg", true);   
                ra.setRequired("Ves_txtDsc", false);    

                ra.setRequired("Supp_txtAdresa", false);    
                ra.setRequired("Supp_txtPlace", false); 

                ra.setCursorPosition("Coll_txtSecTypeCode");

            } else if (vrsta.equalsIgnoreCase("2STR")) {

                //  strojevi        
                ra.setContext("Coll_lblSecType","fld_plain");
                ra.setContext("Coll_txtSecTypeCode","fld_plain");
                ra.setContext("Coll_txtSecTypeName","fld_protected");
                ra.setContext("Mov_lblMobel","fld_plain");
                ra.setContext("Vehi_txtVehModel","fld_plain");
                ra.setContext("Mov_lblSerNum","fld_plain");
                ra.setContext("Mov_txtSerNum","fld_plain");
                ra.setContext("Vehi_lblVehMadeYear","fld_plain");
                ra.setContext("Vehi_txtVehMadeYear","fld_plain");
                ra.setContext("Mov_lblRegSign","fld_hidden");                   
                ra.setContext("Mov_txtRegSign","fld_hidden");
                ra.setContext("Mov_lblHrReg","fld_hidden");
                ra.setContext("Mov_txtHrReg","fld_hidden");
                ra.setContext("Coll_lblMovCondition","fld_plain");
                ra.setContext("Ves_txtDsc","fld_plain");
                ra.setContext("Supp_lblAdresa","fld_plain");
                ra.setContext("Supp_txtAdresa","fld_plain");
                ra.setContext("Supp_lblPlace","fld_plain");
                ra.setContext("Supp_txtPlace","fld_plain");

                ra.setRequired("Coll_txtSecTypeCode", true);    
                ra.setRequired("Coll_txtSecTypeName", true);
                ra.setRequired("Vehi_txtVehModel", true);   
                ra.setRequired("Mov_txtSerNum", true);  
                ra.setRequired("Vehi_txtVehMadeYear", true);    
                ra.setRequired("Mov_txtRegSign", false);    
                ra.setRequired("Mov_txtHrReg", false);  
                ra.setRequired("Ves_txtDsc", false);    

                ra.setRequired("Supp_txtAdresa", false);    
                ra.setRequired("Supp_txtPlace", false);                     

                ra.setCursorPosition("Coll_txtSecTypeCode");                    

            } else if ( vrsta.equalsIgnoreCase("2RAC")) {
                // racunala             
                ra.setContext("Coll_lblSecType","fld_hidden");
                ra.setContext("Coll_txtSecTypeCode","fld_hidden");
                ra.setContext("Coll_txtSecTypeName","fld_hidden");
                ra.setContext("Mov_lblMobel","fld_plain");
                ra.setContext("Vehi_txtVehModel","fld_plain");
                ra.setContext("Mov_lblSerNum","fld_plain");  
                ra.setContext("Mov_txtSerNum","fld_plain");
                ra.setContext("Vehi_lblVehMadeYear","fld_plain");
                ra.setContext("Vehi_txtVehMadeYear","fld_plain");
                ra.setContext("Mov_lblRegSign","fld_hidden");                           
                ra.setContext("Mov_txtRegSign","fld_hidden");
                ra.setContext("Mov_lblHrReg","fld_hidden");
                ra.setContext("Mov_txtHrReg","fld_hidden");
                ra.setContext("Coll_lblMovCondition","fld_plain");
                ra.setContext("Ves_txtDsc","fld_plain");
                ra.setContext("Supp_lblAdresa","fld_plain");
                ra.setContext("Supp_txtAdresa","fld_plain");
                ra.setContext("Supp_lblPlace","fld_plain");
                ra.setContext("Supp_txtPlace","fld_plain");

                ra.setRequired("Coll_txtSecTypeCode", false);   
                ra.setRequired("Coll_txtSecTypeName", false);
                ra.setRequired("Vehi_txtVehModel", true);   
                ra.setRequired("Mov_txtSerNum", true);  
                ra.setRequired("Vehi_txtVehMadeYear", true);    
                ra.setRequired("Mov_txtRegSign", false);    
                ra.setRequired("Mov_txtHrReg", false);  
                ra.setRequired("Ves_txtDsc", false);    

                ra.setRequired("Supp_txtAdresa", false);    
                ra.setRequired("Supp_txtPlace", false);     

                ra.setCursorPosition("Vehi_txtVehModel");                   

            } else if ( vrsta.equalsIgnoreCase("2OPP") || vrsta.equalsIgnoreCase("2BRD") ) {
                // ostalo           
                ra.setContext("Coll_lblSecType","fld_hidden");
                ra.setContext("Coll_txtSecTypeCode","fld_hidden");
                ra.setContext("Coll_txtSecTypeName","fld_hidden");
                ra.setContext("Mov_lblMobel","fld_hidden");
                ra.setContext("Vehi_txtVehModel","fld_hidden");
                ra.setContext("Mov_lblSerNum","fld_hidden");
                ra.setContext("Mov_txtSerNum","fld_hidden");
                ra.setContext("Vehi_lblVehMadeYear","fld_hidden");
                ra.setContext("Vehi_txtVehMadeYear","fld_hidden");
                ra.setContext("Mov_lblRegSign","fld_hidden");                       
                ra.setContext("Mov_txtRegSign","fld_hidden");
                ra.setContext("Mov_lblHrReg","fld_hidden");
                ra.setContext("Mov_txtHrReg","fld_hidden");
                ra.setContext("Coll_lblMovCondition","fld_plain");
                ra.setContext("Ves_txtDsc","fld_plain");
                ra.setContext("Supp_lblAdresa","fld_plain");
                ra.setContext("Supp_txtAdresa","fld_plain");
                ra.setContext("Supp_lblPlace","fld_plain");
                ra.setContext("Supp_txtPlace","fld_plain");

                ra.setRequired("Coll_txtSecTypeCode", false);   
                ra.setRequired("Coll_txtSecTypeName", false);
                ra.setRequired("Vehi_txtVehModel", false);  
                ra.setRequired("Mov_txtSerNum", false); 
                ra.setRequired("Vehi_txtVehMadeYear", false);   
                ra.setRequired("Mov_txtRegSign", false);    
                ra.setRequired("Mov_txtHrReg", false);  
                ra.setRequired("Ves_txtDsc", true); 

                ra.setRequired("Supp_txtAdresa", false);    
                ra.setRequired("Supp_txtPlace", false); 

                ra.setCursorPosition("Ves_txtDsc");                         
            } 
        } else {
            ra.setContext("Coll_txtSecTypeCode","fld_protected");
            ra.setContext("Coll_txtSecTypeName","fld_protected");
            ra.setContext("Vehi_txtVehModel","fld_protected");
            ra.setContext("Mov_txtSerNum","fld_protected");
            ra.setContext("Vehi_txtVehMadeYear","fld_protected");

            ra.setContext("Mov_txtRegSign","fld_protected");
            ra.setContext("Mov_txtHrReg","fld_protected");
            ra.setContext("Ves_txtDsc","fld_protected");

            ra.setContext("Supp_txtAdresa","fld_protected");
            ra.setContext("Supp_txtPlace","fld_protected");

            ra.setContext("Coll_lblSecType","fld_plain");
            ra.setContext("Mov_lblMobel","fld_plain");
            ra.setContext("Mov_lblSerNum","fld_plain");
            ra.setContext("Vehi_lblVehMadeYear","fld_plain");
            ra.setContext("Mov_lblRegSign","fld_plain");                        
            ra.setContext("Mov_lblHrReg","fld_plain");
            ra.setContext("Coll_lblMovCondition","fld_plain");
            ra.setContext("Supp_lblAdresa","fld_plain");
            ra.setContext("Supp_lblPlace","fld_plain");
        }
        return;  
    }       

    public void setSupplyTypCtx (ResourceAccessor ra) {
        String vrsta = (String) ra.getAttribute("CollHeadLDB","Coll_txtCollTypeCode");

        if ((vrsta != null) && (!(vrsta.equals("")))) {
            if (vrsta.equals("2ZAL")) {  // 
                // zalihe
                ra.setRequired("Supp_txtLocation", true);   
                ra.setRequired("Supp_txtAdresa", true);
                ra.setRequired("Supp_txtPlace", true);  

            } else if (vrsta.equalsIgnoreCase("2ZLT")) {
                //  zalihe u transportu     
                ra.setRequired("Supp_txtLocation", false);  
                ra.setRequired("Supp_txtAdresa", false);
                ra.setRequired("Supp_txtPlace", false); 
            }           
        } else {
            ra.setRequired("Supp_txtLocation", false);  
            ra.setRequired("Supp_txtAdresa", false);
            ra.setRequired("Supp_txtPlace", false); 
        }

        return;
    }                        

    private String algoritam11_CD(String referenceNumberWithoutControlDigit){

        String result = null;
        int sum = 0;
        int start = 2;
        int last  = 0;
        int controlDigit = 0;
        StringBuffer tmpSBReferenceNumber = new StringBuffer();
        String tmpReferenceNumber= null;
        boolean isNoDigit = false;

        for(int i=0;i<referenceNumberWithoutControlDigit.length();i++){
            if(referenceNumberWithoutControlDigit.charAt(i) == '-'){
                isNoDigit = true;
            }else{
                if(Character.isDigit(referenceNumberWithoutControlDigit.charAt(i))){
                    tmpSBReferenceNumber.append(referenceNumberWithoutControlDigit.charAt(i));
                }else{
                    isNoDigit = true;
                }
            }
        }

        tmpReferenceNumber = tmpSBReferenceNumber.toString();

        last = tmpReferenceNumber.length();
        for(int i=last;i> 0;i--){
            int digit = Integer.parseInt(tmpReferenceNumber.substring(i-1,i));
            sum += digit * start;
            start++;

        }
        int remainder = sum % 11;

        if ((remainder == 0) || (remainder == 1)) {
            controlDigit =  0;
        } else {
            controlDigit = 11 - remainder;
        }
        result = controlDigit+"";
        return result;
    }        

    
    public void setVrpFirstCtx (BigDecimal col_cat_id) {

        if (col_cat_id != null) {
            // DIONICE - 613223
            // OBVEZNICE - 619223  
            // ZAPISI - 627223
            // UDJELI U FONDU - 622223  
            // UDJELI U PODUZECU - 629223           
            if (col_cat_id.compareTo(new BigDecimal("613223")) == 0) {
                // dionice

                ra.setRequired("Coll_txtRefMarketCode", true);  
                ra.setRequired("Vrp_txtRefMarketInx", true);    
                ra.setRequired("Vrp_txtStopSell", true);        

                // zaprotektirati izdavatelja, referentno trziste, glavni index burze
                ra.setContext("Coll_txtIssuerCode","fld_protected");
                ra.setContext("Coll_txtRefMarketCode","fld_protected");
                ra.setContext("Vrp_txtRefMarketInx","fld_protected");

            }else if (col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||col_cat_id.compareTo(new BigDecimal("627223")) == 0) {

                ra.setRequired("Coll_txtRefMarketCode", true);  
                ra.setRequired("Vrp_txtRefMarketInx", true);    
                ra.setRequired("Vrp_txtStopSell", false);   
                ra.setContext("Vrp_txtStopSell","fld_protected");
                ra.setRequired("Vrp_txtPeriodStopSell", false); 
                ra.setContext("Vrp_txtPeriodStopSell","fld_change_protected");
                // obveznice,zapisi              

                // zaprotektirati izdavatelja, referentno trziste, glavni index burze
                ra.setContext("Coll_txtIssuerCode","fld_change_protected");
                ra.setContext("Coll_txtRefMarketCode","fld_change_protected");
                ra.setContext("Vrp_txtRefMarketInx","fld_change_protected");
            }else if (col_cat_id.compareTo(new BigDecimal("622223")) == 0) {

                // udjeli u fondu    
                ra.setContext("Vrp_txtRatingLong","fld_protected");
                ra.setContext("Vrp_txtRatingShort","fld_protected");                
                ra.setContext("Vrp_txtRatingIssuer","fld_protected");                
                ra.setContext("Coll_txtRefMarketCode","fld_protected");   
                ra.setContext("Vrp_txtRefMarketInx","fld_protected");

                ra.setRequired("Vrp_txtStopSell", false);   
                ra.setContext("Vrp_txtStopSell","fld_protected");
                ra.setRequired("Vrp_txtPeriodStopSell", false); 
                ra.setContext("Vrp_txtPeriodStopSell","fld_change_protected");              

                ra.setRequired("Vrp_txtRefMarketInx", false);       

                ra.setContext("Coll_txtIssuerCode","fld_change_protected");
            }else if (col_cat_id.compareTo(new BigDecimal("629223")) == 0) {

                // udjeli u poduzecu
                ra.setContext("Coll_txtISIN","fld_protected");
                // protektirati polje za valutnu klauzulu
                ra.setContext("Vrp_txtValutnaKlauzula","fld_protected");                
                // otprotektirati naziv izdavatelja, osum za detalje
                ra.setContext("Coll_txtIssuerName","fld_plain");

                ra.setContext("Vrp_txtRatingLong","fld_protected");
                ra.setContext("Vrp_txtRatingShort","fld_protected");                
                ra.setContext("Vrp_txtRatingIssuer","fld_protected");                
                ra.setContext("Coll_txtRefMarketCode","fld_protected"); 
                ra.setContext("Vrp_txtRefMarketInx","fld_protected");

                ra.setRequired("Vrp_txtStopSell", false);   
                ra.setContext("Vrp_txtStopSell","fld_protected");
                ra.setRequired("Vrp_txtPeriodStopSell", false); 
                ra.setContext("Vrp_txtPeriodStopSell","fld_change_protected");

                ra.setRequired("Coll_txtISIN", false);
                ra.setRequired("Vrp_txtRefMarketInx", false);   
            }
        }  
        return;
    }   
    
    public void setVrpSecondCtxUdjeliUPod (BigDecimal col_cat_id) {
        if (col_cat_id.compareTo(new BigDecimal("629223")) == 0) {
            // udjeli u poduzecu            
            ra.setContext("Coll_lblTotalMarketValue","fld_hidden");
            ra.setContext("Coll_txtTotalMarketValue","fld_hidden");

            ra.setContext("Vrp_lblUdjeliUPod","fld_protected");
            ra.setContext("Vrp_txtUdjeliUPod","fld_protected");

            ra.setContext("Coll_lblNominalAmountTot","fld_hidden");
            ra.setContext("Vrp_lblUdjeliUPodUkupno","fld_protected");
        } else if (col_cat_id.compareTo(new BigDecimal("619223")) == 0) {
            // obveznice  
            ra.setContext("Coll_lblNominalAmount","fld_hidden");
            ra.setContext("Coll_lblNumOfSec","fld_hidden");

            ra.setContext("Coll_lblOsnovaGlavnice","fld_plain");
            ra.setContext("Coll_lblUnosUkupneNominale","fld_plain");            
        }
    } 


    public void setVrpSecondCtx(BigDecimal col_cat_id)
    {
        if (col_cat_id == null) return;
        
        ra.setContext("Coll_txtB1Eligibility", "fld_plain");

        if (col_cat_id.compareTo(new BigDecimal("613223")) == 0)  // DIONICE - 613223
        {
            ra.setRequired("Coll_txtNominalAmount", true);  
            ra.setRequired("Coll_txtAccruedInterest", false);   
            ra.setContext("Coll_txtAccruedInterest","fld_protected");
            ra.setRequired("Coll_txtMaturityDate", false);  
            ra.setContext("Coll_txtMaturityDate","fld_protected");      
        }
        else if (col_cat_id.compareTo(new BigDecimal("619223")) == 0)  // OBVEZNICE - 619223
        {
            ra.setRequired("Coll_txtNominalAmount", true);  
            ra.setContext("Coll_txtAccruedInterest","fld_protected");   
            ra.setContext("Coll_txtMaturityDate","fld_protected");      

            ra.setContext("Coll_lblNominalAmount","fld_hidden");
            ra.setContext("Coll_lblNumOfSec","fld_hidden");

            ra.setContext("Coll_lblOsnovaGlavnice","fld_plain");
            ra.setContext("Coll_lblUnosUkupneNominale","fld_plain");
        }
        else if (col_cat_id.compareTo(new BigDecimal("627223")) == 0)  // ZAPISI - 627223
        {
            ra.setRequired("Coll_txtNominalAmount", true);          
            ra.setContext("Coll_txtAccruedInterest","fld_protected");   
            ra.setContext("Coll_txtMaturityDate","fld_protected");  
        }
        else if (col_cat_id.compareTo(new BigDecimal("622223")) == 0)  // UDJELI U FONDU - 622223 
        {
            ra.setRequired("Coll_txtNominalAmount", false); 
            ra.setContext("Coll_txtNominalAmount","fld_protected");
            ra.setRequired("Coll_txtAccruedInterest", false);   
            ra.setContext("Coll_txtAccruedInterest","fld_protected");
            ra.setRequired("Coll_txtMaturityDate", false);  
            ra.setContext("Coll_txtMaturityDate","fld_protected");              
        } 
        else if (col_cat_id.compareTo(new BigDecimal("629223")) == 0)  // UDJELI U PODUZECU - 629223
        {
            ra.setContext("Coll_txtNominalAmountCur","fld_protected");              
            ra.setRequired("Coll_txtNominalAmount", false); 
            ra.setContext("Coll_txtNominalAmount","fld_protected");
            ra.setRequired("Coll_txtMarketPrice", false);   
            ra.setContext("Coll_txtMarketPrice","fld_protected");

            ra.setRequired("Coll_txtMarketPriceDate", false);            
            this.enableField("Coll_txtMarketPriceDate", 0);         

            ra.setRequired("Coll_txtAccruedInterest", false);   
            ra.setContext("Coll_txtAccruedInterest","fld_protected");
            ra.setRequired("Coll_txtMaturityDate", false);  
            ra.setContext("Coll_txtMaturityDate","fld_protected");  

            ra.setRequired("Coll_txtNominalAmountTot", true);   
            ra.setContext("Coll_txtNominalAmountTot","fld_plain");

            ra.setRequired("Coll_txtNominalAmountTotKn", false);
            ra.setRequired("Coll_txtTotalMarketValueKn", false);

            ra.setContext("Coll_lblTotalMarketValue","fld_hidden");
            ra.setRequired("Coll_txtTotalMarketValue", false);  
            ra.setContext("Coll_txtTotalMarketValue","fld_hidden");

            ra.setContext("Vrp_lblUdjeliUPod","fld_plain");
            ra.setRequired("Vrp_txtUdjeliUPod",true);
            ra.setContext("Vrp_txtUdjeliUPod","fld_plain");

            ra.setRequired("Coll_txtMarketPriceKn", false); 

            ra.setContext("Coll_lblNominalAmountTot","fld_hidden");
            ra.setContext("Vrp_lblUdjeliUPodUkupno","fld_plain");       
        }
    }


    public void setVrpStopSellCtx (String stop_sell_ind) {
        if ((stop_sell_ind != null) && (stop_sell_ind.equals("D"))) {
            ra.setRequired("Vrp_txtPeriodStopSell", true);  
            ra.setContext("Vrp_txtPeriodStopSell","fld_plain");
        } else if ((stop_sell_ind != null) && (stop_sell_ind.equals("N"))) {
            ra.setRequired("Vrp_txtPeriodStopSell", false); 
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtPeriodStopSell","");
            ra.setContext("Vrp_txtPeriodStopSell","fld_change_protected");
        }
    }
    
    public boolean chkValutnaKlauzula(String valutna_klauzula, BigDecimal cur_id) {
        if ((valutna_klauzula != null) && (valutna_klauzula.equals("3") || valutna_klauzula.equals("1"))) {
            //  nije dozvoljena kuna        
            if (cur_id != null && cur_id.equals(new BigDecimal("63999")))
                return true;    
        } else if ((valutna_klauzula != null) && (valutna_klauzula.equals("0"))) {            
            if (cur_id != null && !cur_id.equals(new BigDecimal("63999")))
                return true;    
        }
        return false;
    }    
    
    public void countVrpAmounts(ResourceAccessor ra) {
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");   
        BigDecimal nomAmountOne = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmount");
        BigDecimal numOfSec = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNumOfSec");
        BigDecimal nomCurId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "NOMINAL_CUR_ID");
        BigDecimal marketAmountOne = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtMarketPrice");

        BigDecimal nomAmountOneKn = null;
        BigDecimal nomAmountTot = null;
        BigDecimal nomAmountTotKn = null;
        BigDecimal exchRateRef = null; 
        BigDecimal marketAmountOnePercent = null;  

        BigDecimal ponder = (BigDecimal) ra.getAttribute("CollHeadLDB","Coll_txtCollMvpPonder");
        BigDecimal ponderAmount = null;
        BigDecimal numberOneHundred = new BigDecimal("100.00");

        BigDecimal availValue = null;
        BigDecimal thirdRightNom = (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtThirdRightInNom");
        BigDecimal hfsValue = (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtHfsValue");

        // racunam iznos jedinicne nominale u kn ako je valuta nominale razlicita od kn, inace prepisem
        Date todaySQLDate = null;
        GregorianCalendar calendar = new GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new Date(timeT);     
        // dohvat tecaja za valutu nominalne vrijednosti
        try{
            if (getCurrentExchRate(todaySQLDate,nomCurId)){
                exchRateRef = getMiddRate();
                // System.out.println(" Valuta i tecaj originalne valute   " + nomCurId + "   " + exchRateRef );
            }
        }catch(java.sql.SQLException sqle){
        }catch(hr.vestigo.framework.util.db.EmptyRowSet db_ers_e){
        }    
        //  racunam nominalni iznos jedinicnog u kn     
        if (isAmount(nomAmountOne) && isAmount(exchRateRef)) {
            nomAmountOneKn = setProductTwoBigDec(nomAmountOne, exchRateRef);
            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmountKn",nomAmountOneKn);
        }

        // racunam nominalni iznos posjedujuceg broja u valuti 
        if (isAmount(numOfSec) && isAmount(nomAmountOne)) {
            nomAmountTot = setProductTwoBigDec(numOfSec,nomAmountOne);
            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmountTot",nomAmountTot);
        }

        // racunam nominalni iznos posjedujuceg broja u kn
        if (isAmount(numOfSec) && isAmount(nomAmountOneKn)) {
            nomAmountTotKn = setProductTwoBigDec(numOfSec,nomAmountOneKn);
            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmountTotKn",nomAmountTotKn);
        }       

        // racunam iznos jedinicne trzisne vrijednosti u valuti u % - nema kod dionica i udjela u fondovima
        if (col_cat_id.compareTo(new BigDecimal("619223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0) {
            if (isAmount(nomAmountOne) && isAmountNull(marketAmountOne)) {
                marketAmountOnePercent = setDivideTwoBigDecPercent(marketAmountOne, nomAmountOne);
                ra.setAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceFo",marketAmountOnePercent);
            }
        }

        nomAmountOneKn = null;
        nomAmountTot = null;
        nomAmountTotKn = null;

        // racunam iznos jedinicne trzisne vrijednosti u kn 
        if (isAmountNull(marketAmountOne) && isAmount(exchRateRef)) {
            nomAmountOneKn = setProductTwoBigDec(marketAmountOne, exchRateRef);
            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceKn",nomAmountOneKn);
        }       

        // racunam trzisni iznos posjedujuceg broja u valuti - to je i trenutna tržišna vrijednost 
        if (isAmount(numOfSec) && isAmountNull(marketAmountOne)) {  
            //Milka, 11.01.2010 - samo za obveznice         
            if (col_cat_id.compareTo(new BigDecimal("619223")) == 0 && isAmount(nomAmountOne)) {

                nomAmountTot = setProductTwoBigDec(numOfSec,marketAmountOne);
                nomAmountTot = setProductTwoBigDec(nomAmountTot,nomAmountOne);

            } else {
                nomAmountTot = setProductTwoBigDec(numOfSec,marketAmountOne);
            }
            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtTotalMarketValue",nomAmountTot);
            // ovo se zapisuje u coll_head, to je trenutni market value kolaterala
            ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu",nomAmountTot);
            // neponderirana            
            ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue",nomAmountTot); 
            // izracunati ponderiranu
            if (isAmount(ponder) && isAmount(nomAmountTot)) {
                ponderAmount = setProductTwoBigDec(ponder,nomAmountTot);
                ponderAmount = ponderAmount.divide(numberOneHundred, 2, java.math.BigDecimal.ROUND_HALF_UP);
            }     
            // izracunati raspolozivu = ponderirana - hipoteke
            availValue = ponderAmount;          
            if (isAmountNull(availValue) && isAmountNull(thirdRightNom))
                availValue = availValue.subtract(thirdRightNom);

            if (isAmountNull(availValue) && isAmountNull(hfsValue)) 
                availValue = availValue.subtract(hfsValue);

            ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",availValue);                     
        }

        // racunam trzisni iznos posjedujuceg broja u kn
        if (isAmount(numOfSec) && isAmountNull(nomAmountOneKn)) {
            if (col_cat_id.compareTo(new BigDecimal("619223")) == 0 && isAmount(nomAmountOne)) {
                nomAmountTotKn = setProductTwoBigDec(numOfSec,nomAmountOneKn);
                nomAmountTotKn = setProductTwoBigDec(nomAmountTotKn,nomAmountOne);
            } else {
                nomAmountTotKn = setProductTwoBigDec(numOfSec,nomAmountOneKn);
            }

            ra.setAttribute("CollSecPaperDialogLDB","Coll_txtTotalMarketValueKn",nomAmountTotKn);
        }           

        return;
    }

    public boolean chek_is_there_decimal(BigDecimal decimal_number) {
        if (decimal_number != null) {
            BigDecimal num1 = decimal_number;
            BigDecimal ten = new BigDecimal ("10000.00");
            BigDecimal one = new BigDecimal ("1.00");
            BigDecimal nula = new BigDecimal("0.00");

            BigDecimal rez1 = num1.multiply(ten);
            BigDecimal rez2 = num1.divide(one,0,BigDecimal.ROUND_HALF_UP);
            BigDecimal rez3 = rez2.multiply(ten);

            BigDecimal rez4 = rez1.subtract(rez3);

            if (rez4.compareTo(nula) == 0) {
                return false;
            } else {
                return true;        
            }           
        }

        return false;
    }               

    // napravitit samo jednu metodu za RBA i B2 prihvatljivost !!!
    public String chk_RBA_eligibility_for_all(BigDecimal col_cat_id, BigDecimal col_typ_id, String low_eligibility, 
            Date maturity_date, String upisana_hipoteka, String first_call, String sva_dokumentacija, 
            String knjizica_vozila, String banka_skladisti, String cesija_naplata ){

        String rba_eligibility = "N";
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");

        if (col_cat_id == null)
            return rba_eligibility;     

        if (col_cat_id.compareTo(new BigDecimal("612223")) == 0) {
            //          depoziti    

            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) && 
                    (maturity_date != null && maturity_date.after(current_date)) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }

        } else if (col_cat_id.compareTo(new BigDecimal("615223")) == 0) {
            // Strong Leter of Comfort
            if (col_typ_id != null && (col_typ_id.compareTo(new BigDecimal("73777")) == 0 || col_typ_id.compareTo(new BigDecimal("56777")) == 0)) {
                // 73777 - Strong LoC
                // 56777 - URP-a                
                rba_eligibility = "D";
                //                }                       
            } else if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("35777")) == 0 ) {
                // Week LoC - 35777                
                rba_eligibility = "N";
            } else {
                // garancije banaka i ostale garancije      
                if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) && 
                        (first_call != null && first_call.equalsIgnoreCase("D"))) {
                    rba_eligibility = "D";
                }       
            }

        } else if (col_cat_id.compareTo(new BigDecimal("617223")) == 0) {
            // mjenice - 617223     
            if (low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) {
                rba_eligibility = "D";
            }   

        } else if (col_cat_id.compareTo(new BigDecimal("625223")) == 0) {
            // zaduznice - 625223   
            if (low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) {
                rba_eligibility = "D";
            }               

        } else if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
            // nekretnine           
            // stambene nekretnine - col_typ_id = 8777
            // zemljiste - col_typ_id = 7777
            // komercijalne nekretnine - sve ostalo         
            if ((sva_dokumentacija != null && sva_dokumentacija.equalsIgnoreCase("D")) &&
                    (low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) && 
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                       
        } else if (col_cat_id.compareTo(new BigDecimal("624223")) == 0) {
            // vozila           
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (knjizica_vozila != null && knjizica_vozila.equalsIgnoreCase("D")) &&   
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }               

        } else if (col_cat_id.compareTo(new BigDecimal("621223")) == 0) {
            // pokretnine           
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                           
        } else if (col_cat_id.compareTo(new BigDecimal("620223")) == 0) {
            // plovila  
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                   
        } else if (col_cat_id.compareTo(new BigDecimal("626223")) == 0) {
            // zalihe           
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                   
        } else if (col_cat_id.compareTo(new BigDecimal("616223")) == 0) {
            // police osiguranja        
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }               
        } else if (col_cat_id.compareTo(new BigDecimal("611223")) == 0)  {
            // akreditivi   
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }    
        } else if (col_cat_id.compareTo(new BigDecimal("614223")) == 0)  {
            // potrazivanja- cesije 
            if (col_typ_id != null && (col_typ_id.compareTo(new BigDecimal("45777")) == 0 || col_typ_id.compareTo(new BigDecimal("46777")) == 0)) {
                rba_eligibility = "N";                
            } else {
                if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                        (cesija_naplata != null && cesija_naplata.equalsIgnoreCase("N"))   ) {
                    rba_eligibility = "D";
                } else {
                    rba_eligibility = "N";
                }
            }
        } else if (col_cat_id.compareTo(new BigDecimal("628223")) == 0) {
            // zlato
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D")) &&
                    (banka_skladisti != null && banka_skladisti.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                               
        } else if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ){
            // finacijski kolaterali(dionice, obveznice, udjeli, zapisi, udjeli u poduzecima        
            if ((low_eligibility != null && low_eligibility.equalsIgnoreCase("D")) &&
                    (upisana_hipoteka != null && upisana_hipoteka.equalsIgnoreCase("D"))) {
                rba_eligibility = "D";
            }                               

        } else if (col_cat_id.compareTo(new BigDecimal("629223")) == 0) {
            // udjeli u poduzecima          
            rba_eligibility = "N";          
        }

        return rba_eligibility;
    }  

    // ND prihvatljivost
    public String chk_ND_eligibility_for_all(BigDecimal col_cat_id, BigDecimal col_typ_id, String rba_eligibility, 
            String kolateral_osiguran, String kasko_osigurano, String cesija_naplata){

        String ND_eligibility = "N";

        if (col_cat_id == null)
            return ND_eligibility;

        if (col_cat_id.compareTo(new BigDecimal("612223")) == 0) {
            //          depoziti    
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }
        } else if (col_cat_id.compareTo(new BigDecimal("615223")) == 0) {
            // garancije banaka i ostale garancije  
            if (col_typ_id != null  && (col_typ_id.compareTo(new BigDecimal("56777")) == 0 || 
                    col_typ_id.compareTo(new BigDecimal("73777")) == 0)) {
                //  56777 - URP-a
                //  73777 -  Strong LoC                
                ND_eligibility = "D";
            } else if (col_typ_id != null  && col_typ_id.compareTo(new BigDecimal("35777")) == 0 ) {
                //  35777 - Week LoC               
                ND_eligibility = "N";
            } else {            
                if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                    ND_eligibility = "D";
                }
            }
        } else if (col_cat_id.compareTo(new BigDecimal("617223")) == 0) {
            // mjenice      
            ND_eligibility = "N";

        } else if (col_cat_id.compareTo(new BigDecimal("625223")) == 0) {
            // zaduznice    
            ND_eligibility = "N";

        } else if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
            // nekretnine           
            // stambene nekretnine - col_typ_id = 8777
            if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0) {
                //stambene nekretnine   
                if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                        (kolateral_osiguran != null && kolateral_osiguran.equalsIgnoreCase("A"))) {
                    ND_eligibility = "D";
                }       
            } else if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("7777")) == 0) {
                // zemljiste - col_typ_id = 7777
                if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) ) {
                    ND_eligibility = "D";
                }                       
            } else {
                // komercijalne nekretnine - sve ostalo 
                // proizvodni objekt - real_es_type_id = 7222
                // hotel - 10222
                // pansion - 11222
                // hotelsko naselje - 31931223
                // bazen - 154714356223
                // privatan smjestaj - 12222    
                if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                        (kolateral_osiguran != null && kolateral_osiguran.equalsIgnoreCase("A"))) {
                    ND_eligibility = "D";
                }   
            }
        } else if (col_cat_id.compareTo(new BigDecimal("624223")) == 0) {
            // vozila           
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                    (kasko_osigurano != null && kasko_osigurano.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }

        } else if (col_cat_id.compareTo(new BigDecimal("621223")) == 0) {
            // pokretnine           
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                    (kolateral_osiguran != null && kolateral_osiguran.equalsIgnoreCase("A")) ) {
                ND_eligibility = "D";
            }   
        } else if (col_cat_id.compareTo(new BigDecimal("620223")) == 0) {
            // plovila  
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                    (kolateral_osiguran != null && kolateral_osiguran.equalsIgnoreCase("A")) ) {
                ND_eligibility = "D";
            }   
        } else if (col_cat_id.compareTo(new BigDecimal("626223")) == 0) {
            // zalihe           
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                    (kolateral_osiguran != null && kolateral_osiguran.equalsIgnoreCase("A"))) {
                ND_eligibility = "D";
            }    
        } else if (col_cat_id.compareTo(new BigDecimal("616223")) == 0) {
            // police osiguranja        
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }               
        } else if (col_cat_id.compareTo(new BigDecimal("611223")) == 0)  {
            // potrazivanja, akreditivi 
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }  
        } else if (col_cat_id.compareTo(new BigDecimal("614223")) == 0)  {
            // potrazivanja- cesije 
            if (col_typ_id != null && (col_typ_id.compareTo(new BigDecimal("45777")) == 0 || col_typ_id.compareTo(new BigDecimal("46777")) == 0)) {
                ND_eligibility = "N";                
            } else {
                if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D")) &&
                        (cesija_naplata != null && cesija_naplata.equalsIgnoreCase("N"))) {
                    ND_eligibility = "D";
                } else {
                    ND_eligibility = "N";
                }
            }                       
        } else if (col_cat_id.compareTo(new BigDecimal("628223")) == 0) {
            // zlato
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }                               
        } else if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0) {
            // finacijski kolaterali(dionice, obveznice, udjeli, zapisi )     
            if ((rba_eligibility != null && rba_eligibility.equalsIgnoreCase("D"))) {
                ND_eligibility = "D";
            }

        } else if ( col_cat_id.compareTo(new BigDecimal("629223")) == 0) {
            // udjeli u poduzecima            
            ND_eligibility = "N";           
        }

        return ND_eligibility;
    }           

    public void setRealEstateFirstScreenCtx (BigDecimal col_typ_id, String screen_context, BigDecimal real_es_type_id) {
        if (col_typ_id.compareTo(new BigDecimal("7777")) == 0) {
            // zemljiste              
            ra.setContext("Kol_lblLegality","fld_hidden");
            ra.setContext("Kol_txtLegality","fld_hidden");
            ra.setRequired("Kol_txtLegality", false); 
            
            if (ra.getScreenContext().compareTo("scr_detail")== 0 || ra.getScreenContext().compareTo("scr_detail_deact")== 0 || ra.getScreenContext().compareTo("scr_detail_co")== 0) {
                ra.setContext("Kol_lblBuildPerm","fld_protected");
                ra.setContext("Kol_txtBuildPerm","fld_protected");                
            } else {
                ra.setContext("Kol_lblBuildPerm","fld_plain");
                ra.setContext("RealEstate_txtBuildPermInd","fld_protected");    
                ra.setAttribute("RealEstate_txtBuildPermInd", "D");

                if (real_es_type_id != null && 
                        (real_es_type_id.compareTo(new BigDecimal("1222")) == 0 || real_es_type_id.compareTo(new BigDecimal("13222")) == 0 || real_es_type_id.compareTo(new BigDecimal("61324")) == 0)) {
                    ra.setContext("Kol_txtBuildPerm","fld_plain");
                    ra.setRequired("Kol_txtBuildPerm", true);              
                } else {
                    ra.setContext("Kol_txtBuildPerm","fld_protected");
                    ra.setRequired("Kol_txtBuildPerm", false);   
                    ra.setAttribute("Kol_txtBuildPerm", null);
                }
            }              
        } else {
            // ostale nekretnine  
            ra.setContext("Kol_lblBuildPerm","fld_hidden");
            ra.setContext("Kol_txtBuildPerm","fld_hidden"); 
            ra.setContext("RealEstate_txtBuildPermInd","fld_plain");    
            if (ra.getScreenContext().compareTo("scr_detail")== 0 || ra.getScreenContext().compareTo("scr_detail_deact")== 0 || ra.getScreenContext().compareTo("scr_detail_co")== 0) {
                ra.setContext("Kol_lblLegality","fld_protected");
                ra.setContext("Kol_txtLegality","fld_protected");  
                ra.setContext("RealEstate_txtBuildPermInd","fld_protected");  
            } else {
                ra.setContext("Kol_lblLegality","fld_plain");
                ra.setContext("Kol_txtLegality","fld_plain");
                // col_cat_id = 618223;
                // col_typ_id = 9777 and real_es_type_id = 7222 
                // col_typ_id = 10777  and real_es_type_id in (10222,11222,31931223,154714356223)   
                if (real_es_type_id != null) {
                    if((col_typ_id.compareTo(new BigDecimal("9777")) == 0 && real_es_type_id.compareTo(new BigDecimal("7222")) == 0) ||
                            (col_typ_id.compareTo(new BigDecimal("10777")) == 0 && (real_es_type_id.compareTo(new BigDecimal("10222")) == 0 ||
                                    real_es_type_id.compareTo(new BigDecimal("11222")) == 0 || real_es_type_id.compareTo(new BigDecimal("31931223")) == 0 ||
                                    real_es_type_id.compareTo(new BigDecimal("154714356223")) == 0))) {
                        //System.out.println("TU SAM u metodi 1 "+ra.getScreenContext() + col_typ_id + real_es_type_id);   
                        //ra.setContext("RealEstate_txtRealEstSpecStat","fld_protected"); // FBPr200016947 - 5.9.2012. - omoguæiti unos CRM mišljenja za sve vrste kolaterala
                    } else {
                        ra.setContext("RealEstate_txtRealEstSpecStat","fld_plain");  
                    }
                }
            }
            ra.setRequired("Kol_txtLegality", true);   
            ra.setRequired("Kol_txtBuildPerm", false);   
        }
    }     

    /**
     * Metoda racuna novu ponderiranu i raspolozivu vrijednost
     * @param ra
     */
    public void getPonderAndRestAmount (ResourceAccessor ra) {

        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
        BigDecimal numberZero = new java.math.BigDecimal("0.00");
        BigDecimal numberOneHundred = new java.math.BigDecimal("100.00");

        BigDecimal ponderAmount = numberZero;
        BigDecimal restAmount = numberZero; 

        BigDecimal nominalAmount = numberZero;
        BigDecimal exposureAmount = numberZero;
        BigDecimal ponder = numberZero;

        if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
            nominalAmount = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_txtNomiValu");
            exposureAmount = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_txtSumPartVal");
            ponder = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder");            
        } else {
            nominalAmount = (BigDecimal) ra.getAttribute("CollHeadLDB", "Coll_txtNomiValu");
            exposureAmount = (BigDecimal) ra.getAttribute("CollHeadLDB", "Coll_txtSumPartVal");
            ponder = (BigDecimal) ra.getAttribute("CollHeadLDB", "Coll_txtCollMvpPonder");
        }

        if (isAmountNull(nominalAmount) && isAmountNull(ponder)) {
            ponderAmount = nominalAmount.multiply(ponder);
            ponderAmount= ponderAmount.divide(numberOneHundred, 2, java.math.BigDecimal.ROUND_HALF_UP);
        }        

        restAmount = ponderAmount;

        if (isAmountNull(restAmount) && isAmountNull(exposureAmount)) {
            restAmount = restAmount.subtract(exposureAmount);
        }

        if (restAmount.signum() == -1 ) {
            restAmount = new BigDecimal("0.00");
        }

        Date todaySQLDate = null;
        GregorianCalendar calendar = new GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new Date(timeT);     

        if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue",ponderAmount);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue",restAmount);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate",todaySQLDate);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate",todaySQLDate);            
        } else {
            ra.setAttribute("CollHeadLDB","Coll_txtAcouValue",ponderAmount);
            ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",restAmount);
            ra.setAttribute("CollHeadLDB","Coll_txtAcouDate",todaySQLDate);
            ra.setAttribute("CollHeadLDB","Coll_txtAvailDate",todaySQLDate);
        }
        return;
    }

    /**
     * Metoda postavlja default za CRM misljenje prema vrsti i podvrsti nekretnine
     * @param col_typ_id, col_sub_typ_id
     */
    public void setRealEstateCRMopinion (BigDecimal col_typ_id, BigDecimal col_sub_typ_id) {
        
        //        KATEGORIJA        VRSTA                               PODVRSTA                        CRM 
        //        Nekretnine        2POP - Poslovni objekt(prostor)     PROIHALA - Proizvodni objekt    ne
        //        Nekretnine        2TOO - Turistièki objekt            HOTEL    - Hotel                ne
        //        Nekretnine        2TOO - Turistièki objekt            PANSION  - Pansion              ne
        //        Nekretnine        2TOO - Turistièki objekt            HOTNAS   - Hotelsko naselje     ne
        //        Nekretnine        2TOO - Turistièki objekt            2TOOBAZ  - Bazen                ne 
        // col_cat_id = 618223;
        // col_typ_id = 9777 and real_es_type_id = 7222 
        // col_typ_id = 10777  and real_es_type_id in (10222,11222,31931223,154714356223) 
        // col_typ_id = 7777 - zemljiste
        // real_es_type_id = 1222 - gradjevinsko zemljiste
        // real_es_type_id = 2222 - poljoprivredno zemljiste

        if (col_typ_id != null && col_sub_typ_id != null) {
            if ((col_typ_id.compareTo(new BigDecimal("9777")) == 0 && col_sub_typ_id.compareTo(new BigDecimal("7222")) == 0) ||
                    (col_typ_id.compareTo(new BigDecimal("10777")) == 0 &&
                            ( col_sub_typ_id.compareTo(new BigDecimal("10222")) == 0 ||
                                    col_sub_typ_id.compareTo(new BigDecimal("11222")) == 0  ||
                                    col_sub_typ_id.compareTo(new BigDecimal("31931223")) == 0  ||
                                    col_sub_typ_id.compareTo(new BigDecimal("154714356223")) == 0))) { 

                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtRealEstSpecStat","N");   
                // ra.setContext("RealEstate_txtRealEstSpecStat","fld_protected"); // FBPr200016947 - 5.9.2012. - omoguæiti unos CRM mišljenja za sve vrste kolaterala                     
            } else {
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtRealEstSpecStat","D");   
                ra.setContext("RealEstate_txtRealEstSpecStat","fld_plain");     
                if (col_typ_id.compareTo(new BigDecimal("7777")) == 0 && (col_sub_typ_id.compareTo(new BigDecimal("1222")) == 0 || col_sub_typ_id.compareTo(new BigDecimal("13222")) == 0 || col_sub_typ_id.compareTo(new BigDecimal("61324")) == 0)) {
                    ra.setContext("Kol_txtBuildPerm","fld_plain");
                    ra.setRequired("Kol_txtBuildPerm", true);                
                } else if (col_typ_id.compareTo(new BigDecimal("7777")) == 0 && col_sub_typ_id.compareTo(new BigDecimal("2222")) == 0) {
                    ra.setContext("Kol_txtBuildPerm","fld_protected");
                    ra.setRequired("Kol_txtBuildPerm", false);    
                    ra.setAttribute("Kol_txtBuildPerm", null);
                }                
            }
        } else {
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtRealEstSpecStat","D");   
            ra.setContext("RealEstate_txtRealEstSpecStat","fld_plain");    
        }
    }

    public void setNumberOfOwnersCtx (BigDecimal col_typ_id, BigDecimal cus_typ_id) {
        // System.out.println("TU SAM u metodi  "+ra.getScreenContext() + col_typ_id + cus_typ_id); 

        if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0) {
            if (cus_typ_id != null && (cus_typ_id.compareTo(new BigDecimal("1999")) == 0 || cus_typ_id.compareTo(new BigDecimal("1998")) == 0)){
                ra.setContext("Kol_txtOwnNumOfEstate","fld_plain");  
                ra.setRequired("Kol_txtOwnNumOfEstate", true);     

                ra.setContext("Kol_txtStatementRegNo","fld_plain");  
                ra.setRequired("Kol_txtStatementRegNo", false);       
                ra.setContext("Kol_txtStatementName","fld_plain");  
                ra.setRequired("Kol_txtStatementName", false);          
            } else {    
                ra.setContext("Kol_txtOwnNumOfEstate","fld_plain");  
                ra.setRequired("Kol_txtOwnNumOfEstate", false);     
                ra.setContext("Kol_txtStatementRegNo","fld_plain");  
                ra.setRequired("Kol_txtStatementRegNo", false);    
                ra.setContext("Kol_txtStatementName","fld_plain");  
                ra.setRequired("Kol_txtStatementName", false);   
            }
            ra.setContext("Kol_txtMainOwner","fld_plain");   
            ra.setRequired("Kol_txtMainOwner", true);               
        } else {
            ra.setContext("Kol_txtOwnNumOfEstate","fld_protected");   
            ra.setRequired("Kol_txtOwnNumOfEstate", false);   
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstate", "");   
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc","");

            ra.setContext("Kol_txtMainOwner","fld_plain");   
            ra.setRequired("Kol_txtMainOwner", false);    

            ra.setContext("Kol_txtStatementRegNo","fld_protected");  
            ra.setRequired("Kol_txtStatementRegNo", false);     
            ra.setContext("Kol_txtStatementName","fld_protected");  
            ra.setRequired("Kol_txtStatementName", false);     
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", "");   
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName","");            
        }
    }         

    public void setCesijaFirstScrCtx (BigDecimal cesija_vrsta_id) {

        if (cesija_vrsta_id != null ) {
            if (cesija_vrsta_id.compareTo(new BigDecimal("3763282384")) == 0 ||
                    cesija_vrsta_id.compareTo(new BigDecimal("3763283814")) == 0 ) {
                ra.setRequired("Kol_txtCesijaDateExp", true); 
                ra.setRequired("Kol_txtGuarAmount", true); 
                ra.setRequired("Kol_txtCesijaMatDate", true); 
            } else {
                ra.setRequired("Kol_txtCesijaDateExp", false);  
                ra.setRequired("Kol_txtGuarAmount", false); 
                ra.setRequired("Kol_txtCesijaMatDate", false); 
                ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount",new BigDecimal("0.00"));
                // procijenjena 
                ra.setAttribute("CollHeadLDB", "Coll_txtEstnValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
                // nominalna        
                ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
                // neponderirana        
                ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
            }
        } else {
            ra.setRequired("Kol_txtCesijaDateExp", false);  
            ra.setRequired("Kol_txtGuarAmount", false); 
            ra.setRequired("Kol_txtCesijaMatDate", false);    
        }
    } 

    public void setCesijaListaScrCtx (String cesija_lista) {

        if (cesija_lista != null ) {
            if (cesija_lista.equalsIgnoreCase("D")) {
                ra.setRequired("Kol_txtCesijaListaDatum", true); 
            } else {
                ra.setRequired("Kol_txtCesijaListaDatum", false);  
            }
        } else {
            ra.setRequired("Kol_txtCesijaListaDatum", false);  
        }
    }     

    public Date setCesijaListaNextDate (Date date, int amount) {

        GregorianCalendar calendar = new GregorianCalendar();      
        calendar.setTime(date);           
        calendar.add(Calendar.MONTH, amount);
        return new Date(calendar.getTime().getTime());       
    }   
    
    public void clearFields(String LDBName, String[] fields){
        for (String field : fields) {
            clearField(LDBName, field);
        }
    }   
    
    public void clearField(String LDBName, String fieldName){
        ra.setAttribute(LDBName, fieldName, null);
    }    
    
    /**
     * @param fieldNames
     * @param state:<br/> 0-fld_plain<br/>1-fld_change_protected<br/>2-fld_protected<br/>
     * Ako se za polje posalje stanje  1 ili 2, automatski se postavlja da nije obavezno, ako je bilo 
     */
    public void enableFields(String[] fieldNames,int state){
        for (String field : fieldNames) {
            enableField(field, state);
        }
    }
    
    /**
     * @param fieldName
     * @param state:<br/> 0-fld_plain<br/>1-fld_change_protected<br/>2-fld_protected<br/>
     * Ako se za polje posalje stanje  1 ili 2, automatski se postavlja da nije obavezno, ako je bilo 
     */
    public void enableField(String fieldName, int state){
        if(state==0) ra.setContext(fieldName, "fld_plain");
        else if(state==1) {
            ra.setContext(fieldName, "fld_change_protected");
            try {
                ra.setRequired(fieldName, false);
            } catch (Exception e) {
                // Ako se ne uspije postaviti Required idemop dalje. 
                // greska se najvjerovatnije dogodila zato sto polje ne postoji na ekranu 
            }
        }
        else {
            ra.setContext(fieldName, "fld_protected");
            try {
                ra.setRequired(fieldName, false);
            } catch (Exception e) {
                // Ako se ne uspije postaviti Required idemop dalje. 
                // greska se najvjerovatnije dogodila zato sto polje ne postoji na ekranu 
            }
        }
    }
    /**Metoda sluzi za skrivanje polja 
     * @param fieldNames
     */
    public void hideFields(String[] fieldNames){
        for (String field : fieldNames) {
            ra.setContext(field, "fld_hidden");
        }
    }
    
    /**Metoda sluzi za prikazivanje polja ako je polje bilo hidden 
     * @param fieldNames
     */
    public void showFields(String[] fieldNames){
        for (String field : fieldNames) {
            this.enableField(field, 0);
        }
    }
    
    /**Metoda služi za postavljanje više polja u required status
     * @param fieldNames
     * @param required
     */
    public void setRequired(String[] fieldNames, boolean required){
        for (String field : fieldNames) {
            try {
                ra.setRequired(field, required);
            } catch (Exception e) {
                // Ako se ne uspije postaviti Required idemop dalje. 
                // greska se najvjerovatnije dogodila zato sto polje ne postoji na ekranu 
            }
        }        
    }
    
    public boolean checkIfFieldsAreFilled(Vector<CollReqFieldData> fields){  
        String scrCtx=ra.getScreenContext();
        for (CollReqFieldData field : fields) {
            boolean preCond=true;
            //provjeravam da li ima preduvjeta koji moraju biti ispunjeni da se ide u provjeru polja
            String elContext=null;
            if(ra.isElementExist(field.field)){
                elContext=ra.getContext(field.field);    
            } 
            //this.ShowInfoMessage("field.field=" + field.field+", context="+scrCtx);
            if(elContext!=null && (elContext.equals("fld_protected") || elContext.equals("fld_change_protected") || elContext.equals("fld_hidden"))) continue;
            else if(field.context!=null && !field.context.equals(scrCtx)) {
                //ShowInfoMessage("razlicit context-preskacem");
                continue;
            }
            
            if(field.preConditions!=null && field.preConditions.size()>0){
                //provjera preduvjeta ako ih ima; 
                for (CollReqFieldDataCondition pre : field.preConditions) {
                    //ShowInfoMessage("preduvjet="+pre.preConditionField+", vrijednost="+pre.preConditionValue);
                    Object pre_value=null;
                    if(pre.preConditionLDB!=null){
                        pre_value = ra.getAttribute(pre.preConditionLDB, pre.preConditionField);
                    }else{
                        pre_value = ra.getAttribute(field.ldb, pre.preConditionField);
                    }
                    if(pre_value!=null && pre_value.getClass().getName()=="java.lang.String") pre_value=((String)pre_value).trim();
                    if(pre.equals){ 
                        //this.ShowInfoMessage("1.pre.preConditionValue="+pre.preConditionValue + ", pre.preConditionField="+ pre.preConditionField);
                        //this.ShowInfoMessage("1.pre.pre_value="+pre_value + ", field.field=" + field.field);
                        if(pre_value!=null && !pre_value.equals(pre.preConditionValue)) { preCond=false; break; }
                        else if(pre_value==null && pre_value!=pre.preConditionValue){ preCond=false; break; }
                    }
                    if(!pre.equals){ 
                        //this.ShowInfoMessage("2.pre.preConditionValue="+pre.preConditionValue + ", pre.preConditionField="+ pre.preConditionField);
                        //this.ShowInfoMessage("2.pre.pre_value="+pre_value + ", field.field=" + field.field);
                        if(pre_value!=null && pre_value.equals(pre.preConditionValue)) { preCond=false; break; }
                        else if(pre_value==null && pre_value==pre.preConditionValue){ preCond=false; break; }
                    }
                }
            }
            if(preCond){
                Object value=ra.getAttribute(field.ldb, field.field);

                if(value==null || (value.getClass().getName()=="java.lang.String" && ((String)value).trim().equals(""))){
                    if(field.messageID!=null){
                        ra.showMessage(field.messageID);  
                    }else{
                        HashMap x=new HashMap();
                        x.put("msg", field.msg);
                        ra.showMessage("infzstColl06", x);                        
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    public void SetDefaultValue(String ldb, String fieldName, Object defaultValue){
        Object value = ra.getAttribute(ldb, fieldName);
        if(value!=null && value.toString().length()!=0) return;
        ra.setAttribute(ldb, fieldName,defaultValue);
    }
    
    public void UnprotectAllFields(){
        for (Iterator iterator = ra.getScreenElements().iterator(); iterator.hasNext();) {
            String type = (String) iterator.next();
            //System.out.println(type);
            this.enableField(type, 0);
        }
    }
    
    /**Metoda provjerava da li navedeno polje postoji na ekranu
     * 
     * @param fieldName
     * @return true - ako postoji, false ako ne postoji
     */
    public boolean CheckIfExistFieldOnScreen(String fieldName){
        for (Iterator iterator = ra.getScreenElements().iterator(); iterator.hasNext();) {
            String type = (String) iterator.next();
            if(type.equals(fieldName)) return true;
        }
        return false;
    }    
    
    public boolean isEmpty(String s){
        if(s==null || s.trim().equals("")) return true;
        return false;
    }
    
    public String GetDefaultValueFromSystemCodeValue(String sys_cod_id){
        String value="";
        String sql="SELECT * FROM system_code_value where sys_cod_id='" + sys_cod_id + "' AND default_flag='1'";
        VestigoResultSet rs=null;
        
        try {
            rs=ra.getMetaModelDataSource().select(sql, "aix");
            if (rs.rs.next()) {
                value = rs.rs.getString("sys_code_value");
            }
        }catch (Exception e) {

        } finally {
            if (rs != null) rs.close();
        }
        return value;        
    } 
    
    public String GetRegionFromDatabase(BigDecimal pol_map_id){
        String value="";
        String sql="SELECT s.SYS_CODE_DESC FROM POL_REGION_MAP prm INNER JOIN  SYSTEM_CODE_VALUE s ON s.SYS_COD_VAL_ID = prm.region_id" +
                 " WHERE current date BETWEEN prm.date_from AND prm.date_until AND prm.pol_map_id ="+pol_map_id+" WITH UR";
        VestigoResultSet rs=null;
        
        try {
            rs=ra.getMetaModelDataSource().select(sql, "aix");
            if (rs.rs.next()) {
                value = rs.rs.getString("SYS_CODE_DESC");
            }
        }catch (Exception e) {
            
        } finally {
            if (rs != null) rs.close();
        }
        return value;        
    }
    
    public void ShowInfoMessage(String message) {//TODO debug method
        HashMap<String, String> messageText = new HashMap<String, String>();
        messageText.put("message",  message);
        debug("***debugShowMessage-->"+ message);  
        ra.showMessage("inf968", messageText);
    }      
}  