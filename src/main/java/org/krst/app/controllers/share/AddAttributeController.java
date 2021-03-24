package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.krst.app.domains.Attribute;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * In   : None
 * Out  : Attribute, new created Attribute if successfully created
 * Note : new created Attribute will be stored in CacheService
 */
@FXMLController
public class AddAttributeController {

    @FXML private TextField attribute, leader, leaderPhone, altLeader, altLeaderPhone;

    @Autowired private AttributeRepository attributeRepository;
    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;

    public void approve() {
        if (attributeRepository.existsById(attribute.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建所属堂区失败");
            alert.setHeaderText("失败原因：所属堂区已存在");
            alert.setContentText("解决方法：更换所属堂区");
            alert.showAndWait();
        } else {
            Attribute attr = new Attribute(
                    attribute.getText(),
                    leader.getText(),
                    leaderPhone.getText(),
                    altLeader.getText(),
                    altLeaderPhone.getText());
            if (!attr.equals(new Attribute())) {
                dataPassService.setValue(attributeRepository.save(attr));
                cacheService.refreshAttributeCache();
            }
            close();
        }
    }

    public void close() {
        ((Stage)attribute.getScene().getWindow()).close();
    }
}
