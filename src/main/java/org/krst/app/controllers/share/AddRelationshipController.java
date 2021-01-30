package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Relation;
import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.services.DataPassService;
import org.krst.app.services.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

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
        setEditableMode(false);
        setButtonMode(false);

        type.getItems().addAll(Relation.Type.STUDENT, Relation.Type.TEACHER, Relation.Type.STAFF, Relation.Type.PERSON);
        type.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            if (oldValue == null) {
                setEditableMode(true);
            }

            if (selectedType != newValue) {
                relative.getSelectionModel().clearSelection();
                relative.getItems().clear();
                relative.getItems().addAll(relationshipService.getAllPossibleRelatives(newValue));
                selectedType = newValue;
            }
        });

        relative.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            BName1.setText(newValue.getName());
            BName2.setText(newValue.getName());
        });

        relative.setCellFactory(new Callback<ListView<InformationOperations>, ListCell<InformationOperations>>() {
            @Override
            public ListCell<InformationOperations> call(ListView<InformationOperations> param) {
                return new ListCell<InformationOperations>() {
                    @Override
                    protected void updateItem(InformationOperations item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            relativeModel = item;
                            this.setText(item.getNameAndId());
                        }
                    }
                };
            }
        });
    }

    private void initData(Pair<Relation.Type, Pair<String, String>> data) {
        _type = data.getFirst();
        id.setText(data.getSecond().getFirst());
        name.setText(data.getSecond().getSecond());
        AName1.setText(data.getSecond().getSecond());
        AName2.setText(data.getSecond().getSecond());
        BName1.setText("?");
        BName2.setText("?");
    }

    public void accept() {
        relationshipService.addRelationship(_type, id.getText(), relativeModel.getId(), relativeModel.getName(), relationshipBtoA.getText(), type.getValue());
        relationshipService.addRelationship(type.getValue(), relativeModel.getId(), id.getId(), name.getText(), relationshipAtoB.getText(), _type);
        relationshipService.getDataModel(_type, id.getText()).ifPresent(data -> dataPassService.setValue(data));
        close();
    }

    public void close() {
        ((Stage)id.getScene().getWindow()).close();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(false, id, name, relationshipAtoB, relationshipBtoA);
        setComboBoxEditableMode(false, relative);
    }

    @Override
    public void setButtonMode(boolean state) {
        accept.setDisable(!state);
    }
}
