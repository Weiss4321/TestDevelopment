package hr.vestigo.modules.collateral.batch.bo56;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo56.BO561.Iter1;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0.Uvjet;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * Batch obrada za ažuriranje prihvatljivosti.
 * @author hrakis
 */
public class BO560 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo56/BO560.java,v 1.5 2015/06/11 07:16:27 hrakis Exp $";
    
    private String[] kategorije;
    private String[] prihvatljivosti;
    private boolean zapisatiPromjene;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO560 pokrenut.");
        BO561 bo561 = new BO561(bc);
        
        // ubacivanje eventa
        BigDecimal eve_id = bo561.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");

        // inicijaliziraj common za ažuriranje prihvatljivosti
        YOYG0 yoyG0 = new YOYG0(bc, null);
        yoyG0.setSpremiPromjene(zapisatiPromjene);
        
        // postavi koje se prihvatljivosti odreðuju
        if(!(prihvatljivosti.length == 1 && prihvatljivosti[0].equalsIgnoreCase("SVE")))
        {
            yoyG0.setOdrediCRMHNBMisljenje(false);
            yoyG0.setOdrediHNBPrihvatljivost(false);
            yoyG0.setOdrediB2StandPrihvatljivost(false);
            yoyG0.setOdrediB2IRBPrihvatljivost(false);
            yoyG0.setOdrediNDPrihvatljivost(false);
            for(int b = 0; b < prihvatljivosti.length; b++)
            {
                if(prihvatljivosti[b].equalsIgnoreCase("CRMHNB")) yoyG0.setOdrediCRMHNBMisljenje(true);
                else if(prihvatljivosti[b].equalsIgnoreCase("HNB")) yoyG0.setOdrediHNBPrihvatljivost(true);
                else if(prihvatljivosti[b].equalsIgnoreCase("B2STAND")) yoyG0.setOdrediB2StandPrihvatljivost(true);
                else if(prihvatljivosti[b].equalsIgnoreCase("B2IRB")) yoyG0.setOdrediB2IRBPrihvatljivost(true);
                else if(prihvatljivosti[b].equalsIgnoreCase("ND")) yoyG0.setOdrediNDPrihvatljivost(true);
            }
        }
        
        // stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String fileName = dir + "AzuriranePrihvatljivosti" + dateString + ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);
        
        streamWriter.write(getHeaderRow());
       
        // za svaku traženu kategoriju dohvati kolaterale
        for(int a = 0; a < kategorije.length; a++)
        {
            String col_cat_name = kategorije[a].trim();
            bc.debug("---------------------- Kategorija: " +  col_cat_name + " ----------------------");
            Iter1 iter = bo561.selectData(col_cat_name);
            if(iter == null) return RemoteConstants.RET_CODE_ERROR;
            
            // za svaki dohvaæeni kolateral ažuriraj prihvatljivosti
            while(iter.next())
            {
                bc.debug("COL_HEA_ID = " + iter.col_hea_id());
                bc.startStopWatch("yoyG0.azurirajPrihvatljivosti_" + col_cat_name);
                
                yoyG0.setColHeaId(iter.col_hea_id());
                boolean jeLiBiloPromjena = yoyG0.azurirajPrihvatljivosti();
                
                bc.stopStopWatch("yoyG0.azurirajPrihvatljivosti_" + col_cat_name);
                
                ispisUvjeta(yoyG0, bc);
                if(jeLiBiloPromjena)
                {
                    bc.debug("...stare i nove prihvatljivosti se razlikuju");
                    streamWriter.write(getDetailsRow(yoyG0));
                }
            }
        }
        
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u izlaznu datoteku.");

        // slanje maila
        bc.startStopWatch("sendMail");
        YXY70.send(bc, "csv250", bc.getLogin(), fileName);
        bc.stopStopWatch("sendMail");
        bc.debug("Mail poslan.");

        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }


    /** Metoda ispisuje u log sve izraèunate prihvatljivosti i kriterije na temelju kojih su se izraèunale prihvatljivosti.
     * @param yoyG0 objekt commona
     * @param bc batch context
     */
    private void ispisUvjeta(YOYG0 yoyG0, BatchContext bc)
    {
        bc.startStopWatch("ispisUvjeta");
        if(yoyG0.getOdrediCRMHNBMisljenje())
        {
            bc.debug("...CRM HNB misljenje: " + yoyG0.getStaroCRMHNBMisljenje() + " -> " + yoyG0.getCRMHNBMisljenje() + mapToString(yoyG0.getUvjetiZaCRMHNB()));
        }
        if(yoyG0.getOdrediHNBPrihvatljivost())
        {
            bc.debug("...HNB prihvatljivost: " + yoyG0.getStaraHNBPrihvatljivost() + " -> " + yoyG0.getHNBPrihvatljivost() + mapToString(yoyG0.getUvjetiZaHNB()));
        }
        if(yoyG0.getOdrediB2StandPrihvatljivost())
        {
            bc.debug("...B2 Stand prihvatljivost: " + yoyG0.getStaraB2StandPrihvatljivost() + " -> " + yoyG0.getB2StandPrihvatljivost() + mapToString(yoyG0.getUvjetiZaB2Stand()));
        }
        if(yoyG0.getOdrediB2IRBPrihvatljivost())
        {
            bc.debug("...B2 IRB prihvatljivost: " + yoyG0.getStaraB2IRBPrihvatljivost() + " -> " + yoyG0.getB2IRBPrihvatljivost() + mapToString(yoyG0.getUvjetiZaB2IRB()));
        }
        if(yoyG0.getOdrediNDPrihvatljivost())
        {
            bc.debug("...ND prihvatljivost: " + yoyG0.getStaraNDPrihvatljivost() + " -> " + yoyG0.getNDPrihvatljivost() + mapToString(yoyG0.getUvjetiZaND()));
        }
        bc.stopStopWatch("ispisUvjeta");
    }
    
    /** Zadanu kolekciju parova kriterij/ispunjenost pretvara u tekst pogodan za ispis. */
    private String mapToString(LinkedHashMap<Uvjet,Boolean> map)
    {
        StringBuilder sb = new StringBuilder(" (");
        if (map != null)
        {
            for (Iterator<Uvjet> iter = map.keySet().iterator(); iter.hasNext(); )
            {
                Uvjet uvjet = iter.next();
                Boolean vrijednost = map.get(uvjet);
                sb.append(", ");
                sb.append(uvjet.toString());
                sb.append(":");
                sb.append(vrijednost.equals(Boolean.TRUE) ? "D" : "N");
            }
        }
        sb.append(")");
        return sb.toString().replaceFirst(", ", "");
    }
    
    /**
     * Metoda formira zaglavlje izlazne datoteke i vraæa ga u obliku stringa.
     */
    private String getHeaderRow() throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Stare prihvatljivosti").append(";");
        buffer.append("CRM HNB mi\u0161ljenje").append(";");
        buffer.append("HNB").append(";");
        buffer.append("B2STAND").append(";");
        buffer.append("B2IRB").append(";");
        buffer.append("ND").append(";");
        buffer.append("Nove prihvatljivosti").append(";");
        buffer.append("CRM HNB mi\u0161ljenje").append(";");
        buffer.append("HNB").append(";");
        buffer.append("B2STAND").append(";");
        buffer.append("B2IRB").append(";");
        buffer.append("ND").append(";");
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda formira jedan red izlazne datoteke i vraæa ga u obliku stringa.
     */
    private String getDetailsRow(YOYG0 yoyG0) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(yoyG0.getColNum()).append(";");
        buffer.append("").append(";");
        buffer.append(yoyG0.getStaroCRMHNBMisljenje()).append(";");
        buffer.append(yoyG0.getStaraHNBPrihvatljivost()).append(";");
        buffer.append(yoyG0.getStaraB2StandPrihvatljivost()).append(";");
        buffer.append(yoyG0.getStaraB2IRBPrihvatljivost()).append(";");
        buffer.append(yoyG0.getStaraNDPrihvatljivost()).append(";");
        buffer.append("").append(";");
        buffer.append(yoyG0.getCRMHNBMisljenje()).append(";");
        buffer.append(yoyG0.getHNBPrihvatljivost()).append(";");
        buffer.append(yoyG0.getB2StandPrihvatljivost()).append(";");
        buffer.append(yoyG0.getB2IRBPrihvatljivost()).append(";");
        buffer.append(yoyG0.getNDPrihvatljivost()).append(";");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku: RB kategorije prihvatljivosti zapisatiPromjene
     * Parametar "kategorije" predstavlja popis svih kategorija kolaterala (odvojenih zarezom) za koje se vrši obrada. Moguæe je predati vrijednost "SVE".
     * Parametar "prihvatljivosti" predstavlja popis svih prihvatljivosti (odvojenih zarezom) za koje se vrši obrada. Moguæe vrijednosti su CRMHNB,HNB,B2STAND,B2IRB,ND ili SVE.
     * Parametar "zapisatiPromjene" predstavlja oznaku da li da se izraèunate vrijednosti zapišu u bazu podataka. Moguæe vrijednosti su D ili N.
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 4)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                kategorije = bc.getArg(1).trim().split(",");
                prihvatljivosti = bc.getArg(2).trim().split(",");
                zapisatiPromjene = bc.getArg(3).trim().equalsIgnoreCase("D") ? true : false;
            }
            else throw new Exception("Neispravan broj parametara!");
        }
        catch(Exception ex)
        {
            bc.error("Neispravno zadani parametri!", ex);
            return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("3751692234"));
        batchParameters.setArgs(args);
        new BO560().run(batchParameters);
    }
}