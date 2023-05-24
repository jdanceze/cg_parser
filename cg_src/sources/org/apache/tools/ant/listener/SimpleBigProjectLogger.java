package org.apache.tools.ant.listener;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.NoBannerLogger;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/SimpleBigProjectLogger.class */
public class SimpleBigProjectLogger extends NoBannerLogger {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.NoBannerLogger
    public String extractTargetName(BuildEvent event) {
        String targetName = super.extractTargetName(event);
        String projectName = extractProjectName(event);
        if (projectName == null || targetName == null) {
            return targetName;
        }
        return projectName + '.' + targetName;
    }
}
