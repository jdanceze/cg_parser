package soot.shimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.options.ShimpleOptions;
import soot.shimple.DefaultShimpleFactory;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.scalar.UnusedLocalEliminator;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/ShimpleBodyBuilder.class */
public class ShimpleBodyBuilder {
    private static String freshSeparator = "_";
    protected final ShimpleBody body;
    protected final ShimpleOptions options;
    protected final ShimpleFactory sf;
    protected List<Local> origLocals;
    protected Map<String, Local> newLocals;
    protected Map<Local, Local> newLocalsToOldLocal;
    protected int[] assignmentCounters;
    protected Stack<Integer>[] namingStacks;
    public final PhiNodeManager phi;
    public final PiNodeManager pi;

    public ShimpleBodyBuilder(ShimpleBody body) {
        NopEliminator.v().transform(body);
        this.body = body;
        this.options = body.getOptions();
        this.sf = new DefaultShimpleFactory(body);
        this.sf.clearCache();
        this.phi = new PhiNodeManager(body, this.sf);
        this.pi = new PiNodeManager(body, false, this.sf);
        makeUniqueLocalNames();
    }

    public void update() {
        this.origLocals = Collections.unmodifiableList(new ArrayList(this.body.getLocals()));
    }

    /* JADX WARN: Incorrect condition in loop: B:9:0x003d */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void transform() {
        /*
            r2 = this;
            r0 = r2
            soot.shimple.ShimpleFactory r0 = r0.sf
            r0.clearCache()
            r0 = r2
            r0.update()
            r0 = r2
            soot.shimple.internal.PhiNodeManager r0 = r0.phi
            boolean r0 = r0.insertTrivialPhiNodes()
            r0 = r2
            soot.options.ShimpleOptions r0 = r0.options
            boolean r0 = r0.extended()
            if (r0 == 0) goto L40
            r0 = r2
            soot.shimple.internal.PiNodeManager r0 = r0.pi
            boolean r0 = r0.insertTrivialPiNodes()
            r3 = r0
            goto L3c
        L2a:
            r0 = r2
            soot.shimple.internal.PhiNodeManager r0 = r0.phi
            boolean r0 = r0.insertTrivialPhiNodes()
            if (r0 == 0) goto L40
            r0 = r2
            soot.shimple.internal.PiNodeManager r0 = r0.pi
            boolean r0 = r0.insertTrivialPiNodes()
            r3 = r0
        L3c:
            r0 = r3
            if (r0 != 0) goto L2a
        L40:
            r0 = r2
            r0.renameLocals()
            r0 = r2
            soot.shimple.internal.PhiNodeManager r0 = r0.phi
            r0.trimExceptionalPhiNodes()
            r0 = r2
            r0.makeUniqueLocalNames()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.shimple.internal.ShimpleBodyBuilder.transform():void");
    }

    public void preElimOpt() {
    }

    public void postElimOpt() {
        if (this.options.node_elim_opt()) {
            DeadAssignmentEliminator.v().transform(this.body);
            UnreachableCodeEliminator.v().transform(this.body);
            UnconditionalBranchFolder.v().transform(this.body);
            Aggregator.v().transform(this.body);
            UnusedLocalEliminator.v().transform(this.body);
        }
    }

    public void eliminatePhiNodes() {
        if (this.phi.doEliminatePhiNodes()) {
            makeUniqueLocalNames();
        }
    }

    public void eliminatePiNodes() {
        this.pi.eliminatePiNodes(this.options.node_elim_opt());
    }

    public void renameLocals() {
        update();
        this.newLocals = new HashMap();
        this.newLocalsToOldLocal = new HashMap();
        int size = this.origLocals.size();
        this.assignmentCounters = new int[size];
        Stack[] temp = new Stack[size];
        for (int i = 0; i < size; i++) {
            temp[i] = new Stack();
        }
        this.namingStacks = temp;
        List<Block> heads = this.sf.getBlockGraph().getHeads();
        switch (heads.size()) {
            case 0:
                return;
            case 1:
                renameLocalsSearch(heads.get(0));
                return;
            default:
                throw new RuntimeException("Assertion failed: Only one head expected.");
        }
    }

    public void renameLocalsSearch(Block block) {
        List<Local> lhsLocals = new ArrayList<>();
        Iterator<Unit> it = block.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (!Shimple.isPhiNode(unit)) {
                for (ValueBox useBox : unit.getUseBoxes()) {
                    Value use = useBox.getValue();
                    int localIndex = indexOfLocal(use);
                    if (localIndex != -1) {
                        Stack<Integer> namingStack = this.namingStacks[localIndex];
                        if (!namingStack.empty()) {
                            Local renamedLocal = fetchNewLocal((Local) use, namingStack.peek());
                            useBox.setValue(renamedLocal);
                        }
                    }
                }
            }
            if (unit instanceof DefinitionStmt) {
                DefinitionStmt defStmt = (DefinitionStmt) unit;
                ValueBox lhsLocalBox = defStmt.getLeftOpBox();
                Value lhsValue = lhsLocalBox.getValue();
                if (this.origLocals.contains(lhsValue)) {
                    Local lhsLocal = (Local) lhsValue;
                    lhsLocals.add(lhsLocal);
                    int localIndex2 = indexOfLocal(lhsLocal);
                    if (localIndex2 == -1) {
                        throw new RuntimeException("Assertion failed.");
                    }
                    Integer subscript = Integer.valueOf(this.assignmentCounters[localIndex2]);
                    Local newLhsLocal = fetchNewLocal(lhsLocal, subscript);
                    lhsLocalBox.setValue(newLhsLocal);
                    this.namingStacks[localIndex2].push(subscript);
                    int[] iArr = this.assignmentCounters;
                    iArr[localIndex2] = iArr[localIndex2] + 1;
                } else {
                    continue;
                }
            }
        }
        for (Block succ : this.sf.getBlockGraph().getSuccsOf(block)) {
            if (block.getHead() != null || block.getTail() != null) {
                Iterator<Unit> it2 = succ.iterator();
                while (it2.hasNext()) {
                    PhiExpr phiExpr = Shimple.getPhiExpr(it2.next());
                    if (phiExpr != null) {
                        ValueBox phiArgBox = phiExpr.getArgBox(block);
                        if (phiArgBox == null) {
                            throw new RuntimeException("Assertion failed. Cannot find " + block + " in " + phiExpr);
                        }
                        Local phiArg = (Local) phiArgBox.getValue();
                        int localIndex3 = indexOfLocal(phiArg);
                        if (localIndex3 == -1) {
                            throw new RuntimeException("Assertion failed.");
                        }
                        Stack<Integer> namingStack2 = this.namingStacks[localIndex3];
                        if (!namingStack2.empty()) {
                            Local newPhiArg = fetchNewLocal(phiArg, namingStack2.peek());
                            phiArgBox.setValue(newPhiArg);
                        }
                    }
                }
                continue;
            }
        }
        DominatorTree<Block> dt = this.sf.getDominatorTree();
        for (DominatorNode<Block> childNode : dt.getChildrenOf(dt.getDode(block))) {
            renameLocalsSearch(childNode.getGode());
        }
        for (Local lhsLocal2 : lhsLocals) {
            int lhsLocalIndex = indexOfLocal(lhsLocal2);
            if (lhsLocalIndex == -1) {
                throw new RuntimeException("Assertion failed.");
            }
            this.namingStacks[lhsLocalIndex].pop();
        }
    }

    protected Local fetchNewLocal(Local local, Integer subscript) {
        Local oldLocal = this.origLocals.contains(local) ? local : this.newLocalsToOldLocal.get(local);
        if (subscript.intValue() == 0) {
            return oldLocal;
        }
        String name = String.valueOf(oldLocal.getName()) + freshSeparator + subscript;
        Local newLocal = this.newLocals.get(name);
        if (newLocal == null) {
            newLocal = new JimpleLocal(name, oldLocal.getType());
            this.newLocals.put(name, newLocal);
            this.newLocalsToOldLocal.put(newLocal, oldLocal);
            this.body.getLocals().add(newLocal);
        }
        return newLocal;
    }

    protected int indexOfLocal(Value local) {
        int localIndex = this.origLocals.indexOf(local);
        if (localIndex == -1) {
            Local oldLocal = this.newLocalsToOldLocal.get(local);
            localIndex = this.origLocals.indexOf(oldLocal);
        }
        return localIndex;
    }

    public void makeUniqueLocalNames() {
        if (this.options.standard_local_names()) {
            LocalNameStandardizer.v().transform(this.body);
            return;
        }
        Set<String> localNames = new HashSet<>();
        for (Local local : this.body.getLocals()) {
            String localName = local.getName();
            if (localNames.contains(localName)) {
                String uniqueName = makeUniqueLocalName(localName, localNames);
                local.setName(uniqueName);
                localNames.add(uniqueName);
            } else {
                localNames.add(localName);
            }
        }
    }

    public static String makeUniqueLocalName(String dupName, Set<String> localNames) {
        int counter = 1;
        String str = dupName;
        while (true) {
            String newName = str;
            if (localNames.contains(newName)) {
                int i = counter;
                counter++;
                str = String.valueOf(dupName) + freshSeparator + i;
            } else {
                return newName;
            }
        }
    }

    public static void setSeparator(String sep) {
        freshSeparator = sep;
    }
}
