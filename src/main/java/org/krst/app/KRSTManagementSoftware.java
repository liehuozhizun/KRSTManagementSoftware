package org.krst.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KRSTManagementSoftware extends Application {

    private static Stage primaryStage;
    private static Stage secondaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/views/system/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("科瑞斯特管理软件");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(Parent root) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    public static void openNewWindow(Parent root, String title, Boolean resizable) {
        secondaryStage = new Stage();
        Scene scene = new Scene(root);
        secondaryStage.setTitle(title);
        secondaryStage.setScene(scene);
        secondaryStage.setResizable(resizable);
        secondaryStage.showAndWait();
    }

    public static void closeNewWindow() {
        secondaryStage.close();
    }
}