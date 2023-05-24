package org.jf.dexlib2.writer.builder;

import com.google.common.collect.ImmutableList;
import java.util.AbstractList;
import java.util.List;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderTypeList.class */
public class BuilderTypeList extends AbstractList<BuilderTypeReference> {
    static final BuilderTypeList EMPTY = new BuilderTypeList(ImmutableList.of());
    @Nonnull
    final List<? extends BuilderTypeReference> types;
    int offset = 0;

    public BuilderTypeList(@Nonnull List<? extends BuilderTypeReference> types) {
        this.types = types;
    }

    @Override // java.util.AbstractList, java.util.List
    public BuilderTypeReference get(int index) {
        return this.types.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.types.size();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
