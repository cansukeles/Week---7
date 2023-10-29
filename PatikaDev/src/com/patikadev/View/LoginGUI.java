package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_username;
    private JPasswordField fld_password;
    private JButton btn_login;
    private JButton btn_sign_up;

    public LoginGUI() {
        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)) {
                    Helper.showMessage("fill");
                } else {
                    User u = User.getFetch(fld_username.getText(), fld_password.getText());
                    if (u == null) {
                        Helper.showMessage("User not found!");

                    } else {
                        switch (u.getType()) {
                            case "operator":
                                OperatorGUI opGUI = new OperatorGUI((Operator) u);
                                break;
                            case "educator":
                                EducatorGUI edGUI = new EducatorGUI((Educator) u);
                                break;
                            case "student":
                                StudentGUI stGUI = new StudentGUI((Student) u);
                                break;
                        }
                        dispose();
                    }
                }
            }
        });

        //Redirect to the sign-up screen
        btn_sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpGUI signup = new SignUpGUI();
            }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
