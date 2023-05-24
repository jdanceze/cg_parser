package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Case.class */
public abstract class Case extends Stmt implements Cloneable {
    protected Map isDAbefore_Variable_values;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean label_computed = false;
    protected soot.jimple.Stmt label_value;
    protected Map bind_Case_values;

    public abstract boolean constValue(Case r1);

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAbefore_Variable_values = null;
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.label_computed = false;
        this.label_value = null;
        this.bind_Case_values = null;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public Case clone() throws CloneNotSupportedException {
        Case node = (Case) super.clone();
        node.isDAbefore_Variable_values = null;
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.label_computed = false;
        node.label_value = null;
        node.bind_Case_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.addLabel(label());
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDAbefore(Variable v) {
        if (this.isDAbefore_Variable_values == null) {
            this.isDAbefore_Variable_values = new HashMap(4);
        }
        if (this.isDAbefore_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAbefore_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAbefore_Variable_value = isDAbefore_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, Boolean.valueOf(isDAbefore_Variable_value));
        }
        return isDAbefore_Variable_value;
    }

    private boolean isDAbefore_compute(Variable v) {
        return (getParent().getParent() instanceof Block) && ((Block) getParent().getParent()).isDAbefore(v) && super.isDAbefore(v);
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
        return isDAbefore(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean isDUbefore(Variable v) {
        state();
        return (getParent().getParent() instanceof Block) && ((Block) getParent().getParent()).isDUbefore(v) && super.isDUbefore(v);
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
        return isDUbefore(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean reachable() {
        state();
        return (getParent().getParent() instanceof Block) && ((Block) getParent().getParent()).reachable();
    }

    public soot.jimple.Stmt label() {
        if (this.label_computed) {
            return this.label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.label_value = label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.label_computed = true;
        }
        return this.label_value;
    }

    private soot.jimple.Stmt label_compute() {
        return newLabel();
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    public boolean isDefaultCase() {
        state();
        return false;
    }

    public Case bind(Case c) {
        if (this.bind_Case_values == null) {
            this.bind_Case_values = new HashMap(4);
        }
        if (this.bind_Case_values.containsKey(c)) {
            return (Case) this.bind_Case_values.get(c);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Case bind_Case_value = getParent().Define_Case_bind(this, null, c);
        if (isFinal && num == state().boundariesCrossed) {
            this.bind_Case_values.put(c, bind_Case_value);
        }
        return bind_Case_value;
    }

    public TypeDecl switchType() {
        state();
        TypeDecl switchType_value = getParent().Define_TypeDecl_switchType(this, null);
        return switchType_value;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
