package org.krst.app.controllers.student;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.models.RelationPassModel;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.*;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Student, the Student model that need to be displayed
 * Out : None
 */
@FXMLController
public class StudentInfoController implements InfoPageControllerTemplate {
    @FXML private SplitPane splitPane;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField age;
    @FXML private ComboBox<String> gender;
    @FXML private TextField baptismalName;
    @FXML private DatePicker birthday;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;

    @FXML private ComboBox<Attribute> attribute;
    @FXML private TextField leader;
    @FXML private TextField leaderPhone;
    @FXML private TextField altLeader;
    @FXML private TextField altLeaderPhone;

    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private TextArea address;
    @FXML private TextArea experience;
    @FXML private TextArea education;
    @FXML private TextArea talent;
    @FXML private TextArea resource;
    @FXML private ComboBox<Staff> staff;

    @FXML private Button change;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button close;

    @FXML private TableView<Visit> visit;
    @FXML private TableColumn<Visit, String> visit_date;
    @FXML private TableColumn<Visit, String> visit_content;
    @FXML private TableColumn<Visit, String> visit_summary;
    @FXML private TableView<Internship> internship;
    @FXML private TableColumn<Internship, String> internship_startDate;
    @FXML private TableColumn<Internship, String> internship_endDate;
    @FXML private TableColumn<Internship, String> internship_purpose;
    @FXML private TableView<Grade> grade;
    @FXML private TableColumn<Grade, String> grade_courseTemplate;
    @FXML private TableColumn<Grade, String> grade_course;
    @FXML private TableColumn<Grade, Integer> grade_score;
    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;

    @Autowired private VisitRepository visitRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;
    @Autowired private RelationshipService relationshipService;
    @Autowired private Logger logger;

    private Student originalStudent;
    private Boolean isDeleteOperation;

    @FXML
    public void initialize() {
        originalStudent = (Student) dataPassService.getValue();
        if (originalStudent == null) {
            close();
            return;
        }

        initDefaultComponents();
        refreshAll(originalStudent);
    }

    private void initDefaultComponents() {
        setEditableMode(false);
        setButtonMode(false);
        splitPane.getDividers().get(0).positionProperty()
                .addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(0.4258));

        initLeftSideDefaultComponents();
        initRightSideDefaultComponents();
    }

    private void initLeftSideDefaultComponents() {
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());
        attribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            if (Constants.CREATE_PROMPT.equals(newValue.getAttribute())) {
                KRSTManagementSoftware.openWindow(AddAttribute.class);
                Attribute temp = (Attribute) dataPassService.getValue();
                if (temp != null) {
                    attribute.getItems().add(temp);
                    attribute.getSelectionModel().selectLast();
                }
                return;
            }

            leader.setText(newValue.getLeader());
            leaderPhone.setText(newValue.getLeaderPhone());
            altLeader.setText(newValue.getAltLeader());
            altLeaderPhone.setText(newValue.getAltLeaderPhone());
        });

        attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return string == null ? null : attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });

        staff.getItems().setAll(cacheService.getStaffs());
        staff.setConverter(new StringConverter<Staff>() {
            @Override
            public String toString(Staff staff) {
                return staff == null ? null : staff.getNameAndId();
            }

            @Override
            public Staff fromString(String string) {
                return string == null ? null : staff.getItems().stream().filter(staff ->
                        staff.getName().equals(string) || staff.getId().equals(string) || staff.getNameAndId().equals(string))
                        .findFirst().orElse(null);
            }
        });

        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                gender.getEditor().setText(newValue);
        });
    }

    private void initRightSideDefaultComponents() {
        visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visit.setRowFactory( tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalStudent, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                    Pair<Boolean, Visit> returnedData = (Pair<Boolean, Visit>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalStudent.getVisits().removeIf(vis -> vis.getId().equals(row.getItem().getId()));
                        if (returnedData.getKey()) {
                            originalStudent.getVisits().add(returnedData.getValue());
                            visit.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            originalStudent = studentRepository.save(originalStudent);
                            visitRepository.delete(row.getItem());
                            logger.logInfo(this.getClass().toString(), "删除探访记录：探访记录，类型-学生，编号-{}，姓名-{}", row.getItem().getId().toString(), name.getText());
                            visit.getItems().remove(row.getIndex());
                        }
                    }
                }
            });
            return row ;
        });
        internship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        internship.setRowFactory( tv -> {
            TableRow<Internship> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalStudent, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(InternshipInfoPage.class);
                    Pair<Boolean, Internship> returnedData = (Pair<Boolean, Internship>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalStudent.getInternships().remove(row.getItem());
                        if (returnedData.getKey()) {
                            originalStudent.getInternships().add(returnedData.getValue());
                            internship.getItems().set(row.getIndex(), returnedData.getValue());
                            logger.logInfo(this.getClass().toString(), "更改学生实践记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        } else {
                            internship.getItems().remove(row.getIndex());
                            logger.logInfo(this.getClass().toString(), "删除学生实践记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        }
                        originalStudent = studentRepository.save(originalStudent);
                    }
                }
            });
            return row ;
        });
        grade.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        grade.setRowFactory(tv -> {
            TableRow<Grade> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    // TBC    :      LEAVE EMPTY FOR NOW
                }
            });
            return row ;
        });
        relationship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        relationship.setRowFactory( tv -> {
            TableRow<Relation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Relation tempRelation = row.getItem();
                    RelationPassModel relationPassModel = new RelationPassModel(
                            Relation.Type.STAFF,
                            originalStudent.getId(),
                            originalStudent.getName(),
                            tempRelation.getType(),
                            tempRelation.getId(),
                            tempRelation.getName(),
                            tempRelation.getRelationship()
                    );
                    dataPassService.setValue(relationPassModel);
                    KRSTManagementSoftware.openWindow(RelationshipInfoPage.class);
                    Pair<Boolean, String> returnedData = (Pair<Boolean, String>) dataPassService.getValue();
                    if (returnedData != null) {
                        if (returnedData.getKey()) {
                            originalStudent.getRelationships().remove(row.getItem());
                            Relation temp = row.getItem();
                            temp.setRelationship(returnedData.getValue());
                            relationship.getItems().set(row.getIndex(), temp);
                            originalStudent.getRelationships().add(row.getItem());
                        } else {
                            originalStudent.getRelationships().remove(row.getItem());
                            relationship.getItems().remove(row.getItem());
                        }
                    }
                }
            });
            return row ;
        });

        visit_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        visit_content.setCellValueFactory(new PropertyValueFactory<>("content"));
        visit_summary.setCellValueFactory(new PropertyValueFactory<>("summary"));

        internship_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        internship_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        internship_purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));

        grade_courseTemplate.setCellValueFactory(new PropertyValueFactory<>("courseTemplate"));
        grade_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        grade_score.setCellValueFactory(new PropertyValueFactory<>("score"));

        relationship_relation.setCellValueFactory(new PropertyValueFactory<>("relation"));
        relationship_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        relationship_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        relationship_type.setCellFactory(new Callback<TableColumn<Relation, Relation.Type>, TableCell<Relation, Relation.Type>>() {
            @Override
            public TableCell<Relation, Relation.Type> call(TableColumn<Relation, Relation.Type> param) {
                return new TableCell<Relation, Relation.Type>() {
                    @Override
                    protected void updateItem(Relation.Type item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.toString());
                        }
                    }
                };
            }
        });
        relationship_id.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void refreshAll(Student student) {
        refreshBasicInfo(student);
        refreshOtherInfo(student);
    }

    private void refreshBasicInfo(Student student) {
        id.setText(student.getId());
        name.setText(student.getName());
        baptismalName.setText(student.getBaptismalName());
        gender.getSelectionModel().select(student.getGender() == null ? null : student.getGender());
        birthday.setValue(student.getBirthday());
        isGregorianCalendar.setSelected(student.getIsGregorianCalendar() != null && student.getIsGregorianCalendar());
        if (student.getBirthday() != null)
            age.setText(student.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        baptismalDate.setValue(student.getBaptismalDate());
        confirmationDate.setValue(student.getConfirmationDate());
        marriageDate.setValue(student.getMarriageDate());
        deathDate.setValue(student.getDeathDate());
        if (student.getAttribute() != null) {
            attribute.getSelectionModel().select(student.getAttribute());
        }
        phone.setText(student.getPhone());
        altPhone.setText(student.getAltPhone());
        address.setText(student.getAddress());
        experience.setText(student.getExperience());
        talent.setText(student.getTalent());
        resource.setText(student.getResource());
        education.setText(student.getEducation());
        if (student.getStaff() != null) {
            staff.getSelectionModel().select(student.getStaff());
        }
    }

    private void refreshOtherInfo(Student student){
        if (student.getVisits() != null)
            visit.getItems().addAll(student.getVisits());
        if (student.getInternships() != null)
            internship.getItems().addAll(student.getInternships());
        if (student.getGrades() != null)
            grade.getItems().addAll(student.getGrades());
        if (student.getRelationships() != null)
            relationship.getItems().addAll(student.getRelationships());
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept(){
        if (isDeleteOperation) {
            studentRepository.delete(originalStudent);
            logger.logInfo(this.getClass().toString(), "删除学生档案：编号-{}，姓名-{}", id.getText(), name.getText());
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改学生档案失败");
            alert.setHeaderText("失败原因：未填入学生编号");
            alert.setContentText("解决方法：请输入学生编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalStudent.getId())) {
            if (!name.getText().equals(originalStudent.getName()))
                relationshipService.updateIdAndName(originalStudent.getRelationships(), originalStudent.getId(), id.getText(), name.getText());

            originalStudent = studentRepository.save(loadValuesIntoStaffModel());
            logger.logInfo(this.getClass().toString(), "更改学生档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (studentRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改学生档案失败");
            alert.setHeaderText("失败原因：该学生编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改学生编号");
            alert.show();
        } else {
            relationshipService.updateIdAndName(originalStudent.getRelationships(), originalStudent.getId(), id.getText(), name.getText());

            studentRepository.delete(originalStudent);
            originalStudent = studentRepository.save(loadValuesIntoStaffModel());
            refreshBasicInfo(originalStudent);
            logger.logInfo(this.getClass().toString(), "更改学生档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Student loadValuesIntoStaffModel() {
        return new Student(id.getText(), name.getText(), baptismalName.getText(), gender.getValue(), birthday.getValue(),
                isGregorianCalendar.isSelected(), baptismalDate.getValue(), confirmationDate.getValue(), marriageDate.getValue(),
                deathDate.getValue(), attribute.getValue(), phone.getText(), altPhone.getText(),
                address.getText(), experience.getText(), talent.getText(), resource.getText(), education.getText(),
                staff.getValue(), originalStudent.getVisits(), originalStudent.getInternships(), originalStudent.getRelationships(),
                originalStudent.getGrades()
        );
    }

    public void delete(){
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel(){
        if (!isDeleteOperation) {
            refreshBasicInfo(originalStudent);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close(){
        ((Stage)id.getScene().getWindow()).close();
    }

    public void addVisit(){
        KRSTManagementSoftware.openWindow(AddVisit.class);
        Visit vis = (Visit) dataPassService.getValue();
        if (vis != null) {
            originalStudent.getVisits().add(vis);
            originalStudent = studentRepository.save(originalStudent);
            visit.getItems().add(vis);
        }
    }

    public void addInternship(){
        KRSTManagementSoftware.openWindow(AddInternship.class);
        Internship intern = (Internship) dataPassService.getValue();
        if (intern != null) {
            originalStudent.getInternships().add(intern);
            originalStudent = studentRepository.save(originalStudent);
            internship.getItems().add(intern);
        }
    }

    public void addRelationship(){
        dataPassService.setValue(new Pair<>(Relation.Type.STAFF, new Pair<>(originalStudent.getId(), originalStudent.getName())));
        KRSTManagementSoftware.openWindow(AddRelationship.class);
        Student tempStudent = (Student) dataPassService.getValue();
        if (tempStudent != null) {
            originalStudent = tempStudent;
            relationship.getItems().setAll(originalStudent.getRelationships());
        }
    }

    public void addGrade() {
        // LEAVE EMPTY FOR NOW
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName, phone, altPhone, address, experience, talent, resource, education);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        setComboBoxEditableMode(state, gender, attribute, staff);
        setCheckBoxEditableMode(state, isGregorianCalendar);
        staff.setEditable(state);
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
