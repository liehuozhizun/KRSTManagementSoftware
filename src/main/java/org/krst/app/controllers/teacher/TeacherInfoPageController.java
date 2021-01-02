package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Teacher;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class TeacherInfoPageController implements Initializable {

    @FXML private TextField id,name,baptismalName;
    @FXML private TextField leader;
    @FXML private TextField leaderPhone;
    @FXML private TextField altLeader;
    @FXML private TextField altLeaderPhone;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private DatePicker birthday;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;
    @FXML private TextArea address;
    @FXML private TextArea experience;
    @FXML private TextArea talent;
    @FXML private TextArea resource;
    @FXML private Button confirm,delete,close,change,cancel;
    @FXML private ComboBox gender;
    @FXML private ComboBox <Attribute>changeableAttribute;
    @FXML private ComboBox <Staff>changeableStaff;
    @FXML private CheckBox isGregorianCalendar;



    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;
    @Autowired
    private DataPassService dataPassService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Teacher selectedOne =(Teacher) dataPassService.getValue();
        start(selectedOne);
        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            gender.setPromptText((String) gender.getSelectionModel().getSelectedItem());
        });
        changeableAttribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeableAttribute.setPromptText((changeableAttribute.getSelectionModel().getSelectedItem().getAttribute()));
        });
        changeableStaff.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeableStaff.setPromptText(changeableStaff.getSelectionModel().getSelectedItem().getName());

        });

//
    }

        public void close() {
            ((Stage)id.getScene().getWindow()).close();
        }
        public  void start(Teacher selectedOne){
            id.setId(selectedOne.getId());
            name.setText(selectedOne.getName());
            baptismalName.setText(selectedOne.getBaptismalName());
            //gender_male.setSelected(selectedOne.getGender().equals("男"));
            birthday.setValue(selectedOne.getBirthday());
            isGregorianCalendar.setSelected(selectedOne.getIsGregorianCalendar());
            baptismalDate.setValue(selectedOne.getBaptismalDate());
            confirmationDate.setValue(selectedOne.getConfirmationDate());
            marriageDate.setValue(selectedOne.getMarriageDate());
            deathDate.setValue(selectedOne.getDeathDate());
            //attribute.setText(selectedOne.getAttribute().getAttribute());
            leader.setText(selectedOne.getAttribute().getLeader());
            leaderPhone.setText(selectedOne.getAttribute().getLeaderPhone());
            altLeader.setText(selectedOne.getAttribute().getAltLeader());
            altLeaderPhone.setText(selectedOne.getAttribute().getAltLeaderPhone());
            phone.setText(selectedOne.getPhone());
            altPhone.setText(selectedOne.getAltPhone());
            address.setText(selectedOne.getAddress());
            experience.setText(selectedOne.getExperience());
            talent.setText(selectedOne.getTalent());
            resource.setText(selectedOne.getResource());
            System.out.println("reach end");
            gender.setPromptText(selectedOne.getGender());
            changeableAttribute.setPromptText(selectedOne.getAttribute().getAttribute());
            changeableStaff.setPromptText(selectedOne.getStaff().getName());
            gender.getItems().add("男");
            gender.getItems().add("女");
            changeableAttribute.getItems().addAll(attributeRepository.findAll());
            changeableStaff.getItems().addAll(staffRepository.findAll());
        }
        public void change(){
        changeSet(name);
        changeSet(baptismalName);
        changeSet(leader);
        changeSet(leaderPhone);
        changeSet(altLeader);
        changeSet(altLeaderPhone);
        changeSet(phone);
        changeSet(altPhone);
        changeSet(id);
        changeAreaSet(address);
        changeAreaSet(experience);
        changeAreaSet(talent);
        changeAreaSet(resource);
        changeDateSet(baptismalDate);
        changeDateSet(confirmationDate);
        changeDateSet(marriageDate);
        changeDateSet(deathDate);
        changeComboboxSet(gender);
        changeComboboxSet(changeableAttribute);
        changeComboboxSet(changeableStaff);
        buttonHide(change);
        buttonHide(delete);
        buttonHide(close);
        buttonShow(confirm);
        buttonShow(cancel);
        }
        public void delete(){
        teacherRepository.delete((Teacher) dataPassService.getValue());
            cacheService.refreshAttributeCache();
            cacheService.refreshStaffCache();
            cacheService.refreshCourseTemplateCache();
        }
        public void confirm(){
            Teacher teacher = new Teacher();
            teacher.setId(id.getText());
            teacher.setName(name.getText());
            teacher.setBaptismalName(baptismalName.getText());
            teacher.setBirthday(birthday.getValue());
            teacher.setIsGregorianCalendar(isGregorianCalendar.isSelected());
            teacher.setBaptismalDate(baptismalDate.getValue());
            teacher.setConfirmationDate(confirmationDate.getValue());
            teacher.setMarriageDate(marriageDate.getValue());
            teacher.setDeathDate(deathDate.getValue());
            teacher.setPhone(phone.getText());
            teacher.setAltPhone(altPhone.getText());
            teacher.setAddress(address.getText());
            teacher.setExperience(experience.getText());
            teacher.setResource(resource.getText());
            teacher.setTalent(talent.getText());
            teacher.setAttribute(changeableAttribute.getSelectionModel().getSelectedItem());
            teacher.setStaff(changeableStaff.getSelectionModel().getSelectedItem());
            teacher.setGender(gender.getPromptText());
            close();
        }
        public void cancel(){
        close();
        }
        private void changeSet(TextField obj){
        obj.setDisable(false);
        obj.setVisible(true);
        obj.setEditable(true);
        }
        private void changeAreaSet(TextArea obj){
            obj.setDisable(false);
            obj.setVisible(true);
            obj.setEditable(true);
        }
        private void changeDateSet(DatePicker obj){
            obj.setDisable(false);
            obj.setVisible(true);
            obj.setEditable(true);
        }
        private void changeComboboxSet(ComboBox obj){
            obj.setDisable(false);
            obj.setVisible(true);
            obj.setEditable(true);
        }
        private void buttonHide(Button obj){
            obj.setDisable(true);
            obj.setVisible(false);
        }
        private void buttonShow(Button obj){
            obj.setDisable(false);
            obj.setVisible(true);

        }

}
