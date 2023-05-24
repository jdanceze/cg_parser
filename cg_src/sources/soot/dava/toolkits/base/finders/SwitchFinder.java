package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import soot.G;
import soot.Singletons;
import soot.Value;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETSwitchNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/SwitchFinder.class */
public class SwitchFinder implements FactFinder {
    private IterableSet junkBody;
    private HashSet targetSet;
    private LinkedList targetList;
    private LinkedList snTargetList;
    private LinkedList tSuccList;
    private HashMap index2target;
    private HashMap tSucc2indexSet;
    private HashMap tSucc2target;
    private HashMap tSucc2Body;

    public SwitchFinder(Singletons.Global g) {
    }

    public static SwitchFinder v() {
        return G.v().soot_dava_toolkits_base_finders_SwitchFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody davaBody, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        Dava.v().log("SwitchFinder::find()");
        Iterator asgit = asg.iterator();
        while (asgit.hasNext()) {
            AugmentedStmt as = asgit.next();
            Stmt s = as.get_Stmt();
            if ((s instanceof TableSwitchStmt) || (s instanceof LookupSwitchStmt)) {
                Value key = null;
                this.junkBody = new IterableSet();
                this.targetSet = new HashSet();
                this.targetList = new LinkedList();
                this.snTargetList = new LinkedList();
                this.tSuccList = new LinkedList();
                this.index2target = new HashMap();
                this.tSucc2indexSet = new HashMap();
                this.tSucc2target = new HashMap();
                this.tSucc2Body = new HashMap();
                if (s instanceof TableSwitchStmt) {
                    TableSwitchStmt tss = (TableSwitchStmt) s;
                    int target_count = (tss.getHighIndex() - tss.getLowIndex()) + 1;
                    for (int i = 0; i < target_count; i++) {
                        build_Bindings(as, new Integer(i + tss.getLowIndex()), asg.get_AugStmt((Stmt) tss.getTarget(i)));
                    }
                    build_Bindings(as, "default", asg.get_AugStmt((Stmt) tss.getDefaultTarget()));
                    key = tss.getKey();
                } else if (s instanceof LookupSwitchStmt) {
                    LookupSwitchStmt lss = (LookupSwitchStmt) s;
                    int target_count2 = lss.getTargetCount();
                    for (int i2 = 0; i2 < target_count2; i2++) {
                        build_Bindings(as, new Integer(lss.getLookupValue(i2)), asg.get_AugStmt((Stmt) lss.getTarget(i2)));
                    }
                    build_Bindings(as, "default", asg.get_AugStmt((Stmt) lss.getDefaultTarget()));
                    key = lss.getKey();
                }
                Iterator tsit = this.tSuccList.iterator();
                while (tsit.hasNext()) {
                    AugmentedStmt tSucc = (AugmentedStmt) tsit.next();
                    AugmentedStmt target = (AugmentedStmt) this.tSucc2target.get(tSucc);
                    this.snTargetList.addLast(new SwitchNode(target, (TreeSet) this.tSucc2indexSet.get(tSucc), (IterableSet) this.tSucc2Body.get(tSucc)));
                }
                TreeSet targetHeads = new TreeSet();
                TreeSet killBodies = new TreeSet();
                asg.calculate_Reachability(this.targetList, this.targetSet, as);
                SwitchNodeGraph sng = new SwitchNodeGraph(this.snTargetList);
                killBodies.addAll(this.snTargetList);
                this.snTargetList = new LinkedList();
                LinkedList worklist = new LinkedList();
                worklist.addAll(sng.getHeads());
                while (!worklist.isEmpty()) {
                    SwitchNode sn = (SwitchNode) worklist.removeFirst();
                    this.snTargetList.addLast(sn);
                    killBodies.remove(sn);
                    SwitchNode champ = null;
                    for (SwitchNode ssn : sn.get_Succs()) {
                        if (champ == null || champ.get_Score() < ssn.get_Score()) {
                            champ = ssn;
                        }
                    }
                    if (champ != null && champ.get_Score() > 0) {
                        worklist.addLast(champ);
                    }
                }
                Iterator kit = killBodies.iterator();
                while (kit.hasNext()) {
                    SwitchNode sn2 = (SwitchNode) kit.next();
                    IterableSet snBody = sn2.get_Body();
                    snBody.clear();
                    snBody.add(sn2.get_AugStmt());
                }
                SwitchNodeGraph sng2 = new SwitchNodeGraph(this.snTargetList);
                targetHeads.addAll(sng2.getHeads());
                LinkedList<SwitchNode> switchNodeList = new LinkedList<>();
                while (true) {
                    if (targetHeads.isEmpty() && killBodies.isEmpty()) {
                        break;
                    } else if (targetHeads.isEmpty() || (!targetHeads.isEmpty() && !killBodies.isEmpty() && ((SwitchNode) targetHeads.first()).compareTo(killBodies.first()) > 0)) {
                        SwitchNode nextNode = (SwitchNode) killBodies.first();
                        killBodies.remove(nextNode);
                        switchNodeList.addLast(nextNode);
                    } else {
                        SwitchNode nextNode2 = (SwitchNode) targetHeads.first();
                        targetHeads.remove(nextNode2);
                        while (true) {
                            switchNodeList.addLast(nextNode2);
                            if (nextNode2.get_Succs().isEmpty()) {
                                break;
                            }
                            nextNode2 = (SwitchNode) nextNode2.get_Succs().get(0);
                        }
                    }
                }
                IterableSet body = new IterableSet();
                body.add(as);
                Iterator<SwitchNode> it = switchNodeList.iterator();
                while (it.hasNext()) {
                    SwitchNode sn3 = it.next();
                    body.addAll(sn3.get_Body());
                    if (sn3.get_IndexSet().contains("default")) {
                        sn3.get_IndexSet().clear();
                        sn3.get_IndexSet().add("default");
                    }
                }
                body.addAll(this.junkBody);
                Iterator<ExceptionNode> it2 = davaBody.get_ExceptionFacts().iterator();
                while (it2.hasNext()) {
                    ExceptionNode en = it2.next();
                    IterableSet tryBody = en.get_TryBody();
                    if (tryBody.contains(as)) {
                        Iterator fbit = body.snapshotIterator();
                        while (fbit.hasNext()) {
                            AugmentedStmt fbas = (AugmentedStmt) fbit.next();
                            if (!tryBody.contains(fbas)) {
                                body.remove(fbas);
                                Iterator<SwitchNode> it3 = switchNodeList.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        break;
                                    }
                                    IterableSet switchBody = it3.next().get_Body();
                                    if (switchBody.contains(fbas)) {
                                        switchBody.remove(fbas);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                SET.nest(new SETSwitchNode(as, key, body, switchNodeList, this.junkBody));
            }
        }
    }

    private IterableSet find_SubBody(AugmentedStmt switchAS, AugmentedStmt branchS) {
        IterableSet subBody = new IterableSet();
        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
        subBody.add(branchS);
        AugmentedStmt branchS2 = branchS.bsuccs.get(0);
        if (branchS2.get_Dominators().contains(switchAS)) {
            worklist.addLast(branchS2);
            subBody.add(branchS2);
        }
        while (!worklist.isEmpty()) {
            AugmentedStmt as = worklist.removeFirst();
            for (AugmentedStmt sas : as.csuccs) {
                if (!subBody.contains(sas) && sas.get_Dominators().contains(branchS2)) {
                    worklist.addLast(sas);
                    subBody.add(sas);
                }
            }
        }
        return subBody;
    }

    private void build_Bindings(AugmentedStmt swAs, Object index, AugmentedStmt target) {
        AugmentedStmt tSucc = target.bsuccs.get(0);
        if (this.targetSet.add(tSucc)) {
            this.targetList.addLast(tSucc);
        }
        this.index2target.put(index, target);
        TreeSet treeSet = (TreeSet) this.tSucc2indexSet.get(tSucc);
        TreeSet indices = treeSet;
        if (treeSet == null) {
            indices = new TreeSet(new IndexComparator());
            this.tSucc2indexSet.put(tSucc, indices);
            this.tSucc2target.put(tSucc, target);
            this.tSucc2Body.put(tSucc, find_SubBody(swAs, target));
            this.tSuccList.add(tSucc);
        } else {
            this.junkBody.add(target);
            for (AugmentedStmt augmentedStmt : target.bsuccs) {
                augmentedStmt.bpreds.remove(target);
            }
            for (AugmentedStmt augmentedStmt2 : target.csuccs) {
                augmentedStmt2.cpreds.remove(target);
            }
            target.bsuccs.clear();
            target.csuccs.clear();
        }
        indices.add(index);
    }
}
