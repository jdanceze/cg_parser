package soot.util.backend;

import org.objectweb.asm.ClassWriter;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/util/backend/SootASMClassWriter.class */
public class SootASMClassWriter extends ClassWriter {
    public SootASMClassWriter(int flags) {
        super(flags);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.objectweb.asm.ClassWriter
    public String getCommonSuperClass(String type1, String type2) {
        Type mergedType;
        String typeName1 = type1.replace('/', '.');
        String typeName2 = type2.replace('/', '.');
        SootClass s1 = Scene.v().getSootClass(typeName1);
        SootClass s2 = Scene.v().getSootClass(typeName2);
        if (s1.isPhantom() || s2.isPhantom() || s1.resolvingLevel() == 0 || s2.resolvingLevel() == 0) {
            mergedType = Scene.v().getObjectType();
        } else {
            Type t1 = s1.getType();
            Type t2 = s2.getType();
            mergedType = t1.merge(t2, Scene.v());
        }
        if (mergedType instanceof RefType) {
            return ASMBackendUtils.slashify(((RefType) mergedType).getClassName());
        }
        throw new RuntimeException("Could not find common super class");
    }
}
