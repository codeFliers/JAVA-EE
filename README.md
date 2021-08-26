# JAVA-EE 
(² mean in the glossary)

It complet JAVA SE² Framework and give the possibility to handle distributed application².

It is divised in 3 layers: **client** (*1*), a **medium** (*2*) and **data** (*3*).

   -(*1*) interact with the user (JSE², Applet² and web pages)
   
   -(*2*) handle HTTP Request of the client (Servlet²)
   
   -(*3*) DB or others
   
Every part of it are running in "**containers**"² who are virtualised environment in which application get executed (app container), applet (applet container) and
the handle of http request by the server (web container).

Put in a very simple way, it is very usefull in Enterprise ecosystems and to get into web application development.

### How is the basic logic structure of a Web Application ?

      Client => HTTP REQUEST => Web Container (Servlet, JSP² and .java classes) => DB
      
A request is interpreted by the "**Controller**", namely the "*Servlet*".

According to the request, it ask to the java classes "**model**" the execution of a task / work.

These classes may need to communicate with the database if needed.

Then, after having retrieved the task result, the Servlet ask the "*JSP*" which is the "**View**", to generate dynamically the HTML pages.

In the end, the client obtain a response, an updated view.


We have a *MVC architecture* : **M** (Classes) **V** (JSP) **C** (Servlet).


### How does a JAVA EE Project look like ?

![project archi](https://user-images.githubusercontent.com/58827656/130973322-ed157119-b95b-4dcf-9d7b-59b3932e3c16.png)


*web.xml*: A file descriptor that is partialy depreciated since the v3.0 of the Servlet.  It handle the life cycle of the application, the servlet,  session and many more...
Now we use what is called "annotation".  

For example, it can have default value that we can use in our code:  

```

<context-param>
    <param-name>myValueName</param-name>
    <param-value>My Value Result</param-value>
</context-param>

```

Access it :  
```
//"My Value Result" as a result
out.println("<h1>" + servletContext.getInitParameter("myValueName")  + "</h1>");

```  


An annotation is directly put into the servlet file and can have multiple paramters. The most basic one is his name:

```
@WebServlet(name = "helloServlet", value = "/hello-servlet");
)

```


*pom.xml*: It is an XML file that contains information about the project and configuration details used by Maven to build the project. It handle every dependencies you need and can be reuse on multiple projects.

A Servlet inherite from the class "httpServlet" which inherite from GenericServlet and implement the interface from Servlet.
It ehence the server functionnality by handling request and response, most often HTTP.  
A Servlet rewrite differents methods: init(), service() and doXXX (get, post ...):

  First, the container execute *init()* that initialise variables, it look like a constructor.  
  Then, it is the *service* method that take 2 parameters : *(HttpServletRequest request, HttpServletResponse response)*.  
  The *do**XXX*** depending (ie: doGet if GET).  
  Finaly, the *destroy()* method.





### Simple example of an MVC architecture with delegation principal

Basic HTML Form in GET:  

```
<!DOCTYPE html>
<html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Title</title>
  </head>
  <body>
      <form method="get" action="handleProcessServlet">
          Ground code:<input type="text" name="groundCode" />
          <br/>
          Sport autorisé(s):
          <select name="selectSport" multiple="multiple">
              <option value="1">Badminton</option>
              <option value="2">Foot</option>
              <option value="3">Tenis</option>
          </select>
          <br/>
          <input type="submit" value="Valider"/>
      </form>
  </body>
</html>

```

First Servlet that handle the task / result from the class :

```

@WebServlet(name = "handleProcessServlet", value = "/handleProcessServlet")
public class HandleProcessServlet extends HttpServlet {
    private int idGround;
    private Map<String, String[]> listSportAllowed;

    public void init() {
        this.listSportAllowed = new HashMap<>();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("method doGet()");

        this.listSportAllowed = request.getParameterMap();
        this.idGround = Integer.parseInt(request.getParameter("idGround"));

        //Delegation (Ground handle the object creation)
        Ground newGround = new Ground(idGround, "Concrete");
        //Update the request
        request.setAttribute("newGround", newGround);
        request.setAttribute("listSportAllowed", listSportAllowed);

        //Dispatch: to draw the result, we use handleResultServlet
        RequestDispatcher rd = request.getRequestDispatcher("/HandleResultServlet");
        rd.forward(request, response);
    }
    
```
We use *RequestDispatcher* and *forward* to update the Request with new data (newGround and ListSportAllowed).  
Then, we let handleResultServlet handle the rest (**Delegation**). What is it ? We just let an other Servlet handle the job itself.


The one that handle the result :

```

@WebServlet(name = "HandleResultServlet", value = "/HandleResultServlet")
public class HandleResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Ground info</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>");
        out.println("<strong>Ground ID: </strong>"+((Ground)request.getAttribute("newGround")).getIdGround()+"<br/>");

        String res = "";
        Map<String, String[]> listSportAllowed = (Map<String, String[]>)request.getAttribute("listSportAllowed");
        for (Map.Entry<String, String[]> entry : listSportAllowed.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if(key.equals("selectSport")) {
                for(String str : value) {
                    res += str + " ";
                }
            }
        }
        out.println("<strong>Sport allowed: </strong>"+res);
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }
  
```
Then there is the Ground class ...




   
   

