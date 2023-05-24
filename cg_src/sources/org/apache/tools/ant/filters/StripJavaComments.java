package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/StripJavaComments.class */
public final class StripJavaComments extends BaseFilterReader implements ChainableReader {
    private int readAheadCh;
    private boolean inString;
    private boolean quoted;

    public StripJavaComments() {
        this.readAheadCh = -1;
        this.inString = false;
        this.quoted = false;
    }

    public StripJavaComments(Reader in) {
        super(in);
        this.readAheadCh = -1;
        this.inString = false;
        this.quoted = false;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch;
        if (this.readAheadCh != -1) {
            ch = this.readAheadCh;
            this.readAheadCh = -1;
        } else {
            ch = this.in.read();
            if (ch == 34 && !this.quoted) {
                this.inString = !this.inString;
                this.quoted = false;
            } else if (ch == 92) {
                this.quoted = !this.quoted;
            } else {
                this.quoted = false;
                if (!this.inString && ch == 47) {
                    ch = this.in.read();
                    if (ch == 47) {
                        while (ch != 10 && ch != -1 && ch != 13) {
                            ch = this.in.read();
                        }
                    } else if (ch == 42) {
                        while (true) {
                            if (ch == -1) {
                                break;
                            }
                            ch = this.in.read();
                            if (ch == 42) {
                                int read = this.in.read();
                                while (true) {
                                    ch = read;
                                    if (ch != 42) {
                                        break;
                                    }
                                    read = this.in.read();
                                }
                                if (ch == 47) {
                                    ch = read();
                                    break;
                                }
                            }
                        }
                    } else {
                        this.readAheadCh = ch;
                        ch = 47;
                    }
                }
            }
        }
        return ch;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        return new StripJavaComments(rdr);
    }
}
