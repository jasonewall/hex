package config;

import controllers.PeopleController;
import hex.action.RouteManager;

/**
 * Created by jason on 14-11-16.
 */
public class ApplicationRoutes extends RouteManager {
    @Override
    public void defineRoutes() {
        matches("/", PeopleController::new, "home");
        get("/readme", PeopleController::new, "readme");
        get("/people", PeopleController::new, "index");
        post("/people", PeopleController::new, "create");
        get("/people/new", PeopleController::new, "newForm");
        get("/people/:id", PeopleController::new, "show");
    }
}
