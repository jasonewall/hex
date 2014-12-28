package hex.dev;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.net.URLClassLoader;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-21.
 */
public class Compiler {
    private final Project project;

    private String compiler;

    private Supplier<Stream<String>> sourcePaths;

    private File destDir;

    public String getCompiler() {
        return compiler;
    }

    public void setCompiler(String compiler) {
        this.compiler = compiler;
    }

    public void setSourcePaths(Supplier<Stream<String>> sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public Compiler(String applicationRoot) {
        this.project = new Project();
        project.setBasedir(applicationRoot);
    }

    public void compile() {
        compile("**/*.java");
    }

    public void compile(String pattern) {
        Mkdir mkdir = new Mkdir();
        mkdir.setDir(destDir);
        mkdir.setProject(project);
        mkdir.execute();
        Javac javac = new Javac();
        javac.setCompiler(getCompiler());
        javac.setProject(project);
        Path srcs = new Path(project);
        sourcePaths.get().map(s -> new Path(project, s)).forEach(srcs::add);
        javac.setSrcdir(srcs);
        javac.setSourcepath(new Path(project, ""));
        javac.setIncludes(pattern);
        javac.setClasspath(getClasspath(project));
        javac.setDestdir(destDir);
        javac.execute();
        sourcePaths.get().forEach(s -> {
            Copy copy = new Copy();
            copy.setTodir(destDir);
            copy.setProject(project);
            FileSet fileSet = new FileSet();
            fileSet.createExclude().setName("**/*.java");
            fileSet.setDir(new File(project.getBaseDir(), s));
            fileSet.setProject(project);
            copy.addFileset(fileSet);
            copy.execute();
        });
    }

    private Path getClasspath(Project project) {
        Path classpath = new Path(project);
        // TODO relying on URLClassLoader being correct is sketchy. Find something betta.
        addClassLoaderToClasspath((URLClassLoader)Compiler.class.getClassLoader(), classpath, project);
        return classpath;
    }

    private void addClassLoaderToClasspath(URLClassLoader classLoader, Path classpath, Project project) {
        if(classLoader == null) return;
        Stream.of(classLoader.getURLs()).forEach(url ->
                classpath.add(new Path(project, url.toString()))
        );
    }
}
