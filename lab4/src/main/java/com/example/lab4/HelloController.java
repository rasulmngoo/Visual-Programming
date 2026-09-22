package com.example.lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {
    @FXML private TextField txtFullName;
    @FXML private TextField txtGroup;
    @FXML private ComboBox<String> cmbCourse;
    @FXML private TableView<Student> tblStudents;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colGroup;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private Label lblStatus;

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbCourse.getItems().addAll("1 курс", "2 курс", "3 курс", "4 курс");
        cmbCourse.getSelectionModel().selectFirst();

        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("group"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        tblStudents.setItems(studentList);
    }

    @FXML
    private void onSaveClick() {
        String name = txtFullName.getText().trim();
        String group = txtGroup.getText().trim();
        String course = cmbCourse.getValue();
        if (name.isBlank() || group.isBlank()) {
            lblStatus.setText("Статус: заполните ФИО и группу");
            return;
        }
        studentList.add(new Student(name, group, course));
        lblStatus.setText("Статус: данные сохранены: " + name + ", " + group + ", " + course);
        txtFullName.clear();
        txtGroup.clear();
    }

    @FXML
    private void onClearClick() {
        txtFullName.clear();
        txtGroup.clear();
        cmbCourse.getSelectionModel().selectFirst();

        // Очищаем весь список студентов в таблице
        studentList.clear();

        lblStatus.setText("Статус: форма и список очищены");
        txtFullName.requestFocus();
    }
}