package org.powermock.core.transformers;

import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/MethodSignatureWriter.class */
public interface MethodSignatureWriter<T> {
    String signatureFor(T t);

    String signatureForReflection(Method method);
}
