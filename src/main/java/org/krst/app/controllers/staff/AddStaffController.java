package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.krst.app.domains.Staff;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : None
 * Out : None
 */
@FXMLController
public class AddStaffController {

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
    private TextField title;
    @FXML
    private TextArea responsibility;
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

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private Logger logger;
    @Autowired
    private CacheService cacheService;

    public void approve() {
        Staff staff = new Staff();
        staff.setId(id.getText());
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建员工档案失败");
            alert.setHeaderText("失败原因：未填入员工编号");
            alert.setContentText("解决方法：请输入员工编号");
            alert.show();
            return;
        } else if (staffRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建员工档案错误");
            alert.setHeaderText("错误原因：使用已存在的员工编号");
            alert.setContentText("解决方法：请输入不同的员工编号");
            alert.show();
            return;
        }

        staff.setName(name.getText());
        staff.setBaptismalName(baptismalName.getText());
        staff.setGender(gender_male.isSelected() ? "男" : "女");
        staff.setBirthday(birthday.getValue());
        staff.setIsGregorianCalendar(isGregorianCalendar.isSelected());
        staff.setBaptismalDate(baptismalDate.getValue());
        staff.setConfirmationDate(confirmationDate.getValue());
        staff.setMarriageDate(marriageDate.getValue());
        staff.setDeathDate(deathDate.getValue());
        staff.setTitle(title.getText());
        staff.setResponsibility(responsibility.getText());
        staff.setPhone(phone.getText());
        staff.setAltPhone(altPhone.getText());
        staff.setAddress(address.getText());
        staff.setExperience(experience.getText());
        staff.setResource(resource.getText());
        staff.setTalent(talent.getText());
        staff.setEducation(education.getText());

        staffRepository.save(staff);

        cacheService.refreshStaffCache();
        logger.logInfo(getClass().toString(), "新建员工档案，编号：{}，姓名：{}", id.getText(), name.getText());
        close();
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }
}
