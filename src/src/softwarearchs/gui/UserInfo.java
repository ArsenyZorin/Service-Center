package softwarearchs.gui;

import javax.swing.*;

/**
 * Created by zorin on 27.05.2017.
 */
public class UserInfo extends JFrame{
    private JPanel rootPanel;
    private JList list1;
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

    public UserInfo(){
        setContentPane(rootPanel);
        setVisible(true);
    }
}
