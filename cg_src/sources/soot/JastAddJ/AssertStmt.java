package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AssertStmt.class */
public class AssertStmt extends Stmt implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public AssertStmt clone() throws CloneNotSupportedException {
        AssertStmt node = (AssertStmt) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AssertStmt node = clone();
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
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("assert ");
        getfirst().toString(s);
        if (hasExpr()) {
            s.append(" : ");
            getExpr().toString(s);
        }
        s.append(";");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getfirst().type().isBoolean()) {
            error("Assert requires boolean condition");
        }
        if (hasExpr() && getExpr().type().isVoid()) {
            error("The second part of an assert statement may not be void");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        super.transformation();
        hostType().topLevelType().createStaticClassField(hostType().topLevelType().referenceClassFieldName());
        FieldDeclaration assertionsDisabled = hostType().createAssertionsDisabled();
        Expr condition = (Expr) getfirst().fullCopy();
        List args = new List();
        if (hasExpr()) {
            if (getExpr().type().isString()) {
                args.add(new CastExpr(new TypeAccess("java.lang", "Object"), (Expr) getExpr().fullCopy()));
            } else {
                args.add(getExpr().fullCopy());
            }
        }
        Stmt stmt = new IfStmt(new LogNotExpr(new ParExpr(new OrLogicalExpr(new BoundFieldAccess(assertionsDisabled), condition))), new ThrowStmt(new ClassInstanceExpr(lookupType("java.lang", "AssertionError").createQualifiedAccess(), args, new Opt())), new Opt());
        replace(this).with(stmt);
        stmt.transformation();
    }

    public AssertStmt() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new Opt(), 1);
    }

    public AssertStmt(Expr p0, Opt<Expr> p1) {
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

    public void setfirst(Expr node) {
        setChild(node, 0);
    }

    public Expr getfirst() {
        return (Expr) getChild(0);
    }

    public Expr getfirstNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setExprOpt(Opt<Expr> opt) {
        setChild(opt, 1);
    }

    public boolean hasExpr() {
        return getExprOpt().getNumChild() != 0;
    }

    public Expr getExpr() {
        return getExprOpt().getChild(0);
    }

    public void setExpr(Expr node) {
        getExprOpt().setChild(node, 0);
    }

    public Opt<Expr> getExprOpt() {
        return (Opt) getChild(1);
    }

    public Opt<Expr> getExprOptNoTransform() {
        return (Opt) getChildNoTransform(1);
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
        return getfirst().isDAafter(v);
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
        return getfirst().isDUafter(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getExprOptNoTransform()) {
            return getfirst().isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
