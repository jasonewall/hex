<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hex" uri="http://hex.org/tags" %>
<%
    for(java.util.Map.Entry<String,Object> e : ((java.util.Map<String,Object>)request.getAttribute("hex.action.ControllerAction.VIEW_CONTEXT")).entrySet()) {
        pageContext.setAttribute(e.getKey(), e.getValue());
    }
%>

<hex:content-for name="styles">
    <style type="text/css">
        table.people {
            margin: 0 auto;
            width: 600px;
        }
    </style>
</hex:content-for>

<table class="people">
    <thead>
        <tr>
            <th>First Name</th>
            <th>Last Name</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${people}" var="p">
            <tr>
                <td>${p.firstName}</td>
                <td>${p.lastName}</td>
                <td><a href="${pageContext.request.contextPath}/people/${p.id}">View</a></td>
            </tr>
        </c:forEach>
    </tbody
</table>

<a href="${pageContext.request.contextPath}/people/new">Create New Person</a>
