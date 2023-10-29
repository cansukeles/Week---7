package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private String quizQuestion;
    private String answer;
    private String contentTitle;
    private Content content;


    public Quiz(int id, String quizQuestion, String answer, String contentTitle,int contentId){
        this.id = id;
        this.quizQuestion = quizQuestion;
        this.answer = answer;
        this.content = Content.getFetch(contentId);
        this.contentTitle = content.getTitle();






    }

    public static boolean add(String quizQuestion, String answer, String contentTitle, int contentId) {
        String query = "INSERT INTO quiz (quiz_question, answer, content_title, content_id) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, quizQuestion);
            pr.setString(2, answer);
            pr.setString(3, contentTitle);
            pr.setInt(4, contentId);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.getStackTrace();
        }

        String contentQuery = "INSERT INTO quiz (quiz_question, answer, content_title, content_id) VALUES (?,?,?,?)";

        return true;
    }
    public static ArrayList<Quiz> getList(){
        ArrayList<Quiz> quizList = new ArrayList<>();
        Quiz obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz");
            while(rs.next()){
                obj = new Quiz(rs.getInt("id"), rs.getString("quiz_question"), rs.getString("answer"),
                        rs.getString("content_title"),rs.getInt("content_id"));
                quizList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return quizList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }
}
