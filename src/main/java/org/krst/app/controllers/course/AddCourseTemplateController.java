package org.krst.app.controllers.course;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import de.felixroske.jfxsupport.FXMLController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.CourseTemplate;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.CourseTemplateRepository;
import org.krst.app.repositories.TeacherRepository;
import org.krst.app.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;

@FXMLController
public class AddCourseTemplateController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextArea topic;
    @FXML
    private ComboBox<Teacher> teacherSelector;
    @FXML
    private ListView<Teacher> teachers;
    @FXML
    private ListView<Teacher> emptyteachers;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseTemplateRepository courseTemplateRepository;
    @Autowired
    private CacheService cacheService;
    private int biggestIndex=-1;
    private  boolean triggerByRemove=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherSelector.getItems().addAll(teacherRepository.findAll());

        flushonetime();



        teacherSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            //Teacher selectedItem=teacherSelector.getSelectionModel().getSelectedItem();
            // teacherSelector.getSelectionModel().clearSelection();
            System.out.println("newValue:"+newValue);
            System.out.println("oldValue:"+oldValue);
            System.out.println("observe:"+observable);
            System.out.println(Thread.currentThread().getId());

            if((int)newValue>biggestIndex)
            {biggestIndex=(int)newValue;}
            //Teacher selectedItem=teacherSelector.getItems().get(biggestIndex);
            System.out.println("saveIndex="+biggestIndex);
            //System.out.println("index: "+teacherSelector.getSelectionModel().getSelectedIndex());
            if (biggestIndex!= -1&&triggerByRemove==false) {
                addTeacherToTeachers(biggestIndex);
                triggerByRemove=true;
                teacherSelector.getSelectionModel().clearSelection();


                flushonetime();
            }
//            if (selectedItem!= null) {
//
//                //System.out.println("1"+teacherSelector.getSelectionModel().getSelectedItem());
//
//                addTeacherToTeachers(selectedItem);
//
//                teacherSelector.getItems().remove(selectedItem);
//                //teacherSelector.getSelectionModel().clearSelection(teacherSelector.getSelectionModel().getSelectedIndex());
//                teacherSelector.getSelectionModel().clearSelection();
//
//                System.out.println("2"+teacherSelector.getSelectionModel().getSelectedItem());
//                flushonetime();
//
//            }
            System.out.println("reach end { index="+biggestIndex+" trigger="+triggerByRemove+" }");

//            if(triggerByRemove==true&&biggestIndex==-1&&((int)oldValue-(int)newValue==-1)){
//                triggerByRemove=false;
//            }
        });


        teacherSelector.setOnMousePressed(event -> {
            flushonetime();
            if (triggerByRemove==true) {
                teacherSelector.getItems().remove(biggestIndex);
                biggestIndex=-1;
            triggerByRemove=false;
            System.out.println("trigger renew to false");
            }
        });
        teachers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Teacher selectedItem=teachers.getSelectionModel().getSelectedItem();
                if(selectedItem!=null) {
                    teacherSelector.getItems().add(selectedItem);
                    teachers.getItems().remove(teachers.getSelectionModel().getSelectedIndex());
                    flushonetime();
                }
            }
        });
    }

    private void addTeacherToTeachers(int index) {
        System.out.println("4:"+teacherSelector.getItems().get(index));
        if (!teachers.getItems().contains(teacherSelector.getItems().get(index))) {
            System.out.println("getInAdd");
            teachers.getItems().add(teacherSelector.getItems().get(index));


        //    teacherSelector.getItems().addAll(teacherRepository.findAll());
        }
    }
    private  void flushonetime(){
        teachers.getItems().stream().sorted().distinct();
        teacherSelector.getItems().stream().sorted().distinct();
        teachers.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
            @Override
            public ListCell<Teacher> call(ListView<Teacher> param) {
                return new ListCell<Teacher>() {
                    @Override
                    protected void updateItem(Teacher item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " [" + item.getId() + "]");
                        }
                    }
                };
            }
        });
        teacherSelector.setCellFactory(new Callback<ListView<Teacher>, ListCell<Teacher>>() {
            @Override
            public ListCell<Teacher> call(ListView<Teacher> param) {
                return new ListCell<Teacher>() {
                    @Override
                    protected void updateItem(Teacher item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " [" + item.getId() + "]");
                        }
                    }
                };
            }
        });


    }

    public void approve() {
        CourseTemplate courseTemplate = new CourseTemplate(
                id.getText(),
                name.getText(),
                topic.getText(),
                new HashSet<>(teachers.getItems()));

        if (courseTemplateRepository.existsById(courseTemplate.getId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("新建课程模板失败");
            alert.setHeaderText("失败原因：课程编号已存在");
            alert.setContentText("解决方法：更换课程编号");
            alert.showAndWait();
        } else {
            courseTemplateRepository.save(courseTemplate);
            cacheService.refreshCourseTemplateCache();
            KRSTManagementSoftware.closeWindow();
        }
    }

    public void cancel() {
        KRSTManagementSoftware.closeWindow();
    }
}
