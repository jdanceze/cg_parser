package org.powermock.core.classloader;

import org.powermock.core.transformers.ClassWrapper;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/classloader/ClassMarker.class */
public interface ClassMarker {
    <T> void mark(ClassWrapper<T> classWrapper);
}
