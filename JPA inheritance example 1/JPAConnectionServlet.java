package com.example.jpaProject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "jpaConnectionServlet", value = "/jpaConnectionServlet")
public class JPAConnectionServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            //MAKE THE CONNECTION
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "test", "test");
            System.out.println(conn.isClosed()?"close":"open");

            PrintWriter out = response.getWriter();
            response.setContentType("text/plain");

            if(ApplicationListener.getEmf().isOpen()) {
                ApplicationListener.setPersistenceUnitName("PU_JPA");
                //createEntityManager is "thread-safe" (1 thread by EntityManager)
                EntityManager em = JPAUtil.getEntityManager();
                if(em.isOpen()) {
                    out.print("Connection JPA OK");
                    JPAUtil.persistEntities(em);
                    JPAUtil.runNativeQueries(em);
                    JPAUtil.loadEntities(em);
                    em.close();
                }else {
                    out.print("Connection JPA not OK");
                }
            }
            out.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
    }
}