package com.example.decathlon.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	public String getCellInfo(String filePath, int sheetNumber, int rowNumber, int colNumber) throws IOException {
		File excelfile = new File(filePath);
		try (FileInputStream fis = new FileInputStream(excelfile);
			 XSSFWorkbook wb = new XSSFWorkbook(fis)) {

			Sheet sheet = wb.getSheetAt(sheetNumber);
			Row row = sheet.getRow(rowNumber);
			Cell cell = row.getCell(colNumber);

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(cell);
		}
	}

	public Object[][] readSheet(String filePath, int sheetNumber) throws IOException {
		File excelfile = new File(filePath);

		try (FileInputStream fis = new FileInputStream(excelfile);
			 XSSFWorkbook wb = new XSSFWorkbook(fis)) {

			Sheet sheet = wb.getSheetAt(sheetNumber);
			DataFormatter dataFormatter = new DataFormatter();
			List<Object[]> rows = new ArrayList<>();

			int maxColumns = 0;
			for (Row row : sheet) {
				if (row.getLastCellNum() > maxColumns) {
					maxColumns = row.getLastCellNum();
				}
			}

			for (Row row : sheet) {
				Object[] rowData = new Object[maxColumns];
				for (int col = 0; col < maxColumns; col++) {
					Cell cell = row.getCell(col);
					rowData[col] = cell == null ? "" : dataFormatter.formatCellValue(cell);
				}
				rows.add(rowData);
			}

			Object[][] data = new Object[rows.size()][maxColumns];
			for (int i = 0; i < rows.size(); i++) {
				data[i] = rows.get(i);
			}
			return data;
		}
	}
}