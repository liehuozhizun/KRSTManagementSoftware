package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
public class ImportPanelController {
    @FXML private ProgressBar progressBar;
    @FXML private Button startBtn;
    @FXML private Text progress;
    @FXML private Text selectedFileName;
    @FXML private Text operationName;

    @Autowired private DataPassService dataPassService;
    @Autowired private ImportExportSupport importSupport;
    @Autowired private TemplateFileSupport templateFileSupport;

    private ImportExportOperation operation;
    private File importedFile;

    @FXML public void initialize() {
        this.operation = (ImportExportOperation)dataPassService.getValue();
        operationName.setText(operation.getOperationName());
    }

    public void getTemplate() {
        templateFileSupport.generateTemplateFile(progress.getScene().getWindow(), operation);
    }

    public void selectTemplate() {
        importedFile = templateFileSupport.chooseFile(progress.getScene().getWindow());
        if (importedFile != null) {
            if (templateFileSupport.verifyTemplateFile(importedFile, operation)) {
                selectedFileName.setText(importedFile.getPath());
                selectedFileName.setStyle("-fx-text-fill: black");
                startBtn.setDisable(false);
            } else {
                selectedFileName.setText("导入文件错误：校验码验证错误");
                selectedFileName.setStyle("-fx-text-fill: red");
            }
        }
    }

    public void start() {
        try {
            importSupport.importFile(importedFile, operation, progressBar, progress);
        } catch (Exception e) {
            CommonUtils.alertSystemError("导入数据失败，失败原因：" + e.getMessage());
        }
    }

    public void close() {
        ((Stage)progressBar.getScene().getWindow()).close();
    }
}
