package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentGUI extends JFrame {
    private final Student student;
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JTabbedPane tab_contents;
    private JButton btn_log_out;
    private JTextField fld_patikaName_srch;
    private JButton btn_search_patika;
    private JTable tbl_patika_list;

    private JTextField fld_patika_ID;
    private JTextField fld_course_name;
    private JButton btn_register;
    private JTable tbl_registered_courses_list;
    private JTable tbl_content_list;
    private JComboBox cmb_select_course;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private Course course;
    private DefaultTableModel  mdl_registeredCourse_list;
    private Object[] row_registeredCourse_list;
    private JPopupMenu contentMenu;

    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;

    private int selectedCourse;

    public StudentGUI(Student student) {
        this.student = student;
        this.selectedCourse = -1;

        add(wrapper);
        setSize(700, 500);
        setLocation(Helper.screenCenterLoc("x", getSize()), Helper.screenCenterLoc("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        lbl_welcome.setText("Welcome " + student.getName());

        //patika table
        mdl_patika_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_patika_list = {"ID", "Patika Name", "Course Name", "Course ID"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];

        loadPatikaListModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);


        //course table
        mdl_registeredCourse_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_course_list = {"ID", "Course Name", "Patika Name"};
        mdl_registeredCourse_list.setColumnIdentifiers(col_course_list);
        row_registeredCourse_list = new Object[col_course_list.length];

       loadRegisteredCourseListModel();

        tbl_registered_courses_list.setModel(mdl_registeredCourse_list);
        tbl_registered_courses_list.getTableHeader().setReorderingAllowed(false);

        // contents
        contentMenu = new JPopupMenu();
        JMenuItem viewContentMenu = new JMenuItem("View Content");
        JMenuItem solveQuizMenu = new JMenuItem("Solve Quiz");
        contentMenu.add(viewContentMenu);
        contentMenu.add(solveQuizMenu);

        loadCoursesCombo();
        String[] headerContent = {"ID", "Title", "Description", "Youtube Link", "Course ID", "Course Name"};
        mdl_content_list = new DefaultTableModel(headerContent, 0);
        mdl_content_list.setColumnIdentifiers(headerContent);
        row_content_list = new Object[headerContent.length];

        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.setComponentPopupMenu(contentMenu);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);


        tbl_patika_list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    String selected_patika_id = tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString();
                    fld_patika_ID.setText(selected_patika_id);
                    String selected_course_name = tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 2).toString();
                    fld_course_name.setText(selected_course_name);
                    int selectedCourse = (int) tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 3);
                    setSelectedCourse(selectedCourse);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        });

        btn_search_patika.addActionListener(e -> {
            String name = fld_patikaName_srch.getText();
            ArrayList<Patika> patikaList = Patika.searchPatika(name);

            ArrayList<Course> searchedCourses = new ArrayList<>();

            for (Patika patika : patikaList) {
                int patikaId = patika.getId();
                ArrayList<Course> patikaCourses = Course.getListByPatikaId(patikaId);

                for (Course course : patikaCourses) {
                    searchedCourses.add(course);
                }
            }

            loadPatikaModel(searchedCourses);


        });

        btn_log_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGUI login = new LoginGUI();
            }
        });

        btn_register.addActionListener(e -> {
            this.addRegistration();
            loadRegisteredCourseListModel();

        });

        viewContentMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                Content content = Content.getFetch(select_id);
                String courseName = content.getCourseName();
                ViewContentGUI viewContentGUI = new ViewContentGUI(content, courseName);

                viewContentGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadPatikaListModel();
                        loadRegisteredCourseListModel();
                    }
                });
            }
        });

        solveQuizMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select_id = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                Content content = Content.getFetch(select_id);
                ArrayList<Quiz> quizes = Content.getQuizes(content.getId());
                Quiz firstQuiz = quizes.get(0);
                SolveQuizGUI solveQuizGUI = new SolveQuizGUI(firstQuiz.getQuizQuestion(), firstQuiz.getAnswer());

                solveQuizGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadPatikaListModel();
                        loadRegisteredCourseListModel();
                    }
                });
            }
        });


        cmb_select_course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item courseItem = (Item) cmb_select_course.getSelectedItem();
                String courseName = courseItem.getValue();
                int courseId = courseItem.getKey();

                ArrayList<Content> contentList = Content.getList(courseId);


                loadContentModel(contentList);
            }
        });


    }

    public ArrayList<Course> getRegisteredCourses(){
        ArrayList<Registration> registrationList = new ArrayList<>();
        Registration obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM registration WHERE student_id = " + this.student.getId());
            while (rs.next()) {
                int id = rs.getInt("id");
                int studentID = rs.getInt("student_id");
                int courseID = rs.getInt("course_id");
                obj = new Registration(id, studentID, courseID);
                registrationList.add(obj);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }


        ArrayList<Course> registeredCoursesList = new ArrayList<>();

        for(Registration reg: registrationList) {
            int courseId = reg.getCourse_id();
            Course course = Course.fetchById(courseId);
            registeredCoursesList.add(course);
        }
        return registeredCoursesList;
    }

    private void loadRegisteredCourseListModel() {
        ArrayList<Course> registeredCoursesList = this.getRegisteredCourses();
        DefaultTableModel clearModel = (DefaultTableModel) tbl_registered_courses_list.getModel();
        clearModel.setRowCount(0);


        for (Course course : registeredCoursesList) {
            int i = 0;
            int patikaId = course.getPatika_id();
            String patikaName = Patika.fetchById(patikaId).getName();

            row_registeredCourse_list[i++] = patikaId;
            row_registeredCourse_list[i++] = course.getName();
            row_registeredCourse_list[i++] = patikaName;
            mdl_registeredCourse_list.addRow(row_registeredCourse_list);

        }
    }

    public boolean addRegistration() {
        String query = "INSERT INTO registration (student_id, course_id) VALUES (?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, this.student.getId());
            pr.setInt(2, this.getSelectedCourse());
            Helper.showMessage("done");
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void loadPatikaListModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);

        for (Course obj : Course.getList()) {
            int i = 0;
            int patikaId = obj.getPatika_id();
            String patikaName = Patika.fetchById(patikaId).getName();

            row_patika_list[i++] = patikaId;
            row_patika_list[i++] = patikaName;
            row_patika_list[i++] = obj.getName();
            row_patika_list[i++] = obj.getId();
            mdl_patika_list.addRow(row_patika_list);

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

    public void loadPatikaModel(ArrayList<Course> courseList) {

        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);

        for (Course obj : courseList) {
            int i = 0;
            int patikaId = obj.getPatika_id();
            String patikaName = Patika.fetchById(patikaId).getName();

            row_patika_list[i++] = patikaId;
            row_patika_list[i++] = patikaName;
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }




    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public int getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(int selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
    private void loadCoursesCombo() {
        cmb_select_course.removeAllItems();
        cmb_select_course.addItem(new Item(-1, "Select"));
        for(Course obj : this.getRegisteredCourses()){
            cmb_select_course.addItem(new Item(obj.getId(), obj.getName()));
        }
    }
}
