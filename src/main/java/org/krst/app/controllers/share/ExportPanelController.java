package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.krst.app.configurations.Logger;
import org.krst.app.services.DataPassService;
import org.krst.app.services.supports.ImportExportSupport;
import org.krst.app.services.supports.TemplateFileSupport;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.ImportExportOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/*
 * In  : ImportExportOperation, the operation will be executed in this window
 * Out : None
 */
@FXMLController
public class ExportPanelController {
    @FXML private ProgressBar progressBar;
    @FXML private Button startBtn;
    @FXML private Text progress;
    @FXML private Text selectedPath;
    @FXML private Text operationName;

    @Autowired private Logger logger;
    @Autowired private DataPassService dataPassService;
    @Autowired private ImportExportSupport exportSupport;
    @Autowired private TemplateFileSupport templateFileSupport;

    private ImportExportOperation operation;
    private File dstFile;

    @FXML public void initialize() {
        this.operation = (ImportExportOperation)dataPassService.getValue();
        operationName.setText(operation.getOperationName());
    }

    public void selectPath() {
        File dstDirectory = templateFileSupport.chooseDirectory(progress.getScene().getWindow());
        if (dstDirectory != null) {
            dstFile = templateFileSupport.validateDirectoryForNewFileCreation(dstDirectory, operation);
            if (dstFile != null) {
                startBtn.setDisable(false);
                selectedPath.setText(dstDirectory.getPath());
            }
        }
    }

    public void start() {
        startBtn.setDisable(true);
        progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        try {
            exportSupport.exportFile(dstFile, operation, progressBar, progress);
        } catch (Exception e) {
            CommonUtils.alertFeatureError("导出数据", e.getMessage());
            logger.logError(this.getClass().toString(), "导出数据失败，失败原因：{}, StackTrace: {}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            templateFileSupport.deleteTemplateFile(dstFile);
        }
    }

    public void close() {
        ((Stage)progressBar.getScene().getWindow()).close();
    }
}
