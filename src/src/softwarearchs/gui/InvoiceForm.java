package softwarearchs.gui;

import softwarearchs.Main;
import softwarearchs.enums.InvoiceStatus;
import softwarearchs.enums.Role;
import softwarearchs.facade.Facade;
import softwarearchs.invoice.Invoice;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arseny on 11/06/17.
 */
public class InvoiceForm extends JFrame{
    private JTable invoiceTable;
    private JPanel rootPanel;
    private JTextField invoiceNumber;
    private JTextField dateValue;
    private JTextField priceValue;
    private JTextField clientValue;
    private JTextField receiverValue;
    private JComboBox invoiceStatus;
    private JButton exitButton;
    private JButton newButton;
    private JButton paymentButton;
    private Facade facade = Main.facade;
    private AbstractMap<String, Invoice> invoices= new HashMap<>();
    private Role currentUserClass;
    private User currentUser;
    private Receipt currentReceipt;
    private int clickedRow = -1;
    private Invoice selectedInvoice;

    public InvoiceForm(Receipt currentReceipt){
        this.currentUser = Main.currentUser;
        this.currentUserClass = Role.valueOf(this.currentUser.getClass().getSimpleName());
        this.currentReceipt = currentReceipt;

        for(InvoiceStatus status : InvoiceStatus.values())
            invoiceStatus.addItem(status);

        setInfo();
        fillTable();
        setHandlers();
        Main.frameInit(this, rootPanel, 900, 350);
        setVisible(true);
    }

    private void setInfo(){
        invoiceNumber.setText(this.currentReceipt.getReceiptNumber());
        dateValue.setText(Main.stringFromDate(new Date()));
        priceValue.setText("0.0");
        clientValue.setText(this.currentReceipt.getClient().getFIO());
        receiverValue.setText(this.currentReceipt.getReceiver().getFIO());
        invoiceStatus.setSelectedItem(InvoiceStatus.Waiting_For_Payment);
    }

    private void setHandlers(){
        exitButton.addActionListener(ev -> Main.closeFrame(this));
        paymentButton.addActionListener(ev ->
            Main.showPayment(selectedInvoice)
        );
        newButton.addActionListener(ev -> {
            if("New".equals(newButton.getText())) {
                newButton.setText("Add invoice");
                priceValue.setEditable(true);
                invoiceNumber.setText(currentReceipt.getReceiptNumber());
                dateValue.setText(Main.stringFromDate(new Date()));
                priceValue.setText("" + 0.0);
                clientValue.setText(currentReceipt.getClient().getFIO());
                receiverValue.setText(currentReceipt.getReceiver().getFIO());
                invoiceStatus.setSelectedItem(InvoiceStatus.Waiting_For_Payment);
                paymentButton.setEnabled(false);
                return;
            }
            if(("Add invoice").equals(newButton.getText())){
                priceValue.setEditable(false);
                Invoice invoice;
                try{
                    invoice = facade.addInvoice(dateValue.getText(), currentReceipt, priceValue.getText());
                } catch(Exception e){
                    Main.showErrorMessage(e.getMessage());
                    return;
                }

                Main.showInformationMessage("Invoice creation succeeded");
                addTableRow(invoice);
                newButton.setText("New");
                paymentButton.setEnabled(true);
            }
        });
        invoiceTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rowClicked(e);
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


    private void rowClicked(MouseEvent e){
        clickedRow = invoiceTable.rowAtPoint(e.getPoint());
        if(clickedRow < 0)
            return;
        TableModel model = invoiceTable.getModel();
        selectedInvoice = invoices.get(model.getValueAt(clickedRow, 0));
        selectedInvoiceInfo(selectedInvoice);
        paymentButton.setEnabled(true);
    }

    private void selectedInvoiceInfo(Invoice invoice){
        invoiceNumber.setText(invoice.getInvoiceNumber());
        dateValue.setText(Main.stringFromDate(invoice.getInvoiceDate()));
        priceValue.setText("" + invoice.getPrice());
        clientValue.setText(invoice.getClient().getFIO());
        receiverValue.setText(invoice.getReceiver().getFIO());
        invoiceStatus.setSelectedItem(invoice.getStatus());
    }

    private void fillTable() {
        invoices.clear();
        //invoices = facade.getInvoicesByUser(currentUser);
        invoices = facade.getAllInvoices();
        DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
        model.setColumnCount(2);
        Object[] cols = new Object[]{"#", "Status"};
        model.setColumnIdentifiers(cols);
        for (Map.Entry<String, Invoice> receipt : invoices.entrySet())
            model.addRow(new Object[]{receipt.getKey(), receipt.getValue().getStatus()});
        invoiceTable.setModel(model);
    }

    private void addTableRow(Invoice invoice) {
        DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
        model.addRow(new Object[]{invoice.getInvoiceNumber(), invoice.getStatus()});
        invoiceTable.setModel(model);
        invoiceTable.repaint();
    }

    private void replaceRow(Invoice invoice) {
        TableModel model = invoiceTable.getModel();
        model.setValueAt(invoice.getInvoiceNumber(), clickedRow, 0);
        model.setValueAt(invoice.getStatus(), clickedRow, 1);
        invoiceTable.setModel(model);
        invoiceTable.repaint();
    }
}