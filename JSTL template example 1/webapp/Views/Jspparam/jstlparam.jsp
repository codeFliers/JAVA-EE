
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>jstlparam</title>
</head>
<body>
    <p>JSTLPARAM</p>
    <table>
        <!-- Iterating hashmap<String, ArrayList> -->
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
</body>
</html>
