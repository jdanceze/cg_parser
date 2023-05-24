package soot.jimple.infoflow.android.callbacks;

import heros.solver.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.callbacks.AndroidCallbackDefinition;
import soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointConstants;
import soot.jimple.infoflow.android.entryPointCreators.AndroidEntryPointUtils;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/DefaultCallbackAnalyzer.class */
public class DefaultCallbackAnalyzer extends AbstractCallbackAnalyzer implements IMemoryBoundedSolver {
    private MultiMap<SootClass, SootMethod> callbackWorklist;
    private AndroidEntryPointUtils entryPointUtils;
    private Set<IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification> notificationListeners;
    private ISolverTerminationReason isKilled;
    private MultiMap<SootClass, AndroidCallbackDefinition> viewCallbacks;
    QueueReader<MethodOrMethodContext> reachableChangedListener;
    Iterator<MethodOrMethodContext> rmIterator;

    public DefaultCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses) throws IOException {
        super(config, entryPointClasses);
        this.callbackWorklist = null;
        this.entryPointUtils = new AndroidEntryPointUtils();
        this.notificationListeners = new HashSet();
        this.isKilled = null;
    }

    public DefaultCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, MultiMap<SootClass, AndroidCallbackDefinition> viewCallbacks, String callbackFile) throws IOException {
        super(config, entryPointClasses, callbackFile);
        this.callbackWorklist = null;
        this.entryPointUtils = new AndroidEntryPointUtils();
        this.notificationListeners = new HashSet();
        this.isKilled = null;
        this.viewCallbacks = viewCallbacks;
    }

    public DefaultCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, MultiMap<SootClass, AndroidCallbackDefinition> viewCallbacks, Set<String> androidCallbacks) throws IOException {
        super(config, entryPointClasses, androidCallbacks);
        this.callbackWorklist = null;
        this.entryPointUtils = new AndroidEntryPointUtils();
        this.notificationListeners = new HashSet();
        this.isKilled = null;
        this.viewCallbacks = viewCallbacks;
    }

    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public void collectCallbackMethods() {
        super.collectCallbackMethods();
        Transform transform = new Transform("wjtp.ajc", new SceneTransformer() { // from class: soot.jimple.infoflow.android.callbacks.DefaultCallbackAnalyzer.1
            @Override // soot.SceneTransformer
            protected void internalTransform(String phaseName, Map options) {
                for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener : DefaultCallbackAnalyzer.this.notificationListeners) {
                    listener.notifySolverStarted(DefaultCallbackAnalyzer.this);
                }
                if (DefaultCallbackAnalyzer.this.callbackWorklist == null) {
                    DefaultCallbackAnalyzer.this.logger.info("Collecting callbacks in DEFAULT mode...");
                    DefaultCallbackAnalyzer.this.callbackWorklist = new HashMultiMap();
                    DefaultCallbackAnalyzer.this.findClassLayoutMappings();
                    for (SootClass sc : DefaultCallbackAnalyzer.this.entryPointClasses) {
                        if (DefaultCallbackAnalyzer.this.isKilled != null) {
                            break;
                        }
                        List<MethodOrMethodContext> methods = new ArrayList<>(DefaultCallbackAnalyzer.this.entryPointUtils.getLifecycleMethods(sc));
                        DefaultCallbackAnalyzer.this.analyzeReachableMethods(sc, methods);
                        DefaultCallbackAnalyzer.this.analyzeMethodOverrideCallbacks(sc);
                        DefaultCallbackAnalyzer.this.analyzeClassInterfaceCallbacks(sc, sc, sc);
                    }
                    DefaultCallbackAnalyzer.this.reachableChangedListener = Scene.v().getReachableMethods().listener();
                    DefaultCallbackAnalyzer.this.logger.info("Callback analysis done.");
                } else {
                    DefaultCallbackAnalyzer.this.findClassLayoutMappings();
                    MultiMap<SootMethod, SootClass> reverseViewCallbacks = new HashMultiMap<>();
                    for (Pair<SootClass, AndroidCallbackDefinition> i : DefaultCallbackAnalyzer.this.viewCallbacks) {
                        reverseViewCallbacks.put(i.getO2().getTargetMethod(), i.getO1());
                    }
                    while (DefaultCallbackAnalyzer.this.reachableChangedListener.hasNext()) {
                        SootMethod m = DefaultCallbackAnalyzer.this.reachableChangedListener.next().method();
                        Set<SootClass> o = reverseViewCallbacks.get(m);
                        for (SootClass i2 : o) {
                            DefaultCallbackAnalyzer.this.callbackWorklist.put(i2, m);
                        }
                    }
                    DefaultCallbackAnalyzer.this.logger.info(String.format("Running incremental callback analysis for %d components...", Integer.valueOf(DefaultCallbackAnalyzer.this.callbackWorklist.size())));
                    MultiMap<SootClass, SootMethod> workList = new HashMultiMap<>(DefaultCallbackAnalyzer.this.callbackWorklist);
                    Iterator<SootClass> it = workList.keySet().iterator();
                    while (it.hasNext() && DefaultCallbackAnalyzer.this.isKilled == null) {
                        SootClass componentClass = it.next();
                        Set<SootMethod> callbacks = DefaultCallbackAnalyzer.this.callbackWorklist.get(componentClass);
                        DefaultCallbackAnalyzer.this.callbackWorklist.remove(componentClass);
                        Set<SootClass> activityComponents = DefaultCallbackAnalyzer.this.fragmentClassesRev.get(componentClass);
                        if (activityComponents == null || activityComponents.isEmpty()) {
                            activityComponents = Collections.singleton(componentClass);
                        }
                        if (DefaultCallbackAnalyzer.this.config.getCallbackConfig().getMaxCallbacksPerComponent() > 0 && callbacks.size() > DefaultCallbackAnalyzer.this.config.getCallbackConfig().getMaxCallbacksPerComponent()) {
                            DefaultCallbackAnalyzer.this.callbackMethods.remove(componentClass);
                            DefaultCallbackAnalyzer.this.entryPointClasses.remove(componentClass);
                        } else {
                            DefaultCallbackAnalyzer.this.analyzeMethodOverrideCallbacks(componentClass);
                            for (SootClass activityComponent : activityComponents) {
                                if (activityComponent == null) {
                                    activityComponent = componentClass;
                                }
                                DefaultCallbackAnalyzer.this.analyzeClassInterfaceCallbacks(componentClass, componentClass, activityComponent);
                            }
                            List<MethodOrMethodContext> entryClasses = new ArrayList<>(callbacks.size());
                            for (SootMethod sm : callbacks) {
                                if (sm != null) {
                                    entryClasses.add(sm);
                                }
                            }
                            DefaultCallbackAnalyzer.this.analyzeReachableMethods(componentClass, entryClasses);
                        }
                    }
                    DefaultCallbackAnalyzer.this.logger.info("Incremental callback analysis done.");
                }
                for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener2 : DefaultCallbackAnalyzer.this.notificationListeners) {
                    listener2.notifySolverTerminated(DefaultCallbackAnalyzer.this);
                }
            }
        });
        PackManager.v().getPack("wjtp").add(transform);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void analyzeReachableMethods(SootClass lifecycleElement, List<MethodOrMethodContext> methods) {
        ComponentReachableMethods rm = new ComponentReachableMethods(this.config, lifecycleElement, methods);
        rm.update();
        QueueReader<MethodOrMethodContext> reachableMethods = rm.listener();
        while (reachableMethods.hasNext() && this.isKilled == null) {
            for (ICallbackFilter filter : this.callbackFilters) {
                filter.setReachableMethods(rm);
            }
            SootMethod method = reachableMethods.next().method();
            if (method.isConcrete()) {
                analyzeMethodForCallbackRegistrations(lifecycleElement, method);
                analyzeMethodForDynamicBroadcastReceiver(method);
                analyzeMethodForServiceConnection(method);
                analyzeMethodForFragmentTransaction(lifecycleElement, method);
                analyzeMethodForViewPagers(lifecycleElement, method);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public boolean checkAndAddMethod(SootMethod method, SootMethod parentMethod, SootClass lifecycleClass, AndroidCallbackDefinition.CallbackType callbackType) {
        if (!this.excludedEntryPoints.contains(lifecycleClass) && super.checkAndAddMethod(method, parentMethod, lifecycleClass, callbackType)) {
            this.callbackWorklist.put(lifecycleClass, method);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public void checkAndAddFragment(SootClass componentClass, SootClass fragmentClass) {
        if (!this.excludedEntryPoints.contains(componentClass)) {
            super.checkAndAddFragment(componentClass, fragmentClass);
            for (SootMethod sm : fragmentClass.getMethods()) {
                if (sm.isConstructor() || AndroidEntryPointConstants.getFragmentLifecycleMethods().contains(sm.getSubSignature())) {
                    this.callbackWorklist.put(fragmentClass, sm);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void findClassLayoutMappings() {
        Integer intValue;
        if (this.rmIterator == null) {
            this.rmIterator = Scene.v().getReachableMethods().listener();
        }
        while (this.rmIterator.hasNext()) {
            SootMethod sm = this.rmIterator.next().method();
            if (sm.isConcrete() && !SystemClassHandler.v().isClassInSystemPackage(sm.getDeclaringClass().getName())) {
                RefType.v(AndroidEntryPointConstants.FRAGMENTCLASS);
                Iterator<Unit> it = sm.retrieveActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    if (u instanceof Stmt) {
                        Stmt stmt = (Stmt) u;
                        if (stmt.containsInvokeExpr()) {
                            InvokeExpr inv = stmt.getInvokeExpr();
                            if (invokesSetContentView(inv)) {
                                for (Value val : inv.getArgs()) {
                                    Integer intValue2 = (Integer) this.valueProvider.getValue(sm, stmt, val, Integer.class);
                                    if (intValue2 != null) {
                                        this.layoutClasses.put(sm.getDeclaringClass(), intValue2);
                                    }
                                }
                            }
                            if (invokesInflate(inv) && (intValue = (Integer) this.valueProvider.getValue(sm, stmt, inv.getArg(0), Integer.class)) != null) {
                                this.layoutClasses.put(sm.getDeclaringClass(), intValue);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
        this.isKilled = reason;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isTerminated() {
        return this.isKilled != null;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isKilled() {
        return this.isKilled != null;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void reset() {
        this.isKilled = null;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
        this.notificationListeners.add(listener);
    }

    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public void excludeEntryPoint(SootClass entryPoint) {
        super.excludeEntryPoint(entryPoint);
        this.callbackWorklist.remove(entryPoint);
        this.callbackMethods.remove(entryPoint);
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public ISolverTerminationReason getTerminationReason() {
        return this.isKilled;
    }
}
