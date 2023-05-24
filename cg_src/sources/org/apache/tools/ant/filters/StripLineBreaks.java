package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/StripLineBreaks.class */
public final class StripLineBreaks extends BaseParamFilterReader implements ChainableReader {
    private static final String DEFAULT_LINE_BREAKS = "\r\n";
    private static final String LINE_BREAKS_KEY = "linebreaks";
    private String lineBreaks;

    public StripLineBreaks() {
        this.lineBreaks = "\r\n";
    }

    public StripLineBreaks(Reader in) {
        super(in);
        this.lineBreaks = "\r\n";
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch;
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        int read = this.in.read();
        while (true) {
            ch = read;
            if (ch == -1 || this.lineBreaks.indexOf(ch) == -1) {
                break;
            }
            read = this.in.read();
        }
        return ch;
    }

    public void setLineBreaks(String lineBreaks) {
        this.lineBreaks = lineBreaks;
    }

    private String getLineBreaks() {
        return this.lineBreaks;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        StripLineBreaks newFilter = new StripLineBreaks(rdr);
        newFilter.setLineBreaks(getLineBreaks());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        String userDefinedLineBreaks = null;
        Parameter[] params = getParameters();
        if (params != null) {
            int length = params.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Parameter param = params[i];
                if (!LINE_BREAKS_KEY.equals(param.getName())) {
                    i++;
                } else {
                    userDefinedLineBreaks = param.getValue();
                    break;
                }
            }
        }
        if (userDefinedLineBreaks != null) {
            this.lineBreaks = userDefinedLineBreaks;
        }
    }
}
