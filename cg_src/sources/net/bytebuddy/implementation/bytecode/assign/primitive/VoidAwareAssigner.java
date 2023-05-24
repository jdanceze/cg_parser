package net.bytebuddy.implementation.bytecode.assign.primitive;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.Removal;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/assign/primitive/VoidAwareAssigner.class */
public class VoidAwareAssigner implements Assigner {
    private final Assigner chained;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.chained.equals(((VoidAwareAssigner) obj).chained);
    }

    public int hashCode() {
        return (17 * 31) + this.chained.hashCode();
    }

    public VoidAwareAssigner(Assigner chained) {
        this.chained = chained;
    }

    @Override // net.bytebuddy.implementation.bytecode.assign.Assigner
    public StackManipulation assign(TypeDescription.Generic source, TypeDescription.Generic target, Assigner.Typing typing) {
        if (source.represents(Void.TYPE) && target.represents(Void.TYPE)) {
            return StackManipulation.Trivial.INSTANCE;
        }
        if (source.represents(Void.TYPE)) {
            return typing.isDynamic() ? DefaultValue.of(target) : StackManipulation.Illegal.INSTANCE;
        } else if (target.represents(Void.TYPE)) {
            return Removal.of(source);
        } else {
            return this.chained.assign(source, target, typing);
        }
    }
}
