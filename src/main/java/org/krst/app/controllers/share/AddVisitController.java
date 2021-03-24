package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.krst.app.controllers.ControllerTemplate;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Visit;
import org.krst.app.repositories.VisitRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/*
 * In  : None
 * Out : Visit, new created visit model
 */
@FXMLController
public class AddVisitController extends ControllerTemplate {
    @FXML private DatePicker date;
    @FXML private ComboBox<Staff> visitor;
    @FXML private ListView<Staff> visitors;
    @FXML private TextArea content;
    @FXML private TextArea summary;
    @FXML private TextArea comment;

    @Autowired private VisitRepository visitRepository;
    @Autowired private DataPassService dataPassService;
    @Autowired private CacheService cacheService;

    @FXML public void initialize() {
        visitor.getItems().addAll(cacheService.getStaffs());
        setUpSelectorAndList(visitor, visitors);
    }

    public void approve() {
        Visit visit = new Visit(
                null,
                date.getValue(),
                new ArrayList<>(visitors.getItems()),
                content.getText(),
                summary.getText(),
                comment.getText());
        if (!visit.equals(new Visit()))
            dataPassService.setValue(visitRepository.save(visit));
        close();
    }

    public void close() {
        ((Stage)comment.getScene().getWindow()).close();
    }

}
