package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/Tokenizer.class */
public interface Tokenizer {
    String getToken(Reader reader) throws IOException;

    String getPostToken();
}
