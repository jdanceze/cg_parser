package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.writer.TypeSection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/TypePool.class */
public class TypePool extends StringTypeBasePool implements TypeSection<CharSequence, CharSequence, TypeReference> {
    public TypePool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull CharSequence type) {
        String typeString = type.toString();
        Integer prev = (Integer) this.internedItems.put(typeString, 0);
        if (prev == null) {
            ((StringPool) this.dexPool.stringSection).intern(typeString);
        }
    }

    public void internNullable(@Nullable CharSequence type) {
        if (type != null) {
            intern(type);
        }
    }

    @Override // org.jf.dexlib2.writer.TypeSection
    public int getItemIndex(@Nonnull TypeReference key) {
        return getItemIndex((CharSequence) key.getType());
    }

    @Override // org.jf.dexlib2.writer.TypeSection
    @Nonnull
    public CharSequence getString(@Nonnull CharSequence type) {
        return type;
    }
}
