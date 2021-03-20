package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Course;
import org.krst.app.domains.Grade;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.GradeRepository;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : Grade, data model needs to be displayed
 * OUT : null, no changes are made
 *       OR
 *       Pair<Boolean, Grade>
 *         Boolean, true  update operation
 *                  false delete operation
 *         Grade, updated Grade model
 */
@FXMLController
public class GradeInfoPageController implements InfoPageControllerTemplate {
    @FXML private Text id;
    @FXML private TextField student_id, student_name, courseTemplate_id, courseTemplate_name, course_id,
            course_className, course_startDate, course_endDate, course_primaryTeacher, course_secondaryTeacher, score;
    @FXML private TextArea courseFeedback, teacherFeedback, offer;
    @FXML private Button change, accept, delete, cancel, close;

    @Autowired private DataPassService dataPassService;
    @Autowired private GradeRepository gradeRepository;
    @Autowired private Logger logger;

    private Grade originalGrade = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        Grade grade = (Grade) dataPassService.getValue();
        if (grade != null) {
            setEditableMode(false);
            setButtonMode(false);
            originalGrade = grade;
            refreshAll(grade);
        } else {
            close();
        }
    }

    private void refreshAll(Grade grade) {
        id.setText(grade.getId().toString());
        if (grade.getStudent() != null) {
            student_id.setText(grade.getStudent().getId());
            student_name.setText(grade.getStudent().getName());
        }
        if (grade.getCourse() != null) {
            Course course = grade.getCourse();
            if (course.getCourseTemplate() != null) {
                courseTemplate_id.setText(course.getCourseTemplate().getId());
                courseTemplate_name.setText(course.getCourseTemplate().getName());
            }
            course_id.setText(course.getId());
            course_className.setText(course.getClassName());
            course_startDate.setText(course.getStartDate() == null ? null : course.getStartDate().toString());
            course_endDate.setText(course.getEndDate() == null ? null : course.getEndDate().toString());
            Teacher primaryTeacher = course.getPrimaryTeacher();
            Teacher secondaryTeacher = course.getSecondaryTeacher();
            course_primaryTeacher.setText(primaryTeacher == null ? null : primaryTeacher.getNameAndId());
            course_secondaryTeacher.setText(secondaryTeacher == null ? null : secondaryTeacher.getNameAndId());
        }
        refreshBasicInfo(grade);
    }

    private void refreshBasicInfo(Grade grade) {
        score.setText(grade.getScore());
        courseFeedback.setText(grade.getCourseFeedback());
        teacherFeedback.setText(grade.getTeacherFeedback());
        offer.setText(grade.getOffer());
    }

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept() {
        if (isDeleteOperation) {
            gradeRepository.removeGradeCourse(originalGrade.getCourse().getId(), originalGrade.getId());
            gradeRepository.removeGradeStudent(originalGrade.getStudent().getId(), originalGrade.getId());
            gradeRepository.delete(originalGrade);
            logger.logInfo(this.getClass().toString(), "删除成绩单：编号-{}，学生-{}，课程-{}", originalGrade.getStudent().getNameAndId(), originalGrade.getCourse().getIdAndName());
            dataPassService.setValue(new Pair<>(false, null));
            close();
            return;
        }

        loadValuesIntoOriginalGradeModel();
        originalGrade = gradeRepository.save(originalGrade);
        logger.logInfo(this.getClass().toString(), "更改课程模板：编号-{}", id.getText());
        dataPassService.setValue(new Pair<>(true, originalGrade));
        setEditableMode(false);
        setButtonMode(false);
    }

    private void loadValuesIntoOriginalGradeModel() {
        originalGrade.setScore(score.getText());
        originalGrade.setCourseFeedback(courseFeedback.getText());
        originalGrade.setTeacherFeedback(teacherFeedback.getText());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalGrade);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)student_id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, score, courseFeedback, teacherFeedback, offer);
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
