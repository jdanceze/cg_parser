package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.tagkit.ThrowCreatedByCompilerTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SynchronizedStmt.class */
public class SynchronizedStmt extends Stmt implements Cloneable, FinallyHost {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_value;
    protected Map monitor_Body_values;
    protected ArrayList exceptionRanges_value;
    protected soot.jimple.Stmt label_begin_value;
    protected soot.jimple.Stmt label_end_value;
    protected soot.jimple.Stmt label_finally_value;
    protected soot.jimple.Stmt label_finally_block_value;
    protected soot.jimple.Stmt label_exception_handler_value;
    protected boolean canCompleteNormally_computed = false;
    protected boolean exceptionRanges_computed = false;
    protected boolean label_begin_computed = false;
    protected boolean label_end_computed = false;
    protected boolean label_finally_computed = false;
    protected boolean label_finally_block_computed = false;
    protected boolean label_exception_handler_computed = false;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.monitor_Body_values = null;
        this.exceptionRanges_computed = false;
        this.exceptionRanges_value = null;
        this.label_begin_computed = false;
        this.label_begin_value = null;
        this.label_end_computed = false;
        this.label_end_value = null;
        this.label_finally_computed = false;
        this.label_finally_value = null;
        this.label_finally_block_computed = false;
        this.label_finally_block_value = null;
        this.label_exception_handler_computed = false;
        this.label_exception_handler_value = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public SynchronizedStmt clone() throws CloneNotSupportedException {
        SynchronizedStmt node = (SynchronizedStmt) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.monitor_Body_values = null;
        node.exceptionRanges_computed = false;
        node.exceptionRanges_value = null;
        node.label_begin_computed = false;
        node.label_begin_value = null;
        node.label_end_computed = false;
        node.label_end_value = null;
        node.label_finally_computed = false;
        node.label_finally_value = null;
        node.label_finally_block_computed = false;
        node.label_finally_block_value = null;
        node.label_exception_handler_computed = false;
        node.label_exception_handler_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            SynchronizedStmt node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void collectFinally(Stmt branchStmt, ArrayList list) {
        list.add(this);
        super.collectFinally(branchStmt, list);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("synchronized(");
        getExpr().toString(s);
        s.append(") ");
        getBlock().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl type = getExpr().type();
        if (!type.isReferenceType() || type.isNull()) {
            error("*** The type of the expression must be a reference");
        }
    }

    @Override // soot.JastAddJ.FinallyHost
    public void emitFinallyCode(Body b) {
        b.setLine(this);
        b.add(b.newExitMonitorStmt(monitor(b), this));
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        soot.jimple.Stmt stmtEnd;
        b.setLine(this);
        b.add(b.newEnterMonitorStmt(monitor(b), this));
        b.addLabel(label_begin());
        exceptionRanges().add(label_begin());
        getBlock().jimplify2(b);
        if (getBlock().canCompleteNormally()) {
            emitFinallyCode(b);
            b.add(b.newGotoStmt(label_end(), this));
        }
        b.addLabel(label_exception_handler());
        Local l = b.newTemp(typeThrowable().getSootType());
        b.add(b.newIdentityStmt(l, b.newCaughtExceptionRef(this), this));
        emitFinallyCode(b);
        b.addLabel(label_end());
        soot.jimple.Stmt throwStmt = b.newThrowStmt(l, this);
        throwStmt.addTag(new ThrowCreatedByCompilerTag());
        b.add(throwStmt);
        Iterator iter = exceptionRanges().iterator();
        while (iter.hasNext()) {
            soot.jimple.Stmt stmtBegin = (soot.jimple.Stmt) iter.next();
            if (iter.hasNext()) {
                stmtEnd = (soot.jimple.Stmt) iter.next();
            } else {
                stmtEnd = label_end();
            }
            if (stmtBegin != stmtEnd) {
                b.addTrap(typeThrowable(), stmtBegin, stmtEnd, label_exception_handler());
            }
        }
    }

    public SynchronizedStmt() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public SynchronizedStmt(Expr p0, Block p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setExpr(Expr node) {
        setChild(node, 0);
    }

    public Expr getExpr() {
        return (Expr) getChild(0);
    }

    public Expr getExprNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setBlock(Block node) {
        setChild(node, 1);
    }

    public Block getBlock() {
        return (Block) getChild(1);
    }

    public Block getBlockNoTransform() {
        return (Block) getChildNoTransform(1);
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
        return getBlock().isDAafter(v);
    }

    @Override // soot.JastAddJ.FinallyHost
    public boolean isDUafterFinally(Variable v) {
        state();
        return true;
    }

    @Override // soot.JastAddJ.FinallyHost
    public boolean isDAafterFinally(Variable v) {
        state();
        return false;
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
        return getBlock().isDUafter(v);
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
        return getBlock().canCompleteNormally();
    }

    public Local monitor(Body b) {
        if (this.monitor_Body_values == null) {
            this.monitor_Body_values = new HashMap(4);
        }
        if (this.monitor_Body_values.containsKey(b)) {
            return (Local) this.monitor_Body_values.get(b);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Local monitor_Body_value = monitor_compute(b);
        if (isFinal && num == state().boundariesCrossed) {
            this.monitor_Body_values.put(b, monitor_Body_value);
        }
        return monitor_Body_value;
    }

    private Local monitor_compute(Body b) {
        return b.newTemp(getExpr().eval(b));
    }

    public boolean needsFinallyTrap() {
        state();
        return enclosedByExceptionHandler();
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

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return getBlock().modifiedInScope(var);
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
        if (caller == getBlockNoTransform()) {
            return getExpr().isDAafter(v);
        }
        if (caller == getExprNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return getExpr().isDUafter(v);
        }
        if (caller == getExprNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
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
        if (caller == getBlockNoTransform()) {
            return exceptionRanges();
        }
        return getParent().Define_ArrayList_exceptionRanges(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
