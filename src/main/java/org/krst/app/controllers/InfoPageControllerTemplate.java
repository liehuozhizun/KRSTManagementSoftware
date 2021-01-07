package org.krst.app.controllers;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.ColorAdjust;

public interface InfoPageControllerTemplate {

    double OPACITY_DISABLE_MODE = 0.85;
    double OPACITY_ENABLE_MODE = 1;

    ColorAdjust editableColorAdjust = new ColorAdjust(0, 0, 0, 0);
    ColorAdjust uneditableColorAdjust = new ColorAdjust(0, 0, -0.01, 0);

    void setEditableMode(boolean state);

    default void setTextEditableMode(boolean state, TextInputControl... textInputControls) {
        ColorAdjust colorAdjust = state ? editableColorAdjust : uneditableColorAdjust;
        for (TextInputControl textInputControl : textInputControls) {
            textInputControl.setEditable(state);
            textInputControl.setEffect(colorAdjust);
        }
    }

    default void setDatePickerEditableMode(boolean state, DatePicker...datePickers) {
        double opacity = state ? OPACITY_ENABLE_MODE : OPACITY_DISABLE_MODE;
        for (DatePicker datePicker : datePickers) {
            datePicker.setDisable(!state);
            datePicker.getEditor().setOpacity(OPACITY_ENABLE_MODE);
            datePicker.setOpacity(opacity);
        }
    }

    void setButtonMode(boolean state);
}
