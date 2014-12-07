package hex.dev;

import hex.action.Application;
import hex.action.initialization.InitializationException;
import hex.action.initialization.InitializerRunner;
import hex.routing.*;

import javax.servlet.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-21.
 */
public class DevRoutingFilter implements Filter, RoutingConfig {
    private static final String OUT_DIR_PROPERTY = "out";

    private RoutingConfig config;

    private RoutingFilter filter = new RoutingFilter();

    private Supplier<Stream<String>> sourcePaths;

    private File outPath;

    private URL[] getClassPathURLs() throws MalformedURLException {
        return new URL[] {
                outPath.toURI().toURL()
        };
    }

    private String applicationRootPath;

    private Properties applicationProperties;

    private Compiler applicationCompiler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        applicationRootPath = filterConfig.getInitParameter(Application.ROOT);
        applicationProperties = new Properties();
        File applicationConfigFile = new File(applicationRootPath, Application.CONFIG);
        try(InputStream propStream = new FileInputStream(applicationConfigFile)) {
            applicationProperties.load(propStream);
            initCompiler(applicationRootPath);
            initPaths();
            applicationCompiler.compile(sourcePaths, outPath); // do this for initializers at first
            runApplicationInitializers(filterConfig);
            filterConfig.getServletContext().setAttribute(Routing.CONFIG, this);
            filter.init(filterConfig);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private void initPaths() {
        sourcePaths = () -> Stream.of(applicationProperties.getProperty("src").split(",")).map(String::trim);
        if (applicationProperties.containsKey(OUT_DIR_PROPERTY)) {
            outPath = new File(applicationRootPath, applicationProperties.getProperty(OUT_DIR_PROPERTY));
        } else {
            outPath = new File(System.getProperty("java.io.tmpdir"), "hex" + applicationRootPath.replaceAll("/", "_"));
        }
    }

    private void initCompiler(String applicationRootPath) {
        applicationCompiler = new Compiler(applicationRootPath);
        if(applicationProperties.containsKey("build.compiler"))
            applicationCompiler.setCompiler(applicationProperties.getProperty("build.compiler"));
    }

    private void runApplicationInitializers(FilterConfig filterConfig) throws IOException {
        URLClassLoader classLoader = new URLClassLoader(getClassPathURLs(), this.getClass().getClassLoader());
        InitializerRunner runner = new InitializerRunner(classLoader);
        try {
            runner.run();
        } catch (InitializationException e) {
            filterConfig.getServletContext().setAttribute(InitializationException.class.getName(), e);
        } finally {
            classLoader.close();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        applicationCompiler.compile(sourcePaths, outPath);
        try (URLClassLoader requestClassLoader = new URLClassLoader(getClassPathURLs(), this.getClass().getClassLoader())) {
            config = Application.initializeRoutes(requestClassLoader);
            filter.doFilter(servletRequest, servletResponse, filterChain);
        }  finally {
            config = null;
        }
    }

    @Override
    public void destroy() {
        filter.destroy();
        try {
            Files.walkFileTree(outPath.toPath(), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace(); // nothing else we can do at this point I guess
        }
    }

    @Override
    public boolean hasRoute(String path) {
        return config.hasRoute(path);
    }

    @Override
    public boolean hasRoute(String method, String path) {
        return config.hasRoute(method, path);
    }

    @Override
    public boolean hasRoute(HttpMethod method, String path) {
        return config.hasRoute(method, path);
    }

    @Override
    public RouteHandler getRouteHandler(String path) {
        return config.getRouteHandler(path);
    }

    @Override
    public RouteHandler getRouteHandler(String method, String path) {
        return config.getRouteHandler(method, path);
    }

    @Override
    public RouteHandler getRouteHandler(HttpMethod method, String path) {
        return config.getRouteHandler(method, path);
    }
}
