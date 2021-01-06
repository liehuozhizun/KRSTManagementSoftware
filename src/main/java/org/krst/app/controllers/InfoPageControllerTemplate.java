package org.krst.app.controllers;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.ColorAdjust;

public abstract class InfoPageControllerTemplate {

    private final double OPACITY_DISABLE_MODE = 0.85;
    private final double OPACITY_ENABLE_MODE = 1;

    private final ColorAdjust editableColorAdjust = new ColorAdjust(0, 0, 0, 0);
    private final ColorAdjust uneditableColorAdjust = new ColorAdjust(0, 0, -0.01, 0);

    protected abstract void setEditableMode(boolean state);

    protected void setTextEditableMode(boolean state, TextInputControl...textInputControls) {
        ColorAdjust colorAdjust = state ? editableColorAdjust : uneditableColorAdjust;
        for (TextInputControl textInputControl : textInputControls) {
            textInputControl.setEditable(state);
            textInputControl.setEffect(colorAdjust);
        }
    }

    protected void setDatePickerEditableMode(boolean state, DatePicker...datePickers) {
        double opacity = state ? OPACITY_ENABLE_MODE : OPACITY_DISABLE_MODE;
        for (DatePicker datePicker : datePickers) {
            datePicker.setDisable(!state);
            datePicker.getEditor().setOpacity(OPACITY_ENABLE_MODE);
            datePicker.setOpacity(opacity);
        }
    }

    protected abstract void setButtonMode(boolean state);
}
