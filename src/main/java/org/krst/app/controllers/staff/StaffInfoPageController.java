package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.krst.app.domains.Evaluation;
import org.krst.app.domains.Internship;
import org.krst.app.domains.Visit;

@FXMLController
public class StaffInfoPageController {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField baptismalName;
    @FXML private ComboBox<String> gender;
    @FXML private DatePicker birthday;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;
    @FXML private TextField title;
    @FXML private TextArea responsibility;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private TextArea address;
    @FXML private TextArea experience;
    @FXML private TextArea talent;
    @FXML private TextArea resource;
    @FXML private TextArea education;

    @FXML private Button change;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button close;

    @FXML private TableView<Visit> visit;
    @FXML private TableView<Internship> internship;
    @FXML private TableView<Evaluation> evaluation;
    @FXML private TableView relationship;

    @FXML public void initialize() {

    }

    public void change() {

    }

    public void accept() {

    }

    public void delete() {

    }

    public void cancel() {

    }

    public void close() {

    }

    public void addVisit() {

    }

    public void addInternship() {

    }

    public void addEvaluation() {

    }

    public void addRelationship() {

    }
}
