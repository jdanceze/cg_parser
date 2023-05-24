package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.SimpleSet;
import soot.Local;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParameterDeclaration.class */
public class ParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, Variable {
    private ParameterDeclaration iterElem;
    public Local local;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected boolean type_computed;
    protected TypeDecl type_value;
    protected boolean sourceVariableDecl_computed;
    protected Variable sourceVariableDecl_value;
    protected boolean throwTypes_computed;
    protected Collection<TypeDecl> throwTypes_value;
    protected boolean localNum_computed;
    protected int localNum_value;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
        this.throwTypes_computed = false;
        this.throwTypes_value = null;
        this.localNum_computed = false;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public ParameterDeclaration clone() throws CloneNotSupportedException {
        ParameterDeclaration node = (ParameterDeclaration) super.mo287clone();
        node.type_computed = false;
        node.type_value = null;
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.throwTypes_computed = false;
        node.throwTypes_value = null;
        node.localNum_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ParameterDeclaration node = clone();
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

    public ParameterDeclaration(Access type, String name) {
        this(new Modifiers(new List()), type, name);
    }

    public ParameterDeclaration(TypeDecl type, String name) {
        this(new Modifiers(new List()), type.createQualifiedAccess(), name);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getModifiers().toString(s);
        getTypeAccess().toString(s);
        s.append(Instruction.argsep + name());
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.setLine(this);
        this.local = b.newLocal(name(), type().getSootType());
        b.add(b.newIdentityStmt(this.local, b.newParameterRef(type().getSootType(), localNum(), this), this));
    }

    public ParameterDeclaration() {
        this.type_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
    }

    public ParameterDeclaration(Modifiers p0, Access p1, String p2) {
        this.type_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
    }

    public ParameterDeclaration(Modifiers p0, Access p1, Symbol p2) {
        this.type_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
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

    public void setTypeAccess(Access node) {
        setChild(node, 1);
    }

    public Access getTypeAccess() {
        return (Access) getChild(1);
    }

    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(1);
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

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        SimpleSet<Variable> decls = outerScope().lookupVariable(name());
        for (Variable var : decls) {
            if (var instanceof VariableDeclaration) {
                VariableDeclaration decl = (VariableDeclaration) var;
                if (decl.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of parameter " + name());
                }
            } else if (var instanceof ParameterDeclaration) {
                ParameterDeclaration decl2 = (ParameterDeclaration) var;
                if (decl2.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of parameter " + name());
                }
            } else if (var instanceof CatchParameterDeclaration) {
                CatchParameterDeclaration decl3 = (CatchParameterDeclaration) var;
                if (decl3.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of parameter " + name());
                }
            }
        }
        if (!lookupVariable(name()).contains(this)) {
            error("duplicate declaration of parameter " + name());
        }
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
    public boolean isSynthetic() {
        state();
        return getModifiers().isSynthetic();
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    @Override // soot.JastAddJ.Variable
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
        return getTypeAccess().type();
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
        return getModifiers().isFinal();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isVolatile() {
        state();
        return getModifiers().isVolatile();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isBlank() {
        state();
        return true;
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

    public boolean isVariableArity() {
        state();
        return false;
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
        if (isCatchParam() && effectivelyFinal()) {
            return catchClause().caughtExceptions();
        }
        Collection<TypeDecl> tts = new LinkedList<>();
        tts.add(type());
        return tts;
    }

    public boolean effectivelyFinal() {
        state();
        return isFinal() || !inhModifiedInScope(this);
    }

    public ParameterDeclaration substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
        state();
        return new ParameterDeclaration((Modifiers) getModifiers().cloneSubtree(), getTypeAccess().substituted(original, substitution), getID());
    }

    public SimpleSet lookupVariable(String name) {
        state();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        return lookupVariable_String_value;
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

    @Override // soot.JastAddJ.Variable
    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
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

    public int localNum() {
        if (this.localNum_computed) {
            return this.localNum_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.localNum_value = getParent().Define_int_localNum(this, null);
        if (isFinal && num == state().boundariesCrossed) {
            this.localNum_computed = true;
        }
        return this.localNum_value;
    }

    public boolean inhModifiedInScope(Variable var) {
        state();
        boolean inhModifiedInScope_Variable_value = getParent().Define_boolean_inhModifiedInScope(this, null, var);
        return inhModifiedInScope_Variable_value;
    }

    public boolean isCatchParam() {
        state();
        boolean isCatchParam_value = getParent().Define_boolean_isCatchParam(this, null);
        return isCatchParam_value;
    }

    public CatchClause catchClause() {
        state();
        CatchClause catchClause_value = getParent().Define_CatchClause_catchClause(this, null);
        return catchClause_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("PARAMETER");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
