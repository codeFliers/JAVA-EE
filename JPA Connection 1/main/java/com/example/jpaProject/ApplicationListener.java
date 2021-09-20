package com.example.jpaProject;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private static EntityManagerFactory emf;

    public ApplicationListener() {
    }

    public static EntityManagerFactory getEmf()  {
        return ApplicationListener.emf;
    }
    public static void setEmf(EntityManagerFactory emf) {
        ApplicationListener.emf = emf;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //name of the concerned unit in the persistence.xml file (<persistence-unit name="...">)
        ApplicationListener.emf = Persistence.createEntityManagerFactory("PU_JPA");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(ApplicationListener.emf.isOpen() && ApplicationListener.emf != null) {
            ApplicationListener.emf.close();
        }
    }
}
