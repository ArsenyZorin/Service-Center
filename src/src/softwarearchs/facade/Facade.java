package softwarearchs.facade;

import softwarearchs.Main;
import softwarearchs.repair.Device;
import softwarearchs.enums.InvoiceStatus;
import softwarearchs.enums.Role;
import softwarearchs.exceptions.*;
import softwarearchs.additional.BankAccount;
import softwarearchs.repair.Invoice;
import softwarearchs.repair.Receipt;
import softwarearchs.storage.MapperRepository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import javax.mail.internet.AddressException;
import java.util.AbstractMap;
import java.util.Date;

/**
 * Created by zorin on 27.05.2017.
 */
public class Facade {
    private MapperRepository repos;

    public Facade(){
        repos = new MapperRepository();
    }

    public User signIn(String login, String pwd) throws InvalidSignIn{
        return User.signIn(login, pwd);
    }

    //User info
    public User getUser(String login) { return repos.findUser(login); }
    public User getUser(String name, String surname, String patronymic){
        return repos.findUser(name, surname, patronymic);
    }
    public User addUser(String userRole, String userName, String userSurname,
                        String userPatronymic, String userLogin,
                        String userPhoneNumber, String userEmail,
                        String pwd, String repeatPwd)
            throws CreationFailed, AddressException, EmailSendingFailed, InvalidUser {

        if(!pwd.equals(repeatPwd))
            throw new CreationFailed("Different passwords");
        User user = ((Receiver)Main.currentUser).addUser(Role.valueOf(userRole), userName, userSurname,
                  userPatronymic, userLogin,
                  userPhoneNumber, userEmail);
        if(!repos.addUser(user, pwd))
            throw new CreationFailed("User creation failed");

        user = getUser(userLogin);
        if(!user.registrationNotification(pwd))
            throw new EmailSendingFailed("Failed to send an email");
        return user;
    }

    public AbstractMap<String, User> getAllUsers() { return repos.findAllUsers(); }
    public boolean updateUser(User user){ return repos.updateUser(user); }
    public boolean deleteUser(String login) { return repos.deleteUser(login); }

    //Device info
    private Device addDevice(Device device) throws CreationFailed, IllegalWarranty{
        device.isWarrantyValid();
        if(!repos.addDevice(device))
            throw new CreationFailed("Device creation failed");
        return device;
    }
    public Device getDevice(String serialNumber){ return repos.findDevice(serialNumber.toUpperCase()); }

    private boolean updateDevice(Device device){ return repos.updateDevice(device); }

    public Device getReceiptDevice(String serial, String type, String brand, String model,
                      String clientFIO, String purchaseDate, String warrantyExp,
                      String prevRepair, String repWarranty)
            throws InvalidUser, CreationFailed, UpdationFailed, IllegalWarranty {
        String clientArr[] = clientFIO.split(" ");
        Client client = (Client)getUser(clientArr[0], clientArr[1], clientArr[2]);

        if(client == null || client.getId() == 0){
            throw new InvalidUser("Invalid user");
        }

        Device startDevice = getDevice(serial);
        Device secondDevice = ((Receiver)Main.currentUser).createDevice(serial, type, brand,
                model, client, purchaseDate, warrantyExp, prevRepair, repWarranty);

        boolean absEqual;
        if(secondDevice.Eq(startDevice)) {
            try {
                absEqual = secondDevice.absEq(startDevice);
            } catch (Exception e) {
                absEqual = false;
            }
        } else {
            return addDevice(secondDevice);
        }

        if(!absEqual)
            if(!updateDevice(secondDevice))
                throw new UpdationFailed("Device update failed");

        return secondDevice;
    }

    //Receipt info
    public Receipt addReceipt(String receiptNumber, String receiptDate, String repairType,
                              Device device, String deviceMalfunction, String deviceNote,
                              String receiptStatus) throws InvalidUser, CreationFailed,
            AddressException, EmailSendingFailed, IllegalWarranty {
        Receipt receipt;
        try {
            receipt = ((Receiver) Main.currentUser).createReceipt(receiptNumber, receiptDate,
                    repairType, device, deviceMalfunction, deviceNote, receiptStatus);
        }catch (Exception e){
            throw new InvalidUser("Invalid user trying to create new receipt");
        }

        receipt.isWarrantyValid();

        if(!repos.addReceipt(receipt))
            throw new CreationFailed("Receipt creation failed");

        if(!receipt.getClient().statusChangingNotification(receipt))
            throw new EmailSendingFailed("Failed send an email");

        return receipt;
    }
    public Receipt getReceipt(String receiptNumber) throws ElementNotFound {
        Receipt receipt = repos.findReceipt(receiptNumber);
        if(receipt == null)
            throw new ElementNotFound("Receipt was not found");

        return receipt;
    }

    public void updateReceipt(Receipt receipt) throws UpdationFailed, AddressException, EmailSendingFailed {
        if(!repos.updateReceipt(receipt))
            throw new UpdationFailed("Receipt updation failed");

        if(!receipt.getClient().statusChangingNotification(receipt))
            throw new EmailSendingFailed("Failed send an email");
    }

    public Receipt setReceiptStatus(String status, Receipt receipt) throws AcessPermision{
        if(Main.currentUser.getClass().getSimpleName().equals(Role.Master.toString()))
            receipt = ((Master)Main.currentUser).setRecStatus(status, receipt);
        else if(Main.currentUser.getClass().getSimpleName().equals(Role.Receiver.toString()))
            receipt = ((Receiver)Main.currentUser).setRecStatus(status, receipt);

        return receipt;
    }

    public Receipt assignOnRepair(Receipt receipt){
        try{
            receipt = ((Master)Main.currentUser).assignOnRepair(receipt);
        } catch (Exception e){
            return receipt;
        }
        return receipt;
    }

    public AbstractMap<String, Receipt> getAllReceipts() {return repos.findAllReceipts(); }
    public AbstractMap<String, Receipt> getByUser(User user) {return repos.findByUser(user); }

    //Invoice info
    public AbstractMap<String, Invoice> getAllInvoices() { return repos.findAllInvoices(); }
    public AbstractMap<String, Invoice> getInvoicesByUser(User user) {return repos.findInvoiceByUser(user); }
    public boolean updateInvoice(Invoice invoice) throws UpdationFailed {
        if(!repos.updateInvoice(invoice))
            throw new UpdationFailed("Invoice update failed");

        return true;
    }

    public Invoice getInvoice(String invoiceNumber){
        return repos.findInvoice(invoiceNumber);
    }

    public Invoice addInvoice(String currentDate, Receipt receipt, String price)
            throws CreationFailed{

        Invoice invoice = ((Receiver)Main.currentUser).createInvoice(currentDate, receipt, price);
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

        account.isValid();

        BankAccount account2 = getAccount(accountNumber);

        if(!account.Eq(account2))
            throw new InvalidPaymentData("Invalid payment data");

        account2.payForRepair(invoice.getPrice());
        updateAccount(account2);

        invoice.setStatus(InvoiceStatus.Paid);
        return true;
    }

    public boolean updateAccount(BankAccount account) { return repos.updateAccount(account); }
}
