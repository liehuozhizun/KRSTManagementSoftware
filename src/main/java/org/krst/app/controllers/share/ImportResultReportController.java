package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.krst.app.models.ImportExportFailure;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * IN  : List<ImportExportFailure>, contains all the failure record of import
 * OUT : None
 */
@FXMLController
public class ImportResultReportController {
    @FXML private Text successNumber;
    @FXML private Text failNumber;
    @FXML private Text totalNumber;
    @FXML private TableView<ImportExportFailure> fails;
    @FXML private TableColumn<ImportExportFailure, Integer> fails_row;
    @FXML private TableColumn<ImportExportFailure, String> fails_id;
    @FXML private TableColumn<ImportExportFailure, String> fails_name;
    @FXML private TableColumn<ImportExportFailure, String> fails_error;

    @Autowired private DataPassService dataPassService;

    @FXML public void initialize() {
        initDefaultComponents();
        refreshData();
    }

    private void initDefaultComponents() {
        fails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        fails_row.setCellValueFactory(new PropertyValueFactory<>("rowNumber"));
        fails_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        fails_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        fails_error.setCellValueFactory(new PropertyValueFactory<>("error"));
    }

    private void refreshData() {
        List<ImportExportFailure> failures = (List<ImportExportFailure>)dataPassService.getValue();
        int totalResultNumber = failures.size();
        totalNumber.setText(Integer.toString(totalResultNumber));
        failures = failures.stream().filter(Objects::nonNull).collect(Collectors.toList());
        fails.getItems().setAll(failures);
        int failResultNumber = failures.size();
        failNumber.setText(Integer.toString(failResultNumber));
        int successResultNumber = totalResultNumber - failResultNumber;
        successNumber.setText(Integer.toString(successResultNumber));
    }

    public void close() {
        ((Stage)successNumber.getScene().getWindow()).close();
    }
}
