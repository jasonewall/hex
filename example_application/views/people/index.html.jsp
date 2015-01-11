<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hex" uri="http://hex.org/tags" %>

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
                <c:url value="/people/${p.id}" var="showPersonPath"/>
                <td><a href="${showPersonPath}">View</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<hex:link action="newForm_people_new_path">Create New Person</hex:link>
