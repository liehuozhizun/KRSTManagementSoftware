package org.krst.app.services.supports;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelSupport {
    public List<String> getMetaData(File file, String dataName) throws IOException, InvalidFormatException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(file));
        SXSSFSheet sheet = workbook.getSheetAt(1);
        SXSSFRow row = sheet.getRow(0);
        short lastCellNum = row.getLastCellNum();

        List<String> list = new ArrayList<>();
        for (int cellIdx = 0; cellIdx < lastCellNum; cellIdx++) {
            if (row.getCell(cellIdx).getStringCellValue().equals(dataName)) {
                int lastRowNum = sheet.getLastRowNum();
                for (int rowIdx = 0; rowIdx < lastRowNum; rowIdx++) {
                    SXSSFCell cell = sheet.getRow(rowIdx).getCell(cellIdx);
                    if (cell == null) break;
                    list.add(cell.getStringCellValue());
                }
                break;
            }
        }
        return list;
    }
}
