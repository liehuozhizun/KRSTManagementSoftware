package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.krst.app.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@FXMLController
public class SystemInfoPageController {
    @FXML private Text version, JVMVersion, databaseLocation, databaseSize;

    @Value("${krst.version}")
    private String versionNumber;

    @Value("${krst.JVMVersion}")
    private String JVMVersionNumber;

    @Autowired private DatabaseService databaseService;

    @FXML public void initialize() {
        version.setText(versionNumber);
        JVMVersion.setText(JVMVersionNumber);
        databaseLocation.setText(databaseService.getDatabaseAbsolutePath());
        databaseSize.setText(databaseService.getDatabaseSize());
    }
}
