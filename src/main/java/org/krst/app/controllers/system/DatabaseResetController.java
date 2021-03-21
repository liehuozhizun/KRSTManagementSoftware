package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.krst.app.models.Status;
import org.krst.app.repositories.*;
import org.krst.app.services.DataPassService;
import org.krst.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : none
 * OUT : Boolean, true, reset database
 *       OR
 *       null, nothing happened
 */
@FXMLController
public class DatabaseResetController {
    @FXML private Text prompt;
    @FXML private PasswordField password;
    @FXML private Button verification, accept;

    @Autowired private LoginService loginService;
    @Autowired private DataPassService dataPassService;
    @Autowired private AttributeRepository attributeRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CourseTemplateRepository courseTemplateRepository;
    @Autowired private GradeRepository gradeRepository;
    @Autowired private PatronSaintDateRepository patronSaintDateRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private VisitRepository visitRepository;

    public void verify() {
        if (Status.SUCCESS.equals(loginService.unlimitedVerify(password.getText()))) {
            prompt.setVisible(true);
            verification.setDisable(true);
            accept.setDisable(false);
        } else {
            password.clear();
            password.setPromptText("密码不正确");
        }
    }

    public void reset() {
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        personRepository.deleteAll();
        attributeRepository.deleteAll();
        courseRepository.deleteAll();
        courseTemplateRepository.deleteAll();
        gradeRepository.deleteAll();
        patronSaintDateRepository.deleteAll();
        staffRepository.deleteAll();
        visitRepository.deleteAll();

        dataPassService.setValue(true);
        close();
    }

    public void close() {
        ((Stage)password.getScene().getWindow()).close();
    }
}
