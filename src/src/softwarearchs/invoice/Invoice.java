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

    public Invoice(int invoiceNumber){
        this.invoiceNumber = invoiceNumber;
    }

    public Invoice(int invoiceNumber, Receipt receipt, Receiver receiver, Client client){
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = new Date();
        this.receipt = receipt;
        this.receiver = receiver;
        this.client = client;
        this.status = InvoiceStatus.Waiting_For_Payment;
    }

    public Receipt getReceipt() { return  this.receipt; }
    public double getPrice() { return this.price; }
    public InvoiceStatus getStatus() { return this.status; }
    public int getInvoiceNumber() { return this.invoiceNumber; }
    public Date getInvoiceDate() { return this.invoiceDate; }
    public Client getClient() { return this.client; }
    public Receiver getReceiver() { return this.receiver; }

    public void setReceipt(Receipt receipt) { this.receipt = receipt; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
    public void setClient(Client client) { this.client = client; }
    public void setReceiver(Receiver receiver) {this.receiver = receiver; }
    public void setInvoiceDate(Date invoiceDate){ this.invoiceDate = invoiceDate; }

    public boolean addInvoice() { return (new Repository()).addInvoice(this, this.receipt); }
}
