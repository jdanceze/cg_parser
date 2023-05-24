package soot.shimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import soot.Local;
import soot.PatchingChain;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.shimple.PiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ReversibleGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/PiNodeManager.class */
public class PiNodeManager {
    protected final ShimpleBody body;
    protected final ShimpleFactory sf;
    protected final boolean trimmed;
    protected ReversibleGraph<Block> cfg;
    protected MultiMap<Local, Block> varToBlocks;

    public PiNodeManager(ShimpleBody body, boolean trimmed, ShimpleFactory sf) {
        this.body = body;
        this.trimmed = trimmed;
        this.sf = sf;
    }

    public void update() {
        ReversibleGraph<Block> oldCfg = this.cfg;
        this.cfg = this.sf.getReverseBlockGraph();
        if (oldCfg != this.cfg) {
            this.varToBlocks = null;
        }
    }

    public boolean insertTrivialPiNodes() {
        update();
        this.varToBlocks = new HashMultiMap();
        MultiMap<Local, Block> localsToUsePoints = new SHashMultiMap<>();
        for (Block block : this.cfg) {
            Iterator<Unit> it = block.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                for (ValueBox next : unit.getUseBoxes()) {
                    Value use = next.getValue();
                    if (use instanceof Local) {
                        localsToUsePoints.put((Local) use, block);
                    }
                }
                if (Shimple.isPiNode(unit)) {
                    this.varToBlocks.put(Shimple.getLhsLocal(unit), block);
                }
            }
        }
        boolean change = false;
        DominatorTree<Block> dt = this.sf.getReverseDominatorTree();
        DominanceFrontier<Block> df = this.sf.getReverseDominanceFrontier();
        int iterCount = 0;
        int[] workFlags = new int[this.cfg.size()];
        int[] hasAlreadyFlags = new int[this.cfg.size()];
        Stack<Block> workList = new Stack<>();
        for (Local local : localsToUsePoints.keySet()) {
            iterCount++;
            for (Block block2 : localsToUsePoints.get(local)) {
                workFlags[block2.getIndexInMethod()] = iterCount;
                workList.push(block2);
            }
            while (!workList.empty()) {
                for (DominatorNode<Block> frontierNode : df.getDominanceFrontierOf(dt.getDode(workList.pop()))) {
                    Block frontierBlock = frontierNode.getGode();
                    int fBIndex = frontierBlock.getIndexInMethod();
                    if (hasAlreadyFlags[fBIndex] < iterCount) {
                        hasAlreadyFlags[fBIndex] = iterCount;
                        insertPiNodes(local, frontierBlock);
                        change = true;
                        if (workFlags[fBIndex] < iterCount) {
                            workFlags[fBIndex] = iterCount;
                            workList.push(frontierBlock);
                        }
                    }
                }
            }
        }
        if (change) {
            this.sf.clearCache();
        }
        return change;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void insertPiNodes(soot.Local r7, soot.toolkits.graph.Block r8) {
        /*
            r6 = this;
            r0 = r6
            soot.util.MultiMap<soot.Local, soot.toolkits.graph.Block> r0 = r0.varToBlocks
            r1 = r7
            java.util.Set r0 = r0.get(r1)
            r1 = r8
            java.util.List r1 = r1.getSuccs()
            r2 = 0
            java.lang.Object r1 = r1.get(r2)
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L1d
            return
        L1d:
            r0 = r8
            soot.Unit r0 = r0.getTail()
            r9 = r0
            r0 = r6
            boolean r0 = r0.trimmed
            if (r0 == 0) goto L61
            r0 = r9
            java.util.List r0 = r0.getUseBoxes()
            java.util.Iterator r0 = r0.iterator()
            r11 = r0
            goto L56
        L39:
            r0 = r11
            java.lang.Object r0 = r0.next()
            soot.ValueBox r0 = (soot.ValueBox) r0
            r10 = r0
            r0 = r10
            soot.Value r0 = r0.getValue()
            r1 = r7
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L56
            goto L61
        L56:
            r0 = r11
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L39
            return
        L61:
            r0 = r9
            boolean r0 = r0 instanceof soot.jimple.IfStmt
            if (r0 == 0) goto L74
            r0 = r6
            r1 = r7
            r2 = r9
            soot.jimple.IfStmt r2 = (soot.jimple.IfStmt) r2
            r0.piHandleIfStmt(r1, r2)
            goto La3
        L74:
            r0 = r9
            boolean r0 = r0 instanceof soot.jimple.LookupSwitchStmt
            if (r0 != 0) goto L82
            r0 = r9
            boolean r0 = r0 instanceof soot.jimple.TableSwitchStmt
            if (r0 == 0) goto L8b
        L82:
            r0 = r6
            r1 = r7
            r2 = r9
            r0.piHandleSwitchStmt(r1, r2)
            goto La3
        L8b:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            java.lang.String r4 = "Assertion failed: Unhandled stmt: "
            r3.<init>(r4)
            r3 = r9
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        La3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.shimple.internal.PiNodeManager.insertPiNodes(soot.Local, soot.toolkits.graph.Block):void");
    }

    public void piHandleIfStmt(Local local, IfStmt u) {
        Unit predOfTarget;
        PatchingChain<Unit> units = this.body.getUnits();
        Unit target = u.getTarget();
        Unit addt = Jimple.v().newAssignStmt(local, Shimple.v().newPiExpr(local, u, Boolean.TRUE));
        Unit addf = Jimple.v().newAssignStmt(local, Shimple.v().newPiExpr(local, u, Boolean.FALSE));
        units.insertAfter(addf, (Unit) u);
        try {
            predOfTarget = units.getPredOf((PatchingChain<Unit>) target);
        } catch (NoSuchElementException e) {
            predOfTarget = null;
        }
        if (predOfTarget != null && predOfTarget.fallsThrough()) {
            units.insertAfter(Jimple.v().newGotoStmt(target), predOfTarget);
        }
        units.getNonPatchingChain().insertBefore(addt, target);
        u.setTarget(addt);
    }

    public void piHandleSwitchStmt(Local local, Unit u) {
        Unit predOfTarget;
        List<UnitBox> targetBoxes = new ArrayList<>();
        List<Object> targetKeys = new ArrayList<>();
        if (u instanceof LookupSwitchStmt) {
            LookupSwitchStmt lss = (LookupSwitchStmt) u;
            targetBoxes.add(lss.getDefaultTargetBox());
            targetKeys.add("default");
            int e = lss.getTargetCount();
            for (int i = 0; i < e; i++) {
                targetBoxes.add(lss.getTargetBox(i));
            }
            targetKeys.addAll(lss.getLookupValues());
        } else if (u instanceof TableSwitchStmt) {
            TableSwitchStmt tss = (TableSwitchStmt) u;
            int low = tss.getLowIndex();
            int hi = tss.getHighIndex();
            targetBoxes.add(tss.getDefaultTargetBox());
            targetKeys.add("default");
            int e2 = hi - low;
            for (int i2 = 0; i2 <= e2; i2++) {
                targetBoxes.add(tss.getTargetBox(i2));
            }
            for (int i3 = low; i3 <= hi; i3++) {
                targetKeys.add(Integer.valueOf(i3));
            }
        } else {
            throw new RuntimeException("Assertion failed.");
        }
        int e3 = targetBoxes.size();
        for (int count = 0; count < e3; count++) {
            UnitBox targetBox = targetBoxes.get(count);
            Unit target = targetBox.getUnit();
            PatchingChain<Unit> units = this.body.getUnits();
            try {
                predOfTarget = units.getPredOf((PatchingChain<Unit>) target);
            } catch (NoSuchElementException e4) {
                predOfTarget = null;
            }
            if (predOfTarget != null && predOfTarget.fallsThrough()) {
                units.insertAfter(Jimple.v().newGotoStmt(target), predOfTarget);
            }
            Unit add1 = Jimple.v().newAssignStmt(local, Shimple.v().newPiExpr(local, u, targetKeys.get(count)));
            units.getNonPatchingChain().insertBefore(add1, target);
            targetBox.setUnit(add1);
        }
    }

    public void eliminatePiNodes(boolean smart) {
        if (smart) {
            Map<Value, Value> newToOld = new HashMap<>();
            List<ValueBox> boxes = new ArrayList<>();
            Iterator<Unit> unitsIt = this.body.getUnits().iterator();
            while (unitsIt.hasNext()) {
                Unit u = unitsIt.next();
                PiExpr pe = Shimple.getPiExpr(u);
                if (pe != null) {
                    newToOld.put(Shimple.getLhsLocal(u), pe.getValue());
                    unitsIt.remove();
                } else {
                    boxes.addAll(u.getUseBoxes());
                }
            }
            for (ValueBox box : boxes) {
                Value value = box.getValue();
                Value old = newToOld.get(value);
                if (old != null) {
                    box.setValue(old);
                }
            }
            DeadAssignmentEliminator.v().transform(this.body);
            CopyPropagator.v().transform(this.body);
            DeadAssignmentEliminator.v().transform(this.body);
            return;
        }
        Iterator<Unit> it = this.body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u2 = it.next();
            PiExpr pe2 = Shimple.getPiExpr(u2);
            if (pe2 != null) {
                ((AssignStmt) u2).setRightOp(pe2.getValue());
            }
        }
    }

    public static List<ValueBox> getUseBoxesFromBlock(Block block) {
        List<ValueBox> useBoxesList = new ArrayList<>();
        Iterator<Unit> it = block.iterator();
        while (it.hasNext()) {
            Unit next = it.next();
            useBoxesList.addAll(next.getUseBoxes());
        }
        return useBoxesList;
    }
}
