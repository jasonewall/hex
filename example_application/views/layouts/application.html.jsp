<%@ taglib uri="http://hex.org/tags" prefix="hex" %>
<html>
    <head>
        <title>Hex Example Application</title>
        <hex:view-content name="styles"/>
    </head>
    <body>
        <h1>Hex Example Application</h1>
        <h2>Menu</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/">Home</a>
            <li><a href="${pageContext.request.contextPath}/people">List of People</a></li>
        </ul>
        <hex:view-content/>
        <hex:view-content name="postBody"/>
    </body>
</html>
