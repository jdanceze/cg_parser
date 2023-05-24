package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.LineTokenizer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/HeadFilter.class */
public final class HeadFilter extends BaseParamFilterReader implements ChainableReader {
    private static final String LINES_KEY = "lines";
    private static final String SKIP_KEY = "skip";
    private long linesRead;
    private static final int DEFAULT_NUM_LINES = 10;
    private long lines;
    private long skip;
    private LineTokenizer lineTokenizer;
    private String line;
    private int linePos;
    private boolean eof;

    public HeadFilter() {
        this.linesRead = 0L;
        this.lines = 10L;
        this.skip = 0L;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
    }

    public HeadFilter(Reader in) {
        super(in);
        this.linesRead = 0L;
        this.lines = 10L;
        this.skip = 0L;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        this.lineTokenizer = new LineTokenizer();
        this.lineTokenizer.setIncludeDelims(true);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        while (true) {
            if (this.line == null || this.line.isEmpty()) {
                this.line = this.lineTokenizer.getToken(this.in);
                if (this.line == null) {
                    return -1;
                }
                this.line = headFilter(this.line);
                if (this.eof) {
                    return -1;
                }
                this.linePos = 0;
            } else {
                int ch = this.line.charAt(this.linePos);
                this.linePos++;
                if (this.linePos == this.line.length()) {
                    this.line = null;
                }
                return ch;
            }
        }
    }

    public void setLines(long lines) {
        this.lines = lines;
    }

    private long getLines() {
        return this.lines;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    private long getSkip() {
        return this.skip;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        HeadFilter newFilter = new HeadFilter(rdr);
        newFilter.setLines(getLines());
        newFilter.setSkip(getSkip());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                String paramName = param.getName();
                if (LINES_KEY.equals(paramName)) {
                    this.lines = Long.parseLong(param.getValue());
                } else if ("skip".equals(paramName)) {
                    this.skip = Long.parseLong(param.getValue());
                }
            }
        }
    }

    private String headFilter(String line) {
        this.linesRead++;
        if (this.skip > 0 && this.linesRead - 1 < this.skip) {
            return null;
        }
        if (this.lines > 0 && this.linesRead > this.lines + this.skip) {
            this.eof = true;
            return null;
        }
        return line;
    }
}
