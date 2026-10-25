
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {

	private static final String FILE_PATH = "src/test/resources/RegisterLoginData.xlsx";

	public static Object[][] getData(String sheetName) throws IOException {
		List<Object[]> data = new ArrayList<>();
		int columnCount;
		if (sheetName.equalsIgnoreCase("ValidRegistration1")) {
			columnCount = 6;
		}
		else if (sheetName.equalsIgnoreCase("ValidRegistration2NegCheck")) {
			columnCount = 6;
		}
		else if (sheetName.equalsIgnoreCase("ValidLogin")) {
			columnCount = 3;
		} else if (sheetName.equalsIgnoreCase("InvalidLogin")) {
			columnCount = 3;
		} else {
			throw new IllegalArgumentException("Invalid sheet name: " + sheetName);
		}
		try (FileInputStream fis = new FileInputStream(FILE_PATH); Workbook workbook = WorkbookFactory.create(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet not found: " + sheetName);
			}
			DataFormatter formatter = new DataFormatter();

			// Starts data read from Row1 since Row0 contains headers
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				Object[] rowData = new Object[columnCount];
				boolean isEmpty = true;
				for (int j = 0; j < columnCount; j++) {
					String value = formatter.formatCellValue(row.getCell(j)).trim();
					rowData[j] = value;
					if (!value.isEmpty()) {
						isEmpty = false;
					}
				}
				// Ignore completely empty rows.
				if (!isEmpty) {
					data.add(rowData);
				}
			}
		}
		return data.toArray(new Object[0][]);
	}
}
