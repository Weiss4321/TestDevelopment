package hr.vestigo.modules.collateral.batch.bo80;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * Klasa koja kreira sve potrebne stilove u Excel datoteci.
 */
public class ExcelStyleData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo80/ExcelStyleData.java,v 1.1 2014/03/27 13:38:45 hrakis Exp $";
    
    public short dateFormat;
    public short numericFormat;
    
    public Font normalFont;
    public Font titleFont;
    public Font headingFont;
    
    public CellStyle titleStyle;
    public CellStyle titleDateStyle;
    public CellStyle headingStyle; 
    public CellStyle normalStyle;
    public CellStyle normalWrapStyle;
    public CellStyle normalNumericStyle; 
    public CellStyle normalDateStyle;

    
    public static ExcelStyleData createStyles(Workbook workbook)
    {
        ExcelStyleData styles = new ExcelStyleData();
        
        // create data formats
        DataFormat format = workbook.createDataFormat();
        styles.dateFormat = format.getFormat("dd.MM.yyyy");
        styles.numericFormat = format.getFormat("#,##0.00");
        
        // create fonts
        styles.normalFont = workbook.createFont();
        styles.normalFont.setFontName("Calibri");
        styles.normalFont.setFontHeightInPoints((short)11);
    
        styles.titleFont = workbook.createFont();
        styles.titleFont.setFontName("Calibri");
        styles.titleFont.setFontHeightInPoints((short)13);
        styles.titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        styles.headingFont = workbook.createFont();
        styles.headingFont.setFontName("Calibri");
        styles.headingFont.setFontHeightInPoints((short)11);
        styles.headingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // create styles
        styles.titleStyle = workbook.createCellStyle();
        styles.titleStyle.setFont(styles.titleFont);
        
        styles.titleDateStyle = workbook.createCellStyle();
        styles.titleDateStyle.setFont(styles.titleFont);
        styles.titleDateStyle.setDataFormat(styles.dateFormat);
        
        styles.headingStyle = workbook.createCellStyle();
        styles.headingStyle.setFont(styles.headingFont);
        styles.headingStyle.setWrapText(true);
        
        styles.normalStyle = workbook.createCellStyle();
        styles.normalStyle.setFont(styles.normalFont);
        
        styles.normalWrapStyle = workbook.createCellStyle();
        styles.normalWrapStyle.setFont(styles.normalFont);
        styles.normalWrapStyle.setWrapText(true);
        
        styles.normalNumericStyle = workbook.createCellStyle();
        styles.normalNumericStyle.setFont(styles.normalFont);
        styles.normalNumericStyle.setDataFormat(styles.numericFormat);
        
        styles.normalDateStyle = workbook.createCellStyle();
        styles.normalDateStyle.setFont(styles.normalFont);
        styles.normalDateStyle.setDataFormat(styles.dateFormat);
        
        return styles;
    }
}