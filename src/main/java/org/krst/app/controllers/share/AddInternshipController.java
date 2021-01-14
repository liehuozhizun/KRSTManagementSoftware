package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.krst.app.domains.Internship;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : None
 * Out : Internship, new created internship model
 */
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
                startDate.getValue(),
                endDate.getValue(),
                purpose.getText(),
                summary.getText(),
                comment.getText());
        if (!internship.equals(new Internship()))
            dataPassService.setValue(internship);
        close();
    }

    public void close() {
        ((Stage)comment.getScene().getWindow()).close();
    }
}
