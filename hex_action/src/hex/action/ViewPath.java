package hex.action;

/**
 * [viewsRoot]/[controllerTemplateDirectory]/[action].[format].[engine]
 */
public class ViewPath implements PathFragment {
    private static final String PS = "/";

    private StringBuilder viewPath;

    public ViewPath() {
        viewPath = new StringBuilder();
    }

    public ViewPath(String viewsRoot) {
        viewPath = new StringBuilder(viewsRoot);
    }

    public String toString() {
        return viewPath.toString();
    }

    public PathFragment set(String templateDirectory) {
        viewPath.append(PS).append(templateDirectory);
        return new Action();
    }

    private class Format implements PathFragment {
        public PathFragment set(String format) {
            viewPath.append(".").append(format);
            return new Engine();
        }
    }

    private class Action implements PathFragment {
        public PathFragment set(String action) {
            viewPath.append(PS).append(action);
            return new Format();
        }
    }

    private class Engine implements PathFragment {
        public PathFragment set(String engine) {
            viewPath.append(".").append(engine);
            return ViewPath.this;
        }
    }
}

interface PathFragment {
    PathFragment set(String fragment);
}
