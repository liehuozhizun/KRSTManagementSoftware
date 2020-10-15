package org.krst.app.controllers.staff;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Staff;
import org.krst.app.models.Status;
import org.krst.app.repositories.Logger;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

public class AddStaff {

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

    public void approve() {
        Staff staff = new Staff();
        staff.setId(id.getText());
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("添加员工失败");
            alert.setHeaderText("失败原因：未填入员工编号");
            alert.setContentText("解决方法：请输入员工编号");
            alert.showAndWait();
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

        Status status = DatabaseFactory.getDatabase(DatabaseType.STAFF).save(staff);
        if (status == Status.CONSTRAINT_VIOLATION) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("添加员工错误");
            alert.setHeaderText("错误原因：使用已存在的员工编号");
            alert.setContentText("解决方法：请输入不同的员工编号");
            alert.showAndWait();
        } else if (status == Status.ERROR) {
            Logger.logError(getClass().toString(), "添加员工失败，编号：" + id.getText() + "，姓名：" + name.getText());
            CommonUtils.alertOperationError("添加员工操作");
            KRSTManagementSoftware.closeNewWindow();
        } else {
            Logger.logInfo(getClass().toString(), "添加员工成功，编号：" + id.getText() + "，姓名：" + name.getText());
            KRSTManagementSoftware.closeNewWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }
}
