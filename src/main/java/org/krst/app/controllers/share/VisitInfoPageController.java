package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.configurations.Logger;
import org.krst.app.controllers.ControllerTemplate;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Visit;
import org.krst.app.domains.InformationOperations;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
 * In  : Pair<InformationOperations, Visit>
 *       InformationOperations, Student/Teacher/Staff/Person implement this interface
 *       Visit, visit data model
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, Visit>
 *         Boolean, true  update operation
 *                  false delete operation
 *         Visit, updated Visit model
 */
@FXMLController
public class VisitInfoPageController extends ControllerTemplate implements InfoPageControllerTemplate {
    @FXML private TextField name;
    @FXML private ListView<Staff> visitors;
    @FXML private ComboBox<Staff> visitor;
    @FXML private DatePicker date;
    @FXML private TextArea content;
    @FXML private TextArea summary;
    @FXML private TextArea comment;

    @FXML private Button change;
    @FXML private Button accept;
    @FXML private Button delete;
    @FXML private Button cancel;
    @FXML private Button close;

    @Autowired private VisitRepository visitRepository;
    @Autowired private DataPassService dataPassService;
    @Autowired private CacheService cacheService;
    @Autowired private Logger logger;

    private Visit originalVisit = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        Pair<InformationOperations, Visit> data = (Pair<InformationOperations, Visit>)dataPassService.getValue();
        if (data.getKey() == null || data.getValue() == null) {
            close();
            return;
        }

        setUpSelectorAndList(visitor, visitors);
        name.setText(data.getKey().getName());
        originalVisit = data.getValue();
        refreshBasicInfo(originalVisit);
        setEditableMode(false);
    }

    private void refreshBasicInfo(Visit visit) {
        date.setValue(visit.getDate());
        content.setText(visit.getContent());
        summary.setText(visit.getSummary());
        comment.setText(visit.getComment());

        visitors.getItems().setAll(originalVisit.getVisitors());
        List<Staff> list = cacheService.getStaffs();
        list.removeAll(originalVisit.getVisitors());
        visitor.getItems().setAll(list);
        visitor.getSelectionModel().clearSelection();
    }

    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, content, summary, comment);
        visitor.setVisible(state);
        visitors.setMouseTransparent(!state);
        setDatePickerEditableMode(state, date);
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

    /*
     * ATTENTION:
     * The caller window who opened this window is RESPONSIBLE to delete this visit model
     * by following steps:
     *  1. @Autowired VisitRepository
     *  2. visitRepository.delete(*);
     *  3. logger.logInfo(this.getClass().toString(), "删除探访记录：探访编号-{}，姓名-{}", id, name);
     */
    public void accept() {
        if (isDeleteOperation) {
            dataPassService.setValue(new Pair<>(false, null));
            close();
        } else {
            loadValuesIntoVisitModel();
            originalVisit = visitRepository.save(originalVisit);
            logger.logInfo(this.getClass().toString(), "更改探访记录：探访记录编号-{}，姓名-{}", originalVisit.getId().toString(), name.getText());
            setEditableMode(false);
            setButtonMode(false);
            dataPassService.setValue(new Pair<>(true, originalVisit));
        }
    }

    private void loadValuesIntoVisitModel() {
        originalVisit.setDate(date.getValue());
        originalVisit.setVisitors(visitors.getItems());
        originalVisit.setContent(content.getText());
        originalVisit.setSummary(summary.getText());
        originalVisit.setComment(comment.getText());
    }

    public void delete() {
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel() {
        if (!isDeleteOperation) {
            refreshBasicInfo(originalVisit);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close() {
        ((Stage)name.getScene().getWindow()).close();
    }

}
