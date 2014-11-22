package hex.dev;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by jason on 14-11-21.
 */
public class Compiler {
    public static void compile(Supplier<Stream<String>> sourcePaths, String outPath) {
        Project project = new Project();
        project.setBasedir(System.getProperty("user.dir"));
        File destDir = new File(project.getBaseDir(), outPath);
        Mkdir mkdir = new Mkdir();
        mkdir.setDir(destDir);
        mkdir.setProject(project);
        mkdir.execute();
        Javac javac = new Javac();
        javac.setProject(project);
        Path srcs = new Path(project);
        sourcePaths.get().map(s -> new Path(project, s)).forEach(srcs::add);
        javac.setSrcdir(srcs);
        javac.setDestdir(destDir);
        javac.execute();
        sourcePaths.get().forEach(s -> {
            Copy copy = new Copy();
            copy.setTodir(destDir);
            copy.setProject(project);
            FileSet fileSet = new FileSet();
            fileSet.createExclude().setName("**/*/java");
            fileSet.setDir(new File(project.getBaseDir(), s));
            fileSet.setProject(project);
            copy.addFileset(fileSet);
            copy.execute();
        });
    }
}
