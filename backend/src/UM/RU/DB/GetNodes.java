package UM.RU.DB;

import UM.RU.DB.Datasets.NodeList;
import UM.RU.DB.Datasets.Nodes;
import atg.taglib.json.util.JSONArray;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNodes {

    private String DB_UserName;
    private String DB_Password;
    private String DB_URL_Connection;
    private String ClassDriverName;
    //    private String username;
//    private String password;
    private Connection connection;

    public GetNodes(String DB_UserName, String DB_Password, String DB_URL_Connection, String ClassDriverName) {
        this.DB_UserName = DB_UserName;
        this.DB_Password = DB_Password;
        this.DB_URL_Connection = DB_URL_Connection;
        this.ClassDriverName = ClassDriverName;
        connection = ConnectionManager.getConnection(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
    }


    public String CreateNodes(String User_ID){
        String Output=null;
        String Select_Text ="select \n" +
                "cab.id, cab.user_id, node.id as node_id, node.hostname, node.status, count(metr.id) as metric_count\n" +
                "from um_nodes node \n" +
                "left join (select * from um_cabinets t where t.user_id=?) cab on node.cabinet_id=cab.id\n" +
                "left join (select * from um_templates) temp on node.template_id=temp.id\n" +
                "left join (select * from um_metrics) metr on temp.id=metr.template_id\n" +
                "where\n" +
                "1=1\n" +
                "group by node.hostname\n" +
                "order by node.hostname";
        ArrayList nodeList_json = new ArrayList();
        JSONArray nodes_json = new JSONArray();
        // MZOM_is_name mzom_is_name=null;
        Nodes nodes=null;
        NodeList nodeList=null;

        //List rowValues = new ArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Select_Text);
            // Parameters start with 1
            preparedStatement.setString(1,User_ID);
            ResultSet result=preparedStatement.executeQuery();
            nodes=new Nodes();
            nodes.setUserId(User_ID);
            Integer countRows=0;
            while (result.next()) {
                nodeList=new NodeList();
                nodeList.setNodeId(result.getInt("node_id"));
                nodeList.setNodeName(result.getString("hostname"));
                nodeList.setNodeStatus(result.getString("status"));
                nodeList.setMetricCount(result.getInt("metric_count"));
               // nodeList
               // nodeList.setIS_NAME(result.getString("IS_NAME"));
                nodeList_json.add(nodeList);

                //  String num = rs.getString("num");
//                System.out.println(result.getString("first_name"));
//                res.put("first_name", result.getString("first_name"));
//                res.put("last_name", result.getString("last_name"));
//                res.put("validation_error", "");
            countRows++;
            }
            nodes.setNodeCount(countRows);
            nodes.setNodeList(nodeList_json);
            nodes_json.put(nodes);
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
        return gson.toJson(nodes_json);
    }
    
    public String getNodesHttp(String userId){
        String query = "SELECT n.id, n.hostname, n.description, t.name, n.status\n" +
        "FROM um_nodes n INNER JOIN um_templates t ON n.template_id = t.id \n" +
        "                                      INNER JOIN um_cabintes c ON n.cabinet_id = c.id\n" +
        "WHERE c.user_id = ?";
        List<Map<String, String> > res = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Parameters start with 1
            preparedStatement.setString(1,userId);
            ResultSet result=preparedStatement.executeQuery();
            
            while (result.next()) {
                Map<String, String> mp = new HashMap<>();
                mp.put("hostname", result.getString("hostname"));
                mp.put("description", result.getString("description"));
                mp.put("name", result.getString("name"));
                mp.put("status", result.getString("status"));
                mp.put("id", result.getString("id"));
                res.add(mp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
       // return gson.toJson(json);
        return gson.toJson(res);
    }


}
