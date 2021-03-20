package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Relation;
import org.krst.app.models.RelationPassModel;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : RelationPassModel, the model contains the relationship information
 * Out : null, nothing changed
 *       Pair<Boolean, String>, result of changed relationship
 *          <false, null>, delete this relationship
 *          <true, String>, updated the relationship
 */
@FXMLController
public class RelationshipInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField AType;
    @FXML private TextField AId;
    @FXML private TextField AName;
    @FXML private TextField BType;
    @FXML private TextField BId;
    @FXML private TextField BName;
    @FXML private Text AName1;
    @FXML private Text AName2;
    @FXML private Text BName1;
    @FXML private Text BName2;
    @FXML private TextField relationshipAtoB;
    @FXML private TextField relationshipBtoA;
    @FXML private Text deletePromptText;

    @FXML private Button change,  accept, delete, cancel, close;

    @Autowired private DataPassService dataPassService;
    @Autowired private RelationshipService relationshipService;
    @Autowired private Logger logger;

    private boolean isDeleteOperation = false;
    private Relation.Type _AType;
    private Relation.Type _BType;
    private String _relationshipAtoB;
    private String _relationshipBtoA;

    @FXML public void initialize() {
        RelationPassModel data = (RelationPassModel) dataPassService.getValue();
        if (data == null) {
            close();
        } else initComponent(data);
    }

    private void initComponent(RelationPassModel data) {
        setTextEditableMode(false, AType, AId, AName, BType, BId, BName);
        setEditableMode(false);
        setButtonMode(false);

        _AType = data.getAType();
        AType.setText(data.getAType().toString());
        AId.setText(data.getAId());
        AName.setText(data.getAName());
        AName1.setText(data.getAName());
        AName2.setText(data.getAName());
        _BType = data.getBType();
        BType.setText(data.getBType().toString());
        BId.setText(data.getBId());
        BName.setText(data.getBName());
        BName1.setText(data.getBName());
        BName2.setText(data.getBName());

        _relationshipAtoB = relationshipService.getRelationship(data.getBType(), data.getBId(), data.getAId(), data.getAType());
        _relationshipBtoA = data.getRelationshipBtoA();
        relationshipAtoB.setText(_relationshipAtoB);
        relationshipBtoA.setText(data.getRelationshipBtoA());
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, relationshipAtoB, relationshipBtoA);
    }

    @Override
    public void setButtonMode(boolean state) {
        change.setVisible(!state);
        accept.setVisible(state);
        accept.setStyle(isDeleteOperation ? "-fx-text-fill: red" : "-fx-text-fill: black");
        delete.setVisible(!state);
        deletePromptText.setVisible(isDeleteOperation && state);
        cancel.setVisible(state);
        close.setVisible(!state);
    }

    public void change() {
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    /*
     * ATTENTION: The caller window need to update its own data model
     */
    public void accept() {
        if (isDeleteOperation) {
            relationshipService.removeRelationship(_AType, AId.getText(), BId.getText(), _BType);
            relationshipService.removeRelationship(_BType, BId.getText(), AId.getText(), _AType);
            logger.logInfo(this.getClass().toString(), "删除亲属关系：A类型-{}，A编号-{}，A姓名-{}，B2A原关系-{}; B类型-{}，B编号-{}，B姓名-{}，A2B原关系-{}",
                    AType.getText(), AId.getText(), AName.getText(), _relationshipBtoA, BType.getText(), BId.getText(), BName.getText(), _relationshipAtoB);
            dataPassService.setValue(new Pair<>(false, null));
            close();
        } else if (!_relationshipAtoB.equals(relationshipAtoB.getText()) || !_relationshipBtoA.equals(relationshipBtoA.getText())) {
            relationshipService.updateRelationship(_AType, AId.getText(), _BType, BId.getText(), relationshipAtoB.getText(), relationshipBtoA.getText());
            logger.logInfo(this.getClass().toString(),"更改亲属关系：A类型-{}，A编号-{}，A姓名-{}，B2A原关系-{}，B2A现关系-{}; B类型-{}，B编号-{}，B姓名-{}，A2B原关系-{}，A2B现关系-{}",
                    AType.getText(), AId.getText(), AName.getText(), _relationshipBtoA, relationshipBtoA.getText(), BType.getText(), BId.getText(), BName.getText(), _relationshipAtoB, relationshipAtoB.getText());
            setEditableMode(false);
            setButtonMode(false);
            dataPassService.setValue(new Pair<>(true, relationshipBtoA.getText()));
            _relationshipAtoB = relationshipAtoB.getText();
            _relationshipBtoA = relationshipBtoA.getText();
        }
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            relationshipAtoB.setText(_relationshipAtoB);
            relationshipBtoA.setText(_relationshipBtoA);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)AType.getScene().getWindow()).close();
    }
}
