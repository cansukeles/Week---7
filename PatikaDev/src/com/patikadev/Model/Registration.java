package com.patikadev.Model;

public class Registration {
    private int course_id;
    private int student_id;
    private int id;

    public Registration() {

    }

    public Registration(int id, int student_id, int course_id) {
        this.id = id;
        this.course_id = course_id;
        this.student_id = student_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

