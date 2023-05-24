package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/debug/ImmutableSetSourceFile.class */
public class ImmutableSetSourceFile extends ImmutableDebugItem implements SetSourceFile {
    @Nullable
    protected final String sourceFile;

    public ImmutableSetSourceFile(int codeAddress, @Nullable String sourceFile) {
        super(codeAddress);
        this.sourceFile = sourceFile;
    }

    @Nonnull
    public static ImmutableSetSourceFile of(@Nonnull SetSourceFile setSourceFile) {
        if (setSourceFile instanceof ImmutableSetSourceFile) {
            return (ImmutableSetSourceFile) setSourceFile;
        }
        return new ImmutableSetSourceFile(setSourceFile.getCodeAddress(), setSourceFile.getSourceFile());
    }

    @Override // org.jf.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public String getSourceFile() {
        return this.sourceFile;
    }

    @Override // org.jf.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public StringReference getSourceFileReference() {
        if (this.sourceFile == null) {
            return null;
        }
        return new BaseStringReference() { // from class: org.jf.dexlib2.immutable.debug.ImmutableSetSourceFile.1
            @Override // org.jf.dexlib2.iface.reference.StringReference
            @Nonnull
            public String getString() {
                return ImmutableSetSourceFile.this.sourceFile;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 9;
    }
}
