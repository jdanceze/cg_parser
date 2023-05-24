package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.writer.StringSection;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/StringPool.class */
public class StringPool extends StringTypeBasePool implements StringSection<CharSequence, StringReference> {
    public StringPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull CharSequence string) {
        this.internedItems.put(string.toString(), 0);
    }

    public void internNullable(@Nullable CharSequence string) {
        if (string != null) {
            intern(string);
        }
    }

    @Override // org.jf.dexlib2.writer.StringSection
    public int getItemIndex(@Nonnull StringReference key) {
        Integer index = (Integer) this.internedItems.get(key.toString());
        if (index == null) {
            throw new ExceptionWithContext("Item not found.: %s", key.toString());
        }
        return index.intValue();
    }

    @Override // org.jf.dexlib2.writer.StringSection
    public boolean hasJumboIndexes() {
        return this.internedItems.size() > 65536;
    }
}
