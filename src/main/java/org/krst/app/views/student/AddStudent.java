package org.krst.app.views.student;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView(title = "新建学生档案", value = "AddStudent.fxml")
@Scope("prototype")
public class AddStudent extends AbstractFxmlView {

}
