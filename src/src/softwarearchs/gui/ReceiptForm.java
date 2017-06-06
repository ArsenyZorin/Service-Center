package softwarearchs.gui;

import softwarearchs.Main;
import softwarearchs.additional.Device;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.facade.Facade;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by zorin on 27.05.2017.
 */
public class ReceiptForm extends JFrame {
    private JPanel rootPanel;
    private JTable receipts;
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
    private JButton userInfoButton;
    private JButton findDevice;
    private JButton findClient;


    private User currentUser;
    private Facade facade = Main.facade;
    private AbstractMap<String, Receipt> receiptList = new HashMap<>();

    public ReceiptForm(User user){
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
        Dimension size = new Dimension(820, 470);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setVisible(true);

        if(repairType.getItemCount() == 0)
            for (RepairType type : RepairType.values())
                repairType.addItem(type);

        if(receiptStatus.getItemCount() == 0)
            for(ReceiptStatus status : ReceiptStatus.values())
                receiptStatus.addItem(status);

        currentUser = user;
        if("Receiver".equals(currentUser.getClass().getSimpleName())){
            receiverSignedIn();
        }
        else if("Master".equals(currentUser.getClass().getSimpleName())){
            masterSingedIn();
        }
        else
            clientSignedIn();

        setupHandlers();
    }

    public void masterPermissions(boolean state){
        repairType.setEditable(state);
        receiptStatus.setEditable(state);
        master.setEditable(master.getText().isEmpty());
    }

    private void receiverPermissions(boolean state){
        repairType.setEditable(state);
        repairType.setEnabled(state);
        deviceSerial.setEditable(state);
        deviceType.setEditable(state);
        deviceBrand.setEditable(state);
        deviceModel.setEditable(state);
        devicePurchaseDate.setEditable(state);
        deviceWarrantyExpiration.setEditable(state);
        devicePreviousRepair.setEditable(state);
        deviceRepairWarrantyExpiration.setEditable(state);
        clientName.setEditable(state);
        clientSurname.setEditable(state);
        clientPatronymic.setEditable(state);
        clientPhoneNumber.setEditable(state);
        clientEmail.setEditable(state);
        deviceMalfunction.setEnabled(state);
        deviceNote.setEnabled(state);
        receiptStatus.setEnabled(state);
    }

    private void clearElements(){
        SimpleDateFormat dt = new SimpleDateFormat("yyddMM");
        SimpleDateFormat dt1 = new SimpleDateFormat("hhmmss");
        Date today = new Date();
        receiptNumber.setText(dt.format(today) + dt1.format(today) + currentUser.getId());
        dt = new SimpleDateFormat("yyyy-MM-dd");
        receiptDate.setText(dt.format(today));
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
        receiver.setText(currentUser.getFIO());
    }

    private boolean receiptAdded(){

        if(isReceiptAdditionDataValid())
        {
            Main.showErrorMessage("Fill in all fields");
            return false;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(receiptDate.getText());
        }catch (ParseException e){
            Main.showErrorMessage("Invalid date format");
            return false;
        }

        Device device = facade.getDevice(deviceSerial.getText());
        if(device == null){
            Client client = (Client)facade.getUser(clientName.getText(), clientSurname.getText(),
                    clientPatronymic.getText(), clientEmail.getText());
            if(client == null) {
                Main.showUsers(currentUser, false);
            }

            device = new Device(deviceSerial.getText(), deviceType.getText(),
                    deviceBrand.getText(), deviceModel.getText(), client);
            facade.addDevice(device);
        }

        Receipt receipt = new Receipt(receiptNumber.getText(), date,
                RepairType.valueOf(repairType.getSelectedItem().toString()),
                facade.getDevice(deviceSerial.getText()), facade.getDeviceClient(deviceSerial.getText()),
                (Receiver) currentUser, deviceMalfunction.getText());
        if(!facade.addReceipt(receipt))
            return false;

        receiptList.put(receipt.getReceiptNumber(), receipt);
        addTableRow(receipt);
        return true;
    }

    private void setupHandlers(){
        ReceiptForm thisFrame = this;
        exitButton.addActionListener(ev -> {
            Main.showSignIn();
            Main.closeFrame(thisFrame);
        });

        newButton.addActionListener(ev -> {
            if("New".equals(newButton.getText())) {
                receiptStatus.setEditable(false);
                receiptStatus.setEnabled(false);
                receiptStatus.setSelectedItem(ReceiptStatus.Opened);
                clearElements();
                receiverPermissions(true);
                findDevice.setEnabled(true);
                newButton.setText("Add receipt");

                return;
            }
            if("Add receipt".equals(newButton.getText())){
                receiverPermissions(false);
                findDevice.setEnabled(false);
                newButton.setText("New");
                if(!receiptAdded())
                    Main.showErrorMessage("Failure during adding receipt");
                else
                    JOptionPane.showMessageDialog(new JFrame(),
                            "New receipt was succesfully added", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        });
        findDevice.addActionListener(e -> {
            String serial = deviceSerial.getText();
            if(facade.getDevice(serial) != null){
                deviceType.setText(facade.getDeviceType(serial));
                deviceBrand.setText(facade.getDeviceBrand(serial));
                deviceModel.setText(facade.getDeviceModel(serial));
                devicePurchaseDate.setText(
                        facade.getDevicePurchaseDate(serial) == null ? "" :
                                facade.getDevicePurchaseDate(serial).toString());
                deviceWarrantyExpiration.setText(
                        facade.getDeviceWarrantyExp(serial) == null ? "" :
                                facade.getDeviceWarrantyExp(serial).toString());
                devicePreviousRepair.setText(
                        facade.getDevicePrevRepair(serial) == null ? "" :
                                facade.getDevicePrevRepair(serial).toString());
                deviceRepairWarrantyExpiration.setText(
                        facade.getDevicePrevRepairWarrantyExp(serial) == null ? "" :
                                facade.getDevicePrevRepairWarrantyExp(serial).toString());

                clientName.setText(facade.getDeviceClient(serial).getName());
                clientSurname.setText(facade.getDeviceClient(serial).getSurname());
                clientPatronymic.setText(facade.getDeviceClient(serial).getPatronymic());
                clientEmail.setText(facade.getDeviceClient(serial).geteMail());
                clientPhoneNumber.setText(facade.getDeviceClient(serial).getPhoneNumber());
            }
        });

        findClient.addActionListener(e -> {

            String login = clientName.getText();
            Client client = (Client)facade.getUser(login);
            if(client == null) {
                Main.showErrorMessage("Client not found");
                Main.showUsers(currentUser, false);
                return;
            }

            clientName.setText(client.getName());
            clientSurname.setText(client.getSurname());
            clientPatronymic.setText(client.getPatronymic());
            clientEmail.setText(client.geteMail());
            clientPhoneNumber.setText(client.getPhoneNumber());
        });

        userInfoButton.addActionListener(e -> {
            Main.showUsers(currentUser, true);
            Main.closeFrame(thisFrame);
        });

        receipts.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = receipts.rowAtPoint(e.getPoint());
                TableModel model = receipts.getModel();

                Receipt selectedReceipt = receiptList.get(model.getValueAt(row, 0));
                selectedReceiptInfo(selectedReceipt);
                if("Receiver".equals(currentUser.getClass().getSimpleName())) {
                    updateButton.setEnabled(true);
                    receiverPermissions(true);
                }
                if("Master".equals(currentUser.getClass().getSimpleName())){
                    updateButton.setEnabled(true);
                    masterPermissions(true);
                }
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

    private void masterSingedIn(){
        this.receiptList.clear();
        this.receiptList = facade.getByMaster((Master)currentUser);
        fillTable(receiptList);
    }

    private void clientSignedIn(){
        this.receiptList.clear();
        this.receiptList = facade.getByClient((Client) currentUser);
        fillTable(this.receiptList);

    }

    private void receiverSignedIn(){
        this.receiptList.clear();
        newButton.setEnabled(true);
        userInfoButton.setEnabled(true);
        this.receiptList = facade.getAllReceipts();
        fillTable(this.receiptList);
    }

    private void fillTable(AbstractMap<String, Receipt> receiptList){
        DefaultTableModel model = (DefaultTableModel) receipts.getModel();
        model.setColumnCount(2);
        Object[] cols = new Object[]{"Receipt", "Status"};
        model.setColumnIdentifiers(cols);
        for(Map.Entry<String, Receipt> receipt : receiptList.entrySet())
            model.addRow(new Object[]{receipt.getKey(), receipt.getValue().getStatus()});
        receipts.setModel(model);
    }

    private void addTableRow(Receipt receipt){
        DefaultTableModel model = (DefaultTableModel)receipts.getModel();
        model.addRow(new Object[]{receipt.getReceiptNumber(), receipt.getStatus()});
        receipts.setModel(model);
        receipts.repaint();
    }

    private void selectedReceiptInfo(Receipt receipt){
        receiptNumber.setText(receipt.getReceiptNumber());
        receiptDate.setText(receipt.getReceiptDate().toString());
        repairType.setSelectedItem(receipt.getRepairType());

        deviceSerial.setText(receipt.getDevice().getSerialNumber());
        deviceType.setText(receipt.getDevice().getDeviceType());
        deviceBrand.setText(receipt.getDevice().getDeviceBrand());
        deviceModel.setText(receipt.getDevice().getDeviceModel());
        devicePurchaseDate.setText(receipt.getDevice().getDateOfPurchase().toString());
        deviceWarrantyExpiration.setText(receipt.getDevice().getWarrantyExpiration().toString());
        devicePreviousRepair.setText(
                receipt.getDevice().getPrevRepair() == null ? ""
                : receipt.getDevice().getPrevRepair().toString());
        deviceRepairWarrantyExpiration.setText(
                receipt.getDevice().getRepairWarrantyExpiration() == null ? ""
                : receipt.getDevice().getRepairWarrantyExpiration().toString());

        clientName.setText(receipt.getClient().getName());
        clientSurname.setText(receipt.getClient().getSurname());
        clientPatronymic.setText(receipt.getClient().getPatronymic());
        clientPhoneNumber.setText(receipt.getClient().getPhoneNumber());
        clientEmail.setText(receipt.getClient().geteMail());

        deviceMalfunction.setText(receipt.getMalfuncDescr());
        deviceNote.setText(receipt.getNote());

        receiver.setText(receipt.getReceiver().getFIO());
        master.setText(receipt.getMaster() == null ? "" : receipt.getMaster().getFIO());
    }

    private boolean isReceiptAdditionDataValid(){
        return repairType.getSelectedIndex() == -1 || deviceSerial.getText().isEmpty() ||
                deviceType.getText().isEmpty() || deviceBrand.getText().isEmpty() ||
                deviceModel.getText().isEmpty() || deviceMalfunction.getText().isEmpty() ||
                clientName.getText().isEmpty() || clientSurname.getText().isEmpty() ||
                clientPhoneNumber.getText().isEmpty() || clientEmail.getText().isEmpty();
    }
}
