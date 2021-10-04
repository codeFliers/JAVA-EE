<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<br/>
<ul>
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

    <li><a href="${pageContext.request.contextPath}/Views/Jspparam/mypage.jsp">invoke template</a></li>
    <li><a href="${pageContext.request.contextPath}/Views/Jspparam/mypage2.jsp">invoke template2</a></li>
</ul>

</body>
</html>