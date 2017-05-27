package softwarearchs.user;

/**
 * Created by arseny on 07.04.17.
 */
public class Client extends User {
    public Client(int id, String name, String surname, String patronymic, String login){
        super(id, name, surname, patronymic, login);
    }

    public Client(String name, String surname, String patronymic, String login){
        super(name, surname, patronymic, login);
    }
}
