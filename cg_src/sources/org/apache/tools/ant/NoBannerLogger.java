package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/NoBannerLogger.class */
public class NoBannerLogger extends DefaultLogger {
    protected String targetName;

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public synchronized void targetStarted(BuildEvent event) {
        this.targetName = extractTargetName(event);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String extractTargetName(BuildEvent event) {
        return event.getTarget().getName();
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public synchronized void targetFinished(BuildEvent event) {
        this.targetName = null;
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
        if (event.getPriority() > this.msgOutputLevel || null == event.getMessage() || event.getMessage().trim().isEmpty()) {
            return;
        }
        synchronized (this) {
            if (null != this.targetName) {
                this.out.println(String.format("%n%s:", this.targetName));
                this.targetName = null;
            }
        }
        super.messageLogged(event);
    }
}
