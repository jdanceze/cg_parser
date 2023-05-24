package org.apache.tools.ant.listener;

import org.apache.tools.ant.DefaultLogger;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/TimestampedLogger.class */
public class TimestampedLogger extends DefaultLogger {
    public static final String SPACER = " - at ";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.DefaultLogger
    public String getBuildFailedMessage() {
        return super.getBuildFailedMessage() + SPACER + getTimestamp();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.DefaultLogger
    public String getBuildSuccessfulMessage() {
        return super.getBuildSuccessfulMessage() + SPACER + getTimestamp();
    }
}
