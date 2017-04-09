package softwarearchs.storage;

import softwarearchs.additional.Device;
import softwarearchs.receipt.Receipt;
import softwarearchs.user.User;

import java.security.SecureRandom;
import java.util.HashMap;

/**
 * Created by arseny on 09.04.17.
 */
public class Repository {

    private static HashMap<User, String> pswds = new HashMap<>();
    private static HashMap<String, Device> devices = new HashMap<>();
    private static HashMap<Integer, Receipt> receipts = new HashMap<>();

    /**
     * Добавление пользователя в базу
     * @param user Информация о пользователе
     * @param pwd Пароль
     * @return Возвращает результат добавления пользователя в базу
     */
    public static boolean addUser(User user, String pwd){
        if(pswds.containsKey(user)){
            System.out.println("Такой пользователь уже существует");
            return false;
        }
        pswds.put(user, pwd);

        System.out.println("Пользователь добавлен");
        return true;
    }

    /**
     * Проверка логина на свободность
     * @param login Логин для проверки
     * @return Возвращает результат проверки
     */
    public static boolean validLogin(String login){
        for(User user : pswds.keySet())
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
            System.out.println("Такое устройство уже существует");
            return false;
        }

        devices.put(serialNumber, device);

        System.out.println("Устройство добавлено");
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
            if (res)
                System.out.println("Устройство обновлено");
            return res;
        }
        return false;
    }

    /**
     * Аутентификация пользователя
     * @param login Логин пользователя
     * @param pwd Пароль пользователя
     * @return Результат аутентификации
     */
    public static boolean signIn(String login, String pwd) {
        for (User user : pswds.keySet()) {
            if (user.getLogin().equals(login))
                return pswds.get(user).equals(pwd);
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
            System.out.println("Такая квитанция уже существует");
            return false;
        }

        receipts.put(receipt.getReceiptNumber(), receipt);
        System.out.println("Квитанция добавлена");
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
            if (res)
                System.out.println("Квитанция обновлена");
            return res;
        }
        return false;
    }
}
