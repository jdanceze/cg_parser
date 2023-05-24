package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ReturnStmt.class */
public class ReturnStmt extends Stmt implements Cloneable {
    protected boolean finallyList_computed;
    protected ArrayList finallyList_value;
    protected Map isDAafter_Variable_values;
    protected Map isDUafterReachedFinallyBlocks_Variable_values;
    protected Map isDAafterReachedFinallyBlocks_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_computed;
    protected boolean canCompleteNormally_value;
    protected boolean inSynchronizedBlock_computed;
    protected boolean inSynchronizedBlock_value;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.finallyList_computed = false;
        this.finallyList_value = null;
        this.isDAafter_Variable_values = null;
        this.isDUafterReachedFinallyBlocks_Variable_values = null;
        this.isDAafterReachedFinallyBlocks_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.inSynchronizedBlock_computed = false;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public ReturnStmt clone() throws CloneNotSupportedException {
        ReturnStmt node = (ReturnStmt) super.clone();
        node.finallyList_computed = false;
        node.finallyList_value = null;
        node.isDAafter_Variable_values = null;
        node.isDUafterReachedFinallyBlocks_Variable_values = null;
        node.isDAafterReachedFinallyBlocks_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.inSynchronizedBlock_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ReturnStmt node = clone();
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
        c.add(this);
    }

    public ReturnStmt(Expr expr) {
        this(new Opt(expr));
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("return ");
        if (hasResult()) {
            getResult().toString(s);
        }
        s.append(";");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (hasResult() && !returnType().isVoid() && !getResult().type().assignConversionTo(returnType(), getResult())) {
            error("return value must be an instance of " + returnType().typeName() + " which " + getResult().type().typeName() + " is not");
        }
        if (returnType().isVoid() && hasResult()) {
            error("return stmt may not have an expression in void methods");
        }
        if (!returnType().isVoid() && !hasResult()) {
            error("return stmt must have an expression in non void methods");
        }
        if ((enclosingBodyDecl() instanceof InstanceInitializer) || (enclosingBodyDecl() instanceof StaticInitializer)) {
            error("Initializers may not return");
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        if (hasResult()) {
            TypeDecl type = returnType();
            if (type.isVoid()) {
                throw new Error("Can not return a value from a void body");
            }
            Local local = asLocal(b, getResult().type().emitCastTo(b, getResult().eval(b), type, getResult()), type.getSootType());
            ArrayList list = exceptionRanges();
            if (!inSynchronizedBlock()) {
                endExceptionRange(b, list);
            }
            Iterator iter = finallyList().iterator();
            while (iter.hasNext()) {
                FinallyHost stmt = (FinallyHost) iter.next();
                stmt.emitFinallyCode(b);
            }
            b.setLine(this);
            if (inSynchronizedBlock()) {
                endExceptionRange(b, list);
            }
            b.add(b.newReturnStmt(local, this));
            beginExceptionRange(b, list);
            return;
        }
        ArrayList list2 = exceptionRanges();
        if (!inSynchronizedBlock()) {
            endExceptionRange(b, list2);
        }
        Iterator iter2 = finallyList().iterator();
        while (iter2.hasNext()) {
            FinallyHost stmt2 = (FinallyHost) iter2.next();
            stmt2.emitFinallyCode(b);
        }
        b.setLine(this);
        if (inSynchronizedBlock()) {
            endExceptionRange(b, list2);
        }
        b.add(b.newReturnVoidStmt(this));
        beginExceptionRange(b, list2);
    }

    public ReturnStmt() {
        this.finallyList_computed = false;
        this.canCompleteNormally_computed = false;
        this.inSynchronizedBlock_computed = false;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new Opt(), 0);
    }

    public ReturnStmt(Opt<Expr> p0) {
        this.finallyList_computed = false;
        this.canCompleteNormally_computed = false;
        this.inSynchronizedBlock_computed = false;
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setResultOpt(Opt<Expr> opt) {
        setChild(opt, 0);
    }

    public boolean hasResult() {
        return getResultOpt().getNumChild() != 0;
    }

    public Expr getResult() {
        return getResultOpt().getChild(0);
    }

    public void setResult(Expr node) {
        getResultOpt().setChild(node, 0);
    }

    public Opt<Expr> getResultOpt() {
        return (Opt) getChild(0);
    }

    public Opt<Expr> getResultOptNoTransform() {
        return (Opt) getChildNoTransform(0);
    }

    public ArrayList finallyList() {
        if (this.finallyList_computed) {
            return this.finallyList_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.finallyList_value = finallyList_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.finallyList_computed = true;
        }
        return this.finallyList_value;
    }

    private ArrayList finallyList_compute() {
        ArrayList list = new ArrayList();
        collectFinally(this, list);
        return list;
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
        return true;
    }

    public boolean isDUafterReachedFinallyBlocks(Variable v) {
        if (this.isDUafterReachedFinallyBlocks_Variable_values == null) {
            this.isDUafterReachedFinallyBlocks_Variable_values = new HashMap(4);
        }
        if (this.isDUafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUafterReachedFinallyBlocks_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUafterReachedFinallyBlocks_Variable_value = isDUafterReachedFinallyBlocks_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUafterReachedFinallyBlocks_Variable_values.put(v, Boolean.valueOf(isDUafterReachedFinallyBlocks_Variable_value));
        }
        return isDUafterReachedFinallyBlocks_Variable_value;
    }

    private boolean isDUafterReachedFinallyBlocks_compute(Variable v) {
        if (!isDUbefore(v) && finallyList().isEmpty()) {
            return false;
        }
        Iterator iter = finallyList().iterator();
        while (iter.hasNext()) {
            FinallyHost f = (FinallyHost) iter.next();
            if (!f.isDUafterFinally(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDAafterReachedFinallyBlocks(Variable v) {
        if (this.isDAafterReachedFinallyBlocks_Variable_values == null) {
            this.isDAafterReachedFinallyBlocks_Variable_values = new HashMap(4);
        }
        if (this.isDAafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterReachedFinallyBlocks_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterReachedFinallyBlocks_Variable_value = isDAafterReachedFinallyBlocks_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterReachedFinallyBlocks_Variable_values.put(v, Boolean.valueOf(isDAafterReachedFinallyBlocks_Variable_value));
        }
        return isDAafterReachedFinallyBlocks_Variable_value;
    }

    private boolean isDAafterReachedFinallyBlocks_compute(Variable v) {
        if (hasResult()) {
            if (getResult().isDAafter(v)) {
                return true;
            }
        } else if (isDAbefore(v)) {
            return true;
        }
        if (finallyList().isEmpty()) {
            return false;
        }
        Iterator iter = finallyList().iterator();
        while (iter.hasNext()) {
            FinallyHost f = (FinallyHost) iter.next();
            if (!f.isDAafterFinally(v)) {
                return false;
            }
        }
        return true;
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
        return true;
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
        return false;
    }

    public boolean inSynchronizedBlock() {
        if (this.inSynchronizedBlock_computed) {
            return this.inSynchronizedBlock_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.inSynchronizedBlock_value = inSynchronizedBlock_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.inSynchronizedBlock_computed = true;
        }
        return this.inSynchronizedBlock_value;
    }

    private boolean inSynchronizedBlock_compute() {
        return !finallyList().isEmpty() && (finallyList().iterator().next() instanceof SynchronizedStmt);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    public TypeDecl returnType() {
        state();
        TypeDecl returnType_value = getParent().Define_TypeDecl_returnType(this, null);
        return returnType_value;
    }

    public ArrayList exceptionRanges() {
        state();
        ArrayList exceptionRanges_value = getParent().Define_ArrayList_exceptionRanges(this, null);
        return exceptionRanges_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getResultOptNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getResultOptNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getResultOptNoTransform()) {
            return returnType();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
