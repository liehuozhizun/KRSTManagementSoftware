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

    @FXML private Button change;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button close;

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
        attributeRepository.delete(originalAttribute);
        if (isDeleteOperation) {
            dataPassService.setValue(new Pair<>(false, null));
            close();
        } else {
            if (attributeRepository.existsById(attribute.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("新建所属堂区失败");
                alert.setHeaderText("失败原因：所属堂区已存在");
                alert.setContentText("解决方法：更换所属堂区");
                alert.showAndWait();
                attributeRepository.save(originalAttribute);
            } else {
                originalAttribute = attributeRepository.save(loadValuesIntoAttributeModel());
                setEditableMode(false);
                setButtonMode(false);
                refreshBasicInfo(originalAttribute);
                cacheService.refreshAttributeCache();
                dataPassService.setValue(new Pair<>(true, originalAttribute));
            }
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
