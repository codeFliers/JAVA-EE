package com.example.jpaTest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private static EntityManagerFactory emf;
    private static String persistenceUnitName;

    public ApplicationListener() {
    }

    public static EntityManagerFactory getEmf()  {
        return ApplicationListener.emf;
    }
    public static void setEmf(EntityManagerFactory emf) {
        ApplicationListener.emf = emf;
    }

    public static void setPersistenceUnitName(String name) {
        ApplicationListener.persistenceUnitName = name;
    }
    public static String getPersistenceUnitName() {
        return ApplicationListener.persistenceUnitName;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //name of the concerned unit in the persistence.xml file (<persistence-unit name="...">)
        ApplicationListener.emf = Persistence.createEntityManagerFactory(ApplicationListener.getPersistenceUnitName());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(ApplicationListener.emf.isOpen() && ApplicationListener.emf != null) {
            ApplicationListener.emf.close();
        }
    }
}
