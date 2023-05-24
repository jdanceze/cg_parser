package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSCREATE.class */
public class MSVSSCREATE extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_CREATE);
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getComment());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getQuiet());
        commandLine.createArgument().setValue(getLogin());
        return commandLine;
    }

    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    public final void setQuiet(boolean quiet) {
        super.setInternalQuiet(quiet);
    }

    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }
}
