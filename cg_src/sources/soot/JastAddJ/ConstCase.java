package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ConstCase.class */
public class ConstCase extends Case implements Cloneable {
    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode, beaver.Symbol
    public ConstCase clone() throws CloneNotSupportedException {
        ConstCase node = (ConstCase) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ConstCase node = clone();
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
    public void nameCheck() {
        if (getValue().isConstant() && bind(this) != this) {
            error("constant expression " + getValue() + " is multiply declared in two case statements");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        s.append("case ");
        getValue().toString(s);
        s.append(":");
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        if ((getValue() instanceof VarAccess) && (getValue().varDecl() instanceof EnumConstant)) {
            int i = hostType().createEnumIndex((EnumConstant) getValue().varDecl());
            setValue(new IntegerLiteral(new Integer(i).toString()));
        }
        super.transformation();
    }

    public ConstCase() {
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ConstCase(Expr p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setValue(Expr node) {
        setChild(node, 0);
    }

    public Expr getValue() {
        return (Expr) getChild(0);
    }

    public Expr getValueNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    public void refined_Enums_ConstCase_typeCheck() {
        boolean isEnumConstant = getValue().isEnumConstant();
        if (switchType().isEnumDecl() && !isEnumConstant) {
            error("Unqualified enumeration constant required");
            return;
        }
        TypeDecl switchType = switchType();
        TypeDecl type = getValue().type();
        if (!type.assignConversionTo(switchType, getValue())) {
            error("Constant expression must be assignable to Expression");
        }
        if (!getValue().isConstant() && !getValue().type().isUnknown() && !isEnumConstant) {
            error("Switch expression must be constant");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        boolean isEnumConstant = getValue().isEnumConstant();
        TypeDecl switchType = switchType();
        TypeDecl type = getValue().type();
        if (switchType.isEnumDecl() && !isEnumConstant) {
            error("Unqualified enumeration constant required");
        }
        if (!type.assignConversionTo(switchType, getValue())) {
            error("Case label has incompatible type " + switchType.name() + ", expected type compatible with " + type.name());
        }
        if (!getValue().isConstant() && !getValue().type().isUnknown() && !isEnumConstant) {
            error("Case label must have constant expression");
        }
    }

    private boolean refined_NameCheck_ConstCase_constValue_Case(Case c) {
        return (c instanceof ConstCase) && getValue().isConstant() && getValue().type().assignableToInt() && ((ConstCase) c).getValue().type().assignableToInt() && getValue().constant().intValue() == ((ConstCase) c).getValue().constant().intValue();
    }

    private boolean refined_Enums_ConstCase_constValue_Case(Case c) {
        if (switchType().isEnumDecl()) {
            return (c instanceof ConstCase) && getValue().isConstant() && getValue().varDecl() == ((ConstCase) c).getValue().varDecl();
        }
        return refined_NameCheck_ConstCase_constValue_Case(c);
    }

    @Override // soot.JastAddJ.Case
    public boolean constValue(Case c) {
        state();
        if (isDefaultCase() || c.isDefaultCase()) {
            return isDefaultCase() && c.isDefaultCase();
        }
        Expr myValue = getValue();
        Expr otherValue = ((ConstCase) c).getValue();
        TypeDecl myType = myValue.type();
        TypeDecl otherType = otherValue.type();
        if (myType.isString() || otherType.isString()) {
            if (!myType.isString() || !otherType.isString() || !myValue.isConstant() || !otherValue.isConstant()) {
                return false;
            }
            return myValue.constant().stringValue().equals(otherValue.constant().stringValue());
        }
        return refined_Enums_ConstCase_constValue_Case(c);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getValueNoTransform()) {
            return switchType().isEnumDecl() ? switchType().memberFields(name) : lookupVariable(name);
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.Case, soot.JastAddJ.Stmt, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
