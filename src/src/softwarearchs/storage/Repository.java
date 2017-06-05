package softwarearchs.storage;

import softwarearchs.additional.Device;
import softwarearchs.invoice.BankAccount;
import softwarearchs.invoice.Invoice;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.Client;
import softwarearchs.user.User;

import java.util.HashMap;

/**
 * Created by arseny on 09.04.17.
 */
public class Repository {

    private static HashMap<User, String> pwds = new HashMap<>();
    private static HashMap<String, Device> devices = new HashMap<>();
    private static HashMap<String, Receipt> receipts = new HashMap<>();
    private static HashMap<BankAccount, Client> bankAccounts = new HashMap<>();
    private static HashMap<Invoice, Receipt> invoices = new HashMap<>();
    /**
     * Добавление пользователя в базу
     * @param user Информация о пользователе
     * @param pwd Пароль
     * @return Возвращает результат добавления пользователя в базу
     */
    public static boolean addUser(User user, String pwd){
        if(pwds.containsKey(user)){
            System.out.println("Такой пользователь уже существует в базе");
            return false;
        }
        pwds.put(user, pwd);

        System.out.println("Пользователь добавлен в базу");
        return true;
    }

    /**
     * Поиск пользователя
     * @param login Логин по которосу нужно искать пользователя
     * @return Результат поиска
     */
    public static User findUser(String login){
        for (User user : pwds.keySet()){
            if(user.getLogin().equals(login))
                return user;
        }
        return null;
    }

    /**
     * Проверка логина на свободность
     * @param login Логин для проверки
     * @return Возвращает результат проверки
     */
    public static boolean validLogin(String login){
        for(User user : pwds.keySet())
            if (login.equals(user.getLogin()))
                return false;

        return true;
    }

    /**
     * Добавление устройства в базу
     * @param serialNumber Серийный номер устройства
     * @param device Описание устройства
     * @return Возвращает результат добавления устройства в баззу
     */
    public static boolean addDevice (String serialNumber, Device device){
        if(devices.containsKey(serialNumber)){
            System.out.println("Такое устройство уже существует в базе");
            return false;
        }

        devices.put(serialNumber, device);

        System.out.println("Устройство добавлено в базу");
        return true;
    }

    /**
     * Обновение информации об устройстве
     * @param device Обновленное устройство
     * @return Результат операции
     */
    public static boolean updateDevice(Device device){
        if (devices.containsKey(device.getSerialNumber())) {
            boolean res = devices.replace(device.getSerialNumber(),
                    devices.get(device.getSerialNumber()), device);
            if (res){
                System.out.println("Устройство обновлено");
                return res;
            }
            else
                return false;
        }
        else
            return addDevice(device.getSerialNumber(), device);
    }

    /**
     * Получить устройство с заданным серийным номером
     * @param serialNumber Серийный номер
     * @return Устройство с серийным номером, если такое имеется
     */
    public static Device findDevice(String serialNumber) {
        if(devices.containsKey(serialNumber))
            return devices.get(serialNumber);

        return null;
    }

    /**
     * Аутентификация пользователя
     * @param login Логин пользователя
     * @param pwd Пароль пользователя
     * @return Результат аутентификации
     */
    public static boolean signIn(String login, String pwd) {
        for (User user : pwds.keySet()) {
            if (user.getLogin().equals(login))
                return pwds.get(user).equals(pwd);
        }
        return false;
    }

    /**
     * Добавление квитанции
     * @param receipt Квитанция, которую необходимо добавить
     * @return Результат операции
     */
    public static boolean addReceipt(Receipt receipt){
        if(receipts.containsKey(receipt.getReceiptNumber())){
            System.out.println("Такая квитанция уже существует в базе");
            return false;
        }

        receipts.put(receipt.getReceiptNumber(), receipt);
        System.out.println("Квитанция добавлена в базу");
        return true;
    }

    /**
     * Обновление квитанции
     * @param receipt Обновленная квитанция
     * @return Результат операции
     */
    public static boolean updateReceipt(Receipt receipt) {
        if (receipts.containsKey(receipt.getReceiptNumber())) {
            boolean res = receipts.replace(receipt.getReceiptNumber(),
                    receipts.get(receipt.getReceiptNumber()), receipt);
            if (res) {
                System.out.println("Квитанция обновлена");
                return res;
            }
            else
                return false;
        }
        else
            return addReceipt(receipt);
    }

    /**
     * Поиск квитанции по ее номеру
     * @param receiptNumber Номер квитанции
     * @return Квитанция, если такая имеется
     */
    public static Receipt findReceipt(String receiptNumber) {
        if(receipts.containsKey(receiptNumber))
            return receipts.get(receiptNumber);
        return null;
    }

    /**
     * Добавление банковского счета в базу
     * @param bankAccount Банковский счет
     * @param client Информация о клиенте
     * @return Результат операции
     */
    public static boolean addBankAccount(BankAccount bankAccount, Client client){
        if(bankAccounts.containsKey(bankAccount)){
            System.out.println("Такой банковский счет уже существует в базе");
            return false;
        }

        bankAccounts.put(bankAccount, client);
        System.out.println("Банковский счет добавлен в базу");
        return true;
    }

    /**
     * Добавление счета за ремонт в базу
     * @param invoice Счет за ремонт
     * @param receipt Квитанция, к которой он относится
     * @return Результат операции
     */
    public static boolean addInvoice(Invoice invoice, Receipt receipt){
        if(invoices.containsKey(invoice)){
            System.out.println("Счет с таким номером уже есть в базе");
            return false;
        }

        invoices.put(invoice, receipt);
        System.out.println("Счет за ремонт добавлен в базу");
        return true;
    }

    /**
     * Очищение хранилища
     */
    public static void clear(){
        pwds.clear();
        devices.clear();
        receipts.clear();
        bankAccounts.clear();
        invoices.clear();
    }
}
