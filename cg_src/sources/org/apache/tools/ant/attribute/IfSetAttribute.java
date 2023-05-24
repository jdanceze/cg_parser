package org.apache.tools.ant.attribute;

import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfSetAttribute.class */
public class IfSetAttribute extends BaseIfAttribute {

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfSetAttribute$Unless.class */
    public static class Unless extends IfSetAttribute {
        public Unless() {
            setPositive(false);
        }
    }

    @Override // org.apache.tools.ant.attribute.EnableAttribute
    public boolean isEnabled(UnknownElement el, String value) {
        return convertResult(getProject().getProperty(value) != null);
    }
}
