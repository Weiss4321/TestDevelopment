/*
 * Created on 2007.03.12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo07;
import java.sql.Date;
import java.math.BigDecimal;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO07CRMData {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo07/BO07CRMData.java,v 1.3 2007/09/05 13:05:19 hratar Exp $";
	 
	private Date dateOfReport;
	private String register_no;
	private String client_name;
	private String invest_party;
	private BigDecimal exposure;
	private BigDecimal cash_deposit;
	private BigDecimal assur_policy;
	private BigDecimal guaranty_state;
	private BigDecimal guaranty_other_bank;
	private BigDecimal real_estate1;
	private BigDecimal real_estate2;
	private BigDecimal real_estate3;
	private BigDecimal real_estate4;
	private BigDecimal real_estate5;
	private BigDecimal real_estate6;
	private BigDecimal real_estate7;
	private BigDecimal real_estate8;
	private BigDecimal real_estate9;
	private BigDecimal real_estate10;
	private BigDecimal priv_cars;
	private BigDecimal other_veh;
	private BigDecimal trucks_etc;
	private BigDecimal building_veh;
	private BigDecimal vessels;
	private BigDecimal airplanes;
	private BigDecimal machines;
	private BigDecimal insured;
	private BigDecimal not_insured;
	
	/**
	 * @return Returns the airplanes.
	 */
	public BigDecimal getAirplanes() {
		return airplanes;
	}
	/**
	 * @param airplanes The airplanes to set.
	 */
	public void setAirplanes(BigDecimal airplanes) {
		this.airplanes = airplanes;
	}
	/**
	 * @return Returns the assur_policy.
	 */
	public BigDecimal getAssur_policy() {
		return assur_policy;
	}
	/**
	 * @param assur_policy The assur_policy to set.
	 */
	public void setAssur_policy(BigDecimal assur_policy) {
		this.assur_policy = assur_policy;
	}
	
	/**
	 * @return Returns the building_veh.
	 */
	public BigDecimal getBuilding_veh() {
		return building_veh;
	}
	/**
	 * @param building_veh The building_veh to set.
	 */
	public void setBuilding_veh(BigDecimal building_veh) {
		this.building_veh = building_veh;
	}
	/**
	 * @return Returns the cash_deposit.
	 */
	public BigDecimal getCash_deposit() {
		return cash_deposit;
	}
	/**
	 * @param cash_deposit The cash_deposit to set.
	 */
	public void setCash_deposit(BigDecimal cash_deposit) {
		this.cash_deposit = cash_deposit;
	}
	/**
	 * @return Returns the client_name.
	 */
	public String getClient_name() {
		return client_name;
	}
	/**
	 * @param client_name The client_name to set.
	 */
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	/**
	 * @return Returns the dateOfReport.
	 */
	public Date getDateOfReport() {
		return dateOfReport;
	}
	/**
	 * @param dateOfReport The dateOfReport to set.
	 */
	public void setDateOfReport(Date dateOfReport) {
		this.dateOfReport = dateOfReport;
	}
	/**
	 * @return Returns the exposure.
	 */
	public BigDecimal getExposure() {
		return exposure;
	}
	/**
	 * @param exposure The exposure to set.
	 */
	public void setExposure(BigDecimal exposure) {
		this.exposure = exposure;
	}
	/**
	 * @return Returns the guaranty_other_bank.
	 */
	public BigDecimal getGuaranty_other_bank() {
		return guaranty_other_bank;
	}
	/**
	 * @param guaranty_other_bank The guaranty_other_bank to set.
	 */
	public void setGuaranty_other_bank(BigDecimal guaranty_other_bank) {
		this.guaranty_other_bank = guaranty_other_bank;
	}
	/**
	 * @return Returns the guaranty_state.
	 */
	public BigDecimal getGuaranty_state() {
		return guaranty_state;
	}
	/**
	 * @param guaranty_state The guaranty_state to set.
	 */
	public void setGuaranty_state(BigDecimal guaranty_state) {
		this.guaranty_state = guaranty_state;
	}
	
	
	/**
	 * @return Returns the insured.
	 */
	public BigDecimal getInsured() {
		return insured;
	}
	/**
	 * @param insured The insured to set.
	 */
	public void setInsured(BigDecimal insured) {
		this.insured = insured;
	}
	/**
	 * @return Returns the invest_party.
	 */
	public String getInvest_party() {
		return invest_party;
	}
	/**
	 * @param invest_party The invest_party to set.
	 */
	public void setInvest_party(String invest_party) {
		this.invest_party = invest_party;
	}
	/**
	 * @return Returns the machines.
	 */
	public BigDecimal getMachines() {
		return machines;
	}
	/**
	 * @param machines The machines to set.
	 */
	public void setMachines(BigDecimal machines) {
		this.machines = machines;
	}
	/**
	 * @return Returns the not_insured.
	 */
	public BigDecimal getNot_insured() {
		return not_insured;
	}
	/**
	 * @param not_insured The not_insured to set.
	 */
	public void setNot_insured(BigDecimal not_insured) {
		this.not_insured = not_insured;
	}
	/**
	 * @return Returns the other_veh.
	 */
	public BigDecimal getOther_veh() {
		return other_veh;
	}
	/**
	 * @param other_veh The other_veh to set.
	 */
	public void setOther_veh(BigDecimal other_veh) {
		this.other_veh = other_veh;
	}
	/**
	 * @return Returns the priv_cars.
	 */
	public BigDecimal getPriv_cars() {
		return priv_cars;
	}
	/**
	 * @param priv_cars The priv_cars to set.
	 */
	public void setPriv_cars(BigDecimal priv_cars) {
		this.priv_cars = priv_cars;
	}
	
	/**
	 * @return Returns the register_no.
	 */
	public String getRegister_no() {
		return register_no;
	}
	/**
	 * @param register_no The register_no to set.
	 */
	public void setRegister_no(String register_no) {
		this.register_no = register_no;
	}
	
	/**
	 * @return Returns the trucks_etc.
	 */
	public BigDecimal getTrucks_etc() {
		return trucks_etc;
	}
	/**
	 * @param trucks_etc The trucks_etc to set.
	 */
	public void setTrucks_etc(BigDecimal trucks_etc) {
		this.trucks_etc = trucks_etc;
	}
	/**
	 * @return Returns the vessels.
	 */
	public BigDecimal getVessels() {
		return vessels;
	}
	/**
	 * @param vessels The vessels to set.
	 */
	public void setVessels(BigDecimal vessels) {
		this.vessels = vessels;
	}
	public void setAllAmountToZero() {
		cash_deposit = new BigDecimal(0);
		assur_policy = new BigDecimal(0);
		guaranty_state = new BigDecimal(0);
		guaranty_other_bank = new BigDecimal(0);
		real_estate1 = new BigDecimal(0);
		real_estate2 = new BigDecimal(0);
		real_estate3 = new BigDecimal(0);
		real_estate4 = new BigDecimal(0);
		real_estate5 = new BigDecimal(0);
		real_estate6 = new BigDecimal(0);
		real_estate7 = new BigDecimal(0);
		real_estate8 = new BigDecimal(0);
		real_estate9 = new BigDecimal(0);
		real_estate10 = new BigDecimal(0);
		priv_cars = new BigDecimal(0);
		other_veh = new BigDecimal(0);
		trucks_etc = new BigDecimal(0);
		building_veh = new BigDecimal(0);
		vessels = new BigDecimal(0);
		airplanes = new BigDecimal(0);
		machines = new BigDecimal(0);
		
	
	
	}
	/**
	 * @return Returns the real_estate1.
	 */
	public BigDecimal getReal_estate1() {
		return real_estate1;
	}
	/**
	 * @param real_estate1 The real_estate1 to set.
	 */
	public void setReal_estate1(BigDecimal real_estate1) {
		this.real_estate1 = real_estate1;
	}
	/**
	 * @return Returns the real_estate10.
	 */
	public BigDecimal getReal_estate10() {
		return real_estate10;
	}
	/**
	 * @param real_estate10 The real_estate10 to set.
	 */
	public void setReal_estate10(BigDecimal real_estate10) {
		this.real_estate10 = real_estate10;
	}
	/**
	 * @return Returns the real_estate2.
	 */
	public BigDecimal getReal_estate2() {
		return real_estate2;
	}
	/**
	 * @param real_estate2 The real_estate2 to set.
	 */
	public void setReal_estate2(BigDecimal real_estate2) {
		this.real_estate2 = real_estate2;
	}
	/**
	 * @return Returns the real_estate3.
	 */
	public BigDecimal getReal_estate3() {
		return real_estate3;
	}
	/**
	 * @param real_estate3 The real_estate3 to set.
	 */
	public void setReal_estate3(BigDecimal real_estate3) {
		this.real_estate3 = real_estate3;
	}
	/**
	 * @return Returns the real_estate4.
	 */
	public BigDecimal getReal_estate4() {
		return real_estate4;
	}
	/**
	 * @param real_estate4 The real_estate4 to set.
	 */
	public void setReal_estate4(BigDecimal real_estate4) {
		this.real_estate4 = real_estate4;
	}
	/**
	 * @return Returns the real_estate5.
	 */
	public BigDecimal getReal_estate5() {
		return real_estate5;
	}
	/**
	 * @param real_estate5 The real_estate5 to set.
	 */
	public void setReal_estate5(BigDecimal real_estate5) {
		this.real_estate5 = real_estate5;
	}
	/**
	 * @return Returns the real_estate6.
	 */
	public BigDecimal getReal_estate6() {
		return real_estate6;
	}
	/**
	 * @param real_estate6 The real_estate6 to set.
	 */
	public void setReal_estate6(BigDecimal real_estate6) {
		this.real_estate6 = real_estate6;
	}
	/**
	 * @return Returns the real_estate7.
	 */
	public BigDecimal getReal_estate7() {
		return real_estate7;
	}
	/**
	 * @param real_estate7 The real_estate7 to set.
	 */
	public void setReal_estate7(BigDecimal real_estate7) {
		this.real_estate7 = real_estate7;
	}
	/**
	 * @return Returns the real_estate8.
	 */
	public BigDecimal getReal_estate8() {
		return real_estate8;
	}
	/**
	 * @param real_estate8 The real_estate8 to set.
	 */
	public void setReal_estate8(BigDecimal real_estate8) {
		this.real_estate8 = real_estate8;
	}
	/**
	 * @return Returns the real_estate9.
	 */
	public BigDecimal getReal_estate9() {
		return real_estate9;
	}
	/**
	 * @param real_estate9 The real_estate9 to set.
	 */
	public void setReal_estate9(BigDecimal real_estate9) {
		this.real_estate9 = real_estate9;
	}
}
