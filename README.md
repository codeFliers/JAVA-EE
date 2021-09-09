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

A filter catch up the HTTP Requests which it is interest in to do a "filter" action, pre-treatment before the resource access or after the servlet treatment.  
It is possible to use one or more filter and associate them to certain servlets.  
First, the filter is initiate (Filter.init) and eventually destroy (Filter.destroy).  
The treatment of the filter itself go by the function "doFilter()". This, once treated will start an other "doFilder()" on the previous method param "FilterChain chain" , so it can call the next filter or the direct access to the resource if there is no more.  

A filter can be declared by a descriptor or an annotation. For this last one, there is no garantee of the treatment order.



