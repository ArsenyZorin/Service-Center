package softwarearchs.gui;

import softwarearchs.Main;
import softwarearchs.facade.Facade;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Created by zorin on 27.05.2017.
 */
public class UserInfo extends JFrame{
    private JPanel rootPanel;
    private JTable usersTable;
    private JTextField userName;
    private JTextField userSurname;
    private JTextField userPhoneNumber;
    private JTextField userPatronymic;
    private JTextField userEmail;
    private JTextField userLogin;
    private JButton newButton;
    private JButton exitButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField userRole;
    private JPasswordField userPassword;
    private JPasswordField repeatUserPassword;

    private Facade facade = Main.facade;
    private User currentUser;
    private boolean window;
    private AbstractMap<String, User> users;
    private int clickedRow;

    public UserInfo(User currentUser, boolean window){
        this.currentUser = currentUser;
        this.window = window;

        updateButton.setEnabled(false);

        if(!"Receiver".equals(currentUser.getClass().getSimpleName())){
            updateButton.setEnabled(false);
            newButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        fillTable();
        setInfo();
        setHandlers();
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void setHandlers(){
        JFrame currentFrame = this;

        exitButton.addActionListener(ev -> {
            if(window)
                Main.showReceiptForm(currentUser);
            Main.closeFrame(currentFrame);
        });

        updateButton.addActionListener(ev -> {
            if(userName.getText().isEmpty() || userSurname.getText().isEmpty() ||
                    userPatronymic.getText().isEmpty() || userEmail.getText().isEmpty() ||
                    userEmail.getText().isEmpty() || userPhoneNumber.getText().isEmpty() ||
                    userLogin.getText().isEmpty()) {
                Main.showErrorMessage("Fill all fields to update user");
                return;
            }
            User user = facade.getUser(userLogin.getText());
            user.setName(userName.getText());
            user.setSurname(userSurname.getText());
            user.setPatronymic(userPatronymic.getText());
            user.seteMail(userEmail.getText());
            user.setPhoneNumber(userPhoneNumber.getText());
            user.setLogin(userLogin.getText());

            if(!facade.updateUser(user)){
                Main.showErrorMessage("User update failed");
                return;
            }
            users.replace(user.getLogin(), user);
            replaceRow(user, clickedRow);
        });

        newButton.addActionListener(ev -> {
            if("New".equals(newButton.getText())) {
                newButton.setText("Add user");

                userName.setText("");
                userSurname.setText("");
                userPatronymic.setText("");
                userEmail.setText("");
                userPhoneNumber.setText("");
                userLogin.setText("");
                userRole.setText("");

                return;
            }
            if("Add user".equals(newButton.getText())){
                addUser();
                newButton.setText("New");
                return;
            }
        });

        deleteButton.addActionListener(ev -> {

        });

        usersTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickedRow = usersTable.rowAtPoint(e.getPoint());
                TableModel model = usersTable.getModel();

                User selectedUser = users.get(model.getValueAt(clickedRow, 0));
                selectedUserInfo(selectedUser);
                updateButton.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void setInfo(){
        userName.setText(currentUser.getName());
        userSurname.setText(currentUser.getSurname());
        userPatronymic.setText(currentUser.getPatronymic());
        userEmail.setText(currentUser.geteMail());
        userPhoneNumber.setText(currentUser.getPhoneNumber());
        userLogin.setText(currentUser.getLogin());
        userRole.setText(currentUser.getClass().getSimpleName());
    }

    private void addUser(){
        if(userName.getText().isEmpty() || userSurname.getText().isEmpty() ||
                userEmail.getText().isEmpty() || userPhoneNumber.getText().isEmpty() ||
                userLogin.getText().isEmpty() || userRole.getText().isEmpty() ||
                userPassword.getPassword().length == 0 || repeatUserPassword.getPassword().length == 0){
            Main.showErrorMessage("Fill in all fields");
            return;
        }

        if(!new String(userPassword.getPassword()).equals(new String(repeatUserPassword.getPassword()))){
            Main.showErrorMessage("Different passwords");
            return;
        }

        User user = null;
        switch (userRole.getText()){
            case "Receiver":
                user = new Receiver(userName.getText(), userSurname.getText(),
                        userPatronymic.getText(), userLogin.getText());
                break;
            case "Master":
                user = new Master(userName.getText(), userSurname.getText(),
                        userPatronymic.getText(), userLogin.getText());
                break;
            case "Client":
                user = new Client(userName.getText(), userSurname.getText(),
                        userPatronymic.getText(), userLogin.getText());
                break;
        }
        user.setPhoneNumber(userPhoneNumber.getText());
        user.seteMail(userEmail.getText());

        boolean added = Main.facade.addUser(user, new String(userPassword.getPassword()));

        if(!added)
            Main.showErrorMessage("Failed to add new user");

        System.out.println("addition");
        users.put(user.getLogin(), user);
        addTableRow(user);
        System.out.println("User was added");
    }

    private void selectedUserInfo(User selectedUser){
        userName.setText(selectedUser.getName());
        userSurname.setText(selectedUser.getSurname());
        userPatronymic.setText(selectedUser.getPatronymic());
        userEmail.setText(selectedUser.geteMail());
        userPhoneNumber.setText(selectedUser.getPhoneNumber());
        userLogin.setText(selectedUser.getLogin());
        userRole.setText(selectedUser.getClass().getSimpleName());
        userPassword.setText("");
        repeatUserPassword.setText("");
    }

    private void fillTable(){
        users = facade.getAllUsers();
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setColumnCount(3);
        Object[] cols = new Object[]{"Login", "User", "Role"};
        model.setColumnIdentifiers(cols);
        for(Map.Entry<String, User> user : users.entrySet())
            model.addRow(new Object[]{user.getKey(), user.getValue().getFIO(),
                    user.getValue().getClass().getSimpleName()});
        usersTable.setModel(model);
    }

    private void addTableRow(User user){
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.addRow(new Object[]{user.getLogin(), user.getFIO(), user.getClass().getSimpleName()});
        usersTable.setModel(model);
        usersTable.repaint();
    }

    private void replaceRow(User user, int row){
        TableModel model = usersTable.getModel();
        model.setValueAt(user.getLogin(), row, 0);
        model.setValueAt(user.getFIO(), row, 1);
        model.setValueAt(user.getClass().getSimpleName(), row, 2);
        usersTable.setModel(model);
        usersTable.repaint();
    }
}
