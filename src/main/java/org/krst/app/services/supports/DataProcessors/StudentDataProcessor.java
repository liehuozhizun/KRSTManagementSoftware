package org.krst.app.services.supports.DataProcessors;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class StudentDataProcessor implements TriConsumer<Row, Object, CellStyle> {
    @Autowired StudentRepository studentRepository;

    private final DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ImportExportFailure process(Row row) {
        String id = row.getCell(0).getStringCellValue();
        if (id.isEmpty())
            return new ImportExportFailure(
                    row.getRowNum(),
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    "编号为空");
        if (studentRepository.existsById(id))
            return new ImportExportFailure(
                    row.getRowNum(),
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    "此编号已被使用"
            );

        Student student = new Student(
                id,
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                LocalDate.parse(row.getCell(4).getStringCellValue(), dateTimeFormatter),
                "是".equals(row.getCell(5).getStringCellValue()),
                LocalDate.parse(row.getCell(6).getStringCellValue(), dateTimeFormatter),
                LocalDate.parse(row.getCell(7).getStringCellValue(), dateTimeFormatter),
                LocalDate.parse(row.getCell(8).getStringCellValue(), dateTimeFormatter),
                LocalDate.parse(row.getCell(9).getStringCellValue(), dateTimeFormatter),
                new Attribute(row.getCell(10).getStringCellValue(),null,null,null,null),
                row.getCell(11).getStringCellValue(),
                row.getCell(12).getStringCellValue(),
                row.getCell(13).getStringCellValue(),
                row.getCell(14).getStringCellValue(),
                row.getCell(15).getStringCellValue(),
                row.getCell(16).getStringCellValue(),
                row.getCell(17).getStringCellValue(),
                new Staff(row.getCell(18).getStringCellValue()),
                null,null,null,null
                );

        studentRepository.save(student);
        return null;
    }

    @Override
    public void accept(Row cells, Object obj, CellStyle cellStyle) {

    }
}
