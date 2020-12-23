package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.configurations.Logger;
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

    private ObservableList<Teacher> observableTeachers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableTeachers.addAll(teacherRepository.findAll());
        teacherSelector.setItems(observableTeachers);

        teacherSelector.setPlaceholder(new Text("无可选教师"));

        teachers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Teacher t = teachers.getSelectionModel().getSelectedItem();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!"+t);
                try {
                teacherSelector.getSelectionModel().clearSelection();
                }catch(Exception e) {
                    System.out.println("4");
                }
                try {
                teacherSelector.getItems().add(t);
                }catch(Exception e) {
                    System.out.println("5");
                }
                try {
                teachers.getSelectionModel().clearSelection();
                }catch(Exception e) {
                    System.out.println("6");
                }
                try {
                teachers.getItems().remove(t);
                }catch(Exception e) {
                    System.out.println("7");
                }
            }
        });

        teacherSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!1  " + newValue);
            if (newValue != null) {
                teachers.getItems().add(newValue);
                teacherSelector.getSelectionModel().clearSelection();
                teacherSelector.getItems().remove(newValue);
                teacherSelector.setVisibleRowCount(Math.min(observableTeachers.size(), 5));
            }
        });

        teacherSelector.editorProperty().get().textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("---------------------" + newValue);
            teacherSelector.show();
            if (newValue != null) {
                FilteredList<Teacher> filteredList = observableTeachers.filtered(teacher -> teacher.getName().contains(newValue) || teacher.getId().contains(newValue));
                teacherSelector.setItems(filteredList);
                teacherSelector.setVisibleRowCount(Math.min(filteredList.size(), 5));
            }
        });
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
