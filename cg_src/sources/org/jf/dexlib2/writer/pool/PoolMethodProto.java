package org.jf.dexlib2.writer.pool;

import java.util.List;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/PoolMethodProto.class */
public class PoolMethodProto extends BaseMethodProtoReference implements MethodProtoReference {
    private final MethodReference methodReference;

    public PoolMethodProto(MethodReference methodReference) {
        this.methodReference = methodReference;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    public List<? extends CharSequence> getParameterTypes() {
        return this.methodReference.getParameterTypes();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    public String getReturnType() {
        return this.methodReference.getReturnType();
    }
}
