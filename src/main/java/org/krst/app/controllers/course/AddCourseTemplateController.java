package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.ControllerTemplate;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

@FXMLController
public class AddCourseTemplateController extends ControllerTemplate {

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
    @Autowired
    private Logger logger;

    @FXML
    public void initialize() {
        teacherSelector.getItems().addAll(teacherRepository.findAll());
        setUpSelectorAndList(teacherSelector, teachers);
    }

    public void approve() {
        if (courseTemplateRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程模板失败");
            alert.setHeaderText("失败原因：课程模板编号已存在");
            alert.setContentText("解决方法：更换课程模板编号");
            alert.showAndWait();
            return;
        }

        CourseTemplate courseTemplate = new CourseTemplate(
                id.getText(),
                name.getText(),
                topic.getText(),
                new HashSet<>(teachers.getItems()));

        courseTemplateRepository.save(courseTemplate);
        cacheService.refreshCourseTemplateCache();
        logger.logInfo(this.getClass().toString(), "新建课程模板，编号-{}，名称-{}", id.getText(), name.getText());
        close();
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }
}
