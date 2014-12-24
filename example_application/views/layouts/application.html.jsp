<html>
    <head>
        <title>Hex Example Application</title>
    </head>
    <body>
        <h1>Hex Example Application</h1>
        <h2>Menu</h2>
        <ul>
            <li><a href="${pageContext.request.contextPath}/">Home</a>
            <li><a href="${pageContext.request.contextPath}/people">List of People</a></li>
        </ul>
        <%= ((hex.action.ViewContext)request.getAttribute("hex.action.ControllerAction.VIEW_CONTEXT")).getContent() %>
    </body>
</html>
