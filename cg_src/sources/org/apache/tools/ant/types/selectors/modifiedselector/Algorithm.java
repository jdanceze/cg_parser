package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/Algorithm.class */
public interface Algorithm {
    boolean isValid();

    String getValue(File file);
}
