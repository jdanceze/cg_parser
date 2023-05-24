package org.apache.tools.ant.util;

import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/RegexpPatternMapper.class */
public class RegexpPatternMapper implements FileNameMapper {
    private static final int DECIMAL = 10;
    protected RegexpMatcher reg;
    protected char[] to = null;
    protected StringBuffer result = new StringBuffer();
    private boolean handleDirSep = false;
    private int regexpOptions = 0;

    public RegexpPatternMapper() throws BuildException {
        this.reg = null;
        this.reg = new RegexpMatcherFactory().newRegexpMatcher();
    }

    public void setHandleDirSep(boolean handleDirSep) {
        this.handleDirSep = handleDirSep;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.regexpOptions = RegexpUtil.asOptions(caseSensitive);
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String from) throws BuildException {
        if (from == null) {
            throw new BuildException("this mapper requires a 'from' attribute");
        }
        try {
            this.reg.setPattern(from);
        } catch (NoClassDefFoundError e) {
            throw new BuildException("Cannot load regular expression matcher", e);
        }
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String to) {
        if (to == null) {
            throw new BuildException("this mapper requires a 'to' attribute");
        }
        this.to = to.toCharArray();
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        if (sourceFileName == null) {
            return null;
        }
        if (this.handleDirSep && sourceFileName.contains("\\")) {
            sourceFileName = sourceFileName.replace('\\', '/');
        }
        if (this.reg == null || this.to == null || !this.reg.matches(sourceFileName, this.regexpOptions)) {
            return null;
        }
        return new String[]{replaceReferences(sourceFileName)};
    }

    protected String replaceReferences(String source) {
        List<String> v = this.reg.getGroups(source, this.regexpOptions);
        this.result.setLength(0);
        int i = 0;
        while (i < this.to.length) {
            if (this.to[i] == '\\') {
                i++;
                if (i < this.to.length) {
                    int value = Character.digit(this.to[i], 10);
                    if (value > -1) {
                        this.result.append(v.get(value));
                    } else {
                        this.result.append(this.to[i]);
                    }
                } else {
                    this.result.append('\\');
                }
            } else {
                this.result.append(this.to[i]);
            }
            i++;
        }
        return this.result.substring(0);
    }
}
