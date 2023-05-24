package org.hamcrest.generator.qdox.model;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaField.class */
public class JavaField extends AbstractJavaEntity implements Member {
    private Type type;
    private String initializationExpression;

    public JavaField() {
    }

    public JavaField(String name) {
        setName(name);
    }

    public JavaField(Type type, String name) {
        setType(type);
        setName(name);
    }

    public Type getType() {
        return this.type;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    protected void writeBody(IndentBuffer result) {
        writeAllModifiers(result);
        result.write(this.type.toString());
        result.write(' ');
        result.write(this.name);
        if (this.initializationExpression != null && this.initializationExpression.length() > 0) {
            result.write(" = ");
            result.write(this.initializationExpression);
        }
        result.write(';');
        result.newline();
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        return getName().compareTo(((JavaField) o).getName());
    }

    @Override // org.hamcrest.generator.qdox.model.Member
    public String getDeclarationSignature(boolean withModifiers) {
        IndentBuffer result = new IndentBuffer();
        if (withModifiers) {
            writeAllModifiers(result);
        }
        result.write(this.type.toString());
        result.write(' ');
        result.write(this.name);
        return result.toString();
    }

    @Override // org.hamcrest.generator.qdox.model.Member
    public String getCallSignature() {
        return getName();
    }

    public String getInitializationExpression() {
        return this.initializationExpression;
    }

    public void setInitializationExpression(String initializationExpression) {
        this.initializationExpression = initializationExpression;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if (isPrivate()) {
            result.append("private ");
        } else if (isProtected()) {
            result.append("protected ");
        } else if (isPublic()) {
            result.append("public ");
        }
        if (isStatic()) {
            result.append("static ");
        }
        if (isFinal()) {
            result.append("final ");
        }
        if (isTransient()) {
            result.append("transient ");
        }
        if (isVolatile()) {
            result.append("volatile ");
        }
        result.append(new StringBuffer().append(getType().getValue()).append(Instruction.argsep).toString());
        result.append(new StringBuffer().append(getParentClass().getFullyQualifiedName()).append(".").append(getName()).toString());
        return result.toString();
    }
}
