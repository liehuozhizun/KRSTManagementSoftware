package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.krst.app.models.Status;
import org.krst.app.configurations.Logger;
import org.krst.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class ChangePasswordController {
    @FXML private TextField oldPassword;
    @FXML private PasswordField newPassword, repeatedNewPassword;

    @Autowired private LoginService loginService;
    @Autowired private Logger logger;

    public void pressEnterEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            change();
        }
    }

    public void change() {
        if (oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || repeatedNewPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误原因：三个必填项不可为空");
            alert.setContentText("解决方法：补充缺失项");
            alert.showAndWait();
            return;
        }

        if (newPassword.getText().equals(repeatedNewPassword.getText())) {
            Status status = loginService.verify(oldPassword.getText());
            if (Status.SUCCESS == status) {
                loginService.changePassword(newPassword.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("重置密码操作");
                alert.setHeaderText("密码更改成功");
                alert.setContentText("新密码将在重启程序后生效");
                alert.showAndWait();
                logger.logInfo(getClass().toString(), "重置密码成功");
                close();
            } if (Status.CONSTRAINT_VIOLATION == status) {
                logger.logFetal(getClass().toString(), "!!! 非法入侵修改密码 !!!");
                close();
            } else {
                oldPassword.clear();
                newPassword.clear();
                repeatedNewPassword.clear();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("重置密码操作");
                alert.setHeaderText("错误原因：输入的原密码错误");
                alert.setContentText("解决方法：重新输入原密码");
                alert.showAndWait();
            }
        } else {
            newPassword.clear();
            repeatedNewPassword.clear();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误原因：两次输入密码不匹配");
            alert.setContentText("解决方法：重新输入新密码");
            alert.showAndWait();
        }
    }

    private void close() {
        ((Stage)oldPassword.getScene().getWindow()).close();
    }
}
