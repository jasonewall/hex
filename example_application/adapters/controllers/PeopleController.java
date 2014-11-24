package controllers;

import hex.action.Controller;
import hex.action.annotations.RouteParam;
import people.Person;

/**
 * Created by jason on 14-11-15.
 */
public class PeopleController extends Controller {
    public void index() {
        view.put("message", "This is a list of people.");
        // TODO: fix itttttttttt. Make it so we don't need the people prefix.
        renderPage("people/index.html.jsp");
    }

    public void show(@RouteParam("id") int id) {
        Person person = new Person();
        person.setId(id);
        view.put("person", person);
        renderPage("show.html.jsp");
    }

    public void home() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        view.put("message", "Hello World!");
        renderPage("layout.jsp");
    }
}
