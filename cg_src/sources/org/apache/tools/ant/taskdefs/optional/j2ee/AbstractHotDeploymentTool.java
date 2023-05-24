package org.apache.tools.ant.taskdefs.optional.j2ee;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/j2ee/AbstractHotDeploymentTool.class */
public abstract class AbstractHotDeploymentTool implements HotDeploymentTool {
    private ServerDeploy task;
    private Path classpath;
    private String userName;
    private String password;
    private String server;

    protected abstract boolean isActionValid();

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(this.task.getProject());
        }
        return this.classpath.createPath();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void validateAttributes() throws BuildException {
        if (this.task.getAction() == null) {
            throw new BuildException("The \"action\" attribute must be set");
        }
        if (!isActionValid()) {
            throw new BuildException("Invalid action \"%s\" passed", this.task.getAction());
        }
        if (this.classpath == null) {
            throw new BuildException("The classpath attribute must be set");
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void setTask(ServerDeploy task) {
        this.task = task;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ServerDeploy getTask() {
        return this.task;
    }

    public Path getClasspath() {
        return this.classpath;
    }

    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
