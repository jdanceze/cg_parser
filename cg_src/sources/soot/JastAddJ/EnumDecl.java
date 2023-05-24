package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EnumDecl.class */
public class EnumDecl extends ClassDecl implements Cloneable {
    protected boolean isStatic_value;
    protected Opt getSuperClassAccessOpt_value;
    protected ArrayList enumConstants_value;
    protected Collection unimplementedMethods_value;
    private boolean done = false;
    protected boolean isStatic_computed = false;
    protected boolean getSuperClassAccessOpt_computed = false;
    protected boolean enumConstants_computed = false;
    protected boolean unimplementedMethods_computed = false;

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isStatic_computed = false;
        this.getSuperClassAccessOpt_computed = false;
        this.getSuperClassAccessOpt_value = null;
        this.enumConstants_computed = false;
        this.enumConstants_value = null;
        this.unimplementedMethods_computed = false;
        this.unimplementedMethods_value = null;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public EnumDecl clone() throws CloneNotSupportedException {
        EnumDecl node = (EnumDecl) super.clone();
        node.isStatic_computed = false;
        node.getSuperClassAccessOpt_computed = false;
        node.getSuperClassAccessOpt_value = null;
        node.enumConstants_computed = false;
        node.enumConstants_value = null;
        node.unimplementedMethods_computed = false;
        node.unimplementedMethods_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            EnumDecl node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.EnumDecl] */
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 4:
                        copy.children[i] = new Opt();
                        break;
                    default:
                        ASTNode child = this.children[i];
                        if (child != null) {
                            copy.setChild(child.fullCopy(), i);
                            break;
                        } else {
                            break;
                        }
                }
            }
        }
        return copy;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        for (MethodDecl m : memberMethods("finalize")) {
            if (m.getNumParameter() == 0 && m.hostType() == this) {
                error("an enum may not declare a finalizer");
            }
        }
        checkEnum(this);
    }

    private boolean done() {
        if (this.done) {
            return true;
        }
        this.done = true;
        return false;
    }

    private void addValues() {
        enumConstants().size();
        List initValues = new List();
        Iterator iter = enumConstants().iterator();
        while (iter.hasNext()) {
            EnumConstant c = (EnumConstant) iter.next();
            initValues.add(c.createBoundFieldAccess());
        }
        FieldDeclaration values = new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.PRIVATE)).add(new Modifier(Jimple.STATIC)).add(new Modifier(Jimple.FINAL)).add(new Modifier("synthetic"))), arrayType().createQualifiedAccess(), "$VALUES", new Opt(new ArrayCreationExpr(new ArrayTypeWithSizeAccess(createQualifiedAccess(), Literal.buildIntegerLiteral(enumConstants().size())), new Opt(new ArrayInit(initValues)))));
        addBodyDecl(values);
        addBodyDecl(new MethodDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.STATIC)).add(new Modifier(Jimple.FINAL)).add(new Modifier("synthetic"))), arrayType().createQualifiedAccess(), "values", new List(), new List(), new Opt(new Block(new List().add(new ReturnStmt(new Opt(new CastExpr(arrayType().createQualifiedAccess(), values.createBoundFieldAccess().qualifiesAccess(new MethodAccess("clone", new List()))))))))));
        addBodyDecl(new MethodDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC)).add(new Modifier(Jimple.STATIC)).add(new Modifier("synthetic"))), createQualifiedAccess(), "valueOf", new List().add(new ParameterDeclaration(new Modifiers(new List()), typeString().createQualifiedAccess(), "s")), new List(), new Opt(new Block(new List().add(new ReturnStmt(new Opt(new CastExpr(createQualifiedAccess(), lookupType("java.lang", "Enum").createQualifiedAccess().qualifiesAccess(new MethodAccess("valueOf", new List().add(createQualifiedAccess().qualifiesAccess(new ClassAccess())).add(new VarAccess("s"))))))))))));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void checkEnum(EnumDecl enumDecl) {
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof ConstructorDecl) {
                getBodyDecl(i).checkEnum(enumDecl);
            } else if (getBodyDecl(i) instanceof InstanceInitializer) {
                getBodyDecl(i).checkEnum(enumDecl);
            } else if (getBodyDecl(i) instanceof FieldDeclaration) {
                FieldDeclaration f = (FieldDeclaration) getBodyDecl(i);
                if (!f.isStatic() && f.hasInit()) {
                    f.checkEnum(enumDecl);
                }
            }
        }
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getModifiers().toString(s);
        s.append("enum " + name());
        if (getNumImplements() > 0) {
            s.append(" implements ");
            getImplements(0).toString(s);
            for (int i = 1; i < getNumImplements(); i++) {
                s.append(", ");
                getImplements(i).toString(s);
            }
        }
        s.append(" {");
        for (int i2 = 0; i2 < getNumBodyDecl(); i2++) {
            BodyDecl d = getBodyDecl(i2);
            if (d instanceof EnumConstant) {
                d.toString(s);
                if (i2 + 1 < getNumBodyDecl() && !(getBodyDecl(i2 + 1) instanceof EnumConstant)) {
                    s.append(String.valueOf(indent()) + ";");
                }
            } else if (d instanceof ConstructorDecl) {
                ConstructorDecl c = (ConstructorDecl) d;
                if (!c.isSynthetic()) {
                    s.append(indent());
                    c.getModifiers().toString(s);
                    s.append(String.valueOf(c.name()) + "(");
                    if (c.getNumParameter() > 2) {
                        c.getParameter(2).toString(s);
                        for (int j = 3; j < c.getNumParameter(); j++) {
                            s.append(", ");
                            c.getParameter(j).toString(s);
                        }
                    }
                    s.append(")");
                    if (c.getNumException() > 0) {
                        s.append(" throws ");
                        c.getException(0).toString(s);
                        for (int j2 = 1; j2 < c.getNumException(); j2++) {
                            s.append(", ");
                            c.getException(j2).toString(s);
                        }
                    }
                    s.append(" {");
                    for (int j3 = 0; j3 < c.getBlock().getNumStmt(); j3++) {
                        c.getBlock().getStmt(j3).toString(s);
                    }
                    s.append(indent());
                    s.append("}");
                }
            } else if (d instanceof MethodDecl) {
                MethodDecl m = (MethodDecl) d;
                if (!m.isSynthetic()) {
                    m.toString(s);
                }
            } else if (d instanceof FieldDeclaration) {
                FieldDeclaration f = (FieldDeclaration) d;
                if (!f.isSynthetic()) {
                    f.toString(s);
                }
            } else {
                d.toString(s);
            }
        }
        s.append(String.valueOf(indent()) + "}");
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void checkModifiers() {
        super.checkModifiers();
        if (!unimplementedMethods().isEmpty()) {
            StringBuffer s = new StringBuffer();
            s.append(name() + " lacks implementations in one or more enum constants for the following methods:\n");
            for (MethodDecl m : unimplementedMethods()) {
                s.append("  " + m.signature() + " in " + m.hostType().typeName() + "\n");
            }
            error(s.toString());
        }
    }

    public EnumDecl() {
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new Opt(), 3);
    }

    public EnumDecl(Modifiers p0, String p1, List<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public EnumDecl(Modifiers p0, Symbol p1, List<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setImplementsList(List<Access> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ClassDecl
    public int getNumImplements() {
        return getImplementsList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassDecl
    public int getNumImplementsNoTransform() {
        return getImplementsListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl
    public Access getImplements(int i) {
        return getImplementsList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void addImplements(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getImplementsListNoTransform() : getImplementsList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void addImplementsNoTransform(Access node) {
        List<Access> list = getImplementsListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setImplements(Access node, int i) {
        List<Access> list = getImplementsList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementss() {
        return getImplementsList();
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementssNoTransform() {
        return getImplementsListNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementsList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassDecl
    public List<Access> getImplementsListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 3);
    }

    @Override // soot.JastAddJ.ClassDecl
    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ClassDecl
    public Access getSuperClassAccess() {
        return (Access) getSuperClassAccessOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassDecl
    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(3);
    }

    protected int getSuperClassAccessOptChildPosition() {
        return 3;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public boolean isValidAnnotationMethodReturnType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isEnumDecl() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isStatic() {
        if (this.isStatic_computed) {
            return this.isStatic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isStatic_value = isStatic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isStatic_computed = true;
        }
        return this.isStatic_value;
    }

    private boolean isStatic_compute() {
        return isNestedType();
    }

    @Override // soot.JastAddJ.ClassDecl
    public Opt getSuperClassAccessOpt() {
        if (this.getSuperClassAccessOpt_computed) {
            return (Opt) getChild(getSuperClassAccessOptChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSuperClassAccessOpt_value = getSuperClassAccessOpt_compute();
        setSuperClassAccessOpt(this.getSuperClassAccessOpt_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getSuperClassAccessOpt_computed = true;
        }
        return (Opt) getChild(getSuperClassAccessOptChildPosition());
    }

    private Opt getSuperClassAccessOpt_compute() {
        return new Opt(new ParTypeAccess(new TypeAccess("java.lang", "Enum"), new List().add(createQualifiedAccess())));
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isFinal() {
        state();
        Iterator iter = enumConstants().iterator();
        while (iter.hasNext()) {
            EnumConstant c = (EnumConstant) iter.next();
            ClassInstanceExpr e = (ClassInstanceExpr) c.getInit();
            if (e.hasTypeDecl()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList enumConstants() {
        if (this.enumConstants_computed) {
            return this.enumConstants_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.enumConstants_value = enumConstants_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.enumConstants_computed = true;
        }
        return this.enumConstants_value;
    }

    private ArrayList enumConstants_compute() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i).isEnumConstant()) {
                list.add(getBodyDecl(i));
            }
        }
        return list;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isAbstract() {
        state();
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl m = (MethodDecl) getBodyDecl(i);
                if (m.isAbstract()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
    public Collection unimplementedMethods() {
        if (this.unimplementedMethods_computed) {
            return this.unimplementedMethods_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unimplementedMethods_value = unimplementedMethods_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unimplementedMethods_computed = true;
        }
        return this.unimplementedMethods_value;
    }

    private Collection unimplementedMethods_compute() {
        Collection<MethodDecl> methods = new LinkedList<>();
        Iterator iter = interfacesMethodsIterator();
        while (iter.hasNext()) {
            MethodDecl method = (MethodDecl) iter.next();
            SimpleSet set = localMethodsSignature(method.signature());
            if (set.size() == 1) {
                MethodDecl n = (MethodDecl) set.iterator().next();
                if (!n.isAbstract()) {
                }
            }
            boolean implemented = false;
            Iterator i2 = ancestorMethods(method.signature()).iterator();
            while (true) {
                if (!i2.hasNext()) {
                    break;
                }
                MethodDecl n2 = (MethodDecl) i2.next();
                if (!n2.isAbstract()) {
                    implemented = true;
                    break;
                }
            }
            if (!implemented) {
                methods.add(method);
            }
        }
        Iterator iter2 = localMethodsIterator();
        while (iter2.hasNext()) {
            MethodDecl method2 = (MethodDecl) iter2.next();
            if (method2.isAbstract()) {
                methods.add(method2);
            }
        }
        Collection unimplemented = new ArrayList();
        for (MethodDecl method3 : methods) {
            if (enumConstants().isEmpty()) {
                unimplemented.add(method3);
            } else {
                boolean missing = false;
                Iterator iter3 = enumConstants().iterator();
                while (true) {
                    if (iter3.hasNext()) {
                        if (!((EnumConstant) iter3.next()).implementsMethod(method3)) {
                            missing = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (missing) {
                    unimplemented.add(method3);
                }
            }
        }
        return unimplemented;
    }

    @Override // soot.JastAddJ.TypeDecl
    public int sootTypeModifiers() {
        state();
        return super.sootTypeModifiers() | 16384;
    }

    public TypeDecl typeString() {
        state();
        TypeDecl typeString_value = getParent().Define_TypeDecl_typeString(this, null);
        return typeString_value;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return false;
        }
        return super.Define_boolean_mayBeAbstract(caller, child);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return isNestedType();
        }
        return super.Define_boolean_mayBeStatic(caller, child);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return false;
        }
        return super.Define_boolean_mayBeFinal(caller, child);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (!done()) {
            state().duringEnums++;
            ASTNode result = rewriteRule0();
            state().duringEnums--;
            return result;
        }
        return super.rewriteTo();
    }

    private EnumDecl rewriteRule0() {
        if (noConstructor()) {
            List parameterList = new List();
            parameterList.add(new ParameterDeclaration(new TypeAccess("java.lang", "String"), "p0"));
            parameterList.add(new ParameterDeclaration(new TypeAccess("int"), "p1"));
            addBodyDecl(new ConstructorDecl(new Modifiers(new List().add(new Modifier(Jimple.PRIVATE)).add(new Modifier("synthetic"))), name(), parameterList, new List(), new Opt(new ExprStmt(new SuperConstructorAccess("super", new List().add(new VarAccess("p0")).add(new VarAccess("p1"))))), new Block(new List())));
        } else {
            transformEnumConstructors();
        }
        addValues();
        return this;
    }
}
