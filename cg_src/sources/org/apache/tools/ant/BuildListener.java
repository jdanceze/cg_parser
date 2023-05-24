package org.apache.tools.ant;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/BuildListener.class */
public interface BuildListener extends EventListener {
    void buildStarted(BuildEvent buildEvent);

    void buildFinished(BuildEvent buildEvent);

    void targetStarted(BuildEvent buildEvent);

    void targetFinished(BuildEvent buildEvent);

    void taskStarted(BuildEvent buildEvent);

    void taskFinished(BuildEvent buildEvent);

    void messageLogged(BuildEvent buildEvent);
}
