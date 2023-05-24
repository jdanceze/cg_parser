package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/LogStreamHandler.class */
public class LogStreamHandler extends PumpStreamHandler {
    public LogStreamHandler(Task task, int outlevel, int errlevel) {
        this((ProjectComponent) task, outlevel, errlevel);
    }

    public LogStreamHandler(ProjectComponent pc, int outlevel, int errlevel) {
        super(new LogOutputStream(pc, outlevel), new LogOutputStream(pc, errlevel));
    }

    @Override // org.apache.tools.ant.taskdefs.PumpStreamHandler, org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void stop() {
        super.stop();
        FileUtils.close(getErr());
        FileUtils.close(getOut());
    }
}
