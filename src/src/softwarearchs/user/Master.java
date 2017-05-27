package softwarearchs.user;

/**
 * Created by arseny on 07.04.17.
 */
public class Master extends User {
    public Master(int id, String name, String surname, String patronymic, String login){
        super(id, name, surname, patronymic, login);
    }

    public Master(String name, String surname, String patronymic, String login){
        super(name, surname, patronymic, login);
    }
}
