package org.krst.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.krst.app.models.Status;
import org.krst.app.repositories.AdminDatabase;
import org.krst.app.repositories.Logger;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

public class ChangePassword {

    @FXML
    private TextField textField_oldPassword;
    @FXML
    private PasswordField textField_newPassword;
    @FXML
    private PasswordField textField_repeatNewPassword;

    private final AdminDatabase adminDatabase = (AdminDatabase)(DatabaseFactory.getDatabase(DatabaseType.ADMIN));

    public void pressEnterEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            change();
        }
    }

    public void change() {
        if (textField_oldPassword.getText().isEmpty() ||
                textField_newPassword.getText().isEmpty() ||
                textField_repeatNewPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误");
            alert.setContentText("三个必填项不可为空");
            alert.showAndWait();
            return;
        }

        if (textField_newPassword.getText().equals(textField_repeatNewPassword.getText())) {
            if (Status.SUCCESS != adminDatabase.verify(textField_oldPassword.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("重置密码操作");
                alert.setHeaderText("错误");
                alert.setContentText("原密码错误");
                alert.showAndWait();
            } else if (adminDatabase.changePassword(textField_newPassword.getText())) {
                Logger.logInfo(getClass().toString(), "重置密码：成功");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("重置密码操作");
                alert.setHeaderText("重置成功");
                alert.showAndWait();
            } else {
                CommonUtils.alertOperationError("重置密码操作");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("重置密码操作");
            alert.setHeaderText("错误");
            alert.setContentText("二次输入密码不匹配");
            alert.showAndWait();
        }
    }
}
