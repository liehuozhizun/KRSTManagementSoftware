package org.krst.app.controllers.teacher;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Teacher;
import org.krst.app.configurations.Logger;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.krst.app.utils.Constants;
import org.krst.app.views.share.AddAttribute;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class TeacherInfoPageController implements Initializable {

//    @FXML
//    private TextField id;
    @FXML
    public TextField name;
    @FXML
    private TextField baptismalName;
    @FXML
    private RadioButton gender_male;
    @FXML
    private DatePicker birthday;
    @FXML
    private CheckBox isGregorianCalendar;
    @FXML
    private DatePicker baptismalDate;
    @FXML
    private DatePicker confirmationDate;
    @FXML
    private DatePicker marriageDate;
    @FXML
    private DatePicker deathDate;
    @FXML
    private TextField attribute;
    @FXML
    private TextField leader;
    @FXML
    private TextField leaderPhone;
    @FXML
    private TextField altLeader;
    @FXML
    private TextField altLeaderPhone;
    @FXML
    private TextField phone;
    @FXML
    private TextField altPhone;
    @FXML
    private TextArea address;
    @FXML
    private TextArea experience;
    @FXML
    private TextArea talent;
    @FXML
    private TextArea resource;
    @FXML
    private TextField staff;
    @FXML
    private ComboBox<Teacher> teacherSelectors;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Logger logger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        teacherSelectors.getItems().addAll(teacherRepository.findAll());

        teacherSelectors.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
            @Override
            public ListCell<Teacher> call(ListView<Teacher> param) {
                return new ListCell<Teacher>() {
                    @Override
                    protected void updateItem(Teacher item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId());
                        }
                    }
                };
            }
        });
        teacherSelectors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           Teacher selectedOne=teacherSelectors.getSelectionModel().getSelectedItem();
           System.out.println(selectedOne.getId());
            //id.setId(selectedOne.getId());
            //id.setText(selectedOne.getId());
           // System.out.println(id);
            name.setText(teacherSelectors.getSelectionModel().getSelectedItem().getName());
            System.out.println(teacherSelectors.getSelectionModel().getSelectedItem().getName());
            System.out.println(name);

            baptismalName.setText(selectedOne.getBaptismalName());
            gender_male.setSelected(selectedOne.getGender().equals("男"));
            birthday.setValue(selectedOne.getBirthday());
            isGregorianCalendar.setSelected(selectedOne.getIsGregorianCalendar());
            baptismalDate.setValue(selectedOne.getBaptismalDate());
            confirmationDate.setValue(selectedOne.getConfirmationDate());
            marriageDate.setValue(selectedOne.getMarriageDate());
            deathDate.setValue(selectedOne.getDeathDate());
            attribute.setText(selectedOne.getAttribute().getAttribute());
            leader.setText(selectedOne.getAttribute().getLeader());
            leaderPhone.setText(selectedOne.getAttribute().getLeaderPhone());
            altLeader.setText(selectedOne.getAttribute().getAltLeader());
            altLeaderPhone.setText(selectedOne.getAttribute().getAltLeaderPhone());
            phone.setText(selectedOne.getPhone());
            altPhone.setText(selectedOne.getAltPhone());
            address.setText(selectedOne.getAddress());
            experience.setText(selectedOne.getExperience());
            talent.setText(selectedOne.getTalent());
            resource.setText(selectedOne.getResource());
            staff.setText(selectedOne.getStaff().getName());
            System.out.println("reach end");
        });
//
    }
    public void close() {
        KRSTManagementSoftware.closeWindow();
    }
}
