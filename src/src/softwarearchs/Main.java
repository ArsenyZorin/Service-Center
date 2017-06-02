package softwarearchs;

import oracle.jrockit.jfr.JFR;
import softwarearchs.facade.Facade;
import softwarearchs.gui.Login;
import softwarearchs.gui.ReceiptForm;
import softwarearchs.user.User;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zorin on 29.05.2017.
 */
public class Main {
    public static Facade facade = new Facade();

    public static void main(String[] args) {
        showSignIn();
    }

    public static void showSignIn(){
        Login login = new Login();
        login.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void showReceiptForm(User user){
        ReceiptForm receiptForm = new ReceiptForm(user);
        receiptForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void closeFrame(JFrame frame){
        //frame.setVisible(false);
        frame.dispose();
    }
}

