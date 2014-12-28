package hex.dev;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by jason on 14-12-28.
 */
public class HexClassLoader extends URLClassLoader {
    private final Compiler compiler;

    public HexClassLoader(Compiler compiler, ClassLoader parent) throws MalformedURLException {
        super(new URL[]{ compiler.getDestDir().toURI().toURL() }, parent);
        this.compiler = compiler;
    }

    /**
     * Returns an input stream for reading the specified resource.
     * If this loader is closed, then any resources opened by this method
     * will be closed.
     * <p>
     * <p> The search order is described in the documentation for {@link
     * #getResource(String)}.  </p>
     *
     * @param name The resource name
     * @return An input stream for reading the resource, or {@code null}
     * if the resource could not be found
     * @since 1.7
     */
    @Override
    public InputStream getResourceAsStream(String name) {
        return super.getResourceAsStream(name);
    }

    /**
     * Loads the class with the specified <a href="#name">binary name</a>.
     * This method searches for classes in the same manner as the {@link
     * #loadClass(String, boolean)} method.  It is invoked by the Java virtual
     * machine to resolve class references.  Invoking this method is equivalent
     * to invoking {@link #loadClass(String, boolean) <tt>loadClass(name,
     * false)</tt>}.
     *
     * @param name The <a href="#name">binary name</a> of the class
     * @return The resulting <tt>Class</tt> object
     * @throws ClassNotFoundException If the class was not found
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // java.lang.String -> java/lang/String
        compiler.compile(String.format("%s.java", name.replace('.', '/')));
        return super.loadClass(name);
    }
}
