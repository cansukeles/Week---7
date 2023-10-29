package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateContentGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_title_update;
    private JTextArea text_dscrp_update;
    private JTextArea text_youtube_update;
    private JButton bttn_update;
    private Content content;

    public UpdateContentGUI(Content content) {
        this.content = content;
        add(wrapper);
        setSize(500, 400);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        setResizable(false);

        // fld_patika_name.setText(patika.getName());
        text_dscrp_update.setLineWrap(true);
        //text_dscrp_update.setColumns(10);

        fld_title_update.setText(content.getTitle());
        text_dscrp_update.setText(content.getDescription());
        text_youtube_update.setText(content.getYoutubeLink());


        bttn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_title_update) || Helper.isFieldEmpty(text_dscrp_update) || Helper.isFieldEmpty(text_youtube_update)) {
                    Helper.showMessage("fill");
                } else {
                    if (Content.update(content.getId(), fld_title_update.getText(), text_dscrp_update.getText(), text_youtube_update.getText())) {
                        Helper.showMessage("done");
                    }
                    dispose();
                }
            }
        });
    }


}
