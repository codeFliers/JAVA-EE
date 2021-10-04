package Controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "JspparamServlet", value = "/JspparamServlet")
public class JspparamServlet extends HttpServlet {
    private static final String  VUES="/Views/Jspparam/" /*, DEFVUE="index.jsp"*/;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maVue = VUES + "jspparam.jsp";

        System.out.println(request.getParameter("p1"));
        System.out.println(request.getParameter("p2"));
        System.out.println(request.getParameter("p3"));
        request.setAttribute("test", "test");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(maVue);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
