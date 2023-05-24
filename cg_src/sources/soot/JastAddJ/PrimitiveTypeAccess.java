package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PrimitiveTypeAccess.class */
public class PrimitiveTypeAccess extends TypeAccess implements Cloneable {
    protected String tokenString_Name;
    public int Namestart;
    public int Nameend;
    protected String tokenString_Package;
    protected String tokenString_ID;
    protected SimpleSet decls_value;
    protected String getPackage_value;
    protected String getID_value;
    protected boolean decls_computed = false;
    protected boolean getPackage_computed = false;
    protected boolean getID_computed = false;

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decls_computed = false;
        this.decls_value = null;
        this.getPackage_computed = false;
        this.getPackage_value = null;
        this.getID_computed = false;
        this.getID_value = null;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public PrimitiveTypeAccess clone() throws CloneNotSupportedException {
        PrimitiveTypeAccess node = (PrimitiveTypeAccess) super.clone();
        node.decls_computed = false;
        node.decls_value = null;
        node.getPackage_computed = false;
        node.getPackage_value = null;
        node.getID_computed = false;
        node.getID_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            PrimitiveTypeAccess node = clone();
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

    public PrimitiveTypeAccess() {
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public PrimitiveTypeAccess(String p0) {
        setName(p0);
    }

    public PrimitiveTypeAccess(Symbol p0) {
        setName(p0);
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setName(String value) {
        this.tokenString_Name = value;
    }

    public void setName(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setName is only valid for String lexemes");
        }
        this.tokenString_Name = (String) symbol.value;
        this.Namestart = symbol.getStart();
        this.Nameend = symbol.getEnd();
    }

    public String getName() {
        return this.tokenString_Name != null ? this.tokenString_Name : "";
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public SimpleSet decls() {
        if (this.decls_computed) {
            return this.decls_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decls_value = decls_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decls_computed = true;
        }
        return this.decls_value;
    }

    private SimpleSet decls_compute() {
        return lookupType("@primitive", name());
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getPackage() {
        if (this.getPackage_computed) {
            return this.getPackage_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getPackage_value = getPackage_compute();
        setPackage(this.getPackage_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getPackage_computed = true;
        }
        return this.getPackage_value;
    }

    private String getPackage_compute() {
        return "@primitive";
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getID() {
        if (this.getID_computed) {
            return this.getID_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getID_value = getID_compute();
        setID(this.getID_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getID_computed = true;
        }
        return this.getID_value;
    }

    private String getID_compute() {
        return getName();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getName() + "]";
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
