package hr.vestigo.modules.collateral;

/**
 * @author HRAMKR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
    
import java.math.BigDecimal;
import java.util.Vector;
 
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralCmpUtil;
 
/**
 *
 * PRIKAZ POCETNOG EKRANA I PUNJENJE TABLICE
 */
public class CollSecPaper extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollSecPaper.java,v 1.24 2006/05/20 13:52:50 hramkr Exp $";
	
	private String title = "";
	private String tblname = "";
	private String scrname = "";	

	CollateralCmpUtil coll_cmp_util = null;
	
	public CollSecPaper(ResourceAccessor ra) {
		super(ra);
		coll_cmp_util = new CollateralCmpUtil(ra);
	}
 
// za vrijednosne papire
	
	public void CollSecPaper_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}
		
//		ra.createActionListSession("tblCollSecPaper");
		ra.createActionListSession("tblCollSecPaper", false);
		tblname = "tblCollSecPaper";
		scrname = "CollSecPaperDialog";
		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}	

// za pokretnine i opremu
	

	public void CollMovable_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		
		
		ra.createActionListSession("tblCollMovable", false);
		tblname = "tblCollMovable";
		scrname = "CollMovableDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}	
	
// za zalihe	

	public void CollSupply_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblCollSupply", false);
		tblname = "tblCollSupply";
		scrname = "CollMovableDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}	
 
//	 za umjetnine	

	public void CollArt_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblCollArt", false);
		tblname = "tblCollArt";
		scrname = "CollArtDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 

	
//	 za dragocjenosti	

	public void CollPrec_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblCollPrec", false);
		tblname = "tblCollPrec";
		scrname = "CollPrecDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  

//	 za plovila	

	public void Vessel_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblVessel", false);
		tblname = "tblVessel";
		scrname = "VesselDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  	
  
	
//	 za gotovinske depozite	

	public void CollCashDep_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblCashDep", false);
		tblname = "tblCashDep";
		scrname = "CollCashDepDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  		

//	 za police osiguranja depozite	

	public void CollInsPol_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}

		ra.createActionListSession("tblCollInsPol", false);
		tblname = "tblCollInsPol";
		scrname = "CollInsPolDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  			
	 

//	 za loro garancije	

	public void CollGuarant_SE() { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}
 
		ra.createActionListSession("tblCollGuarant", false);
		tblname = "tblCollGuarant";
		scrname = "CollGuarantDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  			
	 

// zaduznica	
	public void CollLoanStock_SE () { 
		
		if (!(ra.isLDBExists("CollSecPaperLDB"))) {
			ra.createLDB("CollSecPaperLDB");
		}
 
		ra.createActionListSession("tblCollLoanStock", false);
		tblname = "tblCollLoanStock";
		scrname = "CollLoanStockDialog";		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
	 		System.out.println("podvrsta : "+title);			
			ra.setAttribute("CollSecPaperLDB", "CollateralSubCategory", title);
			System.out.println("param : "+param);	
	 		System.out.println("podvrsta : "+title);	
		}	
 
	}		 	  				
	
// unos	
	public void add(){
		//Dizanje ekrana vrijednosnica nakon definicije akcije

		ra.loadScreen(scrname,"scr_change");
		if (title.equalsIgnoreCase("ZALI"))
			ra.setScreenTitle("Zalihe");
		else if (title.equalsIgnoreCase("OBVE"))
			ra.setScreenTitle("Obveznice");
		else if (title.equalsIgnoreCase("DION"))
			ra.setScreenTitle("Dionice");		
		else if (title.equalsIgnoreCase("ZAPI"))
			ra.setScreenTitle("Zapisi");
		else if (title.equalsIgnoreCase("UDJE"))
			ra.setScreenTitle("Udjeli");		

	}
	
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;

		if (td.getData().size() == 0)
			return true;

		return false;
	}
	
	
	public void insu_policy(){
		//POLICA OSIGURANJA NEKRETNINE, VOZILA, PLOVILA... NE KAO KOLATERAL
		//Vessel	
		if (title.equalsIgnoreCase("PLOV")){
			if (isTableEmpty("tblVessel")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblVessel");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);	
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.showMessage("wrnclt0");
				//ra.loadScreen("CollHfPrior","base_vs");
			}
			
			
		}
		//CollPrec
		if (title.equalsIgnoreCase("ZLAT")){
			if (isTableEmpty("tblCollPrec")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollPrec");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.showMessage("wrnclt0");
				//ra.loadScreen("CollHfPrior","base_pr");
			}
			
		}
		if (title.equalsIgnoreCase("UMJE")){
			//CollArt
			if (isTableEmpty("tblCollArt")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollArt");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.showMessage("wrnclt0");
				//ra.loadScreen("CollHfPrior","base_um");
			}
			
			
			
			
			
		}
		if (title.equalsIgnoreCase("ZALI")){
			//CollSupply
			if (isTableEmpty("tblCollSupply")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSupply");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.showMessage("wrnclt0");
				//ra.loadScreen("CollHfPrior","base_za");
			}
		}
		if (title.equalsIgnoreCase("POKR")){
			//CollMovable
			if (isTableEmpty("tblCollMovable")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollMovable");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.showMessage("wrnclt0");
				//ra.loadScreen("CollHfPrior","base_po");
			}
		}
		
		
		
	}//insu_policy
	
	
	
	
// pregled
	public void details(){

			
		if (isTableEmpty(tblname)) {
			ra.showMessage("wrn299");
			return;
		}
		
		TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", tblname);
		Vector hiddenVector = (Vector) td.getSelectedRowUnique();
		BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);		
//		BigDecimal hidden = (BigDecimal) td.getSelectedRowUnique();
		
		Vector row = td.getSelectedRowData();		
		
		BigDecimal col_hea_id = null;
		col_hea_id = hidden;

		ra.loadScreen(scrname,"scr_detail"); 
		
		if (title.equalsIgnoreCase("ZALI"))
			ra.setScreenTitle("Zalihe");
		else if (title.equalsIgnoreCase("OBVE"))
			ra.setScreenTitle("Obveznice");
		else if (title.equalsIgnoreCase("DION"))
			ra.setScreenTitle("Dionice");		
		else if (title.equalsIgnoreCase("ZAPI"))
			ra.setScreenTitle("Zapisi");
		else if (title.equalsIgnoreCase("UDJE"))
			ra.setScreenTitle("Udjeli");		
		
		ra.setAttribute("CollHeadLDB", "COL_HEA_ID", col_hea_id); 
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
		   

		
	}
	

	
// promjena	- obicna, dok je collteral jos u statusu "A"
	public void action(){

		if (isTableEmpty(tblname)) {
			ra.showMessage("wrn299");
			return;
		}
		
		
		TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", tblname);
		Vector hiddenVector = (Vector) td.getSelectedRowUnique();
		BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);		
//		BigDecimal hidden = (BigDecimal) td.getSelectedRowUnique();
		
		Vector row = td.getSelectedRowData();		
		int i =  row.size();

		System.out.println("i : "+i);	
		i--;
		System.out.println("i : "+i);			
		BigDecimal col_hea_id = null;
		col_hea_id = hidden;
		
		String status = null;
		status = (String)row.elementAt(i);

		if (status.equals("A"))
			ra.loadScreen(scrname,"scr_update"); 
		else 
			ra.loadScreen(scrname,"scr_detail"); 
		
		if (title.equalsIgnoreCase("ZALI"))
			ra.setScreenTitle("Zalihe");
		else if (title.equalsIgnoreCase("OBVE"))
			ra.setScreenTitle("Obveznice");
		else if (title.equalsIgnoreCase("DION"))
			ra.setScreenTitle("Dionice");		
		else if (title.equalsIgnoreCase("ZAPI"))
			ra.setScreenTitle("Zapisi");
		else if (title.equalsIgnoreCase("UDJE"))
			ra.setScreenTitle("Udjeli");		
 
		
		ra.setAttribute("CollHeadLDB", "COL_HEA_ID", col_hea_id); 
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 

		if (ra.getScreenContext().equalsIgnoreCase("scr_update")) {
			if (!(ra.isLDBExists("CollOldLDB"))) 
				ra.createLDB("CollOldLDB");
			coll_cmp_util.fillData(ra);
		}
		
	}	

 

	
	public void hypo_fid_con(){
		//HIOPOTEKA NEKRETNINE, VOZILA, PLOVILA... NE KAO KOLATERAL
		//Vessel	
		if (title.equalsIgnoreCase("PLOV")){
			if (isTableEmpty("tblVessel")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblVessel");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_vs");
			}
			
			
		}
		

		//CollPrec
		if (title.equalsIgnoreCase("ZLAT")){
			if (isTableEmpty("tblCollPrec")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollPrec");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_pr");
			}
			
		}


		
		if (title.equalsIgnoreCase("UMJE")){
			
			if (isTableEmpty("tblCollArt")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollArt");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_um");
			}
			
			
			
			
			
		}
		

		if (title.equalsIgnoreCase("ZALI")){
			//CollSupply
			if (isTableEmpty("tblCollSupply")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSupply");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_zh");
			}
		}
		if (title.equalsIgnoreCase("POKR")){
			//CollMovable
			if (isTableEmpty("tblCollMovable")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollMovable");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_po");
			}
		}
		
		
//		Vessel
//		CollPrec
//		CollArt		
//		CollSupply		
//		CollMovable		
		
		
		
		//CollInsPol
		//CollCashDep
		//CollGuarant
		//CollSecPaper
		if (title.equalsIgnoreCase("INSP")){
			//CollInsPol
			//HIPOTEKA NA POLICU OSIGURANJA
			if (isTableEmpty("tblCollInsPol")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollInsPol");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_ip");
			}
		}
		
		if (title.equalsIgnoreCase("CASH")){
			//CollCashDep
			//HIPOTEKA NA CASH DEPOSIT KAO KOLATERAL
			if (isTableEmpty("tblCashDep")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCashDep");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_ca");
			}
		}
		
		
		if (title.equalsIgnoreCase("GARA")){
			//CollGuarant
			//HIPOTEKA NA LORO GARANCIJE KAO KOLATERAL
			if (isTableEmpty("tblCollGuarant")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollGuarant");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_ga");
			}
		}
		
		if (title.equalsIgnoreCase("OBVE")){
			//CollSecPaper
			//HIPOTEKA NA OBVEZNICU KAO KOLATERAL
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSecPaper");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_ob");
			}
		}
		
			
		if (title.equalsIgnoreCase("DION")){
			//CollSecPaper
			//HIPOTEKA NA DIONICU KAO KOLATERAL
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSecPaper");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_di");
			}
		}
		
		if (title.equalsIgnoreCase("ZAPI")){
			//CollSecPaper
			//HIPOTEKA NA ZAPIS KAO KOLATERAL
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSecPaper");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_za");
			}
		}
		
		if (title.equalsIgnoreCase("UDJE")){
			//CollSecPaper
			//HIPOTEKA NA UDJEL KAO KOLATERAL
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			TableData td = (TableData) ra.getAttribute("CollSecPaperLDB", "tblCollSecPaper");
			Vector hiddenVector = (Vector) td.getSelectedRowUnique();
			java.math.BigDecimal collHeaId = (java.math.BigDecimal) hiddenVector.elementAt(0);
			
			if(collHeaId == null){
				ra.showMessage("wrnclt32");
				
			}else{
				ra.loadScreen("CollHfPrior","base_ud");
			}
		}
		
		
		
	
	}//hypo_fid_con	 
	
	public void owners(){
		//Dizanje ekrana vrijednosnica nakon definicije akcije
		if (title.equalsIgnoreCase("PLOV")){
			//PLOVILA
			if (isTableEmpty("tblVessel")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners", "scr_vessel");
		}//PLOV
		if (title.equalsIgnoreCase("INSP")){
			//CollInsPol
			//POLICA OSIGURANJA
			if (isTableEmpty("tblCollInsPol")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners", "scr_insurpo");
		}//INSP
		
		if (title.equalsIgnoreCase("ZLAT")){
			//DRAGOCJENOSTI
			if (isTableEmpty("tblCollPrec")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners", "scr_precious");
		}//ZLAT
		
		if (title.equalsIgnoreCase("UMJE")){
			//UMJETNINE
			if (isTableEmpty("tblCollArt")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners", "scr_workofart");
		}//UMJE
		

		if (title.equalsIgnoreCase("ZALI")){
			//ZALIHE
			if (isTableEmpty("tblCollSupply")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_supply");
		}//ZALI
		
		
		if (title.equalsIgnoreCase("POKR")){
			//POKRETNINE
			if (isTableEmpty("tblCollMovable")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_movable");
		}//POKR
		if (title.equalsIgnoreCase("CASH")){
			//CollCashDep
			//CASH DEPOSIT
			if (isTableEmpty("tblCashDep")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_cashdep");
		}//CASH
		if (title.equalsIgnoreCase("GARA")){
			//CollGuarant
			//LORO GARANCIJA
			if (isTableEmpty("tblCollGuarant")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","csr_lorogar");
		}//GARA
		
		if (title.equalsIgnoreCase("OBVE")){
			//CollSecPaper
			//OBVEZNICA 
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_bond");
		}//OBVE
		if (title.equalsIgnoreCase("DION")){
			//CollSecPaper
			//DIONICA
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_stockshare");
		}//DION
		if (title.equalsIgnoreCase("ZAPI")){
			//CollSecPaper
			//ZAPIS
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_billpap");
		}//ZAPI
		if (title.equalsIgnoreCase("UDJE")){
			//CollSecPaper
			//UDJEL
			if (isTableEmpty("tblCollSecPaper")) {
				ra.showMessage("wrn299");
				return;
			}
			ra.loadScreen("CollOwners","scr_udjeli");
		}//UDJE
		

		
		
		
		//ra.showMessage("wrnclt0");
	}		
	
	public boolean Coll_txtCarrierRegisterNoRE_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollSecPaperLDB";
		java.math.BigDecimal cusId = null;
		java.math.BigDecimal collCusId = null;
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(ldbName,"Coll_txtCarrierRegisterNoRE",null);
			ra.setAttribute(ldbName,"Coll_txtCarrierNameRE",null);
			ra.setAttribute(ldbName,"CUS_ID",null);
			return true;
		}
		
		if (ra.getCursorPosition().equals("Coll_txtCarrierNameRE")) {
			ra.setAttribute(ldbName, "Coll_txtCarrierRegisterNoRE", "");
		} else if (ra.getCursorPosition().equals("Coll_txtCarrierRegisterNoRE")) {
			ra.setAttribute(ldbName, "Coll_txtCarrierNameRE", "");
			//ra.setCursorPosition(2);
		}
		
		
		 
		 
		String d_name = "";
		if (ra.getAttribute(ldbName, "Coll_txtCarrierNameRE") != null){
			d_name = (String) ra.getAttribute(ldbName, "Coll_txtCarrierNameRE");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNoRE") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNoRE");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
			ra.showMessage("wrn366");
			return false;
		}

		
		
		
		//JE LI zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("Coll_txtCarrierRegisterNoRE")) 
			ra.setCursorPosition(2);
					
		
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {
			ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
			ra.setAttribute("CustomerAllLookUpLDB", "code", "");
			ra.setAttribute("CustomerAllLookUpLDB", "name", "");
			ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
			ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
			ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
		} else {
			ra.createLDB("CustomerAllLookUpLDB");
		}

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNoRE"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtCarrierNameRE"));

		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");
	
		try { 
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}  

		ra.setAttribute(ldbName, "CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute(ldbName, "Coll_txtCarrierRegisterNoRE", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "Coll_txtCarrierNameRE", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		//
		
		
		cusId = (java.math.BigDecimal) ra.getAttribute(ldbName, "CUS_ID");
//		collCusId = (java.math.BigDecimal) ra.getAttribute(ldbName, "COLL_CUS_ID");
	//	if((cusId == null) && (collCusId == null)){
		if((cusId == null)) { 
			ra.showMessage("wrnclt2");
			return false;
		}
		
		
		if((cusId != null)){
			ra.refreshActionList(tblname);	
		}
		
		ldbName = null;
		cusId = null;
		collCusId = null;
		return true;
	} //Coll_txtCarrierRegisterNoRE_FV
}//CollSecPaper
