package softwarearchs.storage.mapper;

import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class UserMapper {

    private static Set<User> users = new HashSet<>();

    public boolean addUser(User user, String pwd) throws SQLException{
        if(findUser(user.getId()) != null ) return false;

        String statement = "INSERT INTO users VALUES (" + user.getId() + ", " + user.getName() + ", " +
                user.getSurname() + ", " + user.getPatronymic() + ", " + user.getPhoneNumber() + ", " +
                user.geteMail() + ", " + user.getLogin() + ", " + pwd + ", " + user.getClass().getName() + ");";
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        ResultSet rs = insert.getGeneratedKeys();
        users.add(user);
        return true;
    }

    private static User getUser(ResultSet rs) throws SQLException{
        User user;
        switch (rs.getString("Role")){
            case "Receiver":
                user = new Receiver(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            case  "Master":
                user = new Master(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            case "Client":
                user = new Client(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            default:
                user = null;
                break;
        }
        if(user == null) return null;

        user.seteMail(rs.getString("E-mail"));
        user.setPhoneNumber(rs.getString("Phone number"));

        return user;
    }

    public static User findUser(String login)  throws SQLException{
        for (User user : users){
            if (login.equals(user.getLogin()))
                    return user;
        }

        String statement = "SELECT from users WHERE Login = " + login + ";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.getGeneratedKeys();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null) return null;

        users.add(user);
        return user;
    }

    public static User findUser(int id)  throws SQLException{
        for (User user : users){
            if (id ==user.getId())
                return user;
        }

        String statement = "SELECT from users WHERE id = " + id + ";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.getGeneratedKeys();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null)
            return null;

        users.add(user);
        return user;
    }
}
