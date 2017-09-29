/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UM.RU.DB;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vassilii
 */
public class GetTasks {
    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;
    private String ClassDriverName;
    //    private String username;
//    private String password;
    private Connection connection;
    
    private final String QUERY = "SELECT m.url, r.status_code, r.is_success, r.message, r.loading_time, "
                                      + "r.execute_started, r.execute_end, r.page_size, r.javascript_errors, t.name\n" +
"FROM um_results r INNER JOIN um_mentrix m ON r.check_id = m.id \n" +
"                                       INNER JOIN um_cabints c ON m.cabinet_id = c.id\n" +
"				       INNER JOIN um_templates t ON m.template_id = t.id WHERE c.user_id = ?";

    public GetTasks(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        this.ClassDriverName = ClassDriverName;
        connection = ConnectionManager.getConnection(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
    }
    
   public String getList(String userId){
        List<Map<String, String> > res = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
            // Parameters start with 1
            preparedStatement.setString(1,userId);
            ResultSet result=preparedStatement.executeQuery();
            
            while (result.next()) {
                Map<String, String> mp = new HashMap<>();
                mp.put("url", result.getString("url"));
                mp.put("status_code", result.getString("status_code"));
                mp.put("is_success", result.getString("is_success"));
                mp.put("message", result.getString("message"));
                mp.put("page_size", result.getString("page_size"));
                mp.put("javascript_errors", result.getString("javascript_errors"));
                mp.put("loading_time", result.getString("loading_time"));
                mp.put("execute_started",getStringDate( result.getLong("execute_started")) );
                mp.put("execute_end",getStringDate( result.getLong("execute_end")) );
                mp.put("name", result.getString("name"));
                res.add(mp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
       // return gson.toJson(json);
        return gson.toJson(res);
   }
   
   private String getStringDate(long time){
       Date dt = new Date(time);
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
       return df.format(dt);
   }
}
