package UM.RU.DB;

import UM.RU.DB.Datasets.MetricList;
import UM.RU.DB.Datasets.Metrics;
import atg.taglib.json.util.JSONArray;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetMetrics {

    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;
    private String ClassDriverName;
    //    private String username;
//    private String password;
    private Connection connection;

    public GetMetrics(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        this.ClassDriverName = ClassDriverName;
        connection = ConnectionManager.getConnection(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
    }


    public String CreateMetrics(String User_ID, String Node_ID){
        String Output=null;
        String Select_Text ="select node.id as node_id, node.hostname, metr.id as metric_id, metr.name, metr.status\n" +
                "FROM\n" +
                "um_nodes node \n" +
                "left join (select * from um_templates) temp on node.template_id=temp.id\n" +
                "left join (select * from um_metrics) metr on temp.id=metr.template_id\n" +
                "where\n" +
                "1=1\n" +
                "and node.id=?\n" +
                "order by metr.name";
        ArrayList metricsList_json = new ArrayList();
        JSONArray metrics_json = new JSONArray();
        // MZOM_is_name mzom_is_name=null;
        Metrics metrics=null;
        MetricList metricList=null;

        //List rowValues = new ArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Select_Text);
            // Parameters start with 1
            preparedStatement.setString(1,Node_ID);
            ResultSet result=preparedStatement.executeQuery();
            metrics=new Metrics();
            metrics.setUserId(User_ID);
            Integer countRows=0;
            while (result.next()) {
                metricList=new MetricList();
                metricList.setMetricId(result.getInt("metric_id"));
                metricList.setMetricName(result.getString("name"));
                metricList.setMetricStatus(result.getString("status"));
                metricsList_json.add(metricList);

                //  String num = rs.getString("num");
//                System.out.println(result.getString("first_name"));
//                res.put("first_name", result.getString("first_name"));
//                res.put("last_name", result.getString("last_name"));
//                res.put("validation_error", "");
                countRows++;
            }
            metrics.setMetricCount(countRows);
            metrics.setMetricList(metricsList_json);
            metrics_json.put(metrics);
//            if (res.get("first_name")!=null) {
//                res.put("validation_error", "Авторизация прошла успешно!");
//            }
//            if (res.get("first_name")==null) {
//                res.put("validation_error", "Неверный либо имя пользователя либо пароль! "+Select_Text);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        // return gson.toJson(json);
        return gson.toJson(metrics_json);
    }

}
