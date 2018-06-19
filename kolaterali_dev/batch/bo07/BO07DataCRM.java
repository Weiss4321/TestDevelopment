package hr.vestigo.modules.collateral.batch.bo07;
import java.sql.Date;
import java.math.BigDecimal;

public class BO07DataCRM {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo07/BO07DataCRM.java,v 1.2 2007/08/28 12:12:09 hratar Exp $";
	
		private Date dateOfReport;
		private String register_no;
		private String client_name;
		private String invest_party;
		private BigDecimal exposure;
		private BigDecimal cash_deposit;
		private BigDecimal assur_policy;
		private BigDecimal guaranty_state;
		private BigDecimal guaranty_other_bank;
		private BigDecimal housing_object;
		private BigDecimal building_plot;
		private BigDecimal agricultural_plot;
		private BigDecimal indust_hall;
		private BigDecimal office_rooms;
		private BigDecimal hotel;
		private BigDecimal private_accom;
		private BigDecimal stock;
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
		 * @return Returns the agricultural_plot.
		 */
		public BigDecimal getAgricultural_plot() {
			return agricultural_plot;
		}
		/**
		 * @param agricultural_plot The agricultural_plot to set.
		 */
		public void setAgricultural_plot(BigDecimal agricultural_plot) {
			this.agricultural_plot = agricultural_plot;
		}
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
		 * @return Returns the building_plot.
		 */
		public BigDecimal getBuilding_plot() {
			return building_plot;
		}
		/**
		 * @param building_plot The building_plot to set.
		 */
		public void setBuilding_plot(BigDecimal building_plot) {
			this.building_plot = building_plot;
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
		 * @return Returns the hotel.
		 */
		public BigDecimal getHotel() {
			return hotel;
		}
		/**
		 * @param hotel The hotel to set.
		 */
		public void setHotel(BigDecimal hotel) {
			this.hotel = hotel;
		}
		/**
		 * @return Returns the housing_object.
		 */
		public BigDecimal getHousing_object() {
			return housing_object;
		}
		/**
		 * @param housing_object The housing_object to set.
		 */
		public void setHousing_object(BigDecimal housing_object) {
			this.housing_object = housing_object;
		}
		/**
		 * @return Returns the indust_hall.
		 */
		public BigDecimal getIndust_hall() {
			return indust_hall;
		}
		/**
		 * @param indust_hall The indust_hall to set.
		 */
		public void setIndust_hall(BigDecimal indust_hall) {
			this.indust_hall = indust_hall;
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
		 * @return Returns the office_rooms.
		 */
		public BigDecimal getOffice_rooms() {
			return office_rooms;
		}
		/**
		 * @param office_rooms The office_rooms to set.
		 */
		public void setOffice_rooms(BigDecimal office_rooms) {
			this.office_rooms = office_rooms;
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
		 * @return Returns the private_accom.
		 */
		public BigDecimal getPrivate_accom() {
			return private_accom;
		}
		/**
		 * @param private_accom The private_accom to set.
		 */
		public void setPrivate_accom(BigDecimal private_accom) {
			this.private_accom = private_accom;
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
		 * @return Returns the stock.
		 */
		public BigDecimal getStock() {
			return stock;
		}
		/**
		 * @param stock The stock to set.
		 */
		public void setStock(BigDecimal stock) {
			this.stock = stock;
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

}
