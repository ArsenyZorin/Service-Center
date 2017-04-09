package softwarearchs.user;

import softwarearchs.storage.Repository;

/**
 * Created by arseny on 07.04.17.
 */
public abstract class User {

    /* User information */
    protected String name;
    protected String surname;
    protected String patronymic;

    protected String phoneNumber;
    protected String eMail;

    protected String login;
    protected boolean authenticated;

    public User(){
        this.name = null;
        this.surname = null;
        this.patronymic = null;

        this.phoneNumber = null;
        this.eMail = null;

        this.authenticated = false;
    }

    public User(String name, String surname, String patronymic){
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;

        this.authenticated = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean signIn(String login, String pwd){
        this.authenticated = new Repository().signIn(login, pwd);
        return this.authenticated;
    }

    public void signOut(){
        this.authenticated = false;
    }
}
