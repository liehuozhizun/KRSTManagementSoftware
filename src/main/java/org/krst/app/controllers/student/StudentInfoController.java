package org.krst.app.controllers.student;
import ch.qos.logback.core.net.SyslogOutputStream;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.krst.app.domains.Student;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@FXMLController
public class StudentInfoController implements Initializable {
    private String studentid;
    @FXML
    private TextField leader;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField baptismalName;
    @FXML
    private TextField leaderPhone;
    @FXML
    private TextField altLeader;
    @FXML
    private TextField altLeaderPhone;
    @FXML
    private TextField phone;
    @FXML
    private TextField altPhone;
//    点击更改需要改换界面的
    @FXML
    private TextField arrtibute;
    @FXML
    private TextField gender;
    @FXML
    private TextField birthday;
    @FXML
    private TextField isGregorianCalendar;
    @FXML
    private TextField baptismalDate;
    @FXML
    private TextField confirmationDate;
    @FXML
    private TextField marriageDate;
    @FXML
    private TextField deathDate;
//     点击更改需要改换界面的
    @FXML
    private javafx.scene.control.TextArea address;
    @FXML
    private javafx.scene.control.TextArea experience;
    @FXML
    private javafx.scene.control.TextArea talent;
    @FXML
    private TextArea resource;
//    这啥意思
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DataPassService dataPassService;


//    studentid=0001

    public void change(){
        System.out.println("change Button Clicked");
    }
    public void delate(){
        System.out.println("Delate Button Clicked");
    }
    public void back(){
        System.out.println("Back Button Clicked");
    }
//dataPassService.getValue()
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataPassService.getValue();
        System.out.println( dataPassService.getValue());
        studentid=id.getText();
        Student student;
        Optional<Student> studentOptional = studentRepository.findById((studentid));
//        Student student;
//        if (studentOptional.isPresent()) {
//            student = studentOptional.get();
//            System.out.println(student.getName());
//        } else {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.showAndWait();
//        }


    }


}
