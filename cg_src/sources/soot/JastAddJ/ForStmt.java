package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ForStmt.class */
public class ForStmt extends BranchTargetStmt implements Cloneable, VariableScope {
    protected Map targetOf_ContinueStmt_values;
    protected Map targetOf_BreakStmt_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map isDUbeforeCondition_Variable_values;
    protected Map localLookup_String_values;
    protected Map localVariableDeclaration_String_values;
    protected boolean canCompleteNormally_value;
    protected soot.jimple.Stmt cond_label_value;
    protected soot.jimple.Stmt begin_label_value;
    protected soot.jimple.Stmt update_label_value;
    protected soot.jimple.Stmt end_label_value;
    protected Map lookupVariable_String_values;
    protected boolean canCompleteNormally_computed = false;
    protected boolean cond_label_computed = false;
    protected boolean begin_label_computed = false;
    protected boolean update_label_computed = false;
    protected boolean end_label_computed = false;

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.targetOf_ContinueStmt_values = null;
        this.targetOf_BreakStmt_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.isDUbeforeCondition_Variable_values = null;
        this.localLookup_String_values = null;
        this.localVariableDeclaration_String_values = null;
        this.canCompleteNormally_computed = false;
        this.cond_label_computed = false;
        this.cond_label_value = null;
        this.begin_label_computed = false;
        this.begin_label_value = null;
        this.update_label_computed = false;
        this.update_label_value = null;
        this.end_label_computed = false;
        this.end_label_value = null;
        this.lookupVariable_String_values = null;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public ForStmt clone() throws CloneNotSupportedException {
        ForStmt node = (ForStmt) super.clone();
        node.targetOf_ContinueStmt_values = null;
        node.targetOf_BreakStmt_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.isDUbeforeCondition_Variable_values = null;
        node.localLookup_String_values = null;
        node.localVariableDeclaration_String_values = null;
        node.canCompleteNormally_computed = false;
        node.cond_label_computed = false;
        node.cond_label_value = null;
        node.begin_label_computed = false;
        node.begin_label_value = null;
        node.update_label_computed = false;
        node.update_label_value = null;
        node.end_label_computed = false;
        node.end_label_value = null;
        node.lookupVariable_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ForStmt node = clone();
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
        s.append("for(");
        if (getNumInitStmt() > 0) {
            if (getInitStmt(0) instanceof VariableDeclaration) {
                int minDimension = Integer.MAX_VALUE;
                for (int i = 0; i < getNumInitStmt(); i++) {
                    minDimension = Math.min(minDimension, ((VariableDeclaration) getInitStmt(i)).type().dimension());
                }
                VariableDeclaration v = (VariableDeclaration) getInitStmt(0);
                v.getModifiers().toString(s);
                s.append(v.type().elementType().typeName());
                for (int i2 = minDimension; i2 > 0; i2--) {
                    s.append("[]");
                }
                for (int i3 = 0; i3 < getNumInitStmt(); i3++) {
                    if (i3 != 0) {
                        s.append(",");
                    }
                    VariableDeclaration v2 = (VariableDeclaration) getInitStmt(i3);
                    s.append(Instruction.argsep + v2.name());
                    for (int j = v2.type().dimension() - minDimension; j > 0; j--) {
                        s.append("[]");
                    }
                    if (v2.hasInit()) {
                        s.append(" = ");
                        v2.getInit().toString(s);
                    }
                }
            } else if (getInitStmt(0) instanceof ExprStmt) {
                ExprStmt stmt = (ExprStmt) getInitStmt(0);
                stmt.getExpr().toString(s);
                for (int i4 = 1; i4 < getNumInitStmt(); i4++) {
                    s.append(", ");
                    ExprStmt stmt2 = (ExprStmt) getInitStmt(i4);
                    stmt2.getExpr().toString(s);
                }
            } else {
                throw new Error("Unexpected initializer in for loop: " + getInitStmt(0));
            }
        }
        s.append("; ");
        if (hasCondition()) {
            getCondition().toString(s);
        }
        s.append("; ");
        if (getNumUpdateStmt() > 0) {
            ExprStmt stmt3 = (ExprStmt) getUpdateStmt(0);
            stmt3.getExpr().toString(s);
            for (int i5 = 1; i5 < getNumUpdateStmt(); i5++) {
                s.append(", ");
                ExprStmt stmt4 = (ExprStmt) getUpdateStmt(i5);
                stmt4.getExpr().toString(s);
            }
        }
        s.append(") ");
        getStmt().toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (hasCondition()) {
            TypeDecl cond = getCondition().type();
            if (!cond.isBoolean()) {
                error("the type of \"" + getCondition() + "\" is " + cond.name() + " which is not boolean");
            }
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        for (int i = 0; i < getNumInitStmt(); i++) {
            getInitStmt(i).jimplify2(b);
        }
        b.addLabel(cond_label());
        getCondition().emitEvalBranch(b);
        if (getCondition().canBeTrue()) {
            b.addLabel(begin_label());
            getStmt().jimplify2(b);
            b.addLabel(update_label());
            for (int i2 = 0; i2 < getNumUpdateStmt(); i2++) {
                getUpdateStmt(i2).jimplify2(b);
            }
            b.setLine(this);
            b.add(b.newGotoStmt(cond_label(), this));
        }
        if (canCompleteNormally()) {
            b.addLabel(end_label());
        }
    }

    public ForStmt() {
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 0);
        setChild(new Opt(), 1);
        setChild(new List(), 2);
    }

    public ForStmt(List<Stmt> p0, Opt<Expr> p1, List<Stmt> p2, Stmt p3) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
        setChild(p3, 3);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 4;
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    public void setInitStmtList(List<Stmt> list) {
        setChild(list, 0);
    }

    public int getNumInitStmt() {
        return getInitStmtList().getNumChild();
    }

    public int getNumInitStmtNoTransform() {
        return getInitStmtListNoTransform().getNumChildNoTransform();
    }

    public Stmt getInitStmt(int i) {
        return getInitStmtList().getChild(i);
    }

    public void addInitStmt(Stmt node) {
        List<Stmt> list = (this.parent == null || state == null) ? getInitStmtListNoTransform() : getInitStmtList();
        list.addChild(node);
    }

    public void addInitStmtNoTransform(Stmt node) {
        List<Stmt> list = getInitStmtListNoTransform();
        list.addChild(node);
    }

    public void setInitStmt(Stmt node, int i) {
        List<Stmt> list = getInitStmtList();
        list.setChild(node, i);
    }

    public List<Stmt> getInitStmts() {
        return getInitStmtList();
    }

    public List<Stmt> getInitStmtsNoTransform() {
        return getInitStmtListNoTransform();
    }

    public List<Stmt> getInitStmtList() {
        List<Stmt> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Stmt> getInitStmtListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public void setConditionOpt(Opt<Expr> opt) {
        setChild(opt, 1);
    }

    public boolean hasCondition() {
        return getConditionOpt().getNumChild() != 0;
    }

    public Expr getCondition() {
        return getConditionOpt().getChild(0);
    }

    public void setCondition(Expr node) {
        getConditionOpt().setChild(node, 0);
    }

    public Opt<Expr> getConditionOpt() {
        return (Opt) getChild(1);
    }

    public Opt<Expr> getConditionOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    public void setUpdateStmtList(List<Stmt> list) {
        setChild(list, 2);
    }

    public int getNumUpdateStmt() {
        return getUpdateStmtList().getNumChild();
    }

    public int getNumUpdateStmtNoTransform() {
        return getUpdateStmtListNoTransform().getNumChildNoTransform();
    }

    public Stmt getUpdateStmt(int i) {
        return getUpdateStmtList().getChild(i);
    }

    public void addUpdateStmt(Stmt node) {
        List<Stmt> list = (this.parent == null || state == null) ? getUpdateStmtListNoTransform() : getUpdateStmtList();
        list.addChild(node);
    }

    public void addUpdateStmtNoTransform(Stmt node) {
        List<Stmt> list = getUpdateStmtListNoTransform();
        list.addChild(node);
    }

    public void setUpdateStmt(Stmt node, int i) {
        List<Stmt> list = getUpdateStmtList();
        list.setChild(node, i);
    }

    public List<Stmt> getUpdateStmts() {
        return getUpdateStmtList();
    }

    public List<Stmt> getUpdateStmtsNoTransform() {
        return getUpdateStmtListNoTransform();
    }

    public List<Stmt> getUpdateStmtList() {
        List<Stmt> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<Stmt> getUpdateStmtListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public void setStmt(Stmt node) {
        setChild(node, 3);
    }

    public Stmt getStmt() {
        return (Stmt) getChild(3);
    }

    public Stmt getStmtNoTransform() {
        return (Stmt) getChildNoTransform(3);
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
        return !stmt.hasLabel();
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
        if (hasCondition() && !getCondition().isDAafterFalse(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDAafterInitialization(Variable v) {
        state();
        return getNumInitStmt() == 0 ? isDAbefore(v) : getInitStmt(getNumInitStmt() - 1).isDAafter(v);
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
        if (!isDUbeforeCondition(v)) {
            return false;
        }
        if (hasCondition() && !getCondition().isDUafterFalse(v)) {
            return false;
        }
        for (BreakStmt stmt : targetBreaks()) {
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDUafterInit(Variable v) {
        state();
        return getNumInitStmt() == 0 ? isDUbefore(v) : getInitStmt(getNumInitStmt() - 1).isDUafter(v);
    }

    public boolean isDUbeforeCondition(Variable v) {
        ASTNode.State.CircularValue _value;
        boolean new_isDUbeforeCondition_Variable_value;
        if (this.isDUbeforeCondition_Variable_values == null) {
            this.isDUbeforeCondition_Variable_values = new HashMap(4);
        }
        if (this.isDUbeforeCondition_Variable_values.containsKey(v)) {
            Object _o = this.isDUbeforeCondition_Variable_values.get(v);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.isDUbeforeCondition_Variable_values.put(v, _value);
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
                new_isDUbeforeCondition_Variable_value = isDUbeforeCondition_compute(v);
                if (new_isDUbeforeCondition_Variable_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_isDUbeforeCondition_Variable_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.isDUbeforeCondition_Variable_values.remove(v);
                state.RESET_CYCLE = true;
                isDUbeforeCondition_compute(v);
                state.RESET_CYCLE = false;
            } else {
                this.isDUbeforeCondition_Variable_values.put(v, Boolean.valueOf(new_isDUbeforeCondition_Variable_value));
            }
            state.IN_CIRCLE = false;
            return new_isDUbeforeCondition_Variable_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_isDUbeforeCondition_Variable_value2 = isDUbeforeCondition_compute(v);
            if (state.RESET_CYCLE) {
                this.isDUbeforeCondition_Variable_values.remove(v);
            } else if (new_isDUbeforeCondition_Variable_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_isDUbeforeCondition_Variable_value2);
            }
            return new_isDUbeforeCondition_Variable_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean isDUbeforeCondition_compute(Variable v) {
        if (!isDUafterInit(v) || !isDUafterUpdate(v)) {
            return false;
        }
        return true;
    }

    public boolean isDUafterUpdate(Variable v) {
        state();
        if (!isDUbeforeCondition(v)) {
            return false;
        }
        if (getNumUpdateStmt() > 0) {
            return getUpdateStmt(getNumUpdateStmt() - 1).isDUafter(v);
        }
        if (!getStmt().isDUafter(v)) {
            return false;
        }
        for (ContinueStmt stmt : targetContinues()) {
            if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                return false;
            }
        }
        return true;
    }

    public SimpleSet localLookup(String name) {
        if (this.localLookup_String_values == null) {
            this.localLookup_String_values = new HashMap(4);
        }
        if (this.localLookup_String_values.containsKey(name)) {
            return (SimpleSet) this.localLookup_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet localLookup_String_value = localLookup_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localLookup_String_values.put(name, localLookup_String_value);
        }
        return localLookup_String_value;
    }

    private SimpleSet localLookup_compute(String name) {
        VariableDeclaration v = localVariableDeclaration(name);
        return v != null ? v : lookupVariable(name);
    }

    public VariableDeclaration localVariableDeclaration(String name) {
        if (this.localVariableDeclaration_String_values == null) {
            this.localVariableDeclaration_String_values = new HashMap(4);
        }
        if (this.localVariableDeclaration_String_values.containsKey(name)) {
            return (VariableDeclaration) this.localVariableDeclaration_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        VariableDeclaration localVariableDeclaration_String_value = localVariableDeclaration_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localVariableDeclaration_String_values.put(name, localVariableDeclaration_String_value);
        }
        return localVariableDeclaration_String_value;
    }

    private VariableDeclaration localVariableDeclaration_compute(String name) {
        for (int i = 0; i < getNumInitStmt(); i++) {
            if (getInitStmt(i).declaresVariable(name)) {
                return (VariableDeclaration) getInitStmt(i);
            }
        }
        return null;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean continueLabel() {
        state();
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
        return (reachable() && hasCondition() && !(getCondition().isConstant() && getCondition().isTrue())) || reachableBreak();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return true;
    }

    public soot.jimple.Stmt cond_label() {
        if (this.cond_label_computed) {
            return this.cond_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.cond_label_value = cond_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.cond_label_computed = true;
        }
        return this.cond_label_value;
    }

    private soot.jimple.Stmt cond_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt begin_label() {
        if (this.begin_label_computed) {
            return this.begin_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.begin_label_value = begin_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.begin_label_computed = true;
        }
        return this.begin_label_value;
    }

    private soot.jimple.Stmt begin_label_compute() {
        return newLabel();
    }

    public soot.jimple.Stmt update_label() {
        if (this.update_label_computed) {
            return this.update_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.update_label_value = update_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.update_label_computed = true;
        }
        return this.update_label_value;
    }

    private soot.jimple.Stmt update_label_compute() {
        return newLabel();
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
    public soot.jimple.Stmt continue_label() {
        state();
        return update_label();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        Iterator<Stmt> it = getInitStmtList().iterator();
        while (it.hasNext()) {
            Stmt stmt = it.next();
            if (stmt.modifiedInScope(var)) {
                return true;
            }
        }
        Iterator<Stmt> it2 = getUpdateStmtList().iterator();
        while (it2.hasNext()) {
            Stmt stmt2 = it2.next();
            if (stmt2.modifiedInScope(var)) {
                return true;
            }
        }
        return getStmt().modifiedInScope(var);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.VariableScope
    public SimpleSet lookupVariable(String name) {
        if (this.lookupVariable_String_values == null) {
            this.lookupVariable_String_values = new HashMap(4);
        }
        if (this.lookupVariable_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupVariable_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
        }
        return lookupVariable_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getUpdateStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            if (!getStmt().isDAafter(v)) {
                return false;
            }
            for (ContinueStmt stmt : targetContinues()) {
                if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                    return false;
                }
            }
            return true;
        } else if (caller == getStmtNoTransform()) {
            if (hasCondition() && getCondition().isDAafterTrue(v)) {
                return true;
            }
            if (!hasCondition() && isDAafterInitialization(v)) {
                return true;
            }
            return false;
        } else if (caller == getConditionOptNoTransform()) {
            return isDAafterInitialization(v);
        } else {
            if (caller == getInitStmtListNoTransform()) {
                int i = caller.getIndexOfChild(child);
                return i == 0 ? isDAbefore(v) : getInitStmt(i - 1).isDAafter(v);
            }
            return getParent().Define_boolean_isDAbefore(this, caller, v);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getUpdateStmtListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            if (!isDUbeforeCondition(v)) {
                return false;
            }
            if (i == 0) {
                if (!getStmt().isDUafter(v)) {
                    return false;
                }
                for (ContinueStmt stmt : targetContinues()) {
                    if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                        return false;
                    }
                }
                return true;
            }
            return getUpdateStmt(i - 1).isDUafter(v);
        } else if (caller == getStmtNoTransform()) {
            if (isDUbeforeCondition(v)) {
                return hasCondition() ? getCondition().isDUafterTrue(v) : isDUafterInit(v);
            }
            return false;
        } else if (caller == getConditionOptNoTransform()) {
            return isDUbeforeCondition(v);
        } else {
            if (caller == getInitStmtListNoTransform()) {
                int childIndex = caller.getIndexOfChild(child);
                return childIndex == 0 ? isDUbefore(v) : getInitStmt(childIndex - 1).isDUafter(v);
            }
            return getParent().Define_boolean_isDUbefore(this, caller, v);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getStmtNoTransform()) {
            return localLookup(name);
        }
        if (caller == getUpdateStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            return localLookup(name);
        } else if (caller == getConditionOptNoTransform()) {
            return localLookup(name);
        } else {
            if (caller == getInitStmtListNoTransform()) {
                caller.getIndexOfChild(child);
                return localLookup(name);
            }
            return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return this;
        }
        if (caller == getInitStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_insideLoop(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            if (reachable()) {
                return (hasCondition() && getCondition().isConstant() && getCondition().isFalse()) ? false : true;
            }
            return false;
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtNoTransform()) {
            return reachable();
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionOptNoTransform()) {
            return end_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getConditionOptNoTransform()) {
            return begin_label();
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.BranchTargetStmt, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (!hasCondition()) {
            state().duringDU++;
            ASTNode result = rewriteRule0();
            state().duringDU--;
            return result;
        }
        return super.rewriteTo();
    }

    private ForStmt rewriteRule0() {
        setCondition(new BooleanLiteral("true"));
        return this;
    }
}
