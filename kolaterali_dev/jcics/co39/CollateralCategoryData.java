package hr.vestigo.modules.collateral.jcics.co39;

public enum CollateralCategoryData {
	
	/**
	 * Gotovinski depoziti
	 */
	CASH_DEPOSIT(612223),
	/**
	 * Mjenice
	 */
	BILLS(617223),
	/**
	 * Zaduznica
	 */
	DEBENTURE(625223),
	/**
	 * Garancija
	 */
	GUARANTEE(615223),
	/**
	 * Polica osiguranja
	 */
	INSURANCE(616223),
	/**
	 * Nekretnina
	 */
	REAL_ESTATE(618223),
	/**
	 * Vozilo
	 */
	VEHICLE(624223),
	/**
	 * Plovilo
	 */
	VESSEL(620223),
	/**
	 * Pokretnine
	 */
	MOVABLE(621223),
	/**
	 * Prazno
	 */
	NONE(0);
	
	/**
	 * col_cat_id
	 */
	private final int id;
	
	CollateralCategoryData(int ident){
		id=ident;
	}
	
	public boolean equals(int ident){
		return (id==ident);
	}
	
	public int value(){
		return id;
	}
	
	

}
