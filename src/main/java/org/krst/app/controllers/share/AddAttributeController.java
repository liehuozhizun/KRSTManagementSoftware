package org.krst.app.controllers.share;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.models.Status;
import org.krst.app.repositories.AttributeRepository;
import org.krst.app.services.CacheService;
import org.krst.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class AddAttributeController {

    @FXML
    private TextField attribute;
    @FXML
    private TextField leader;
    @FXML
    private TextField leaderPhone;
    @FXML
    private TextField altLeader;
    @FXML
    private TextField altLeaderPhone;

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private CacheService cacheService;

    public void approve() {
        if (attributeRepository.existsById(attribute.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建所属堂区失败");
            alert.setHeaderText("失败原因：所属堂区已存在");
            alert.setContentText("解决方法：更换所属堂区");
            alert.showAndWait();
        } else {
            attributeRepository.save(new Attribute(
                    attribute.getText(),
                    leader.getText(),
                    leaderPhone.getText(),
                    altLeader.getText(),
                    altLeaderPhone.getText()));
            cacheService.refreshAttributeCache();
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
