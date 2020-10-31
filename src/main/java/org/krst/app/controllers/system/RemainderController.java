package org.krst.app.controllers.system;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.models.Remainder;
import org.krst.app.services.RemainderService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.RemainderDateType;
import org.krst.app.utils.RemainderEventType;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
public class RemainderController implements Initializable {

    @FXML
    private TableView<Remainder> today;
    @FXML
    private TableColumn<Remainder, String> today_date;
    @FXML
    private TableColumn<Remainder, String> today_type;
    @FXML
    private TableColumn<Remainder, String> today_name;
    @FXML
    private TableView<Remainder> oneDay;
    @FXML
    private TableColumn<Remainder, String> oneDay_date;
    @FXML
    private TableColumn<Remainder, String> oneDay_type;
    @FXML
    private TableColumn<Remainder, String> oneDay_name;
    @FXML
    private TableView<Remainder> oneWeek;
    @FXML
    private TableColumn<Remainder, String> oneWeek_date;
    @FXML
    private TableColumn<Remainder, String> oneWeek_type;
    @FXML
    private TableColumn<Remainder, String> oneWeek_name;
    @FXML
    private TableView<Remainder> halfMonth;
    @FXML
    private TableColumn<Remainder, String> halfMonth_date;
    @FXML
    private TableColumn<Remainder, String> halfMonth_type;
    @FXML
    private TableColumn<Remainder, String> halfMonth_name;
    @FXML
    private TableView<Remainder> oneMonth;
    @FXML
    private TableColumn<Remainder, String> oneMonth_date;
    @FXML
    private TableColumn<Remainder, String> oneMonth_type;
    @FXML
    private TableColumn<Remainder, String> oneMonth_name;
    @FXML
    private Text TODAY_DATE;

    @Autowired
    private RemainderService remainderService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TODAY_DATE.setText(CommonUtils.getCurrentTime());

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

        Callback<TableColumn<Remainder, String>, TableCell<Remainder, String>> callback =
                new Callback<TableColumn<Remainder, String>, TableCell<Remainder, String>>() {
            @Override
            public TableCell<Remainder, String> call(TableColumn<Remainder, String> param) {
                return new TableCell<Remainder, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        if (!empty) {
                            RemainderEventType type = this.getTableView().getItems().get(this.getIndex()).getEvent();
                            this.setText(convertEventTypeToString(type));
                            switch (type) {
                                case BIRTHDAY: this.setTextFill(Color.GREEN); break;
                                case BAPTISMAL: this.setTextFill(Color.BLUE); break;
                                case CONFIRMATION: this.setTextFill(Color.DEEPPINK); break;
                                case MARRIAGE: this.setTextFill(Color.RED); break;
                                case DEATH: this.setTextFill(Color.GREY); break;
                                case PATRON_SAINT: this.setTextFill(Color.GOLD); break;
                                default: break;
                            }
                        }
                    }
                };
            }
        };

        today_type.setCellFactory(callback);
        oneDay_type.setCellFactory(callback);
        oneWeek_type.setCellFactory(callback);
        halfMonth_type.setCellFactory(callback);
        oneMonth_type.setCellFactory(callback);

        today_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneDay_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneWeek_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        halfMonth_name.setCellValueFactory(new PropertyValueFactory<>("message"));
        oneMonth_name.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    private String convertEventTypeToString(RemainderEventType type) {
        switch (type) {
            case BIRTHDAY: return "生日";
            case BAPTISMAL: return "领洗纪念日";
            case CONFIRMATION: return "坚振纪念日";
            case MARRIAGE: return "结婚纪念日";
            case DEATH: return "祭日";
            case PATRON_SAINT: return "圣人主保日";
            default: return null;
        }
    }

    public void refresh() throws InterruptedException {
        remainderService.refresh();
        Thread.sleep(1000);
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
        TODAY_DATE.setText(CommonUtils.getCurrentTime());
    }

    public void close() {
        KRSTManagementSoftware.closeWindow();
    }

}
