package org.jf.dexlib2.iface;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.DexFile;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/MultiDexContainer.class */
public interface MultiDexContainer<T extends DexFile> {

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/MultiDexContainer$DexEntry.class */
    public interface DexEntry<T extends DexFile> {
        @Nonnull
        String getEntryName();

        @Nonnull
        T getDexFile();

        @Nonnull
        MultiDexContainer<? extends T> getContainer();
    }

    @Nonnull
    List<String> getDexEntryNames() throws IOException;

    @Nullable
    DexEntry<T> getEntry(@Nonnull String str) throws IOException;
}
