package soot;

import soot.javaToJimple.InitialResolver;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/JavaClassProvider.class */
public class JavaClassProvider implements ClassProvider {

    /* loaded from: gencallgraphv3.jar:soot/JavaClassProvider$JarException.class */
    public static class JarException extends RuntimeException {
        private static final long serialVersionUID = 1;

        public JarException(String className) {
            super("Class " + className + " was found in an archive, but Soot doesn't support reading source files out of an archive");
        }
    }

    @Override // soot.ClassProvider
    public ClassSource find(String className) {
        if (Options.v().polyglot() && InitialResolver.v().hasASTForSootName(className)) {
            InitialResolver.v().setASTForSootName(className);
            return new JavaClassSource(className);
        }
        boolean checkAgain = className.indexOf(36) >= 0;
        IFoundFile file = null;
        try {
            SourceLocator loc = SourceLocator.v();
            String javaClassName = loc.getSourceForClass(className);
            IFoundFile file2 = loc.lookupInClassPath(String.valueOf(javaClassName.replace('.', '/')) + ".java");
            if (file2 == null && checkAgain) {
                file2 = loc.lookupInClassPath(String.valueOf(className.replace('.', '/')) + ".java");
            }
            if (file2 != null) {
                if (file2.isZipFile()) {
                    throw new JarException(className);
                }
                JavaClassSource javaClassSource = new JavaClassSource(className, file2.getFile());
                if (file2 != null) {
                    file2.close();
                }
                return javaClassSource;
            } else if (file2 == null) {
                return null;
            } else {
                file2.close();
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                file.close();
            }
            throw th;
        }
    }
}
