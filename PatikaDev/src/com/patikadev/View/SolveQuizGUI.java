package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SolveQuizGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_question;
    private JTextField fld_answer;
    private JButton btn_submit;
    private String question;
    private String answer;

    public SolveQuizGUI(String question, String answer){
        this.question = question;
        this.answer = answer;

        add(wrapper);
        setSize(300, 200);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        fld_question.setText(this.question);

        btn_submit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(fld_answer.getText().equals(answer)){
                    Helper.showMessage("Correct answer!");
                } else {
                    Helper.showMessage("Wrong answer!");
                } dispose();
            }
        });
    }
}
