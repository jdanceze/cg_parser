package org.apache.tools.ant.filters;

import org.apache.tools.ant.filters.TokenFilter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/UniqFilter.class */
public class UniqFilter extends TokenFilter.ChainableReaderFilter {
    private String lastLine = null;

    @Override // org.apache.tools.ant.filters.TokenFilter.Filter
    public String filter(String string) {
        if (this.lastLine == null || !this.lastLine.equals(string)) {
            this.lastLine = string;
            return this.lastLine;
        }
        return null;
    }
}
