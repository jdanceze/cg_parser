package org.apache.tools.ant.attribute;

import java.util.Map;
import java.util.stream.Collectors;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/attribute/BaseIfAttribute.class */
public abstract class BaseIfAttribute extends ProjectComponent implements EnableAttribute {
    private boolean positive = true;

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    protected boolean isPositive() {
        return this.positive;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean convertResult(boolean val) {
        return this.positive == val;
    }

    protected Map<String, String> getParams(UnknownElement el) {
        return (Map) el.getWrapper().getAttributeMap().entrySet().stream().filter(e -> {
            return ((String) e.getKey()).startsWith("ant-attribute:param");
        }).collect(Collectors.toMap(e2 -> {
            return ((String) e2.getKey()).substring(((String) e2.getKey()).lastIndexOf(58) + 1);
        }, e3 -> {
            return el.getProject().replaceProperties((String) e3.getValue());
        }, a, b -> {
            return b;
        }));
    }
}
