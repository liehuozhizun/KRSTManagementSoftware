package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Relation;
import org.krst.app.domains.InformationOperations;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In    : Pair<Relation.Type, Pair<String, String>>
 *              Relation.Type, type of the current person
 *              Pair<String, String>, pair of the id and name of current person
 * Out   : null, nothing added
 *         <? extends InformationOperations>, persistent data model of current person
 */
@FXMLController
public class AddRelationshipController implements InfoPageControllerTemplate {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private ComboBox<Relation.Type> type;
    @FXML private ComboBox<InformationOperations> relative;
    @FXML private TextField relationshipAtoB;
    @FXML private TextField relationshipBtoA;
    @FXML private Button accept;
    @FXML private Text AName1, AName2, BName1, BName2;

    @Autowired private DataPassService dataPassService;
    @Autowired private RelationshipService relationshipService;

    private Relation.Type _type;
    private Relation.Type selectedType;
    private InformationOperations relativeModel;

    @FXML public void initialize() {
        Pair<Relation.Type, Pair<String, String>> data = (Pair<Relation.Type, Pair<String, String>>) dataPassService.getValue();

        if (data == null) {
            close();
        } else {
            initData(data);
        }

        initComponent();
    }

    private void initComponent() {
        selectedType = null;
        setEditableMode(false);
        setButtonMode(false);

        type.getItems().addAll(Relation.Type.STUDENT, Relation.Type.TEACHER, Relation.Type.STAFF, Relation.Type.PERSON);
        type.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            if (oldValue == null) {
                setEditableMode(true);
                setButtonMode(true);
            }

            if (selectedType != newValue) {
                relative.getItems().clear();
                relative.getItems().addAll(relationshipService.getAllPossibleRelatives(newValue));
                selectedType = newValue;
            }
        });

        relative.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            BName1.setText(newValue.getName());
            BName2.setText(newValue.getName());
            relativeModel = newValue;
        });

        relative.setConverter(new StringConverter<InformationOperations>() {
            @Override
            public String toString(InformationOperations object) {
                return object == null ? null : object.getNameAndId();
            }

            @Override
            public InformationOperations fromString(String string) {
                InformationOperations temp = relative.getItems().stream()
                        .filter(x -> x.getName().equals(string) || x.getId().equals(string) || x.getNameAndId().equals(string))
                        .findAny().orElse(null);
                if (temp != null) {
                    BName1.setText(temp.getName());
                    BName2.setText(temp.getName());
                } else {
                    BName1.setText("?");
                    BName2.setText("?");
                }

                return temp;
            }
        });
    }

    private void initData(Pair<Relation.Type, Pair<String, String>> data) {
        _type = data.getKey();
        id.setText(data.getValue().getKey());
        name.setText(data.getValue().getValue());
        AName1.setText(data.getValue().getValue());
        AName2.setText(data.getValue().getValue());
        BName1.setText("B 姓名");
        BName2.setText("B 姓名");
    }

    public void accept() {
        relationshipService.addRelationship(_type, id.getText(), relativeModel.getId(), relativeModel.getName(), relationshipBtoA.getText(), type.getValue());
        relationshipService.addRelationship(type.getValue(), relativeModel.getId(), id.getText(), name.getText(), relationshipAtoB.getText(), _type);
        relationshipService.getDataModel(_type, id.getText()).ifPresent(data -> dataPassService.setValue(data));
        close();
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, id, name, relationshipAtoB, relationshipBtoA);
        setComboBoxEditableMode(state, relative);
    }

    @Override
    public void setButtonMode(boolean state) {
        accept.setDisable(!state);
    }
}
