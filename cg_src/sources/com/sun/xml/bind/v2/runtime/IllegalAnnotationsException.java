package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.v2.model.core.ErrorHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/IllegalAnnotationsException.class */
public class IllegalAnnotationsException extends JAXBException {
    private final List<IllegalAnnotationException> errors;
    private static final long serialVersionUID = 1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IllegalAnnotationsException.class.desiredAssertionStatus();
    }

    public IllegalAnnotationsException(List<IllegalAnnotationException> errors) {
        super(errors.size() + " counts of IllegalAnnotationExceptions");
        if (!$assertionsDisabled && errors.isEmpty()) {
            throw new AssertionError("there must be at least one error");
        }
        this.errors = Collections.unmodifiableList(new ArrayList(errors));
    }

    @Override // javax.xml.bind.JAXBException, java.lang.Throwable
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('\n');
        for (IllegalAnnotationException error : this.errors) {
            sb.append(error.toString()).append('\n');
        }
        return sb.toString();
    }

    public List<IllegalAnnotationException> getErrors() {
        return this.errors;
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/IllegalAnnotationsException$Builder.class */
    public static class Builder implements ErrorHandler {
        private final List<IllegalAnnotationException> list = new ArrayList();

        @Override // com.sun.xml.bind.v2.model.core.ErrorHandler
        public void error(IllegalAnnotationException e) {
            this.list.add(e);
        }

        public void check() throws IllegalAnnotationsException {
            if (this.list.isEmpty()) {
                return;
            }
            throw new IllegalAnnotationsException(this.list);
        }
    }
}
