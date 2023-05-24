package org.hamcrest.generator.qdox;

import java.util.Hashtable;
import java.util.Map;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaClassCache;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/DefaultJavaClassCache.class */
public class DefaultJavaClassCache implements JavaClassCache {
    private Map classes = new Hashtable();

    @Override // org.hamcrest.generator.qdox.model.JavaClassCache
    public JavaClass getClassByName(String name) {
        return (JavaClass) this.classes.get(name);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassCache
    public JavaClass[] getClasses() {
        return (JavaClass[]) this.classes.values().toArray(new JavaClass[0]);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassCache
    public void putClassByName(String name, JavaClass javaClass) {
        this.classes.put(name, javaClass);
    }
}
