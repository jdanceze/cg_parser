package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.Mapper;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/RenameExtensions.class */
public class RenameExtensions extends MatchingTask {
    private File srcDir;
    private String fromExtension = "";
    private String toExtension = "";
    private boolean replace = false;
    private Mapper.MapperType globType = new Mapper.MapperType();

    public RenameExtensions() {
        this.globType.setValue("glob");
    }

    public void setFromExtension(String from) {
        this.fromExtension = from;
    }

    public void setToExtension(String to) {
        this.toExtension = to;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public void setSrcDir(File srcDir) {
        this.srcDir = srcDir;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.fromExtension == null || this.toExtension == null || this.srcDir == null) {
            throw new BuildException("srcDir, fromExtension and toExtension attributes must be set!");
        }
        log("DEPRECATED - The renameext task is deprecated.  Use move instead.", 1);
        log("Replace this with:", 2);
        log("<move todir=\"" + this.srcDir + "\" overwrite=\"" + this.replace + "\">", 2);
        log("  <fileset dir=\"" + this.srcDir + "\" />", 2);
        log("  <mapper type=\"glob\"", 2);
        log("          from=\"*" + this.fromExtension + "\"", 2);
        log("          to=\"*" + this.toExtension + "\" />", 2);
        log("</move>", 2);
        log("using the same patterns on <fileset> as you've used here", 2);
        Move move = new Move();
        move.bindToOwner(this);
        move.setOwningTarget(getOwningTarget());
        move.setTaskName(getTaskName());
        move.setLocation(getLocation());
        move.setTodir(this.srcDir);
        move.setOverwrite(this.replace);
        this.fileset.setDir(this.srcDir);
        move.addFileset(this.fileset);
        Mapper me = move.createMapper();
        me.setType(this.globType);
        me.setFrom("*" + this.fromExtension);
        me.setTo("*" + this.toExtension);
        move.execute();
    }
}
