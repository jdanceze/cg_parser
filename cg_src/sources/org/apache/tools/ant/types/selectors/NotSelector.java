package org.apache.tools.ant.types.selectors;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/NotSelector.class */
public class NotSelector extends NoneSelector {
    public NotSelector() {
    }

    public NotSelector(FileSelector other) {
        this();
        appendSelector(other);
    }

    @Override // org.apache.tools.ant.types.selectors.NoneSelector, org.apache.tools.ant.types.selectors.BaseSelectorContainer, org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (hasSelectors()) {
            buf.append("{notselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (selectorCount() != 1) {
            setError("One and only one selector is allowed within the <not> tag");
        }
    }
}
