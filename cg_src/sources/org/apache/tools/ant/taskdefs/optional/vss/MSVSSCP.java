package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSCP.class */
public class MSVSSCP extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    protected Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_CP);
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getLogin());
        return commandLine;
    }

    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }
}
