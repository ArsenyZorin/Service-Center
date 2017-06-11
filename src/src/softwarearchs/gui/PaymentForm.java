package softwarearchs.gui;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import softwarearchs.Main;
import softwarearchs.facade.Facade;
import softwarearchs.invoice.BankAccount;
import softwarearchs.invoice.Invoice;
import softwarearchs.user.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;

/**
 * Created by arseny on 11/06/17.
 */
public class PaymentForm extends JFrame{
    private JPanel rootPanel;
    private JPasswordField cvcValue;
    private JTextField validationDateValue;
    private JTextField accountNumberValue;
    private JTextField clientValue;
    private JTextField invoiceNumberValue;
    private JTextField priceValue;
    private JTextField receiverValue;
    private JButton payButton;
    private JCheckBox paymentType;

    private Facade facade = Main.facade;
    private Invoice currentInvoice;
    private int amountOfFields = 0;

    public PaymentForm(Invoice currentInvoice){
        this.currentInvoice = currentInvoice;

        Main.frameInit(this, rootPanel, 600, 350);
        setInfo();
        setHandler();
        setVisible(true);
    }

    private void setInfo(){
        invoiceNumberValue.setText(currentInvoice.getInvoiceNumber());
        priceValue.setText("" + currentInvoice.getPrice());
        receiverValue.setText(currentInvoice.getReceiver().getFIO());
        clientValue.setText(currentInvoice.getClient().getFIO());
    }

    private void setHandler(){
        paymentType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JCheckBox cb = (JCheckBox) ev.getSource();
                if(cb.isSelected()){
                    paymentType(false);
                } else{
                    paymentType(true);
                }
            }
        });
        accountNumberValue.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}
            @Override
            public void focusLost(FocusEvent focusEvent) {
                lostFocus(focusEvent);
            }
        });
        validationDateValue.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}
            @Override
            public void focusLost(FocusEvent focusEvent) {
                lostFocus(focusEvent);
            }
        });
        cvcValue.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}
            @Override
            public void focusLost(FocusEvent focusEvent) { lostFocus(focusEvent); }
        });
        payButton.addActionListener(ev -> payAction());
    }

    private void lostFocus(FocusEvent ev) {
        if (!ev.isTemporary()) {
            if (paymentType.isSelected())
                return;

            if (new String(cvcValue.getPassword()).isEmpty() ||
                    accountNumberValue.getText().isEmpty() ||
                    validationDateValue.getText().isEmpty()) {
                payButton.setEnabled(false);
                return;
            }
            payButton.setEnabled(true);
        }
    }

    private void paymentType(boolean state){
        accountNumberValue.setEnabled(state);
        validationDateValue.setEnabled(state);
        cvcValue.setEnabled(state);
        payButton.setEnabled(!state);
    }

    private void payAction(){
        if(paymentType.isSelected()) {
            final JOptionPane optionPane = new JOptionPane(
                    "Cash payment?",
                    JOptionPane.QUESTION_MESSAGE,
                    JOptionPane.YES_NO_OPTION);
            if(optionPane.getValue().equals(JOptionPane.YES_OPTION)) {
                Main.showInformationMessage("Cash payment succeed");
                return;
            }
        }

        Date validDate = Main.dateFromString(validationDateValue.getText());
        if(validDate.before(new Date())){
            Main.showErrorMessage("Invalid bank account. Expired");
            return;
        }

        try {
            facade.payForRepair(accountNumberValue.getText(), validDate, new String(cvcValue.getPassword())
                    , clientValue.getText(), currentInvoice);
        } catch(Exception e){
            Main.showErrorMessage(e.getMessage());
            return;
        }

        Main.showInformationMessage("Payment successful");
        Main.closeFrame(this);
    }
}
