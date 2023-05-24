package net.bytebuddy.implementation.bytecode.assign.primitive;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/PrimitiveTypeAwareAssigner.class */
public class PrimitiveTypeAwareAssigner implements Assigner {
    private final Assigner referenceTypeAwareAssigner;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.referenceTypeAwareAssigner.equals(((PrimitiveTypeAwareAssigner) obj).referenceTypeAwareAssigner);
    }

    public int hashCode() {
        return (17 * 31) + this.referenceTypeAwareAssigner.hashCode();
    }

    public PrimitiveTypeAwareAssigner(Assigner referenceTypeAwareAssigner) {
        this.referenceTypeAwareAssigner = referenceTypeAwareAssigner;
    }

    @Override // net.bytebuddy.implementation.bytecode.assign.Assigner
    public StackManipulation assign(TypeDescription.Generic source, TypeDescription.Generic target, Assigner.Typing typing) {
        if (source.isPrimitive() && target.isPrimitive()) {
            return PrimitiveWideningDelegate.forPrimitive(source).widenTo(target);
        }
        if (source.isPrimitive()) {
            return PrimitiveBoxingDelegate.forPrimitive(source).assignBoxedTo(target, this.referenceTypeAwareAssigner, typing);
        }
        if (target.isPrimitive()) {
            return PrimitiveUnboxingDelegate.forReferenceType(source).assignUnboxedTo(target, this.referenceTypeAwareAssigner, typing);
        }
        return this.referenceTypeAwareAssigner.assign(source, target, typing);
    }
}
