package org.apache.tools.ant.taskdefs.cvslib;

import java.io.ByteArrayOutputStream;
import org.apache.http.protocol.HTTP;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/RedirectingStreamHandler.class */
class RedirectingStreamHandler extends PumpStreamHandler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RedirectingStreamHandler(ChangeLogParser parser) {
        super(new RedirectingOutputStream(parser), new ByteArrayOutputStream());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getErrors() {
        try {
            ByteArrayOutputStream error = (ByteArrayOutputStream) getErr();
            return error.toString(HTTP.ASCII);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.PumpStreamHandler, org.apache.tools.ant.taskdefs.ExecuteStreamHandler
    public void stop() {
        super.stop();
        FileUtils.close(getErr());
        FileUtils.close(getOut());
    }
}
