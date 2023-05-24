package org.hamcrest.generator;

import java.io.File;
import java.io.Reader;
import org.hamcrest.generator.qdox.JavaDocBuilder;
import org.hamcrest.generator.qdox.model.JavaClass;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/QDox.class */
public class QDox {
    private final JavaDocBuilder javaDocBuilder = new JavaDocBuilder();

    public void addSourceTree(File sourceDir) {
        this.javaDocBuilder.addSourceTree(sourceDir);
    }

    public void addSource(Reader reader) {
        this.javaDocBuilder.addSource(reader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JavaClass getClassByName(String className) {
        return this.javaDocBuilder.getClassByName(className);
    }
}
