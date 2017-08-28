package UM.RU.HTTP.Servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet(
        name = "LogoutUser",
        description = "Разрыв сессии пользователя",
        urlPatterns = "/LogoutUser"
)
public class LogoutUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

// GET - doGet
// POST - doPost

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    System.out.println("JSESSIONID=" + cookie.getValue());
                    break;
                }
            }
        }
        //invalidate the session if exists
        try {
            HttpSession session = request.getSession(false);
            System.out.println("User=" + session.getAttribute("user"));
            if (session != null) {
                session.invalidate();
            }
            RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
            dispatch.forward(request, response);
        } catch (Exception e) {
            //    response.sendRedirect("index.jsp");

            RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
            dispatch.forward(request, response);
        }
    }
}
