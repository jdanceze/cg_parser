package org.jf.dexlib2.iface.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/DualReferenceInstruction.class */
public interface DualReferenceInstruction extends ReferenceInstruction {
    @Nonnull
    Reference getReference2();

    int getReferenceType2();
}
