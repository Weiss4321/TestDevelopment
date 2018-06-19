package hr.vestigo.modules.collateral.common.yoyM;

import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData.GcmTypeMappingData;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
 

/**
 * Common za mapiranje kategorija/vrsta/podvrsta kolaterala.
 * <p>Za korištenje je potrebno instancirati common s oznakom vrste mapiranja i datumom.
 * Nakon što je common instanciran, pozivima metode resolve dobivaju se stavke na koje su odreðene kategorije/vrste/podvrste mapirane.</p> 
 * @author hrakis
 */
public class YOYM0
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyM/YOYM0.java,v 1.3 2015/04/10 06:42:40 hrakis Exp $";
    
    private RemoteContext rc;
    private YOYM1 yoym1;
    
    private ArrayList<GcmTypeData> gcmTypes;
    private ArrayList<GcmTypeMappingData> gcmTypeMappings;
    
    
    /**
     * Konstruktor commona.
     * @param remoteContext Kontekst u kojem se izvršava common 
     * @param map_code Oznaka vrste mapiranja
     * @param value_date Datum za koji vrijedi mapiranje
     */
    public YOYM0(RemoteContext remoteContext, String map_code, Date value_date) throws Exception
    {
        this.rc = remoteContext;
        this.yoym1 = new YOYM1(remoteContext);
        
        // dohvati sve moguæe stavke na koje se mogu mapirati RBA kategorije/vrste/podvrste kolaterala
        gcmTypes = new ArrayList<GcmTypeData>();
        GcmTypeIterator iterType = yoym1.selectGcmTypes(map_code, value_date);
        while(iterType.next())
        {
            GcmTypeData type = new GcmTypeData();
            type.col_gcm_typ_id = iterType.col_gcm_typ_id();
            type.map_code = iterType.map_code();
            type.ord_no = iterType.ord_no();
            type.code = iterType.code();
            type.name = iterType.name();
            type.name_add = iterType.name_add();
            type.param_value = iterType.param_value();
            type.param_indic = iterType.param_indic();
            gcmTypes.add(type);
        }
        
        // dohvati sva mapiranja i poveži ih sa stavkama
        gcmTypeMappings = new ArrayList<GcmTypeMappingData>();
        GcmTypeMappingIterator iterMapping = yoym1.selectGcmTypeMappings(map_code, value_date);
        while(iterMapping.next())
        {
            GcmTypeData gcmType = getGcmTypeById(iterMapping.col_gcm_typ_id());
            GcmTypeMappingData mapping = gcmType.new GcmTypeMappingData();
            mapping.col_cat_id = iterMapping.col_cat_id();
            mapping.col_typ_id = iterMapping.col_typ_id();
            mapping.col_sub_id = iterMapping.col_sub_id();
            mapping.col_gro_id = iterMapping.col_gro_id();
            mapping.gcmType = gcmType;
            gcmTypeMappings.add(mapping);
        }
        
        // ispiši sva mapiranja
        rc.debug("MAPIRANJE ZA MAP_CODE = " + map_code + " i VALUE_DATE = " + value_date + ":");
        for(GcmTypeMappingData mapping : gcmTypeMappings)
        {
            rc.debug("   COL_CAT_ID = " + mapping.col_cat_id + ", COL_TYP_ID = " + mapping.col_typ_id + ", COL_SUB_ID = " + mapping.col_sub_id + ", COL_GRO_ID = " + mapping.col_gro_id + " -> " + mapping.gcmType.code + " - " + mapping.gcmType.name);
        }
    }

    
    /**
     * Metoda dohvaæa stavku na koju je mapirana zadana RBA kategorija, vrsta i podvrsta kolaterala.
     * Ako kategorija/vrsta/podvrsta nije mapirana, metoda vraæa null.
     * U sluèaju više odgovarajuæih mapiranja, vraæa se specifiènije mapiranje.
     * Npr. ako postoji jedno mapiranje u kojem je navedena samo kategorija i jedno u kojem su navedene kategorija i vrsta, uzet æe se potonje.  
     * @param col_cat_id ID kategorije kolaterala
     * @param col_typ_id ID vrste kolaterala
     * @param col_sub_id ID podvrste kolaterala
     * @return mapirana stavka, null ako ne postoji odgovarajuæe mapiranje
     */
    public GcmTypeData resolve(BigDecimal col_cat_id, BigDecimal col_typ_id, BigDecimal col_sub_id)
    {
        return resolve(col_cat_id, col_typ_id, col_sub_id, null);
    }
    
    
    /**
     * Metoda dohvaæa stavku na koju je mapirana zadana RBA kategorija, vrsta i podvrsta kolaterala.
     * Ako kategorija/vrsta/podvrsta nije mapirana, metoda vraæa null.
     * U sluèaju više odgovarajuæih mapiranja, vraæa se specifiènije mapiranje.
     * Npr. ako postoji jedno mapiranje u kojem je navedena samo kategorija i jedno u kojem su navedene kategorija i vrsta, uzet æe se potonje.  
     * @param col_cat_id ID kategorije kolaterala
     * @param col_typ_id ID vrste kolaterala
     * @param col_sub_id ID podvrste kolaterala
     * @param col_gro_id ID grupe kolaterala
     * @return mapirana stavka, null ako ne postoji odgovarajuæe mapiranje
     */
    public GcmTypeData resolve(BigDecimal col_cat_id, BigDecimal col_typ_id, BigDecimal col_sub_id, BigDecimal col_gro_id)
    {
        int specificity = 0;
        GcmTypeData resolvedType = null;

        for (GcmTypeMappingData mapping : gcmTypeMappings)
        {
            if (specificity < 4 && objectsEqual(mapping.col_cat_id, col_cat_id) && objectsEqual(mapping.col_typ_id, col_typ_id) && objectsEqual(mapping.col_sub_id, col_sub_id) && objectsEqual(mapping.col_gro_id, col_gro_id))
            {
                specificity = 4;
                resolvedType = mapping.gcmType;
            }
            else if (specificity < 3 && objectsEqual(mapping.col_cat_id, col_cat_id) && objectsEqual(mapping.col_typ_id, col_typ_id) && objectsEqual(mapping.col_sub_id, col_sub_id))
            {
                specificity = 3;
                resolvedType = mapping.gcmType;
            }
            else if (specificity < 2 && objectsEqual(mapping.col_cat_id, col_cat_id) && objectsEqual(mapping.col_typ_id, col_typ_id) && mapping.col_sub_id == null)
            {
                specificity = 2;
                resolvedType = mapping.gcmType;
            }
            else if (specificity < 1 && objectsEqual(mapping.col_cat_id, col_cat_id) && mapping.col_typ_id == null && mapping.col_sub_id == null)
            {
                specificity = 1;
                resolvedType = mapping.gcmType;
            }
        }

        return resolvedType;
    }
    
    
    
    /**
     * Metoda ispituje jednakost dva objekta.
     * @param obj1 Prvi objekt
     * @param obj2 Drugi objekt
     * @return true ako su objekti jednaki i nisu null, false inaèe.
     */
    private boolean objectsEqual(Object obj1, Object obj2)
    {
        return (obj1 != null && obj1.equals(obj2));
    }
    
    
    /** Metoda vraæa sve moguæe stavke na koje se mogu mapirati RBA kategorije/vrste/podvrste kolaterala. */
    public ArrayList<GcmTypeData> getGcmTypes()
    {
        return gcmTypes;
    }
    
    /** Metoda vraæa sva mapiranja za trenutnu vrstu mapiranja. */
    public ArrayList<GcmTypeMappingData> getGcmTypeMappings()
    {
        return gcmTypeMappings;
    }
    
    /** Metoda dohvaæa stavku koja ima zadani ID. */
    public GcmTypeData getGcmTypeById(BigDecimal col_gcm_typ_id)
    {
        for(GcmTypeData type : gcmTypes)
        {
            if(type.col_gcm_typ_id.equals(col_gcm_typ_id))
            {
                return type;
            }
        }
        return null;
    }
}