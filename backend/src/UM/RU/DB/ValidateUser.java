package UM.RU.DB;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ValidateUser {

    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;
    private String ClassDriverName;
//    private String username;
//    private String password;
    private Connection connection;

    public ValidateUser(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        this.ClassDriverName = ClassDriverName;
        connection = ConnectionManager.getConnection(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
    }

    //---------------------------------
    // метод Select - для валидации УЗ
    //---------------------------------
    public static Map CheckUser(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName, String username, String password) throws IOException
    {
        String CheckUser = "";
        String Select_Text ="select * FROM UM_USERS a WHERE username=? and password=?";
        HashMap res = new HashMap();
        try {
            Class.forName(ClassDriverName);
        } catch (ClassNotFoundException e) {
            CheckUser ="Driver not found:" + e + e.getMessage() +".";
            res.put("validation_error", CheckUser);
        }
        try (Connection conn = DriverManager.getConnection (DB_URL_Connection, DB_UserName, DB_Password)){
            PreparedStatement stat=conn.prepareStatement(Select_Text);
            stat.setString(1,username);
            stat.setString(2,password);
            int num=0;
            //stmt.execute("insert into users (name) values ('Новое имя')");
            ResultSet result=stat.executeQuery();
            while ( result.next() ) {
                //  String num = rs.getString("num");
                System.out.println(result.getString("first_name"));
                res.put("first_name", result.getString("first_name"));
                res.put("last_name", result.getString("last_name"));
                res.put("validation_error", "");
            }
            if (res.get("first_name")!=null) {
                res.put("validation_error", "Авторизация прошла успешно!");
            }
            if (res.get("first_name")==null) {
                res.put("validation_error", "Неверный либо имя пользователя либо пароль! "+Select_Text);
            }
            conn.close();
        } catch (Exception e) {
            res.put("validation_error", e);
            // для того чтобы ошибка ушла наверх
            throw new RuntimeException(e.getMessage());
        }
        return res;
    }

    public Map CheckUser(String username, String password) {
        HashMap res = new HashMap();
        String Select_Text ="select * FROM UM_USERS a WHERE username=? and password=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Select_Text);
            // Parameters start with 1
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet result=preparedStatement.executeQuery();
            while ( result.next() ) {
                //  String num = rs.getString("num");
                System.out.println(result.getString("first_name"));
                res.put("first_name", result.getString("first_name"));
                res.put("last_name", result.getString("last_name"));
                res.put("validation_error", "");
            }
            if (res.get("first_name")!=null) {
                res.put("validation_error", "Авторизация прошла успешно!");
            }
            if (res.get("first_name")==null) {
                res.put("validation_error", "Неверный либо имя пользователя либо пароль! "+Select_Text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}
