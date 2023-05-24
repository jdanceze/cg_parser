package soot.JastAddJ;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Type;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Expr.class */
public abstract class Expr extends ASTNode<ASTNode> implements Cloneable {
    protected soot.jimple.Stmt false_label_value;
    protected soot.jimple.Stmt true_label_value;
    protected boolean false_label_computed = false;
    protected boolean true_label_computed = false;

    public abstract TypeDecl type();

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.false_label_computed = false;
        this.false_label_value = null;
        this.true_label_computed = false;
        this.true_label_value = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public Expr clone() throws CloneNotSupportedException {
        Expr node = (Expr) super.mo287clone();
        node.false_label_computed = false;
        node.false_label_value = null;
        node.true_label_computed = false;
        node.true_label_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public SimpleSet keepAccessibleTypes(SimpleSet oldSet) {
        SimpleSet newSet = SimpleSet.emptySet;
        TypeDecl hostType = hostType();
        Iterator iter = oldSet.iterator();
        while (iter.hasNext()) {
            TypeDecl t = (TypeDecl) iter.next();
            if ((hostType != null && t.accessibleFrom(hostType)) || (hostType == null && t.accessibleFromPackage(hostPackage()))) {
                newSet = newSet.add(t);
            }
        }
        return newSet;
    }

    public SimpleSet keepAccessibleFields(SimpleSet oldSet) {
        SimpleSet newSet = SimpleSet.emptySet;
        Iterator iter = oldSet.iterator();
        while (iter.hasNext()) {
            Variable v = (Variable) iter.next();
            if (v instanceof FieldDeclaration) {
                FieldDeclaration f = (FieldDeclaration) v;
                if (mayAccess(f)) {
                    newSet = newSet.add(f);
                }
            }
        }
        return newSet;
    }

    public boolean mayAccess(FieldDeclaration f) {
        if (f.isPublic()) {
            return true;
        }
        if (f.isProtected()) {
            if (f.hostPackage().equals(hostPackage())) {
                return true;
            }
            return hostType().mayAccess(this, f);
        } else if (f.isPrivate()) {
            return f.hostType().topLevelType() == hostType().topLevelType();
        } else {
            return f.hostPackage().equals(hostType().hostPackage());
        }
    }

    public Dot qualifiesAccess(Access access) {
        Dot dot = new Dot(this, access);
        dot.setStart(getStart());
        dot.setEnd(access.getEnd());
        return dot;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleSet chooseConstructor(Collection constructors, List argList) {
        SimpleSet<ConstructorDecl> potentiallyApplicable = SimpleSet.emptySet;
        Iterator iter = constructors.iterator();
        while (iter.hasNext()) {
            ConstructorDecl decl = (ConstructorDecl) iter.next();
            if (decl.potentiallyApplicable(argList) && decl.accessibleFrom(hostType())) {
                potentiallyApplicable = potentiallyApplicable.add(decl);
            }
        }
        SimpleSet maxSpecific = SimpleSet.emptySet;
        for (ConstructorDecl decl2 : potentiallyApplicable) {
            if (decl2.applicableBySubtyping(argList)) {
                maxSpecific = mostSpecific(maxSpecific, decl2);
            }
        }
        if (maxSpecific.isEmpty()) {
            for (ConstructorDecl decl3 : potentiallyApplicable) {
                if (decl3.applicableByMethodInvocationConversion(argList)) {
                    maxSpecific = mostSpecific(maxSpecific, decl3);
                }
            }
        }
        if (maxSpecific.isEmpty()) {
            for (ConstructorDecl decl4 : potentiallyApplicable) {
                if (decl4.isVariableArity() && decl4.applicableVariableArity(argList)) {
                    maxSpecific = mostSpecific(maxSpecific, decl4);
                }
            }
        }
        return maxSpecific;
    }

    protected static SimpleSet mostSpecific(SimpleSet maxSpecific, ConstructorDecl decl) {
        if (maxSpecific.isEmpty()) {
            maxSpecific = maxSpecific.add(decl);
        } else if (decl.moreSpecificThan((ConstructorDecl) maxSpecific.iterator().next())) {
            maxSpecific = SimpleSet.emptySet.add(decl);
        } else if (!((ConstructorDecl) maxSpecific.iterator().next()).moreSpecificThan(decl)) {
            maxSpecific = maxSpecific.add(decl);
        }
        return maxSpecific;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Value emitBooleanCondition(Body b) {
        b.setLine(this);
        emitEvalBranch(b);
        soot.jimple.Stmt end_label = newLabel();
        b.addLabel(false_label());
        Local result = b.newTemp(soot.BooleanType.v());
        b.add(b.newAssignStmt(result, BooleanType.emitConstant(false), this));
        b.add(b.newGotoStmt(end_label, this));
        b.addLabel(true_label());
        b.add(b.newAssignStmt(result, BooleanType.emitConstant(true), this));
        b.addLabel(end_label);
        return result;
    }

    public void refined_BooleanExpressions_Expr_emitEvalBranch(Body b) {
        b.setLine(this);
        if (isTrue()) {
            b.add(b.newGotoStmt(true_label(), this));
        } else if (isFalse()) {
            b.add(b.newGotoStmt(false_label(), this));
        } else {
            b.add(b.newIfStmt(b.newEqExpr(asImmediate(b, eval(b)), BooleanType.emitConstant(false), this), false_label(), this));
            b.add(b.newGotoStmt(true_label(), this));
        }
    }

    public Value eval(Body b) {
        throw new Error("Operation eval not supported for " + getClass().getName());
    }

    public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
        b.setLine(this);
        b.add(b.newAssignStmt(lvalue, asLocal(b, rvalue, lvalue.getType()), location));
        return rvalue;
    }

    @Override // soot.JastAddJ.ASTNode
    public void collectTypesToHierarchy(Collection<Type> set) {
        super.collectTypesToHierarchy(set);
        addDependencyIfNeeded(set, type());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addDependencyIfNeeded(Collection<Type> set, TypeDecl type) {
        TypeDecl type2 = type.elementType().erasure();
        if (type2.isReferenceType() && !type2.isUnknown()) {
            set.add(type2.getSootType());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void emitEvalBranch(Body b) {
        if (type().isReferenceType()) {
            b.setLine(this);
            b.add(b.newIfStmt(b.newEqExpr(asImmediate(b, type().emitUnboxingOperation(b, eval(b), this)), BooleanType.emitConstant(false), this), false_label(), this));
            b.add(b.newGotoStmt(true_label(), this));
            return;
        }
        refined_BooleanExpressions_Expr_emitEvalBranch(b);
    }

    public Constant constant() {
        state();
        throw new UnsupportedOperationException("ConstantExpression operation constant not supported for type " + getClass().getName());
    }

    public boolean isPositive() {
        state();
        return false;
    }

    public boolean representableIn(TypeDecl t) {
        state();
        if (type().isByte() || type().isChar() || type().isShort() || type().isInt()) {
            return t.isByte() ? constant().intValue() >= -128 && constant().intValue() <= 127 : t.isChar() ? constant().intValue() >= 0 && constant().intValue() <= 65535 : t.isShort() ? constant().intValue() >= -32768 && constant().intValue() <= 32767 : t.isInt() && constant().intValue() >= Integer.MIN_VALUE && constant().intValue() <= Integer.MAX_VALUE;
        }
        return false;
    }

    public boolean isConstant() {
        state();
        return false;
    }

    public boolean isTrue() {
        state();
        return isConstant() && (type() instanceof BooleanType) && constant().booleanValue();
    }

    public boolean isFalse() {
        state();
        return isConstant() && (type() instanceof BooleanType) && !constant().booleanValue();
    }

    public Variable varDecl() {
        state();
        return null;
    }

    public boolean isDAafterFalse(Variable v) {
        state();
        return isTrue() || isDAbefore(v);
    }

    public boolean isDAafterTrue(Variable v) {
        state();
        return isFalse() || isDAbefore(v);
    }

    public boolean isDAafter(Variable v) {
        state();
        return (isDAafterFalse(v) && isDAafterTrue(v)) || isDAbefore(v);
    }

    public boolean isDUafterFalse(Variable v) {
        state();
        if (isTrue()) {
            return true;
        }
        return isDUbefore(v);
    }

    public boolean isDUafterTrue(Variable v) {
        state();
        if (isFalse()) {
            return true;
        }
        return isDUbefore(v);
    }

    public boolean isDUafter(Variable v) {
        state();
        return (isDUafterFalse(v) && isDUafterTrue(v)) || isDUbefore(v);
    }

    public SimpleSet mostSpecificConstructor(Collection constructors) {
        state();
        SimpleSet maxSpecific = SimpleSet.emptySet;
        Iterator iter = constructors.iterator();
        while (iter.hasNext()) {
            ConstructorDecl decl = (ConstructorDecl) iter.next();
            if (applicableAndAccessible(decl)) {
                if (maxSpecific.isEmpty()) {
                    maxSpecific = maxSpecific.add(decl);
                } else if (decl.moreSpecificThan((ConstructorDecl) maxSpecific.iterator().next())) {
                    maxSpecific = SimpleSet.emptySet.add(decl);
                } else if (!((ConstructorDecl) maxSpecific.iterator().next()).moreSpecificThan(decl)) {
                    maxSpecific = maxSpecific.add(decl);
                }
            }
        }
        return maxSpecific;
    }

    public boolean applicableAndAccessible(ConstructorDecl decl) {
        state();
        return false;
    }

    public boolean hasQualifiedPackage(String packageName) {
        state();
        return false;
    }

    public SimpleSet qualifiedLookupType(String name) {
        state();
        return keepAccessibleTypes(type().memberTypes(name));
    }

    public SimpleSet qualifiedLookupVariable(String name) {
        state();
        if (type().accessibleFrom(hostType())) {
            return keepAccessibleFields(type().memberFields(name));
        }
        return SimpleSet.emptySet;
    }

    public String packageName() {
        state();
        return "";
    }

    public String typeName() {
        state();
        return "";
    }

    public boolean isTypeAccess() {
        state();
        return false;
    }

    public boolean isMethodAccess() {
        state();
        return false;
    }

    public boolean isFieldAccess() {
        state();
        return false;
    }

    public boolean isSuperAccess() {
        state();
        return false;
    }

    public boolean isThisAccess() {
        state();
        return false;
    }

    public boolean isPackageAccess() {
        state();
        return false;
    }

    public boolean isArrayAccess() {
        state();
        return false;
    }

    public boolean isClassAccess() {
        state();
        return false;
    }

    public boolean isSuperConstructorAccess() {
        state();
        return false;
    }

    public boolean isLeftChildOfDot() {
        state();
        return hasParentDot() && parentDot().getLeft() == this;
    }

    public boolean isRightChildOfDot() {
        state();
        return hasParentDot() && parentDot().getRight() == this;
    }

    public AbstractDot parentDot() {
        state();
        if (getParent() instanceof AbstractDot) {
            return (AbstractDot) getParent();
        }
        return null;
    }

    public boolean hasParentDot() {
        state();
        return parentDot() != null;
    }

    public Access nextAccess() {
        state();
        return parentDot().nextAccess();
    }

    public boolean hasNextAccess() {
        state();
        return isLeftChildOfDot();
    }

    public Stmt enclosingStmt() {
        ASTNode node;
        state();
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if (node == null || (node instanceof Stmt)) {
                break;
            }
            aSTNode = node.getParent();
        }
        return (Stmt) node;
    }

    public boolean isVariable() {
        state();
        return false;
    }

    public boolean isUnknown() {
        state();
        return type().isUnknown();
    }

    public boolean staticContextQualifier() {
        state();
        return false;
    }

    public boolean isEnumConstant() {
        state();
        return false;
    }

    public soot.jimple.Stmt false_label() {
        if (this.false_label_computed) {
            return this.false_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.false_label_value = false_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.false_label_computed = true;
        }
        return this.false_label_value;
    }

    private soot.jimple.Stmt false_label_compute() {
        return getParent().definesLabel() ? condition_false_label() : newLabel();
    }

    public soot.jimple.Stmt true_label() {
        if (this.true_label_computed) {
            return this.true_label_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.true_label_value = true_label_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.true_label_computed = true;
        }
        return this.true_label_value;
    }

    private soot.jimple.Stmt true_label_compute() {
        return getParent().definesLabel() ? condition_true_label() : newLabel();
    }

    public boolean canBeTrue() {
        state();
        return !isFalse();
    }

    public boolean canBeFalse() {
        state();
        return !isTrue();
    }

    public Collection<TypeDecl> throwTypes() {
        state();
        Collection<TypeDecl> tts = new LinkedList<>();
        tts.add(type());
        return tts;
    }

    public boolean modifiedInScope(Variable var) {
        state();
        return false;
    }

    public boolean isVariable(Variable var) {
        state();
        return false;
    }

    public boolean isDest() {
        state();
        boolean isDest_value = getParent().Define_boolean_isDest(this, null);
        return isDest_value;
    }

    public boolean isSource() {
        state();
        boolean isSource_value = getParent().Define_boolean_isSource(this, null);
        return isSource_value;
    }

    public boolean isIncOrDec() {
        state();
        boolean isIncOrDec_value = getParent().Define_boolean_isIncOrDec(this, null);
        return isIncOrDec_value;
    }

    public boolean isDAbefore(Variable v) {
        state();
        boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);
        return isDAbefore_Variable_value;
    }

    public boolean isDUbefore(Variable v) {
        state();
        boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
        return isDUbefore_Variable_value;
    }

    public Collection lookupMethod(String name) {
        state();
        Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);
        return lookupMethod_String_value;
    }

    public TypeDecl typeBoolean() {
        state();
        TypeDecl typeBoolean_value = getParent().Define_TypeDecl_typeBoolean(this, null);
        return typeBoolean_value;
    }

    public TypeDecl typeByte() {
        state();
        TypeDecl typeByte_value = getParent().Define_TypeDecl_typeByte(this, null);
        return typeByte_value;
    }

    public TypeDecl typeShort() {
        state();
        TypeDecl typeShort_value = getParent().Define_TypeDecl_typeShort(this, null);
        return typeShort_value;
    }

    public TypeDecl typeChar() {
        state();
        TypeDecl typeChar_value = getParent().Define_TypeDecl_typeChar(this, null);
        return typeChar_value;
    }

    public TypeDecl typeInt() {
        state();
        TypeDecl typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);
        return typeInt_value;
    }

    public TypeDecl typeLong() {
        state();
        TypeDecl typeLong_value = getParent().Define_TypeDecl_typeLong(this, null);
        return typeLong_value;
    }

    public TypeDecl typeFloat() {
        state();
        TypeDecl typeFloat_value = getParent().Define_TypeDecl_typeFloat(this, null);
        return typeFloat_value;
    }

    public TypeDecl typeDouble() {
        state();
        TypeDecl typeDouble_value = getParent().Define_TypeDecl_typeDouble(this, null);
        return typeDouble_value;
    }

    public TypeDecl typeString() {
        state();
        TypeDecl typeString_value = getParent().Define_TypeDecl_typeString(this, null);
        return typeString_value;
    }

    public TypeDecl typeVoid() {
        state();
        TypeDecl typeVoid_value = getParent().Define_TypeDecl_typeVoid(this, null);
        return typeVoid_value;
    }

    public TypeDecl typeNull() {
        state();
        TypeDecl typeNull_value = getParent().Define_TypeDecl_typeNull(this, null);
        return typeNull_value;
    }

    public TypeDecl unknownType() {
        state();
        TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        return unknownType_value;
    }

    public boolean hasPackage(String packageName) {
        state();
        boolean hasPackage_String_value = getParent().Define_boolean_hasPackage(this, null, packageName);
        return hasPackage_String_value;
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    public SimpleSet lookupType(String name) {
        state();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        return lookupType_String_value;
    }

    public SimpleSet lookupVariable(String name) {
        state();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        return lookupVariable_String_value;
    }

    public NameType nameType() {
        state();
        NameType nameType_value = getParent().Define_NameType_nameType(this, null);
        return nameType_value;
    }

    public BodyDecl enclosingBodyDecl() {
        state();
        BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);
        return enclosingBodyDecl_value;
    }

    public String hostPackage() {
        state();
        String hostPackage_value = getParent().Define_String_hostPackage(this, null);
        return hostPackage_value;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    public String methodHost() {
        state();
        String methodHost_value = getParent().Define_String_methodHost(this, null);
        return methodHost_value;
    }

    public boolean inStaticContext() {
        state();
        boolean inStaticContext_value = getParent().Define_boolean_inStaticContext(this, null);
        return inStaticContext_value;
    }

    public TypeDecl assignConvertedType() {
        state();
        TypeDecl assignConvertedType_value = getParent().Define_TypeDecl_assignConvertedType(this, null);
        return assignConvertedType_value;
    }

    public boolean inExtendsOrImplements() {
        state();
        boolean inExtendsOrImplements_value = getParent().Define_boolean_inExtendsOrImplements(this, null);
        return inExtendsOrImplements_value;
    }

    public soot.jimple.Stmt condition_false_label() {
        state();
        soot.jimple.Stmt condition_false_label_value = getParent().Define_soot_jimple_Stmt_condition_false_label(this, null);
        return condition_false_label_value;
    }

    public soot.jimple.Stmt condition_true_label() {
        state();
        soot.jimple.Stmt condition_true_label_value = getParent().Define_soot_jimple_Stmt_condition_true_label(this, null);
        return condition_true_label_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
