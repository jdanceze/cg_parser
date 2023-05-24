package org.jf.dexlib2.iface.instruction.formats;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.instruction.PayloadInstruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/formats/ArrayPayload.class */
public interface ArrayPayload extends PayloadInstruction {
    int getElementWidth();

    @Nonnull
    List<Number> getArrayElements();
}
