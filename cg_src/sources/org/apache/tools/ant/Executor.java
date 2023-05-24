package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Executor.class */
public interface Executor {
    void executeTargets(Project project, String[] strArr) throws BuildException;

    Executor getSubProjectExecutor();
}
