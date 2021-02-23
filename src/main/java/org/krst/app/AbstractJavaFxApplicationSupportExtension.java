package org.krst.app;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.krst.app.configurations.Logger;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class AbstractJavaFxApplicationSupportExtension extends AbstractJavaFxApplicationSupport {

    private static Logger logger;

    public static void openWindow(final Class<? extends AbstractFxmlView> window) {
        openWindow(window, true);
    }

    public static void openWindow(final Class<? extends AbstractFxmlView> window, boolean OpenAndWait) {
        initContext();
        final AbstractFxmlView view = context.getBean(window);
        Stage newStage = new Stage();

        Scene newScene;
        if (view.getView().getScene() != null) {
            // This view was already shown so
            // we have a scene for it and use this one.
            newScene = view.getView().getScene();
        } else {
            newScene = new Scene(view.getView());
        }

        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(getStage());

        String defaultTitle = null;
        try {
            Method privateStringMethod = AbstractFxmlView.class.getDeclaredMethod("getDefaultTitle");
            privateStringMethod.setAccessible(true);
            defaultTitle = (String) privateStringMethod.invoke(view);
        } catch (Exception e) {
            logger.logError("KRSTManagementSoftware - Starter Class", "无法获取defualtStyle： e - " + e.getMessage());
        }
        newStage.setTitle(defaultTitle);

        StageStyle defaultStyle = null;
        try {
            Method privateStringMethod = AbstractFxmlView.class.getDeclaredMethod("getDefaultStyle");
            privateStringMethod.setAccessible(true);
            defaultStyle = (StageStyle) privateStringMethod.invoke(view);
        } catch (Exception e) {
            logger.logError("KRSTManagementSoftware - Starter Class", "无法获取defualtStyle： e - " + e.getMessage());
        }
        newStage.initStyle(defaultStyle);

        newStage.centerOnScreen();
        if (OpenAndWait) {
            newStage.showAndWait();
        } else {
            newStage.show();
        }
    }

    public static void switchScene(final Class<? extends AbstractFxmlView> window) {
        showView(window);
    }

    public static void resizeWindow(double height, double width) {
        getStage().setResizable(false);
        getStage().setHeight(height);
        getStage().setWidth(width);
        getStage().centerOnScreen();
    }

    public static void resizeWindow(double height, double width, String title) {
        resizeWindow(height, width);
        getStage().setTitle(title);
    }

    private static ConfigurableApplicationContext context = null;
    private static void initContext() {
        if (context == null) {
            try {
                Field field = AbstractJavaFxApplicationSupport.class.getDeclaredField("applicationContext");
                field.setAccessible(true);
                context = (ConfigurableApplicationContext) field.get(null);
                logger = context.getBean(Logger.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.logFetal("KRSTManagementSoftware - Starter Class", "无法加载ConfigurableApplicationContext： e - " + e.getMessage());
            }
        }
    }

}
