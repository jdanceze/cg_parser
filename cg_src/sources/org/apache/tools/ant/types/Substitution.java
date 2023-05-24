package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Substitution.class */
public class Substitution extends DataType {
    public static final String DATA_TYPE_NAME = "substitution";
    private String expression = null;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression(Project p) {
        if (isReference()) {
            return getRef(p).getExpression(p);
        }
        return this.expression;
    }

    public Substitution getRef(Project p) {
        return (Substitution) getCheckedRef(Substitution.class, getDataTypeName(), p);
    }
}
