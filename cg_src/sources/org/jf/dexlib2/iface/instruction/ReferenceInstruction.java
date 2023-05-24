package org.jf.dexlib2.iface.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/ReferenceInstruction.class */
public interface ReferenceInstruction extends Instruction {
    @Nonnull
    Reference getReference();

    int getReferenceType();
}
