package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/IntegralType.class */
public abstract class IntegralType extends NumericType implements Cloneable {
    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public IntegralType clone() throws CloneNotSupportedException {
        IntegralType node = (IntegralType) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public IntegralType() {
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 1);
        setChild(new List(), 2);
    }

    public IntegralType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public IntegralType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public void setSuperClassAccessOpt(Opt<Access> opt) {
        setChild(opt, 1);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public boolean hasSuperClassAccess() {
        return getSuperClassAccessOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Access getSuperClassAccess() {
        return getSuperClassAccessOpt().getChild(0);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public void setSuperClassAccess(Access node) {
        getSuperClassAccessOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOpt() {
        return (Opt) getChild(1);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType
    public Opt<Access> getSuperClassAccessOptNoTransform() {
        return (Opt) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant cast(Constant c) {
        state();
        return Constant.create(c.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant plus(Constant c) {
        state();
        return c;
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant minus(Constant c) {
        state();
        return Constant.create(-c.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant bitNot(Constant c) {
        state();
        return Constant.create(c.intValue() ^ (-1));
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant mul(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() * c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant div(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() / c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant mod(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() % c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant add(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() + c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant sub(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() - c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant lshift(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() << c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant rshift(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() >> c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant urshift(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() >>> c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant andBitwise(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() & c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant xorBitwise(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() ^ c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant orBitwise(Constant c1, Constant c2) {
        state();
        return Constant.create(c1.intValue() | c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public Constant questionColon(Constant cond, Constant c1, Constant c2) {
        state();
        return Constant.create(cond.booleanValue() ? c1.intValue() : c2.intValue());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean eqIsTrue(Expr left, Expr right) {
        state();
        return left.constant().intValue() == right.constant().intValue();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean ltIsTrue(Expr left, Expr right) {
        state();
        return left.constant().intValue() < right.constant().intValue();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean leIsTrue(Expr left, Expr right) {
        state();
        return left.constant().intValue() <= right.constant().intValue();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean assignableToInt() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isIntegralType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.NumericType, soot.JastAddJ.PrimitiveType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
