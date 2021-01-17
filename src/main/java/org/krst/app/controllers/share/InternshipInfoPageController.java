package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Internship;
import org.krst.app.domains.operations.InformationOperations;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : Pair<InformationOperations, Internship>
 *       InformationOperations, Student/Teacher/Staff/Person implement this interface
 *       Internship, internship data model
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, Internship>
 *         Boolean, true  update operation
 *                  false delete operation
 *         Internship, updated Internship model
 */
@FXMLController
public class InternshipInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField name;
    @FXML private TextField id;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextArea purpose;
    @FXML private TextArea summary;
    @FXML private TextArea comment;

    @FXML private Button change;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button close;

    @Autowired private DataPassService dataPassService;

    private Internship originalInternship;
    private boolean isDeleteOperation;

    @FXML public void initialize() {
        Pair<InformationOperations, Internship> data = (Pair<InformationOperations, Internship>) dataPassService.getValue();
        if (data == null || data.getKey() == null || data.getValue() == null) {
            close();
            return;
        }

        originalInternship = data.getValue();
        refreshConstantInfo(data.getKey());
        refreshBasicInfo(data.getValue());
        setEditableMode(false);
    }

    private void refreshConstantInfo(InformationOperations informationOperations) {
        name.setText(informationOperations.getName());
        id.setText(informationOperations.getId());
    }

    private void refreshBasicInfo(Internship internship) {
        startDate.setValue(internship.getStartDate());
        endDate.setValue(internship.getEndDate());
        purpose.setText(internship.getPurpose());
        summary.setText(internship.getSummary());
        comment.setText(internship.getComment());
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, purpose, summary, comment);
        setDatePickerEditableMode(state, startDate, endDate);
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
            loadValuesIntoInternshipModel();
            setEditableMode(false);
            setButtonMode(false);
            dataPassService.setValue(new Pair<>(true, originalInternship));
        }
    }

    private void loadValuesIntoInternshipModel() {
        originalInternship.setStartDate(startDate.getValue());
        originalInternship.setEndDate(endDate.getValue());
        originalInternship.setPurpose(purpose.getText());
        originalInternship.setSummary(summary.getText());
        originalInternship.setComment(comment.getText());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalInternship);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }

}
