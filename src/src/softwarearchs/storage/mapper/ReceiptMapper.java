package softwarearchs.storage.mapper;

import softwarearchs.additional.Device;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.receipt.Receipt;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zorin on 23.05.2017.
 */
public class ReceiptMapper {
    private static AbstractMap<String, Receipt> receipts = new HashMap<>();

    public boolean addReceipt(Receipt receipt) throws SQLException{
        if(findReceipt(receipt.getReceiptNumber()) != null) return false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String statement = "INSERT INTO receipt VALUES (\"" + receipt.getReceiptNumber()
                + "\", DATE \'" + dateFormat.format(receipt.getReceiptDate())
                + "\', \"" + receipt.getDevice().getSerialNumber() + "\", " + receipt.getClient().getId()
                + ", " + receipt.getReceiver().getId() + ", \"" + receipt.getMalfuncDescr()
                + "\", " + (receipt.getNote() == null ? "NULL" : "\"" + receipt.getNote() + "\"")
                + ", " + (receipt.getMaster() == null ? "NULL" : "\"" + receipt.getMaster().getId() + "\"")
                + ", \"" + receipt.getRepairType() + "\", \"" + receipt.getStatus() + "\");";
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        receipts.put(receipt.getReceiptNumber(), receipt);
        return true;
    }

    public boolean updateReceipt(Receipt receipt) throws SQLException{
        String statement = "UPDATE receipt SET RepairType =\"" + receipt.getRepairType()
                + "\", Malfunction = \"" + receipt.getMalfuncDescr()
                + "\", Note = \"" + receipt.getNote()
                + "\", Status = \"" + receipt.getStatus()
                + "\", Master = \"" + receipt.getMaster().getId() + "\";";
        PreparedStatement update = Gateway.getGateway().getConnection().prepareStatement(statement);
        if(update.executeUpdate() == 0)
            return false;

        receipts.replace(receipt.getReceiptNumber(), receipt);
        return true;
    }

    private static Receipt getReceipt(ResultSet rs) throws SQLException{
        Receipt receipt = new Receipt(rs.getString("id"), rs.getDate("ReceiptDate"),
                RepairType.valueOf(rs.getString("RepairType")),
                DeviceMapper.findDevice(rs.getString("Device")),
                (Client)UserMapper.findUser(rs.getInt("Client")),
                (Receiver)UserMapper.findUser(rs.getInt("Receiver")),
                rs.getString("Malfunction"));
        receipt.setNote(rs.getString("Note"));
        receipt.setMaster((Master)UserMapper.findUser(rs.getInt("Master")));
        receipt.setStatus(ReceiptStatus.valueOf(rs.getString("Status")));
        return receipt;
    }

    public static Receipt findReceipt(String receiptNumber) throws SQLException {
        if(receipts.containsKey(receiptNumber))
            return receipts.get(receiptNumber);

        String statement = "SELECT * FROM receipt WHERE id = \"" + receiptNumber + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if(!rs.next()) return null;

        Receipt receipt = getReceipt(rs);
        receipts.put(receiptNumber, receipt);
        return receipt;
    }

    public AbstractMap<String, Receipt> findByClient(Client client) throws SQLException {
        AbstractMap<String, Receipt> receiptsByClient = new HashMap<>();

        String query = "SELECT * FROM receipt WHERE Client = " + client.getId() + ";";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next())
            receiptsByClient.put(rs.getString("id"), getReceipt(rs));

        return receiptsByClient;
    }

    public AbstractMap<String, Receipt> findByMaster(Master master) throws SQLException {
        AbstractMap<String, Receipt> receiptsByMaster = new HashMap<>();

        String query = "SELECT * FROM receipt WHERE Master = " + master.getId() + " OR Master is NULL;";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next())
            receiptsByMaster.put(rs.getString("id"), getReceipt(rs));

        return receiptsByMaster;
    }

    public AbstractMap<String, Receipt> findAll() throws SQLException{
        receipts.clear();

        String query = "SELECT * FROM receipt;";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        if(!rs.next()) {
            System.out.println("null");
            return null;
        }
        receipts.put(rs.getString("id"), getReceipt(rs));
        while(rs.next()) {
            receipts.put(rs.getString("id"), getReceipt(rs));
        }
        System.out.println(receipts.size());
        return receipts;
    }
}
