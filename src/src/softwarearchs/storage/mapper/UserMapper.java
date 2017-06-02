package softwarearchs.storage.mapper;

import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        ResultSet rs = insert.executeQuery();
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

        String statement = "SELECT * from users WHERE Login = \"" + login + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null) return null;

        users.add(user);
        return user;
    }

    public boolean isLoginValid(String login) throws SQLException {
        return findUser(login) == null;
    }

    public static User findUser(int id)  throws SQLException{
        for (User user : users){
            if (id ==user.getId())
                return user;
        }

        String statement = "SELECT FROM users WHERE id = \"" + id + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();
        if (!rs.next()) return null;
        User user = getUser(rs);

        if(user == null)
            return null;

        users.add(user);
        return user;
    }

    public static List<User> findAll() throws SQLException{
        List<User> allUsers = new ArrayList<>();

        String query = "SELECT * FROM users;";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next())
            allUsers.add(findUser(rs.getInt("id")));

        return allUsers;
    }

    public boolean updateUser(User user) throws SQLException {

        String statement = "UPDATE users SET Name = " + user.getName() + ", Surname = " + user.getSurname()
                + ", Patronymic = " + user.getPatronymic() + ", Phone number = " + user.getPhoneNumber()
                + ", E-mail = " + user.geteMail() + ", Login = " + user.getLogin()
                + ", Role = " + user.getClass().getName() + " WHERE id = " + user.getId() + ";";
        PreparedStatement updateStatement = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = updateStatement.executeQuery();

        if(!rs.next()) return false;

        User oldUser = null;
        for (User old : users)
            if (old.getId() == user.getId()) {
                oldUser = old;
                break;
            }
        if (oldUser != null)
            users.remove(oldUser);
        users.add(user);
        return true;
    }

    public boolean signIn(String login, String pwd) throws SQLException {
        String statement = "SELECT * FROM users WHERE Login = \"" + login + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();

        if(!rs.next()) return false;

        return rs.getString("Password").equals(pwd);
    }

    public void clear(){ users.clear(); }

}
