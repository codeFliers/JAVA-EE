# JAVA-EE 
(² mean in the glossary)

It complet JAVA SE² Framework and give the possibility to handle distributed application².

It is divised in 3 layers: **client** (*1*), a **medium** (*2*) and **data** (*3*).

   -(*1*) interact with the user (JSE², Applet² and web pages)
   
   -(*2*) handle HTTP Request of the client (Servlet²)
   
   -(*3*) DB or others
   
Every part of it are running in "**containers**"² who are virtualised environment in which application get executed (app container), applet (applet container) and
the handle of http request by the server (web container).

Put in a very simple way, it is very usefull in Enterprise1:1 UNIDIRECTIONAL relationship ecosystems and to get into web application development.

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
Then tis the Ground class ...

**What is include() ?**  
*include()* is an usefull method from RequestDispatcher that give the possibility to include code from an other file.  
For example, in web development HTML code are use by many pages and to not write it down each time, you can reuse it: 
```
<footer>
    i am a footer
</footer>
``` 
``` 
out.println("<strong>Sport autorisé(s): </strong>"+res);
out.println("</p>");

RequestDispatcher rd = request.getRequestDispatcher("/footer.html");
rd.include(request, response);

out.println("</body>");
out.println("</html>");
out.flush();
out.close();
```
```
<html>
   <head>
      <meta charset="UTF-8">
      <title>Confirmation réception terrain info</title>
   </head>
   <body>
      <p>
         <strong>Code terrain: </strong>10<br>
         <strong>Sport autorisé(s): </strong>1 
      </p>
      <footer>
         i am a footer
      </footer>
   </body>
</html>
```
 **Redirection**  
We can use **redirection** if we want to do a new URL Request. We can then redirect to a servlet or a webpage :
```
  <form action="RedirectionServlet" method="Get">
    <input type="submit" name="RedirectionServlet" value="RedirectionServlet">
  </form>

  <form action="RedirectionServlet" method="Get">
    <input type="submit" name="RedirectionWebsite" value="RedirectionWebsite">
  </form>
```
```
  @WebServlet(name = "RedirectionServlet", value = "/RedirectionServlet")
public class RedirectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("RedirectionWebsite") != null) {
            response.sendRedirect("https://www.google.fr");

        } else if (request.getParameter("RedirectionServlet") != null) {
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", request.getContextPath() + "/test.html");
        }
        //...
    }
```
```
<!DOCTYPE html>
<html lang="en">
   <head>
    <meta charset="UTF-8">
    <title>Title</title>
   </head>
   <body>
      <p>HELLO redirection test</p>
   </body>
</html>
 ```
 
 **How to handle error**  
It is possible to use the method "*sendError()*" from HttpServletResponse:
 ```
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
   response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //500
}
 ```
 
 To add personalization (web.xml and a custom HTML page "pageError500.html" that work like a redirection):  
 ```
 <error-page>
   <error-code>500</error-code>
   <location>/pageError500.html</location>
 </error-pages>
 ```
 
 We could add this code to a previous example:
 
 ```
 //...
}else {
      //SC_NOT_FOUND : Resource not found
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "The parameter is null and so the resource asked");
}
 ```
**Update a JSP page (NOT ASYNC)**

We have a basic form in a JSP file "test.jsp". It will be handled by a Servlet "TestServlet" that will change the parameter value and get back the result to "test.jsp".

```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test</title>
</head>
<body>
    <form method="get" action="TestServlet">
        <input type="text" name="inputText" />
        <br/>
        <input type="submit" value="Validate"/>
    </form>
    <div id="response">
        <p>Response: </p>
        <br/>
        <% if(application.getAttribute("inputText") != null)  { %>
        <%= application.getAttribute("inputText")%>
        <%}%>
    </div>
</body>
</html>
```

```
@WebServlet(name = "TestServlet", value = "/TestServlet")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "";
        System.out.println("doGet");
        if(request.getParameter("inputText") != null) {
            System.out.println("not null");
            message = "DoGet: "+request.getParameter("inputText");

            ServletContext sc = request.getServletContext();
            sc.setAttribute("inputText", message);
            response.sendRedirect("test.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
```
 
**Asynchronous servlet**

Asynchronous mean that multiple part of a program can work on their own in parallel. This functionality is handled by "Threads" but
a program does have a limited pool of it and here come two issues :  
First, like i said they are limited in numbers. And secondly, the power available to handle them is limited too : resources exhaustion.  
In a Web project, where we can face many users at the same time, it is an issue.  We don't want our thread blocked and doing nothing while waiting for a response.  

Two example of waiting time:  
-wait a data to construct a response (ie. data base access).  
-wait an event to occure to construct a response (ie. need an information from an other client).

To solve the pool issue, we can use an *asynchronous servlet*. We will need one of the main threads and as it take notice of the HTTP Request, it will then charge a "child" thread to this specific task. While the child is working (child labor is ok), the parent return to the initial HTTP pool to be available again.

A simple example: 

```
@WebServlet(urlPatterns = "/asyncServlet", asyncSupported = true)
public class TestAsyncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        final long startTime = System.nanoTime();
        //the request is now asynchronous
        final AsyncContext asyncContext = request.startAsync(request, response);

        System.out.println(1);
        //background / child thread
        new Thread() {

            @Override
            public void run() {
                try {
                    System.out.println(3);
                    //The required facilities in order for the application
                    //to further interact with the current request and response, we make them accessible
                    ServletResponse response = asyncContext.getResponse();
                    response.setContentType("text/plain");
                    PrintWriter out = response.getWriter();
                    Thread.sleep(2000);
                    out.print("Work completed. Time elapsed: " + (System.nanoTime() - startTime));
                    out.flush();
                    //complete the asynchronous request processing
                    asyncContext.complete();
                    System.out.println(4);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
        //the response should not be committed - and also not sent to the client - when doGet() method execution completes.
        System.out.println(2);
        //the original HTTP thread will return to the HTTP thread pool and will become ready to handle another incoming request.
    }
}
```
First, we need the servlet to be asynchronous : *asyncSupported = true*.  
Then, we have to give to our http request an asynchronous context to gain new functionalities: *final AsyncContext asyncContext = request.startAsync(request, response)*.  
What is going to happen is that the thread which started the HTTP request will create an asyncContext object, start a child thread and - end - the doGet method.
It is important to understand that the task is not his : after sysout (1), we don't go to the (3) but at the end of the method (2).  

At this moment, the main thread completed his job and may return to the HTTP thread pool. While in the other end, the child thread will use the asyncContext object, take all the time it need and enventualy finish his duty before being released : *asyncContext.complete()*.

To know when the child thread is one, we can add a listener to the asyncContext : 

```
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                System.out.println("on time out");
            }
            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                System.out.println("onStartAsync()");
            }
            @Override
            public void onError(AsyncEvent event) throws IOException {
                System.out.println("onerror");
            }
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("onComplete");
            }
        }, request, response);
```
We will then have : 
```
1
2
3
4
onComplete
```
For now, we handle the main Thread but not the number of children threads.  
In this example, we will have a limited amount of children threads. Client will be able to download a file (fake) but only 10 bytes at a time for a total of 100 bytes.  
We are limited to 3 background threads and will use a limited FIFO table "LinkedBlockingQueue". When a client download 10 bytes and is not at 100 yet, it return to the tails of the table before downloading again.
![asyncExample](https://user-images.githubusercontent.com/58827656/131699397-a62592b1-1b18-4852-a912-fc61030c42c8.png)

```
//Remote Client class
import javax.servlet.AsyncContext;
public class RemoteClient {
    private final AsyncContext asyncContext;
    private int bytesSent;
    //Save th
    public RemoteClient(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public void incrementBytesSent() {
        this.bytesSent += 10;
    }

    public int getBytesSent() {
        return bytesSent;
    }
}
```
```
//Web servlet
//asyncSupported = true /!\
@WebServlet(urlPatterns = "/streamingAsyncServlet", asyncSupported = true)
public class StreamingAsyncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        //Link asynchronous context to our request
        AsyncContext asyncContext = request.startAsync(request, response);
        //Time out if something is wrong
        asyncContext.setTimeout(10 * 60 * 1000);
        //It will happen each time a HTTP Request is sent from startStreamingAsyncServlet.html
        Dispatcher.addRemoteClient(new RemoteClient(asyncContext));
    }
}
```

```
@WebListener
public class Dispatcher implements ServletContextListener {

    //MAX CHILDREN THREAD ALLOWED
    private static final int PROCESSING_THREAD_COUNT = 3;
    //Dynamic FIFO array of 'RemoteClient' limited to the PROCESSING_THREAD_COUNT
    private static final BlockingQueue<RemoteClient>
            REMOTE_CLIENTS = new LinkedBlockingQueue<RemoteClient>();
    //Execute a child thread from a limited pool (PROCESSING_THREAD_COUNT)
    private final Executor executor = Executors.newFixedThreadPool(PROCESSING_THREAD_COUNT);

    public static void addRemoteClient(RemoteClient remoteClient) {
        System.out.println("add remote client");
        REMOTE_CLIENTS.add(remoteClient);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        int count = 0;
        //THE STATIC COUNT WILL HELP TO INITIALISE THE DIFFERENT BACKGROUND THREADS WHILE WE HAD NEW REMOTECLIENT
        //1 to 3 CHILDREN THREADS MAX
        while (count < PROCESSING_THREAD_COUNT) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println("1");

                        RemoteClient remoteClient;
                        try {
                            // take out the oldest REMOTECLIENT in the waiting lines (TAILS)
                            //
                            //(Productor - Consumer) pattern  (this call blocks until a client is available)
                            remoteClient = REMOTE_CLIENTS.take();
                        } catch (InterruptedException e1) {
                            throw new RuntimeException("Interrupted while waiting for remote clients");
                        }

                        AsyncContext asyncContext = remoteClient.getAsyncContext();
                        ServletResponse response = asyncContext.getResponse();
                        response.setContentType("text/plain");

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                            throw new RuntimeException(e1);
                        }

                        // increment bytes sent by 10
                        remoteClient.incrementBytesSent();

                        try {
                            // send bytes to client
                            PrintWriter out = response.getWriter();
                            //10, 20, 30, [...], 100
                            out.print("Already sent " + remoteClient.getBytesSent() + " bytes");
                            out.flush();

                            // check if we have already sent the 100 bytes to this client
                            if (remoteClient.getBytesSent() < 100) {
                                // if not, put the client again in the queue
                                REMOTE_CLIENTS.put(remoteClient);
                            } else {
                                // if the 100 bytes are sent, the response is complete
                                //RELEASE OF THE CHILD THREAD
                                asyncContext.complete();
                            }

                            ////RELEASE OF THE CHILD THREAD IF AN ERROR OCCURE
                        } catch (Exception e) {
                            // discard current client
                            asyncContext.complete();
                        }
                    }
                }
            });
            count++;
        }
    }
```
(<a href="https://www.hackerearth.com/practice/notes/asynchronous-servlets-in-java/">example from here</a>)  
Now our main threads will not focus on the task and our children will not be unlimited and consume too much resources.   

**Servlet and AJAX**

We load "index.jsp", the function "getMessages()" start a first time after "onload" et update the content of the current page (div id="content") and update the asyncContext from the "doGet" function.  

Then, when a user press submit it start the "postMessage()" function that retrieve the form content, empty it and create an ajax request that send a post request that the "doPost()" retrieve.  

This last one retrieve every asyncContexts (different pages), retrieve the content of the new request POST, update the variable that contains the last messages and put it to production for every pages (contexts).  

A setInterval will call again the "getMessages()" function every second and update the content from the postMessage update.

```
<form>
    <table>
        <tr>
            <td>Your name:</td>
            <td><input type="text" id="name" name="name"/></td>
        </tr>
        <tr>
            <td>Your shout:</td>
            <td><input type="text" id="message" name="message" /></td>
        </tr>
        <tr>
            <td><input type="button" onclick="postMessage();" value="SHOUT" /></td>
        </tr>
    </table>
</form>
<h2> Current Shouts </h2>
<div id="content">
    <% if (application.getAttribute("messages") != null) {%>
    <%= application.getAttribute("messages")%>
    <% }%>
</div>
<script>
    function postMessage() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", "shoutServlet?t="+new Date(), false);
        xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        var nameText = escape(document.getElementById("name").value);
        var messageText = escape(document.getElementById("message").value);
        document.getElementById("message").value = "";
        xmlhttp.send("name="+nameText+"&message="+messageText);
    }
    var messagesWaiting = false;
    /*
    This function establish an AJAX connection and update the index.jsp page with the lastest known information.
    It update the async context too in ShoutServlet
     */
    function getMessages(){
        if(!messagesWaiting){
            alert("getmessage")
            messagesWaiting = true;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange=function(){
                if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                    messagesWaiting = false;
                    var contentElement = document.getElementById("content");
                    contentElement.innerHTML = xmlhttp.responseText + contentElement.innerHTML;
                }
            }
            //make contact with the servlet to add a new context
            xmlhttp.open("GET", "shoutServlet?t="+new Date(), true);
            xmlhttp.send();
        }
    }
    //update the content after each post request
    setInterval(getMessages, 1000);
```

```
@WebServlet(urlPatterns = {"/shoutServlet"}, asyncSupported=true)
public class ShoutServlet extends HttpServlet {
    //every brownser page (index.jsp) running
    private List<AsyncContext> contexts = new LinkedList<>();
    @Override
    //add new context (browser page)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("do get");
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(10 * 60 * 1000);
        contexts.add(asyncContext);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //make a copy of the context
        List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
        //empty the original context
        this.contexts.clear();
        //get the new form information
        String name = request.getParameter("name");
        String message = request.getParameter("message");
        String htmlMessage = "<p><b>" + name + "</b><br/>" + message + "</p>";

        //Update the request with a new information
        ServletContext sc = request.getServletContext();
        if (sc.getAttribute("messages") == null) {
            sc.setAttribute("messages", htmlMessage);
        } else {
            String currentMessages = (String) sc.getAttribute("messages");
            sc.setAttribute("messages", htmlMessage + currentMessages);
        }

        //work in the background
        for (AsyncContext asyncContext : asyncContexts) {
            try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                writer.println(htmlMessage);
                writer.flush();
                asyncContext.complete();
            } catch (Exception ex) {
            }
        }
    }
}
```

*Summary*:  
We load "index.jsp", the function "getMessages()" start a first time after "onload", update the content of the current page (div id="content") and update the asyncContext array from the "doGet" function.

Then, when a user press submit it start the "postMessage()" function that retrieve the form content, empty it, create an ajax request that send a post request that the "doPost()" retrieve.

This last one retrieve every asyncContexts (different browser pages), retrieve the content of the new request POST, update the variable that contains the last message and put it to production for every pages (contexts).

A setInterval() function will call again the "getMessages()" every second and update the content from the postMessage update.  
(<a href="https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/async-servlet/async-servlets.html">example from here</a>)  

**COOKIE**

On internet, we need some persistance to manage some activities around the user. For example: remembering who is connected right now, save the shopping cart ...  
We can either user "cookie" (client side) or "session" (server side).

For the cookies, they contains "key:value" data format and are send by the server to the client navigator. When the client send an new request, the server may remember the client from the previous cookie. The server can update the cookie before sending it back to the client.

Client --> Serveur = request heading -> cookie(s)
Serveur --> Client = response heading -> Set-Cookie(s) 

JSP Page:  
```
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="CreationDUnCookie">Création d'un cookie</a>
<br/>
<a href="LectureDUnCookie">Lecture d'un cookie</a>
</body>
</html>
```
Create cookie:  
```
@WebServlet(name = "CreationDUnCookie", value = "/CreationDUnCookie")
public class CreationDUnCookieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = new Cookie("key", "value");

        cookie.setVersion(1);
        cookie.setComment("first cookie");
        cookie.setMaxAge(1000);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

```
Read cookie(s):  
```
@WebServlet(name = "LectureDUnCookie", value = "/LectureDUnCookie")
public class LectureDUnCookieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie [] arrCookie = request.getCookies();
        if(arrCookie != null) {
            for(Cookie cookie : arrCookie) {
                System.out.println("Cookie: "+cookie.getName());
                System.out.println("=> Valeur: "+cookie.getValue());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
```
Example: 
```
Cookie: Idea-653b643a
=> Valeur: bd05758f-4531-4714-aca6-83cac26ffbf9
Cookie: JSESSIONID
=> Valeur: 4E758E3F7EF22981E32D4FB48C9B40A0
Cookie: cle
=> Valeur: valeur
```

**SESSION**  
Session save the information on the server. When the user register for the first time, the server give back an unique ID via a cookie to the user.  
Next, the server will be able to know who is who with this ID.

Result from the example: 
```
La session vient d'être créée (bool): false
L'attribut nom n'existe pas encore
L'attribut prenom n'existe pas encore
/CookiTest_war_exploded/chemin
```

```
La session vient d'être créée (bool): false
L'attribut nom est: Dupont
L'attribut prenom est: Jean
/CookiTest_war_exploded/chemin
```

(<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Session%20url%20rewrite%20example">example here</a>)


**Filter**  
A filter catch up the HTTP Requests which it is interest in to do a "filter" action. It can happen as a pre-treatment before the resource access or after the servlet treatment.  
It is possible to use one or more filters and associate them to certain servlets.  
First, the filter is initiate (Filter.init) and eventually destroy (Filter.destroy).  
The treatment of the filter itself go by the function "doFilter()". This, once treated will start an other "doFilder()" on the previous method param "FilterChain chain" , so it can call the next filter or have a direct access to the resource if there is no more filter.  
A filter can be declared by a descriptor or an annotation. For the last one, there is no garantee of the treatment order and a descriptor must be use.
It is interesting to use to handle pre-connection, to execute some protection/security check on users input...  

For example, if we want to use two filters : 
*web.xml*  
```
    <filter-mapping>
        <filter-name>Filter1</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter-mapping>
        <filter-name>Filter2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```
*Filter1*  
```
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //do something to an attribute for example
        chain.doFilter(request, response);
    }
```
*Filter2*  
```
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //do something more to the same attribute for example
        chain.doFilter(request, response);
    }
```
*AServlet*  
```
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //do something with the parameter after it being filtered
        doGet(request, response);
    }
```

**EVENT**  
Exactly like event listeners, ie "onMouseClick Listener", when an event happen to a class it is possible to catch it for multiple purpose.
Depending of the "context", we have to implement different interface to catch what we're interested in. It can be context from the app, session or http request, 
*Example*:
```
public class ExempleListener implements ServletRequestListener, ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
```
When we detect an http request:
```

@Override
public void requestInitialized(ServletRequestEvent sre) {
    System.out.println("REQUESTINITIALIZED");
    ServletContext servletContext = sre.getServletContext();

}
```

**JSP in general**  
To have a well organized MVC Pattern, JSP Pages should be accessible only by a Servlet process and not directly through an URL. The servlet have to delegate the "generating response" to the JSP.
To do that, the JSP file have to be protected by being put in the WEB-INF and not into the webapp folder. Then, there is two ways to access it : create a RequestDispatcher or update the descriptor.

RequestDispatcher example:  
*Index.jsp*  
```
<br/>
<a href="AccessJSPProtegeeServlet">JSP PROTECTED Access</a> <br/>
```
*The servlet*  
```
u/Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/pageProtegee.jsp"); 
    rd.forward(request, response); 
}
```
The protected JSP  
```
<html>
<head>
 <title>You're on a protected page</title>  
</head>  
<body>  
<h1>Protected page not directly accessible by modifying the URL</h1>  
</body>  
</html>
```

When a request imply the exécution of a JSP (ie: servlet -> jsp) then two things happen.
Transformation of the jsp to a java file before the compilation into a class.

**Directives**  
There are 3 types of directives:  
-pages, taglib and include.

These give caracteristics to JSP pages to bring it up to the container during the "transformation" phase.  
The main directive is :  
```<%@ page contentType="text/html;charset=UTF-8" language="java" %>```

To import like in .java:   
``` <%@ page import="java.util.Date" %> ```

To create and access custom tag:   
``` 
<%@ taglib prefix="prefix" tagdir="/WEB_INF/tags" %>
<!-- ...-->
<prefix:tagName/>
```

To include (footer example):  
```
<!-- folder htmlPagesFragments in WEB-INF -->
<%@ include file="../htmlPagesFragments/footer.html" %>
```

**SCRIPT**

To write down java code into the JSP pages, just like in PHP, we have to use special tags to contain it.  
When it is not a statement:  
```
<% java code here %>
```  
If it's a statement:  
```  
<%= java statement here %>
```  
If it's the variables / methods zone:  
```
<%!  %>
```  

When a conversion happen, the code will automatically change. Example:  
```
out.write("<!DOCTYPE html>\r\n");
out.write(...);
for(int k = 0; k <=10 ; k++) {
out.print(k);
}
out.write(\r\n");
out.write("</body>\r\n");
out.write("</html>");
```

In this next example, we will create a list of sport in a servlet and draw the content of it into the JSP page:  
*index.jsp* 
```
<br/>
<a href="AccessObjectServlet">Acces Object Servlet</a>
<br/>
```  
*AccessObjectServlet*   
```
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Create an object list
        List<Sport> myArr = new ArrayList<>();
        myArr.add(new Sport("Tennis"));
        myArr.add(new Sport("Foot"));
        myArr.add(new Sport("Swimming"));
        myArr.add(new Sport("Rugby"));
        myArr.add(new Sport("Running"));

        //Save the object list and link it to an attribute to the request
        request.setAttribute("Sport", myArr);

        //prepare to redirect the request to an URL
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/drawList.jsp");
        //redirect it
        rd.forward(request, response);
    }
```  
*drawList.jsp*  
```
<%-- Directives --%>
<%@ page import="com.example.CookiTest.Sport" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
    //Variables and methods
    private List<Sport> listSport;
%>

<html>
<head>
    <title>My sport list</title>
</head>
<body>
    <h2>SPORT LIST: </h2>
    <% listSport = (List<Sport>) request.getAttribute("Sport"); %>
    <% if(listSport != null) { %>
        <label for="sport-select">Choose a sport:</label>
        <select name="sports" id="sport-select">
        <option><%= "" %></option>
            <% for(Sport sport : listSport) {%>
                    <option><%= sport.getNomSport() %></option>
            <% } %>
        </select>
    <% }else { %>
        <p>Sport list is empty</p>
    <% } %>
</body>
</html>
```  
*The result*:  
![image](https://user-images.githubusercontent.com/58827656/132954880-bfc3ff9a-c91b-40b6-b877-ff7aec7f2a48.png)


**ERROR HANDLING**

Error can happen during the transformation(1) or the compilation(2).  
(1) bad directives or scriptlet  
(2) error in the code itself (exceptions)

Beside "try/catch", we can handle compilation error by redirecting error to a dedicated handler page.  
Example:

*index.jsp*  
```
<br/>
<a href="ErrorServlet">Error handler test</a>
<br/>
```

*ErrorServlet.java*  
```
 @Override
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //prepare to redirect the request to an URL
     RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/drawErrorMess.jsp");
     //redirect it
     rd.forward(request, response);
 }  
```

*drawErrorMess.jsp*  
```  
<%--
    errorPage must link to the jsp page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="./handleError.jsp" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <p>There is an error in this page (we are in drawErrorMess.jsp)</p>
    <%
        String str = null;
        out.flush();
        /*
            This line alone will send the complet result when it's done
            wherease flush will send what he already have at the moment of the function call (buffer dump).

            This means that flush() will send out <p>There is an error ...</p> before failing the write(str) because it is null
            wherease with flush commented, we will not have the <p>...</p>
         */
        out.write(str);
    %>
</body>
</html>
```                                                                
*handleError.jsp*  
```
<%--
    isErrorPage must be TRUE
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <!-- exception is accessible only if isErrorPage is set to 'true' -->
    <h2>An error happened (we are at handleError.jsp)</h2>
    <p><%= exception.getClass().getName() %></>p>
    <p><%= exception.getMessage() %></p>
    <a href="<%= request.getContextPath()%>/index.jsp">clic here to return to index.html</a>
</body>
</html>
```  
<!> Note errorPage and isErrorPage <!>  

Error message with flush commented:  
![image](https://user-images.githubusercontent.com/58827656/133046819-6f76b785-4f20-4486-b0ef-cbfbd82ae5f8.png)  
Error message with flush decommented:  
![image](https://user-images.githubusercontent.com/58827656/133046868-82e0d27b-40df-4c6f-88a4-8c640967b987.png)  


It is possible to handle general error from "*transformation errors*" in the *web.xml* file.  
In this simple example, we will handle specificaly the '500' error code:  
```
<%-- Will generate a transformation error (500) --%>
<%= This is an error (no quotation marks) %>
```  
*error500.jsp*  
```
<body>
    <h2>ERROR 500</h2>
</body>
```

*web.xml*  
```
    <error-page>
        <error-code>500</error-code>
        <location>/WEB-INF/error500.jsp</location>
    </error-page>
```

**Use of fragment**

Fragment mean that we can reuse file structure multiple time in other files. We already saw include.  
When we want to include an HTML page for example, we can use static include:  
```
<%@ include file="/WEB-INF/footer.html"%>
```  
Or a dynamic include. We can't update the request from it. We gonna use what is called "standard action":  
```
<jsp:include page="header.jsp"></jsp:include>
```
Example:  
*index.jsp*  
```
<br/>
<a href="ConnectionServlet">Static and dynamic inclusion example</a>
<br/>
```
*ConnectionServlet.java*  
```
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     response.setContentType("text/plain");
     HttpSession session = request.getSession();
     session.setAttribute("userConnect", "invited");
     response.sendRedirect(request.getContextPath() + "/ListOfAvailableSportsServlet");
 }
```  
*ListOfAvailableSportsServlet.java*  
```
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //Create an object list
     List<Sport> myArr = new ArrayList<>();
     myArr.add(new Sport("Tennis"));
     myArr.add(new Sport("Foot"));
     myArr.add(new Sport("Swimming"));
     myArr.add(new Sport("Rugby"));
     myArr.add(new Sport("Running"));

     //Save the object list and link it to an attribute to the request
     request.setAttribute("Sport", myArr);
     request.setAttribute("Ressource", "header.jsp");
     //prepare to redirect the request to an URL
     RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/drawAvailableSports.jsp");
     //redirect it
     rd.forward(request, response);
 }
```  
*drawAvailableSports.jsp*  
```
<%@ page import="com.example.CookiTest.Sport" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Available Sports</title>
</head>
<body>
    <!-- header (dynamic include from a variable) -->
    <% String ressource = "/WEB-INF/"+ (String) request.getAttribute("Ressource"); %>
    <jsp:include page="<%=ressource%>"></jsp:include>

    <p>List of sports: </p>
    <% List<Sport> myList = (List<Sport>) request.getAttribute("Sport"); %>
    <label for="sport-select">Choose a sport:</label>
    <select name="sports" id="sport-select">
        <option value="">Choose something</option>
        <% for(Sport sport : myList) { %>
        <% String sportName = sport.getNomSport(); %>
        <option value="<%= sportName %>"><%= sportName %></option>
        <% } %>
    </select>

    <!-- static include -->
    <%@ include file="/WEB-INF/footer.html"%>
</body>
</html>
```  
*header.jsp*  
```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>HEADER</title>
</head>
<body>
    <header>
        <h2>Welcome to <%= session.getAttribute("userConnect") %></h2>
    </header>
</body>
</html>
```  
*footer.html*  
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Footer</title>
</head>
<body>
    <footer>FOOTER PAGE</footer>
</body>
</html>
```  

**JAVA Bean and Standard Action**  

A "Bean" is a java object respecting 4 rules:  
-Being public  
-Implementing the Interface "Serializable"  
-Having a default constructor (mandatory declared if other constructors exist)  
-Having a 'couple' of "getter / setter" for every variables members of the class.
These methods must respect the typography: set / get + NameOfTheVariable  

Example:  
``` 
Public class MyBeanClass implements Serializable {
              private String myVar;
	public MyBeanClass() {
		//
               }

	public MyBeanClass(String str) {
		//
	}
	public void setMyVar(String varValue) {
		this.myVar = varValue;
}
	public String getMyVar() {
	return this.myVar;
}
}
```



A Bean is attached to a "context" : page, request, session or application.
An application means that it is available for every pages on the programs.
For example, "page" means it is only accessible in the jsp file where it is declared.
Something important, it is a java class made to be reused.
Then, a bean is pre-existing or have to be made.

Pre-Existing:
```
<jsp:useBean 
   id="marin" 
   beanName="marin" 
   scope="request" 
   type="org.paumard.cours.model.Marin"
/>

```

**id**: name of the object instance to retrieve or create. It give the possibility to manipulate the object.  
**beanName**: this attribut means that we're using a bean already made.  
**scope**: it is the bean interaction range put by the scope parameter.  
**type**:  address of the object class. If it is not there, check out *class*.

To create: 

```
<jsp:useBean 
    id="marin" 
    scope="page"  
    class="org.paumard.cours.model.Marin">

    <jsp:setProperty name="marin" property="name"    value="Surcouf"/>
    <jsp:setProperty name="marin" property="surName" param="surName"/>
    <jsp:setProperty name="marin" property="age"    param="age"/>
</jsp:useBean>

```  
The use the class parameter rather than "**type**" and the lack of "beanName" means that we're creating a bean.  
The attribute "name" have to match the "**id"**.  
**setProperty** will call setters (setName, setSurName and setAge), impose a value for "name" and will retrieve in the http request the data of surName and age (param).



To work with the object "marin":
```
    <body>
        <p>Nom = ${marin.nom}</p>
    </body>

```
This notation is named "EL (Expression Langage)"

An example:  

*Index.jsp*
```
<br/>
<a href="Redirect1Servlet">Action standard, exemple 1</a>
<br/>
```
*Redirect1Servlet*
```
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("ageValueParam", 18);
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/actionStandard.jsp");
    rd.forward(request, response);
}
```  
*actionStandard.jsp*  
```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Action Standard JSP</title>
</head>
<body>
    <% int weight = 100; %>
    <jsp:useBean
            id="person"
            scope="page"
            class="com.example.CookiTest.Person">

            <jsp:setProperty name="person" property="name" value="MyNameValue"/>
            <jsp:setProperty name="person" property="surName" value="MySurnameValue"/>
            <jsp:setProperty name="person" property="age" param="ageValueParam"/>
            <jsp:setProperty name="person" property="weight" value="<%= weight %>"/>
    </jsp:useBean>
    <p>Value of the parameter 'ageValueParam': <%= request.getAttribute("ageValueParam")%><p/><br/>
    <h2>Hello ${person.name} (<jsp:getProperty name="person" property="name"/>)</h2>
    <p>You're surname is ${person.surName} (<jsp:getProperty name="person" property="surName"/>)</p>
    <p>And your age is ${person.age} (<jsp:getProperty name="person" property="age"/>)</p>
    <p>Your weight is ${person.weight} (<jsp:getProperty name="person" property="weight"/>)</p>

    <!-- You can not directly pass an Object using jsp:include or :forward -->
    <%--
    <p>Forward jsp:
    <jsp:forward page="/WEB-INF/forwardJSP.jsp">
        <jsp:param name="person" value="${person.name}"/>
    </jsp:forward> </p>
    --%>

    <jsp:include page="/WEB-INF/footer.html"></jsp:include>
</body>
</html>
```  
*forward.jsp*  
```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <p>Forward</p>
    <p><%= request.getParameter("person") %></p>
</body>
</html>
```  
*Person.java*  
```
package com.example.CookiTest;

public class Person {
    private String name;
    private String surName;
    private int age;
    private int weight;

    public Person() {
        //
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSurName(String surname) {
        this.surName = surname;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }
    public String getSurName() {
        return this.surName;
    }
    public int getAge() {
        return this.age;
    }
    public int getWeight() {
        return this.weight;
    }
}
```
**Expression Langage (EL)**

It is a way to replace the use of java in JSP pages. It gives the possibility to handle java object and logical expression but no if or for.  
It is easier to access variable from different contexts and easier to handle error as if it is null, it will not create an error but just return null.

Example with a session (sessionScope):  
**  
```
<br/>
<a href="SessionServlet">Result by java and EL</a>
<br/>
```  

*SessionServlet*  
```
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        HttpSession session = request.getSession();

        Person person = new Person();
        person.setAge(80);
        person.setName("O'Neil");
        person.setSurName("Peter");
        Address address = new Address("New Address");
        person.setAddress(address);

        session.setAttribute("person", person);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/resultJavaAndEl.jsp");
        rd.forward(request, response);
    }
```  

*resultJavaAndEl*  
```
    <!-- Need to handle errors and typography is more complicated-->
    <% Person person = (Person) session.getAttribute("person"); %>
    <% if (person != null) { %>
        <h1>Result from the person using java</h1>
        <p>Name: <%= person.getName() %></p>
        <p>Surname: <%= person.getSurName() %></p>
        <p>Age: <%= person.getAge() %></p>
        <% if(person.getAddress() != null) { %>
            <p>Address: <%= person.getAddress().name %></p>
        <% } %>
    <% } %>
    
    <br/><br/><br/>

    <!-- Error will return a 'null', easier to avoir error and easier typography -->
    <h1>Result from the person using EL</h1>
    <p>Name: ${sessionScope.person.getName()}</p>
    <p>Surname: ${sessionScope.person.getSurName()}</p>
    <p>Age: ${sessionScope.person.getAge()}</p>
    <p>Address: <%= person.getAddress().name %></p>
```  
Result:  
![image](https://user-images.githubusercontent.com/58827656/133282465-e869fc3a-84d5-459d-b658-e7a0597dfb61.png)

If we comment:  
```
//Address address = new Address("New Address");
//person.setAddress(address);
```
And use this:  
``` <p>Address: <%= person.getAddress() != null ? person.getAddress():"no address" %></p>  ``` 

We have this result instead: "Address: no address"

**JSTL**  

Java server page Standard Tag Library (*JSTL*) is a library with tags available that can be use in JSP pages to bring more java like functionnalities.  
To make it work :  
``` <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> ```  
Examples:  
*Passing parameters in URL*  
```
<li><a href="${pageContext.request.contextPath}/JspparamServlet?p1=A&p2=B&p3=C">click me</a></li>
    <li>
        <a href="
        <c:url value="/JspparamServlet">
            <c:param name="p1" value="A"/>
            <c:param name="p2" value="B"/>
            <c:param name="p3" value="C"/>
        </c:url>"
        >clickmeJSTL</a>
    </li>
```
To use information from a form:  
```
    <form method="get" action='Views/Jspparam/jstlparam.jsp'>
        <table>
            <tr>
                <td>Prénom:</td>
                <td><input type='text' name='prenom' /></td>
            </tr>
            <tr>
                <td>Nom:</td>
                <td><input type='text' name='nom' /></td>
            </tr>
            <tr>
                <td>Langages que vous connaissez:</td>
                <td><select name='langages' size='7' multiple='true'>
                    <option value='C'>C</option>
                    <option value='C++'>C++</option>
                    <option value='Objective-C'>Objective-C</option>
                    <option value='Java'>Java</option>
                </select></td>
            </tr>
        </table>
        <p>
            <input type='submit' value='Afficher!' />
    </form>
```  
Other:  
```
        <!-- Secured Display -->
        <p><c:out value="${ param.p1}"/></p>
        <p><c:out value="${ param.p2}"/></p>
        <p><c:out value="${ param.p3}"/></p>
        <p><c:out value="${ requestScope.test}"/></p>
```
```
        <!-- affectate value to a variable + displaying it -->
        <c:set var="uneVariable" value="${5 * 5}" />
        <p><c:out value="${ uneVariable }"/></p>
```
```
        <!-- condition -->
        <c:if test="${uneVariable > 20}">
                <p>variable is superior to 20</p>
        </c:if>
```
```
<!-- switch/case -->
<c:choose>
	<c:when test="${uneVariable == null}">Variable null</c:when>
	<c:when test="${uneVariable > 0}">Variable > 0</c:when>
	<c:otherwise>Variable < 0</c:otherwise>
</c:choose>
```
```
<!-- simple array -->
<table style="border:1px solid #333">
	<thead style="background-color: #333;color: #fff;">
		<th colspan="6">TABLE HEADER</th>
	</thead>
	<tbody>
		<tr>
			<c:forEach var="i" begin="0" end="5" step="1">
				<td style="border:1px solid #333"><c:out value="${i}"/></td>
			</c:forEach>
		</tr>
	</tbody>
</table>
```
```
<!-- foreach map -->
<%
  ArrayList list = new ArrayList<>();
  list.add("1");list.add("2");list.add("3");
  pageContext.setAttribute("list", list);
%>
<c:forEach items="${list}" var="value">
	<p><c:out value="${value}"/></p>
</c:forEach>
```
```
<!-- hashmap -->
<table>
	<c:forEach var="entry" items="${headerValues}">
		<tr>
			<td><c:out value="${entry.key}"/></td>
			<td><c:out value="${entry.value}"/> </td>
		</tr>
	</c:forEach>
</table>
```
```
        <!-- Iterating hashmap<String, ArrayList> -->
    <table>
    <c:forEach items="${paramValues}" var="it">
        <tr>
            <td><c:out value="${it.key}"/></td>
            <c:if test="${it.key == 'langages'}">
                <c:forEach items="${it.value}" var="value">
                    <td><c:out value="${value}"/></td>
                </c:forEach>
            </c:if>
        </tr>
    </c:forEach>
    </table>
```

What is a template in this context ?  It is a way to save a recurring page organization (tag, script, css import, ...) and use "placeholders" to incorporate dynamic data to it.  
JSP give this functionality and use "fragments" (html content).  
First, we create a template into the "/WEB-INF/tags" folder:  
(*simple_layout*)  
![image](https://user-images.githubusercontent.com/58827656/135811733-6628580e-0714-4c57-a420-7b33216d21b2.png)

Attributes tag declare our fragment variables then jsp:invoke point the placeholders to the right place.  
Then, we have our JSP page that will call the previous template and fill it with it's data :  
(*mypage.jsp*)  
![image](https://user-images.githubusercontent.com/58827656/135812051-e72267db-7ae4-445f-a9cb-cd580a68bae4.png)  
(A placeholder is optional)   

*Summary* :  
Index.jsp send a HTTP request to "mypage.jsp"  
mypage.jsp call the template "simple_layout" and incorporate it  
At this moment, mypage.jsp detect the placeholders and fill them with it's informations  

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/JSTL%20template%20example%201">JSTL + Template example</a>  

### JDBC and JPA - data persistence ###

Oracle wrote what is called "Specifications" / standards (JSR xxx) on how technologies should work.  
**JDBC** (*Java DataBase Connectivity*) is one result of it. It is a Java API use to connect application with database. No matter which database is used, it will work exactly the same (no to marginal code change).  

**JPA** (*Java Persistence API*) is an other specification use by different java framework like **Hiberate**. These frameworks are called **ORM** (*Object Relational Mapping*) and give the possibility to work with database using object and without SQL request.  

**JDBC**

Companies behind database provide different types of "drivers" that implements the different interface of the JDBC to make it work. 

To comunicate with a database, a connection have to be establish. Then, three types of request exist:  
-*Statement* (used to Create, alter, drop (...) db. It does have bad performance so it have to be use occasionaly. No parameter !)  
-*PreparedStatement* (better perf and parameters accepted) 
-*CallableStatement* (execute stored procedures)  

**How to connect to databases**  

*CODE EXAMPLE FOR ORACLE*  
```
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        //CALL THE DRIVER
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            //MAKE THE CONNECTION (database name = "test", pw = "test")
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","test","test");
            out.println(conn.isClosed()?"close":"open");
    	} catch (SQLException e) {
	    e.printStackTrace();
    	}
	conn.close();
	out.close();
}
```  

*Example of Statement && preparedStatement*
```
[...]
            //count of inserted data
            int count = stmt.executeUpdate("insert into Person values(4,'Irfan','50000')");

            String sql = "select * from Person where id=1";
            //or executeQuery(sql) that is better to use for SELECT query
            boolean isResultSet = stmt.execute(sql);

            if (isResultSet) {
                ResultSet rs = stmt.getResultSet();
                //only 1 result, if is enough
                if (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    // ID: 1, NAME: Irfan SURNAME: 50000
                    System.out.println("ID: " + id + ", NAME: " + name + " SURNAME: " + surname);
                }
            }

            String preparedStatementStr = "SELECT * FROM Person WHERE id=? AND name=?";
            PreparedStatement ps = conn.prepareStatement(preparedStatementStr);
            ps.setInt(1, 28);
            ps.setString(2, "toto");
            //ID: 28, NAME: toto SURNAME: supertoto
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                //ID: 1, NAME: Irfan SURNAME: 50000
                System.out.println("ID: " + id + ", NAME: " + name + " SURNAME: " + surname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Client client = new Client();
        client.setName("Super");
        client.setSurName("Louis");
        
        String sqlInsert = "insert into Person(name, surname) values (?, ?)";

        try {
            PreparedStatement ps2 = conn.prepareStatement(sqlInsert);
            ps2.setString(1, client.getName());
            ps2.setString(2, client.getSurname());

            int count = ps2.executeUpdate();

            if (count > 0) {
                System.out.println("good " + count);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
```

It is possible to create an object from a resultset: 
```
    while (rs.next()) {
	String id = rs.getString("id");
	String name = rs.getString("name");
	String surname = rs.getString("surname");
	//check if the last rs was null (surname here)
	if(rs.wasNull()) {
	    surname = "is null";
	}
	//ID: 1, NAME: Irfan SURNAME: 50000
	System.out.println("ID: " + id + ", NAME: " + name + " SURNAME: " + surname);

	Client client = new Client();
	client.setName(name);client.setSurName(surname);
	List<Client> listClient = new ArrayList<>();
	listClient.add(client);
    }
```

If a primitive type is null, then the result will be 0. If it is an object type (String, Date...), the result will be "null".

**Transaction**

A transaction describe every steps from the beginning of an activity until it is done (ie when you buy something on the internet).  
With SQL, a transaction happen when you ask or do something to a database. When something fail during the transaction, you can avort what you already pushed.
For example, if your process have to do 5 things to be successfull but that last one failed, then you may want to come back to the 4th or even cancel everything you did from the first one to the last action.

Different types of transaction mod exist:  
-*dirty read* (TRANSACTION_READ_UNCOMITTED) (you are interacting with db data that have not been yet validated by an other transaction (commit))  
-*not reproductibl*e read (TRANSACTION_READ_COMITTED) (first transaction read a line followed up by an other transaction reading the same line. The first update the value as the second one is doing the same)  
-*ghost read*   (TRANSACTION_REPEATABLE_READ) (first transaction recover a resultset from x sql request just before an other transaction update data concerning the same x. If the first transaction sql happen again, it will more data)  

A transaction have to follow the ACID properties:  
-**Atomicity (1)**: a transaction is either 100 % good or avorted.   
-**Consistency (2)**:   consistency between transaction of the system in place.  
-**Isolation (3)**:   transactions are isolated from one to an other (concurrent).  
-**Durability (4)**:   a successfull change made to the database (transaction) will remain permanently.  

(1): A = 10 and B = 10. A gives 10 to B but while A is now 0, B isn't 20.  
(2): A = 50 and B = 50. A + B should always be 100. If A+B=90 then it is not concistent.  
(2): If an object A is persistent while having other objects in it that are not, it cause an issue.  
(3): A = 10 and B = 10. T1 : A gives 5 to B. T2 : B gives 15 to A.  If at one point, B gives 15 to A before it does receive the extra 5 from A, something is wrong.  
(4): Something happen at one point during the transaction. The user receive the confirmation while the transaction failed.  

*Small transaction example*:  
```
// 1 - Transcient object  (java / garbage collector)
        Pays pays = new Pays();
        pays.setCode("is");
        pays.setLangue("Islandais");
        pays.setNom("Islande");

// 2 - object trying to be persistent (into the cache)
        Transaction tx = null;
        try {
	        //start transaction and link it to the ongoing session
                tx = session.beginTransaction();
                //save the object into the cache where persistent object are (data graph)
                session.save(pays);
                //commit the change to the database (not dirty anymore)
                tx.commit();
// 2.1 - may fail (rollback to the previous valid state)		
        } catch (RuntimeException e) {
                if (tx != null)
                        tx.rollback();
                throw e; // Gérer le message (log, affichage, etc.)
        } finally {
	        //sessions's objects are now detached
                session.close();
        }

// 3 - 'pays' is now detached from the session because it is close (detached = persistent + session close())  
```
A persistent object but not yet commited is concidered "**dirty**".  

![image](https://user-images.githubusercontent.com/58827656/137880534-efac13e3-e87f-481c-8e4b-a5448ea95b36.png)  


*Example of a simple transaction*:  
```  
@WebServlet(name = "TransactionServlet", value = "/TransactionServlet")
public class TransactionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //CALL THE DRIVER
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            //MAKE THE CONNECTION
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "test", "test");
            //if we use 'transaction principle' or not
            conn.setAutoCommit(false);
            //get and set transaction isolation mod (dirty, not reproductible and ghost)
            int transactionIsolationMod = Connection.TRANSACTION_SERIALIZABLE;
            conn.setTransactionIsolation(transactionIsolationMod);
            //LIFO Array
            Queue<Savepoint> queue = Collections.asLifoQueue(new ArrayDeque<>());
            //create a savepoint before any sql operation
            Savepoint save1 = conn.setSavepoint();
            //save it to our LIFO Array (ie: array{save1, save2})
            queue.add(save1);

            //Create 2 client object
            Client client = new Client();
            client.setName("Super");client.setSurName("Louis");
            Client client2 = new Client();
            client2.setName("Not Super");client2.setSurName("Still Louis");
            Client client3 = new Client();
            client3.setName("");client3.setSurName("");
            //save them into a client array
            List<Client> clientList = new ArrayList<>();
            clientList.add(client);clientList.add(client2);clientList.add(client3);

            String sqlInsert = "insert into Person(name, surname) values (?, ?)";

            for(Client myClient : clientList) {
                boolean bool = true;
                try {
                    PreparedStatement ps = conn.prepareStatement(sqlInsert);
                    ps.setString(1, myClient.getName());
                    ps.setString(2, myClient.getSurname());

                    int count = ps.executeUpdate();
                    System.out.println(myClient.getName()+" "+myClient.getSurname());
                    if(myClient.getName().equals("") && myClient.getSurname().equals("")) {
                        System.out.println("Error, name and surname are empties (rollback)");
                        System.out.println(myClient.getName()+" "+myClient.getSurname()+" will not be saved to the BDD");
                        conn.rollback(queue.remove());
                        bool = false;
                    }
                    //if not failed the previous if statement then ...
                    if(count > 0 && bool) {
                        //make a save after each successful executeUpdate
                        queue.add(conn.setSavepoint());
                        System.out.println("save point success");
                    }
                }catch(SQLException e) {
                    System.out.println("! Error !"+e.getErrorCode());
                    //array{save1, save2} => array{save1, X} => array{X, X} (x mean delete)
                    conn.rollback(queue.remove());
                }
            }
            System.out.println("MyClient list size: "+clientList.size());
            //queueArray {before statement, first client, second client, third client}
            //in the end: {before statement, first client, second client, X} (x mean delete)
            System.out.println("Queue size (savepoint) "+queue.size());
            //release the transaction and start a new one
            conn.commit();
            //[...]
            conn.close();

            //FINAL RESULT
            /*
                Super Louis
                save point success
                Not Super Still Louis
                save point success

                Error, name and surname are empties (rollback)
                  will not be saved to the BDD
                MyClient list size: 3
                Queue size (savepoint) 2
             */

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
```

**JPA SPECIFICATION**

The JPA API is a specification and ORM like Hibernate follow it and are the actual implementation.
It has multiple functionnalities like:  
-automate the mapping of the relational data and variables members (object associated variables).  
-automate part of the sql code.  
-bring standardization / good practice to the java-database relationship.  

*Entities* or *Persistent classes* are presented as java objects which represent data from the database.
Exactly like *beans*, it has to follow the same rules:  
-Being public / protected (not final !)  
-Implementing the Interface "Serializable"  
-Having a default constructor (mandatory declared if other constructors exist)  
-Having a 'couple' of "getter / setter" for every variables members of the class. These methods must respect the typography: set / get + NameOfTheVariable()  

Entities classes need to be identifiable, this principle is called *persistency idendity* of an entity.  
In the database, this is characterized by the *primary key* of a table.  

How to declare an entity (@Entity annotation) and have avalide entity class:    
``` 
@Entity
public class Client implements Serializable {
     private String name;
     private Long id;
     
     public Client() {
          //
     }

     protected setName(String name) {
          this.name = name;
     } 
     protected getName() {
          return this.name;
     }
    public void setId(Long id) { this.id = id; }
    @Id
    public Long getId() { return id; }
}
```  
An *Entities Manager* implement the interface "*EntityManager*". It establish a link between java classes and the database data.  
For each databases, it handle what we call a "*persistency context*" that contains every entities from a program.  A persistency context must have a unique "*persistency identities*".  
Exactly like in a database, you can't have 2 objects with the same persisency identities aka "same ID".  
It is characterized by a "*persistency unit*" (parameters within *persistence.xml*).  
His role is to manage the different entities within these contexts.  It can :  
-create or delete entities    
-Look at an entity by his ID or by more sophisticated ways.  

There are different types of entities:  
- *transient entity* (it doesn't exist yet in the database, isn't in a persistency context yet and doesn't have a persistency identity yet).  
- *managed entity / persistent entity* (complet opposite of the previous entity described). If we want a persistent object with an already known id into the db then we use saveOrUpdate().  
- *detached entity* (like the previous entity described except the persistency context is close).  
- *deleted entity* (doesn't exist in a *persistency context* and **may have not yet been** deleted from the database).    

*Transaction* are managed by the JTA (*Java Transaction API*). Tomcat doesn't implement it but it can be use manually by an object that implement the interface "*EntityTransaction*".

*Example of the persistence.xml*:  
```
    <!-- name of the persistence unit -->
    <persistence-unit name="PU_JPA">
        <!-- Declare entity classes -->
        <class>com.example.jpaProject.Client</class>
        <!-- <class>...</class> -->

        <!-- additional standards proprieties information (ie database connection) -->
        <!-- have to add the obdj14.jar to the library -->
        <properties>
            <property name="javax.persistence.jdbc.driver" value="oracle.jdbc.OracleDriver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:xe" />
            <property name="javax.persistence.jdbc.user" value="test" />
            <property name="javax.persistence.jdbc.password" value="test" />

            <!-- log of the SQL Request -->
            <property name="hibernate.show_sql" value="true"/>
            <!-- improve the previous functionality -->
            <property name="hibernate.format_sql" value="true"/>
            <!-- destroy and re-create the database from the entities (classes/tables) when the app start -->
            <property name="hibernate.hbm2ddl.auto" value="create"/>
        </properties>
    </persistence-unit>
```
What create and other proprieties logs:  
```
Hibernate: 
    
    drop table Client cascade constraints
Hibernate: 
    
    create table Client (
       id number(19,0) not null,
        email varchar2(255 char),
        name varchar2(255 char),
        surname varchar2(255 char),
        primary key (id)
    )
    //[...]
```
*Connection* is replaced by an *EntityManager* object that came from *EntityManagerFactory* lnked to a specific unit.  
![image](https://user-images.githubusercontent.com/58827656/133973785-4efe60c8-801f-444f-bb40-cead6a613284.png)  

 => <a href="https://github.com/codeFliers/JAVA-EE/tree/main/JPA%20Connection%201">check here</a>  
To create only one EntityManagerFactory, we can create a listener that will listen to http requests.  
We use a JPAUtil class for convenient purpose to not rewrite "ApplicationListener.getEmf().createEntityManager()" each time.  
 
**Setup of the classes mapping**  
Hibernate can partially automate the conversion of our classes to tables into the database of our choice and determined by the property "hibernate.hbm2dd1.auto=' ' " from prestitence.xml.

To be elligible:  
-@Entity annotation  
-being declared in persistence.xml  
-Correct synthax with getters/setters  

In the next example, we have what is called "Single Entity with @SecondaryTable".  
Meaning that we have enough data in one classes to make 2 tables.  
We will create a 1 to 1 relation between the 2 classes "Client" and "Preferences".  
![image](https://user-images.githubusercontent.com/58827656/134035898-d440c436-8590-492e-a2d2-b396ab6f096e.png)  

How does our classes look like in SQL:   
![image](https://user-images.githubusercontent.com/58827656/134035932-02a2f030-2187-4584-ac03-e5acdf011d68.png)  

In this example, we renamed the class to "Clients" by using "*@Table(name="clients")*".  
We declared the name and to which classes the variables were aimed to.  
The "*SecondaryTable*" annotation means that we have a 1To1 relation.  
*Name* is the secondary table name. pkJoinColumns contains the primary key (s) of the secondary table.  The "*PrimaryKeyJoinColumn *" is our 1to1 and foreignkey, the name of this FK relation.  
```
//CLient is an entity
@Entity
//Rename the entity to clients with an 's'
@Table(name="clients")
//add a 'preferences' table
@SecondaryTable(name="preferences",
        //1 to 1 relationship between 'identifiant' FROM Client and
        //identifiant_client FROM Preferences
        pkJoinColumns=@PrimaryKeyJoinColumn(name="identifiant_client"),
        //name of the constraint
        foreignKey=@ForeignKey(name="fk_preferences_clients")
)
```

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/JPA%20persistence%20example%201">Example here</a> 


**JPA inheritance strategy**  
With JPA, we can use different stategies to translate inheritance in different ways.
Examples:  

*--JOINED--*  
-each classes have their own tables  
-subclasses do not contains variables from the root class  
-Root class have a primary key that is shared with subclasses with the exception it is FK too.  
-The root class have the **@Inheritance** annotation  and the **@DiscriminatorColumn**  
-The subclasses does have a **@DiscriminatorValue**  and extends the root class  

*How does it work ?*    
The "DiscriminatorColumn" is a variable created by the annotation that will have the value of the "DiscriminatorValue". If we select a row in the root, emp_type will have the value of one of the discriminatorValue from the subclasses.  
![image](https://user-images.githubusercontent.com/58827656/134137039-8df4c51b-176d-4be8-8cae-336e80137585.png)  

<a href="https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/joined-table-inheritance.html">Example here</a>
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/JPA%20inheritance%20example%201">Example here</a>

*--SINGLE_TABLE--* 
-Variables from all the subclasses will be in the root classes.  
-The root class need to have the annotation @Inheritance(strategy=InheritanceType.SINGLE_TABLE)  
-The root class need a @DescriminatorColumn to know from which class a variable is.  
-Each classes need a @DescriminatorValue to know from where it comes.  
-Names of variables have to be différent.  

In this example, we will have **3 classes** : **Person** which is *extended* by **Mayor** which have a *OneToOne* relationship with **Town**.  
![image](https://user-images.githubusercontent.com/58827656/134507878-7a20ee09-b3b9-4318-8810-72d9028eee2c.png)  

Person and Mayor are only one table now. The *@OneToOne* relation on the *Town town* object has been translated to a *Primary* and *oreign key* referencing id from town.  

A *Mayor* handle only one *Town* at a time.  
A *Town* is handle by only one *Mayor* at a time.

In this context, we have 1.1+1.1 => oneToOne relation.  Both could have the PK/FK Key or even the two at the same time (not a good idea). But because Person is only one class, it is better to have Mayor handling it.  

One down side, the *town_id* can be **null** when the Person is not a Mayor. If he isn't anymore, he will have to be removed too.  
<a href=http://blog.paumard.org/cours/jpa/chap05-heritage-single-table.html>Source here</a>  
<a href=https://github.com/codeFliers/JAVA-EE/tree/main/SINGE_TABLE%20example%201>Source Code here</a>  

*--TABLE_PER_CLASS--*  
We will not talk about it.  

**Composite PK**  

Composite means that the primary key is not unique and multiple PK are needed to assure the job.  
Let's replace the id from Client by using as a composite key both name and surname.
In order to do this:  
-JPA will create a "*technical class*" : *@idClass(ClientPK.class)* 
```
@Entity  
@Table(name="clients")  
@idClass(ClientPK.class)  
```  
Then Client class will have to declare both name and surname variable with the annotation @id:  
```
@Id  
private String name;
@Id  
private String surname;
```  
Next, we have to create the "*ClientPK*" class. It have to follow these rules:  
-the two @id variable have to be here with the same types as in the original class  
-the class must be Serializable  
-Getters / Setters for every variables from the main class  
-Default constructor   
-@Override hashcode and equals methods  

```
import java.io.Serializable;

public class ClientPK implements Serializable {
    private String name;
    private String surname;

    public ClientPK() {
        //
    }

    public ClientPK(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    //GETTERS && SETTERS =>
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getSurname() {
        return this.surname;
    }

    //<=
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.surname.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ClientPK){
            ClientPK clientPK = (ClientPK) o;

            if (this.getName().equals(clientPK.getName())) {
                if(this.getSurname().equals(clientPK.getSurname())) {
                    return true;
                }
            }
        }
        return false;
    }
}
```  
As a result, in SQL we should have : *primary key (name, surname)*.  

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Composite%20key%20example%201">Code example here</a>  

A composite key can be composed of objects. Lets see this UML example :   
A **Client** can have 1 or more **Ticket**  (1.*)    
A **Ticket** designate only 1 **Client**  (1.1)    

A **Sport** can be accessed by 1 or more **Ticket** (1.*)    
A **Ticket** designate only 1 **Sport** at a time (1.1)    
 
We have to create a Ticket class like we did in our previous Client example with other parameters : 
```
@Entity
@Table(name="TICKETS")
@IdClass(TicketPK.class)
public class Ticket implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="identifiant_client",
            foreignKey=@ForeignKey(name="FK_TICKETS_CLIENTS"))
    private Client client;
    @Id
    @ManyToOne
    @JoinColumn(name="identifiant_sport",
            foreignKey=@ForeignKey(name="FK_TICKETS_SPORTS"))
    //@Column(name="sport", table="TICKETS", nullable = false)
    private Sport sport;
    //[…]
}
```

Then, we have to create the **TicketPK** technical class. Our objects will have an "int" type :  
```
import java.io.Serializable;
public class TicketPK implements Serializable {
    private int client;
    private int sport;

    public TicketPK() {
        //
    }

    public void setClient(int client) {
        this.client = client;
    }
    public int getClient() {
        return this.client;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }
    public int getSport() {
        return this.sport;
    }

    @Override
    public int hashCode() {
        return this.getClient() + this.getSport();
    }
    @Override
    public boolean equals(Object o) {
        boolean boolRes = false;
        if (o != null && o instanceof TicketPK) {
            TicketPK ticketPK = (TicketPK) o;
            boolRes = ( ticketPK.getClient() == this.getClient() && ticketPK.getSport() == this.getSport() );
        }
        return boolRes;
    }
}
```  
As a result, we have :  
```
Hibernate: 
    alter table TICKETS 
       add constraint FK_TICKETS_CLIENTS 
       foreign key (identifiant_client) 
       references clients
Hibernate: 
    alter table TICKETS 
       add constraint FK_TICKETS_SPORTS 
       foreign key (identifiant_sport) 
       references Sport
```

In our database, we have : 
```
CREATE TABLE  "TICKETS" 
   (	"IDENTIFIANT_CLIENT" NUMBER(10,0) NOT NULL ENABLE, 
	"IDENTIFIANT_SPORT" NUMBER(10,0) NOT NULL ENABLE, 
	 PRIMARY KEY ("IDENTIFIANT_CLIENT", "IDENTIFIANT_SPORT") ENABLE, 
	 CONSTRAINT "FK_TICKETS_CLIENTS" FOREIGN KEY ("IDENTIFIANT_CLIENT")
	  REFERENCES  "CLIENTS" ("IDENTIFIANT") ENABLE, 
	 CONSTRAINT "FK_TICKETS_SPORTS" FOREIGN KEY ("IDENTIFIANT_SPORT")
	  REFERENCES  "SPORT" ("IDENTIFIANT") ENABLE
   )
/
```  
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Composite%20key%20on%20object%20example%201">Code here</a>  

Note : If an @Id is auto-generated, the setter should be private.  

**Composite key using @EmbeddedId, @Embeddable and @MapsId**  
In Tickets:  
-We replace *@Id* that designated our composite key by *@MapsId* (same name as in carnetPKEmbedded and different from Tickets class).  
-We delete *@IdClass* and replace it in the class with @EmbeddedId on top of an object *private CarnetPKEmbedded carnetPKEmbedded*.  
-We instanciate the previous object in the default constructor.  
-We add a public getter and a private setter for the *carnetPKEmbedded* object.  
Then, we create *CarnetPKEmbedded*, with the annotation *@Embeddable* and the *Serializable* implementation, that will contains getters/setters, hashCode and equals overrides.  

Our technical class is linked to his class by an object. We designate the composite key by using the *@MapsId*. The object class look like the same as the previous example with an annotation *@Embeddable*.  
Embeddable designate the component table (composite), MapsId the id that will be present to compose the component and EmbeddedId create a link to the technical class.   

```
@Entity
@Table(name="TICKETS") //<=
public class Ticket implements Serializable {
    @EmbeddedId //<=
    private TicketPKEmbedded ticketPKEmbedded;

    @MapsId("idClient") //<=
    @ManyToOne
    @JoinColumn(name="identifiant_client",
            foreignKey=@ForeignKey(name="FK_TICKETS_CLIENTS"))
    private Client client;

    @MapsId("idSport") //<=
    @ManyToOne
    @JoinColumn(name="identifiant_sport",
            foreignKey=@ForeignKey(name="FK_TICKETS_SPORTS"))
    //@Column(name="sport", table="TICKETS", nullable = false)
    private Sport sport;
    //[...]
    public TicketPKEmbedded getTicketPKEmbedded() { //<=
        return this.ticketPKEmbedded;
    }

    public Ticket() { //<=
        this.ticketPKEmbedded = new TicketPKEmbedded();
    }
```

```
@Embeddable //<=
public class TicketPKEmbedded implements Serializable {
    private int idClient;
    private int idSport;
    //getters, setters, default constructor, hashCode and equals override
```  
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Composite%20key%20with%20object%20and%20EmbededId%2C%20MapsId%2C%20Embeddable%20example%201">Example code here</a>  

**1:1 UNIDIRECTIONAL relationship**  
![image](https://user-images.githubusercontent.com/58827656/134878361-0dc29981-35e8-4d58-9b6e-b421e7f17799.png)  

*First solution*: Only one class, CLIENT with Address Attributes.  

What we need :  
-Both implement Serializable  and the setters/getters/default constructors …  
-Client is an @Entity and does have a PK (@Id)  
-Address have @Embeddable (intégrable) and not an @Entity  
-Client does have an @Embedded Address object (intégrer) to link to the Address  

As a result:  
```
create table clients (
       identifiant number(19,0) not null,
        codePostal number(10,0) not null,
        rue varchar2(255 char) not null,
        ville varchar2(255 char) not null,
        email varchar2(45 char) not null,
        name varchar2(45 char) not null,
        password varchar2(100 char) not null,
        surname varchar2(45 char) not null,
        primary key (identifiant)
    )
```
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1to1%20only%201%20class%20Embeddable%20Embedded">Code here</a>  

Sometime, a class doesn't need to be materialized into a database in contrary to what's in it.  
With the use of @Embeddable that replace @Entity and @Embedded, we can retrieve the proprieties of a class, group them into an other class and so having only one entity into the database that will contains both classes properties.  

The "compenent" (composante) class will have the annotation @Embeddable and the other will retrieve the other class information by an object of this class annoted by @Embedded.  
![image](https://user-images.githubusercontent.com/58827656/136916021-df7d13f9-8046-4db0-a94a-883f11ad9088.png)  
Internaute(email, nom, prenom, adresse, codePostal, ville);  
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/One%20class%20Embedded%20Embeddable%20exemple%202">Code example here</a>  

*Second solution*: Two classes with Client having an object Address under the @OneToOne annotation. Remember, it is unidirectional so only one class does have a FK at a time.   

What we need :  
-Address does have an *@Id* and is an *@Entity*  
-Client does need an Address object under the annotation *@OneToOne*  
*Note: The @JoinColumn + foreignKey is usefull to  put a name on the constraint but is not mandatory.*  

As a result: 
```
    create table addresses (
       identifiant number(10,0) not null,
        codePostal number(10,0) not null,
        rue varchar2(255 char) not null,
        ville varchar2(255 char) not null,
        primary key (identifiant)
    ) 
    create table clients (
       identifiant number(19,0) not null,
        email varchar2(45 char) not null,
        name varchar2(45 char) not null,
        password varchar2(100 char) not null,
        surname varchar2(45 char) not null,
        address number(10,0),
        primary key (identifiant)
    )
    alter table clients 
       add constraint fk_clients_addresses 
       foreign key (address) 
       references addresses
```
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1to1%20-%202%20classes%20%40OneToOne">Code here</a>  


**1:1 BIDIRECTIONAL relationship** 

Both classes will have an object from the relationship counter part.  
What we need :  
-@OneToOne relationship on both parties  
-@Entity, @Table, @Id, Serializable, constructor, getter/setter on both parties.  

Still there is a slave/master in this relationship to make it work like a "mirror".  
-Address (Slave) will have the parameter *@mappedBy(name="address")* (name of the variable in address) in the *@OneToOne* annotation.  

Address in Client will be a PK/FK in the database.

As a result : 
```
CREATE TABLE  "CLIENTS" 
   (	"IDENTIFIANT" NUMBER(19,0) NOT NULL ENABLE, 
	"EMAIL" VARCHAR2(45 CHAR) NOT NULL ENABLE, 
	"NAME" VARCHAR2(45 CHAR) NOT NULL ENABLE, 
	"PASSWORD" VARCHAR2(100 CHAR) NOT NULL ENABLE, 
	"SURNAME" VARCHAR2(45 CHAR) NOT NULL ENABLE, 
	"ADDRESS" NUMBER(10,0), 
	 PRIMARY KEY ("IDENTIFIANT") ENABLE, 
	 CONSTRAINT "FK_CLIENTS_ADDRESSES" FOREIGN KEY ("ADDRESS")
	  REFERENCES  "ADDRESSES" ("IDENTIFIANT") ENABLE
   )
CREATE TABLE  "ADDRESSES"
   (           "IDENTIFIANT" NUMBER(10,0) NOT NULL ENABLE,
               "CODEPOSTAL" NUMBER(10,0) NOT NULL ENABLE,
               "RUE" VARCHAR2(255 CHAR) NOT NULL ENABLE,
               "VILLE" VARCHAR2(255 CHAR) NOT NULL ENABLE,
                PRIMARY KEY ("IDENTIFIANT") ENABLE

   )
```
In JAVA, we have a bidirectionnal code but it doesn't translate in SQL.
```
public class Address implements Serializable {
    @Id
    private int identifiant;
    
    //name of the variable on the client.java side
    @OneToOne(mappedBy = "address")
    private Client client;
```
```
public class Client implements Serializable {
    @Column(name="identifiant", table="clients")
    @Id
    private Long identifiant;
    
    @OneToOne
    @JoinColumn(name="address",
    foreignKey = @ForeignKey(name="fk_clients_addresses"))
    private Address address;
```

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1to1%20-%202%20classes%202%20%40OneToOne%20mappedBy">Code example here</a>   

**1:N UNIDIRECTIONAL relationship**  
In this, we will be using either *@OneToMany* or *@ManyToOne*.  

How to read it ?  
-One *CLIENT* (master) to Many *MESSAGES* (slaves)  
-Many *MESSAGES* (slaves) to One *CLIENT* (master)  

@OneToMany example : 
The MASTER will have a **Collection** of *MESSAGES* objects.  
![image](https://user-images.githubusercontent.com/58827656/135043655-cff84481-7207-488c-a9bc-10d1c231ecf0.png)

Client code :  
```
@OneToMany
@JoinColumn(name="identifiant_client",
foreignKey = @ForeignKey(name="fk_messages_clients"))
private List<Message> messages;
```
"identifiant_client" will be a FK in the SQL code from Messages table.  

SQL code : 
```
    create table messages (
       identifiant number(19,0) not null,
        message varchar2(255 char) not null,
        message_date date not null,
        identifiant_client number(19,0),
        primary key (identifiant)
    )
    alter table messages 
       add constraint fk_messages_clients 
       foreign key (identifiant_client) 
       references clients
```
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1toN%20unidirectional%20%40OneToMany%20example%201">Code here</a>  
<a href="http://blog.paumard.org/cours/jpa/chap03-entite-relation.html">An other example here</a>  

@ManyToOne example :  
The SLAVE will have a MASTER object under the @ManyToOne annotation that will be a FK in the database.  
![image](https://user-images.githubusercontent.com/58827656/135049901-83b6a81e-1876-4f0d-8321-9fee7874d162.png)

Client code :  
```
@ManyToOne
@JoinColumn(name="identifiant_client",
    foreignKey = @ForeignKey(name="fk_messages_clients"))
private Client client;
```
SQL code is the same as in the @OneToMany example.  

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1toN%20unidirectional%20%40ManyToOne%20example%201">Code here</a>  
<a href="http://blog.paumard.org/cours/jpa/chap03-entite-relation.html">An other example here</a>  


**1:N BIDIRECTIONAL relationship**  
It look a lot like the unidirectional code. It is a conjonction of *@OneToMany* and *@ManyToMany* with the parameter *@mappedBy* on the *@OneToMany* annotation.  
When we have *bidirectional*, we use *@mappedBy* to translate it.  
![image](https://user-images.githubusercontent.com/58827656/135053581-9b694887-680e-45d7-bebd-3ef0f3e205e1.png)

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/1toN%20bidirectional%20%40ManyToOne%20%40OneToMany%20example%201">Code example here</a>  
<a href="http://blog.paumard.org/cours/jpa/chap03-entite-relation.html">An other example here</a>  

**N:M Relationship**  

In the relational model, the association between two or more entities is transformed in it own table, we call it *"insertion table"*.  
The goal of this table is to link multiple table between each other (ie: client / product / shopping list).  
![image](https://user-images.githubusercontent.com/58827656/135061129-7c19d9b2-a390-40cf-9fda-ba6056f2f94e.png)  

*SPORTS* can be played on compatible *FIELDS*  
*FIELDS* allows *SPORTS* to be played on them

**BIDIRECTIONAL n:m** :  

How do we manage it ?  
We have to identify the master from the slave in this relation. The sport can only play on a field if it is allowed, so sport is the slave of a field which is the master.  
Because it is a bidirectional context, the slave will have the *@mappedBy* parameter on the @ManyToMany annotation.  

Now, we have to create translate this insertion table : 
```
public class Field {
//...
@ManyToMany
@JoinTable(
name="fields_sports",
joinColumns=@JoinColumn(name="identifiant_field",
foreignKey=@ForeignKey(name="fk_fields"), nullable = false),
inverseJoinColumns=@JoinColumn(name="identifiant_sport",
foreignKey=@ForeignKey(name="fk_sports"), nullable = false))
private List<Sport> sportsAllowed;
```

```
    @ManyToMany(mappedBy = "sportsAllowed")
    private List<Field> compatibleFields;
```

*Join table* => create the insertion table then with "joinColumns (fk master) +inverseJoinColumns (fk slave)", we complet it.  
Join table represent the association table (fields_sports). JoinColumn the FK key for the class it is written in (identifiant_field and identifiant_sport). InverseJoinColumn represent the FK of the other entity.  

SQL code: 
```
    create table fields_sports (
       identifiant_field number(19,0) not null,
        identifiant_sport number(19,0) not null
    )
    
    create table sports (
       idendifiant number(19,0) not null,
        name varchar2(255 char),
        number_Players number(10,0) not null,
        primary key (idendifiant)
    )
    
    alter table fields_sports 
       add constraint fk_sports 
       foreign key (identifiant_sport) 
       references sports
       
    alter table fields_sports 
       add constraint fk_fields 
       foreign key (identifiant_field) 
       references fields
    }
```
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/MtoN%20bidirectional%20example%201">code here</a>  
<a href="http://blog.paumard.org/cours/jpa/chap03-entite-relation.html">an other example here</a>	

**1:n RELATIONSHIP MORE EXAMPLES**  

![OneToManyUniBe](https://user-images.githubusercontent.com/58827656/136944426-f8da535e-3412-4d4b-999d-28eabc0c7ff6.png)  

**READS OPTIMIZATION**  
When we "navigate" into the application, we generate SQL requests which will progressively formulate a data graph (level 2).  
To minimize the numbers of SQL req, while we already know in advance the data involved, we can anticipate and materialize the data graph with the right datas.  

This anticipation go through différents strategies : *lazy*, *eager* (see below). It works on 2 levels :  
-*mapping configuration via JPA annotation*  
-*while executing HQL request with the option fetch to materialize the data graph in advance*  

The first one generalize a strategy what-ever the ongoing situation wherease the second one "override" it to answere specific situations.  

By default, *@ManyToOne* go through the eager mod (glouton).  
It means that if a class own one or multiple times this annotation then it will load by default the différents objects linked to it (main entity / secondary entity).  

For exemple, if we want to find a specific movie but we use only one parameter from it in a view later on ( *${film.titre}* ), will will have useless requests.  

```
@ManyToOne
@JoinColumn(name="id_realisateur")
private Artiste realisateur;

@ManyToOne
@JoinColumn(name="code_pays")
private Pays pays;

@Embedded
private Genre genre;
```
![image](https://user-images.githubusercontent.com/58827656/137868298-d10a2901-9150-462e-acdc-ec20c484c11c.png)  
We do have 3 useless join.  

If we use HQL instead to draw the 2 next attributes : film.titre and film.realisateur.nom :  
![image](https://user-images.githubusercontent.com/58827656/137868332-9497e637-c275-47d1-8663-dadf36083d51.png)  

Only the third is usefull (1/3) to find the film maker name.  

If we add the annotation *(fetch=FetchType.LAZY)* to the *@ManyToOne*, we will load only the strict minimum.  
In the first case :  
![image](https://user-images.githubusercontent.com/58827656/137868361-83057646-d298-4915-b14e-0d613c7df735.png)  
Problem, in the second case, we have 2 requests instead of 1 :  
![image](https://user-images.githubusercontent.com/58827656/137868378-5be77f4d-ffa0-4c4d-99a8-80ef21e6ae1e.png)  
```
SELECT f
FROM Film f, Artiste a
WHERE f.realisateur = a
AND f.titre = 'Vertigo'

select f 
from Film f 
join f.realisateur
where f.titre='Vertigo'
```  
We have to use *"fetch"* (aller chercher) :  
```
select f 
from Film f 
join fetch f.realisateur
where f.titre='Vertigo'

Hibernate: select […] from Film film0_ inner join Artiste artiste1_ on film0_.id_realisateur=artiste1_.id where film0_.titre=?
```  
On the opposite to the request :  
![image](https://user-images.githubusercontent.com/58827656/137868438-09d271a7-94f7-4278-97f3-81c812e397cb.png)  

We don't signal that we want to load the data graph (cache) of the Artiste realisateur from Film.  
**JPA/HIBERNATE STRATEGIES**  

Vocabularies:  
**Film** is a *main entity* to which is associate *secondaries entity* (*@xToOne*) like *pays* and *realisateur*.  
The annotation *@xToMany* designates a *collection*, here *roles*.  
![image](https://user-images.githubusercontent.com/58827656/137868485-e74cf620-c47b-4900-be0f-ac9a0e6cfb2b.png)  

Our 2 mains strategies to load to the data graph :  
-**Eager** : load entity and collection the soonest into the data graph.  
We associates the sonnest possible the secondaries entities to the mains ones.  
It goes through outer joins and some simple requests if it is not enough.  
-**Lazy** : load entity and collection as late as possible into the data graph.  
By default, JPA load **entities** with **eager** and the **collections**  with **Lazy**.  

In our example, realisateur, pays and genre are EAGER while roles is LAZY.  
![image](https://user-images.githubusercontent.com/58827656/137868534-fc902486-2b2c-4bef-896d-8b99cdc1d0c8.png)

**/!\ use lazy on the mapping level and fetch (eager) on a HQL level /!\**  

![image](https://user-images.githubusercontent.com/58827656/137868557-ffd36d24-8623-4830-8e75-ab1f861389d1.png)  

There is an issue name "1+n requests" with collection. Because they use the LAZY mod by default, it implies massive queries if we work on collection.  
If we want by example, see the list of films and their notations, we need first to select all the movies in SQL then do one request at a time for every movies…
One time :  
![image](https://user-images.githubusercontent.com/58827656/137868582-70649fa9-381f-4be5-93d6-616fe54aa476.png)  
As many times as there are movies :  
![image](https://user-images.githubusercontent.com/58827656/137868614-e1ad6134-9378-4722-80e3-07f6080291f0.png)  
To mitigate it, we gonna use one HQL request to do the same as the SQL one and add *fetch* to load up the movies and their annotations into the cache.  
SQL :  
![image](https://user-images.githubusercontent.com/58827656/137868636-109219c3-eb6e-4c2d-87ca-fb74a03fd33b.png)  
Will become with HQL:  
![image](https://user-images.githubusercontent.com/58827656/137868670-71c52438-d02b-4e1d-bc05-0b81b7532a50.png)  

<a href="http://orm.bdpedia.fr/optimisation.html">Credit</a>  



**SIMPLE TRANSACTION**  
In this simple example, we will see : register, modify and delete information.  
We start by creating a persistence context:  
```
//return ApplicationListener.getEmf().createEntityManager();
//public static EntityManagerFactory getEmf()  {return ApplicationListener.emf;}
EntityManager em = JPAUtil.getEntityManager();
```  
If the connection is open we continue:  
```
if(em.isOpen()) {...
```  
We start a transaction by using :  
```
em.getTransaction().begin();
```  
And finish it with a commit() :  
```
em.getTransaction().commit();
```  

If we want to add the object to the database :  
```
em.persist(object);
```  
If we want to update an entity in the database :  
```  
client = em.find(Client.class, (long) 2);
if(client != null) {
	out.println("Client found");
	//start
	em.getTransaction().begin();
	//modify object
	client.setPassword("no");
	//commit the modification to the database
	em.getTransaction().commit();
	out.println("Client modified");
}
```  
If we want to remove an entity in the database :  
```
//start
em.getTransaction().begin();
//modify object
em.remove(client);
//commit the modification to the database
em.getTransaction().commit();
```  
If we want to avort a commit() that turned bad : 
```
if(em.getTransaction().isActive()) {
em.getTransaction().rollback();
}
```  
<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Transaction%20simple%20example%201">code here</a>

**Cascade Behavior**

An EntityManager object have différents methods like **PERSIST, MERGE, REMOVE** …  
To persist data into the database, every actors of this relation have to be persisted.  

Example: 
```
Mayor m = new Mayor();  
Town t = new Town();
EntityManager em = JPAUtil.getEntityManager();

em.getTransaction().begin();
t.setMayor(m);
em.persist(t);
em.getTransaction().commit(); 
```

It will cause an error because while 't' persist in the persistence context, 'm' from Mayor is not.  
While it is possible to manually make persistent every actors of this relationship : 
```
//[…]
em.getTransaction().begin();
t.setMayor(m);
em.persist(t);
em.persist(m);
em.getTransaction().commit(); 
```
It would be better to have a "cascade effect", meaning that one action will have a snowball effect on the others with what it has a relation to.  

It is a parameter on @OneToOne, @OneToMany and @ManyToMany annotations.  

Example:  
```
@Entity
 public  class Town  implements Serializable {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;
     @OneToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
     private Mayor mayor ;
}
```
If a Town is persisted or removed, then mayor will automatically be too.  

<a href="http://blog.paumard.org/cours/jpa/chap03-entite-relation.html">Example here 4.7</a>  

**Detached entity**  

Being de detached entity mean to *not being attached to a persistant context*, so without any link with a database.  
This context is materialize by an EntityManager object and for the sake of performance, that must close itself.  
For example, a JPS page work mostly with detached entities. Or between two http request concerning one user.  
In these two examples, the entities can be maintened to a SESSION, meaning on the server side.  

In the next example, first we simulate a first persistent context within we attached for a moment a Client object before detaching from it and saving it to a session.  
Next, we recover it from the session into a new persistent context, modify it and use a persist() method on it.  But in order to do this, we use a copy of the original object persistent context from the merge method.  

<a href="https://github.com/codeFliers/JAVA-EE/tree/main/Detached%20entity%20example%201">Example code here</a>  

An ORM application dispose of 3 principal layers :  
![image](https://user-images.githubusercontent.com/58827656/137515916-ea46c78b-ed9c-470f-b3b0-6f7a996efe3a.png)  

The **applicative layer** and it's **transient objects**.  It does mean any objects without persistence.  
The **ORM layer**  which include different elements:  
-an objet **session** which establish a link with the **first level cache** (FLC) and the **relational database** of the next layer.  
This *FLC* include persistent objects that form a *persistent graph of objects* linked to a particular session.  
This session instantiate these different objects in concordence with the relational database following the queries.  
-The **persistent layer** is made of persistent data but on the relational database level.  

When we reach the database to get datas which will be converted as a transient object, the session watch out in the first place if this object corresponding to it isn't already into the *FLC*.  
If it is the case, we retrieve the existing persistent object. Otherwise, the session query on the database and make a new object persistent.  

A session is linked to his down FLC. As well as in case of a closing session and the creation of a new one, the references from the first (s1) will not be in the second (s2).  

To differenciate an object to an other, we use the hash of the primary key and the table name.  

An example:  
```
@Entity
public class User implements Serializable {
    private String nom;
    private String prenom;

    @Id
    private Integer id;
    public User() {

    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getId() {
        return id;
    }
    private void setId(Integer id) {
        this.id = id;
    }
}
```
At this moment, because there isn't any mean to differenciate one to an other, when i create 2 identical User objects, they will still be concidered different.  

```
public void test() {
    Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
    configuration.addAnnotatedClass(User.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    // Une iste des utilisateurs (Set => sans doublon)
    Set<User> utilisateurs = new HashSet<>();

    // Ouverture d'une première session
    Session s1 = sessionFactory.openSession();

    // On ajoute l'utilisateur 1 à la liste
    User u1 = s1.load (User.class, 1);
    utilisateurs.add(u1);

    // Fermeture de s1
    s1.close();

    // Idem, avec une session 2
    Session s2 =  sessionFactory.openSession();
    User u2 = s2.load (User.class, 1);
    utilisateurs.add(u2);

    System.out.println("hashcode u1 "+u1.hashCode()); //1124360095
    System.out.println("hashcode u2 "+u2.hashCode()); //675889995
    System.out.println(u1.equals(u2) ? "oui u1=u2":"non u1!=u2"); // non //...
}
```

We have an array which should as we want it to be, the size of 1 instead of 2. Our hashcode and equals are not right while we have the same datas.  

We have to rewrite our 2 functions *hashcode* and *equals()* to take into concideration the right datas to compare :  

```
@Override
public boolean equals(Object o) {
    Boolean bool = false;
    if(o != null && o instanceof User) {
        User userParam = (User) o;

        if(userParam.getId().equals(this.getId()) &&
                userParam.getNom().equals(this.getNom()) &&
                userParam.getPrenom().equals(this.getPrenom()))  {
            bool = true;
        }
    }
    return bool;
}

@Override
public int hashCode() {
    return Objects.hash(getNom(), getPrenom(), getId());
}
```

Now, we have :  
```
System.out.println("Taille tableau "+utilisateurs.size()); // 1
System.out.println("hashcode u1 "+u1.hashCode()); //1974052605
System.out.println("hashcode u2 "+u2.hashCode()); //1974052605
System.out.println(u1.equals(u2) ? "oui u1=u2":"non u1!=u2"); // oui ...
```
Another thing to mention, when we instantiate an object which we want persistent :  
``` User u1 = s1.load (User.class, 1); ```
Either it already exist in the FCL and our session will retrieve it direction from it.  
Or, like here, the session will have to query to the database before persisting the returned data into an object in the cache.  
Console result:  
```
Hibernate: select user0_.id as id1_0_0_, user0_.nom as nom2_0_0_, user0_.prenom as prenom3_0_0_ from User user0_ where user0_.id=?
```
Here, because we have 2 different sessions, the caches being not the same, while identical, we have 2 requests:  
```
Hibernate: select user0_.id as id1_0_0_, user0_.nom as nom2_0_0_, user0_.prenom as prenom3_0_0_ from User user0_ where user0_.id=?  
Hibernate: select user0_.id as id1_0_0_, user0_.nom as nom2_0_0_, user0_.prenom as prenom3_0_0_ from User user0_ where user0_.id=?  
```
However, if we call for the same session an new object but which call the same primary key as the previous registered object into the cache, then there isn't any query :  
```
// On ajoute l'utilisateur 1 à la liste (inconnu donc requêtage)
User u1 = s1.load (User.class, 1);
utilisateurs.add(u1);
//pas de requêtage (on connait déjà la clé primaire 1 d'u1 donc pas de requêtage)
User u1Copy = s1.load (User.class, 2);
utilisateurs.add(u1Copy);
```
Something to notice, with the overriding of hashcode and equals, if our two objects u1 and u2 were from the same session, then there would be no need to override them.  
```
// On ajoute l'utilisateur 1 à la liste (inconnu donc requêtage)
User u1 = s1.load (User.class, 1);
utilisateurs.add(u1);

User u2 = s1.load (User.class, 1);
utilisateurs.add(u2);
```  
```
Taille tableau 1
hashcode u1 1124360095
hashcode u2 1124360095
oui u1=u2
```  
To summary, for every access to a database row, it's already the same object that is returned to the app and in what context.  

<a href="http://orm.bdpedia.fr/lectures.html">resources here</a>  
