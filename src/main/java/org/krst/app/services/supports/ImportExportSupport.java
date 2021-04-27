package org.krst.app.services.supports;

import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.services.DataPassService;
import org.krst.app.services.supports.DataProcessors.TriConsumer;
import org.krst.app.utils.ImportExportOperation;
import org.krst.app.views.share.ImportResultReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Service
public class ImportExportSupport {
    @Autowired private DataPassService dataPassService;
    @Autowired private DataProcessor dataProcessor;

    public void importFile(File file, ImportExportOperation operation, ProgressBar progressBar, Text progress)
            throws IOException, InvalidFormatException {
        Function<Row, ImportExportFailure> processor = dataProcessor.getImportProcessor(operation);
        List<ImportExportFailure> failures = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNumber = sheet.getLastRowNum();
        for (int rowIdx = 1; rowIdx <= lastRowNumber; rowIdx++) {
            failures.add(processor.apply(sheet.getRow(rowIdx)));
            int p = rowIdx / lastRowNumber * 100;
            progressBar.setProgress(p);
            progress.setText(Integer.toString(p));
        }
        workbook.close();

        dataPassService.setValue(failures);
        KRSTManagementSoftware.openWindow(ImportResultReport.class);
    }

    public void exportFile(File file, ImportExportOperation operation, ProgressBar progressBar, Text progress)
            throws IOException, InvalidFormatException {
        XSSFWorkbook template = new XSSFWorkbook(ResourceUtils.getFile(operation.getInternalPath()));
        Workbook workbook = new SXSSFWorkbook(template);
        final CellStyle cellStyle = workbook.getSheetAt(1).getRow(0).getCell(0).getCellStyle();
        workbook.removeSheetAt(1);
        Sheet sheet = workbook.getSheet(operation.getOperationName());

        TriConsumer<Row, Object, CellStyle> processor = dataProcessor.getExportProcessor(operation);
        AtomicInteger rowIdx = new AtomicInteger(2);
        List list = dataProcessor.getData(operation).get();
        int totalNumber = list.size();
        list.forEach(x -> {
            processor.accept(sheet.createRow(rowIdx.getAndIncrement()), x, cellStyle);
            int p = rowIdx.get() / totalNumber * 100;
            progressBar.setProgress(p);
            progress.setText(Integer.toString(p));
        });

        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        template.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("导出数据");
        alert.setHeaderText("导出数据结束，共：" + list.size() + "条");
        alert.setContentText("请查看您的导出数据\n位于：" + file.getPath());
        alert.show();
    }
}
