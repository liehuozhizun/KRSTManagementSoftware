package org.krst.app.views.person;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView(title = "新建普通档案", value = "AddPerson.fxml")
@Scope("prototype")
public class AddPerson extends AbstractFxmlView {
}
