package org.krst.app.controllers.staff;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Evaluation;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Pair<String, Evaluation>
 *       String, the name of Staff who has this evaluation record
 *       Evaluation, Evuation data model
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, Evaluation>
 *         Boolean, true  update operation
 *                  false delete operation
 *         Evaluation, updated Evaluation model
 */
@FXMLController
public class EvaluationInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField name;
    @FXML private TextField year;
    @FXML private TextField title;
    @FXML private TextArea responsibility;
    @FXML private TextArea comment;

    @FXML private Button change,  accept, delete, cancel, close;

    @Autowired private DataPassService dataPassService;

    private Evaluation originalEvaluation = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        Pair<String, Evaluation> data = (Pair<String, Evaluation>) dataPassService.getValue();
        if (data.getKey() == null || data.getValue() == null) {
            close();
            return;
        }
        name.setText(data.getKey());
        originalEvaluation = data.getValue();
        refreshBasicInfo(originalEvaluation);
        setEditableMode(false);
    }

    private void refreshBasicInfo(Evaluation evaluation) {
        year.setText(evaluation.getYear());
        title.setText(evaluation.getTitle());
        responsibility.setText(evaluation.getResponsibility());
        comment.setText(evaluation.getComment());
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, year, title, responsibility, comment);
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
            dataPassService.setValue(new Pair<>(false, null));
            close();
        } else {
            loadValuesIntoEvaluationModel();
            setEditableMode(false);
            setButtonMode(false);
            dataPassService.setValue(new Pair<>(true, originalEvaluation));
        }
    }

    private void loadValuesIntoEvaluationModel() {
        originalEvaluation.setYear(year.getText());
        originalEvaluation.setTitle(title.getText());
        originalEvaluation.setResponsibility(responsibility.getText());
        originalEvaluation.setComment(comment.getText());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalEvaluation);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }


}
