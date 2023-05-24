package org.apache.tools.ant.filters;

import java.io.StringReader;
import org.apache.tools.ant.util.ReaderInputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/StringInputStream.class */
public class StringInputStream extends ReaderInputStream {
    public StringInputStream(String source) {
        super(new StringReader(source));
    }

    public StringInputStream(String source, String encoding) {
        super(new StringReader(source), encoding);
    }
}
