package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashSet;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AnonymousDecl.class */
public class AnonymousDecl extends ClassDecl implements Cloneable {
    protected boolean isCircular_value;
    protected Opt getSuperClassAccessOpt_value;
    protected List getImplementsList_value;
    protected int isCircular_visited = -1;
    protected boolean isCircular_computed = false;
    protected boolean isCircular_initialized = false;
    protected boolean getSuperClassAccessOpt_computed = false;
    protected boolean getImplementsList_computed = false;

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isCircular_visited = -1;
        this.isCircular_computed = false;
        this.isCircular_initialized = false;
        this.getSuperClassAccessOpt_computed = false;
        this.getSuperClassAccessOpt_value = null;
        this.getImplementsList_computed = false;
        this.getImplementsList_value = null;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public AnonymousDecl clone() throws CloneNotSupportedException {
        AnonymousDecl node = (AnonymousDecl) super.clone();
        node.isCircular_visited = -1;
        node.isCircular_computed = false;
        node.isCircular_initialized = false;
        node.getSuperClassAccessOpt_computed = false;
        node.getSuperClassAccessOpt_value = null;
        node.getImplementsList_computed = false;
        node.getImplementsList_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            AnonymousDecl node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.AnonymousDecl] */
    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 3:
                        copy.children[i] = new Opt();
                        break;
                    case 4:
                        copy.children[i] = new List();
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

    public AnonymousDecl() {
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new Opt(), 2);
        setChild(new List(), 3);
    }

    public AnonymousDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    public AnonymousDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
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

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
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
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 2);
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
        return (Opt) getChildNoTransform(2);
    }

    protected int getSuperClassAccessOptChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.ClassDecl
    public void setImplementsList(List<Access> list) {
        setChild(list, 3);
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
        return (Access) getImplementsList().getChild(i);
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
    public List<Access> getImplementsListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    protected int getImplementsListChildPosition() {
        return 3;
    }

    protected List constructorParameterList(ConstructorDecl decl) {
        List parameterList = new List();
        for (int i = 0; i < decl.getNumParameter(); i++) {
            ParameterDeclaration param = decl.getParameter(i);
            if (param instanceof VariableArityParameterDeclaration) {
                parameterList.add(new VariableArityParameterDeclaration(new Modifiers(new List()), ((ArrayDecl) param.type()).componentType().createBoundAccess(), param.name()));
            } else {
                parameterList.add(new ParameterDeclaration(param.type().createBoundAccess(), param.name()));
            }
        }
        return parameterList;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.TypeDecl
    public boolean isCircular() {
        if (this.isCircular_computed) {
            return this.isCircular_value;
        }
        ASTNode.State state = state();
        if (!this.isCircular_initialized) {
            this.isCircular_initialized = true;
            this.isCircular_value = true;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.isCircular_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_isCircular_value = isCircular_compute();
                if (new_isCircular_value != this.isCircular_value) {
                    state.CHANGE = true;
                }
                this.isCircular_value = new_isCircular_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.isCircular_computed = true;
            } else {
                state.RESET_CYCLE = true;
                isCircular_compute();
                state.RESET_CYCLE = false;
                this.isCircular_computed = false;
                this.isCircular_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.isCircular_value;
        } else if (this.isCircular_visited != state.CIRCLE_INDEX) {
            this.isCircular_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.isCircular_computed = false;
                this.isCircular_initialized = false;
                this.isCircular_visited = -1;
                return this.isCircular_value;
            }
            boolean new_isCircular_value2 = isCircular_compute();
            if (new_isCircular_value2 != this.isCircular_value) {
                state.CHANGE = true;
            }
            this.isCircular_value = new_isCircular_value2;
            return this.isCircular_value;
        } else {
            return this.isCircular_value;
        }
    }

    private boolean isCircular_compute() {
        return false;
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
        if (superType().isInterfaceDecl()) {
            return new Opt(typeObject().createQualifiedAccess());
        }
        return new Opt(superType().createBoundAccess());
    }

    @Override // soot.JastAddJ.ClassDecl
    public List getImplementsList() {
        if (this.getImplementsList_computed) {
            return (List) getChild(getImplementsListChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getImplementsList_value = getImplementsList_compute();
        setImplementsList(this.getImplementsList_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getImplementsList_computed = true;
        }
        return (List) getChild(getImplementsListChildPosition());
    }

    private List getImplementsList_compute() {
        if (superType().isInterfaceDecl()) {
            return new List().add(superType().createBoundAccess());
        }
        return new List();
    }

    public TypeDecl superType() {
        state();
        TypeDecl superType_value = getParent().Define_TypeDecl_superType(this, null);
        return superType_value;
    }

    public ConstructorDecl constructorDecl() {
        state();
        ConstructorDecl constructorDecl_value = getParent().Define_ConstructorDecl_constructorDecl(this, null);
        return constructorDecl_value;
    }

    public TypeDecl typeNullPointerException() {
        state();
        TypeDecl typeNullPointerException_value = getParent().Define_TypeDecl_typeNullPointerException(this, null);
        return typeNullPointerException_value;
    }

    @Override // soot.JastAddJ.ClassDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (noConstructor()) {
            state().duringAnonymousClasses++;
            ASTNode result = rewriteRule0();
            state().duringAnonymousClasses--;
            return result;
        }
        return super.rewriteTo();
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [soot.JastAddJ.Modifiers] */
    private AnonymousDecl rewriteRule0() {
        setModifiers(new Modifiers(new List().add(new Modifier(Jimple.FINAL))));
        ConstructorDecl decl = constructorDecl();
        ?? fullCopy2 = decl.getModifiers().fullCopy2();
        String anonName = "Anonymous" + nextAnonymousIndex();
        ConstructorDecl constructor = new ConstructorDecl((Modifiers) fullCopy2, anonName, constructorParameterList(decl), new List(), new Opt(), new Block());
        constructor.setDefaultConstructor();
        addBodyDecl(constructor);
        setID(anonName);
        List argList = new List();
        for (int i = 0; i < constructor.getNumParameter(); i++) {
            argList.add(new VarAccess(constructor.getParameter(i).name()));
        }
        constructor.setConstructorInvocation(new ExprStmt(new SuperConstructorAccess("super", argList)));
        HashSet set = new HashSet();
        for (int i2 = 0; i2 < getNumBodyDecl(); i2++) {
            if (getBodyDecl(i2) instanceof InstanceInitializer) {
                InstanceInitializer init = (InstanceInitializer) getBodyDecl(i2);
                set.addAll(init.exceptions());
            } else if (getBodyDecl(i2) instanceof FieldDeclaration) {
                FieldDeclaration f = (FieldDeclaration) getBodyDecl(i2);
                if (f.isInstanceVariable()) {
                    set.addAll(f.exceptions());
                }
            }
        }
        List exceptionList = new List();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            TypeDecl exceptionType = (TypeDecl) iter.next();
            if (exceptionType.isNull()) {
                exceptionType = typeNullPointerException();
            }
            exceptionList.add(exceptionType.createQualifiedAccess());
        }
        constructor.setExceptionList(exceptionList);
        return this;
    }
}
