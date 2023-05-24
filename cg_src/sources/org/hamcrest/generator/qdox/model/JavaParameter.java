package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaParameter.class */
public class JavaParameter extends AbstractBaseJavaEntity implements Serializable {
    public static final JavaParameter[] EMPTY_ARRAY = new JavaParameter[0];
    private String name;
    private Type type;
    private JavaMethod parentMethod;
    private boolean varArgs;

    public JavaParameter(Type type, String name) {
        this(type, name, false);
    }

    public JavaParameter(Type type, String name, boolean varArgs) {
        this.name = name;
        this.type = type;
        this.varArgs = varArgs;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public String getName() {
        return this.name;
    }

    public Type getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        JavaParameter p = (JavaParameter) obj;
        return getType().equals(p.getType());
    }

    public int hashCode() {
        return getType().hashCode();
    }

    public JavaMethod getParentMethod() {
        return this.parentMethod;
    }

    public void setParentMethod(JavaMethod parentMethod) {
        this.parentMethod = parentMethod;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public JavaClass getParentClass() {
        return getParentMethod().getParentClass();
    }

    public boolean isVarArgs() {
        return this.varArgs;
    }

    public String toString() {
        return new StringBuffer().append(getResolvedValue()).append(Instruction.argsep).append(this.name).toString();
    }

    public String getResolvedValue() {
        return this.type.getResolvedValue(getParentMethod().getTypeParameters());
    }

    public String getResolvedGenericValue() {
        return this.type.getResolvedGenericValue(getParentMethod().getTypeParameters());
    }
}
