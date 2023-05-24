package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.SynchronizedBy;
import heros.solver.IDESolver;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.FastHierarchy;
import soot.Local;
import soot.Main;
import soot.NullType;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.Unit;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.toolkits.pointer.LocalMustNotAliasAnalysis;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/OnTheFlyJimpleBasedICFG.class */
public class OnTheFlyJimpleBasedICFG extends AbstractJimpleBasedICFG {
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected final LoadingCache<Body, LocalMustNotAliasAnalysis> bodyToLMNAA;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected final LoadingCache<Unit, Set<SootMethod>> unitToCallees;
    @SynchronizedBy("explicit lock on data structure")
    protected Map<SootMethod, Set<Unit>> methodToCallers;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !OnTheFlyJimpleBasedICFG.class.desiredAssertionStatus();
    }

    public OnTheFlyJimpleBasedICFG(SootMethod... entryPoints) {
        this(Arrays.asList(entryPoints));
    }

    public OnTheFlyJimpleBasedICFG(Collection<SootMethod> entryPoints) {
        this.bodyToLMNAA = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Body, LocalMustNotAliasAnalysis>() { // from class: soot.jimple.toolkits.ide.icfg.OnTheFlyJimpleBasedICFG.1
            @Override // com.google.common.cache.CacheLoader
            public LocalMustNotAliasAnalysis load(Body body) throws Exception {
                return new LocalMustNotAliasAnalysis(OnTheFlyJimpleBasedICFG.this.getOrCreateUnitGraph(body), body);
            }
        });
        this.unitToCallees = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Unit, Set<SootMethod>>() { // from class: soot.jimple.toolkits.ide.icfg.OnTheFlyJimpleBasedICFG.2
            @Override // com.google.common.cache.CacheLoader
            public Set<SootMethod> load(Unit u) throws Exception {
                SootClass baseTypeClass;
                Stmt stmt = (Stmt) u;
                InvokeExpr ie = stmt.getInvokeExpr();
                FastHierarchy fastHierarchy = Scene.v().getFastHierarchy();
                if (ie instanceof InstanceInvokeExpr) {
                    if (ie instanceof SpecialInvokeExpr) {
                        return Collections.singleton(ie.getMethod());
                    }
                    InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                    Local base = (Local) iie.getBase();
                    RefType concreteType = OnTheFlyJimpleBasedICFG.this.bodyToLMNAA.getUnchecked(OnTheFlyJimpleBasedICFG.this.getBodyOf(u)).concreteType(base, stmt);
                    if (concreteType != null) {
                        SootMethod singleTargetMethod = fastHierarchy.resolveConcreteDispatch(concreteType.getSootClass(), iie.getMethod());
                        return Collections.singleton(singleTargetMethod);
                    }
                    if (base.getType() instanceof RefType) {
                        RefType refType = (RefType) base.getType();
                        baseTypeClass = refType.getSootClass();
                    } else if (!(base.getType() instanceof ArrayType)) {
                        if (base.getType() instanceof NullType) {
                            return Collections.emptySet();
                        }
                        throw new InternalError("Unexpected base type:" + base.getType());
                    } else {
                        baseTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
                    }
                    return fastHierarchy.resolveAbstractDispatch(baseTypeClass, iie.getMethod());
                }
                return Collections.singleton(ie.getMethod());
            }
        });
        this.methodToCallers = new HashMap();
        for (SootMethod m : entryPoints) {
            initForMethod(m);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [soot.Scene] */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v16 */
    protected Body initForMethod(SootMethod m) {
        if ($assertionsDisabled || Scene.v().hasFastHierarchy()) {
            Body b = null;
            if (m.isConcrete()) {
                SootClass declaringClass = m.getDeclaringClass();
                ensureClassHasBodies(declaringClass);
                ?? v = Scene.v();
                synchronized (v) {
                    b = m.retrieveActiveBody();
                    v = v;
                    if (b != null) {
                        Iterator<Unit> it = b.getUnits().iterator();
                        while (it.hasNext()) {
                            Unit u = it.next();
                            if (!setOwnerStatement(u, b)) {
                                break;
                            }
                        }
                    }
                }
            }
            if ($assertionsDisabled || Scene.v().hasFastHierarchy()) {
                return b;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    private synchronized void ensureClassHasBodies(SootClass cl) {
        if (!$assertionsDisabled && !Scene.v().hasFastHierarchy()) {
            throw new AssertionError();
        }
        if (cl.resolvingLevel() < 3) {
            Scene.v().forceResolve(cl.getName(), 3);
            Scene.v().getOrMakeFastHierarchy();
        }
        if (!$assertionsDisabled && !Scene.v().hasFastHierarchy()) {
            throw new AssertionError();
        }
    }

    @Override // heros.InterproceduralCFG
    public Set<SootMethod> getCalleesOfCallAt(Unit u) {
        Set<SootMethod> targets = this.unitToCallees.getUnchecked(u);
        for (SootMethod m : targets) {
            addCallerForMethod(u, m);
            initForMethod(m);
        }
        return targets;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Map<soot.SootMethod, java.util.Set<soot.Unit>>] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Throwable] */
    private void addCallerForMethod(Unit callSite, SootMethod target) {
        ?? r0 = this.methodToCallers;
        synchronized (r0) {
            Set<Unit> callers = this.methodToCallers.get(target);
            Set<Unit> callers2 = callers;
            if (callers == null) {
                Set<Unit> callers3 = new HashSet<>();
                this.methodToCallers.put(target, callers3);
                callers2 = callers3;
            }
            callers2.add(callSite);
            r0 = r0;
        }
    }

    @Override // heros.InterproceduralCFG
    public Set<Unit> getCallersOf(SootMethod m) {
        Set<Unit> callers = this.methodToCallers.get(m);
        return callers == null ? Collections.emptySet() : callers;
    }

    public static void loadAllClassesOnClassPathToSignatures() {
        for (String path : SourceLocator.explodeClassPath(Scene.v().getSootClassPath())) {
            for (String cl : SourceLocator.v().getClassesUnder(path)) {
                Scene.v().forceResolve(cl, 2);
            }
        }
    }

    public static void main(String[] args) {
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.onflyicfg", new SceneTransformer() { // from class: soot.jimple.toolkits.ide.icfg.OnTheFlyJimpleBasedICFG.3
            @Override // soot.SceneTransformer
            protected void internalTransform(String phaseName, Map<String, String> options) {
                if (Scene.v().hasCallGraph()) {
                    throw new RuntimeException("call graph present!");
                }
                OnTheFlyJimpleBasedICFG.loadAllClassesOnClassPathToSignatures();
                SootMethod mainMethod = Scene.v().getMainMethod();
                OnTheFlyJimpleBasedICFG icfg = new OnTheFlyJimpleBasedICFG(mainMethod);
                Set<SootMethod> worklist = new LinkedHashSet<>();
                Set<SootMethod> visited = new HashSet<>();
                worklist.add(mainMethod);
                int monomorphic = 0;
                int polymorphic = 0;
                while (!worklist.isEmpty()) {
                    Iterator<SootMethod> iter = worklist.iterator();
                    SootMethod currMethod = iter.next();
                    iter.remove();
                    visited.add(currMethod);
                    System.err.println(currMethod);
                    Body body = currMethod.getActiveBody();
                    if (body != null) {
                        Iterator<Unit> it = body.getUnits().iterator();
                        while (it.hasNext()) {
                            Unit u = it.next();
                            Stmt s = (Stmt) u;
                            if (s.containsInvokeExpr()) {
                                Set<SootMethod> calleesOfCallAt = icfg.getCalleesOfCallAt((Unit) s);
                                if ((s.getInvokeExpr() instanceof VirtualInvokeExpr) || (s.getInvokeExpr() instanceof InterfaceInvokeExpr)) {
                                    if (calleesOfCallAt.size() <= 1) {
                                        monomorphic++;
                                    } else {
                                        polymorphic++;
                                    }
                                    System.err.println("mono: " + monomorphic + "   poly: " + polymorphic);
                                }
                                for (SootMethod callee : calleesOfCallAt) {
                                    if (!visited.contains(callee)) {
                                        System.err.println(callee);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));
        Options.v().set_on_the_fly(true);
        Main.main(args);
    }
}
