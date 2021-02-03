package org.krst.app.controllers.staff;

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
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.models.RelationPassModel;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.views.share.*;
import org.krst.app.views.staff.AddEvaluation;
import org.krst.app.views.staff.EvaluationInfoPage;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Staff, the Staff model that need to be displayed
 * Out : None
 */
@FXMLController
public class StaffInfoPageController implements InfoPageControllerTemplate {
    @FXML private SplitPane splitPane;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField baptismalName;
    @FXML private ComboBox<String> gender;
    @FXML private DatePicker birthday;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private TextField age;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;
    @FXML private TextField title;
    @FXML private TextArea responsibility;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private TextArea address;
    @FXML private TextArea experience;
    @FXML private TextArea talent;
    @FXML private TextArea resource;
    @FXML private TextArea education;

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
    @FXML private TableView<Evaluation> evaluation;
    @FXML private TableColumn<Evaluation, String> evaluation_year;
    @FXML private TableColumn<Evaluation, String> evaluation_content;
    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;

    @Autowired private StaffRepository staffRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private DataPassService dataPassService;
    @Autowired private RelationshipService relationshipService;
    @Autowired private Logger logger;

    private Staff originalStaff;
    private Boolean isDeleteOperation;

    @FXML public void initialize() {
        Staff staff = (Staff)dataPassService.getValue();
        if (staff == null) {
            close();
            return;
        }
        refreshAll(staff);
        initDefaultComponents();
        setEditableMode(false);
        splitPane.getDividers().get(0).positionProperty()
                .addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(0.4074));
    }

    private void initDefaultComponents() {
        visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visit.setRowFactory( tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalStaff, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                    Pair<Boolean, Visit> returnedData = (Pair<Boolean, Visit>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalStaff.getVisits().removeIf(vis -> vis.getId().equals(row.getItem().getId()));
                        if (returnedData.getKey()) {
                            originalStaff.getVisits().add(returnedData.getValue());
                            visit.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            originalStaff = staffRepository.save(originalStaff);
                            visitRepository.delete(row.getItem());
                            logger.logInfo(this.getClass().toString(), "删除探访记录：探访记录，类型-员工，编号-{}，姓名-{}", row.getItem().getId().toString(), name.getText());
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
                    dataPassService.setValue(new Pair<>(originalStaff, row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(InternshipInfoPage.class);
                    Pair<Boolean, Internship> returnedData = (Pair<Boolean, Internship>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalStaff.getInternships().remove(row.getItem());
                        if (returnedData.getKey()) {
                            originalStaff.getInternships().add(returnedData.getValue());
                            internship.getItems().set(row.getIndex(), returnedData.getValue());
                            logger.logInfo(this.getClass().toString(), "更改员工服侍记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        } else {
                            internship.getItems().remove(row.getIndex());
                            logger.logInfo(this.getClass().toString(), "删除员工服侍记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        }
                        originalStaff = staffRepository.save(originalStaff);
                    }
                }
            });
            return row ;
        });
        evaluation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        evaluation.setRowFactory( tv -> {
            TableRow<Evaluation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(new Pair<>(originalStaff.getName(), row.getItem().clone()));
                    KRSTManagementSoftware.openWindow(EvaluationInfoPage.class);
                    Pair<Boolean, Evaluation> returnedData = (Pair<Boolean, Evaluation>) dataPassService.getValue();
                    if (returnedData != null) {
                        originalStaff.getEvaluations().remove(row.getItem());
                        if (returnedData.getKey()) {
                            originalStaff.getEvaluations().add(returnedData.getValue());
                            evaluation.getItems().set(row.getIndex(), returnedData.getValue());
                            logger.logInfo(this.getClass().toString(), "更改员工评定记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        } else {
                            originalStaff.getEvaluations().remove(row.getItem());
                            evaluation.getItems().remove(row.getIndex());
                            logger.logInfo(this.getClass().toString(), "删除员工评定记录：编号-{}，姓名-{}", id.getText(), name.getText());
                        }
                        originalStaff = staffRepository.save(originalStaff);
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
                    Relation tempRelation = row.getItem();
                    RelationPassModel relationPassModel = new RelationPassModel(
                            Relation.Type.STAFF,
                            originalStaff.getId(),
                            originalStaff.getName(),
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
                            originalStaff.getRelationships().remove(row.getItem());
                            Relation temp = row.getItem();
                            temp.setRelationship(returnedData.getValue());
                            relationship.getItems().set(row.getIndex(), temp);
                            originalStaff.getRelationships().add(row.getItem());
                        } else {
                            originalStaff.getRelationships().remove(row.getItem());
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

        evaluation_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        evaluation_content.setCellValueFactory(new PropertyValueFactory<>("comment"));

        relationship_relation.setCellValueFactory(new PropertyValueFactory<>("relationship"));
        relationship_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        relationship_type.setCellValueFactory(new PropertyValueFactory<>("type"));
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
        relationship_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                gender.getEditor().setText(newValue);
        });
    }

    private void refreshAll(Staff staff) {
        if (staff == null) return;
        originalStaff = staff;
        refreshBasicInfo(staff);
        refreshOtherInfo(staff);
    }

    private void refreshBasicInfo(Staff staff) {
        id.setText(staff.getId());
        name.setText(staff.getName());
        baptismalName.setText(staff.getBaptismalName());
        gender.getSelectionModel().select(staff.getGender() == null ? null : staff.getGender());
        birthday.setValue(staff.getBirthday());
        isGregorianCalendar.setSelected(staff.getIsGregorianCalendar() != null && staff.getIsGregorianCalendar());
        if (staff.getBirthday() != null)
            age.setText(staff.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        baptismalDate.setValue(staff.getBaptismalDate());
        confirmationDate.setValue(staff.getConfirmationDate());
        marriageDate.setValue(staff.getMarriageDate());
        deathDate.setValue(staff.getDeathDate());
        title.setText(staff.getTitle());
        responsibility.setText(staff.getResponsibility());
        phone.setText(staff.getPhone());
        altPhone.setText(staff.getAltPhone());
        address.setText(staff.getAddress());
        education.setText(staff.getEducation());
        resource.setText(staff.getResource());
        talent.setText(staff.getTalent());
        experience.setText(staff.getExperience());
    }

    private void refreshOtherInfo(Staff staff) {
        if (staff.getVisits() != null)
            visit.getItems().addAll(staff.getVisits());
        if (staff.getInternships() != null)
            internship.getItems().addAll(staff.getInternships());
        if (staff.getEvaluations() != null)
            evaluation.getItems().addAll(staff.getEvaluations());
        if (staff.getRelationships() != null)
            relationship.getItems().addAll(staff.getRelationships());
    }

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept() {
        if (isDeleteOperation) {
            staffRepository.delete(originalStaff);
            logger.logInfo(this.getClass().toString(), "删除员工档案：编号-{}，姓名-{}", id.getText(), name.getText());
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改员工档案失败");
            alert.setHeaderText("失败原因：未填入员工编号");
            alert.setContentText("解决方法：请输入员工编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalStaff.getId())) {
            if (!name.getText().equals(originalStaff.getName()))
                relationshipService.updateIdAndName(originalStaff.getRelationships(), originalStaff.getId(), id.getText(), name.getText());

            originalStaff = staffRepository.save(loadValuesIntoStaffModel());
            logger.logInfo(this.getClass().toString(), "更改员工档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (staffRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改员工档案失败");
            alert.setHeaderText("失败原因：该员工编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改员工编号");
            alert.show();
        } else {
            relationshipService.updateIdAndName(originalStaff.getRelationships(), originalStaff.getId(), id.getText(), name.getText());

            staffRepository.delete(originalStaff);
            originalStaff = staffRepository.save(loadValuesIntoStaffModel());
            logger.logInfo(this.getClass().toString(), "更改员工档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Staff loadValuesIntoStaffModel() {
        return new Staff(id.getText(), name.getText(), baptismalName.getText(), gender.getValue(), birthday.getValue(),
                isGregorianCalendar.isSelected(), baptismalDate.getValue(), confirmationDate.getValue(), marriageDate.getValue(),
                deathDate.getValue(), title.getText(), responsibility.getText(), phone.getText(), altPhone.getText(),
                address.getText(), education.getText(), experience.getText(), talent.getText(), resource.getText(),
                originalStaff.getVisits(), originalStaff.getInternships(), originalStaff.getEvaluations(), originalStaff.getRelationships());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalStaff);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName, title, responsibility,
                phone, altPhone, address, experience, talent, resource, education);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        setCheckBoxEditableMode(state, isGregorianCalendar);
        setComboBoxEditableMode(state, gender);
    }

    // true: hide change/delete/close buttons; show accept/cancel buttons
    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: red" : "-fx-text-fill: black");
        delete.setVisible(!state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }

    public void addVisit() {
        KRSTManagementSoftware.openWindow(AddVisit.class);
        Visit vis = (Visit) dataPassService.getValue();
        if (vis != null) {
            originalStaff.getVisits().add(vis);
            originalStaff = staffRepository.save(originalStaff);
            visit.getItems().add(vis);
        }
    }

    public void addInternship() {
        KRSTManagementSoftware.openWindow(AddInternship.class);
        Internship intern = (Internship) dataPassService.getValue();
        if (intern != null) {
            originalStaff.getInternships().add(intern);
            originalStaff = staffRepository.save(originalStaff);
            internship.getItems().add(intern);
        }
    }

    public void addEvaluation() {
        dataPassService.setValue(originalStaff);
        KRSTManagementSoftware.openWindow(AddEvaluation.class);
        Evaluation eva = (Evaluation) dataPassService.getValue();
        if (eva != null) {
            originalStaff.getEvaluations().add(eva);
            originalStaff = staffRepository.save(originalStaff);
            evaluation.getItems().add(eva);
        }
    }

    public void addRelationship() {
        dataPassService.setValue(new Pair<>(Relation.Type.STAFF, new Pair<>(originalStaff.getId(), originalStaff.getName())));
        KRSTManagementSoftware.openWindow(AddRelationship.class);
        Staff tempStaff = (Staff) dataPassService.getValue();
        if (tempStaff != null) {
            originalStaff = tempStaff;
            relationship.getItems().setAll(originalStaff.getRelationships());
        }
    }
}
