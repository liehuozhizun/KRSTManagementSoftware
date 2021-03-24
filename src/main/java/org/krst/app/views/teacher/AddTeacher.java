package org.krst.app.views.teacher;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView(title = "新建教师档案", value = "AddTeacher.fxml")
@Scope("prototype")
public class AddTeacher extends AbstractFxmlView {
}
