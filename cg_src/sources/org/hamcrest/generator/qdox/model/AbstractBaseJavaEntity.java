package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/AbstractBaseJavaEntity.class */
public class AbstractBaseJavaEntity implements Serializable {
    protected String name;
    private Annotation[] annotations = new Annotation[0];
    private int lineNumber = -1;
    protected JavaClassParent parent;

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getName() {
        return this.name;
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public JavaClassParent getParent() {
        return this.parent;
    }

    public void setParent(JavaClassParent parent) {
        this.parent = parent;
    }

    public JavaClass getParentClass() {
        return null;
    }
}
