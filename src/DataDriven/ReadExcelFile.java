package DataDriven;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelFile {
	/*
	 * XSSFWorkbook wb; XSSFSheet sheet;
	 */

	XSSFWorkbook wb;
	XSSFSheet sheet;

	ReadExcelFile(String excelPath) {

		try {
			File src = new File(excelPath);
			FileInputStream fis = new FileInputStream(src);
			wb = new XSSFWorkbook(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getData(int sheetNumer, int row, int column) {
		sheet = wb.getSheetAt(sheetNumer);
		String data = "";

		if (sheet != null && sheet.getRow(row) != null && sheet.getRow(row).getCell(column) != null) {
			//Setting all cell types to String 
			sheet.getRow(row).getCell(column).setCellType(CellType.STRING);
			data = sheet.getRow(row).getCell(column) != null ? sheet.getRow(row).getCell(column).getStringCellValue()
					: "";
		}
		return data;

	}

	public int getRowCount(int sheetIndex) {
		int row = wb.getSheetAt(sheetIndex).getLastRowNum();
		return row;

	}

}
