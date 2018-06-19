//created 2009.01.19
package hr.vestigo.modules.collateral.batch.bo20;

/**
 *
 * @author hraamh
 */
public enum ProcessFileEnum {
    
    
    KREDITI("COLL_IN_KR_SME"),
    GARANCIJE("COLL_IN_GAR_SME"),
    AKREDITIVI("COLL_IN_AKR_SME"),
    OVERDRAFT("COLL_IN_OVD_SME"),
    KREDITNE_KARTICE("COLL_IN_KKR_SME"),
    NEPOZNATO("");
    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo20/ProcessFileEnum.java,v 1.3 2009/02/04 14:30:07 hraamh Exp $";
    
    private final int key_in;
    private final int key_out;
    
    private final String name_in;
    private final String name_out;
    private final String proc_type;
    
    ProcessFileEnum(String in_name){ 
        name_in=in_name;
        if(in_name!=null){
            name_out=name_in.replaceAll("_IN", "_OUT");
        }else{
            name_out=null;
        }
        if ("COLL_IN_KR_SME".compareToIgnoreCase(in_name)==0){
            key_in=130;
            key_out=140;
            proc_type="CL0";
        }else if ("COLL_IN_GAR_SME".compareToIgnoreCase(in_name)==0){
            key_in=131;
            key_out=141;
            proc_type="CL1";
        }else if ("COLL_IN_AKR_SME".compareToIgnoreCase(in_name)==0){
            key_in=132;
            key_out=142;
            proc_type="CL2";
        }else if ("COLL_IN_OVD_SME".compareToIgnoreCase(in_name)==0){
            key_in=133;
            key_out=143;
            proc_type="CL3";
        }else if ("COLL_IN_KKR_SME".compareToIgnoreCase(in_name)==0){
            key_in=134;
            key_out=144;
            proc_type="CL4";
        }else{
            key_in=0;
            key_out=0;
            proc_type="CLE";
        }
        
    }
    
    public String toOutCode(){
        return ""+ key_out;
    }

    public String toInCode(){
        return ""+ key_in;
    }
    
    public String toInName(){
        return name_in;
    }
    
    public String toOutName(){
        return name_out;
    }
    
    public String toProcType(){
        return proc_type;
    }
    
    public static ProcessFileEnum make(String in_name){
        if ("COLL_IN_KR_SME".compareToIgnoreCase(in_name)==0){
            return KREDITI;
        }else if ("COLL_IN_GAR_SME".compareToIgnoreCase(in_name)==0){
            return GARANCIJE;
        }else if ("COLL_IN_AKR_SME".compareToIgnoreCase(in_name)==0){
           return AKREDITIVI;
        }else if ("COLL_IN_OVD_SME".compareToIgnoreCase(in_name)==0){
            return OVERDRAFT;
        }else if ("COLL_IN_KKR_SME".compareToIgnoreCase(in_name)==0){
            return KREDITNE_KARTICE;
        }else{
            return NEPOZNATO;
        }
    }
}

