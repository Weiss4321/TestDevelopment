/*
 * Created on 2007.04.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo10;

import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InputData {

    public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo10/InputData.java,v 1.9 2014/01/16 09:33:01 hrajkl Exp $";
    /**
     * vrsta depozita:
     * 1FCRBA – depozit u stranoj valuti u RBA
     * 1KNKNRBA – depozit u domaæoj valutu u RBA
     * 1KNCCRBA – depozit u kunama s valutnom klauzulom u RBA
     * 1FCRZB – depozit u stranoj valuti unutar RZB grupe
     */
    public String cde_typ=null;
    /** valuta depozita (troslovna ili numericka oznaka) */
    public String cde_cur=null;
    /** iznos depozita u valuti depozita */
    public BigDecimal cde_amount=null;
    /** prolongat:
     * da li je depozit ugovoren uz prolongat; 
     * moguæe vrijednosti
     * "D" ili "N"
     */
    public String cde_prolong=null;
    /** banka kod koje je prolongat, Omega Id banke kod koje je depozit */
    public String cde_reg_no=null;
    /** broj partije depozita */
    public String cde_account=null;
    /** datum oroèenja depozita */
    public Date cde_dep_from=null;
    /** datum dospijeæa depozita */
    public Date cde_dep_unti=null;
    /** vlasnik depozita - Omega ID vlasnika depozita */
    public String cde_owner=null;
    /** partija plasmana - broj partije plasmana */
    public String acc_num=null;
    /** vlasnik plasmana - Omega ID korisnika plasmana */
    public String loan_owner=null;
    /** id depozita koji se iskljucuje iz updatea iznosa */
    public BigDecimal cas_exc_id=null;
    /** status partije depozita u originalnom modulu */
    public String status=null;
    /** datum krajnjeg roka dospijeæa depozita */
    public Date cde_dep_unti_final=null;	


    public InputData(){

    }

    public String toString(){
        String result="\n";
        result+="cde_typ: "+cde_typ+"\n";		
        result+="cde_cur: "+cde_cur+"\n";
        result+="cde_amount: "+cde_amount+"\n";
        result+="cde_prolong: "+cde_prolong+"\n";
        result+="cde_reg_no: "+cde_reg_no+"\n";
        result+="cde_account: "+cde_account+"\n";
        result+="cde_dep_from: "+cde_dep_from+"\n";
        result+="cde_dep_unti: "+cde_dep_unti+"\n";
        result+="cde_owner: "+cde_owner+"\n";
        result+="acc_num: "+acc_num+"\n";
        result+="loan_owner: "+loan_owner+"\n";
        result+="cas_exc_id: "+cas_exc_id+"\n";
        result+="status: "+status+"\n";
        result+="cde_dep_unti_final: "+cde_dep_unti_final+"\n";

        return result;
    }

    /**
     * provjera, trimanje i nuliranje ulaznih podataka
     * 
     */
    public void trim(){
        cde_typ=trimAndNull(cde_typ);
        cde_cur=trimAndNull(cde_cur);
        cde_prolong=trimAndNull(cde_prolong);
        cde_reg_no=trimAndNull(cde_reg_no);
        cde_account=trimAndNull(cde_account);
        cde_owner=trimAndNull(cde_owner);
        acc_num=trimAndNull(acc_num);
        loan_owner=trimAndNull(loan_owner);		
    }

    private String trimAndNull(String input){
        String result=input;
        if(input!=null){
            result=input.trim();
            if(result.equals("")){
                result=null;
            }
        }
        return result;
    }

    /**
     * Metoda provjerava podatke objekta
     * <pre>@return 
     * 1 - cde_typ (vrsta depozita) je null
     * 2 - cde_cur (valuta depozita) je null
     * 3 - cde_account (broj partije depozita) je null
     * 4 - cde_amount (iznos depozita) je null 
     * 0 - inaèe </pre>
     */
    public int check(){
        if(cde_typ==null){
            return 1;
        }else if(cde_cur==null){
            return 2;
        }else if(cde_account==null){
            return 3;
        }else if((cde_amount==null)){
            return 4;
        }   
        return 0;
    }
}
