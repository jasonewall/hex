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
            <li><hex:link action="home_path">Home</hex:link></li>
            <li>
                <hex:link action="index_people_path">
                    List of People
                </hex:link>
            </li>
        </ul>
        <hex:view-content/>
        <hex:view-content name="postBody"/>
    </body>
</html>
