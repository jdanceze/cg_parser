package org.apache.tools.ant.taskdefs;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DefaultExcludes.class */
public class DefaultExcludes extends Task {
    private String add = "";
    private String remove = "";
    private boolean defaultrequested = false;
    private boolean echo = false;
    private int logLevel = 1;

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (!this.defaultrequested && this.add.isEmpty() && this.remove.isEmpty() && !this.echo) {
            throw new BuildException("<defaultexcludes> task must set at least one attribute (echo=\"false\" doesn't count since that is the default");
        }
        if (this.defaultrequested) {
            DirectoryScanner.resetDefaultExcludes();
        }
        if (!this.add.isEmpty()) {
            DirectoryScanner.addDefaultExclude(this.add);
        }
        if (!this.remove.isEmpty()) {
            DirectoryScanner.removeDefaultExclude(this.remove);
        }
        if (this.echo) {
            String message = (String) Arrays.stream(DirectoryScanner.getDefaultExcludes()).map(exclude -> {
                return String.format("  %s%n", exclude);
            }).collect(Collectors.joining("", "Current Default Excludes:%n", ""));
            log(message, this.logLevel);
        }
    }

    public void setDefault(boolean def) {
        this.defaultrequested = def;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }
}
