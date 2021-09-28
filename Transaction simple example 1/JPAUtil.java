package com.example.jpaTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class JPAUtil {

    public static EntityManager getEntityManager()  {
        //createEntityManager is "thread-safe" (1 thread by EntityManager)
        return ApplicationListener.getEmf().createEntityManager();
    }
}
