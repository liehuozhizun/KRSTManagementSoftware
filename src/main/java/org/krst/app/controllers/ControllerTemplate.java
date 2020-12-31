package org.krst.app.controllers;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.krst.app.domains.operations.InformationOperations;

/*
 * This class is intended to support ComboBox and ListView exchange domain data
 * Instructions:
 *   1. Extend the controller class which needs data interaction with THIS abstract class
 *   2. Override flush() method to allow refresh displayed data for every action
 *   3. In the initialize() method, call setUpSelectorAndList()
 */
public abstract class ControllerTemplate {

    protected int biggestIndex = -1;
    protected boolean triggerByRemove = false;

    /*
     * Set all control components which are getting involved in data exchange
     */
    public abstract void flush();

    protected <T> void setUpSelectorAndList(ComboBox<T> selector, ListView<T> listView) {
        selector.getSelectionModel().selectedIndexProperty().addListener(getChangeListener(selector, listView));

        selector.setOnMousePressed(event -> {
            if (triggerByRemove) {
                selector.getItems().remove(biggestIndex);
                biggestIndex = -1;
                triggerByRemove = false;
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                T selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selector.getItems().add(selectedItem);
                    listView.getItems().remove(selectedItem);
                    flush();
                }
            }
        });

        flush();
    }

    private <T> ChangeListener<Number> getChangeListener(ComboBox<T> selector, ListView<T> listView) {
        return (observable, oldValue, newValue) -> {
            if(newValue.intValue() > biggestIndex) {
                biggestIndex = newValue.intValue();
            }
            if (biggestIndex != -1 && !triggerByRemove) {
                addElementToListView(selector.getValue(), listView);
                triggerByRemove = true;
                selector.getSelectionModel().clearSelection();
            }
        };
    }

    private <T> void addElementToListView(T object, ListView<T> listView) {
        if (!listView.getItems().contains(object)) {
            listView.getItems().add(object);
        }
    }

    protected <T> Callback<ListView<T>, ListCell<T>> getCellFactory() {
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
