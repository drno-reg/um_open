package UM.RU.FilesDirs;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadingFiles {

    //---------------------------------------
    // чтение информации о подключении
    //---------------------------------------
    public static Map<String, String> LoadCFGMap(String FileName) throws IOException {
        File file=null;
        String[] PathVariant={"webapps\\um_open\\WEB-INF\\views\\config\\","frontend\\web\\WEB-INF\\views\\config\\"};
        if ((FileName.indexOf("\\"))==-1) {
            String path = new File(".").getAbsolutePath();
            System.out.println(path);
            path = path.replace(".", "");

            if (path.indexOf("bin\\.") != -1) {
                file = new File(path.replace("bin\\.", "") + PathVariant[0] + FileName);
                System.out.println(path.replace("bin\\.", "") + PathVariant[0] + FileName);
            }
            if (path.indexOf("bin\\") != -1) {
                file = new File(path.replace("bin\\", "") + PathVariant[0] + FileName);
                System.out.println(path.replace("bin\\", "") + PathVariant[0] + FileName);
            } else {

                if (path.indexOf("omcat") != -1) {
                    file = new File(path.replace(".", "") + PathVariant[0] + FileName);
                    System.out.println(path + PathVariant[0] + FileName);
                } else {
                    file = new File(path.replace(".", "") + PathVariant[1] + FileName);
                    System.out.println(path + PathVariant[1] + FileName);
                }
            }
        }
        else {
            file = new File(FileName);
            System.out.println(FileName);
        }
        Map<String, String> map=new HashMap<String,String>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не нейден! Путь от "+new File(".").getAbsolutePath());
            e.printStackTrace();
        }
        String str;
        int counter = 0;
        try {
            while ((str = br.readLine()) != null) {
                counter++;
                // исключаем в работе строки которые начинаются с #
                char charFirst = str.charAt(0);
//                System.out.println(str.toCharArray());
//                System.out.println(charFirst);
                String temp = Character.toString(charFirst);
                if (!("#".equals(temp))) {
//                    System.out.println("match");
                    map.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1, str.length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String s = "This is a demo of the getChars method.";
//        int start = 10;
//        int end = 14;
//        char buf[] = new char[end - start];
//        s.getChars(start, end, buf, 0);


//        for (int i = 0; i <  map.size(); i++) {
//            //    System.out.println(file_strings.get(i));
//        }

//        for (Map.Entry<String, String> entry : map.entrySet())
//        {
//            System.out.println(entry.getKey() + " :: " + entry.getValue());
//        }
        return map;
    }


}
