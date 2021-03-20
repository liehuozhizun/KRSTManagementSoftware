package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.*;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.krst.app.views.share.AddRelationship;
import org.krst.app.views.share.AddVisit;
import org.krst.app.views.share.VisitInfoPage;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Teacher, the Teacher model that need to be displayed
 * Out : Boolean, false delete operation
 *       OR
 *       null, update operation or nothing changed
 */
@FXMLController
public class TeacherInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField id, name, baptismalName, age;
    @FXML private DatePicker birthday, baptismalDate, confirmationDate, marriageDate, deathDate;
    @FXML private TextField leader, leaderPhone, altLeader, altLeaderPhone, phone, altPhone;
    @FXML private TextArea address, experience, talent, resource, education;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private ComboBox<String> gender;
    @FXML private ComboBox<Attribute> attribute;
    @FXML private ComboBox<Staff> staff;

    @FXML private Button accept, delete, close, change, cancel;

    @FXML private TableView<Visit> visit;
    @FXML private TableColumn<Visit, String> visit_date;
    @FXML private TableColumn<Visit, String> visit_content;
    @FXML private TableColumn<Visit, String> visit_summary;
    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;
    @FXML private SplitPane splitPane;

    @Autowired private TeacherRepository teacherRepository;
    @Autowired private CacheService cacheService;
    @Autowired private Logger logger;
    @Autowired private DataPassService dataPassService;
    @Autowired private VisitRepository visitRepository;
    @Autowired private RelationshipService relationshipService;

    private Teacher originalTeacher = null;
    private Boolean isDeleteOperation = false;

    @FXML public void initialize() {
        originalTeacher = (Teacher) dataPassService.getValue();
        if (originalTeacher == null) {
            close();
            return;
        }

        initDefaultComponents();
        refreshAll(originalTeacher);
    }

    private void initDefaultComponents() {
        splitPane.getDividers().get(0).positionProperty()
                .addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(0.5757));
        setEditableMode(false);
        setButtonMode(false);

        initLeftSideDefaultComponents();
        initRightSideDefaultComponents();
    }

    private void initLeftSideDefaultComponents() {
        attribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        attribute.getItems().addAll(cacheService.getAttributes());

        attribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            if (Constants.CREATE_PROMPT.equals(newValue.getAttribute())) {
                KRSTManagementSoftware.openWindow(AddAttribute.class, true);
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
        visit_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        visit_content.setCellValueFactory(new PropertyValueFactory<>("content"));
        visit_summary.setCellValueFactory(new PropertyValueFactory<>("summary"));
        visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visit.setRowFactory( tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalTeacher, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                    Pair<Boolean, Visit> returnedData = (Pair<Boolean, Visit>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalTeacher.getVisits().removeIf(vis -> vis.getId().equals(row.getItem().getId()));
                        if (returnedData.getKey()) {
                            originalTeacher.getVisits().add(returnedData.getValue());
                            visit.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            originalTeacher = teacherRepository.save(originalTeacher);
                            visitRepository.delete(row.getItem());
                            logger.logInfo(this.getClass().toString(), "删除探访记录：类型-教师，探访记录编号-{}，姓名-{}", row.getItem().getId().toString(), name.getText());
                            visit.getItems().remove(row.getIndex());
                        }
                    }
                }
            });
            return row ;
        });

        relationship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        relationship_relation.setCellValueFactory(new PropertyValueFactory<>("relationship"));
        relationship_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        relationship_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        relationship_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        relationship_type.setCellFactory(new Callback<TableColumn<Relation, Relation.Type>, TableCell<Relation, Relation.Type>>() {
            @Override
            public TableCell<Relation, Relation.Type> call(TableColumn<Relation, Relation.Type> param) {
                return new TableCell<Relation, Relation.Type>() {
                    @Override
                    protected void updateItem(Relation.Type item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(item.toString());
                        }
                    }
                };
            }
        });
    }

    private void refreshAll(Teacher teacher){
        refreshBasicInfo(teacher);
        refreshOtherInfo(teacher);
    }

    private void refreshBasicInfo(Teacher teacher) {
        id.setText(teacher.getId());
        name.setText(teacher.getName());
        baptismalName.setText(teacher.getBaptismalName());
        gender.getSelectionModel().select(teacher.getGender() == null ? null : teacher.getGender());
        birthday.setValue(teacher.getBirthday());
        isGregorianCalendar.setSelected(teacher.getIsGregorianCalendar() != null && teacher.getIsGregorianCalendar());
        if (teacher.getBirthday() != null)
            age.setText(teacher.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        baptismalDate.setValue(teacher.getBaptismalDate());
        confirmationDate.setValue(teacher.getConfirmationDate());
        marriageDate.setValue(teacher.getMarriageDate());
        deathDate.setValue(teacher.getDeathDate());
        if (teacher.getAttribute() != null) {
            attribute.getSelectionModel().select(teacher.getAttribute());
        }
        phone.setText(teacher.getPhone());
        altPhone.setText(teacher.getAltPhone());
        address.setText(teacher.getAddress());
        experience.setText(teacher.getExperience());
        talent.setText(teacher.getTalent());
        resource.setText(teacher.getResource());
        education.setText(teacher.getEducation());
        if (teacher.getStaff() != null) {
            staff.getSelectionModel().select(teacher.getStaff());
        }
    }

    private void refreshOtherInfo(Teacher teacher) {
        if (teacher.getVisits() != null) {
            visit.getItems().setAll(teacher.getVisits());
        if (teacher.getRelationships() != null)
            relationship.getItems().setAll(teacher.getRelationships());
        }
    }

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept() {
        if (isDeleteOperation) {
            teacherRepository.delete(originalTeacher);
            dataPassService.setValue(false);
            logger.logInfo(getClass().toString(), "删除教师档案，编号：{}，姓名：{}", id.getText(), name.getText());
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改教师档案失败");
            alert.setHeaderText("失败原因：未填入教师编号");
            alert.setContentText("解决方法：请输入教师编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalTeacher.getId())) {
            if (!name.getText().equals(originalTeacher.getName()))
                relationshipService.updateIdAndName(originalTeacher.getRelationships(), originalTeacher.getId(), id.getText(), name.getText());

            originalTeacher = teacherRepository.save(loadValuesIntoTeacherModel());
            logger.logInfo(this.getClass().toString(), "更改教师档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (teacherRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改教师档案失败");
            alert.setHeaderText("失败原因：该教师编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改教师编号");
            alert.show();
        } else {
            relationshipService.updateIdAndName(originalTeacher.getRelationships(), originalTeacher.getId(), id.getText(), name.getText());

            teacherRepository.delete(originalTeacher);
            originalTeacher = teacherRepository.save(loadValuesIntoTeacherModel());
            logger.logInfo(this.getClass().toString(), "更改教师档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Teacher loadValuesIntoTeacherModel() {
        return new Teacher(id.getText(), name.getText(), baptismalName.getText(), gender.getValue(), birthday.getValue(),
                isGregorianCalendar.isSelected(), baptismalDate.getValue(), confirmationDate.getValue(),
                marriageDate.getValue(), deathDate.getValue(), attribute.getValue(), phone.getText(),
                altPhone.getText(), address.getText(), experience.getText(), talent.getText(), resource.getText(),
                education.getText(), staff.getValue(), originalTeacher.getVisits(), originalTeacher.getRelationships());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalTeacher);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

    public void addRelationship() {
        dataPassService.setValue(new Pair<>(Relation.Type.TEACHER, new Pair<>(originalTeacher.getId(), originalTeacher.getName())));
        KRSTManagementSoftware.openWindow(AddRelationship.class);
        Teacher tempTeacher = (Teacher) dataPassService.getValue();
        if (tempTeacher != null) {
            originalTeacher = tempTeacher;
            relationship.getItems().setAll(originalTeacher.getRelationships());
        }
    }

    public void addVisit() {
        KRSTManagementSoftware.openWindow(AddVisit.class);
        Visit vis = (Visit) dataPassService.getValue();
        if (vis != null) {
            originalTeacher.getVisits().add(vis);
            visit.getItems().add(vis);
            originalTeacher = teacherRepository.save(originalTeacher);
        }
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName, phone, altPhone, address, experience, talent, resource, education);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        setComboBoxEditableMode(state, gender, attribute, staff);
        setCheckBoxEditableMode(state, isGregorianCalendar);
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
