package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Binary.class */
public abstract class Binary extends Expr implements Cloneable {
    protected int isConstant_visited = -1;
    protected boolean isConstant_computed = false;
    protected boolean isConstant_initialized = false;
    protected boolean isConstant_value;
    protected Map isDAafterTrue_Variable_values;
    protected Map isDAafterFalse_Variable_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map isDUbefore_Variable_values;

    public abstract String printOp();

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
        this.isDAafterTrue_Variable_values = null;
        this.isDAafterFalse_Variable_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.isDUbefore_Variable_values = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public Binary clone() throws CloneNotSupportedException {
        Binary node = (Binary) super.clone();
        node.isConstant_visited = -1;
        node.isConstant_computed = false;
        node.isConstant_initialized = false;
        node.isDAafterTrue_Variable_values = null;
        node.isDAafterFalse_Variable_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.isDUbefore_Variable_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getLeftOperand().toString(s);
        s.append(printOp());
        getRightOperand().toString(s);
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return asLocal(b, emitOperation(b, getLeftOperand().type().emitCastTo(b, getLeftOperand(), type()), getRightOperand().type().emitCastTo(b, getRightOperand(), type())));
    }

    public Value emitShiftExpr(Body b) {
        return asLocal(b, emitOperation(b, getLeftOperand().type().emitCastTo(b, getLeftOperand(), type()), getRightOperand().type().emitCastTo(b, getRightOperand(), typeInt())));
    }

    public Value emitOperation(Body b, Value left, Value right) {
        throw new Error("emitOperation not implemented in " + getClass().getName());
    }

    public Binary() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public Binary(Expr p0, Expr p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setLeftOperand(Expr node) {
        setChild(node, 0);
    }

    public Expr getLeftOperand() {
        return (Expr) getChild(0);
    }

    public Expr getLeftOperandNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setRightOperand(Expr node) {
        setChild(node, 1);
    }

    public Expr getRightOperand() {
        return (Expr) getChild(1);
    }

    public Expr getRightOperandNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    private TypeDecl refined_ConstantExpression_Binary_binaryNumericPromotedType() {
        TypeDecl leftType = left().type();
        TypeDecl rightType = right().type();
        if (leftType.isString()) {
            return leftType;
        }
        if (rightType.isString()) {
            return rightType;
        }
        if (leftType.isNumericType() && rightType.isNumericType()) {
            return leftType.binaryNumericPromotion(rightType);
        }
        if (leftType.isBoolean() && rightType.isBoolean()) {
            return leftType;
        }
        return unknownType();
    }

    @Override // soot.JastAddJ.Expr
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
        return getLeftOperand().isConstant() && getRightOperand().isConstant();
    }

    public Expr left() {
        state();
        return getLeftOperand();
    }

    public Expr right() {
        state();
        return getRightOperand();
    }

    public TypeDecl binaryNumericPromotedType() {
        state();
        TypeDecl leftType = left().type();
        TypeDecl rightType = right().type();
        if (leftType.isBoolean() && rightType.isBoolean()) {
            return leftType.isReferenceType() ? leftType.unboxed() : leftType;
        }
        return refined_ConstantExpression_Binary_binaryNumericPromotedType();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        if (this.isDAafterTrue_Variable_values == null) {
            this.isDAafterTrue_Variable_values = new HashMap(4);
        }
        if (this.isDAafterTrue_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterTrue_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterTrue_Variable_value = isDAafterTrue_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterTrue_Variable_values.put(v, Boolean.valueOf(isDAafterTrue_Variable_value));
        }
        return isDAafterTrue_Variable_value;
    }

    private boolean isDAafterTrue_compute(Variable v) {
        return getRightOperand().isDAafter(v) || isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        if (this.isDAafterFalse_Variable_values == null) {
            this.isDAafterFalse_Variable_values = new HashMap(4);
        }
        if (this.isDAafterFalse_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterFalse_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterFalse_Variable_value = isDAafterFalse_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterFalse_Variable_values.put(v, Boolean.valueOf(isDAafterFalse_Variable_value));
        }
        return isDAafterFalse_Variable_value;
    }

    private boolean isDAafterFalse_compute(Variable v) {
        return getRightOperand().isDAafter(v) || isTrue();
    }

    @Override // soot.JastAddJ.Expr
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
        return getRightOperand().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
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
        return getRightOperand().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUbefore(Variable v) {
        if (this.isDUbefore_Variable_values == null) {
            this.isDUbefore_Variable_values = new HashMap(4);
        }
        if (this.isDUbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDUbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, Boolean.valueOf(isDUbefore_Variable_value));
        }
        return isDUbefore_Variable_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightOperandNoTransform()) {
            return getLeftOperand().isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightOperandNoTransform()) {
            return getLeftOperand().isDUafter(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
