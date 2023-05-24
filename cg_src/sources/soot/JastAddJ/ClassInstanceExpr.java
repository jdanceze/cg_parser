package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ClassInstanceExpr.class */
public class ClassInstanceExpr extends Access implements Cloneable {
    protected boolean addEnclosingVariables;
    protected Map isDAafterInstance_Variable_values;
    protected Map computeDAbefore_int_Variable_values;
    protected Map computeDUbefore_int_Variable_values;
    protected boolean decls_computed;
    protected SimpleSet decls_value;
    protected boolean decl_computed;
    protected ConstructorDecl decl_value;
    protected Map localLookupType_String_values;
    protected boolean type_computed;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafterInstance_Variable_values = null;
        this.computeDAbefore_int_Variable_values = null;
        this.computeDUbefore_int_Variable_values = null;
        this.decls_computed = false;
        this.decls_value = null;
        this.decl_computed = false;
        this.decl_value = null;
        this.localLookupType_String_values = null;
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ClassInstanceExpr clone() throws CloneNotSupportedException {
        ClassInstanceExpr node = (ClassInstanceExpr) super.clone();
        node.isDAafterInstance_Variable_values = null;
        node.computeDAbefore_int_Variable_values = null;
        node.computeDUbefore_int_Variable_values = null;
        node.decls_computed = false;
        node.decls_value = null;
        node.decl_computed = false;
        node.decl_value = null;
        node.localLookupType_String_values = null;
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
            ClassInstanceExpr node = clone();
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
    public void accessControl() {
        super.accessControl();
        if (type().isAbstract()) {
            error("Can not instantiate abstract class " + type().fullName());
        }
        if (!decl().accessibleFrom(hostType())) {
            error("constructor " + decl().signature() + " is not accessible");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void exceptionHandling() {
        Iterator<Access> it = decl().getExceptionList().iterator();
        while (it.hasNext()) {
            Access exception = it.next();
            TypeDecl exceptionType = exception.type();
            if (!handlesException(exceptionType)) {
                error(this + " may throw uncaught exception " + exceptionType.fullName() + "; it must be caught or declared as being thrown");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean reachedException(TypeDecl catchType) {
        ConstructorDecl decl = decl();
        for (int i = 0; i < decl.getNumException(); i++) {
            TypeDecl exceptionType = decl.getException(i).type();
            if (catchType.mayCatch(exceptionType)) {
                return true;
            }
        }
        for (int i2 = 0; i2 < getNumArg(); i2++) {
            if (getArg(i2).reachedException(catchType)) {
                return true;
            }
        }
        return false;
    }

    public SimpleSet keepInnerClasses(SimpleSet c) {
        SimpleSet newSet = SimpleSet.emptySet;
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            TypeDecl t = (TypeDecl) iter.next();
            if (t.isInnerType() && t.isClassDecl()) {
                newSet = newSet.add(c);
            }
        }
        return newSet;
    }

    public void refined_NameCheck_ClassInstanceExpr_nameCheck() {
        super.nameCheck();
        if (decls().isEmpty()) {
            error("can not instantiate " + type().typeName() + " no matching constructor found in " + type().typeName());
        } else if (decls().size() > 1 && validArgs()) {
            error("several most specific constructors found");
            Iterator iter = decls().iterator();
            while (iter.hasNext()) {
                error("         " + ((ConstructorDecl) iter.next()).signature());
            }
        }
    }

    public ClassInstanceExpr(Access type, List args) {
        this(type, args, new Opt());
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("new ");
        getAccess().toString(s);
        s.append("(");
        if (getNumArg() > 0) {
            getArg(0).toString(s);
            for (int i = 1; i < getNumArg(); i++) {
                s.append(", ");
                getArg(i).toString(s);
            }
        }
        s.append(")");
        if (hasTypeDecl()) {
            TypeDecl decl = getTypeDecl();
            s.append(" {");
            for (int i2 = 0; i2 < decl.getNumBodyDecl(); i2++) {
                if (!(decl.getBodyDecl(i2) instanceof ConstructorDecl)) {
                    decl.getBodyDecl(i2).toString(s);
                }
            }
            s.append(typeDeclIndent());
            s.append("}");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (isQualified() && qualifier().isTypeAccess() && !qualifier().type().isUnknown()) {
            error("*** The expression in a qualified class instance expr must not be a type name");
        }
        if (isQualified() && !type().isInnerClass() && !((ClassDecl) type()).superclass().isInnerClass() && !type().isUnknown()) {
            error("*** Qualified class instance creation can only instantiate inner classes and their anonymous subclasses");
        }
        if (!type().isClassDecl()) {
            error("*** Can only instantiate classes, which " + type().typeName() + " is not");
        }
        typeCheckEnclosingInstance();
        typeCheckAnonymousSuperclassEnclosingInstance();
    }

    public void typeCheckEnclosingInstance() {
        TypeDecl nest;
        TypeDecl nest2;
        TypeDecl C = type();
        if (!C.isInnerClass()) {
            return;
        }
        TypeDecl enclosing = null;
        if (C.isAnonymous()) {
            if (noEnclosingInstance()) {
                enclosing = null;
            } else {
                enclosing = hostType();
            }
        } else if (C.isLocalClass()) {
            if (C.inStaticContext()) {
                enclosing = null;
            } else if (noEnclosingInstance()) {
                enclosing = unknownType();
            } else {
                TypeDecl hostType = hostType();
                while (true) {
                    nest2 = hostType;
                    if (nest2 == null || nest2.instanceOf(C.enclosingType())) {
                        break;
                    }
                    hostType = nest2.enclosingType();
                }
                enclosing = nest2;
            }
        } else if (C.isMemberType()) {
            if (!isQualified()) {
                if (noEnclosingInstance()) {
                    error("No enclosing instance to initialize " + C.typeName() + " with");
                    enclosing = unknownType();
                } else {
                    TypeDecl hostType2 = hostType();
                    while (true) {
                        nest = hostType2;
                        if (nest == null || nest.instanceOf(C.enclosingType())) {
                            break;
                        }
                        hostType2 = nest.enclosingType();
                    }
                    enclosing = nest == null ? unknownType() : nest;
                }
            } else {
                enclosing = enclosingInstance();
            }
        }
        if (enclosing != null && !enclosing.instanceOf(type().enclosingType())) {
            String msg = enclosing == null ? "None" : enclosing.typeName();
            error("*** Can not instantiate " + type().typeName() + " with the enclosing instance " + msg + " due to incorrect enclosing instance");
        } else if (!isQualified() && C.isMemberType() && inExplicitConstructorInvocation() && enclosing == hostType()) {
            error("*** The innermost enclosing instance of type " + enclosing.typeName() + " is this which is not yet initialized here.");
        }
    }

    public void typeCheckAnonymousSuperclassEnclosingInstance() {
        TypeDecl nest;
        if (type().isAnonymous() && ((ClassDecl) type()).superclass().isInnerType()) {
            TypeDecl S = ((ClassDecl) type()).superclass();
            if (S.isLocalClass()) {
                if (!S.inStaticContext()) {
                    if (noEnclosingInstance()) {
                        error("*** No enclosing instance to class " + type().typeName() + " due to static context");
                    } else if (inExplicitConstructorInvocation()) {
                        error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName() + " since this is not initialized yet");
                    }
                }
            } else if (S.isMemberType() && !isQualified()) {
                if (noEnclosingInstance()) {
                    error("*** No enclosing instance to class " + type().typeName() + " due to static context");
                    return;
                }
                TypeDecl hostType = hostType();
                while (true) {
                    nest = hostType;
                    if (nest == null || nest.instanceOf(S.enclosingType())) {
                        break;
                    }
                    hostType = nest.enclosingType();
                }
                if (nest == null) {
                    error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName());
                } else if (inExplicitConstructorInvocation()) {
                    error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName() + " since this is not initialized yet");
                }
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (decl().isDeprecated() && !withinDeprecatedAnnotation() && hostType().topLevelType() != decl().hostType().topLevelType() && !withinSuppressWarnings("deprecation")) {
            warning(String.valueOf(decl().signature()) + " in " + decl().hostType().typeName() + " has been deprecated");
        }
    }

    public void addEnclosingVariables() {
        if (this.addEnclosingVariables) {
            this.addEnclosingVariables = false;
            decl().addEnclosingVariables();
            for (Variable v : decl().hostType().enclosingVariables()) {
                getArgList().add(new VarAccess(v.name()));
            }
        }
    }

    public void refined_Transformations_ClassInstanceExpr_transformation() {
        addEnclosingVariables();
        if (decl().isPrivate() && type() != hostType()) {
            decl().createAccessor();
        }
        super.transformation();
    }

    private Value emitLocalEnclosing(Body b, TypeDecl localClass) {
        if (!localClass.inStaticContext()) {
            return emitThis(b, localClass.enclosingType());
        }
        throw new Error("Not implemented");
    }

    private Value emitInnerMemberEnclosing(Body b, TypeDecl innerClass) {
        if (hasPrevExpr()) {
            Local base = asLocal(b, prevExpr().eval(b));
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(base, Scene.v().getMethod("<java.lang.Object: java.lang.Class getClass()>").makeRef(), this), this));
            return base;
        }
        TypeDecl hostType = hostType();
        while (true) {
            TypeDecl enclosing = hostType;
            if (!enclosing.hasType(innerClass.name())) {
                hostType = enclosing.enclosingType();
            } else {
                return emitThis(b, enclosing);
            }
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        Local local = asLocal(b, b.newNewExpr(type().sootRef(), this));
        ArrayList list = new ArrayList();
        if (type().isAnonymous()) {
            if (type().isAnonymousInNonStaticContext()) {
                list.add(asImmediate(b, b.emitThis(hostType())));
            }
            ClassDecl C = (ClassDecl) type();
            TypeDecl S = C.superclass();
            if (S.isLocalClass()) {
                if (!type().inStaticContext()) {
                    list.add(asImmediate(b, emitLocalEnclosing(b, S)));
                }
            } else if (S.isInnerType()) {
                list.add(asImmediate(b, emitInnerMemberEnclosing(b, S)));
            }
        } else if (type().isLocalClass()) {
            if (!type().inStaticContext()) {
                list.add(asImmediate(b, emitLocalEnclosing(b, type())));
            }
        } else if (type().isInnerType()) {
            list.add(asImmediate(b, emitInnerMemberEnclosing(b, type())));
        }
        for (int i = 0; i < getNumArg(); i++) {
            list.add(asImmediate(b, getArg(i).type().emitCastTo(b, getArg(i), decl().getParameter(i).type())));
        }
        if (decl().isPrivate() && type() != hostType()) {
            list.add(asImmediate(b, NullConstant.v()));
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, decl().createAccessor().sootRef(), list, this), this));
            return local;
        }
        b.setLine(this);
        b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, decl().sootRef(), list, this), this));
        return local;
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectTypesToSignatures(Collection<Type> set) {
        super.collectTypesToSignatures(set);
        addDependencyIfNeeded(set, decl().erasedConstructor().hostType());
    }

    public ClassInstanceExpr() {
        this.addEnclosingVariables = true;
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new Opt(), 2);
    }

    public ClassInstanceExpr(Access p0, List<Expr> p1, Opt<TypeDecl> p2) {
        this.addEnclosingVariables = true;
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setAccess(Access node) {
        setChild(node, 0);
    }

    public Access getAccess() {
        return (Access) getChild(0);
    }

    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    public void setArgList(List<Expr> list) {
        setChild(list, 1);
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
        List<Expr> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setTypeDeclOpt(Opt<TypeDecl> opt) {
        setChild(opt, 2);
    }

    public boolean hasTypeDecl() {
        return getTypeDeclOpt().getNumChild() != 0;
    }

    public TypeDecl getTypeDecl() {
        return getTypeDeclOpt().getChild(0);
    }

    public void setTypeDecl(TypeDecl node) {
        getTypeDeclOpt().setChild(node, 0);
    }

    public Opt<TypeDecl> getTypeDeclOpt() {
        return (Opt) getChild(2);
    }

    public Opt<TypeDecl> getTypeDeclOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (getAccess().type().isEnumDecl() && !enclosingBodyDecl().isEnumConstant()) {
            error("enum types may not be instantiated explicitly");
        } else {
            refined_NameCheck_ClassInstanceExpr_nameCheck();
        }
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
        refined_Transformations_ClassInstanceExpr_transformation();
    }

    public boolean isDAafterInstance(Variable v) {
        if (this.isDAafterInstance_Variable_values == null) {
            this.isDAafterInstance_Variable_values = new HashMap(4);
        }
        if (this.isDAafterInstance_Variable_values.containsKey(v)) {
            return ((Boolean) this.isDAafterInstance_Variable_values.get(v)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean isDAafterInstance_Variable_value = isDAafterInstance_compute(v);
        if (isFinal && num == state().boundariesCrossed) {
            this.isDAafterInstance_Variable_values.put(v, Boolean.valueOf(isDAafterInstance_Variable_value));
        }
        return isDAafterInstance_Variable_value;
    }

    private boolean isDAafterInstance_compute(Variable v) {
        if (getNumArg() == 0) {
            return isDAbefore(v);
        }
        return getArg(getNumArg() - 1).isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return isDAafterInstance(v);
    }

    public boolean computeDAbefore(int i, Variable v) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(v);
        if (this.computeDAbefore_int_Variable_values == null) {
            this.computeDAbefore_int_Variable_values = new HashMap(4);
        }
        if (this.computeDAbefore_int_Variable_values.containsKey(arrayList)) {
            return ((Boolean) this.computeDAbefore_int_Variable_values.get(arrayList)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean computeDAbefore_int_Variable_value = computeDAbefore_compute(i, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.computeDAbefore_int_Variable_values.put(arrayList, Boolean.valueOf(computeDAbefore_int_Variable_value));
        }
        return computeDAbefore_int_Variable_value;
    }

    private boolean computeDAbefore_compute(int i, Variable v) {
        return i == 0 ? isDAbefore(v) : getArg(i - 1).isDAafter(v);
    }

    public boolean isDUafterInstance(Variable v) {
        state();
        if (getNumArg() == 0) {
            return isDUbefore(v);
        }
        return getArg(getNumArg() - 1).isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return isDUafterInstance(v);
    }

    public boolean computeDUbefore(int i, Variable v) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(v);
        if (this.computeDUbefore_int_Variable_values == null) {
            this.computeDUbefore_int_Variable_values = new HashMap(4);
        }
        if (this.computeDUbefore_int_Variable_values.containsKey(arrayList)) {
            return ((Boolean) this.computeDUbefore_int_Variable_values.get(arrayList)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean computeDUbefore_int_Variable_value = computeDUbefore_compute(i, v);
        if (isFinal && num == state().boundariesCrossed) {
            this.computeDUbefore_int_Variable_values.put(arrayList, Boolean.valueOf(computeDUbefore_int_Variable_value));
        }
        return computeDUbefore_int_Variable_value;
    }

    private boolean computeDUbefore_compute(int i, Variable v) {
        return i == 0 ? isDUbefore(v) : getArg(i - 1).isDUafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean applicableAndAccessible(ConstructorDecl decl) {
        state();
        if (decl.applicable(getArgList()) && decl.accessibleFrom(hostType())) {
            return !decl.isProtected() || hasTypeDecl() || decl.hostPackage().equals(hostPackage());
        }
        return false;
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
        TypeDecl typeDecl = hasTypeDecl() ? getTypeDecl() : getAccess().type();
        return chooseConstructor(typeDecl.constructors(), getArgList());
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

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupType(String name) {
        state();
        SimpleSet c = keepAccessibleTypes(type().memberTypes(name));
        if (!c.isEmpty()) {
            return c;
        }
        if (type().name().equals(name)) {
            return SimpleSet.emptySet.add(type());
        }
        return SimpleSet.emptySet;
    }

    public SimpleSet localLookupType(String name) {
        if (this.localLookupType_String_values == null) {
            this.localLookupType_String_values = new HashMap(4);
        }
        if (this.localLookupType_String_values.containsKey(name)) {
            return (SimpleSet) this.localLookupType_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet localLookupType_String_value = localLookupType_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.localLookupType_String_values.put(name, localLookupType_String_value);
        }
        return localLookupType_String_value;
    }

    private SimpleSet localLookupType_compute(String name) {
        if (hasTypeDecl() && getTypeDecl().name().equals(name)) {
            return SimpleSet.emptySet.add(getTypeDecl());
        }
        return SimpleSet.emptySet;
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

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.EXPRESSION_NAME;
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
        return hasTypeDecl() ? getTypeDecl() : getAccess().type();
    }

    public boolean noEnclosingInstance() {
        state();
        return isQualified() ? qualifier().staticContextQualifier() : inStaticContext();
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

    public TypeDecl typeObject() {
        state();
        TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
        return typeObject_value;
    }

    public ConstructorDecl unknownConstructor() {
        state();
        ConstructorDecl unknownConstructor_value = getParent().Define_ConstructorDecl_unknownConstructor(this, null);
        return unknownConstructor_value;
    }

    public String typeDeclIndent() {
        state();
        String typeDeclIndent_value = getParent().Define_String_typeDeclIndent(this, null);
        return typeDeclIndent_value;
    }

    public TypeDecl enclosingInstance() {
        state();
        TypeDecl enclosingInstance_value = getParent().Define_TypeDecl_enclosingInstance(this, null);
        return enclosingInstance_value;
    }

    @Override // soot.JastAddJ.Access
    public boolean inExplicitConstructorInvocation() {
        state();
        boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
        return inExplicitConstructorInvocation_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            return getAccess().type();
        }
        return getParent().Define_TypeDecl_superType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            Collection c = getAccess().type().constructors();
            SimpleSet maxSpecific = chooseConstructor(c, getArgList());
            if (maxSpecific.size() == 1) {
                return (ConstructorDecl) maxSpecific.iterator().next();
            }
            return unknownConstructor();
        }
        return getParent().Define_ConstructorDecl_constructorDecl(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getTypeDeclOptNoTransform()) {
            return isDAafterInstance(v);
        }
        if (caller == getArgListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return computeDAbefore(i, v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getArgListNoTransform()) {
            int i = caller.getIndexOfChild(child);
            return computeDUbefore(i, v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
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
        if (caller == getTypeDeclOptNoTransform()) {
            SimpleSet c = localLookupType(name);
            if (!c.isEmpty()) {
                return c;
            }
            SimpleSet c2 = lookupType(name);
            if (!c2.isEmpty()) {
                return c2;
            }
            return unqualifiedScope().lookupType(name);
        } else if (caller == getAccessNoTransform()) {
            SimpleSet c3 = lookupType(name);
            if (c3.size() == 1 && isQualified()) {
                c3 = keepInnerClasses(c3);
            }
            return c3;
        } else if (caller == getArgListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        } else {
            return getParent().Define_SimpleSet_lookupType(this, caller, name);
        }
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
        } else if (caller == getTypeDeclOptNoTransform()) {
            return NameType.TYPE_NAME;
        } else {
            if (caller == getAccessNoTransform()) {
                return NameType.TYPE_NAME;
            }
            return getParent().Define_NameType_nameType(this, caller);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isAnonymous(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isMemberType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            return hostType();
        }
        return getParent().Define_TypeDecl_hostType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclOptNoTransform()) {
            return isQualified() ? qualifier().staticContextQualifier() : inStaticContext();
        }
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return this;
        }
        return getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return hasTypeDecl();
        }
        return getParent().Define_boolean_isAnonymousDecl(this, caller);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
