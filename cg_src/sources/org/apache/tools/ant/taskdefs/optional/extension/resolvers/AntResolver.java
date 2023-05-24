package org.apache.tools.ant.taskdefs.optional.extension.resolvers;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.optional.extension.Extension;
import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/resolvers/AntResolver.class */
public class AntResolver implements ExtensionResolver {
    private File antfile;
    private File destfile;
    private String target;

    public void setAntfile(File antfile) {
        this.antfile = antfile;
    }

    public void setDestfile(File destfile) {
        this.destfile = destfile;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver
    public File resolve(Extension extension, Project project) throws BuildException {
        validate();
        Ant ant = new Ant();
        ant.setProject(project);
        ant.setInheritAll(false);
        ant.setAntfile(this.antfile.getName());
        try {
            File dir = this.antfile.getParentFile().getCanonicalFile();
            ant.setDir(dir);
            if (null != this.target) {
                ant.setTarget(this.target);
            }
            ant.execute();
            return this.destfile;
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage(), ioe);
        }
    }

    private void validate() {
        if (null == this.antfile) {
            throw new BuildException("Must specify Buildfile");
        }
        if (null == this.destfile) {
            throw new BuildException("Must specify destination file");
        }
    }

    public String toString() {
        return "Ant[" + this.antfile + "==>" + this.destfile + "]";
    }
}
