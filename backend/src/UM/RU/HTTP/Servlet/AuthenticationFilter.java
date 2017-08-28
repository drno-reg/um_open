package UM.RU.HTTP.Servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.io.IOException;

@WebServlet(
        name = "AuthenticationFilter",
        description = "Аутентификационный фильтр",
        urlPatterns = "/AuthenticationFilter"
)
@WebFilter(
        urlPatterns={"*.jsp"},
        dispatcherTypes= {
                DispatcherType.FORWARD,
                DispatcherType.REQUEST
        },
        filterName="AuthenticationFilter",
        description="Checks if user already logged in"
)
public class AuthenticationFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

    @Override
    public void destroy() {
        //close any resources here
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        this.context.log("Requested Resource::" + uri);

        HttpSession session = req.getSession(false);
        Object user_o = req.getSession().getAttribute("username");
        this.context.log("Фильтр аутентификации, пользователь::" + user_o);
        // if(req.getContentType().equals("css")){
        //     chain.doFilter(request, response);
        //     return;
        // }
        this.context.log("Лог: " + user_o + " : " + uri.endsWith("index.jsp") + " : " + uri.endsWith("LoginUser"));
        //  System.out.print();

        if (user_o == null && !(uri.endsWith("index.jsp") || uri.endsWith("LoginUser"))) {
            // если использовать HTML то фильтр работает по условию session=null
            //    if(session == null && !(uri.endsWith("login.html") || uri.endsWith("LoginServlet"))) {
            this.context.log("Неавторизованный запрос, сессия:: " + session);
            //   res.sendRedirect("index.jsp");

            RequestDispatcher dispatch = request.getRequestDispatcher("/WEB-INF/views/index.jsp");
            dispatch.forward(req, res);

            // request.getRequestDispatcher("/WEB-INF/TargetJspFile.jsp").forward(request, response);

        } else {
            // pass the request along the filter chain
            this.context.log("Авторизованный запрос, сессия:: " + session);
            chain.doFilter(request, response);
        }

    }
}
