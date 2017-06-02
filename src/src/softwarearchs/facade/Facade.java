package softwarearchs.facade;

import softwarearchs.additional.Device;
import softwarearchs.enums.InvoiceStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.receipt.Receipt;
import softwarearchs.storage.MapperRepository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.util.Date;
import java.util.List;

/**
 * Created by zorin on 27.05.2017.
 */
public class Facade {
    private MapperRepository repos;

    public Facade(){ repos = new MapperRepository(); }

    public boolean signIn(String login, String pwd){
        return repos.signIn(login, pwd);
    }

    //User info
    public User getUser(String login) { return repos.findUser(login); }
    public List<User> getAllUsers() { return repos.findAllUsers(); }
    public String getUserName(String login){ return repos.findUser(login).getName(); }
    public String getUserSurname(String login){ return repos.findUser(login).getSurname(); }
    public String getUserPatronymic(String login){ return repos.findUser(login).getPatronymic(); }
    public String getUserEmail(String login){ return repos.findUser(login).geteMail(); }
    public String getUserPhone(String login){ return repos.findUser(login).getPhoneNumber(); }

    //Device info
    public Client getDeviceClient(String serialNumber){ return repos.findDevice(serialNumber).getClient(); }
    public String getDeviceType(String serialNumber){ return repos.findDevice(serialNumber).getDeviceType(); }
    public String getDeviceBrand(String serialNumber){ return repos.findDevice(serialNumber).getDeviceBrand(); }
    public String getDeviceModel(String serialNumber){ return repos.findDevice(serialNumber).getDeviceModel(); }
    public Date getDevicePurchaseDate(String serialNumber){ return repos.findDevice(serialNumber).getDateOfPurchase();}
    public Date getDeviceWarrantyExp(String serialNumber){ return repos.findDevice(serialNumber).getWarrantyExpiration();}
    public Date getDevicePrevRepair(String serialNumber){ return repos.findDevice(serialNumber).getPrevRepair();}
    public Date getDevicePrevRepairWarrantyExp(String serialNumber){
        return repos.findDevice(serialNumber).getWarrantyExpiration();
    }

    //Receipt info
    public boolean addReceipt(int receiptNumber, Date receiptDate, RepairType repairType, Device device, Client client,
                              Receiver receiver, String malfuncDescr) {
        return repos.addReceipt(receiptNumber, receiptDate, repairType, device, client, receiver, malfuncDescr);
    }
    public List<Receipt> getAllReceipts() {return repos.findAllReceipts(); }
    public List<Receipt> getByClient(Client client) {return repos.findByClient(client); }
    public RepairType getReceiptRepairType(int receiptNumber){ return repos.findReceipt(receiptNumber).getRepairType(); }
    public Device getReceiptDevice(int receiptNumber) { return repos.findReceipt(receiptNumber).getDevice(); }
    public Client getReceiptClient(int receiptNumber) { return repos.findReceipt(receiptNumber).getClient(); }
    public String getReceiptMalfuncDescr(int receiptNumber) { return repos.findReceipt(receiptNumber).getMalfuncDescr(); }
    public String getReceiptNote(int receiptNumber) { return repos.findReceipt(receiptNumber).getNote(); }
    public Master getReceiptMaster(int receiptNumber) { return repos.findReceipt(receiptNumber).getMaster(); }
    public Receiver getReceiptReceiver(int receiptNumber) { return repos.findReceipt(receiptNumber).getReceiver(); }

    //Invoice info
    public Date getInvoiceDate(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getInvoiceDate(); }
    public Receipt getInvoiceReceipt(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getReceipt(); }
    public double getInvoicePrice(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getPrice(); }
    public Client getInvoiceClient(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getClient(); }
    public Receiver getInvoiceReceiver(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getReceiver(); }
    public InvoiceStatus getInvoiceStatus(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getStatus(); }

    //BankAccount info
    public Client getAccountClient(int accountNumber) { return repos.findAccount(accountNumber).getClient(); }
    public Date getAccountValidDate(int accountNumber) { return repos.findAccount(accountNumber).getValidTill(); }
    public int getAccountCvc(int accountNumber) { return repos.findAccount(accountNumber).getCvc(); }
}
