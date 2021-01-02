package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Visit;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@FXMLController
public class AddVisitController {

    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<Staff> visitor;
    @FXML
    private ListView<Staff> visitors;
    @FXML
    private TextArea content;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea comment;

    @Autowired
    private DataPassService dataPassService;
    @Autowired
    private CacheService cacheService;

    @FXML
    public void initialize() {
        visitor.getItems().addAll(cacheService.getStaffs());

        visitor.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
            @Override
            public ListCell<Staff> call(ListView param) {
                return new ListCell<Staff>() {
                    @Override
                    protected void updateItem(Staff item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " [" + item.getId() + "]");
                        }
                    }
                };
            }
        });

        visitor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addVisitorToVisitors(newValue);
            }
        });
    }

    private void addVisitorToVisitors(Staff staff) {
        if (!visitors.getItems().contains(staff)) {
            visitors.getItems().add(staff);
        }
    }

    public void approve() {
        Visit visit = new Visit(
                null,
                date.getValue(),
                new ArrayList<>(visitors.getItems()),
                content.getText(),
                summary.getText(),
                comment.getText());
        dataPassService.setValue(visit);
        close();
    }

    public void close() {
        ((Stage)comment.getScene().getWindow()).close();
    }

}
