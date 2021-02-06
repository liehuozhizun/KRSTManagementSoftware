package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : Course, data model needs to be displayed
 * OUT : none
 */
@FXMLController
public class CourseInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField id, location, className, name;
    @FXML private DatePicker startDate, endDate;
    @FXML private Text number, prompt1, prompt2;
    @FXML private TextArea topic;
    @FXML private ComboBox<CourseTemplate> courseTemplate;
    @FXML private ComboBox<Teacher> primaryTeacher, secondaryTeacher;
    @FXML private Button change, accept, delete, cancel, close;

    @FXML private TableView<Grade> grades;
    @FXML private TableColumn<Grade, String> grades_id;
    @FXML private TableColumn<Grade, Student> grades_name;
    @FXML private TableColumn<Grade, Integer> grades_score;
    @FXML private TableView<Grade> offers;
    @FXML private TableColumn<Grade, Student> offers_name;
    @FXML private TableColumn<Grade, String> offers_offer;
    @FXML private TableView<Grade> courseComments;
    @FXML private TableColumn<Grade, Student> courseComments_name;
    @FXML private TableColumn<Grade, String> courseComments_comment;
    @FXML private TableView<Grade> teacherComments;
    @FXML private TableColumn<Grade, Student> teacherComments_name;
    @FXML private TableColumn<Grade, String> teacherComments_comment;

    @Autowired private CourseRepository courseRepository;
    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;
    @Autowired private Logger logger;

    private Course originalCourse;
    private Boolean isDeleteOperation;

    @FXML public void initialize() {
        originalCourse = (Course) dataPassService.getValue();
        if (originalCourse == null) {
            close();
            return;
        }

        initDefaultComponents();
        refreshAll(originalCourse);
    }

    private void initDefaultComponents() {
        setEditableMode(false);
        setButtonMode(false);

        initLeftSideDefaultComponents();
        initMiddleSideDefaultComponents();
        initRightSideDefaultComponents();
    }

    private void initLeftSideDefaultComponents() {
        courseTemplate.getItems().setAll(cacheService.getCourseTemplates());
        courseTemplate.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            name.setText(newValue.getName());
            topic.setText(newValue.getTopic());
            primaryTeacher.getSelectionModel().clearSelection();
            primaryTeacher.getItems().setAll(newValue.getTeachers());
            secondaryTeacher.getSelectionModel().clearSelection();
            secondaryTeacher.getItems().setAll(oldValue.getTeachers());
        });
    }

    private void initMiddleSideDefaultComponents() {
        grades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        grades_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        grades_name.setCellValueFactory(new PropertyValueFactory<>("student"));
        grades_name.setCellFactory(new Callback<TableColumn<Grade, Student>, TableCell<Grade, Student>>() {
            @Override
            public TableCell<Grade, Student> call(TableColumn<Grade, Student> param) {
                return new TableCell<Grade, Student>() {
                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        grades_score.setCellValueFactory(new PropertyValueFactory<>("score"));

        grades.setRowFactory(defaultRowFactory());
    }

    private void initRightSideDefaultComponents() {
        offers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        offers_name.setCellValueFactory(new PropertyValueFactory<>("student"));
        offers_name.setCellFactory(new Callback<TableColumn<Grade, Student>, TableCell<Grade, Student>>() {
            @Override
            public TableCell<Grade, Student> call(TableColumn<Grade, Student> param) {
                return new TableCell<Grade, Student>() {
                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        offers_offer.setCellValueFactory(new PropertyValueFactory<>("offer"));
        offers.setRowFactory(defaultRowFactory());

        courseComments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        courseComments_name.setCellValueFactory(new PropertyValueFactory<>("student"));
        courseComments_name.setCellFactory(new Callback<TableColumn<Grade, Student>, TableCell<Grade, Student>>() {
            @Override
            public TableCell<Grade, Student> call(TableColumn<Grade, Student> param) {
                return new TableCell<Grade, Student>() {
                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        courseComments_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        courseComments.setRowFactory(defaultRowFactory());

        teacherComments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        teacherComments_name.setCellValueFactory(new PropertyValueFactory<>("student"));
        teacherComments_name.setCellFactory(new Callback<TableColumn<Grade, Student>, TableCell<Grade, Student>>() {
            @Override
            public TableCell<Grade, Student> call(TableColumn<Grade, Student> param) {
                return new TableCell<Grade, Student>() {
                    @Override
                    protected void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        teacherComments_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        teacherComments.setRowFactory(defaultRowFactory());
    }

    private Callback<TableView<Grade>, TableRow<Grade>> defaultRowFactory() {
        return tv -> {
            TableRow<Grade> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // TBC  -  LEAVE EMPTY FOR NOW
                }
            });
            return row;
        };
    }

    private void refreshAll(Course course) {
        refreshBasicInfo(course);
        refreshOtherInfo(course);
    }

    private void refreshBasicInfo(Course course) {
        id.setText(course.getId());
        number.setText(String.valueOf(course.getGrades() == null ? 0 : course.getGrades().size()));
        startDate.setValue(course.getStartDate());
        endDate.setValue(course.getEndDate());
        location.setText(course.getLocation());
        className.setText(course.getClassName());
        courseTemplate.getSelectionModel().select(course.getCourseTemplate());
        primaryTeacher.getSelectionModel().select(course.getPrimaryTeacher());
        secondaryTeacher.getSelectionModel().select(course.getSecondaryTeacher());
    }

    private void refreshOtherInfo(Course course) {
        grades.getItems().setAll(course.getGrades());
        offers.getItems().setAll(course.getGrades());
        courseComments.getItems().setAll(course.getGrades());
        teacherComments.getItems().setAll(course.getGrades());
    }

    public void addGrade() {
        // LEAVE EMPTY FOR NOW
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept(){
        if (isDeleteOperation) {
            courseRepository.delete(originalCourse);
            logger.logInfo(this.getClass().toString(), "删除课程信息：编号-{}，授课班级-{}", id.getText(), className.getText());
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改课程信息失败");
            alert.setHeaderText("失败原因：未填入课程编号");
            alert.setContentText("解决方法：请输入课程编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalCourse.getId())) {
            originalCourse = courseRepository.save(loadValuesIntoCourseModel());
            logger.logInfo(this.getClass().toString(), "更改课程信息：编号-{}，授课班级-{}", id.getText(), className.getText());
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (courseRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改课程信息失败");
            alert.setHeaderText("失败原因：该课程编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改课程编号");
            alert.show();
        } else {
            courseRepository.delete(originalCourse);
            originalCourse = courseRepository.save(loadValuesIntoCourseModel());
            refreshBasicInfo(originalCourse);
            logger.logInfo(this.getClass().toString(), "更改课程信息：编号-{}，授课班级-{}", id.getText(), className.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Course loadValuesIntoCourseModel() {
        return new Course(id.getText(), startDate.getValue(), endDate.getValue(), location.getText(), className.getText(),
                courseTemplate.getValue(), primaryTeacher.getValue(), secondaryTeacher.getValue(),
                originalCourse.getGrades());
    }

    public void delete(){
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel(){
        if (!isDeleteOperation) {
            refreshBasicInfo(originalCourse);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close(){
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, location, className);
        setDatePickerEditableMode(state, startDate, endDate);
        setComboBoxEditableMode(state, primaryTeacher, secondaryTeacher);
    }

    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: red" : "-fx-text-fill: black");
        delete.setVisible(!state);
        prompt1.setVisible(isDeleteOperation && !state);
        prompt2.setVisible(isDeleteOperation && !state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }
}
