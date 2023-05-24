package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Diagnostics;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/DiagnosticsTask.class */
public class DiagnosticsTask extends Task {
    private static final String[] ARGS = new String[0];

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Diagnostics.main(ARGS);
    }
}
