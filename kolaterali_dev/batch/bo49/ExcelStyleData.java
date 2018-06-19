package hr.vestigo.modules.collateral.batch.bo49;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * Klasa koja kreira sve potrebne stilove u Excel datoteci.
 */
public class ExcelStyleData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo49/ExcelStyleData.java,v 1.1 2016/11/25 12:43:30 hrakis Exp $";
    
    public short dateFormat;
    public short numericFormat;
    public short generalNumericFormat;
    
    public Font normalFont;
    public Font headingFont;
    
    public CellStyle headingStyle; 
    public CellStyle normalStyle;
    public CellStyle normalWrapStyle;
    public CellStyle normalNumericStyle;
    public CellStyle normalGeneralNumericStyle;
    public CellStyle normalDateStyle;

    
    public static ExcelStyleData createStyles(Workbook workbook)
    {
        ExcelStyleData styles = new ExcelStyleData();
        
        // create data formats
        DataFormat format = workbook.createDataFormat();
        styles.dateFormat = format.getFormat("dd.MM.yyyy");
        styles.numericFormat = format.getFormat("#,##0.00");
        styles.generalNumericFormat = format.getFormat("0");
        
        // create fonts
        styles.normalFont = workbook.createFont();
        styles.normalFont.setFontName("Arial");
        styles.normalFont.setFontHeightInPoints((short)9);
    
        styles.headingFont = workbook.createFont();
        styles.headingFont.setFontName("Arial");
        styles.headingFont.setFontHeightInPoints((short)9);
        styles.headingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // create styles
        styles.headingStyle = workbook.createCellStyle();
        styles.headingStyle.setFont(styles.headingFont);
        styles.headingStyle.setWrapText(true);
        styles.headingStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styles.headingStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.headingStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalStyle = workbook.createCellStyle();
        styles.normalStyle.setFont(styles.normalFont);
        
        styles.normalWrapStyle = workbook.createCellStyle();
        styles.normalWrapStyle.setFont(styles.normalFont);
        styles.normalWrapStyle.setWrapText(true);
        
        styles.normalNumericStyle = workbook.createCellStyle();
        styles.normalNumericStyle.setFont(styles.normalFont);
        styles.normalNumericStyle.setDataFormat(styles.numericFormat);
        
        styles.normalGeneralNumericStyle = workbook.createCellStyle();
        styles.normalGeneralNumericStyle.setFont(styles.normalFont);
        styles.normalGeneralNumericStyle.setDataFormat(styles.generalNumericFormat);
        
        styles.normalDateStyle = workbook.createCellStyle();
        styles.normalDateStyle.setFont(styles.normalFont);
        styles.normalDateStyle.setDataFormat(styles.dateFormat);
        
        return styles;
    }
}