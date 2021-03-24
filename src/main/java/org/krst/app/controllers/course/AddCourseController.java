package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.krst.app.configurations.Logger;
import org.krst.app.domains.Course;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : none
 * OUT : Course, newly added Course data model
 */
@FXMLController
public class AddCourseController {
    @FXML private TextField id;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField location1;
    @FXML private TextField className;
    @FXML private ComboBox<CourseTemplate> courseTemplate;
    @FXML private TextField name;
    @FXML private TextArea topic;
    @FXML private ComboBox<Teacher> primaryTeacher;
    @FXML private ComboBox<Teacher> secondaryTeacher;

    @Autowired private CourseRepository courseRepository;
    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;
    @Autowired private Logger logger;

    @FXML public void initialize() {
        courseTemplate.getItems().setAll(cacheService.getCourseTemplates());
        initDefaultComponents();
    }

    private void initDefaultComponents() {
        courseTemplate.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            name.setText(newValue.getName());
            topic.setText(newValue.getTopic());
            primaryTeacher.getSelectionModel().clearSelection();
            secondaryTeacher.getSelectionModel().clearSelection();
            if (newValue.getTeachers() != null) {
                primaryTeacher.getItems().setAll(newValue.getTeachers());
                secondaryTeacher.getItems().setAll(newValue.getTeachers());
            } else {
                primaryTeacher.getItems().clear();
                secondaryTeacher.getItems().clear();
            }
        });
        courseTemplate.setConverter(new StringConverter<CourseTemplate>() {
            @Override
            public String toString(CourseTemplate courseTemplate1) {
                return courseTemplate1 == null ? null : courseTemplate1.getIdAndName();
            }

            @Override
            public CourseTemplate fromString(String string) {
                return string == null ? null : courseTemplate.getItems().stream().filter(ct -> ct.getIdAndName().equals(string) ||
                        ct.getId().equals(string) || ct.getName().equals(string)).findFirst().orElse(null);
            }
        });
        primaryTeacher.setConverter(new StringConverter<Teacher>() {
            @Override
            public String toString(Teacher teacher) {
                return teacher == null ? null : teacher.getNameAndId();
            }

            @Override
            public Teacher fromString(String string) {
                return string == null ? null : primaryTeacher.getItems().stream().filter(ct -> ct.getNameAndId().equals(string) ||
                        ct.getId().equals(string) || ct.getName().equals(string)).findFirst().orElse(null);
            }
        });
        secondaryTeacher.setConverter(new StringConverter<Teacher>() {
            @Override
            public String toString(Teacher teacher) {
                return teacher == null ? null : teacher.getNameAndId();
            }

            @Override
            public Teacher fromString(String string) {
                return string == null ? null : secondaryTeacher.getItems().stream().filter(ct -> ct.getNameAndId().equals(string) ||
                        ct.getId().equals(string) || ct.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void accept() {
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程失败");
            alert.setHeaderText("失败原因：未填写课程编号");
            alert.setContentText("解决方法：请填入课程编号");
            alert.showAndWait();
            return;
        }

        if (courseRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程失败");
            alert.setHeaderText("失败原因：课程编号已存在");
            alert.setContentText("解决方法：更换课程编号");
            alert.showAndWait();
            return;
        }

        dataPassService.setValue(courseRepository.save(loadValuesIntoCourseModel()));
        logger.logInfo(this.getClass().toString(), "新建课程，编号-{}，授课班级-{}", id.getText(), name.getText());
        close();
    }

    private Course loadValuesIntoCourseModel() {
        return new Course(id.getText(), startDate.getValue(), endDate.getValue(), location1.getText(), className.getText(),
                courseTemplate.getValue(), primaryTeacher.getValue(), secondaryTeacher.getValue(), null);
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }
}
