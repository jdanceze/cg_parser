package org.apache.tools.ant.util;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/GlobPatternMapper.class */
public class GlobPatternMapper implements FileNameMapper {
    protected int prefixLength;
    protected int postfixLength;
    protected String fromPrefix = null;
    protected String fromPostfix = null;
    protected String toPrefix = null;
    protected String toPostfix = null;
    private boolean fromContainsStar = false;
    private boolean toContainsStar = false;
    private boolean handleDirSep = false;
    private boolean caseSensitive = true;

    public void setHandleDirSep(boolean handleDirSep) {
        this.handleDirSep = handleDirSep;
    }

    public boolean getHandleDirSep() {
        return this.handleDirSep;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setFrom(String from) {
        if (from == null) {
            throw new BuildException("this mapper requires a 'from' attribute");
        }
        int index = from.lastIndexOf(42);
        if (index < 0) {
            this.fromPrefix = from;
            this.fromPostfix = "";
        } else {
            this.fromPrefix = from.substring(0, index);
            this.fromPostfix = from.substring(index + 1);
            this.fromContainsStar = true;
        }
        this.prefixLength = this.fromPrefix.length();
        this.postfixLength = this.fromPostfix.length();
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public void setTo(String to) {
        if (to == null) {
            throw new BuildException("this mapper requires a 'to' attribute");
        }
        int index = to.lastIndexOf(42);
        if (index < 0) {
            this.toPrefix = to;
            this.toPostfix = "";
            return;
        }
        this.toPrefix = to.substring(0, index);
        this.toPostfix = to.substring(index + 1);
        this.toContainsStar = true;
    }

    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        String str;
        if (sourceFileName == null) {
            return null;
        }
        String modName = modifyName(sourceFileName);
        if (this.fromPrefix != null && sourceFileName.length() >= this.prefixLength + this.postfixLength) {
            if (this.fromContainsStar || modName.equals(modifyName(this.fromPrefix))) {
                if (this.fromContainsStar && (!modName.startsWith(modifyName(this.fromPrefix)) || !modName.endsWith(modifyName(this.fromPostfix)))) {
                    return null;
                }
                String[] strArr = new String[1];
                StringBuilder append = new StringBuilder().append(this.toPrefix);
                if (this.toContainsStar) {
                    str = extractVariablePart(sourceFileName) + this.toPostfix;
                } else {
                    str = "";
                }
                strArr[0] = append.append(str).toString();
                return strArr;
            }
            return null;
        }
        return null;
    }

    protected String extractVariablePart(String name) {
        return name.substring(this.prefixLength, name.length() - this.postfixLength);
    }

    private String modifyName(String name) {
        if (!this.caseSensitive) {
            name = name.toLowerCase();
        }
        if (this.handleDirSep && name.contains("\\")) {
            name = name.replace('\\', '/');
        }
        return name;
    }
}
