package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Attribute;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Attribute, data model needs to be displayed
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, Attribute>
 *         Boolean, true  update operation
 *                  false delete operation
 *         Attribute, updated Attribute model
 */
@FXMLController
public class AttributeInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField attribute;
    @FXML private TextField leader;
    @FXML private TextField leaderPhone;
    @FXML private TextField altLeader;
    @FXML private TextField altLeaderPhone;

    @FXML private Button change,  accept, delete, cancel, close;

    @Autowired private DataPassService dataPassService;
    @Autowired private AttributeRepository attributeRepository;
    @Autowired private CacheService cacheService;

    private Attribute originalAttribute = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        originalAttribute = (Attribute) dataPassService.getValue();
        if (originalAttribute == null) {
            close();
            return;
        }

        refreshBasicInfo(originalAttribute);
        setEditableMode(false);
    }

    private void refreshBasicInfo(Attribute attr) {
        attribute.setText(attr.getAttribute());
        leader.setText(attr.getLeader());
        leaderPhone.setText(attr.getLeaderPhone());
        altLeader.setText(attr.getAltLeader());
        altLeaderPhone.setText(attr.getAltLeaderPhone());
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, attribute, leader, leaderPhone, altLeader, altLeaderPhone);
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

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept() {
        if (isDeleteOperation) {
            attributeRepository.delete(originalAttribute);
            attributeRepository.updateAttributeInStudent(originalAttribute.getAttribute(), null);
            attributeRepository.updateAttributeInTeacher(originalAttribute.getAttribute(), null);

            dataPassService.setValue(new Pair<>(false, null));
            close();
            return;
        }

        if (attribute.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建所属堂区失败");
            alert.setHeaderText("失败原因：所属堂区名称缺失");
            alert.setContentText("解决方法：请添加所属堂区名称");
            alert.showAndWait();
            return;
        }

        if (originalAttribute.getAttribute().equals(attribute.getText())) {
            originalAttribute = attributeRepository.save(loadValuesIntoAttributeModel());

            refreshBasicInfo(originalAttribute);
            cacheService.refreshAttributeCache();
            dataPassService.setValue(new Pair<>(true, originalAttribute));
            setEditableMode(false);
            setButtonMode(false);
            return;
        }

        if (attributeRepository.existsById(attribute.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建所属堂区失败");
            alert.setHeaderText("失败原因：所属堂区已存在");
            alert.setContentText("解决方法：更换所属堂区");
            alert.showAndWait();
        } else {
            attributeRepository.delete(originalAttribute);
            attributeRepository.updateAttributeInStudent(originalAttribute.getAttribute(), attribute.getText());
            attributeRepository.updateAttributeInTeacher(originalAttribute.getAttribute(), attribute.getText());

            originalAttribute = attributeRepository.save(loadValuesIntoAttributeModel());
            refreshBasicInfo(originalAttribute);
            cacheService.refreshAttributeCache();
            dataPassService.setValue(new Pair<>(true, originalAttribute));
            setEditableMode(false);
            setButtonMode(false);
        }
    }

    private Attribute loadValuesIntoAttributeModel() {
        return new Attribute(
                attribute.getText(),
                leader.getText(),
                leaderPhone.getText(),
                altLeader.getText(),
                altLeaderPhone.getText());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalAttribute);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)attribute.getScene().getWindow()).close();
    }

}
