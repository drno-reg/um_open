package UM.RU.HTTP.Servlet.API;

import javax.servlet.annotation.WebServlet;

        import javax.servlet.ServletException;
                import javax.servlet.annotation.WebServlet;
                import javax.servlet.http.HttpServlet;
                import javax.servlet.http.HttpServletRequest;
                import javax.servlet.http.HttpServletResponse;
                import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import UM.RU.DB.Datasets.NodeList;
import atg.taglib.json.util.JSONArray;
import com.google.gson.Gson;

@WebServlet(
        name = "GetNodes",
        description = "API получить состояние объектов мониторинга",
        urlPatterns = "/api/GetNodes"
)

public class GetNodes extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // важное замечание - изменение в сервлете выозможно наблюдать если только переразорвать сессию через Logout
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String user_id = request.getParameter("user_id");

        Map Settings = UM.RU.Config.LoadSettings.Settings("settings.cfg");
        String LoginType = (String) Settings.get("LoginType");
        String FileNameDB = (String) Settings.get("FileNameDB");
        String DB_UserName = (String) Settings.get("DB_UserName");
        String DB_Password = (String) Settings.get("DB_Password");
        String DB_URL_Connection = (String) Settings.get("DB_URL_Connection");
        String ClassDriverName = (String) Settings.get("ClassDriverName");
        String LDAP_Domain = (String) Settings.get("LDAP_Domain");
        String LDAP_Host = (String) Settings.get("LDAP_Host");
        String LDAP_SearchBase = (String) Settings.get("LDAP_SearchBase");

        UM.RU.DB.GetNodes getNodes=new UM.RU.DB.GetNodes(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
        String Result=getNodes.CreateNodes(user_id);
      //  System.out.println(Get);


     //   String result="{\"string\"=\"Hello World\"}";

        response.getWriter().append(Result);

    }
}

