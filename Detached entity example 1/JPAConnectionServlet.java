package com.example.jpaTest;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                    out.println("Connection JPA OK");
                    //String name, String surname, String email, String password
                    //Create an object

                    Client client = new Client("Jean", "Dupond", "jean.dupond@gmail.com", "abcd123");
                    Client client2 = new Client("Jean2", "Dupond2", "jean.dupond@gmail.com", "abcd123");
                    Client client3 = new Client("Jean3", "Dupond3", "jean.dupond@gmail.com", "abcd123");

                    System.out.println(1);
                    //start a new the transaction or retrieve the ongoing one
                    em.getTransaction().begin();
                    System.out.println(2);
                    try {
                        //save the client to the persistence context
                        em.persist(client);
                        em.persist(client2);
                        em.persist(client3);
                        System.out.println(3);
                        //save in the BDD the ongoing transaction
                        em.getTransaction().commit();
                        System.out.println(4);
                        out.println("transaction success");

                        try {
                            client = em.find(Client.class, (long) 2);
                            System.out.println(5);
                            if(client != null) {
                                System.out.println(6);
                                out.println("Client found");
                                //start
                                em.getTransaction().begin();
                                //modify object
                                client.setPassword("no");
                                //commit the modification to the database
                                em.getTransaction().commit();
                                out.println("Client modified");
                                System.out.println(client.getPassword());

                            }else {
                                System.out.println(7);
                                out.println("client not found");
                            }


                        }catch(Exception e) {
                            System.out.println(e.getMessage());
                            if(em.getTransaction().isActive()) {
                                em.getTransaction().rollback();
                            }
                        }

                        try {
                            client = em.find(Client.class, (long) 3);
                            if(client != null) {
                                out.println("Client found");
                                //start
                                em.getTransaction().begin();
                                //modify object
                                em.remove(client);
                                //commit the modification to the database
                                em.getTransaction().commit();
                                out.println("Client deleted");
                            }else {
                                out.println("client not found");
                            }
                        }catch(Exception e) {
                            System.out.println(e.getMessage());
                            if(em.getTransaction().isActive()) {
                                em.getTransaction().rollback();
                            }
                        }

                    }catch(Exception e) {
                        //rollback to the state prior to the commit()
                        em.getTransaction().rollback();
                        System.out.println(e.getMessage());
                        out.println("Transaction failed :/");
                    }
                    em.close();
                    out.close();

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