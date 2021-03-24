package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.krst.app.controllers.InfoPageControllerTemplate;
import org.krst.app.domains.PatronSaintDate;
import org.krst.app.repositories.PatronSaintDateRepository;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In  : PatronSaintDate, data model needs to be displayed
 * Out : null, no changes are made
 *       OR
 *       Pair<Boolean, PatronSaintDate>
 *         Boolean, true  update operation
 *                  false delete operation
 *         PatronSaintDate, updated PatronSaintDate model
 */
@FXMLController
public class PatronSaintDateInfoPageController implements InfoPageControllerTemplate {
    @FXML private TextField id;
    @FXML private TextField name;
    @FXML private DatePicker date;
    @FXML private Button change, accept, delete, cancel, close;

    @Autowired private PatronSaintDateRepository patronSaintDateRepository;
    @Autowired private DataPassService dataPassService;

    private PatronSaintDate originalPatronSaintDate = null;
    private boolean isDeleteOperation = false;

    @FXML public void initialize() {
        originalPatronSaintDate = (PatronSaintDate) dataPassService.getValue();
        refreshBasicInfo(originalPatronSaintDate);
    }

    private void refreshBasicInfo(PatronSaintDate patronSaintDate) {
        id.setText(String.valueOf(patronSaintDate.getId()));
        name.setText(patronSaintDate.getName());
        date.setValue(patronSaintDate.getDate());
    }

    public void change(){
        isDeleteOperation = false;
        setEditableMode(true);
        setButtonMode(true);
    }

    public void accept(){
        if (isDeleteOperation) {
            patronSaintDateRepository.delete(originalPatronSaintDate);
            dataPassService.setValue(new Pair<>(false, null));
            close();
            return;
        }

        if (name.getText().isEmpty() || date.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("更改圣人纪念日失败");
            alert.setHeaderText("失败原因：未填入圣人姓名或纪念日期");
            alert.setContentText("解决方法：请输入圣人姓名或纪念日期");
            alert.show();
            return;
        }

        originalPatronSaintDate = patronSaintDateRepository.save(new PatronSaintDate(originalPatronSaintDate.getId(), name.getText(), date.getValue()));
        refreshBasicInfo(originalPatronSaintDate);
        setEditableMode(false);
        setButtonMode(false);
    }

    public void delete(){
        isDeleteOperation = true;
        setButtonMode(true);
    }

    public void cancel(){
        if (!isDeleteOperation) {
            refreshBasicInfo(originalPatronSaintDate);
            setEditableMode(false);
        }
        setButtonMode(false);
    }

    public void close(){
        ((Stage)id.getScene().getWindow()).close();
    }


    @Override
    public void setEditableMode(boolean state) {
        setTextEditableMode(state, name);
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
}
