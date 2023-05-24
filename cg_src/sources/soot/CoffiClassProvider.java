package soot;
/* loaded from: gencallgraphv3.jar:soot/CoffiClassProvider.class */
public class CoffiClassProvider implements ClassProvider {
    @Override // soot.ClassProvider
    public ClassSource find(String className) {
        String fileName = String.valueOf(className.replace('.', '/')) + ".class";
        IFoundFile file = SourceLocator.v().lookupInClassPath(fileName);
        if (file == null) {
            return null;
        }
        return new CoffiClassSource(className, file);
    }
}
