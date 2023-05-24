package org.hamcrest.generator.qdox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.generator.qdox.model.ClassLibrary;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaClassCache;
import org.hamcrest.generator.qdox.model.JavaPackage;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/JavaClassContext.class */
public class JavaClassContext implements Serializable {
    private ClassLibrary classLibrary;
    private JavaDocBuilder builder;
    private Map packageMap = new HashMap();
    private final JavaClassCache cache = new DefaultJavaClassCache();

    public JavaClassContext(JavaDocBuilder builder) {
        this.builder = builder;
    }

    public JavaClassContext(ClassLibrary classLibrary) {
        this.classLibrary = classLibrary;
    }

    public void setClassLibrary(ClassLibrary classLibrary) {
        this.classLibrary = classLibrary;
    }

    public ClassLibrary getClassLibrary() {
        return this.classLibrary;
    }

    public JavaClass getClassByName(String name) {
        JavaClass result = this.cache.getClassByName(name);
        if (result == null && this.builder != null) {
            result = this.builder.createBinaryClass(name);
            if (result == null) {
                result = this.builder.createSourceClass(name);
            }
            if (result == null) {
                result = this.builder.createUnknownClass(name);
            }
            if (result != null) {
                add(result);
                result.setJavaClassContext(this);
            }
        }
        return result;
    }

    public JavaClass[] getClasses() {
        return this.cache.getClasses();
    }

    public void add(JavaClass javaClass) {
        this.cache.putClassByName(javaClass.getFullyQualifiedName(), javaClass);
        JavaPackage jPackage = getPackageByName(javaClass.getPackageName());
        if (jPackage != null) {
            jPackage.addClass(javaClass);
        }
    }

    public void add(String fullyQualifiedClassName) {
        this.classLibrary.add(fullyQualifiedClassName);
    }

    public Class getClass(String name) {
        return this.classLibrary.getClass(name);
    }

    public JavaPackage getPackageByName(String name) {
        return (JavaPackage) this.packageMap.get(name);
    }

    public void add(JavaPackage jPackage) {
        String packageName = jPackage.getName();
        if (getPackageByName(packageName) == null) {
            JavaPackage javaPackage = new JavaPackage(packageName);
            javaPackage.setContext(this);
            this.packageMap.put(packageName, javaPackage);
        }
        jPackage.setContext(this);
    }

    public JavaPackage[] getPackages() {
        return (JavaPackage[]) this.packageMap.values().toArray(new JavaPackage[0]);
    }
}
