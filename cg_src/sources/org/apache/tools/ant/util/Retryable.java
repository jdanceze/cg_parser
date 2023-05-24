package org.apache.tools.ant.util;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/Retryable.class */
public interface Retryable {
    public static final int RETRY_FOREVER = -1;

    void execute() throws IOException;
}
