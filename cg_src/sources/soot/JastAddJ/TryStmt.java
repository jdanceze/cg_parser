package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.tagkit.Tag;
import soot.tagkit.ThrowCreatedByCompilerTag;
import soot.tagkit.TryCatchTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TryStmt.class */
public class TryStmt extends Stmt implements Cloneable, FinallyHost {
    protected Collection branches_value;
    protected Collection branchesFromFinally_value;
    protected Collection targetBranches_value;
    protected Collection escapedBranches_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUbefore_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map catchableException_TypeDecl_values;
    protected boolean canCompleteNormally_value;
    protected soot.jimple.Stmt label_begin_value;
    protected soot.jimple.Stmt label_block_end_value;
    protected soot.jimple.Stmt label_end_value;
    protected soot.jimple.Stmt label_finally_value;
    protected soot.jimple.Stmt label_finally_block_value;
    protected soot.jimple.Stmt label_exception_handler_value;
    protected soot.jimple.Stmt label_catch_end_value;
    protected ArrayList exceptionRanges_value;
    protected Map handlesException_TypeDecl_values;
    protected TypeDecl typeError_value;
    protected TypeDecl typeRuntimeException_value;
    protected boolean branches_computed = false;
    protected boolean branchesFromFinally_computed = false;
    protected boolean targetBranches_computed = false;
    protected boolean escapedBranches_computed = false;
    protected boolean canCompleteNormally_computed = false;
    protected boolean label_begin_computed = false;
    protected boolean label_block_end_computed = false;
    protected boolean label_end_computed = false;
    protected boolean label_finally_computed = false;
    protected boolean label_finally_block_computed = false;
    protected boolean label_exception_handler_computed = false;
    protected boolean label_catch_end_computed = false;
    protected boolean exceptionRanges_computed = false;
    protected boolean typeError_computed = false;
    protected boolean typeRuntimeException_computed = false;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.branches_computed = false;
        this.branches_value = null;
        this.branchesFromFinally_computed = false;
        this.branchesFromFinally_value = null;
        this.targetBranches_computed = false;
        this.targetBranches_value = null;
        this.escapedBranches_computed = false;
        this.escapedBranches_value = null;
        this.isDAafter_Variable_values = null;
        this.isDUbefore_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.catchableException_TypeDecl_values = null;
        this.canCompleteNormally_computed = false;
        this.label_begin_computed = false;
        this.label_begin_value = null;
        this.label_block_end_computed = false;
        this.label_block_end_value = null;
        this.label_end_computed = false;
        this.label_end_value = null;
        this.label_finally_computed = false;
        this.label_finally_value = null;
        this.label_finally_block_computed = false;
        this.label_finally_block_value = null;
        this.label_exception_handler_computed = false;
        this.label_exception_handler_value = null;
        this.label_catch_end_computed = false;
        this.label_catch_end_value = null;
        this.exceptionRanges_computed = false;
        this.exceptionRanges_value = null;
        this.handlesException_TypeDecl_values = null;
        this.typeError_computed = false;
        this.typeError_value = null;
        this.typeRuntimeException_computed = false;
        this.typeRuntimeException_value = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public TryStmt clone() throws CloneNotSupportedException {
        TryStmt node = (TryStmt) super.clone();
        node.branches_computed = false;
        node.branches_value = null;
        node.branchesFromFinally_computed = false;
        node.branchesFromFinally_value = null;
        node.targetBranches_computed = false;
        node.targetBranches_value = null;
        node.escapedBranches_computed = false;
        node.escapedBranches_value = null;
        node.isDAafter_Variable_values = null;
        node.isDUbefore_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.catchableException_TypeDecl_values = null;
        node.canCompleteNormally_computed = false;
        node.label_begin_computed = false;
        node.label_begin_value = null;
        node.label_block_end_computed = false;
        node.label_block_end_value = null;
        node.label_end_computed = false;
        node.label_end_value = null;
        node.label_finally_computed = false;
        node.label_finally_value = null;
        node.label_finally_block_computed = false;
        node.label_finally_block_value = null;
        node.label_exception_handler_computed = false;
        node.label_exception_handler_value = null;
        node.label_catch_end_computed = false;
        node.label_catch_end_value = null;
        node.exceptionRanges_computed = false;
        node.exceptionRanges_value = null;
        node.handlesException_TypeDecl_values = null;
        node.typeError_computed = false;
        node.typeError_value = null;
        node.typeRuntimeException_computed = false;
        node.typeRuntimeException_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            TryStmt node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    @Override // soot.JastAddJ.ASTNode, soot.JastAddJ.BranchPropagation
    public void collectBranches(Collection c) {
        c.addAll(escapedBranches());
    }

    @Override // soot.JastAddJ.ASTNode, soot.JastAddJ.BranchPropagation
    public Stmt branchTarget(Stmt branchStmt) {
        if (targetBranches().contains(branchStmt)) {
            return this;
        }
        return super.branchTarget(branchStmt);
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectFinally(Stmt branchStmt, ArrayList list) {
        if (hasFinally() && !branchesFromFinally().contains(branchStmt)) {
            list.add(this);
        }
        if (targetBranches().contains(branchStmt)) {
            return;
        }
        super.collectFinally(branchStmt, list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl type) {
        boolean found = false;
        for (int i = 0; i < getNumCatchClause() && !found; i++) {
            if (getCatchClause(i).handles(type)) {
                found = true;
            }
        }
        if (!found && ((!hasFinally() || getFinally().canCompleteNormally()) && getBlock().reachedException(type))) {
            return true;
        }
        for (int i2 = 0; i2 < getNumCatchClause(); i2++) {
            if (getCatchClause(i2).reachedException(type)) {
                return true;
            }
        }
        return hasFinally() && getFinally().reachedException(type);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("try ");
        getBlock().toString(s);
        for (int i = 0; i < getNumCatchClause(); i++) {
            s.append(indent());
            getCatchClause(i).toString(s);
        }
        if (hasFinally()) {
            s.append(indent());
            s.append("finally ");
            getFinally().toString(s);
        }
    }

    @Override // soot.JastAddJ.FinallyHost
    public void emitFinallyCode(Body b) {
        if (hasFinally()) {
            getFinally().flushCaches();
            getFinally().jimplify2(b);
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        soot.jimple.Stmt stmtEnd;
        ArrayList ranges = exceptionRanges();
        b.addLabel(label_begin());
        ranges.add(label_begin());
        getBlock().jimplify2(b);
        soot.jimple.Stmt label_block_end = null;
        soot.jimple.Stmt label_end = null;
        if (getBlock().canCompleteNormally()) {
            if (hasFinally() && getNumCatchClause() != 0) {
                label_block_end = label_block_end();
                b.addLabel(label_block_end);
            }
            emitFinallyCode(b);
            b.setLine(this);
            if ((!hasFinally() || getFinally().canCompleteNormally()) && (getNumCatchClause() != 0 || hasFinally())) {
                soot.jimple.Stmt label_end2 = label_end();
                label_end = label_end2;
                b.add(b.newGotoStmt(label_end2, this));
            }
        }
        if (getNumCatchClause() != 0) {
            if (label_block_end == null) {
                label_block_end = ((BasicCatch) getCatchClause(0)).label();
            }
            ranges.add(label_block_end);
            ranges.add(label_block_end);
            for (int i = 0; i < getNumCatchClause(); i++) {
                getCatchClause(i).jimplify2(b);
                if (getCatchClause(i).getBlock().canCompleteNormally()) {
                    b.setLine(getCatchClause(i));
                    endExceptionRange(b, ranges);
                    emitFinallyCode(b);
                    if (!hasFinally() || getFinally().canCompleteNormally()) {
                        soot.jimple.Stmt label_end3 = label_end();
                        label_end = label_end3;
                        b.add(b.newGotoStmt(label_end3, this));
                    }
                    beginExceptionRange(b, ranges);
                }
                b.setLine(getCatchClause(i));
            }
        }
        if (hasFinally()) {
            b.addLabel(label_exception_handler());
            emitExceptionHandler(b);
            b.setLine(getFinally());
        }
        if (label_end != null) {
            b.addLabel(label_end);
        }
        for (int i2 = 0; i2 < getNumCatchClause(); i2++) {
            Iterator iter = ranges.iterator();
            while (iter.hasNext()) {
                soot.jimple.Stmt stmtBegin = (soot.jimple.Stmt) iter.next();
                soot.jimple.Stmt stmtEnd2 = (soot.jimple.Stmt) iter.next();
                if (stmtBegin != stmtEnd2) {
                    soot.jimple.Stmt lbl = ((BasicCatch) getCatchClause(i2)).label();
                    b.addTrap(((BasicCatch) getCatchClause(i2)).getParameter().type(), stmtBegin, stmtEnd2, lbl);
                    addFallThroughLabelTag(b, lbl, label_end);
                }
                if (stmtEnd2 == label_block_end) {
                    break;
                }
            }
        }
        if (hasFinally()) {
            Iterator iter2 = ranges.iterator();
            while (iter2.hasNext()) {
                soot.jimple.Stmt stmtBegin2 = (soot.jimple.Stmt) iter2.next();
                if (iter2.hasNext()) {
                    stmtEnd = (soot.jimple.Stmt) iter2.next();
                } else {
                    stmtEnd = label_exception_handler();
                }
                if (stmtBegin2 != stmtEnd) {
                    soot.jimple.Stmt lbl2 = label_exception_handler();
                    b.addTrap(typeThrowable(), stmtBegin2, stmtEnd, lbl2);
                    addFallThroughLabelTag(b, lbl2, label_end);
                }
            }
        }
    }

    protected void addFallThroughLabelTag(Body b, soot.jimple.Stmt handler, soot.jimple.Stmt fallThrough) {
        soot.Body body = b.body;
        Tag tag = (TryCatchTag) body.getTag(TryCatchTag.NAME);
        if (tag == null) {
            tag = new TryCatchTag();
            body.addTag(tag);
        }
        tag.register(handler, fallThrough);
    }

    public void emitExceptionHandler(Body b) {
        Local l = b.newTemp(typeThrowable().getSootType());
        b.setLine(this);
        b.add(b.newIdentityStmt(l, b.newCaughtExceptionRef(this), this));
        emitFinallyCode(b);
        soot.jimple.Stmt throwStmt = b.newThrowStmt(l, this);
        throwStmt.addTag(new ThrowCreatedByCompilerTag());
        b.add(throwStmt);
    }

    public TryStmt() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new Opt(), 2);
    }

    public TryStmt(Block p0, List<CatchClause> p1, Opt<Block> p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setBlock(Block node) {
        setChild(node, 0);
    }

    public Block getBlock() {
        return (Block) getChild(0);
    }

    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(0);
    }

    public void setCatchClauseList(List<CatchClause> list) {
        setChild(list, 1);
    }

    public int getNumCatchClause() {
        return getCatchClauseList().getNumChild();
    }

    public int getNumCatchClauseNoTransform() {
        return getCatchClauseListNoTransform().getNumChildNoTransform();
    }

    public CatchClause getCatchClause(int i) {
        return getCatchClauseList().getChild(i);
    }

    public void addCatchClause(CatchClause node) {
        List<CatchClause> list = (this.parent == null || state == null) ? getCatchClauseListNoTransform() : getCatchClauseList();
        list.addChild(node);
    }

    public void addCatchClauseNoTransform(CatchClause node) {
        List<CatchClause> list = getCatchClauseListNoTransform();
        list.addChild(node);
    }

    public void setCatchClause(CatchClause node, int i) {
        List<CatchClause> list = getCatchClauseList();
        list.setChild(node, i);
    }

    public List<CatchClause> getCatchClauses() {
        return getCatchClauseList();
    }

    public List<CatchClause> getCatchClausesNoTransform() {
        return getCatchClauseListNoTransform();
    }

    public List<CatchClause> getCatchClauseList() {
        List<CatchClause> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<CatchClause> getCatchClauseListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setFinallyOpt(Opt<Block> opt) {
        setChild(opt, 2);
    }

    public boolean hasFinally() {
        return getFinallyOpt().getNumChild() != 0;
    }

    public Block getFinally() {
        return getFinallyOpt().getChild(0);
    }

    public void setFinally(Block node) {
        getFinallyOpt().setChild(node, 0);
    }

    public Opt<Block> getFinallyOpt() {
        return (Opt) getChild(2);
    }

    public Opt<Block> getFinallyOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    public Collection branches() {
        if (this.branches_computed) {
            return this.branches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.branches_value = branches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.branches_computed = true;
        }
        return this.branches_value;
    }

    private Collection branches_compute() {
        HashSet set = new HashSet();
        getBlock().collectBranches(set);
        for (int i = 0; i < getNumCatchClause(); i++) {
            getCatchClause(i).collectBranches(set);
        }
        return set;
    }

    public Collection branchesFromFinally() {
        if (this.branchesFromFinally_computed) {
            return this.branchesFromFinally_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.branchesFromFinally_value = branchesFromFinally_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.branchesFromFinally_computed = true;
        }
        return this.branchesFromFinally_value;
    }

    private Collection branchesFromFinally_compute() {
        HashSet set = new HashSet();
        if (hasFinally()) {
            getFinally().collectBranches(set);
        }
        return set;
    }

    public Collection targetBranches() {
        if (this.targetBranches_computed) {
            return this.targetBranches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.targetBranches_value = targetBranches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.targetBranches_computed = true;
        }
        return this.targetBranches_value;
    }

    private Collection targetBranches_compute() {
        HashSet set = new HashSet();
        if (hasFinally() && !getFinally().canCompleteNormally()) {
            set.addAll(branches());
        }
        return set;
    }

    public Collection escapedBranches() {
        if (this.escapedBranches_computed) {
            return this.escapedBranches_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.escapedBranches_value = escapedBranches_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.escapedBranches_computed = true;
        }
        return this.escapedBranches_value;
    }

    private Collection escapedBranches_compute() {
        HashSet set = new HashSet();
        if (hasFinally()) {
            set.addAll(branchesFromFinally());
        }
        if (!hasFinally() || getFinally().canCompleteNormally()) {
            set.addAll(branches());
        }
        return set;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDAafter(Variable v) {
        if (this.isDAafter_Variable_values == null) {
            this.isDAafter_Variable_values = new HashMap(4);
        }
        if (this.isDAafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafter_Variable_value = isDAafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, Boolean.valueOf(isDAafter_Variable_value));
        }
        return isDAafter_Variable_value;
    }

    private boolean isDAafter_compute(Variable v) {
        if (!hasFinally()) {
            if (!getBlock().isDAafter(v)) {
                return false;
            }
            for (int i = 0; i < getNumCatchClause(); i++) {
                if (!getCatchClause(i).getBlock().isDAafter(v)) {
                    return false;
                }
            }
            return true;
        } else if (getFinally().isDAafter(v)) {
            return true;
        } else {
            if (!getBlock().isDAafter(v)) {
                return false;
            }
            for (int i2 = 0; i2 < getNumCatchClause(); i2++) {
                if (!getCatchClause(i2).getBlock().isDAafter(v)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override // soot.JastAddJ.FinallyHost
    public boolean isDUafterFinally(Variable v) {
        state();
        return getFinally().isDUafter(v);
    }

    @Override // soot.JastAddJ.FinallyHost
    public boolean isDAafterFinally(Variable v) {
        state();
        return getFinally().isDAafter(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDUbefore(Variable v) {
        ASTNode.State.CircularValue _value;
        boolean new_isDUbefore_Variable_value;
        if (this.isDUbefore_Variable_values == null) {
            this.isDUbefore_Variable_values = new HashMap(4);
        }
        if (this.isDUbefore_Variable_values.containsKey(v)) {
            Object _o = this.isDUbefore_Variable_values.get(v);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.isDUbefore_Variable_values.put(v, _value);
            _value.value = true;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_isDUbefore_Variable_value = isDUbefore_compute(v);
                if (new_isDUbefore_Variable_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_isDUbefore_Variable_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.isDUbefore_Variable_values.remove(v);
                state.RESET_CYCLE = true;
                isDUbefore_compute(v);
                state.RESET_CYCLE = false;
            } else {
                this.isDUbefore_Variable_values.put(v, Boolean.valueOf(new_isDUbefore_Variable_value));
            }
            state.IN_CIRCLE = false;
            return new_isDUbefore_Variable_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_isDUbefore_Variable_value2 = isDUbefore_compute(v);
            if (state.RESET_CYCLE) {
                this.isDUbefore_Variable_values.remove(v);
            } else if (new_isDUbefore_Variable_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_isDUbefore_Variable_value2);
            }
            return new_isDUbefore_Variable_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean isDUbefore_compute(Variable v) {
        return super.isDUbefore(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDUafter(Variable v) {
        if (this.isDUafter_Variable_values == null) {
            this.isDUafter_Variable_values = new HashMap(4);
        }
        if (this.isDUafter_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUafter_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUafter_Variable_value = isDUafter_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, Boolean.valueOf(isDUafter_Variable_value));
        }
        return isDUafter_Variable_value;
    }

    private boolean isDUafter_compute(Variable v) {
        if (!hasFinally()) {
            if (!getBlock().isDUafter(v)) {
                return false;
            }
            for (int i = 0; i < getNumCatchClause(); i++) {
                if (!getCatchClause(i).getBlock().isDUafter(v)) {
                    return false;
                }
            }
            return true;
        }
        return getFinally().isDUafter(v);
    }

    public boolean catchableException(TypeDecl type) {
        if (this.catchableException_TypeDecl_values == null) {
            this.catchableException_TypeDecl_values = new HashMap(4);
        }
        if (this.catchableException_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.catchableException_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean catchableException_TypeDecl_value = catchableException_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.catchableException_TypeDecl_values.put(type, Boolean.valueOf(catchableException_TypeDecl_value));
        }
        return catchableException_TypeDecl_value;
    }

    private boolean catchableException_compute(TypeDecl type) {
        return getBlock().reachedException(type);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean canCompleteNormally() {
        if (this.canCompleteNormally_computed) {
            return this.canCompleteNormally_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.canCompleteNormally_value = canCompleteNormally_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
        }
        return this.canCompleteNormally_value;
    }

    private boolean canCompleteNormally_compute() {
        boolean anyCatchClauseCompleteNormally = false;
        for (int i = 0; i < getNumCatchClause() && !anyCatchClauseCompleteNormally; i++) {
            anyCatchClauseCompleteNormally = getCatchClause(i).getBlock().canCompleteNormally();
        }
        if (getBlock().canCompleteNormally() || anyCatchClauseCompleteNormally) {
            return !hasFinally() || getFinally().canCompleteNormally();
        }
        return false;
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt break_label() {
        state();
        return label_finally();
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt continue_label() {
        state();
        return label_finally();
    }

    public soot.jimple.Stmt label_begin() {
        if (this.label_begin_computed) {
            return this.label_begin_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_begin_value = label_begin_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_begin_computed = true;
        }
        return this.label_begin_value;
    }

    private soot.jimple.Stmt label_begin_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt label_block_end() {
        if (this.label_block_end_computed) {
            return this.label_block_end_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_block_end_value = label_block_end_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_block_end_computed = true;
        }
        return this.label_block_end_value;
    }

    private soot.jimple.Stmt label_block_end_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt label_end() {
        if (this.label_end_computed) {
            return this.label_end_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_end_value = label_end_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_end_computed = true;
        }
        return this.label_end_value;
    }

    private soot.jimple.Stmt label_end_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt label_finally() {
        if (this.label_finally_computed) {
            return this.label_finally_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_finally_value = label_finally_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_finally_computed = true;
        }
        return this.label_finally_value;
    }

    private soot.jimple.Stmt label_finally_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.FinallyHost
    public soot.jimple.Stmt label_finally_block() {
        if (this.label_finally_block_computed) {
            return this.label_finally_block_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_finally_block_value = label_finally_block_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_finally_block_computed = true;
        }
        return this.label_finally_block_value;
    }

    private soot.jimple.Stmt label_finally_block_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt label_exception_handler() {
        if (this.label_exception_handler_computed) {
            return this.label_exception_handler_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_exception_handler_value = label_exception_handler_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_exception_handler_computed = true;
        }
        return this.label_exception_handler_value;
    }

    private soot.jimple.Stmt label_exception_handler_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt label_catch_end() {
        if (this.label_catch_end_computed) {
            return this.label_catch_end_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_catch_end_value = label_catch_end_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_catch_end_computed = true;
        }
        return this.label_catch_end_value;
    }

    private soot.jimple.Stmt label_catch_end_compute() {
        return newLabel();
    }

    public boolean needsFinallyTrap() {
        state();
        return getNumCatchClause() != 0 || enclosedByExceptionHandler();
    }

    public ArrayList exceptionRanges() {
        if (this.exceptionRanges_computed) {
            return this.exceptionRanges_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.exceptionRanges_value = exceptionRanges_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.exceptionRanges_computed = true;
        }
        return this.exceptionRanges_value;
    }

    private ArrayList exceptionRanges_compute() {
        return new ArrayList();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        if (getBlock().modifiedInScope(var)) {
            return true;
        }
        Iterator<CatchClause> it = getCatchClauseList().iterator();
        while (it.hasNext()) {
            CatchClause cc = it.next();
            if (cc.modifiedInScope(var)) {
                return true;
            }
        }
        return hasFinally() && getFinally().modifiedInScope(var);
    }

    public boolean handlesException(TypeDecl exceptionType) {
        if (this.handlesException_TypeDecl_values == null) {
            this.handlesException_TypeDecl_values = new HashMap(4);
        }
        if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
            return ((Boolean) this.handlesException_TypeDecl_values.get(exceptionType)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        if (isFinal && num == state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, Boolean.valueOf(handlesException_TypeDecl_value));
        }
        return handlesException_TypeDecl_value;
    }

    public TypeDecl typeError() {
        if (this.typeError_computed) {
            return this.typeError_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeError_value = getParent().Define_TypeDecl_typeError(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeError_computed = true;
        }
        return this.typeError_value;
    }

    public TypeDecl typeRuntimeException() {
        if (this.typeRuntimeException_computed) {
            return this.typeRuntimeException_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeRuntimeException_value = getParent().Define_TypeDecl_typeRuntimeException(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeRuntimeException_computed = true;
        }
        return this.typeRuntimeException_value;
    }

    public boolean enclosedByExceptionHandler() {
        state();
        boolean enclosedByExceptionHandler_value = getParent().Define_boolean_enclosedByExceptionHandler(this, null);
        return enclosedByExceptionHandler_value;
    }

    public TypeDecl typeThrowable() {
        state();
        TypeDecl typeThrowable_value = getParent().Define_TypeDecl_typeThrowable(this, null);
        return typeThrowable_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getFinallyOptNoTransform()) {
            return isDAbefore(v);
        }
        if (caller == getCatchClauseListNoTransform()) {
            caller.getIndexOfChild(child);
            return getBlock().isDAbefore(v);
        } else if (caller == getBlockNoTransform()) {
            return isDAbefore(v);
        } else {
            return getParent().Define_boolean_isDAbefore(this, caller, v);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getFinallyOptNoTransform()) {
            if (!getBlock().isDUeverywhere(v)) {
                return false;
            }
            for (int i = 0; i < getNumCatchClause(); i++) {
                if (!getCatchClause(i).getBlock().unassignedEverywhere(v, this)) {
                    return false;
                }
            }
            return true;
        } else if (caller == getCatchClauseListNoTransform()) {
            caller.getIndexOfChild(child);
            if (!getBlock().isDUafter(v) || !getBlock().isDUeverywhere(v)) {
                return false;
            }
            return true;
        } else if (caller == getBlockNoTransform()) {
            return isDUbefore(v);
        } else {
            return getParent().Define_boolean_isDUbefore(this, caller, v);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getBlockNoTransform()) {
            for (int i = 0; i < getNumCatchClause(); i++) {
                if (getCatchClause(i).handles(exceptionType)) {
                    return true;
                }
            }
            if (hasFinally() && !getFinally().canCompleteNormally()) {
                return true;
            }
            return handlesException(exceptionType);
        } else if (caller == getCatchClauseListNoTransform()) {
            caller.getIndexOfChild(child);
            if (hasFinally() && !getFinally().canCompleteNormally()) {
                return true;
            }
            return handlesException(exceptionType);
        } else {
            return getParent().Define_boolean_handlesException(this, caller, exceptionType);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getFinallyOptNoTransform()) {
            return reachable();
        }
        if (caller == getBlockNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getCatchClauseListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            for (int i = 0; i < childIndex; i++) {
                if (getCatchClause(i).handles(exceptionType)) {
                    return false;
                }
            }
            if (catchableException(exceptionType) || exceptionType.mayCatch(typeError()) || exceptionType.mayCatch(typeRuntimeException())) {
                return true;
            }
            return false;
        }
        return getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getFinallyOptNoTransform()) {
            return reachable();
        }
        if (caller == getCatchClauseListNoTransform()) {
            caller.getIndexOfChild(child);
            return reachable();
        } else if (caller == getBlockNoTransform()) {
            return reachable();
        } else {
            return getParent().Define_boolean_reportUnreachable(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
        if (caller == getCatchClauseListNoTransform()) {
            caller.getIndexOfChild(child);
            return exceptionRanges();
        } else if (caller == getBlockNoTransform()) {
            return exceptionRanges();
        } else {
            return getParent().Define_ArrayList_exceptionRanges(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
        if (caller == getCatchClauseListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            Collection<TypeDecl> excp = new HashSet<>();
            getBlock().collectExceptions(excp, this);
            Collection<TypeDecl> caught = new LinkedList<>();
            for (TypeDecl exception : excp) {
                if (getCatchClause(index).handles(exception)) {
                    boolean already = false;
                    int i = 0;
                    while (true) {
                        if (i < index) {
                            if (!getCatchClause(i).handles(exception)) {
                                i++;
                            } else {
                                already = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!already) {
                        caught.add(exception);
                    }
                }
            }
            return caught;
        }
        return getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
