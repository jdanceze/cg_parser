package org.apache.tools.ant.types.selectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/ContainsSelector.class */
public class ContainsSelector extends BaseExtendSelector implements ResourceSelector {
    public static final String EXPRESSION_KEY = "expression";
    public static final String CONTAINS_KEY = "text";
    public static final String CASE_KEY = "casesensitive";
    public static final String WHITESPACE_KEY = "ignorewhitespace";
    private String contains = null;
    private boolean casesensitive = true;
    private boolean ignorewhitespace = false;
    private String encoding = null;

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return String.format("{containsselector text: \"%s\" casesensitive: %s ignorewhitespace: %s}", this.contains, Boolean.valueOf(this.casesensitive), Boolean.valueOf(this.ignorewhitespace));
    }

    public void setText(String contains) {
        this.contains = contains;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setCasesensitive(boolean casesensitive) {
        this.casesensitive = casesensitive;
    }

    public void setIgnorewhitespace(boolean ignorewhitespace) {
        this.ignorewhitespace = ignorewhitespace;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if ("text".equalsIgnoreCase(paramname)) {
                    setText(parameter.getValue());
                } else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    setCasesensitive(Project.toBoolean(parameter.getValue()));
                } else if (WHITESPACE_KEY.equalsIgnoreCase(paramname)) {
                    setIgnorewhitespace(Project.toBoolean(parameter.getValue()));
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.contains == null) {
            setError("The text attribute is required");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        return isSelected(new FileResource(file));
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector, org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        validate();
        if (r.isDirectory() || this.contains.isEmpty()) {
            return true;
        }
        String userstr = this.contains;
        if (!this.casesensitive) {
            userstr = this.contains.toLowerCase();
        }
        if (this.ignorewhitespace) {
            userstr = SelectorUtils.removeWhitespace(userstr);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(r.getInputStream(), this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding)));
            try {
                String teststr = in.readLine();
                while (teststr != null) {
                    if (!this.casesensitive) {
                        teststr = teststr.toLowerCase();
                    }
                    if (this.ignorewhitespace) {
                        teststr = SelectorUtils.removeWhitespace(teststr);
                    }
                    if (!teststr.contains(userstr)) {
                        teststr = in.readLine();
                    } else {
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
