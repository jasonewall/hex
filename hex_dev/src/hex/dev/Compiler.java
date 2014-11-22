package hex.dev;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;

import java.io.File;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-21.
 */
public class Compiler {
    public static void compile(Stream<String> sourcePaths) {
        Project project = new Project();
        project.setBasedir(System.getProperty("user.dir"));
        Javac task = new Javac();
        task.setProject(project);
        Path srcs = sourcePaths.reduce(new Path(project), (par, p) -> {
            par.add(new Path(project, p));
            return par;
        }, (p1, p2) -> { p1.add(p2); return p1; });
        task.setSrcdir(srcs);
        task.setDestdir(new File(project.getBaseDir(), "out/production"));
        FilenameSelector sel = new FilenameSelector();
        sel.setName("*.properties");
        task.add(sel);
        task.execute();
    }
}
