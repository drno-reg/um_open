package UM.RU.Test;

import java.io.IOException;
import java.util.Map;

public class Get {

    public static void main (String[] args) throws IOException {
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

//        UM.RU.DB.GetNodes getNodes=new UM.RU.DB.GetNodes(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
//        String Result=getNodes.CreateNodes("4");

        UM.RU.DB.GetMetrics getMetrics=new UM.RU.DB.GetMetrics(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
        String Result=getMetrics.CreateMetrics("4","1");
        System.out.println(Result);
    }

}
