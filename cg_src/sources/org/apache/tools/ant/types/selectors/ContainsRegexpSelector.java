package org.apache.tools.ant.types.selectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/ContainsRegexpSelector.class */
public class ContainsRegexpSelector extends BaseExtendSelector implements ResourceSelector {
    public static final String EXPRESSION_KEY = "expression";
    private static final String CS_KEY = "casesensitive";
    private static final String ML_KEY = "multiline";
    private static final String SL_KEY = "singleline";
    private String userProvidedExpression = null;
    private RegularExpression myRegExp = null;
    private Regexp myExpression = null;
    private boolean caseSensitive = true;
    private boolean multiLine = false;
    private boolean singleLine = false;

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return String.format("{containsregexpselector expression: %s}", this.userProvidedExpression);
    }

    public void setExpression(String theexpression) {
        this.userProvidedExpression = theexpression;
    }

    public void setCaseSensitive(boolean b) {
        this.caseSensitive = b;
    }

    public void setMultiLine(boolean b) {
        this.multiLine = b;
    }

    public void setSingleLine(boolean b) {
        this.singleLine = b;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if ("expression".equalsIgnoreCase(paramname)) {
                    setExpression(parameter.getValue());
                } else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    setCaseSensitive(Project.toBoolean(parameter.getValue()));
                } else if (ML_KEY.equalsIgnoreCase(paramname)) {
                    setMultiLine(Project.toBoolean(parameter.getValue()));
                } else if (SL_KEY.equalsIgnoreCase(paramname)) {
                    setSingleLine(Project.toBoolean(parameter.getValue()));
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.userProvidedExpression == null) {
            setError("The expression attribute is required");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        return isSelected(new FileResource(file));
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector, org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        validate();
        if (r.isDirectory()) {
            return true;
        }
        if (this.myRegExp == null) {
            this.myRegExp = new RegularExpression();
            this.myRegExp.setPattern(this.userProvidedExpression);
            this.myExpression = this.myRegExp.getRegexp(getProject());
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(r.getInputStream()));
            try {
                for (String teststr = in.readLine(); teststr != null; teststr = in.readLine()) {
                    if (this.myExpression.matches(teststr, RegexpUtil.asOptions(this.caseSensitive, this.multiLine, this.singleLine))) {
                        in.close();
                        return true;
                    }
                }
                in.close();
                return false;
            } catch (IOException e) {
                throw new BuildException("Could not read " + r.toLongString());
            }
        } catch (IOException e2) {
            throw new BuildException("Could not get InputStream from " + r.toLongString(), e2);
        }
    }
}
