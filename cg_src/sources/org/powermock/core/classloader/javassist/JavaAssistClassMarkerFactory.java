package org.powermock.core.classloader.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import org.powermock.core.classloader.ClassMarker;
import org.powermock.core.transformers.ClassWrapper;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/javassist/JavaAssistClassMarkerFactory.class */
public class JavaAssistClassMarkerFactory {
    JavaAssistClassMarkerFactory() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassMarker createClassMarker(ClassPool classPool) {
        return new InterfaceClassMarker(classPool);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/javassist/JavaAssistClassMarkerFactory$InterfaceClassMarker.class */
    public static class InterfaceClassMarker implements ClassMarker {
        private final ClassPool classPool;

        InterfaceClassMarker(ClassPool classPool) {
            this.classPool = classPool;
        }

        @Override // org.powermock.core.classloader.ClassMarker
        public <T> void mark(ClassWrapper<T> type) {
            T unwrapped = type.unwrap();
            if (unwrapped instanceof CtClass) {
                mark((CtClass) unwrapped);
            }
        }

        public void mark(CtClass type) {
            CtClass powerMockInterface = this.classPool.makeInterface("org.powermock.core.classloader.PowerMockModified");
            type.addInterface(powerMockInterface);
            powerMockInterface.detach();
        }
    }
}
