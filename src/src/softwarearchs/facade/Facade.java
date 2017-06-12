package softwarearchs.facade;

import softwarearchs.Main;
import softwarearchs.additional.Device;
import softwarearchs.enums.InvoiceStatus;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.enums.Role;
import softwarearchs.exceptions.*;
import softwarearchs.invoice.BankAccount;
import softwarearchs.invoice.Invoice;
import softwarearchs.receipt.Receipt;
import softwarearchs.storage.MapperRepository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import javax.jws.soap.SOAPBinding;
import javax.mail.internet.AddressException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;

/**
 * Created by zorin on 27.05.2017.
 */
public class Facade {
    private MapperRepository repos;

    public Facade(){
        repos = new MapperRepository();
    }

    public boolean signIn(String login, String pwd){
        return repos.signIn(login, pwd);
    }

    //User info
    public User getUser(String login) { return repos.findUser(login); }
    public User getUser(String name, String surname, String patronymic){
        return repos.findUser(name, surname, patronymic);
    }
    public User addUser(String userRole, String userName, String userSurname,
                        String userPatronymic, String userLogin,
                        String userPhoneNumber, String userEmail,
                        String pwd) throws CreationFailed, AddressException, EmailSendingFailed {


        if(!repos.addUser(userName, userSurname, userPatronymic, userPhoneNumber,
                userEmail, userLogin, Role.valueOf(userRole), pwd))
            throw new CreationFailed("User creation failed");
        User user = getUser(userLogin);
        if(!user.registrationNotification(pwd))
            throw new EmailSendingFailed("Failed to send an email");
        return user;
    }

    public AbstractMap<String, User> getAllUsers() { return repos.findAllUsers(); }
    public boolean updateUser(User user){ return repos.updateUser(user); }
    public boolean deleteUser(String login) { return repos.deleteUser(login); }

    //Device info
    public Device addDevice(String deviceSerial, String clientFIO, String deviceType,
                             String deviceBrand, String deviceModel, String devicePurchaseDate,
                             String deviceWarrantyExpiration, String devicePreviousRepair,
                             String deviceRepairWarrantyExpiration) throws InvalidUser, CreationFailed{

        String clientArr[] = clientFIO.split(" ");
        Client client = (Client)getUser(clientArr[0], clientArr[1], clientArr[2]);
        if(client == null || client.getId() == 0) {
            throw new InvalidUser("Invalid user");
        }

        Device device = new Device(deviceSerial.toUpperCase(), deviceType, deviceBrand, deviceModel, client);
        device.setDateOfPurchase(Main.dateFromString(devicePurchaseDate));
        device.setWarrantyExpiration(Main.dateFromString(deviceWarrantyExpiration));
        device.setPrevRepair(Main.dateFromString(devicePreviousRepair));
        device.setRepairWarrantyExpiration(Main.dateFromString(deviceRepairWarrantyExpiration));

        if(!repos.addDevice(device))
            throw new CreationFailed("Device creation failed");
        return device;
    }
    public Device getDevice(String serialNumber){ return repos.findDevice(serialNumber.toUpperCase()); }

    //Receipt info
    public Receipt addReceipt(String receiptNumber, String receiptDate, String repairType,
                              Device device, String deviceMalfunction, String deviceNote,
                              String receiptStatus) throws InvalidUser, CreationFailed,
            AddressException, EmailSendingFailed{
        Date date = Main.dateFromString(receiptDate);

        Receipt receipt = new Receipt(receiptNumber, date, RepairType.valueOf(repairType),
                device, device.getClient(), (Receiver) Main.currentUser, deviceMalfunction);
        receipt.setStatus(ReceiptStatus.valueOf(receiptStatus));
        receipt.setNote(deviceNote);
        if(!repos.addReceipt(receipt))
            throw new CreationFailed("Receipt creation failed");

        if(!receipt.getClient().statusChangingNotification(receipt))
            throw new EmailSendingFailed("Failed send an email");

        return receipt;
    }

    public boolean updateReceipt(Receipt receipt) throws UpdationFailed, AddressException, EmailSendingFailed {
        if(!repos.updateReceipt(receipt))
            throw new UpdationFailed("Receipt updation failed");

        if(!receipt.getClient().statusChangingNotification(receipt))
            throw new EmailSendingFailed("Failed send an email");

        return true;
    }
    public AbstractMap<String, Receipt> getAllReceipts() {return repos.findAllReceipts(); }
    public AbstractMap<String, Receipt> getByUser(User user) {return repos.findByUser(user); }

    //Invoice info
    public AbstractMap<String, Invoice> getAllInvoices() { return repos.findAllInvoices(); }
    public AbstractMap<String, Invoice> getInvoicesByUser(User user) {return repos.findInvoiceByUser(user); }
    public boolean updateInvoice(Invoice invoice) throws UpdationFailed {
        if(!repos.updateInvoice(invoice))
            throw new UpdationFailed("Invoice updation failed");

        return true;
    }

    public Invoice getInvoice(String invoiceNumber){
        return repos.findInvoice(invoiceNumber);
    }

    public Invoice addInvoice(String currentDate, Receipt receipt, String price)
            throws CreationFailed{
        Invoice invoice = new Invoice(Main.dateFromString(currentDate), receipt);
        invoice.setPrice(Double.parseDouble(price));
        if(!repos.addInvoice(invoice))
            throw new CreationFailed("Invoice creation failed");
        return invoice;
    }

    //BankAccount info
    public BankAccount getAccount(String accountNumber) { return  repos.findAccount(accountNumber); }

    public boolean payForRepair(String accountNumber, Date validDate, String cvcValue,
                                String clientFIO, Invoice invoice)
            throws InvalidPaymentData, InsufficientFunds{
        String client[] = clientFIO.split(" ");
        BankAccount account = new BankAccount(accountNumber,
                (Client)getUser(client[0], client[1], client[2]), validDate,
                Integer.parseInt(cvcValue));
        BankAccount account2 = getAccount(accountNumber);

        account.Eq(account2);
        account2.payForRepair(invoice.getPrice());
        updateAccount(account2);
        invoice.setStatus(InvoiceStatus.Paid);

        return true;
    }

    public boolean updateAccount(BankAccount account) {return repos.updateAccount(account); }
}
