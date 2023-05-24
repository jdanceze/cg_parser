package soot.dava.internal.SET;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.AbruptEdgeFinder;
import soot.dava.toolkits.base.finders.LabeledBlockFinder;
import soot.dava.toolkits.base.finders.SequenceFinder;
import soot.util.IterableSet;
import soot.util.UnmodifiableIterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETNode.class */
public abstract class SETNode {
    private static final Logger logger = LoggerFactory.getLogger(SETNode.class);
    private IterableSet<AugmentedStmt> body;
    protected AugmentedStmt entryStmt;
    protected SETNode parent = null;
    private final SETNodeLabel label = new SETNodeLabel();
    protected LinkedList<IterableSet> subBodies = new LinkedList<>();
    protected Map<IterableSet, IterableSet> body2childChain = new HashMap();
    protected IterableSet predecessors = new IterableSet();
    protected IterableSet successors = new IterableSet();

    public abstract IterableSet get_NaturalExits();

    public abstract ASTNode emit_AST();

    public abstract AugmentedStmt get_EntryStmt();

    protected abstract boolean resolve(SETNode sETNode);

    public SETNode(IterableSet<AugmentedStmt> body) {
        this.body = body;
    }

    public void add_SubBody(IterableSet body) {
        this.subBodies.add(body);
        this.body2childChain.put(body, new IterableSet());
    }

    public Map<IterableSet, IterableSet> get_Body2ChildChain() {
        return this.body2childChain;
    }

    public List<IterableSet> get_SubBodies() {
        return this.subBodies;
    }

    public IterableSet<AugmentedStmt> get_Body() {
        return this.body;
    }

    public SETNodeLabel get_Label() {
        return this.label;
    }

    public SETNode get_Parent() {
        return this.parent;
    }

    public boolean contains(Object o) {
        return this.body.contains(o);
    }

    public IterableSet get_Successors() {
        return this.successors;
    }

    public IterableSet get_Predecessors() {
        return this.predecessors;
    }

    public boolean add_Child(SETNode child, IterableSet children) {
        if (this == child || children.contains(child)) {
            return false;
        }
        children.add(child);
        child.parent = this;
        return true;
    }

    public boolean remove_Child(SETNode child, IterableSet children) {
        if (this == child || !children.contains(child)) {
            return false;
        }
        children.remove(child);
        child.parent = null;
        return true;
    }

    public boolean insert_ChildBefore(SETNode child, SETNode point, IterableSet children) {
        if (this == child || this == point || !children.contains(point)) {
            return false;
        }
        children.insertBefore(child, point);
        child.parent = this;
        return true;
    }

    public List<Object> emit_ASTBody(IterableSet children) {
        LinkedList<Object> l = new LinkedList<>();
        Iterator cit = children.iterator();
        while (cit.hasNext()) {
            ASTNode astNode = ((SETNode) cit.next()).emit_AST();
            if (astNode != null) {
                l.addLast(astNode);
            }
        }
        return l;
    }

    public IterableSet<AugmentedStmt> get_IntersectionWith(SETNode other) {
        return this.body.intersection(other.get_Body());
    }

    public boolean has_IntersectionWith(SETNode other) {
        Iterator<AugmentedStmt> it = other.get_Body().iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            if (this.body.contains(as)) {
                return true;
            }
        }
        return false;
    }

    public boolean is_SupersetOf(SETNode other) {
        return this.body.isSupersetOf(other.get_Body());
    }

    public boolean is_StrictSupersetOf(SETNode other) {
        return this.body.isStrictSubsetOf(other.get_Body());
    }

    public void find_SmallestSETNode(AugmentedStmt as) {
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            Iterator it = this.body2childChain.get(sbit.next()).iterator();
            while (it.hasNext()) {
                SETNode child = (SETNode) it.next();
                if (child.contains(as)) {
                    child.find_SmallestSETNode(as);
                    return;
                }
            }
        }
        as.myNode = this;
    }

    public void find_LabeledBlocks(LabeledBlockFinder lbf) {
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            Iterator cit = this.body2childChain.get(sbit.next()).iterator();
            while (cit.hasNext()) {
                ((SETNode) cit.next()).find_LabeledBlocks(lbf);
            }
        }
        lbf.perform_ChildOrder(this);
        lbf.find_LabeledBlocks(this);
    }

    public void find_StatementSequences(SequenceFinder sf, DavaBody davaBody) {
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            IterableSet body = sbit.next();
            IterableSet children = this.body2childChain.get(body);
            HashSet<AugmentedStmt> childUnion = new HashSet<>();
            Iterator cit = children.iterator();
            while (cit.hasNext()) {
                SETNode child = (SETNode) cit.next();
                child.find_StatementSequences(sf, davaBody);
                childUnion.addAll(child.get_Body());
            }
            sf.find_StatementSequences(this, body, childUnion, davaBody);
        }
    }

    public void find_AbruptEdges(AbruptEdgeFinder aef) {
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            IterableSet body = sbit.next();
            IterableSet children = this.body2childChain.get(body);
            Iterator cit = children.iterator();
            while (cit.hasNext()) {
                ((SETNode) cit.next()).find_AbruptEdges(aef);
            }
            aef.find_Continues(this, body, children);
        }
        Iterator<IterableSet> sbit2 = this.subBodies.iterator();
        while (sbit2.hasNext()) {
            Iterator cit2 = this.body2childChain.get(sbit2.next()).iterator();
            if (cit2.hasNext()) {
                SETNode cur = (SETNode) cit2.next();
                while (cit2.hasNext()) {
                    SETNode prev = cur;
                    cur = (SETNode) cit2.next();
                    aef.find_Breaks(prev, cur);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void remove_AugmentedStmt(AugmentedStmt as) {
        IterableSet childChain = this.body2childChain.remove(this.body);
        if (this.body instanceof UnmodifiableIterableSet) {
            ((UnmodifiableIterableSet) this.body).forceRemove(as);
        } else {
            this.body.remove(as);
        }
        if (childChain != null) {
            this.body2childChain.put(this.body, childChain);
        }
        Iterator<IterableSet> it = this.subBodies.iterator();
        while (it.hasNext()) {
            IterableSet subBody = it.next();
            if (subBody.contains(as)) {
                IterableSet childChain2 = this.body2childChain.remove(subBody);
                if (subBody instanceof UnmodifiableIterableSet) {
                    ((UnmodifiableIterableSet) subBody).forceRemove(as);
                } else {
                    subBody.remove(as);
                }
                if (childChain2 != null) {
                    this.body2childChain.put(subBody, childChain2);
                    return;
                }
                return;
            }
        }
    }

    public boolean nest(SETNode other) {
        if (!other.resolve(this)) {
            return false;
        }
        IterableSet otherBody = other.get_Body();
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            IterableSet subBody = sbit.next();
            if (subBody.intersects(otherBody)) {
                IterableSet childChain = this.body2childChain.get(subBody);
                Iterator ccit = childChain.snapshotIterator();
                while (ccit.hasNext()) {
                    SETNode curChild = (SETNode) ccit.next();
                    IterableSet childBody = curChild.get_Body();
                    if (childBody.intersects(otherBody)) {
                        if (childBody.isSupersetOf(otherBody)) {
                            return curChild.nest(other);
                        }
                        remove_Child(curChild, childChain);
                        Iterator<IterableSet> osbit = other.subBodies.iterator();
                        while (true) {
                            if (!osbit.hasNext()) {
                                break;
                            }
                            IterableSet otherSubBody = osbit.next();
                            if (otherSubBody.isSupersetOf(childBody)) {
                                other.add_Child(curChild, other.get_Body2ChildChain().get(otherSubBody));
                                break;
                            }
                        }
                    }
                }
                add_Child(other, childChain);
            }
        }
        return true;
    }

    public void dump() {
        dump(G.v().out);
    }

    public void dump(PrintStream out) {
        dump(out, "");
    }

    private void dump(PrintStream out, String indentation) {
        out.println(indentation);
        out.println(String.valueOf(indentation) + ".---");
        out.println(String.valueOf(indentation) + "|  " + getClass());
        out.println(String.valueOf(indentation) + "|  ");
        Iterator it = this.body.iterator();
        while (it.hasNext()) {
            out.println(String.valueOf(indentation) + "|  " + it.next().toString());
        }
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            IterableSet subBody = sbit.next();
            out.println(String.valueOf(indentation) + "+---");
            Iterator bit = subBody.iterator();
            while (bit.hasNext()) {
                out.println(String.valueOf(indentation) + "|  " + ((AugmentedStmt) bit.next()).toString());
            }
            out.println(String.valueOf(indentation) + "|  ");
            Iterator cit = this.body2childChain.get(subBody).iterator();
            while (cit.hasNext()) {
                ((SETNode) cit.next()).dump(out, String.valueOf("|  ") + indentation);
            }
        }
        out.println(String.valueOf(indentation) + "`---");
    }

    public void verify() {
        Iterator<IterableSet> sbit = this.subBodies.iterator();
        while (sbit.hasNext()) {
            IterableSet body = sbit.next();
            Iterator bit = body.iterator();
            while (bit.hasNext()) {
                if (!(bit.next() instanceof AugmentedStmt)) {
                    logger.debug("Error in body: " + getClass());
                }
            }
            Iterator cit = this.body2childChain.get(body).iterator();
            while (cit.hasNext()) {
                ((SETNode) cit.next()).verify();
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof SETNode)) {
            return false;
        }
        SETNode typed_other = (SETNode) other;
        if (!this.body.equals(typed_other.body) || !this.subBodies.equals(typed_other.subBodies) || !this.body2childChain.equals(typed_other.body2childChain)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return 1;
    }
}
