package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/FileTokenizer.class */
public class FileTokenizer extends ProjectComponent implements Tokenizer {
    @Override // org.apache.tools.ant.util.Tokenizer
    public String getToken(Reader in) throws IOException {
        return FileUtils.readFully(in);
    }

    @Override // org.apache.tools.ant.util.Tokenizer
    public String getPostToken() {
        return "";
    }
}
