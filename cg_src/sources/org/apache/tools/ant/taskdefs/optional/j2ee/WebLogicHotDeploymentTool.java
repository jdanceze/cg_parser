package org.apache.tools.ant.taskdefs.optional.j2ee;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/j2ee/WebLogicHotDeploymentTool.class */
public class WebLogicHotDeploymentTool extends AbstractHotDeploymentTool implements HotDeploymentTool {
    private static final int STRING_BUFFER_SIZE = 1024;
    private static final String WEBLOGIC_DEPLOY_CLASS_NAME = "weblogic.deploy";
    private static final String[] VALID_ACTIONS = {HotDeploymentTool.ACTION_DELETE, HotDeploymentTool.ACTION_DEPLOY, HotDeploymentTool.ACTION_LIST, HotDeploymentTool.ACTION_UNDEPLOY, "update"};
    private boolean debug;
    private String application;
    private String component;

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void deploy() {
        Java java = new Java(getTask());
        java.setFork(true);
        java.setFailonerror(true);
        java.setClasspath(getClasspath());
        java.setClassname(WEBLOGIC_DEPLOY_CLASS_NAME);
        java.createArg().setLine(getArguments());
        java.execute();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void validateAttributes() throws BuildException {
        super.validateAttributes();
        String action = getTask().getAction();
        if (getPassword() == null) {
            throw new BuildException("The password attribute must be set.");
        }
        if ((action.equals(HotDeploymentTool.ACTION_DEPLOY) || action.equals("update")) && this.application == null) {
            throw new BuildException("The application attribute must be set if action = %s", action);
        }
        if ((action.equals(HotDeploymentTool.ACTION_DEPLOY) || action.equals("update")) && getTask().getSource() == null) {
            throw new BuildException("The source attribute must be set if action = %s", action);
        }
        if ((action.equals(HotDeploymentTool.ACTION_DELETE) || action.equals(HotDeploymentTool.ACTION_UNDEPLOY)) && this.application == null) {
            throw new BuildException("The application attribute must be set if action = %s", action);
        }
    }

    public String getArguments() throws BuildException {
        String action = getTask().getAction();
        if (action.equals(HotDeploymentTool.ACTION_DEPLOY) || action.equals("update")) {
            return buildDeployArgs();
        }
        if (action.equals(HotDeploymentTool.ACTION_DELETE) || action.equals(HotDeploymentTool.ACTION_UNDEPLOY)) {
            return buildUndeployArgs();
        }
        if (action.equals(HotDeploymentTool.ACTION_LIST)) {
            return buildListArgs();
        }
        return null;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool
    protected boolean isActionValid() {
        String[] strArr;
        String action = getTask().getAction();
        for (String validAction : VALID_ACTIONS) {
            if (action.equals(validAction)) {
                return true;
            }
        }
        return false;
    }

    protected StringBuffer buildArgsPrefix() {
        String str;
        String str2;
        ServerDeploy task = getTask();
        StringBuffer stringBuffer = new StringBuffer(1024);
        if (getServer() != null) {
            str = "-url " + getServer();
        } else {
            str = "";
        }
        StringBuffer append = stringBuffer.append(str).append(Instruction.argsep).append(this.debug ? "-debug " : "");
        if (getUserName() != null) {
            str2 = "-username " + getUserName();
        } else {
            str2 = "";
        }
        return append.append(str2).append(Instruction.argsep).append(task.getAction()).append(Instruction.argsep).append(getPassword()).append(Instruction.argsep);
    }

    protected String buildDeployArgs() {
        String args = buildArgsPrefix().append(this.application).append(Instruction.argsep).append(getTask().getSource()).toString();
        if (this.component != null) {
            args = "-component " + this.component + Instruction.argsep + args;
        }
        return args;
    }

    protected String buildUndeployArgs() {
        return buildArgsPrefix().append(this.application).append(Instruction.argsep).toString();
    }

    protected String buildListArgs() {
        return buildArgsPrefix().toString();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
