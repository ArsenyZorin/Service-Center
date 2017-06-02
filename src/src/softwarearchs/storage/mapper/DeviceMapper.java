package softwarearchs.storage.mapper;

import softwarearchs.additional.Device;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class DeviceMapper {

    private static Set<Device> devices = new HashSet<>();
    private static Date today = new Date();

    public boolean addDevice(Device device) throws SQLException {

        if(findDevice(device.getSerialNumber()) != null) return false;

        String statement = "INSERT INTO device VALUES (" + device.getSerialNumber() +
                ", " + device.getDeviceType() + ", " + device.getDeviceBrand() +
                ", " + device.getDeviceModel() + ", " + device.getDateOfPurchase() +
                ", " + device.getWarrantyExpiration() + ", " +
                device.getPrevRepair() + ", " + device.getRepairWarrantyExpiration() + ");";
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.executeQuery();
        devices.add(device);
        return true;
    }

    public static Device findDevice(String serialNumber) throws SQLException{
        for (Device device : devices)
            if(serialNumber.equals(device.getSerialNumber()))
                return device;

        String statement = "SELECT * FROM device WHERE Serial number = \"" + serialNumber + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();

        if(!rs.next()) return null;

        Device device = new Device(rs.getString("Serial number"),
                rs.getString("Type"), rs.getString("Brand"),
                rs.getString("Model"), (Client)UserMapper.findUser(rs.getInt("Client")));

        device.setDateOfPurchase(rs.getDate("Purchase"));
        device.setWarrantyExpiration(rs.getDate("Warranty expiration"));
        device.setPrevRepair(rs.getDate("Previous repair"));
        device.setRepairWarrantyExpiration(rs.getDate("Repair warranty expiration"));
        device.setWarrantyAvailable(
                device.getWarrantyExpiration().after(today) ||
                        device.getWarrantyExpiration().equals(today)
        );
        device.setRepairWarrantyAvailable(
                device.getRepairWarrantyExpiration().after(today) ||
                        device.getRepairWarrantyExpiration().equals(today)
        );

        devices.add(device);
        return device;
    }

}
