package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
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
public class AddCourseTemplateController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherSelector.getItems().addAll(teacherRepository.findAll());

        teacherSelector.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
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
        });

        teacherSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addTeacherToTeachers(newValue);
            }
            teacherSelector.getSelectionModel().clearSelection();
        });

        teachers.setCache(false);
        teachers.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
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
        });

        teachers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                teachers.getItems().remove(teachers.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void addTeacherToTeachers(Teacher teacher) {
        if (!teachers.getItems().contains(teacher)) {
            teachers.getItems().add(teacher);
        }
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
            logger.logInfo(getClass().toString(), "新建课程模板，编号：{}，名称：{}", courseTemplate.getId(), courseTemplate.getName());
            cacheService.refreshCourseTemplateCache();
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
