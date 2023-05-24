package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ModExpr.class */
public class ModExpr extends MultiplicativeExpr implements Cloneable {
    protected int isConstant_visited = -1;
    protected boolean isConstant_computed = false;
    protected boolean isConstant_initialized = false;
    protected boolean isConstant_value;

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ModExpr clone() throws CloneNotSupportedException {
        ModExpr node = (ModExpr) super.clone();
        node.isConstant_visited = -1;
        node.isConstant_computed = false;
        node.isConstant_initialized = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ModExpr node = clone();
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

    @Override // soot.JastAddJ.Binary
    public Value emitOperation(Body b, Value left, Value right) {
        return asLocal(b, b.newRemExpr(asImmediate(b, left), asImmediate(b, right), this));
    }

    public ModExpr() {
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public ModExpr(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary
    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().mod(getLeftOperand().constant(), getRightOperand().constant());
    }

    @Override // soot.JastAddJ.Binary, soot.JastAddJ.Expr
    public boolean isConstant() {
        if (this.isConstant_computed) {
            return this.isConstant_value;
        }
        ASTNode.State state = state();
        if (!this.isConstant_initialized) {
            this.isConstant_initialized = true;
            this.isConstant_value = false;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.isConstant_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_isConstant_value = isConstant_compute();
                if (new_isConstant_value != this.isConstant_value) {
                    state.CHANGE = true;
                }
                this.isConstant_value = new_isConstant_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.isConstant_computed = true;
            } else {
                state.RESET_CYCLE = true;
                isConstant_compute();
                state.RESET_CYCLE = false;
                this.isConstant_computed = false;
                this.isConstant_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.isConstant_value;
        } else if (this.isConstant_visited != state.CIRCLE_INDEX) {
            this.isConstant_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.isConstant_computed = false;
                this.isConstant_initialized = false;
                this.isConstant_visited = -1;
                return this.isConstant_value;
            }
            boolean new_isConstant_value2 = isConstant_compute();
            if (new_isConstant_value2 != this.isConstant_value) {
                state.CHANGE = true;
            }
            this.isConstant_value = new_isConstant_value2;
            return this.isConstant_value;
        } else {
            return this.isConstant_value;
        }
    }

    private boolean isConstant_compute() {
        if (getLeftOperand().isConstant() && getRightOperand().isConstant()) {
            return (getRightOperand().type().isInt() && getRightOperand().constant().intValue() == 0) ? false : true;
        }
        return false;
    }

    @Override // soot.JastAddJ.Binary
    public String printOp() {
        state();
        return " % ";
    }

    @Override // soot.JastAddJ.MultiplicativeExpr, soot.JastAddJ.ArithmeticExpr, soot.JastAddJ.Binary, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
