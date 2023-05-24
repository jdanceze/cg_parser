package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/LineContainsRegExp.class */
public final class LineContainsRegExp extends BaseParamFilterReader implements ChainableReader {
    private static final String REGEXP_KEY = "regexp";
    private static final String NEGATE_KEY = "negate";
    private static final String CS_KEY = "casesensitive";
    private Vector<RegularExpression> regexps;
    private String line;
    private boolean negate;
    private int regexpOptions;

    public LineContainsRegExp() {
        this.regexps = new Vector<>();
        this.line = null;
        this.negate = false;
        this.regexpOptions = 0;
    }

    public LineContainsRegExp(Reader in) {
        super(in);
        this.regexps = new Vector<>();
        this.line = null;
        this.negate = false;
        this.regexpOptions = 0;
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
            this.line = readLine();
            while (this.line != null) {
                boolean matches = true;
                Iterator<RegularExpression> it = this.regexps.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RegularExpression regexp = it.next();
                    if (!regexp.getRegexp(getProject()).matches(this.line, this.regexpOptions)) {
                        matches = false;
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

    public void addConfiguredRegexp(RegularExpression regExp) {
        this.regexps.addElement(regExp);
    }

    private void setRegexps(Vector<RegularExpression> regexps) {
        this.regexps = regexps;
    }

    private Vector<RegularExpression> getRegexps() {
        return this.regexps;
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        LineContainsRegExp newFilter = new LineContainsRegExp(rdr);
        newFilter.setRegexps(getRegexps());
        newFilter.setNegate(isNegated());
        newFilter.setCaseSensitive(!RegexpUtil.hasFlag(this.regexpOptions, 256));
        return newFilter;
    }

    public void setNegate(boolean b) {
        this.negate = b;
    }

    public void setCaseSensitive(boolean b) {
        this.regexpOptions = RegexpUtil.asOptions(b);
    }

    public boolean isNegated() {
        return this.negate;
    }

    public void setRegexp(String pattern) {
        RegularExpression regexp = new RegularExpression();
        regexp.setPattern(pattern);
        this.regexps.addElement(regexp);
    }

    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (Parameter param : params) {
                if ("regexp".equals(param.getType())) {
                    setRegexp(param.getValue());
                } else if ("negate".equals(param.getType())) {
                    setNegate(Project.toBoolean(param.getValue()));
                } else if ("casesensitive".equals(param.getType())) {
                    setCaseSensitive(Project.toBoolean(param.getValue()));
                }
            }
        }
    }
}
