package org.krst.app.services.supports.DataProcessors;

import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class AbstractDataProcessor {
    DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected String getCellStringValue(Row row, Integer cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell == null) return null;
        return cell.getStringCellValue();
    }

    protected LocalDate getCellDateValue(Row row, Integer cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell == null) return null;
        return row.getCell(cellIdx).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    protected void setCellValue(Row row, Integer cellIdx, String value) {
        if (value != null)
            row.createCell(cellIdx).setCellValue(value);
    }

    protected void setCellValue(Row row, Integer cellIdx, LocalDate date) {
        if (date != null) {
            row.createCell(cellIdx).setCellValue(simpleDateFormat.format(date));
        }
    }

    protected void setCellValue(Row row, Integer cellIdx, Boolean value) {
        if (value != null)
            row.createCell(cellIdx).setCellValue(value ? "是" : "否");
    }

    protected void setCellValue(Row row, Integer cellIdx, Integer value) {
        if (value != null)
            row.createCell(cellIdx).setCellValue(value);
    }
}
