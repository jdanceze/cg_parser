package org.apache.tools.ant.attribute;

import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfBlankAttribute.class */
public class IfBlankAttribute extends BaseIfAttribute {

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfBlankAttribute$Unless.class */
    public static class Unless extends IfBlankAttribute {
        public Unless() {
            setPositive(false);
        }
    }

    @Override // org.apache.tools.ant.attribute.EnableAttribute
    public boolean isEnabled(UnknownElement el, String value) {
        return convertResult(value == null || value.isEmpty());
    }
}
