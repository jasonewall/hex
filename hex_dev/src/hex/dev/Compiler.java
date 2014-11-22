package hex.dev;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Created by jason on 14-11-21.
 */
public class Compiler {
    public static void compile() {
        Project project = new Project();
        project.setBasedir("/Users/jason/Code/embedded_jetty");
        Javac task = new Javac();
        task.setProject(project);
        task.setSrcdir(new Path(project, "src"));
        task.setDestdir(new File(project.getBaseDir(), "out/production"));
        task.execute();
    }
}
