package org.jf.dexlib2.rewriter;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DexFileRewriter.class */
public class DexFileRewriter implements Rewriter<DexFile> {
    @Nonnull
    protected final Rewriters rewriters;

    public DexFileRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public DexFile rewrite(@Nonnull DexFile value) {
        return new RewrittenDexFile(value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/DexFileRewriter$RewrittenDexFile.class */
    public class RewrittenDexFile implements DexFile {
        @Nonnull
        protected final DexFile dexFile;

        public RewrittenDexFile(@Nonnull DexFile dexFile) {
            this.dexFile = dexFile;
        }

        @Override // org.jf.dexlib2.iface.DexFile
        @Nonnull
        public Set<? extends ClassDef> getClasses() {
            return RewriterUtils.rewriteSet(DexFileRewriter.this.rewriters.getClassDefRewriter(), this.dexFile.getClasses());
        }

        @Override // org.jf.dexlib2.iface.DexFile
        @Nonnull
        public Opcodes getOpcodes() {
            return this.dexFile.getOpcodes();
        }
    }
}
