package softwarearchs.storage;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import softwarearchs.additional.Device;
import softwarearchs.enums.RepairType;
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
                           String phone, String eMail, String login, String role, String pwd){
        try {
            if(userMapper.findUser(login) != null)
                return false;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        User user = null;
        switch (role){
            case "Receiver":
                user = new Receiver(name, surname, patronymic, login);
                break;
            case "Master":
                user = new Master(name, surname, patronymic, login);
                break;
            case "Client":
                user = new Client(name, surname, patronymic,login);
                break;
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

    public User findUser(int id){
        try{
            return userMapper.findUser(id);
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

    public boolean addReceipt(RepairType repairType, Device device, Client client,
                              Receiver receiver, String malfuncDescr){
        Receipt receipt = new Receipt(repairType, device, client, receiver, malfuncDescr);
        try{
            return receiptMapper.addReceipt(receipt);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Receipt getReceipt(String receiptNumber){
        try{
            return receiptMapper.findReceipt(receiptNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addInvoice(int invoiceNumber, Receipt receipt, Receiver receiver, Client client){
        Invoice invoice = new Invoice(invoiceNumber, receipt, receiver, client);
        try{
            return invoiceMapper.addInvoice(invoice);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Invoice findInvoice(int invoiceNumber){
        try {
            return invoiceMapper.findInvoice(invoiceNumber);
        } catch(SQLException e){
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

    public Device findDevice(String serialNumber){
        try{
            return deviceMapper.findDevice(serialNumber);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addBankAccount(int accountNumber, Client client){
        BankAccount account = new BankAccount(accountNumber, client);
        try{
            return bankMapper.addAccount(account);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public BankAccount findAccount(int accountNumber){
        try{
            return bankMapper.findAccount(accountNumber);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
