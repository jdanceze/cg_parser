package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaClassCache.class */
public interface JavaClassCache extends Serializable {
    JavaClass[] getClasses();

    JavaClass getClassByName(String str);

    void putClassByName(String str, JavaClass javaClass);
}
