package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AbstractDot.class */
public class AbstractDot extends Access implements Cloneable {
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean type_computed = false;
    protected TypeDecl type_value;
    protected Map isDUbefore_Variable_values;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.type_computed = false;
        this.type_value = null;
        this.isDUbefore_Variable_values = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public AbstractDot clone() throws CloneNotSupportedException {
        AbstractDot node = (AbstractDot) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.type_computed = false;
        node.type_value = null;
        node.isDUbefore_Variable_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            AbstractDot node = clone();
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
        getLeft().toString(s);
        if (!nextAccess().isArrayAccess()) {
            s.append(".");
        }
        getRight().toString(s);
    }

    public Access extractLast() {
        return getRightNoTransform();
    }

    public void replaceLast(Access access) {
        setRight(access);
    }

    @Override // soot.JastAddJ.Expr
    public void emitEvalBranch(Body b) {
        lastAccess().emitEvalBranch(b);
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return lastAccess().eval(b);
    }

    @Override // soot.JastAddJ.Expr
    public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
        return lastAccess().emitStore(b, lvalue, rvalue, location);
    }

    public AbstractDot() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public AbstractDot(Expr p0, Access p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setLeft(Expr node) {
        setChild(node, 0);
    }

    public Expr getLeft() {
        return (Expr) getChild(0);
    }

    public Expr getLeftNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void setRight(Access node) {
        setChild(node, 1);
    }

    public Access getRight() {
        return (Access) getChild(1);
    }

    public Access getRightNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return lastAccess().constant();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        state();
        return lastAccess().isConstant();
    }

    @Override // soot.JastAddJ.Expr
    public Variable varDecl() {
        state();
        return lastAccess().varDecl();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterTrue(Variable v) {
        state();
        return isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafterFalse(Variable v) {
        state();
        return isDAafter(v);
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
        return lastAccess().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterTrue(Variable v) {
        state();
        return isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafterFalse(Variable v) {
        state();
        return isDUafter(v);
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
        return lastAccess().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public String typeName() {
        state();
        return lastAccess().typeName();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isTypeAccess() {
        state();
        return getRight().isTypeAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isMethodAccess() {
        state();
        return getRight().isMethodAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isFieldAccess() {
        state();
        return getRight().isFieldAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isSuperAccess() {
        state();
        return getRight().isSuperAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isThisAccess() {
        state();
        return getRight().isThisAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isPackageAccess() {
        state();
        return getRight().isPackageAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isArrayAccess() {
        state();
        return getRight().isArrayAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isClassAccess() {
        state();
        return getRight().isClassAccess();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isSuperConstructorAccess() {
        state();
        return getRight().isSuperConstructorAccess();
    }

    @Override // soot.JastAddJ.Access
    public boolean isQualified() {
        state();
        return hasParentDot();
    }

    public Expr leftSide() {
        state();
        return getLeft();
    }

    public Access rightSide() {
        state();
        return getRight() instanceof AbstractDot ? (Access) ((AbstractDot) getRight()).getLeft() : getRight();
    }

    @Override // soot.JastAddJ.Access
    public Access lastAccess() {
        state();
        return getRight().lastAccess();
    }

    @Override // soot.JastAddJ.Expr
    public Access nextAccess() {
        state();
        return rightSide();
    }

    @Override // soot.JastAddJ.Access
    public Expr prevExpr() {
        state();
        return leftSide();
    }

    @Override // soot.JastAddJ.Access
    public boolean hasPrevExpr() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return getLeft() instanceof Access ? ((Access) getLeft()).predNameType() : NameType.NO_NAME;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr
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
        return lastAccess().type();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isVariable() {
        state();
        return lastAccess().isVariable();
    }

    @Override // soot.JastAddJ.Expr
    public boolean staticContextQualifier() {
        state();
        return lastAccess().staticContextQualifier();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return getParent().definesLabel();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeTrue() {
        state();
        return lastAccess().canBeTrue();
    }

    @Override // soot.JastAddJ.Expr
    public boolean canBeFalse() {
        state();
        return lastAccess().canBeFalse();
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
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getLeftNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getLeftNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightNoTransform()) {
            return getLeft().isDAafter(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getRightNoTransform()) {
            return getLeft().isDUafter(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
        if (caller == getRightNoTransform()) {
            return getLeft().type().constructors();
        }
        return getParent().Define_Collection_lookupConstructor(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
        if (caller == getRightNoTransform()) {
            return getLeft().type().lookupSuperConstructor();
        }
        return getParent().Define_Collection_lookupSuperConstructor(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
        if (caller == getLeftNoTransform()) {
            return isQualified() ? nestedScope() : this;
        } else if (caller == getRightNoTransform()) {
            return isQualified() ? nestedScope() : this;
        } else {
            return getParent().Define_Expr_nestedScope(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getRightNoTransform()) {
            return getLeft().type().memberMethods(name);
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if (caller == getRightNoTransform()) {
            return getLeft().hasQualifiedPackage(packageName);
        }
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getRightNoTransform()) {
            return getLeft().qualifiedLookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getRightNoTransform()) {
            return getLeft().qualifiedLookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getLeftNoTransform()) {
            return getRight().predNameType();
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        if (caller == getRightNoTransform()) {
            return getLeft().type();
        }
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        if (caller == getRightNoTransform()) {
            return getLeft().type().typeName();
        }
        return getParent().Define_String_methodHost(this, caller);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
