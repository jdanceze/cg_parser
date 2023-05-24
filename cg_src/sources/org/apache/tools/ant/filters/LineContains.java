package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/LineContains.class */
public final class LineContains extends BaseParamFilterReader implements ChainableReader {
    private static final String CONTAINS_KEY = "contains";
    private static final String NEGATE_KEY = "negate";
    private Vector<String> contains;
    private String line;
    private boolean negate;
    private boolean matchAny;

    public LineContains() {
        this.contains = new Vector<>();
        this.line = null;
        this.negate = false;
        this.matchAny = false;
    }

    public LineContains(Reader in) {
        super(in);
        this.contains = new Vector<>();
        this.line = null;
        this.negate = false;
        this.matchAny = false;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        int ch = -1;
        if (this.line != null) {
            ch = this.line.charAt(0);
            if (this.line.length() == 1) {
                this.line = null;
            } else {
                this.line = this.line.substring(1);
            }
        } else {
            int containsSize = this.contains.size();
            this.line = readLine();
            while (this.line != null) {
                boolean matches = true;
                for (int i = 0; i < containsSize; i++) {
                    String containsStr = this.contains.elementAt(i);
                    matches = this.line.contains(containsStr);
                    if (!matches) {
                        if (!this.matchAny) {
                            break;
                        }
                    } else if (this.matchAny) {
                        break;
                    }
                }
                if (matches ^ isNegated()) {
                    break;
                }
                this.line = readLine();
            }
            if (this.line != null) {
                return read();
            }
        }
        return ch;
    }

    public void addConfiguredContains(Contains contains) {
        this.contains.addElement(contains.getValue());
    }

    public void setNegate(boolean b) {
        this.negate = b;
    }

    public boolean isNegated() {
        return this.negate;
    }

    public void setMatchAny(boolean matchAny) {
        this.matchAny = matchAny;
    }

    public boolean isMatchAny() {
        return this.matchAny;
    }

    private void setContains(Vector<String> contains) {
        this.contains = contains;
    }

    private Vector<String> getContains() {
        return this.contains;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        LineContains newFilter = new LineContains(rdr);
        newFilter.setContains(getContains());
        newFilter.setNegate(isNegated());
        newFilter.setMatchAny(isMatchAny());
        return newFilter;
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if (CONTAINS_KEY.equals(param.getType())) {
                    this.contains.addElement(param.getValue());
                } else if ("negate".equals(param.getType())) {
                    setNegate(Project.toBoolean(param.getValue()));
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/LineContains$Contains.class */
    public static class Contains {
        private String value;

        public final void setValue(String contains) {
            this.value = contains;
        }

        public final String getValue() {
            return this.value;
        }
    }
}
