package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.*;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.repositories.StaffRepository;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.krst.app.views.share.AddVisit;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class TeacherInfoPageController {

    @FXML private TextField id,name,baptismalName;
    @FXML private TextField leader;
    @FXML private TextField leaderPhone;
    @FXML private TextField altLeader;
    @FXML private TextField altLeaderPhone;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private DatePicker birthday;
    @FXML private DatePicker baptismalDate;
    @FXML private DatePicker confirmationDate;
    @FXML private DatePicker marriageDate;
    @FXML private DatePicker deathDate;
    @FXML private TextArea address;
    @FXML private TextArea experience;
    @FXML private TextArea talent;
    @FXML private TextArea resource;
    @FXML private Button confirm,delete,close,change,cancel;
    @FXML private ComboBox <String>gender;
    @FXML private ComboBox <Attribute>changeableAttribute;
    @FXML private ComboBox <Staff>changeableStaff;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private TableView<Visit> visit;
    @FXML private TableColumn<Visit, String> visit_date;
    @FXML private TableColumn<Visit, String> visit_content;
    @FXML private TableColumn<Visit, String> visit_summary;
    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;

    @Autowired private StaffRepository staffRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;
    @Autowired
    private DataPassService dataPassService;
    private Staff originalStaff;
    Teacher selectedOne;
    @FXML
    public void initialize() {
        selectedOne =(Teacher) dataPassService.getValue();

        start(selectedOne);
        initSetRightSide();
        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            //gender.setPromptText( gender.getSelectionModel().getSelectedItem());
            gender.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String gender) {
                    return gender;
                }

                @Override
                public String fromString(String string) {
                    return gender.getItems().stream().filter(gender->
                            gender.equals(string)).findFirst().orElse(null);
                }
            });
        });
        changeableAttribute.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (Constants.CREATE_PROMPT.equals(changeableAttribute.getSelectionModel().getSelectedItem().getAttribute())) {
                KRSTManagementSoftware.openWindow(AddAttribute.class, true);
                refreshAttributeComboBoxContent();

            }
            changeableAttribute.setPromptText((changeableAttribute.getSelectionModel().getSelectedItem().getAttribute()));
            leader.setText(changeableAttribute.getSelectionModel().getSelectedItem().getLeader());
            leaderPhone.setText(changeableAttribute.getSelectionModel().getSelectedItem().getLeaderPhone());
            altLeader.setText(changeableAttribute.getSelectionModel().getSelectedItem().getAltLeader());
            changeableAttribute.setPromptText(changeableAttribute.getSelectionModel().getSelectedItem().getAttribute());
            altLeaderPhone.setText(changeableAttribute.getSelectionModel().getSelectedItem().getAltLeaderPhone());
            changeableAttribute.setConverter(new StringConverter<Attribute>() {
                @Override
                public String toString(Attribute attribute) {
                    return attribute == null ? null : attribute.getAttribute();
                }

                @Override
                public Attribute fromString(String string) {
                    return changeableAttribute.getItems().stream().filter(attribute ->
                            attribute.getAttribute().equals(string)).findFirst().orElse(null);
                }
            });
        });
        changeableStaff.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeableStaff.setPromptText(changeableStaff.getSelectionModel().getSelectedItem().getName());
            changeableStaff.setConverter(new StringConverter<Staff>() {
                @Override
                public String toString(Staff staff) {
                    return staff == null ? null : staff.getName();
                }

                @Override
                public Staff fromString(String string) {
                    return changeableStaff.getItems().stream().filter(staff ->
                            staff.getName().equals(string)).findFirst().orElse(null);
                }
            });
        });

//
    }
    private void refreshAttributeComboBoxContent() {

        changeableAttribute.getItems().clear();
        changeableAttribute.getItems().add(new Attribute(Constants.CREATE_PROMPT, null, null, null, null));
        changeableAttribute.getItems().addAll(cacheService.getAttributes());
    }


        public void close() {
            ((Stage)id.getScene().getWindow()).close();
        }
        public  void start(Teacher selectedOne){
            id.setText(selectedOne.getId());
            name.setText(selectedOne.getName());
            baptismalName.setText(selectedOne.getBaptismalName());
            birthday.setValue(selectedOne.getBirthday());
            if(selectedOne.getIsGregorianCalendar()!=null){
            isGregorianCalendar.setSelected(selectedOne.getIsGregorianCalendar());}
            baptismalDate.setValue(selectedOne.getBaptismalDate());
            confirmationDate.setValue(selectedOne.getConfirmationDate());
            marriageDate.setValue(selectedOne.getMarriageDate());
            deathDate.setValue(selectedOne.getDeathDate());
            if (selectedOne.getAttribute() != null) {
                leader.setText(selectedOne.getAttribute().getLeader());
                leaderPhone.setText(selectedOne.getAttribute().getLeaderPhone());
                altLeader.setText(selectedOne.getAttribute().getAltLeader());
                changeableAttribute.setPromptText(selectedOne.getAttribute().getAttribute());
                altLeaderPhone.setText(selectedOne.getAttribute().getAltLeaderPhone());
            }
            //System.out.println(selectedOne.getPhone());
            phone.setText(selectedOne.getPhone());
            altPhone.setText(selectedOne.getAltPhone());
            address.setText(selectedOne.getAddress());
            experience.setText(selectedOne.getExperience());
            talent.setText(selectedOne.getTalent());
            resource.setText(selectedOne.getResource());
            gender.getItems().addAll("男","女");
            gender.setPromptText(selectedOne.getGender());
            System.out.println(selectedOne.getGender());
            if (selectedOne.getStaff() != null) {
                changeableStaff.setPromptText(selectedOne.getStaff().getName());
            }
            if(cacheService!=null) {
                Attribute newOne=new Attribute();
                newOne.setAttribute(Constants.CREATE_PROMPT);
                changeableAttribute.getItems().add(newOne);
                changeableAttribute.getItems().addAll(cacheService.getAttributes());
                changeableStaff.getItems().addAll(cacheService.getStaffs());
            }
            changeableAttribute.setCellFactory(new Callback<ListView<Attribute>, ListCell<Attribute>>() {
                @Override
                public ListCell<Attribute> call(ListView<Attribute> param) {
                    return new ListCell<Attribute>(){
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
            changeableStaff.setCellFactory(new Callback<ListView<Staff>, ListCell<Staff>>() {
                @Override
                public ListCell<Staff> call(ListView<Staff> param) {
                    return new ListCell<Staff>(){
                        @Override
                        protected void updateItem(Staff item,boolean empty){
                            super.updateItem(item,empty);
                            if(item==null||empty){
                                setGraphic(null);
                            }else{
                                setText(item.getName());
                            }
                        }

                    };
                }
            });
            changeableStaff.setConverter(new StringConverter<Staff>() {
                @Override
                public String toString(Staff staff) {
                    return staff == null ? null : staff.getName();
                }

                @Override
                public Staff fromString(String string) {
                    return changeableStaff.getItems().stream().filter(staff ->
                            staff.getName().equals(string)).findFirst().orElse(null);
                }
            });
//            gender.setConverter(new StringConverter<String>() {
//                @Override
//                public String toString(String gender) {
//                    return gender==null?null : gender;
//                }
//
//                @Override
//                public String fromString(String string) {
//                    return gender.getItems().stream().filter(gender ->
//                            gender.equals(string)).findFirst().orElse(null);
//                }
//            });

        }

        public void change() {
            changeSet(name,true);
            changeSet(baptismalName,true);
            changeSet(phone,true);
            changeSet(altPhone,true);
            changeSet(id,true);
            changeAreaSet(address,true);
            changeAreaSet(experience,true);
            changeAreaSet(talent,true);
            changeAreaSet(resource,true);
            changeDateSet(birthday,true);
            changeDateSet(baptismalDate,true);
            changeDateSet(confirmationDate,true);
            changeDateSet(marriageDate,true);
            changeDateSet(deathDate,true);
            changeComboboxSet(gender,true);
            changeComboboxSet(changeableAttribute,true);
            changeComboboxSet(changeableStaff,true);
            buttonHide(change,true);
            buttonHide(delete,true);
            buttonHide(close,true);
            buttonShow(confirm,true);
            buttonShow(cancel,true);
            isGregorianCalendar.setDisable(false);

        }
    public void changeFalse() {
        changeSet(name,false);
        changeSet(baptismalName,false);
        changeSet(phone,false);
        changeSet(altPhone,false);
        changeSet(id,false);
        changeAreaSet(address,false);
        changeAreaSet(experience,false);
        changeAreaSet(talent,false);
        changeAreaSet(resource,false);
        changeDateSet(birthday,false);
        changeDateSet(baptismalDate,false);
        changeDateSet(confirmationDate,false);
        changeDateSet(marriageDate,false);
        changeDateSet(deathDate,false);
        changeComboboxSet(gender,false);
        changeComboboxSet(changeableAttribute,false);
        changeComboboxSet(changeableStaff,false);
        buttonHide(change,false);
        buttonHide(delete,false);
        buttonHide(close,false);
        buttonShow(confirm,false);
        buttonShow(cancel,false);
    }

        public void delete() {
        //确认窗口，确认红色
            logger.logInfo(getClass().toString(), "删除教师档案，编号：{}，姓名：{}", id.getText(), name.getText());
            teacherRepository.delete(selectedOne);
            close();
        }

        public void confirm() {
            if(!id.getText().equals(selectedOne.getId())
                    &&teacherRepository.existsById(id.getText())
            ) {
                System.out.println("test1");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("更新教师档案错误");
                alert.setHeaderText("错误原因：使用已存在的教师编号");
                alert.setContentText("解决方法：请输入不同的教师编号");
                alert.showAndWait();
            }
            else{
                if(!id.getText().equals(selectedOne.getId())) {
                    teacherRepository.delete(selectedOne);
                }
                Teacher teacher = new Teacher();
                teacher.setId(id.getText());
                teacher.setName(name.getText());
                teacher.setBaptismalName(baptismalName.getText());
                teacher.setBirthday(birthday.getValue());
                teacher.setIsGregorianCalendar(isGregorianCalendar.isSelected());
                teacher.setBaptismalDate(baptismalDate.getValue());
                teacher.setConfirmationDate(confirmationDate.getValue());
                teacher.setMarriageDate(marriageDate.getValue());
                teacher.setDeathDate(deathDate.getValue());
                teacher.setPhone(phone.getText());
                teacher.setAltPhone(altPhone.getText());
                teacher.setAddress(address.getText());
                teacher.setExperience(experience.getText());
                teacher.setResource(resource.getText());
                teacher.setTalent(talent.getText());
                teacher.setAttribute(changeableAttribute.getSelectionModel().getSelectedItem());
                teacher.setStaff(changeableStaff.getSelectionModel().getSelectedItem());
                teacher.setGender(gender.getPromptText());
                teacherRepository.save(teacher);
                changeFalse();
                isGregorianCalendar.setDisable(true);
                }
        }
        public void cancel() {
        //回到上级不保存
            changeFalse();
            start(selectedOne);
        }
        private void changeSet(TextField textField, boolean status) {

            textField.setDisable(!status);
            textField.setEditable(status);
        }
        private void changeAreaSet(TextArea textArea, boolean status) {
            textArea.setDisable(!status);
            textArea.setEditable(status);
        }
        private void changeDateSet(DatePicker datePicker, boolean status) {
            datePicker.setDisable(!status);
            datePicker.setEditable(status);
        }
        private void changeComboboxSet(ComboBox comboBox, boolean status) {
            comboBox.setDisable(!status);
            //comboBox.setEditable(status);
        }
        private void buttonHide(Button button, boolean status) {
            button.setDisable(status);
            button.setVisible(!status);
        }
        private void buttonShow(Button button, boolean status){
            button.setDisable(!status);
            button.setVisible(status);

        }
        private void initSetRightSide(){
            visit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            visit.setRowFactory( tv -> {
                TableRow<Visit> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                        dataPassService.setValue(new Pair<>(name.getText(), row.getItem()));
                        KRSTManagementSoftware.openWindow(VisitInfoPage.class);
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


        }});}
    public void addRelationship() {
//        KRSTManagementSoftware.openWindow(AddRelation.class);
        Relation relation = (Relation) dataPassService.getValue();
        if (relation != null) {
            originalStaff.getRelationships().add(relation);
            staffRepository.save(originalStaff);
            relationship.getItems().add(relation);
        }
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

}
