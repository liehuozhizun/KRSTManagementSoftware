package org.krst.app.services.supports.DataProcessors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.ZoneId;

public abstract class AbstractDataProcessor {
    protected String getCellStringValue(Row row, Integer cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    protected LocalDate getCellDateValue(Row row, Integer cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell == null) return null;
        return row.getCell(cellIdx).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
