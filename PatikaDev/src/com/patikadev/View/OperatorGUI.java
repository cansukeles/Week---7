package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane sc_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_username;
    private JPasswordField fld_user_pass;
    private JComboBox cmb_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_remove;
    private JTextField fld_srch_user_name;
    private JTextField fld_srch_user_uname;
    private JComboBox cmb_srch_user_type;
    private JButton btn_srch_user;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_user_top;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private JPopupMenu patikaMenu;

    //patika model
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;

        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterLoc("x", getSize());
        int y = Helper.screenCenterLoc("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Welcome " + operator.getName());


        //modelUserList
        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "Name Surname", "Username", "Password", "Membership Type"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        /* Object [] firstRow ={"1", "Cansu Keleş", "Cansu", "123", "operator"};
        mdl_user_list.addRow(firstRow); */

        row_user_list = new Object[col_user_list.length];

        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        //we can use lambda expression here because of anonymous class
        tbl_user_list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    String selected_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                    fld_user_id.setText(selected_user_id);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        });

        //Changing data from table
        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_username = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_username, user_password, user_type)) {
                    Helper.showMessage("done");

                }

                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });


        //Patika List
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Update");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        deleteMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.confirm("sure")) {
                    int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                    if (Patika.delete(select_id)) {
                        Helper.showMessage("done");
                        loadPatikaModel();
                        loadPatikaCombo();
                        loadCourseModel();
                    } else {
                        Helper.showMessage("error");
                    }
                }
            }
        });

        updateMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.fetchById(select_id));
                updateGUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loadPatikaModel();
                        loadPatikaCombo();
                        loadCourseModel();
                    }
                });

            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Name"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selectedRow, selectedRow);
            }

            // right click listener to select the row that is clicked by the mouse
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = tbl_patika_list.rowAtPoint(e.getPoint());
                if (r >= 0 && r < tbl_patika_list.getRowCount()) {
                    tbl_patika_list.setRowSelectionInterval(r, r);
                } else {
                    tbl_patika_list.clearSelection();
                }

                int rowindex = tbl_patika_list.getSelectedRow();

                if (rowindex < 0)
                    return;
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    tbl_patika_list.setRowSelectionInterval(rowindex, rowindex);
                }
            }
        });

        //##Patika List

        //Course List
        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Course Name", "Programming Language", "Patika", "Educator"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();

        //##Course List


        //we can use lambda expression here because of anonymous class
        btn_user_add.addActionListener(new ActionListener() {
            @Override
            //check if the field is empty or not
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_user_pass)) {
                    Helper.showMessage("fill");
                } else {
                    String name = fld_user_name.getText();
                    String username = fld_username.getText();
                    String password = fld_user_pass.getText();
                    String type = cmb_type.getSelectedItem().toString();
                    if (User.add(name, username, password, type)) {
                        Helper.showMessage("done");
                        loadUserModel();
                        loadEducatorCombo();
                        fld_user_name.setText(null);
                        fld_username.setText(null);
                        fld_user_pass.setText(null);

                    }
                }
            }
        });
        btn_user_remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_user_id)) {
                    Helper.showMessage("fill");
                } else {
                    if (Helper.confirm("sure")) {
                        int user_id = Integer.parseInt(fld_user_id.getText());
                        if (User.delete(user_id)) {
                            Helper.showMessage("done");
                            loadUserModel();
                            loadEducatorCombo();
                            loadCourseModel();
                            fld_user_id.setText(null);
                        } else {
                            Helper.showMessage("error");
                        }
                    }
                }
            }
        });

        //user search button
        btn_srch_user.addActionListener(e -> {
            String name = fld_srch_user_name.getText();
            String username = fld_srch_user_uname.getText();
            String type = cmb_srch_user_type.getSelectedItem().toString();
            String query = User.searchQuery(name, username, type);
            ArrayList<User> searchingUser = User.searchUserList(query);
            loadUserModel(searchingUser);
        });

        //dispose -- log out
        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGUI login = new LoginGUI();
            }
        });

        //patika add button
        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)) {
                Helper.showMessage("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);
                } else {
                    Helper.showMessage("error");
                }
            }

        });

        //course add
        btn_course_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
                Item userItem = (Item) cmb_course_user.getSelectedItem();
                if (Helper.isFieldEmpty(fld_course_name) || Helper.isFieldEmpty(fld_course_lang)) {
                    Helper.showMessage("fill");
                } else {
                    if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())) {
                        Helper.showMessage("done");
                        loadCourseModel();
                        fld_course_lang.setText(null);
                        fld_course_name.setText(null);
                    } else {
                        Helper.showMessage("error");
                    }
                }

            }
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);

        int i = 0;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    //we need to use an ArrayList for patika list (creating into patika class)
    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;

        for (Patika obj : Patika.getList()) {
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);

        }
    }

    public void loadUserModel() {

        //to see whole users in the table(but same value don't repeat 2 times)
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getList()) {

            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list) {

        //to see whole users in the table(but same value don't repeat 2 times)
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list) {

            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducatorCombo() {
        cmb_course_user.removeAllItems();
        for (User obj : User.getList()) {
            if (obj.getType().equals("educator")) {
                cmb_course_user.addItem(new Item(obj.getId(), obj.getName()));
            }
        }
    }

    public static void main(String[] args) {
        //Layout
        Helper.setLayout();
        Operator operator1 = new Operator();
        operator1.setId(1);
        operator1.setName("Cansu Keleş");
        operator1.setUsername("Cansu");
        operator1.setPassword("1234");
        operator1.setType("operator");

        OperatorGUI opGUI = new OperatorGUI(operator1);
    }


}
