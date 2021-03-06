//created 2014.04.02
package hr.vestigo.modules.collateral.batch.bo44;
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
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo44/ExcelStyleData.java,v 1.1 2014/05/09 09:07:48 hraziv Exp $";
    
    public short dateFormat;
    public short timestampFormat;
    public short numericFormat;
    
    public Font normalFont;
    public Font titleFont;
    public Font headingFont;
    
    public CellStyle titleStyle;
    public CellStyle headingStyle; 
    public CellStyle headingWrapStyle; 
    public CellStyle headingWrapAllBordersStyle; 
    public CellStyle normalStyle;
    public CellStyle normalAllBordersStyle; 
    public CellStyle normalWrapStyle;
    public CellStyle normalNumericStyle; 
    public CellStyle normalDateStyle;
    public CellStyle normalDateAllBordersStyle;
    public CellStyle normalTimestampAllBordersStyle;
    public CellStyle normalTimestampStyle; 
    public CellStyle normalNumericAllBordersStyle; 
    public CellStyle normalTopBordersStyle;
    public CellStyle normalNumericTopBordersStyle;
    public CellStyle normalCenterAllBordersStyle;

    
    public static ExcelStyleData createStyles(Workbook workbook)
    {
        ExcelStyleData styles = new ExcelStyleData();
        
        // create data formats
        DataFormat format = workbook.createDataFormat();
        styles.dateFormat = format.getFormat("dd.MM.yyyy");
        styles.timestampFormat = format.getFormat("dd.MM.yyyy HH:mm:ss");
        styles.numericFormat = format.getFormat("#,##0.00");
        
        // create fonts
        styles.normalFont = workbook.createFont();
        styles.normalFont.setFontName("Calibri");
        styles.normalFont.setFontHeightInPoints((short)11);
    
        styles.titleFont = workbook.createFont();
        styles.titleFont.setFontName("Calibri");
        styles.titleFont.setFontHeightInPoints((short)14);
        styles.titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        styles.headingFont = workbook.createFont();
        styles.headingFont.setFontName("Calibri");
        styles.headingFont.setFontHeightInPoints((short)11);
        styles.headingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // create styles
        styles.titleStyle = workbook.createCellStyle();
        styles.titleStyle.setFont(styles.titleFont);
        
        styles.headingStyle = workbook.createCellStyle();
        styles.headingStyle.setFont(styles.headingFont);
        
        styles.headingWrapStyle = workbook.createCellStyle();
        styles.headingWrapStyle.setFont(styles.headingFont);
        styles.headingWrapStyle.setWrapText(true);
        
        styles.headingWrapAllBordersStyle = workbook.createCellStyle();
        styles.headingWrapAllBordersStyle.setFont(styles.headingFont);
        styles.headingWrapAllBordersStyle.setWrapText(true);
        styles.headingWrapAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.headingWrapAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.headingWrapAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.headingWrapAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
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
        
        styles.normalAllBordersStyle = workbook.createCellStyle();
        styles.normalAllBordersStyle.setFont(styles.normalFont);
        styles.normalAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalDateAllBordersStyle = workbook.createCellStyle();
        styles.normalDateAllBordersStyle.setFont(styles.normalFont);
        styles.normalDateAllBordersStyle.setDataFormat(styles.dateFormat);
        styles.normalDateAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalDateAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalDateAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalDateAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalNumericAllBordersStyle = workbook.createCellStyle();
        styles.normalNumericAllBordersStyle.setFont(styles.normalFont);
        styles.normalNumericAllBordersStyle.setDataFormat(styles.numericFormat);
        styles.normalNumericAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalNumericAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalNumericAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalNumericAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalTopBordersStyle = workbook.createCellStyle();
        styles.normalTopBordersStyle.setFont(styles.normalFont);
        styles.normalTopBordersStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        
        styles.normalNumericTopBordersStyle = workbook.createCellStyle();
        styles.normalNumericTopBordersStyle.setFont(styles.normalFont);
        styles.normalNumericTopBordersStyle.setDataFormat(styles.numericFormat);
        styles.normalNumericTopBordersStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        
        styles.normalCenterAllBordersStyle = workbook.createCellStyle();
        styles.normalCenterAllBordersStyle.setFont(styles.normalFont);
        styles.normalCenterAllBordersStyle.setAlignment(CellStyle.ALIGN_CENTER);  
        styles.normalCenterAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalCenterAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalCenterAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalCenterAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalTimestampAllBordersStyle=workbook.createCellStyle();
        styles.normalTimestampAllBordersStyle.setFont(styles.normalFont);
        styles.normalTimestampAllBordersStyle.setDataFormat(styles.timestampFormat);
        styles.normalTimestampAllBordersStyle.setBorderTop(CellStyle.BORDER_THIN);
        styles.normalTimestampAllBordersStyle.setBorderLeft(CellStyle.BORDER_THIN);
        styles.normalTimestampAllBordersStyle.setBorderBottom(CellStyle.BORDER_THIN);
        styles.normalTimestampAllBordersStyle.setBorderRight(CellStyle.BORDER_THIN);
        
        styles.normalTimestampStyle = workbook.createCellStyle();
        styles.normalTimestampStyle.setFont(styles.normalFont);
        styles.normalTimestampStyle.setDataFormat(styles.timestampFormat);
                
        return styles;
    }
}