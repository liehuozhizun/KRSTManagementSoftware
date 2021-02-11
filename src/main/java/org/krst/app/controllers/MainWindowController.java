package org.krst.app.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@FXMLController
public class MainWindowController {
    @FXML BorderPane basePane;
    @FXML SplitPane student;
    @FXML SplitPane teacher;
    @FXML SplitPane staff;
    @FXML SplitPane course;
    @FXML SplitPane other;

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

    }
    public void system_info() {

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

    }
    public void other_log(){

    }
}
