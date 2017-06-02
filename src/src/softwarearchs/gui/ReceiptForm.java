package softwarearchs.gui;

import org.jdatepicker.JDatePanel;
import softwarearchs.Main;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.facade.Facade;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.Client;
import softwarearchs.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by zorin on 27.05.2017.
 */
public class ReceiptForm extends JFrame {
    private JPanel rootPanel;
    private JList receipts;
    private JTextField receiptNumber;
    private JTextField receiptDate;
    private JComboBox repairType;
    private JTextField deviceSerial;
    private JTextField deviceType;
    private JTextField deviceBrand;
    private JTextField deviceModel;
    private JTextField devicePurchaseDate;
    private JTextField deviceWarrantyExpiration;
    private JTextField devicePreviousRepair;
    private JTextField deviceRepairWarrantyExpiration;
    private JTextField clientName;
    private JTextField clientSurname;
    private JTextField clientPatronymic;
    private JTextField clientPhoneNumber;
    private JTextField clientEmail;
    private JTextArea deviceMalfunction;
    private JTextArea deviceNote;
    private JTextField receiver;
    private JTextField master;
    private JComboBox receiptStatus;
    private JButton exitButton;
    private JButton updateButton;
    private JButton newButton;
    private JButton UserInfoButton;

    private User currentUser;
    private Facade facade = Main.facade;

    public ReceiptForm(User user){
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        Dimension size = new Dimension(820, 470);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        //clearElements();
        setVisible(true);

        setupHandlers();
        receipts.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        if(repairType.getItemCount() == 0)
            for (RepairType type : RepairType.values())
                repairType.addItem(type);

        if(receiptStatus.getItemCount() == 0)
            for(ReceiptStatus status : ReceiptStatus.values())
                receiptStatus.addItem(status);

        currentUser = user;
        if("Receiver".equals(currentUser.getClass().getName())){
            newButton.setEnabled(true);
        }
    }

    public void masterPermissions(){
        repairType.setEditable(true);
        receiptStatus.setEditable(true);
        master.setEditable(master.getText().isEmpty());
    }

    public void receiverPermissions(){
        repairType.setEditable(true);
        deviceSerial.setEditable(true);
        deviceType.setEditable(true);
        deviceBrand.setEditable(true);
        deviceModel.setEditable(true);
        devicePurchaseDate.setEditable(true);
        deviceWarrantyExpiration.setEditable(true);
        devicePreviousRepair.setEditable(true);
        deviceRepairWarrantyExpiration.setEditable(true);
        clientName.setEditable(true);
        clientSurname.setEditable(true);
        clientPatronymic.setEditable(true);
        clientPhoneNumber.setEditable(true);
        clientEmail.setEditable(true);
        deviceMalfunction.setEnabled(true);
        deviceNote.setEnabled(true);
        receiptStatus.setEnabled(true);
    }

    public void clearElements(){
        SimpleDateFormat dt = new SimpleDateFormat("yyddMM");
        SimpleDateFormat dt1 = new SimpleDateFormat("hhmmss");
        Date today = new Date();
        receiptNumber.setText(dt.format(today) + dt1.format(today) + currentUser.getId());
        receiptDate.setText(today.toString());
        repairType.setSelectedIndex(-1);
        deviceSerial.setText("");
        deviceType.setText("");
        deviceBrand.setText("");
        deviceModel.setText("");
        devicePurchaseDate.setText("");
        deviceWarrantyExpiration.setText("");
        devicePreviousRepair.setText("");
        deviceRepairWarrantyExpiration.setText("");
        clientName.setText("");
        clientSurname.setText("");
        clientPatronymic.setText("");
        clientPhoneNumber.setText("");
        clientEmail.setText("");
        deviceMalfunction.setText("");
        deviceNote.setText("");
        receiptStatus.setSelectedIndex(-1);
        master.setText("");
        receiver.setText(currentUser.getName() + " " + currentUser.getSurname() + " " + currentUser.getPatronymic());
    }

    public boolean receiptAdded(){
        int receiptNumberText = Integer.parseInt(receiptNumber.getText());
        String receiptDateDate = receiptDate.getText();
        return false;
    }

    public void setupHandlers(){
        ReceiptForm thisFrame = this;
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Main.showSignIn();
                Main.closeFrame(thisFrame);
            }});

        newButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if("New".equals(newButton.getText())) {
                    clearElements();
                    receiverPermissions();
                    newButton.setText("Add receipt");
                }
                if("Add receipt".equals(newButton.getText())){

                }

            }});
    }

    public void masterSingedIn(){}

    public void clientSignedIn(){
        List<Receipt> clientReceipt = facade.getByClient((Client) currentUser);
    }

    public void receiverSignedIn(){}
}
