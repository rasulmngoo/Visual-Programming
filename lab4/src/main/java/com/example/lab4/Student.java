package com.example.lab4;

public class Student {
    private String fullName;
    private String group;
    private String course;

    public Student(String fullName, String group, String course) {
        this.fullName = fullName;
        this.group = group;
        this.course = course;
    }

    public String getFullName() { return fullName; }
    public String getGroup() { return group; }
    public String getCourse() { return course; }
}