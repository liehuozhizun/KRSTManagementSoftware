package org.krst.app.controllers.share;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.models.Status;
import org.krst.app.services.CacheService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

public class AddAttribute {

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

    public void approve() {
        Status status = DatabaseFactory.getDatabase(DatabaseType.ATTRIBUTE).save(new Attribute(
                attribute.getText(),
                leader.getText(),
                leaderPhone.getText(),
                altLeader.getText(),
                altLeaderPhone.getText()));
        if (status == Status.SUCCESS) {
            CacheService.get().refreshAttributeCache();
        } else {
            CommonUtils.alertOperationError("添加所属堂区操作");
        }
        KRSTManagementSoftware.closeNewWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }
}
