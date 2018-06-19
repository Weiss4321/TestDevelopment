//created 2008.11.11
package hr.vestigo.modules.collateral.batch.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.rba.batch.util.AbstractFileTransferBatch;
import hr.vestigo.modules.rba.interfaces.jms.MQMessageSender;
import hr.vestigo.modules.rba.interfaces.jms.MQMessageSenderFactory;
import hr.vestigo.modules.rba.util.DateUtils;

/**
 * BAse klasa za sve batch obrade iz kolaterala. klasa implementira neke zajednick radnje poput logiranja u col_proc tablicu,
 * provjera da li se radi o novoj obradi ili se nastavlja stara, kopiranje datoteka i filetransfer...
 *
 * @author hraamh
 */
public abstract class AbstractCollateralFileTransferBatch extends
        AbstractFileTransferBatch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/util/AbstractCollateralFileTransferBatch.java,v 1.9 2010/04/30 15:56:18 hraamh Exp $";
    
    /**
     * povratna vrijednost obrade
     */
    protected String toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;
    
    /**
     * tip collateral obrade
     */
    protected String proc_type=null;
    /**
     * batch tip dogadaja
     */
    protected BigDecimal eve_typ_id=null;
    /**
     * organizacijska jedinica =53253
     */
    protected BigDecimal org_uni_id=null;
    /**
     * dogadaj pustanja obrade
     */
    protected BigDecimal eve_id=null;
    /**
     * korisnik=1
     */
    protected BigDecimal use_id= null;
    /**
     * datum obrade
     */
    protected Date proc_date=null;
    /**
     * datum valute
     */
    protected Date value_date=null;
    
    protected String proc_way="A";
    /**
     * da li se radi o potpuno novoj collateral obradi ili se nastavlja prethodno nezavrsena
     */
    private boolean freshProcess=true;
    
    /**
     * col_proc user lock
     */
    //protected Timestamp pc_user_lock=null;
    /**
     * brojac za obradene podatke
     */
    protected long dataCounter=0;
    /**
     * common sa zajednickim metodama koje se cesto koriste kod collateral obrada
     * 
     */
    protected CommonCollateralMethods commonMethods=null;
    
    protected Map<BigDecimal, BigDecimal> exchangeRate=null;
    
    
    
    /**
     * folder ulazne datoteke sa / na kraju
     */
    protected String inputDir=null;
    /**
     * folder izlazne datoteke sa / na kraju
     */
    protected String outputDir=null;
    /**
     * naziv datoteke (ne full path)
     */
    protected String inputFile=null;
    /**
     * naziv odlazne datoteke (full path)
     */
    protected String outputFile=null;
    /**
     * filtar za kopiranje odlaznih datoteka
     */
    protected String outputFileFilter=null;
    
    
    
    /**
     * server na koji se salje odlazna datoteka. Ako je null nece se slati
     */
    protected String dServer=null;
    /**
     * folder na remote serveru
     */
    protected String dDir=null;
    /**
     * remote user
     */
    protected String dUser=null;
    /**
     * direktorij obrade
     */
    protected String hfsHDir=null;
    /**
     * MQ poruka za notifikaciju kopiranja izlazne datoteke
     */
    protected String mqNotifyMessage=null;

    /**
     * 
     */
    public AbstractCollateralFileTransferBatch() {
        super();
        org_uni_id=new BigDecimal(53253);
        use_id=new BigDecimal(1);
        setEve_typ_id();
        exchangeRate= new HashMap<BigDecimal, BigDecimal>();
        exchangeRate.put(new BigDecimal(63999), new BigDecimal(1));
    }
    
    /**
     * vraca id dogadaja pustanja obrade
     * 
     * @return the eve_id
     */
    protected BigDecimal getEventId() {
        return eve_id;
    }

    /**
     * @return the eve_typ_id
     */
    protected BigDecimal getEve_typ_id() {
        return eve_typ_id;
    }

    /**
     * Potrebno je implementirati u podklasi dodajuci hardcode id na eve_typ_id
     * 
     */
    protected abstract void setEve_typ_id();
    
    protected abstract void init() throws Exception;
    /**
     * dohvat ulaznih parametara
     * 
     * @param bc
     * @return
     */
    protected abstract int getArgs(BatchContext bc);
    /**
     * da li obrada pocinje iznova; tj da li se nastavlja u slucaju prekida
     * 
     * @return true ako uvijek pocinje iz pocetka
     */
    protected abstract boolean isAlwaysFreshStart(); 
    
    /**
     * da li se obrada moze izvrsavati samo jedanput dnevno.
     * Default postavka je da se izvrsava samo jedanput. U slucaju visestrukog izvrsavanja mora se 
     * napraviti override motede koja vraca false 
     * 
     * @return true po defaultu
     */
    protected boolean isOnlyOnceADay(){
        return true;
    }
    
    /**
     * da li se salje MQ poruka pri kopiranju datoteke na udaljen server
     * 
     * @return
     */
    protected abstract boolean isMQNotify(); 
    /**
     * da li se kopiraju ulazne datoteke u log folder
     * 
     * @return
     */
    protected abstract boolean isInFileManipulation();
    
    /**
     * da li se kopiraju izlazne datoteke u out folder
     * 
     * @return
     */
    protected abstract boolean isOutFileManipulation();
    /**
     * da li se kopira izlazna datoteka na udaljeni server
     * 
     * @return
     */
    protected abstract boolean isFileTransfer();
    /**
     * dohvat id-a collateral obrade
     * 
     * @return
     * @throws Exception
     */
    protected abstract BigDecimal getColProId() throws Exception;
    /**
     * postavljanje id-a collateral obrade
     * @param col_pro_id
     */
    protected abstract void setColProId(BigDecimal col_pro_id);
    /**
     * naziv obrade (npr bo20)
     * @return
     */
    protected abstract String getBatchName();
    /**
     * target modul u slucaju MQ notifikacije. blank ako se ne koristi MQ notifikacija
     * 
     * @return
     */
    protected abstract String getTargetModule();
    /**
     * povecanje (ili smanjenje ) brojaca obradenih slogova
     * 
     * @param num
     */
    protected void incrementCounter(long num){
        dataCounter+=num;
    }
    /**
     * dohvat batch konteksta
     * 
     * @return
     */
    protected BatchContext getContext(){
        return this.bc;
    }
    /**
     * u slucaju da se koriste 2 ili vise konekcija potrebno je zatvoriti sve dodatne. takoder i iteratore vezane na njih
     * 
     * @throws Exception
     */
    protected abstract void closeExtraConnections() throws Exception;
    
    /**
     * Implementacija obrade. Moraju se rucno postaviti vrijednost propertia:
     * <hr>
     * 
     * Za col_proc obradu:
     * <li><b>proc_type</b></li>
     * <li><b>proc_date</b></ul>
     * <li><b>value_date</b></ul>
     * 
     * Za manipulaciju datoteka:
     * <li><b>inputDir</b></ul>
     * <li><b>outputDir</b></ul>
     * <li><b>inputFile</b></ul>
     * <li><b>outputFile</b></ul>
     * <li><b>outputFileFilter</b></ul>
     * 
     * Za kopiranje na remote server:
     * <li><b>dServer</b></ul>
     * <li><b>dDir</b></ul>
     * <li><b>dUser</b></ul>
     * <li><b>hfsHDir</b></li>
     * 
     * Za notifikaciju o kopiranju datoteke putem MQ-a
     * <li><b>mqNotifyMessage</b></li>
     * <hr>
     * 
     * @return RemoteConstants vrijednosti
     * @throws Exception
     */
    protected abstract String runBatch() throws Exception;

    /* (non-Javadoc)
     * @see hr.vestigo.modules.rba.batch.util.AbstractFileTransferBatch#execute()
     */
    @Override
    protected String execute() throws Exception {
        if(getArgs(getContext())!=0){
            return RemoteConstants.RET_CODE_ERROR;
        }
        init();    
        setEve_typ_id();
        long startTime=System.currentTimeMillis();
        Timestamp user_lock=null;
        BigDecimal proc_id=null;
        try{
            commonMethods= CollateralCommonFactory.getCommonCollateralMethods(bc);
            
            if(isAlwaysFreshStart()){
                //ako uvijek pocinje iz pocetka
                bc.beginTransaction();
                proc_id=getColProId();
                user_lock= commonMethods.insertColProc(getColProcMap(proc_id,"0",new BigDecimal(0),user_lock));
                eve_id= commonMethods.insertIntoEvent(this.eve_typ_id,this.org_uni_id,this.use_id);               
                bc.commitTransaction();
            }else{
                
                //ako se obrada moze raditi samo jednaput dnevno
                if(isOnlyOnceADay()){
                    proc_id=commonMethods.selectColProc(proc_date, proc_type, "1");
                    if(proc_id!=null){
                        //nema potreba nastavljati obradu
                        bc.debug("Obrada za dan "+proc_date+" je vec zavrsena");
                        return toReturn;
                    }
                }
                proc_id=commonMethods.selectColProc(proc_date, proc_type, "0");
                if(proc_id!=null){
                    bc.debug("Obrada za dan "+proc_date+" je vec zapoceta ali nije zavrsena. Nastavljam...");
                    freshProcess=false;
                    setColProId(proc_id);
                }else{
                    bc.beginTransaction();                   
                    proc_id=getColProId();
                    user_lock= commonMethods.insertColProc(getColProcMap(proc_id,"0",new BigDecimal(0),user_lock));
                    eve_id= commonMethods.insertIntoEvent(eve_typ_id,org_uni_id,use_id);
                    bc.commitTransaction();
                }
            }       
            
            toReturn=runBatch();
            if((toReturn==RemoteConstants.RET_CODE_ERROR) || (toReturn==RemoteConstants.RET_CODE_FATAL)){
                //u slucaju da je ozbiljan eror prekida se rad batcha
                return toReturn;
            }
            user_lock= new Timestamp(System.currentTimeMillis());
            bc.beginTransaction();
            commonMethods.updateColProc(getColProcMap(proc_id,"1",new BigDecimal(dataCounter),user_lock));
            bc.commitTransaction();
            
            //ako kopira izlazne datoteke u predvideni OutFolder
            if(isOutFileManipulation()){
                if (!copyCreatedFile(getBatchName(),outputDir, outputFileFilter)){                
                    bc.debug("Nije uspjelo kopiranje datoteka.");
                    return RemoteConstants.RET_CODE_ERROR;
                }else{
                    //ako salje datoteku na drugi server
                    if(isFileTransfer()){
                        if (!transferFileToAnotherServer(hfsHDir,getBatchName(),dServer,dDir,dUser)){                    
                            bc.debug("Nije uspio prijenos datoteke.");
                            return RemoteConstants.RET_CODE_ERROR;
                        } else {     
                            //ako salje MQ obavjest o spustanju datoteke na server
                            if(isMQNotify()){
                                try{ 
                                    String target=getTargetModule();
                                    bc.debug(mqNotifyMessage);
                                    MQMessageSender notifier=MQMessageSenderFactory.getMqSenderInstance(MQMessageSenderFactory.SIRIUS, this.bc);
                                    notifier.sendMessageToQueue(mqNotifyMessage, target);
                                }
                                catch (Exception e) {
                                    bc.info("Nije se uspjela poslati poruka zbog " + e.getMessage());
                                    e.printStackTrace();
                                    return RemoteConstants.RET_CODE_ERROR;
                                }
                            }
                        } 
                    }
                }
            }
            //ako kopira ulaznu datoteku u log folder
            if(isInFileManipulation()){               
                try {
                    moveFileToLogDir(outputDir, inputDir+inputFile);
                } catch (Exception e) {
                    bc.error(e.getMessage(), e);
                    return RemoteConstants.RET_CODE_ERROR;
                }
            }
            
        }catch (Exception e) {
            e.printStackTrace();
            toReturn=RemoteConstants.RET_CODE_ERROR;
        }finally{
           closeExtraConnections();
        }       
        long endTime=System.currentTimeMillis();
        bc.debug("Vrijeme izvodenja :" + (endTime-startTime)/1000.0 + " sekundi.");        
        bc.debug("******************** execute() zavrsio *********************");
        if (toReturn.equals(RemoteConstants.RET_CODE_WARNING)) toReturn=RemoteConstants.RET_CODE_SUCCESSFUL;
        return toReturn;      
    }
    
    /**
     * postavljanje porametara za upis/osvjezavanje zapisa obrade u col_proc tablici
     * 
     * @param col_pro_id
     * @param proc_status
     * @param col_number
     * @param user_lock
     * @return
     * @throws Exception
     */
    public Map getColProcMap(BigDecimal col_pro_id, String proc_status,BigDecimal col_number, Timestamp user_lock) throws Exception{
        Map map= new HashMap();
        map.put("col_pro_id",col_pro_id); 
        map.put("proc_date",proc_date); 
        map.put("value_date",value_date); 
        map.put("proc_type",proc_type);    
        map.put("proc_way",proc_way);            
        map.put("proc_status",proc_status); 
        map.put("col_number",col_number);   
        map.put("org_uni_id",org_uni_id);   
        map.put("use_id", use_id); 
        map.put("user_lock", user_lock);
        return map;     
    }

    /**
     * Da li je obrada potpuno nova ili se nastavlja prethodno nezavrsena obrada
     * 
     * @return freshProcess flag
     */
    protected boolean isFreshProcess() {
        return freshProcess;
    }

    /**
     * Dohvat commona koji implementira neke zajednicke funkcionalnosti 
     * prihvacene u obradama kolaterala (COL_PROC, garbace, insert eventa ...)
     * 
     * @return
     */
    public CommonCollateralMethods getCommonCollateralMethods(){
        return this.commonMethods;
    }
    
    /**
     * Pretvorba vrijednosti u drugu valutu. radi se cachiranje tecaja.
     * 
     * @param cur_id valuta pretvorbe
     * @param value vrijednost
     * @param date datum valute
     * @param toKn true- preracunava se u kune, false - kunska vrijednost se racuna u predanu valutu
     * @return 
     * @throws Exception
     */
    public BigDecimal exchange(BigDecimal cur_id,BigDecimal value, Date date, boolean toKn) throws Exception{
        if((value==null)||(cur_id==null)) return null;
        BigDecimal result=null;     
        BigDecimal rate=(BigDecimal)this.exchangeRate.get(cur_id);
        if(rate==null){
            rate=commonMethods.selectMiddRate(cur_id,date, "RB");
            this.exchangeRate.put(cur_id,rate);
        }
        
        if(toKn){
            result=value.multiply(rate);
        }else{
            result=value.divide(rate,BigDecimal.ROUND_HALF_EVEN);
        }
        return result;  
    }
}

