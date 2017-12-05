package UM.RU.DB;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OracleConnector {


    //-------------------------
    // метод Update
    //------------------------
    public static String Update(String Lang, String DB_UserName, String DB_Password, String DB_URL_Connection, String [] arg) throws IOException
    {
        String Update=null;
        String Update_Text = arg[0];
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            Update ="Driver not found:" + e + e.getMessage() +".";
        }
        try
        {
            Connection conn = DriverManager.getConnection(DB_URL_Connection, DB_UserName, DB_Password);
            // String Update_Text = "UPDATE tasks_dir SET PHASE = 'Стоп'";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(Update_Text);
            preparedStmt.executeUpdate();
            conn.close();
            if ("ENG".equals(Lang)){
                Update="Update completed";
            }
            if ("RUS".equals(Lang)){
                Update="Update выполнен";
            }
        }
        catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");
            if ("ENG".equals(Lang)){
                Update ="Error "+e+" try Update "+Update_Text;
            }
            if ("RUS".equals(Lang)){
                Update ="Ошибка "+e+" при попытке Update "+Update_Text;
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
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            Select ="Driver not found:" + e + e.getMessage() +".";
        }
        try {
            Connection conn = DriverManager.getConnection (DB_URL_Connection, DB_UserName, DB_Password);
            Statement stmt = conn.createStatement();
            ResultSet rs;
            int num=0;
            //stmt.execute("insert into users (name) values ('Новое имя')"); hh24:mi:ss dd.mm.yyyy
            rs = stmt.executeQuery("select ID, PATH, FREQUENCY, FREQUENCY_TYPE, PHASE, to_char(a.NEXT_START, 'hh24:mi dd.mm.yyyy') as NEXT_START  FROM um_tasks_dir a WHERE a.status='Активный' /*and a.PHASE='Stop'*/ order by a.ID");
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
        String Insert_Text ="";
// set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        java.util.Date date = new java.util.Date();
        //ContentValues initialValues = new ContentValues();
        //initialValues.put("date_created", dateFormat.format(date));
        //long rowId = mDb.insert(DATABASE_TABLE, null, initialValues);
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            Insert ="Driver not found:" + e + e.getMessage() +".";
        }
        try {
            Connection conn = DriverManager.getConnection (DB_URL_Connection, DB_UserName, DB_Password);
            Insert_Text = "insert into um_tasks (id, id_task, status, result, start_date, end_date, duration) VALUES (TASK_ID_S.nextval, "+arg[0]+", '"+arg[1]+"', '"+arg[2]+"', to_date('"+arg[3]+"','hh24:mi:ss dd.mm.yyyy'), to_date('"+arg[4]+"','hh24:mi:ss dd.mm.yyyy'), "+arg[5]+")";
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(Insert_Text);
            preparedStmt.execute();
            conn.close();
            if ("ENG".equals(Lang)){
                Insert="Insert completed";
            }
            if ("RUS".equals(Lang)){
                Insert="Insert выполнен";
            }
            conn.close();
        }
        catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");
            if ("ENG".equals(Lang)){
                Insert="Error try Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text;
            }
            if ("RUS".equals(Lang)){
               // System.out.println("Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text);
                Insert="Ошибка при попытке Insert: ChildTaskExecutor.class.getName()  (" + e.getClass().getName() + ") " + e.getMessage()+" "+Insert_Text;
            }
        }
        return Insert;
    }
}
