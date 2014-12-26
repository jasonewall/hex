<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hex" uri="http://hex.org/tags" %>

<hex:view-init/>

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
<form action="${pageContext.request.contextPath}/" method="POST">
    <input type="text" name="message" value="${message}"/>
    <button>Say Hello</button>
</form>
