package org.apache.tools.ant.types;

import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/LogLevel.class */
public class LogLevel extends EnumeratedAttribute {
    public static final LogLevel ERR = new LogLevel("error");
    public static final LogLevel WARN = new LogLevel("warn");
    public static final LogLevel INFO = new LogLevel("info");
    public static final LogLevel VERBOSE = new LogLevel("verbose");
    public static final LogLevel DEBUG = new LogLevel(Report.debug);
    private static int[] levels = {0, 1, 1, 2, 3, 4};

    public LogLevel() {
    }

    private LogLevel(String value) {
        this();
        setValue(value);
    }

    @Override // org.apache.tools.ant.types.EnumeratedAttribute
    public String[] getValues() {
        return new String[]{"error", "warn", "warning", "info", "verbose", Report.debug};
    }

    public int getLevel() {
        return levels[getIndex()];
    }
}
