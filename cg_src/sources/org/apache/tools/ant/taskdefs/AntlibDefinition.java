package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AntlibDefinition.class */
public class AntlibDefinition extends Task {
    private String uri = "";
    private ClassLoader antlibClassLoader;

    public void setURI(String uri) throws BuildException {
        if (ProjectHelper.ANT_CORE_URI.equals(uri)) {
            uri = "";
        }
        if (uri.startsWith("ant:")) {
            throw new BuildException("Attempt to use a reserved URI %s", uri);
        }
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }

    public void setAntlibClassLoader(ClassLoader classLoader) {
        this.antlibClassLoader = classLoader;
    }

    public ClassLoader getAntlibClassLoader() {
        return this.antlibClassLoader;
    }
}
