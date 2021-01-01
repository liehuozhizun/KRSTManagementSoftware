package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.krst.app.configurations.Logger;
import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Staff;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@FXMLController
public class AddEvaluationController {

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
    @Autowired
    private Logger logger;

    @FXML
    public void initialize() {
        try {
            Staff staff = (Staff) dataPassService.getValue();
            name.setText(staff.getName());
            year.setText(String.valueOf(LocalDate.now().getYear()));
            title.setText(staff.getTitle());
            responsibility.setText(staff.getResponsibility());
        } catch (Exception e) {
            String err = e.getMessage() == null ? "空指针异常" : e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("无法打开员工评定");
            alert.setHeaderText("失败原因：" + err);
            alert.setContentText("解决方法：重试或联系开发人员");
            alert.showAndWait();
            logger.logError(getClass().toString(), "打开员工评定失败，失败原因：{}", err);
            throw new RuntimeException("Expected Exception to Interrupt Initialization");
        }
    }

    public void approve() {
        Evaluation evaluation = new Evaluation(
                null,
                Integer.valueOf(year.getText()),
                title.getText(),
                responsibility.getText(),
                comment.getText());
        dataPassService.setValue(evaluation);
        close();
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }

}
