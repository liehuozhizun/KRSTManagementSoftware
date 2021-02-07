package org.krst.app.controllers.person;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.krst.app.configurations.Logger;
import org.krst.app.domains.Person;
import org.krst.app.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class AddPersonController {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField baptismalName;
    @FXML private RadioButton gender_male;
    @FXML private DatePicker birthday;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private TextArea address;
    @FXML private TextArea job;

    @Autowired private PersonRepository personRepository;
    @Autowired private Logger logger;

    public void accept() {
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建普通人档案失败");
            alert.setHeaderText("失败原因：未填入普通人编号");
            alert.setContentText("解决方法：请输入普通人编号");
            alert.show();
            return;
        } else if (personRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建普通人档案错误");
            alert.setHeaderText("错误原因：使用已存在的普通人编号");
            alert.setContentText("解决方法：请输入不同的普通人编号");
            alert.show();
            return;
        }

        personRepository.save(loadInfoIntoPersonModel());
        logger.logInfo(getClass().toString(), "新建普通热档案，编号：{}，姓名：{}", id.getText(), name.getText());
        close();
    }

    private Person loadInfoIntoPersonModel() {
        return new Person(id.getText(), name.getText(), baptismalName.getText(), gender_male.isSelected() ? "男" : "女",
                birthday.getValue(), isGregorianCalendar.isSelected(), phone.getText(), altPhone.getText(),
                address.getText(), job.getText(), null);
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }
}
