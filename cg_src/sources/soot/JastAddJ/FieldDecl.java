package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/FieldDecl.class */
public class FieldDecl extends MemberDecl implements Cloneable {
    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public FieldDecl clone() throws CloneNotSupportedException {
        FieldDecl node = (FieldDecl) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            FieldDecl node = clone();
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

    public FieldDecl() {
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 2);
    }

    public FieldDecl(Modifiers p0, Access p1, List<VariableDecl> p2) {
        setChild(p0, 0);
        setChild(p1, 1);
        setChild(p2, 2);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
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

    public void setVariableDeclList(List<VariableDecl> list) {
        setChild(list, 2);
    }

    public int getNumVariableDecl() {
        return getVariableDeclList().getNumChild();
    }

    public int getNumVariableDeclNoTransform() {
        return getVariableDeclListNoTransform().getNumChildNoTransform();
    }

    public VariableDecl getVariableDecl(int i) {
        return getVariableDeclList().getChild(i);
    }

    public void addVariableDecl(VariableDecl node) {
        List<VariableDecl> list = (this.parent == null || state == null) ? getVariableDeclListNoTransform() : getVariableDeclList();
        list.addChild(node);
    }

    public void addVariableDeclNoTransform(VariableDecl node) {
        List<VariableDecl> list = getVariableDeclListNoTransform();
        list.addChild(node);
    }

    public void setVariableDecl(VariableDecl node, int i) {
        List<VariableDecl> list = getVariableDeclList();
        list.setChild(node, i);
    }

    public List<VariableDecl> getVariableDecls() {
        return getVariableDeclList();
    }

    public List<VariableDecl> getVariableDeclsNoTransform() {
        return getVariableDeclListNoTransform();
    }

    public List<VariableDecl> getVariableDeclList() {
        List<VariableDecl> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<VariableDecl> getVariableDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return getModifiers().isStatic();
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
        if (caller == getVariableDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return null;
        }
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v29, types: [soot.JastAddJ.ASTNode] */
    /* JADX WARN: Type inference failed for: r1v15, types: [soot.JastAddJ.ASTNode] */
    @Override // soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (getNumVariableDecl() == 1) {
            state().duringVariableDeclarationTransformation++;
            ASTNode result = rewriteRule0();
            state().duringVariableDeclarationTransformation--;
            return result;
        } else if ((getParent().getParent() instanceof TypeDecl) && ((TypeDecl) getParent().getParent()).getBodyDeclListNoTransform() == getParent() && getNumVariableDecl() > 1) {
            state().duringVariableDeclarationTransformation++;
            List list = (List) getParent();
            int i = list.getIndexOfChild(this);
            List newList = rewriteTypeDecl_getBodyDecl();
            for (int j = 1; j < newList.getNumChildNoTransform(); j++) {
                i++;
                list.insertChild(newList.getChildNoTransform(j), i);
            }
            state().duringVariableDeclarationTransformation--;
            return newList.getChildNoTransform(0);
        } else {
            return super.rewriteTo();
        }
    }

    private FieldDeclaration rewriteRule0() {
        FieldDeclaration decl = getVariableDecl(0).createFieldDeclarationFrom(getModifiers(), getTypeAccess());
        decl.setStart(this.start);
        decl.setEnd(this.end);
        return decl;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [soot.JastAddJ.Modifiers] */
    private List rewriteTypeDecl_getBodyDecl() {
        List varList = new List();
        for (int j = 0; j < getNumVariableDecl(); j++) {
            FieldDeclaration f = getVariableDecl(j).createFieldDeclarationFrom(getModifiers().fullCopy2(), (Access) getTypeAccess().fullCopy());
            if (j == 0) {
                f.setStart(this.start);
            } else {
                f.getModifiersNoTransform().clearLocations();
                f.getTypeAccessNoTransform().clearLocations();
            }
            f.setFieldDecl(this);
            varList.add(f);
        }
        return varList;
    }
}
