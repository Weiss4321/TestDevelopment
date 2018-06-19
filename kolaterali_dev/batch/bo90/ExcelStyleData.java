package hr.vestigo.modules.collateral.batch.bo90;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * Klasa koja kreira sve potrebne stilove u Excel datoteci.
 */
public class ExcelStyleData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo90/ExcelStyleData.java,v 1.1 2014/04/30 10:32:59 hrakis Exp $";
    
    public short dateFormat;
    public short numericFormat;
    public short percentageFormat;
    
    public Font normalFont;
    public Font normalSmallFont;
    public Font titleFont;
    public Font headingFont;
    
    public CellStyle titleStyle;
    public CellStyle titleWrapStyle;
    public CellStyle titleDateStyle;
    public CellStyle headingStyle; 
    public CellStyle normalStyle;
    public CellStyle normalSmallStyle;
    public CellStyle normalWrapStyle;
    public CellStyle normalNumericStyle; 
    public CellStyle normalDateStyle;
    public CellStyle normalPercentageStyle;

    
    public static ExcelStyleData createStyles(Workbook workbook)
    {
        ExcelStyleData styles = new ExcelStyleData();
        
        // create data formats
        DataFormat format = workbook.createDataFormat();
        styles.dateFormat = format.getFormat("dd.MM.yyyy");
        styles.numericFormat = format.getFormat("#,##0.00");
        styles.percentageFormat = format.getFormat("0.00%");
        
        // create fonts
        styles.normalFont = workbook.createFont();
        styles.normalFont.setFontName("Calibri");
        styles.normalFont.setFontHeightInPoints((short)10);
        
        styles.normalSmallFont = workbook.createFont();
        styles.normalSmallFont.setFontName("Calibri");
        styles.normalSmallFont.setFontHeightInPoints((short)9);
    
        styles.titleFont = workbook.createFont();
        styles.titleFont.setFontName("Futura CE Book");
        styles.titleFont.setFontHeightInPoints((short)10);
        
        styles.headingFont = workbook.createFont();
        styles.headingFont.setFontName("Calibri");
        styles.headingFont.setFontHeightInPoints((short)11);

        // create styles
        styles.titleStyle = workbook.createCellStyle();
        styles.titleStyle.setFont(styles.titleFont);
        styles.titleStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        
        styles.titleDateStyle = workbook.createCellStyle();
        styles.titleDateStyle.setFont(styles.titleFont);
        styles.titleDateStyle.setDataFormat(styles.dateFormat);
        styles.titleDateStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        
        styles.titleWrapStyle = workbook.createCellStyle();
        styles.titleWrapStyle.setFont(styles.titleFont);
        styles.titleWrapStyle.setWrapText(true);
        styles.titleWrapStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        
        styles.headingStyle = workbook.createCellStyle();
        styles.headingStyle.setFont(styles.headingFont);
        styles.headingStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
        styles.headingStyle.setWrapText(true);
        styles.headingStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.headingStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalStyle = workbook.createCellStyle();
        styles.normalStyle.setFont(styles.normalFont);
        styles.normalStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        styles.normalSmallStyle = workbook.createCellStyle();
        styles.normalSmallStyle.setFont(styles.normalSmallFont);
        styles.normalSmallStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalSmallStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalSmallStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalSmallStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalSmallStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        styles.normalWrapStyle = workbook.createCellStyle();
        styles.normalWrapStyle.setFont(styles.normalFont);
        styles.normalWrapStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalWrapStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalWrapStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalWrapStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalWrapStyle.setWrapText(true);
        styles.normalWrapStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        styles.normalNumericStyle = workbook.createCellStyle();
        styles.normalNumericStyle.setFont(styles.normalFont);
        styles.normalNumericStyle.setDataFormat(styles.numericFormat);
        styles.normalNumericStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalNumericStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalNumericStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalNumericStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalNumericStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        styles.normalDateStyle = workbook.createCellStyle();
        styles.normalDateStyle.setFont(styles.normalFont);
        styles.normalDateStyle.setDataFormat(styles.dateFormat);
        styles.normalDateStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalDateStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalDateStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalDateStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalDateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        styles.normalPercentageStyle = workbook.createCellStyle();
        styles.normalPercentageStyle.setFont(styles.normalFont);
        styles.normalPercentageStyle.setDataFormat(styles.percentageFormat);
        styles.normalPercentageStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalPercentageStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalPercentageStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalPercentageStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.normalPercentageStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        return styles;
    }
}