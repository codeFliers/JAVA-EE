package com.example.jpaTest;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "sessionTestServlet", value = "/sessionTestServlet")
public class SessionTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            //MAKE THE CONNECTION
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "test", "test");
            System.out.println(conn.isClosed() ? "close" : "open");

            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");

            if (ApplicationListener.getEmf().isOpen()) {
                ApplicationListener.setPersistenceUnitName("PU_JPA");

                //simulate a detached client into a session
                putClientIntoSession(request);
                //create an other EntityManager object (new persistence context)
                EntityManager em = JPAUtil.getEntityManager();
                //start transaction
                em.getTransaction().begin();
                //retrieve the detached object from the ongoing session
                Client clientDetach = (Client) request.getSession().getAttribute("clientSessionDetach");
                //retrieve the original/equivalent persistent context from the object in parameter
                Client clientAttach = em.merge(clientDetach);
                //do something
                clientAttach.setPassword("my brand new password");
                //save it to the new context
                em.persist(clientAttach);
                //save to the db
                em.getTransaction().commit();
                //close the persistent context
                em.close();
            } else {
                //
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
        /*
            Simulate a detached client in a session
        */
        protected void putClientIntoSession(HttpServletRequest request) {
        //create a persistence context
        EntityManager em = JPAUtil.getEntityManager();
        //find a client in the attached database with an ID of 1
        Client client = em.find(Client.class, (long) 1);
        //create/retrieve session a add to the specified parameter the previous object
        request.getSession().setAttribute("clientSessionDetach", client);
        //now the object is detached to any persistence context
        em.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
