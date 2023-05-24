package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TabsToSpaces.class */
public final class TabsToSpaces extends BaseParamFilterReader implements ChainableReader {
    private static final int DEFAULT_TAB_LENGTH = 8;
    private static final String TAB_LENGTH_KEY = "tablength";
    private int tabLength;
    private int spacesRemaining;

    public TabsToSpaces() {
        this.tabLength = 8;
        this.spacesRemaining = 0;
    }

    public TabsToSpaces(Reader in) {
        super(in);
        this.tabLength = 8;
        this.spacesRemaining = 0;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        int ch;
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        if (this.spacesRemaining > 0) {
            this.spacesRemaining--;
            ch = 32;
        } else {
            ch = this.in.read();
            if (ch == 9) {
                this.spacesRemaining = this.tabLength - 1;
                ch = 32;
            }
        }
        return ch;
    }

    public void setTablength(int tabLength) {
        this.tabLength = tabLength;
    }

    private int getTablength() {
        return this.tabLength;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        TabsToSpaces newFilter = new TabsToSpaces(rdr);
        newFilter.setTablength(getTablength());
        newFilter.setInitialized(true);
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if (param != null && TAB_LENGTH_KEY.equals(param.getName())) {
                    this.tabLength = Integer.parseInt(param.getValue());
                    return;
                }
            }
        }
    }
}
