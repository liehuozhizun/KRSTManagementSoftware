package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.domains.Log;
import org.krst.app.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/*
 * IN  : none
 * OUT : none
 */
@FXMLController
public class LogInfoPageController {
    @FXML private TableView<Log> logs;
    @FXML private TableColumn<Log, Integer> logs_id;
    @FXML private TableColumn<Log, Log.Type> logs_type;
    @FXML private TableColumn<Log, String> logs_time;
    @FXML private TableColumn<Log, String> logs_path;
    @FXML private TableColumn<Log, String> logs_message;
    @FXML private Button btn_previous, btn_next;

    @Autowired private LogRepository logRepository;
    private Pageable pageable = PageRequest.of(0, 40);

    @FXML public void initialize() {
        initComponent();

        getLogs();
        if (pageable.next().isPaged()) btn_next.setDisable(false);
    }

    private void initComponent() {
        logs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        logs_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        logs_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        logs_type.setCellFactory(new Callback<TableColumn<Log, Log.Type>, TableCell<Log, Log.Type>>() {
            @Override
            public TableCell<Log, Log.Type> call(TableColumn<Log, Log.Type> param) {
                return new TableCell<Log, Log.Type>() {
                    @Override
                    protected void updateItem(Log.Type item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setStyle("-fx-background-color: none");
                            switch (item) {
                                case INFO:
                                    this.setText("信息");
                                    this.setTextFill(Color.GREEN);
                                    break;
                                case WARN:
                                    this.setText("警告");
                                    this.setTextFill(Color.ORANGE);
                                    break;
                                case ERROR:
                                    this.setText("错误");
                                    this.setTextFill(Color.RED);
                                    break;
                                case FETAL:
                                    this.setText("崩溃");
                                    this.setTextFill(Color.WHITE);
                                    this.setStyle("-fx-background-color: RED");
                                    break;
                                default:
                                    this.setText("-无-");
                                    break;
                            }
                        }
                    }
                };
            }
        });
        logs_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        logs_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        logs_path.setCellValueFactory(new PropertyValueFactory<>("path"));
    }

    private void getLogs() {
        Page<Log> page = logRepository.findAll(pageable);
        int numberOfElements = page.getNumberOfElements();
        if (numberOfElements == 0) {
            pageable = pageable.previousOrFirst();
            btn_next.setDisable(true);
            return;
        }

        if (numberOfElements < 40) btn_next.setDisable(true);
        logs.getItems().setAll(page.toList());
    }

    public void previous() {
        pageable = pageable.previousOrFirst();
        getLogs();
        btn_previous.setDisable(!pageable.hasPrevious());
        btn_next.setDisable(false);
    }

    public void next() {
        pageable = pageable.next();
        getLogs();
        btn_previous.setDisable(false);
    }

    public void close() {
        ((Stage) logs.getScene().getWindow()).close();
    }
}
