package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
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

@FXMLController
public class TeacherInfoPageController {

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
    @FXML private ComboBox <String>gender;
    @FXML private ComboBox <Attribute>changeableAttribute;
    @FXML private ComboBox <Staff>changeableStaff;
    @FXML private CheckBox isGregorianCalendar;



    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;
    @Autowired
    private DataPassService dataPassService;


    @FXML
    public void initialize() {
        Teacher selectedOne =(Teacher) dataPassService.getValue();

        start(selectedOne);
        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            gender.setPromptText( gender.getSelectionModel().getSelectedItem());
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
            id.setText(selectedOne.getId());
            name.setText(selectedOne.getName());
            baptismalName.setText(selectedOne.getBaptismalName());
            birthday.setValue(selectedOne.getBirthday());
            isGregorianCalendar.setSelected(selectedOne.getIsGregorianCalendar());
            baptismalDate.setValue(selectedOne.getBaptismalDate());
            confirmationDate.setValue(selectedOne.getConfirmationDate());
            marriageDate.setValue(selectedOne.getMarriageDate());
            deathDate.setValue(selectedOne.getDeathDate());
            if (selectedOne.getAttribute() != null) {
                leader.setText(selectedOne.getAttribute().getLeader());
                leaderPhone.setText(selectedOne.getAttribute().getLeaderPhone());
                altLeader.setText(selectedOne.getAttribute().getAltLeader());
                changeableAttribute.setPromptText(selectedOne.getAttribute().getAttribute());
                altLeaderPhone.setText(selectedOne.getAttribute().getAltLeaderPhone());
            }
            phone.setText(selectedOne.getPhone());
            altPhone.setText(selectedOne.getAltPhone());
            address.setText(selectedOne.getAddress());
            experience.setText(selectedOne.getExperience());
            talent.setText(selectedOne.getTalent());
            resource.setText(selectedOne.getResource());
            System.out.println("reach end");
            gender.setPromptText(selectedOne.getGender());
            if (selectedOne.getStaff() != null) {
                changeableStaff.setPromptText(selectedOne.getStaff().getName());
            }
            gender.getItems().addAll("男","女");
            if(cacheService!=null) {
                changeableAttribute.getItems().addAll(cacheService.getAttributes());
                changeableStaff.getItems().addAll(cacheService.getStaffs());
            }
            changeableAttribute.setCellFactory(new Callback<ListView<Attribute>, ListCell<Attribute>>() {
                @Override
                public ListCell<Attribute> call(ListView<Attribute> param) {
                    return new ListCell<Attribute>(){
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
            changeableStaff.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
                @Override
                public ListCell<Staff> call(ListView<Staff> param) {
                    return new ListCell<Staff>(){
                        @Override
                        protected void updateItem(Staff item,boolean empty){
                            super.updateItem(item,empty);
                            if(item==null||empty){
                                setGraphic(null);
                            }else{
                                setText(item.getName());
                            }
                        }

                    };
                }
            });

        }

        public void change() {
            changeSet(name);
            changeSet(baptismalName);
            //changeSet(leader);
            //changeSet(leaderPhone);
            //changeSet(altLeader);
            //changeSet(altLeaderPhone);
            changeSet(phone);
            changeSet(altPhone);
            changeSet(id);
            changeAreaSet(address);
            changeAreaSet(experience);
            changeAreaSet(talent);
            changeAreaSet(resource);
            changeDateSet(birthday);
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

        public void delete() {
            logger.logInfo(getClass().toString(), "删除教师档案，编号：{}，姓名：{}", id.getText(), name.getText());
            teacherRepository.delete((Teacher) dataPassService.getValue());
        }

        public void confirm() {
            Teacher selectedOne =(Teacher) dataPassService.getValue();
            if(!id.getText().equals(selectedOne.getId())
                    &&teacherRepository.existsById(id.getId())
            ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("更新教师档案错误");
                alert.setHeaderText("错误原因：使用已存在的教师编号");
                alert.setContentText("解决方法：请输入不同的教师编号");
                alert.showAndWait();
            }
            else{
                if(!id.getText().equals(selectedOne.getId())) {
                    teacherRepository.delete((Teacher) dataPassService.getValue());
                }
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
                teacherRepository.save(teacher);
                close();}
        }
        public void cancel() {
        close();
        }
        private void changeSet(TextField textField) {
            textField.setDisable(false);
            textField.setVisible(true);
            textField.setEditable(true);
        }
        private void changeAreaSet(TextArea textArea) {
            textArea.setDisable(false);
            textArea.setVisible(true);
            textArea.setEditable(true);
        }
        private void changeDateSet(DatePicker datePicker) {
            datePicker.setDisable(false);
            datePicker.setVisible(true);
            datePicker.setEditable(true);
        }
        private void changeComboboxSet(ComboBox comboBox) {
            comboBox.setDisable(false);
            comboBox.setVisible(true);
            comboBox.setEditable(true);
        }
        private void buttonHide(Button button) {
            button.setDisable(true);
            button.setVisible(false);
        }
        private void buttonShow(Button button){
            button.setDisable(false);
            button.setVisible(true);

        }


}
