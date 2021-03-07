package org.krst.app.controllers.person;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Person;
import org.krst.app.domains.Relation;
import org.krst.app.repositories.PersonRepository;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.views.share.AddRelationship;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Person, the Person model that need to be displayed
 * Out : Boolean, false delete operation
 *       OR
 *       null, update operation or nothing changed
 */
@FXMLController
public class PersonInfoPageController implements InfoPageControllerTemplate {
    @FXML private SplitPane splitPane;
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private TextField age;
    @FXML private TextField baptismalName;
    @FXML private ComboBox<String> gender;
    @FXML private DatePicker birthday;
    @FXML private CheckBox isGregorianCalendar;
    @FXML private TextField phone;
    @FXML private TextField altPhone;
    @FXML private TextArea address;
    @FXML private TextArea job;

    @FXML private Button change, accept, delete, cancel, close;

    @FXML private TableView<Relation> relationship;
    @FXML private TableColumn<Relation, String> relationship_relation;
    @FXML private TableColumn<Relation, String> relationship_name;
    @FXML private TableColumn<Relation, Relation.Type> relationship_type;
    @FXML private TableColumn<Relation, String> relationship_id;

    @Autowired private DataPassService dataPassService;
    @Autowired private PersonRepository personRepository;
    @Autowired private RelationshipService relationshipService;
    @Autowired private Logger logger;

    private Person originalPerson = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        originalPerson = (Person) dataPassService.getValue();
        if (originalPerson == null) {
            close();
            return;
        }

        initDefaultComponents();
        refreshBasicInfo(originalPerson);
        refreshOtherInfo(originalPerson);
    }

    private void initDefaultComponents() {
        splitPane.getDividers().get(0).positionProperty()
                .addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(0.5));
        setEditableMode(false);
        setButtonMode(false);

        gender.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                gender.getEditor().setText(newValue);
        });

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

    private void refreshBasicInfo(Person person) {
        id.setText(person.getId());
        name.setText(person.getName());
        baptismalName.setText(person.getBaptismalName());
        gender.getSelectionModel().select(person.getGender() == null ? null : person.getGender());
        birthday.setValue(person.getBirthday());
        isGregorianCalendar.setSelected(person.getIsGregorianCalendar() != null && person.getIsGregorianCalendar());
        if (person.getBirthday() != null)
            age.setText(person.getBirthday().until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears() + " 岁");
        phone.setText(person.getPhone());
        altPhone.setText(person.getAltPhone());
        address.setText(person.getAddress());
        job.setText(person.getJob());
    }

    private void refreshOtherInfo(Person person) {
        if (person.getRelationships() != null)
            relationship.getItems().setAll(person.getRelationships());
    }

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept() {
        if (isDeleteOperation) {
            personRepository.delete(originalPerson);
            dataPassService.setValue(false);
            logger.logInfo(getClass().toString(), "删除普通人档案，编号：{}，姓名：{}", id.getText(), name.getText());
            close();
            return;
        }

        if (id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改普通人档案失败");
            alert.setHeaderText("失败原因：未填入普通人编号");
            alert.setContentText("解决方法：请输入普通人编号");
            alert.show();
            return;
        }

        if (id.getText().equals(originalPerson.getId())) {
            if (!name.getText().equals(originalPerson.getName()))
                relationshipService.updateIdAndName(originalPerson.getRelationships(), originalPerson.getId(), id.getText(), name.getText());

            originalPerson = personRepository.save(loadValuesIntoPersonModel());
            logger.logInfo(this.getClass().toString(), "更改普通人档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (personRepository.existsById(id.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改普通人档案失败");
            alert.setHeaderText("失败原因：该普通人编号已存在：" + id.getText());
            alert.setContentText("解决方法：请更改普通人编号");
            alert.show();
        } else {
            relationshipService.updateIdAndName(originalPerson.getRelationships(), originalPerson.getId(), id.getText(), name.getText());

            personRepository.delete(originalPerson);
            originalPerson = personRepository.save(loadValuesIntoPersonModel());
            logger.logInfo(this.getClass().toString(), "更改普通人档案：编号-{}，姓名-{}", id.getText(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Person loadValuesIntoPersonModel() {
        return new Person(id.getText(), name.getText(), baptismalName.getText(), gender.getValue(), birthday.getValue(),
                isGregorianCalendar.isSelected(), phone.getText(), altPhone.getText(), address.getText(), job.getText(),
                originalPerson.getRelationships());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalPerson);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, baptismalName, phone, altPhone, address, job);
        setDatePickerEditableMode(state, birthday);
        setComboBoxEditableMode(state, gender);
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

    public void addRelationship() {
        dataPassService.setValue(new Pair<>(Relation.Type.PERSON, new Pair<>(originalPerson.getId(), originalPerson.getName())));
        KRSTManagementSoftware.openWindow(AddRelationship.class);
        Person tempPerson = (Person) dataPassService.getValue();
        if (tempPerson != null) {
            originalPerson = tempPerson;
            relationship.getItems().setAll(originalPerson.getRelationships());
        }
    }
}
