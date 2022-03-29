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
import org.krst.app.configurations.Logger;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.services.DataPassService;
import org.krst.app.services.supports.DataProcessors.TriConsumer;
import org.krst.app.utils.ImportExportOperation;
import org.krst.app.views.share.ImportResultReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Service
public class ImportExportSupport {
    @Autowired private DataPassService dataPassService;
    @Autowired private DataProcessor dataProcessor;
    @Autowired private Logger logger;

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

        logger.logInfo(this.getClass().toString(), "执行导入数据操作：{}, 尝试{}条，成功{}条，失败{}条",
                operation.getOperationName(), 
                String.valueOf(failures.size()),
                String.valueOf(failures.stream().filter(Objects::nonNull).count()),
                String.valueOf(failures.stream().filter(Objects::isNull).count()));
    }

    public void exportFile(File dstFile, ImportExportOperation operation, ProgressBar progressBar, Text progress)
            throws IOException {
        List list;
        try (FileInputStream templateInputStream = new FileInputStream(ResourceUtils.getFile(operation.getInternalPath()));
             XSSFWorkbook xssfWorkbook = new XSSFWorkbook(templateInputStream);
             SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook);
             FileOutputStream outputStream = new FileOutputStream(dstFile))
        {
            final CellStyle cellStyle = xssfWorkbook.getSheetAt(1).getRow(0).getCell(0).getCellStyle();
            workbook.removeSheetAt(1);
            Sheet sheet = workbook.getSheetAt(0);

            TriConsumer<Row, Object, CellStyle> processor = dataProcessor.getExportProcessor(operation);
            AtomicInteger rowIdx = new AtomicInteger(2);
            list = dataProcessor.getData(operation).get();
            int totalNumber = list.size();
            list.forEach(x -> {
                processor.accept(sheet.createRow(rowIdx.getAndIncrement()), x, cellStyle);
                int p = rowIdx.get() / totalNumber * 100;
                progressBar.setProgress(p);
                progress.setText(Integer.toString(p));
            });

            workbook.write(outputStream);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("导出数据");
        alert.setHeaderText("导出数据结束，共：" + list.size() + "条");
        alert.setContentText("请查看您的导出数据\n位于：" + dstFile.getPath());
        alert.show();

        logger.logInfo(this.getClass().toString(), "执行导出数据操作：{}, 尝试{}条",
                operation.getOperationName(),
                String.valueOf(list.size()));
    }
}
