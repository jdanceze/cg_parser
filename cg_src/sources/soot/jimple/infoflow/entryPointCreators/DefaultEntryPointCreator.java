package soot.jimple.infoflow.entryPointCreators;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.IntType;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.EqExpr;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
import soot.jimple.toolkits.scalar.NopEliminator;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/entryPointCreators/DefaultEntryPointCreator.class */
public class DefaultEntryPointCreator extends BaseEntryPointCreator {
    private static final Logger logger = LoggerFactory.getLogger(DefaultEntryPointCreator.class);
    private final Collection<String> methodsToCall;

    public DefaultEntryPointCreator(Collection<String> methodsToCall) {
        this.methodsToCall = methodsToCall;
        setAllowNonPublicConstructors(true);
    }

    @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
    protected SootMethod createDummyMainInternal() {
        Map<String, Set<String>> classMap = SootMethodRepresentationParser.v().parseClassNames(this.methodsToCall, false);
        HashMap<String, Local> localVarsForClasses = new HashMap<>();
        for (String className : classMap.keySet()) {
            SootClass createdClass = Scene.v().forceResolve(className, 3);
            createdClass.setApplicationClass();
            Local localVal = generateClassConstructor(createdClass);
            if (localVal == null) {
                logger.warn("Cannot generate constructor for class: {}", createdClass);
            } else {
                localVarsForClasses.put(className, localVal);
            }
        }
        int conditionCounter = 0;
        Jimple jimple = Jimple.v();
        NopStmt startStmt = jimple.newNopStmt();
        NopStmt endStmt = jimple.newNopStmt();
        Value intCounter = this.generator.generateLocal(IntType.v());
        this.body.getUnits().add((UnitPatchingChain) startStmt);
        for (Map.Entry<String, Set<String>> entry : classMap.entrySet()) {
            Local classLocal = localVarsForClasses.get(entry.getKey());
            for (String method : entry.getValue()) {
                SootMethodAndClass methodAndClass = SootMethodRepresentationParser.v().parseSootMethodString(method);
                SootMethod currentMethod = findMethod(Scene.v().getSootClass(methodAndClass.getClassName()), methodAndClass.getSubSignature());
                if (currentMethod == null) {
                    logger.warn("Entry point not found: {}", method);
                } else {
                    EqExpr cond = jimple.newEqExpr(intCounter, IntConstant.v(conditionCounter));
                    conditionCounter++;
                    NopStmt thenStmt = jimple.newNopStmt();
                    IfStmt ifStmt = jimple.newIfStmt(cond, thenStmt);
                    this.body.getUnits().add((UnitPatchingChain) ifStmt);
                    buildMethodCall(currentMethod, classLocal);
                    this.body.getUnits().add((UnitPatchingChain) thenStmt);
                }
            }
        }
        this.body.getUnits().add((UnitPatchingChain) endStmt);
        GotoStmt gotoStart = jimple.newGotoStmt(startStmt);
        this.body.getUnits().add((UnitPatchingChain) gotoStart);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        NopEliminator.v().transform(this.body);
        eliminateSelfLoops();
        return this.mainMethod;
    }

    @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
    public Collection<String> getRequiredClasses() {
        return SootMethodRepresentationParser.v().parseClassNames(this.methodsToCall, false).keySet();
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
