package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/sos/SOSLabel.class */
public class SOSLabel extends SOS {
    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.sos.SOS
    protected Commandline buildCmdLine() {
        this.commandLine = new Commandline();
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
        this.commandLine.createArgument().setValue(SOSCmd.COMMAND_LABEL);
        getRequiredAttributes();
        if (getLabel() == null) {
            throw new BuildException("label attribute must be set!", getLocation());
        }
        this.commandLine.createArgument().setValue(SOSCmd.FLAG_LABEL);
        this.commandLine.createArgument().setValue(getLabel());
        this.commandLine.createArgument().setValue(getVerbose());
        if (getComment() != null) {
            this.commandLine.createArgument().setValue("-log");
            this.commandLine.createArgument().setValue(getComment());
        }
        return this.commandLine;
    }
}
