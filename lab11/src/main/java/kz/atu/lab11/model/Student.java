package kz.atu.lab11.model;

import javafx.beans.property.*;

/** Модель данных: ФИО, группа и средний балл (GPA) студента. */
public class Student {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty group = new SimpleStringProperty();
    private final DoubleProperty gpa = new SimpleDoubleProperty();

    public Student(String n, String g, double p) {
        setName(n);
        setGroup(g);
        setGpa(p);
    }

    public String getName() { return name.get(); }
    public void setName(String v) { name.set(v); }
    public StringProperty nameProperty() { return name; }

    public String getGroup() { return group.get(); }
    public void setGroup(String v) { group.set(v); }
    public StringProperty groupProperty() { return group; }

    public double getGpa() { return gpa.get(); }
    public void setGpa(double v) { gpa.set(v); }
    public DoubleProperty gpaProperty() { return gpa; }

    @Override
    public String toString() {
        return name.get() + " (" + group.get() + ", GPA " + gpa.get() + ")";
    }
}
