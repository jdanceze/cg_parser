package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/selectors/Name.class */
public class Name implements ResourceSelector {
    private String pattern;
    private RegularExpression reg;
    private Regexp expression;
    private Project project;
    private String regex = null;
    private boolean cs = true;
    private boolean handleDirSep = false;

    public void setProject(Project p) {
        this.project = p;
    }

    public void setName(String n) {
        this.pattern = n;
    }

    public String getName() {
        return this.pattern;
    }

    public void setRegex(String r) {
        this.regex = r;
        this.reg = null;
    }

    public String getRegex() {
        return this.regex;
    }

    public void setCaseSensitive(boolean b) {
        this.cs = b;
    }

    public boolean isCaseSensitive() {
        return this.cs;
    }

    public void setHandleDirSep(boolean handleDirSep) {
        this.handleDirSep = handleDirSep;
    }

    public boolean doesHandledirSep() {
        return this.handleDirSep;
    }

    @Override // org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource r) {
        String n = r.getName();
        if (matches(n)) {
            return true;
        }
        String s = r.toString();
        return !s.equals(n) && matches(s);
    }

    private boolean matches(String name) {
        if (this.pattern != null) {
            return SelectorUtils.match(modify(this.pattern), modify(name), this.cs);
        }
        if (this.reg == null) {
            this.reg = new RegularExpression();
            this.reg.setPattern(this.regex);
            this.expression = this.reg.getRegexp(this.project);
        }
        return this.expression.matches(modify(name), RegexpUtil.asOptions(this.cs));
    }

    private String modify(String s) {
        if (s == null || !this.handleDirSep || !s.contains("\\")) {
            return s;
        }
        return s.replace('\\', '/');
    }
}
