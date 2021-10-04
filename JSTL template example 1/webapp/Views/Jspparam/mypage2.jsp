
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:test title="My page">
    <jsp:attribute name="head_area">
            <header class="w3-container w3-center w3-padding-32">
                <h1><b>MY BLOG</b></h1>
                <p>Welcome to the blog of <span class="w3-tag">unknown</span></p>
            </header>
    </jsp:attribute>
    <jsp:attribute name="blog_area">
<p>..................SUPER COOL MESSAGES..................</p>
    </jsp:attribute>

</t:test>