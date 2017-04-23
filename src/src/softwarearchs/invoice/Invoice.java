package softwarearchs.invoice;

import softwarearchs.enums.InvoiceStatus;
import softwarearchs.receipt.Receipt;
import softwarearchs.storage.Repository;
import softwarearchs.user.Client;
import softwarearchs.user.Receiver;

import java.util.Date;

/**
 * Created by arseny on 09.04.17.
 */
public class Invoice {
    private int invoiceNumber;
    private Date invoiceDate;
    private Receipt receipt;
    private double price;
    private Client client;
    private Receiver receiver;
    private InvoiceStatus status;

    public Invoice(int invoiceNumber, Receipt receipt, Receiver receiver, Client client){
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = new Date();
        this.receipt = receipt;
        this.receiver = receiver;
        this.client = client;
        this.status = InvoiceStatus.Waiting_For_Payment;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public int getInvoiceNumber() { return invoiceNumber; }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public Client getClient() {
        return client;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public boolean addInvoice() { return (new Repository()).addInvoice(this, this.receipt); }
}
