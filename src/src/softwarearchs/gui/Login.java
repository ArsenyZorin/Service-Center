package softwarearchs.gui;

import sun.rmi.runtime.Log;

import javax.swing.*;

/**
 * Created by zorin on 27.05.2017.
 */
public class Login extends JFrame{
    private JPanel rootPanel;
    private JButton signInButton;
    private JPasswordField userPassword;
    private JTextField userLogin;

    public Login(){
        setContentPane(rootPanel);
        setVisible(true);
    }
}
