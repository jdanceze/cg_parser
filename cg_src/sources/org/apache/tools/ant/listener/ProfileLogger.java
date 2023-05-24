package org.apache.tools.ant.listener;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/ProfileLogger.class */
public class ProfileLogger extends DefaultLogger {
    private Map<Object, Date> profileData = new ConcurrentHashMap();

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
        Date now = new Date();
        String name = "Target " + event.getTarget().getName();
        logStart(event, now, name);
        this.profileData.put(event.getTarget(), now);
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
        Date start = this.profileData.remove(event.getTarget());
        String name = "Target " + event.getTarget().getName();
        logFinish(event, start, name);
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
        String name = event.getTask().getTaskName();
        Date now = new Date();
        logStart(event, now, name);
        this.profileData.put(event.getTask(), now);
    }

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
        Date start = this.profileData.remove(event.getTask());
        String name = event.getTask().getTaskName();
        logFinish(event, start, name);
    }

    private void logFinish(BuildEvent event, Date start, String name) {
        String msg;
        Date now = new Date();
        if (start != null) {
            long diff = now.getTime() - start.getTime();
            msg = String.format("%n%s: finished %s (%d)", name, now, Long.valueOf(diff));
        } else {
            msg = String.format("%n%s: finished %s (unknown duration, start not detected)", name, now);
        }
        printMessage(msg, this.out, event.getPriority());
        log(msg);
    }

    private void logStart(BuildEvent event, Date start, String name) {
        String msg = String.format("%n%s: started %s", name, start);
        printMessage(msg, this.out, event.getPriority());
        log(msg);
    }
}
