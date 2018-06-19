package hr.vestigo.modules.collateral.jcics.co39;

import java.math.BigDecimal;
import java.util.HashSet;

public class CacheMemoryData {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/CacheMemoryData.java,v 1.1 2008/05/21 11:39:09 hraamh Exp $";
	
	private HashSet<Long> collateral=null;
	private HashSet<Long> hf_prior=null;
	private HashSet<Long> owner=null;
	private HashSet<Long> frame_agreement=null;
	
	//tipovi kolaterala
	private HashSet<Long> bills=null;
	private HashSet<Long> cash_deposit=null;
	private HashSet<Long> debenture=null;
	private HashSet<Long> guarantee=null;
	private HashSet<Long> insurance=null;
	private HashSet<Long> real_estate=null;
	private HashSet<Long> vehicle=null;
	private HashSet<Long> vessel=null;
	
	public CacheMemoryData(){
		collateral= new HashSet<Long>();
		hf_prior= new HashSet<Long>();
		owner= new HashSet<Long>();
		frame_agreement= new HashSet<Long>();		
		bills= new HashSet<Long>();
		cash_deposit= new HashSet<Long>();
		debenture= new HashSet<Long>();
		guarantee= new HashSet<Long>();
		insurance= new HashSet<Long>();
		real_estate= new HashSet<Long>();
		vehicle= new HashSet<Long>();
		vessel= new HashSet<Long>();
	}
	
	public void putCollateral(long col_hea_id){
		collateral.add(col_hea_id);
	}
	
	public boolean hasCollateral(long col_hea_id){
		return collateral.contains(col_hea_id);
	}
	
	public void putHfPriority(long hf_prior_id){
		hf_prior.add(hf_prior_id);
	}
	
	public boolean hasHfPriority(long hf_prior_id){
		return hf_prior.contains(hf_prior_id);
	}
	
	public void putOwner(long col_hea_id){
		owner.add(col_hea_id);
	}
	
	public boolean hasOwner(long col_hea_id){
		return owner.contains(col_hea_id);
	}
	
	public void putFrameAgreement(long fra_agr_id){
		frame_agreement.add(fra_agr_id);
	}
	
	public boolean hasFrameAgreement(long fra_agr_id){
		return frame_agreement.contains(fra_agr_id);
	}

}
