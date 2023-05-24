package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.writer.MethodSection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/MethodPool.class */
public class MethodPool extends BaseIndexPool<MethodReference> implements MethodSection<CharSequence, CharSequence, MethodProtoReference, MethodReference, PoolMethod> {
    public MethodPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull MethodReference method) {
        Integer prev = (Integer) this.internedItems.put(method, 0);
        if (prev == null) {
            ((TypePool) this.dexPool.typeSection).intern(method.getDefiningClass());
            ((ProtoPool) this.dexPool.protoSection).intern(new PoolMethodProto(method));
            ((StringPool) this.dexPool.stringSection).intern(method.getName());
        }
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public MethodReference getMethodReference(@Nonnull PoolMethod poolMethod) {
        return poolMethod;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public CharSequence getDefiningClass(@Nonnull MethodReference methodReference) {
        return methodReference.getDefiningClass();
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public MethodProtoReference getPrototype(@Nonnull MethodReference methodReference) {
        return new PoolMethodProto(methodReference);
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public MethodProtoReference getPrototype(@Nonnull PoolMethod poolMethod) {
        return new PoolMethodProto(poolMethod);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.writer.MethodSection
    @Nonnull
    public CharSequence getName(@Nonnull MethodReference methodReference) {
        return methodReference.getName();
    }

    @Override // org.jf.dexlib2.writer.MethodSection
    public int getMethodIndex(@Nonnull PoolMethod poolMethod) {
        return getItemIndex(poolMethod);
    }
}
