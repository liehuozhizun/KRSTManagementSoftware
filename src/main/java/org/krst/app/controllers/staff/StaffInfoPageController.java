package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.*;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.views.share.AddInternship;
import org.krst.app.views.share.AddVisit;
import org.krst.app.views.staff.AddEvaluation;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class StaffInfoPageController extends InfoPageControllerTemplate {
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
    @Autowired private DataPassService dataPassService;
    @Autowired private Logger logger;

    private Staff originalStaff;
    private Boolean isDeleteOperation;

    @FXML public void initialize() {
        Staff staff = (Staff)dataPassService.getValue();
        staff = staffRepository.findById("1").orElse(new Staff());
        refreshAll(staff);
        initDefaultComponents();
        setEditableMode(false);
    }

    private void initDefaultComponents() {
        visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visit.setRowFactory( tv -> {
            TableRow<Visit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem());
                    System.out.println("打开VisitInfoPage窗口");
//                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                }
            });
            return row ;
        });
        internship.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        internship.setRowFactory( tv -> {
            TableRow<Internship> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem());
                    System.out.println("打开InternshipInfoPage窗口");
//                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
                }
            });
            return row ;
        });
        evaluation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        evaluation.setRowFactory( tv -> {
            TableRow<Evaluation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem());
                    System.out.println("打开EvaluationInfoPage窗口");
//                    KRSTManagementSoftware.openWindow(VisitInfoPage.class);
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

        evaluation_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        evaluation_content.setCellValueFactory(new PropertyValueFactory<>("comment"));

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
                            String type = null;
                            switch (item) {
                                case STUDENT:
                                    type = "学生";
                                    break;
                                case TEACHER:
                                    type = "教师";
                                    break;
                                case STAFF:
                                    type = "员工";
                                    break;
                                case PERSON:
                                    type = "普通";
                                    break;
                                default: break;
                            }
                            this.setText(type);
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
        gender.getItems().addAll("男", "女");
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
            originalStaff = staffRepository.save(loadValuesIntoStaffModel());
            refreshBasicInfo(originalStaff);
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
            staffRepository.delete(originalStaff);
            originalStaff = staffRepository.save(loadValuesIntoStaffModel());
            refreshBasicInfo(originalStaff);
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
    protected void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName, title, responsibility,
                phone, altPhone, address, experience, talent, resource, education);
        setDatePickerEditableMode(state, birthday, baptismalDate, confirmationDate, marriageDate, deathDate);
        isGregorianCalendar.setDisable(!state);
        gender.setDisable(!state);
    }

    @Override
    // true: hide change/delete/close buttons; show accept/cancel buttons
    protected void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        delete.setVisible(!state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }

    public void addVisit() {
        KRSTManagementSoftware.openWindow(AddVisit.class);
        Visit vis = (Visit) dataPassService.getValue();
        if (vis != null) {
            originalStaff.getVisits().add(vis);
            staffRepository.save(originalStaff);
            visit.getItems().add(vis);
        }
    }

    public void addInternship() {
        KRSTManagementSoftware.openWindow(AddInternship.class);
        Internship intern = (Internship) dataPassService.getValue();
        if (intern != null) {
            originalStaff.getInternships().add(intern);
            staffRepository.save(originalStaff);
            internship.getItems().add(intern);
        }
    }

    public void addEvaluation() {
        dataPassService.setValue(originalStaff);
        KRSTManagementSoftware.openWindow(AddEvaluation.class);
        Evaluation eva = (Evaluation) dataPassService.getValue();
        if (eva != null) {
            originalStaff.getEvaluations().add(eva);
            staffRepository.save(originalStaff);
            evaluation.getItems().add(eva);
        }
    }

    public void addRelationship() {
//        KRSTManagementSoftware.openWindow(AddRelation.class);
        Relation relation = (Relation) dataPassService.getValue();
        if (relation != null) {
            originalStaff.getRelationships().add(relation);
            staffRepository.save(originalStaff);
            relationship.getItems().add(relation);
        }
    }
}
