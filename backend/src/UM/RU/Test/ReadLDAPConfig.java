package UM.RU.Test;


import java.io.IOException;
import java.util.Map;

public class ReadLDAPConfig {

    public static void main (String[] args) throws IOException {
        String ConnectionFileName="C:\\Server\\Repositories\\projects\\um_open\\frontend\\web\\WEB-INF\\views\\config\\ldap_connection.cfg";
        Map DBMS_Settings = UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(ConnectionFileName);
        DBMS_Settings.forEach((key, value) -> System.out.println(key + " :: " + value));
        ConnectionFileName="C:\\Server\\Repositories\\projects\\um_open\\frontend\\web\\WEB-INF\\views\\config\\settings.cfg";
        Map Settings = UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(ConnectionFileName);
        Settings.forEach((key, value) -> System.out.println(key + " :: " + value));

        Settings= UM.RU.Config.LoadSettings.Settings(ConnectionFileName);
        Settings.forEach((key, value) -> System.out.println(key + " :: " + value));
    }

}
