package org.hamcrest.generator.qdox.model;

import org.hamcrest.generator.qdox.JavaClassContext;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaClassParent.class */
public interface JavaClassParent {
    String resolveType(String str);

    JavaClassContext getJavaClassContext();

    String getClassNamePrefix();

    JavaSource getParentSource();

    void addClass(JavaClass javaClass);

    JavaClass getNestedClassByName(String str);
}
