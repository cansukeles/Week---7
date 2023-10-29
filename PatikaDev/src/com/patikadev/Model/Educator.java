package com.patikadev.Model;

import java.util.ArrayList;

public class Educator extends User{

    public Educator(){

    }
    public Educator(int id, String name, String username, String password, String type){

    }

    public ArrayList<Course> getEducatorsCourses(){
        ArrayList<Course> courses = new ArrayList<>();

        for (Course obj : Course.getList()) {
            if (obj.getEducator().getName().equals(this.getName())) {
                courses.add(obj);
            }
        }

        return courses;
    }

    public ArrayList<Content> getEducatorsContents(){
        ArrayList<Content> contents = new ArrayList<>();

       for(Course course : this.getEducatorsCourses()) {
           for(Content content: Content.getList(course.getId())) {
               contents.add(content);
           }
       }

        return contents;
    }
}
