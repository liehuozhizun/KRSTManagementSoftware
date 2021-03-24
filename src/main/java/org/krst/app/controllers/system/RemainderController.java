package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.krst.app.models.Remainder;
import org.krst.app.services.RemainderService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.RemainderDateType;
import org.krst.app.utils.RemainderEventType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@FXMLController
public class RemainderController  {
    @FXML private TableView<Remainder> today;
    @FXML private TableColumn<Remainder, String> today_date;
    @FXML private TableColumn<Remainder, RemainderEventType> today_type;
    @FXML private TableColumn<Remainder, String> today_name;
    @FXML private TableView<Remainder> oneDay;
    @FXML private TableColumn<Remainder, String> oneDay_date;
    @FXML private TableColumn<Remainder, RemainderEventType> oneDay_type;
    @FXML private TableColumn<Remainder, String> oneDay_name;
    @FXML private TableView<Remainder> oneWeek;
    @FXML private TableColumn<Remainder, String> oneWeek_date;
    @FXML private TableColumn<Remainder, RemainderEventType> oneWeek_type;
    @FXML private TableColumn<Remainder, String> oneWeek_name;
    @FXML private TableView<Remainder> halfMonth;
    @FXML private TableColumn<Remainder, String> halfMonth_date;
    @FXML private TableColumn<Remainder, RemainderEventType> halfMonth_type;
    @FXML private TableColumn<Remainder, String> halfMonth_name;
    @FXML private TableView<Remainder> oneMonth;
    @FXML private TableColumn<Remainder, String> oneMonth_date;
    @FXML private TableColumn<Remainder, RemainderEventType> oneMonth_type;
    @FXML private TableColumn<Remainder, String> oneMonth_name;
    @FXML private Text refreshedTime;

    @Autowired private RemainderService remainderService;

    @FXML
    public void initialize() {
        refreshedTime.setText(CommonUtils.getCurrentTime());
        remainderService.refresh();
        Map<RemainderDateType, List<Remainder>> remainders = remainderService.getRemainder();
        today.getItems().addAll(remainders.get(RemainderDateType.TODAY));
        oneDay.getItems().addAll(remainders.get(RemainderDateType.ONE_DAY));
        oneWeek.getItems().addAll(remainders.get(RemainderDateType.ONE_WEEK));
        halfMonth.getItems().addAll(remainders.get(RemainderDateType.HALF_MONTH));
        oneMonth.getItems().addAll(remainders.get(RemainderDateType.ONE_MONTH));

        today.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        oneDay.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        oneWeek.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        halfMonth.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        oneMonth.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        today_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        oneDay_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        oneWeek_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        halfMonth_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        oneMonth_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        Callback<TableColumn<Remainder, RemainderEventType>, TableCell<Remainder, RemainderEventType>> callback =
                new Callback<TableColumn<Remainder, RemainderEventType>, TableCell<Remainder, RemainderEventType>>() {
            @Override
            public TableCell<Remainder, RemainderEventType> call(TableColumn<Remainder, RemainderEventType> param) {
                return new TableCell<Remainder, RemainderEventType>() {
                    @Override
                    protected void updateItem(RemainderEventType item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            String eventTypeName = null;
                            switch (item) {
                                case BIRTHDAY:
                                    this.setTextFill(Color.GREEN);
                                    eventTypeName = "生日";
                                    break;
                                case BAPTISMAL:
                                    this.setTextFill(Color.BLUE);
                                    eventTypeName = "领洗纪念日";
                                    break;
                                case CONFIRMATION:
                                    this.setTextFill(Color.DEEPPINK);
                                    eventTypeName = "坚振纪念日";
                                    break;
                                case MARRIAGE:
                                    this.setTextFill(Color.RED);
                                    eventTypeName = "结婚纪念日";
                                    break;
                                case DEATH:
                                    this.setTextFill(Color.GREY);
                                    eventTypeName = "祭日";
                                    break;
                                case PATRON_SAINT:
                                    this.setTextFill(Color.GOLD);
                                    eventTypeName = "圣人主保日";
                                    break;
                                default: break;
                            }
                            this.setText(eventTypeName);
                        }
                    }
                };
            }
        };

        today_type.setCellValueFactory(new PropertyValueFactory<>("event"));
        today_type.setCellFactory(callback);
        oneDay_type.setCellValueFactory(new PropertyValueFactory<>("event"));
        oneDay_type.setCellFactory(callback);
        oneWeek_type.setCellValueFactory(new PropertyValueFactory<>("event"));
        oneWeek_type.setCellFactory(callback);
        halfMonth_type.setCellValueFactory(new PropertyValueFactory<>("event"));
        halfMonth_type.setCellFactory(callback);
        oneMonth_type.setCellValueFactory(new PropertyValueFactory<>("event"));
        oneMonth_type.setCellFactory(callback);

        today_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneDay_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneWeek_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        halfMonth_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneMonth_name.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    public void refresh() {
        remainderService.refresh();
        Map<RemainderDateType, List<Remainder>> remainders = remainderService.getRemainder();
        today.getItems().clear();
        today.getItems().addAll(remainders.get(RemainderDateType.TODAY));
        oneDay.getItems().clear();
        oneDay.getItems().addAll(remainders.get(RemainderDateType.ONE_DAY));
        oneWeek.getItems().clear();
        oneWeek.getItems().addAll(remainders.get(RemainderDateType.ONE_WEEK));
        halfMonth.getItems().clear();
        halfMonth.getItems().addAll(remainders.get(RemainderDateType.HALF_MONTH));
        oneMonth.getItems().clear();
        oneMonth.getItems().addAll(remainders.get(RemainderDateType.ONE_MONTH));
        refreshedTime.setText(CommonUtils.getCurrentTime());
    }

    public void close() {
        ((Stage)refreshedTime.getScene().getWindow()).close();
    }

}
