package UM.RU.Scheduler;

import UM.RU.DB.SQLiteConnector;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 Таск главного задания
 */
public class MainTaskExecutor extends TimerTask {
    // Date now;
    private String Lang;
    private String TypeDB;
    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;

    public MainTaskExecutor(String Lang, String TypeDB, String DB_UserName, String DB_Password, String DB_URL_Connection) {
        this.Lang = Lang;
        this.TypeDB = TypeDB;
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
    }

    // Добавляем главный таск
    @Override
    public void run() {
        String ID = "";
        String PATH = "";
        String FREQUENCY = "";
        String FREQUENCY_TYPE = "";
        String Log_Text = "";
        Map<String, String> SelectNew = null;
        try {
            // выборка информации о заданиях из БД
            if (TypeDB.equals("Oracle")) {
                SelectNew = UM.RU.DB.OracleConnector.Select(Lang, DB_UserName, DB_Password, DB_URL_Connection);
            }
            if (TypeDB.equals("SQLite")) {
                SelectNew = SQLiteConnector.Select(Lang, DB_UserName, DB_Password, DB_URL_Connection);
            }
            Set<Map.Entry<String, String>> set = SelectNew.entrySet();
            int counter = 0;
// Отобразим набор
            for (Map.Entry<String, String> me : set) {
                counter = counter + 1;
                //    System.out.print(me.getKey() + ": ");
                //    System.out.println(me.getValue());
            }
            //System.out.println("Всего элементов - "+(counter-1));
            //System.out.println("Количество заданий - "+(counter/4));
            for (int x = 1; x <= (counter - 1) / 6; x = x + 1) {

                //ID=(String) SelectNew.get("ID"+x);
                //PATH=(String) SelectNew.get("PATH"+x);
                //System.out.print(SelectNew.get("PATH" + x));
                //FREQUENCY=(String) SelectNew.get("FREQUENCY"+x);
                //System.out.print(SelectNew.get("FREQUENCY" + x));
                //FREQUENCY_TYPE=(String) SelectNew.get("FREQUENCY_TYPE"+x);
                //System.out.print(SelectNew.get("FREQUENCY_TYPE" + x));
                Date dt = new Date();
                Calendar CurrentTime = Calendar.getInstance();
                Calendar NewTime = Calendar.getInstance();
                SimpleDateFormat ShortDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                SimpleDateFormat FullDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                CurrentTime.setTime(dt);
                NewTime.setTime(dt);
                // запись в log информации о найденных заданиях
                if ((SelectNew.get("PHASE" + x).equals("Waiting"))) {
                    if ("ENG".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Finded task " + SelectNew.get("ID" + x) + "; phase - " + SelectNew.get("PHASE" + x) + "; start task planned on " + SelectNew.get("NEXT_START" + x));
                    }
                    if ("RUS".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Найдено задание " + SelectNew.get("ID" + x) + "; фаза - " + SelectNew.get("PHASE" + x) + "; запуск запланирован на " + SelectNew.get("NEXT_START" + x));
                    }
                }
                // если подошло время выполнения задания
                //System.out.println(SelectNew.get("NEXT_START" + x));
                //System.out.println(ShortDateFormat.format(CurrentTime.getTime()));
                if (
                        (SelectNew.get("PHASE" + x).equals("Waiting")) &&
                        (SelectNew.get("NEXT_START" + x).equals(ShortDateFormat.format(CurrentTime.getTime())))
// не нравится идея деления на типы, попытаюсь 10:30 [пн;ср] превратить в 10:30 06:12:2017
//                        &&
//                        (
//                        (SelectNew.get("FREQUENCY_TYPE" + x).equals("Минуты"))
//                        )
) {
                    if ("ENG".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Launch time " + FullDateFormat.format(CurrentTime.getTime()) + " for run task " + SelectNew.get("ID" + x)+".");
                    }
                    if ("RUS".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Подошло время " + FullDateFormat.format(CurrentTime.getTime()) + " для исполнения задания " + SelectNew.get("ID" + x)+".");
                    }
                    if (TypeDB.equals("Oracle")) {
                        // RunUpdate = RU.Servlet.DB.OracleConnector.Update(update); // update[0]
                        String update[] = {"UPDATE um_tasks_dir SET NEXT_START=to_date('" + ShortDateFormat.format(NewTime.getTime()) + "','hh24:mi dd.mm.yyyy'), PHASE='Progress' WHERE ID=" + SelectNew.get("ID" + x)};
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". " + UM.RU.DB.OracleConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, update));
                    }
                    if (TypeDB.equals("SQLite")) {
                        // RunUpdate = RU.Servlet.DB.SQLiteConnector.Update(update); // update[0]
                        String update[] = {"UPDATE um_tasks_dir SET NEXT_START='" + ShortDateFormat.format(NewTime.getTime()) + "', PHASE='Progress' WHERE ID=" + SelectNew.get("ID" + x)};
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". " + SQLiteConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, update));
                    }
                    // создание потока
                    ChildTaskExecutor thread = new ChildTaskExecutor(Integer.parseInt(SelectNew.get("ID" + x)), SelectNew.get("PATH" + x), Lang, TypeDB, DB_UserName, DB_Password, DB_URL_Connection);
                    //  thread.start();
                    //  try {
                    //      thread.join();
                    // действия после завершения работы потока
                    //  } catch (InterruptedException e) {
                    //  }
                }
                // блок обновлениея расписания
                if (SelectNew.get("PHASE" + x).equals("Stop")) {
                    if ("ENG".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Finded task " + SelectNew.get("ID" + x) + "; phase - " + SelectNew.get("PHASE" + x) + "; last start was " + SelectNew.get("NEXT_START" + x));
                    }
                    if ("RUS".equals(Lang)){
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: Найдено задание " + SelectNew.get("ID" + x) + "; фаза - " + SelectNew.get("PHASE" + x) + "; последний запуск был " + SelectNew.get("NEXT_START" + x));
                    }
                    //System.out.print(dateFormat.format(c.getTime()));
                    NewTime.add(Calendar.MINUTE, Integer.parseInt(SelectNew.get("FREQUENCY" + x)));
                    String RunUpdate = null;
                    if ("ENG".equals(Lang)){
                        Log_Text = "Updating schedule task: " + SelectNew.get("ID" + x) + " time start planned on " + ShortDateFormat.format(NewTime.getTime());
                    }
                    if ("RUS".equals(Lang)){
                        Log_Text = "Обновление расписания задания: " + SelectNew.get("ID" + x) + " время запуска запланировано на " + ShortDateFormat.format(NewTime.getTime());
                    }
                    if (TypeDB.equals("Oracle")) {
                        // RunUpdate = RU.Servlet.DB.OracleConnector.Update(update); // update[0]
                        String update[] = {"UPDATE um_tasks_dir SET NEXT_START=to_date('" + ShortDateFormat.format(NewTime.getTime()) + "','hh24:mi dd.mm.yyyy'), PHASE='Waiting' WHERE ID=" + SelectNew.get("ID" + x)};
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". " + UM.RU.DB.OracleConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, update));
                    }
                    if (TypeDB.equals("SQLite")) {
                        // RunUpdate = RU.Servlet.DB.SQLiteConnector.Update(update); // update[0]
                        String update[] = {"UPDATE um_tasks_dir SET NEXT_START='" + ShortDateFormat.format(NewTime.getTime()) + "', PHASE='Waiting' WHERE ID=" + SelectNew.get("ID" + x)};
                        System.out.println("[" + FullDateFormat.format(CurrentTime.getTime()) + "]: " + Log_Text + ". " + SQLiteConnector.Update(Lang, DB_UserName, DB_Password, DB_URL_Connection, update));
                    }
                    // System.out.println();
                }
            }
        } catch (Exception e) {
            if ("ENG".equals(Lang)){
                System.out.println("Error " + e + " try to select from tasks directory.");
            }
            if ("RUS".equals(Lang)){
                System.out.println("Ошибка " + e + " при попытке выборки из справочника заданий.");            }
        }
        //   Timer time = new Timer();
        //   ChildTaskExecutor ChildTaskExecutorNew = new ChildTaskExecutor();
        //   time.schedule(ChildTaskExecutorNew, 0, 10000*18); // Создаем задачу с повторением через 10 сек.
    }

}