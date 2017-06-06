package softwarearchs;

import softwarearchs.facade.Facade;
import softwarearchs.gui.Login;
import softwarearchs.gui.ReceiptForm;
import softwarearchs.gui.UserInfo;
import softwarearchs.user.User;
import javax.swing.*;

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

    public static void showUsers(User user, boolean window){
        UserInfo userInfo = new UserInfo(user, window);
        userInfo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void closeFrame(JFrame frame){
        frame.dispose();
    }

    public static void showErrorMessage(String message){
        JOptionPane.showMessageDialog(new JFrame(),
                message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

