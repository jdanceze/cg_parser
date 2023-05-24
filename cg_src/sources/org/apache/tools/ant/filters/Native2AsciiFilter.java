package org.apache.tools.ant.filters;

import org.apache.tools.ant.filters.TokenFilter;
import org.apache.tools.ant.util.Native2AsciiUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/Native2AsciiFilter.class */
public class Native2AsciiFilter extends TokenFilter.ChainableReaderFilter {
    private boolean reverse;

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override // org.apache.tools.ant.filters.TokenFilter.Filter
    public String filter(String line) {
        if (this.reverse) {
            return Native2AsciiUtils.ascii2native(line);
        }
        return Native2AsciiUtils.native2ascii(line);
    }
}
