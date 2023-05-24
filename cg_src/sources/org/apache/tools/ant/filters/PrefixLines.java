package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/PrefixLines.class */
public final class PrefixLines extends BaseParamFilterReader implements ChainableReader {
    private static final String PREFIX_KEY = "prefix";
    private String prefix;
    private String queuedData;

    public PrefixLines() {
        this.prefix = null;
        this.queuedData = null;
    }

    public PrefixLines(Reader in) {
        super(in);
        this.prefix = null;
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
        if (this.queuedData != null) {
            ch = this.queuedData.charAt(0);
            this.queuedData = this.queuedData.substring(1);
            if (this.queuedData.isEmpty()) {
                this.queuedData = null;
            }
        } else {
            this.queuedData = readLine();
            if (this.queuedData == null) {
                ch = -1;
            } else {
                if (this.prefix != null) {
                    this.queuedData = this.prefix + this.queuedData;
                }
                return read();
            }
        }
        return ch;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String getPrefix() {
        return this.prefix;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        PrefixLines newFilter = new PrefixLines(rdr);
        newFilter.setPrefix(getPrefix());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if (PREFIX_KEY.equals(param.getName())) {
                    this.prefix = param.getValue();
                    return;
                }
            }
        }
    }
}
