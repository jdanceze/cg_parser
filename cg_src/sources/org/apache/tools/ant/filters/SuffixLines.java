package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/SuffixLines.class */
public final class SuffixLines extends BaseParamFilterReader implements ChainableReader {
    private static final String SUFFIX_KEY = "suffix";
    private String suffix;
    private String queuedData;

    public SuffixLines() {
        this.suffix = null;
        this.queuedData = null;
    }

    public SuffixLines(Reader in) {
        super(in);
        this.suffix = null;
        this.queuedData = null;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch;
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        if (this.queuedData != null && this.queuedData.isEmpty()) {
            this.queuedData = null;
        }
        if (this.queuedData == null) {
            this.queuedData = readLine();
            if (this.queuedData == null) {
                ch = -1;
            } else {
                if (this.suffix != null) {
                    String lf = "";
                    if (this.queuedData.endsWith("\r\n")) {
                        lf = "\r\n";
                    } else if (this.queuedData.endsWith("\n")) {
                        lf = "\n";
                    }
                    this.queuedData = this.queuedData.substring(0, this.queuedData.length() - lf.length()) + this.suffix + lf;
                }
                return read();
            }
        } else {
            ch = this.queuedData.charAt(0);
            this.queuedData = this.queuedData.substring(1);
            if (this.queuedData.isEmpty()) {
                this.queuedData = null;
            }
        }
        return ch;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private String getSuffix() {
        return this.suffix;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        SuffixLines newFilter = new SuffixLines(rdr);
        newFilter.setSuffix(getSuffix());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if (SUFFIX_KEY.equals(param.getName())) {
                    this.suffix = param.getValue();
                    return;
                }
            }
        }
    }
}
