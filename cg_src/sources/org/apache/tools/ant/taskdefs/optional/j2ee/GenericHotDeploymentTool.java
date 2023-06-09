package org.apache.tools.ant.taskdefs.optional.j2ee;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/j2ee/GenericHotDeploymentTool.class */
public class GenericHotDeploymentTool extends AbstractHotDeploymentTool {
    private Java java;
    private String className;
    private static final String[] VALID_ACTIONS = {HotDeploymentTool.ACTION_DEPLOY};

    public Commandline.Argument createArg() {
        return this.java.createArg();
    }

    public Commandline.Argument createJvmarg() {
        return this.java.createJvmarg();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool
    protected boolean isActionValid() {
        return getTask().getAction().equals(VALID_ACTIONS[0]);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void setTask(ServerDeploy task) {
        super.setTask(task);
        this.java = new Java(task);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void deploy() throws BuildException {
        this.java.setClassname(this.className);
        this.java.setClasspath(getClasspath());
        this.java.setFork(true);
        this.java.setFailonerror(true);
        this.java.execute();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void validateAttributes() throws BuildException {
        super.validateAttributes();
        if (this.className == null) {
            throw new BuildException("The classname attribute must be set");
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Java getJava() {
        return this.java;
    }

    public String getClassName() {
        return this.className;
    }
}
