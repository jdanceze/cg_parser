package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableMethodParameter.class */
public class ImmutableMethodParameter extends BaseMethodParameter {
    @Nonnull
    protected final String type;
    @Nonnull
    protected final ImmutableSet<? extends ImmutableAnnotation> annotations;
    @Nullable
    protected final String name;
    private static final ImmutableConverter<ImmutableMethodParameter, MethodParameter> CONVERTER = new ImmutableConverter<ImmutableMethodParameter, MethodParameter>() { // from class: org.jf.dexlib2.immutable.ImmutableMethodParameter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        public boolean isImmutable(@Nonnull MethodParameter item) {
            return item instanceof ImmutableMethodParameter;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public ImmutableMethodParameter makeImmutable(@Nonnull MethodParameter item) {
            return ImmutableMethodParameter.of(item);
        }
    };

    public ImmutableMethodParameter(@Nonnull String type, @Nullable Set<? extends Annotation> annotations, @Nullable String name) {
        this.type = type;
        this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
        this.name = name;
    }

    public ImmutableMethodParameter(@Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable String name) {
        this.type = type;
        this.annotations = ImmutableUtils.nullToEmptySet(annotations);
        this.name = name;
    }

    public static ImmutableMethodParameter of(MethodParameter methodParameter) {
        if (methodParameter instanceof ImmutableMethodParameter) {
            return (ImmutableMethodParameter) methodParameter;
        }
        return new ImmutableMethodParameter(methodParameter.getType(), methodParameter.getAnnotations(), methodParameter.getName());
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // org.jf.dexlib2.iface.MethodParameter
    @Nonnull
    public Set<? extends Annotation> getAnnotations() {
        return this.annotations;
    }

    @Override // org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override // org.jf.dexlib2.base.BaseMethodParameter, org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        return null;
    }

    @Nonnull
    public static ImmutableList<ImmutableMethodParameter> immutableListOf(@Nullable Iterable<? extends MethodParameter> list) {
        return CONVERTER.toList(list);
    }
}
