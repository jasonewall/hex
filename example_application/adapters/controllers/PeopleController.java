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
    }

    public void show(@RouteParam("id") int id) {
        Person person = new Person();
        person.setId(id);
        view.put("person", person);
    }

    public void readme() {
        renderText("Hex README");
        renderText("Hi. Welcome to Hex.");
    }

    public void home() {
        view.put("message", "Hello World!");
        renderPage("layout.jsp");
    }
}
