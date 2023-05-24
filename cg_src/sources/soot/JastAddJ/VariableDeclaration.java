package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.SimpleSet;
import soot.Local;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/VariableDeclaration.class */
public class VariableDeclaration extends Stmt implements Cloneable, SimpleSet, Iterator, Variable {
    private VariableDeclaration iterElem;
    public Local local;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected Map isDAafter_Variable_values;
    protected Map isDUafter_Variable_values;
    protected boolean constant_computed;
    protected Constant constant_value;
    protected boolean sourceVariableDecl_computed;
    protected Variable sourceVariableDecl_value;
    protected boolean throwTypes_computed;
    protected Collection<TypeDecl> throwTypes_value;
    protected boolean localNum_computed;
    protected int localNum_value;

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isDAafter_Variable_values = null;
        this.isDUafter_Variable_values = null;
        this.constant_computed = false;
        this.constant_value = null;
        this.sourceVariableDecl_computed = false;
        this.sourceVariableDecl_value = null;
        this.throwTypes_computed = false;
        this.throwTypes_value = null;
        this.localNum_computed = false;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public VariableDeclaration clone() throws CloneNotSupportedException {
        VariableDeclaration node = (VariableDeclaration) super.clone();
        node.isDAafter_Variable_values = null;
        node.isDUafter_Variable_values = null;
        node.constant_computed = false;
        node.constant_value = null;
        node.sourceVariableDecl_computed = false;
        node.sourceVariableDecl_value = null;
        node.throwTypes_computed = false;
        node.throwTypes_value = null;
        node.localNum_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            VariableDeclaration node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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

    public VariableDeclaration(Access type, String name, Expr init) {
        this(new Modifiers(new List()), type, name, new Opt(init));
    }

    public VariableDeclaration(Access type, String name) {
        this(new Modifiers(new List()), type, name, new Opt());
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        getTypeAccess().toString(s);
        s.append(Instruction.argsep + name());
        if (hasInit()) {
            s.append(" = ");
            getInit().toString(s);
        }
        s.append(";");
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (hasInit()) {
            TypeDecl source = getInit().type();
            TypeDecl dest = type();
            if (!source.assignConversionTo(dest, getInit())) {
                error("can not assign variable " + name() + " of type " + dest.typeName() + " a value of type " + source.typeName());
            }
        }
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void jimplify2(Body b) {
        b.setLine(this);
        this.local = b.newLocal(name(), type().getSootType());
        if (hasInit()) {
            b.add(b.newAssignStmt(this.local, asRValue(b, getInit().type().emitCastTo(b, getInit(), type())), this));
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkWarnings() {
        if (hasInit() && !suppressWarnings("unchecked")) {
            checkUncheckedConversion(getInit().type(), type());
        }
    }

    public VariableDeclaration() {
        this.constant_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 2);
    }

    public VariableDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
        this.constant_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    public VariableDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
        this.constant_computed = false;
        this.sourceVariableDecl_computed = false;
        this.throwTypes_computed = false;
        this.localNum_computed = false;
        setChild(p0, 0);
        setChild(p1, 1);
        setID(p2);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

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

    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 2);
    }

    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(2);
    }

    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        SimpleSet<Variable> decls = outerScope().lookupVariable(name());
        for (Variable var : decls) {
            if (var instanceof VariableDeclaration) {
                VariableDeclaration decl = (VariableDeclaration) var;
                if (decl != this && decl.enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of local variable " + name());
                }
            } else if (var instanceof ParameterDeclaration) {
                if (((ParameterDeclaration) var).enclosingBodyDecl() == enclosingBodyDecl()) {
                    error("duplicate declaration of local variable " + name());
                }
            } else if ((var instanceof CatchParameterDeclaration) && ((CatchParameterDeclaration) var).enclosingBodyDecl() == enclosingBodyDecl()) {
                error("duplicate declaration of local variable " + name());
            }
        }
        if (getParent().getParent() instanceof Block) {
            Block block = (Block) getParent().getParent();
            for (int i = 0; i < block.getNumStmt(); i++) {
                if (block.getStmt(i) instanceof Variable) {
                    Variable v = (Variable) block.getStmt(i);
                    if (v.name().equals(name()) && v != this) {
                        error("duplicate declaration of local variable " + name());
                    }
                }
            }
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

    public boolean isBlankFinal() {
        state();
        if (isFinal()) {
            return (hasInit() && getInit().isConstant()) ? false : true;
        }
        return false;
    }

    public boolean isValue() {
        state();
        return isFinal() && hasInit() && getInit().isConstant();
    }

    @Override // soot.JastAddJ.Stmt
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
        if (v == this) {
            return hasInit();
        }
        return hasInit() ? getInit().isDAafter(v) : isDAbefore(v);
    }

    @Override // soot.JastAddJ.Stmt
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
        return v == this ? !hasInit() : hasInit() ? getInit().isDUafter(v) : isDUbefore(v);
    }

    @Override // soot.JastAddJ.Stmt
    public boolean declaresVariable(String name) {
        state();
        return name().equals(name);
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
        state();
        return getTypeAccess().type();
    }

    @Override // soot.JastAddJ.Variable
    public boolean isParameter() {
        state();
        return false;
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
    public boolean isMethodParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isConstructorParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isExceptionHandlerParameter() {
        state();
        return false;
    }

    @Override // soot.JastAddJ.Variable
    public boolean isLocalVariable() {
        state();
        return true;
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
        return !hasInit();
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
    public Constant constant() {
        if (this.constant_computed) {
            return this.constant_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.constant_value = constant_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.constant_computed = true;
        }
        return this.constant_value;
    }

    private Constant constant_compute() {
        return type().cast(getInit().constant());
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
        Collection<TypeDecl> tts = new LinkedList<>();
        tts.add(type());
        return tts;
    }

    @Override // soot.JastAddJ.Stmt
    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        return getModifiers().hasAnnotationSuppressWarnings(s);
    }

    public boolean suppressWarnings(String type) {
        state();
        return hasAnnotationSuppressWarnings(type) || withinSuppressWarnings(type);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.VariableScope
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

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.Variable
    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    @Override // soot.JastAddJ.Stmt
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

    public boolean withinSuppressWarnings(String s) {
        state();
        boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, s);
        return withinSuppressWarnings_String_value;
    }

    public boolean resourcePreviouslyDeclared(String name) {
        state();
        boolean resourcePreviouslyDeclared_String_value = getParent().Define_boolean_resourcePreviouslyDeclared(this, null, name);
        return resourcePreviouslyDeclared_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getInitOptNoTransform()) {
            return isDAbefore(v);
        }
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        if (caller == getInitOptNoTransform()) {
            return isDUbefore(v);
        }
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("LOCAL_VARIABLE");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type();
        }
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        if (caller == getInitOptNoTransform()) {
            return type().componentType();
        }
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    @Override // soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
