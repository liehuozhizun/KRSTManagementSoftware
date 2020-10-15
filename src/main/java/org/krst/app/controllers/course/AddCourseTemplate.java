package org.krst.app.controllers.course;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.models.Status;
import org.krst.app.services.CacheService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.database.DatabaseFactory;
import org.krst.app.utils.database.DatabaseType;

public class AddCourseTemplate {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextArea topic;

    public void approve() {
        CourseTemplate courseTemplate = new CourseTemplate(
                id.getText(),
                name.getText(),
                topic.getText());
        Status status = DatabaseFactory.getDatabase(DatabaseType.COURSE_TEMPLATE).save(courseTemplate);
        if (status == Status.SUCCESS) {
            CacheService.get().refreshCourseTemplateCache();
        } else {
            CommonUtils.alertOperationError("添加课程模板操作");
        }
        KRSTManagementSoftware.closeNewWindow();
    }

    public void cancel() {
        KRSTManagementSoftware.closeNewWindow();
    }
}
