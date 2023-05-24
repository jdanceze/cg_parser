package org.apache.tools.ant.helper;

import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/IgnoreDependenciesExecutor.class */
public class IgnoreDependenciesExecutor implements Executor {
    private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();

    @Override // org.apache.tools.ant.Executor
    public void executeTargets(Project project, String[] targetNames) throws BuildException {
        Target t;
        Hashtable<String, Target> targets = project.getTargets();
        BuildException thrownException = null;
        for (String targetName : targetNames) {
            try {
                t = targets.get(targetName);
            } catch (BuildException ex) {
                if (project.isKeepGoingMode()) {
                    thrownException = ex;
                } else {
                    throw ex;
                }
            }
            if (t == null) {
                throw new BuildException("Unknown target " + targetName);
                break;
            }
            t.performTasks();
        }
        if (thrownException != null) {
            throw thrownException;
        }
    }

    @Override // org.apache.tools.ant.Executor
    public Executor getSubProjectExecutor() {
        return SUB_EXECUTOR;
    }
}
