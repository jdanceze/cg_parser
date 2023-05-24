package org.jf.dexlib2.builder.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/debug/BuilderSetSourceFile.class */
public class BuilderSetSourceFile extends BuilderDebugItem implements SetSourceFile {
    @Nullable
    private final StringReference sourceFile;

    public BuilderSetSourceFile(@Nullable StringReference sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override // org.jf.dexlib2.iface.debug.DebugItem
    public int getDebugItemType() {
        return 9;
    }

    @Override // org.jf.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public String getSourceFile() {
        if (this.sourceFile == null) {
            return null;
        }
        return this.sourceFile.getString();
    }

    @Override // org.jf.dexlib2.iface.debug.SetSourceFile
    @Nullable
    public StringReference getSourceFileReference() {
        return this.sourceFile;
    }
}
