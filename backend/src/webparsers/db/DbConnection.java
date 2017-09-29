/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webparsers.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vassilii
 */
public class DbConnection {
    private final static String url = "jdbc:sqlite:/home/vassilii/tmp/test/chck_rule.sql";
    private static Connection connection = null;
    static {
        try {
            Map Settings = UM.RU.Config.LoadSettings.Settings("settings.cfg");
            String LoginType = (String) Settings.get("LoginType");
            String FileNameDB = (String) Settings.get("FileNameDB");
            String DB_UserName = (String) Settings.get("DB_UserName");
            String DB_Password = (String) Settings.get("DB_Password");
            String DB_URL_Connection = (String) Settings.get("DB_URL_Connection");
            String ClassDriverName = (String) Settings.get("ClassDriverName");
            String LDAP_Domain = (String) Settings.get("LDAP_Domain");
            String LDAP_Host = (String) Settings.get("LDAP_Host");
            String LDAP_SearchBase = (String) Settings.get("LDAP_SearchBase");
            connection = DriverManager.getConnection(DB_URL_Connection);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getConnection(){
        return connection;
    }
    
}
