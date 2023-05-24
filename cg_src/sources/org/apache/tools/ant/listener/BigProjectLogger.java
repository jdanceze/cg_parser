package org.apache.tools.ant.listener;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/BigProjectLogger.class */
public class BigProjectLogger extends SimpleBigProjectLogger implements SubBuildListener {
    private volatile boolean subBuildStartedRaised = false;
    private final Object subBuildLock = new Object();
    public static final String HEADER = "======================================================================";
    public static final String FOOTER = "======================================================================";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.DefaultLogger
    public String getBuildFailedMessage() {
        return super.getBuildFailedMessage() + TimestampedLogger.SPACER + getTimestamp();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.DefaultLogger
    public String getBuildSuccessfulMessage() {
        return super.getBuildSuccessfulMessage() + TimestampedLogger.SPACER + getTimestamp();
    }

    @Override // org.apache.tools.ant.NoBannerLogger, org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
        maybeRaiseSubBuildStarted(event);
        super.targetStarted(event);
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
        maybeRaiseSubBuildStarted(event);
        super.taskStarted(event);
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        maybeRaiseSubBuildStarted(event);
        subBuildFinished(event);
        super.buildFinished(event);
    }

    @Override // org.apache.tools.ant.NoBannerLogger, org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
        maybeRaiseSubBuildStarted(event);
        super.messageLogged(event);
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildStarted(BuildEvent event) {
        Project project = event.getProject();
        String path = project == null ? "With no base directory" : "In " + project.getBaseDir().getAbsolutePath();
        printMessage(String.format("%n%s%nEntering project %s%n%s%n%s", getHeader(), extractNameOrDefault(event), path, getFooter()), this.out, event.getPriority());
    }

    protected String extractNameOrDefault(BuildEvent event) {
        String name;
        String name2 = extractProjectName(event);
        if (name2 == null) {
            name = "";
        } else {
            name = '\"' + name2 + '\"';
        }
        return name;
    }

    @Override // org.apache.tools.ant.SubBuildListener
    public void subBuildFinished(BuildEvent event) {
        Object[] objArr = new Object[4];
        objArr[0] = getHeader();
        objArr[1] = event.getException() != null ? "failing " : "";
        objArr[2] = extractNameOrDefault(event);
        objArr[3] = getFooter();
        printMessage(String.format("%n%s%nExiting %sproject %s%n%s", objArr), this.out, event.getPriority());
    }

    protected String getHeader() {
        return "======================================================================";
    }

    protected String getFooter() {
        return "======================================================================";
    }

    private void maybeRaiseSubBuildStarted(BuildEvent event) {
        if (!this.subBuildStartedRaised) {
            synchronized (this.subBuildLock) {
                if (!this.subBuildStartedRaised) {
                    this.subBuildStartedRaised = true;
                    subBuildStarted(event);
                }
            }
        }
    }
}
