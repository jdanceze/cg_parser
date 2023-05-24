package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/NumericType.class */
public abstract class NumericType extends PrimitiveType implements Cloneable {
    protected boolean unaryNumericPromotion_computed = false;
    protected TypeDecl unaryNumericPromotion_value;
    protected Map binaryNumericPromotion_TypeDecl_values;

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.unaryNumericPromotion_computed = false;
        this.unaryNumericPromotion_value = null;
        this.binaryNumericPromotion_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public NumericType clone() throws CloneNotSupportedException {
        NumericType node = (NumericType) super.clone();
        node.unaryNumericPromotion_computed = false;
        node.unaryNumericPromotion_value = null;
        node.binaryNumericPromotion_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.TypeDecl
    public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
        if (type.isUnknown()) {
            throw new Error("Trying to cast to Unknown");
        }
        if (type == this) {
            return v;
        }
        if ((isLong() || (this instanceof FloatingPointType)) && type.isIntegralType()) {
            return typeInt().emitCastTo(b, b.newCastExpr(asImmediate(b, v), typeInt().getSootType(), location), type, location);
        } else if (type instanceof NumericType) {
            return b.newCastExpr(asImmediate(b, v), type.getSootType(), location);
        } else {
            if (!type.isNumericType()) {
                return emitCastTo(b, v, boxed(), location);
            }
            return boxed().emitBoxingOperation(b, emitCastTo(b, v, type.unboxed(), location), location);
        }
    }

    public NumericType() {
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
    }

    public NumericType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public NumericType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.PrimitiveType
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 1);
    }

    @Override // soot.JastAddJ.PrimitiveType
    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.PrimitiveType
    public Access getSuperClassAccess() {
        return getSuperClassAccessOpt().getChild(0);
    }

    @Override // soot.JastAddJ.PrimitiveType
    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOpt() {
        return (Opt) getChild(1);
    }

    @Override // soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    private TypeDecl refined_NumericPromotion_NumericType_binaryNumericPromotion_TypeDecl(TypeDecl type) {
        if (type.isNumericType()) {
            return unaryNumericPromotion().instanceOf(type) ? type : unaryNumericPromotion();
        }
        return unknownType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl unaryNumericPromotion() {
        if (this.unaryNumericPromotion_computed) {
            return this.unaryNumericPromotion_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unaryNumericPromotion_value = unaryNumericPromotion_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unaryNumericPromotion_computed = true;
        }
        return this.unaryNumericPromotion_value;
    }

    private TypeDecl unaryNumericPromotion_compute() {
        return this;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl binaryNumericPromotion(TypeDecl type) {
        if (this.binaryNumericPromotion_TypeDecl_values == null) {
            this.binaryNumericPromotion_TypeDecl_values = new HashMap(4);
        }
        if (this.binaryNumericPromotion_TypeDecl_values.containsKey(type)) {
            return (TypeDecl) this.binaryNumericPromotion_TypeDecl_values.get(type);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        TypeDecl binaryNumericPromotion_TypeDecl_value = binaryNumericPromotion_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.binaryNumericPromotion_TypeDecl_values.put(type, binaryNumericPromotion_TypeDecl_value);
        }
        return binaryNumericPromotion_TypeDecl_value;
    }

    private TypeDecl binaryNumericPromotion_compute(TypeDecl type) {
        if (type.isReferenceType()) {
            type = type.unboxed();
        }
        return refined_NumericPromotion_NumericType_binaryNumericPromotion_TypeDecl(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isNumericType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
