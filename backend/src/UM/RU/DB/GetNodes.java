package UM.RU.DB;

import UM.RU.DB.Datasets.Nodes;

import java.sql.Connection;

public class GetNodes {

    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;
    private String ClassDriverName;
    //    private String username;
//    private String password;
    private Connection connection;

    public GetNodes(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        this.ClassDriverName = ClassDriverName;
        connection = ConnectionManager.getConnection(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
    }


    public String CreateNodes(){
        String result=null;
        String Select_Text ="select * FROM UM_USERS a WHERE username=? and password=?";

        return result;
    }


}
