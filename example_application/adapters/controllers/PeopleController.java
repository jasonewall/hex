package controllers;

import hex.action.Controller;
import hex.action.annotations.RouteParam;

/**
 * Created by jason on 14-11-15.
 */
public class PeopleController extends Controller {
    public void index() {
        view.put("message", "This is a list of people.");
        renderPage("/layout.jsp");
    }

    public void show(@RouteParam("id") int id) {
        view.put("message", String.format("This is where I would show the person with the id of %d", id));
        renderPage("/layout.jsp");
    }

    public void home() {
        view.put("message", "Hello World!");
        renderPage("/layout.jsp");
    }
}
