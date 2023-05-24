package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSLABEL.class */
public class MSVSSLABEL extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        String label = getLabel();
        if (label.isEmpty()) {
            throw new BuildException("label attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_LABEL);
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getComment());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(label);
        commandLine.createArgument().setValue(getVersion());
        commandLine.createArgument().setValue(getLogin());
        return commandLine;
    }

    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }
}
