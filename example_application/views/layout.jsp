<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    for(java.util.Map.Entry<String,Object> e : ((java.util.Map<String,Object>)request.getAttribute("hex.action.ControllerAction.VIEW_CONTEXT")).entrySet()) {
        pageContext.setAttribute(e.getKey(), e.getValue());
    }
%>
<html>
    <head>
        <title>People App</title>
    </head>
    <body>
        <h1>People App</h1>
        <p>${message}</p>
        <p>${java_home}</p>
        <p>
            <%= application.getAttribute("javax.servlet.context.tempdir") %>
        </p>
        <ul>
            <c:forEach items="${wat}" var="i">
                <li>${i}</li>
            </c:forEach>
        </ul>
        <form action="${pageContext.request.contextPath}" method="POST">
            <input type="text" name="message" value="${message}"/>
            <button>Say Hello</button>
        </form>
    </body>
</html>
