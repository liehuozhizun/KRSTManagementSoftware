package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.models.Status;
import org.krst.app.configurations.Logger;
import org.krst.app.services.LoginService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.views.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private Logger logger;

    @FXML
    private PasswordField textField_password;

    public void pressEnterEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            login();
        }
    }

    public void login() {
        logger.logInfo(getClass().toString(), "用户成功登录");
        String password = textField_password.getText();
        if (password == null || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("登陆提示");
            alert.setHeaderText("请输入密码");
            alert.showAndWait();
            return;
        }

        Status status = loginService.verify(password);
        switch (status) {
            case SUCCESS:
                try {
                    KRSTManagementSoftware.switchScene(MainWindow.class);
                } catch (Exception e) {
                    logger.logFetal(getClass().toString(), e.getMessage());
                    CommonUtils.alertSystemError(e.getMessage());
                }
                break;
            case CONSTRAINT_VIOLATION:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("登陆警告");
                alert.setHeaderText("尝试次数过多，系统已锁定");
                alert.setContentText("请联系管理员进行解锁");
                logger.logWarn(getClass().toString(), "密码错误达到最大可尝试次数，已锁定系统");
                alert.showAndWait();
                break;
            case ERROR:
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("登陆提示");
                alert.setHeaderText("密码错误，请重新输入");
                alert.setContentText("剩余可尝试的次数: " + loginService.getRemainingRetryTimes());
                alert.showAndWait();
                break;
        }
    }
}
