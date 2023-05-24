package soot.JastAddJ;

import java.util.ArrayList;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ConditionalExpr.class */
public class ConditionalExpr extends Expr implements Cloneable {
    protected Constant constant_value;
    protected boolean isConstant_value;
    protected boolean booleanOperator_value;
    protected TypeDecl type_value;
    protected soot.jimple.Stmt else_branch_label_value;
    protected soot.jimple.Stmt then_branch_label_value;
    protected boolean constant_computed = false;
    protected boolean isConstant_computed = false;
    protected boolean booleanOperator_computed = false;
    protected boolean type_computed = false;
    protected boolean else_branch_label_computed = false;
    protected boolean then_branch_label_computed = false;

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.constant_computed = false;
        this.constant_value = null;
        this.isConstant_computed = false;
        this.booleanOperator_computed = false;
        this.type_computed = false;
        this.type_value = null;
        this.else_branch_label_computed = false;
        this.else_branch_label_value = null;
        this.then_branch_label_computed = false;
        this.then_branch_label_value = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ConditionalExpr clone() throws CloneNotSupportedException {
        ConditionalExpr node = (ConditionalExpr) super.clone();
        node.constant_computed = false;
        node.constant_value = null;
        node.isConstant_computed = false;
        node.booleanOperator_computed = false;
        node.type_computed = false;
        node.type_value = null;
        node.else_branch_label_computed = false;
        node.else_branch_label_value = null;
        node.then_branch_label_computed = false;
        node.then_branch_label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ConditionalExpr node = clone();
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
        getCondition().toString(s);
        s.append(" ? ");
        getTrueExpr().toString(s);
        s.append(" : ");
        getFalseExpr().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!getCondition().type().isBoolean()) {
            error("The first operand of a conditional expression must be a boolean");
        }
        if (type().isUnknown() && !getTrueExpr().type().isUnknown() && !getFalseExpr().type().isUnknown()) {
            error("The types of the second and third operand in this conditional expression do not match");
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        b.setLine(this);
        if (type().isBoolean()) {
            return emitBooleanCondition(b);
        }
        Local result = b.newTemp(type().getSootType());
        soot.jimple.Stmt endBranch = newLabel();
        getCondition().emitEvalBranch(b);
        if (getCondition().canBeTrue()) {
            b.addLabel(then_branch_label());
            b.add(b.newAssignStmt(result, getTrueExpr().type().emitCastTo(b, getTrueExpr(), type()), this));
            if (getCondition().canBeFalse()) {
                b.add(b.newGotoStmt(endBranch, this));
            }
        }
        if (getCondition().canBeFalse()) {
            b.addLabel(else_branch_label());
            b.add(b.newAssignStmt(result, getFalseExpr().type().emitCastTo(b, getFalseExpr(), type()), this));
        }
        b.addLabel(endBranch);
        return result;
    }

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        b.setLine(this);
        newLabel();
        getCondition().emitEvalBranch(b);
        b.addLabel(then_branch_label());
        if (getCondition().canBeTrue()) {
            getTrueExpr().emitEvalBranch(b);
            b.add(b.newGotoStmt(true_label(), this));
        }
        b.addLabel(else_branch_label());
        if (getCondition().canBeFalse()) {
            getFalseExpr().emitEvalBranch(b);
            b.add(b.newGotoStmt(true_label(), this));
        }
    }

    public ConditionalExpr() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
    }

    public ConditionalExpr(Expr p0, Expr p1, Expr p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setCondition(Expr node) {
        setChild(node, 0);
    }

    public Expr getCondition() {
        return (Expr) getChild(0);
    }

    public Expr getConditionNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setTrueExpr(Expr node) {
        setChild(node, 1);
    }

    public Expr getTrueExpr() {
        return (Expr) getChild(1);
    }

    public Expr getTrueExprNoTransform() {
        return (Expr) getChildNoTransform(1);
    }

    public void setFalseExpr(Expr node) {
        setChild(node, 2);
    }

    public Expr getFalseExpr() {
        return (Expr) getChild(2);
    }

    public Expr getFalseExprNoTransform() {
        return (Expr) getChildNoTransform(2);
    }

    private TypeDecl refined_TypeAnalysis_ConditionalExpr_type() {
        TypeDecl trueType = getTrueExpr().type();
        TypeDecl falseType = getFalseExpr().type();
        if (trueType == falseType) {
            return trueType;
        }
        if (trueType.isNumericType() && falseType.isNumericType()) {
            if (trueType.isByte() && falseType.isShort()) {
                return falseType;
            }
            if (trueType.isShort() && falseType.isByte()) {
                return trueType;
            }
            if ((trueType.isByte() || trueType.isShort() || trueType.isChar()) && falseType.isInt() && getFalseExpr().isConstant() && getFalseExpr().representableIn(trueType)) {
                return trueType;
            }
            if ((falseType.isByte() || falseType.isShort() || falseType.isChar()) && trueType.isInt() && getTrueExpr().isConstant() && getTrueExpr().representableIn(falseType)) {
                return falseType;
            }
            return trueType.binaryNumericPromotion(falseType);
        } else if (trueType.isBoolean() && falseType.isBoolean()) {
            return trueType;
        } else {
            if (trueType.isReferenceType() && falseType.isNull()) {
                return trueType;
            }
            if (trueType.isNull() && falseType.isReferenceType()) {
                return falseType;
            }
            if (trueType.isReferenceType() && falseType.isReferenceType()) {
                if (trueType.assignConversionTo(falseType, null)) {
                    return falseType;
                }
                if (falseType.assignConversionTo(trueType, null)) {
                    return trueType;
                }
                return unknownType();
            }
            return unknownType();
        }
    }

    private TypeDecl refined_AutoBoxing_ConditionalExpr_type() {
        TypeDecl trueType = getTrueExpr().type();
        TypeDecl falseType = getFalseExpr().type();
        if (trueType.isBoolean() && falseType.isBoolean()) {
            if (trueType == falseType) {
                return trueType;
            }
            if (trueType.isReferenceType()) {
                return trueType.unboxed();
            }
            return trueType;
        }
        return refined_TypeAnalysis_ConditionalExpr_type();
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        if (this.constant_computed) {
            return this.constant_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.constant_value = constant_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.constant_computed = true;
        }
        return this.constant_value;
    }

    private Constant constant_compute() {
        return type().questionColon(getCondition().constant(), getTrueExpr().constant(), getFalseExpr().constant());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        if (this.isConstant_computed) {
            return this.isConstant_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isConstant_value = isConstant_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isConstant_computed = true;
        }
        return this.isConstant_value;
    }

    private boolean isConstant_compute() {
        return getCondition().isConstant() && getTrueExpr().isConstant() && getFalseExpr().isConstant();
    }

    public boolean booleanOperator() {
        if (this.booleanOperator_computed) {
            return this.booleanOperator_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.booleanOperator_value = booleanOperator_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.booleanOperator_computed = true;
        }
        return this.booleanOperator_value;
    }

    private boolean booleanOperator_compute() {
        return getTrueExpr().type().isBoolean() && getFalseExpr().type().isBoolean();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        return (getTrueExpr().isDAafterTrue(v) && getFalseExpr().isDAafterTrue(v)) || isFalse();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        return (getTrueExpr().isDAafterFalse(v) && getFalseExpr().isDAafterFalse(v)) || isTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return booleanOperator() ? isDAafterTrue(v) && isDAafterFalse(v) : getTrueExpr().isDAafter(v) && getFalseExpr().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return getTrueExpr().isDUafterTrue(v) && getFalseExpr().isDUafterTrue(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return getTrueExpr().isDUafterFalse(v) && getFalseExpr().isDUafterFalse(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return booleanOperator() ? isDUafterTrue(v) && isDUafterFalse(v) : getTrueExpr().isDUafter(v) && getFalseExpr().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        TypeDecl type = refined_AutoBoxing_ConditionalExpr_type();
        TypeDecl trueType = getTrueExpr().type();
        TypeDecl falseType = getFalseExpr().type();
        if (type.isUnknown()) {
            if (!trueType.isReferenceType() && !trueType.boxed().isUnknown()) {
                trueType = trueType.boxed();
            }
            if (!falseType.isReferenceType() && !falseType.boxed().isUnknown()) {
                falseType = falseType.boxed();
            }
            ArrayList list = new ArrayList();
            list.add(trueType);
            list.add(falseType);
            return type.lookupLUBType(list);
        }
        return type;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeTrue() {
        state();
        if (type().isBoolean()) {
            if (getTrueExpr().canBeTrue() && getFalseExpr().canBeTrue()) {
                return true;
            }
            if (getCondition().isTrue() && getTrueExpr().canBeTrue()) {
                return true;
            }
            return getCondition().isFalse() && getFalseExpr().canBeTrue();
        }
        return false;
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeFalse() {
        state();
        if (type().isBoolean()) {
            if (getTrueExpr().canBeFalse() && getFalseExpr().canBeFalse()) {
                return true;
            }
            if (getCondition().isTrue() && getTrueExpr().canBeFalse()) {
                return true;
            }
            return getCondition().isFalse() && getFalseExpr().canBeFalse();
        }
        return false;
    }

    public soot.jimple.Stmt else_branch_label() {
        if (this.else_branch_label_computed) {
            return this.else_branch_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.else_branch_label_value = else_branch_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.else_branch_label_computed = true;
        }
        return this.else_branch_label_value;
    }

    private soot.jimple.Stmt else_branch_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt then_branch_label() {
        if (this.then_branch_label_computed) {
            return this.then_branch_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.then_branch_label_value = then_branch_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.then_branch_label_computed = true;
        }
        return this.then_branch_label_value;
    }

    private soot.jimple.Stmt then_branch_label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getFalseExprNoTransform()) {
            return getCondition().isDAafterFalse(v);
        }
        if (caller == getTrueExprNoTransform()) {
            return getCondition().isDAafterTrue(v);
        }
        if (caller == getConditionNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getFalseExprNoTransform()) {
            return getCondition().isDUafterFalse(v);
        }
        if (caller == getTrueExprNoTransform()) {
            return getCondition().isDUafterTrue(v);
        }
        if (caller == getConditionNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getFalseExprNoTransform()) {
            return false_label();
        }
        if (caller == getTrueExprNoTransform()) {
            return false_label();
        }
        if (caller == getConditionNoTransform()) {
            return else_branch_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getFalseExprNoTransform()) {
            return true_label();
        }
        if (caller == getTrueExprNoTransform()) {
            return true_label();
        }
        if (caller == getConditionNoTransform()) {
            return then_branch_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
