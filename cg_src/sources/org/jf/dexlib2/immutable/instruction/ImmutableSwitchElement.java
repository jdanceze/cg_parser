package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.util.ImmutableConverter;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableSwitchElement.class */
public class ImmutableSwitchElement implements SwitchElement {
    protected final int key;
    protected final int offset;
    private static final ImmutableConverter<ImmutableSwitchElement, SwitchElement> CONVERTER = new ImmutableConverter<ImmutableSwitchElement, SwitchElement>() { // from class: org.jf.dexlib2.immutable.instruction.ImmutableSwitchElement.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        public boolean isImmutable(@Nonnull SwitchElement item) {
            return item instanceof ImmutableSwitchElement;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.jf.util.ImmutableConverter
        @Nonnull
        public ImmutableSwitchElement makeImmutable(@Nonnull SwitchElement item) {
            return ImmutableSwitchElement.of(item);
        }
    };

    public ImmutableSwitchElement(int key, int offset) {
        this.key = key;
        this.offset = offset;
    }

    @Nonnull
    public static ImmutableSwitchElement of(SwitchElement switchElement) {
        if (switchElement instanceof ImmutableSwitchElement) {
            return (ImmutableSwitchElement) switchElement;
        }
        return new ImmutableSwitchElement(switchElement.getKey(), switchElement.getOffset());
    }

    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
    public int getKey() {
        return this.key;
    }

    @Override // org.jf.dexlib2.iface.instruction.SwitchElement
    public int getOffset() {
        return this.offset;
    }

    @Nonnull
    public static ImmutableList<ImmutableSwitchElement> immutableListOf(@Nullable List<? extends SwitchElement> list) {
        return CONVERTER.toList(list);
    }
}
