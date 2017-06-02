package softwarearchs.storage;

/**
 * Created by zorin on 22.05.2017.
 */

import java.sql.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.util.*;
import java.util.Date;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gateway {

    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String mysql_url = "jdbc:mysql://localhost:3306/servicecenter";
    private static final String mysql_user = "root";
    private static final String mysql_password = "root";
    private static MysqlDataSource dataSource;

    private static Connection con;
    private static Statement stmt;

    private static Gateway gateway;

    public Gateway () throws SQLException{
        //this.con = DriverManager.getConnection(mysql_url, mysql_user, mysql_password);
        //this.stmt = this.con.createStatement();
        dataSource = new MysqlDataSource();
        dataSource.setURL(mysql_url);
        dataSource.setUser(mysql_user);
        dataSource.setPassword(mysql_password);

        try{
            Class.forName(JDBC_DRIVER);
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static Gateway getGateway() throws SQLException{
        if(gateway == null)
            gateway = new Gateway();
        return gateway;
    }

    public Connection getConnection(){
        Connection result = null;
        try{
            result = dataSource.getConnection();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}
