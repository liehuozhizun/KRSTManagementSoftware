package org.krst.app.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.views.system.ChangePassword;
import org.krst.app.views.system.Remainder;
import org.krst.app.views.system.SystemInfoPage;

@FXMLController
public class MainWindowController {
    @FXML BorderPane basePane;
    @FXML SplitPane student;
    @FXML SplitPane teacher;
    @FXML SplitPane staff;
    @FXML SplitPane course;
    @FXML SplitPane other;

    @FXML public void initialize() {
        KRSTManagementSoftware.resizeWindow(835.0, 1250.0, "科瑞斯特管理软件");
    }

    // ----------------- pane switcher -----------------
    public void showStudent() {
        student.toFront();
    }
    public void showTeacher() {
        teacher.toFront();
    }
    public void showStaff() {
        staff.toFront();
    }
    public void showCourse() {
        course.toFront();
    }
    public void showOther() {
        other.toFront();
    }

    // ----------------- system control -----------------
    public void system_changePassword() {
        KRSTManagementSoftware.openWindow(ChangePassword.class);
    }
    public void system_info() {
        KRSTManagementSoftware.openWindow(SystemInfoPage.class);
    }
    public void system_exit() {
        ((Stage)basePane.getScene().getWindow()).close();
    }
    public void database_destroy() {

    }
    public void database_import() {

    }
    public void database_export() {

    }
    public void other_remainder() {
        KRSTManagementSoftware.openWindow(Remainder.class);
    }
    public void other_log(){

    }
}
