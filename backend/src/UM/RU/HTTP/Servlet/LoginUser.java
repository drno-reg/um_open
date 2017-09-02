package UM.RU.HTTP.Servlet;

import UM.RU.DB.ValidateUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

//@WebServlet("/LoginUser")
@WebServlet(
        name = "LoginUser",
        description = "Вход пользователя - проверка имени пользователя и пароля",
        urlPatterns = "/LoginUser"
)

public class LoginUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
//    private ValidateUser dao;
//
//    public LoginUser(String username, String password) {
//        super();
//        dao = new ValidateUser();
//    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // read form fields
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String first_name = "";
        String last_name = "";
        String validation_error = "";

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

        if ("DB".equals(LoginType)) {
            // валидация пользователя в БД SQLite, Oracle, MySQL
            // считывание информации о подключении
            System.out.println("username: " + username);
            System.out.println("password: " + password);
//            Map Validate = ValidateUser.CheckUser(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName, username, password);
//            first_name = (String) Validate.get("first_name");
//            last_name = (String) Validate.get("last_name");
//            validation_error = (String) Validate.get("validation_error");

            UM.RU.DB.ValidateUser validateUser=new UM.RU.DB.ValidateUser(DB_UserName, DB_Password, DB_URL_Connection, ClassDriverName);
            Map Validate=validateUser.CheckUser(username, password);
            first_name = (String) Validate.get("first_name");
            last_name = (String) Validate.get("last_name");
            validation_error = (String) Validate.get("validation_error");

        }
        if ("LDAP".equals(LoginType)) {
            // валидация пользователя в LDAP
            //Map Validate_LDAP=RU.LDAP.ValidateUser.CheckUser_LDAP();
            Map<String, Object> Validate = null;
            try {
                UM.RU.LDAP.ValidateUser adAuthenticator = new UM.RU.LDAP.ValidateUser(LDAP_Domain,LDAP_Host,LDAP_SearchBase);
                Validate = adAuthenticator.CheckUser(username, password);
                first_name = (String) Validate.get("sn");
                last_name = (String) Validate.get("givenName");
                validation_error = (String) Validate.get("validation_error");
            } catch (Exception e) {
                validation_error = (String) Validate.get("validation_error");
            }
        }

        System.out.println("first_name: " + first_name);
        System.out.println("last_name: " + last_name);

        if (first_name != null && first_name != "") {
            HttpSession session = request.getSession();
            session.setAttribute("username", first_name + " " + last_name);
            session.setAttribute("DBMS", FileNameDB);
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30 * 60);
            Cookie userName = new Cookie("username", username);
            userName.setMaxAge(30 * 60);
            response.addCookie(userName);
            Cookie DBMS = new Cookie("DBMS", FileNameDB);
            DBMS.setMaxAge(30 * 60);
            response.addCookie(DBMS);
            // переход на главную страницу
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/views/main.jsp");
            PrintWriter out = response.getWriter();
            rd.include(request, response);
            //out.println("<font color=red>Вход выполнен.</font><br>Привет "+first_name+" "+last_name);

        } else {
            try {
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/views/index.jsp");
                PrintWriter out = response.getWriter();
                //  out.println("<font color=red><br>"+validation_error+"</font>");
                rd.include(request, response);
                out.println("<font color=red>" + validation_error + ".<br>Попробуйте снова.</font><BR>" + username + "::" + password);
            } catch (Exception e) {
                e.printStackTrace();
                //res.put("validation_error", e);
                System.out.println(e);
            }
        }
    }
}

