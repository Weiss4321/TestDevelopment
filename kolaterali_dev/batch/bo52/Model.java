//created 2010.02.22
package hr.vestigo.modules.collateral.batch.bo52;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

/**
 * !!! NE KORISTI SE VIŠE !!!
 * @author hraamh
 */
public class Model {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo52/Model.java,v 1.9 2010/09/22 08:31:05 hrakis Exp $";
    
    private Vector<AccCoverData> domain=null;
    private boolean debugMode=true;
    
    public Model(boolean isDebug){
        debugMode=isDebug;
        domain = new Vector<AccCoverData>();
    }
    /**
     * dodavanje plasmana u model izracuna
     * @param data
     */
    public void add(AccCoverData data){
        domain.add(data);
    }
    
    /**
     * dohvaca plasman za dani index
     * @param i
     * @return
     */
    public AccCoverData get(int i){
        return domain.elementAt(i);
    }
    /**
     * dohvaca broj plasmana u modelu
     * @return
     */
    public int count(){
        return domain.size();
    }
    /**
     * vraca Vektor sa svim plasmanima u modelu. Poziva se nakon calculate(BigDecimal frameAmountHrk) metode
     * @return
     */
    public Vector<AccCoverData> getDomain(){
        return domain;
    } 
    /**
     * metoda za proracun pokrivenosti. nakon sto se svi plasmani stave u model poziva se ova metoda.
     * ona alikvotno dijeli iznos kojim je okvir pokriven na sve plasmane u modelu.
     * 
     * @param frameAmountHrk Pokrivenost okvira kolateralom
     * @param frameTotalCover Ukupna pokrivenost okvira svim kolateralima
     */
    public void calculate(BigDecimal frameAmountHrk, BigDecimal frameTotalCover){
        BigDecimal sumExposure=new BigDecimal(0);
        for (int i = 0; i < domain.size(); i++) {
            AccCoverData data = domain.get(i);
            sumExposure=sumExposure.add(data.acc_amount_hrk);  // suma izlozenosti svih plasmana
            
        }
        print("sumExposure: suma svih plasmana: "+sumExposure);

// alikvotni dio plasmana = plasman / suma svih plasmana        
        if(DecimalUtils.compareValues(frameTotalCover, sumExposure, DecimalUtils.LOW_PRECISION)>0){  // ukupna pokrivenost okvira je veæa od sume izloženosti svih plasmana iz okvira 
// iznos do kojeg se pokriva je suma izlozenosti svih plasmana         
            print("prvi slucaj");
            print("frameAmountHrk: pokrivenost okvira kolateralom: "+frameAmountHrk);
            print("frameTotalCover: ukupna pokirvenost okvira: "+frameTotalCover);
            for (int i = 0; i < domain.size(); i++) {
                AccCoverData data = domain.get(i);
                if(frameTotalCover.compareTo(BigDecimal.ZERO) > 0) {  // provjera zbog izbjegavanja dijeljenja s nulom
                    data.acc_cov_amount_kn = data.acc_amount_hrk.multiply(frameAmountHrk).divide(frameTotalCover, DecimalUtils.HUGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                    data.exp_percent=new BigDecimal("1.00");
                }else{  
                    data.acc_cov_amount_kn = new BigDecimal("0.00");
                    data.exp_percent=new BigDecimal("0.00");                    
                }
                print("data.acc_amount_hrk: iznos plasmana: "+data.acc_amount_hrk);
                print("data.acc_cov_amount_kn: pokrivenost plasmana kolateralom: "+data.acc_cov_amount_kn);
/*                if(data.frame_exposure_kn.compareTo(BigDecimal.ZERO) > 0) {  // provjera zbog izbjegavanja dijeljenja s nulom
                    data.acc_cov_amount_kn = data.acc_amount_hrk.multiply(frameAmountHrk).divide(data.frame_exposure_kn, DecimalUtils.HUGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                    data.exp_percent=new BigDecimal("1.00");
                }else{
                    data.acc_cov_amount_kn = new BigDecimal("0.00");
                    data.exp_percent=new BigDecimal("0.00");                    
                } */
            }
        }else{
            print("vrsi se izracun");
            if(sumExposure.compareTo(BigDecimal.ZERO) > 0) {  // provjera zbog izbjegavanja dijeljenja s nulom
                BigDecimal precentage=frameAmountHrk.divide(sumExposure,DecimalUtils.HUGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
/*                if(domain.size()==1){
                    AccCoverData data = domain.get(0);
                    data.acc_cov_amount_kn=sumExposure;
                    data.exp_percent=DecimalUtils.scale(precentage, 4);
                     print("3 SLUCAJ data.acc_cov_amount_kn: pokrivenost plasmana kolateralom: "+data.acc_cov_amount_kn);                     
                }else{ */
                    for (int i = 0; i < domain.size(); i++) {
                        AccCoverData data = domain.get(i);
                        //racunamo udio plasmana u alikvotnom zbroju
                        data.acc_cov_amount_kn = frameAmountHrk.multiply(data.acc_amount_hrk).divide(sumExposure, DecimalUtils.HUGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                        data.exp_percent=DecimalUtils.scale(precentage, 4);
                        print("2 SLUCAJ data.acc_amount_hrk: iznos plasmana: "+data.acc_amount_hrk);
                        print("2 SLUCAJ data.acc_cov_amount_kn: pokrivenost plasmana kolateralom: "+data.acc_cov_amount_kn);                        
                    }
//                }
            }else{
                for (int i = 0; i < domain.size(); i++) {
                    AccCoverData data = domain.get(i);
                    data.acc_cov_amount_kn = new BigDecimal("0.00");
                    data.exp_percent = new BigDecimal("0.00");
                }
            }
        }
        BigDecimal afterCalc= new BigDecimal(0);
        for (int i = 0; i < domain.size(); i++) {
            afterCalc =afterCalc.add(domain.get(i).acc_cov_amount_kn);
        }
        print("Model-> afterCalc:"+afterCalc);
        print("Model->\n"+toString());
    }
    
    /**
     * ispis plasmana u modelu
     */
    public String toString(){
        String result="";
        for (int i = 0; i < domain.size(); i++) {
            AccCoverData data=get(i);
            result+="\t Data "+i+"\n"+data+"\n";
        }
        return result;
    }
    /**
     * ispis SystemOut
     * @param toPrint
     */
    public void print(String toPrint){
        if(debugMode) System.out.println(toPrint);
    }
    
    
}

