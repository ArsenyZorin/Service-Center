package softwarearchs.user;

import softwarearchs.integration.emailNotification.Notifications;
import softwarearchs.repair.Receipt;

import javax.mail.internet.AddressException;

/**
 * Created by arseny on 07.04.17.
 */
public abstract class User {

    /* User information */
    protected int id;
    protected String name;
    protected String surname;
    protected String patronymic;

    protected String phoneNumber;
    protected String eMail;

    protected String login;
    protected boolean authenticated;

    public User(int id, String name, String surname, String patronymic, String login) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;

        this.login = login;
        this.authenticated = false;
    }

    public User(String name, String surname, String patronymic, String login) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;

        this.login = login;
        this.authenticated = false;
    }

    public int getId() { return this.id; }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String geteMail() {
        return eMail;
    }
    public String getLogin() {
        return login;
    }
    public String getFIO() { return this.name + " " + this.surname + " " + this.patronymic; }

    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public void setLogin(String login) {
        this.login = login;
    }


    public boolean registrationNotification(String pwd) throws AddressException{
        Notifications nots = new Notifications(login, getFIO(), eMail, pwd);
        if(!nots.verify("registration"))
            throw new AddressException("Invalid email address");

        return true;
    }
}
