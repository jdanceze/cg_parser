package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Access.class */
public abstract class Access extends Expr implements Cloneable {
    protected Expr prevExpr_value;
    protected boolean hasPrevExpr_value;
    protected TypeDecl type_value;
    protected boolean prevExpr_computed = false;
    protected boolean hasPrevExpr_computed = false;
    protected boolean type_computed = false;

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.prevExpr_computed = false;
        this.prevExpr_value = null;
        this.hasPrevExpr_computed = false;
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public Access clone() throws CloneNotSupportedException {
        Access node = (Access) super.clone();
        node.prevExpr_computed = false;
        node.prevExpr_value = null;
        node.hasPrevExpr_computed = false;
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public Access addArrayDims(List list) {
        Access arrayTypeAccess;
        Access a = this;
        for (int i = 0; i < list.getNumChildNoTransform(); i++) {
            Dims dims = (Dims) list.getChildNoTransform(i);
            Opt opt = dims.getExprOpt();
            if (opt.getNumChildNoTransform() == 1) {
                arrayTypeAccess = new ArrayTypeWithSizeAccess(a, opt.getChildNoTransform(0));
            } else {
                arrayTypeAccess = new ArrayTypeAccess(a);
            }
            a = arrayTypeAccess;
            a.setStart(dims.start());
            a.setEnd(dims.end());
        }
        return a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TypeDecl superConstructorQualifier(TypeDecl targetEnclosingType) {
        TypeDecl hostType = hostType();
        while (true) {
            TypeDecl enclosing = hostType;
            if (!enclosing.instanceOf(targetEnclosingType)) {
                hostType = enclosing.enclosingType();
            } else {
                return enclosing;
            }
        }
    }

    public Value emitLoadLocalInNestedClass(Body b, Variable v) {
        if (inExplicitConstructorInvocation() && (enclosingBodyDecl() instanceof ConstructorDecl)) {
            ConstructorDecl c = (ConstructorDecl) enclosingBodyDecl();
            return ((ParameterDeclaration) c.parameterDeclaration(v.name()).iterator().next()).local;
        }
        return b.newInstanceFieldRef(b.emitThis(hostType()), Scene.v().makeFieldRef(hostType().getSootClassDecl(), "val$" + v.name(), v.type().getSootType(), false), this);
    }

    public Local emitThis(Body b, TypeDecl targetDecl) {
        Local base;
        b.setLine(this);
        if (targetDecl == hostType()) {
            return b.emitThis(hostType());
        }
        TypeDecl enclosing = hostType();
        if (inExplicitConstructorInvocation()) {
            base = asLocal(b, b.newParameterRef(enclosing.enclosingType().getSootType(), 0, this));
            enclosing = enclosing.enclosing();
        } else {
            base = b.emitThis(hostType());
        }
        while (enclosing != targetDecl) {
            Local next = b.newTemp(enclosing.enclosingType().getSootType());
            b.add(b.newAssignStmt(next, b.newInstanceFieldRef(base, enclosing.getSootField("this$0", enclosing.enclosingType()).makeRef(), this), this));
            base = next;
            enclosing = enclosing.enclosingType();
        }
        return base;
    }

    public void addArraySize(Body b, ArrayList list) {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public Expr unqualifiedScope() {
        state();
        return isQualified() ? nestedScope() : this;
    }

    public boolean isQualified() {
        state();
        return hasPrevExpr();
    }

    public Expr qualifier() {
        state();
        return prevExpr();
    }

    public Access lastAccess() {
        state();
        return this;
    }

    public Expr prevExpr() {
        if (this.prevExpr_computed) {
            return this.prevExpr_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.prevExpr_value = prevExpr_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.prevExpr_computed = true;
        }
        return this.prevExpr_value;
    }

    private Expr prevExpr_compute() {
        if (isLeftChildOfDot()) {
            if (parentDot().isRightChildOfDot()) {
                return parentDot().parentDot().leftSide();
            }
        } else if (isRightChildOfDot()) {
            return parentDot().leftSide();
        }
        throw new Error(this + " does not have a previous expression");
    }

    public boolean hasPrevExpr() {
        if (this.hasPrevExpr_computed) {
            return this.hasPrevExpr_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.hasPrevExpr_value = hasPrevExpr_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.hasPrevExpr_computed = true;
        }
        return this.hasPrevExpr_value;
    }

    private boolean hasPrevExpr_compute() {
        if (isLeftChildOfDot()) {
            if (parentDot().isRightChildOfDot()) {
                return true;
            }
            return false;
        } else if (isRightChildOfDot()) {
            return true;
        } else {
            return false;
        }
    }

    public NameType predNameType() {
        state();
        return NameType.NO_NAME;
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
        return unknownType();
    }

    public boolean isDiamond() {
        state();
        return false;
    }

    public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
        state();
        return (Access) cloneSubtree();
    }

    public Expr nestedScope() {
        state();
        Expr nestedScope_value = getParent().Define_Expr_nestedScope(this, null);
        return nestedScope_value;
    }

    @Override // soot.JastAddJ.Expr
    public TypeDecl unknownType() {
        state();
        TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        return unknownType_value;
    }

    public Variable unknownField() {
        state();
        Variable unknownField_value = getParent().Define_Variable_unknownField(this, null);
        return unknownField_value;
    }

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    public boolean withinDeprecatedAnnotation() {
        state();
        boolean withinDeprecatedAnnotation_value = getParent().Define_boolean_withinDeprecatedAnnotation(this, null);
        return withinDeprecatedAnnotation_value;
    }

    public boolean inExplicitConstructorInvocation() {
        state();
        boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
        return inExplicitConstructorInvocation_value;
    }

    @Override // soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
