package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.LogOutputResource;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Echo.class */
public class Echo extends Task {
    protected String message = "";
    protected File file = null;
    protected boolean append = false;
    private String encoding = "";
    private boolean force = false;
    protected int logLevel = 1;
    private Resource output;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Echo$EchoLevel.class */
    public static class EchoLevel extends LogLevel {
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        try {
            ResourceUtils.copyResource(new StringResource(this.message.isEmpty() ? System.lineSeparator() : this.message), this.output == null ? new LogOutputResource(this, this.logLevel) : this.output, null, null, false, false, this.append, null, this.encoding.isEmpty() ? null : this.encoding, getProject(), this.force);
        } catch (IOException ioe) {
            throw new BuildException(ioe, getLocation());
        }
    }

    public void setMessage(String msg) {
        this.message = msg == null ? "" : msg;
    }

    public void setFile(File file) {
        setOutput(new FileResource(getProject(), file));
    }

    public void setOutput(Resource output) {
        if (this.output != null) {
            throw new BuildException("Cannot set > 1 output target");
        }
        this.output = output;
        FileProvider fp = (FileProvider) output.as(FileProvider.class);
        this.file = fp != null ? fp.getFile() : null;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void addText(String msg) {
        this.message += getProject().replaceProperties(msg);
    }

    public void setLevel(EchoLevel echoLevel) {
        this.logLevel = echoLevel.getLevel();
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setForce(boolean f) {
        this.force = f;
    }
}
