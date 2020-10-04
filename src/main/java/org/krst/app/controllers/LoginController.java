package org.krst.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.krst.app.domains.Log;
import org.krst.app.repositories.AdminDatabase;
import org.krst.app.repositories.Database;
import org.krst.app.repositories.Logger;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

public class LoginController {

    @FXML
    private TextField textField_password;

    private final AdminDatabase adminDatabase = (AdminDatabase)(DatabaseFactory.getDatabase(DatabaseType.ADMIN));

    public void pressEnterEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }

    public void login() {
        Logger.logInfo(getClass().toString(), "USER LOGIN");
        String password = textField_password.getText();
        if (password == null || password.isEmpty()) {
            // LEAVE EMPTY
        } else if (adminDatabase.verify(password)) {
            // TODO jump to MainWindow
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("成功");
            alert.setContentText("剩余可尝试的次数: " + adminDatabase.getRemainingRetryTimes());
            Logger.logInfo(getClass().toString(), "用户登陆系统");
            alert.showAndWait();
        } else if (adminDatabase.getRemainingRetryTimes() > 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("密码错误，请重新输入");
            alert.setContentText("剩余可尝试的次数: " + adminDatabase.getRemainingRetryTimes());
            Logger.logWarn(getClass().toString(), "密码错误： " + password);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("尝试次数过多，系统已锁定");
            alert.setContentText("请联系管理员进行解锁");
            Logger.logWarn(getClass().toString(), "密码错误达到最大可尝试次数，已锁定系统");
            alert.showAndWait();
        }
    }
}
