package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ConstructorAccess.class */
public class ConstructorAccess extends Access implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected SimpleSet decls_value;
    protected ConstructorDecl decl_value;
    protected TypeDecl type_value;
    protected boolean addEnclosingVariables = true;
    protected boolean decls_computed = false;
    protected boolean decl_computed = false;
    protected boolean type_computed = false;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decls_computed = false;
        this.decls_value = null;
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
    public ConstructorAccess clone() throws CloneNotSupportedException {
        ConstructorAccess node = (ConstructorAccess) super.clone();
        node.decls_computed = false;
        node.decls_value = null;
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
            ConstructorAccess node = clone();
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
    public void exceptionHandling() {
        for (int i = 0; i < decl().getNumException(); i++) {
            TypeDecl exceptionType = decl().getException(i).type();
            if (!handlesException(exceptionType)) {
                error(this + " may throw uncaught exception " + exceptionType.fullName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl catchType) {
        for (int i = 0; i < decl().getNumException(); i++) {
            TypeDecl exceptionType = decl().getException(i).type();
            if (catchType.mayCatch(exceptionType)) {
                return true;
            }
        }
        return super.reachedException(catchType);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        if (decls().isEmpty()) {
            error("no constructor named " + this);
        }
        if (decls().size() > 1 && validArgs()) {
            error("several most specific constructors for " + this);
            Iterator iter = decls().iterator();
            while (iter.hasNext()) {
                error("         " + ((ConstructorDecl) iter.next()).signature());
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(name());
        s.append("(");
        if (getNumArg() > 0) {
            getArg(0).toString(s);
            for (int i = 1; i < getNumArg(); i++) {
                s.append(", ");
                getArg(i).toString(s);
            }
        }
        s.append(")");
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (decl().isDeprecated() && !withinDeprecatedAnnotation() && hostType().topLevelType() != decl().hostType().topLevelType() && !withinSuppressWarnings("deprecation")) {
            warning(String.valueOf(decl().signature()) + " in " + decl().hostType().typeName() + " has been deprecated");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void transformEnumConstructors() {
        super.transformEnumConstructors();
        getArgList().insertChild(new VarAccess("@p0"), 0);
        getArgList().insertChild(new VarAccess("@p1"), 1);
    }

    public void addEnclosingVariables() {
        if (this.addEnclosingVariables) {
            this.addEnclosingVariables = false;
            decl().addEnclosingVariables();
            for (Variable v : decl().hostType().enclosingVariables()) {
                getArgList().add(new VarAccess("val$" + v.name()));
            }
        }
    }

    public void refined_Transformations_ConstructorAccess_transformation() {
        addEnclosingVariables();
        if (decl().isPrivate() && decl().hostType() != hostType()) {
            decl().createAccessor();
        }
        super.transformation();
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectTypesToSignatures(Collection<Type> set) {
        super.collectTypesToSignatures(set);
        addDependencyIfNeeded(set, decl().erasedConstructor().hostType());
    }

    public ConstructorAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public ConstructorAccess(String p0, List<Expr> p1) {
        setID(p0);
        setChild(p1, 0);
    }

    public ConstructorAccess(Symbol p0, List<Expr> p1) {
        setID(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
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

    public void setArgList(List<Expr> list) {
        setChild(list, 0);
    }

    public int getNumArg() {
        return getArgList().getNumChild();
    }

    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    public List<Expr> getArgs() {
        return getArgList();
    }

    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        if (decl().isVariableArity() && !invokesVariableArityAsArray()) {
            List list = new List();
            for (int i = 0; i < decl().getNumParameter() - 1; i++) {
                list.add(getArg(i).fullCopy());
            }
            List last = new List();
            for (int i2 = decl().getNumParameter() - 1; i2 < getNumArg(); i2++) {
                last.add(getArg(i2).fullCopy());
            }
            Access typeAccess = decl().lastParameter().type().elementType().createQualifiedAccess();
            for (int i3 = 0; i3 < decl().lastParameter().type().dimension(); i3++) {
                typeAccess = new ArrayTypeAccess(typeAccess);
            }
            list.add(new ArrayCreationExpr(typeAccess, new Opt(new ArrayInit(last))));
            setArgList(list);
        }
        refined_Transformations_ConstructorAccess_transformation();
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        b.setLine(this);
        ConstructorDecl c = decl().erasedConstructor();
        Local base = b.emitThis(hostType());
        int index = 0;
        ArrayList list = new ArrayList();
        if (c.needsEnclosing()) {
            index = 0 + 1;
            list.add(asImmediate(b, b.newParameterRef(hostType().enclosingType().getSootType(), 0, this)));
        }
        if (c.needsSuperEnclosing()) {
            TypeDecl superClass = ((ClassDecl) hostType()).superclass();
            int i = index;
            int i2 = index + 1;
            list.add(asImmediate(b, b.newParameterRef(superClass.enclosingType().getSootType(), i, this)));
        }
        for (int i3 = 0; i3 < getNumArg(); i3++) {
            list.add(asImmediate(b, getArg(i3).type().emitCastTo(b, getArg(i3), c.getParameter(i3).type())));
        }
        if (decl().isPrivate() && decl().hostType() != hostType()) {
            list.add(asImmediate(b, NullConstant.v()));
            b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(base, decl().erasedConstructor().createAccessor().sootRef(), list, this), this));
            return base;
        }
        return b.newSpecialInvokeExpr(base, c.sootRef(), list, this);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return decl().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return decl().isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean applicableAndAccessible(ConstructorDecl decl) {
        state();
        return decl.applicable(getArgList()) && decl.accessibleFrom(hostType());
    }

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
        return chooseConstructor(lookupConstructor(), getArgList());
    }

    public ConstructorDecl decl() {
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

    private ConstructorDecl decl_compute() {
        SimpleSet decls = decls();
        if (decls.size() == 1) {
            return (ConstructorDecl) decls.iterator().next();
        }
        return unknownConstructor();
    }

    public boolean validArgs() {
        state();
        for (int i = 0; i < getNumArg(); i++) {
            if (getArg(i).type().isUnknown()) {
                return false;
            }
        }
        return true;
    }

    public String name() {
        state();
        return "this";
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.AMBIGUOUS_NAME;
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
        return decl().type();
    }

    public int arity() {
        state();
        return getNumArg();
    }

    public boolean invokesVariableArityAsArray() {
        state();
        if (!decl().isVariableArity() || arity() != decl().arity()) {
            return false;
        }
        return getArg(getNumArg() - 1).type().methodInvocationConversionTo(decl().lastParameter().type());
    }

    public boolean handlesException(TypeDecl exceptionType) {
        state();
        boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
        return handlesException_TypeDecl_value;
    }

    public Collection lookupConstructor() {
        state();
        Collection lookupConstructor_value = getParent().Define_Collection_lookupConstructor(this, null);
        return lookupConstructor_value;
    }

    public ConstructorDecl unknownConstructor() {
        state();
        ConstructorDecl unknownConstructor_value = getParent().Define_ConstructorDecl_unknownConstructor(this, null);
        return unknownConstructor_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupMethod(name);
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().hasPackage(packageName);
        }
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.EXPRESSION_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unqualifiedScope().methodHost();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return true;
        }
        return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
