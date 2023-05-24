package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.DontSynchronize;
import heros.SynchronizedBy;
import heros.ThreadSafe;
import heros.solver.IDESolver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;
import soot.jimple.toolkits.callgraph.Filter;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/JimpleBasedInterproceduralCFG.class */
public class JimpleBasedInterproceduralCFG extends AbstractJimpleBasedICFG {
    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);
    protected boolean includeReflectiveCalls;
    protected boolean includePhantomCallees;
    protected boolean fallbackToImmediateCallees;
    @DontSynchronize("readonly")
    protected final CallGraph cg;
    protected CacheLoader<Unit, Collection<SootMethod>> loaderUnitToCallees;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected final LoadingCache<Unit, Collection<SootMethod>> unitToCallees;
    protected CacheLoader<SootMethod, Collection<Unit>> loaderMethodToCallers;
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected final LoadingCache<SootMethod, Collection<Unit>> methodToCallers;

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/icfg/JimpleBasedInterproceduralCFG$EdgeFilter.class */
    public class EdgeFilter extends Filter {
        protected EdgeFilter() {
            super(new EdgePredicate() { // from class: soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG.EdgeFilter.1
                @Override // soot.jimple.toolkits.callgraph.EdgePredicate
                public boolean want(Edge e) {
                    if (e.kind().isExplicit() || e.kind().isFake() || e.kind().isClinit()) {
                        return true;
                    }
                    return JimpleBasedInterproceduralCFG.this.includeReflectiveCalls && e.kind().isReflection();
                }
            });
        }
    }

    public JimpleBasedInterproceduralCFG() {
        this(true);
    }

    public JimpleBasedInterproceduralCFG(boolean enableExceptions) {
        this(enableExceptions, false);
    }

    public JimpleBasedInterproceduralCFG(boolean enableExceptions, boolean includeReflectiveCalls) {
        super(enableExceptions);
        this.includeReflectiveCalls = false;
        this.includePhantomCallees = false;
        this.fallbackToImmediateCallees = false;
        this.loaderUnitToCallees = new CacheLoader<Unit, Collection<SootMethod>>() { // from class: soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG.1
            @Override // com.google.common.cache.CacheLoader
            public Collection<SootMethod> load(Unit u) throws Exception {
                ArrayList<SootMethod> res = null;
                Iterator<Edge> edgeIter = new EdgeFilter().wrap(JimpleBasedInterproceduralCFG.this.cg.edgesOutOf(u));
                while (edgeIter.hasNext()) {
                    Edge edge = edgeIter.next();
                    SootMethod m = edge.getTgt().method();
                    if (JimpleBasedInterproceduralCFG.this.includePhantomCallees || m.hasActiveBody()) {
                        if (res == null) {
                            res = new ArrayList<>();
                        }
                        res.add(m);
                    } else if (IDESolver.DEBUG) {
                        JimpleBasedInterproceduralCFG.logger.error(String.format("Method %s is referenced but has no body!", m.getSignature(), new Exception()));
                    }
                }
                if (res != null) {
                    res.trimToSize();
                    return res;
                }
                if (JimpleBasedInterproceduralCFG.this.fallbackToImmediateCallees && (u instanceof Stmt)) {
                    Stmt s = (Stmt) u;
                    if (s.containsInvokeExpr()) {
                        SootMethod immediate = s.getInvokeExpr().getMethod();
                        if (JimpleBasedInterproceduralCFG.this.includePhantomCallees || immediate.hasActiveBody()) {
                            return Collections.singleton(immediate);
                        }
                    }
                }
                return Collections.emptySet();
            }
        };
        this.unitToCallees = IDESolver.DEFAULT_CACHE_BUILDER.build(this.loaderUnitToCallees);
        this.loaderMethodToCallers = new CacheLoader<SootMethod, Collection<Unit>>() { // from class: soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG.2
            @Override // com.google.common.cache.CacheLoader
            public Collection<Unit> load(SootMethod m) throws Exception {
                ArrayList<Unit> res = new ArrayList<>();
                Iterator<Edge> edgeIter = new EdgeFilter().wrap(JimpleBasedInterproceduralCFG.this.cg.edgesInto(m));
                while (edgeIter.hasNext()) {
                    Edge edge = edgeIter.next();
                    res.add(edge.srcUnit());
                }
                res.trimToSize();
                return res;
            }
        };
        this.methodToCallers = IDESolver.DEFAULT_CACHE_BUILDER.build(this.loaderMethodToCallers);
        this.includeReflectiveCalls = includeReflectiveCalls;
        this.cg = Scene.v().getCallGraph();
        initializeUnitToOwner();
    }

    protected void initializeUnitToOwner() {
        Iterator<MethodOrMethodContext> iter = Scene.v().getReachableMethods().listener();
        while (iter.hasNext()) {
            SootMethod m = iter.next().method();
            initializeUnitToOwner(m);
        }
    }

    @Override // heros.InterproceduralCFG
    public Collection<SootMethod> getCalleesOfCallAt(Unit u) {
        return this.unitToCallees.getUnchecked(u);
    }

    @Override // heros.InterproceduralCFG
    public Collection<Unit> getCallersOf(SootMethod m) {
        return this.methodToCallers.getUnchecked(m);
    }

    public void setIncludePhantomCallees(boolean includePhantomCallees) {
        this.includePhantomCallees = includePhantomCallees;
    }

    public void setFallbackToImmediateCallees(boolean fallbackToImmediateCallees) {
        this.fallbackToImmediateCallees = fallbackToImmediateCallees;
    }
}
