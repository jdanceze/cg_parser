package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.util.Preconditions;
import org.jf.util.ImmutableUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableArrayPayload.class */
public class ImmutableArrayPayload extends ImmutableInstruction implements ArrayPayload {
    public static final Opcode OPCODE = Opcode.ARRAY_PAYLOAD;
    protected final int elementWidth;
    @Nonnull
    protected final ImmutableList<Number> arrayElements;

    public ImmutableArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
        super(OPCODE);
        this.elementWidth = Preconditions.checkArrayPayloadElementWidth(elementWidth);
        this.arrayElements = (ImmutableList) Preconditions.checkArrayPayloadElements(elementWidth, arrayElements == null ? ImmutableList.of() : ImmutableList.copyOf((Collection) arrayElements));
    }

    public ImmutableArrayPayload(int elementWidth, @Nullable ImmutableList<Number> arrayElements) {
        super(OPCODE);
        this.elementWidth = Preconditions.checkArrayPayloadElementWidth(elementWidth);
        this.arrayElements = (ImmutableList) Preconditions.checkArrayPayloadElements(elementWidth, ImmutableUtils.nullToEmptyList(arrayElements));
    }

    @Nonnull
    public static ImmutableArrayPayload of(ArrayPayload instruction) {
        if (instruction instanceof ImmutableArrayPayload) {
            return (ImmutableArrayPayload) instruction;
        }
        return new ImmutableArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.ArrayPayload
    public int getElementWidth() {
        return this.elementWidth;
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.ArrayPayload
    @Nonnull
    public List<Number> getArrayElements() {
        return this.arrayElements;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction, org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return 4 + (((this.elementWidth * this.arrayElements.size()) + 1) / 2);
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return OPCODE.format;
    }
}
