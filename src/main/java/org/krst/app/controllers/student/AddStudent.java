package org.krst.app.controllers.student;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.SneakyThrows;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.models.Status;
import org.krst.app.repositories.Logger;
import org.krst.app.services.CacheService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.Constants;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

import java.net.URL;
import java.util.ResourceBundle;

public class AddStudent implements Initializable {
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
    private ComboBox attribute;
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
    private ComboBox staff;

    private String staffId;
    private String staffName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        staff.getItems().addAll(CacheService.get().getStaffCache());

        staff.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ListCell<Staff>() {
                    @Override
                    protected void updateItem(Staff item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });

        staff.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                if (object instanceof Staff) {
                    return ((Staff)object).getName();
                }
                return null;
            }

            @Override
            public Object fromString(String string) {
                return staff.getItems().stream().filter(attr ->
                        ((Staff)attr).getName().equals(string)).findFirst().orElse(null);
            }
        });

        staff.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    staffId = "";
                    staffName = "";
                } else {
                    if (newValue instanceof Staff) {
                        Staff selectedStaff = (Staff) newValue;
                        staffId = selectedStaff.getId();
                        staffName = selectedStaff.getName();
                    }
                }
            }
        });

        refreshAttributeComboBoxContent();

        attribute.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
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

        attribute.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    leader.setText("");
                    leaderPhone.setText("");
                    altLeader.setText("");
                    altLeaderPhone.setText("");
                } else {
                    if (newValue instanceof Attribute) {
                        Attribute selectedAttribute = (Attribute)newValue;
                        if (Constants.CREATE_PROMPT.equals(selectedAttribute.getAttribute())) {
                            KRSTManagementSoftware.openNewWindow(
                                    FXMLLoader.load(getClass().getResource("/views/share/AddAttribute.fxml")),
                                    "新建所属堂区",
                                    false);
                            refreshAttributeComboBoxContent();
                            attribute.getSelectionModel().selectLast();
                        } else {
                            leader.setText(selectedAttribute.getLeader());
                            leaderPhone.setText(selectedAttribute.getLeaderPhone());
                            altLeader.setText(selectedAttribute.getAltLeader());
                            altLeaderPhone.setText(selectedAttribute.getAltLeaderPhone());
                        }
                    }
                }
            }
        });

        attribute.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                if (object instanceof Attribute) {
                    return ((Attribute)object).getAttribute();
                }
                return null;
            }

            @Override
            public Object fromString(String string) {
                return attribute.getItems().stream().filter(attr ->
                        ((Attribute)attr).getAttribute().equals(string)).findFirst().orElse(string);
            }
        });
    }

    private void refreshAttributeComboBoxContent() {
        attribute.getItems().clear();
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(CacheService.get().getAttributeCache());
    }

    public void approve() {
        Student student = new Student();
        student.setId(id.getText());
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("添加学生失败");
            alert.setHeaderText("失败原因：未填入学生编号");
            alert.setContentText("解决方法：请输入学生编号");
            alert.showAndWait();
        }
        student.setName(name.getText());
        student.setBaptismalName(baptismalName.getText());
        student.setGender(gender_male.isSelected() ? "男" : "女");
        student.setBirthday(birthday.getValue());
        student.setIsGregorianCalendar(isGregorianCalendar.isSelected());
        student.setBaptismalDate(baptismalDate.getValue());
        student.setConfirmationDate(confirmationDate.getValue());
        student.setMarriageDate(marriageDate.getValue());
        student.setDeathDate(deathDate.getValue());
        Object attributeValue = attribute.getValue();
        String attributeStringValue;
        if (attributeValue instanceof Attribute) {
            attributeStringValue = ((Attribute)attributeValue).getAttribute();
        } else {
            attributeStringValue = attribute.getEditor().getText();
        }
        student.setAttribute(new Attribute(
                attributeStringValue,
                leader.getText(),
                leaderPhone.getText(),
                altLeader.getText(),
                altLeaderPhone.getText()
        ));
        student.setPhone(phone.getText());
        student.setAltPhone(altPhone.getText());
        student.setAddress(address.getText());
        student.setExperience(experience.getText());
        student.setResource(resource.getText());
        student.setTalent(talent.getText());
        student.setStaffId(staffId);
        student.setStaffName(staffName);

        Status status = DatabaseFactory.getDatabase(DatabaseType.STUDENT).save(student);
        if (status == Status.CONSTRAINT_VIOLATION) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("添加学生错误");
            alert.setHeaderText("错误原因：使用已存在的学生编号");
            alert.setContentText("解决方法：请输入不同的学生编号");
            alert.showAndWait();
        } else if (status == Status.ERROR) {
            Logger.logError(getClass().toString(), "添加学生失败，编号：" + id.getText() + "，姓名：" + name.getText());
            CommonUtils.alertOperationError("添加学生操作");
            KRSTManagementSoftware.closeNewWindow();
        } else {
            Logger.logInfo(getClass().toString(), "添加学生成功，编号：" + id.getText() + "，姓名：" + name.getText());
            KRSTManagementSoftware.closeNewWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }

}
