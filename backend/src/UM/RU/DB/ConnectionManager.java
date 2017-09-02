package UM.RU.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    public static Connection getConnection(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        try  {
            Class.forName(ClassDriverName);
            Connection con = DriverManager.getConnection(DB_URL_Connection, DB_UserName,DB_Password);
            return con;
        }
        catch(Exception ex) {
            System.out.println("ConnectionManager.getConnection() Error -->" + ex.getMessage());
            return null;
        }
    }

    public static void close(Connection con) {
        try  {
            con.close();
        }
        catch(Exception ex) {
        }
    }
}
