package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/sos/SOSCheckin.class */
public class SOSCheckin extends SOS {
    public final void setFile(String filename) {
        super.setInternalFilename(filename);
    }

    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.sos.SOS
    protected Commandline buildCmdLine() {
        this.commandLine = new Commandline();
        if (getFilename() != null) {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            this.commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKIN_FILE);
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_FILE);
            this.commandLine.createArgument().setValue(getFilename());
        } else {
            this.commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            this.commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKIN_PROJECT);
            this.commandLine.createArgument().setValue(getRecursive());
        }
        getRequiredAttributes();
        getOptionalAttributes();
        if (getComment() != null) {
            this.commandLine.createArgument().setValue("-log");
            this.commandLine.createArgument().setValue(getComment());
        }
        return this.commandLine;
    }
}
