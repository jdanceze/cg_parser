package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodHandleReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/reference/ImmutableMethodHandleReference.class */
public class ImmutableMethodHandleReference extends BaseMethodHandleReference implements ImmutableReference {
    protected final int methodHandleType;
    @Nonnull
    protected final ImmutableReference memberReference;

    public ImmutableMethodHandleReference(int methodHandleType, @Nonnull ImmutableReference memberReference) {
        this.methodHandleType = methodHandleType;
        this.memberReference = memberReference;
    }

    public ImmutableMethodHandleReference(int methodHandleType, @Nonnull Reference memberReference) {
        this.methodHandleType = methodHandleType;
        this.memberReference = ImmutableReferenceFactory.of(memberReference);
    }

    @Nonnull
    public static ImmutableMethodHandleReference of(@Nonnull MethodHandleReference methodHandleReference) {
        ImmutableReference memberReference;
        if (methodHandleReference instanceof ImmutableMethodHandleReference) {
            return (ImmutableMethodHandleReference) methodHandleReference;
        }
        int methodHandleType = methodHandleReference.getMethodHandleType();
        switch (methodHandleType) {
            case 0:
            case 1:
            case 2:
            case 3:
                memberReference = ImmutableFieldReference.of((FieldReference) methodHandleReference.getMemberReference());
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                memberReference = ImmutableMethodReference.of((MethodReference) methodHandleReference.getMemberReference());
                break;
            default:
                throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleType));
        }
        return new ImmutableMethodHandleReference(methodHandleType, memberReference);
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    public int getMethodHandleType() {
        return this.methodHandleType;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    @Nonnull
    public Reference getMemberReference() {
        return this.memberReference;
    }
}
