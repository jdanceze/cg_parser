package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SuperConstructorAccess.class */
public class SuperConstructorAccess extends ConstructorAccess implements Cloneable {
    protected boolean decls_computed = false;
    protected SimpleSet decls_value;

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decls_computed = false;
        this.decls_value = null;
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public SuperConstructorAccess clone() throws CloneNotSupportedException {
        SuperConstructorAccess node = (SuperConstructorAccess) super.clone();
        node.decls_computed = false;
        node.decls_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            SuperConstructorAccess node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        TypeDecl c = hostType();
        TypeDecl s = (c.isClassDecl() && ((ClassDecl) c).hasSuperclass()) ? ((ClassDecl) c).superclass() : unknownType();
        if (isQualified()) {
            if (!s.isInnerType() || s.inStaticContext()) {
                error("the super type " + s.typeName() + " of " + c.typeName() + " is not an inner class");
            } else if (!qualifier().type().instanceOf(s.enclosingType())) {
                error("The type of this primary expression, " + qualifier().type().typeName() + " is not enclosing the super type, " + s.typeName() + ", of " + c.typeName());
            }
        }
        if (!isQualified() && s.isInnerType() && !c.isInnerType()) {
            error("no enclosing instance for " + s.typeName() + " when accessed in " + this);
        }
        if (s.isInnerType() && hostType().instanceOf(s.enclosingType())) {
            error("cannot reference this before supertype constructor has been called");
        }
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public void transformation() {
        addEnclosingVariables();
        if (decl().isPrivate() && decl().hostType() != hostType()) {
            decl().createAccessor();
        }
        super.transformation();
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public void collectTypesToSignatures(Collection<Type> set) {
        super.collectTypesToSignatures(set);
        addDependencyIfNeeded(set, decl().erasedConstructor().hostType());
    }

    public SuperConstructorAccess() {
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public SuperConstructorAccess(String p0, List<Expr> p1) {
        setID(p0);
        setChild(p1, 0);
    }

    public SuperConstructorAccess(Symbol p0, List<Expr> p1) {
        setID(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void setArgList(List<Expr> list) {
        setChild(list, 0);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public int getNumArg() {
        return getArgList().getNumChild();
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public List<Expr> getArgs() {
        return getArgList();
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Expr
    public Value eval(Body b) {
        ConstructorDecl c = decl().erasedConstructor();
        Local base = b.emitThis(hostType());
        ArrayList list = new ArrayList();
        if (c.needsEnclosing()) {
            if (hasPrevExpr() && !prevExpr().isTypeAccess()) {
                list.add(asImmediate(b, prevExpr().eval(b)));
            } else if (hostType().needsSuperEnclosing()) {
                Type type = ((ClassDecl) hostType()).superclass().enclosingType().getSootType();
                if (hostType().needsEnclosing()) {
                    list.add(asImmediate(b, b.newParameterRef(type, 1, this)));
                } else {
                    list.add(asImmediate(b, b.newParameterRef(type, 0, this)));
                }
            } else {
                list.add(emitThis(b, superConstructorQualifier(c.hostType().enclosingType())));
            }
        }
        for (int i = 0; i < getNumArg(); i++) {
            list.add(asImmediate(b, getArg(i).type().emitCastTo(b, getArg(i), c.getParameter(i).type())));
        }
        if (decl().isPrivate() && decl().hostType() != hostType()) {
            list.add(asImmediate(b, NullConstant.v()));
            b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(base, decl().erasedConstructor().createAccessor().sootRef(), list, this), this));
            return base;
        }
        return b.newSpecialInvokeExpr(base, c.sootRef(), list, this);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return isDAbefore(v);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return isDUbefore(v);
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public SimpleSet decls() {
        if (this.decls_computed) {
            return this.decls_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decls_value = decls_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decls_computed = true;
        }
        return this.decls_value;
    }

    private SimpleSet decls_compute() {
        Collection c = (!hasPrevExpr() || prevExpr().isTypeAccess()) ? lookupSuperConstructor() : hostType().lookupSuperConstructor();
        return chooseConstructor(c, getArgList());
    }

    @Override // soot.JastAddJ.ConstructorAccess
    public String name() {
        state();
        return "super";
    }

    @Override // soot.JastAddJ.Expr
    public boolean isSuperConstructorAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.EXPRESSION_NAME;
    }

    public Collection lookupSuperConstructor() {
        state();
        Collection lookupSuperConstructor_value = getParent().Define_Collection_lookupSuperConstructor(this, null);
        return lookupSuperConstructor_value;
    }

    public TypeDecl enclosingInstance() {
        state();
        TypeDecl enclosingInstance_value = getParent().Define_TypeDecl_enclosingInstance(this, null);
        return enclosingInstance_value;
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().hasPackage(packageName);
        }
        return super.Define_boolean_hasPackage(caller, child, packageName);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupVariable(name);
        }
        return super.Define_SimpleSet_lookupVariable(caller, child, name);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return super.Define_boolean_inExplicitConstructorInvocation(caller, child);
    }

    @Override // soot.JastAddJ.ConstructorAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
