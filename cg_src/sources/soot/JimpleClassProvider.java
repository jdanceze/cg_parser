package soot;

import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/JimpleClassProvider.class */
public class JimpleClassProvider implements ClassProvider {
    @Override // soot.ClassProvider
    public ClassSource find(String className) {
        IFoundFile file = SourceLocator.v().lookupInClassPath(String.valueOf(className) + ".jimple");
        if (file == null) {
            if (Options.v().permissive_resolving()) {
                file = SourceLocator.v().lookupInClassPath(String.valueOf(className.replace('.', '/')) + ".jimple");
            }
            if (file == null) {
                return null;
            }
        }
        return new JimpleClassSource(className, file);
    }
}
