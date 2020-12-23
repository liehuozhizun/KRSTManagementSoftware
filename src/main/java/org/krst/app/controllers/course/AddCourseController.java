package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.domains.Course;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.services.CacheService;
import org.krst.app.utils.Constants;
import org.krst.app.views.course.AddCourseTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;


@FXMLController
public class AddCourseController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField location;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<CourseTemplate> courseTemplate;
    @FXML
    private TextField templateName;
    @FXML
    private TextArea templateTopic;
    @FXML
    private ComboBox<Teacher> primaryTeacher;
    @FXML
    private ComboBox<Teacher> secondaryTeacher;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshCourseTemplate();

        courseTemplate.setCellFactory(new Callback<ListView<CourseTemplate>, ListCell<CourseTemplate>>() {
            @Override
            public ListCell<CourseTemplate> call(ListView param) {
                return new ListCell<CourseTemplate>() {
                    @Override
                    protected void updateItem(CourseTemplate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId());
                        }
                    }
                };
            }
        });

        courseTemplate.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                templateName.setText("");
                templateTopic.setText("");
                primaryTeacher.getItems().clear();
                secondaryTeacher.getItems().clear();
            } else {
                if (Constants.CREATE_PROMPT.equals(newValue.getId())) {
                    KRSTManagementSoftware.openWindow(AddCourseTemplate.class, true);
                    refreshCourseTemplate();
                    courseTemplate.getSelectionModel().selectLast();
                } else {
                    templateName.setText(newValue.getName());
                    templateTopic.setText(newValue.getTopic());
                    primaryTeacher.getItems().clear();
                    primaryTeacher.getItems().addAll(newValue.getTeachers());
                    secondaryTeacher.getItems().clear();
                    secondaryTeacher.getItems().addAll(newValue.getTeachers());
                }
            }
        });

        Callback<ListView<Teacher>, ListCell<Teacher>> callback =
                new Callback<ListView<Teacher>, ListCell<Teacher>>() {
            @Override
            public ListCell<Teacher> call(ListView<Teacher> param) {
                return new ListCell<Teacher>() {
                    @Override
                    protected void updateItem(Teacher item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " [" + item.getId() + "]");
                        }
                    }
                };
            }
        };

        primaryTeacher.setCellFactory(callback);
        secondaryTeacher.setCellFactory(callback);
    }

    private void refreshCourseTemplate() {
        courseTemplate.getItems().clear();
        courseTemplate.getItems().add(new CourseTemplate(Constants.CREATE_PROMPT, null, null, null));
        courseTemplate.getItems().addAll(cacheService.getCourseTemplates());
    }

    public void approve() {
        Course course = new Course();
        course.setId(id.getText());
        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建课程失败");
            alert.setHeaderText("失败原因：未填入课程编号");
            alert.setContentText("解决方法：请输入课程编号");
            alert.showAndWait();
        }
        course.setStartDate(startDate.getValue());
        course.setEndDate(endDate.getValue());
        course.setLocation(location.getText());
        course.setClassName(name.getText());
        course.setCourseTemplate(courseTemplate.getValue());
        course.setPrimaryTeacher(primaryTeacher.getValue());
        if (primaryTeacher.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建课程失败");
            alert.setHeaderText("失败原因：未选择任课教师");
            alert.setContentText("解决方法：请选择一位任课教师");
            alert.showAndWait();
        }
        course.setSecondaryTeacher(secondaryTeacher.getValue());
        if (primaryTeacher.getValue().equals(secondaryTeacher.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("新建课程失败");
            alert.setHeaderText("失败原因：选择同一人担任主课教师与副课教师");
            alert.setContentText("解决方法：请选择不同的副课教师，或仅选择一位主课教师");
            alert.showAndWait();
        }

        if (courseRepository.existsById(course.getId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程错误");
            alert.setHeaderText("错误原因：使用已存在的课程编号");
            alert.setContentText("解决方法：请输入不同的课程编号");
            alert.showAndWait();
        } else {
            courseRepository.save(course);
            logger.logInfo(getClass().toString(), "新建课程，编号：{}，名称：{}", id.getText(), name.getText());
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
