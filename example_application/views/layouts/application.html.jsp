<%@ taglib uri="http://hex.org/tags" prefix="hex" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Hex Example Application</title>
        <hex:view-content name="styles"/>
    </head>
    <body>
        <h1>Hex Example Application</h1>
        <h2>Menu</h2>
        <ul>
            <c:url var="rootPath" value="/"/>
            <li><a href="${rootPath}">Home</a></li>
            <c:url var="peoplePath" value="/people"/>
            <li><a href="${peoplePath}">List of People</a></li>
        </ul>
        <hex:view-content/>
        <hex:view-content name="postBody"/>
    </body>
</html>
