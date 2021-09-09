package com.example.CookiTest;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreationSessionServlet", value = "/CreationSessionServlet")
public class CreationSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //prepare the stream response
        PrintWriter pw = response.getWriter();
        response.setContentType("text/plain");

        //retrieve or create the session depending of if it already exist or not
        HttpSession session = request.getSession();

        //True or False
        pw.println("La session vient d'être créée (bool): "+session.isNew());

        //recover session attributs (nom and prenom)
        String nom = (String) session.getAttribute("nom");
        if(nom != null) {
            pw.println("L'attribut nom est: "+nom);
        }else {
            pw.println("L'attribut nom n'existe pas encore");
        }

        String prenom = (String) session.getAttribute("prenom");
        if(prenom != null) {
            pw.println("L'attribut prenom est: "+prenom);
        }else {
            pw.println("L'attribut prenom n'existe pas encore");
        }

        //will set the attributs for the next try
        session.setAttribute("nom", "Dupont");
        session.setAttribute("prenom", "Jean");

        /*
        Whenever the client doesn't accept 'cookie', the url rewrite have to be used.
        It will rewrite the URL dynamicaly by adding ';jsessionid=...' where '...' is a suit of numbers
        */
        String url = request.getContextPath() + "/chemin";
        String urlEncode = response.encodeURL(url);
        pw.println(urlEncode);
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
