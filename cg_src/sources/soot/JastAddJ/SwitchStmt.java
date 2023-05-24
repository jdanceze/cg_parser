package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SwitchStmt.class */
public class SwitchStmt extends BranchTargetStmt implements Cloneable {
    protected Map targetOf_ContinueStmt_values;
    protected Map targetOf_BreakStmt_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean canCompleteNormally_value;
    protected DefaultCase defaultCase_value;
    protected soot.jimple.Stmt end_label_value;
    protected TypeDecl typeInt_value;
    protected TypeDecl typeLong_value;
    protected boolean canCompleteNormally_computed = false;
    protected boolean defaultCase_computed = false;
    protected boolean end_label_computed = false;
    protected boolean typeInt_computed = false;
    protected boolean typeLong_computed = false;

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetOf_ContinueStmt_values = null;
        this.targetOf_BreakStmt_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.canCompleteNormally_computed = false;
        this.defaultCase_computed = false;
        this.defaultCase_value = null;
        this.end_label_computed = false;
        this.end_label_value = null;
        this.typeInt_computed = false;
        this.typeInt_value = null;
        this.typeLong_computed = false;
        this.typeLong_value = null;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public SwitchStmt clone() throws CloneNotSupportedException {
        SwitchStmt node = (SwitchStmt) super.clone();
        node.targetOf_ContinueStmt_values = null;
        node.targetOf_BreakStmt_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.canCompleteNormally_computed = false;
        node.defaultCase_computed = false;
        node.defaultCase_value = null;
        node.end_label_computed = false;
        node.end_label_value = null;
        node.typeInt_computed = false;
        node.typeInt_value = null;
        node.typeLong_computed = false;
        node.typeLong_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            SwitchStmt node = clone();
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
        s.append("switch (");
        getExpr().toString(s);
        s.append(")");
        getBlock().toString(s);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        soot.jimple.Stmt cond_label = newLabel();
        soot.jimple.Stmt switch_label = newLabel();
        b.setLine(this);
        b.add(b.newGotoStmt(cond_label, this));
        getBlock().jimplify2(b);
        if (canCompleteNormally()) {
            b.setLine(this);
            b.add(b.newGotoStmt(end_label(), this));
        }
        b.addLabel(cond_label);
        Value expr = asImmediate(b, getExpr().eval(b));
        TreeMap map = new TreeMap();
        for (int i = 0; i < getBlock().getNumStmt(); i++) {
            if (getBlock().getStmt(i) instanceof ConstCase) {
                ConstCase ca = (ConstCase) getBlock().getStmt(i);
                map.put(new Integer(ca.getValue().constant().intValue()), ca);
            }
        }
        long low = map.isEmpty() ? 0 : ((Integer) map.firstKey()).intValue();
        long high = map.isEmpty() ? 0 : ((Integer) map.lastKey()).intValue();
        long tableSwitchSize = 8 + (((high - low) + 1) * 4);
        long lookupSwitchSize = 4 + (map.size() * 8);
        b.addLabel(switch_label);
        soot.jimple.Stmt defaultStmt = defaultCase() != null ? defaultCase().label() : end_label();
        if (tableSwitchSize < lookupSwitchSize) {
            ArrayList targets = new ArrayList();
            long j = low;
            while (true) {
                long i2 = j;
                if (i2 > high) {
                    break;
                }
                ConstCase ca2 = (ConstCase) map.get(new Integer((int) i2));
                if (ca2 != null) {
                    targets.add(ca2.label());
                } else {
                    targets.add(defaultStmt);
                }
                j = i2 + 1;
            }
            b.setLine(this);
            b.add(b.newTableSwitchStmt(expr, (int) low, (int) high, targets, defaultStmt, this));
        } else {
            ArrayList targets2 = new ArrayList();
            ArrayList values = new ArrayList();
            for (ConstCase ca3 : map.values()) {
                targets2.add(ca3.label());
                values.add(IntType.emitConstant(ca3.getValue().constant().intValue()));
            }
            b.setLine(this);
            b.add(b.newLookupSwitchStmt(expr, values, targets2, defaultStmt, this));
        }
        b.addLabel(end_label());
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        if (getExpr().type().isEnumDecl()) {
            TypeDecl type = getExpr().type();
            hostType().createEnumArray(type);
            hostType().createEnumMethod(type);
            setExpr(hostType().createEnumMethod(type).createBoundAccess(new List()).qualifiesAccess(new ArrayAccess(getExpr().qualifiesAccess(new MethodAccess("ordinal", new List())))));
        }
        super.transformation();
    }

    public SwitchStmt() {
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public SwitchStmt(Expr p0, Block p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
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

    public void refined_Enums_SwitchStmt_typeCheck() {
        TypeDecl type = getExpr().type();
        if ((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl()) {
            error("Switch expression must be of char, byte, short, int, or enum type");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl type = getExpr().type();
        if ((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl() && !type.isString()) {
            error("Switch expression must be of type char, byte, short, int, enum, or string");
        }
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.BranchPropagation
    public boolean targetOf(ContinueStmt stmt) {
        if (this.targetOf_ContinueStmt_values == null) {
            this.targetOf_ContinueStmt_values = new HashMap(4);
        }
        if (this.targetOf_ContinueStmt_values.containsKey(stmt)) {
            return ((Boolean) this.targetOf_ContinueStmt_values.get(stmt)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean targetOf_ContinueStmt_value = targetOf_compute(stmt);
        if (isFinal && num == state().boundariesCrossed) {
            this.targetOf_ContinueStmt_values.put(stmt, Boolean.valueOf(targetOf_ContinueStmt_value));
        }
        return targetOf_ContinueStmt_value;
    }

    private boolean targetOf_compute(ContinueStmt stmt) {
        return false;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.BranchPropagation
    public boolean targetOf(BreakStmt stmt) {
        if (this.targetOf_BreakStmt_values == null) {
            this.targetOf_BreakStmt_values = new HashMap(4);
        }
        if (this.targetOf_BreakStmt_values.containsKey(stmt)) {
            return ((Boolean) this.targetOf_BreakStmt_values.get(stmt)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean targetOf_BreakStmt_value = targetOf_compute(stmt);
        if (isFinal && num == state().boundariesCrossed) {
            this.targetOf_BreakStmt_values.put(stmt, Boolean.valueOf(targetOf_BreakStmt_value));
        }
        return targetOf_BreakStmt_value;
    }

    private boolean targetOf_compute(BreakStmt stmt) {
        return !stmt.hasLabel();
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
        if (noDefaultLabel() && !getExpr().isDAafter(v)) {
            return false;
        }
        if ((switchLabelEndsBlock() && !getExpr().isDAafter(v)) || !assignedAfterLastStmt(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean assignedAfterLastStmt(Variable v) {
        state();
        return getBlock().isDAafter(v);
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
        if (noDefaultLabel() && !getExpr().isDUafter(v)) {
            return false;
        }
        if ((switchLabelEndsBlock() && !getExpr().isDUafter(v)) || !unassignedAfterLastStmt(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean unassignedAfterLastStmt(Variable v) {
        state();
        return getBlock().isDUafter(v);
    }

    public boolean switchLabelEndsBlock() {
        state();
        return getBlock().getNumStmt() > 0 && (getBlock().getStmt(getBlock().getNumStmt() - 1) instanceof ConstCase);
    }

    public boolean lastStmtCanCompleteNormally() {
        state();
        return getBlock().canCompleteNormally();
    }

    public boolean noStmts() {
        state();
        for (int i = 0; i < getBlock().getNumStmt(); i++) {
            if (!(getBlock().getStmt(i) instanceof Case)) {
                return false;
            }
        }
        return true;
    }

    public boolean noStmtsAfterLastLabel() {
        state();
        return getBlock().getNumStmt() > 0 && (getBlock().getStmt(getBlock().getNumStmt() - 1) instanceof Case);
    }

    public boolean noDefaultLabel() {
        state();
        for (int i = 0; i < getBlock().getNumStmt(); i++) {
            if (getBlock().getStmt(i) instanceof DefaultCase) {
                return false;
            }
        }
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
        return lastStmtCanCompleteNormally() || noStmts() || noStmtsAfterLastLabel() || noDefaultLabel() || reachableBreak();
    }

    public DefaultCase defaultCase() {
        if (this.defaultCase_computed) {
            return this.defaultCase_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.defaultCase_value = defaultCase_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.defaultCase_computed = true;
        }
        return this.defaultCase_value;
    }

    private DefaultCase defaultCase_compute() {
        for (int i = 0; i < getBlock().getNumStmt(); i++) {
            if (getBlock().getStmt(i) instanceof DefaultCase) {
                return (DefaultCase) getBlock().getStmt(i);
            }
        }
        return null;
    }

    public soot.jimple.Stmt end_label() {
        if (this.end_label_computed) {
            return this.end_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.end_label_value = end_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.end_label_computed = true;
        }
        return this.end_label_value;
    }

    private soot.jimple.Stmt end_label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.Stmt
    public soot.jimple.Stmt break_label() {
        state();
        return end_label();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return getBlock().modifiedInScope(var);
    }

    public TypeDecl typeInt() {
        if (this.typeInt_computed) {
            return this.typeInt_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeInt_computed = true;
        }
        return this.typeInt_value;
    }

    public TypeDecl typeLong() {
        if (this.typeLong_computed) {
            return this.typeLong_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeLong_value = getParent().Define_TypeDecl_typeLong(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.typeLong_computed = true;
        }
        return this.typeLong_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getBlockNoTransform()) {
            return getExpr().isDAafter(v);
        }
        if (caller == getExprNoTransform()) {
            if (((ASTNode) v).isDescendantTo(this)) {
                return false;
            }
            boolean result = isDAbefore(v);
            return result;
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
    public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_insideSwitch(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
        if (caller == getBlockNoTransform()) {
            Block b = getBlock();
            for (int i = 0; i < b.getNumStmt(); i++) {
                if ((b.getStmt(i) instanceof Case) && ((Case) b.getStmt(i)).constValue(c)) {
                    return (Case) b.getStmt(i);
                }
            }
            return null;
        }
        return getParent().Define_Case_bind(this, caller, c);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
        if (caller == getBlockNoTransform()) {
            return getExpr().type();
        }
        return getParent().Define_TypeDecl_switchType(this, caller);
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

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
