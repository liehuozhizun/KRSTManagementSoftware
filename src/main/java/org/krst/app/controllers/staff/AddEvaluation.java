package org.krst.app.controllers.staff;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Staff;
import org.krst.app.services.DataPassService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddEvaluation implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(((Staff)DataPassService.getValue()).getName());
        year.setText(String.valueOf(LocalDate.now().getYear()));
        title.setText(((Staff)DataPassService.getValue()).getTitle());
        responsibility.setText(((Staff)DataPassService.getValue()).getResponsibility());
    }

    public void approve() {
        Evaluation evaluation = new Evaluation(
                null,
                Integer.valueOf(year.getText()),
                title.getText(),
                responsibility.getText(),
                comment.getText());
        DataPassService.setValue(evaluation);
        KRSTManagementSoftware.closeNewWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }

}
