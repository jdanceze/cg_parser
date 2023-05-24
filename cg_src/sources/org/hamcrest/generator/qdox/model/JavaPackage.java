package org.hamcrest.generator.qdox.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hamcrest.generator.qdox.JavaClassContext;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaPackage.class */
public class JavaPackage extends AbstractBaseJavaEntity {
    private JavaClassContext context;
    private String name;
    private Annotation[] annotations;
    private int lineNumber;
    private List classes;

    public JavaPackage() {
        this.annotations = new Annotation[0];
        this.lineNumber = -1;
        this.classes = new ArrayList();
    }

    public JavaPackage(String name) {
        this(name, null);
    }

    public JavaPackage(String name, Map allPackages) {
        this.annotations = new Annotation[0];
        this.lineNumber = -1;
        this.classes = new ArrayList();
        this.name = name;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public String getName() {
        return this.name;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setName(String name) {
        this.name = name;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setContext(JavaClassContext context) {
        this.context = context;
    }

    public void addClass(JavaClass clazz) {
        clazz.setJavaPackage(this);
        this.classes.add(clazz);
    }

    public JavaClass[] getClasses() {
        if (this == this.context.getPackageByName(this.name)) {
            return (JavaClass[]) this.classes.toArray(new JavaClass[this.classes.size()]);
        }
        return this.context.getPackageByName(this.name).getClasses();
    }

    public JavaPackage getParentPackage() {
        String parentName = this.name.substring(0, this.name.lastIndexOf("."));
        return this.context.getPackageByName(parentName);
    }

    public JavaPackage[] getSubPackages() {
        String expected = new StringBuffer().append(this.name).append(".").toString();
        JavaPackage[] jPackages = this.context.getPackages();
        List retList = new ArrayList();
        for (JavaPackage javaPackage : jPackages) {
            String pName = javaPackage.getName();
            if (pName.startsWith(expected) && pName.substring(expected.length()).indexOf(".") <= -1) {
                retList.add(this.context.getPackageByName(pName));
            }
        }
        return (JavaPackage[]) retList.toArray(new JavaPackage[retList.size()]);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaPackage that = (JavaPackage) o;
        return this.name.equals(that.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return new StringBuffer().append("package ").append(this.name).toString();
    }
}
