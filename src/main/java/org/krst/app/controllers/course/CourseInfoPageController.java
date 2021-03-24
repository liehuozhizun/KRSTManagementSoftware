package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.views.course.AddGrade;
import org.krst.app.views.course.GradeInfoPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * IN  : Course, data model needs to be displayed
 * OUT : Boolean
 *          true, update Id
 *          false, delete operation
 *       OR
 *       null, update operation or nothing changed
 */
@FXMLController
public class CourseInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField id, location1, className, name;
    @FXML private DatePicker startDate, endDate;
    @FXML private Text number, prompt1, prompt2;
    @FXML private TextArea topic;
    @FXML private ComboBox<CourseTemplate> courseTemplate;
    @FXML private ComboBox<Teacher> primaryTeacher, secondaryTeacher;
    @FXML private Button change, accept, delete, cancel, close;

    @FXML private TableView<Grade> grades;
    @FXML private TableColumn<Grade, String> grades_id;
    @FXML private TableColumn<Grade, Student> grades_name;
    @FXML private TableColumn<Grade, String> grades_score;
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

    private Course originalCourse = null;
    private boolean isDeleteOperation = false;

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
                        if (empty || item == null) {
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

        grades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
                        if (empty || item == null) {
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
                        if (empty || item == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        courseComments_comment.setCellValueFactory(new PropertyValueFactory<>("courseFeedback"));
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
                        if (empty || item == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
        teacherComments_comment.setCellValueFactory(new PropertyValueFactory<>("teacherFeedback"));
        teacherComments.setRowFactory(defaultRowFactory());
    }

    private void refreshPartialData(Grade oldGrade, Grade newGrade, TableView<Grade> tableView, Function<Grade, String> getter) {
        int itemIndex = tableView.getItems().indexOf(oldGrade);
        if (itemIndex != -1)
            if (getter.apply(newGrade).isEmpty())
                tableView.getItems().remove(itemIndex);
            else
                tableView.getItems().set(itemIndex, newGrade);
        else if (!getter.apply(newGrade).isEmpty())
            tableView.getItems().add(newGrade);
    }

    private Callback<TableView<Grade>, TableRow<Grade>> defaultRowFactory() {
        return tv -> {
            TableRow<Grade> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    dataPassService.setValue(row.getItem());
                    KRSTManagementSoftware.openWindow(GradeInfoPage.class);
                    Pair<Boolean, Grade> returnedData = (Pair<Boolean, Grade>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalCourse.getGrades().remove(row.getItem());
                        if (returnedData.getKey()) {
                            originalCourse.getGrades().add(returnedData.getValue());

                            int itemIndex = grades.getItems().indexOf(row.getItem());
                            grades.getItems().set(itemIndex, returnedData.getValue());

                            refreshPartialData(row.getItem(), returnedData.getValue(), offers, Grade::getOffer);
                            refreshPartialData(row.getItem(), returnedData.getValue(), teacherComments, Grade::getTeacherFeedback);
                            refreshPartialData(row.getItem(), returnedData.getValue(), courseComments, Grade::getCourseFeedback);
                        } else {
                            int itemIndex = grades.getItems().indexOf(row.getItem());
                            grades.getItems().remove(itemIndex);
                            itemIndex = offers.getItems().indexOf(row.getItem());
                            if (itemIndex != -1) offers.getItems().remove(itemIndex);
                            itemIndex = teacherComments.getItems().indexOf(row.getItem());
                            if (itemIndex != -1) teacherComments.getItems().remove(itemIndex);
                            itemIndex = courseComments.getItems().indexOf(row.getItem());
                            if (itemIndex != -1) courseComments.getItems().remove(itemIndex);
                        }
                    }
                }
            });
            return row;
        };
    }

    private void refreshAll(Course course) {
        refreshBasicInfo(course);
        refreshOtherInfo(course.getGrades());
    }

    private void refreshBasicInfo(Course course) {
        id.setText(course.getId());
        number.setText(String.valueOf(course.getGrades() == null ? 0 : course.getGrades().size()));
        startDate.setValue(course.getStartDate());
        endDate.setValue(course.getEndDate());
        location1.setText(course.getLocation());
        className.setText(course.getClassName());
        if (course.getCourseTemplate() != null)
            courseTemplate.getSelectionModel().select(course.getCourseTemplate());
        if (course.getPrimaryTeacher() != null)
            primaryTeacher.getSelectionModel().select(course.getPrimaryTeacher());
        if (course.getSecondaryTeacher() != null)
            secondaryTeacher.getSelectionModel().select(course.getSecondaryTeacher());
    }

    private void refreshOtherInfo(Set<Grade> gradeSet) {
        if(gradeSet == null) return;
        grades.getItems().setAll(gradeSet);
        offers.getItems().setAll(gradeSet.stream().filter(g->!g.getOffer().isEmpty()).collect(Collectors.toSet()));
        courseComments.getItems().setAll(gradeSet.stream().filter(g->!g.getCourseFeedback().isEmpty()).collect(Collectors.toSet()));
        teacherComments.getItems().setAll(gradeSet.stream().filter(g->!g.getTeacherFeedback().isEmpty()).collect(Collectors.toSet()));
    }

    public void addGrade() {
        dataPassService.setValue(originalCourse);
        KRSTManagementSoftware.openWindow(AddGrade.class);
        Grade tempGrade = (Grade) dataPassService.getValue();
        if (tempGrade != null) {
            grades.getItems().add(tempGrade);
            if (tempGrade.getOffer() != null && !tempGrade.getOffer().isEmpty()) offers.getItems().add(tempGrade);
            if (tempGrade.getTeacherFeedback() != null && !tempGrade.getTeacherFeedback().isEmpty()) teacherComments.getItems().add(tempGrade);
            if (tempGrade.getCourseFeedback() != null && !tempGrade.getCourseFeedback().isEmpty()) courseComments.getItems().add(tempGrade);
        }
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept(){
        if (isDeleteOperation) {
            courseRepository.delete(originalCourse);
            courseRepository.updateCourseIdInGrade(originalCourse.getId(), null);

            dataPassService.setValue(false);
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
            courseRepository.updateCourseIdInGrade(originalCourse.getId(), id.getText());

            originalCourse = courseRepository.save(loadValuesIntoCourseModel());
            refreshBasicInfo(originalCourse);
            dataPassService.setValue(true);
            logger.logInfo(this.getClass().toString(), "更改课程信息：编号-{}，授课班级-{}", id.getText(), className.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Course loadValuesIntoCourseModel() {
        return new Course(id.getText(), startDate.getValue(), endDate.getValue(), location1.getText(), className.getText(),
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
        setTextEditableMode(state, id, location1, className);
        setDatePickerEditableMode(state, startDate, endDate);
        setComboBoxEditableMode(state, courseTemplate, primaryTeacher, secondaryTeacher);
    }

    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: red" : "-fx-text-fill: black");
        delete.setVisible(!state);
        prompt1.setVisible(isDeleteOperation && state);
        prompt2.setVisible(isDeleteOperation && state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }
}
