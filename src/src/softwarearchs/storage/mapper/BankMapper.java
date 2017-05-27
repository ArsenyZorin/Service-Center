package softwarearchs.storage.mapper;

import softwarearchs.invoice.BankAccount;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class BankMapper {

    private static Set<BankAccount> accounts = new HashSet<>();

    public boolean addAccount(BankAccount account) throws SQLException {

        if(findAccount(account.getAccountNumber()) != null )
            return false;

        String statement = "INSERT INTO bankaccount VALUES (" + account.getAccountNumber() +
                ", " + account.getClient() + ", " + account.getValidTill() +
                ", " + account.getCvc() + ");";

        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.execute();
        ResultSet rs = insert.getGeneratedKeys();
        accounts.add(account);
        return true;
    }

    public static BankAccount findAccount(int accountNumber) throws SQLException{
        for(BankAccount account : accounts)
            if(accountNumber == account.getAccountNumber())
                return account;

        String statement = "SELECT * FROM bankaccount WHERE id = " + accountNumber +";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        find.execute();
        ResultSet rs = find.getGeneratedKeys();

        if(!rs.next()) return null;

        BankAccount account = new BankAccount(accountNumber);
        account.setClient((Client)UserMapper.findUser(rs.getString("Client")));
        account.setValidTill(rs.getDate("Date"));
        account.setCvc(rs.getInt("CVC"));

        accounts.add(account);
        return account;

    }

}
