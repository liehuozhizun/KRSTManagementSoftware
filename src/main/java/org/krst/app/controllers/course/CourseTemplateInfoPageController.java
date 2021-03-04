package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.ControllerTemplate;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : CourseTemplate, data model needs to be displayed
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, CourseTemplate>
 *         Boolean, true  update operation
 *                  false delete operation
 *         CourseTemplate, updated CourseTemplate model
 */
@FXMLController
public class CourseTemplateInfoPageController extends ControllerTemplate implements InfoPageControllerTemplate {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextArea topic;
    @FXML private Text prompt;
    @FXML private ComboBox<Teacher> teacherSelector;
    @FXML private ListView<Teacher> teachers;
    @FXML private Button change, accept, delete, cancel, close;

    @Autowired private CourseTemplateRepository courseTemplateRepository;
    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;
    @Autowired private Logger logger;

    private CourseTemplate originalCourseTemplate = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        originalCourseTemplate = (CourseTemplate) dataPassService.getValue();
        if (originalCourseTemplate == null) {
            close();
            return;
        }

        refreshBasicInfo(originalCourseTemplate);
        setUpSelectorAndList(teacherSelector, teachers);
    }

    private void refreshBasicInfo(CourseTemplate courseTemplate) {
        id.setText(courseTemplate.getId());
        name.setText(courseTemplate.getName());
        topic.setText(courseTemplate.getTopic());
        teachers.getItems().setAll(courseTemplate.getTeachers());
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept(){
        if (isDeleteOperation) {
            courseTemplateRepository.delete(originalCourseTemplate);
            logger.logInfo(this.getClass().toString(), "删除课程模板：编号-{}，名称-{}", id.getText(), name.getText());
            dataPassService.setValue(new Pair<>(false, null));
            cacheService.refreshCourseTemplateCache();
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改课程模板失败");
            alert.setHeaderText("失败原因：未填入课程模板编号");
            alert.setContentText("解决方法：请输入课程模板编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalCourseTemplate.getId())) {
            originalCourseTemplate = courseTemplateRepository.save(loadValuesIntoCourseTemplateModel());
            logger.logInfo(this.getClass().toString(), "更改课程模板：编号-{}，名称-{}", id.getText(), name.getText());
            dataPassService.setValue(new Pair<>(true, originalCourseTemplate));
            cacheService.refreshCourseTemplateCache();
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (courseTemplateRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改课程模板失败");
            alert.setHeaderText("失败原因：该课程模板编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改课程模板编号");
            alert.show();
        } else {
            courseTemplateRepository.delete(originalCourseTemplate);
            originalCourseTemplate = courseTemplateRepository.save(loadValuesIntoCourseTemplateModel());
            refreshBasicInfo(originalCourseTemplate);
            logger.logInfo(this.getClass().toString(), "更改课程模板：编号-{}，名称-{}", id.getText(), name.getText());
            dataPassService.setValue(new Pair<>(true, originalCourseTemplate));
            cacheService.refreshCourseTemplateCache();
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private CourseTemplate loadValuesIntoCourseTemplateModel() {
        return new CourseTemplate(id.getText(), name.getText(), topic.getText(), originalCourseTemplate.getTeachers());
    }

    public void delete(){
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel(){
        if (!isDeleteOperation) {
            refreshBasicInfo(originalCourseTemplate);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close(){
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, topic);
        teachers.setMouseTransparent(state);
        prompt.setVisible(state);
        teacherSelector.setVisible(state);
    }

    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: red" : "-fx-text-fill: black");
        delete.setVisible(!state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }
}
