package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/sos/SOSGet.class */
public class SOSGet extends SOS {
    public final void setFile(String filename) {
        super.setInternalFilename(filename);
    }

    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.sos.SOS
    protected Commandline buildCmdLine() {
        this.commandLine = new Commandline();
        if (getFilename() != null) {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            this.commandLine.createArgument().setValue(SOSCmd.COMMAND_GET_FILE);
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_FILE);
            this.commandLine.createArgument().setValue(getFilename());
            if (getVersion() != null) {
                this.commandLine.createArgument().setValue(SOSCmd.FLAG_VERSION);
                this.commandLine.createArgument().setValue(getVersion());
            }
        } else {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            this.commandLine.createArgument().setValue(SOSCmd.COMMAND_GET_PROJECT);
            this.commandLine.createArgument().setValue(getRecursive());
            if (getLabel() != null) {
                this.commandLine.createArgument().setValue(SOSCmd.FLAG_LABEL);
                this.commandLine.createArgument().setValue(getLabel());
            }
        }
        getRequiredAttributes();
        getOptionalAttributes();
        return this.commandLine;
    }
}
