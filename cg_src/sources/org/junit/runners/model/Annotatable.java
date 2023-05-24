package org.junit.runners.model;

import java.lang.annotation.Annotation;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/Annotatable.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/Annotatable.class */
public interface Annotatable {
    Annotation[] getAnnotations();

    <T extends Annotation> T getAnnotation(Class<T> cls);
}
