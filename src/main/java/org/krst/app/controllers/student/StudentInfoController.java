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
import org.krst.app.repositories.CourseRepository;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.*;
import org.springframework.beans.factory.annotation.Autowired;


@FXMLController
public class StudentInfoController implements InfoPageControllerTemplate {
    private String originalStudentId;
    @FXML private SplitPane splitPane;
    @FXML private TextField leader;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField baptismalName;
    @FXML private TextField leaderPhone;
    @FXML private TextField altLeader;
    @FXML private TextField altLeaderPhone;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private ComboBox<Attribute> attribute;
    @FXML private ComboBox<String> gender;
    @FXML private ComboBox<Staff> staff;//还一点没写
    @FXML private CheckBox isGregorianCalendar;
    @FXML private DatePicker birthday;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;
    @FXML private javafx.scene.control.TextArea address;
    @FXML private javafx.scene.control.TextArea experience;
    @FXML private javafx.scene.control.TextArea education;
    @FXML private javafx.scene.control.TextArea talent;
    @FXML private TextArea resource;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button change;
    @FXML private Button close;
    @FXML private Button addVisit;
    @FXML private Button addInternship;
    @FXML private Button addscore;
    @FXML private Button addRelationship;
    @FXML private TextField age;

    @FXML private TableView<Internship> internship;
    @FXML private TableColumn<Internship, String> internship_startDate;
    @FXML private TableColumn<Internship, String> internship_endDate;
    @FXML private TableColumn<Internship, String> internship_purpose;
    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;

    @FXML private TableView<Visit> visit;
    @Autowired private VisitRepository visitRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CacheService cacheService;
    @Autowired private Logger logger;
    @Autowired private DataPassService dataPassService;
    @Autowired private CourseRepository courseRepository;

    @FXML private TableColumn<Visit, String> visit_date;
    @FXML private TableColumn<Visit, String> visit_content;
    @FXML private TableColumn<Visit, String> visit_summary;


    private Student originalStudent;
    private Boolean isDeleteOperation;




    @FXML
    public void initialize() {
        staff.getItems().addAll(cacheService.getStaffs());

        //refreshAttributeComboBoxContent();
        Student student = (Student) dataPassService.getValue();
        refreshAll(student);
        //refreshAll(student);
        setEditableMode(false);
        initDefaultComponents();
        splitPane.getDividers().get(0).positionProperty()
                .addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(0.4074));
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void delete(){
        isDeleteOperation = true;
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
            originalStudent = studentRepository.save(loadValuesIntoStaffModel());
            refreshBasicInfo(originalStudent);
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
            studentRepository.delete(originalStudent);
            originalStudent = studentRepository.save(loadValuesIntoStaffModel());
            refreshBasicInfo(originalStudent);
            logger.logInfo(this.getClass().toString(), "更改学生档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    public void cancel(){
        if (!isDeleteOperation) {
            refreshBasicInfo(originalStudent);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close(){
        turnOff();
    }

    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: #ff0000" : "-fx-text-fill: black");
        delete.setVisible(!state);
        cancel.setVisible(state);
        close.setVisible(!state);

    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName,phone,education,
                altPhone, address, experience, talent, resource);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        isGregorianCalendar.setDisable(!state);
        gender.setMouseTransparent(!state);
        attribute.setMouseTransparent(!state);
        staff.setMouseTransparent(!state);

    }

    private void refreshBasicInfo(Student student) {
        id.setText(student.getId());
        name.setText(student.getName());
        baptismalName.setText(student.getBaptismalName());
        gender.setValue(student.getGender());
        education.setText(student.getEducation());
        birthday.setValue(student.getBirthday());
        isGregorianCalendar.setSelected(student.getIsGregorianCalendar() != null && student.getIsGregorianCalendar());
        if (student.getBirthday() != null)
            age.setText(student.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        baptismalDate.setValue(student.getBaptismalDate());
        confirmationDate.setValue(student.getConfirmationDate());
        marriageDate.setValue(student.getMarriageDate());
        deathDate.setValue(student.getDeathDate());
        phone.setText(student.getPhone());
        altPhone.setText(student.getAltPhone());
        address.setText(student.getAddress());
        resource.setText(student.getResource());
        talent.setText(student.getTalent());
        experience.setText(student.getExperience());
        staff.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
            @Override
            public ListCell<Staff> call(ListView<Staff> param) {
                return new ListCell<Staff>() {
                    @Override
                    protected void updateItem(Staff item, boolean empty){
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }

                    }

                };
            }
        });
        staff.setConverter(new StringConverter<Staff>() {
            @Override
            public String toString(Staff staff) {
                return staff == null ? null : staff.getName();
            }

            @Override
            public Staff fromString(String string) {
                return staff.getItems().stream().filter(attribute ->
                        staff.getItems().equals(string)).findFirst().orElse(null);
            }
        });
        staff.setValue(student.getStaff());
        attribute.setCellFactory(new Callback<ListView<Attribute>, ListCell<Attribute>>() {
            @Override
            public ListCell<Attribute> call(ListView<Attribute> param) {
                return new ListCell<Attribute>() {
                    @Override
                    protected void updateItem(Attribute item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getAttribute());
                        }
                    }
                };
            }
        });
        attribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                leader.setText("");
                leaderPhone.setText("");
                altLeader.setText("");
                altLeaderPhone.setText("");
            } else {
                if (Constants.CREATE_PROMPT.equals(newValue.getAttribute())) {
                    KRSTManagementSoftware.openWindow(AddAttribute.class);
                    refreshAttributeComboBoxContent();
                    attribute.getSelectionModel().selectLast();
                } else {
                    leader.setText(newValue.getLeader());
                    leaderPhone.setText(newValue.getLeaderPhone());
                    altLeader.setText(newValue.getAltLeader());
                    altLeaderPhone.setText(newValue.getAltLeaderPhone());
                }
            }
        });
        attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
        attribute.setValue(student.getAttribute());
        leader.setText(student.getAttribute().getLeader());
        leaderPhone.setText(student.getAttribute().getLeaderPhone());
        altLeader.setText(student.getAttribute().getAltLeader());
        altLeaderPhone.setText(student.getAttribute().getAltLeaderPhone());

//        private void initDefaultComponents() {
//            System.out.println("q");;
//        }

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
    public void addscore(){
        return;
    }
    public void addRelationship(){
//        KRSTManagementSoftware.openWindow(AddRelation.class);
        Relation relation = (Relation) dataPassService.getValue();
        if (relation != null) {
            originalStudent.getRelationships().add(relation);
            originalStudent = studentRepository.save(originalStudent);
            relationship.getItems().add(relation);
        }
    }

    private void refreshAttributeComboBoxContent() {
        attribute.getItems().clear();
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());
    }

    public void turnOff() {
        ((Stage)id.getScene().getWindow()).close();
    }


    private void refreshOtherInfo(Student student){
    if (student.getVisits() != null)
        visit.getItems().addAll(student.getVisits());
    if (student.getInternships() != null)
        internship.getItems().addAll(student.getInternships());
    if (student.getRelationships() != null)
        relationship.getItems().addAll(student.getRelationships());

    }
    private void initDefaultComponents(){
        visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visit.setRowFactory( tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // double click a nonempty row
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    dataPassService.setValue(new Pair<>(originalStudent, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                    Pair<Boolean, Visit> returnedData = (Pair<Boolean, Visit>) dataPassService.getValue();
                    if (returnedData != null) { // no changes are made, just ignore it
                        originalStudent.getVisits().removeIf(vis -> vis.getId().equals(row.getItem().getId())); // remove old data
                        if (returnedData.getKey()) { // true: update operation
                            originalStudent.getVisits().add(returnedData.getValue()); // store new Visit into originalStaff
                            visit.getItems().set(row.getIndex(), returnedData.getValue()); // update data for row in TableView
                        } else { // false: delete operation
                            originalStudent = studentRepository.save(originalStudent); // remove mapping between Staff and Visit
                            visitRepository.delete(row.getItem()); // remove Visit model in database
                            logger.logInfo(this.getClass().toString(), "删除探访记录：探访记录编号-{}，姓名-{}", row.getItem().getId().toString(), name.getText());
                            visit.getItems().remove(row.getIndex()); // remove data from TableView
                        }
                    }
                }
            });
            return row;
        });

        internship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        internship.setRowFactory( tv -> {
            TableRow<Internship> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalStudent, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(InternshipInfoPage.class);
                    Pair<Boolean, Internship> returnedData = (Pair<Boolean, Internship>) dataPassService.getValue();
                    if (returnedData != null) { // no changes are made, just ignore it
                        originalStudent.getInternships().remove(row.getItem()); // remove old data
                        if (returnedData.getKey()) { // true: update operation
                            originalStudent.getInternships().add(returnedData.getValue()); // store new Visit into originalStaff
                            internship.getItems().set(row.getIndex(), returnedData.getValue()); // update data for row in TableView
                            logger.logInfo(this.getClass().toString(), "更改员工服侍记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        } else { // false: delete operation
                            internship.getItems().remove(row.getIndex()); // remove data from TableView
                            logger.logInfo(this.getClass().toString(), "删除员工服侍记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        }
                        originalStudent = studentRepository.save(originalStudent); // remove this Internship from Staff
                    }
                }
            });
            return row ;
        });

        relationship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        relationship.setRowFactory( tv -> {
            TableRow<Relation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem());
//                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
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
                            this.setText(item.getTypeString());
                        }
                    }
                };
            }
        });
        relationship_id.setCellValueFactory(new PropertyValueFactory<>("id"));
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

    private void refreshAll(Student student) {
        if (staff == null) return;
        originalStudent = student;
        refreshBasicInfo(student);
        refreshOtherInfo(student);
    }
}
