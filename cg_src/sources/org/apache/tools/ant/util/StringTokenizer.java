package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/StringTokenizer.class */
public class StringTokenizer extends ProjectComponent implements Tokenizer {
    private static final int NOT_A_CHAR = -2;
    private String intraString = "";
    private int pushed = -2;
    private char[] delims = null;
    private boolean delimsAreTokens = false;
    private boolean suppressDelims = false;
    private boolean includeDelims = false;

    public void setDelims(String delims) {
        this.delims = StringUtils.resolveBackSlash(delims).toCharArray();
    }

    public void setDelimsAreTokens(boolean delimsAreTokens) {
        this.delimsAreTokens = delimsAreTokens;
    }

    public void setSuppressDelims(boolean suppressDelims) {
        this.suppressDelims = suppressDelims;
    }

    public void setIncludeDelims(boolean includeDelims) {
        this.includeDelims = includeDelims;
    }

    @Override // org.apache.tools.ant.util.Tokenizer
    public String getToken(Reader in) throws IOException {
        int ch;
        if (this.pushed != -2) {
            ch = this.pushed;
            this.pushed = -2;
        } else {
            ch = in.read();
        }
        if (ch == -1) {
            return null;
        }
        boolean inToken = true;
        this.intraString = "";
        StringBuilder word = new StringBuilder();
        StringBuilder padding = new StringBuilder();
        while (true) {
            if (ch == -1) {
                break;
            }
            char c = (char) ch;
            boolean isDelim = isDelim(c);
            if (inToken) {
                if (isDelim) {
                    if (this.delimsAreTokens) {
                        if (word.length() > 0) {
                            this.pushed = ch;
                        } else {
                            word.append(c);
                        }
                    } else {
                        padding.append(c);
                        inToken = false;
                    }
                } else {
                    word.append(c);
                }
                ch = in.read();
            } else if (isDelim) {
                padding.append(c);
                ch = in.read();
            } else {
                this.pushed = ch;
                break;
            }
        }
        this.intraString = padding.toString();
        if (this.includeDelims) {
            word.append(this.intraString);
        }
        return word.toString();
    }

    @Override // org.apache.tools.ant.util.Tokenizer
    public String getPostToken() {
        return (this.suppressDelims || this.includeDelims) ? "" : this.intraString;
    }

    private boolean isDelim(char ch) {
        char[] cArr;
        if (this.delims == null) {
            return Character.isWhitespace(ch);
        }
        for (char delim : this.delims) {
            if (delim == ch) {
                return true;
            }
        }
        return false;
    }
}
