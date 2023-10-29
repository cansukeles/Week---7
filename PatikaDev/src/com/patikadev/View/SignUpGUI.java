package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wbottom;
    private JPanel wtop;
    private JTextField fld_name_surname;
    private JTextField fld_username;
    private JPasswordField fld_password;
    private JButton btn_sign_up;
    private JButton bttn_s_login;

    public SignUpGUI(){
        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        //sign up button
        btn_sign_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Helper.isFieldEmpty(fld_name_surname) || Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)){
                    Helper.showMessage("fill");
                } else {
                    String name = fld_name_surname.getText();
                    String username = fld_username.getText();
                    String password = fld_password.getText();
                    String type = "student";
                    if(User.add(name, username, password, type)){
                        Helper.showMessage("done");
                        fld_name_surname.setText(null);
                        fld_username.setText(null);
                        fld_password.setText(null);
                    }
                }

            }
        });
        bttn_s_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI login = new LoginGUI();
            }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        SignUpGUI signup = new SignUpGUI();

    }
}
