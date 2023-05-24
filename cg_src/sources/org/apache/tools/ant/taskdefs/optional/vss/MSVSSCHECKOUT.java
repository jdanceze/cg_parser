package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSS;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSCHECKOUT.class */
public class MSVSSCHECKOUT extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    protected Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_CHECKOUT);
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getVersionDateLabel());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getFileTimeStamp());
        commandLine.createArgument().setValue(getWritableFiles());
        commandLine.createArgument().setValue(getGetLocalCopy());
        return commandLine;
    }

    public void setLocalpath(Path localPath) {
        super.setInternalLocalPath(localPath.toString());
    }

    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    public void setDate(String date) {
        super.setInternalDate(date);
    }

    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }

    public void setFileTimeStamp(MSVSS.CurrentModUpdated timestamp) {
        super.setInternalFileTimeStamp(timestamp);
    }

    public void setWritableFiles(MSVSS.WritableFiles files) {
        super.setInternalWritableFiles(files);
    }

    public void setGetLocalCopy(boolean get) {
        super.setInternalGetLocalCopy(get);
    }
}
