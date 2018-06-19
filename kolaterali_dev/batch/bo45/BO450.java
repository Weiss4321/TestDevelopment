package hr.vestigo.modules.collateral.batch.bo45;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo45.BO451.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * CRM izvješæe
 * @author hrakis
 */
public class BO450 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo45/BO450.java,v 1.27 2014/03/10 13:29:56 hrakis Exp $";
    private BatchContext bc;
    private BO451 bo451;
    private Date report_date;
    private String register_no, client_type, ponder, eligibility;
    private BigDecimal col_pro_id;
    private BigDecimal zero = new BigDecimal(0);
    private HashMap frameAccExceptions;
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        bc.debug("BO450 pokrenut.");
        this.bc = bc;
        bo451 = new BO451(bc);
        
        // ubacivanje eventa
        BigDecimal eve_id = bo451.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        boolean ponded = "P".equals(ponder);
        bc.debug("Parametri dohvaceni.");
        
        // dohvat ID obrade izraèuna pokrivenosti
        col_pro_id = bo451.selectColProId(report_date, ponder, eligibility);
        if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("COL_PRO_ID = " + col_pro_id);
        
        // stvaranje izlaznih datoteka
        String dir = bc.getOutDir() + "/";
        String mod = "CUS";
        if("P".equals(client_type)) mod = "CO"; else if("F".equals(client_type)) mod = "RET";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(report_date);
        String pond = ponded ? "pond" : "nepond";
        String fileNameCore = dir + mod + "_" + dateString + "_" + eligibility + "_" + pond + "<suffix><suffix2>.csv";
        
        if(eligibility.endsWith("GK")) fileNameCore = fileNameCore.replaceFirst("<suffix2>", "_FINAL");
        else if(eligibility.endsWith("MICRO")) fileNameCore = fileNameCore.replaceFirst("<suffix2>", "_MICRO");
        else fileNameCore = fileNameCore.replaceFirst("<suffix2>", "");
        
        String fileNameSynthetic = fileNameCore.replaceFirst("<suffix>", "_sintetika");
        String fileNameAnalytic = null;
        if(ponded) fileNameAnalytic = fileNameCore.replaceFirst("<suffix>", "_analitika");
        String fileNameFrame = fileNameCore.replaceFirst("<suffix>", "_partijeIzOkvira");

        OutputStreamWriter streamWriterSynthetic = new OutputStreamWriter(new FileOutputStream(new File(fileNameSynthetic)), "Cp1250");
        OutputStreamWriter streamWriterAnalytic = null;
        if(ponded) streamWriterAnalytic = new OutputStreamWriter(new FileOutputStream(new File(fileNameAnalytic)), "Cp1250");
        OutputStreamWriter streamWriterFrame = new OutputStreamWriter(new FileOutputStream(new File(fileNameFrame)), "Cp1250");
        bc.debug("Stvorene izlazne datoteke:");
        bc.debug("Datoteka za sintetiku: " + fileNameSynthetic);
        if(fileNameAnalytic != null) bc.debug("Datoteka za analitiku: " + fileNameAnalytic);
        bc.debug("Datoteka za partije iz okvira: " + fileNameFrame);

        // zapisivanje zaglavlja u izlazne datoteke
        streamWriterSynthetic.write(getHeaderSynthetic(false));
        if(ponded) streamWriterAnalytic.write(getHeaderAnalytic());
        streamWriterFrame.write(getHeaderSynthetic(true));
        bc.debug("Zapisana zaglavlja.");
        
        // dohvat partija iz okvira koje se mogu vezati i samostalno
        frameAccExceptions = bo451.selectFrameAccExceptions();
        if(frameAccExceptions == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvacene partije iz okvira koje se mogu vezati i samostalno.");

        // dohvat podataka
        Iter1 iter = bo451.selectData(col_pro_id, register_no, client_type, eligibility);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci.");
        
        // zapisivanje podataka u analitièko i sintetièko izvješæe
        if(!writeData(iter, ponded, streamWriterAnalytic, streamWriterSynthetic, false)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Podaci zapisani u analiticko i sinteticko izvjesce.");
        
        // dohvat podataka o plasmanima iz okvira
        iter = bo451.selectFrameAccounts(col_pro_id, register_no, client_type, eligibility);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o plasmanima iz okvira.");
        
        // zapisivanje podataka u izvješæe za partije iz okvira
        if(!writeData(iter, false, null, streamWriterFrame, true)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Podaci zapisani u izvjesce za partije iz okvira.");
        
        // zatvaranje izlaznih datoteka
        streamWriterSynthetic.flush();
        streamWriterSynthetic.close();
        if(streamWriterAnalytic != null) streamWriterAnalytic.flush();
        if(streamWriterAnalytic != null) streamWriterAnalytic.close();
        streamWriterFrame.flush();
        streamWriterFrame.close();
        bc.debug("Zatvorene izlazne datoteke.");

        // slanje maila
        bc.startStopWatch("sendMail");
        Vector attachments = new Vector();
        attachments.add(fileNameSynthetic);
        if (fileNameAnalytic != null) attachments.add(fileNameAnalytic);
        attachments.add(fileNameFrame);
        YXY70.send(bc, "csv212", bc.getLogin(), attachments);
        bc.stopStopWatch("sendMail");

        // stvaranje marker datoteka
        new File(fileNameSynthetic + ".marker").createNewFile();
        if(ponded) new File(fileNameAnalytic + ".marker").createNewFile();
        new File(fileNameFrame + ".marker").createNewFile();
        bc.debug("Stvoreni markeri.");

        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
 
    /**
     * Metoda koja upisuje podatke u izlazne datoteke.
     * @param iter Iterator s podacima
     * @param ponded Da li se radi o ponderiranom izvješæu
     * @param streamWriterAnalytic Streamwriter za analitièko izvješæe
     * @param streamWriterSynthetic Streamwriter za sintetièko izvješæe
     * @param isFrameAccountsReport da li je izvješæe s partijama iz okvira
     * @return da li je metoda uspješno završila 
     */
    private boolean writeData(Iter1 iter, boolean ponded, OutputStreamWriter streamWriterAnalytic, OutputStreamWriter streamWriterSynthetic, boolean isFrameAccountsReport) throws Exception
    {
        CRMData data = null;    // objekt u koji se spremaju podaci o plasmanu i njegovoj pokrivenosti
        while(iter.next())
        {
            if(!isFrameAccountsReport && iter.frame_cus_acc_no() != null && !frameAccExceptions.containsKey(iter.cus_acc_id())) continue;  // ako nije izvješæe s partijama iz okvira, preskoèi partije iz okvira koje se ne mogu povezati samostalno
            bc.debug("CUS_ACC_ID=" + iter.cus_acc_id() + ", CUS_ACC_NO=" + iter.cus_acc_no() + ", FRAME_CUS_ACC_NO=" + iter.frame_cus_acc_no() + ", COL_HEA_ID=" + iter.col_hea_id());
            
            if(data == null || !iter.cus_acc_no().equals(data.cus_acc_no))  // ako se promijenila partija plasmana
            {
                if(data != null) streamWriterSynthetic.write(getDetailsRowSynthetic(data, isFrameAccountsReport));  // zapiši podatke u sintetièko izvješæe
                data = new CRMData();                                                                              // stvori novi data objekt
            }
            fillDataFromIterator(iter, data);                        // prebaci podatke iz iteratora u data objekt
            if(!bo451.selectAdditionalData(data)) return false;      // dohvati dodatne podatke (podtip kolaterala)
            boolean evaluated = evaluateCollateral(data);            // zbroji pokrivenost u odgovarajuæi stupac
            if(ponded && evaluated) streamWriterAnalytic.write(getDetailsRowAnalytic(data));  // ako je izvješæe ponderirano i kolateral ulazi u izvješæe, zapiši analitiku
        }
        if(data != null) streamWriterSynthetic.write(getDetailsRowSynthetic(data, isFrameAccountsReport));  // zapiši zadnji red u sintetièko izvješæe
        iter.close();
        return true;
    }
    
    /**
     * Metoda koja podatke iz iteratora prebacuje u data objekt.
     * @param iter Iterator s podacima
     * @param data Data objekt
     */
    private void fillDataFromIterator(Iter1 iter, CRMData data) throws SQLException
    { 
        data.cus_id = iter.cus_id();
        data.register_no = iter.register_no();
        data.name = iter.name();
        data.b2_asset_class = iter.b2_asset_class();
        data.cus_acc_no = iter.cus_acc_no();
        data.cus_acc_id = iter.cus_acc_id();
        data.exp_balance_hrk = iter.exp_balance_hrk();
        data.frame_cus_acc_no = iter.frame_cus_acc_no();
        data.module_code = iter.module_code();
        data.cus_acc_orig_st = iter.cus_acc_orig_st();
        data.exp_off_bal_lcy = iter.exp_off_bal_lcy();

        data.col_hea_id = iter.col_hea_id();
        data.exp_coll_amount = iter.exp_coll_amount();
        data.col_cat_id = iter.col_cat_id();
        data.col_type_id = iter.col_type_id();
        data.ponder = iter.ponder();
        data.col_type_name = iter.col_type_name();
    }
    
    /**
     * Metoda koja iznos pokrivenosti zbraja u odgovarajuæi stupac ovisno o kategoriji i tipu kolaterala. 
     * @param data Data objekt
     * @return da li kolateral ulazi u izvješæe
     */
    private boolean evaluateCollateral(CRMData data)
    {
        BigDecimal cat = data.col_cat_id;
        BigDecimal typ = data.col_type_id;
        if (cat == null || typ == null) return false;
        BigDecimal sub = data.subtype_coll;
        String subs = data.subtype_coll_str;
        
        if (cat.equals(new BigDecimal("612223")))   
            data.gotovinskiDepozit = data.gotovinskiDepozit.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("616223")) && typ.equals(new BigDecimal("54777")))
            data.policaOsiguranja = data.policaOsiguranja.add(data.exp_coll_amount);  
        else if (cat.equals(new BigDecimal("616223")) && typ.equals(new BigDecimal("55777")) && subs.substring(0,5).equals("MOZOS"))
            data.policaOsiguranja = data.policaOsiguranja.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("615223")) && typ.equals(new BigDecimal("30777")))
            data.garancijaDrzave = data.garancijaDrzave.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("615223")) && typ.equals(new BigDecimal("31777")))  
            data.garancijaDrugeBanke = data.garancijaDrugeBanke.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("9777")) && (data.subtype_coll.equals(new BigDecimal("1063223")) || sub.equals(new BigDecimal("7222"))))            
            data.poljoprivrednoGospodarstvo = data.poljoprivrednoGospodarstvo.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("9777")) && (sub.equals(new BigDecimal("292825223")) || sub.equals(new BigDecimal("9222")) || sub.equals(new BigDecimal("8222"))))           
            data.poslovnoStambeniObjekt = data.poslovnoStambeniObjekt.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("9777")) && sub.equals(new BigDecimal("6524223")))
            data.skladProdUredskiProstor = data.skladProdUredskiProstor.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("12777")))
            data.skladisniProstor = data.skladisniProstor.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("8777")))
            data.stambeniObjekt = data.stambeniObjekt.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("10777")) && (sub.equals(new BigDecimal("10222")) || sub.equals(new BigDecimal("11222"))))
            data.hotelPansion = data.hotelPansion.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("10777")) &&  sub.equals(new BigDecimal("31931223")))
            data.hotelskoNaselje = data.hotelskoNaselje.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("10777")) &&  sub.equals(new BigDecimal("12222")))
            data.privatanSmjestaj = data.privatanSmjestaj.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("7777")) &&  (sub.equals(new BigDecimal("1222")) || sub.equals(new BigDecimal("13222"))))
            data.gradjevinskoZemljiste = data.gradjevinskoZemljiste.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("618223")) && typ.equals(new BigDecimal("7777")) &&  sub.equals(new BigDecimal("2222")))
            data.poljoprivrednoZemljiste = data.poljoprivrednoZemljiste.add(data.exp_coll_amount);  
        else if (cat.equals(new BigDecimal("624223")) && typ.equals(new BigDecimal("14777")) &&  sub.equals(new BigDecimal("1333")))
            data.osobniAutomobil = data.osobniAutomobil.add(data.exp_coll_amount);  
        else if (cat.equals(new BigDecimal("624223")) && typ.equals(new BigDecimal("14777")) && (sub.equals(new BigDecimal("2333")) || sub.equals(new BigDecimal("5333"))))
            data.motocikl = data.motocikl.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("624223")) && typ.equals(new BigDecimal("14777")) &&  sub.equals(new BigDecimal("3333")))
            data.kamion = data.kamion.add(data.exp_coll_amount); 
        else if (cat.equals(new BigDecimal("624223")) && typ.equals(new BigDecimal("14777")) &&  sub.equals(new BigDecimal("4333")))
            data.gradjevinskoVoziloStroj = data.gradjevinskoVoziloStroj.add(data.exp_coll_amount);   
        else if (cat.equals(new BigDecimal("620223")))
            data.plovilo = data.plovilo.add(data.exp_coll_amount);    
        else if (cat.equals(new BigDecimal("621223")) && typ.equals(new BigDecimal("60777")))
            data.zrakoplov = data.zrakoplov.add(data.exp_coll_amount);  
        else if (cat.equals(new BigDecimal("621223")) && typ.equals(new BigDecimal("61777")))
            data.stroj = data.stroj.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("615223")) && (typ.equals(new BigDecimal("32777")) || typ.equals(new BigDecimal("34777")) || typ.equals(new BigDecimal("35777")) || typ.equals(new BigDecimal("36777")) || typ.equals(new BigDecimal("37777")) || typ.equals(new BigDecimal("38777")) || typ.equals(new BigDecimal("56777")) || typ.equals(new BigDecimal("57777")) || typ.equals(new BigDecimal("70777")) || typ.equals(new BigDecimal("73777"))))
            data.ostalaGarancija = data.ostalaGarancija.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("613223")))
            data.dionica = data.dionica.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("619223")))
            data.obveznica = data.obveznica.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("622223")))
            data.udioUFondu = data.udioUFondu.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("629223")))
            data.udioUPoduzecu = data.udioUPoduzecu.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("621223")) && typ.equals(new BigDecimal("16777")))
            data.pokretnineOstale = data.pokretnineOstale.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("621223")) && typ.equals(new BigDecimal("62777")))
            data.pokretnineOprema = data.pokretnineOprema.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("621223")) && typ.equals(new BigDecimal("74777")))
            data.pokretnineIToprema = data.pokretnineIToprema.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("614223")))
            data.cesija = data.cesija.add(data.exp_coll_amount);
        else if (cat.equals(new BigDecimal("626223")))
            data.zaliha = data.zaliha.add(data.exp_coll_amount);
        else return false;
        
        return true;
    }
    
    /**
     * Metoda formira jedan red tablice sintetièkog izvješæa i vraæa ga u obliku stringa.
     * @param data Data objekt
     * @param isFrameAccountsReport da li je izvješæe s partijama iz okvira
     * @return formirani red
     */
    private String getDetailsRowSynthetic(CRMData data, boolean isFrameAccountsReport)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(data.register_no).append(";");
        buffer.append(data.b2_asset_class).append(";");
        buffer.append(data.name).append(";");
        buffer.append(data.cus_acc_no).append(";");
        if(isFrameAccountsReport) buffer.append(data.frame_cus_acc_no).append(";");
        buffer.append(data.exp_balance_hrk).append(";");

        buffer.append(data.gotovinskiDepozit).append(";");
        buffer.append(data.policaOsiguranja).append(";");
        buffer.append(data.garancijaDrzave).append(";");
        buffer.append(data.garancijaDrugeBanke).append(";");
        buffer.append(data.poljoprivrednoGospodarstvo).append(";");
        buffer.append(data.poslovnoStambeniObjekt).append(";");
        buffer.append(data.skladProdUredskiProstor).append(";");
        buffer.append(data.skladisniProstor).append(";");
        buffer.append(data.stambeniObjekt).append(";");
        buffer.append(data.hotelPansion).append(";");
        buffer.append(data.hotelskoNaselje).append(";");
        buffer.append(data.privatanSmjestaj).append(";");
        buffer.append(data.gradjevinskoZemljiste).append(";");
        buffer.append(data.poljoprivrednoZemljiste).append(";");
        buffer.append(data.osobniAutomobil).append(";");
        buffer.append(data.motocikl).append(";");
        buffer.append(data.kamion).append(";");
        buffer.append(data.gradjevinskoVoziloStroj).append(";");
        buffer.append(data.plovilo).append(";");
        buffer.append(data.zrakoplov).append(";");
        buffer.append(data.stroj).append(";");
        buffer.append(data.ostalaGarancija).append(";");
        buffer.append(data.dionica).append(";");
        buffer.append(data.obveznica).append(";");
        buffer.append(data.udioUFondu).append(";");
        buffer.append(data.udioUPoduzecu).append(";");
        buffer.append(data.pokretnineOstale).append(";");
        buffer.append(data.pokretnineOprema).append(";");
        buffer.append(data.pokretnineIToprema).append(";");
        buffer.append(data.cesija).append(";");
        buffer.append(data.zaliha).append(";");
        
        BigDecimal osigurano = data.ukupnaPokrivenost();
        if(!eligibility.equals("RBACOLL") && osigurano.compareTo(data.exp_balance_hrk) > 0) osigurano = data.exp_balance_hrk;  // osigurani dio ne smije biti veæi od izloženosti
        BigDecimal neosigurano = data.exp_balance_hrk.subtract(osigurano);
        if(neosigurano.compareTo(zero) < 0) neosigurano = zero;  // neosigurani dio ne smije biti negativan broj
        buffer.append(osigurano).append(";");
        buffer.append(neosigurano).append(";");
        
        if(!isFrameAccountsReport)  // ako nije izvještaj s partijama iz okvira
        {
            if(data.isFrame())  // ako se radi o okviru, dodaj još tri kolone u izvještaj
            {
                BigDecimal pokrivenostSvihPlasmana = bo451.selectTotalCoverageAmount(col_pro_id, data.cus_acc_id);
                BigDecimal izlozenostSvihPlasmana = bo451.selectTotalExposureAmount(col_pro_id, data.cus_acc_id);
                if(pokrivenostSvihPlasmana != null && izlozenostSvihPlasmana != null)
                {
                    //BigDecimal vanbilanca = data.exp_balance_hrk.subtract(izlozenostSvihPlasmana);
                    BigDecimal vanbilanca = data.exp_off_bal_lcy;  // ne raèuna se, nego se uzima gotov podatak iz DWH (FBPr200020109)
                    if(vanbilanca.compareTo(zero) < 0) vanbilanca = zero;  // vanbilanca ne smije biti negativna
                    
                    BigDecimal pokrivenostVanbilance = osigurano.subtract(pokrivenostSvihPlasmana);
                    if(pokrivenostVanbilance.compareTo(zero) < 0) pokrivenostVanbilance = zero;
                    buffer.append(izlozenostSvihPlasmana).append(";");
                    buffer.append(vanbilanca).append(";");
                    buffer.append(pokrivenostVanbilance).append(";");
                    buffer.append(";");  // prazan indikator za partiju iz okvira
                }
            }
            else   // ako nije okvir
            {
                buffer.append(";;;");  // tri prazne kolone (za okvire)
                buffer.append(data.isAccountFromFrame() ? "*" : "").append(";");  // indikator za partiju iz okvira
            }
        }
        
        buffer.append(data.isSSP() ? "SSP" : "").append(";");  // da li je plasman na SSP-u
         
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda formira jedan red tablice analitièkog izvješæa i vraæa ga u obliku stringa.
     * @param data Data objekt
     * @return formirani red
     */
    private String getDetailsRowAnalytic(CRMData data)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(data.register_no).append(";");
        buffer.append(data.b2_asset_class).append(";");
        buffer.append(data.name).append(";");
        buffer.append(data.cus_acc_no).append(";");
        buffer.append(data.exp_balance_hrk).append(";");
        buffer.append(data.exp_coll_amount).append(";");
        buffer.append(data.col_type_name).append(";");
        buffer.append(data.ponder).append(";");
        buffer.append(data.exp_coll_amount).append(";");
        buffer.append(data.exp_balance_hrk.subtract(data.exp_coll_amount)).append(";");
        buffer.append(data.isAccountFromFrame() ? "*" : "").append(";");  // indikator za partiju iz okvira
        buffer.append(data.isSSP() ? "SSP" : "").append(";");  // da li je plasman na SSP-u
        return buffer.append("\n").toString();
    }

    /**
     * Metoda formira zaglavlje za sintetièko izvješæe i vraæa ga u obliku stringa.
     * @param isFrameAccountsReport da li je izvješæe s partijama iz okvira 
     * @return formirano zaglavlje
     */
    private String getHeaderSynthetic(boolean isFrameAccountsReport)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getHeaderFixed());
        buffer.append("Interni MB komitenta korisnika plasmana").append(";");
        buffer.append("B2 asset class korisnika plasmana").append(";");        
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Partija plasmana").append(";");
        if(isFrameAccountsReport) buffer.append("Partija okvira").append(";");
        buffer.append("Izlo\u017Eenost").append(";");

        buffer.append("Gotovinski depozit").append(";");
        buffer.append("Polica osiguranja sa \u0161tednom komponentom").append(";");
        buffer.append("Garancija dr\u017Eave").append(";");
        buffer.append("Garancija druge banke").append(";");
        buffer.append("Poljop.gospodarstvo,proizvodni objekt").append(";");
        buffer.append("Posl.stamb.objekt,prodajno uslu\u017Eni prostor, uredske prostorije").append(";");
        buffer.append("Sklad., prod. i uredski prostori").append(";");
        buffer.append("Skladi\u0161ni prostor").append(";");
        buffer.append("Stambeni objekt").append(";");
        buffer.append("Hotel, pansion").append(";");
        buffer.append("Hotelsko naselje").append(";");
        buffer.append("Privatni smje\u0161taj").append(";");
        buffer.append("Gra\u0111evinsko zemlji\u0161te").append(";");
        buffer.append("Poljoprivredno zemlji\u0161te").append(";");
        buffer.append("Osobni automobili").append(";");
        buffer.append("Motocikli i ostala vozila").append(";");
        buffer.append("Kamioni, autobusi, pru\u017Ena vozila").append(";");
        buffer.append("Gra\u0111evinska vozila i gra\u0111evinski strojevi").append(";");
        buffer.append("Plovila").append(";");
        buffer.append("Zrakoplovi").append(";");
        buffer.append("Strojevi").append(";");
        buffer.append("Ostale garancije").append(";");
        buffer.append("Dionice").append(";");
        buffer.append("Obveznice").append(";");
        buffer.append("Udjeli u fondovima").append(";");
        buffer.append("Udjeli u poduze\u0107u").append(";");
        buffer.append("Ostale pokretnine i oprema").append(";");
        buffer.append("Uredska oprema i namje\u0161taj").append(";");
        buffer.append("IT i telekom oprema").append(";");
        buffer.append("Cesija").append(";");
        buffer.append("Zaliha").append(";");

        buffer.append("Osigurano").append(";");
        buffer.append("Neosigurano").append(";");
        
        if(!isFrameAccountsReport)  // ako nije izvješæe s partijama iz okvira
        {
            buffer.append("Izlo\u017Eenost svih plasmana").append(";");
            buffer.append("Vanbilanca").append(";");
            buffer.append("Pokrivenost vanbilance").append(";");
            buffer.append("Partija iz okvira").append(";");
        }
        
        buffer.append("SSP plasmani").append(";");
        
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda formira zaglavlje za analitièko izvješæe i vraæa ga u obliku stringa.
     * @return formirano zaglavlje
     */
    private String getHeaderAnalytic()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getHeaderFixed());
        buffer.append("Interni MB komitenta").append(";");
        buffer.append("B2 asset class").append(";");
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Izlo\u017Eenost").append(";");
        buffer.append("Osigurano").append(";");
        buffer.append("Vrsta kolaterala").append(";");
        buffer.append("Ponder").append(";");
        buffer.append("Osigurano").append(";");
        buffer.append("Neosigurano").append(";");
        buffer.append("Partija iz okvira").append(";");
        buffer.append("SSP plasmani").append(";");
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda formira dio zaglavlja koji je jednak za sva izvješæa i vraæa ga u obliku stringa.
     * @return formirani dio zaglavlja
     */
    private String getHeaderFixed()
    {
        String eligHeader = eligibility;
        if(eligibility.equals("B1")) eligHeader = "Prihvatljivost za rezervacije";
        else if(eligibility.equals("B2")) eligHeader = "B2 HNB Standard";
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtils.getDDMMYYYY(report_date)).append("\n");
        buffer.append("Izvje\u0161\u0107e za CRM s ").append("P".equals(ponder) ? "ponderiranim" : "neponderiranim").append(" vrijednostima.").append("\n");
        buffer.append("Prihvatljivost: ").append(eligHeader).append("\n\n");
        return buffer.toString();
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "RB;report_date;register_no;client_type;ponder;eligibility;X".
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if (brojParametara == 7)
            {
                if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                
                report_date = DateUtils.parseDate(bc.getArg(1).trim());
                
                if (bc.getArg(2).trim().equals("")) register_no = null; else register_no = bc.getArg(2).trim();
                
                if (bc.getArg(3).trim().equals("")) client_type = null; else client_type = bc.getArg(3).trim();
                if(client_type != null && !client_type.equals("P") && !client_type.equals("F")) throw new Exception("Dozvoljene vrijednosti za client_type su P,F ili blank!");
                
                ponder = bc.getArg(4).trim().toUpperCase();
                if(!ponder.equals("P") && !ponder.equals("N")) throw new Exception("Dozvoljene vrijednosti za ponder su P ili N!");
                
                eligibility = bc.getArg(5).trim().toUpperCase();
                if(!eligibility.equals("B1") && !eligibility.equals("B2") && !eligibility.equals("RBA") && 
                   !eligibility.equals("B2IRB") && !eligibility.equals("ND") && 
                   !eligibility.equals("B2GK") && !eligibility.equals("RBAGK") &&
                   !eligibility.equals("RBAMICRO") && !eligibility.equals("RBACOLL"))
                    throw new Exception("Dozvoljene vrijednosti za eligibility su B1,B2,RBA,B2IRB,ND,B2GK,RBAGK,RBAMICRO ili RBACOLL!");
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("120227254"));
        batchParameters.setArgs(args);
        new BO450().run(batchParameters);
    }
}