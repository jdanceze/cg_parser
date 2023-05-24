package soot.jimple.toolkits.thread.mhp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.PAG;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/StartJoinFinder.class */
public class StartJoinFinder {
    Set<Stmt> startStatements = new HashSet();
    Set<Stmt> joinStatements = new HashSet();
    Map<Stmt, List<SootMethod>> startToRunMethods = new HashMap();
    Map<Stmt, List<AllocNode>> startToAllocNodes = new HashMap();
    Map<Stmt, Stmt> startToJoin = new HashMap();
    Map<Stmt, SootMethod> startToContainingMethod = new HashMap();

    public StartJoinFinder(CallGraph callGraph, PAG pag) {
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootMethod method : appClass.getMethods()) {
                boolean mayHaveStartStmt = false;
                Iterator edgesIt = callGraph.edgesOutOf(method);
                while (edgesIt.hasNext()) {
                    SootMethod target = edgesIt.next().tgt();
                    if (target.getName().equals("start") || target.getName().equals("run")) {
                        mayHaveStartStmt = true;
                    }
                }
                if (mayHaveStartStmt && method.isConcrete()) {
                    Body b = method.retrieveActiveBody();
                    StartJoinAnalysis sja = new StartJoinAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b), method, callGraph, pag);
                    this.startStatements.addAll(sja.getStartStatements());
                    this.joinStatements.addAll(sja.getJoinStatements());
                    this.startToRunMethods.putAll(sja.getStartToRunMethods());
                    this.startToAllocNodes.putAll(sja.getStartToAllocNodes());
                    this.startToJoin.putAll(sja.getStartToJoin());
                    for (Stmt start : sja.getStartStatements()) {
                        this.startToContainingMethod.put(start, method);
                    }
                }
            }
        }
    }

    public Set<Stmt> getStartStatements() {
        return this.startStatements;
    }

    public Set<Stmt> getJoinStatements() {
        return this.joinStatements;
    }

    public Map<Stmt, List<SootMethod>> getStartToRunMethods() {
        return this.startToRunMethods;
    }

    public Map<Stmt, List<AllocNode>> getStartToAllocNodes() {
        return this.startToAllocNodes;
    }

    public Map<Stmt, Stmt> getStartToJoin() {
        return this.startToJoin;
    }

    public Map<Stmt, SootMethod> getStartToContainingMethod() {
        return this.startToContainingMethod;
    }
}
