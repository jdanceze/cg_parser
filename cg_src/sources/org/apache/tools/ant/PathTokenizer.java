package org.apache.tools.ant;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.tools.ant.taskdefs.condition.Os;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PathTokenizer.class */
public class PathTokenizer {
    private StringTokenizer tokenizer;
    private String lookahead = null;
    private boolean onNetWare = Os.isFamily(Os.FAMILY_NETWARE);
    private boolean dosStyleFilesystem;

    public PathTokenizer(String path) {
        if (this.onNetWare) {
            this.tokenizer = new StringTokenizer(path, ":;", true);
        } else {
            this.tokenizer = new StringTokenizer(path, ":;", false);
        }
        this.dosStyleFilesystem = File.pathSeparatorChar == ';';
    }

    public boolean hasMoreTokens() {
        return this.lookahead != null || this.tokenizer.hasMoreTokens();
    }

    public String nextToken() throws NoSuchElementException {
        String token;
        if (this.lookahead != null) {
            token = this.lookahead;
            this.lookahead = null;
        } else {
            token = this.tokenizer.nextToken().trim();
        }
        if (!this.onNetWare) {
            if (token.length() == 1 && Character.isLetter(token.charAt(0)) && this.dosStyleFilesystem && this.tokenizer.hasMoreTokens()) {
                String nextToken = this.tokenizer.nextToken().trim();
                if (nextToken.startsWith("\\") || nextToken.startsWith("/")) {
                    token = token + ":" + nextToken;
                } else {
                    this.lookahead = nextToken;
                }
            }
        } else {
            if (token.equals(File.pathSeparator) || ":".equals(token)) {
                token = this.tokenizer.nextToken().trim();
            }
            if (this.tokenizer.hasMoreTokens()) {
                String nextToken2 = this.tokenizer.nextToken().trim();
                if (!nextToken2.equals(File.pathSeparator)) {
                    if (":".equals(nextToken2)) {
                        if (!token.startsWith("/") && !token.startsWith("\\") && !token.startsWith(".") && !token.startsWith("..")) {
                            String oneMore = this.tokenizer.nextToken().trim();
                            if (!oneMore.equals(File.pathSeparator)) {
                                token = token + ":" + oneMore;
                            } else {
                                token = token + ":";
                                this.lookahead = oneMore;
                            }
                        }
                    } else {
                        this.lookahead = nextToken2;
                    }
                }
            }
        }
        return token;
    }
}
