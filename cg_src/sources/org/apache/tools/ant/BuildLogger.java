package org.apache.tools.ant;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/BuildLogger.class */
public interface BuildLogger extends BuildListener {
    void setMessageOutputLevel(int i);

    void setOutputPrintStream(PrintStream printStream);

    void setEmacsMode(boolean z);

    void setErrorPrintStream(PrintStream printStream);
}
