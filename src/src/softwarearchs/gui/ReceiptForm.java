package softwarearchs.gui;

import javax.swing.*;

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

    public ReceiptForm(){
        setContentPane(rootPanel);
        setVisible(true);
    }
}
