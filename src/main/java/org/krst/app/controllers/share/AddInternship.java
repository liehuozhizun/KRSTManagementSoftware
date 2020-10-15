package org.krst.app.controllers.share;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Internship;
import org.krst.app.services.DataPassService;

public class AddInternship {

    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextArea purpose;
    @FXML
    private TextArea summary;
    @FXML
    private TextArea comment;

    public void approve() {
        Internship internship = new Internship(
                null,
                startDate.getValue(),
                endDate.getValue(),
                purpose.getText(),
                summary.getText(),
                comment.getText());
        DataPassService.setValue(internship);
        KRSTManagementSoftware.closeNewWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }
}
