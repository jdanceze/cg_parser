package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/FilenameSelector.class */
public class FilenameSelector extends BaseExtendSelector {
    public static final String NAME_KEY = "name";
    public static final String CASE_KEY = "casesensitive";
    public static final String NEGATE_KEY = "negate";
    public static final String REGEX_KEY = "regex";
    private String pattern = null;
    private String regex = null;
    private boolean casesensitive = true;
    private boolean negated = false;
    private RegularExpression reg;
    private Regexp expression;

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder("{filenameselector name: ");
        if (this.pattern != null) {
            buf.append(this.pattern);
        }
        if (this.regex != null) {
            buf.append(this.regex).append(" [as regular expression]");
        }
        buf.append(" negate: ").append(this.negated);
        buf.append(" casesensitive: ").append(this.casesensitive);
        buf.append("}");
        return buf.toString();
    }

    public void setName(String pattern) {
        String pattern2 = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        if (pattern2.endsWith(File.separator)) {
            pattern2 = pattern2 + SelectorUtils.DEEP_TREE_MATCH;
        }
        this.pattern = pattern2;
    }

    public void setRegex(String pattern) {
        this.regex = pattern;
        this.reg = null;
    }

    public void setCasesensitive(boolean casesensitive) {
        this.casesensitive = casesensitive;
    }

    public void setNegate(boolean negated) {
        this.negated = negated;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if ("name".equalsIgnoreCase(paramname)) {
                    setName(parameter.getValue());
                } else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    setCasesensitive(Project.toBoolean(parameter.getValue()));
                } else if (NEGATE_KEY.equalsIgnoreCase(paramname)) {
                    setNegate(Project.toBoolean(parameter.getValue()));
                } else if (REGEX_KEY.equalsIgnoreCase(paramname)) {
                    setRegex(parameter.getValue());
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.pattern == null && this.regex == null) {
            setError("The name or regex attribute is required");
        } else if (this.pattern != null && this.regex != null) {
            setError("Only one of name and regex attribute is allowed");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        if (this.pattern != null) {
            return SelectorUtils.matchPath(this.pattern, filename, this.casesensitive) == (!this.negated);
        }
        if (this.reg == null) {
            this.reg = new RegularExpression();
            this.reg.setPattern(this.regex);
            this.expression = this.reg.getRegexp(getProject());
        }
        int options = RegexpUtil.asOptions(this.casesensitive);
        return this.expression.matches(filename, options) == (!this.negated);
    }
}
