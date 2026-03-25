package com.example.decathlon.excel;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelPrinter {

	private final XSSFWorkbook workbook;
	private final String filePath;

	public ExcelPrinter(String filePath) throws IOException {
		workbook = new XSSFWorkbook();
		this.filePath = filePath;
	}

	public void add(Object[][] data, String sheetName) {
		XSSFSheet sheet = workbook.createSheet(sheetName);

		int rowCount = 0;
		for (Object[] rowData : data) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			for (Object field : rowData) {
				Cell cell = row.createCell(columnCount++);

				if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				} else if (field instanceof Double) {
					cell.setCellValue((Double) field);
				} else if (field instanceof Long) {
					cell.setCellValue((Long) field);
				} else {
					cell.setCellValue(field == null ? "" : String.valueOf(field));
				}
			}
		}

		if (data.length > 0) {
			for (int i = 0; i < data[0].length; i++) {
				sheet.autoSizeColumn(i);
			}
		}
	}

	public void write() throws IOException {
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			workbook.write(out);
		}
		workbook.close();
	}
}