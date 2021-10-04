<%@ page import="java.util.ArrayList" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>JSPPARAM</title>
</head>
<body>
        <!-- include html fragment -->
        <%@ include file="header.jsp" %>

        <!-- affichage non sécurisé -->
        <p>${ param.p1}</p>
        <p>${ param.p2}</p>
        <p>${ param.p3}</p>
        <p>${ requestScope.test}</p>

        <!-- affichage sécurisé -->
        <p><c:out value="${ param.p1}"/></p>
        <p><c:out value="${ param.p2}"/></p>
        <p><c:out value="${ param.p3}"/></p>
        <p><c:out value="${ requestScope.test}"/></p>

        <!-- affectation valeur à une variable + affichage -->
        <c:set var="uneVariable" value="${5 * 5}" />
        <p><c:out value="${ uneVariable }"/></p>

        <!-- condition -->
        <c:if test="${uneVariable > 20}">
                <p>variable supérieur à 20</p>
        </c:if>

        <!-- switch/case -->
        <c:choose>
                <c:when test="${uneVariable == null}">Variable null</c:when>
                <c:when test="${uneVariable > 0}">Variable > 0</c:when>
                <c:otherwise>Variable < 0</c:otherwise>
        </c:choose>


        <!-- foreach -->
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

        <!-- foreach map -->
        <%
          ArrayList list = new ArrayList<>();
          list.add("1");list.add("2");list.add("3");
          pageContext.setAttribute("list", list);
        %>
        <c:forEach items="${list}" var="value">
                <p><c:out value="${value}"/></p>
        </c:forEach>

        <!--
        <ul>
            <li><a href="<%--${pageContext.request.contextPath}--%>/JspparamServlet?p1=A&p2=B&p3=C">click me</a></li>
            <li>
                <a href="
                <%--<c:url value="/JspparamServlet">
                    <c:param name="p1" value="A"/>
                    <c:param name="p2" value="B"/>
                    <c:param name="p3" value="C"/>
                </c:url>"--%>
                >clickmeJSTL</a>
            </li>
        </ul>
        -->

        <!-- hashmap -->
        <table>
                <c:forEach var="entry" items="${headerValues}">
                        <tr>
                                <td><c:out value="${entry.key}"/></td>
                                <td><c:out value="${entry.value}"/> </td>
                        </tr>
                </c:forEach>
        </table>

</body>
</html>
