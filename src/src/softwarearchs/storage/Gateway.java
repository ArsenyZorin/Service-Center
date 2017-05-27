package softwarearchs.storage;

/**
 * Created by zorin on 22.05.2017.
 */

import java.sql.*;
import java.util.*;
import java.util.Date;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gateway {

    private static final String mysql_url = "jdbc:mysql://localhost:3306/servicecenter";
    private static final String mysql_user = "root";
    private static final String mysql_password = "root";

    private static Connection con;
    private static Statement stmt;

    private static Gateway gateway;

    public Gateway () throws SQLException{
        this.con = DriverManager.getConnection(mysql_url, mysql_user, mysql_password);
        this.stmt = this.con.createStatement();
    }

    public static Gateway getGateway() throws SQLException{
        if(gateway == null)
            gateway = new Gateway();
        return gateway;
    }

    public Connection getConnection(){ return this.con; }

    public Statement getStatement() { return this.stmt; }
}
