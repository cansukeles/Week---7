package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Educator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddContentGUI extends JFrame {
    private final Educator educator;
    private JPanel wrapper;
    private JComboBox cmb_course_list;
    private JTextField fld_title;
    private JTextField fld_description;
    private JTextField fld_youtubeLink;
    private JButton bttn_add_content;
    Content content;



    public AddContentGUI(Educator educator) {
        this.educator = educator;
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        loadCoursesCombo();

       /* TransferHandler transferHandler = fld_description.getTransferHandler();
        int action = transferHandler.getSourceActions(fld_description);
        System.out.println(action); */


        bttn_add_content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_title) || Helper.isFieldEmpty(fld_description) || Helper.isFieldEmpty(fld_youtubeLink)) {
                    Helper.showMessage("fill");
                } else {
                    Item selectedItem = (Item) cmb_course_list.getSelectedItem();

                    int courseId = selectedItem.getKey();
                    String courseName = selectedItem.getValue();

                    Content.add(fld_title.getText(), fld_description.getText(), fld_youtubeLink.getText(), "", courseId, courseName);
                    Helper.showMessage("done");
                    dispose();


                }
            }
        });
    }



    private void loadCoursesCombo() {
        cmb_course_list.removeAllItems();
        for (Course obj : this.educator.getEducatorsCourses()) {
            cmb_course_list.addItem(new Item(obj.getId(), obj.getName()));
        }
    }


}
