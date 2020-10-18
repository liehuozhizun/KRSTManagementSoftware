package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Visit;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FXMLController
public class AddVisitController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        KRSTManagementSoftware.closeWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }

}
