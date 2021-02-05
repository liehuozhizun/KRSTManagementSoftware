package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.PatronSaintDate;
import org.krst.app.repositories.PatronSaintDateRepository;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class PatronSaintDateControlPanelController {
    @FXML private TextField number;
    @FXML private TableView<PatronSaintDate> table;
    @FXML private TableColumn<PatronSaintDate, String> id;
    @FXML private TableColumn<PatronSaintDate, String> name;
    @FXML private TableColumn<PatronSaintDate, String> date;

    @Autowired private PatronSaintDateRepository patronSaintDateRepository;
    @Autowired private DataPassService dataPassService;

    @FXML public void initialize() {
        if (number.getText() == null || number.getText().equals("0"))
            table.getItems().setAll(patronSaintDateRepository.findAll());

        number.setText(String.valueOf(table.getItems().size()));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setRowFactory( tv -> {
            TableRow<PatronSaintDate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem().clone());
                    KRSTManagementSoftware.openWindow(PatronSaintDateInfoPage.class);
                    Pair<Boolean, PatronSaintDate> returnedData = (Pair<Boolean, PatronSaintDate>) dataPassService.getValue();
                    if (returnedData != null) {
                        if (returnedData.getKey()) {
                            table.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            table.getItems().remove(row.getIndex());
                        }
                        number.setText(String.valueOf(table.getItems().size()));
                    }
                }
            });
            return row ;
        });

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void addPatronSaintDate() {
        KRSTManagementSoftware.openWindow(AddPatronSaintDate.class);
        PatronSaintDate temp = (PatronSaintDate) dataPassService.getValue();
        if (temp != null) {
            table.getItems().add(temp);
            number.setText(String.valueOf(table.getItems().size()));
        }
    }
}
