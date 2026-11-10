package kz.atu.lab11.service;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kz.atu.lab11.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** Сервисный слой: бизнес-правила и операции CRUD. Не зависит от элементов интерфейса. */
public class StudentService {

    // extractor: список сообщает об изменении полей студента,
    // поэтому FilteredList/SortedList пересчитываются после редактирования
    private final ObservableList<Student> students =
            FXCollections.observableArrayList(
                    s -> new Observable[]{s.nameProperty(), s.groupProperty(), s.gpaProperty()});

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void addStudent(String n, String g, double p) {
        validate(n, g, p);
        checkDuplicate(n, g, null);
        students.add(new Student(n.trim(), g.trim(), p));
    }

    public void updateStudent(Student s, String n, String g, double p) {
        if (s == null) throw new IllegalArgumentException("Студент не выбран");
        validate(n, g, p);
        checkDuplicate(n, g, s);
        s.setName(n.trim());
        s.setGroup(g.trim());
        s.setGpa(p);
    }

    public void deleteStudent(Student s) {
        if (s == null) throw new IllegalArgumentException("Студент не выбран");
        students.remove(s);
    }

    /** Список уникальных групп в алфавитном порядке (для динамического фильтра). */
    public List<String> getGroups() {
        return students.stream()
                .map(Student::getGroup)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /** Средний GPA всех студентов; для пустого списка возвращает 0. */
    public double getAverageGpa() {
        return averageGpa(students);
    }

    /** Средний GPA произвольной выборки (например, после фильтрации). */
    public double averageGpa(Collection<? extends Student> list) {
        return list.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
    }

    private void checkDuplicate(String n, String g, Student exclude) {
        String name = n.trim();
        String group = g.trim();
        for (Student st : students) {
            if (st != exclude
                    && st.getName().equalsIgnoreCase(name)
                    && st.getGroup().equalsIgnoreCase(group)) {
                throw new IllegalArgumentException("Такой студент уже есть в этой группе");
            }
        }
    }

    private void validate(String n, String g, double p) {
        if (n == null || n.isBlank()) throw new IllegalArgumentException("Введите ФИО");
        if (g == null || g.isBlank()) throw new IllegalArgumentException("Введите группу");
        if (!Double.isFinite(p) || p < 0 || p > 4)
            throw new IllegalArgumentException("GPA должен быть от 0 до 4");
    }
}
