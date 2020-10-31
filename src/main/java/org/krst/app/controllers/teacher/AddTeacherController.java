package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Teacher;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class AddTeacherController implements Initializable {

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
    private ComboBox<Staff> staff;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        staff.getItems().addAll(cacheService.getStaffs());

        staff.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
            @Override
            public ListCell<Staff> call(ListView<Staff> param) {
                return new ListCell<Staff>() {
                    @Override
                    protected void updateItem(Staff item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " [" + item.getId() + "]");
                        }
                    }
                };
            }
        });

        staff.setConverter(new StringConverter<Staff>() {
            @Override
            public String toString(Staff staff) {
                return staff == null ? null : staff.getName() + " [" + staff.getId() + "]";
            }

            @Override
            public Staff fromString(String string) {
                return staff.getItems().stream().filter(staff ->
                        staff.getName().equals(string)).findFirst().orElse(null);
            }
        });

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

        attribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                leader.setText("");
                leaderPhone.setText("");
                altLeader.setText("");
                altLeaderPhone.setText("");
            } else {
                if (Constants.CREATE_PROMPT.equals(newValue.getAttribute())) {
                    KRSTManagementSoftware.openWindow(AddAttribute.class, true);
                    refreshAttributeComboBoxContent();
                    attribute.getSelectionModel().selectLast();
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
                return attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void refreshAttributeComboBoxContent() {
        attribute.getItems().clear();
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());
    }

    public void approve() {
        Teacher teacher = new Teacher();
        teacher.setId(id.getText());
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建教师档案失败");
            alert.setHeaderText("失败原因：未填入教师编号");
            alert.setContentText("解决方法：请输入教师编号");
            alert.showAndWait();
        }
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
        teacher.setStaff(staff.getValue());

        if (teacherRepository.existsById(teacher.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("新建教师档案错误");
            alert.setHeaderText("错误原因：使用已存在的教师编号");
            alert.setContentText("解决方法：请输入不同的教师编号");
            alert.showAndWait();
        } else {
            teacherRepository.save(teacher);
            logger.logInfo(getClass().toString(), "新建教师档案，编号：{}，姓名：{}", id.getText(), name.getText());
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }

}
