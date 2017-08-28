package UM.RU.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadSettings {

    public static Map Settings(String FileName) throws IOException {

        Map Settings = new HashMap();
        Map Buffer_Settings = UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(FileName);
        Settings.put("LoginType",(String) Buffer_Settings.get("LoginType"));
        String ConnectionFileNameDB = (String) Buffer_Settings.get("FileNameDB");
        String ConnectionFileNameLDAP = (String) Buffer_Settings.get("FileNameLDAP");
        String first_name = "";
        String last_name = "";
        String validation_error = "";
        String username = "";
        String password = "";

        // валидация пользователя в БД SQLite
        // считывание информации о подключении к БД
        Buffer_Settings = UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(ConnectionFileNameDB);
        Settings.put("DB_UserName",(String) Buffer_Settings.get("DB_UserName"));
        Settings.put("DB_Password",(String) Buffer_Settings.get("DB_Password"));
        Settings.put("DB_URL_Connection",(String) Buffer_Settings.get("DB_URL_Connection"));
        Settings.put("ClassDriverName",(String) Buffer_Settings.get("ClassDriverName"));
        // считывание информации о подключении к LDAP
        Buffer_Settings = UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(ConnectionFileNameLDAP);
        Settings.put("LDAP_Domain",(String) Buffer_Settings.get("LDAP_Domain"));
        Settings.put("LDAP_Host",(String) Buffer_Settings.get("LDAP_Host"));
        Settings.put("LDAP_SearchBase",(String) Buffer_Settings.get("LDAP_SearchBase"));
        //  System.out.print(DB_URL_Connection);

        return Settings;
    }

}
