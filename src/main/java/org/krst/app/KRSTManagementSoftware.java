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
    private static Stage thirdStage;
    private static Stage forthStage;
    private static Stage fifthStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/views/student/AddStudent.fxml"));
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
        if (secondaryStage == null || !secondaryStage.isShowing()) {
            secondaryStage = new Stage();
            Scene scene = new Scene(root);
            secondaryStage.setTitle(title);
            secondaryStage.setScene(scene);
            secondaryStage.setResizable(resizable);
            secondaryStage.showAndWait();
        } else if (thirdStage == null || !thirdStage.isShowing()){
            thirdStage = new Stage();
            Scene scene = new Scene(root);
            thirdStage.setTitle(title);
            thirdStage.setScene(scene);
            thirdStage.setResizable(resizable);
            thirdStage.showAndWait();
        } else if (forthStage == null || !forthStage.isShowing()){
            forthStage = new Stage();
            Scene scene = new Scene(root);
            forthStage.setTitle(title);
            forthStage.setScene(scene);
            forthStage.setResizable(resizable);
            forthStage.showAndWait();
        } else {
            fifthStage = new Stage();
            Scene scene = new Scene(root);
            fifthStage.setTitle(title);
            fifthStage.setScene(scene);
            fifthStage.setResizable(resizable);
            fifthStage.showAndWait();
        }

    }

    public static void closeNewWindow() {
        if (fifthStage != null && fifthStage.isShowing()) {
            fifthStage.close();
        } else if (forthStage != null && forthStage.isShowing()) {
            forthStage.close();
        } else if (thirdStage != null && thirdStage.isShowing()){
            thirdStage.close();
        } else if (secondaryStage != null && secondaryStage.isShowing()) {
            secondaryStage.close();
        } else {
            primaryStage.close();
        }
    }
}