package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/*
    此界面为测试专用
    使用方法：
        1. 按照提示在私有方法setWindowsForDisplay()中添加测试界面
        2. 打开顶部菜单栏Run->Edit Configurations
        3. 在Program arguments中输入“-test=true"
        4. 直接运行程序即可唤出测试界面
 */
@FXMLController
public class TestEngineController implements Initializable {

    @FXML
    private ListView<Class> windowList;
    @FXML
    private CheckBox openAndWaitFlag;

    /************************************************
     *        在此设置需要在测试界面中显示的窗口
     ************************************************/
    private List<Class> setWindowsForDisplay() {
        List<Class> list = new ArrayList<>();

        // 依次添加org.krst.app中views的class，例：
        //      list.add(Login.class);

        return list;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        windowList.getItems().addAll(setWindowsForDisplay());

        windowList.setCellFactory(new Callback<ListView<Class>, ListCell<Class>>() {
            @Override
            public ListCell<Class> call(ListView<Class> param) {
                return new ListCell<Class>() {
                    @Override
                    protected void updateItem(Class item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName().substring(item.getName().lastIndexOf(".") + 1));
                        }
                    }
                };
            }
        });

        windowList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Class clazz = windowList.getSelectionModel().getSelectedItem();
                KRSTManagementSoftware.openWindow(clazz, openAndWaitFlag.isSelected());
            }
        });
    }
}
