package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PackageAccess.class */
public class PackageAccess extends Access implements Cloneable {
    protected String tokenString_Package;
    public int Packagestart;
    public int Packageend;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PackageAccess clone() throws CloneNotSupportedException {
        PackageAccess node = (PackageAccess) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            PackageAccess node = clone();
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
        if (!hasPackage(packageName())) {
            error(String.valueOf(packageName()) + " not found");
        }
    }

    public PackageAccess(String name, int start, int end) {
        this(name);
        this.Packagestart = start;
        this.start = start;
        this.Packageend = end;
        this.end = end;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(getPackage());
    }

    public PackageAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public PackageAccess(String p0) {
        setPackage(p0);
    }

    public PackageAccess(Symbol p0) {
        setPackage(p0);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    public void setPackage(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
        }
        this.tokenString_Package = (String) symbol.value;
        this.Packagestart = symbol.getStart();
        this.Packageend = symbol.getEnd();
    }

    public String getPackage() {
        return this.tokenString_Package != null ? this.tokenString_Package : "";
    }

    @Override // soot.JastAddJ.Expr
    public boolean hasQualifiedPackage(String packageName) {
        state();
        return hasPackage(String.valueOf(packageName()) + "." + packageName);
    }

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupType(String name) {
        state();
        SimpleSet c = SimpleSet.emptySet;
        TypeDecl typeDecl = lookupType(packageName(), name);
        if (nextAccess() instanceof ClassInstanceExpr) {
            if (typeDecl != null && typeDecl.accessibleFrom(hostType())) {
                c = c.add(typeDecl);
            }
            return c;
        }
        if (typeDecl != null) {
            if (hostType() != null && typeDecl.accessibleFrom(hostType())) {
                c = c.add(typeDecl);
            } else if (hostType() == null && typeDecl.accessibleFromPackage(hostPackage())) {
                c = c.add(typeDecl);
            }
        }
        return c;
    }

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupVariable(String name) {
        state();
        return SimpleSet.emptySet;
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getPackage() + "]";
    }

    public String name() {
        state();
        return getPackage();
    }

    @Override // soot.JastAddJ.Expr
    public String packageName() {
        state();
        StringBuffer s = new StringBuffer();
        if (hasPrevExpr()) {
            s.append(prevExpr().packageName());
            s.append(".");
        }
        s.append(getPackage());
        return s.toString();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isPackageAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.PACKAGE_NAME;
    }

    @Override // soot.JastAddJ.Expr
    public boolean isUnknown() {
        state();
        return !hasPackage(packageName());
    }

    @Override // soot.JastAddJ.Expr
    public boolean hasPackage(String packageName) {
        state();
        boolean hasPackage_String_value = getParent().Define_boolean_hasPackage(this, null, packageName);
        return hasPackage_String_value;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
