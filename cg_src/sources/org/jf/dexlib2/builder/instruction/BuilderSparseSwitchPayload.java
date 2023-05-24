package org.jf.dexlib2.builder.instruction;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderSwitchPayload;
import org.jf.dexlib2.builder.SwitchLabelElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/instruction/BuilderSparseSwitchPayload.class */
public class BuilderSparseSwitchPayload extends BuilderSwitchPayload implements SparseSwitchPayload {
    public static final Opcode OPCODE = Opcode.SPARSE_SWITCH_PAYLOAD;
    @Nonnull
    protected final List<BuilderSwitchElement> switchElements;

    public BuilderSparseSwitchPayload(@Nullable List<? extends SwitchLabelElement> switchElements) {
        super(OPCODE);
        if (switchElements == null) {
            this.switchElements = ImmutableList.of();
        } else {
            this.switchElements = Lists.transform(switchElements, new Function<SwitchLabelElement, BuilderSwitchElement>() { // from class: org.jf.dexlib2.builder.instruction.BuilderSparseSwitchPayload.1
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !BuilderSparseSwitchPayload.class.desiredAssertionStatus();
                }

                @Override // com.google.common.base.Function
                @Nullable
                public BuilderSwitchElement apply(@Nullable SwitchLabelElement element) {
                    if ($assertionsDisabled || element != null) {
                        return new BuilderSwitchElement(BuilderSparseSwitchPayload.this, element.key, element.target);
                    }
                    throw new AssertionError();
                }
            });
        }
    }

    @Override // org.jf.dexlib2.builder.BuilderSwitchPayload, org.jf.dexlib2.iface.instruction.SwitchPayload
    @Nonnull
    public List<BuilderSwitchElement> getSwitchElements() {
        return this.switchElements;
    }

    @Override // org.jf.dexlib2.builder.BuilderInstruction, org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return 2 + (this.switchElements.size() * 4);
    }

    @Override // org.jf.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
