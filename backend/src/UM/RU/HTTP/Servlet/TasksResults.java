/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UM.RU.HTTP.Servlet;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author vassilii
 */
@WebServlet(
        name = "TasksResults",
        description = "получит список результатов проверки",
        urlPatterns = "/TasksResults"
)
public class TasksResults extends HttpServlet{
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("user_id");
        
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
        
        UM.RU.DB.GetTasks task =  new UM.RU.DB.GetTasks(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
        response.getWriter().append(task.getList(userId));
    }
}
