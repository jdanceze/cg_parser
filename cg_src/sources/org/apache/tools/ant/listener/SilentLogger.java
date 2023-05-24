package org.apache.tools.ant.listener;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/SilentLogger.class */
public class SilentLogger extends DefaultLogger {
    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        if (event.getException() != null) {
            super.buildFinished(event);
        }
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
    }
}
