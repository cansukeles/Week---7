package com.patikadev.Helper;

import javax.swing.*;
import java.awt.*;

public class Helper {

    //a static method for changing theme
    public static void setLayout() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Metal".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    //a static method for finding screen size and centering GUI
    public static int screenCenterLoc(String axis, Dimension size) {
        int point;
        switch (axis) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }
        return point;
    }

    public static boolean isFieldEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldEmpty(JTextArea textArea) {
        return textArea.getText().trim().isEmpty();
    }

    public static void showMessage(String str) {
        optionPaneMessage();
        String msg;
        String title;
        switch (str) {
            case "fill":
                msg = "Fill in all fields!";
                title = "Invalid Action";
                break;
            case "done":
                msg = "Successful transaction";
                title = "Result";
                break;
            case "error":
                msg = "Something went wrong";
                title = "Invalid Action";
                break;
            default:
                msg = str;
                title = "message";
        }

         JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str){
        String message;
        switch (str){
            case "sure":
                message = "Are you sure you want to delete?";
                break;
            default:
                message = str;
        }
        return JOptionPane.showConfirmDialog(null, message, "Is it your final decision?", JOptionPane.YES_NO_OPTION) == 0;


    }

    //Change Button message
    public static void optionPaneMessage(){
        UIManager.put("OptionPane.okButtonText", "OK");
    }
}
