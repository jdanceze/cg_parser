package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/DexFile.class */
public interface DexFile {
    @Nonnull
    Set<? extends ClassDef> getClasses();

    @Nonnull
    Opcodes getOpcodes();
}
