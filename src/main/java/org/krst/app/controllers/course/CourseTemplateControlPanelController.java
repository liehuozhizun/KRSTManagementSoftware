package org.krst.app.controllers.course;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.views.course.AddCourseTemplate;
import org.krst.app.views.course.CourseTemplateInfoPage;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * IN  : none
 * OUT : none
 */
@FXMLController
public class CourseTemplateControlPanelController {
    @FXML private Text number;
    @FXML private TableView<CourseTemplate> courseTemplates;
    @FXML private TableColumn<CourseTemplate, String> id;
    @FXML private TableColumn<CourseTemplate, String> name;
    @FXML private TableColumn<CourseTemplate, String> topic;

    @Autowired private CacheService cacheService;
    @Autowired private DataPassService dataPassService;

    @FXML public void initialize() {
        if (number.getText() == null || number.getText().equals("0"))
            courseTemplates.getItems().setAll(cacheService.getCourseTemplates());

        number.setText(String.valueOf(courseTemplates.getItems().size()));
        courseTemplates.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        courseTemplates.setRowFactory( tv -> {
            TableRow<CourseTemplate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    dataPassService.setValue(row.getItem().clone());
                    KRSTManagementSoftware.openWindow(CourseTemplateInfoPage.class);
                    Pair<Boolean, CourseTemplate> returnedData = (Pair<Boolean, CourseTemplate>) dataPassService.getValue();
                    if (returnedData != null) {
                        if (returnedData.getKey()) {
                            courseTemplates.getItems().set(row.getIndex(), returnedData.getValue());
                        } else {
                            courseTemplates.getItems().remove(row.getIndex());
                        }
                        number.setText(String.valueOf(courseTemplates.getItems().size()));
                    }
                }
            });
            return row ;
        });

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
    }

    public void addCourseTemplate() {
        KRSTManagementSoftware.openWindow(AddCourseTemplate.class);
        CourseTemplate temp = (CourseTemplate) dataPassService.getValue();
        if (temp != null) {
            courseTemplates.getItems().add(temp);
            number.setText(String.valueOf(courseTemplates.getItems().size()));
        }
    }

}
