package org.apache.tools.ant.taskdefs.optional.ccm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ccm/CCMCreateTask.class */
public class CCMCreateTask extends Continuus implements ExecuteStreamHandler {
    public static final String FLAG_COMMENT = "/synopsis";
    public static final String FLAG_PLATFORM = "/plat";
    public static final String FLAG_RESOLVER = "/resolver";
    public static final String FLAG_RELEASE = "/release";
    public static final String FLAG_SUBSYSTEM = "/subsystem";
    public static final String FLAG_TASK = "/task";
    private String comment = null;
    private String platform = null;
    private String resolver = null;
    private String release = null;
    private String subSystem = null;
    private String task = null;

    public CCMCreateTask() {
        setCcmAction(Continuus.COMMAND_CREATE_TASK);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable(getCcmCommand());
        commandLine.createArgument().setValue(getCcmAction());
        checkOptions(commandLine);
        if (Execute.isFailure(run(commandLine, this))) {
            throw new BuildException("Failed executing: " + commandLine, getLocation());
        }
        Commandline commandLine2 = new Commandline();
        commandLine2.setExecutable(getCcmCommand());
        commandLine2.createArgument().setValue(Continuus.COMMAND_DEFAULT_TASK);
        commandLine2.createArgument().setValue(getTask());
        log(commandLine.describeCommand(), 4);
        if (run(commandLine2) != 0) {
            throw new BuildException("Failed executing: " + commandLine2, getLocation());
        }
    }

    private void checkOptions(Commandline cmd) {
        if (getComment() != null) {
            cmd.createArgument().setValue(FLAG_COMMENT);
            cmd.createArgument().setValue("\"" + getComment() + "\"");
        }
        if (getPlatform() != null) {
            cmd.createArgument().setValue(FLAG_PLATFORM);
            cmd.createArgument().setValue(getPlatform());
        }
        if (getResolver() != null) {
            cmd.createArgument().setValue(FLAG_RESOLVER);
            cmd.createArgument().setValue(getResolver());
        }
        if (getSubSystem() != null) {
            cmd.createArgument().setValue(FLAG_SUBSYSTEM);
            cmd.createArgument().setValue("\"" + getSubSystem() + "\"");
        }
        if (getRelease() != null) {
            cmd.createArgument().setValue(FLAG_RELEASE);
            cmd.createArgument().setValue(getRelease());
        }
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String v) {
        this.comment = v;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String v) {
        this.platform = v;
    }

    public String getResolver() {
        return this.resolver;
    }

    public void setResolver(String v) {
        this.resolver = v;
    }

    public String getRelease() {
        return this.release;
    }

    public void setRelease(String v) {
        this.release = v;
    }

    public String getSubSystem() {
        return this.subSystem;
    }

    public void setSubSystem(String v) {
        this.subSystem = v;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String v) {
        this.task = v;
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void start() throws IOException {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void stop() {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessInputStream(OutputStream param1) throws IOException {
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessErrorStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String s = reader.readLine();
            if (s != null) {
                log("err " + s, 4);
            }
            reader.close();
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void setProcessOutputStream(InputStream is) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                String buffer = reader.readLine();
                if (buffer != null) {
                    log("buffer:" + buffer, 4);
                    String taskstring = buffer.substring(buffer.indexOf(32)).trim();
                    setTask(taskstring.substring(0, taskstring.lastIndexOf(32)).trim());
                    log("task is " + getTask(), 4);
                }
                reader.close();
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (NullPointerException npe) {
            log("error procession stream, null pointer exception", 0);
            log(StringUtils.getStackTrace(npe), 0);
            throw new BuildException(npe);
        } catch (Exception e) {
            log("error procession stream " + e.getMessage(), 0);
            throw new BuildException(e.getMessage());
        }
    }
}
