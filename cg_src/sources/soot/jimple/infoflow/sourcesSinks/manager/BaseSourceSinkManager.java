package soot.jimple.infoflow.sourcesSinks.manager;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.istack.NotNull;
import heros.solver.IDESolver;
import heros.solver.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.coffi.Instruction;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.callbacks.CallbackDefinition;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.FieldSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.StatementSourceSinkDefinition;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.infoflow.values.IValueProvider;
import soot.jimple.infoflow.values.SimpleConstantValueProvider;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/BaseSourceSinkManager.class */
public abstract class BaseSourceSinkManager implements IReversibleSourceSinkManager, IOneSourceAtATimeManager {
    private static final String GLOBAL_SIG = "--GLOBAL--";
    private final Logger logger;
    protected MultiMap<String, ISourceSinkDefinition> sourceDefs;
    protected MultiMap<String, ISourceSinkDefinition> sinkDefs;
    protected Map<SootMethod, ISourceSinkDefinition> sourceMethods;
    protected Map<Stmt, ISourceSinkDefinition> sourceStatements;
    protected Map<SootMethod, ISourceSinkDefinition> sinkMethods;
    protected Map<SootMethod, ISourceSinkDefinition> sinkReturnMethods;
    protected Map<SootMethod, CallbackDefinition> callbackMethods;
    protected Map<SootField, ISourceSinkDefinition> sourceFields;
    protected Map<SootField, ISourceSinkDefinition> sinkFields;
    protected Map<Stmt, ISourceSinkDefinition> sinkStatements;
    protected final InfoflowConfiguration.SourceSinkConfiguration sourceSinkConfig;
    protected final Set<SootMethod> excludedMethods;
    protected boolean oneSourceAtATime;
    protected SourceType osaatType;
    protected Iterator<SootMethod> osaatIterator;
    protected SootMethod currentSource;
    protected IValueProvider valueProvider;
    protected final LoadingCache<SootClass, Collection<SootClass>> parentClassesAndInterfaces;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/BaseSourceSinkManager$SourceType.class */
    public enum SourceType {
        NoSource,
        MethodCall,
        Callback,
        UISource;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SourceType[] valuesCustom() {
            SourceType[] valuesCustom = values();
            int length = valuesCustom.length;
            SourceType[] sourceTypeArr = new SourceType[length];
            System.arraycopy(valuesCustom, 0, sourceTypeArr, 0, length);
            return sourceTypeArr;
        }
    }

    protected abstract boolean isEntryPointMethod(SootMethod sootMethod);

    static {
        $assertionsDisabled = !BaseSourceSinkManager.class.desiredAssertionStatus();
    }

    public BaseSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, InfoflowConfiguration config) {
        this(sources, sinks, Collections.emptySet(), config);
    }

    public BaseSourceSinkManager(Collection<? extends ISourceSinkDefinition> sources, Collection<? extends ISourceSinkDefinition> sinks, Set<? extends CallbackDefinition> callbackMethods, InfoflowConfiguration config) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.excludedMethods = new HashSet();
        this.oneSourceAtATime = false;
        this.osaatType = SourceType.MethodCall;
        this.osaatIterator = null;
        this.currentSource = null;
        this.valueProvider = new SimpleConstantValueProvider();
        this.parentClassesAndInterfaces = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootClass, Collection<SootClass>>() { // from class: soot.jimple.infoflow.sourcesSinks.manager.BaseSourceSinkManager.1
            @Override // com.google.common.cache.CacheLoader
            public Collection<SootClass> load(SootClass sc) throws Exception {
                Hierarchy h = Scene.v().getActiveHierarchy();
                if (sc.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
                    return Collections.emptySet();
                }
                if (sc.isInterface()) {
                    return h.getSuperinterfacesOfIncluding(sc);
                }
                Set<SootClass> res = new HashSet<>();
                res.addAll(h.getSuperclassesOfIncluding(sc));
                res.addAll((Collection) res.stream().flatMap(c -> {
                    return c.getInterfaces().stream();
                }).flatMap(i -> {
                    try {
                        return BaseSourceSinkManager.this.parentClassesAndInterfaces.get(i).stream();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet()));
                return res;
            }
        });
        this.sourceSinkConfig = config.getSourceSinkConfig();
        this.sourceDefs = new HashMultiMap();
        for (ISourceSinkDefinition am : sources) {
            this.sourceDefs.put(getSignature(am), am);
        }
        this.sinkDefs = new HashMultiMap();
        for (ISourceSinkDefinition am2 : sinks) {
            this.sinkDefs.put(getSignature(am2), am2);
        }
        this.callbackMethods = new HashMap();
        for (CallbackDefinition cb : callbackMethods) {
            this.callbackMethods.put(cb.getTargetMethod(), cb);
        }
        this.logger.info(String.format("Created a SourceSinkManager with %d sources, %d sinks, and %d callback methods.", Integer.valueOf(this.sourceDefs.size()), Integer.valueOf(this.sinkDefs.size()), Integer.valueOf(this.callbackMethods.size())));
    }

    private String getSignature(ISourceSinkDefinition am) {
        if (am instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition methodSource = (MethodSourceSinkDefinition) am;
            return methodSource.getMethod().getSignature();
        } else if (am instanceof FieldSourceSinkDefinition) {
            FieldSourceSinkDefinition fieldSource = (FieldSourceSinkDefinition) am;
            return fieldSource.getFieldSignature();
        } else if (am instanceof StatementSourceSinkDefinition) {
            return GLOBAL_SIG;
        } else {
            throw new RuntimeException(String.format("Invalid type of source/sink definition: %s", am.getClass().getName()));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ISourceSinkDefinition getSinkDefinition(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        ISourceSinkDefinition def;
        ISourceSinkDefinition def2 = this.sinkStatements.get(sCallSite);
        if (def2 != null) {
            return def2;
        }
        if (sCallSite.containsInvokeExpr()) {
            SootMethod callee = sCallSite.getInvokeExpr().getMethod();
            if (!SystemClassHandler.v().isTaintVisible(ap, callee)) {
                return null;
            }
            ISourceSinkDefinition def3 = this.sinkMethods.get(sCallSite.getInvokeExpr().getMethod());
            if (def3 != null) {
                return def3;
            }
            String subSig = callee.getSubSignature();
            for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(sCallSite.getInvokeExpr().getMethod().getDeclaringClass())) {
                if (i.declaresMethod(subSig) && (def = this.sinkMethods.get(i.getMethod(subSig))) != null) {
                    return def;
                }
            }
            for (SootMethod sm : manager.getICFG().getCalleesOfCallAt(sCallSite)) {
                ISourceSinkDefinition def4 = this.sinkMethods.get(sm);
                if (def4 != null) {
                    return def4;
                }
            }
            return null;
        } else if (sCallSite instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) sCallSite;
            if (assignStmt.getLeftOp() instanceof FieldRef) {
                FieldRef fieldRef = (FieldRef) assignStmt.getLeftOp();
                ISourceSinkDefinition def5 = this.sinkFields.get(fieldRef.getField());
                if (def5 != null) {
                    return def5;
                }
                return null;
            }
            return null;
        } else if (sCallSite instanceof ReturnStmt) {
            return this.sinkReturnMethods.get(manager.getICFG().getMethodOf(sCallSite));
        } else {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ISourceSinkDefinition getInverseSource(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        ISourceSinkDefinition def;
        ISourceSinkDefinition def2 = this.sourceStatements.get(sCallSite);
        if (def2 != null) {
            return def2;
        }
        if (sCallSite.containsInvokeExpr()) {
            SootMethod callee = sCallSite.getInvokeExpr().getMethod();
            if (!SystemClassHandler.v().isTaintVisible(ap, callee)) {
                return null;
            }
            ISourceSinkDefinition def3 = getSourceDefinition(callee);
            if (def3 != null) {
                return def3;
            }
            String subSig = callee.getSubSignature();
            for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(callee.getDeclaringClass())) {
                SootMethod m = i.getMethodUnsafe(subSig);
                if (m != null && (def = getSourceDefinition(m)) != null) {
                    return def;
                }
            }
            for (SootMethod sm : manager.getICFG().getCalleesOfCallAt(sCallSite)) {
                ISourceSinkDefinition def4 = getSourceDefinition(sm);
                if (def4 != null) {
                    return def4;
                }
            }
        }
        ISourceSinkDefinition def5 = getUISourceDefinition(sCallSite, manager.getICFG());
        if (def5 != null) {
            return def5;
        }
        ISourceSinkDefinition def6 = checkCallbackParamSource(sCallSite, manager.getICFG());
        if (def6 != null) {
            return def6;
        }
        ISourceSinkDefinition def7 = checkFieldSource(sCallSite, manager.getICFG());
        if (def7 != null) {
            return def7;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SinkInfo getSinkInfo(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        ISourceSinkDefinition def;
        if (this.excludedMethods.contains(manager.getICFG().getMethodOf(sCallSite)) || sCallSite.hasTag(SimulatedCodeElementTag.TAG_NAME) || (def = getSinkDefinition(sCallSite, manager, ap)) == null) {
            return null;
        }
        return new SinkInfo(def);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SinkInfo getInverseSourceInfo(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        if (this.oneSourceAtATime) {
            this.logger.error("This does not support one source at a time for inverse methods.");
            return null;
        }
        ISourceSinkDefinition def = getInverseSource(sCallSite, manager, ap);
        if (def == null) {
            return null;
        }
        return new SinkInfo(def);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SourceInfo getSourceInfo(Stmt sCallSite, InfoflowManager manager) {
        if (this.excludedMethods.contains(manager.getICFG().getMethodOf(sCallSite)) || sCallSite.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
            return null;
        }
        ISourceSinkDefinition def = getSource(sCallSite, manager.getICFG());
        return createSourceInfo(sCallSite, manager, def);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SourceInfo getInverseSinkInfo(Stmt sCallSite, InfoflowManager manager) {
        if (this.oneSourceAtATime) {
            this.logger.error("This does not support one source at a time for inverse methods.");
            return null;
        } else if (this.excludedMethods.contains(manager.getICFG().getMethodOf(sCallSite)) || sCallSite.hasTag(SimulatedCodeElementTag.TAG_NAME)) {
            return null;
        } else {
            ISourceSinkDefinition def = getInverseSink(sCallSite, manager.getICFG());
            return createInverseSinkInfo(sCallSite, manager, def);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SourceInfo createSourceInfo(Stmt sCallSite, InfoflowManager manager, ISourceSinkDefinition def) {
        if (def == null) {
            return null;
        }
        if (!sCallSite.containsInvokeExpr()) {
            if (sCallSite instanceof DefinitionStmt) {
                DefinitionStmt defStmt = (DefinitionStmt) sCallSite;
                return new SourceInfo(def, manager.getAccessPathFactory().createAccessPath(defStmt.getLeftOp(), null, null, true, false, true, AccessPath.ArrayTaintType.ContentsAndLength, false));
            }
            return null;
        }
        InvokeExpr iexpr = sCallSite.getInvokeExpr();
        Type returnType = iexpr.getMethod().getReturnType();
        if ((sCallSite instanceof DefinitionStmt) && returnType != null && returnType != VoidType.v()) {
            DefinitionStmt defStmt2 = (DefinitionStmt) sCallSite;
            return new SourceInfo(def, manager.getAccessPathFactory().createAccessPath(defStmt2.getLeftOp(), null, null, true, false, true, AccessPath.ArrayTaintType.ContentsAndLength, false));
        } else if ((iexpr instanceof InstanceInvokeExpr) && returnType == VoidType.v()) {
            InstanceInvokeExpr iinv = (InstanceInvokeExpr) sCallSite.getInvokeExpr();
            return new SourceInfo(def, manager.getAccessPathFactory().createAccessPath(iinv.getBase(), true));
        } else {
            return null;
        }
    }

    protected SourceInfo createInverseSinkInfo(Stmt sCallSite, InfoflowManager manager, ISourceSinkDefinition def) {
        Value[] selectBaseList;
        if (def == null) {
            return null;
        }
        HashSet<AccessPath> aps = new HashSet<>();
        if (sCallSite.containsInvokeExpr()) {
            InvokeExpr iExpr = sCallSite.getInvokeExpr();
            for (Value arg : iExpr.getArgs()) {
                if (!(arg instanceof Constant)) {
                    aps.add(manager.getAccessPathFactory().createAccessPath(arg, true));
                }
            }
            if (iExpr instanceof InstanceInvokeExpr) {
                InstanceInvokeExpr iiExpr = (InstanceInvokeExpr) iExpr;
                aps.add(manager.getAccessPathFactory().createAccessPath(iiExpr.getBase(), true));
            }
        } else if (sCallSite instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) sCallSite;
            for (Value rightVal : BaseSelector.selectBaseList(assignStmt.getRightOp(), true)) {
                aps.add(manager.getAccessPathFactory().createAccessPath(rightVal, true));
            }
        } else if (sCallSite instanceof ReturnStmt) {
            ReturnStmt retStmt = (ReturnStmt) sCallSite;
            aps.add(manager.getAccessPathFactory().createAccessPath(retStmt.getOp(), true));
        }
        return new SourceInfo(def, aps);
    }

    protected ISourceSinkDefinition getSourceMethod(SootMethod method) {
        if (this.oneSourceAtATime && (this.osaatType != SourceType.MethodCall || this.currentSource != method)) {
            return null;
        }
        return this.sourceMethods.get(method);
    }

    protected ISourceSinkDefinition getInverseSourceMethod(SootMethod method) {
        if (this.oneSourceAtATime && (this.osaatType != SourceType.MethodCall || this.currentSource != method)) {
            return null;
        }
        return this.sinkMethods.get(method);
    }

    protected ISourceSinkDefinition getSourceDefinition(SootMethod method) {
        return getDefFromMap(this.sourceMethods, method);
    }

    private ISourceSinkDefinition getDefFromMap(Map<SootMethod, ISourceSinkDefinition> map, SootMethod method) {
        if (this.oneSourceAtATime) {
            if (this.osaatType == SourceType.MethodCall && this.currentSource == method) {
                return map.get(method);
            }
            return null;
        }
        return map.get(method);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CallbackDefinition getCallbackDefinition(SootMethod method) {
        if (this.oneSourceAtATime) {
            if (this.osaatType == SourceType.Callback && this.currentSource == method) {
                return this.callbackMethods.get(method);
            }
            return null;
        }
        return this.callbackMethods.get(method);
    }

    protected ISourceSinkDefinition getSource(Stmt sCallSite, IInfoflowCFG cfg) {
        ISourceSinkDefinition def;
        ISourceSinkDefinition def2;
        if ($assertionsDisabled || cfg != null) {
            if ($assertionsDisabled || (cfg instanceof BiDiInterproceduralCFG)) {
                ISourceSinkDefinition def3 = this.sourceStatements.get(sCallSite);
                if (def3 != null) {
                    return def3;
                }
                if ((!this.oneSourceAtATime || this.osaatType == SourceType.MethodCall) && sCallSite.containsInvokeExpr()) {
                    SootMethod callee = sCallSite.getInvokeExpr().getMethod();
                    ISourceSinkDefinition def4 = getSourceDefinition(callee);
                    if (def4 != null) {
                        return def4;
                    }
                    String subSig = callee.getSubSignature();
                    for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(callee.getDeclaringClass())) {
                        SootMethod m = i.getMethodUnsafe(subSig);
                        if (m != null && (def = getSourceDefinition(m)) != null) {
                            return def;
                        }
                    }
                    for (SootMethod sm : cfg.getCalleesOfCallAt(sCallSite)) {
                        ISourceSinkDefinition def5 = getSourceDefinition(sm);
                        if (def5 != null) {
                            return def5;
                        }
                    }
                }
                if ((!this.oneSourceAtATime || this.osaatType == SourceType.UISource) && (def2 = getUISourceDefinition(sCallSite, cfg)) != null) {
                    return def2;
                }
                ISourceSinkDefinition def6 = checkCallbackParamSource(sCallSite, cfg);
                if (def6 != null) {
                    return def6;
                }
                ISourceSinkDefinition def7 = checkFieldSource(sCallSite, cfg);
                if (def7 != null) {
                    return def7;
                }
                return null;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ISourceSinkDefinition getInverseSink(Stmt sCallSite, IInfoflowCFG cfg) {
        ISourceSinkDefinition def;
        ISourceSinkDefinition def2 = this.sinkStatements.get(sCallSite);
        if (def2 != null) {
            return def2;
        }
        if ((!this.oneSourceAtATime || this.osaatType == SourceType.MethodCall) && sCallSite.containsInvokeExpr()) {
            SootMethod callee = sCallSite.getInvokeExpr().getMethod();
            ISourceSinkDefinition def3 = this.sinkMethods.get(callee);
            if (def3 != null) {
                return def3;
            }
            String subSig = callee.getSubSignature();
            for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(callee.getDeclaringClass())) {
                if (i.declaresMethod(subSig) && (def = this.sinkMethods.get(i.getMethod(subSig))) != null) {
                    return def;
                }
            }
            for (SootMethod sm : cfg.getCalleesOfCallAt(sCallSite)) {
                ISourceSinkDefinition def4 = this.sinkMethods.get(sm);
                if (def4 != null) {
                    return def4;
                }
            }
            return null;
        } else if (sCallSite instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) sCallSite;
            if (assignStmt.getLeftOp() instanceof FieldRef) {
                FieldRef fieldRef = (FieldRef) assignStmt.getLeftOp();
                ISourceSinkDefinition def5 = this.sinkFields.get(fieldRef.getField());
                if (def5 != null) {
                    return def5;
                }
                return null;
            }
            return null;
        } else if (sCallSite instanceof ReturnStmt) {
            return this.sinkReturnMethods.get(cfg.getMethodOf(sCallSite));
        } else {
            return null;
        }
    }

    private ISourceSinkDefinition checkFieldSource(Stmt stmt, IInfoflowCFG cfg) {
        if (stmt instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) stmt;
            if (assignStmt.getRightOp() instanceof FieldRef) {
                FieldRef fieldRef = (FieldRef) assignStmt.getRightOp();
                return this.sourceFields.get(fieldRef.getField());
            }
            return null;
        }
        return null;
    }

    protected ISourceSinkDefinition checkCallbackParamSource(Stmt sCallSite, IInfoflowCFG cfg) {
        CallbackDefinition def;
        Set[] methodParamDefs;
        Set<AccessPathTuple> apTuples;
        if (this.sourceSinkConfig.getCallbackSourceMode() == InfoflowConfiguration.CallbackSourceMode.NoParametersAsSources) {
            return null;
        }
        if ((this.oneSourceAtATime && this.osaatType != SourceType.Callback) || !(sCallSite instanceof IdentityStmt)) {
            return null;
        }
        IdentityStmt is = (IdentityStmt) sCallSite;
        if (!(is.getRightOp() instanceof ParameterRef)) {
            return null;
        }
        ParameterRef paramRef = (ParameterRef) is.getRightOp();
        SootMethod parentMethod = cfg.getMethodOf(sCallSite);
        if (parentMethod == null) {
            return null;
        }
        if ((!this.sourceSinkConfig.getEnableLifecycleSources() && isEntryPointMethod(parentMethod)) || (def = getCallbackDefinition(parentMethod)) == null) {
            return null;
        }
        if (this.sourceSinkConfig.getCallbackSourceMode() == InfoflowConfiguration.CallbackSourceMode.AllParametersAsSources) {
            return MethodSourceSinkDefinition.createParameterSource(paramRef.getIndex(), MethodSourceSinkDefinition.CallType.Callback);
        }
        ISourceSinkDefinition sourceSinkDef = this.sourceMethods.get(def.getParentMethod());
        if (sourceSinkDef instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition methodDef = (MethodSourceSinkDefinition) sourceSinkDef;
            if (this.sourceSinkConfig.getCallbackSourceMode() == InfoflowConfiguration.CallbackSourceMode.SourceListOnly && sourceSinkDef != null && (methodParamDefs = methodDef.getParameters()) != null && methodParamDefs.length > paramRef.getIndex() && (apTuples = methodDef.getParameters()[paramRef.getIndex()]) != null && !apTuples.isEmpty()) {
                for (AccessPathTuple curTuple : apTuples) {
                    if (curTuple.getSourceSinkType().isSource()) {
                        return sourceSinkDef;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    protected ISourceSinkDefinition getUISourceDefinition(Stmt sCallSite, IInfoflowCFG cfg) {
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public void initialize() {
        if (this.sourceDefs != null) {
            this.sourceMethods = new HashMap();
            this.sourceFields = new HashMap();
            this.sourceStatements = new HashMap();
            Iterator<Pair<K, V>> it = this.sourceDefs.iterator();
            while (it.hasNext()) {
                Pair<String, ISourceSinkDefinition> entry = (Pair) it.next();
                ISourceSinkDefinition sourceSinkDef = entry.getO2();
                if (sourceSinkDef instanceof MethodSourceSinkDefinition) {
                    SootMethodAndClass method = ((MethodSourceSinkDefinition) sourceSinkDef).getMethod();
                    String returnType = method.getReturnType();
                    if (returnType == null || returnType.isEmpty()) {
                        String className = method.getClassName();
                        String subSignatureWithoutReturnType = ((MethodSourceSinkDefinition) sourceSinkDef).getMethod().getSubSignature();
                        SootMethod sootMethod = grabMethodWithoutReturn(className, subSignatureWithoutReturnType);
                        if (sootMethod != null) {
                            this.sourceMethods.put(sootMethod, sourceSinkDef);
                        }
                    } else {
                        SootMethod sm = grabMethod(entry.getO1());
                        if (sm != null) {
                            this.sourceMethods.put(sm, sourceSinkDef);
                        }
                    }
                } else if (sourceSinkDef instanceof FieldSourceSinkDefinition) {
                    SootField sf = Scene.v().grabField(entry.getO1());
                    if (sf != null) {
                        this.sourceFields.put(sf, sourceSinkDef);
                    }
                } else if (sourceSinkDef instanceof StatementSourceSinkDefinition) {
                    StatementSourceSinkDefinition sssd = (StatementSourceSinkDefinition) sourceSinkDef;
                    this.sourceStatements.put(sssd.getStmt(), sssd);
                }
            }
            this.sourceDefs = null;
        }
        if (this.sinkDefs != null) {
            this.sinkMethods = new HashMap();
            this.sinkFields = new HashMap();
            this.sinkReturnMethods = new HashMap();
            this.sinkStatements = new HashMap();
            Iterator<Pair<K, V>> it2 = this.sinkDefs.iterator();
            while (it2.hasNext()) {
                Pair<String, ISourceSinkDefinition> entry2 = (Pair) it2.next();
                ISourceSinkDefinition sourceSinkDef2 = entry2.getO2();
                if (sourceSinkDef2 instanceof MethodSourceSinkDefinition) {
                    MethodSourceSinkDefinition methodSourceSinkDef = (MethodSourceSinkDefinition) sourceSinkDef2;
                    if (methodSourceSinkDef.getCallType() == MethodSourceSinkDefinition.CallType.Return) {
                        SootMethod m = Scene.v().grabMethod(methodSourceSinkDef.getMethod().getSignature());
                        if (m != null) {
                            this.sinkReturnMethods.put(m, methodSourceSinkDef);
                        }
                    } else {
                        SootMethodAndClass method2 = methodSourceSinkDef.getMethod();
                        String returnType2 = method2.getReturnType();
                        boolean isMethodWithoutReturnType = returnType2 == null || returnType2.isEmpty();
                        if (isMethodWithoutReturnType) {
                            String className2 = method2.getClassName();
                            String subSignatureWithoutReturnType2 = ((MethodSourceSinkDefinition) sourceSinkDef2).getMethod().getSubSignature();
                            SootMethod sootMethod2 = grabMethodWithoutReturn(className2, subSignatureWithoutReturnType2);
                            if (sootMethod2 != null) {
                                this.sinkMethods.put(sootMethod2, sourceSinkDef2);
                            }
                        } else {
                            SootMethod sm2 = grabMethod(entry2.getO1());
                            if (sm2 != null) {
                                this.sinkMethods.put(sm2, entry2.getO2());
                            }
                        }
                    }
                } else if (sourceSinkDef2 instanceof FieldSourceSinkDefinition) {
                    SootField sf2 = Scene.v().grabField(entry2.getO1());
                    if (sf2 != null) {
                        this.sinkFields.put(sf2, sourceSinkDef2);
                    }
                } else if (sourceSinkDef2 instanceof StatementSourceSinkDefinition) {
                    StatementSourceSinkDefinition sssd2 = (StatementSourceSinkDefinition) sourceSinkDef2;
                    this.sinkStatements.put(sssd2.getStmt(), sssd2);
                }
            }
            this.sinkDefs = null;
        }
    }

    private SootMethod matchMethodWithoutReturn(@NotNull SootClass sootClass, String subSignature) {
        if (sootClass.resolvingLevel() == 0) {
            List<SootMethod> sootMethods = sootClass.getMethods();
            for (SootMethod s : sootMethods) {
                String[] tempSignature = s.getSubSignature().split(Instruction.argsep);
                if (tempSignature.length == 2 && tempSignature[1].equals(subSignature)) {
                    return s;
                }
            }
            return null;
        }
        return null;
    }

    private SootMethod grabMethodWithoutReturn(String sootClassName, String subSignature) {
        SootClass sootClass = Scene.v().getSootClassUnsafe(sootClassName);
        if (sootClass == null) {
            return null;
        }
        SootMethod sootMethod = matchMethodWithoutReturn(sootClass, subSignature);
        if (sootMethod != null) {
            return sootMethod;
        }
        for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(sootClass)) {
            SootMethod sootMethod2 = matchMethodWithoutReturn(i, subSignature);
            if (sootMethod2 != null) {
                return sootMethod2;
            }
        }
        return null;
    }

    private SootMethod grabMethod(String signature) {
        String sootClassName = Scene.signatureToClass(signature);
        SootClass sootClass = Scene.v().getSootClassUnsafe(sootClassName);
        if (sootClass == null) {
            return null;
        }
        String subSignature = Scene.signatureToSubsignature(signature);
        SootMethod sootMethod = sootClass.getMethodUnsafe(subSignature);
        if (sootMethod != null) {
            return sootMethod;
        }
        for (SootClass i : this.parentClassesAndInterfaces.getUnchecked(sootClass)) {
            SootMethod sootMethod2 = i.getMethodUnsafe(subSignature);
            if (sootMethod2 != null) {
                return sootMethod2;
            }
        }
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager
    public void setOneSourceAtATimeEnabled(boolean enabled) {
        this.oneSourceAtATime = enabled;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager
    public boolean isOneSourceAtATimeEnabled() {
        return this.oneSourceAtATime;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager
    public void resetCurrentSource() {
        this.osaatIterator = this.sourceMethods.keySet().iterator();
        this.osaatType = SourceType.MethodCall;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager
    public void nextSource() {
        if (this.osaatType == SourceType.MethodCall || this.osaatType == SourceType.Callback) {
            this.currentSource = this.osaatIterator.next();
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IOneSourceAtATimeManager
    public boolean hasNextSource() {
        if (this.osaatType == SourceType.MethodCall) {
            if (this.osaatIterator.hasNext()) {
                return true;
            }
            this.osaatType = SourceType.Callback;
            this.osaatIterator = this.callbackMethods.keySet().iterator();
            return hasNextSource();
        } else if (this.osaatType == SourceType.Callback) {
            if (this.osaatIterator.hasNext()) {
                return true;
            }
            this.osaatType = SourceType.UISource;
            return true;
        } else if (this.osaatType == SourceType.UISource) {
            this.osaatType = SourceType.NoSource;
            return false;
        } else {
            return false;
        }
    }

    public void excludeMethod(SootMethod toExclude) {
        this.excludedMethods.add(toExclude);
    }
}
