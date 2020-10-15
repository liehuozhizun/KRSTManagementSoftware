package org.krst.app.controllers.share;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Visit;
import org.krst.app.services.DataPassService;

import java.util.stream.Collectors;

public class AddVisit {

    @FXML
    private DatePicker date;
    @FXML
    private ComboBox visitor;
    @FXML
    private ListView<String> visitors;
    @FXML
    private TextArea content;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea comment;

    public void approve() {
        Visit visit = new Visit(
                null,
                date.getValue(),
                visitors.getItems().stream().collect(Collectors.toList()),
                content.getText(),
                summary.getText(),
                comment.getText());
        DataPassService.setValue(visit);
        KRSTManagementSoftware.closeNewWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }
}
