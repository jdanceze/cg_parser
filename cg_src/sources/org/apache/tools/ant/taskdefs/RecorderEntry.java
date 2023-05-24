package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.util.FileUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/RecorderEntry.class */
public class RecorderEntry implements BuildLogger, SubBuildListener {
    private String filename;
    private long targetStartTime;
    private Project project;
    private boolean record = true;
    private int loglevel = 2;
    private PrintStream out = null;
    private boolean emacsMode = false;

    /* JADX INFO: Access modifiers changed from: protected */
    public RecorderEntry(String name) {
        this.filename = null;
        this.targetStartTime = 0L;
        this.targetStartTime = System.currentTimeMillis();
        this.filename = name;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setRecordState(Boolean state) {
        if (state != null) {
            flush();
            this.record = state.booleanValue();
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
        log("> BUILD STARTED", 4);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        log("< BUILD FINISHED", 4);
        if (this.record && this.out != null) {
            Throwable error = event.getException();
            if (error == null) {
                this.out.println(String.format("%nBUILD SUCCESSFUL", new Object[0]));
            } else {
                this.out.println(String.format("%nBUILD FAILED%n", new Object[0]));
                error.printStackTrace(this.out);
            }
        }
        cleanup();
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildFinished(BuildEvent event) {
        if (event.getProject() == this.project) {
            cleanup();
        }
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
        log(">> TARGET STARTED -- " + event.getTarget(), 4);
        log(String.format("%n%s:", event.getTarget().getName()), 2);
        this.targetStartTime = System.currentTimeMillis();
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
        log("<< TARGET FINISHED -- " + event.getTarget(), 4);
        String time = formatTime(System.currentTimeMillis() - this.targetStartTime);
        log(event.getTarget() + ":  duration " + time, 3);
        flush();
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
        log(">>> TASK STARTED -- " + event.getTask(), 4);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
        log("<<< TASK FINISHED -- " + event.getTask(), 4);
        flush();
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
        log("--- MESSAGE LOGGED", 4);
        StringBuffer buf = new StringBuffer();
        if (event.getTask() != null) {
            String name = event.getTask().getTaskName();
            if (!this.emacsMode) {
                String label = "[" + name + "] ";
                int size = 12 - label.length();
                for (int i = 0; i < size; i++) {
                    buf.append(Instruction.argsep);
                }
                buf.append(label);
            }
        }
        buf.append(event.getMessage());
        log(buf.toString(), event.getPriority());
    }

    private void log(String mesg, int level) {
        if (this.record && level <= this.loglevel && this.out != null) {
            this.out.println(mesg);
        }
    }

    private void flush() {
        if (this.record && this.out != null) {
            this.out.flush();
        }
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setMessageOutputLevel(int level) {
        if (level >= 0 && level <= 4) {
            this.loglevel = level;
        }
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setOutputPrintStream(PrintStream output) {
        closeFile();
        this.out = output;
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setEmacsMode(boolean emacsMode) {
        this.emacsMode = emacsMode;
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setErrorPrintStream(PrintStream err) {
        setOutputPrintStream(err);
    }

    private static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        if (minutes > 0) {
            return Long.toString(minutes) + " minute" + (minutes == 1 ? Instruction.argsep : "s ") + Long.toString(seconds % 60) + " second" + (seconds % 60 == 1 ? "" : "s");
        }
        return Long.toString(seconds) + " second" + (seconds % 60 == 1 ? "" : "s");
    }

    public void setProject(Project project) {
        this.project = project;
        if (project != null) {
            project.addBuildListener(this);
        }
    }

    public Project getProject() {
        return this.project;
    }

    public void cleanup() {
        closeFile();
        if (this.project != null) {
            this.project.removeBuildListener(this);
        }
        this.project = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void openFile(boolean append) throws BuildException {
        openFileImpl(append);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeFile() {
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reopenFile() throws BuildException {
        openFileImpl(true);
    }

    private void openFileImpl(boolean append) throws BuildException {
        if (this.out == null) {
            try {
                this.out = new PrintStream(FileUtils.newOutputStream(Paths.get(this.filename, new String[0]), append));
            } catch (IOException ioe) {
                throw new BuildException("Problems opening file using a recorder entry", ioe);
            }
        }
    }
}
