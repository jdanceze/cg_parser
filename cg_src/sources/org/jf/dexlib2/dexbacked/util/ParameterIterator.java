package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/ParameterIterator.class */
public class ParameterIterator implements Iterator<MethodParameter> {
    private final Iterator<? extends CharSequence> parameterTypes;
    private final Iterator<? extends Set<? extends Annotation>> parameterAnnotations;
    private final Iterator<String> parameterNames;

    public ParameterIterator(@Nonnull List<? extends CharSequence> parameterTypes, @Nonnull List<? extends Set<? extends Annotation>> parameterAnnotations, @Nonnull Iterator<String> parameterNames) {
        this.parameterTypes = parameterTypes.iterator();
        this.parameterAnnotations = parameterAnnotations.iterator();
        this.parameterNames = parameterNames;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.parameterTypes.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Iterator
    public MethodParameter next() {
        Set<? extends Annotation> annotations;
        String name;
        final String type = this.parameterTypes.next().toString();
        if (this.parameterAnnotations.hasNext()) {
            annotations = this.parameterAnnotations.next();
        } else {
            annotations = ImmutableSet.of();
        }
        if (this.parameterNames.hasNext()) {
            name = this.parameterNames.next();
        } else {
            name = null;
        }
        final Set<? extends Annotation> set = annotations;
        final String str = name;
        return new BaseMethodParameter() { // from class: org.jf.dexlib2.dexbacked.util.ParameterIterator.1
            @Override // org.jf.dexlib2.iface.MethodParameter
            @Nonnull
            public Set<? extends Annotation> getAnnotations() {
                return set;
            }

            @Override // org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getName() {
                return str;
            }

            @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
            @Nonnull
            public String getType() {
                return type;
            }
        };
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
