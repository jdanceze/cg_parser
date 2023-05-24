package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.util.LineOrientedOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/RedirectingOutputStream.class */
class RedirectingOutputStream extends LineOrientedOutputStream {
    private final ChangeLogParser parser;

    public RedirectingOutputStream(ChangeLogParser parser) {
        this.parser = parser;
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream
    protected void processLine(String line) {
        this.parser.stdout(line);
    }
}
