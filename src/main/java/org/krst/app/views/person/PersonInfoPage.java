package org.krst.app.views.person;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView(title = "普通人档案", value = "PersonInfoPage.fxml")
@Scope("prototype")
public class PersonInfoPage extends AbstractFxmlView {
}
