package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PackageOrTypeAccess.class */
public class PackageOrTypeAccess extends Access implements Cloneable {
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PackageOrTypeAccess clone() throws CloneNotSupportedException {
        PackageOrTypeAccess node = (PackageOrTypeAccess) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            PackageOrTypeAccess node = clone();
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
        error("packageortype name " + name());
    }

    public PackageOrTypeAccess(String name, int start, int end) {
        this(name);
        this.IDstart = start;
        this.start = start;
        this.IDend = end;
        this.end = end;
    }

    public PackageOrTypeAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public PackageOrTypeAccess(String p0) {
        setID(p0);
    }

    public PackageOrTypeAccess(Symbol p0) {
        setID(p0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
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

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupType(String name) {
        state();
        return SimpleSet.emptySet;
    }

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupVariable(String name) {
        state();
        return SimpleSet.emptySet;
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
    public String packageName() {
        state();
        StringBuffer s = new StringBuffer();
        if (hasPrevExpr()) {
            s.append(prevExpr().packageName());
            s.append(".");
        }
        s.append(name());
        return s.toString();
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.PACKAGE_OR_TYPE_NAME;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (!duringSyntacticClassification()) {
            state().duringNameResolution++;
            ASTNode result = rewriteRule0();
            state().duringNameResolution--;
            return result;
        }
        return super.rewriteTo();
    }

    private Access rewriteRule0() {
        if (!lookupType(name()).isEmpty()) {
            return new TypeAccess(name(), start(), end());
        }
        return new PackageAccess(name(), start(), end());
    }
}
