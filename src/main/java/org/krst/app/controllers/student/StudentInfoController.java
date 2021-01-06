package org.krst.app.controllers.student;
import ch.qos.logback.core.net.SyslogOutputStream;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.val;
import org.krst.app.domains.Student;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

@FXMLController
public class StudentInfoController {
    private Student student;

    private String originalStudentId;
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
    private ComboBox<Attribute> attribute;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<Staff> staff;//还一点没写
    @FXML
    private CheckBox isGregorianCalendar;
    @FXML
    private DatePicker birthday;
    @FXML
    private DatePicker baptismalDate;
    @FXML
    private DatePicker confirmationDate;
    @FXML
    private DatePicker marriageDate;
    @FXML
    private DatePicker deathDate;
//     点击更改需要改换界面的
    @FXML
    private javafx.scene.control.TextArea address;
    @FXML
    private javafx.scene.control.TextArea experience;
    @FXML
    private javafx.scene.control.TextArea talent;
    @FXML
    private TextArea resource;
    @FXML
    private Button confirm;
    @FXML
    private Button delete;
    @FXML
    private Button cancel;
    @FXML
    private Button change;
    @FXML
    private Button close;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;

    @Autowired
    private DataPassService dataPassService;

    public void change(){
        System.out.println("change");
        isEditable(true);
        isInChangePage(true);
        attribute.setValue(student.getAttribute());
    }

    public void delete(){
        studentRepository.deleteById(originalStudentId);//我觉得这里要有alart，不然失手删掉了很难找回来
        turnOff();
    }

    public void close(){
        turnOff();
    }

    public void confirm(){
        isEditable(false);
        Student student = new Student();
        student.setId(id.getText());
        System.out.println(id.getText());
        System.out.println(originalStudentId);
        System.out.println(! id.getText().equals(originalStudentId));
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建学生档案失败");
            alert.setHeaderText("失败原因：未填入学生编号");
            alert.setContentText("解决方法：请输入学生编号");
            alert.showAndWait();
        }
        student.setName(name.getText());
        student.setBaptismalName(baptismalName.getText());
        student.setGender(gender.getValue());
        student.setBirthday(birthday.getValue());
        student.setIsGregorianCalendar(isGregorianCalendar.isSelected());
        student.setBaptismalDate(baptismalDate.getValue());
        student.setConfirmationDate(confirmationDate.getValue());
        student.setMarriageDate(marriageDate.getValue());
        student.setDeathDate(deathDate.getValue());
        student.setAttribute(attribute.getValue());
        student.setPhone(phone.getText());
        student.setAltPhone(altPhone.getText());
        student.setAddress(address.getText());
        student.setExperience(experience.getText());
        student.setResource(resource.getText());
        student.setTalent(talent.getText());
        student.setStaff(staff.getValue());
        if (studentRepository.existsById(id.getText()) && !id.getText().equals(originalStudentId)) {//这个地方如果id=Null 的话就需要报错（还没做等下问）
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建教师档案错误");
            alert.setHeaderText("错误原因：使用已存在的学生编号");
            alert.setContentText("解决方法：请输入未用过的的学生编号");
            alert.showAndWait();
        } else {
            System.out.println(id.getText());
            System.out.println(originalStudentId);
            System.out.println("不存在的");
            studentRepository.deleteById(originalStudentId);
            studentRepository.save(student);
            logger.logInfo(getClass().toString(), "新建学生档案，编号：{}，姓名：{}", id.getText(), name.getText());
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel(){
        isInChangePage(false);
        isEditable(false);
        postStudentInfo();
    }

    @FXML
    public void initialize() {
        student = (Student) dataPassService.getValue();
        gender.getItems().addAll("男","女");
        postStudentInfo();
    }


    private void postStudentInfo(){
        originalStudentId=student.getId();
        id.setText(student.getId());
        name.setText(student.getName());
        baptismalName.setText(student.getBaptismalName());
        birthday.setValue(student.getBirthday());
        isGregorianCalendar.setSelected(student.getIsGregorianCalendar());
        gender.setValue(student.getGender());
        baptismalDate.setValue(student.getBaptismalDate());
        confirmationDate.setValue(student.getConfirmationDate());
        marriageDate.setValue(student.getMarriageDate());
        deathDate.setValue(student.getDeathDate());
        address.setText(student.getAddress());
        experience.setText(student.getExperience());
        talent.setText(student.getTalent());
        resource.setText(student.getResource());
        gender.getSelectionModel().select(student.getGender());
        refreshAttributeComboBoxContent();
        attribute.setCellFactory(new Callback<ListView<Attribute>, ListCell<Attribute>>() {
            @Override
            public ListCell<Attribute> call(ListView<Attribute> param) {
                return new ListCell<Attribute>() {
                    @Override
                    protected void updateItem(Attribute item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getAttribute());
                        }
                    }
                };
            }
        });
        attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
        attribute.setValue(student.getAttribute());
        leader.setText(student.getAttribute().getLeader());
        leaderPhone.setText(student.getAttribute().getLeaderPhone());
        altLeader.setText(student.getAttribute().getAltLeader());
        altLeaderPhone.setText(student.getAttribute().getAltLeaderPhone());
    }

    private void refreshAttributeComboBoxContent() {
        attribute.getItems().clear();
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());
    }

    private void isEditable(boolean editable){
//        attribute.setEditable(editable);
        attribute.setDisable(!editable);
        gender.setDisable(!editable);
        textFieldEditable(id,editable);
        textFieldEditable(name,editable);
        textFieldEditable(baptismalName,editable);
        textFieldEditable(experience,editable);
        textFieldEditable(address,editable);
        textFieldEditable(talent,editable);
        textFieldEditable(resource,editable);
        textFieldEditable(birthday,editable);
        textFieldEditable(baptismalDate,editable);
        textFieldEditable(confirmationDate,editable);
        textFieldEditable(marriageDate,editable);
        textFieldEditable(deathDate,editable);
    }

    private void textFieldEditable(TextField textField, Boolean isEditable){
        textField.setEditable(isEditable);
        textField.setDisable(!isEditable);
    }

    private void textFieldEditable(TextArea textArea, Boolean isEditable){
        textArea.setEditable(isEditable);
        textArea.setDisable(!isEditable);
    }

    private void textFieldEditable(DatePicker datePicker, Boolean isEditable){
        datePicker.setEditable(isEditable);
        datePicker.setDisable(!isEditable);
    }

    private void isButtonShow(Button button, Boolean show){
        button.setDisable(!show);
        button.setVisible(show);
    }

    private void isInChangePage(Boolean boo){
        isButtonShow(confirm,boo);
        isButtonShow(cancel,boo);
        isButtonShow(delete,!boo);
        isButtonShow(close,!boo);
        isButtonShow(change,!boo);
    }

    public void turnOff() {
        ((Stage)id.getScene().getWindow()).close();
    }
}
