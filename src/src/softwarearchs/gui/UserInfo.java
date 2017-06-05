package softwarearchs.gui;

import softwarearchs.Main;
import softwarearchs.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    User currentUser;

    public UserInfo(User currentUser){
        this.currentUser = currentUser;

        if(!"Receiver".equals(currentUser.getClass().getSimpleName())){
            updateButton.setEnabled(false);
            newButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        setInfo();
        setHandlers();
        setContentPane(rootPanel);
        setVisible(true);
    }

    private void setHandlers(){
        JFrame currentFrame = this;

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Main.showReceiptForm(currentUser);
                Main.closeFrame(currentFrame);
            }
        });

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
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
                    return;
                }
            }


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
            JOptionPane.showMessageDialog(new JFrame(),
                    "Fill in all fields", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(userPassword.getPassword() != repeatUserPassword.getPassword()){
            JOptionPane.showMessageDialog(new JFrame(),
                    "Different passwords", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean added = Main.facade.addUser(userName.getText(), userSurname.getText(),
                userPatronymic.getText(), userPhoneNumber.getText(), userEmail.getText(),
                userLogin.getText(), userRole.getText(), new String(userPassword.getPassword()));

        if(!added)
            JOptionPane.showMessageDialog(new JFrame(),
                    "Failed to add new user", "Error",
                    JOptionPane.ERROR_MESSAGE);
    }
}
