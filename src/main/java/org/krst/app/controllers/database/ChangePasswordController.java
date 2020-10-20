package org.krst.app.controllers.database;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.krst.app.models.Status;
import org.krst.app.configurations.Logger;
import org.krst.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class ChangePasswordController {

    @FXML
    private TextField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField repeatNewPassword;

    @Autowired
    private LoginService loginService;
    @Autowired
    private Logger logger;

    public void pressEnterEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            change();
        }
    }

    public void change() {
        if (oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || repeatNewPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误原因：三个必填项不可为空");
            alert.setContentText("解决方法：输入密码");
            alert.showAndWait();
            return;
        }

        if (newPassword.getText().equals(repeatNewPassword.getText())) {
            if (Status.SUCCESS == loginService.verify(oldPassword.getText())) {
                loginService.changePassword(newPassword.getText());
                logger.logInfo(getClass().toString(), "重置密码");
            } else {
                oldPassword.clear();
                newPassword.clear();
                repeatNewPassword.clear();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("重置密码操作");
                alert.setHeaderText("错误原因：输入的原密码错误");
                alert.setContentText("解决方法：重新输入原密码");
                alert.showAndWait();
            }
        } else {
            newPassword.clear();
            repeatNewPassword.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误原因：两次输入密码不匹配");
            alert.setContentText("解决方法：重新输入新密码");
            alert.showAndWait();
        }
    }
}
