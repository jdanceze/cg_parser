package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.LineTokenizer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TailFilter.class */
public final class TailFilter extends BaseParamFilterReader implements ChainableReader {
    private static final String LINES_KEY = "lines";
    private static final String SKIP_KEY = "skip";
    private static final int DEFAULT_NUM_LINES = 10;
    private long lines;
    private long skip;
    private boolean completedReadAhead;
    private LineTokenizer lineTokenizer;
    private String line;
    private int linePos;
    private LinkedList<String> lineList;

    public TailFilter() {
        this.lines = 10L;
        this.skip = 0L;
        this.completedReadAhead = false;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        this.lineList = new LinkedList<>();
    }

    public TailFilter(Reader in) {
        super(in);
        this.lines = 10L;
        this.skip = 0L;
        this.completedReadAhead = false;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        this.lineList = new LinkedList<>();
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
                this.line = tailFilter(this.line);
                if (this.line == null) {
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
        TailFilter newFilter = new TailFilter(rdr);
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
                    setLines(Long.parseLong(param.getValue()));
                } else if ("skip".equals(paramName)) {
                    this.skip = Long.parseLong(param.getValue());
                }
            }
        }
    }

    private String tailFilter(String line) {
        if (!this.completedReadAhead) {
            if (line != null) {
                this.lineList.add(line);
                if (this.lines == -1) {
                    if (this.lineList.size() > this.skip) {
                        return this.lineList.removeFirst();
                    }
                    return "";
                }
                long linesToKeep = this.lines + (this.skip > 0 ? this.skip : 0L);
                if (linesToKeep < this.lineList.size()) {
                    this.lineList.removeFirst();
                    return "";
                }
                return "";
            }
            this.completedReadAhead = true;
            if (this.skip > 0) {
                for (int i = 0; i < this.skip; i++) {
                    this.lineList.removeLast();
                }
            }
            if (this.lines > -1) {
                while (this.lineList.size() > this.lines) {
                    this.lineList.removeFirst();
                }
            }
        }
        if (this.lineList.size() > 0) {
            return this.lineList.removeFirst();
        }
        return null;
    }
}
