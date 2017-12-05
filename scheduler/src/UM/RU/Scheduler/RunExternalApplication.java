package UM.RU.Scheduler;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * запускаем внешние приложения и возвращаем результат
 */
public class RunExternalApplication {

    // метод запуска внешнего приложения
    public static Map CMDRun(String CMDPath) //throws IOException
    {
        //Charset.forName("UTF-8");
        HashMap res = new HashMap();
        //    String consoleEnc = System.getProperty("Dconsole.encoding","windows-1251");
        String Response = "";
        String line;
        //  String[] command = new String[] { "ipconfig" };
        Process p;
        // String[] envArray = new String[0];
        //  Charset.forName("windows-1251");
        //System.out.println(Charset.defaultCharset());

        try {
            p = Runtime.getRuntime().exec(CMDPath);
            //        System.setOut(new PrintStream(System.out, true, "utf-8"));
// если мы выполняем команды windows типа ipconfig то необходимо указывать параметр кодировки "Cp866"
            //  BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), "Cp866"));
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                //  System.out.println(line);
                Response = Response + "\n" + line;
            }
            input.close();
            res.put("STATUS", "OK");
            //String command = URLEncoder.encode(Response, "windows-1251");

            res.put("RESPONCE", Response);
        } catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");
            res.put("STATUS", "ERROR");
            res.put("RESPONCE", RunExternalApplication.class.getName() + " (" + e.getClass().getName() + ") " + e.getMessage());
            return res;
        }
        return res;
    }

    public static String Runner(String[] cmd) throws UnsupportedEncodingException {
        String Runner = null;
        Runtime RuntimeNew = Runtime.getRuntime();
        Process ProcessNew = null;
        byte[] a = new byte[1024];
        //String cmd[]={"notepad","c:/mytext.txt"};
        //String cmd[]={"java -jar E:\\Server\\Repositories\\projects\\examples\\out\\artifacts\\jsp_jar\\jsp.jar \"Hello Russia and World!!!\""};
        try {
            //  Runner = RuntimeNew.exec(cmd).toString();
            ProcessBuilder pb = new ProcessBuilder(cmd);
            ProcessNew = pb.start();
            //ProcessNew = RuntimeNew.exec(cmd);
            ProcessNew.getInputStream().read(a);
            //Runner = a.toString();
        } catch (Exception e) {
            //System.out.println("Ошибка "+e+" при выборе параметра ");
            Runner = "Ошибка " + e + " при выборе параметра ";
        }
        return Runner;
    }

}
