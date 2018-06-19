//created 2017.03.02
package hr.vestigo.modules.collateral.common.yoyJ;

import hr.vestigo.modules.coreapp.share.DataObject;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author hrazst
 */
public class CollCashDepData extends DataObject<Object>{

    public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyJ/CollCashDepData.java,v 1.1 2017/03/02 12:45:58 hrazst Exp $";
    /**
     * vrsta depozita:
     * 1FCRBA – depozit u stranoj valuti u RBA
     * 1KNKNRBA – depozit u domaæoj valutu u RBA
     * 1KNCCRBA – depozit u kunama s valutnom klauzulom u RBA
     * 1FCRZB – depozit u stranoj valuti unutar RZB grupe
     */
    public String cde_typ=null;
    /** valuta depozita id */
    public String cde_cur_id=null;
    /** valuta depozita (troslovna oznaka) */
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
    /** id partije depozita */
    public String cde_account_id=null;
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
    
    public CollCashDepData(){ }

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

