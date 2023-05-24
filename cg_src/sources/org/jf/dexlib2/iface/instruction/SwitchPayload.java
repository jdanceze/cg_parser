package org.jf.dexlib2.iface.instruction;

import java.util.List;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/SwitchPayload.class */
public interface SwitchPayload extends PayloadInstruction {
    @Nonnull
    List<? extends SwitchElement> getSwitchElements();
}
