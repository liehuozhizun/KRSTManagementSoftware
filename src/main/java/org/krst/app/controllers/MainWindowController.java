package org.krst.app.controllers;

import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.krst.app.KRSTManagementSoftware;
import org.krst.app.domains.Attribute;
import org.krst.app.domains.Staff;
import org.krst.app.domains.Student;
import org.krst.app.domains.Teacher;
import org.krst.app.repositories.*;
import org.krst.app.services.CacheService;
import org.krst.app.services.DataPassService;
import org.krst.app.utils.CommonUtils;
import org.krst.app.views.course.CourseTemplateControlPanel;
import org.krst.app.views.staff.AddStaff;
import org.krst.app.views.staff.StaffInfoPage;
import org.krst.app.views.student.AddStudent;
import org.krst.app.views.student.StudentInfoPage;
import org.krst.app.views.system.*;
import org.krst.app.views.teacher.AddTeacher;
import org.krst.app.views.teacher.TeacherInfoPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.time.LocalDate;

@FXMLController
public class MainWindowController {
    @FXML private BorderPane basePane;
    @FXML private SplitPane course;
    @FXML private SplitPane other;

    @Autowired private DataPassService dataPassService;
    @Autowired private StudentRepository studentRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private CacheService cacheService;

    @FXML public void initialize() {
        KRSTManagementSoftware.resizeWindow(785.0, 1250.0, "科瑞斯特管理软件");
        student_initialize();
    }

    // ----------------- pane switcher -----------------
    private boolean _teacherReady = false;
    private boolean _staffReady = false;
    private boolean _courseReady = false;
    public void showStudent() {
        student.toFront();
    }
    public void showTeacher() {
        teacher.toFront();
        if (!_teacherReady) teacher_initialize();
    }
    public void showStaff() {
        staff.toFront();
        if (!_staffReady) staff_initialize();
    }
    public void showCourse() {
        course.toFront();
    }
    public void showOther() {
        other.toFront();
    }

    // ----------------- system control -----------------
    public void system_changePassword() {
        KRSTManagementSoftware.openWindow(ChangePassword.class);
    }
    public void system_info() {
        KRSTManagementSoftware.openWindow(SystemInfoPage.class);
    }
    public void system_exit() {
        ((Stage)basePane.getScene().getWindow()).close();
    }
    public void database_destroy() {

    }
    public void database_import() {

    }
    public void database_export() {

    }
    public void other_remainder() {
        KRSTManagementSoftware.openWindow(Remainder.class);
    }
    public void other_log(){

    }

    // ----------------- Student Panel  -----------------
    @FXML private SplitPane student;
    @FXML private TextField student_id, student_name;
    @FXML private Text student_totalNumber, student_searchNumberPrompt, student_searchNumber;
    @FXML private ComboBox<String> student_gender;
    @FXML private ComboBox<Attribute> student_attribute;
    @FXML private TableView<Student> students;
    @FXML private TableColumn<Student, String> students_id, students_name, students_baptismalName, students_gender,
            students_phone, students_altPhone, students_address;
    @FXML private TableColumn<Student, Attribute> students_attribute, students_leader;
    @FXML private TableColumn<Student, LocalDate> students_birthday, students_age;

    private void student_initialize() {
        students.getItems().setAll(studentRepository.findAll());
        student_attribute.getItems().setAll(cacheService.getAttributes());
        student_attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return string == null ? null : student_attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
        student_totalNumber.setText(String.valueOf(students.getItems().size()));
        students.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Student student = row.getItem();
                    dataPassService.setValue(student);
                    KRSTManagementSoftware.openWindow(StudentInfoPage.class);
                    Boolean returnedData = (Boolean) dataPassService.getValue();
                    if (returnedData != null && !returnedData) {
                        students.getItems().remove(row.getIndex());
                        student_totalNumber.setText(String.valueOf(Integer.parseInt(student_totalNumber.getText()) - 1));
                    } else {
                        students.getItems().set(row.getIndex(), studentRepository.findById(row.getItem().getId()).orElse(null));
                    }
                }
            });
            return row ;
        });
        students.getItems().addListener((ListChangeListener<Student>) c -> {
            student_searchNumber.setText(String.valueOf(students.getItems().size()));
        });
        students_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        students_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        students_baptismalName.setCellValueFactory(new PropertyValueFactory<>("baptismalName"));
        students_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        students_birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        students_age.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        students_age.setCellFactory(new Callback<TableColumn<Student, LocalDate>, TableCell<Student, LocalDate>>() {
            @Override
            public TableCell<Student, LocalDate> call(TableColumn<Student, LocalDate> param) {
                return new TableCell<Student, LocalDate>() {
                    @Override
                    protected void updateItem(LocalDate birthday, boolean empty) {
                        super.updateItem(birthday, empty);
                        if (empty || birthday == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(String.valueOf(birthday.until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears()));
                        }
                    }
                };
            }
        });
        students_attribute.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        students_attribute.setCellFactory(new Callback<TableColumn<Student, Attribute>, TableCell<Student, Attribute>>() {
            @Override
            public TableCell<Student, Attribute> call(TableColumn<Student, Attribute> param) {
                return new TableCell<Student, Attribute>() {
                    @Override
                    protected void updateItem(Attribute attribute, boolean empty) {
                        super.updateItem(attribute, empty);
                        if (empty || attribute == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(attribute.getAttribute());
                        }
                    }
                };
            }
        });
        students_leader.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        students_leader.setCellFactory(new Callback<TableColumn<Student, Attribute>, TableCell<Student, Attribute>>() {
            @Override
            public TableCell<Student, Attribute> call(TableColumn<Student, Attribute> param) {
                return new TableCell<Student, Attribute>() {
                    @Override
                    protected void updateItem(Attribute attribute, boolean empty) {
                        super.updateItem(attribute, empty);
                        if (empty || attribute == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(attribute.getLeader());
                        }
                    }
                };
            }
        });
        students_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        students_altPhone.setCellValueFactory(new PropertyValueFactory<>("altPhone"));
        students_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    public void student_search() {
        student_searchNumberPrompt.setVisible(true);
        student_searchNumber.setVisible(true);

        Student studentExample = new Student();
        studentExample.setId(student_id.getText().isEmpty() ? null : student_id.getText());
        studentExample.setName(student_name.getText().isEmpty() ? null : student_name.getText());
        studentExample.setGender(student_gender.getValue());
        studentExample.setAttribute(student_attribute.getValue());
        students.getItems().setAll(studentRepository.findAll(Example.of(studentExample)));
    }
    public void student_clear() {
        students.getItems().setAll(studentRepository.findAll());
        student_searchNumberPrompt.setVisible(false);
        student_searchNumber.setVisible(false);
        student_id.clear();
        student_name.clear();
        student_gender.setValue(null);
        student_attribute.setValue(null);
        student_attribute.getItems().setAll(cacheService.getAttributes());
    }
    public void student_addStudent() {
        KRSTManagementSoftware.openWindow(AddStudent.class);
        Student savedStudent = (Student)dataPassService.getValue();
        if (savedStudent != null) {
            students.getItems().add(savedStudent);
            student_totalNumber.setText(String.valueOf(Integer.parseInt(student_totalNumber.getText()) + 1));
        }
    }

    // ----------------- Teacher Panel  -----------------
    @FXML private SplitPane teacher;
    @FXML private TextField teacher_id, teacher_name;
    @FXML private Text teacher_totalNumber, teacher_searchNumberPrompt, teacher_searchNumber;
    @FXML private ComboBox<String> teacher_gender;
    @FXML private ComboBox<Attribute> teacher_attribute;
    @FXML private TableView<Teacher> teachers;
    @FXML private TableColumn<Teacher, String> teachers_id, teachers_name, teachers_baptismalName, teachers_gender,
            teachers_phone, teachers_altPhone, teachers_address;
    @FXML private TableColumn<Teacher, Attribute> teachers_attribute, teachers_leader;
    @FXML private TableColumn<Teacher, LocalDate> teachers_birthday, teachers_age;

    private void teacher_initialize() {
        _teacherReady = true;
        teachers.getItems().setAll(teacherRepository.findAll());
        teacher_attribute.getItems().setAll(cacheService.getAttributes());
        teacher_attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute attribute) {
                return attribute == null ? null : attribute.getAttribute();
            }

            @Override
            public Attribute fromString(String string) {
                return string == null ? null : teacher_attribute.getItems().stream().filter(attribute ->
                        attribute.getAttribute().equals(string)).findFirst().orElse(null);
            }
        });
        teacher_totalNumber.setText(String.valueOf(teachers.getItems().size()));
        teachers.setRowFactory(tv -> {
            TableRow<Teacher> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Teacher teacher = row.getItem();
                    dataPassService.setValue(teacher);
                    KRSTManagementSoftware.openWindow(TeacherInfoPage.class);
                    Boolean returnedData = (Boolean) dataPassService.getValue();
                    if (returnedData != null && !returnedData) {
                        teachers.getItems().remove(row.getIndex());
                        teacher_totalNumber.setText(String.valueOf(Integer.parseInt(teacher_totalNumber.getText()) - 1));
                    } else {
                        teachers.getItems().set(row.getIndex(), teacherRepository.findById(row.getItem().getId()).orElse(null));
                    }
                }
            });
            return row ;
        });
        teachers.getItems().addListener((ListChangeListener<Teacher>) c -> {
            teacher_searchNumber.setText(String.valueOf(teachers.getItems().size()));
        });
        teachers_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        teachers_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        teachers_baptismalName.setCellValueFactory(new PropertyValueFactory<>("baptismalName"));
        teachers_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        teachers_birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        teachers_age.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        teachers_age.setCellFactory(new Callback<TableColumn<Teacher, LocalDate>, TableCell<Teacher, LocalDate>>() {
            @Override
            public TableCell<Teacher, LocalDate> call(TableColumn<Teacher, LocalDate> param) {
                return new TableCell<Teacher, LocalDate>() {
                    @Override
                    protected void updateItem(LocalDate birthday, boolean empty) {
                        super.updateItem(birthday, empty);
                        if (empty || birthday == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(String.valueOf(birthday.until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears()));
                        }
                    }
                };
            }
        });
        teachers_attribute.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        teachers_attribute.setCellFactory(new Callback<TableColumn<Teacher, Attribute>, TableCell<Teacher, Attribute>>() {
            @Override
            public TableCell<Teacher, Attribute> call(TableColumn<Teacher, Attribute> param) {
                return new TableCell<Teacher, Attribute>() {
                    @Override
                    protected void updateItem(Attribute attribute, boolean empty) {
                        super.updateItem(attribute, empty);
                        if (empty || attribute == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(attribute.getAttribute());
                        }
                    }
                };
            }
        });
        teachers_leader.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        teachers_leader.setCellFactory(new Callback<TableColumn<Teacher, Attribute>, TableCell<Teacher, Attribute>>() {
            @Override
            public TableCell<Teacher, Attribute> call(TableColumn<Teacher, Attribute> param) {
                return new TableCell<Teacher, Attribute>() {
                    @Override
                    protected void updateItem(Attribute attribute, boolean empty) {
                        super.updateItem(attribute, empty);
                        if (empty || attribute == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(attribute.getLeader());
                        }
                    }
                };
            }
        });
        teachers_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        teachers_altPhone.setCellValueFactory(new PropertyValueFactory<>("altPhone"));
        teachers_address.setCellValueFactory(new PropertyValueFactory<>("address"));
    }
    public void teacher_search() {
        teacher_searchNumberPrompt.setVisible(true);
        teacher_searchNumber.setVisible(true);

        Teacher teacherExample = new Teacher();
        teacherExample.setId(teacher_id.getText().isEmpty() ? null : teacher_id.getText());
        teacherExample.setName(teacher_name.getText().isEmpty() ? null : teacher_name.getText());
        teacherExample.setGender(teacher_gender.getValue());
        teacherExample.setAttribute(teacher_attribute.getValue());
        teachers.getItems().setAll(teacherRepository.findAll(Example.of(teacherExample)));
    }
    public void teacher_clear() {
        teachers.getItems().setAll(teacherRepository.findAll());
        teacher_searchNumberPrompt.setVisible(false);
        teacher_searchNumber.setVisible(false);
        teacher_id.clear();
        teacher_name.clear();
        teacher_gender.setValue(null);
        teacher_attribute.setValue(null);
        teacher_attribute.getItems().setAll(cacheService.getAttributes());
    }
    public void teacher_addTeacher() {
        KRSTManagementSoftware.openWindow(AddTeacher.class);
        Teacher savedTeacher = (Teacher) dataPassService.getValue();
        if (savedTeacher != null) {
            teachers.getItems().add(savedTeacher);
            teacher_totalNumber.setText(String.valueOf(Integer.parseInt(teacher_totalNumber.getText()) + 1));
        }
    }

    // ----------------- Staff Panel  -----------------
    @FXML private SplitPane staff;
    @FXML private TextField staff_id, staff_name, staff_title;
    @FXML private Text staff_totalNumber, staff_searchNumberPrompt, staff_searchNumber;
    @FXML private ComboBox<String> staff_gender;
    @FXML private TableView<Staff> staffs;
    @FXML private TableColumn<Staff, String> staffs_id, staffs_name, staffs_baptismalName, staffs_gender, staffs_title,
            staffs_responsibility, staffs_phone, staffs_altPhone;
    @FXML private TableColumn<Staff, LocalDate> staffs_birthday, staffs_age;

    private void staff_initialize() {
        _staffReady = true;
        staffs.getItems().setAll(cacheService.getStaffs());
        staff_totalNumber.setText(String.valueOf(staffs.getItems().size()));
        staffs.setRowFactory(tv -> {
            TableRow<Staff> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Staff staff = row.getItem();
                    dataPassService.setValue(staff);
                    KRSTManagementSoftware.openWindow(StaffInfoPage.class);
                    Boolean returnedData = (Boolean) dataPassService.getValue();
                    if (returnedData != null && !returnedData) {
                        staffs.getItems().remove(row.getIndex());
                        staff_totalNumber.setText(String.valueOf(Integer.parseInt(staff_totalNumber.getText()) - 1));
                    } else {
                        staffs.getItems().set(row.getIndex(), staffRepository.findById(row.getItem().getId()).orElse(null));
                    }
                }
            });
            return row ;
        });
        staffs.getItems().addListener((ListChangeListener<Staff>) c -> {
            staff_searchNumber.setText(String.valueOf(staffs.getItems().size()));
        });
        staffs_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        staffs_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        staffs_baptismalName.setCellValueFactory(new PropertyValueFactory<>("baptismalName"));
        staffs_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        staffs_birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        staffs_age.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        staffs_age.setCellFactory(new Callback<TableColumn<Staff, LocalDate>, TableCell<Staff, LocalDate>>() {
            @Override
            public TableCell<Staff, LocalDate> call(TableColumn<Staff, LocalDate> param) {
                return new TableCell<Staff, LocalDate>() {
                    @Override
                    protected void updateItem(LocalDate birthday, boolean empty) {
                        super.updateItem(birthday, empty);
                        if (empty || birthday == null) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            this.setText(String.valueOf(birthday.until(CommonUtils.getCurrentZonedTime().toLocalDate()).getYears()));
                        }
                    }
                };
            }
        });
        staffs_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        staffs_responsibility.setCellValueFactory(new PropertyValueFactory<>("responsibility"));
        staffs_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        staffs_altPhone.setCellValueFactory(new PropertyValueFactory<>("altPhone"));
    }
    public void staff_search() {
        staff_searchNumberPrompt.setVisible(true);
        staff_searchNumber.setVisible(true);

        Staff staffExample = new Staff();
        staffExample.setId(staff_id.getText().isEmpty() ? null : staff_id.getText());
        staffExample.setName(staff_name.getText().isEmpty() ? null : staff_name.getText());
        staffExample.setGender(staff_gender.getValue());
        staffExample.setTitle(staff_title.getText().isEmpty() ? null : staff_title.getText());
        if (staffExample.equals(new Staff())) staff_clear();
        else staffs.getItems().setAll(staffRepository.findAll(Example.of(staffExample)));
    }
    public void staff_clear() {
        staffs.getItems().setAll(cacheService.getStaffs());
        staff_searchNumberPrompt.setVisible(false);
        staff_searchNumber.setVisible(false);
        staff_id.clear();
        staff_name.clear();
        staff_gender.setValue(null);
        staff_title.clear();
    }
    public void staff_addStaff() {
        KRSTManagementSoftware.openWindow(AddStaff.class);
        Staff savedStaff = (Staff) dataPassService.getValue();
        if (savedStaff != null) {
            staffs.getItems().add(savedStaff);
            staff_totalNumber.setText(String.valueOf(Integer.parseInt(staff_totalNumber.getText()) + 1));
        }
    }

    // ----------------- Other Panel  -----------------
    public void other_courseTemplate() {
        KRSTManagementSoftware.openWindow(CourseTemplateControlPanel.class);
    }
    public void other_attribute() {
        KRSTManagementSoftware.openWindow(AttributeControlPanel.class);
    }
    public void other_patronSaintDate() {
        KRSTManagementSoftware.openWindow(PatronSaintDateControlPanel.class);
    }
}
