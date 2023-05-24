package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.writer.MethodSection;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodPool.class */
public class BuilderMethodPool extends BaseBuilderPool implements MethodSection<BuilderStringReference, BuilderTypeReference, BuilderMethodProtoReference, BuilderMethodReference, BuilderMethod> {
    @Nonnull
    private final ConcurrentMap<MethodReference, BuilderMethodReference> internedItems;

    public BuilderMethodPool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderMethodReference internMethod(@Nonnull MethodReference methodReference) {
        BuilderMethodReference ret = this.internedItems.get(methodReference);
        if (ret != null) {
            return ret;
        }
        BuilderMethodReference dexPoolMethodReference = new BuilderMethodReference(((BuilderTypePool) this.dexBuilder.typeSection).internType(methodReference.getDefiningClass()), ((BuilderStringPool) this.dexBuilder.stringSection).internString(methodReference.getName()), ((BuilderProtoPool) this.dexBuilder.protoSection).internMethodProto(methodReference));
        BuilderMethodReference ret2 = this.internedItems.putIfAbsent(dexPoolMethodReference, dexPoolMethodReference);
        return ret2 == null ? dexPoolMethodReference : ret2;
    }

    @Nonnull
    public BuilderMethodReference internMethod(@Nonnull String definingClass, @Nonnull String name, @Nonnull List<? extends CharSequence> parameters, @Nonnull String returnType) {
        return internMethod(new MethodKey(definingClass, name, parameters, returnType));
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public BuilderMethodReference getMethodReference(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.methodReference;
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public BuilderTypeReference getDefiningClass(@Nonnull BuilderMethodReference key) {
        return key.definingClass;
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public BuilderMethodProtoReference getPrototype(@Nonnull BuilderMethodReference key) {
        return key.proto;
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public BuilderMethodProtoReference getPrototype(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.methodReference.proto;
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public BuilderStringReference getName(@Nonnull BuilderMethodReference key) {
        return key.name;
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    public int getMethodIndex(@Nonnull BuilderMethod builderMethod) {
        return builderMethod.methodReference.index;
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderMethodReference key) {
        return key.index;
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderMethodReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderMethodReference>(this.internedItems.values()) { // from class: org.jf.dexlib2.writer.builder.BuilderMethodPool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderMethodReference key) {
                return key.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderMethodReference key, int value) {
                int prev = key.index;
                key.index = value;
                return prev;
            }
        };
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderMethodPool$MethodKey.class */
    private static class MethodKey extends BaseMethodReference implements MethodReference {
        @Nonnull
        private final String definingClass;
        @Nonnull
        private final String name;
        @Nonnull
        private final List<? extends CharSequence> parameterTypes;
        @Nonnull
        private final String returnType;

        public MethodKey(@Nonnull String definingClass, @Nonnull String name, @Nonnull List<? extends CharSequence> parameterTypes, @Nonnull String returnType) {
            this.definingClass = definingClass;
            this.name = name;
            this.parameterTypes = parameterTypes;
            this.returnType = returnType;
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getDefiningClass() {
            return this.definingClass;
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method, org.jf.dexlib2.iface.Member
        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference
        @Nonnull
        public List<? extends CharSequence> getParameterTypes() {
            return this.parameterTypes;
        }

        @Override // org.jf.dexlib2.iface.reference.MethodReference, org.jf.dexlib2.iface.Method
        @Nonnull
        public String getReturnType() {
            return this.returnType;
        }
    }
}
