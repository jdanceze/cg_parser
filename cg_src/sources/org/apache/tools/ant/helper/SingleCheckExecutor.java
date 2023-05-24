package org.apache.tools.ant.helper;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Executor;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/SingleCheckExecutor.class */
public class SingleCheckExecutor implements Executor {
    @Override // org.apache.tools.ant.Executor
    public void executeTargets(Project project, String[] targetNames) throws BuildException {
        project.executeSortedTargets(project.topoSort(targetNames, project.getTargets(), false));
    }

    @Override // org.apache.tools.ant.Executor
    public Executor getSubProjectExecutor() {
        return this;
    }
}
