package soot;

import java.util.Iterator;
import java.util.List;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/PolymorphicMethodRef.class */
public class PolymorphicMethodRef extends SootMethodRefImpl {
    public static final String METHODHANDLE_SIGNATURE = "java.lang.invoke.MethodHandle";
    public static final String VARHANDLE_SIGNATURE = "java.lang.invoke.VarHandle";
    public static final String POLYMORPHIC_SIGNATURE = "java/lang/invoke/MethodHandle$PolymorphicSignature";

    public static boolean handlesClass(SootClass declaringClass) {
        return handlesClass(declaringClass.getName());
    }

    public static boolean handlesClass(String declaringClassName) {
        return METHODHANDLE_SIGNATURE.equals(declaringClassName) || VARHANDLE_SIGNATURE.equals(declaringClassName);
    }

    public PolymorphicMethodRef(SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean isStatic) {
        super(declaringClass, name, parameterTypes, returnType, isStatic);
    }

    @Override // soot.SootMethodRefImpl, soot.SootMethodRef
    public SootMethod resolve() {
        Tag annotationsTag;
        SootMethod method = getDeclaringClass().getMethodUnsafe(getName(), getParameterTypes(), getReturnType());
        if (method != null) {
            return method;
        }
        for (SootMethod candidateMethod : getDeclaringClass().getMethods()) {
            if (candidateMethod.getName().equals(getName()) && (annotationsTag = candidateMethod.getTag(VisibilityAnnotationTag.NAME)) != null) {
                Iterator<AnnotationTag> it = ((VisibilityAnnotationTag) annotationsTag).getAnnotations().iterator();
                while (it.hasNext()) {
                    AnnotationTag annotation = it.next();
                    if ("Ljava/lang/invoke/MethodHandle$PolymorphicSignature;".equals(annotation.getType())) {
                        return addPolyMorphicMethod(candidateMethod);
                    }
                }
                continue;
            }
        }
        return super.resolve();
    }

    private SootMethod addPolyMorphicMethod(SootMethod originalPolyMorphicMethod) {
        SootMethod newMethod = new SootMethod(getName(), getParameterTypes(), getReturnType(), originalPolyMorphicMethod.modifiers);
        getDeclaringClass().addMethod(newMethod);
        return newMethod;
    }
}
