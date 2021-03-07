package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.views.share.AddAttribute;
import org.krst.app.views.share.AttributeInfoPage;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : none
 * OUT : none
 */
@FXMLController
public class AttributeControlPanelController {
    @FXML private Text number;
    @FXML private TableView<Attribute> attributes;
    @FXML private TableColumn<Attribute, String> attributes_attribute;
    @FXML private TableColumn<Attribute, String> attributes_leader;
    @FXML private TableColumn<Attribute, String> attributes_leaderPhone;
    @FXML private TableColumn<Attribute, String> attributes_altLeader;
    @FXML private TableColumn<Attribute, String> attributes_altLeaderPhone;

    @Autowired private DataPassService dataPassService;
    @Autowired private CacheService cacheService;

    @FXML public void initialize() {
        attributes.getItems().setAll(cacheService.getAttributes());
        number.setText(String.valueOf(attributes.getItems().size()));

        attributes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        attributes.setRowFactory( tv -> {
            TableRow<Attribute> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem().clone());
                    KRSTManagementSoftware.openWindow(AttributeInfoPage.class);
                    Pair<Boolean, Attribute> returnedData = (Pair<Boolean, Attribute>) dataPassService.getValue();
                    if (returnedData != null) {
                        if (returnedData.getKey()) {
                            attributes.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            attributes.getItems().remove(row.getIndex());
                        }
                        number.setText(String.valueOf(attributes.getItems().size()));
                    }
                }
            });
            return row ;
        });

        attributes_attribute.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        attributes_leader.setCellValueFactory(new PropertyValueFactory<>("leader"));
        attributes_leaderPhone.setCellValueFactory(new PropertyValueFactory<>("leaderPhone"));
        attributes_altLeader.setCellValueFactory(new PropertyValueFactory<>("altLeader"));
        attributes_altLeaderPhone.setCellValueFactory(new PropertyValueFactory<>("altLeaderPhone"));
    }

    public void addAttribute() {
        KRSTManagementSoftware.openWindow(AddAttribute.class);
        Attribute temp = (Attribute) dataPassService.getValue();
        if (temp != null) {
            attributes.getItems().add(temp);
            number.setText(String.valueOf(attributes.getItems().size()));
        }
    }
}
