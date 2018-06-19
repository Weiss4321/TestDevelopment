/*
 * hrazst Created on 2009.03.11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo40;

import java.math.BigDecimal;
import java.sql.Date;

/**Data objekt za balance iz tablice cusacc_balance
 * 
 * @author hrazst
 *
 */
public class BalanceData {
    /**
     * primarni kljuè zapisa u tablici cusacc_balance
     */
    public BigDecimal cusacc_bal_id=null;
    /**
     * strani kljuè na partiju klijenta
     */
    public BigDecimal cus_acc_id=null;
    /**
     * knjigovodstvena partija
     */
    public BigDecimal acc_num_id=null;
    /**
     * id komitenta
     */
    public BigDecimal cus_id=null;
    /**
     * šifra konta
     */
    public String account=null;
    /**
     * vrsta poslovnog odnosa
     */
    public String ban_rel_type=null;
    /**
     * vrsta iznosa
     */
    public String amo_type=null;
    /**
     * postupak po poslu
     */
    public BigDecimal pro_typ_id=null;
    /**
     * datum stanja
     */
    public Date balance_date=null;
    /**
     * valuta 
     */
    public BigDecimal cur_id=null;
    /**
     * dugovni kumulativ
     */
    public BigDecimal debit_total=null;
    /**
     * potrazni kumulativ
     */
    public BigDecimal credit_total=null;    
    /**
     * saldo partije na dan
     */
    public BigDecimal balance=null;
    /**
     * protuvrijenost stanja u kunama za valutu valutne klauzule 
     */
    public BigDecimal clause_balance=null; 
    
    
    public String toString(){
        StringBuffer buffy=new StringBuffer();
        
        buffy.append("cusacc_bal_id=[").append(this.cusacc_bal_id).append("],");
        buffy.append("cus_acc_id=[").append(this.cus_acc_id).append("],");
        buffy.append("acc_num_id=[").append(this.acc_num_id).append("],");
        buffy.append("cus_id=[").append(this.cus_id).append("],");
        buffy.append("account=[").append(this.account).append("],");
        buffy.append("ban_rel_type=[").append(this.ban_rel_type).append("],");
        buffy.append("amo_type=[").append(this.amo_type).append("],");
        buffy.append("pro_typ_id=[").append(this.pro_typ_id).append("],");
        buffy.append("balance_date=[").append(this.balance_date).append("],");
        buffy.append("cur_id=[").append(this.cur_id).append("],");
        buffy.append("debit_total=[").append(this.debit_total).append("],");
        buffy.append("credit_total=[").append(this.credit_total).append("],");
        buffy.append("balance=[").append(this.balance).append("],");
        buffy.append("clause_balance=[").append(this.clause_balance).append("].");
        
        return buffy.toString();
    }
}
