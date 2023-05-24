package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.writer.ProtoSection;
import org.jf.dexlib2.writer.pool.TypeListPool;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/ProtoPool.class */
public class ProtoPool extends BaseIndexPool<MethodProtoReference> implements ProtoSection<CharSequence, CharSequence, MethodProtoReference, TypeListPool.Key<? extends Collection<? extends CharSequence>>> {
    public ProtoPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull MethodProtoReference reference) {
        Integer prev = (Integer) this.internedItems.put(reference, 0);
        if (prev == null) {
            ((StringPool) this.dexPool.stringSection).intern(getShorty(reference));
            ((TypePool) this.dexPool.typeSection).intern(reference.getReturnType());
            ((TypeListPool) this.dexPool.typeListSection).intern(reference.getParameterTypes());
        }
    }

    @Override // org.jf.dexlib2.writer.ProtoSection
    @Nonnull
    public CharSequence getShorty(@Nonnull MethodProtoReference reference) {
        return MethodUtil.getShorty(reference.getParameterTypes(), reference.getReturnType());
    }

    @Override // org.jf.dexlib2.writer.ProtoSection
    @Nonnull
    public CharSequence getReturnType(@Nonnull MethodProtoReference protoReference) {
        return protoReference.getReturnType();
    }

    @Override // org.jf.dexlib2.writer.ProtoSection
    @Nullable
    public TypeListPool.Key<List<? extends CharSequence>> getParameters(@Nonnull MethodProtoReference methodProto) {
        return new TypeListPool.Key<>(methodProto.getParameterTypes());
    }
}
