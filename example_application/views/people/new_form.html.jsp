<form action="${pageContext.request.contextPath}/people" method="POST">
    <p>
        <label for="person_firstName">First Name:</label>
        <input id="person_firstName" type="text" name="person[firstName]"/>
    </p>
    <p>
        <label for="person_lastName">Last Name:</label>
        <input id="person_lastName" type="text" name="person[lastName]"/>
    </p>
    <button>Save</button>
</form>
