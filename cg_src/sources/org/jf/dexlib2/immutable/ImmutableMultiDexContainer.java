package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.MultiDexContainer;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableMultiDexContainer.class */
public class ImmutableMultiDexContainer implements MultiDexContainer<ImmutableDexFile> {
    private final ImmutableMap<String, ImmutableDexEntry> entries;

    public ImmutableMultiDexContainer(Map<String, ImmutableDexFile> entries) {
        ImmutableMap.Builder<String, ImmutableDexEntry> builder = ImmutableMap.builder();
        for (Map.Entry<String, ImmutableDexFile> entry : entries.entrySet()) {
            ImmutableDexEntry dexEntry = new ImmutableDexEntry(entry.getKey(), entry.getValue());
            builder.put(dexEntry.getEntryName(), dexEntry);
        }
        this.entries = builder.build();
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nonnull
    public List<String> getDexEntryNames() {
        return ImmutableList.copyOf((Collection) this.entries.keySet());
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nullable
    /* renamed from: getEntry */
    public MultiDexContainer.DexEntry<ImmutableDexFile> getEntry2(@Nonnull String entryName) {
        return this.entries.get(entryName);
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/ImmutableMultiDexContainer$ImmutableDexEntry.class */
    public class ImmutableDexEntry implements MultiDexContainer.DexEntry<ImmutableDexFile> {
        private final String entryName;
        private final ImmutableDexFile dexFile;

        protected ImmutableDexEntry(String entryName, ImmutableDexFile dexFile) {
            this.entryName = entryName;
            this.dexFile = dexFile;
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public String getEntryName() {
            return this.entryName;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public ImmutableDexFile getDexFile() {
            return this.dexFile;
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public MultiDexContainer<? extends ImmutableDexFile> getContainer() {
            return ImmutableMultiDexContainer.this;
        }
    }
}
