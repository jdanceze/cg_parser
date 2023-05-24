package javassist.scopedpool;

import javassist.ClassPool;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/scopedpool/ScopedClassPoolFactory.class */
public interface ScopedClassPoolFactory {
    ScopedClassPool create(ClassLoader classLoader, ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository);

    ScopedClassPool create(ClassPool classPool, ScopedClassPoolRepository scopedClassPoolRepository);
}
