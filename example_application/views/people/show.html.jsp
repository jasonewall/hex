<%
    for(java.util.Map.Entry<String,Object> e : ((java.util.Map<String,Object>)request.getAttribute("hex.action.ControllerAction.VIEW_CONTEXT")).entrySet()) {
        pageContext.setAttribute(e.getKey(), e.getValue());
    }
%>

<h1>${person.fullName}</h1>
