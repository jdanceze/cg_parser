package soot.jimple.infoflow.sourcesSinks.manager;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.solver.IDESolver;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/DefaultSourceSinkManager.class */
public class DefaultSourceSinkManager implements IReversibleSourceSinkManager {
    protected Collection<String> sourceDefs;
    protected Collection<String> sinkDefs;
    private Collection<SootMethod> sources;
    private Collection<SootMethod> sinks;
    private Collection<String> returnTaintMethodDefs;
    private Collection<String> parameterTaintMethodDefs;
    private Collection<SootMethod> returnTaintMethods;
    private Collection<SootMethod> parameterTaintMethods;
    protected final LoadingCache<SootClass, Collection<SootClass>> interfacesOf;

    public DefaultSourceSinkManager(Collection<String> sources, Collection<String> sinks) {
        this(sources, sinks, null, null);
    }

    public DefaultSourceSinkManager(Collection<String> sources, Collection<String> sinks, Collection<String> parameterTaintMethods, Collection<String> returnTaintMethods) {
        this.interfacesOf = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootClass, Collection<SootClass>>() { // from class: soot.jimple.infoflow.sourcesSinks.manager.DefaultSourceSinkManager.1
            @Override // com.google.common.cache.CacheLoader
            public Collection<SootClass> load(SootClass sc) throws Exception {
                Set<SootClass> set = new HashSet<>(sc.getInterfaceCount());
                for (SootClass i : sc.getInterfaces()) {
                    set.add(i);
                    set.addAll(DefaultSourceSinkManager.this.interfacesOf.getUnchecked(i));
                }
                SootClass superClass = sc.getSuperclassUnsafe();
                if (superClass != null) {
                    set.addAll(DefaultSourceSinkManager.this.interfacesOf.getUnchecked(superClass));
                }
                return set;
            }
        });
        this.sourceDefs = sources;
        this.sinkDefs = sinks;
        this.parameterTaintMethodDefs = parameterTaintMethods != null ? parameterTaintMethods : new HashSet<>();
        this.returnTaintMethodDefs = returnTaintMethods != null ? returnTaintMethods : new HashSet<>();
    }

    public DefaultSourceSinkManager(ISourceSinkDefinitionProvider sourceSinkProvider) {
        this.interfacesOf = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootClass, Collection<SootClass>>() { // from class: soot.jimple.infoflow.sourcesSinks.manager.DefaultSourceSinkManager.1
            @Override // com.google.common.cache.CacheLoader
            public Collection<SootClass> load(SootClass sc) throws Exception {
                Set<SootClass> set = new HashSet<>(sc.getInterfaceCount());
                for (SootClass i : sc.getInterfaces()) {
                    set.add(i);
                    set.addAll(DefaultSourceSinkManager.this.interfacesOf.getUnchecked(i));
                }
                SootClass superClass = sc.getSuperclassUnsafe();
                if (superClass != null) {
                    set.addAll(DefaultSourceSinkManager.this.interfacesOf.getUnchecked(superClass));
                }
                return set;
            }
        });
        this.sourceDefs = new HashSet();
        this.sinkDefs = new HashSet();
        for (ISourceSinkDefinition ssd : sourceSinkProvider.getSources()) {
            if (ssd instanceof MethodSourceSinkDefinition) {
                MethodSourceSinkDefinition mssd = (MethodSourceSinkDefinition) ssd;
                this.sourceDefs.add(mssd.getMethod().getSignature());
            }
        }
        for (ISourceSinkDefinition ssd2 : sourceSinkProvider.getSinks()) {
            if (ssd2 instanceof MethodSourceSinkDefinition) {
                MethodSourceSinkDefinition mssd2 = (MethodSourceSinkDefinition) ssd2;
                this.sinkDefs.add(mssd2.getMethod().getSignature());
            }
        }
    }

    public void setSources(List<String> sources) {
        this.sourceDefs = sources;
    }

    public void setSinks(List<String> sinks) {
        this.sinkDefs = sinks;
    }

    private SootMethod getMethodInSet(InfoflowManager manager, Stmt callStmt, Collection<SootMethod> set) {
        if (!callStmt.containsInvokeExpr() || set == null) {
            return null;
        }
        SootMethod callee = callStmt.getInvokeExpr().getMethod();
        if (set.contains(callee)) {
            return callee;
        }
        String subSig = callee.getSubSignature();
        for (SootClass i : this.interfacesOf.getUnchecked(callee.getDeclaringClass())) {
            SootMethod sm = i.getMethodUnsafe(subSig);
            if (sm != null && set.contains(sm)) {
                return sm;
            }
        }
        for (SootMethod sm2 : manager.getICFG().getCalleesOfCallAt(callStmt)) {
            if (set.contains(sm2)) {
                return sm2;
            }
        }
        return null;
    }

    protected boolean isSourceMethod(InfoflowManager manager, Stmt sCallSite) {
        return getMethodInSet(manager, sCallSite, this.sources) != null;
    }

    protected SootMethodAndClass isInverseSourceMethod(InfoflowManager manager, Stmt sCallSite) {
        SootMethod sm = getMethodInSet(manager, sCallSite, this.sources);
        if (sm == null) {
            return null;
        }
        return new SootMethodAndClass(sm);
    }

    protected SootMethodAndClass isSinkMethod(InfoflowManager manager, Stmt sCallSite) {
        SootMethod sm = getMethodInSet(manager, sCallSite, this.sinks);
        if (sm == null) {
            return null;
        }
        return new SootMethodAndClass(sm);
    }

    protected boolean isInverseSinkMethod(InfoflowManager manager, Stmt sCallSite) {
        return getMethodInSet(manager, sCallSite, this.sinks) != null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SourceInfo getSourceInfo(Stmt sCallSite, InfoflowManager manager) {
        SootMethod callee = sCallSite.containsInvokeExpr() ? sCallSite.getInvokeExpr().getMethod() : null;
        AccessPath targetAP = null;
        if (isSourceMethod(manager, sCallSite)) {
            if (callee.getReturnType() != null && (sCallSite instanceof DefinitionStmt)) {
                Value leftOp = ((DefinitionStmt) sCallSite).getLeftOp();
                targetAP = manager.getAccessPathFactory().createAccessPath(leftOp, true);
            } else if (sCallSite.getInvokeExpr() instanceof InstanceInvokeExpr) {
                Value base = ((InstanceInvokeExpr) sCallSite.getInvokeExpr()).getBase();
                targetAP = manager.getAccessPathFactory().createAccessPath(base, true);
            }
        } else if (sCallSite instanceof IdentityStmt) {
            IdentityStmt istmt = (IdentityStmt) sCallSite;
            if (istmt.getRightOp() instanceof ParameterRef) {
                ParameterRef pref = (ParameterRef) istmt.getRightOp();
                SootMethod currentMethod = manager.getICFG().getMethodOf(istmt);
                if (this.parameterTaintMethods != null && this.parameterTaintMethods.contains(currentMethod)) {
                    targetAP = manager.getAccessPathFactory().createAccessPath(currentMethod.getActiveBody().getParameterLocal(pref.getIndex()), true);
                }
            }
        }
        if (targetAP == null) {
            return null;
        }
        return new SourceInfo(callee == null ? null : new MethodSourceSinkDefinition(new SootMethodAndClass(callee)), targetAP);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public SinkInfo getSinkInfo(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        if (this.returnTaintMethods != null && (sCallSite instanceof ReturnStmt)) {
            SootMethod sm = manager.getICFG().getMethodOf(sCallSite);
            if (this.returnTaintMethods != null && this.returnTaintMethods.contains(sm)) {
                return new SinkInfo(new MethodSourceSinkDefinition(new SootMethodAndClass(sm)));
            }
        }
        if (this.sinks != null && !this.sinks.isEmpty() && sCallSite.containsInvokeExpr()) {
            InvokeExpr iexpr = sCallSite.getInvokeExpr();
            SootMethodAndClass smac = isSinkMethod(manager, sCallSite);
            if (smac != null && SystemClassHandler.v().isTaintVisible(ap, iexpr.getMethod())) {
                if (ap == null) {
                    return new SinkInfo(new MethodSourceSinkDefinition(smac));
                }
                if (!ap.isStaticFieldRef()) {
                    for (int i = 0; i < iexpr.getArgCount(); i++) {
                        if (iexpr.getArg(i) == ap.getPlainValue() && (ap.getTaintSubFields() || ap.isLocal())) {
                            return new SinkInfo(new MethodSourceSinkDefinition(smac));
                        }
                    }
                    if ((iexpr instanceof InstanceInvokeExpr) && ((InstanceInvokeExpr) iexpr).getBase() == ap.getPlainValue()) {
                        return new SinkInfo(new MethodSourceSinkDefinition(smac));
                    }
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SinkInfo getInverseSourceInfo(Stmt sCallSite, InfoflowManager manager, AccessPath ap) {
        SootMethodAndClass smac = isInverseSourceMethod(manager, sCallSite);
        if (smac != null) {
            InvokeExpr ie = sCallSite.getInvokeExpr();
            if (!SystemClassHandler.v().isTaintVisible(ap, ie.getMethod())) {
                return null;
            }
            if (ap == null) {
                return new SinkInfo(new MethodSourceSinkDefinition(smac));
            }
            if (!ap.isStaticFieldRef()) {
                if (sCallSite instanceof AssignStmt) {
                    if (((AssignStmt) sCallSite).getLeftOp() == ap.getPlainValue()) {
                        return new SinkInfo(new MethodSourceSinkDefinition(smac));
                    }
                    return null;
                } else if ((ie instanceof InstanceInvokeExpr) && ((InstanceInvokeExpr) ie).getBase() == ap.getPlainValue()) {
                    return new SinkInfo(new MethodSourceSinkDefinition(smac));
                } else {
                    return null;
                }
            }
            return null;
        } else if (sCallSite instanceof IdentityStmt) {
            IdentityStmt istmt = (IdentityStmt) sCallSite;
            if (istmt.getRightOp() instanceof ParameterRef) {
                SootMethod currentMethod = manager.getICFG().getMethodOf(istmt);
                if (this.parameterTaintMethods != null && this.parameterTaintMethods.contains(currentMethod)) {
                    SootMethodAndClass pSmac = new SootMethodAndClass(currentMethod);
                    return new SinkInfo(new MethodSourceSinkDefinition(pSmac));
                }
                return null;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager
    public SourceInfo getInverseSinkInfo(Stmt sCallSite, InfoflowManager manager) {
        SootMethod callee = sCallSite.containsInvokeExpr() ? sCallSite.getInvokeExpr().getMethod() : null;
        Set<AccessPath> aps = new HashSet<>();
        if (this.returnTaintMethods != null && (sCallSite instanceof ReturnStmt)) {
            SootMethod sm = manager.getICFG().getMethodOf(sCallSite);
            if (this.returnTaintMethods != null && this.returnTaintMethods.contains(sm)) {
                Value op = ((ReturnStmt) sCallSite).getOp();
                aps.add(manager.getAccessPathFactory().createAccessPath(op, true));
            }
        }
        if (isInverseSinkMethod(manager, sCallSite)) {
            InvokeExpr ie = sCallSite.getInvokeExpr();
            for (Value arg : ie.getArgs()) {
                if (!(arg instanceof Constant)) {
                    aps.add(manager.getAccessPathFactory().createAccessPath(arg, true));
                }
            }
            if (ie instanceof InstanceInvokeExpr) {
                Value base = ((InstanceInvokeExpr) sCallSite.getInvokeExpr()).getBase();
                aps.add(manager.getAccessPathFactory().createAccessPath(base, true));
            }
        }
        if (aps.isEmpty()) {
            return null;
        }
        aps.remove(null);
        return new SourceInfo(callee == null ? null : new MethodSourceSinkDefinition(new SootMethodAndClass(callee)), aps);
    }

    public void setParameterTaintMethods(List<String> parameterTaintMethods) {
        this.parameterTaintMethodDefs = parameterTaintMethods;
    }

    public void setReturnTaintMethods(List<String> returnTaintMethods) {
        this.returnTaintMethodDefs = returnTaintMethods;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.ISourceSinkManager
    public void initialize() {
        if (this.sourceDefs != null) {
            this.sources = new HashSet();
            for (String signature : this.sourceDefs) {
                SootMethod sm = Scene.v().grabMethod(signature);
                if (sm != null) {
                    this.sources.add(sm);
                }
            }
            this.sourceDefs = null;
        }
        if (this.sinkDefs != null) {
            this.sinks = new HashSet();
            for (String signature2 : this.sinkDefs) {
                SootMethod sm2 = Scene.v().grabMethod(signature2);
                if (sm2 != null) {
                    this.sinks.add(sm2);
                }
            }
            this.sinkDefs = null;
        }
        if (this.returnTaintMethodDefs != null) {
            this.returnTaintMethods = new HashSet();
            for (String signature3 : this.returnTaintMethodDefs) {
                SootMethod sm3 = Scene.v().grabMethod(signature3);
                if (sm3 != null) {
                    this.returnTaintMethods.add(sm3);
                }
            }
            this.returnTaintMethodDefs = null;
        }
        if (this.parameterTaintMethodDefs != null) {
            this.parameterTaintMethods = new HashSet();
            for (String signature4 : this.parameterTaintMethodDefs) {
                SootMethod sm4 = Scene.v().grabMethod(signature4);
                if (sm4 != null) {
                    this.parameterTaintMethods.add(sm4);
                }
            }
            this.parameterTaintMethodDefs = null;
        }
    }
}
