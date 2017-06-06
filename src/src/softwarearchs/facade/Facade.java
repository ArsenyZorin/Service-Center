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

import java.util.AbstractMap;
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
    public User getUser(String name, String surname, String patronymic, String email){
        return repos.findUser(name, surname, patronymic, email);
    }
    public boolean addUser(User user, String pwd){ return repos.addUser(user, pwd); }
    public AbstractMap<String, User> getAllUsers() { return repos.findAllUsers(); }
    public boolean updateUser(User user){ return repos.updateUser(user); }
    public String getUserName(String login){ return repos.findUser(login).getName(); }
    public String getUserSurname(String login){ return repos.findUser(login).getSurname(); }
    public String getUserPatronymic(String login){ return repos.findUser(login).getPatronymic(); }
    public String getUserEmail(String login){ return repos.findUser(login).geteMail(); }
    public String getUserPhone(String login){ return repos.findUser(login).getPhoneNumber(); }

    //Device info
    public boolean addDevice(Device device) { return repos.addDevice(device); }
    public Device getDevice(String serialNumber){ return repos.findDevice(serialNumber); }
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
    public boolean addReceipt(Receipt receipt) {return repos.addReceipt(receipt); }
    public AbstractMap<String, Receipt> getAllReceipts() {return repos.findAllReceipts(); }
    public AbstractMap<String, Receipt> getByClient(Client client) {return repos.findByClient(client); }
    public AbstractMap<String, Receipt> getByMaster(Master master) { return repos.findByMaster(master); }
    public RepairType getReceiptRepairType(String receiptNumber){ return repos.findReceipt(receiptNumber).getRepairType(); }
    public Device getReceiptDevice(String receiptNumber) { return repos.findReceipt(receiptNumber).getDevice(); }
    public Client getReceiptClient(String receiptNumber) { return repos.findReceipt(receiptNumber).getClient(); }
    public String getReceiptMalfuncDescr(String receiptNumber) { return repos.findReceipt(receiptNumber).getMalfuncDescr(); }
    public String getReceiptNote(String receiptNumber) { return repos.findReceipt(receiptNumber).getNote(); }
    public Master getReceiptMaster(String receiptNumber) { return repos.findReceipt(receiptNumber).getMaster(); }
    public Receiver getReceiptReceiver(String receiptNumber) { return repos.findReceipt(receiptNumber).getReceiver(); }

    //Invoice info
    public Date getInvoiceDate(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getInvoiceDate(); }
    public Receipt getInvoiceReceipt(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getReceipt(); }
    public double getInvoicePrice(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getPrice(); }
    public Client getInvoiceClient(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getClient(); }
    public Receiver getInvoiceReceiver(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getReceiver(); }
    public InvoiceStatus getInvoiceStatus(int invoiceNumber) { return repos.findInvoice(invoiceNumber).getStatus(); }

    //BankAccount info
    public Client getAccountClient(String accountNumber) { return repos.findAccount(accountNumber).getClient(); }
    public Date getAccountValidDate(String accountNumber) { return repos.findAccount(accountNumber).getValidTill(); }
    public int getAccountCvc(String accountNumber) { return repos.findAccount(accountNumber).getCvc(); }
}
