package org.jf.dexlib2.writer;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/InstructionFactory.class */
public interface InstructionFactory<Ref extends Reference> {
    Instruction makeInstruction10t(@Nonnull Opcode opcode, int i);

    Instruction makeInstruction10x(@Nonnull Opcode opcode);

    Instruction makeInstruction11n(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction11x(@Nonnull Opcode opcode, int i);

    Instruction makeInstruction12x(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction20bc(@Nonnull Opcode opcode, int i, @Nonnull Ref ref);

    Instruction makeInstruction20t(@Nonnull Opcode opcode, int i);

    Instruction makeInstruction21c(@Nonnull Opcode opcode, int i, @Nonnull Ref ref);

    Instruction makeInstruction21ih(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction21lh(@Nonnull Opcode opcode, int i, long j);

    Instruction makeInstruction21s(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction21t(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction22b(@Nonnull Opcode opcode, int i, int i2, int i3);

    Instruction makeInstruction22c(@Nonnull Opcode opcode, int i, int i2, @Nonnull Ref ref);

    Instruction makeInstruction22s(@Nonnull Opcode opcode, int i, int i2, int i3);

    Instruction makeInstruction22t(@Nonnull Opcode opcode, int i, int i2, int i3);

    Instruction makeInstruction22x(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction23x(@Nonnull Opcode opcode, int i, int i2, int i3);

    Instruction makeInstruction30t(@Nonnull Opcode opcode, int i);

    Instruction makeInstruction31c(@Nonnull Opcode opcode, int i, @Nonnull Ref ref);

    Instruction makeInstruction31i(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction31t(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction32x(@Nonnull Opcode opcode, int i, int i2);

    Instruction makeInstruction35c(@Nonnull Opcode opcode, int i, int i2, int i3, int i4, int i5, int i6, @Nonnull Ref ref);

    Instruction makeInstruction3rc(@Nonnull Opcode opcode, int i, int i2, @Nonnull Ref ref);

    Instruction makeInstruction51l(@Nonnull Opcode opcode, int i, long j);

    Instruction makeSparseSwitchPayload(@Nullable List<? extends SwitchElement> list);

    Instruction makePackedSwitchPayload(@Nullable List<? extends SwitchElement> list);

    Instruction makeArrayPayload(int i, @Nullable List<Number> list);
}
