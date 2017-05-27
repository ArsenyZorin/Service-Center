package softwarearchs.storage.mapper;

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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class ReceiptMapper {
    private static Set<Receipt> receipts = new HashSet<>();

    public boolean addReceipt(Receipt receipt) throws SQLException{
        //Проверка на существование квитанции с id

        String statement = "INSERT INTO receipt VALUES (" + receipt.getReceiptNumber()
                + ", " + receipt.getReceiptDate() + ", " + receipt.getDevice().getSerialNumber()
                + ", " + receipt.getClient().getId() + ", " + receipt.getReceiver().getId()
                + ", " + receipt.getMalfuncDescr() + ", " + receipt.getNote()
                + ", " + receipt.getMaster().getId() + ", " + receipt.getRepairType()
                + ", " + receipt.getStatus();
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        insert.getGeneratedKeys();
        receipts.add(receipt);
        return true;
    }

    public static Receipt findReceipt(String receiptNumber) throws SQLException {
        for(Receipt receipt : receipts)
            if(receiptNumber.equals(receipt.getReceiptNumber()))
                return receipt;

        String statement = "SELECT * FROM receipt WHERE id number = " + receiptNumber + ";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.getGeneratedKeys();
        if(!rs.next()) return null;

        Receipt receipt = new Receipt();
        receipt.setDevice(DeviceMapper.findDevice(rs.getString("Device")));
        receipt.setClient((Client)UserMapper.findUser(rs.getString("Client")));
        receipt.setReceiver((Receiver)UserMapper.findUser(rs.getString("Receiver")));
        receipt.setMalfuncDescr(rs.getString("Malfunction"));
        receipt.setNote(rs.getString("Note"));
        receipt.setMaster((Master)UserMapper.findUser(rs.getString("Master")));
        receipt.setRepairType(RepairType.valueOf(rs.getString("Repair type")));
        receipt.setStatus(ReceiptStatus.valueOf(rs.getString("Status")));

        receipts.add(receipt);
        return receipt;
    }

}
