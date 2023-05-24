package org.mockito.plugins;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/AnnotationEngine.class */
public interface AnnotationEngine {
    AutoCloseable process(Class<?> cls, Object obj);

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/AnnotationEngine$NoAction.class */
    public static class NoAction implements AutoCloseable {
        @Override // java.lang.AutoCloseable
        public void close() {
        }
    }
}
