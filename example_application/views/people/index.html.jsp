<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    for(java.util.Map.Entry<String,Object> e : ((java.util.Map<String,Object>)request.getAttribute("hex.action.ControllerAction.VIEW_CONTEXT")).entrySet()) {
        pageContext.setAttribute(e.getKey(), e.getValue());
    }
%>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${people}" var="p">
            <tr>
                <td><a href="${request.contextPath}/people/${p.id}">${p.id}</a></td>
                <td>${p.firstName}</td>
                <td>${p.lastName}</td>
            </tr>
        </c:forEach>
    </tbody
</table>

<a href="/people/new">Create New Person</a>
