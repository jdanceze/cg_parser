package soot.JastAddJ;

import beaver.Symbol;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BytecodeTypeAccess.class */
public class BytecodeTypeAccess extends TypeAccess implements Cloneable {
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public BytecodeTypeAccess clone() throws CloneNotSupportedException {
        BytecodeTypeAccess node = (BytecodeTypeAccess) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            BytecodeTypeAccess node = clone();
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
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
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

    public BytecodeTypeAccess() {
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public BytecodeTypeAccess(String p0, String p1) {
        setPackage(p0);
        setID(p1);
    }

    public BytecodeTypeAccess(Symbol p0, Symbol p1) {
        setPackage(p0);
        setID(p1);
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
        }
        this.tokenString_Package = (String) symbol.value;
        this.Packagestart = symbol.getStart();
        this.Packageend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getPackage() {
        return this.tokenString_Package != null ? this.tokenString_Package : "";
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        state().duringBoundNames++;
        ASTNode result = rewriteRule0();
        state().duringBoundNames--;
        return result;
    }

    private Access rewriteRule0() {
        SimpleSet set;
        if (name().indexOf("$") == -1) {
            return new TypeAccess(packageName(), name());
        }
        String[] names = name().split("\\$");
        Access a = null;
        String newName = null;
        TypeDecl type = null;
        for (int i = 0; i < names.length; i++) {
            newName = newName == null ? names[i] : String.valueOf(newName) + "$" + names[i];
            if (type != null) {
                set = type.memberTypes(newName);
            } else if (packageName().equals("")) {
                set = lookupType(newName);
            } else {
                TypeDecl typeDecl = lookupType(packageName(), newName);
                set = SimpleSet.emptySet;
                if (typeDecl != null) {
                    set = set.add(typeDecl);
                }
            }
            if (!set.isEmpty()) {
                a = a == null ? new TypeAccess(packageName(), newName) : a.qualifiesAccess(new TypeAccess(newName));
                type = (TypeDecl) set.iterator().next();
                newName = null;
            }
        }
        if (a == null) {
            a = new TypeAccess(packageName(), name());
        }
        return a;
    }
}
