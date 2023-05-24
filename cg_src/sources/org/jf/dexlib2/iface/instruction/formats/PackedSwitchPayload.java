package org.jf.dexlib2.iface.instruction.formats;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.SwitchPayload;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/formats/PackedSwitchPayload.class */
public interface PackedSwitchPayload extends SwitchPayload {
    @Override // org.jf.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    List<? extends SwitchElement> getSwitchElements();
}
