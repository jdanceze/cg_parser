package com.sun.xml.bind.v2.model.annotation;

import com.sun.xml.bind.v2.model.core.ErrorHandler;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import java.lang.annotation.Annotation;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/annotation/AbstractInlineAnnotationReaderImpl.class */
public abstract class AbstractInlineAnnotationReaderImpl<T, C, F, M> implements AnnotationReader<T, C, F, M> {
    private ErrorHandler errorHandler;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract String fullName(M m);

    static {
        $assertionsDisabled = !AbstractInlineAnnotationReaderImpl.class.desiredAssertionStatus();
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler == null) {
            throw new IllegalArgumentException();
        }
        this.errorHandler = errorHandler;
    }

    public final ErrorHandler getErrorHandler() {
        if ($assertionsDisabled || this.errorHandler != null) {
            return this.errorHandler;
        }
        throw new AssertionError("error handler must be set before use");
    }

    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public final <A extends Annotation> A getMethodAnnotation(Class<A> annotation, M getter, M setter, Locatable srcPos) {
        A a1 = (A) (getter == null ? null : getMethodAnnotation(annotation, getter, srcPos));
        A a2 = (A) (setter == null ? null : getMethodAnnotation(annotation, setter, srcPos));
        if (a1 == null) {
            if (a2 == null) {
                return null;
            }
            return a2;
        } else if (a2 == null) {
            return a1;
        } else {
            getErrorHandler().error(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(annotation.getName(), fullName(getter), fullName(setter)), a1, a2));
            return a1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.bind.v2.model.annotation.AnnotationReader
    public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, String propertyName, M getter, M setter, Locatable srcPos) {
        boolean x = getter != null && hasMethodAnnotation(annotation, getter);
        boolean y = setter != null && hasMethodAnnotation(annotation, setter);
        if (x && y) {
            getMethodAnnotation(annotation, getter, setter, srcPos);
        }
        return x || y;
    }
}
