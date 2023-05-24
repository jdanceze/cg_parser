package soot.dava.toolkits.base.finders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETSynchronizedBlockNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JGotoStmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/SynchronizedBlockFinder.class */
public class SynchronizedBlockFinder implements FactFinder {
    private HashMap<AugmentedStmt, Map<Value, Integer>> as2ml;
    private DavaBody davaBody;
    private IterableSet monitorLocalSet;
    private IterableSet monitorEnterSet;
    private final Integer WHITE = new Integer(0);
    private final Integer GRAY = new Integer(1);
    private final Integer BLACK = new Integer(2);
    private final int UNKNOWN = -100000;
    private final Integer VARIABLE_INCR = new Integer(-100000);
    private final String THROWABLE = "java.lang.Throwable";

    public SynchronizedBlockFinder(Singletons.Global g) {
    }

    public static SynchronizedBlockFinder v() {
        return G.v().soot_dava_toolkits_base_finders_SynchronizedBlockFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        IterableSet synchSet;
        this.davaBody = body;
        Dava.v().log("SynchronizedBlockFinder::find()");
        this.as2ml = new HashMap<>();
        IterableSet synchronizedBlockFacts = body.get_SynchronizedBlockFacts();
        synchronizedBlockFacts.clear();
        set_MonitorLevels(asg);
        Map<AugmentedStmt, IterableSet> as2synchSet = build_SynchSets();
        IterableSet usedMonitors = new IterableSet();
        AugmentedStmt previousStmt = null;
        Iterator asgit = asg.iterator();
        while (asgit.hasNext()) {
            AugmentedStmt as = asgit.next();
            if ((as.get_Stmt() instanceof EnterMonitorStmt) && (synchSet = as2synchSet.get(as)) != null) {
                IterableSet synchBody = get_BodyApproximation(as, synchSet);
                Value local = ((EnterMonitorStmt) as.get_Stmt()).getOp();
                Value copiedLocal = null;
                if (previousStmt != null) {
                    Stmt previousS = previousStmt.get_Stmt();
                    if (previousS instanceof DefinitionStmt) {
                        DefinitionStmt previousDef = (DefinitionStmt) previousS;
                        Value rightPrevious = previousDef.getRightOp();
                        if (rightPrevious.toString().compareTo(local.toString()) == 0) {
                            copiedLocal = previousDef.getLeftOp();
                        }
                    }
                }
                Integer level = this.as2ml.get(as).get(local);
                Iterator enit = body.get_ExceptionFacts().iterator();
                boolean done = false;
                while (true) {
                    if (!enit.hasNext()) {
                        break;
                    }
                    ExceptionNode en = enit.next();
                    if (verify_CatchBody(en, (IterableSet) synchBody.clone(), local, copiedLocal)) {
                        if (SET.nest(new SETSynchronizedBlockNode(en, local))) {
                            done = true;
                            Iterator ssit = synchSet.iterator();
                            while (ssit.hasNext()) {
                                AugmentedStmt ssas = (AugmentedStmt) ssit.next();
                                Stmt sss = ssas.get_Stmt();
                                if (sss instanceof MonitorStmt) {
                                    if (((MonitorStmt) sss).getOp() == local && this.as2ml.get(ssas).get(local).equals(level) && !usedMonitors.contains(ssas)) {
                                        usedMonitors.add(ssas);
                                    } else if (((MonitorStmt) sss).getOp() == copiedLocal && !usedMonitors.contains(ssas)) {
                                        usedMonitors.add(ssas);
                                    }
                                }
                            }
                            synchronizedBlockFacts.add(en);
                        }
                    }
                }
                if (!done) {
                    throw new RuntimeException("Could not verify approximated Synchronized body!\nMethod:\n" + body.getMethod() + "Body:\n===============================================================\n" + body.getUnits() + "===============================================================\n");
                }
            }
            previousStmt = as;
        }
        IterableSet<AugmentedStmt> monitorFacts = body.get_MonitorFacts();
        monitorFacts.clear();
        Iterator<AugmentedStmt> it = asg.iterator();
        while (it.hasNext()) {
            AugmentedStmt as2 = it.next();
            if ((as2.get_Stmt() instanceof MonitorStmt) && !usedMonitors.contains(as2)) {
                monitorFacts.add(as2);
            }
        }
    }

    private void find_VariableIncreasing(AugmentedStmtGraph asg, HashMap local2level_template, LinkedList<AugmentedStmt> viAugStmts, HashMap<AugmentedStmt, LinkedList<Value>> as2locals) {
        StronglyConnectedComponentsFast scc = new StronglyConnectedComponentsFast(asg);
        IterableSet viSeeds = new IterableSet();
        HashMap as2color = new HashMap();
        HashMap as2rml = new HashMap();
        Iterator asgit = asg.iterator();
        while (asgit.hasNext()) {
            as2rml.put(asgit.next(), local2level_template.clone());
        }
        for (List componentList : scc.getTrueComponents()) {
            IterableSet component = new IterableSet();
            component.addAll(componentList);
            Iterator cit = component.iterator();
            while (cit.hasNext()) {
                as2color.put(cit.next(), this.WHITE);
            }
            AugmentedStmt seedStmt = (AugmentedStmt) component.getFirst();
            DFS_Scc(seedStmt, component, as2rml, as2color, seedStmt, viSeeds);
        }
        IterableSet worklist = new IterableSet();
        worklist.addAll(viSeeds);
        while (!worklist.isEmpty()) {
            AugmentedStmt as = (AugmentedStmt) worklist.getFirst();
            worklist.removeFirst();
            HashMap local2level = (HashMap) as2rml.get(as);
            for (AugmentedStmt sas : as.csuccs) {
                HashMap<Value, Integer> slocal2level = (HashMap) as2rml.get(sas);
                Iterator mlsit = this.monitorLocalSet.iterator();
                while (mlsit.hasNext()) {
                    Value local = (Value) mlsit.next();
                    if (local2level.get(local) == this.VARIABLE_INCR && slocal2level.get(local) != this.VARIABLE_INCR) {
                        slocal2level.put(local, this.VARIABLE_INCR);
                        if (!worklist.contains(sas)) {
                            worklist.addLast(sas);
                        }
                    }
                }
            }
        }
        Iterator asgit2 = asg.iterator();
        while (asgit2.hasNext()) {
            AugmentedStmt as2 = asgit2.next();
            HashMap local2level2 = (HashMap) as2rml.get(as2);
            Iterator mlsit2 = this.monitorLocalSet.iterator();
            while (mlsit2.hasNext()) {
                Value local2 = (Value) mlsit2.next();
                if (local2level2.get(local2) == this.VARIABLE_INCR) {
                    if (!viAugStmts.isEmpty()) {
                        if (viAugStmts.getLast() != as2) {
                            viAugStmts.addLast(as2);
                        }
                    } else {
                        viAugStmts.addLast(as2);
                    }
                    LinkedList<Value> linkedList = as2locals.get(as2);
                    LinkedList<Value> locals = linkedList;
                    if (linkedList == null) {
                        locals = new LinkedList<>();
                        as2locals.put(as2, locals);
                    }
                    locals.addLast(local2);
                }
            }
        }
    }

    private void DFS_Scc(AugmentedStmt as, IterableSet component, HashMap as2rml, HashMap as2color, AugmentedStmt seedStmt, IterableSet viSeeds) {
        as2color.put(as, this.GRAY);
        Stmt s = as.get_Stmt();
        HashMap<Value, Integer> local2level = (HashMap) as2rml.get(as);
        if (s instanceof MonitorStmt) {
            Value local = ((MonitorStmt) s).getOp();
            if (s instanceof EnterMonitorStmt) {
                local2level.put(local, new Integer(local2level.get(local).intValue() + 1));
            } else {
                local2level.put(local, new Integer(local2level.get(local).intValue() - 1));
            }
        }
        for (AugmentedStmt sas : as.csuccs) {
            if (component.contains(sas)) {
                HashMap<Value, Integer> slocal2level = (HashMap) as2rml.get(sas);
                Integer scolor = (Integer) as2color.get(sas);
                if (scolor.equals(this.WHITE)) {
                    Iterator mlsit = this.monitorLocalSet.iterator();
                    while (mlsit.hasNext()) {
                        Value local2 = (Value) mlsit.next();
                        slocal2level.put(local2, local2level.get(local2));
                    }
                    DFS_Scc(sas, component, as2rml, as2color, seedStmt, viSeeds);
                } else {
                    Iterator mlsit2 = this.monitorLocalSet.iterator();
                    while (mlsit2.hasNext()) {
                        Value local3 = (Value) mlsit2.next();
                        if (slocal2level.get(local3).intValue() < local2level.get(local3).intValue()) {
                            slocal2level.put(local3, this.VARIABLE_INCR);
                            if (!viSeeds.contains(sas)) {
                                viSeeds.add(sas);
                            }
                        }
                    }
                }
            }
        }
        as2color.put(as, this.BLACK);
    }

    private Map<AugmentedStmt, IterableSet> build_SynchSets() {
        HashMap<AugmentedStmt, IterableSet> as2synchSet = new HashMap<>();
        Iterator mesit = this.monitorEnterSet.iterator();
        while (mesit.hasNext()) {
            AugmentedStmt headAs = (AugmentedStmt) mesit.next();
            Value local = ((EnterMonitorStmt) headAs.get_Stmt()).getOp();
            IterableSet synchSet = new IterableSet();
            int monitorLevel = this.as2ml.get(headAs).get(local).intValue();
            IterableSet worklist = new IterableSet();
            worklist.add(headAs);
            while (true) {
                if (!worklist.isEmpty()) {
                    AugmentedStmt as = (AugmentedStmt) worklist.getFirst();
                    worklist.removeFirst();
                    Stmt s = as.get_Stmt();
                    if (!(s instanceof DefinitionStmt) || ((DefinitionStmt) s).getLeftOp() != local) {
                        synchSet.add(as);
                        for (AugmentedStmt sas : as.csuccs) {
                            int sml = this.as2ml.get(sas).get(local).intValue();
                            if (sas.get_Dominators().contains(headAs) && sml >= monitorLevel && !worklist.contains(sas) && !synchSet.contains(sas)) {
                                worklist.addLast(sas);
                            }
                        }
                    }
                } else {
                    as2synchSet.put(headAs, synchSet);
                    break;
                }
            }
        }
        return as2synchSet;
    }

    private void set_MonitorLevels(AugmentedStmtGraph asg) {
        this.monitorLocalSet = new IterableSet();
        this.monitorEnterSet = new IterableSet();
        Iterator<AugmentedStmt> asgit = asg.iterator();
        while (asgit.hasNext()) {
            AugmentedStmt as = asgit.next();
            Stmt s = as.get_Stmt();
            if (s instanceof MonitorStmt) {
                Value local = ((MonitorStmt) s).getOp();
                if (!this.monitorLocalSet.contains(local)) {
                    this.monitorLocalSet.add(local);
                }
                if (s instanceof EnterMonitorStmt) {
                    this.monitorEnterSet.add(as);
                }
            }
        }
        HashMap local2level_template = new HashMap();
        Iterator mlsit = this.monitorLocalSet.iterator();
        while (mlsit.hasNext()) {
            local2level_template.put(mlsit.next(), new Integer(0));
        }
        Iterator<AugmentedStmt> asgit2 = asg.iterator();
        while (asgit2.hasNext()) {
            this.as2ml.put(asgit2.next(), (Map) local2level_template.clone());
        }
        LinkedList<AugmentedStmt> viAugStmts = new LinkedList<>();
        HashMap<AugmentedStmt, LinkedList<Value>> incrAs2locals = new HashMap<>();
        find_VariableIncreasing(asg, local2level_template, viAugStmts, incrAs2locals);
        Iterator<AugmentedStmt> viasit = viAugStmts.iterator();
        while (viasit.hasNext()) {
            AugmentedStmt vias = viasit.next();
            Map local2level = this.as2ml.get(vias);
            Iterator lit = incrAs2locals.get(vias).iterator();
            while (lit.hasNext()) {
                local2level.put(lit.next(), this.VARIABLE_INCR);
            }
        }
        IterableSet worklist = new IterableSet();
        worklist.addAll(this.monitorEnterSet);
        while (!worklist.isEmpty()) {
            AugmentedStmt as2 = (AugmentedStmt) worklist.getFirst();
            worklist.removeFirst();
            Map<Value, Integer> cur_local2level = this.as2ml.get(as2);
            for (AugmentedStmt pas : as2.cpreds) {
                Stmt s2 = as2.get_Stmt();
                Map pred_local2level = this.as2ml.get(pas);
                Iterator mlsit2 = this.monitorLocalSet.iterator();
                while (mlsit2.hasNext()) {
                    Value local2 = (Value) mlsit2.next();
                    int predLevel = pred_local2level.get(local2).intValue();
                    Stmt ps = pas.get_Stmt();
                    if (predLevel != -100000) {
                        if (ps instanceof ExitMonitorStmt) {
                            ExitMonitorStmt ems = (ExitMonitorStmt) ps;
                            if (ems.getOp() == local2 && predLevel > 0) {
                                predLevel--;
                            }
                        }
                        if (s2 instanceof EnterMonitorStmt) {
                            EnterMonitorStmt ems2 = (EnterMonitorStmt) s2;
                            if (ems2.getOp() == local2 && predLevel >= 0) {
                                predLevel++;
                            }
                        }
                        int curLevel = cur_local2level.get(local2).intValue();
                        if (predLevel > curLevel) {
                            cur_local2level.put(local2, new Integer(predLevel));
                            for (AugmentedStmt augmentedStmt : as2.csuccs) {
                                if (!worklist.contains(augmentedStmt)) {
                                    worklist.add(augmentedStmt);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeOtherDominatedStmts(IterableSet synchBody, AugmentedStmt sas) {
        ArrayList toRemove = new ArrayList();
        Iterator it = synchBody.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = (AugmentedStmt) it.next();
            IterableSet doms = as.get_Dominators();
            if (doms.contains(sas)) {
                toRemove.add(as);
            }
        }
        Iterator it2 = toRemove.iterator();
        while (it2.hasNext()) {
            synchBody.remove((AugmentedStmt) it2.next());
        }
    }

    private boolean verify_CatchBody(ExceptionNode en, IterableSet synchBody, Value monitorVariable, Value copiedLocal) {
        IterableSet tryBodySet = en.get_TryBody();
        Iterator tempIt = tryBodySet.iterator();
        AugmentedStmt tempas = null;
        loop0: while (tempIt.hasNext()) {
            tempas = tempIt.next();
            for (AugmentedStmt succAS : tempas.bsuccs) {
                if (!tryBodySet.contains(succAS)) {
                    break loop0;
                }
            }
        }
        if (tempas != null) {
            for (AugmentedStmt sas : tempas.bsuccs) {
                synchBody.remove(sas);
                removeOtherDominatedStmts(synchBody, sas);
            }
        }
        Iterator tempIt2 = en.get_TryBody().iterator();
        while (tempIt2.hasNext()) {
            AugmentedStmt as = tempIt2.next();
            if (as.get_Stmt() instanceof ExitMonitorStmt) {
                List csuccs = as.csuccs;
                csuccs.remove(as.bsuccs);
                for (AugmentedStmt as1 : csuccs) {
                    if (as1.get_Stmt() instanceof GotoStmt) {
                        Unit target = ((JGotoStmt) as1.get_Stmt()).getTarget();
                        if (target instanceof DefinitionStmt) {
                            DefinitionStmt defStmt = (DefinitionStmt) target;
                            if (!(defStmt.getRightOp() instanceof CaughtExceptionRef)) {
                                synchBody.remove(as1);
                                removeOtherDominatedStmts(synchBody, as1);
                            } else {
                                Value leftOp = defStmt.getLeftOp();
                                HashSet params = new HashSet();
                                params.addAll(this.davaBody.get_CaughtRefs());
                                Iterator localIt = this.davaBody.getLocals().iterator();
                                String typeName = "";
                                while (true) {
                                    if (!localIt.hasNext()) {
                                        break;
                                    }
                                    Local local = localIt.next();
                                    if (local.toString().compareTo(leftOp.toString()) == 0) {
                                        Type t = local.getType();
                                        typeName = t.toString();
                                        break;
                                    }
                                }
                                if (typeName.compareTo("java.lang.Throwable") != 0) {
                                    synchBody.remove(as1);
                                    removeOtherDominatedStmts(synchBody, as1);
                                }
                            }
                        } else {
                            synchBody.remove(as1);
                            synchBody.remove(as1.bsuccs.get(0));
                            removeOtherDominatedStmts(synchBody, as1);
                        }
                    }
                }
            }
        }
        AugmentedStmt synchEnter = null;
        Iterator synchIt = synchBody.iterator();
        loop6: while (true) {
            if (!synchIt.hasNext()) {
                break;
            }
            AugmentedStmt as2 = (AugmentedStmt) synchIt.next();
            for (AugmentedStmt pas : as2.cpreds) {
                if (!synchBody.contains(pas)) {
                    Stmt pasStmt = pas.get_Stmt();
                    if (pasStmt instanceof EnterMonitorStmt) {
                        synchEnter = as2;
                        break loop6;
                    }
                }
            }
        }
        if (synchEnter == null) {
            throw new RuntimeException("Could not find enter stmt of the synchBody: " + this.davaBody.getMethod().getSignature());
        }
        boolean unChanged = false;
        while (!unChanged) {
            unChanged = true;
            List<AugmentedStmt> toRemove = new ArrayList<>();
            Iterator synchIt2 = synchBody.iterator();
            while (synchIt2.hasNext()) {
                AugmentedStmt synchAs = (AugmentedStmt) synchIt2.next();
                if (synchAs != synchEnter) {
                    boolean remove = true;
                    for (AugmentedStmt pAs : synchAs.cpreds) {
                        if (synchBody.contains(pAs)) {
                            remove = false;
                        }
                    }
                    if (remove) {
                        toRemove.add(synchAs);
                    }
                }
            }
            if (toRemove.size() > 0) {
                synchBody.removeAll(toRemove);
                unChanged = false;
            }
        }
        if (!en.get_Body().equals(synchBody) || !en.get_Exception().getName().equals("java.lang.Throwable") || en.get_CatchList().size() > 1) {
            return false;
        }
        IterableSet catchBody = en.get_CatchBody();
        AugmentedStmt entryPoint = null;
        Iterator it = catchBody.iterator();
        loop11: while (true) {
            if (!it.hasNext()) {
                break;
            }
            AugmentedStmt as3 = it.next();
            for (AugmentedStmt pas2 : as3.cpreds) {
                if (!catchBody.contains(pas2)) {
                    entryPoint = as3;
                    break loop11;
                }
            }
        }
        Unit entryPointTarget = null;
        if (entryPoint.get_Stmt() instanceof GotoStmt) {
            entryPointTarget = ((JGotoStmt) entryPoint.get_Stmt()).getTarget();
        }
        AugmentedStmt as4 = entryPoint;
        if (as4.bsuccs.size() != 1) {
            return false;
        }
        while (as4.get_Stmt() instanceof GotoStmt) {
            as4 = as4.bsuccs.get(0);
            if (as4.bsuccs.size() != 1) {
                return false;
            }
            if (entryPointTarget != null) {
                if (as4.get_Stmt() != entryPointTarget && as4.cpreds.size() != 1 && as4.cpreds.size() != 1) {
                    return false;
                }
            } else if (as4 != entryPoint && as4.cpreds.size() != 1) {
                return false;
            }
        }
        Stmt s = as4.get_Stmt();
        if (!(s instanceof DefinitionStmt)) {
            return false;
        }
        DefinitionStmt ds = (DefinitionStmt) s;
        Value asnFrom = ds.getRightOp();
        if (!(asnFrom instanceof CaughtExceptionRef) || !((RefType) ((CaughtExceptionRef) asnFrom).getType()).getSootClass().getName().equals("java.lang.Throwable")) {
            return false;
        }
        Value throwlocal = ds.getLeftOp();
        IterableSet esuccs = new IterableSet();
        esuccs.addAll(as4.csuccs);
        esuccs.removeAll(as4.bsuccs);
        AugmentedStmt as5 = as4.bsuccs.get(0);
        Stmt stmt = as5.get_Stmt();
        while (true) {
            Stmt s2 = stmt;
            if (!(s2 instanceof DefinitionStmt) || ((DefinitionStmt) s2).getRightOp().toString().compareTo(throwlocal.toString()) != 0) {
                break;
            }
            throwlocal = ((DefinitionStmt) s2).getLeftOp();
            as5 = as5.bsuccs.get(0);
            stmt = as5.get_Stmt();
        }
        if (as5.bsuccs.size() != 1 || as5.cpreds.size() != 1) {
            return false;
        }
        checkProtectionArea(as5, ds);
        Stmt s3 = as5.get_Stmt();
        if (!(s3 instanceof ExitMonitorStmt)) {
            return false;
        }
        if (((ExitMonitorStmt) s3).getOp() != monitorVariable && ((ExitMonitorStmt) s3).getOp() != copiedLocal) {
            return false;
        }
        AugmentedStmt as6 = as5.bsuccs.get(0);
        if (as6.bsuccs.size() != 0 || as6.cpreds.size() != 1 || !verify_ESuccs(as6, esuccs)) {
            return false;
        }
        Stmt s4 = as6.get_Stmt();
        if (!(s4 instanceof ThrowStmt) || ((ThrowStmt) s4).getOp() != throwlocal) {
            return false;
        }
        return true;
    }

    private boolean checkProtectionArea(AugmentedStmt as, DefinitionStmt s) {
        IterableSet esuccs = new IterableSet();
        esuccs.addAll(as.csuccs);
        esuccs.removeAll(as.bsuccs);
        Iterator it = esuccs.iterator();
        while (it.hasNext()) {
            AugmentedStmt tempas = (AugmentedStmt) it.next();
            Stmt temps = tempas.get_Stmt();
            if (temps instanceof GotoStmt) {
                Unit target = ((GotoStmt) temps).getTarget();
                if (target != s) {
                    return false;
                }
            } else if (temps != s) {
                return false;
            }
        }
        return true;
    }

    private boolean verify_ESuccs(AugmentedStmt as, IterableSet ref) {
        IterableSet esuccs = new IterableSet();
        esuccs.addAll(as.csuccs);
        esuccs.removeAll(as.bsuccs);
        return esuccs.equals(ref);
    }

    private IterableSet get_BodyApproximation(AugmentedStmt head, IterableSet synchSet) {
        IterableSet body = (IterableSet) synchSet.clone();
        Value local = ((EnterMonitorStmt) head.get_Stmt()).getOp();
        Integer level = this.as2ml.get(head).get(local);
        body.remove(head);
        Iterator bit = body.snapshotIterator();
        while (bit.hasNext()) {
            AugmentedStmt as = (AugmentedStmt) bit.next();
            Stmt s = as.get_Stmt();
            if ((s instanceof ExitMonitorStmt) && ((ExitMonitorStmt) s).getOp() == local && this.as2ml.get(as).get(local).equals(level)) {
                for (AugmentedStmt sas : as.csuccs) {
                    if (sas.get_Dominators().contains(head)) {
                        Stmt ss = sas.get_Stmt();
                        if ((ss instanceof GotoStmt) || (ss instanceof ThrowStmt)) {
                            if (!body.contains(sas)) {
                                body.add(sas);
                            }
                        }
                    }
                }
            }
        }
        return body;
    }
}
