package hex.dev;

import hex.action.Application;
import hex.action.initialization.InitializationException;
import hex.action.initialization.InitializerRunner;
import hex.routing.*;

import javax.servlet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * {@link javax.servlet.Filter} implementation for loading a hex application in "development mode." If developing hex
 * outside of the provided embedded {@link hex.dev.HexServer} then use this filter (and only this filter) in
 * your {@code web.xml}. This filter wraps all other initialization classes required for production deployment of a hex
 * application inside of a Servlet Container.
 *
 * <p>
 *     {@code init-param}: {@code hex.action.Application.ROOT} ({@link hex.action.Application#ROOT}) <br/>
 *     {@code description}: Fully qualified path to the hex application directory. hint: The directory that has the
 *     {@code hex.properties} file in it.
 * </p>
 */
public class DevRoutingFilter implements Filter, RoutingConfig {
    private static final String OUT_DIR_PROPERTY = "out";

    private RoutingConfig config;

    private RoutingFilter filter = new RoutingFilter();

    private Supplier<Stream<String>> sourcePaths;

    private File outPath;

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
        initPaths();
        applicationCompiler = new Compiler(applicationRootPath);
        applicationCompiler.setSourcePaths(sourcePaths);
        applicationCompiler.setDestDir(outPath);
        if(applicationProperties.containsKey("build.compiler"))
            applicationCompiler.setCompiler(applicationProperties.getProperty("build.compiler"));
        applicationCompiler.copyResources();
    }

    private void runApplicationInitializers(FilterConfig filterConfig) throws IOException {
        try(URLClassLoader classLoader = new HexClassLoader(applicationCompiler, this.getClass().getClassLoader())) {
            InitializerRunner runner = new InitializerRunner(classLoader);
            runner.run();
        } catch (InitializationException e) {
            filterConfig.getServletContext().setAttribute(InitializationException.class.getName(), e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try (URLClassLoader requestClassLoader = new HexClassLoader(applicationCompiler, this.getClass().getClassLoader())) {
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
