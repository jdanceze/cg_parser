package soot.jimple.infoflow.problems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.cfg.FlowDroidEssentialMethodTag;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.handlers.TaintPropagationHandler;
import soot.jimple.infoflow.nativeCallHandler.INativeCallHandler;
import soot.jimple.infoflow.problems.rules.IPropagationRuleManagerFactory;
import soot.jimple.infoflow.problems.rules.PropagationRuleManager;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.taintWrappers.ITaintPropagationWrapper;
import soot.jimple.infoflow.util.SystemClassHandler;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/AbstractInfoflowProblem.class */
public abstract class AbstractInfoflowProblem extends DefaultJimpleIFDSTabulationProblem<Abstraction, BiDiInterproceduralCFG<Unit, SootMethod>> {
    protected final InfoflowManager manager;
    protected final Map<Unit, Set<Abstraction>> initialSeeds;
    protected ITaintPropagationWrapper taintWrapper;
    protected INativeCallHandler ncHandler;
    protected final Logger logger;
    protected Abstraction zeroValue;
    protected IInfoflowSolver solver;
    protected TaintPropagationHandler taintPropagationHandler;
    private MyConcurrentHashMap<Unit, Set<Unit>> activationUnitsToCallSites;
    protected final PropagationRuleManager propagationRules;
    protected final TaintPropagationResults results;

    public AbstractInfoflowProblem(InfoflowManager manager, Abstraction zeroValue, IPropagationRuleManagerFactory ruleManagerFactory) {
        super(manager.getICFG());
        this.initialSeeds = new HashMap();
        this.logger = LoggerFactory.getLogger(getClass());
        this.zeroValue = null;
        this.solver = null;
        this.taintPropagationHandler = null;
        this.activationUnitsToCallSites = new MyConcurrentHashMap<>();
        this.manager = manager;
        this.zeroValue = zeroValue == null ? createZeroValue() : zeroValue;
        this.results = new TaintPropagationResults(manager);
        this.propagationRules = ruleManagerFactory.createRuleManager(manager, this.zeroValue, this.results);
    }

    public void setSolver(IInfoflowSolver solver) {
        this.solver = solver;
    }

    public void setZeroValue(Abstraction zeroValue) {
        this.zeroValue = zeroValue;
    }

    @Override // heros.template.DefaultIFDSTabulationProblem, heros.SolverConfiguration
    public boolean followReturnsPastSeeds() {
        return true;
    }

    public void setTaintWrapper(ITaintPropagationWrapper wrapper) {
        this.taintWrapper = wrapper;
    }

    public void setNativeCallHandler(INativeCallHandler handler) {
        this.ncHandler = handler;
    }

    protected boolean isInitialMethod(SootMethod sm) {
        for (Unit u : this.initialSeeds.keySet()) {
            if (interproceduralCFG().getMethodOf(u) == sm) {
                return true;
            }
        }
        return false;
    }

    @Override // heros.IFDSTabulationProblem
    public Map<Unit, Set<Abstraction>> initialSeeds() {
        return this.initialSeeds;
    }

    @Override // heros.template.DefaultIFDSTabulationProblem, heros.SolverConfiguration
    public boolean autoAddZero() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isCallSiteActivatingTaint(Unit callSite, Unit activationUnit) {
        Set<Unit> callSites;
        return this.manager.getConfig().getFlowSensitiveAliasing() && activationUnit != null && (callSites = this.activationUnitsToCallSites.get(activationUnit)) != null && callSites.contains(callSite);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean registerActivationCallSite(Unit callSite, SootMethod callee, Abstraction activationAbs) {
        Unit activationUnit;
        if (!this.manager.getConfig().getFlowSensitiveAliasing() || (activationUnit = activationAbs.getActivationUnit()) == null) {
            return false;
        }
        Set<Unit> callSites = this.activationUnitsToCallSites.putIfAbsentElseGet((MyConcurrentHashMap<Unit, Set<Unit>>) activationUnit, (Unit) new ConcurrentHashSet());
        if (callSites.contains(callSite)) {
            return false;
        }
        if (!activationAbs.isAbstractionActive() && !callee.getActiveBody().getUnits().contains(activationUnit)) {
            boolean found = false;
            Iterator<Unit> it = callSites.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Unit au = it.next();
                if (callee.getActiveBody().getUnits().contains(au)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return callSites.add(callSite);
    }

    public void setActivationUnitsToCallSites(AbstractInfoflowProblem other) {
        this.activationUnitsToCallSites = other.activationUnitsToCallSites;
    }

    @Override // heros.template.DefaultIFDSTabulationProblem, heros.IFDSTabulationProblem
    public IInfoflowCFG interproceduralCFG() {
        return (IInfoflowCFG) super.interproceduralCFG();
    }

    public void addInitialSeeds(Unit unit, Set<Abstraction> seeds) {
        if (this.initialSeeds.containsKey(unit)) {
            this.initialSeeds.get(unit).addAll(seeds);
        } else {
            this.initialSeeds.put(unit, new HashSet(seeds));
        }
    }

    public boolean hasInitialSeeds() {
        return !this.initialSeeds.isEmpty();
    }

    public Map<Unit, Set<Abstraction>> getInitialSeeds() {
        return this.initialSeeds;
    }

    public void setTaintPropagationHandler(TaintPropagationHandler handler) {
        this.taintPropagationHandler = handler;
    }

    @Override // heros.template.DefaultIFDSTabulationProblem
    public Abstraction createZeroValue() {
        if (this.zeroValue == null) {
            this.zeroValue = Abstraction.getZeroAbstraction(this.manager.getConfig().getFlowSensitiveAliasing());
        }
        return this.zeroValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Abstraction getZeroValue() {
        return this.zeroValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isExceptionHandler(Unit u) {
        if (u instanceof DefinitionStmt) {
            DefinitionStmt defStmt = (DefinitionStmt) u;
            return defStmt.getRightOp() instanceof CaughtExceptionRef;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<Abstraction> notifyOutFlowHandlers(Unit stmt, Abstraction d1, Abstraction incoming, Set<Abstraction> outgoing, TaintPropagationHandler.FlowFunctionType functionType) {
        if (this.taintPropagationHandler != null && outgoing != null && !outgoing.isEmpty()) {
            outgoing = this.taintPropagationHandler.notifyFlowOut(stmt, d1, incoming, outgoing, this.manager, functionType);
        }
        return outgoing;
    }

    @Override // heros.template.DefaultIFDSTabulationProblem, heros.SolverConfiguration
    public boolean computeValues() {
        return false;
    }

    public InfoflowManager getManager() {
        return this.manager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isExcluded(SootMethod sm) {
        SootClass declClass;
        SootClass declClass2;
        if (sm.hasTag(FlowDroidEssentialMethodTag.TAG_NAME)) {
            return false;
        }
        if (this.manager.getConfig().getExcludeSootLibraryClasses() && (declClass2 = sm.getDeclaringClass()) != null && declClass2.isLibraryClass()) {
            return true;
        }
        if (this.manager.getConfig().getIgnoreFlowsInSystemPackages() && (declClass = sm.getDeclaringClass()) != null && SystemClassHandler.v().isClassInSystemPackage(declClass.getName())) {
            return true;
        }
        return false;
    }

    public TaintPropagationResults getResults() {
        return this.results;
    }

    public PropagationRuleManager getPropagationRules() {
        return this.propagationRules;
    }
}
