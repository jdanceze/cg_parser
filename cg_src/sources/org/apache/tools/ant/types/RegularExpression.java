package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpFactory;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/RegularExpression.class */
public class RegularExpression extends DataType {
    public static final String DATA_TYPE_NAME = "regexp";
    private static final RegexpFactory FACTORY = new RegexpFactory();
    private String myPattern;
    private boolean alreadyInit = false;
    private Regexp regexp = null;
    private boolean setPatternPending = false;

    private void init(Project p) {
        if (!this.alreadyInit) {
            this.regexp = FACTORY.newRegexp(p);
            this.alreadyInit = true;
        }
    }

    private void setPattern() {
        if (this.setPatternPending) {
            this.regexp.setPattern(this.myPattern);
            this.setPatternPending = false;
        }
    }

    public void setPattern(String pattern) {
        if (this.regexp == null) {
            this.myPattern = pattern;
            this.setPatternPending = true;
            return;
        }
        this.regexp.setPattern(pattern);
    }

    public String getPattern(Project p) {
        init(p);
        if (isReference()) {
            return getRef(p).getPattern(p);
        }
        setPattern();
        return this.regexp.getPattern();
    }

    public Regexp getRegexp(Project p) {
        init(p);
        if (isReference()) {
            return getRef(p).getRegexp(p);
        }
        setPattern();
        return this.regexp;
    }

    public RegularExpression getRef(Project p) {
        return (RegularExpression) getCheckedRef(RegularExpression.class, getDataTypeName(), p);
    }
}
