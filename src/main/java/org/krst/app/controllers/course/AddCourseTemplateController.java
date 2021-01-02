package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.controllers.ControllerTemplate;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

@FXMLController
public class AddCourseTemplateController extends ControllerTemplate implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextArea topic;
    @FXML
    private ComboBox<Teacher> teacherSelector;
    @FXML
    private ListView<Teacher> teachers;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseTemplateRepository courseTemplateRepository;
    @Autowired
    private CacheService cacheService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherSelector.getItems().addAll(teacherRepository.findAll());
        setUpSelectorAndList(teacherSelector, teachers);
    }

    @Override
    public void flush(){
        teachers.setCellFactory(getCellFactory());
        teacherSelector.setCellFactory(getCellFactory());
    }

    public void approve() {
        CourseTemplate courseTemplate = new CourseTemplate(
                id.getText(),
                name.getText(),
                topic.getText(),
                new HashSet<>(teachers.getItems()));

        if (courseTemplateRepository.existsById(courseTemplate.getId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程模板失败");
            alert.setHeaderText("失败原因：课程编号已存在");
            alert.setContentText("解决方法：更换课程编号");
            alert.showAndWait();
        } else {
            courseTemplateRepository.save(courseTemplate);
            cacheService.refreshCourseTemplateCache();
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
