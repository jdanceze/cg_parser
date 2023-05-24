package soot.dava.toolkits.base.finders;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETCycleNode;
import soot.dava.internal.SET.SETDoWhileNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETUnconditionalWhileNode;
import soot.dava.internal.SET.SETWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GTableSwitchStmt;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.internal.JGotoStmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.util.IterableSet;
import soot.util.StationaryArrayList;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/CycleFinder.class */
public class CycleFinder implements FactFinder {
    private static final Logger logger = LoggerFactory.getLogger(CycleFinder.class);

    public CycleFinder(Singletons.Global g) {
    }

    public static CycleFinder v() {
        return G.v().soot_dava_toolkits_base_finders_CycleFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        SETCycleNode sETDoWhileNode;
        Dava.v().log("CycleFinder::find()");
        AugmentedStmtGraph wasg = (AugmentedStmtGraph) asg.clone();
        List<List<AugmentedStmt>> build_component_list = build_component_list(wasg);
        while (true) {
            List<List<AugmentedStmt>> component_list = build_component_list;
            if (!component_list.isEmpty()) {
                IterableSet<AugmentedStmt> node_list = new IterableSet<>();
                for (List<AugmentedStmt> cal : component_list) {
                    node_list.clear();
                    node_list.addAll(cal);
                    IterableSet<AugmentedStmt> entry_points = get_EntryPoint(node_list);
                    if (entry_points.size() > 1) {
                        LinkedList<AugmentedStmt> asgEntryPoints = new LinkedList<>();
                        Iterator<AugmentedStmt> it = entry_points.iterator();
                        while (it.hasNext()) {
                            AugmentedStmt au = it.next();
                            asgEntryPoints.addLast(asg.get_AugStmt(au.get_Stmt()));
                        }
                        IterableSet<AugmentedStmt> asgScc = new IterableSet<>();
                        Iterator<AugmentedStmt> it2 = node_list.iterator();
                        while (it2.hasNext()) {
                            AugmentedStmt au2 = it2.next();
                            asgScc.addLast(asg.get_AugStmt(au2.get_Stmt()));
                        }
                        fix_MultiEntryPoint(body, asg, asgEntryPoints, asgScc);
                        throw new RetriggerAnalysisException();
                    }
                    AugmentedStmt succ_stmt = null;
                    AugmentedStmt entry_point = entry_points.getFirst();
                    AugmentedStmt characterizing_stmt = find_CharacterizingStmt(entry_point, node_list, wasg);
                    if (characterizing_stmt != null) {
                        for (AugmentedStmt au3 : characterizing_stmt.bsuccs) {
                            succ_stmt = au3;
                            if (!node_list.contains(succ_stmt)) {
                                break;
                            }
                        }
                    }
                    wasg.calculate_Reachability(succ_stmt, new HashSet(), entry_point);
                    IterableSet<AugmentedStmt> cycle_body = get_CycleBody(entry_point, succ_stmt, asg, wasg);
                    if (characterizing_stmt != null) {
                        Iterator<ExceptionNode> it3 = body.get_ExceptionFacts().iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                break;
                            }
                            ExceptionNode en = it3.next();
                            IterableSet<AugmentedStmt> tryBody = en.get_TryBody();
                            if (tryBody.contains(asg.get_AugStmt(characterizing_stmt.get_Stmt()))) {
                                Iterator<AugmentedStmt> it4 = cycle_body.iterator();
                                while (it4.hasNext()) {
                                    AugmentedStmt cbas = it4.next();
                                    if (!tryBody.contains(cbas)) {
                                        characterizing_stmt = null;
                                        break;
                                    }
                                }
                                continue;
                            }
                        }
                    }
                    if (characterizing_stmt == null) {
                        wasg.remove_AugmentedStmt(entry_point);
                        sETDoWhileNode = new SETUnconditionalWhileNode(cycle_body);
                    } else {
                        body.consume_Condition(asg.get_AugStmt(characterizing_stmt.get_Stmt()));
                        wasg.remove_AugmentedStmt(characterizing_stmt);
                        IfStmt condition = (IfStmt) characterizing_stmt.get_Stmt();
                        if (!cycle_body.contains(asg.get_AugStmt(condition.getTarget()))) {
                            condition.setCondition(ConditionFlipper.flip((ConditionExpr) condition.getCondition()));
                        }
                        if (characterizing_stmt == entry_point) {
                            sETDoWhileNode = new SETWhileNode(asg.get_AugStmt(characterizing_stmt.get_Stmt()), cycle_body);
                        } else {
                            sETDoWhileNode = new SETDoWhileNode(asg.get_AugStmt(characterizing_stmt.get_Stmt()), asg.get_AugStmt(entry_point.get_Stmt()), cycle_body);
                        }
                    }
                    SETCycleNode newNode = sETDoWhileNode;
                    SET.nest(newNode);
                }
                build_component_list = build_component_list(wasg);
            } else {
                return;
            }
        }
    }

    private IterableSet<AugmentedStmt> get_EntryPoint(IterableSet<AugmentedStmt> nodeList) {
        IterableSet<AugmentedStmt> entryPoints = new IterableSet<>();
        Iterator<AugmentedStmt> it = nodeList.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            Iterator<AugmentedStmt> it2 = as.cpreds.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                AugmentedStmt po = it2.next();
                if (!nodeList.contains(po)) {
                    entryPoints.add(as);
                    break;
                }
            }
        }
        return entryPoints;
    }

    private List<List<AugmentedStmt>> build_component_list(AugmentedStmtGraph asg) {
        List<List<AugmentedStmt>> c_list = new LinkedList<>();
        for (List<AugmentedStmt> wcomp : new StronglyConnectedComponentsFast(asg).getComponents()) {
            int size = wcomp.size();
            if (size > 1) {
                c_list.add(wcomp);
            } else if (size == 1) {
                AugmentedStmt as = wcomp.get(0);
                if (as.cpreds.contains(as) && as.csuccs.contains(as)) {
                    List<AugmentedStmt> currentComponent = new StationaryArrayList<>();
                    currentComponent.add(as);
                    c_list.add(currentComponent);
                }
            }
        }
        return c_list;
    }

    private AugmentedStmt find_CharacterizingStmt(AugmentedStmt entry_point, IterableSet<AugmentedStmt> sc_component, AugmentedStmtGraph asg) {
        if (entry_point.get_Stmt() instanceof IfStmt) {
            for (AugmentedStmt au : entry_point.bsuccs) {
                if (!sc_component.contains(au)) {
                    return entry_point;
                }
            }
        }
        IterableSet<AugmentedStmt> candidates = new IterableSet<>();
        HashMap<AugmentedStmt, AugmentedStmt> candSuccMap = new HashMap<>();
        HashSet<AugmentedStmt> blockers = new HashSet<>();
        Iterator<AugmentedStmt> it = entry_point.bpreds.iterator();
        while (it.hasNext()) {
            AugmentedStmt pas = it.next();
            Stmt pasStmt = pas.get_Stmt();
            if ((pasStmt instanceof GotoStmt) && pas.bpreds.size() == 1) {
                pas = pas.bpreds.get(0);
            }
            if (sc_component.contains(pas) && (pasStmt instanceof IfStmt)) {
                Iterator<AugmentedStmt> it2 = pas.bsuccs.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    AugmentedStmt spas = it2.next();
                    if (!sc_component.contains(spas)) {
                        candidates.add(pas);
                        candSuccMap.put(pas, spas);
                        blockers.add(spas);
                        break;
                    }
                }
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.getFirst();
        }
        asg.calculate_Reachability(candidates, blockers, entry_point);
        IterableSet<AugmentedStmt> max_Reach_Set = null;
        int reachSize = 0;
        Iterator<AugmentedStmt> it3 = candidates.iterator();
        while (it3.hasNext()) {
            AugmentedStmt as = it3.next();
            int current_reach_size = candSuccMap.get(as).get_Reachers().intersection(candidates).size();
            if (current_reach_size > reachSize) {
                max_Reach_Set = new IterableSet<>();
                reachSize = current_reach_size;
            }
            if (current_reach_size == reachSize) {
                max_Reach_Set.add(as);
            }
        }
        IterableSet<AugmentedStmt> candidates2 = max_Reach_Set;
        if (candidates2 == null) {
            throw new RuntimeException("Did not find a suitable candidate");
        }
        if (candidates2.size() == 1) {
            return (AugmentedStmt) candidates2.getFirst();
        }
        HashSet<Object> touchSet = new HashSet<>();
        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
        worklist.addLast(entry_point);
        touchSet.add(entry_point);
        while (!worklist.isEmpty()) {
            for (AugmentedStmt so : worklist.removeFirst().csuccs) {
                if (candidates2.contains(so)) {
                    return so;
                }
                if (sc_component.contains(so) && !touchSet.contains(so)) {
                    worklist.addLast(so);
                    touchSet.add(so);
                }
            }
        }
        throw new RuntimeException("Somehow didn't find a condition for a do-while loop!");
    }

    private IterableSet<AugmentedStmt> get_CycleBody(AugmentedStmt entry_point, AugmentedStmt boundary_stmt, AugmentedStmtGraph asg, AugmentedStmtGraph wasg) {
        IterableSet<AugmentedStmt> cycle_body = new IterableSet<>();
        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
        AugmentedStmt asg_ep = asg.get_AugStmt(entry_point.get_Stmt());
        worklist.add(entry_point);
        cycle_body.add(asg_ep);
        while (!worklist.isEmpty()) {
            AugmentedStmt as = worklist.removeFirst();
            for (AugmentedStmt wsas : as.csuccs) {
                AugmentedStmt sas = asg.get_AugStmt(wsas.get_Stmt());
                if (!cycle_body.contains(sas) && !cycle_body.contains(sas) && sas.get_Dominators().contains(asg_ep) && (boundary_stmt == null || (!wsas.get_Reachers().contains(boundary_stmt) && wsas != boundary_stmt))) {
                    worklist.add(wsas);
                    cycle_body.add(sas);
                }
            }
        }
        return cycle_body;
    }

    private void fix_MultiEntryPoint(DavaBody body, AugmentedStmtGraph asg, LinkedList<AugmentedStmt> entry_points, IterableSet<AugmentedStmt> scc) {
        AugmentedStmt naturalEntryPoint = get_NaturalEntryPoint(entry_points, scc);
        Local controlLocal = body.get_ControlLocal();
        TableSwitchStmt tss = new GTableSwitchStmt(controlLocal, 0, entry_points.size() - 2, Collections.nCopies(entry_points.size(), null), naturalEntryPoint.get_Stmt());
        AugmentedStmt dispatchStmt = new AugmentedStmt(tss);
        IterableSet<AugmentedStmt> predecessorSet = new IterableSet<>();
        IterableSet<AugmentedStmt> indirectionStmtSet = new IterableSet<>();
        IterableSet<AugmentedStmt> directionStmtSet = new IterableSet<>();
        int count = 0;
        Iterator<AugmentedStmt> it = entry_points.iterator();
        while (it.hasNext()) {
            AugmentedStmt entryPoint = it.next();
            GotoStmt gotoStmt = new JGotoStmt(entryPoint.get_Stmt());
            AugmentedStmt indirectionStmt = new AugmentedStmt(gotoStmt);
            indirectionStmtSet.add(indirectionStmt);
            int i = count;
            count++;
            tss.setTarget(i, gotoStmt);
            dispatchStmt.add_BSucc(indirectionStmt);
            indirectionStmt.add_BPred(dispatchStmt);
            indirectionStmt.add_BSucc(entryPoint);
            entryPoint.add_BPred(indirectionStmt);
            asg.add_AugmentedStmt(indirectionStmt);
            LinkedList<AugmentedStmt> toRemove = new LinkedList<>();
            for (AugmentedStmt pas : entryPoint.cpreds) {
                if (pas != indirectionStmt && (entryPoint == naturalEntryPoint || !scc.contains(pas))) {
                    if (!scc.contains(pas)) {
                        predecessorSet.add(pas);
                    }
                    AssignStmt asnStmt = new GAssignStmt(controlLocal, DIntConstant.v(count, null));
                    AugmentedStmt directionStmt = new AugmentedStmt(asnStmt);
                    directionStmtSet.add(directionStmt);
                    patch_Stmt(pas.get_Stmt(), entryPoint.get_Stmt(), asnStmt);
                    toRemove.addLast(pas);
                    pas.csuccs.remove(entryPoint);
                    pas.csuccs.add(directionStmt);
                    if (pas.bsuccs.contains(entryPoint)) {
                        pas.bsuccs.remove(entryPoint);
                        pas.bsuccs.add(directionStmt);
                    }
                    directionStmt.cpreds.add(pas);
                    if (pas.bsuccs.contains(directionStmt)) {
                        directionStmt.bpreds.add(pas);
                    }
                    directionStmt.add_BSucc(dispatchStmt);
                    dispatchStmt.add_BPred(directionStmt);
                    asg.add_AugmentedStmt(directionStmt);
                }
            }
            Iterator<AugmentedStmt> it2 = toRemove.iterator();
            while (it2.hasNext()) {
                AugmentedStmt ras = it2.next();
                entryPoint.cpreds.remove(ras);
                if (entryPoint.bpreds.contains(ras)) {
                    entryPoint.bpreds.remove(ras);
                }
            }
        }
        asg.add_AugmentedStmt(dispatchStmt);
        Iterator<ExceptionNode> it3 = body.get_ExceptionFacts().iterator();
        while (it3.hasNext()) {
            ExceptionNode en = it3.next();
            IterableSet<AugmentedStmt> tryBody = en.get_TryBody();
            Iterator<AugmentedStmt> it4 = entry_points.iterator();
            while (true) {
                if (it4.hasNext()) {
                    AugmentedStmt au = it4.next();
                    if (!tryBody.contains(au)) {
                        break;
                    }
                } else {
                    en.add_TryStmts(indirectionStmtSet);
                    en.add_TryStmt(dispatchStmt);
                    Iterator<T> it5 = predecessorSet.iterator();
                    while (true) {
                        if (it5.hasNext()) {
                            AugmentedStmt au2 = (AugmentedStmt) it5.next();
                            if (!tryBody.contains(au2)) {
                                break;
                            }
                        } else {
                            en.add_TryStmts(directionStmtSet);
                            break;
                        }
                    }
                }
            }
        }
    }

    private AugmentedStmt get_NaturalEntryPoint(LinkedList<AugmentedStmt> entry_points, IterableSet<AugmentedStmt> scc) {
        AugmentedStmt best_candidate = null;
        int minScore = 0;
        Iterator<AugmentedStmt> it = entry_points.iterator();
        while (it.hasNext()) {
            AugmentedStmt entryPoint = it.next();
            HashSet<AugmentedStmt> touchSet = new HashSet<>();
            HashSet<AugmentedStmt> backTargets = new HashSet<>();
            touchSet.add(entryPoint);
            DFS(entryPoint, touchSet, backTargets, scc);
            if (best_candidate == null || backTargets.size() < minScore) {
                minScore = touchSet.size();
                best_candidate = entryPoint;
            }
        }
        return best_candidate;
    }

    private void DFS(AugmentedStmt as, HashSet<AugmentedStmt> touchSet, HashSet<AugmentedStmt> backTargets, IterableSet<AugmentedStmt> scc) {
        for (AugmentedStmt sas : as.csuccs) {
            if (scc.contains(sas)) {
                if (touchSet.contains(sas)) {
                    if (!backTargets.contains(sas)) {
                        backTargets.add(sas);
                    }
                } else {
                    touchSet.add(sas);
                    DFS(sas, touchSet, backTargets, scc);
                    touchSet.remove(sas);
                }
            }
        }
    }

    private void patch_Stmt(Stmt src, Stmt oldDst, Stmt newDst) {
        if (src instanceof GotoStmt) {
            ((GotoStmt) src).setTarget(newDst);
        } else if (src instanceof IfStmt) {
            IfStmt ifs = (IfStmt) src;
            if (ifs.getTarget() == oldDst) {
                ifs.setTarget(newDst);
            }
        } else {
            if (src instanceof TableSwitchStmt) {
                TableSwitchStmt tss = (TableSwitchStmt) src;
                if (tss.getDefaultTarget() == oldDst) {
                    tss.setDefaultTarget(newDst);
                    return;
                }
                int e = tss.getHighIndex();
                for (int i = tss.getLowIndex(); i <= e; i++) {
                    if (tss.getTarget(i) == oldDst) {
                        tss.setTarget(i, newDst);
                        return;
                    }
                }
            }
            if (src instanceof LookupSwitchStmt) {
                LookupSwitchStmt lss = (LookupSwitchStmt) src;
                if (lss.getDefaultTarget() == oldDst) {
                    lss.setDefaultTarget(newDst);
                    return;
                }
                int e2 = lss.getTargetCount();
                for (int i2 = 0; i2 < e2; i2++) {
                    if (lss.getTarget(i2) == oldDst) {
                        lss.setTarget(i2, newDst);
                        return;
                    }
                }
            }
        }
    }
}
