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
        <p>from Controller: ${from_hex_class_loader}</p>
    </body>
</html>
