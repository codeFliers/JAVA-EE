package com.example.jpaProject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class JPAUtil {

    public static EntityManager getEntityManager()  {
        //createEntityManager is "thread-safe" (1 thread by EntityManager)
        return ApplicationListener.getEmf().createEntityManager();
    }

    public static void nativeQuery(EntityManager em, String s) {
        System.out.printf("'%s'%n", s);
        Query query = em.createNativeQuery(s);
        List list = query.getResultList();
        for (Object o : list) {
            if (o instanceof Object[]) {
                System.out.println(Arrays.toString((Object[]) o));
            } else {
                System.out.println(o);
            }
        }
    }
    public static void runNativeQueries(EntityManager em) {
        System.out.println("-- Native queries --");
        nativeQuery(em, "Select * from Employee");
        nativeQuery(em, "Select * from FULL_TIME_EMP");
        nativeQuery(em, "Select * from PART_TIME_EMP");
    }
    public static void persistEntities(EntityManager em) throws Exception {
        System.out.println("-- Persisting entities --");

        FullTimeEmployee e1 = new FullTimeEmployee();
        e1.setName("Sara");
        e1.setSalary(100000);
        System.out.println(e1);

        PartTimeEmployee e2 = new PartTimeEmployee();
        e2.setName("Robert");
        e2.setHourlyRate(60);
        System.out.println(e2);

        em.getTransaction().begin();
        em.persist(e1);
        em.persist(e2);
        em.getTransaction().commit();

    }
    public static void loadEntities(EntityManager em) {
        System.out.println("-- Loading entities --");
        List<Employee> entityAList = em.createQuery("Select t from Employee t")
                .getResultList();
        entityAList.forEach(System.out::println);
        em.close();
    }
}
