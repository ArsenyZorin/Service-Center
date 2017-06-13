package softwarearchs.user;

import softwarearchs.Main;
import softwarearchs.repair.Device;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.repair.Receipt;

import java.util.Date;

/**
 * Created by arseny on 07.04.17.
 */
public class Receiver extends User{

    public Receiver(int id, String name, String surname, String patronymic, String login){
        super(id, name, surname, patronymic, login);
    }

    public Receiver(String name, String surname, String patronymic, String login){
        super(name, surname, patronymic, login);
    }

    public Receipt createReceipt(String receiptNumber, String receiptDate, String repairType,
                                 Device device, String deviceMalfunction, String deviceNote,
                                 String receiptStatus){
        Date date = Main.dateFromString(receiptDate);

        Receipt receipt = new Receipt(receiptNumber, date, RepairType.valueOf(repairType),
                device, device.getClient(), this, deviceMalfunction);
        receipt.setStatus(ReceiptStatus.valueOf(receiptStatus));
        receipt.setNote(deviceNote);

        return receipt;
    }

    public Device createDevice(String deviceSerial, String deviceType,
                                String deviceBrand, String deviceModel,
                                Client client, String devicePurchaseDate,
                                String deviceWarrantyExpiration, String devicePreviousRepair,
                                String deviceRepairWarrantyExpiration){

        Device device = new Device(deviceSerial.toUpperCase(), deviceType,
                deviceBrand, deviceModel, client);
        device.setDateOfPurchase(Main.dateFromString(devicePurchaseDate));
        device.setWarrantyExpiration(Main.dateFromString(deviceWarrantyExpiration));
        device.setPrevRepair(Main.dateFromString(devicePreviousRepair));
        device.setRepairWarrantyExpiration(Main.dateFromString(deviceRepairWarrantyExpiration));

        return device;
    }
}
