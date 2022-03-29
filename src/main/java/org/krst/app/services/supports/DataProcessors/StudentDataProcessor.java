package org.krst.app.services.supports.DataProcessors;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.supports.ExcelSupport;
import org.krst.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class StudentDataProcessor extends AbstractDataProcessor implements
        Function<Row, ImportExportFailure>,
        TriConsumer<Row, Object, CellStyle>,
        Supplier<List>,
        BiConsumer<File, File>
{
    @Autowired private CacheService cacheService;
    @Autowired private ExcelSupport excelSupport;
    @Autowired private StudentRepository studentRepository;

    @Override
    public List<Student> get() {
        return studentRepository.findAll();
    }

    @Override
    public ImportExportFailure apply(Row row) {
        String id = getCellStringValue(row, 0);
        if (id.isEmpty())
            return new ImportExportFailure(
                    row.getRowNum(),
                    getCellStringValue(row, 0),
                    getCellStringValue(row, 1),
                    "编号为空");
        if (studentRepository.existsById(id))
            return new ImportExportFailure(
                    row.getRowNum(),
                    getCellStringValue(row, 0),
                    getCellStringValue(row, 1),
                    "此编号已被使用"
            );

        Student student;
        try {
            student = new Student(
                    id,
                    getCellStringValue(row, 1),
                    getCellStringValue(row, 2),
                    getCellStringValue(row, 3),
                    getCellDateValue(row, 4),
                    row.getCell(5) == null || "是".equals(getCellStringValue(row, 5)),
                    getCellDateValue(row, 6),
                    getCellDateValue(row, 7),
                    getCellDateValue(row, 8),
                    getCellDateValue(row, 9),
                    row.getCell(10) == null ? null : new Attribute(getCellStringValue(row, 10)),
                    getCellStringValue(row, 11),
                    getCellStringValue(row, 12),
                    getCellStringValue(row, 13),
                    getCellStringValue(row, 14),
                    getCellStringValue(row, 15),
                    getCellStringValue(row, 16),
                    getCellStringValue(row, 17),
                    row.getCell(18) == null ? null : new Staff(row.getCell(18).getStringCellValue()),
                    null, null, null, null
            );
        } catch (Exception e) {
            return new ImportExportFailure(
                    row.getRowNum(),
                    getCellStringValue(row, 0),
                    getCellStringValue(row, 1),
                    e.getMessage());
        }

        studentRepository.save(student);
        return null;
    }

    @Override
    public void accept(Row row, Object obj, CellStyle cellStyle) {
        int cellIdx = 0;
        Student student = (Student) obj;
        setCellValue(row, cellIdx, student.getId());
        setCellValue(row, ++cellIdx, student.getName());
        setCellValue(row, ++cellIdx, student.getBaptismalName());
        setCellValue(row, ++cellIdx, student.getGender());
        setCellValue(row, ++cellIdx, student.getBirthday());
        setCellValue(row, ++cellIdx, student.getIsGregorianCalendar());
        setCellValue(row, ++cellIdx, student.getBirthday() == null ? null : student.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears());
        setCellValue(row, ++cellIdx, student.getBaptismalDate());
        setCellValue(row, ++cellIdx, student.getConfirmationDate());
        setCellValue(row, ++cellIdx, student.getMarriageDate());
        setCellValue(row, ++cellIdx, student.getDeathDate());
        setCellValue(row, ++cellIdx, student.getAttribute() == null ? null : student.getAttribute().getAttribute());
        setCellValue(row, ++cellIdx, student.getPhone());
        setCellValue(row, ++cellIdx, student.getAltPhone());
        setCellValue(row, ++cellIdx, student.getAddress());
        setCellValue(row, ++cellIdx, student.getExperience());
        setCellValue(row, ++cellIdx, student.getTalent());
        setCellValue(row, ++cellIdx, student.getResource());
        setCellValue(row, ++cellIdx, student.getEducation());
        setCellValue(row, ++cellIdx, student.getStaff() == null ? null : student.getStaff().getName());
    }

    @SneakyThrows
    @Override
    public void accept(File src, File dst) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("所属堂区", cacheService.getAttributes().stream().map(Attribute::getAttribute).collect(Collectors.toList()));
        map.put("负责员工", cacheService.getStaffs().stream().map(Staff::getNameAndId).collect(Collectors.toList()));
        excelSupport.setMetaData(src, dst, map);
    }
}
