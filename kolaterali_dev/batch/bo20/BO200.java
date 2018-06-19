package hr.vestigo.modules.collateral.batch.bo20;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.batch.io.FileConstants;
import hr.vestigo.framework.remote.batch.io.FileManager;
import hr.vestigo.framework.remote.batch.io.VFile;
import hr.vestigo.framework.remote.batch.io.VInputStream;
import hr.vestigo.framework.remote.batch.io.VOutputStream;
import hr.vestigo.framework.remote.batch.io.VReader;
import hr.vestigo.framework.remote.batch.io.VWriter;
import hr.vestigo.modules.collateral.batch.util.AbstractCollateralFileTransferBatch;
import hr.vestigo.modules.collateral.common.data.CollateralInsuranceInstrumentData;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralInsuranceInstrument;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.yoy0.YOY00;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.batch.util.AbstractFileTransferBatch;
import hr.vestigo.modules.rba.interfaces.jms.MQMessageSender;
import hr.vestigo.modules.rba.interfaces.jms.MQMessageSenderFactory;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.StringUtils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BO200 extends AbstractCollateralFileTransferBatch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo20/BO200.java,v 1.9 2009/02/04 14:30:07 hraamh Exp $";
	
	
	/** ekstenzija datoteka */
	private String extension=null;
	/** mapa za tip obrade */
	private Map dataType=null;
    
    private BigDecimal col_pro_id=null;
    
    private ProcessFileEnum processName=ProcessFileEnum.NEPOZNATO;
	
	/**
	 * 
	 */
	public BO200() {
		super();
		
		
	}
    
    private String formatMQMessage(String filename, String date){
        String result=null;
        String code=processName.toOutCode();
        result="100"+date+code+StringUtils.generateStringWithBlanks(filename, 50, true);
        return result;
    }
	 

	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal(2097179003));// id s razvoja
        bp.setArgs(args);
        new BO200().run(bp);
	}

    @Override
    protected void closeExtraConnections() throws Exception {
        // TODO Auto-generated method stub
        
    }
    @Override
    protected int getArgs(BatchContext bc) {
        inputDir=bc.getArg(1);     
        if(!inputDir.endsWith("/")) inputDir+="/";
        outputDir=bc.getOutDir();
        if(!outputDir.endsWith("/")) outputDir+="/";       
        inputFile=bc.getArg(2);
        dServer=bc.getArg(3);
        dDir=bc.getArg(4);
        dUser=bc.getArg(5);
        hfsHDir=bc.getArg(6);
        String toPrint="\n ... getArgs()\n";
        toPrint+="\n inputDir: "+inputDir;
        toPrint+="\n outputDir: "+outputDir;
        toPrint+="\n inputFile: "+inputFile;
        toPrint+="\n dServer: "+dServer;
        toPrint+="\n dDir: "+dDir;
        toPrint+="\n dUser: "+dUser;
        toPrint+="\n hfsHDir: "+hfsHDir;
        getContext().debug(toPrint);
        return 0;
    }
    @Override
    protected String getBatchName() {
        return "bo20";
    }
    @Override
    protected BigDecimal getColProId() throws Exception {
        if(col_pro_id==null){
            this.col_pro_id=(new YOY00(getContext())).getNewId();
        }
        return col_pro_id;
    }
    @Override
    protected String getTargetModule() {
        return "CLC";
    }
    @Override
    protected void init() throws Exception {
        this.proc_date= new Date(System.currentTimeMillis());
        this.value_date=this.proc_date;
        //this.proc_type="CL1";
        this.proc_type=processName.toProcType();
        
        processName=ProcessFileEnum.make(inputFile);
        //setDataType();
    }
    @Override
    protected boolean isAlwaysFreshStart() {
        return true;
    }
    @Override
    protected boolean isInFileManipulation() {
        return true;
    }
    @Override
    protected boolean isOutFileManipulation() {
        return true;
    }
    @Override
    protected boolean isFileTransfer() {
        return true;
    }
    @Override
    protected boolean isMQNotify() {
        return true;
    }
    @Override
    protected String runBatch() throws Exception {
        VFile fileIn=null;
        VFile fileOut=null;
        FileManager fileManager= bc.getFileManager();
        VInputStream fInputStream=null;
        VReader fReader=null; 
        VOutputStream fOutputStream=null;
        VWriter  fWriter=null;
        CollateralInsuranceInstrument collInsInstr=CollateralCommonFactory.getCollateralInsuranceInstrument(this.bc);
        
        String proc_date_s=proc_date.toString().replaceAll("-", "");
        String outFileType=inputFile.replaceAll("_IN", "_OUT");
        

        if(extension==null){
            inputFile=inputFile+"_"+proc_date_s;
        }else{
            inputFile=inputFile+"_"+proc_date_s+"."+extension;
        }
        bc.debug("inFile:"+inputDir+inputFile);
        
        File markerFile= new File(inputDir+inputFile+".marker");
        if(!markerFile.exists()){
            getContext().debug(markerFile.getName()+" does not exists!");
            throw new FileNotFoundException(markerFile.getName()+" does not exists");
        }
        
        outputFile=inputFile.replaceFirst("_IN_", "_OUT_"); 
        outputFileFilter=outputFile;
        //outputFileFilter="";
        fileIn=new VFile(inputDir+inputFile);
        fileOut=new VFile(outputDir+outputFile);
        bc.debug("outputFile:"+outputFile);
        //otvaranje ulazne datoteke
        fInputStream=fileManager.getInputStream(FileManager.INPUT_STREAM_TYPE_FILE, fileIn);
        fReader = fileManager.getReader(FileManager.READER_TYPE_BUFFERED, fInputStream,FileConstants.ENCODING_MS_DOS_LATIN_2_CP852);
        
        fOutputStream= fileManager.getOutputStream(FileManager.OUTPUT_STREAM_TYPE_FILE, fileOut);
        
        fWriter= fileManager.getWriter(FileManager.WRITER_TYPE_BUFFERED, fOutputStream, FileConstants.ENCODING_MS_DOS_LATIN_2_CP852);
        
        String inLine=fReader.readLine();
        bc.debug("fInputStream:"+fInputStream);
        bc.debug("fReader:"+fReader);
        bc.debug("fOutputStream:"+fOutputStream);
        bc.debug("fWriter:"+fWriter);
        bc.debug("inLine:"+inLine);
        
        while(inLine!=null){
            String cus_acc_no=inLine.trim();
            bc.debug("*************************************************************");
            bc.debug("Cus_acc_no: "+cus_acc_no);
            Vector insurance=collInsInstr.getCollaterals(cus_acc_no);
            bc.debug("Broj podataka za plasman: "+insurance.size());
            for(int i=0; i<insurance.size();i++){
                CollateralInsuranceInstrumentData data=(CollateralInsuranceInstrumentData) insurance.get(i);
                
                if(processName==ProcessFileEnum.OVERDRAFT){
                    data.product_code="64";
                }
                
                data.procedure_date=proc_date;
                bc.debug(data+"\n\n");
                fWriter.write(data.toFormatedStringLine("")+"\n");
                fWriter.flush();
                fOutputStream.flush();
                incrementCounter(1);
            }
            bc.debug("*************************************************************");
            inLine=fReader.readLine();              
            if (inLine ==null || inLine.equals("")){
                break;
            }
        }   
        fReader.close();
        fInputStream.close();
        fWriter.close();
        fOutputStream.close();
        bc.markFile(outputFile);
        
        //mqNotifyMessage=formatMQMessage(outFileType,outputFile,DateUtils.getDDMMYYYY(proc_date));
        mqNotifyMessage=formatMQMessage(outputFile,DateUtils.getDDMMYYYY(proc_date));
        return toReturn;        
    }
    @Override
    protected void setColProId(BigDecimal col_pro_id) {
        this.col_pro_id=col_pro_id;
    }
    @Override
    protected void setEve_typ_id() {
        this.eve_typ_id= new BigDecimal(2097163003);    
    }
}