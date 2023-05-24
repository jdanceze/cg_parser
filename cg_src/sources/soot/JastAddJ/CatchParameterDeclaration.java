package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.SimpleSet;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CatchParameterDeclaration.class */
public class CatchParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, Variable, SimpleSet, Iterator {
    private CatchParameterDeclaration iterElem;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Variable sourceVariableDecl_value;
    protected Collection<TypeDecl> throwTypes_value;
    protected boolean sourceVariableDecl_computed = false;
    protected boolean throwTypes_computed = false;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
        this.throwTypes_computed = false;
        this.throwTypes_value = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public CatchParameterDeclaration clone() throws CloneNotSupportedException {
        CatchParameterDeclaration node = (CatchParameterDeclaration) super.mo287clone();
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.throwTypes_computed = false;
        node.throwTypes_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            CatchParameterDeclaration node = clone();
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

    @Override // soot.JastAddJ.SimpleSet
    public SimpleSet add(Object o) {
        return new SimpleSet.SimpleSetImpl().add(this).add(o);
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isSingleton() {
        return true;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isSingleton(Object o) {
        return contains(o);
    }

    @Override // soot.JastAddJ.ASTNode, java.lang.Iterable
    public Iterator iterator() {
        this.iterElem = this;
        return this;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterElem != null;
    }

    @Override // java.util.Iterator
    public Object next() {
        Object o = this.iterElem;
        this.iterElem = null;
        return o;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        for (int i = 0; i < getNumTypeAccess(); i++) {
            for (int j = 0; j < getNumTypeAccess(); j++) {
                if (i != j) {
                    TypeDecl t1 = getTypeAccess(i).type();
                    TypeDecl t2 = getTypeAccess(j).type();
                    if (t2.instanceOf(t1)) {
                        error(String.valueOf(t2.fullName()) + " is a subclass of " + t1.fullName());
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer sb) {
        getModifiers().toString(sb);
        for (int i = 0; i < getNumTypeAccess(); i++) {
            if (i > 0) {
                sb.append(" | ");
            }
            getTypeAccess(i).toString(sb);
        }
        sb.append(Instruction.argsep + getID());
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        SimpleSet<Variable> decls = outerScope().lookupVariable(name());
        for (Variable var : decls) {
            if (var instanceof VariableDeclaration) {
                VariableDeclaration decl = (VariableDeclaration) var;
                if (decl.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of catch parameter " + name());
                }
            } else if (var instanceof ParameterDeclaration) {
                ParameterDeclaration decl2 = (ParameterDeclaration) var;
                if (decl2.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of catch parameter " + name());
                }
            } else if (var instanceof CatchParameterDeclaration) {
                CatchParameterDeclaration decl3 = (CatchParameterDeclaration) var;
                if (decl3.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of catch parameter " + name());
                }
            }
        }
        if (!lookupVariable(name()).contains(this)) {
            error("duplicate declaration of catch parameter " + name());
        }
    }

    public CatchParameterDeclaration() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 1);
    }

    public CatchParameterDeclaration(Modifiers p0, List<Access> p1, String p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
    }

    public CatchParameterDeclaration(Modifiers p0, List<Access> p1, Symbol p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    public void setTypeAccessList(List<Access> list) {
        setChild(list, 1);
    }

    public int getNumTypeAccess() {
        return getTypeAccessList().getNumChild();
    }

    public int getNumTypeAccessNoTransform() {
        return getTypeAccessListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeAccess(int i) {
        return getTypeAccessList().getChild(i);
    }

    public void addTypeAccess(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeAccessListNoTransform() : getTypeAccessList();
        list.addChild(node);
    }

    public void addTypeAccessNoTransform(Access node) {
        List<Access> list = getTypeAccessListNoTransform();
        list.addChild(node);
    }

    public void setTypeAccess(Access node, int i) {
        List<Access> list = getTypeAccessList();
        list.setChild(node, i);
    }

    public List<Access> getTypeAccesss() {
        return getTypeAccessList();
    }

    public List<Access> getTypeAccesssNoTransform() {
        return getTypeAccessListNoTransform();
    }

    public List<Access> getTypeAccessList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeAccessListNoTransform() {
        return (List) getChildNoTransform(1);
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

    @Override // soot.JastAddJ.Variable
    public boolean isParameter() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isClassVariable() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isInstanceVariable() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isLocalVariable() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isFinal() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isVolatile() {
        state();
        return getModifiers().isVolatile();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isBlank() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.Variable
    public boolean hasInit() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public Expr getInit() {
        state();
        throw new UnsupportedOperationException();
    }

    @Override // soot.JastAddJ.Variable
    public Constant constant() {
        state();
        throw new UnsupportedOperationException();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isSynthetic() {
        state();
        return getModifiers().isSynthetic();
    }

    @Override // soot.JastAddJ.Variable
    public Variable sourceVariableDecl() {
        if (this.sourceVariableDecl_computed) {
            return this.sourceVariableDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.sourceVariableDecl_value = sourceVariableDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.sourceVariableDecl_computed = true;
        }
        return this.sourceVariableDecl_value;
    }

    private Variable sourceVariableDecl_compute() {
        return this;
    }

    @Override // soot.JastAddJ.SimpleSet
    public int size() {
        state();
        return 1;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean isEmpty() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.SimpleSet
    public boolean contains(Object o) {
        state();
        return this == o;
    }

    @Override // soot.JastAddJ.Variable
    public TypeDecl type() {
        state();
        ArrayList<TypeDecl> list = new ArrayList<>();
        for (int i = 0; i < getNumTypeAccess(); i++) {
            list.add(getTypeAccess(i).type());
        }
        return lookupLUBType(list).lub();
    }

    @Override // soot.JastAddJ.Variable
    public Collection<TypeDecl> throwTypes() {
        if (this.throwTypes_computed) {
            return this.throwTypes_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.throwTypes_value = throwTypes_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.throwTypes_computed = true;
        }
        return this.throwTypes_value;
    }

    private Collection<TypeDecl> throwTypes_compute() {
        return catchClause().caughtExceptions();
    }

    public SimpleSet lookupVariable(String name) {
        state();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        return lookupVariable_String_value;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isMethodParameter() {
        state();
        boolean isMethodParameter_value = getParent().Define_boolean_isMethodParameter(this, null);
        return isMethodParameter_value;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isConstructorParameter() {
        state();
        boolean isConstructorParameter_value = getParent().Define_boolean_isConstructorParameter(this, null);
        return isConstructorParameter_value;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isExceptionHandlerParameter() {
        state();
        boolean isExceptionHandlerParameter_value = getParent().Define_boolean_isExceptionHandlerParameter(this, null);
        return isExceptionHandlerParameter_value;
    }

    @Override // soot.JastAddJ.Variable
    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    public LUBType lookupLUBType(Collection bounds) {
        state();
        LUBType lookupLUBType_Collection_value = getParent().Define_LUBType_lookupLUBType(this, null, bounds);
        return lookupLUBType_Collection_value;
    }

    public VariableScope outerScope() {
        state();
        VariableScope outerScope_value = getParent().Define_VariableScope_outerScope(this, null);
        return outerScope_value;
    }

    public BodyDecl enclosingBodyDecl() {
        state();
        BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);
        return enclosingBodyDecl_value;
    }

    public CatchClause catchClause() {
        state();
        CatchClause catchClause_value = getParent().Define_CatchClause_catchClause(this, null);
        return catchClause_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
