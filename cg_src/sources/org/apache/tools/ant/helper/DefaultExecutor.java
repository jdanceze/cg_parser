package org.apache.tools.ant.helper;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/DefaultExecutor.class */
public class DefaultExecutor implements Executor {
    private static final SingleCheckExecutor SUB_EXECUTOR = new SingleCheckExecutor();

    @Override // org.apache.tools.ant.Executor
    public void executeTargets(Project project, String[] targetNames) throws BuildException {
        BuildException thrownException = null;
        for (String targetName : targetNames) {
            try {
                project.executeTarget(targetName);
            } catch (BuildException ex) {
                if (project.isKeepGoingMode()) {
                    thrownException = ex;
                } else {
                    throw ex;
                }
            }
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
