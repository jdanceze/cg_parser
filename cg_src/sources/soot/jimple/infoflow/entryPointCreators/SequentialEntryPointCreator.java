package soot.jimple.infoflow.entryPointCreators;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.jimple.Jimple;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/entryPointCreators/SequentialEntryPointCreator.class */
public class SequentialEntryPointCreator extends BaseEntryPointCreator {
    private final Collection<String> methodsToCall;

    public SequentialEntryPointCreator(Collection<String> methodsToCall) {
        this.methodsToCall = methodsToCall;
        setAllowNonPublicConstructors(true);
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<String> getRequiredClasses() {
        return SootMethodRepresentationParser.v().parseClassNames(this.methodsToCall, false).keySet();
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    protected SootMethod createDummyMainInternal() {
        Map<String, Set<String>> classMap = SootMethodRepresentationParser.v().parseClassNames(this.methodsToCall, false);
        for (String className : classMap.keySet()) {
            SootClass createdClass = Scene.v().forceResolve(className, 3);
            createdClass.setApplicationClass();
            Local localVal = generateClassConstructor(createdClass);
            if (localVal == null) {
                this.logger.warn("Cannot generate constructor for class: {}", createdClass);
            } else {
                for (String method : classMap.get(className)) {
                    SootMethodAndClass methodAndClass = SootMethodRepresentationParser.v().parseSootMethodString(method);
                    SootMethod methodToInvoke = findMethod(Scene.v().getSootClass(methodAndClass.getClassName()), methodAndClass.getSubSignature());
                    if (methodToInvoke == null) {
                        this.logger.warn("Method %s not found, skipping", methodAndClass);
                    } else if (methodToInvoke.isConcrete() && !methodToInvoke.isConstructor()) {
                        methodToInvoke.retrieveActiveBody();
                        buildMethodCall(methodToInvoke, localVal);
                    }
                }
            }
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        return this.mainMethod;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootMethod> getAdditionalMethods() {
        return null;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<SootField> getAdditionalFields() {
        return null;
    }
}
