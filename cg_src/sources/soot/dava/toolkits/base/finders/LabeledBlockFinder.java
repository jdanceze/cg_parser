package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETBasicBlock;
import soot.dava.internal.SET.SETLabeledBlockNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETStatementSequenceNode;
import soot.dava.internal.SET.SETTryNode;
import soot.dava.internal.SET.SETUnconditionalWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/LabeledBlockFinder.class */
public class LabeledBlockFinder implements FactFinder {
    private final HashMap<SETNode, Integer> orderNumber = new HashMap<>();

    public LabeledBlockFinder(Singletons.Global g) {
    }

    public static LabeledBlockFinder v() {
        return G.v().soot_dava_toolkits_base_finders_LabeledBlockFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        Dava.v().log("LabeledBlockFinder::find()");
        Iterator bit = SET.get_Body().iterator();
        while (bit.hasNext()) {
            SET.find_SmallestSETNode(bit.next());
        }
        SET.find_LabeledBlocks(this);
    }

    public void perform_ChildOrder(SETNode SETParent) {
        SETNode startSETNode;
        Dava.v().log("LabeledBlockFinder::perform_ChildOrder()");
        if (SETParent instanceof SETStatementSequenceNode) {
            return;
        }
        for (IterableSet body : SETParent.get_SubBodies()) {
            IterableSet children = SETParent.get_Body2ChildChain().get(body);
            HashSet<SETBasicBlock> touchSet = new HashSet<>();
            IterableSet childOrdering = new IterableSet();
            LinkedList worklist = new LinkedList();
            if (SETParent instanceof SETUnconditionalWhileNode) {
                SETNode sETNode = ((SETUnconditionalWhileNode) SETParent).get_CharacterizingStmt().myNode;
                while (true) {
                    startSETNode = sETNode;
                    if (children.contains(startSETNode)) {
                        break;
                    }
                    sETNode = startSETNode.get_Parent();
                }
                build_Connectivity(SETParent, body, startSETNode);
                worklist.add(SETBasicBlock.get_SETBasicBlock(startSETNode));
            } else if (SETParent instanceof SETTryNode) {
                SETNode startSETNode2 = null;
                Iterator bit = body.iterator();
                while (true) {
                    if (!bit.hasNext()) {
                        break;
                    }
                    AugmentedStmt as = (AugmentedStmt) bit.next();
                    for (AugmentedStmt augmentedStmt : as.cpreds) {
                        if (!body.contains(augmentedStmt)) {
                            startSETNode2 = as.myNode;
                            break;
                        }
                    }
                }
                if (startSETNode2 == null) {
                    startSETNode2 = ((SETTryNode) SETParent).get_EntryStmt().myNode;
                }
                while (!children.contains(startSETNode2)) {
                    startSETNode2 = startSETNode2.get_Parent();
                }
                build_Connectivity(SETParent, body, startSETNode2);
                worklist.add(SETBasicBlock.get_SETBasicBlock(startSETNode2));
            } else {
                build_Connectivity(SETParent, body, null);
                Iterator cit = children.iterator();
                while (cit.hasNext()) {
                    SETNode child = (SETNode) cit.next();
                    if (child.get_Predecessors().isEmpty()) {
                        worklist.add(SETBasicBlock.get_SETBasicBlock(child));
                    }
                }
            }
            while (!worklist.isEmpty()) {
                SETBasicBlock sbb = (SETBasicBlock) worklist.removeFirst();
                Iterator bit2 = sbb.get_Body().iterator();
                while (bit2.hasNext()) {
                    childOrdering.addLast(bit2.next());
                }
                touchSet.add(sbb);
                TreeSet sortedSuccessors = new TreeSet();
                Iterator sit = sbb.get_Successors().iterator();
                while (sit.hasNext()) {
                    SETBasicBlock ssbb = (SETBasicBlock) sit.next();
                    if (!touchSet.contains(ssbb)) {
                        Iterator psit = ssbb.get_Predecessors().iterator();
                        while (true) {
                            if (psit.hasNext()) {
                                if (!touchSet.contains(psit.next())) {
                                    break;
                                }
                            } else {
                                sortedSuccessors.add(ssbb);
                                break;
                            }
                        }
                    }
                }
                Iterator sit2 = sortedSuccessors.iterator();
                while (sit2.hasNext()) {
                    worklist.addFirst(sit2.next());
                }
            }
            int count = 0;
            Iterator it = childOrdering.iterator();
            while (it.hasNext()) {
                int i = count;
                count++;
                this.orderNumber.put((SETNode) it.next(), new Integer(i));
            }
            children.clear();
            children.addAll(childOrdering);
        }
    }

    private List<SETBasicBlock> build_Connectivity(SETNode SETParent, IterableSet body, SETNode startSETNode) {
        Dava.v().log("LabeledBlockFinder::build_Connectivity()");
        IterableSet children = SETParent.get_Body2ChildChain().get(body);
        Iterator it = body.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = (AugmentedStmt) it.next();
            for (AugmentedStmt sas : as.csuccs) {
                if (body.contains(sas)) {
                    SETNode srcNode = as.myNode;
                    SETNode dstNode = sas.myNode;
                    while (!children.contains(srcNode)) {
                        srcNode = srcNode.get_Parent();
                    }
                    while (!children.contains(dstNode)) {
                        dstNode = dstNode.get_Parent();
                    }
                    if (srcNode != dstNode) {
                        if (!srcNode.get_Successors().contains(dstNode)) {
                            srcNode.get_Successors().add(dstNode);
                        }
                        if (!dstNode.get_Predecessors().contains(srcNode)) {
                            dstNode.get_Predecessors().add(srcNode);
                        }
                    }
                }
            }
        }
        Dava.v().log("LabeledBlockFinder::build_Connectivity() - built connectivity");
        LinkedList<SETBasicBlock> basicBlockList = new LinkedList<>();
        Iterator cit = children.iterator();
        while (cit.hasNext()) {
            SETNode child = (SETNode) cit.next();
            if (SETBasicBlock.get_SETBasicBlock(child) == null) {
                SETBasicBlock basicBlock = new SETBasicBlock();
                while (child.get_Predecessors().size() == 1 && (startSETNode == null || child != startSETNode)) {
                    SETNode prev = (SETNode) child.get_Predecessors().getFirst();
                    if (SETBasicBlock.get_SETBasicBlock(prev) != null || prev.get_Successors().size() != 1) {
                        break;
                    }
                    child = prev;
                }
                basicBlock.add(child);
                while (child.get_Successors().size() == 1) {
                    child = (SETNode) child.get_Successors().getFirst();
                    if (SETBasicBlock.get_SETBasicBlock(child) != null || child.get_Predecessors().size() != 1) {
                        break;
                    }
                    basicBlock.add(child);
                }
                basicBlockList.add(basicBlock);
            }
        }
        Dava.v().log("LabeledBlockFinder::build_Connectivity() - created basic blocks");
        Iterator<SETBasicBlock> bblit = basicBlockList.iterator();
        while (bblit.hasNext()) {
            SETBasicBlock sbb = bblit.next();
            SETNode entryNode = sbb.get_EntryNode();
            Iterator pit = entryNode.get_Predecessors().iterator();
            while (pit.hasNext()) {
                SETNode psn = (SETNode) pit.next();
                SETBasicBlock psbb = SETBasicBlock.get_SETBasicBlock(psn);
                if (!sbb.get_Predecessors().contains(psbb)) {
                    sbb.get_Predecessors().add(psbb);
                }
                if (!psbb.get_Successors().contains(sbb)) {
                    psbb.get_Successors().add(sbb);
                }
            }
        }
        Dava.v().log("LabeledBlockFinder::build_Connectivity() - done");
        return basicBlockList;
    }

    public void find_LabeledBlocks(SETNode SETParent) {
        SETNode child;
        SETNode child2;
        SETNode srcNode;
        Dava.v().log("LabeledBlockFinder::find_LabeledBlocks()");
        for (IterableSet curBody : SETParent.get_SubBodies()) {
            IterableSet children = SETParent.get_Body2ChildChain().get(curBody);
            Iterator it = children.snapshotIterator();
            if (it.hasNext()) {
                SETNode curNode = (SETNode) it.next();
                while (it.hasNext()) {
                    SETNode prevNode = curNode;
                    curNode = (SETNode) it.next();
                    AugmentedStmt entryStmt = curNode.get_EntryStmt();
                    SETNode minNode = null;
                    boolean build = false;
                    for (AugmentedStmt pas : entryStmt.cpreds) {
                        if (curBody.contains(pas)) {
                            SETNode sETNode = pas.myNode;
                            while (true) {
                                srcNode = sETNode;
                                if (children.contains(srcNode)) {
                                    break;
                                }
                                sETNode = srcNode.get_Parent();
                            }
                            if (srcNode != curNode && srcNode != prevNode) {
                                build = true;
                                if (minNode == null || this.orderNumber.get(srcNode).intValue() < this.orderNumber.get(minNode).intValue()) {
                                    minNode = srcNode;
                                }
                            }
                        }
                    }
                    if (build) {
                        IterableSet labeledBlockBody = new IterableSet();
                        Iterator cit = children.iterator(minNode);
                        while (cit.hasNext() && (child2 = (SETNode) cit.next()) != curNode) {
                            labeledBlockBody.addAll(child2.get_Body());
                        }
                        SETLabeledBlockNode slbn = new SETLabeledBlockNode(labeledBlockBody);
                        this.orderNumber.put(slbn, this.orderNumber.get(minNode));
                        Iterator cit2 = children.snapshotIterator(minNode);
                        while (cit2.hasNext() && (child = (SETNode) cit2.next()) != curNode) {
                            SETParent.remove_Child(child, children);
                            slbn.add_Child(child, slbn.get_Body2ChildChain().get(slbn.get_SubBodies().get(0)));
                        }
                        SETParent.insert_ChildBefore(slbn, curNode, children);
                    }
                }
            }
        }
    }
}
