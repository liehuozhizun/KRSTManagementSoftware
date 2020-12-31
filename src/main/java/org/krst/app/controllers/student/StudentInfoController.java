package org.krst.app.controllers.student;
import ch.qos.logback.core.net.SyslogOutputStream;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import org.krst.app.domains.Student;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@FXMLController
public class StudentInfoController {
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
//    Student student = student.equals()；
//    student.getid();
//    StudentRepository a = studentRepository;
//    public void findStudent(){
//        studentid="0001";
//        studentRepository.getOne(studentid);
//        System.out.println("Back Button Clicked");
//    }
//
//    public void main(String[] args){
//        studentid="0001";
//        studentRepository.getOne(studentid);
//    }
//    System.out.println(student.id)

//    public
//    public void leader(){
//        System.out.println("leader");
//    }
}

//public class Wordid{
//    public void main (String[] args){
//        System.out.print(id);
//    }
//}
//public void change(){
//

//
