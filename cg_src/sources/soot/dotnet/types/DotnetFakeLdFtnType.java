package soot.dotnet.types;

import java.util.ArrayList;
import java.util.List;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Value;
import soot.javaToJimple.IInitialResolver;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dotnet/types/DotnetFakeLdFtnType.class */
public class DotnetFakeLdFtnType {
    private static final String FAKE_LDFTN_METHOD_NAME = "FakeLdFtn";

    public static IInitialResolver.Dependencies resolve(SootClass sootClass) {
        IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
        SootClass superClass = SootResolver.v().makeClassRef(DotnetBasicTypes.SYSTEM_OBJECT);
        deps.typesToHierarchy.add(superClass.getType());
        sootClass.setSuperclass(superClass);
        int classModifier = 0 | 1;
        sootClass.setModifiers(classModifier | 8);
        int modifier = 0 | 1;
        SootMethod m = Scene.v().makeSootMethod(FAKE_LDFTN_METHOD_NAME, new ArrayList(), DotnetTypeFactory.toSootType(DotnetBasicTypes.SYSTEM_INTPTR), modifier | 8 | 256);
        sootClass.addMethod(m);
        return deps;
    }

    public static Value makeMethod() {
        SootClass clazz = Scene.v().getSootClass(DotnetBasicTypes.FAKE_LDFTN);
        List<Local> argsVariables = new ArrayList<>();
        List<Type> methodParams = new ArrayList<>();
        SootMethodRef methodRef = Scene.v().makeMethodRef(clazz, FAKE_LDFTN_METHOD_NAME, methodParams, DotnetTypeFactory.toSootType(DotnetBasicTypes.SYSTEM_INTPTR), true);
        return Jimple.v().newStaticInvokeExpr(methodRef, argsVariables);
    }
}
