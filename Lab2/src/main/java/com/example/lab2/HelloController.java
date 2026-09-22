package com.example.lab2;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtSpeciality;

    @FXML
    private ComboBox<String> cmbCourse;

    @FXML
    private Label lblResult;

    @FXML
    public void initialize() {
        if (cmbCourse != null) {
            cmbCourse.getItems().addAll("1 курс", "2 курс", "3 курс", "4 курс", "5 курс");
        }
    }

    @FXML
    protected void onBuildClick() {
        String name = txtName.getText();
        String surname = txtSurname.getText();
        String age = txtAge.getText();
        String spec = txtSpeciality.getText();
        String course = cmbCourse.getValue();

        if (course == null) course = "не выбран";

        lblResult.setText(String.format("Студент: %s %s | Возраст: %s | Специальность: %s | Курс: %s",
                surname, name, age, spec, course));
    }

    @FXML
    protected void onClearClick() {
        txtName.clear();
        txtSurname.clear();
        txtAge.clear();
        txtSpeciality.clear();
        cmbCourse.setValue(null);
        lblResult.setText("Результат:");
    }

    @FXML
    protected void onExitClick() {
        Platform.exit();
    }
}