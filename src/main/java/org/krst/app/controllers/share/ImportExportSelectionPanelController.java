package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.ImportExportOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/*
 * In  : boolean, true  import
 *                false export
 * Out : ImportExportOperation, the operation will be executed
 *       null, cancelled this request
 */
@FXMLController
public class ImportExportSelectionPanelController {
    @FXML private Text operationType;
    @FXML private ComboBox<ImportExportOperation> operationTarget;
    @FXML private Button approve;

    private final List<ImportExportOperation> importOperations = new ArrayList<>();
    private final List<ImportExportOperation> exportOperations = new ArrayList<>();

    {
        importOperations.add(ImportExportOperation.IMPORT_STUDENT_TEMPLATE);
        exportOperations.add(ImportExportOperation.EXPORT_ALL_STUDENT_INFO);
    }

    @Autowired
    private DataPassService dataPassService;

    @FXML public void initialize() {
        approve.setDisable(true);
        boolean type = (boolean) dataPassService.getValue();
        if (type) {
            operationType.setText("导入");
        } else {
            operationType.setText("导出");
        }

        operationTarget.getItems().setAll(type ? importOperations : exportOperations);
        operationTarget.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                approve.setDisable(false);
        });
        operationTarget.setConverter(new StringConverter<ImportExportOperation>() {
            @Override
            public String toString(ImportExportOperation operation) {
                return operation == null ? null : operation.getOperationName();
            }

            @Override
            public ImportExportOperation fromString(String string) {
                return null;
            }
        });
    }

    public void approve() {
        dataPassService.setValue(operationTarget.getValue());
        close();
    }

    public void close() {
        ((Stage)operationType.getScene().getWindow()).close();
    }
}
