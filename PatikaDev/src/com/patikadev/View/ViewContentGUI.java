package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewContentGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_title;
    private JTextArea text_dscrptn;
    private JTextField fld_youtb_link;
    private JComboBox cmb_rating;
    private JButton btn_save;
    private JLabel lbl_course_content;
    private Content content;
    private String courseName;

    public ViewContentGUI(Content content, String courseName){
        this.content = content;
        this.courseName = courseName;
        add(wrapper);
        setSize(500, 400);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        setResizable(false);

        lbl_course_content.setText(courseName + " Course Content");

        text_dscrptn.setLineWrap(true);
        fld_title.setText(content.getTitle());
        text_dscrptn.setText(content.getDescription());
        fld_youtb_link.setText(content.getYoutubeLink());


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Helper.showMessage("Your rating is saved.");
                dispose();
            }
        });
    }

}
