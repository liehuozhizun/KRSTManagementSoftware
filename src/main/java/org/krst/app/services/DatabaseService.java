package org.krst.app.services;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;

@Service
public class DatabaseService {

    private File database;

    @PostConstruct
    public void init() {
        database = new File("data.db").getAbsoluteFile();
    }

    public String getDatabaseAbsolutePath() {
        if (database.exists()) {
            return database.getAbsolutePath();
        }
        return "未找到数据库文件";
    }

    public String getDatabaseSize() {
        if (database.exists()) {
            return database.length() / 1024 + " KB";
        }
        return "未找到数据库文件";
    }

    public File getDatabaseFile() {
        return database;
    }

    public void openDatabaseLocationFolder() {
        try {
            if (database.exists()) {
                File databaseParent = database.getParentFile();
                if (databaseParent.exists()) {
                    Desktop.getDesktop().open(databaseParent);
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("打开数据库所在文件夹错误");
            alert.setContentText("因未查询到数据库所在文件夹的地址，所以无法打开文件夹目录");
            alert.showAndWait();
        }
    }
}
