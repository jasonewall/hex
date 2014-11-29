package hex.action.initialization;

import hex.action.Application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jason on 14-11-28.
 */
public class InitializerRunner implements ServletContextListener {
    private ClassLoader classLoader;

    public InitializerRunner() {
        classLoader = getClass().getClassLoader();
    }

    public InitializerRunner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void run() throws InitializationException{
        Properties actionProperties = new Properties();
        try {
            actionProperties.load(classLoader.getResourceAsStream(Application.ACTION_CONFIG));
            if (!actionProperties.containsKey(Initializer.PACKAGE) || !actionProperties.containsKey(Initializer.LIST))
                return;

            String packageName = actionProperties.getProperty(Initializer.PACKAGE);
            String[] initializers = actionProperties.getProperty(Initializer.LIST).split(",");

            for (String initializerName : initializers) {
                Initializer initializer = (Initializer)
                        classLoader.loadClass(getClassName(packageName, initializerName)).newInstance();
                initializer.start();
            }

        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | RuntimeException e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            run();
        } catch (InitializationException e) {
            servletContextEvent.getServletContext().setAttribute(InitializationException.class.getName(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private static String getClassName(String packageName, String initializer) {
        return String.format("%s.%s", packageName, initializer);
    }
}
