package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Internship;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class AddInternshipController {

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

    @Autowired
    private DataPassService dataPassService;

    public void approve() {
        Internship internship = new Internship(
                null,
                startDate.getValue(),
                endDate.getValue(),
                purpose.getText(),
                summary.getText(),
                comment.getText());
        dataPassService.setValue(internship);
        KRSTManagementSoftware.closeWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
