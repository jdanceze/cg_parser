package org.apache.tools.ant.taskdefs.optional.j2ee;

import java.io.File;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/j2ee/ServerDeploy.class */
public class ServerDeploy extends Task {
    private String action;
    private File source;
    private List<AbstractHotDeploymentTool> vendorTools = new Vector();

    public void addGeneric(GenericHotDeploymentTool tool) {
        tool.setTask(this);
        this.vendorTools.add(tool);
    }

    public void addWeblogic(WebLogicHotDeploymentTool tool) {
        tool.setTask(this);
        this.vendorTools.add(tool);
    }

    public void addJonas(JonasHotDeploymentTool tool) {
        tool.setTask(this);
        this.vendorTools.add(tool);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        for (HotDeploymentTool tool : this.vendorTools) {
            tool.validateAttributes();
            tool.deploy();
        }
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public File getSource() {
        return this.source;
    }

    public void setSource(File source) {
        this.source = source;
    }
}
