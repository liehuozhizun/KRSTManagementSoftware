package org.krst.app.views.teacher;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@FXMLView(title = "教师档案", value = "TeacherInfoPage.fxml")
public class TeacherInfoPage extends AbstractFxmlView {
}
