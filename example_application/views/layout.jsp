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
        ${message}
    </body>
</html>
