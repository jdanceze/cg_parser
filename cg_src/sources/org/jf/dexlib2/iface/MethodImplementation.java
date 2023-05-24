package org.jf.dexlib2.iface;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/MethodImplementation.class */
public interface MethodImplementation {
    int getRegisterCount();

    @Nonnull
    Iterable<? extends Instruction> getInstructions();

    @Nonnull
    List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks();

    @Nonnull
    Iterable<? extends DebugItem> getDebugItems();
}
