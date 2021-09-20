package com.example.jpaProject;

import javax.persistence.EntityManager;

public class JPAUtil {

    public static EntityManager getEntityManager()  {
        return ApplicationListener.getEmf().createEntityManager();
    }
}
