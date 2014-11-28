package hex.action;

/**
 * [viewsRoot]/[controllerTemplateDirectory]/[format]/[action].[engine]
 */
public class ViewPath {
    private static final String PS = "/";

    private StringBuilder viewPath;

    public ViewPath() {
        viewPath = new StringBuilder();
    }

    public ViewPath(String viewsRoot) {
        viewPath = new StringBuilder(viewsRoot);
    }

    public Format set(String templateDirectory) {
        viewPath.append(PS).append(templateDirectory);
        return new Format();
    }

    public class Format {
        public Action set(String format) {
            viewPath.append(PS).append(format);
            return new Action();
        }
    }

    public class Action {
        public Engine set(String action) {
            viewPath.append(PS).append(action);
            return new Engine();
        }
    }

    public class Engine {
        public String set(String engine) {
            viewPath.append(".").append(engine);
            return viewPath.toString();
        }
    }
}
