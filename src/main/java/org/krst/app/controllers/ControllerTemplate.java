package org.krst.app.controllers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.domains.InformationOperations;

/*
 * This class is intended to support ComboBox and ListView exchange domain data
 * Instructions:
 *   1. Extend the controller class which needs data interaction with THIS abstract class
 *   2. In the initialize() method, call setUpSelectorAndList()
 */
public abstract class ControllerTemplate {

    private boolean flag = false;
    private int idx = -1;
    private String promptText = "选择人员";

    @Deprecated
    protected void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    protected <T extends InformationOperations> void setUpSelectorAndList(ComboBox<T> selector, ListView<T> listView) {
        selector.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (!flag && newValue != null) {
                listView.getItems().add(selector.getValue());
                idx = selector.getSelectionModel().getSelectedIndex();
            }
            flag = !flag;
        });

        selector.setOnMousePressed(event -> {
            if (idx != -1) {
                flag = true;
                selector.getItems().remove(idx);
                idx = -1;
            }
        });

        selector.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return promptText;
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                T selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selector.getItems().add(selectedItem);
                    listView.getItems().remove(selectedItem);
                }
            }
        });

        selector.setCellFactory(getCellFactory());
        listView.setCellFactory(getCellFactory());
    }

    private <T> Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(((InformationOperations)item).getNameAndId());
                        }
                    }
                };
            }
        };
    }
}
