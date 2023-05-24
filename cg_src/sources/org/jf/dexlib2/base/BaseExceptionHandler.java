package org.jf.dexlib2.base;

import com.google.common.base.Objects;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/BaseExceptionHandler.class */
public abstract class BaseExceptionHandler implements ExceptionHandler {
    public static final Comparator<ExceptionHandler> BY_EXCEPTION = new Comparator<ExceptionHandler>() { // from class: org.jf.dexlib2.base.BaseExceptionHandler.2
        @Override // java.util.Comparator
        public int compare(ExceptionHandler o1, ExceptionHandler o2) {
            String exceptionType1 = o1.getExceptionType();
            if (exceptionType1 == null) {
                if (o2.getExceptionType() != null) {
                    return 1;
                }
                return 0;
            }
            String exceptionType2 = o2.getExceptionType();
            if (exceptionType2 == null) {
                return -1;
            }
            return exceptionType1.compareTo(o2.getExceptionType());
        }
    };

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    @Nullable
    public TypeReference getExceptionTypeReference() {
        final String exceptionType = getExceptionType();
        if (exceptionType == null) {
            return null;
        }
        return new BaseTypeReference() { // from class: org.jf.dexlib2.base.BaseExceptionHandler.1
            @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return exceptionType;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public int hashCode() {
        String exceptionType = getExceptionType();
        int hashCode = exceptionType == null ? 0 : exceptionType.hashCode();
        return (hashCode * 31) + getHandlerCodeAddress();
    }

    @Override // org.jf.dexlib2.iface.ExceptionHandler
    public boolean equals(@Nullable Object o) {
        if (o instanceof ExceptionHandler) {
            ExceptionHandler other = (ExceptionHandler) o;
            return Objects.equal(getExceptionType(), other.getExceptionType()) && getHandlerCodeAddress() == other.getHandlerCodeAddress();
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.ExceptionHandler, java.lang.Comparable
    public int compareTo(@Nonnull ExceptionHandler o) {
        String exceptionType = getExceptionType();
        if (exceptionType == null) {
            if (o.getExceptionType() != null) {
                return 1;
            }
        } else {
            String otherExceptionType = o.getExceptionType();
            if (otherExceptionType == null) {
                return -1;
            }
            int res = exceptionType.compareTo(o.getExceptionType());
            if (res != 0) {
                return res;
            }
        }
        return Ints.compare(getHandlerCodeAddress(), o.getHandlerCodeAddress());
    }
}
