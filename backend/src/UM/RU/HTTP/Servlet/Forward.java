package UM.RU.HTTP.Servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "Forward",
        description = "Переход по ссылке",
        urlPatterns = "/Forward"
)

public class Forward extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String URL = "/WEB-INF/views/" + request.getParameter("URL");
        RequestDispatcher dispatch = request.getRequestDispatcher(URL);
        dispatch.forward(request, response);

    }

}

