package softwarearchs;

import softwarearchs.facade.Facade;
import softwarearchs.gui.Login;
import softwarearchs.gui.ReceiptForm;
import softwarearchs.gui.UserInfo;
import softwarearchs.user.User;
import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void showInformationMessage(String message){
        JOptionPane.showMessageDialog(new JFrame(),
                message, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static Date dateFromString(String stringDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(stringDate);
        }catch (ParseException e){
            date = null;
        }
        return date;
    }

    public static String stringFromDate(Date date){
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(date);
    }

}

