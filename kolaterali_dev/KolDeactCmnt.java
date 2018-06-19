/*
 * Created on 2006.06.07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**  
 * @author hramkr 
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KolDeactCmnt extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolDeactCmnt.java,v 1.5 2017/04/27 15:40:04 hrazst Exp $";
	
	public KolDeactCmnt(ResourceAccessor ra) {
		super(ra);
	}  
          
	public void KolDeactCmnt_SE() {
		if (!ra.isLDBExists("KolDeactCmntLDB")) {
			ra.createLDB("KolDeactCmntLDB");
		}
		ra.setAttribute("KolDeactCmntLDB", "col_hea_id", ra.getAttribute("ColWorkListLDB", "col_hea_id"));
		ra.setAttribute("KolDeactCmntLDB", "coll_deact_indic", ra.getAttribute("ColWorkListLDB", "coll_deact_indic"));
		ra.setContext("KolDeactCmnt_blCommentHip", "fld_hidden");

		if(ra.getScreenContext().equalsIgnoreCase("MortageDeactContext")){
            ra.setContext("KolDeactCmnt_blCommentHip", "fld_plain");
            ra.setRequired("KolDeactCmnt_txtCommentHip", true);
            ra.setAttribute("KolDeactCmntLDB", "col_hf_prior_id", ra.getAttribute("KolMortgageLDB", "col_hf_prior_id"));
		}
	}
	
    public void confirm() {
        if (!ra.isRequiredFilled()) {
            return; 
        }
    
        try{
            ra.executeTransaction();//  - CO347.sqlj            
            ra.showMessage("infzstColl08");
        }catch(VestigoTMException vtme){
            ra.showMessage(vtme.getMessageID());
        }
        //ako je screen context 'MortageDeactContext' onda to znaci da se gasila zadnja hipoteka pa 
        //zatvaram ekran liste hipoteka i ekran za unos komentara i razloga deaktivacije tako da se vratimo na listu kolaterala 
        //zato ima dva exitscreen-a. Ako se gasio samo kolateral bez hipoteka onda ce biti drugi kontekst i exitscreen ce 
        //se pozvat samo jednom.
        if(ra.getScreenContext().equalsIgnoreCase("MortageDeactContext")) ra.exitScreen();
        ra.exitScreen();
        //ra.refreshActionList("tblColWorkList");
        ra.refreshActionList("tblColWorkListCellTable");
    }
	
    public boolean KolDeactCmnt_txtReasone_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("KolDeactCmntLDB", "KolDeactCmnt_txtReasoneCode", null);   
            ra.setAttribute("KolDeactCmntLDB", "KolDeactCmnt_txtReasoneDesc", null);
            return true;                                                                                 
        }
        if (!(ra.isLDBExists("SysCodeValueNewLookUpLDB"))) ra.createLDB("SysCodeValueNewLookUpLDB");
        String coll_deact_indic=(String) ra.getAttribute("KolDeactCmntLDB", "coll_deact_indic");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id",  "coll_deact_reason"+coll_deact_indic);
        ra.setAttribute("KolDeactCmntLDB", "dummyBd", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("KolDeactCmntLDB", "KolDeactCmnt_txtReasoneCode", "sys_code_value");
        request.addMapping("KolDeactCmntLDB", "KolDeactCmnt_txtReasoneDesc", "sys_code_desc");
        request.addMapping("KolDeactCmntLDB", "dummyBd", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) { 
            return false;

        }                                                              
        return true;     
    }
        
}  

