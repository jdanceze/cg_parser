package soot.JastAddJ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Block.class */
public class Block extends Stmt implements Cloneable, VariableScope {
    protected Map checkReturnDA_Variable_values;
    protected Map isDAafter_Variable_values;
    protected Map checkReturnDU_Variable_values;
    protected Map isDUafter_Variable_values;
    protected Map localVariableDeclaration_String_values;
    protected boolean canCompleteNormally_computed = false;
    protected boolean canCompleteNormally_value;
    protected Map lookupType_String_values;
    protected Map lookupVariable_String_values;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.checkReturnDA_Variable_values = null;
        this.isDAafter_Variable_values = null;
        this.checkReturnDU_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.localVariableDeclaration_String_values = null;
        this.canCompleteNormally_computed = false;
        this.lookupType_String_values = null;
        this.lookupVariable_String_values = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public Block clone() throws CloneNotSupportedException {
        Block node = (Block) super.clone();
        node.checkReturnDA_Variable_values = null;
        node.isDAafter_Variable_values = null;
        node.checkReturnDU_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.localVariableDeclaration_String_values = null;
        node.canCompleteNormally_computed = false;
        node.lookupType_String_values = null;
        node.lookupVariable_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            Block node = clone();
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

    public boolean declaredBeforeUse(Variable decl, ASTNode use) {
        int indexDecl = ((ASTNode) decl).varChildIndex(this);
        int indexUse = use.varChildIndex(this);
        return indexDecl <= indexUse;
    }

    public boolean declaredBeforeUse(Variable decl, int indexUse) {
        int indexDecl = ((ASTNode) decl).varChildIndex(this);
        return indexDecl <= indexUse;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        String indent = indent();
        s.append(shouldHaveIndent() ? indent : "");
        s.append("{");
        for (int i = 0; i < getNumStmt(); i++) {
            getStmt(i).toString(s);
        }
        s.append(shouldHaveIndent() ? indent : indent.substring(0, indent.length() - 2));
        s.append("}");
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        for (int i = 0; i < getNumStmt(); i++) {
            getStmt(i).jimplify2(b);
        }
    }

    public Block() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public Block(List<Stmt> p0) {
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

    public void setStmtList(List<Stmt> list) {
        setChild(list, 0);
    }

    public int getNumStmt() {
        return getStmtList().getNumChild();
    }

    public int getNumStmtNoTransform() {
        return getStmtListNoTransform().getNumChildNoTransform();
    }

    public Stmt getStmt(int i) {
        return getStmtList().getChild(i);
    }

    public void addStmt(Stmt node) {
        List<Stmt> list = (this.parent == null || state == null) ? getStmtListNoTransform() : getStmtList();
        list.addChild(node);
    }

    public void addStmtNoTransform(Stmt node) {
        List<Stmt> list = getStmtListNoTransform();
        list.addChild(node);
    }

    public void setStmt(Stmt node, int i) {
        List<Stmt> list = getStmtList();
        list.setChild(node, i);
    }

    public List<Stmt> getStmts() {
        return getStmtList();
    }

    public List<Stmt> getStmtsNoTransform() {
        return getStmtListNoTransform();
    }

    public List<Stmt> getStmtList() {
        List<Stmt> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Stmt> getStmtListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public boolean checkReturnDA(Variable v) {
        if (this.checkReturnDA_Variable_values == null) {
            this.checkReturnDA_Variable_values = new HashMap(4);
        }
        if (this.checkReturnDA_Variable_values.containsKey(v)) {
            return ((Boolean) this.checkReturnDA_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean checkReturnDA_Variable_value = checkReturnDA_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.checkReturnDA_Variable_values.put(v, Boolean.valueOf(checkReturnDA_Variable_value));
        }
        return checkReturnDA_Variable_value;
    }

    private boolean checkReturnDA_compute(Variable v) {
        HashSet set = new HashSet();
        collectBranches(set);
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof ReturnStmt) {
                ReturnStmt stmt = (ReturnStmt) o;
                if (!stmt.isDAafterReachedFinallyBlocks(v)) {
                    return false;
                }
            }
        }
        return true;
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
        return getNumStmt() == 0 ? isDAbefore(v) : getStmt(getNumStmt() - 1).isDAafter(v);
    }

    public boolean isDUeverywhere(Variable v) {
        state();
        return isDUbefore(v) && checkDUeverywhere(v);
    }

    public boolean checkReturnDU(Variable v) {
        if (this.checkReturnDU_Variable_values == null) {
            this.checkReturnDU_Variable_values = new HashMap(4);
        }
        if (this.checkReturnDU_Variable_values.containsKey(v)) {
            return ((Boolean) this.checkReturnDU_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean checkReturnDU_Variable_value = checkReturnDU_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.checkReturnDU_Variable_values.put(v, Boolean.valueOf(checkReturnDU_Variable_value));
        }
        return checkReturnDU_Variable_value;
    }

    private boolean checkReturnDU_compute(Variable v) {
        HashSet set = new HashSet();
        collectBranches(set);
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof ReturnStmt) {
                ReturnStmt stmt = (ReturnStmt) o;
                if (!stmt.isDUafterReachedFinallyBlocks(v)) {
                    return false;
                }
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
        return getNumStmt() == 0 ? isDUbefore(v) : getStmt(getNumStmt() - 1).isDUafter(v);
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
        for (int i = 0; i < getNumStmt(); i++) {
            if (getStmt(i).declaresVariable(name)) {
                return (VariableDeclaration) getStmt(i);
            }
        }
        return null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean addsIndentationLevel() {
        state();
        return shouldHaveIndent();
    }

    public boolean shouldHaveIndent() {
        state();
        return (getParent() instanceof List) && (getParent().getParent() instanceof Block);
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
        return getNumStmt() == 0 ? reachable() : getStmt(getNumStmt() - 1).canCompleteNormally();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        Iterator<Stmt> it = getStmtList().iterator();
        while (it.hasNext()) {
            Stmt stmt = it.next();
            if (stmt.modifiedInScope(var)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.Stmt
    public SimpleSet lookupType(String name) {
        if (this.lookupType_String_values == null) {
            this.lookupType_String_values = new HashMap(4);
        }
        if (this.lookupType_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupType_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupType_String_values.put(name, lookupType_String_value);
        }
        return lookupType_String_value;
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

    @Override // soot.JastAddJ.Stmt
    public boolean reachable() {
        state();
        boolean reachable_value = getParent().Define_boolean_reachable(this, null);
        return reachable_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        if (caller == getStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isIncOrDec(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            return index == 0 ? isDAbefore(v) : getStmt(index - 1).isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getStmtListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            return index == 0 ? isDUbefore(v) : getStmt(index - 1).isDUafter(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getStmtListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            SimpleSet c = SimpleSet.emptySet;
            for (int i = index; i >= 0 && !(getStmt(i) instanceof Case); i--) {
                if (getStmt(i) instanceof LocalClassDeclStmt) {
                    TypeDecl t = ((LocalClassDeclStmt) getStmt(i)).getClassDecl();
                    if (t.name().equals(name)) {
                        c = c.add(t);
                    }
                }
            }
            if (!c.isEmpty()) {
                return c;
            }
            return lookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getStmtListNoTransform()) {
            int index = caller.getIndexOfChild(child);
            VariableDeclaration v = localVariableDeclaration(name);
            if (v != null && declaredBeforeUse(v, index)) {
                return v;
            }
            return lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        if (caller == getStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            return this;
        }
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getStmtListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtListNoTransform()) {
            int childIndex = caller.getIndexOfChild(child);
            return childIndex == 0 ? reachable() : getStmt(childIndex - 1).canCompleteNormally();
        }
        return getParent().Define_boolean_reachable(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        if (caller == getStmtListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return i == 0 ? reachable() : getStmt(i - 1).reachable();
        }
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
