package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LineTokenizer.class */
public class LineTokenizer extends ProjectComponent implements Tokenizer {
    private static final int NOT_A_CHAR = -2;
    private String lineEnd = "";
    private int pushed = -2;
    private boolean includeDelims = false;

    public void setIncludeDelims(boolean includeDelims) {
        this.includeDelims = includeDelims;
    }

    @Override // org.apache.tools.ant.util.Tokenizer
    public String getToken(Reader in) throws IOException {
        int ch;
        if (this.pushed == -2) {
            ch = in.read();
        } else {
            ch = this.pushed;
            this.pushed = -2;
        }
        if (ch == -1) {
            return null;
        }
        this.lineEnd = "";
        StringBuilder line = new StringBuilder();
        int state = 0;
        while (true) {
            if (ch == -1) {
                break;
            } else if (state == 0) {
                if (ch == 13) {
                    state = 1;
                } else if (ch == 10) {
                    this.lineEnd = "\n";
                    break;
                } else {
                    line.append((char) ch);
                }
                ch = in.read();
            } else {
                state = 0;
                if (ch == 10) {
                    this.lineEnd = "\r\n";
                } else {
                    this.pushed = ch;
                    this.lineEnd = "\r";
                }
            }
        }
        if (ch == -1 && state == 1) {
            this.lineEnd = "\r";
        }
        if (this.includeDelims) {
            line.append(this.lineEnd);
        }
        return line.toString();
    }

    @Override // org.apache.tools.ant.util.Tokenizer
    public String getPostToken() {
        return this.includeDelims ? "" : this.lineEnd;
    }
}
