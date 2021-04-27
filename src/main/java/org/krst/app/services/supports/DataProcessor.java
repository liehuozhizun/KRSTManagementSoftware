package org.krst.app.services.supports;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.supports.DataProcessors.StudentDataProcessor;
import org.krst.app.services.supports.DataProcessors.TriConsumer;
import org.krst.app.utils.ImportExportOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class DataProcessor {

    @Autowired private StudentDataProcessor studentDataProcessor;
    @Autowired private StudentRepository studentRepository;

    public Function<Row, ImportExportFailure> getImportProcessor(ImportExportOperation operation) {
        switch (operation) {
            case IMPORT_STUDENT_TEMPLATE:
                return studentDataProcessor;
        }
        return (x -> null);
    }

    public TriConsumer<Row, Object, CellStyle> getExportProcessor(ImportExportOperation operation) {
        switch (operation) {
            case IMPORT_STUDENT_TEMPLATE:
                return studentDataProcessor;
        }
        return ((x,y,z) -> {});
    }

    public Supplier<List> getData(ImportExportOperation operation) {
        switch (operation) {
            case EXPORT_ALL_STUDENT_INFO:
                return studentDataProcessor;
        }
        return (() -> new ArrayList());
    }

    public BiConsumer<File, File> setMetaData(ImportExportOperation operation) {
        switch (operation) {
            case IMPORT_STUDENT_TEMPLATE:
                return studentDataProcessor;
        }
        return ((x,y) -> {});
    }
}
