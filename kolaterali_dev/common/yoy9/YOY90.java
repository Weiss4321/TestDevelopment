/*
 * Created on 2007.02.05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy9;

import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.yoy7.CollHeadCusAccNoData;
import hr.vestigo.modules.collateral.common.yoy7.YOY70;
import hr.vestigo.modules.financial.common.yfwL.PostingEngineInputData;
import hr.vestigo.modules.financial.common.yfwL.PostingEngineResponseData;
import hr.vestigo.modules.financial.common.yfwL.YFWL0;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class YOY90 implements CollateralPosting{

	/**
	 * Common za knjiženje kolaterala
	 */
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy9/YOY90.java,v 1.33 2014/11/24 08:09:22 hraskd Exp $";
	
	public RemoteContext rc=null;  
	private YOY91 yoy91 = null;
    private String defaultOrgUnitCode="042"; //default organizacijska jedinica na koju se uvijek knjizi
    BigDecimal defaultOrgUnitId=null;
    
	public YOY90(RemoteContext remoteContext){
		 this.rc=remoteContext;	
	}
    
    public YOY90(){
        
    }
    
    public void setContext(RemoteContext context) throws Exception{
        this.rc=context;
    }
	
	/**Metoda koja otvara partiju i knjizi kolateral 
	 * @param colHeaId - collateral id 
	 * @param deactivateColl - ako se collateral gasi(isknjizuje totalno) postaviti na true, inace postaviti na false 
	 */	
	public void CollPosting(BigDecimal colHeaId, boolean deactivateColl) throws Exception{
		
		YOY9Data dataOneCol = null;
		this.yoy91 = new YOY91(rc);

		try {
			//punjenje objekta potrebnim podacima za otvaranje partije i knjizenje
			dataOneCol = yoy91.fillDataObjectYOY9Data(colHeaId);			
    
			if (dataOneCol!=null){
                rc.debug("YOY9   -   prije checkForNoBooking.");
                System.out.println("YOY9   -   prije checkForNoBooking.");
			    //radi se dodatna provjera  da li se kolateral knjizi jer se neki pod kolaterali u nekoj grupi ne knjize
			    //npr. RBA deposit, polica zivotnog osiguranja lombardni kredit 
			    if (checkForNoBooking(dataOneCol)) return;
                rc.debug("YOY9   -   poslije checkForNoBooking.");
                System.out.println("YOY9   -   poslije checkForNoBooking.");
                
                defaultOrgUnitId=yoy91.getOrgId(defaultOrgUnitCode);
                
				//otvaranje partije za kolateral, 
                //stavljeno da se uvijek partija kolaterala otvara na default-nu organizacisku jedinicu 12.03.2008. hrazst 
                openCusAccNo(dataOneCol.colHeaId, dataOneCol.useOpenId, defaultOrgUnitId);

                //12.03.2008. hrazst
                //override originOrgUniId u objektu sa organizaciskom jedinicom iz customer_accounta. Radi se radi toga da se knjizenje kolaterala
                //uvjek provodi na istu organizacijsku jedinicu na koju se partija i otvorila, jer su partije do 12.03.2008 otvarane na 
                //organizacijsku jedinicu referenta, a od 12.03.2008 na default-nu organizacijsku jedinicu.
                dataOneCol.originOrgUniId=yoy91.getAccountOrgId(dataOneCol.colHeaId);
                                
				//knjizenje kolaterala 
				collBooking(dataOneCol,deactivateColl);	
			}		
		} catch (Exception e) {
			throw e;
		}		
	}
	
	/**	PROVJERAVANJE DA LI SE COLLATERAL NE KNJIZI. KNJIZE SE SVI KOLATERALI KOJI SU POSLANI OSIM CASH DEPOZIT KOJIMA JE VLASNIK RBA,
     *  I ODREDENIH POLICA OSIGURANJA i WEEK LETTER OF COMFORT
	 * @param dataOneCollateral
	 * @return true ako se collateral ne knjizi, inace false
	 * @throws Exception
	 */
	private boolean checkForNoBooking(YOY9Data dataOneCollateral) throws Exception{
	    BigDecimal rbaCusId = new BigDecimal("8218251.0"); //id RBA (konstanta)
	    BigDecimal colCatIdCashDep = new BigDecimal("612223.0"); // cash deposit
        BigDecimal colCatIdInsurance = new BigDecimal("616223.00");  // police osiguranja
        BigDecimal colCatIdGuarantee = new BigDecimal ("615223.00");     // garancije
        
        BigDecimal colTypeIdInsuranceLOMB=new BigDecimal("54777"); 
        BigDecimal colTypeIdInsuranceRS=new BigDecimal("79777");
        BigDecimal colTypeIdGuaranteeWLC=new BigDecimal("35777");
        BigDecimal colTypeIdInsuranceMicro=new BigDecimal("91777"); 
        BigDecimal colTypeIdInsuranceSE=new BigDecimal("93777"); 
        
	    BigDecimal cdeCusId = null;
	    
        rc.debug("dataOneCollateral=" + dataOneCollateral.toString());
        System.out.println("dataOneCollateral=" + dataOneCollateral.toString());
		//PROVJERAVANJE DA LI JE COLLATERAL CASH DEPOZIT, AKO JE PROVJERAVA SE DA LI JE MOZDA RBA CASH DEPOZIT
		//AKO JE RBA CASH DEPOZIT ONDA SE NE KNJIZI (zastavica isRBADeposit se postavlja na true) 
		if(dataOneCollateral.colCatId.compareTo(colCatIdCashDep)==0){
			try {
			    cdeCusId=yoy91.getCashDepozitCdeCusId(dataOneCollateral.colHeaId);
            } catch (Exception e) {
                throw new Exception("Greska u metodi checkRBADeposit pri pokusaju odredivanja getCashDepozitCdeCusId.");
            }		    
			if(cdeCusId!=null && cdeCusId.compareTo(rbaCusId)==0){
			    rc.debug("Kolateral je RBA deposit.");
                System.out.println("Kolateral je RBA deposit.");
			    return true;				
			}
        //PROVJERAVANJE DA LI JE COLLATERAL P0LICA OSIGURANJA, AKO JE ONDA SE PROVJERAVA KOJI JE TIP POLICE OSIGURANJA,
        //JER SE KNJIZI SAMO POLICA ZIVOTNOG OSIGURANJA LOMBARDNI KREDIT(ID=5477) ili RS POLICA OSIGURANJA(79777), A OSTALE SE NE KNJIZE 
		}else if(dataOneCollateral.colCatId.compareTo(colCatIdInsurance)==0){
            rc.debug("Kolateral je polica osiguranja.");
            System.out.println("Kolateral je polica osiguranja.");
		    if(dataOneCollateral.colTypeId.compareTo(colTypeIdInsuranceLOMB)!=0 && dataOneCollateral.colTypeId.compareTo(colTypeIdInsuranceRS)!=0 
		      && dataOneCollateral.colTypeId.compareTo(colTypeIdInsuranceMicro)!=0 && dataOneCollateral.colTypeId.compareTo(colTypeIdInsuranceSE)!=0){
                rc.debug("Kolateral nije lombardni kredit ili RS. ili Micro ili SE ");
                System.out.println("Kolateral nije lombardni kredit ili RS. ili Micro ili SE ");
		        return true;
            }
		    //PROVJERAVANJE DA LI JE COLLATERAL GARANCIJA, AKO JE ONDA SE PROVJERAVA KOJI JE TIP GARANCIJE,
	        //AKO JE WEEK LETTER OF COMFORT ONDA SE NE KNJIZI (zastavica isWeekLetter postavlja se na true) 		    
        } else if (dataOneCollateral.colCatId.compareTo(colCatIdGuarantee)==0) {
            rc.debug("Kolateral je garancija.");
            System.out.println("Kolateral je garancija.");
            if(dataOneCollateral.colTypeId.compareTo(colTypeIdGuaranteeWLC) ==0){
                rc.debug("Kolateral je WEEK Leter of comfort.");
                System.out.println("Kolateral je WEEK Leter of comfort.");
                return true;
            }            
        }
	    return false;
	}
	    
    
	private void openCusAccNo(BigDecimal colHeaId, BigDecimal useOpenId, BigDecimal orgUniId) throws Exception{
		
		YOY70 yoy70=null;
		CollHeadCusAccNoData collHeadCusAccNoData=null;	
		
		//kreiranje data objekta koji se malo dolje koristi za otvaranje partije za collateral
		collHeadCusAccNoData = new CollHeadCusAccNoData();
		
		collHeadCusAccNoData.col_hea_id=colHeaId;
		collHeadCusAccNoData.use_open_id=useOpenId;
		collHeadCusAccNoData.origin_org_uni_id=orgUniId;
						
		//instanciranje objekta za kreiranje partije collaterala i otvaranje partije
		yoy70 = new YOY70(rc,collHeadCusAccNoData);
		try{
			yoy70.insertIntoCustomerAccountFCH();
		}catch(Exception e){
			rc.debug("_______________________________________________________________________________________");
			rc.debug("******* Greska pri kreiranju partije collaterala - insertIntoCustomerAccountFCH *******");
			rc.debug("collHeadCusAccNoData.col_hea_id = "+collHeadCusAccNoData.col_hea_id);
			rc.debug("collHeadCusAccNoData.use_open_id = "+collHeadCusAccNoData.use_open_id);
			rc.debug("collHeadCusAccNoData.origin_org_uni_id = "+collHeadCusAccNoData.origin_org_uni_id);
			rc.debug("_______________________________________________________________________________________");
			throw e;
		}
	}
	
	//deactivateColl - zastavica koja nam govori da li kolateral treba totalno isknjizit true - da , false - ne(radi normalno, knjizenje, isknjizenje
	//sta vec se odredi proracunom). Sa true se forcea totalno isknjizenje
	private void collBooking(YOY9Data dataOneCollateral, boolean deactivateColl) throws Exception{

		//konstante
		BigDecimal colCatIdCashDep = new BigDecimal("612223.0");
		BigDecimal zeroNumber = new BigDecimal("0.00");

		BigDecimal rbaCusId = new BigDecimal("8218251.0"); //id RBA (konstanta)
		BigDecimal hrkCurId = new BigDecimal ("63999.0");
		//kraj konstanti
		
		BigDecimal valueFirstRbaMortgage=zeroNumber;	
		BigDecimal valueBeforeRbaMortgage=zeroNumber; //varijabla u koju se spremaju vrijednosti hipoteka do prve RBA hipoteke
		BigDecimal valueAfterRbaMortgage=zeroNumber;  //varijabla u koju se spremaju vrijednosti hipoteka od prve RBA hipoteke
		
		//varijable za cash depozit
		boolean isRBADeposit = false;//ako je collateral cash deposit zastavica nam govori da li je cash deposit RBA deposit
		boolean vKlauzula=false;
		BigDecimal cdeCusId = null;

		BigDecimal remValue=zeroNumber;			//varijabla za iznos u koju se sprema vrijednost collaterala
		
		//Varijable za odredivanje stanja
		Date maxBalanceDate=null;					//varijabla za odredivanje zadnjeg datuma stanja
	
		Vector balance = new Vector();				//vektor u koji se pohranjuje varijable stanja iz poziva sql-a
    	BigDecimal currentDebitTotal = zeroNumber;	//varijabla za dugovni kumulativ
    	BigDecimal currentCreditTotal = zeroNumber;	//varijabla potrazni kumulativ
    	BigDecimal currentBalance = zeroNumber;		//varijabla za saldo partije na dan maxBalanceDate

    	Vector owner = new Vector();		//vektor gdje su pohranjeni podaci o vlasniku
    	BigDecimal ownerCusId=null;		//owner cus_id
    	String ownerRegisterNo=null;	//owner register_no
    	
    	String currencyNumCode=null;		//code valute 
    	String useOpenAbbreviation=null;	//login korisnika koji je otvorio collateral
    	String orgUniCode=null;				//code ogranizaciske jedinice 
    	    	
    	String status=null;					//status koji se upisuje nakon obrade u financial_flag
    	
    	YOY91.IterMortgage iterMortgage=null; //iterator za hipoteke na kolateralu
    	
		Date todaySQLDate = null;
		GregorianCalendar calendar = new GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new Date(timeT);
				
		PostingEngineInputData postEngInputData = new PostingEngineInputData();
		PostingEngineResponseData postEngRespnse = new PostingEngineResponseData();
		
		try{
		    //ostavio provjeru da li je RBA depozit i ovdje za svaki slucaj
			rc.debug("Vrijednost kolaterala:" + dataOneCollateral.realEstNomiValu);
			//PROVJERAVANJE DA LI JE COLLATERAL CASH DEPOZIT, AKO JE PROVJERAVA SE DA LI JE MOZDA RBA CASH DEPOZIT
			//AKO JE RBA CASH DEPOZIT ONDA SE NE KNJIZI (zastavica isRBADeposit se postavlja na true) 
			if(dataOneCollateral.colCatId.compareTo(colCatIdCashDep)==0){
				cdeCusId=yoy91.getCashDepozitCdeCusId(dataOneCollateral.colHeaId);
				if(cdeCusId!=null && cdeCusId.compareTo(rbaCusId)==0){
					isRBADeposit=true;
					rc.debug("Kolateral je RBA deposit.");
				}
			}
			
			//DOHVAT IZNOSA TUDJIH HIPOTEKA
			iterMortgage=yoy91.getThirdRightsMortgage(dataOneCollateral.colHeaId);
			boolean firstRbaMortgage=false; //zastavica koja odreduje kada se doslo do prve RBA hipoteke
			int i=0; //brojac iteracija
			while(iterMortgage.next()){
			    rc.debug(i+". Iteracija, owner="+iterMortgage.hf_own_cus_id()+" , priority="+iterMortgage.hf_priority()+", amount= "+iterMortgage.amount());
			    if (iterMortgage.hf_own_cus_id().compareTo(rbaCusId)==0 && firstRbaMortgage==false){
			        rc.debug("Prva RBA hipoteka.");
			        valueFirstRbaMortgage=iterMortgage.amount();
			        firstRbaMortgage=true;
			    }			    
			    if (!(iterMortgage.hf_own_cus_id().compareTo(rbaCusId)==0) && !iterMortgage.hf_priority().equals("NA")) {
			        rc.debug("Zbrajam hipoteke.");
				    if (firstRbaMortgage==false){
				        rc.debug("Prije RBA hipoteke");
				        valueBeforeRbaMortgage=valueBeforeRbaMortgage.add(iterMortgage.amount());    
				    }else{
				        rc.debug("Poslje RBA hipoteke");
				        valueAfterRbaMortgage=valueAfterRbaMortgage.add(iterMortgage.amount()); 
				    }
			    }
				rc.debug("Vrijednost hipoteka prije RBA hipoteke:" + valueBeforeRbaMortgage);						
				rc.debug("Vrijednost hipoteka poslije RBA hipoteke:" + valueAfterRbaMortgage);
				i++;
			}
			try { 
			    iterMortgage.close();
			} catch (Exception ignored) { }
			
			//IZNOS COLLATERAL OFFICERA UMANJEN ZA IZNOS TUDJIH HIPOTEKA
			//usporeduje se vrijednost 1 rba hipoteke sa   trzisnom vrijednosti - zbrojem hipoteka prije 1.rba hipoteke i poslje 1.rba hipoteke  
			if (valueFirstRbaMortgage.compareTo(dataOneCollateral.realEstNomiValu.subtract(valueBeforeRbaMortgage.add(valueAfterRbaMortgage)))>=0){
			    remValue = dataOneCollateral.realEstNomiValu.subtract(valueBeforeRbaMortgage);    
			    if (valueFirstRbaMortgage.compareTo(remValue) < 0) {
			    	remValue = valueFirstRbaMortgage;
			    }
			}else{
			    remValue = dataOneCollateral.realEstNomiValu.subtract(valueBeforeRbaMortgage.add(valueAfterRbaMortgage));
			}
			 
			if (remValue.compareTo(zeroNumber) < 0) {
				remValue = zeroNumber;  
			}
			rc.debug("Vrijednost umanjena za tude hipoteke:" + remValue);
			
			//DOHVAT MAX DATUMA U SALDIMA STANJA PARTIJA (CUSACC_BALANCE)
			//AKO SE DATUM DOHVATI ZNACI DA POSTOJI SALDO ZA COLLATERAL I POZIVA SE METODA ZA DOHVAT TIH SALDA
			maxBalanceDate=yoy91.getMaxBalanceDate(dataOneCollateral.colHeaId);
			rc.debug("Vrijednost maxBalanceDate:" + maxBalanceDate);			
			if (maxBalanceDate!=null){
				balance=yoy91.getBalanceState(dataOneCollateral.colHeaId,maxBalanceDate);
				try{
					currentDebitTotal=(BigDecimal) balance.get(0);
					currentCreditTotal=(BigDecimal) balance.get(1);
					currentBalance=(BigDecimal) balance.get(2);
				}catch(Exception e){
					rc.debug("_______________________________________________________________________________________");
					rc.debug("Greska pri postavljanju verijabli iz vektora balance");
					rc.debug("Vektor balance=" + balance.toString());
					rc.debug("Error message = " + e.getMessage());
					rc.debug("_______________________________________________________________________________________");					
					throw e;
				}
				currentBalance = currentBalance.abs();
			}
			rc.debug("Vrijednost currentBalance:" + currentBalance);
			
			owner=yoy91.getCollOwner(dataOneCollateral.colHeaId);
			if (owner!=null){
				ownerCusId = (BigDecimal) owner.get(0);
				ownerRegisterNo = (String) owner.get(1);
			}else{
				rc.debug("Nije pronaðen owner za collateral");
				rc.debug("Nije pronaðen owner za collateral");	
				throw new NullPointerException();
			}
			rc.debug("Register_no vlasnika na kojeg se knjizi:" + ownerRegisterNo);
			rc.debug("Cus_id vlasnika na kojeg se knjizi:" + ownerCusId);
			
			//provjera da li je collateral sa valutnom klauzulom
			vKlauzula=yoy91.checkValutnaKlauzula(dataOneCollateral.colHeaId);
			//ako je valutna klauzula treba vrijednost pretvorit u eure 
			if(vKlauzula){
				if (dataOneCollateral.colCatId.compareTo(colCatIdCashDep)!=0) {
					remValue=yoy91.getValueInEuro(remValue,dataOneCollateral.realEstNmCurId);
			    	dataOneCollateral.realEstNmCurId=new BigDecimal("64999"); //mijenja se vrijednost id-a valute u euro zato jel je kolateral sa VK
					rc.debug("Usao u valutnu klauzulu");
				}
			} 
			

			//DOHVAT CUR CODE ZBOG COMMONA ZA KNJIZENJE
			currencyNumCode=yoy91.getCurrencyNumCode(dataOneCollateral.realEstNmCurId);
			rc.debug("Numericki kod valute kolaterala:" + currencyNumCode);
			 
			//DOHVAT LOGINA KORISNIKA KOJI JE UNIO COLLATERAL
			useOpenAbbreviation=yoy91.getUserAbb(dataOneCollateral.useOpenId);
			rc.debug("useOpenAbbreviation=" + useOpenAbbreviation);
			
			//DOHVAT ORG UNIT CODE-a ZBOG COMMONA ZA KNJIZENJE
			orgUniCode=yoy91.getOrgCode(dataOneCollateral.originOrgUniId);
			
			
			//odlucivanje da li ce se knjizit ili isknjizavat, a ako je 0 onda nema ni knjizenja ni isknjizenja
			//ako je deactivateColl == true ide se na isknjizenje obavezno i postavlja se u vrijednost isknjizenja vrijednost 
			//kolaterala koja se nasla na saldu(currentBalance) 
			if(remValue.compareTo(currentBalance)== -1 || deactivateColl==true){
				//ISKNJIZAVANJE	
			    if (deactivateColl==true){
			        postEngInputData.amount = currentBalance;
			    }else{
			        postEngInputData.amount = currentBalance.subtract(remValue);    
			    }				
				postEngInputData.eventTypeId = new java.math.BigDecimal("1666960003.0");
			} else if(remValue.compareTo(currentBalance)== 1){
				//KNJIZENJE
				postEngInputData.amount = remValue.subtract(currentBalance);
				postEngInputData.eventTypeId = new java.math.BigDecimal("1666879003.0");
			}else{
				postEngInputData.amount = zeroNumber;
			}
			rc.debug("postEngInputData.amount=" + postEngInputData.amount);
			
			postEngInputData.accountCredit =  dataOneCollateral.colNum;
			postEngInputData.accountDebit = dataOneCollateral.colNum;				
			
			postEngInputData.currencyCode = currencyNumCode;
			
			rc.debug("postEngInputData.currencyCode=" + postEngInputData.currencyCode);
			
			postEngInputData.customerCredit = ownerRegisterNo;
			postEngInputData.customerDebit = ownerRegisterNo;
			
			postEngInputData.indicatorOUDomicilCredit = "f";
			postEngInputData.indicatorOUDomicilDebit = "f";
			postEngInputData.organizationUnitDomicilCredit = orgUniCode;
			postEngInputData.organizationUnitDomicilDebit = orgUniCode;
			postEngInputData.organizationUnitInitiator = orgUniCode;			
				
			postEngInputData.paymentData = null;
			postEngInputData.referentIniciatorId=dataOneCollateral.useOpenId;
			postEngInputData.referentIniciator = null; //useOpenAbbreviation - koji se dobije od dataOneCollateral.useOpenId
			postEngInputData.relatedCustomerData = null;
			postEngInputData.reversed = "N";
			postEngInputData.trn = null;
			postEngInputData.turnoverReference = null;
			postEngInputData.valueDate = todaySQLDate;

			//ovdje se ulazi samo ako je iznos za knjizenje/isknjizenje razlicit od nule
			if(postEngInputData.amount.compareTo(zeroNumber)!=0){
			    if(!isRBADeposit){
					YFWL0 objek1 = new YFWL0(rc);
					try{
						postEngRespnse = objek1.execute(postEngInputData);
						if (postEngRespnse.errorCode!=0){
						    rc.debug("KNJIZENJE ZA col_hea_id=" + dataOneCollateral.colHeaId + "zavrseno sa error=" + postEngRespnse.errorCode);
							throw new Exception("KNJIZENJE ZA col_hea_id=" + dataOneCollateral.colHeaId + "nije provedeno zbog error code-a=" + postEngRespnse.errorCode);   
						}
					}catch(Exception e){
						rc.debug("_______________________________________________________________________________________");
						rc.debug("GRESKA za col_hea_id=" + dataOneCollateral.colHeaId + " error=" + e.getMessage());
						rc.debug("GRESKA za col_hea_id=" + dataOneCollateral.colHeaId + " error="  + e.toString());
						rc.debug("_______________________________________________________________________________________");
						throw e;				
					}
					status="1";
					rc.debug("Knjizenje collaterala uspjesno zavrseno.");
					rc.debug("Knjizenje collaterala sa col_hea_id=" + dataOneCollateral.colHeaId + " zavrseno.");
				}else{
					status="X";
					rc.debug("Kolateral se nije knjizio jer je RBA depozit.");
					rc.debug("Knjizenje collaterala sa col_hea_id=" + dataOneCollateral.colHeaId + " se nije izvrsilo jel je RBA depozit.");
				}
					
				//POSTAVI STATUS
				try{
					yoy91.updateFinStatus(dataOneCollateral.colHeaId,status);
				}catch(SQLException sqle){
					rc.debug("_______________________________________________________________________________________");
					rc.debug("Greska pri UPDATE-u statusa za col_hea_id=" + dataOneCollateral.colHeaId);
					rc.debug("_______________________________________________________________________________________");
					throw sqle;
				}
			}else{
				rc.debug("Kolateral se nije knjizio jer je vrijednost za knjizenje '0.00'.\n col_hea_id=" + dataOneCollateral.colHeaId );
			}
		}catch(Exception e){
			rc.debug("_______________________________________________________________________________________");
			rc.debug("******* Greska pri obradi jednog collaterala - collBooking *******");
			rc.debug("colHeaId = "+dataOneCollateral.colHeaId);
			rc.debug("colNum = "+dataOneCollateral.colNum);
			rc.debug("realEstNomiValu = "+dataOneCollateral.realEstNomiValu);
			rc.debug("realEstNmCurId = "+dataOneCollateral.realEstNmCurId);
			rc.debug("useOpenId = "+dataOneCollateral.useOpenId);
			rc.debug("originOrgUniId = "+dataOneCollateral.originOrgUniId);
			rc.debug("colCatId = "+dataOneCollateral.colCatId);
			rc.debug("Error message = "+e.getMessage());		
			rc.debug("_______________________________________________________________________________________");			
			throw e;
		}
	}
	
	/**Provjerava se da li je tip kolaterala koji spada u one kolaterale koji se knjize po valutnoj klauzuli
	 *@param (BigDecimal) id kolaterala (col_cat_id)
	 *@return true ako je za valutnu klauzulu, false inace
	 */
//	private boolean valutnaKlauzula(BigDecimal tip){
//		izbaceno, provjera se radi preko partije
//		Vector collType=new Vector();
//		int i=0; 
//		
//		collType.add(new BigDecimal("618223")); //nekretnine
//		collType.add(new BigDecimal("624223")); //vozila
//		collType.add(new BigDecimal("620223")); //plovila
//		collType.add(new BigDecimal("621223")); //pokretnine		
//		collType.add(new BigDecimal("628223")); //Zlato, dijamanti i plemenite kovine
//		collType.add(new BigDecimal("623223")); // umjetnine
//		collType.add(new BigDecimal("626223")); // zalihe
//		
//		for (i=0;i<collType.size();i++){
//			if (collType.get(i).equals(tip)) return true;
//		}		
//		return false;
//	}
	
	
}
