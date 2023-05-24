package soot.asm;

import soot.ClassProvider;
import soot.ClassSource;
import soot.IFoundFile;
import soot.SourceLocator;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmClassProvider.class */
public class AsmClassProvider implements ClassProvider {
    @Override // soot.ClassProvider
    public ClassSource find(String cls) {
        String clsFile = String.valueOf(cls.replace('.', '/')) + ".class";
        IFoundFile file = SourceLocator.v().lookupInClassPath(clsFile);
        if (file == null) {
            return null;
        }
        return new AsmClassSource(cls, file);
    }
}
