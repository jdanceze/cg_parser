package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Scene;
import soot.SootFieldRef;
import soot.Type;
import soot.Value;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/VarAccess.class */
public class VarAccess extends Access implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected int isConstant_visited;
    protected boolean isConstant_computed;
    protected boolean isConstant_initialized;
    protected boolean isConstant_value;
    protected Map isDAafter_Variable_values;
    protected boolean decls_computed;
    protected SimpleSet decls_value;
    protected boolean decl_computed;
    protected Variable decl_value;
    protected boolean isFieldAccess_computed;
    protected boolean isFieldAccess_value;
    protected boolean type_computed;
    protected TypeDecl type_value;
    protected Map base_Body_values;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
        this.isDAafter_Variable_values = null;
        this.decls_computed = false;
        this.decls_value = null;
        this.decl_computed = false;
        this.decl_value = null;
        this.isFieldAccess_computed = false;
        this.type_computed = false;
        this.type_value = null;
        this.base_Body_values = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public VarAccess clone() throws CloneNotSupportedException {
        VarAccess node = (VarAccess) super.clone();
        node.isConstant_visited = -1;
        node.isConstant_computed = false;
        node.isConstant_initialized = false;
        node.isDAafter_Variable_values = null;
        node.decls_computed = false;
        node.decls_value = null;
        node.decl_computed = false;
        node.decl_value = null;
        node.isFieldAccess_computed = false;
        node.type_computed = false;
        node.type_value = null;
        node.base_Body_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            VarAccess node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void definiteAssignment() {
        if (isSource()) {
            if (decl() instanceof VariableDeclaration) {
                VariableDeclaration v = (VariableDeclaration) decl();
                if (!v.isValue()) {
                    if (v.isBlankFinal()) {
                        if (!isDAbefore(v)) {
                            error("Final variable " + v.name() + " is not assigned before used");
                        }
                    } else if (!isDAbefore(v)) {
                        error("Local variable " + v.name() + " is not assigned before used");
                    }
                }
            } else if ((decl() instanceof FieldDeclaration) && !isQualified()) {
                FieldDeclaration f = (FieldDeclaration) decl();
                if (f.isFinal() && !f.hasInit() && !isDAbefore(f)) {
                    error("Final field " + f + " is not assigned before used");
                }
            }
        }
        if (isDest()) {
            Variable v2 = decl();
            if (v2.isFinal() && v2.isBlank() && !hostType().instanceOf(v2.hostType())) {
                error("The final variable is not a blank final in this context, so it may not be assigned.");
            } else if (v2.isFinal() && isQualified() && (!qualifier().isThisAccess() || ((Access) qualifier()).isQualified())) {
                error("the blank final field " + v2.name() + " may only be assigned by simple name");
            } else if (v2 instanceof VariableDeclaration) {
                VariableDeclaration var = (VariableDeclaration) v2;
                if (!var.isValue() && (var.getParent().getParent().getParent() instanceof SwitchStmt) && var.isFinal()) {
                    if (!isDUbefore(var)) {
                        error("Final variable " + var.name() + " may only be assigned once");
                    }
                } else if (var.isValue()) {
                    if (var.hasInit() || !isDUbefore(var)) {
                        error("Final variable " + var.name() + " may only be assigned once");
                    }
                } else if (var.isBlankFinal() && (var.hasInit() || !isDUbefore(var))) {
                    error("Final variable " + var.name() + " may only be assigned once");
                }
                if (!var.isFinal() || var.hasInit()) {
                    return;
                }
                isDUbefore(var);
            } else if (v2 instanceof FieldDeclaration) {
                FieldDeclaration f2 = (FieldDeclaration) v2;
                if (f2.isFinal()) {
                    if (f2.hasInit()) {
                        error("initialized field " + f2.name() + " can not be assigned");
                        return;
                    }
                    BodyDecl bodyDecl = enclosingBodyDecl();
                    if (!(bodyDecl instanceof ConstructorDecl) && !(bodyDecl instanceof InstanceInitializer) && !(bodyDecl instanceof StaticInitializer) && !(bodyDecl instanceof FieldDeclaration)) {
                        error("final field " + f2.name() + " may only be assigned in constructors and initializers");
                    } else if (!isDUbefore(f2)) {
                        error("Final field " + f2.name() + " may only be assigned once");
                    }
                }
            } else if (v2.isParameter() && v2.isFinal()) {
                error("Final parameter " + v2.name() + " may not be assigned");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public boolean checkDUeverywhere(Variable v) {
        if (isDest() && decl() == v) {
            return false;
        }
        return super.checkDUeverywhere(v);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (decls().isEmpty() && (!isQualified() || !qualifier().type().isUnknown() || qualifier().isPackageAccess())) {
            error("no field named " + name() + " is accessible");
        }
        if (decls().size() > 1) {
            StringBuffer s = new StringBuffer();
            s.append("several fields named " + name());
            for (Variable v : decls()) {
                s.append("\n    " + v.type().typeName() + "." + v.name() + " declared in " + v.hostType().typeName());
            }
            error(s.toString());
        }
        if (inExplicitConstructorInvocation() && !isQualified() && decl().isInstanceVariable() && hostType() == decl().hostType()) {
            error("instance variable " + name() + " may not be accessed in an explicit constructor invocation");
        }
        Variable v2 = decl();
        if (!v2.isFinal() && !v2.isClassVariable() && !v2.isInstanceVariable() && v2.hostType() != hostType()) {
            error("A parameter/variable used but not declared in an inner class must be declared final");
        }
        if ((decl().isInstanceVariable() || decl().isClassVariable()) && !isQualified() && hostType() != null && !hostType().declaredBeforeUse(decl(), this) && inSameInitializer() && !simpleAssignment() && inDeclaringClass()) {
            BodyDecl b = closestBodyDecl(hostType());
            error("variable " + decl().name() + " is used in " + b + " before it is declared");
        }
    }

    public BodyDecl closestBodyDecl(TypeDecl t) {
        ASTNode node;
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if ((node.getParent().getParent() instanceof Program) || node.getParent().getParent() == t) {
                break;
            }
            aSTNode = node.getParent();
        }
        if (node instanceof BodyDecl) {
            return (BodyDecl) node;
        }
        return null;
    }

    public VarAccess(String name, int start, int end) {
        this(name);
        this.IDstart = start;
        this.start = start;
        this.IDend = end;
        this.end = end;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(name());
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (decl() instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration) decl();
            if (f.isDeprecated() && !withinDeprecatedAnnotation() && hostType().topLevelType() != f.hostType().topLevelType() && !withinSuppressWarnings("deprecation")) {
                warning(String.valueOf(f.name()) + " in " + f.hostType().typeName() + " has been deprecated");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.JastAddJ.ASTNode
    public void checkEnum(EnumDecl enumDecl) {
        super.checkEnum(enumDecl);
        if (decl().isStatic() && decl().hostType() == enumDecl && !isConstant()) {
            error("may not reference a static field of an enum type from here");
        }
    }

    private TypeDecl refined_InnerClasses_VarAccess_fieldQualifierType() {
        TypeDecl typeDecl;
        if (hasPrevExpr()) {
            return prevExpr().type();
        }
        TypeDecl hostType = hostType();
        while (true) {
            typeDecl = hostType;
            if (typeDecl == null || typeDecl.hasField(name())) {
                break;
            }
            hostType = typeDecl.enclosingType();
        }
        if (typeDecl != null) {
            return typeDecl;
        }
        return decl().hostType();
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectEnclosingVariables(HashSet set, TypeDecl typeDecl) {
        Variable v = decl();
        if (!v.isInstanceVariable() && !v.isClassVariable() && v.hostType() == typeDecl) {
            set.add(v);
        }
        super.collectEnclosingVariables(set, typeDecl);
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration) v;
            if (requiresAccessor()) {
                TypeDecl typeDecl = fieldQualifierType();
                if (isSource()) {
                    f.createAccessor(typeDecl);
                }
                if (isDest()) {
                    f.createAccessorWrite(typeDecl);
                }
            }
        }
        super.transformation();
    }

    public Value refined_Expressions_VarAccess_eval(Body b) {
        Variable v = decl();
        if (v instanceof VariableDeclaration) {
            VariableDeclaration decl = (VariableDeclaration) v;
            if (decl.hostType() == hostType()) {
                return decl.local;
            }
            return emitLoadLocalInNestedClass(b, decl);
        } else if (v instanceof ParameterDeclaration) {
            ParameterDeclaration decl2 = (ParameterDeclaration) v;
            if (decl2.hostType() == hostType()) {
                return decl2.local;
            }
            return emitLoadLocalInNestedClass(b, decl2);
        } else if (v instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration) v;
            if (f.hostType().isArrayDecl() && f.name().equals(XMLConstants.LENGTH_ATTRIBUTE)) {
                return b.newLengthExpr(asImmediate(b, createLoadQualifier(b)), this);
            }
            if (f.isStatic()) {
                if (isQualified() && !qualifier().isTypeAccess()) {
                    b.newTemp(qualifier().eval(b));
                }
                if (requiresAccessor()) {
                    return b.newStaticInvokeExpr(f.createAccessor(fieldQualifierType()).sootRef(), new ArrayList(), this);
                }
                return b.newStaticFieldRef(sootRef(), this);
            } else if (requiresAccessor()) {
                Local base = base(b);
                ArrayList list = new ArrayList();
                list.add(base);
                return b.newStaticInvokeExpr(f.createAccessor(fieldQualifierType()).sootRef(), list, this);
            } else {
                Local base2 = createLoadQualifier(b);
                return b.newInstanceFieldRef(base2, sootRef(), this);
            }
        } else {
            return super.eval(b);
        }
    }

    public Value refined_Expressions_VarAccess_emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration) v;
            if (requiresAccessor()) {
                if (f.isStatic()) {
                    ArrayList list = new ArrayList();
                    list.add(rvalue);
                    return asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(fieldQualifierType()).sootRef(), list, location));
                }
                Local base = base(b);
                ArrayList list2 = new ArrayList();
                list2.add(base);
                list2.add(asLocal(b, rvalue, lvalue.getType()));
                return asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(fieldQualifierType()).sootRef(), list2, location));
            }
        }
        return super.emitStore(b, lvalue, rvalue, location);
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectTypesToSignatures(Collection<Type> set) {
        super.collectTypesToSignatures(set);
        if (decl() instanceof FieldDeclaration) {
            addDependencyIfNeeded(set, fieldQualifierType());
        }
    }

    public VarAccess() {
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.isFieldAccess_computed = false;
        this.type_computed = false;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public VarAccess(String p0) {
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.isFieldAccess_computed = false;
        this.type_computed = false;
        setID(p0);
    }

    public VarAccess(Symbol p0) {
        this.isConstant_visited = -1;
        this.isConstant_computed = false;
        this.isConstant_initialized = false;
        this.decls_computed = false;
        this.decl_computed = false;
        this.isFieldAccess_computed = false;
        this.type_computed = false;
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

    protected TypeDecl refined_GenericsCodegen_VarAccess_fieldQualifierType() {
        TypeDecl typeDecl = refined_InnerClasses_VarAccess_fieldQualifierType();
        if (typeDecl == null) {
            return null;
        }
        return typeDecl.erasure();
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        Value result;
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = ((FieldDeclaration) v).erasedField();
            if (f.hostType().isArrayDecl() && f.name().equals(XMLConstants.LENGTH_ATTRIBUTE)) {
                return b.newLengthExpr(asImmediate(b, createLoadQualifier(b)), this);
            }
            if (f.isStatic()) {
                if (isQualified() && !qualifier().isTypeAccess()) {
                    b.newTemp(qualifier().eval(b));
                }
                if (requiresAccessor()) {
                    result = b.newStaticInvokeExpr(f.createAccessor(fieldQualifierType().erasure()).sootRef(), new ArrayList(), this);
                } else {
                    result = b.newStaticFieldRef(sootRef(), this);
                }
            } else if (requiresAccessor()) {
                Local base = base(b);
                ArrayList list = new ArrayList();
                list.add(base);
                result = b.newStaticInvokeExpr(f.createAccessor(fieldQualifierType().erasure()).sootRef(), list, this);
            } else {
                Local base2 = createLoadQualifier(b);
                result = b.newInstanceFieldRef(base2, sootRef(), this);
            }
            if (f.type() != v.type()) {
                result = f.type().emitCastTo(b, result, v.type(), this);
            }
            return result;
        }
        return refined_Expressions_VarAccess_eval(b);
    }

    private SootFieldRef sootRef() {
        FieldDeclaration decl = ((FieldDeclaration) decl()).erasedField();
        SootFieldRef ref = Scene.v().makeFieldRef(fieldQualifierType().getSootClassDecl(), decl.name(), decl.type().getSootType(), decl.isStatic());
        return ref;
    }

    @Override // soot.JastAddJ.Expr
    public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = ((FieldDeclaration) v).erasedField();
            if (requiresAccessor()) {
                if (f.isStatic()) {
                    ArrayList list = new ArrayList();
                    list.add(rvalue);
                    return asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(fieldQualifierType().erasure()).sootRef(), list, this));
                }
                Local base = base(b);
                ArrayList list2 = new ArrayList();
                list2.add(base);
                list2.add(asLocal(b, rvalue, lvalue.getType()));
                return asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(fieldQualifierType().erasure()).sootRef(), list2, this));
            }
        }
        return refined_Expressions_VarAccess_emitStore(b, lvalue, rvalue, location);
    }

    public Local createLoadQualifier(Body b) {
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = ((FieldDeclaration) v).erasedField();
            if (hasPrevExpr()) {
                Local qualifier = asLocal(b, prevExpr().eval(b));
                return qualifier;
            } else if (f.isInstanceVariable()) {
                return emitThis(b, fieldQualifierType().erasure());
            }
        }
        throw new Error("createLoadQualifier not supported for " + v.getClass().getName());
    }

    protected TypeDecl fieldQualifierType() {
        TypeDecl typeDecl = refined_GenericsCodegen_VarAccess_fieldQualifierType();
        if (typeDecl != null) {
            return typeDecl;
        }
        return decl().hostType();
    }

    @Override // soot.JastAddJ.Expr
    public Constant constant() {
        state();
        return type().cast(decl().getInit().constant());
    }

    @Override // soot.JastAddJ.Expr
    public boolean isConstant() {
        if (this.isConstant_computed) {
            return this.isConstant_value;
        }
        ASTNode.State state = state();
        if (!this.isConstant_initialized) {
            this.isConstant_initialized = true;
            this.isConstant_value = false;
        }
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                this.isConstant_visited = state.CIRCLE_INDEX;
                state.CHANGE = false;
                boolean new_isConstant_value = isConstant_compute();
                if (new_isConstant_value != this.isConstant_value) {
                    state.CHANGE = true;
                }
                this.isConstant_value = new_isConstant_value;
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (isFinal && num == state().boundariesCrossed) {
                this.isConstant_computed = true;
            } else {
                state.RESET_CYCLE = true;
                isConstant_compute();
                state.RESET_CYCLE = false;
                this.isConstant_computed = false;
                this.isConstant_initialized = false;
            }
            state.IN_CIRCLE = false;
            return this.isConstant_value;
        } else if (this.isConstant_visited != state.CIRCLE_INDEX) {
            this.isConstant_visited = state.CIRCLE_INDEX;
            if (state.RESET_CYCLE) {
                this.isConstant_computed = false;
                this.isConstant_initialized = false;
                this.isConstant_visited = -1;
                return this.isConstant_value;
            }
            boolean new_isConstant_value2 = isConstant_compute();
            if (new_isConstant_value2 != this.isConstant_value) {
                state.CHANGE = true;
            }
            this.isConstant_value = new_isConstant_value2;
            return this.isConstant_value;
        } else {
            return this.isConstant_value;
        }
    }

    private boolean isConstant_compute() {
        Variable v = decl();
        if (v instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration) v;
            if (f.isConstant()) {
                if (isQualified()) {
                    return isQualified() && qualifier().isTypeAccess();
                }
                return true;
            }
            return false;
        }
        boolean result = v.isFinal() && v.hasInit() && v.getInit().isConstant() && (v.type().isPrimitive() || v.type().isString());
        if (result) {
            if (isQualified()) {
                return isQualified() && qualifier().isTypeAccess();
            }
            return true;
        }
        return false;
    }

    @Override // soot.JastAddJ.Expr
    public Variable varDecl() {
        state();
        return decl();
    }

    @Override // soot.JastAddJ.Expr
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
        return isDAbefore(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return isDUbefore(v);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean unassignedEverywhere(Variable v, TryStmt stmt) {
        state();
        if (isDest() && decl() == v && enclosingStmt().reachable()) {
            return false;
        }
        return super.unassignedEverywhere(v, stmt);
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
        SimpleSet set = lookupVariable(name());
        if (set.size() == 1) {
            Variable v = (Variable) set.iterator().next();
            if (!isQualified() && inStaticContext()) {
                if (v.isInstanceVariable() && !hostType().memberFields(v.name()).isEmpty()) {
                    return SimpleSet.emptySet;
                }
            } else if (isQualified() && qualifier().staticContextQualifier() && v.isInstanceVariable()) {
                return SimpleSet.emptySet;
            }
        }
        return set;
    }

    public Variable decl() {
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

    private Variable decl_compute() {
        SimpleSet decls = decls();
        if (decls.size() == 1) {
            return (Variable) decls.iterator().next();
        }
        return unknownField();
    }

    public boolean inSameInitializer() {
        state();
        BodyDecl b = closestBodyDecl(decl().hostType());
        if (b == null) {
            return false;
        }
        if ((b instanceof FieldDeclaration) && ((FieldDeclaration) b).isStatic() == decl().isStatic()) {
            return true;
        }
        if ((b instanceof InstanceInitializer) && !decl().isStatic()) {
            return true;
        }
        if ((b instanceof StaticInitializer) && decl().isStatic()) {
            return true;
        }
        return false;
    }

    public boolean simpleAssignment() {
        state();
        return isDest() && (getParent() instanceof AssignSimpleExpr);
    }

    public boolean inDeclaringClass() {
        state();
        return hostType() == decl().hostType();
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getID() + "]";
    }

    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isFieldAccess() {
        if (this.isFieldAccess_computed) {
            return this.isFieldAccess_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isFieldAccess_value = isFieldAccess_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isFieldAccess_computed = true;
        }
        return this.isFieldAccess_value;
    }

    private boolean isFieldAccess_compute() {
        return decl().isClassVariable() || decl().isInstanceVariable();
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

    @Override // soot.JastAddJ.Expr
    public boolean isVariable() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Expr
    public boolean isEnumConstant() {
        state();
        return varDecl() instanceof EnumConstant;
    }

    public boolean requiresAccessor() {
        state();
        Variable v = decl();
        if (!(v instanceof FieldDeclaration)) {
            return false;
        }
        FieldDeclaration f = (FieldDeclaration) v;
        if (f.isPrivate() && !hostType().hasField(v.name())) {
            return true;
        }
        if (f.isProtected() && !f.hostPackage().equals(hostPackage()) && !hostType().hasField(v.name())) {
            return true;
        }
        return false;
    }

    public Local base(Body b) {
        if (this.base_Body_values == null) {
            this.base_Body_values = new HashMap(4);
        }
        if (this.base_Body_values.containsKey(b)) {
            return (Local) this.base_Body_values.get(b);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Local base_Body_value = base_compute(b);
        if (isFinal && num == state().boundariesCrossed) {
            this.base_Body_values.put(b, base_Body_value);
        }
        return base_Body_value;
    }

    private Local base_compute(Body b) {
        return asLocal(b, createLoadQualifier(b));
    }

    @Override // soot.JastAddJ.Expr
    public Collection<TypeDecl> throwTypes() {
        state();
        return decl().throwTypes();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isVariable(Variable var) {
        state();
        return decl() == var;
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
