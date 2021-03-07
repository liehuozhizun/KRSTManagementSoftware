package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Teacher;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : None
 * Out : null, not added
 *       Teacher, newly added Teacher
 */
@FXMLController
public class AddTeacherController {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField baptismalName;
    @FXML
    private RadioButton gender_male;
    @FXML
    private DatePicker birthday;
    @FXML
    private CheckBox isGregorianCalendar;
    @FXML
    private DatePicker baptismalDate;
    @FXML
    private DatePicker confirmationDate;
    @FXML
    private DatePicker marriageDate;
    @FXML
    private DatePicker deathDate;
    @FXML
    private ComboBox<Attribute> attribute;
    @FXML
    private TextField leader;
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
    @FXML
    private TextArea address;
    @FXML
    private TextArea experience;
    @FXML
    private TextArea talent;
    @FXML
    private TextArea resource;
    @FXML
    private TextArea education;
    @FXML
    private ComboBox<Staff> staff;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private DataPassService dataPassService;
    @Autowired
    private Logger logger;

    @FXML
    public void initialize() {
        staff.getItems().addAll(cacheService.getStaffs());
        staff.setConverter(new StringConverter<Staff>() {
            @Override
            public String toString(Staff staff) {
                return staff == null ? null : staff.getNameAndId();
            }

            @Override
            public Staff fromString(String string) {
                return staff.getItems().stream().filter(staff ->
                        staff.getName().equals(string) || staff.getId().equals(string) || staff.getNameAndId().equals(string))
                        .findFirst().orElse(null);
            }
        });

        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());

        attribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                leader.setText("");
                leaderPhone.setText("");
                altLeader.setText("");
                altLeaderPhone.setText("");
            } else {
                if (Constants.CREATE_PROMPT.equals(newValue.getAttribute())) {
                    KRSTManagementSoftware.openWindow(AddAttribute.class);
                    Attribute temp = (Attribute) dataPassService.getValue();
                    if (temp != null) {
                        attribute.getItems().add(temp);
                        attribute.getSelectionModel().selectLast();
                    }
                } else {
                    leader.setText(newValue.getLeader());
                    leaderPhone.setText(newValue.getLeaderPhone());
                    altLeader.setText(newValue.getAltLeader());
                    altLeaderPhone.setText(newValue.getAltLeaderPhone());
                }
            }
        });

        attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return string == null ? null : attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void approve() {
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建教师档案失败");
            alert.setHeaderText("失败原因：未填入教师编号");
            alert.setContentText("解决方法：请输入教师编号");
            alert.show();
            return;
        } else if (teacherRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("新建教师档案错误");
            alert.setHeaderText("错误原因：使用已存在的教师编号");
            alert.setContentText("解决方法：请输入不同的教师编号");
            alert.show();
            return;
        }

        Teacher teacher = new Teacher();
        teacher.setId(id.getText());
        teacher.setName(name.getText());
        teacher.setBaptismalName(baptismalName.getText());
        teacher.setGender(gender_male.isSelected() ? "男" : "女");
        teacher.setBirthday(birthday.getValue());
        teacher.setIsGregorianCalendar(isGregorianCalendar.isSelected());
        teacher.setBaptismalDate(baptismalDate.getValue());
        teacher.setConfirmationDate(confirmationDate.getValue());
        teacher.setMarriageDate(marriageDate.getValue());
        teacher.setDeathDate(deathDate.getValue());
        teacher.setAttribute(attribute.getValue());
        teacher.setPhone(phone.getText());
        teacher.setAltPhone(altPhone.getText());
        teacher.setAddress(address.getText());
        teacher.setExperience(experience.getText());
        teacher.setResource(resource.getText());
        teacher.setTalent(talent.getText());
        teacher.setEducation(education.getText());
        teacher.setStaff(staff.getValue());

        Teacher savedTeacher = teacherRepository.save(teacher);
        dataPassService.setValue(savedTeacher);
        logger.logInfo(getClass().toString(), "新建教师档案，编号：{}，姓名：{}", id.getText(), name.getText());
        close();
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

}
