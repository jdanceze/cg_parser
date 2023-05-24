package soot.dava.internal.asg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.cli.HelpFormatter;
import soot.Unit;
import soot.coffi.Instruction;
import soot.dava.Dava;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.TrapUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/asg/AugmentedStmtGraph.class */
public class AugmentedStmtGraph implements DirectedGraph<AugmentedStmt> {
    private HashMap<Stmt, AugmentedStmt> binding;
    private HashMap<AugmentedStmt, AugmentedStmt> original2clone;
    private IterableSet<AugmentedStmt> aug_list;
    private IterableSet<Stmt> stmt_list;
    private List<AugmentedStmt> bheads;
    private List<AugmentedStmt> btails;
    private List<AugmentedStmt> cheads;
    private List<AugmentedStmt> ctails;

    public AugmentedStmtGraph(AugmentedStmtGraph other) {
        this();
        HashMap<AugmentedStmt, AugmentedStmt> old2new = new HashMap<>();
        Iterator<AugmentedStmt> it = other.aug_list.iterator();
        while (it.hasNext()) {
            AugmentedStmt oas = it.next();
            Stmt s = oas.get_Stmt();
            AugmentedStmt nas = new AugmentedStmt(s);
            this.aug_list.add(nas);
            this.stmt_list.add(s);
            this.binding.put(s, nas);
            old2new.put(oas, nas);
        }
        Iterator<AugmentedStmt> it2 = other.aug_list.iterator();
        while (it2.hasNext()) {
            AugmentedStmt oas2 = it2.next();
            AugmentedStmt nas2 = old2new.get(oas2);
            for (AugmentedStmt aug : oas2.bpreds) {
                nas2.bpreds.add(old2new.get(aug));
            }
            if (nas2.bpreds.isEmpty()) {
                this.bheads.add(nas2);
            }
            for (AugmentedStmt aug2 : oas2.cpreds) {
                nas2.cpreds.add(old2new.get(aug2));
            }
            if (nas2.cpreds.isEmpty()) {
                this.cheads.add(nas2);
            }
            for (AugmentedStmt aug3 : oas2.bsuccs) {
                nas2.bsuccs.add(old2new.get(aug3));
            }
            if (nas2.bsuccs.isEmpty()) {
                this.btails.add(nas2);
            }
            for (AugmentedStmt aug4 : oas2.csuccs) {
                nas2.csuccs.add(old2new.get(aug4));
            }
            if (nas2.csuccs.isEmpty()) {
                this.ctails.add(nas2);
            }
        }
        find_Dominators();
    }

    public AugmentedStmtGraph(BriefUnitGraph bug, TrapUnitGraph cug) {
        this();
        Dava.v().log("AugmentedStmtGraph::AugmentedStmtGraph() - cug.size() = " + cug.size());
        Iterator<Unit> it = cug.iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt s = (Stmt) u;
            add_StmtBinding(s, new AugmentedStmt(s));
        }
        List<Unit> cugList = new PseudoTopologicalOrderer().newList(cug, false);
        for (Unit u2 : cugList) {
            Stmt s2 = (Stmt) u2;
            this.aug_list.add(get_AugStmt(s2));
            this.stmt_list.add(s2);
        }
        Iterator<AugmentedStmt> it2 = this.aug_list.iterator();
        while (it2.hasNext()) {
            AugmentedStmt as = it2.next();
            mirror_PredsSuccs(as, bug);
            mirror_PredsSuccs(as, cug);
        }
        find_Dominators();
    }

    public AugmentedStmtGraph() {
        this.binding = new HashMap<>();
        this.original2clone = new HashMap<>();
        this.aug_list = new IterableSet<>();
        this.stmt_list = new IterableSet<>();
        this.bheads = new LinkedList();
        this.btails = new LinkedList();
        this.cheads = new LinkedList();
        this.ctails = new LinkedList();
    }

    public void add_AugmentedStmt(AugmentedStmt as) {
        Stmt s = as.get_Stmt();
        this.aug_list.add(as);
        this.stmt_list.add(s);
        add_StmtBinding(s, as);
        if (as.bpreds.isEmpty()) {
            this.bheads.add(as);
        }
        if (as.cpreds.isEmpty()) {
            this.cheads.add(as);
        }
        if (as.bsuccs.isEmpty()) {
            this.btails.add(as);
        }
        if (as.csuccs.isEmpty()) {
            this.ctails.add(as);
        }
        check_List(as.bpreds, this.btails);
        check_List(as.bsuccs, this.bheads);
        check_List(as.cpreds, this.ctails);
        check_List(as.csuccs, this.cheads);
    }

    public boolean contains(Object o) {
        return this.aug_list.contains(o);
    }

    public AugmentedStmt get_CloneOf(AugmentedStmt as) {
        return this.original2clone.get(as);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.aug_list.size();
    }

    private <T> void check_List(List<T> psList, List<T> htList) {
        for (T t : psList) {
            if (htList.contains(t)) {
                htList.remove(t);
            }
        }
    }

    public void calculate_Reachability(AugmentedStmt source, Set<AugmentedStmt> blockers, AugmentedStmt dominator) {
        if (blockers == null) {
            throw new RuntimeException("Tried to call AugmentedStmtGraph:calculate_Reachability() with null blockers.");
        }
        if (source == null) {
            return;
        }
        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
        HashSet<AugmentedStmt> touchSet = new HashSet<>();
        worklist.addLast(source);
        touchSet.add(source);
        while (!worklist.isEmpty()) {
            AugmentedStmt as = worklist.removeFirst();
            for (AugmentedStmt sas : as.csuccs) {
                if (!touchSet.contains(sas) && sas.get_Dominators().contains(dominator)) {
                    touchSet.add(sas);
                    IterableSet<AugmentedStmt> reachers = sas.get_Reachers();
                    if (!reachers.contains(source)) {
                        reachers.add(source);
                    }
                    if (!blockers.contains(sas)) {
                        worklist.addLast(sas);
                    }
                }
            }
        }
    }

    public void calculate_Reachability(Collection<AugmentedStmt> sources, Set<AugmentedStmt> blockers, AugmentedStmt dominator) {
        for (AugmentedStmt next : sources) {
            calculate_Reachability(next, blockers, dominator);
        }
    }

    public void calculate_Reachability(AugmentedStmt source, AugmentedStmt blocker, AugmentedStmt dominator) {
        HashSet<AugmentedStmt> h = new HashSet<>();
        h.add(blocker);
        calculate_Reachability(source, h, dominator);
    }

    public void calculate_Reachability(Collection<AugmentedStmt> sources, AugmentedStmt blocker, AugmentedStmt dominator) {
        HashSet<AugmentedStmt> h = new HashSet<>();
        h.add(blocker);
        calculate_Reachability(sources, h, dominator);
    }

    public void calculate_Reachability(AugmentedStmt source, AugmentedStmt dominator) {
        calculate_Reachability(source, Collections.emptySet(), dominator);
    }

    public void calculate_Reachability(Collection<AugmentedStmt> sources, AugmentedStmt dominator) {
        calculate_Reachability(sources, Collections.emptySet(), dominator);
    }

    public void calculate_Reachability(AugmentedStmt source) {
        calculate_Reachability(source, (AugmentedStmt) null);
    }

    public void calculate_Reachability(Collection<AugmentedStmt> sources) {
        calculate_Reachability(sources, (AugmentedStmt) null);
    }

    public void add_StmtBinding(Stmt s, AugmentedStmt as) {
        this.binding.put(s, as);
    }

    public AugmentedStmt get_AugStmt(Stmt s) {
        AugmentedStmt as = this.binding.get(s);
        if (as == null) {
            throw new RuntimeException("Could not find augmented statement for: " + s.toString());
        }
        return as;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<AugmentedStmt> getHeads() {
        return this.cheads;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<AugmentedStmt> getTails() {
        return this.ctails;
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<AugmentedStmt> iterator() {
        return this.aug_list.iterator();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<AugmentedStmt> getPredsOf(AugmentedStmt s) {
        return s.cpreds;
    }

    public List<AugmentedStmt> getPredsOf(Stmt s) {
        return get_AugStmt(s).cpreds;
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<AugmentedStmt> getSuccsOf(AugmentedStmt s) {
        return s.csuccs;
    }

    public List<AugmentedStmt> getSuccsOf(Stmt s) {
        return get_AugStmt(s).csuccs;
    }

    public List<AugmentedStmt> get_BriefHeads() {
        return this.bheads;
    }

    public List<AugmentedStmt> get_BriefTails() {
        return this.btails;
    }

    public IterableSet<AugmentedStmt> get_ChainView() {
        return new IterableSet<>(this.aug_list);
    }

    public Object clone() {
        return new AugmentedStmtGraph(this);
    }

    public boolean remove_AugmentedStmt(AugmentedStmt toRemove) {
        if (!this.aug_list.contains(toRemove)) {
            return false;
        }
        for (AugmentedStmt pas : toRemove.bpreds) {
            if (pas.bsuccs.contains(toRemove)) {
                pas.bsuccs.remove(toRemove);
            }
        }
        for (AugmentedStmt pas2 : toRemove.cpreds) {
            if (pas2.csuccs.contains(toRemove)) {
                pas2.csuccs.remove(toRemove);
            }
        }
        for (AugmentedStmt sas : toRemove.bsuccs) {
            if (sas.bpreds.contains(toRemove)) {
                sas.bpreds.remove(toRemove);
            }
        }
        for (AugmentedStmt sas2 : toRemove.csuccs) {
            if (sas2.cpreds.contains(toRemove)) {
                sas2.cpreds.remove(toRemove);
            }
        }
        this.aug_list.remove(toRemove);
        this.stmt_list.remove(toRemove.get_Stmt());
        this.bheads.remove(toRemove);
        this.btails.remove(toRemove);
        this.cheads.remove(toRemove);
        this.ctails.remove(toRemove);
        this.binding.remove(toRemove.get_Stmt());
        return true;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("AugmentedStmtGraph (size: ").append(size()).append(" stmts)").append("\n");
        Iterator<AugmentedStmt> it = this.aug_list.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            b.append("| .---").append("\n").append("| | AugmentedStmt ").append(as.toString()).append("\n").append("| |").append("\n").append("| |  preds:");
            for (AugmentedStmt pas : as.cpreds) {
                b.append(Instruction.argsep).append(pas.toString());
            }
            b.append("\n").append("| |").append("\n").append("| |  succs:");
            for (AugmentedStmt sas : as.csuccs) {
                b.append(Instruction.argsep).append(sas.toString());
            }
            b.append("\n").append("| |").append("\n").append("| |  doms:");
            Iterator<AugmentedStmt> it2 = as.get_Dominators().iterator();
            while (it2.hasNext()) {
                AugmentedStmt das = it2.next();
                b.append(Instruction.argsep).append(das.toString());
            }
            b.append("\n").append("| `---").append("\n");
        }
        b.append(HelpFormatter.DEFAULT_OPT_PREFIX).append("\n");
        return b.toString();
    }

    private void mirror_PredsSuccs(AugmentedStmt as, UnitGraph ug) {
        Stmt s = as.get_Stmt();
        LinkedList<AugmentedStmt> preds = new LinkedList<>();
        LinkedList<AugmentedStmt> succs = new LinkedList<>();
        for (Unit u : ug.getPredsOf((Unit) s)) {
            AugmentedStmt po = get_AugStmt((Stmt) u);
            if (!preds.contains(po)) {
                preds.add(po);
            }
        }
        for (Unit u2 : ug.getSuccsOf((Unit) s)) {
            AugmentedStmt so = get_AugStmt((Stmt) u2);
            if (!succs.contains(so)) {
                succs.add(so);
            }
        }
        if (ug instanceof BriefUnitGraph) {
            as.bpreds = preds;
            as.bsuccs = succs;
            if (preds.isEmpty()) {
                this.bheads.add(as);
            }
            if (succs.isEmpty()) {
                this.btails.add(as);
            }
        } else if (ug instanceof TrapUnitGraph) {
            as.cpreds = preds;
            as.csuccs = succs;
            if (preds.isEmpty()) {
                this.cheads.add(as);
            }
            if (succs.isEmpty()) {
                this.ctails.add(as);
            }
        } else {
            throw new RuntimeException("Unknown UnitGraph type: " + ug.getClass());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public IterableSet<AugmentedStmt> clone_Body(IterableSet<AugmentedStmt> oldBody) {
        HashMap<AugmentedStmt, AugmentedStmt> old2new = new HashMap<>();
        HashMap<AugmentedStmt, AugmentedStmt> new2old = new HashMap<>();
        IterableSet<AugmentedStmt> newBody = new IterableSet<>();
        Iterator<AugmentedStmt> it = oldBody.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            AugmentedStmt clone = (AugmentedStmt) as.clone();
            this.original2clone.put(as, clone);
            old2new.put(as, clone);
            new2old.put(clone, as);
            newBody.add(clone);
        }
        Iterator<AugmentedStmt> it2 = newBody.iterator();
        while (it2.hasNext()) {
            AugmentedStmt newAs = it2.next();
            AugmentedStmt oldAs = new2old.get(newAs);
            mirror_PredsSuccs(oldAs, oldAs.bpreds, newAs.bpreds, old2new);
            mirror_PredsSuccs(oldAs, oldAs.cpreds, newAs.cpreds, old2new);
            mirror_PredsSuccs(oldAs, oldAs.bsuccs, newAs.bsuccs, old2new);
            mirror_PredsSuccs(oldAs, oldAs.csuccs, newAs.csuccs, old2new);
        }
        Iterator<AugmentedStmt> it3 = newBody.iterator();
        while (it3.hasNext()) {
            AugmentedStmt au = it3.next();
            add_AugmentedStmt(au);
        }
        HashMap<Unit, Stmt> so2n = new HashMap<>();
        Iterator<AugmentedStmt> it4 = oldBody.iterator();
        while (it4.hasNext()) {
            AugmentedStmt as2 = it4.next();
            so2n.put(as2.get_Stmt(), old2new.get(as2).get_Stmt());
        }
        Iterator<AugmentedStmt> it5 = newBody.iterator();
        while (it5.hasNext()) {
            AugmentedStmt nas = it5.next();
            AugmentedStmt oas = new2old.get(nas);
            Stmt ns = nas.get_Stmt();
            Stmt os = oas.get_Stmt();
            if (os instanceof IfStmt) {
                Unit target = ((IfStmt) os).getTarget();
                Unit newTgt = so2n.get(target);
                ((IfStmt) ns).setTarget(newTgt == 0 ? target : newTgt);
            } else if (os instanceof TableSwitchStmt) {
                TableSwitchStmt otss = (TableSwitchStmt) os;
                TableSwitchStmt ntss = (TableSwitchStmt) ns;
                Unit target2 = otss.getDefaultTarget();
                Stmt newTgt2 = so2n.get(target2);
                ntss.setDefaultTarget(newTgt2 == null ? target2 : newTgt2);
                LinkedList<Unit> new_target_list = new LinkedList<>();
                int target_count = (otss.getHighIndex() - otss.getLowIndex()) + 1;
                for (int i = 0; i < target_count; i++) {
                    Unit target3 = otss.getTarget(i);
                    Stmt newTgt3 = so2n.get(target3);
                    new_target_list.add(newTgt3 == null ? target3 : newTgt3);
                }
                ntss.setTargets(new_target_list);
            } else if (os instanceof LookupSwitchStmt) {
                LookupSwitchStmt olss = (LookupSwitchStmt) os;
                LookupSwitchStmt nlss = (LookupSwitchStmt) ns;
                Unit target4 = olss.getDefaultTarget();
                Stmt newTgt4 = so2n.get(target4);
                nlss.setDefaultTarget(newTgt4 == null ? target4 : newTgt4);
                Unit[] new_target_list2 = new Unit[olss.getTargetCount()];
                for (int i2 = 0; i2 < new_target_list2.length; i2++) {
                    Unit target5 = olss.getTarget(i2);
                    Stmt newTgt5 = so2n.get(target5);
                    new_target_list2[i2] = newTgt5 == null ? target5 : newTgt5;
                }
                nlss.setTargets(new_target_list2);
                nlss.setLookupValues(olss.getLookupValues());
            }
        }
        return newBody;
    }

    private void mirror_PredsSuccs(AugmentedStmt originalAs, List<AugmentedStmt> oldList, List<AugmentedStmt> newList, Map<AugmentedStmt, AugmentedStmt> old2new) {
        for (AugmentedStmt oldAs : oldList) {
            AugmentedStmt newAs = old2new.get(oldAs);
            if (newAs != null) {
                newList.add(newAs);
            } else {
                newList.add(oldAs);
                AugmentedStmt clonedAs = old2new.get(originalAs);
                if (oldList == originalAs.bpreds) {
                    oldAs.bsuccs.add(clonedAs);
                } else if (oldList == originalAs.cpreds) {
                    oldAs.csuccs.add(clonedAs);
                } else if (oldList == originalAs.bsuccs) {
                    oldAs.bpreds.add(clonedAs);
                } else if (oldList == originalAs.csuccs) {
                    oldAs.cpreds.add(clonedAs);
                } else {
                    throw new RuntimeException("Error mirroring preds and succs in Try block splitting.");
                }
            }
        }
    }

    public void find_Dominators() {
        Iterator<AugmentedStmt> it = this.aug_list.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            IterableSet<AugmentedStmt> doms = as.get_Dominators();
            doms.clear();
            if (!as.cpreds.isEmpty()) {
                doms.addAll(this.aug_list);
            }
        }
        IterableSet<AugmentedStmt> worklist = new IterableSet<>();
        worklist.addAll(this.aug_list);
        while (!worklist.isEmpty()) {
            AugmentedStmt as2 = (AugmentedStmt) worklist.getFirst();
            worklist.removeFirst();
            IterableSet<AugmentedStmt> pred_intersection = new IterableSet<>();
            boolean first_pred = true;
            for (AugmentedStmt pas : as2.cpreds) {
                if (first_pred) {
                    pred_intersection.addAll(pas.get_Dominators());
                    if (!pred_intersection.contains(pas)) {
                        pred_intersection.add(pas);
                    }
                    first_pred = false;
                } else {
                    Iterator<AugmentedStmt> piit = pred_intersection.snapshotIterator();
                    while (piit.hasNext()) {
                        AugmentedStmt pid = piit.next();
                        if (!pas.get_Dominators().contains(pid) && pas != pid) {
                            pred_intersection.remove(pid);
                        }
                    }
                }
            }
            if (!as2.get_Dominators().equals(pred_intersection)) {
                for (AugmentedStmt o : as2.csuccs) {
                    if (!worklist.contains(o)) {
                        worklist.add(o);
                    }
                }
                as2.get_Dominators().clear();
                as2.get_Dominators().addAll(pred_intersection);
            }
        }
    }
}
