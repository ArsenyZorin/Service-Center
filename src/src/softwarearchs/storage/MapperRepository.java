package softwarearchs.storage;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import softwarearchs.additional.Device;
import softwarearchs.enums.RepairType;
import softwarearchs.enums.Role;
import softwarearchs.invoice.BankAccount;
import softwarearchs.invoice.Invoice;
import softwarearchs.receipt.Receipt;
import softwarearchs.storage.mapper.*;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Date;
import java.util.List;

/**
 * Created by zorin on 27.05.2017.
 */
public class MapperRepository {
    private static UserMapper userMapper;
    private static DeviceMapper deviceMapper;
    private static ReceiptMapper receiptMapper;
    private static BankMapper bankMapper;
    private static InvoiceMapper invoiceMapper;

    public MapperRepository() {
        if (userMapper == null) userMapper = new UserMapper();
        if (deviceMapper == null) deviceMapper = new DeviceMapper();
        if (receiptMapper == null) receiptMapper = new ReceiptMapper();
        if (bankMapper == null) bankMapper = new BankMapper();
        if (invoiceMapper == null) invoiceMapper = new InvoiceMapper();
    }

    public boolean addUser(String name, String surname, String patronymic,
                           String phone, String eMail, String login, Role role, String pwd){
        try {
            if(userMapper.findUser(login) != null)
                return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        User user = null;
        switch (role){
            case Receiver:
                user = new Receiver(name, surname, patronymic, login);
                break;
            case Master:
                user = new Master(name, surname, patronymic, login);
                break;
            case Client:
                user = new Client(name, surname, patronymic,login);
                break;
        }
        user.setPhoneNumber(phone);
        user.seteMail(eMail);

        try{
            userMapper.addUser(user, pwd);
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addUser(User user, String pwd){
        try {
            if(userMapper.findUser(user.getLogin()) != null)
                return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        try{
            userMapper.addUser(user, pwd);
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public User findUser(String login){
        try {
            return userMapper.findUser(login);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User findUser(String name, String surname, String patronymic){
        try {
            return userMapper.findUser(name, surname, patronymic);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User findUser(int id){
        try{
            return userMapper.findUser(id);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user){
        try{
            return userMapper.updateUser(user);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String login){
        try{
            return userMapper.deleteUser(login);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public AbstractMap<String, User> findAllUsers(){
        try {
            return userMapper.findAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean signIn(String login, String pwd){
        try{
            return userMapper.signIn(login, pwd);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addReceipt(String receiptNumbber, Date receiptDate, RepairType repairType, Device device, Client client,
                              Receiver receiver, String malfuncDescr){
        Receipt receipt = new Receipt(receiptNumbber, receiptDate, repairType, device, client, receiver, malfuncDescr);
        try{
            return receiptMapper.addReceipt(receipt);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addReceipt(Receipt receipt){
        try{
            return receiptMapper.addReceipt(receipt);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReceipt(Receipt receipt){
        try{
            return receiptMapper.updateReceipt(receipt);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Receipt findReceipt(String receiptNumber){
        try{
            return receiptMapper.findReceipt(receiptNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public AbstractMap<String, Receipt> findAllReceipts(){
        try {
            return receiptMapper.findAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public AbstractMap<String, Receipt> findByClient(Client client){
        try {
            return receiptMapper.findByClient(client);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public AbstractMap<String, Receipt> findByMaster(Master master){
        try {
            return receiptMapper.findByMaster(master);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addInvoice(Invoice invoice){
        try{
            return invoiceMapper.addInvoice(invoice);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Invoice findInvoice(String invoiceNumber){
        try {
            return invoiceMapper.findInvoice(invoiceNumber);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public AbstractMap<String, Invoice> findAllInvoices(){
        try {
            return invoiceMapper.findAllInvoices();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public AbstractMap<String, Invoice> findInvoiceByUser(User user){
        try{
            return invoiceMapper.findByUser(user);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDevice(String serialNumber, String deviceType,
                             String deviceBrand, String deviceModel, Client client){
        Device device = new Device(serialNumber, deviceType, deviceBrand, deviceModel, client);
        try{
            return deviceMapper.addDevice(device);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean addDevice(Device device){
        try{
            return deviceMapper.addDevice(device);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Device findDevice(String serialNumber){
        try{
            return deviceMapper.findDevice(serialNumber);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addBankAccount(BankAccount account){
        try{
            return bankMapper.addAccount(account);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public BankAccount findAccount(String accountNumber){
        try{
            return bankMapper.findAccount(accountNumber);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateAccount(BankAccount account){
        try {
            return bankMapper.updateAccount(account);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
