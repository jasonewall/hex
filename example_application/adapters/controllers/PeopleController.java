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
        // or so that it doesn't change depending if we routed from /people or /people/
        // well fuck, this won't matter because I'm just going to do /[controller]/[view].[format].[engine] like the rails anyways
        // as in.. force to go from root
        renderPage("index.html.jsp");
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
