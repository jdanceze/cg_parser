package org.apache.tools.ant.taskdefs;

import java.util.Hashtable;
import java.util.Map;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.LogLevel;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Recorder.class */
public class Recorder extends Task implements SubBuildListener {
    private String filename = null;
    private Boolean append = null;
    private Boolean start = null;
    private int loglevel = -1;
    private boolean emacsMode = false;
    private static Map<String, RecorderEntry> recorderEntries = new Hashtable();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Recorder$VerbosityLevelChoices.class */
    public static class VerbosityLevelChoices extends LogLevel {
    }

    @Override // org.apache.tools.ant.Task
    public void init() {
        getProject().addBuildListener(this);
    }

    public void setName(String fname) {
        this.filename = fname;
    }

    public void setAction(ActionChoices action) {
        if (action.getValue().equalsIgnoreCase("start")) {
            this.start = Boolean.TRUE;
        } else {
            this.start = Boolean.FALSE;
        }
    }

    public void setAppend(boolean append) {
        this.append = append ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setEmacsMode(boolean emacsMode) {
        this.emacsMode = emacsMode;
    }

    public void setLoglevel(VerbosityLevelChoices level) {
        this.loglevel = level.getLevel();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.filename == null) {
            throw new BuildException("No filename specified");
        }
        getProject().log("setting a recorder for name " + this.filename, 4);
        RecorderEntry recorder = getRecorder(this.filename, getProject());
        recorder.setMessageOutputLevel(this.loglevel);
        recorder.setEmacsMode(this.emacsMode);
        if (this.start != null) {
            if (this.start.booleanValue()) {
                recorder.reopenFile();
                recorder.setRecordState(this.start);
                return;
            }
            recorder.setRecordState(this.start);
            recorder.closeFile();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Recorder$ActionChoices.class */
    public static class ActionChoices extends EnumeratedAttribute {
        private static final String[] VALUES = {"start", "stop"};

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALUES;
        }
    }

    protected RecorderEntry getRecorder(String name, Project proj) throws BuildException {
        RecorderEntry entry = recorderEntries.get(name);
        if (entry == null) {
            entry = new RecorderEntry(name);
            if (this.append == null) {
                entry.openFile(false);
            } else {
                entry.openFile(this.append.booleanValue());
            }
            entry.setProject(proj);
            recorderEntries.put(name, entry);
        }
        return entry;
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        cleanup();
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildFinished(BuildEvent event) {
        if (event.getProject() == getProject()) {
            cleanup();
        }
    }

    private void cleanup() {
        recorderEntries.entrySet().removeIf(e -> {
            return ((RecorderEntry) e.getValue()).getProject() == getProject();
        });
        getProject().removeBuildListener(this);
    }
}
