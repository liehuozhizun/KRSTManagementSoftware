package org.krst.app.controllers.student;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.CourseRepository;
import org.krst.app.repositories.StudentRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.springframework.beans.factory.annotation.Autowired;


@FXMLController
public class StudentInfoController implements InfoPageControllerTemplate {
    private String originalStudentId;
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

    @Autowired private StudentRepository studentRepository;
    @Autowired private CacheService cacheService;
    @Autowired private Logger logger;
    @Autowired private DataPassService dataPassService;
    @Autowired private CourseRepository courseRepository;
    private Student originalStudent;
    private Boolean isDeleteOperation;

    @FXML
    public void initialize() {
        staff.getItems().addAll(cacheService.getStaffs());
        refreshAttributeComboBoxContent();
        Student student = (Student) dataPassService.getValue();
        gender.getItems().addAll("男","女");
        refreshAll(student);
        setEditableMode(false);
//        initDefaultComponents();
    }
    public void addVisit(){//探访记录id是谁的id 谁探访谁
        return;
    }
    public void addInternship(){
        return;
    }
    public void addscore(){
        return;
    }
    public void addRelationship(){
        return;
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
        System.out.println("change");
//        System.out.println(courseRepository.findById("1"));

//        isEditable(true);
//        isInChangePage(true);
//        attribute.setValue(originalStudent.getAttribute());
    }

    public void delete(){
        studentRepository.deleteById(originalStudentId);//我觉得这里要有alart，不然失手删掉了很难找回来
        turnOff();
    }

    public void close(){
        turnOff();
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

    private void refreshAttributeComboBoxContent() {
        attribute.getItems().clear();
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());
    }

    public void turnOff() {
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName,phone,
                altPhone, address, experience, talent, resource);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        isGregorianCalendar.setDisable(!state);
        gender.setMouseTransparent(!state);
        attribute.setMouseTransparent(!state);
        staff.setMouseTransparent(!state);

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
    private void refreshAll(Student student) {
        if (student == null) return;
        originalStudent = student;
        refreshBasicInfo(student);
//        refreshOtherInfo(student);
    }
    private void refreshBasicInfo(Student student) {
        id.setText(student.getId());
        name.setText(student.getName());
        baptismalName.setText(student.getBaptismalName());
        gender.setValue(student.getGender());
//        gender.getItems().addAll("男", "女");
//        gender.getSelectionModel().select(staff.getGender() == null ? null : staff.getGender());
        birthday.setValue(student.getBirthday());
        isGregorianCalendar.setSelected(student.getIsGregorianCalendar() != null && student.getIsGregorianCalendar());//不太懂这里 （和我的不一样）
        if (student.getBirthday() != null)
            age.setText(student.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        baptismalDate.setValue(student.getBaptismalDate());
        confirmationDate.setValue(student.getConfirmationDate());
        marriageDate.setValue(student.getMarriageDate());
        deathDate.setValue(student.getDeathDate());
//        title.setText(staff.getTitle());
//        responsibility.setText(staff.getResponsibility());
        phone.setText(student.getPhone());
        altPhone.setText(student.getAltPhone());
        address.setText(student.getAddress());
//        education.setText(staff.getEducation());
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
//        attribute.setCellFactory(new Callback<ListView<Attribute>, ListCell<Attribute>>() {
//            @Override
//            public ListCell<Attribute> call(ListView<Attribute> param) {
//                return new ListCell<Attribute>() {
//                    @Override
//                    protected void updateItem(Attribute item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item == null || empty) {
//                            setGraphic(null);
//                        } else {
//                            setText(item.getAttribute());
//                        }
//                    }
//                };
//            }
//        });
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

    private Student loadValuesIntoStaffModel() {
        return new Student(id.getText(), name.getText(), baptismalName.getText(), gender.getValue(), birthday.getValue(),
                isGregorianCalendar.isSelected(), baptismalDate.getValue(), confirmationDate.getValue(), marriageDate.getValue(),
                deathDate.getValue(), attribute.getValue(), phone.getText(), altPhone.getText(),
                address.getText(), experience.getText(), talent.getText(), resource.getText(),
                staff.getValue(), originalStudent.getVisits(), originalStudent.getInternships(), originalStudent.getRelationships(),
                originalStudent.getGrades()
                );
    }

}
