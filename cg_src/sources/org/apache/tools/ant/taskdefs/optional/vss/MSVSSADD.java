package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSADD.class */
public class MSVSSADD extends MSVSS {
    private String localPath = null;

    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    protected Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getLocalpath() == null) {
            throw new BuildException("localPath attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_ADD);
        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getWritable());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getComment());
        return commandLine;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    public String getLocalpath() {
        return this.localPath;
    }

    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public final void setWritable(boolean writable) {
        super.setInternalWritable(writable);
    }

    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }

    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    public void setLocalpath(Path localPath) {
        this.localPath = localPath.toString();
    }
}
