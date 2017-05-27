package softwarearchs.user;

import softwarearchs.additional.Device;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.receipt.Receipt;

import java.util.Date;

/**
 * Created by arseny on 07.04.17.
 */
public class Receiver extends User{
    Receipt receipt;

    public Receiver(int id, String name, String surname, String patronymic, String login){
        super(id, name, surname, patronymic, login);
    }

    public Receiver(String name, String surname, String patronymic, String login){
        super(name, surname, patronymic, login);
    }
    /**
     * Первоначальное создание квитанции
     * @return При успешном создании - возвращает созданную квитанцию
     */
    public Receipt createReceipt(){
        this.receipt = new Receipt();
        this.receipt.setReceiver(this);
        this.receipt.setStatus(ReceiptStatus.Opened);

        if(this.receipt.findReceipt() == null)
            return this.receipt.addReceipt() ? this.receipt : null;

        return null;
    }

    /**
     * Обновление информации о квитанции
     * @param repairType Тип ремонта
     * @param device Устройство
     * @param client Клиент
     * @param malfuncDescr Описание неисправности
     * @param note Заметка
     * @param receiptStatus Статус квитанции
     * @return Обновленная квитанция
     */
    public Receipt updateReceipt(RepairType repairType, Device device, Client client,
                                 String malfuncDescr, String note,
                                 ReceiptStatus receiptStatus){

        if(repairType != null)
            this.receipt.setRepairType(repairType);
        if(device != null)
            this.receipt.setDevice(device);
        if(client != null)
            this.receipt.setClient(client);
        if(malfuncDescr != null)
            this.receipt.setMalfuncDescr(malfuncDescr);
        if(note != null)
            this.receipt.setNote(note);
        if(receiptStatus != null)
            this.receipt.setStatus(receiptStatus);

        return this.receipt.updateReceipt() ? this.receipt : null;
    }

    /**
     * Создание устройства, которого еще нет в базе
     * @param serialNumber Серийный номер устройства
     * @param deviceType Тип устройства
     * @param deviceBrand Марка устройства
     * @param deviceModel Модель устройства
     * @param client Владелец устройства
     * @return Результат создания устройства
     */
    public boolean createDevice(String serialNumber, String deviceType,
                                String deviceBrand, String deviceModel, Client client){
        Device device = new Device(serialNumber, deviceType, deviceBrand,
                deviceModel, client);

        return (device.findDevice() == null) ? device.addDevice() : null;
    }

    /**
     * Обновелние информации об устройстве
     * @param serialNumber Серийный номер устройства
     * @param dateOfPurchase Дата покупки устройства
     * @param warrantyExpiration Дата истечения гарантии
     * @param prevRepair Дата предыдущего ремонта
     * @param repairWarrantyExpiration Дата истечения гарантии на ремонт
     * @param warrantyAvailable Наличие гарантии производителя
     * @param repairWarrantyAvailable Наличие гарантии на ремонт
     * @param client Владелец устройства
     * @return Результат обновления устройства
     */
    public boolean updateDevice (String serialNumber, Date dateOfPurchase,
                                 Date warrantyExpiration, Date prevRepair,
                                 Date repairWarrantyExpiration, Boolean warrantyAvailable,
                                 Boolean repairWarrantyAvailable, Client client){

        Device device = (new Device()).findDevice(serialNumber);

        if(dateOfPurchase != null)
            device.setDateOfPurchase(dateOfPurchase);
        if(warrantyExpiration != null)
            device.setWarrantyExpiration(warrantyExpiration);
        if(prevRepair != null)
            device.setPrevRepair(prevRepair);
        if(repairWarrantyExpiration != null)
            device.setRepairWarrantyExpiration(repairWarrantyExpiration);
        if(warrantyAvailable != null)
            device.setWarrantyAvailable(warrantyAvailable);
        if(repairWarrantyAvailable != null)
            device.setRepairWarrantyAvailable(repairWarrantyAvailable);
        if(client != null)
            device.setClient(client);

        return device.updateDevice();
    }

    /**
     * Создание пользователя, которого еще не существует
     * @param name Имя
     * @param surname Фамилия
     * @param patronymic Отчество
     * @param phone Телефон
     * @param eMail Электронный адрес
     * @param login Логин
     * @param pwd Пароль
     * @return Результат создания
     */
    public boolean createClient(String name, String surname,
                                String patronymic, String phone,
                                String eMail, String login, String pwd){
        Client client = new Client(name, surname, patronymic, login);
        client.setPhoneNumber(phone);
        client.seteMail(eMail);
        return client.addUser(pwd);
    }
}
