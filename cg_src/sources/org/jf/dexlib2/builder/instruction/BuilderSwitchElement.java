package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.builder.BuilderSwitchPayload;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.SwitchElement;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/instruction/BuilderSwitchElement.class */
public class BuilderSwitchElement implements SwitchElement {
    @Nonnull
    BuilderSwitchPayload parent;
    private final int key;
    @Nonnull
    private final Label target;

    public BuilderSwitchElement(@Nonnull BuilderSwitchPayload parent, int key, @Nonnull Label target) {
        this.parent = parent;
        this.key = key;
        this.target = target;
    }

    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
    public int getKey() {
        return this.key;
    }

    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
    public int getOffset() {
        return this.target.getCodeAddress() - this.parent.getReferrer().getCodeAddress();
    }

    @Nonnull
    public Label getTarget() {
        return this.target;
    }
}
