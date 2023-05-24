package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ShortType.class */
public class ShortType extends IntegralType implements Cloneable {
    protected Map narrowingConversionTo_TypeDecl_values;
    protected TypeDecl unaryNumericPromotion_value;
    protected TypeDecl boxed_value;
    protected String jvmName_value;
    protected Type getSootType_value;
    protected boolean unaryNumericPromotion_computed = false;
    protected boolean boxed_computed = false;
    protected boolean jvmName_computed = false;
    protected boolean getSootType_computed = false;

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.narrowingConversionTo_TypeDecl_values = null;
        this.unaryNumericPromotion_computed = false;
        this.unaryNumericPromotion_value = null;
        this.boxed_computed = false;
        this.boxed_value = null;
        this.jvmName_computed = false;
        this.jvmName_value = null;
        this.getSootType_computed = false;
        this.getSootType_value = null;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ShortType clone() throws CloneNotSupportedException {
        ShortType node = (ShortType) super.clone();
        node.narrowingConversionTo_TypeDecl_values = null;
        node.unaryNumericPromotion_computed = false;
        node.unaryNumericPromotion_value = null;
        node.boxed_computed = false;
        node.boxed_value = null;
        node.jvmName_computed = false;
        node.jvmName_value = null;
        node.getSootType_computed = false;
        node.getSootType_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ShortType node = clone();
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
    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.ASTNode
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
    public void toString(StringBuffer s) {
        s.append("short");
    }

    public ShortType() {
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
    }

    public ShortType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public ShortType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 1);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Access getSuperClassAccess() {
        return getSuperClassAccessOpt().getChild(0);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOpt() {
        return (Opt) getChild(1);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.TypeDecl
    public Constant cast(Constant c) {
        state();
        return Constant.create((int) ((short) c.intValue()));
    }

    @Override // soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public boolean narrowingConversionTo(TypeDecl type) {
        if (this.narrowingConversionTo_TypeDecl_values == null) {
            this.narrowingConversionTo_TypeDecl_values = new HashMap(4);
        }
        if (this.narrowingConversionTo_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.narrowingConversionTo_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean narrowingConversionTo_TypeDecl_value = narrowingConversionTo_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.narrowingConversionTo_TypeDecl_values.put(type, Boolean.valueOf(narrowingConversionTo_TypeDecl_value));
        }
        return narrowingConversionTo_TypeDecl_value;
    }

    private boolean narrowingConversionTo_compute(TypeDecl type) {
        return type.isByte() || type.isChar();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.TypeDecl
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
        return typeInt();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isShort() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl boxed() {
        if (this.boxed_computed) {
            return this.boxed_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.boxed_value = boxed_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.boxed_computed = true;
        }
        return this.boxed_value;
    }

    private TypeDecl boxed_compute() {
        return lookupType("java.lang", "Short");
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl stringPromotion() {
        state();
        return typeInt();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String jvmName() {
        if (this.jvmName_computed) {
            return this.jvmName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.jvmName_value = jvmName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.jvmName_computed = true;
        }
        return this.jvmName_value;
    }

    private String jvmName_compute() {
        return "S";
    }

    @Override // soot.JastAddJ.TypeDecl
    public String primitiveClassName() {
        state();
        return "Short";
    }

    @Override // soot.JastAddJ.TypeDecl
    public Type getSootType() {
        if (this.getSootType_computed) {
            return this.getSootType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSootType_value = getSootType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.getSootType_computed = true;
        }
        return this.getSootType_value;
    }

    private Type getSootType_compute() {
        return soot.ShortType.v();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
