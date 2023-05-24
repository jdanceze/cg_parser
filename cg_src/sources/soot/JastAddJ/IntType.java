package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
import soot.Type;
import soot.Value;
import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/IntType.class */
public class IntType extends IntegralType implements Cloneable {
    protected TypeDecl boxed_value;
    protected String jvmName_value;
    protected Type getSootType_value;
    protected boolean boxed_computed = false;
    protected boolean jvmName_computed = false;
    protected boolean getSootType_computed = false;

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
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
    public IntType clone() throws CloneNotSupportedException {
        IntType node = (IntType) super.clone();
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
            IntType node = clone();
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
        s.append("int");
    }

    public static Value emitConstant(int i) {
        return IntConstant.v(i);
    }

    public IntType() {
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
    }

    public IntType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public IntType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
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

    @Override // soot.JastAddJ.TypeDecl
    public boolean isInt() {
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
        return lookupType("java.lang", "Integer");
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
        return "I";
    }

    @Override // soot.JastAddJ.TypeDecl
    public String primitiveClassName() {
        state();
        return "Integer";
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
        return soot.IntType.v();
    }

    @Override // soot.JastAddJ.IntegralType, soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
