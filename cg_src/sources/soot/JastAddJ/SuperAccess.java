package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SuperAccess.class */
public class SuperAccess extends Access implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected TypeDecl decl_value;
    protected TypeDecl type_value;
    protected boolean decl_computed = false;
    protected boolean type_computed = false;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decl_computed = false;
        this.decl_value = null;
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public SuperAccess clone() throws CloneNotSupportedException {
        SuperAccess node = (SuperAccess) super.clone();
        node.decl_computed = false;
        node.decl_value = null;
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            SuperAccess node = clone();
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
        s.append("super");
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (isQualified()) {
            if (!hostType().isInnerTypeOf(decl()) && hostType() != decl()) {
                error("qualified super must name an enclosing type");
            }
            if (inStaticContext()) {
                error("*** Qualified super may not occur in static context");
            }
        }
        if (inExplicitConstructorInvocation() && hostType().instanceOf(decl().hostType())) {
            error("super may not be accessed in an explicit constructor invocation");
        }
        if (inStaticContext()) {
            error("super may not be accessed in a static context");
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        return emitThis(b, decl());
    }

    public SuperAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public SuperAccess(String p0) {
        setID(p0);
    }

    public SuperAccess(Symbol p0) {
        setID(p0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setID(String value) {
        this.tokenString_ID = value;
    }

    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    private TypeDecl refined_TypeScopePropagation_SuperAccess_decl() {
        return isQualified() ? qualifier().type() : hostType();
    }

    public SimpleSet decls() {
        state();
        return SimpleSet.emptySet;
    }

    public TypeDecl decl() {
        if (this.decl_computed) {
            return this.decl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decl_value = decl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decl_computed = true;
        }
        return this.decl_value;
    }

    private TypeDecl decl_compute() {
        TypeDecl typeDecl = refined_TypeScopePropagation_SuperAccess_decl();
        if (typeDecl instanceof ParTypeDecl) {
            typeDecl = ((ParTypeDecl) typeDecl).genericDecl();
        }
        return typeDecl;
    }

    @Override // soot.JastAddJ.Expr
    public boolean isSuperAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.TYPE_NAME;
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
        TypeDecl typeDecl = decl();
        if (!typeDecl.isClassDecl()) {
            return unknownType();
        }
        ClassDecl classDecl = (ClassDecl) typeDecl;
        if (!classDecl.hasSuperclass()) {
            return unknownType();
        }
        return classDecl.superclass();
    }

    @Override // soot.JastAddJ.Access
    public boolean inExplicitConstructorInvocation() {
        state();
        boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
        return inExplicitConstructorInvocation_value;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
