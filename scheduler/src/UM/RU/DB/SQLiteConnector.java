package UM.RU.DB;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


public class SQLiteConnector {


    //-------------------------
    // метод Update
    //------------------------
    public static String Update(String Lang, String DB_UserName, String DB_Password, String DB_URL_Connection, String [] arg) throws IOException
    {
        String Update=null;
        String Update_Text = arg[0];
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Update ="Driver not found:" + e + e.getMessage() +".";
        }
        try {
            Connection conn = DriverManager.getConnection(DB_URL_Connection, DB_UserName, DB_Password);
            // String Update_Text = "UPDATE tasks_dir SET PHASE = 'Стоп'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(Update_Text);
            preparedStmt.executeUpdate();
            conn.close();
            if ("ENG".equals(Lang)){
                Update="Update completed.";
            }
            if ("RUS".equals(Lang)){
                // System.out.println("Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text);
                Update="Update выполнен.";
            }
        }
        catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");

            if ("ENG".equals(Lang)){
                Update ="Parameters: DB_URL_Connection="+DB_URL_Connection+", DB_UserName="+", DB_Password="+DB_Password+". Error "+e+" try Update "+Update_Text;
            }
            if ("RUS".equals(Lang)){
                // System.out.println("Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text);
                Update ="Параметры: DB_URL_Connection="+DB_URL_Connection+", DB_UserName="+", DB_Password="+DB_Password+". Ошибка "+e+" при попытке Update "+Update_Text;
            }
        }
        return Update;
    }
    //------------------------------------------------
    // метод Select для выборки информации о заданиях
    //------------------------------------------------
    public static Map Select(String Lang, String DB_UserName, String DB_Password, String DB_URL_Connection) throws IOException
    {
        String Select = "";
        HashMap res = new HashMap();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Select ="Driver not found:" + e + e.getMessage() +".";
        }
        try {
            Connection conn = DriverManager.getConnection (DB_URL_Connection, DB_UserName, DB_Password);
            Statement stmt = conn.createStatement();
            ResultSet rs;
            int num=0;
            //stmt.execute("insert into users (name) values ('Новое имя')");
            rs = stmt.executeQuery("select * FROM um_tasks_dir a WHERE a.status='Активный' order by a.ID");
            while ( rs.next() ) {
                //  String num = rs.getString("num");
                num=num+1;
                res.put("ID"+num, rs.getString("id"));
                res.put("PATH"+num, rs.getString("path"));
                res.put("FREQUENCY"+num, rs.getString("frequency"));
                res.put("FREQUENCY_TYPE"+num, rs.getString("frequency_type"));
                res.put("PHASE"+num, rs.getString("phase"));
                res.put("NEXT_START"+num, rs.getString("next_start"));
            }
            res.put("STATUS", "OK");
            conn.close();
        } catch (Exception e) {
            res.put("STATUS", "ERROR: "+e);
        }
        return res;
    }
    //---------------------------------------------------------------------------
    // метод Insert для добавления информации о результате выполнения задания
    //---------------------------------------------------------------------------
    public static String Insert(String Lang, String DB_UserName, String DB_Password, String DB_URL_Connection, String [] arg) throws IOException
    {
        String Insert = "";
// set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        java.util.Date date = new java.util.Date();
        //ContentValues initialValues = new ContentValues();
        //initialValues.put("date_created", dateFormat.format(date));
        //long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            Insert ="Driver not found:" + e + e.getMessage() +".";
        }
        try
        {
            Connection conn = DriverManager.getConnection (DB_URL_Connection, DB_UserName, DB_Password);
            String Insert_Text = "insert into tasks (id_task, status, result, start_date, end_date, duration) VALUES (?, ?, ?, ?, ?, ?)";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(Insert_Text);
            preparedStmt.setString (1, arg[0]);
            preparedStmt.setString (2, arg[1]);
            preparedStmt.setString (3, arg[2]);
            preparedStmt.setString (4, arg[3]);
            preparedStmt.setString (5, arg[4]);
            preparedStmt.setString (6, arg[5]);
            preparedStmt.execute();
            conn.close();
            if ("ENG".equals(Lang)){
                Insert="Insert completed.";
            }
            if ("RUS".equals(Lang)){
                // System.out.println("Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text);
                Insert="Insert выполнен.";
            }
            conn.close();
        }
        catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");
            if ("ENG".equals(Lang)){
                Insert ="Error "+e+" try Insert";
            }
            if ("RUS".equals(Lang)){
                // System.out.println("Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text);
                Insert ="Ошибка "+e+" при попытке Insert";
            }
        }
        return Insert;
    }

}
