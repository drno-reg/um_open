package UM.RU.Scheduler;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;


//import org.apache.poi.ss.formula.functions.Count;
import UM.RU.DB.OracleConnector;
import UM.RU.DB.SQLiteConnector;


public class MainTaskRunner {
    // схема классов
    // MainTaskRunner -> MainTaskExecutor -> ChildTaskExecutor -> RunExternalApplication
    public static void test_cmdrun(String args[]) throws InterruptedException {
        // public static void main(String args[]) throws InterruptedException {
        String TypeDB=args[0];

        Map CMDRunNew = null;
        try {
            // CMDRunNew = RU.Scheduler.RunExternalApplication.CMDRun("ping localhost");
            String STATUS = (String) CMDRunNew.get("STATUS");
            String RESPONCE = (String) CMDRunNew.get("RESPONCE");
            System.out.println(STATUS+RESPONCE);
        }
        catch (Exception e) {
        }
    }

    //  static private boolean shutdownFlag = false;

    public static void main(String args[]) throws InterruptedException {
// принудительный update статусов заданий в БД
        String SoftName="Um_Scheduler 4.1.1";
        String RunUpdate=null;
        String Lang=null;
        String TypeDB=null;
        String FileName=null;
        String DB_UserName ="";
        String DB_Password ="";
        String DB_URL_Connection="";
        Map Connect_Settings=null;
        // параметр выбора БД
        // доступны значения: Oracle, SQLLite
        String UpdateScript[]={"UPDATE um_tasks_dir SET PHASE = 'Stop'"};
        Date dt = new Date();
        Calendar CurrentTime = Calendar.getInstance();
        SimpleDateFormat FullDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        CurrentTime.setTime(dt);
        try {
            Lang=args[0];
            TypeDB = args[1];
            FileName=args[2];
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: "+SoftName+".");
            if ("ENG".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Start main process. Connecting to DB "+TypeDB+".");
            }
            if ("RUS".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Запуск главного процесса. Соединяемся с БД "+TypeDB+".");
            }
            if (TypeDB.equals("Oracle"))
            {
                // RunUpdate= OracleConnector.Update(UpdateScript);
                // считывание информации о подключении к БД Oracle
                Connect_Settings=UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(FileName);
                DB_UserName = (String) Connect_Settings.get("DB_UserName");
                DB_Password = (String) Connect_Settings.get("DB_Password");
                DB_URL_Connection = (String) Connect_Settings.get("DB_URL_Connection");
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Try first Update. ");
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Попытка первого Update. ");
                }
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: "+ OracleConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, UpdateScript));
            }
            if (TypeDB.equals("SQLite"))
            {
                // RunUpdate= SQLiteConnector.Update(UpdateScript);
                Connect_Settings= UM.RU.FilesDirs.ReadingFiles.LoadCFGMap(FileName);
                DB_UserName = (String) Connect_Settings.get("DB_UserName");
                DB_Password = (String) Connect_Settings.get("DB_Password");
                DB_URL_Connection = (String) Connect_Settings.get("DB_URL_Connection");
                if ("ENG".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: First Update "+ SQLiteConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, UpdateScript));
                }
                if ("RUS".equals(Lang)){
                    System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Первый Update "+ SQLiteConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, UpdateScript));
                }
            }
            Timer MainTimer = new Timer();
            MainTaskExecutor MainTaskExecutorNew = new MainTaskExecutor(Lang, TypeDB, DB_UserName, DB_Password, DB_URL_Connection);
            MainTimer.schedule(MainTaskExecutorNew, 0, 10000); // Создаем задачу с повторением через 10 сек.
            //System.out.println(RunUpdate);
        } catch (Exception e) {
            System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: "+SoftName+".");
            if ("ENG".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Absent required imput parameters: DBMS Oracle/SQLite; path to file configuration by connection Oracle/SQLite. ERROR: "+e.toString());
            }
            if ("RUS".equals(Lang)){
                System.out.println("["+FullDateFormat.format(CurrentTime.getTime())+"]: Не заданы обязательные параметры. СУБД: Oracle/SQLite; путь к файлу с конфигурацией подключения Oracle/SQLite. Ошибка: "+e.toString());
            }
        }
        //  Date dt = new Date();
        //  Calendar c = Calendar.getInstance();
        //  SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        //  c.setTime(dt);
        //  System.out.print(dateFormat.format(c.getTime()));
        //  c.add(Calendar.DATE, 1);
        //  dt = c.getTime();
        //  System.out.print(" +1 день: "+dateFormat.format(c.getTime()));
        //  c.add(Calendar.DATE, -2);
        //  System.out.print(" -1 день: "+dateFormat.format(c.getTime()));
        // запуск главного задания

     /*   for (int i = 0; i <= 5; i++) {
            Thread.sleep(3000);
            System.out.println("Execution in Main Thread. #" + i);
            if (i == 5) {
                System.out.println("Application Terminates");
                System.exit(0);
            }
            */
    }


}