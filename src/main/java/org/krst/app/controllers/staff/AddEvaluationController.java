package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Staff;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@FXMLController
public class AddEvaluationController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField year;
    @FXML
    private TextField title;
    @FXML
    private TextArea responsibility;
    @FXML
    private TextArea comment;

    @Autowired
    private DataPassService dataPassService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(((Staff)dataPassService.getValue()).getName());
        year.setText(String.valueOf(LocalDate.now().getYear()));
        title.setText(((Staff)dataPassService.getValue()).getTitle());
        responsibility.setText(((Staff)dataPassService.getValue()).getResponsibility());
    }

    public void approve() {
        Evaluation evaluation = new Evaluation(
                null,
                Integer.valueOf(year.getText()),
                title.getText(),
                responsibility.getText(),
                comment.getText());
        dataPassService.setValue(evaluation);
        KRSTManagementSoftware.closeWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }

}
