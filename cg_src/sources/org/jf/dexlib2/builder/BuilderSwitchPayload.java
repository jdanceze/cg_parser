package org.jf.dexlib2.builder;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.instruction.BuilderSwitchElement;
import org.jf.dexlib2.iface.instruction.SwitchPayload;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderSwitchPayload.class */
public abstract class BuilderSwitchPayload extends BuilderInstruction implements SwitchPayload {
    @Nullable
    MethodLocation referrer;

    @Override // org.jf.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public abstract List<? extends BuilderSwitchElement> getSwitchElements();

    /* JADX INFO: Access modifiers changed from: protected */
    public BuilderSwitchPayload(@Nonnull Opcode opcode) {
        super(opcode);
    }

    @Nonnull
    public MethodLocation getReferrer() {
        if (this.referrer == null) {
            throw new IllegalStateException("The referrer has not been set yet");
        }
        return this.referrer;
    }
}
