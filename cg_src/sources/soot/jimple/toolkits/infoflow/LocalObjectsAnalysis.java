package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/LocalObjectsAnalysis.class */
public class LocalObjectsAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(LocalObjectsAnalysis.class);
    public InfoFlowAnalysis dfa;
    Map<SootMethod, ReachableMethods> rmCache = new HashMap();
    Map callChainsCache = new HashMap();
    UseFinder uf = new UseFinder();
    CallGraph cg = Scene.v().getCallGraph();
    Map<SootClass, ClassLocalObjectsAnalysis> classToClassLocalObjectsAnalysis = new HashMap();
    Map<SootMethod, SmartMethodLocalObjectsAnalysis> mloaCache = new HashMap();

    public LocalObjectsAnalysis(InfoFlowAnalysis dfa) {
        this.dfa = dfa;
    }

    public ClassLocalObjectsAnalysis getClassLocalObjectsAnalysis(SootClass sc) {
        if (!this.classToClassLocalObjectsAnalysis.containsKey(sc)) {
            ClassLocalObjectsAnalysis cloa = newClassLocalObjectsAnalysis(this, this.dfa, this.uf, sc);
            this.classToClassLocalObjectsAnalysis.put(sc, cloa);
        }
        return this.classToClassLocalObjectsAnalysis.get(sc);
    }

    protected ClassLocalObjectsAnalysis newClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sc) {
        return new ClassLocalObjectsAnalysis(loa, dfa, uf, sc);
    }

    public boolean isObjectLocalToParent(Value localOrRef, SootMethod sm) {
        if (localOrRef instanceof StaticFieldRef) {
            return false;
        }
        ClassLocalObjectsAnalysis cloa = getClassLocalObjectsAnalysis(sm.getDeclaringClass());
        return cloa.isObjectLocal(localOrRef, sm);
    }

    public boolean isFieldLocalToParent(SootField sf) {
        if (sf.isStatic()) {
            return false;
        }
        ClassLocalObjectsAnalysis cloa = getClassLocalObjectsAnalysis(sf.getDeclaringClass());
        return cloa.isFieldLocal(sf);
    }

    public boolean isObjectLocalToContext(Value localOrRef, SootMethod sm, SootMethod context) {
        String str;
        if (sm == context) {
            boolean isLocal = isObjectLocalToParent(localOrRef, sm);
            if (this.dfa.printDebug()) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder(ASTNode.TAB);
                if (isLocal) {
                    str = "LOCAL  (Directly Reachable from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")";
                } else {
                    str = "SHARED (Directly Reachable from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")";
                }
                logger2.debug(sb.append(str).toString());
            }
            return isLocal;
        } else if (localOrRef instanceof StaticFieldRef) {
            if (this.dfa.printDebug()) {
                logger.debug("    SHARED (Static             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                return false;
            }
            return false;
        } else if (!sm.isConcrete()) {
            throw new RuntimeException("Attempted to check if a local variable in a non-concrete method is shared/local.");
        } else {
            Body b = sm.retrieveActiveBody();
            CallLocalityContext mergedContext = getClassLocalObjectsAnalysis(context.getDeclaringClass()).getMergedContext(sm);
            if (mergedContext == null) {
                if (this.dfa.printDebug()) {
                    logger.debug("      ------ (Unreachable        from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                    return true;
                }
                return true;
            } else if (localOrRef instanceof InstanceFieldRef) {
                InstanceFieldRef ifr = (InstanceFieldRef) localOrRef;
                Local thisLocal = null;
                try {
                    thisLocal = b.getThisLocal();
                } catch (RuntimeException e) {
                }
                if (ifr.getBase() == thisLocal) {
                    boolean isLocal2 = mergedContext.isFieldLocal(InfoFlowAnalysis.getNodeForFieldRef(sm, ifr.getField()));
                    if (this.dfa.printDebug()) {
                        if (isLocal2) {
                            logger.debug("      LOCAL  (this  .localField  from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                        } else {
                            logger.debug("      SHARED (this  .sharedField from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                        }
                    }
                    return isLocal2;
                }
                boolean isLocal3 = SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, sm, mergedContext, ifr.getBase());
                if (isLocal3) {
                    ClassLocalObjectsAnalysis cloa = getClassLocalObjectsAnalysis(context.getDeclaringClass());
                    boolean isLocal4 = !cloa.getInnerSharedFields().contains(ifr.getField());
                    if (this.dfa.printDebug()) {
                        if (isLocal4) {
                            logger.debug("      LOCAL  (local .localField  from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                        } else {
                            logger.debug("      SHARED (local .sharedField from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                        }
                    }
                    return isLocal4;
                }
                if (this.dfa.printDebug()) {
                    logger.debug("      SHARED (shared.someField   from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                }
                return isLocal3;
            } else {
                boolean isLocal5 = SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, sm, mergedContext, localOrRef);
                if (this.dfa.printDebug()) {
                    if (isLocal5) {
                        logger.debug("      LOCAL  ( local             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                    } else {
                        logger.debug("      SHARED (shared             from " + context.getDeclaringClass().getShortName() + "." + context.getName() + ")");
                    }
                }
                return isLocal5;
            }
        }
    }

    public CallChain getNextCallChainBetween(SootMethod start, SootMethod goal, List previouslyFound) {
        ReachableMethods rm;
        if (this.rmCache.containsKey(start)) {
            rm = this.rmCache.get(start);
        } else {
            List<MethodOrMethodContext> entryPoints = new ArrayList<>();
            entryPoints.add(start);
            rm = new ReachableMethods(this.cg, entryPoints);
            rm.update();
            this.rmCache.put(start, rm);
        }
        if (rm.contains(goal)) {
            return getNextCallChainBetween(rm, start, goal, null, null, previouslyFound);
        }
        return null;
    }

    public CallChain getNextCallChainBetween(ReachableMethods rm, SootMethod start, SootMethod end, Edge endToPath, CallChain path, List previouslyFound) {
        CallChain newpath;
        Pair cacheKey = new Pair(start, end);
        if (this.callChainsCache.containsKey(cacheKey)) {
            return null;
        }
        CallChain path2 = new CallChain(endToPath, path);
        if (start == end) {
            return path2;
        }
        if (!rm.contains(end)) {
            return null;
        }
        Iterator edgeIt = this.cg.edgesInto(end);
        while (edgeIt.hasNext()) {
            Edge e = edgeIt.next();
            SootMethod node = e.src();
            if (!path2.containsMethod(node) && e.isExplicit() && e.srcStmt().containsInvokeExpr() && (newpath = getNextCallChainBetween(rm, start, node, e, path2, previouslyFound)) != null && !previouslyFound.contains(newpath)) {
                return newpath;
            }
        }
        if (previouslyFound.size() == 0) {
            this.callChainsCache.put(cacheKey, null);
            return null;
        }
        return null;
    }

    public List<SootMethod> getAllMethodsForClass(SootClass sootClass) {
        ReachableMethods rm = Scene.v().getReachableMethods();
        List<SootMethod> scopeMethods = new ArrayList<>();
        Iterator scopeMethodsIt = sootClass.methodIterator();
        while (scopeMethodsIt.hasNext()) {
            SootMethod scopeMethod = scopeMethodsIt.next();
            if (rm.contains(scopeMethod)) {
                scopeMethods.add(scopeMethod);
            }
        }
        SootClass superclass = sootClass;
        if (superclass.hasSuperclass()) {
            superclass = sootClass.getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            Iterator scMethodsIt = superclass.methodIterator();
            while (scMethodsIt.hasNext()) {
                SootMethod scMethod = scMethodsIt.next();
                if (rm.contains(scMethod)) {
                    scopeMethods.add(scMethod);
                }
            }
            superclass = superclass.getSuperclass();
        }
        return scopeMethods;
    }

    public boolean hasNonLocalEffects(SootMethod containingMethod, InvokeExpr ie, SootMethod context) {
        SootMethod target = ie.getMethodRef().resolve();
        MutableDirectedGraph dataFlowGraph = this.dfa.getMethodInfoFlowSummary(target);
        if (ie instanceof StaticInvokeExpr) {
            for (EquivalentValue nodeEqVal : dataFlowGraph) {
                Ref node = (Ref) nodeEqVal.getValue();
                if (node instanceof FieldRef) {
                    if (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0) {
                        return true;
                    }
                } else if ((node instanceof ParameterRef) && (dataFlowGraph.getPredsOf(nodeEqVal).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal).size() > 0)) {
                    ParameterRef pr = (ParameterRef) node;
                    if (pr.getIndex() != -1 && !isObjectLocalToContext(ie.getArg(pr.getIndex()), containingMethod, context)) {
                        return true;
                    }
                }
            }
            return false;
        } else if (ie instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
            if (isObjectLocalToContext(iie.getBase(), containingMethod, context)) {
                for (EquivalentValue nodeEqVal2 : dataFlowGraph) {
                    Ref node2 = (Ref) nodeEqVal2.getValue();
                    if (node2 instanceof StaticFieldRef) {
                        if (dataFlowGraph.getPredsOf(nodeEqVal2).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal2).size() > 0) {
                            return true;
                        }
                    } else if ((node2 instanceof ParameterRef) && (dataFlowGraph.getPredsOf(nodeEqVal2).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal2).size() > 0)) {
                        ParameterRef pr2 = (ParameterRef) node2;
                        if (pr2.getIndex() != -1 && !isObjectLocalToContext(ie.getArg(pr2.getIndex()), containingMethod, context)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            for (EquivalentValue nodeEqVal3 : dataFlowGraph) {
                Ref node3 = (Ref) nodeEqVal3.getValue();
                if (node3 instanceof FieldRef) {
                    if (dataFlowGraph.getPredsOf(nodeEqVal3).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal3).size() > 0) {
                        return true;
                    }
                } else if ((node3 instanceof ParameterRef) && (dataFlowGraph.getPredsOf(nodeEqVal3).size() > 0 || dataFlowGraph.getSuccsOf(nodeEqVal3).size() > 0)) {
                    ParameterRef pr3 = (ParameterRef) node3;
                    if (pr3.getIndex() != -1 && !isObjectLocalToContext(ie.getArg(pr3.getIndex()), containingMethod, context)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
