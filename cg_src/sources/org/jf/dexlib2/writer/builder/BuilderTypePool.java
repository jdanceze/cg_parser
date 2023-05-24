package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.TypeSection;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderTypePool.class */
public class BuilderTypePool extends BaseBuilderPool implements TypeSection<BuilderStringReference, BuilderTypeReference, BuilderTypeReference> {
    @Nonnull
    private final ConcurrentMap<String, BuilderTypeReference> internedItems;

    public BuilderTypePool(@Nonnull DexBuilder dexBuilder) {
        super(dexBuilder);
        this.internedItems = Maps.newConcurrentMap();
    }

    @Nonnull
    public BuilderTypeReference internType(@Nonnull String type) {
        BuilderTypeReference ret = this.internedItems.get(type);
        if (ret != null) {
            return ret;
        }
        BuilderStringReference stringRef = ((BuilderStringPool) this.dexBuilder.stringSection).internString(type);
        BuilderTypeReference typeReference = new BuilderTypeReference(stringRef);
        BuilderTypeReference ret2 = this.internedItems.putIfAbsent(type, typeReference);
        return ret2 == null ? typeReference : ret2;
    }

    @Nullable
    public BuilderTypeReference internNullableType(@Nullable String type) {
        if (type == null) {
            return null;
        }
        return internType(type);
    }

    @Override // org.jf.dexlib2.writer.TypeSection
    @Nonnull
    public BuilderStringReference getString(@Nonnull BuilderTypeReference key) {
        return key.stringReference;
    }

    @Override // org.jf.dexlib2.writer.NullableIndexSection
    public int getNullableItemIndex(@Nullable BuilderTypeReference key) {
        if (key == null) {
            return -1;
        }
        return key.index;
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull BuilderTypeReference key) {
        return key.getIndex();
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends BuilderTypeReference, Integer>> getItems() {
        return new BuilderMapEntryCollection<BuilderTypeReference>(this.internedItems.values()) { // from class: org.jf.dexlib2.writer.builder.BuilderTypePool.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int getValue(@Nonnull BuilderTypeReference key) {
                return key.index;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.jf.dexlib2.writer.builder.BuilderMapEntryCollection
            public int setValue(@Nonnull BuilderTypeReference key, int value) {
                int prev = key.index;
                key.index = value;
                return prev;
            }
        };
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    public int getItemCount() {
        return this.internedItems.size();
    }
}
