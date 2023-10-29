package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducatorGUI extends JFrame {
    private final Educator educator;
    private JPanel wrapper;
    private JPanel pnl_top;
    private JButton btn_sign_out;
    private JLabel lbl_welcome;
    private JTabbedPane tab_courses;
    private JTable tbl_course_list;
    private JPanel pnl_bottom;
    private JPanel pnl_content_list;
    private JTable tbl_content_list;
    private JTextField fld_content_title_srch;
    private JComboBox cmb_courses_srch;
    private JButton btn_search;
    private JButton btn_add_content;
    private JTable tble_quiz_list;
    private JComboBox cmb_content_name;
    private JComboBox cmb_course_name;
    private JTextField fld_quiz_question;
    private JTextField fld_answer;
    private JButton btn_add_question;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private DefaultTableModel mdl_content_list;
    private Course selectedCourse;
    private Object[] row_content_list;
    private JPopupMenu contentMenu;
    private DefaultTableModel mdl_quiz_list;
    private Object[]  row_quiz_list;


    public EducatorGUI(Educator educator) {
        this.educator = educator;
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        contentMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("update");
        JMenuItem deleteMenu = new JMenuItem("delete");
        contentMenu.add(updateMenu);
        contentMenu.add(deleteMenu);


        lbl_welcome.setText("Welcome " + educator.getName());


        btn_sign_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGUI login = new LoginGUI();
            }
        });


        String[] header = {"ID", "Course Name", "Programming Language", "Patika"};
        mdl_course_list = new DefaultTableModel(header, 0);
        mdl_course_list.setColumnIdentifiers(header);
        row_course_list = new Object[header.length];
        loadAssignedCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        //tbl_course_list.setComponentPopupMenu(courseMenu);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);

        //Contents
        String[] headerContent = {"ID", "Title", "Description", "Youtube Link", "Course ID", "Course Name"};
        mdl_content_list = new DefaultTableModel(headerContent, 0);
        mdl_content_list.setColumnIdentifiers(headerContent);
        row_content_list = new Object[headerContent.length];
        loadContentModel();

        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.setComponentPopupMenu(contentMenu);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);


        tbl_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_content_list.rowAtPoint(point);
                tbl_content_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });

        loadCoursesCombo();

        //quiz list
        String[] headerQuizContent = {"ID", "Quiz Question", "Answer", "Content Title"};
        mdl_quiz_list = new DefaultTableModel(headerQuizContent, 0);
        mdl_quiz_list.setColumnIdentifiers(headerQuizContent);
        row_quiz_list = new Object[headerQuizContent.length];
        loadContentCombo();
        loadQuizModel();


        tble_quiz_list.setModel(mdl_quiz_list);
        tble_quiz_list.getTableHeader().setReorderingAllowed(false);
        tble_quiz_list.getColumnModel().getColumn(0).setMaxWidth(75);

        //##quiz list


        btn_add_content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddContentGUI addContentGUI = new AddContentGUI(educator);

                addContentGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadAssignedCourseModel();
                        loadContentModel();
                        loadCoursesCombo();
                    }
                });

            }
        });

        updateMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                UpdateContentGUI updateGUI = new UpdateContentGUI(Content.getFetch(select_id));
                updateGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadAssignedCourseModel();
                        loadContentModel();
                        loadCoursesCombo();
                    }
                });
            }
        });

        deleteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int select_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                    if (Content.delete(select_id)) {
                        Helper.showMessage("done");
                        loadContentModel();
                        loadAssignedCourseModel();
                        loadCoursesCombo();


                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = fld_content_title_srch.getText();
                Item courseItem = (Item) cmb_courses_srch.getSelectedItem();
                String courseName = courseItem.getValue();

                String query = Content.searchQuery(title, courseName);
                ArrayList<Content> searchingContent = Content.searchContentList(query);
                loadContentModel(searchingContent);
            }
        });


        btn_add_question.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item contentItem = (Item) cmb_content_name.getSelectedItem();
                // Item courseItem = (Item) cmb_course_name.getSelectedItem();
                if(Helper.isFieldEmpty(fld_quiz_question) || Helper.isFieldEmpty(fld_answer)){
                    Helper.showMessage("fill");
                } else {
                    if(Quiz.add( fld_quiz_question.getText(), fld_answer.getText(),contentItem.getValue(), contentItem.getKey())){
                        Helper.showMessage("done");
                        loadContentCombo();
                        loadQuizModel();
                        fld_quiz_question.setText(null);
                        fld_answer.setText(null);
                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });
    }

    public void loadContentCombo() {
        cmb_content_name.removeAllItems();
        for(Content obj : Content.getList()) {
            cmb_content_name.addItem(new Item(obj.getId(), obj.getTitle()));
        }
    }


    private void loadCoursesCombo() {
        cmb_courses_srch.removeAllItems();
        for (Course obj : this.educator.getEducatorsCourses()) {
            cmb_courses_srch.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    private void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);

        String query = "SELECT * FROM course WHERE user_id = ?";
        ArrayList<String> courseNames = new ArrayList<>();
        ArrayList<Integer> courseIds = new ArrayList<>();

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, this.educator.getId());
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                courseNames.add(rs.getString(4));
                courseIds.add(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.getStackTrace();
        }

        for (int courseId : courseIds) {
            for (Content content : Content.getList(courseId)) {
                int i = 0;
                row_content_list[i++] = content.getId();
                row_content_list[i++] = content.getTitle();
                row_content_list[i++] = content.getDescription();
                row_content_list[i++] = content.getYoutubeLink();
                // row_content_list[i++] = content.getQuizQuestions();
                row_content_list[i++] = content.getCourseID();
                row_content_list[i++] = content.getCourseName();
                mdl_content_list.addRow(row_content_list);
            }
        }


    }

    private void loadContentModel(ArrayList<Content> contentList) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);

        for (Content content : contentList) {
            int i = 0;
            row_content_list[i++] = content.getId();
            row_content_list[i++] = content.getTitle();
            row_content_list[i++] = content.getDescription();
            row_content_list[i++] = content.getYoutubeLink();
            // row_content_list[i++] = content.getQuizQuestions();
            row_content_list[i++] = content.getCourseID();
            row_content_list[i++] = content.getCourseName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    private void loadAssignedCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);

        int i = 0;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatika().getName();
            if (obj.getEducator().getName().equals(this.educator.getName())) {
                mdl_course_list.addRow(row_course_list);
            }
        }

    }
   private void loadQuizModel(){
       DefaultTableModel clearModel = (DefaultTableModel) tble_quiz_list.getModel();
       clearModel.setRowCount(0);
       int i;

       for (Quiz obj : Quiz.getList()) {
           i = 0;
           row_quiz_list[i++] = obj.getId();
           row_quiz_list[i++] = obj.getQuizQuestion();
           row_quiz_list[i++] = obj.getAnswer();
           row_quiz_list[i++] = obj.getContentTitle();
           mdl_quiz_list.addRow(row_quiz_list);
       }
   }


    public static void main(String[] args) {
        Helper.setLayout();
        EducatorGUI educatorgui = new EducatorGUI(new Educator());

    }
}
