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
import org.krst.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Staff, the Staff model that will be used to store this evaluation model
 * Out : null, create an empty Evaluation, which should be ignored
 *       OR
 *       Evaluation, new created Evaluation model
 */
@FXMLController
public class AddEvaluationController {
    @FXML private TextField name, year, title;
    @FXML private TextArea responsibility, comment;

    @Autowired private DataPassService dataPassService;
    @Autowired private Logger logger;

    @FXML public void initialize() {
        try {
            Staff staff = (Staff) dataPassService.getValue();
            name.setText(staff.getName());
            year.setText(String.valueOf(CommonUtils.getCurrentZonedTime().getYear()));
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
        if (year.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建员工评价失败");
            alert.setHeaderText("失败原因：未填入评价年份");
            alert.setContentText("解决方法：请输入评价年份");
            alert.show();
            return;
        }

        Evaluation evaluation = new Evaluation(
                year.getText(),
                title.getText(),
                responsibility.getText(),
                comment.getText());
        if (!evaluation.equals(new Evaluation()))
            dataPassService.setValue(evaluation);
        close();
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }

}
