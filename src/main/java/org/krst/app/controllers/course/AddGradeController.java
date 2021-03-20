package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Course;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Grade;
import org.krst.app.domains.Student;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.repositories.GradeRepository;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/*
 * IN  : Object
 *          Student, add a grade for a specific student
 *          OR
 *          Course, add a grade for a specific course
 * OUT : Grade, newly added Grade data model
 */
@FXMLController
public class AddGradeController implements InfoPageControllerTemplate {
    @FXML private ComboBox<Student> student_id, student_name;
    @FXML private ComboBox<CourseTemplate> courseTemplate_id, courseTemplate_name;
    @FXML private ComboBox<Course> course_id, course_className;
    @FXML private TextField course_startDate, course_endDate, course_primaryTeacher, course_secondaryTeacher, score;
    @FXML private TextArea courseFeedback, teacherFeedback, offer;

    @Autowired private StudentRepository studentRepository;
    @Autowired private DataPassService dataPassService;
    @Autowired private CacheService cacheService;
    @Autowired private CourseRepository courseRepository;
    @Autowired private GradeRepository gradeRepository;

    private boolean studentFlag = true;
    private boolean courseTemplateFlag = true;
    private boolean courseFlag = true;
    private List<Course> courses;

    @FXML public void initialize() {
        Object temp = dataPassService.getValue();
        if (temp instanceof Student) {
            initStudent((Student) temp);
        } else if (temp instanceof Course) {
            initCourse((Course) temp);
        } else {
            close();
        }
        initializeComponent();
    }

    private void initializeComponent() {
        student_id.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? null : student.getId();
            }

            @Override
            public Student fromString(String id) {
                return id == null ? null : student_id.getItems().stream().filter(student ->
                        student.getId().equals(id)).findFirst().orElse(null);
            }
        });
        student_name.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student == null ? null : student.getName();
            }

            @Override
            public Student fromString(String id) {
                return id == null ? null : student_name.getItems().stream().filter(student ->
                        student.getId().equals(id)).findFirst().orElse(null);
            }
        });
        courseTemplate_id.setConverter(new StringConverter<CourseTemplate>() {
            @Override
            public String toString(CourseTemplate courseTemplate) {
                return courseTemplate == null ? null : courseTemplate.getId();
            }

            @Override
            public CourseTemplate fromString(String id) {
                return id == null ? null : courseTemplate_id.getItems().stream().filter(courseTemplate ->
                        courseTemplate.getId().equals(id)).findFirst().orElse(null);
            }
        });
        courseTemplate_name.setConverter(new StringConverter<CourseTemplate>() {
            @Override
            public String toString(CourseTemplate courseTemplate) {
                return courseTemplate == null ? null : courseTemplate.getName();
            }

            @Override
            public CourseTemplate fromString(String name) {
                return name == null ? null : courseTemplate_name.getItems().stream().filter(courseTemplate ->
                        courseTemplate.getName().equals(name)).findFirst().orElse(null);
            }
        });
        course_id.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? null : course.getId();
            }

            @Override
            public Course fromString(String id) {
                return id == null ? null : course_id.getItems().stream().filter(course ->
                        course.getId().equals(id)).findFirst().orElse(null);
            }
        });
        course_className.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course == null ? null : course.getClassName();
            }

            @Override
            public Course fromString(String className) {
                return className == null ? null : course_className.getItems().stream().filter(course ->
                        course.getId().equals(className)).findFirst().orElse(null);
            }
        });
        student_id.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && studentFlag) {
                studentFlag = false;
                student_name.getSelectionModel().select(newValue.intValue());
                studentFlag = true;
            }
        });
        student_name.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && studentFlag) {
                studentFlag = false;
                student_id.getSelectionModel().select(newValue.intValue());
                studentFlag = true;
            }
        });
        courseTemplate_id.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && courseTemplateFlag && newValue.intValue() != oldValue.intValue()) {
                courseTemplateFlag = false;
                courseTemplate_name.getSelectionModel().select(newValue.intValue());

                cleanCourseInfo();
                course_id.getSelectionModel().clearSelection();
                course_className.getSelectionModel().clearSelection();
                String id = courseTemplate_id.getItems().get(newValue.intValue()).getId();
                List<Course> courseResult = courses.stream().filter(course -> course.getCourseTemplate().getId().equals(id)).collect(Collectors.toList());
                course_id.getItems().setAll(courseResult);
                course_className.getItems().setAll(courseResult);

                courseTemplateFlag = true;
            }
        });
        courseTemplate_name.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && courseTemplateFlag) {
                courseTemplateFlag = false;
                courseTemplate_id.getSelectionModel().select(newValue.intValue());

                cleanCourseInfo();
                course_id.getSelectionModel().clearSelection();
                course_className.getSelectionModel().clearSelection();
                String id = courseTemplate_id.getItems().get(newValue.intValue()).getId();
                List<Course> courseResult = courses.stream().filter(course -> course.getCourseTemplate().getId().equals(id)).collect(Collectors.toList());
                course_id.getItems().setAll(courseResult);
                course_className.getItems().setAll(courseResult);

                courseTemplateFlag = true;
            }
        });
        course_id.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && courseFlag && newValue.intValue() != oldValue.intValue()) {
                courseFlag = false;
                refreshCourseInfo(course_id.getItems().get(newValue.intValue()));
                course_className.getSelectionModel().select(newValue.intValue());
                if (courseTemplate_id.getValue() == null) {
                    courseTemplate_id.getSelectionModel().select(course_id.getItems().get(newValue.intValue()).getCourseTemplate());
                }
                courseFlag = true;
            }
        });
        course_className.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() != -1 && courseFlag && newValue.intValue() != oldValue.intValue()) {
                courseFlag = false;
                refreshCourseInfo(course_className.getItems().get(newValue.intValue()));
                course_id.getSelectionModel().select(newValue.intValue());
                if (courseTemplate_id.getValue() == null) {
                    courseTemplate_id.getSelectionModel().select(course_id.getItems().get(newValue.intValue()).getCourseTemplate());
                }
                courseFlag = true;
            }
        });
    }

    private void initStudent(Student student) {
        studentFlag = false;
        setComboBoxEditableMode(false, student_id, student_name);
        student_id.setValue(student);
        student_name.setValue(student);

        List<CourseTemplate> courseTemplates = cacheService.getCourseTemplates();
        courseTemplate_id.getItems().setAll(courseTemplates);
        courseTemplate_name.getItems().setAll(courseTemplates);
        courses = courseRepository.findAll();
        course_id.getItems().setAll(courses);
        course_className.getItems().setAll(courses);
    }

    private void initCourse(Course course) {
        courseTemplateFlag = false;
        courseFlag = false;
        setComboBoxEditableMode(false, courseTemplate_id, courseTemplate_name, course_id, course_className);
        course_id.setValue(course);
        course_className.setValue(course);
        refreshCourseInfo(course);

        List<Student> students = studentRepository.findAll();
        student_id.getItems().setAll(students);
        student_name.getItems().setAll(students);
    }

    private void refreshCourseInfo(Course course) {
        course_startDate.setText(course.getStartDate() == null ? null : course.getStartDate().toString());
        course_endDate.setText(course.getEndDate() == null ? null : course.getEndDate().toString());
        course_primaryTeacher.setText(course.getPrimaryTeacher() == null ? null : course.getPrimaryTeacher().getNameAndId());
        course_secondaryTeacher.setText(course.getSecondaryTeacher() == null ? null : course.getSecondaryTeacher().getNameAndId());
    }

    private void cleanCourseInfo() {
        course_startDate.setText(null);
        course_endDate.setText(null);
        course_primaryTeacher.clear();
        course_secondaryTeacher.clear();
    }

    public void accept() {
        if (student_id.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建成绩单失败");
            alert.setHeaderText("失败原因：未选择学生");
            alert.setContentText("解决方法：选择学生");
            alert.showAndWait();
            return;
        }
        if (course_id.getItems() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建成绩单失败");
            alert.setHeaderText("失败原因：未选择课程");
            alert.setContentText("解决方法：选择课程");
            alert.showAndWait();
            return;
        }
        if (score.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建成绩单失败");
            alert.setHeaderText("失败原因：未添加成绩");
            alert.setContentText("解决方法：添加成绩");
            alert.showAndWait();
            return;
        }

        Grade grade = new Grade(null, student_id.getValue(), course_id.getValue(), score.getText(),
                courseFeedback.getText(), teacherFeedback.getText(), offer.getText());
        grade = gradeRepository.save(grade);

        gradeRepository.addGradeStudent(student_id.getValue().getId(), grade.getId());
        gradeRepository.addGradeCourse(course_id.getValue().getId(), grade.getId());

        dataPassService.setValue(grade);
        close();
    }

    public void close() {
        ((Stage)student_id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        throw new RuntimeException("NOT SUPPORTED");
    }

    @Override
    public void setButtonMode(boolean state) {
        throw new RuntimeException("NOT SUPPORTED");
    }
}
