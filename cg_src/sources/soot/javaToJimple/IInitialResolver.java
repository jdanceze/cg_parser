package soot.javaToJimple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootClass;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/IInitialResolver.class */
public interface IInitialResolver {
    void formAst(String str, List<String> list, String str2);

    Dependencies resolveFromJavaFile(SootClass sootClass);

    /* loaded from: gencallgraphv3.jar:soot/javaToJimple/IInitialResolver$Dependencies.class */
    public static class Dependencies {
        public final Set<Type> typesToHierarchy;
        public final Set<Type> typesToSignature;

        public Dependencies() {
            this.typesToHierarchy = new HashSet();
            this.typesToSignature = new HashSet();
        }

        public Dependencies(Set<Type> typesToHierarchy, Set<Type> typesToSignature) {
            this.typesToHierarchy = typesToHierarchy == null ? new HashSet<>() : typesToHierarchy;
            this.typesToSignature = typesToSignature == null ? new HashSet<>() : typesToSignature;
        }
    }
}
