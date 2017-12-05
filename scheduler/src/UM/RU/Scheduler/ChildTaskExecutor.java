package UM.RU.Scheduler;

import UM.RU.DB.SQLiteConnector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


//public class ChildTaskExecutor extends TimerTask {

public class ChildTaskExecutor extends Thread {
    //implements Runnable {
    private String Path;
    private Integer ID;
    private String Lang;
    private String TypeDB;
    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;

    public ChildTaskExecutor(Integer ID, String Path, String Lang, String TypeDB, String DB_UserName, String DB_Password, String DB_URL_Connection) {
        this.ID = ID;
        this.Path = Path;
        this.Lang = Lang;
        this.TypeDB = TypeDB;
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        new Thread(this).start();
    }

    // Добавляем таск
    @Override
    public void run() {
        //String PATH = "E:\\Server\\Repositories\\projects\\examples\\out\\artifacts\\task1\\task1.bat";
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        Date dt = new Date();
        int StartTime = (int) new Date().getTime();
        String StartDateTime = dateFormat.format(dt);

        Calendar CurrentTime = Calendar.getInstance();
        SimpleDateFormat FullDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

        if ("ENG".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: "+"Begin execute task " + ID + ": " + StartDateTime);        }
        if ("RUS".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: "+"Начало выполнения задания " + ID + ": " + StartDateTime);
        }

        //String Parameter=args[0];
        //String Parameter[]={"java -jar E:\\Server\\Repositories\\projects\\examples\\out\\artifacts\\jsp_jar\\jsp.jar \"Hello Russia and World!!!\""};
        Map CMDRunNew = null;
        try {
            //   System.out.println(SQLiteConnector.Insert());
        } catch (Exception e) {

            if ("ENG".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Error " + e + " try Insert ");
            }
            if ("RUS".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Ошибка " + e + " при попытке Insert ");
            }
        }
        String STATUS = "";
        String RESPONCE = "";
        String Log_Text = "";
        CMDRunNew = UM.RU.Scheduler.RunExternalApplication.CMDRun(Path);
        STATUS = (String) CMDRunNew.get("STATUS");
        RESPONCE = (String) CMDRunNew.get("RESPONCE");
        if ("ENG".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Status execute task: " + STATUS + "; result: " + RESPONCE);
        }
        if ("RUS".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Cтатус выполнения задания: " + STATUS + "; результат: " + RESPONCE);
        }
        int EndTime = (int) new Date().getTime();
        float Run_Duration = (EndTime - StartTime) / 1000;
        dt = new Date();
        String EndDateTime = dateFormat.format(dt);
        if ("ENG".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Execute main task finished: " + EndDateTime);
            System.out.print("["+FullDateFormat.format(CurrentTime.getTime())+"]: Spend time: " + Run_Duration + " c.");
        }
        if ("RUS".equals(Lang)){
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Выполнения главного задания завершено: " + EndDateTime);
            System.out.print("["+FullDateFormat.format(CurrentTime.getTime())+"]: Потрачено времени: " + Run_Duration + " c.");
        }

        String Parameter[] = {ID.toString(), STATUS, RESPONCE, StartDateTime, EndDateTime, Float.toString(Run_Duration)};

        CurrentTime.setTime(dt);

        String RunUpdate = null;
        String UpdateScript[] = {"UPDATE um_tasks_dir SET PHASE='Stop' WHERE ID=" + ID.toString()};

        if (TypeDB.equals("Oracle")) {
            // RunUpdate = RU.Servlet.DB.OracleConnector.Update(update); // update[0]
            try {
                Log_Text = UM.RU.DB.OracleConnector.Insert(Lang, DB_UserName, DB_Password, DB_URL_Connection, Parameter);
                System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". ");
            } catch (IOException e) {
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Error Insert task: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Ошибка Insert задания: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
            }
            try {
                Log_Text = UM.RU.DB.OracleConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, UpdateScript);
                System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". ");
                //    System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + "UPDATE um_tasks_dir SET PHASE='Stop' WHERE ID=" + ID.toString() + ". ");
            } catch (IOException e) {
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Error update task: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Ошибка update задания: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
            }
        }
        if (TypeDB.equals("SQLite")) {
            // RunUpdate = RU.Servlet.DB.SQLiteConnector.Update(update); // update[0]
            try {
                Log_Text = SQLiteConnector.Insert(Lang, DB_UserName, DB_Password, DB_URL_Connection, Parameter);
                System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". ");
            } catch (IOException e) {
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Error Insert task: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Ошибка Insert задания: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
            }
            try {
                Log_Text = SQLiteConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, UpdateScript);
                System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". ");
            } catch (IOException e) {
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Error update task: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Ошибка update задания: " + ChildTaskExecutor.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
                }
            }
        }
    }

}
