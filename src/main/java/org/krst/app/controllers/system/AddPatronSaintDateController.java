package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.krst.app.domains.PatronSaintDate;
import org.krst.app.repositories.PatronSaintDateRepository;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : none
 * OUT : null, nothing added
 *       PatronSaintDate, newly added data model
 */
@FXMLController
public class AddPatronSaintDateController {
    @FXML private TextField name;
    @FXML private DatePicker date;

    @Autowired private PatronSaintDateRepository patronSaintDateRepository;
    @Autowired private DataPassService dataPassService;

    public void accept() {
        dataPassService.setValue(patronSaintDateRepository.save(new PatronSaintDate(null, name.getText(), date.getValue())));
        close();
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }
}
