package softwarearchs.storage.mapper;

import softwarearchs.additional.Device;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zorin on 23.05.2017.
 */
public class DeviceMapper {

    private static AbstractMap<String, Device> devices = new HashMap<>();
    private static Date today = new Date();

    public boolean addDevice(Device device) throws SQLException {

        if(findDevice(device.getSerialNumber()) != null) return false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOfPurchase = device.getDateOfPurchase() == null ?
                "NULL" : "DATE \'" + dateFormat.format(device.getDateOfPurchase()) + "\'";
        String warrantyExpir = device.getWarrantyExpiration() == null ?
                "NULL" : "DATE \'" + dateFormat.format(device.getWarrantyExpiration()) + "\'";
        String prevRepair = device.getPrevRepair() == null ?
                "NULL" : "DATE \'" + dateFormat.format(device.getPrevRepair()) + "\'";
        String repairWarranty = device.getRepairWarrantyExpiration() == null ?
                "NULL" : "DATE \'" + dateFormat.format(device.getRepairWarrantyExpiration()) + "\'";

        String statement = "INSERT INTO device VALUES (\"" + device.getSerialNumber() +
                "\", \"" + device.getDeviceType() + "\", \"" + device.getDeviceBrand() +
                "\", \"" + device.getDeviceModel() + "\", " + dateOfPurchase +
                ", " + warrantyExpir + ", " + prevRepair + ", " + repairWarranty + ", "
                + device.getClient().getId() + ");";
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        devices.put(device.getSerialNumber(), device);
        return true;
    }

    public static Device findDevice(String serialNumber) throws SQLException{

        if(devices.containsKey(serialNumber))
            return devices.get(serialNumber);

        String statement = "SELECT * FROM device WHERE SerialNumber = \"" + serialNumber + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();

        if(!rs.next()) return null;

        Device device = new Device(rs.getString("SerialNumber"),
                rs.getString("DeviceType"), rs.getString("Brand"),
                rs.getString("Model"), (Client)UserMapper.findUser(rs.getInt("Client")));

        device.setDateOfPurchase(rs.getDate("Purchase"));
        device.setWarrantyExpiration(rs.getDate("WarrantyExpiration"));
        device.setPrevRepair(rs.getDate("PreviousRepair"));
        device.setRepairWarrantyExpiration(rs.getDate("RepairWarrantyExpiration"));
        if(device.getWarrantyExpiration() != null) {
            device.setWarrantyAvailable(
                    device.getWarrantyExpiration().after(today) ||
                            device.getWarrantyExpiration().equals(today)
            );
        }
        if(device.getRepairWarrantyExpiration() != null) {
            device.setRepairWarrantyAvailable(
                    device.getRepairWarrantyExpiration().after(today) ||
                            device.getRepairWarrantyExpiration().equals(today)
            );
        }
        devices.put(device.getSerialNumber(), device);
        return device;
    }

}
