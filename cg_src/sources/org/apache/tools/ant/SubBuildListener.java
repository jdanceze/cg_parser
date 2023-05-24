package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/SubBuildListener.class */
public interface SubBuildListener extends BuildListener {
    void subBuildStarted(BuildEvent buildEvent);

    void subBuildFinished(BuildEvent buildEvent);
}
