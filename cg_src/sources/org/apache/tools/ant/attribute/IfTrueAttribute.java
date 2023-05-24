package org.apache.tools.ant.attribute;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfTrueAttribute.class */
public class IfTrueAttribute extends BaseIfAttribute {

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/IfTrueAttribute$Unless.class */
    public static class Unless extends IfTrueAttribute {
        public Unless() {
            setPositive(false);
        }
    }

    @Override // org.apache.tools.ant.attribute.EnableAttribute
    public boolean isEnabled(UnknownElement el, String value) {
        return convertResult(Project.toBoolean(value));
    }
}
