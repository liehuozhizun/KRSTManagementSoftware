package org.krst.app.services.supports;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelSupport {
    public List<String> getMetaData(File file, String dataName) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(1);
        Row row = sheet.getRow(0);
        short lastCellNum = row.getLastCellNum();

        List<String> list = new ArrayList<>();
        for (int cellIdx = 0; cellIdx <= lastCellNum; cellIdx++) {
            if (row.getCell(cellIdx).getStringCellValue().equals(dataName)) {
                int lastRowNum = sheet.getLastRowNum();
                for (int rowIdx = 1; rowIdx <= lastRowNum; rowIdx++) {
                    Cell cell = sheet.getRow(rowIdx).getCell(cellIdx);
                    if (cell == null) break;
                    list.add(cell.getStringCellValue());
                }
                break;
            }
        }
        workbook.close();
        return list;
    }

    public void setMetaData(File src, File dst, Map<String, List<String>> map) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(src);
        Sheet sheet = workbook.getSheetAt(1);
        Row titleRow = sheet.getRow(0);
        short lastCellNum = titleRow.getLastCellNum();

        Cell cell;
        Row row;
        for (int cellIdx = 0; cellIdx <= lastCellNum; cellIdx++) {
            cell = titleRow.getCell(cellIdx);
            if (cell == null) break;

            String key = cell.getStringCellValue();
            if (key.isEmpty()) break;
            if (map.containsKey(key)) {
                int rowIdx = 1;
                for (String value : map.get(key)) {
                    row = sheet.getRow(rowIdx);
                    if (row == null) {
                        sheet.createRow(rowIdx).createCell(cellIdx).setCellValue(value);
                    } else {
                        cell = row.getCell(cellIdx);
                        if (cell == null) row.createCell(cellIdx).setCellValue(value);
                        else cell.setCellValue(value);
                    }
                    rowIdx++;
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream(dst);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
}
