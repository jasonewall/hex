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

    @Override
    public InputStream getResourceAsStream(String name) {
        return super.getResourceAsStream(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // java.lang.String -> java/lang/String
        compiler.compile(String.format("%s.java", name.replace('.', '/')));
        return super.loadClass(name);
    }
}
