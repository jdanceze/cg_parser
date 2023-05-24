package soot.jimple.infoflow.codeOptimization;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.MethodOrMethodContext;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.dexpler.DalvikThrowAnalysis;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator;
import soot.jimple.infoflow.entryPointCreators.IEntryPointCreator;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/codeOptimization/InterproceduralConstantValuePropagator.class */
public class InterproceduralConstantValuePropagator extends SceneTransformer {
    private final Logger logger;
    private final InfoflowManager manager;
    private final Set<SootMethod> excludedMethods;
    private final ISourceSinkManager sourceSinkManager;
    private final ITaintPropagationWrapper taintWrapper;
    private boolean removeSideEffectFreeMethods;
    private boolean excludeSystemClasses;
    protected final Map<SootMethod, Boolean> methodSideEffects;
    protected final LoadingCache<SootMethod, Boolean> methodSinks;
    protected SootClass exceptionClass;
    protected final Map<SootClass, SootMethod> exceptionThrowers;
    private final List<SootMethod> propagationWorklist;
    private final Set<Pair<SootMethod, Integer>> propagatedParameters;

    public InterproceduralConstantValuePropagator(InfoflowManager manager) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.removeSideEffectFreeMethods = true;
        this.excludeSystemClasses = true;
        this.methodSideEffects = new ConcurrentHashMap();
        this.methodSinks = CacheBuilder.newBuilder().build(new CacheLoader<SootMethod, Boolean>() { // from class: soot.jimple.infoflow.codeOptimization.InterproceduralConstantValuePropagator.1
            @Override // com.google.common.cache.CacheLoader
            public Boolean load(SootMethod key) throws Exception {
                if (InterproceduralConstantValuePropagator.this.sourceSinkManager != null && key.hasActiveBody()) {
                    Iterator<Unit> it = key.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        Stmt stmt = (Stmt) u;
                        if (stmt.containsInvokeExpr() && InterproceduralConstantValuePropagator.this.sourceSinkManager.getSinkInfo(stmt, InterproceduralConstantValuePropagator.this.manager, null) != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        this.exceptionClass = null;
        this.exceptionThrowers = new HashMap();
        this.propagationWorklist = new ArrayList();
        this.propagatedParameters = new HashSet();
        this.manager = manager;
        this.excludedMethods = null;
        this.sourceSinkManager = null;
        this.taintWrapper = null;
    }

    public InterproceduralConstantValuePropagator(InfoflowManager manager, Collection<SootMethod> excludedMethods, ISourceSinkManager sourceSinkManager, ITaintPropagationWrapper taintWrapper) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.removeSideEffectFreeMethods = true;
        this.excludeSystemClasses = true;
        this.methodSideEffects = new ConcurrentHashMap();
        this.methodSinks = CacheBuilder.newBuilder().build(new CacheLoader<SootMethod, Boolean>() { // from class: soot.jimple.infoflow.codeOptimization.InterproceduralConstantValuePropagator.1
            @Override // com.google.common.cache.CacheLoader
            public Boolean load(SootMethod key) throws Exception {
                if (InterproceduralConstantValuePropagator.this.sourceSinkManager != null && key.hasActiveBody()) {
                    Iterator<Unit> it = key.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit u = it.next();
                        Stmt stmt = (Stmt) u;
                        if (stmt.containsInvokeExpr() && InterproceduralConstantValuePropagator.this.sourceSinkManager.getSinkInfo(stmt, InterproceduralConstantValuePropagator.this.manager, null) != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        this.exceptionClass = null;
        this.exceptionThrowers = new HashMap();
        this.propagationWorklist = new ArrayList();
        this.propagatedParameters = new HashSet();
        this.manager = manager;
        this.excludedMethods = new HashSet(excludedMethods);
        this.sourceSinkManager = sourceSinkManager;
        this.taintWrapper = taintWrapper;
    }

    public void setRemoveSideEffectFreeMethods(boolean removeSideEffectFreeMethods) {
        this.removeSideEffectFreeMethods = removeSideEffectFreeMethods;
    }

    public void setExcludeSystemClasses(boolean excludeSystemClasses) {
        this.excludeSystemClasses = excludeSystemClasses;
    }

    private void checkAndAddMethod(SootMethod sm) {
        if (sm == null || !sm.hasActiveBody()) {
            return;
        }
        if (this.excludedMethods != null && this.excludedMethods.contains(sm)) {
            return;
        }
        if (this.excludeSystemClasses && SystemClassHandler.v().isClassInSystemPackage(sm.getDeclaringClass())) {
            return;
        }
        if ((sm.getReturnType() != VoidType.v() || sm.getParameterCount() > 0) && !this.propagationWorklist.contains(sm)) {
            this.propagationWorklist.add(sm);
        }
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        this.logger.info("Removing side-effect free methods is " + (this.removeSideEffectFreeMethods ? "enabled" : "disabled"));
        this.propagationWorklist.clear();
        this.propagatedParameters.clear();
        QueueReader<MethodOrMethodContext> rdr = Scene.v().getReachableMethods().listener();
        while (rdr.hasNext()) {
            MethodOrMethodContext mom = rdr.next();
            checkAndAddMethod(mom.method());
        }
        while (!this.propagationWorklist.isEmpty()) {
            SootMethod sm = this.propagationWorklist.remove(0);
            if (sm.getParameterCount() > 0) {
                propagateConstantsIntoCallee(sm);
            }
            if (typeSupportsConstants(sm.getReturnType())) {
                propagateReturnValueIntoCallers(sm);
            }
        }
        QueueReader<MethodOrMethodContext> rdr2 = Scene.v().getReachableMethods().listener();
        while (rdr2.hasNext()) {
            MethodOrMethodContext mom2 = rdr2.next();
            SootMethod sm2 = mom2.method();
            if (sm2.hasActiveBody()) {
                List<Unit> oldCallSites = DeadCodeEliminator.getCallsInMethod(sm2);
                Body body = sm2.retrieveActiveBody();
                ConditionalBranchFolder.v().transform(body);
                UnconditionalBranchFolder.v().transform(body);
                DeadAssignmentEliminator.v().transform(body);
                UnreachableCodeEliminator.v().transform(body);
                UnusedLocalEliminator.v().transform(body);
                DeadCodeEliminator.removeDeadCallgraphEdges(sm2, oldCallSites);
            }
        }
        if (this.removeSideEffectFreeMethods) {
            int callEdgesRemoved = 0;
            QueueReader<MethodOrMethodContext> rdr3 = Scene.v().getReachableMethods().listener();
            while (rdr3.hasNext()) {
                MethodOrMethodContext mom3 = rdr3.next();
                SootMethod sm3 = mom3.method();
                if (sm3 != null && sm3.hasActiveBody() && (this.excludedMethods == null || !this.excludedMethods.contains(sm3))) {
                    Iterator<Unit> unitIt = sm3.getActiveBody().getUnits().snapshotIterator();
                    while (unitIt.hasNext()) {
                        Stmt s = (Stmt) unitIt.next();
                        if (sm3.getActiveBody().getUnits().contains(s) && (s instanceof InvokeStmt) && (this.exceptionClass == null || s.getInvokeExpr().getMethod().getDeclaringClass() != this.exceptionClass)) {
                            if (getNonConstParamCount(s) <= 0) {
                                boolean allCalleesRemoved = true;
                                Set<SootClass> exceptions = new HashSet<>();
                                Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(s);
                                while (edgeIt.hasNext()) {
                                    Edge edge = edgeIt.next();
                                    SootMethod callee = edge.tgt();
                                    boolean remove = callee.getReturnType() == VoidType.v() && !hasSideEffectsOrReadsThis(callee);
                                    if (remove && (!hasSideEffectsOrCallsSink(callee))) {
                                        Scene.v().getCallGraph().removeEdge(edge);
                                        callEdgesRemoved++;
                                        fixExceptions(sm3, s, exceptions);
                                    } else if (!sm3.getName().equals("<clinit>")) {
                                        allCalleesRemoved = false;
                                    }
                                }
                                if (allCalleesRemoved && !isSourceSinkOrTaintWrapped(s)) {
                                    removeCallSite(s, sm3);
                                }
                            }
                        }
                    }
                }
            }
            this.logger.info("Removed %d call edges", Integer.valueOf(callEdgesRemoved));
        }
        if (this.exceptionClass != null) {
            Scene.v().releaseActiveHierarchy();
            Scene.v().releaseFastHierarchy();
            Scene.v().getOrMakeFastHierarchy();
        }
    }

    private int getNonConstParamCount(Stmt s) {
        int cnt = 0;
        for (Value val : s.getInvokeExpr().getArgs()) {
            if (!(val instanceof Constant)) {
                cnt++;
            }
        }
        return cnt;
    }

    private boolean isSourceSinkOrTaintWrapped(Stmt callSite) {
        if (!callSite.containsInvokeExpr()) {
            return false;
        }
        SootMethod method = callSite.getInvokeExpr().getMethod();
        if (this.sourceSinkManager != null && this.sourceSinkManager.getSourceInfo(callSite, this.manager) != null) {
            return true;
        }
        if (this.sourceSinkManager != null && this.sourceSinkManager.getSinkInfo(callSite, this.manager, null) != null) {
            return true;
        }
        if (this.taintWrapper != null && this.taintWrapper.supportsCallee(method)) {
            this.methodSideEffects.put(method, true);
            return true;
        }
        return false;
    }

    private void removeCallSite(Stmt callSite, SootMethod caller) {
        if (!caller.getActiveBody().getUnits().contains(callSite) || !callSite.containsInvokeExpr()) {
            return;
        }
        caller.getActiveBody().getUnits().remove(callSite);
        if (Scene.v().hasCallGraph()) {
            Scene.v().getCallGraph().removeAllEdgesOutOf(callSite);
        }
        this.manager.getICFG().notifyMethodChanged(caller);
    }

    private boolean typeSupportsConstants(Type returnType) {
        if (returnType == IntType.v() || returnType == LongType.v() || returnType == FloatType.v() || returnType == DoubleType.v()) {
            return true;
        }
        if ((returnType instanceof RefType) && ((RefType) returnType).getClassName().equals("java.lang.String")) {
            return true;
        }
        return false;
    }

    private void propagateReturnValueIntoCallers(SootMethod sm) {
        Collection<SootMethod> callees;
        IInfoflowCFG icfg = this.manager.getICFG();
        Constant value = null;
        for (Unit retSite : icfg.getEndPointsOf(sm)) {
            if (retSite instanceof ReturnStmt) {
                ReturnStmt retStmt = (ReturnStmt) retSite;
                if (!(retStmt.getOp() instanceof Constant)) {
                    return;
                }
                if (value != null && retStmt.getOp() != value) {
                    return;
                }
                value = (Constant) retStmt.getOp();
            }
        }
        if (value != null) {
            for (Unit callSite : icfg.getCallersOf(sm)) {
                if (callSite instanceof AssignStmt) {
                    AssignStmt assign = (AssignStmt) callSite;
                    if (this.taintWrapper == null || !this.taintWrapper.supportsCallee(assign)) {
                        if (this.sourceSinkManager == null || this.sourceSinkManager.getSourceInfo(assign, this.manager) == null) {
                            SootMethod caller = icfg.getMethodOf(assign);
                            if (caller != null && caller.getActiveBody().getUnits().contains(assign) && ((callees = icfg.getCalleesOfCallAt(callSite)) == null || callees.size() <= 1)) {
                                AssignStmt newAssignStmt = Jimple.v().newAssignStmt(assign.getLeftOp(), value);
                                if (!hasSideEffectsOrCallsSink(sm)) {
                                    fixExceptions(caller, callSite);
                                    caller.getActiveBody().getUnits().swapWith(assign, newAssignStmt);
                                    if (this.excludedMethods == null || !this.excludedMethods.contains(caller)) {
                                        ConstantPropagatorAndFolder.v().transform(caller.getActiveBody());
                                        checkAndAddMethod(caller);
                                    }
                                    if (Scene.v().hasCallGraph()) {
                                        Scene.v().getCallGraph().removeAllEdgesOutOf(assign);
                                    }
                                } else {
                                    caller.getActiveBody().getUnits().insertAfter(newAssignStmt, assign);
                                    if (this.excludedMethods == null || !this.excludedMethods.contains(caller)) {
                                        ConstantPropagatorAndFolder.v().transform(caller.getActiveBody());
                                        checkAndAddMethod(caller);
                                    }
                                    caller.getActiveBody().getUnits().remove(newAssignStmt);
                                    InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(assign.getInvokeExpr());
                                    caller.getActiveBody().getUnits().swapWith(assign, (AssignStmt) newInvokeStmt);
                                    if (Scene.v().hasCallGraph()) {
                                        Scene.v().getCallGraph().swapEdgesOutOf(assign, newInvokeStmt);
                                    }
                                    icfg.notifyMethodChanged(caller);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void fixExceptions(SootMethod caller, Unit callSite) {
        fixExceptions(caller, callSite, new HashSet());
    }

    private void fixExceptions(SootMethod caller, Unit callSite, Set<SootClass> doneSet) {
        ThrowAnalysis ta = Options.v().src_prec() == 5 ? DalvikThrowAnalysis.v() : UnitThrowAnalysis.v();
        ThrowableSet throwSet = ta.mightThrow(callSite);
        for (final Trap t : caller.getActiveBody().getTraps()) {
            if (doneSet.add(t.getException()) && throwSet.catchableAs(t.getException().getType())) {
                SootMethod thrower = this.exceptionThrowers.get(t.getException());
                if (thrower == null) {
                    if (this.exceptionClass == null) {
                        this.exceptionClass = Scene.v().makeSootClass("FLOWDROID_EXCEPTIONS", 1);
                        this.exceptionClass.setSuperclass(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_OBJECT));
                        this.exceptionClass.addTag(SimulatedCodeElementTag.TAG);
                        Scene.v().addClass(this.exceptionClass);
                    }
                    IEntryPointCreator epc = new BaseEntryPointCreator() { // from class: soot.jimple.infoflow.codeOptimization.InterproceduralConstantValuePropagator.2
                        @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
                        public Collection<String> getRequiredClasses() {
                            return Collections.emptySet();
                        }

                        @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
                        protected SootMethod createDummyMainInternal() {
                            LocalGenerator generator = Scene.v().createLocalGenerator(this.body);
                            Value intCounter = generator.generateLocal(IntType.v());
                            AssignStmt assignStmt = Jimple.v().newAssignStmt(intCounter, IntConstant.v(0));
                            this.body.getUnits().add((UnitPatchingChain) assignStmt);
                            ReturnVoidStmt newReturnVoidStmt = Jimple.v().newReturnVoidStmt();
                            IfStmt ifStmt = Jimple.v().newIfStmt(Jimple.v().newEqExpr(intCounter, IntConstant.v(0)), newReturnVoidStmt);
                            this.body.getUnits().add((UnitPatchingChain) ifStmt);
                            int i = 0 + 1;
                            Local lcEx = generator.generateLocal(t.getException().getType());
                            AssignStmt assignNewEx = Jimple.v().newAssignStmt(lcEx, Jimple.v().newNewExpr(t.getException().getType()));
                            this.body.getUnits().add((UnitPatchingChain) assignNewEx);
                            InvokeStmt consNewEx = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lcEx, Scene.v().makeConstructorRef(InterproceduralConstantValuePropagator.this.exceptionClass, Collections.emptyList())));
                            this.body.getUnits().add((UnitPatchingChain) consNewEx);
                            ThrowStmt throwNewEx = Jimple.v().newThrowStmt(lcEx);
                            this.body.getUnits().add((UnitPatchingChain) throwNewEx);
                            this.body.getUnits().add((UnitPatchingChain) newReturnVoidStmt);
                            this.mainMethod.addTag(SimulatedCodeElementTag.TAG);
                            return this.mainMethod;
                        }

                        @Override // soot.jimple.infoflow.entryPointCreators.BaseEntryPointCreator
                        protected void createEmptyMainMethod() {
                            String methodName;
                            int methodIdx = InterproceduralConstantValuePropagator.this.exceptionThrowers.size();
                            String baseName = "throw_" + t.getException().getName().replaceAll("\\W+", "_") + "_";
                            do {
                                int i = methodIdx;
                                methodIdx++;
                                methodName = String.valueOf(baseName) + i;
                            } while (InterproceduralConstantValuePropagator.this.exceptionClass.declaresMethodByName(methodName));
                            SootMethod thrower2 = Scene.v().makeSootMethod(methodName, Collections.emptyList(), VoidType.v());
                            thrower2.setModifiers(9);
                            Body body = Jimple.v().newBody(thrower2);
                            thrower2.setActiveBody(body);
                            InterproceduralConstantValuePropagator.this.exceptionThrowers.put(t.getException(), thrower2);
                            InterproceduralConstantValuePropagator.this.exceptionClass.addMethod(thrower2);
                            this.mainMethod = thrower2;
                        }

                        @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
                        public Collection<SootMethod> getAdditionalMethods() {
                            return null;
                        }

                        @Override // soot.jimple.infoflow.entryPointCreators.IEntryPointCreator
                        public Collection<SootField> getAdditionalFields() {
                            return null;
                        }
                    };
                    epc.createDummyMain();
                    thrower = epc.getGeneratedMainMethod();
                }
                InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(thrower.makeRef()));
                newInvokeStmt.addTag(SimulatedCodeElementTag.TAG);
                caller.getActiveBody().getUnits().insertBefore(newInvokeStmt, (InvokeStmt) callSite);
            }
        }
    }

    private boolean hasSideEffectsOrCallsSink(SootMethod method) {
        return hasSideEffectsOrCallsSink(method, new HashSet());
    }

    private boolean hasSideEffectsOrCallsSink(SootMethod method, Set<SootMethod> runList) {
        if (!method.hasActiveBody()) {
            return false;
        }
        Boolean hasSideEffects = this.methodSideEffects.get(method);
        if (hasSideEffects != null && hasSideEffects.booleanValue()) {
            return hasSideEffects.booleanValue();
        }
        Boolean hasSink = this.methodSinks.getUnchecked(method);
        if (hasSink != null && hasSink.booleanValue()) {
            return hasSink.booleanValue();
        }
        if (!runList.add(method)) {
            return false;
        }
        if (methodIsAndroidStub(method)) {
            this.methodSideEffects.put(method, false);
            return false;
        }
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                if ((assign.getLeftOp() instanceof FieldRef) || (assign.getLeftOp() instanceof ArrayRef)) {
                    this.methodSideEffects.put(method, true);
                    return true;
                }
            }
            Stmt s = (Stmt) u;
            if (this.taintWrapper != null && this.taintWrapper.supportsCallee(s)) {
                this.methodSideEffects.put(method, true);
                return true;
            } else if (s.containsInvokeExpr()) {
                if (this.sourceSinkManager != null && this.sourceSinkManager.getSinkInfo((Stmt) u, this.manager, null) != null) {
                    this.methodSinks.put(method, true);
                    return true;
                }
                Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(u);
                while (edgeIt.hasNext()) {
                    Edge e = edgeIt.next();
                    if (hasSideEffectsOrCallsSink(e.getTgt().method(), runList)) {
                        return true;
                    }
                }
                continue;
            }
        }
        this.methodSideEffects.put(method, false);
        return false;
    }

    private boolean hasSideEffectsOrReadsThis(SootMethod method) {
        return hasSideEffectsOrReadsThis(method, new HashSet());
    }

    private boolean hasSideEffectsOrReadsThis(SootMethod method, Set<SootMethod> runList) {
        if (!method.hasActiveBody()) {
            return false;
        }
        Boolean hasSideEffects = this.methodSideEffects.get(method);
        if (hasSideEffects != null && hasSideEffects.booleanValue()) {
            return hasSideEffects.booleanValue();
        }
        if (!runList.add(method)) {
            return false;
        }
        if (methodIsAndroidStub(method)) {
            this.methodSideEffects.put(method, false);
            return false;
        }
        Local thisLocal = method.isStatic() ? null : method.getActiveBody().getThisLocal();
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                if ((assign.getLeftOp() instanceof FieldRef) || (assign.getLeftOp() instanceof ArrayRef)) {
                    this.methodSideEffects.put(method, true);
                    return true;
                }
            }
            Stmt s = (Stmt) u;
            if (thisLocal != null) {
                for (ValueBox vb : s.getUseBoxes()) {
                    if (vb.getValue() == thisLocal) {
                        return true;
                    }
                }
            }
            if (s.containsInvokeExpr()) {
                Iterator<Edge> edgeIt = Scene.v().getCallGraph().edgesOutOf(u);
                while (edgeIt.hasNext()) {
                    Edge e = edgeIt.next();
                    if (hasSideEffectsOrReadsThis(e.getTgt().method(), runList)) {
                        return true;
                    }
                }
                continue;
            }
        }
        this.methodSideEffects.put(method, false);
        return false;
    }

    private boolean methodIsAndroidStub(SootMethod method) {
        if (Options.v().src_prec() != 5 || !method.getDeclaringClass().isLibraryClass() || !SystemClassHandler.v().isClassInSystemPackage(method.getDeclaringClass())) {
            return false;
        }
        Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof DefinitionStmt) {
                DefinitionStmt defStmt = (DefinitionStmt) u;
                if (!(defStmt.getRightOp() instanceof ThisRef) && !(defStmt.getRightOp() instanceof ParameterRef) && !(defStmt.getRightOp() instanceof NewExpr)) {
                    return false;
                }
            } else if (u instanceof InvokeStmt) {
                InvokeStmt stmt = (InvokeStmt) u;
                SootMethod callee = stmt.getInvokeExpr().getMethod();
                if (!callee.getSubSignature().equals("void <init>(java.lang.String)") && (!method.getDeclaringClass().hasSuperclass() || callee.getDeclaringClass() != method.getDeclaringClass().getSuperclass() || !callee.isConstructor())) {
                    return false;
                }
            } else if (!(u instanceof ThrowStmt)) {
                return false;
            }
        }
        return true;
    }

    private void propagateConstantsIntoCallee(SootMethod sm) {
        IInfoflowCFG icfg = this.manager.getICFG();
        Collection<Unit> callSites = icfg.getCallersOf(sm);
        if (callSites.isEmpty()) {
            return;
        }
        boolean[] isConstant = new boolean[sm.getParameterCount()];
        Constant[] values = new Constant[sm.getParameterCount()];
        for (int i = 0; i < isConstant.length; i++) {
            isConstant[i] = true;
        }
        boolean hasCallSites = false;
        for (Unit callSite : callSites) {
            if (this.excludedMethods != null && icfg.isReachable(callSite)) {
                SootMethod caller = icfg.getMethodOf(callSite);
                if (this.excludedMethods.contains(caller) || caller.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
                    this.logger.trace("Ignoring calls from {}", caller);
                }
            }
            InvokeExpr iiExpr = ((Stmt) callSite).getInvokeExpr();
            if (iiExpr.getArgCount() == sm.getParameterCount()) {
                hasCallSites = true;
                if (icfg.isReflectiveCallSite(callSite)) {
                    for (int i2 = 0; i2 < isConstant.length; i2++) {
                        isConstant[i2] = false;
                    }
                } else {
                    for (int i3 = 0; i3 < isConstant.length; i3++) {
                        if (isConstant[i3]) {
                            Value argVal = iiExpr.getArg(i3);
                            if (argVal instanceof Constant) {
                                if (values[i3] != null && !values[i3].equals(argVal)) {
                                    isConstant[i3] = false;
                                } else {
                                    values[i3] = (Constant) argVal;
                                }
                            } else {
                                isConstant[i3] = false;
                            }
                        }
                    }
                }
            }
        }
        if (hasCallSites) {
            List<Unit> inserted = null;
            for (int i4 = 0; i4 < isConstant.length; i4++) {
                if (isConstant[i4] && values[i4] != null && this.propagatedParameters.add(new Pair<>(sm, Integer.valueOf(i4)))) {
                    Local paramLocal = sm.getActiveBody().getParameterLocal(i4);
                    Unit point = getFirstNonIdentityStmt(sm);
                    AssignStmt newAssignStmt = Jimple.v().newAssignStmt(paramLocal, values[i4]);
                    sm.getActiveBody().getUnits().insertBefore(newAssignStmt, (AssignStmt) point);
                    if (inserted == null) {
                        inserted = new ArrayList<>();
                    }
                    inserted.add(newAssignStmt);
                }
            }
            if (inserted != null) {
                ConstantPropagatorAndFolder.v().transform(sm.getActiveBody());
                for (Unit u : inserted) {
                    sm.getActiveBody().getUnits().remove(u);
                }
                Iterator<Unit> it = sm.getActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit u2 = it.next();
                    for (SootMethod callee : icfg.getCalleesOfCallAt(u2)) {
                        checkAndAddMethod(callee);
                    }
                }
            }
        }
    }

    private Unit getFirstNonIdentityStmt(SootMethod sm) {
        Iterator<Unit> it = sm.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (!(u instanceof IdentityStmt)) {
                return u;
            }
            IdentityStmt id = (IdentityStmt) u;
            if (!(id.getRightOp() instanceof ThisRef) && !(id.getRightOp() instanceof ParameterRef)) {
                return u;
            }
        }
        return null;
    }
}
