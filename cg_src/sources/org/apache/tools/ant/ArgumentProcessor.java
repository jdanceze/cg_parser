package org.apache.tools.ant;

import java.io.PrintStream;
import java.util.List;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ArgumentProcessor.class */
public interface ArgumentProcessor {
    int readArguments(String[] strArr, int i);

    boolean handleArg(List<String> list);

    void prepareConfigure(Project project, List<String> list);

    boolean handleArg(Project project, List<String> list);

    void printUsage(PrintStream printStream);
}
