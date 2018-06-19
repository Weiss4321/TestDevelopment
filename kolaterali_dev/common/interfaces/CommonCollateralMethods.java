/**
 * 
 */
package hr.vestigo.modules.collateral.common.interfaces;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import hr.vestigo.modules.rba.interfaces.AbstractCommonInterface;

/**
 * Inteface za pozivanje zajednickih metoda koje se cesto koriste u collateral modulu
 * 
 * @author hraamh
 *
 */
public interface CommonCollateralMethods extends AbstractCommonInterface {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/interfaces/CommonCollateralMethods.java,v 1.2 2008/03/05 09:09:40 hraamh Exp $";
	
	/**
	 * insert u tablicu IN_DATA_DWH_ITEM
	 * 
	 * @param col_pro_id id batcha
	 * @param input_id id ulaznog sloga iz ulazne DWH tabele (null Y)
	 * @param input_code kod ulaznog sloga iz ulazne DWH tabele (null Y)
	 * @param status status zapisa
	 * @param output_id id unesenog sloga
	 * @throws Exception
	 */
	public void insertInDataDwhItem(BigDecimal col_pro_id,BigDecimal input_id, String input_code, String status, BigDecimal output_id) throws Exception;
	
	/**
	 * Dohvat zapisa o obradi u kolateral modulu
	 * 
	 * @param value_date datum valute 
	 * @param proc_type tip obrade
	 * @param status 0-zapoceta obrada, 1-zavrsena obrada
	 * @return id obrade
	 * @throws Exception
	 */
	public BigDecimal selectColProc(Date value_date,String proc_type,String status) throws Exception;
	
	/**
	*
	* Dohvat broja obradenih slogova (bez onih koji nisu upisani zbog gresaka)
	* @param col_pro_id id kolateral obrade
	* @return broj obradenih slogova
	*/
	public BigDecimal getRecordCount(BigDecimal col_pro_id);
	
	/**
	 * Dohvat ID za dani kod valute
	 * 
	 * @param currencyCode kod valute
	 * @return id valute
	 */
	public BigDecimal selectCurrencyIdWithCodeNum(String currencyCode) throws Exception;
	
	/**
	 * Dohvat ID za danu kraticu valute
	 * 
	 * @param currencyCode kratica valute
	 * @return id valute
	 */
	public BigDecimal selectCurrencyIdWithCodeChar(String codeChar) throws Exception;
	
	/**
	 * vraca Id za danu sifru racuna
	*
	*@param acc_no sifra racuna
	*@return id racuna; null ako nema podatka
	*@throws
	*/
	public BigDecimal selectCusAccId(String acc_no) throws Exception;
	
	/**
	 * vraca cus_acc_no za dani cus_acc_id
	*
	*@param cus_acc_id id racuna
	*@return cus_acc_no; null ako nema podatka
	*@throws iznimka u slucaju iznimke 
	*/
	public String selectCusAccNo(BigDecimal cus_acc_id) throws Exception;
	
	/**
	 * Vraca request_no iz tablice cusacc_exposure za dani cus_acc_id
	 * 
	 * @param cus_acc_id id racuna
	 * @return request_no racuna; null  ako nema podatka
	 * @throws
	 */
	public String selectRequestNoFromCusaccExposure(BigDecimal cus_acc_id) throws Exception;
	
	/**
	 * Dohvat sifre tipa kolaterala preko id-a
	 * 
	 * @param col_type_id id tipa kolaterala
	 * @return šifra tipa kolaterala
	 * @throws Exception
	 */
	public String selectCollTypeCodeById(BigDecimal col_type_id)  throws Exception;
	
	/**
	 * 
	 * Generiranje broja kolaterala
	 * 
	 * @param recipe recept
	 * @param date datum
	 * @param prefix prefiks na koji se lijepi generirani broj; za prazno staviti null
	 * @param sufix sufiks koji ide na kraj; za prazno staviti null
	 * @return broj kolaterala
	 * @throws Exception
	 */
	public String getCollNum(String recipe, Date date,String prefix, String sufix) throws Exception;
	
	/**
	*Dohvat srednjeg tecaja za dani datum i id valute
	*
	*@param cur_id id valute
	*@param date datum valute
	*@param bankSign oznaka banke
	*@return srednji tecaj
	*@throws
	*/
	public BigDecimal selectMiddRate(BigDecimal cur_id, Date date,String bankSign) throws Exception;
	
	/**
	 * Zapis pocetka obrade u COL_PROC tablicu. U ulaznoj mapi su potrebni podaci:
	 * <table border=1>
	 * <tr><th>Kljuc 			</th><th> Vrsta 			</th><th> Opis	</th></tr>     
	 * <tr><td>col_pro_id: </td><td> BigDecimal 	</td><td> Id kolateral obrade </td></tr>
	 * <tr><td>proc_date:  </td><td> Date 				</td><td> datum obrade</td></tr>
	 * <tr><td>value_date: </td><td> Date 				</td><td> datum valute</td></tr>
	 * <tr><td>proc_type:  </td><td> String 			</td><td> tip obrade</td></tr>
	 * <tr><td>proc_way:   </td><td> String 			</td><td> nacin pokretanj A- automatski, R-rucno</td></tr>
	 * <tr><td>proc_status:</td><td> String 			</td><td> 0-zapoceto, 1 - zavrseno</td></tr>
	 * <tr><td>col_number: </td><td> BigDecimal 	</td><td> broj obradenih slogova</td></tr>
	 * <tr><td>org_uni_id: </td><td> BigDecimal 	</td><td> org_uni_id</td></tr>
	 * <tr><td>use_id:     </td><td> BigDecimal 	</td><td> use_id</td></tr>
	 * <tr><td>user_lock:  </td><td> Timestamp 	</td><td> user_lock</td></tr>  
	 * </table>
	 * 
	 * @param map mapa s podacima
	 * @return timestamp od userlocka
	 * @throws Exception
	 */
	public Timestamp insertColProc(Map map) throws Exception;
	
	/**
	 * Update zapisa o kolateral obradi. U ulaznoj mapi su potrebni podaci:
	 * <table border=1>
	 * <tr><th>Kljuc 			</th><th> Vrsta 			</th><th> Opis	</th></tr>     
	 * <tr><td>col_pro_id: </td><td> BigDecimal 	</td><td> Id kolateral obrade </td></tr>
	 * <tr><td>proc_date:  </td><td> Date 				</td><td> datum obrade</td></tr>
	 * <tr><td>value_date: </td><td> Date 				</td><td> datum valute</td></tr>
	 * <tr><td>proc_type:  </td><td> String 			</td><td> tip obrade</td></tr>
	 * <tr><td>proc_way:   </td><td> String 			</td><td> nacin pokretanj A- automatski, R-rucno</td></tr>
	 * <tr><td>proc_status:</td><td> String 			</td><td> 0-zapoceto, 1 - zavrseno</td></tr>
	 * <tr><td>col_number: </td><td> BigDecimal 	</td><td> broj obradenih slogova</td></tr>
	 * <tr><td>org_uni_id: </td><td> BigDecimal 	</td><td> org_uni_id</td></tr>
	 * <tr><td>use_id:     </td><td> BigDecimal 	</td><td> use_id</td></tr>
	 * <tr><td>user_lock:  </td><td> Timestamp 	</td><td> user_lock</td></tr>  
	 * </table>
	 * 
	 * @param map mapa s podacima
	 * @return timestamp od userlocka
	 * @throws Exception
	 */
	public Timestamp updateColProc(Map map) throws Exception;
	
	/**
	 * Insert u tablicu evenata
	 * @param eve_typ_id tip dogadaja
	 * @param org_uni_id id OJ-a
	 * @param use_id user
	 * @throws Exception
	 */
	public BigDecimal insertIntoEvent(BigDecimal eve_typ_id,BigDecimal org_uni_id,BigDecimal use_id) throws Exception;
	
	/**
	 * Vraca cus_id za dani register_no i oznaku banke
	 * 
	 * @param register_no register_no
	 * @param bank_sign oznaka banke
	 * @return id komitenta; null  ako nema podatka
	 * @throws
	 */
	public BigDecimal selectCustomerId(String register_no,String bank_sign) throws Exception;
	
	/**
	 * Vraca register_no iz tablice customer za dani cus_id i oznaku banke
	 * 
	 * @param cus_id id komitenta
	 * @param bank_sign oznaka banke
	 * @return register_no komitenta; null  ako nema podatka
	 * @throws
	 */
	public String selectCusRegNo(BigDecimal cus_id,String bank_sign) throws Exception;

}


