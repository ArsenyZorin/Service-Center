package softwarearchs.gui;

import softwarearchs.Main;
import softwarearchs.facade.Facade;
import softwarearchs.user.User;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zorin on 27.05.2017.
 */
public class Login extends JFrame{
    private JPanel rootPanel;
    private JButton signInButton;
    private JPasswordField userPassword;
    private JTextField userLogin;

    private Facade facade = Main.facade;
    public Login() {
        Main.frameInit(this, rootPanel, 300, 150);
        setHandler();
        setVisible(true);
    }

    public void setHandler(){
        Login thisFrame = this;
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String login = userLogin.getText();
                String pwd = new String(userPassword.getPassword());
                if(login.isEmpty() || pwd.isEmpty()){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Enter login and password", "error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    if(facade.signIn(login, pwd)){
                        Main.currentUser = facade.getUser(login);
                        Main.closeFrame(thisFrame);
                        Main.showReceiptForm();
                    }
                    else{
                        JOptionPane.showMessageDialog(new JFrame(),
                                "Invalid login or password", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(new JFrame(),
                            e.getClass() + e.getMessage() + " " + e.getCause(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }});
    }
}
