/*
 * hrazst Created on 2009.03.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo40;

import java.math.BigDecimal;

public class CustomerAccountData { 

           
    /**
     * partija raèuna klijenta
     */        
    public String cus_acc_no=null;
    /**
     * orgdio kojem pripada raèun/partija klijenta
     */
    public BigDecimal org_uni_id=null;
    /**
     * Sifra deviznosti 
     */
    public String currency_type=null;
    /**
     * Vlasnik partije/ugovora
     */
    public BigDecimal cus_id=null;
    /**
     * Sifra bankarskog proizvoda
     */
    public BigDecimal ban_pro_typ_id=null;
    /**
     * knjigovodstvena namjena proizvoda
     */
    public BigDecimal pro_cat_id=null;
    /**
     * status racuna
     */
    public String status=null;
    
    
    public String toString(){
        StringBuffer buffy=new StringBuffer();
        
        buffy.append("cus_acc_no=[").append(this.cus_acc_no).append("],");
        buffy.append("org_uni_id=[").append(this.org_uni_id).append("],");
        buffy.append("currency_type=[").append(this.currency_type).append("],");
        buffy.append("cus_id=[").append(this.cus_id).append("],");
        buffy.append("ban_pro_typ_id=[").append(this.ban_pro_typ_id).append("],");
        buffy.append("pro_cat_id=[").append(this.pro_cat_id).append("],");
        buffy.append("status=[").append(this.status).append("].");
        
        return buffy.toString();        
    }
    
}

