package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSS;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/vss/MSVSSGET.class */
public class MSVSSGET extends MSVSS {
    @Override // org.apache.tools.ant.taskdefs.optional.vss.MSVSS
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(MSVSSConstants.COMMAND_GET);
        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getQuiet());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getVersionDateLabel());
        commandLine.createArgument().setValue(getWritable());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getFileTimeStamp());
        commandLine.createArgument().setValue(getWritableFiles());
        return commandLine;
    }

    public void setLocalpath(Path localPath) {
        super.setInternalLocalPath(localPath.toString());
    }

    public final void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    public final void setQuiet(boolean quiet) {
        super.setInternalQuiet(quiet);
    }

    public final void setWritable(boolean writable) {
        super.setInternalWritable(writable);
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
}
