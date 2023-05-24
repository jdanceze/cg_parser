package org.apache.tools.ant.taskdefs;

import java.net.URL;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/WhichResource.class */
public class WhichResource extends Task {
    private Path classpath;
    private String classname;
    private String resource;
    private String property;

    public void setClasspath(Path cp) {
        if (this.classpath == null) {
            this.classpath = cp;
        } else {
            this.classpath.append(cp);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    private void validate() {
        int setcount = 0;
        if (this.classname != null) {
            setcount = 0 + 1;
        }
        if (this.resource != null) {
            setcount++;
        }
        if (setcount == 0) {
            throw new BuildException("One of classname or resource must be specified");
        }
        if (setcount > 1) {
            throw new BuildException("Only one of classname or resource can be specified");
        }
        if (this.property == null) {
            throw new BuildException(MakeUrl.ERROR_NO_PROPERTY);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validate();
        if (this.classpath != null) {
            this.classpath = this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
            getProject().log("using user supplied classpath: " + this.classpath, 4);
        } else {
            this.classpath = new Path(getProject());
            this.classpath = this.classpath.concatSystemClasspath("only");
            getProject().log("using system classpath: " + this.classpath, 4);
        }
        AntClassLoader loader = AntClassLoader.newAntClassLoader(getProject().getCoreLoader(), getProject(), this.classpath, false);
        try {
            if (this.classname != null) {
                this.resource = this.classname.replace('.', '/') + ".class";
            }
            if (this.resource == null) {
                throw new BuildException("One of class or resource is required");
            }
            if (this.resource.startsWith("/")) {
                this.resource = this.resource.substring(1);
            }
            log("Searching for " + this.resource, 3);
            URL url = loader.getResource(this.resource);
            if (url != null) {
                String loc = url.toExternalForm();
                getProject().setNewProperty(this.property, loc);
            }
            if (loader != null) {
                loader.close();
            }
        } catch (Throwable th) {
            if (loader != null) {
                try {
                    loader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setClass(String classname) {
        this.classname = classname;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
