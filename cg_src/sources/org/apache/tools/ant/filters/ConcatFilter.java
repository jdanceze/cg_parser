package org.apache.tools.ant.filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/ConcatFilter.class */
public final class ConcatFilter extends BaseParamFilterReader implements ChainableReader {
    private File prepend;
    private File append;
    private Reader prependReader;
    private Reader appendReader;

    public ConcatFilter() {
        this.prependReader = null;
        this.appendReader = null;
    }

    public ConcatFilter(Reader in) {
        super(in);
        this.prependReader = null;
        this.appendReader = null;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        int ch = -1;
        if (this.prependReader != null) {
            ch = this.prependReader.read();
            if (ch == -1) {
                this.prependReader.close();
                this.prependReader = null;
            }
        }
        if (ch == -1) {
            ch = super.read();
        }
        if (ch == -1 && this.appendReader != null) {
            ch = this.appendReader.read();
            if (ch == -1) {
                this.appendReader.close();
                this.appendReader = null;
            }
        }
        return ch;
    }

    public void setPrepend(File prepend) {
        this.prepend = prepend;
    }

    public File getPrepend() {
        return this.prepend;
    }

    public void setAppend(File append) {
        this.append = append;
    }

    public File getAppend() {
        return this.append;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        ConcatFilter newFilter = new ConcatFilter(rdr);
        newFilter.setPrepend(getPrepend());
        newFilter.setAppend(getAppend());
        return newFilter;
    }

    private void initialize() throws IOException {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                String paramName = param.getName();
                if ("prepend".equals(paramName)) {
                    setPrepend(new File(param.getValue()));
                } else if ("append".equals(paramName)) {
                    setAppend(new File(param.getValue()));
                }
            }
        }
        if (this.prepend != null) {
            if (!this.prepend.isAbsolute()) {
                this.prepend = new File(getProject().getBaseDir(), this.prepend.getPath());
            }
            this.prependReader = new BufferedReader(new FileReader(this.prepend));
        }
        if (this.append != null) {
            if (!this.append.isAbsolute()) {
                this.append = new File(getProject().getBaseDir(), this.append.getPath());
            }
            this.appendReader = new BufferedReader(new FileReader(this.append));
        }
    }
}
