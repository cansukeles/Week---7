package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String title;
    private String description;
    private String youtubeLink;
    private String quizQuestions;
    private int courseID;
    private Course course;
    private String courseName;
    private JPopupMenu contentMenu;
    private Content content;

    public Content() {

    }


    public Content(int id, String title, String description, String youtubeLink, String quizQuestions, int courseID, String courseName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.youtubeLink = youtubeLink;
        //this.quizQuestions = quizQuestions.split(", ");
        this.quizQuestions = quizQuestions;
        this.courseID = courseID;
        this.course = Course.fetchById(courseID);
        this.courseName = courseName;


    }

    public static boolean add(String title, String description, String youtubeLink, String quizQuestions, int courseID, String courseName) {
        String query = "INSERT INTO content(title, description, youtube_link, quiz_questions, course_id, course_name) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, title);
            pr.setString(2, description);
            pr.setString(3, youtubeLink);
            pr.setString(4, quizQuestions);
            pr.setInt(5, courseID);
            pr.setString(6, courseName);

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean update(int id, String title, String description, String youtubeLink) {
        String query = "UPDATE content SET title = ?, description = ?, youtube_link = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, title);
            pr.setString(2, description);
            pr.setString(3, youtubeLink);
            pr.setInt(4, id);

            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM content WHERE id = ?";
       /* ArrayList<Content> contentList = Content.getList();
        for(Content obj : contentList){
            if(obj.getContent().getId() == id){
                Content.delete(obj.getId());
            }
        } */
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return true;
    }


    public static Content getFetch(int id) {
        Content obj = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new Content(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("youtube_link"), rs.getString("quiz_questions"), rs.getInt("course_id"), rs.getString("course_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

    public static ArrayList<Content> getList(int courseId) {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT * FROM content WHERE course_id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, courseId);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String youtubeLink = rs.getString("youtube_link");
                String quizQuestions = rs.getString("quiz_questions");
                String courseName = rs.getString("course_name");

                contentList.add(new Content(id, title, description, youtubeLink, quizQuestions, course_id, courseName));

            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return contentList;
    }

    public static ArrayList<Content> getList() {
        ArrayList<Content> contentList = new ArrayList<>();
        String query = "SELECT * FROM content";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String youtubeLink = rs.getString("youtube_link");
                String quizQuestions = rs.getString("quiz_questions");
                String courseName = rs.getString("course_name");

                contentList.add(new Content(id, title, description, youtubeLink, quizQuestions, course_id, courseName));

            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return contentList;
    }

    public static String searchQuery(String title, String courseName) {
        String query = "SELECT * FROM content WHERE title LIKE '%{{title}}%'";
        query = query.replace("{{title}}", title);
        if (!courseName.isEmpty()) {
            query += "AND course_name ='{{courseName}}'";
            query = query.replace("{{courseName}}", courseName);
        }
        return query;
    }

    public static ArrayList<Content> searchContentList(String query) {
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setTitle(rs.getString("title"));
                obj.setDescription(rs.getString("description"));
                obj.setYoutubeLink(rs.getString("youtube_link"));
                obj.setQuizQuestions(rs.getString("quiz_questions"));
                obj.setCourseID(rs.getInt("course_id"));
                obj.setCourseName(rs.getString("course_name"));

                contentList.add(obj);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contentList;
    }

    public static ArrayList<Quiz> getQuizes(int contentId) {
        ArrayList<Quiz> quizList = new ArrayList<>();
        Quiz obj;

        String query = "SELECT * FROM quiz WHERE content_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, contentId);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {

                obj = new Quiz(rs.getInt("id"), rs.getString("quiz_question"),
                        rs.getString("answer"), rs.getString("content_title"),
                        rs.getInt("content_id"));

                quizList.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return quizList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(String quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    private Content getContent() {
        return content;
    }
}
