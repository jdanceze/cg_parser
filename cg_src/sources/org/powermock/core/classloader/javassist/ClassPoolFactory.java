package org.powermock.core.classloader.javassist;

import javassist.ClassClassPath;
import javassist.ClassPool;
import org.powermock.core.classloader.annotations.UseClassPathAdjuster;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/javassist/ClassPoolFactory.class */
public class ClassPoolFactory {
    private UseClassPathAdjuster useClassPathAdjuster;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassPoolFactory(UseClassPathAdjuster useClassPathAdjuster) {
        this.useClassPathAdjuster = useClassPathAdjuster;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassPool create() {
        ClassPool classPool = new ClassPool();
        classPool.appendClassPath(new ClassClassPath(getClass()));
        if (this.useClassPathAdjuster != null) {
            try {
                Class<? extends ClassPathAdjuster> value = this.useClassPathAdjuster.value();
                ClassPathAdjuster classPathAdjuster = value.newInstance();
                classPathAdjuster.adjustClassPath(classPool);
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating class path adjuster", e);
            }
        }
        return classPool;
    }
}
