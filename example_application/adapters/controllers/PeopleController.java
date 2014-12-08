package controllers;

import hex.action.Controller;
import hex.action.annotations.RouteParam;
import hex.ql.Query;
import hex.repo.Repository;
import hex.repo.RepositoryBase;
import people.Person;

/**
 * Created by jason on 14-11-15.
 */
public class PeopleController extends Controller {
    public void index() {
        Repository<Person> repo = new RepositoryBase<>(Person.class);
        Query<Person> people = repo.stream();
        view.put("people", people.iterator());
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
