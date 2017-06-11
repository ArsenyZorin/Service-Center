package softwarearchs.storage.mapper;

import softwarearchs.enums.Role;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.sql.*;
import java.util.*;

/**
 * Created by zorin on 23.05.2017.
 */
public class UserMapper {

    private static AbstractMap<String, User> users = new HashMap<>();

    public boolean addUser(User user, String pwd) throws SQLException{
        if(findUser(user.getLogin()) != null ) return false;

        String statement = "INSERT INTO users(Name, Surname, Patronymic, PhoneNumber, " +
                "Email, Login, Password, Role) VALUES (\"" + user.getName() +
                "\", \"" + user.getSurname() + "\", \"" + user.getPatronymic() +
                "\", \"" + user.getPhoneNumber() + "\", \"" + user.geteMail() +
                "\", \"" + user.getLogin() + "\", \"" + pwd +
                "\", \"" + user.getClass().getSimpleName() + "\");";
        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        users.put(user.getLogin(), user);
        return true;
    }

    private static User getUser(ResultSet rs) throws SQLException{
        User user;
        switch (Role.valueOf(rs.getString("Role"))){
            case Receiver:
                user = new Receiver(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            case  Master:
                user = new Master(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            case Client:
                user = new Client(rs.getInt("id"), rs.getString("Name"),
                        rs.getString("Surname"), rs.getString("Patronymic"),
                        rs.getString("Login"));
                break;
            default:
                user = null;
                break;
        }
        if(user == null) return null;

        user.seteMail(rs.getString("Email"));
        user.setPhoneNumber(rs.getString("PhoneNumber"));

        return user;
    }

    public static boolean deleteUser(String login) throws SQLException{
        User user = findUser(login);
        if(user == null ) return false;

        users.remove(login);
        String statement = "DELETE FROM users WHERE Login = \"" + login + "\";";

        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        return true;
    }

    public static User findUser(String login)  throws SQLException{
        if(users.containsKey(login))
            return users.get(login);

        String statement = "SELECT * from users WHERE Login = \"" + login + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null) return null;

        users.put(login, user);
        return user;
    }

    public static User findUser(String name, String surname, String patronymic)
            throws SQLException{

        for (User user : users.values()){
            if (name.equals(user.getName()) && surname.equals(user.getSurname())
                    && patronymic.equals(user.getPatronymic()))
                return user;
        }

        String statement = "SELECT * from users WHERE Name = \"" + name +
                "\" AND Surname = \"" + surname +
                "\" AND Patronymic =\"" + patronymic + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null) return null;

        users.put(user.getLogin(), user);
        return user;
    }

    public boolean isLoginValid(String login) throws SQLException {
        return findUser(login) == null;
    }

    public static User findUser(int id)  throws SQLException{
        for (User user : users.values()){
            if (id == user.getId())
                return user;
        }

        String statement = "SELECT * FROM users WHERE id = " + id + ";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null)
            return null;

        users.put(user.getLogin(), user);
        return user;
    }

    public static AbstractMap<String, User> findAll() throws SQLException{
        users.clear();
        String query = "SELECT * FROM users;";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next())
            users.put(rs.getString("Login"), findUser(rs.getString("Login")));

        return users;
    }

    public boolean updateUser(User user) throws SQLException {

        String statement = "UPDATE users SET Name = \"" + user.getName()
                + "\", Surname = \"" + user.getSurname()
                + "\", Patronymic = \"" + user.getPatronymic()
                + "\", PhoneNumber = \"" + user.getPhoneNumber()
                + "\", Email = \"" + user.geteMail()
                + "\", Role = \"" + user.getClass().getSimpleName()
                + "\" WHERE id = " + user.getId() + ";";
        PreparedStatement updateStatement = Gateway.getGateway().getConnection().prepareStatement(statement);
        if(updateStatement.executeUpdate() == 0)
            return false;

        users.replace(user.getLogin(), user);
        return true;
    }

    public boolean signIn(String login, String pwd) throws SQLException {
        String statement = "SELECT * FROM users WHERE Login = \"" + login + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();

        return rs.next() && rs.getString("Password").equals(pwd);
    }

}
