package kz.atu.lab11.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import kz.atu.lab11.model.Student;
import kz.atu.lab11.service.StudentService;

import java.util.Locale;
import java.util.Optional;

/** Контроллер: только реакция на события интерфейса, вся логика — в StudentService. */
public class StudentController {

    private static final String ALL = "Все";

    @FXML private TableView<Student> tableStudents;
    @FXML private TableColumn<Student, String> colName, colGroup;
    @FXML private TableColumn<Student, Number> colGpa;
    @FXML private TextField txtName, txtGroup, txtGpa, txtSearch;
    @FXML private ComboBox<String> cmbGroupFilter;
    @FXML private Label lblCount, lblAvg;

    private final StudentService service = new StudentService();
    private FilteredList<Student> filtered;

    @FXML
    private void initialize() {
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colGroup.setCellValueFactory(c -> c.getValue().groupProperty());
        colGpa.setCellValueFactory(c -> c.getValue().gpaProperty());

        service.addStudent("Иванов Иван", "ИС-301", 3.5);
        service.addStudent("Петров Пётр", "ИС-302", 3.8);

        filtered = new FilteredList<>(service.getStudents(), s -> true);
        SortedList<Student> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableStudents.comparatorProperty());
        tableStudents.setItems(sorted);

        // счётчик и средний GPA обновляются при любом изменении отображаемого списка
        filtered.addListener((ListChangeListener<Student>) c -> updateStats());

        refreshGroups();

        txtSearch.textProperty().addListener((o, a, b) -> applyFilter());
        cmbGroupFilter.valueProperty().addListener((o, a, b) -> applyFilter());

        // при выборе строки поля формы заполняются данными студента
        tableStudents.getSelectionModel().selectedItemProperty().addListener((o, old, s) -> {
            if (s != null) {
                txtName.setText(s.getName());
                txtGroup.setText(s.getGroup());
                txtGpa.setText(String.valueOf(s.getGpa()));
            }
        });

        applyFilter();
    }

    // ---------- поиск, фильтрация, статистика ----------

    private void applyFilter() {
        String q = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        String group = cmbGroupFilter.getValue();
        filtered.setPredicate(s -> s.getName().toLowerCase().contains(q)
                && (group == null || group.equals(ALL) || s.getGroup().equals(group)));
        updateStats();
    }

    private void updateStats() {
        lblCount.setText("Найдено студентов: " + filtered.size());
        lblAvg.setText(String.format(Locale.ROOT, "Средний GPA: %.2f",
                service.averageGpa(filtered)));
    }

    /** Динамический список групп строится из данных студентов. */
    private void refreshGroups() {
        String current = cmbGroupFilter.getValue();
        cmbGroupFilter.getItems().setAll(ALL);
        cmbGroupFilter.getItems().addAll(service.getGroups());
        cmbGroupFilter.setValue(
                current != null && cmbGroupFilter.getItems().contains(current) ? current : ALL);
    }

    // ---------- CRUD ----------

    private double readGpa() {
        try {
            return Double.parseDouble(txtGpa.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Введите числовой GPA");
        }
    }

    @FXML
    private void onAdd() {
        try {
            service.addStudent(txtName.getText(), txtGroup.getText(), readGpa());
            refreshGroups();
            applyFilter();
            clearFields();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        try {
            Student s = tableStudents.getSelectionModel().getSelectedItem();
            service.updateStudent(s, txtName.getText(), txtGroup.getText(), readGpa());
            refreshGroups();
            applyFilter();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        try {
            Student s = tableStudents.getSelectionModel().getSelectedItem();
            if (s != null && !confirmDelete(s)) {
                return;
            }
            service.deleteStudent(s);          // для s == null сервис выбросит исключение
            refreshGroups();
            applyFilter();
            clearFields();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // ---------- вспомогательные методы ----------

    private void clearFields() {
        txtName.clear();
        txtGroup.clear();
        txtGpa.clear();
        tableStudents.getSelectionModel().clearSelection();
    }

    private boolean confirmDelete(Student s) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Подтверждение");
        a.setHeaderText(null);
        a.setContentText("Удалить студента «" + s.getName() + "»?");
        Optional<ButtonType> result = a.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showError(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Ошибка");
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}
